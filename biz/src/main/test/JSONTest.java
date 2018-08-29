import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.junit.Test;

public class JSONTest {

    @Test
    public void parseTest() {
        JSONObject obj = new JSONObject();
        obj.put("dv","12.5");
        obj.put("iv","123");
        obj.put("sv","testString");

        ExampleClass entity = obj.toJavaObject(ExampleClass.class);
        System.out.println(entity.getDv());
        System.out.println(entity.getIv());
        System.out.println(entity.getSv());
    }


}
