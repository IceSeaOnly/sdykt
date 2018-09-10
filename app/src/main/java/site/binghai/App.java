package site.binghai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import site.binghai.lib.config.IceConfig;
import site.binghai.lib.def.SmsService;

@ComponentScan(value = {"site.binghai.lib", "site.binghai.biz"})
@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private SmsService miaoDiSmsTestService;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        for (String arg : args) {
            if (arg.startsWith("-") && arg.contains("=")) {
                arg = arg.substring(1);
                String[] kv = arg.split("=");
                IceConfig.addSetupParam(kv[0], kv[1]);
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
//        miaoDiSmsTestService.sendVerifyCode("17854251817","您已经省略，以下为正文:不过没关系，再来一次");
    }
}