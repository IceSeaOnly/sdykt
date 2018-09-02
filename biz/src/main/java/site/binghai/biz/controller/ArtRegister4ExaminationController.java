package site.binghai.biz.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.binghai.biz.enums.PrivilegeEnum;
import site.binghai.biz.service.ExaminationSchoolRecordService;

@RestController
@RequestMapping("/user/artRegister4Examination/")
public class ArtRegister4ExaminationController extends PrivilegeBasedController {

    @Autowired
    private ExaminationSchoolRecordService examinationSchoolRecordService;

    @GetMapping("selectItemList")
    public Object selectItemList(@RequestParam String query) {
        if (!hasPrivilege()) {
            return permissionDeny();
        }

        String[] fields = query.split(",");
        JSONObject data = newJSONObject();

        for (String field : fields) {
            data.put(field, examinationSchoolRecordService.distinctList(field));
        }

        return data;
    }

    @Override
    PrivilegeEnum privilegeRequired() {
        return PrivilegeEnum.ArtRegister4Examination;
    }
}
