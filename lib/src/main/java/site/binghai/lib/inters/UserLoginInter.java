package site.binghai.lib.inters;

import site.binghai.lib.entity.WxUser;
import site.binghai.lib.utils.MockUtils;

import javax.servlet.http.HttpSession;

public class UserLoginInter extends BaseInterceptor {
    private String TAG;

    public UserLoginInter() {
        this.TAG = new WxUser().sessionTag();
    }

    @Override
    protected String getRedirectUrl(HttpSession session) {
        return "/slogin#slogin";
    }

    @Override
    protected String getFilterTag(HttpSession session) {
        if(session.getAttribute(TAG) == null){
            session.setAttribute(TAG, MockUtils.mockUser());
        }
        return TAG;
    }
}
