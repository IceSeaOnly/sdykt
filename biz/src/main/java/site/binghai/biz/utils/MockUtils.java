package site.binghai.biz.utils;

import org.apache.commons.lang3.StringUtils;
import site.binghai.biz.entity.Token;
import site.binghai.biz.enums.PrivilegeEnum;
import site.binghai.lib.entity.WxUser;

import java.util.Arrays;

public class MockUtils {
    public static WxUser mockUser() {
        WxUser user = new WxUser();
        user.setAvatar("http://cdn.binghai.site/o_1cmc3drecfph1nt48dv194k1p6ta.jpg");
        user.setPassword("111");
        user.setPhone("12345678900");
        user.setUserName("TEST_MOCK");
        user.setId(999999999L);
        return user;
    }

    public static Token mockToken() {
        Token token = new Token();
        token.setPrivileges(StringUtils.join(PrivilegeEnum.values(), ","));
        return token;
    }
}
