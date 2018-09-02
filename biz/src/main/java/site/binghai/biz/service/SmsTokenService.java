package site.binghai.biz.service;

import org.springframework.stereotype.Service;
import site.binghai.biz.entity.SmsToken;
import site.binghai.lib.service.BaseService;
import site.binghai.lib.utils.TimeTools;

import java.util.List;

@Service
public class SmsTokenService extends BaseService<SmsToken> {

    public boolean varify(String phone, String input) {
        SmsToken smsToken = findLastestValidByPhone(phone);
        if (!isValid(smsToken)) return false;
        if (smsToken.getCreated().equals(input)) {
            smsToken.setValid(false);
            update(smsToken);
            return true;
        }
        return false;
    }

    public void sendVerifyCode(String phone) throws Exception {
        String code = TimeTools.now().substring(7);
        if (checkPhoneNumber(phone) && clearHistory(phone)) {
            String resp = sendVerifyCode(phone, code);

            SmsToken smsToken = new SmsToken();
            smsToken.setPhone(phone);
            smsToken.setCode(code);
            smsToken.setValid(true);
            smsToken.setResponse(resp);
            save(smsToken);
        }
    }


    private SmsToken findLastestValidByPhone(String phone) {
        SmsToken smsToken = new SmsToken();
        smsToken.setPhone(phone);
        smsToken.setValid(true);
        List<SmsToken> ls = sortQuery(smsToken, "id", true);
        return safeGet(ls, 0);
    }


    private boolean clearHistory(String phone) {
        SmsToken smsToken = new SmsToken();
        smsToken.setPhone(phone);
        List<SmsToken> ls = query(smsToken);
        if (isEmptyList(ls)) return true;
        for (SmsToken l : ls) {
            l.setValid(false);
            update(l);
        }
        return true;
    }

    private String sendVerifyCode(String phone, String code) throws Exception {
        return "";
    }

    public boolean checkPhoneNumber(String phone) {
        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }
        if (phone.startsWith("86")) {
            phone = phone.substring(2);
        }
        if (phone.length() != 11) {
            return false;
        }
        if (!phone.startsWith("1")) {
            return false;
        }
        return true;
    }

    public boolean isValid(SmsToken smsToken) {
        if (smsToken == null) return false;
        if (!smsToken.isValid()) return false;
        if (now() - smsToken.getCreated() > 600000) {
            smsToken.setValid(false);
            update(smsToken);
            return false;
        }
        return true;
    }
}
