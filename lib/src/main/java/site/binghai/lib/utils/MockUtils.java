package site.binghai.lib.utils;

import site.binghai.lib.entity.WxUser;

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
}
