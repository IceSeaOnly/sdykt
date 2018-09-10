package site.binghai.biz.caches;

import org.springframework.stereotype.Component;
import site.binghai.biz.service.AbastractCacheService;
import site.binghai.lib.utils.HttpUtils;

@Component
public class CalculateSwScoreScriptCache extends AbastractCacheService<String> {
    @Override
    public String load() {
        return HttpUtils.sendGet("https://wx.nanayun.cn/api", "act=20a47cba663cc7e");
    }

    @Override
    public long setExpiredSecs() {
        return 60;
    }
}
