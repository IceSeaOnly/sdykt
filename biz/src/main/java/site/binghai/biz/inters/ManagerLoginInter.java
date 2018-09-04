package site.binghai.biz.inters;

import site.binghai.biz.entity.Token;
import site.binghai.lib.entity.Manager;
import site.binghai.lib.entity.WxUser;
import site.binghai.lib.inters.BaseInterceptor;

import javax.servlet.http.HttpSession;

public class ManagerLoginInter extends BaseInterceptor {
    private String TAG;

    public ManagerLoginInter() {
        this.TAG = new Manager().sessionTag();
    }

    @Override
    protected String getRedirectUrl(HttpSession session) {
        return "/mlogin#login";
    }

    @Override
    protected String getFilterTag(HttpSession session) {
        return TAG;
    }
}
