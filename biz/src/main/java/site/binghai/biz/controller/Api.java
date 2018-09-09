package site.binghai.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.binghai.biz.service.SmsTokenService;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.entity.WxUser;

@RestController
@RequestMapping("/user/api/")
public class Api extends BaseController {
    @Autowired
    private SmsTokenService smsTokenService;

    @GetMapping("applySmsValid")
    public Object applySmsValid(@RequestParam String phone) {
        WxUser user = getSessionPersistent(WxUser.class);
        if(!hasEmptyString(user.getPhone())){
            return fail("您已经绑定手机号，无需重复绑定");
        }

        if (!checkLastSendTime()) {
            return fail("请求过于频繁!");
        }

        try {
            smsTokenService.sendVerifyCode(phone);
        } catch (Exception e) {
            logger.error("send verify code error!,phone:{},", phone, e);
            return fail(e.getMessage());
        }

        setString2Session("VERIFY_CODE_SEND_REQUEST", String.valueOf(now()));
        return success();
    }


    private boolean checkLastSendTime() {
        String lastSend = getStringFromSession("VERIFY_CODE_SEND_REQUEST");
        if (lastSend == null) return true;
        if (now() - Long.valueOf(lastSend) > 6000 * 10) {
            return true;
        }
        return false;
    }
}
