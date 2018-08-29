package site.binghai.biz.controller.manage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.binghai.biz.entity.ExaminationSchoolRecord;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.utils.ExcelUtils;

import java.util.List;

@RestController
@RequestMapping("/manage/school/")
public class SchoolManageController extends BaseController {

    @PostMapping("uploadFile")
    public Object uploadFile(@RequestParam("file") MultipartFile file) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                List<ExaminationSchoolRecord> schoolRecordList = ExcelUtils.readExcelData(file, ExaminationSchoolRecord.class);
                return success(schoolRecordList, null);
            } catch (Exception e) {
                logger.error("readExcelData fail!", e);
                return fail(e.getMessage());
            }
        }
        return success();
    }
}
