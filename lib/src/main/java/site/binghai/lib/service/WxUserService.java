package site.binghai.lib.service;

import org.springframework.stereotype.Service;
import site.binghai.lib.entity.WxUser;

@Service
public class WxUserService extends BaseService<WxUser> {

    public WxUser login(String phone, String passwd) {
        WxUser user = new WxUser();
        user.setPhone(phone);
        user.setPassword(passwd);
        return queryOne(user);
    }


    public WxUser findByPhone(String phone){
        WxUser user = new WxUser();
        user.setPhone(phone);
        return queryOne(user);
    }
}
