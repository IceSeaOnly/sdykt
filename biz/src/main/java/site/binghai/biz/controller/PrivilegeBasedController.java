package site.binghai.biz.controller;

import site.binghai.biz.entity.Token;
import site.binghai.biz.enums.PrivilegeEnum;
import site.binghai.lib.controller.BaseController;

public abstract class PrivilegeBasedController extends BaseController {

    protected boolean hasPrivilege() {
        Token token = getSessionPersistent(Token.class);
        return token != null && token.getPrivilegeList().contains(privilegeRequired().name());
    }

    public Object permissionDeny() {
        return fail("权限未开通");
    }

    abstract PrivilegeEnum privilegeRequired();
}
