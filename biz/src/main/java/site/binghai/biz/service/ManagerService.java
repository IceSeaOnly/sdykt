package site.binghai.biz.service;

import org.springframework.stereotype.Service;
import site.binghai.lib.entity.Manager;
import site.binghai.lib.service.BaseService;
import site.binghai.lib.utils.MD5;

@Service
public class ManagerService extends BaseService<Manager> {

    public Manager login(String name, String passwd) {
        Manager manager = new Manager();
        manager.setUserName(name);
        manager.setPassWord(MD5.encryption(passwd));
        return queryOne(manager);
    }
}
