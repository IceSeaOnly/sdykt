package site.binghai.biz.controller.manage;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.binghai.biz.entity.ExaminationSchoolRecord;
import site.binghai.biz.service.ExaminationSchoolRecordService;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.utils.ExcelUtils;

import java.util.List;

@RestController
@RequestMapping("/manage/school/")
public class SchoolManageController extends BaseController {

    @Autowired
    private ExaminationSchoolRecordService examinationSchoolRecordService;

    @PostMapping("uploadFile")
    public Object uploadFile(@RequestParam("file") MultipartFile file) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                List<ExaminationSchoolRecord> schoolRecordList = ExcelUtils.readExcelData(file, ExaminationSchoolRecord.class);
                if (!CollectionUtils.isEmpty(schoolRecordList)) {
                    schoolRecordList = examinationSchoolRecordService.batchSave(schoolRecordList);
                }
                return success(schoolRecordList, null);
            } catch (Exception e) {
                logger.error("readExcelData fail!", e);
                return fail(e.getMessage());
            }
        }
        return success();
    }

    @GetMapping("list")
    public Object list(Integer page, Integer pageSize) {
        if (page == null || page <= 0) page = 0;
        if (pageSize == null || pageSize <= 0) pageSize = 100;

        JSONObject data = newJSONObject();
        List<ExaminationSchoolRecord> records = examinationSchoolRecordService.findAll(page, pageSize);
        data.put("list",records);
        data.put("total",examinationSchoolRecordService.count());
        
        return success(data, null);
    }

    @GetMapping("delete")
    public Object delete(@RequestParam Long id) {
        examinationSchoolRecordService.delete(id);
        return success();
    }
}
