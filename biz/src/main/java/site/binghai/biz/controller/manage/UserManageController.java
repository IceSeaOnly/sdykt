package site.binghai.biz.controller.manage;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.entity.WxUser;
import site.binghai.lib.service.WxUserService;

import java.util.List;

@RestController
@RequestMapping("/manage/user/")
public class UserManageController extends BaseController {

    @Autowired
    private WxUserService wxUserService;

    @GetMapping("list")
    public Object list(@RequestParam Integer page) {
        List<WxUser> userList = wxUserService.findAll(page, 100);
        JSONObject data = newJSONObject();
        data.put("list", userList);
        data.put("total", wxUserService.count());
        data.put("page", page);
        data.put("pageSize", 100);
        return success(data, null);
    }
}
