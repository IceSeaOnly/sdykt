package site.binghai.biz.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.biz.entity.ExaminationSchoolRecord;
import site.binghai.biz.entity.SelectId;
import site.binghai.biz.enums.PrivilegeEnum;
import site.binghai.biz.service.ExaminationSchoolRecordService;
import site.binghai.lib.utils.HttpUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/artRegister4Examination/")
public class ArtRegister4ExaminationController extends PrivilegeBasedController {
    private Object cityCache;
    private JSONObject swScoreConsultCache;
    private Integer tolerance;

    @Autowired
    private ExaminationSchoolRecordService examinationSchoolRecordService;

    @GetMapping("mySelect")
    public Object mySelect(@RequestParam Long schoolId) {
        SelectId selectIds = getSessionPersistent(SelectId.class);
        if (selectIds == null) {
            return fail("会话超时!请重新登录!");
        }

        if (selectIds.getIds().contains(schoolId)) {
            selectIds.getIds().remove(schoolId);
        } else {
            selectIds.getIds().add(schoolId);
        }

        persistent(selectIds);
        return success();
    }

    @GetMapping("mySelectedList")
    public Object mySelectedList() {
        SelectId selectIds = getSessionPersistent(SelectId.class);
        if (selectIds == null) {
            return fail("会话超时!请重新登录!");
        }

        List<ExaminationSchoolRecord> ls = examinationSchoolRecordService.findByIds(selectIds.getIds());
        ls.sort((a, b) -> b.getMinScore() > a.getMinScore() ? 1 : -1);
        return success(ls, null);
    }

    @GetMapping("consultSwScore")
    public Object consultSwScore(@RequestParam Long score, @RequestParam String year) {
        if (!hasPrivilege()) {
            return permissionDeny();
        }

        if (score == null || score <= 0 || hasEmptyString(year)) {
            return fail("无法三维标准分");
        }
        JSONObject map = getYearConsultCache(year);
        if (map == null) return fail("无法三维标准分");

        score += map.getLong("sw");
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
        String sw_score = getString(map, "sw_score");

        if (hasEmptyString(batchName, batchType, score, year)) {
            return fail("年份、批次、科类、分数都是必填的哦~");
        }

        List<ExaminationSchoolRecord> recordList =
                examinationSchoolRecordService.findByBatchNameAndBatchTypeAndYear(batchName, batchType, year);

        List rs = recordList.stream()
                .filter(v -> inRound(score, v))
                .sorted((a, b) -> b.getMinScore() > a.getMinScore() ? 1 : -1)
                .limit(100)
                .collect(Collectors.toList());

        JSONObject ret = newJSONObject();

        ret.put("list", rs);
        ret.put("total", rs.size());
        return success(ret, null);
    }

    private boolean inRound(String score, ExaminationSchoolRecord v) {
        if (hasEmptyString(v.getMinScore())) return false;
        Integer value = Integer.valueOf(score);
        JSONObject data = getYearConsultCache(v.getYear() + "");
        if (value > v.getMinScore() - data.getInteger("tl")) return true;
        return false;
    }

    private JSONObject getYearConsultCache(String year) {
        if (swScoreConsultCache == null) {
            swScoreConsultCache = HttpUtils.sendJSONGet("https://wx.nanayun.cn/api", "act=9f0ee8e30fe88ee");
        }
        return swScoreConsultCache.getJSONObject(year);
    }


    @GetMapping("options")
    public Object options() {
        if (!hasPrivilege()) {
            return permissionDeny();
        }

        JSONObject data = newJSONObject();
        data.put("yearList", examinationSchoolRecordService.distinctList("year"));
        data.put("cityList", getCityList());
        data.put("batchNameList", examinationSchoolRecordService.distinctList("batchName"));
        data.put("batchTypeList", examinationSchoolRecordService.distinctList("batchType"));
        return success(data, null);
    }

    private Object getCityList() {
        if (cityCache == null) {
            String resp = HttpUtils.sendGet("https://wx.nanayun.cn/api", "act=e53bad5110a7d56");
            cityCache = JSONArray.parse(resp);
        }
        return cityCache;
    }

    @Override
    PrivilegeEnum privilegeRequired() {
        return PrivilegeEnum.ArtRegister4Examination;
    }
}
