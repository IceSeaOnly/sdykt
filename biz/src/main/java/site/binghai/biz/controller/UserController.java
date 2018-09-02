package site.binghai.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.biz.service.SmsTokenService;
import site.binghai.biz.service.TokenService;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.entity.WxUser;
import site.binghai.lib.service.WxUserService;

import java.util.Map;

@RestController
@RequestMapping("/s/")
public class UserController extends BaseController {
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SmsTokenService smsTokenService;

    @RequestMapping("logout")
    public Object logout(){
        getSession().invalidate();
        return success();
    }

    @PostMapping("login")
    public Object login(@RequestBody Map map) {
        String phone = getString(map, "PHONE");
        String passwd = getString(map, "PASS_WD");

        if (hasEmptyString(phone, passwd)) {
            fail("输入不正确!");
        }

        WxUser user = wxUserService.login(phone, passwd);
        if (user == null) {
            return fail("卡号/密码错误!");
        }

        if (user.getBindTokenId() != null) {
            persistent(tokenService.findById(user.getBindTokenId()));
        }

        persistent(user);
        return success();
    }

    @PostMapping("reg")
    public Object reg(@RequestBody Map map) {
        if (!checkRequest()) {
            return fail("请求过于频繁!1分钟后再试!");
        }

        String phoneVerifyCode = getString(map, "PHONE_CODE");
        String userName = getString(map, "USER_NAME");
        String phone = getString(map, "PHONE");
        String pass = getString(map, "PASS_WD");

        if (hasEmptyString(phone, userName, phoneVerifyCode)) {
            return fail("输入不完整!");
        }

        if (wxUserService.findByPhone(phone) != null) {
            return fail("手机号已被注册!");
        }

        if (!smsTokenService.varify(phone, phoneVerifyCode)) {
            return fail("验证码错误!");
        }

        WxUser user = new WxUser();
        user.setBindTokenId(null);
        user.setOpenId(null);
        user.setRefereeId(null);
        user.setUserName(userName);
        user.setPhone(phone);
        user.setPassword(pass);
        user.setAvatar("http://cdn.binghai.site/o_1cmc3drecfph1nt48dv194k1p6ta.jpg");

        wxUserService.save(user);

        setString2Session("REG_REQUEST", now() + "");
        return success();
    }

    private boolean checkRequest() {
        String req = getStringFromSession("REG_REQUEST");
        if (req == null) return true;
        if (now() - Long.valueOf(req) < 6000) return false;
        return true;
    }
}
