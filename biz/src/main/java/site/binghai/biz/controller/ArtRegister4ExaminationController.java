package site.binghai.biz.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.biz.caches.CalculateSwScoreScriptCache;
import site.binghai.biz.caches.CityListCache;
import site.binghai.biz.caches.SwGroovyEngineCache;
import site.binghai.biz.entity.ExaminationSchoolRecord;
import site.binghai.biz.entity.SelectId;
import site.binghai.biz.enums.PrivilegeEnum;
import site.binghai.biz.service.ExaminationSchoolRecordService;
import site.binghai.lib.utils.GroovyEngineUtils;
import site.binghai.lib.utils.HttpUtils;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/artRegister4Examination/")
public class ArtRegister4ExaminationController extends PrivilegeBasedController {
    private GroovyEngineUtils.GroovyEngine swEngine;

    @Autowired
    private ExaminationSchoolRecordService examinationSchoolRecordService;
    @Autowired
    private CityListCache cityListCache;
    @Autowired
    private SwGroovyEngineCache swGroovyEngineCache;

    @GetMapping("mySelect")
    public Object mySelect(@RequestParam Long schoolId, @RequestParam Integer position) {
        SelectId selectIds = getSessionPersistent(SelectId.class);
        if (selectIds == null) {
            return fail("会话超时!请重新登录!");
        }

        List<Long> newIds = emptyList();
        for (int i = 0; i < Math.max(selectIds.getIds().size(), position); i++) {
            if (i == position - 1) {
                newIds.add(schoolId);
            } else {
                if (i < selectIds.getIds().size()) {
                    newIds.add(selectIds.getIds().get(i));
                } else {
                    newIds.add(-1l);
                }
            }
        }
        selectIds.setIds(newIds);
        persistent(selectIds);
        logger.info("selected id changed :{}", selectIds.getIds());
        return success();
    }

    @GetMapping("resetMySelect")
    public Object resetMySelect() {
        persistent(new SelectId());
        return success();
    }

    @GetMapping("mySelectedList")
    public Object mySelectedList(@RequestParam Integer action) {
        SelectId selectIds = getSessionPersistent(SelectId.class);
        if (selectIds == null) {
            return fail("会话超时!请重新登录!");
        }

        List<ExaminationSchoolRecord> ls = emptyList();

        if (isEmptyList(selectIds.getIds())) {
            return success(ls, null);
        }

        if (action == 1) {
            for (int i = 0; i < selectIds.getIds().size(); i++) {
                long id = selectIds.getIds().get(i);
                ExaminationSchoolRecord record;
                if (id == -1l) {
                    record = new ExaminationSchoolRecord();
                    record.setSchoolName("未选择志愿");
                } else {
                    record = examinationSchoolRecordService.findById(id);
                }
                record.setExtra(i + 1);
                ls.add(record);
            }
        } else {
            ls.addAll(examinationSchoolRecordService.findByIds(selectIds.getIds()));
        }

        return success(ls, null);
    }

    @PostMapping("consultSwScore")
    public Object consultSwScore(@RequestBody Map map) throws Exception {
        if (!hasPrivilege()) {
            return permissionDeny();
        }
        Map result = calculateSwScore(map);
        Integer score = getInteger(result, "sw");
        return success(score, null);
    }

    @PostMapping("consult")
    public Object consult(@RequestBody Map map) {
        persistent(new SelectId());
        String batchName = getString(map, "batchName");
        String batchType = getString(map, "batchType");
        String score = getString(map, "score");
        String year = getString(map, "year");
        String city = getString(map, "city");
        Integer sw_score = getInteger(map, "sw_score");

        if (hasEmptyString(batchName, batchType, score, year)) {
            return fail("年份、批次、科类、分数都是必填的哦~");
        }

        List<ExaminationSchoolRecord> recordList =
                examinationSchoolRecordService.findByBatchNameAndBatchTypeAndYear(batchName, batchType, year);

        List rs = recordList.stream()
                .filter(v -> !hasEmptyString(v.getMinScore()))
                .filter(v -> sw_score > v.getMinScore())
                .sorted((a, b) -> b.getMinScore() > a.getMinScore() ? 1 : -1)
                .limit(12)
                .collect(Collectors.toList());

        JSONObject ret = newJSONObject();

        ret.put("list", rs);
        ret.put("total", rs.size());
        return success(ret, null);
    }


    private Map calculateSwScore(Map context) throws Exception {
        return swGroovyEngineCache.get().invoke(context);
    }


    @GetMapping("options")
    public Object options(@RequestParam Integer type) {
        if (!hasPrivilege()) {
            return permissionDeny();
        }

        JSONObject data = newJSONObject();
        data.put("yearList", examinationSchoolRecordService.distinctList("year"));
        data.put("cityList", cityListCache.get());

        if (type == 0) {
            List<String> batchNameList = emptyList();
            batchNameList.add("本科");
            batchNameList.add("专科");
            data.put("batchNameList", batchNameList);

            List<String> batchTypeList = emptyList();
            batchTypeList.add("文科");
            batchTypeList.add("理科");
            data.put("batchTypeList", batchTypeList);
        } else {
            List<String> batchNameList = emptyList();
            batchNameList.add("美术统考提前批");
            batchNameList.add("美术统考一批");
            batchNameList.add("文学编导提前批");
            batchNameList.add("文学编导一批");
            data.put("batchNameList", batchNameList);

            List<String> batchTypeList = emptyList();
            batchTypeList.add("艺术文");
            batchTypeList.add("艺术理");
            data.put("batchTypeList", batchTypeList);
        }
        return success(data, null);
    }

    @Override
    PrivilegeEnum privilegeRequired() {
        return PrivilegeEnum.ArtRegister4Examination;
    }
}
