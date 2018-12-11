package site.binghai.biz.service;

import org.springframework.stereotype.Service;
import site.binghai.biz.entity.ExaminationSchoolRecord;
import site.binghai.lib.service.BaseService;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ExaminationSchoolRecordService extends BaseService<ExaminationSchoolRecord> {

    public List<ExaminationSchoolRecord> findByBatchNameAndBatchTypeAndYear(String batchName, String batchType,
                                                                            String year) {
        ExaminationSchoolRecord record = new ExaminationSchoolRecord();
        record.setBatchName(batchName);
        record.setBatchType(batchType);
        record.setYear(Integer.valueOf(year));
        return query(record);
    }

    //热度+1
    @Transactional
    public void hot(Long schoolId) {
        ExaminationSchoolRecord record = findById(schoolId);
        if (record == null) {
            return;
        }

        if (record.getHot() == null) {
            record.setHot(1);
        } else {
            record.setHot(record.getHot() + 1);
        }
        update(record);
    }
}
