package site.binghai.biz.caches;

import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Component;
import site.binghai.biz.service.AbastractCacheService;
import site.binghai.lib.utils.HttpUtils;

@Component
public class CityListCache extends AbastractCacheService<Object> {

    @Override
    public Object load() {
        String resp = HttpUtils.sendGet("https://wx.nanayun.cn/api", "act=e53bad5110a7d56");
        return JSONArray.parse(resp);
    }

    @Override
    public long setExpiredSecs() {
        return 60;
    }
}
