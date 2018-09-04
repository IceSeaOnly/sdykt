package site.binghai.biz.inters;

import site.binghai.biz.entity.Token;
import site.binghai.lib.entity.WxUser;
import site.binghai.lib.inters.BaseInterceptor;
import site.binghai.biz.utils.MockUtils;

import javax.servlet.http.HttpSession;

public class UserLoginInter extends BaseInterceptor {
    private String TAG;
    private String TOKEN_TAG;

    public UserLoginInter() {
        this.TAG = new WxUser().sessionTag();
        this.TOKEN_TAG = new Token().sessionTag();
    }

    @Override
    protected String getRedirectUrl(HttpSession session) {
        return "/slogin#slogin";
    }

    @Override
    protected String getFilterTag(HttpSession session) {
//        if(session.getAttribute(TAG) == null){
//            session.setAttribute(TAG, MockUtils.mockUser());
//            session.setAttribute(TOKEN_TAG,MockUtils.mockToken());
//        }
        return TAG;
    }
}
