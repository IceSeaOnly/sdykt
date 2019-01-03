package site.binghai.biz.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import site.binghai.biz.entity.AnalysisLog;
import site.binghai.biz.enums.AnalysisTag;
import site.binghai.lib.service.BaseService;

import javax.transaction.Transactional;

/**
 * @author huaishuo
 * @date 2019/1/3 下午3:00
 **/
@Service
public class AnalysisLogService extends BaseService<AnalysisLog> {

    @Transactional
    public void log(AnalysisTag tag, Object obj) {
        AnalysisLog log = new AnalysisLog();
        log.setTag(tag.name());
        log.setContent(JSON.toJSONString(obj));
        save(log);
    }

    @Transactional
    public Builder log(AnalysisTag tag) {
        return new Builder(tag, this);
    }

    public class Builder {
        private AnalysisTag tag;
        private JSONObject obj;
        private AnalysisLogService logService;

        public Builder(AnalysisTag tag, AnalysisLogService analysisLogService) {
            this.tag = tag;
            this.obj = new JSONObject();
            this.logService = analysisLogService;
        }

        public Builder and(String key, Object value) {
            obj.put(key, value);
            return this;
        }

        public void done() {
            try {
                logService.log(tag, obj);
            } catch (Exception e) {
                logger.error("save AnalysisLog error!",e);
            }
        }
    }
}
