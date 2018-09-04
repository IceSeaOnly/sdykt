package site.binghai.biz.controller.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.binghai.biz.service.ManagerService;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.entity.Manager;

import java.util.Map;

@RestController
@RequestMapping("m")
public class ManagerController extends BaseController {
    @Autowired
    private ManagerService managerService;

    @PostMapping("login")
    public Object login(@RequestBody Map map) {
        String name = getString(map, "name");
        String passwd = getString(map, "passwd");

        Manager manager = managerService.login(name, passwd);
        if(manager == null){
            return fail("账号或密码不正确!");
        }
        persistent(manager);
        return success();
    }
}
