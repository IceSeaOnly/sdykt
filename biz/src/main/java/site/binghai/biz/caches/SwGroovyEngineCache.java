package site.binghai.biz.caches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.binghai.biz.service.AbastractCacheService;
import site.binghai.lib.utils.GroovyEngineUtils;

import javax.script.ScriptException;

@Component
public class SwGroovyEngineCache extends AbastractCacheService<GroovyEngineUtils.GroovyEngine> {
    @Autowired
    private CalculateSwScoreScriptCache calculateSwScoreScriptCache;

    @Override
    public GroovyEngineUtils.GroovyEngine load() {
        String script = calculateSwScoreScriptCache.get();
        try {
            return GroovyEngineUtils.instanceGroovyEngine(script);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long setExpiredSecs() {
        return 60;
    }
}
