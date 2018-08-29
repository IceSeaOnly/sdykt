package site.binghai.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.biz.service.TokenService;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.entity.WxUser;
import site.binghai.lib.service.WxUserService;

import java.util.Map;

@RestController
@RequestMapping("/")
public class UserController extends BaseController {
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("login")
    public Object login(@RequestBody Map map) {
        String phone = getString(map, "PHONE");
        String passwd = getString(map, "PASS_WD");

        if (!noEmptyString(phone, passwd)) {
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
        WxUser user = wxUserService.newInstance(map);
        if (!noEmptyString(user.getPhone(), user.getPassword(), user.getUserName())) {
            return fail("输入不完整!");
        }

        if(wxUserService.findByPhone(user.getPhone()) != null){
            return fail("手机号已被注册!");
        }

        user.setBindTokenId(null);
        user.setOpenId(null);
        user.setRefereeId(null);

        wxUserService.save(user);

        return success();
    }
}
