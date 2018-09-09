package site.binghai.biz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.biz.entity.SmsToken;
import site.binghai.biz.utils.TokenGenerator;
import site.binghai.lib.def.SmsService;
import site.binghai.lib.service.BaseService;
import site.binghai.lib.utils.TimeTools;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SmsTokenService extends BaseService<SmsToken> {

    @Autowired
    private SmsService aliyunSmsService;

    @Transactional
    public boolean varify(String phone, String input) {
        SmsToken smsToken = findLastestValidByPhone(phone);
        if (!isValid(smsToken)) return false;
        if (smsToken.getCode().equals(input)) {
            smsToken.setValid(false);
            update(smsToken);
            return true;
        }
        return false;
    }

    @Transactional
    public void sendVerifyCode(String phone) throws Exception {
        String code = (""+now()).substring(7);
        if (checkPhoneNumber(phone) && clearHistory(phone)) {
            String resp = aliyunSmsService.sendVerifyCodeSms(phone, code);

            SmsToken smsToken = new SmsToken();
            smsToken.setPhone(phone);
            smsToken.setCode(code);
            smsToken.setValid(true);
            smsToken.setResponse(resp);
            smsToken = save(smsToken);
            logger.info("sms verify sended:{}",smsToken);
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

    @Transactional
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
