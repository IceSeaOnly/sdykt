package site.binghai.biz.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.InitializingBean;
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
public class AnalysisLogService extends BaseService<AnalysisLog> implements InitializingBean {

    private static AnalysisLogService holder;

    @Transactional
    public void log(AnalysisTag tag, Object obj) {
        AnalysisLog log = new AnalysisLog();
        log.setTag(tag.name());
        log.setContent(JSON.toJSONString(obj));
        save(log);
    }

    public Builder log(AnalysisTag tag) {
        return new Builder(tag);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        holder = this;
    }

    public class Builder {
        private AnalysisTag tag;
        private JSONObject obj;

        public Builder(AnalysisTag tag) {
            this.tag = tag;
            this.obj = new JSONObject();
        }

        public Builder and(String key, Object value) {
            obj.put(key, value);
            return this;
        }

        public void done() {
            holder.log(tag, obj);
        }
    }
}
