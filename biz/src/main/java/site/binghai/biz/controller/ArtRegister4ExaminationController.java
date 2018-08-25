package site.binghai.biz.controller;

import org.springframework.web.bind.annotation.RestController;
import site.binghai.biz.enums.PrivilegeEnum;

@RestController
public class ArtRegister4ExaminationController extends PrivilegeBasedController {




    @Override
    PrivilegeEnum privilegeRequired() {
        return PrivilegeEnum.ArtRegister4Examination;
    }
}
