import org.junit.Test;
import site.binghai.lib.utils.GroovyEngineUtils;

import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

public class GroovyTest {

    @Test
    public void groovy() throws Exception {
        Map data = new HashMap();
        data.put("a",7);
        data.put("b",2);

        String script = "if(context.batchName == '本科' || context.batchName == '专科'){result.sw = context.score}result.sw = context.score - 3;";

        GroovyEngineUtils.GroovyEngine engine = GroovyEngineUtils.instanceGroovyEngine(script);

        System.out.println(engine.invoke(data));
        data.put("b",4);
        System.out.println(engine.invoke(data));
    }
}
