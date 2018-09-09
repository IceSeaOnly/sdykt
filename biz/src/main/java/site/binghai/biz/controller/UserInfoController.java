package site.binghai.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.biz.service.SmsTokenService;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.entity.WxUser;
import site.binghai.lib.service.WxUserService;

import java.util.Map;

@RestController
@RequestMapping("/user/info/")
public class UserInfoController extends BaseController {
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private SmsTokenService smsTokenService;

    @PostMapping("update")
    public Object update(@RequestBody Map map) {
        try {
            map.remove("phone");
            WxUser user = wxUserService.updateAndSave(getSessionPersistent(WxUser.class), map);
            persistent(user);
        } catch (Exception e) {
            return fail(e.getMessage());
        }
        return success();
    }

    @PostMapping("updatePhone")
    public Object updatePhone(@RequestBody Map map) {
        String phone = getString(map, "phone");
        String smsCode = getString(map, "smsCode");

        if (hasEmptyString(phone, smsCode) || phone.length() != 11) {
            return fail("验证失败!请检查验证码是否正确或手机号是否为11位!");
        }

        if (smsTokenService.varify(phone, smsCode)) {
            WxUser user = getSessionPersistent(WxUser.class);
            user = wxUserService.findById(user.getId());
            user.setPhone(phone);
            wxUserService.update(user);
            persistent(user);
            return success();
        }

        return fail("验证码不正确!");
    }

    @GetMapping("my")
    public Object my() {
        return success(getSessionPersistent(WxUser.class), null);
    }

    @GetMapping("checkInfoComplete")
    public Object checkInfoComplete() {
        WxUser user = getSessionPersistent(WxUser.class);
        if (hasEmptyString(user.getPhone(), user.getUserName(), user.getRegion())) {
            return fail("");
        }
        return success();
    }
}
