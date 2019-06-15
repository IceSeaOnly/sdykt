package site.binghai.lib.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import site.binghai.lib.entity.WxUser;

import java.util.List;

@Service
public class WxUserService extends BaseService<WxUser> {

    public WxUser login(String phone, String passwd) {
        WxUser user = new WxUser();
        user.setPhone(phone);
        user.setPassword(passwd);
        return queryOne(user);
    }

    public WxUser findByPhone(String phone) {
        WxUser user = new WxUser();
        user.setPhone(phone);
        return queryOne(user);
    }

    public WxUser findByTokenNo(String token_no) {
        WxUser user = new WxUser();
        user.setTokenNo(token_no);
        return queryOne(user);
    }

    public List<WxUser> findAllWithoutHiden(Integer page, int pageSize) {
        WxUser example = new WxUser();
        example.setCreated(null);
        example.setCreatedTime(null);
        Example<WxUser> ex = Example.of(example);
        return getDao().findAll(ex, new PageRequest(page, pageSize)).getContent();
    }
}
