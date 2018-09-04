package site.binghai.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.entity.WxUser;
import site.binghai.lib.service.WxUserService;

import java.util.Map;

@RestController
@RequestMapping("/user/info/")
public class UserInfoController extends BaseController {
    @Autowired
    private WxUserService wxUserService;

    @PostMapping("update")
    public Object update(@RequestBody Map map) {
        try {
            WxUser user = wxUserService.updateAndSave(getSessionPersistent(WxUser.class), map);
            persistent(user);
        } catch (Exception e) {
            return fail(e.getMessage());
        }
        return success();
    }

    @GetMapping("my")
    public Object my() {
        return success(getSessionPersistent(WxUser.class), null);
    }
}
