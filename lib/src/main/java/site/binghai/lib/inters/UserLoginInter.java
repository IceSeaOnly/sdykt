package site.binghai.lib.inters;

import site.binghai.lib.entity.WxUser;

import javax.servlet.http.HttpSession;

public class UserLoginInter extends BaseInterceptor {
    private String TAG;

    public UserLoginInter() {
        this.TAG = new WxUser().sessionTag();
    }

    @Override
    protected String getRedirectUrl(HttpSession session) {
        return "/login";
    }

    @Override
    protected String getFilterTag(HttpSession session) {
        return TAG;
    }
}
