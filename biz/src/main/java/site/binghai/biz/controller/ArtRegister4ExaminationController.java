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
import site.binghai.biz.entity.Token;
import site.binghai.biz.enums.PrivilegeEnum;
import site.binghai.biz.service.ExaminationSchoolRecordService;
import site.binghai.biz.service.TokenService;
import site.binghai.lib.utils.GroovyEngineUtils;

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
    @Autowired
    private TokenService tokenService;

    /**
     * 用户前台选择后，后台同步更新数据
     */
    @GetMapping("mySelect")
    public Object mySelect(@RequestParam Long schoolId, @RequestParam Integer position) {
        SelectId selectIds = getSessionPersistent(SelectId.class);
        if (selectIds == null) {
            selectIds = new SelectId();
        }

        List<Long> newIds = emptyList();
        for (int i = 0; i < Math.max(selectIds.getIds().size(), position); i++) {
            if (i == position - 1) {
                newIds.add(schoolId);
            } else {
                if (i < selectIds.getIds().size()) {
                    //如果之前已经选过这个学校，则清除之前的选择
                    Long cur = selectIds.getIds().get(i);
                    newIds.add(cur.equals(schoolId) ? -1L : cur);
                } else {
                    newIds.add(-1L);
                }
            }
        }
        selectIds.setIds(newIds);
        persistent(selectIds);
        examinationSchoolRecordService.hot(schoolId);
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
            selectIds = new SelectId();
        }

        List<ExaminationSchoolRecord> ls = emptyList();

        if (isEmptyList(selectIds.getIds())) {
            return success(ls, null);
        }

        if (action == 1) {
            for (int i = 0; i < selectIds.getIds().size(); i++) {
                long id = selectIds.getIds().get(i);
                ExaminationSchoolRecord record;
                if (id == -1L) {
                    record = new ExaminationSchoolRecord();
                    record.setSchoolName("未选择志愿");
                } else {
                    record = examinationSchoolRecordService.findById(id);
                }
                record.setExtra(i + 1);
                ls.add(record);
            }
        } else {
            selectIds.getIds().forEach(id -> {
                ExaminationSchoolRecord record;
                if (id.equals(-1L)) {
                    record = new ExaminationSchoolRecord();
                    record.setId(-1L);
                } else {
                    record = examinationSchoolRecordService.findById(id);
                }
                ls.add(record);
            });
            while (ls.size() < 12) {
                ExaminationSchoolRecord record = new ExaminationSchoolRecord();
                record.setId(-1L);
                ls.add(record);
            }
        }

        return success(ls, null);
    }

    @PostMapping("consultSwScore")
    public Object consultSwScore(@RequestBody Map map) throws Exception {
        long ts = System.currentTimeMillis();
        if (!hasPrivilege()) {
            return permissionDeny();
        }
        Map result = calculateSwScore(map);
        Double score = getDouble(result, "sw");
        return success(score, String.valueOf(ts));
    }

    @PostMapping("consult")
    public Object consult(@RequestBody Map map) {
        if (!hasPrivilege()) {
            return permissionDeny();
        }

        Token token = getSessionPersistent(Token.class);
        token = tokenService.findById(token.getId());
        if (token.getZybkCount() <= 0) {
            return fail("您的志愿模拟次数已经用完，无法使用本功能！");
        }

        String batchName = getString(map, "batchName");
        String batchType = getString(map, "batchType");
        String score = getString(map, "score");
        String year = getString(map, "year");
        String city = getString(map, "city");
        Integer sw_score = getInteger(map, "sw_score");
        Integer art_user_input = getInteger(map, "art_user_input");
        // 以三维标准分计算，如果用户自行输入了三维标准分，就按用户输入的查找
        if (art_user_input != null) {
            sw_score = art_user_input;
        }

        if (hasEmptyString(batchName, batchType, score, year)) {
            return fail("年份、批次、科类、分数都是必填的哦~");
        }

        List<ExaminationSchoolRecord> recordList =
            examinationSchoolRecordService.findByBatchNameAndBatchTypeAndYear(batchName, batchType, year);

        final Integer facotr = sw_score;
        List rs = recordList.stream()
            .filter(v -> !hasEmptyString(v.getMinScore()))
            .filter(v -> facotr > v.getMinScore())
            .sorted((a, b) -> b.getMinScore() > a.getMinScore() ? 1 : -1)
            .limit(16)
            .collect(Collectors.toList());

        JSONObject ret = newJSONObject();

        ret.put("list", rs);
        ret.put("total", rs.size());

        String msg = null;
        if (isEmptyList(rs)) {
            return success(ret, "没有符合条件的院校哦，建议调整一下搜索条件!");
        }
        msg = String.format("已为您优选 %d 个院校%s", rs.size(), art_user_input == null ? ",由于您没有输入三维标准分本次搜索以综合分查找" : "");

        token.setZybkCount(token.getZybkCount() - 1);
        tokenService.update(token);
        if (token.getZybkCount() <= 5) {
            msg += "；您的模拟选择次数还剩" + token.getZybkCount() + "次";
        }
        return success(ret, msg);
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
