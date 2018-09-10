package site.binghai.lib.def;

public interface SmsService {
    String sendVerifyCode(String to,String code);
}
