import org.junit.Test;
import site.binghai.biz.utils.TokenGenerator;

import java.util.HashMap;
import java.util.Map;

public class TokenTest {

    @Test
    public void tokenTest() {
        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            String tk = TokenGenerator.generate(4).toUpperCase();
            System.out.println(tk);
            map.put(tk, i);
        }

        System.out.println(map.size());
    }
}
