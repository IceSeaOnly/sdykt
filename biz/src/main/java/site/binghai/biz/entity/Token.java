package site.binghai.biz.entity;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import site.binghai.biz.enums.PrivilegeEnum;
import site.binghai.lib.entity.BaseEntity;
import site.binghai.lib.interfaces.SessionPersistent;
import site.binghai.lib.utils.TimeTools;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class Token extends BaseEntity implements SessionPersistent {
    @Id
    @GeneratedValue
    private Long id;
    private Long bindUserId;
    private String token;
    private String secret;
    private String tokenName;
    private Long activeTime;
    private String activeTimeStr;
    //ONE,TWO,THREE,etc...
    private String privileges;
    @Transient
    private List<String> privilegeList;


    public void setPrivileges(String privileges) {
        this.privileges = privileges;
        if (StringUtils.isBlank(privileges)) return;
        privilegeList = Arrays.asList(privileges.split(","));
    }

    public List<String> getPrivilegeList() {
        if (CollectionUtils.isEmpty(privilegeList)) {
            this.setPrivileges(this.privileges);
        }
        return privilegeList;
    }

    public List<String> getPrivilegeProjectNameList() {
        List<String> ls = new ArrayList<>();
        for (String s : getPrivilegeList()) {
            ls.add(PrivilegeEnum.valueOfName(s).getProjectName());
        }
        return ls;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
        this.activeTimeStr = TimeTools.format(activeTime);
    }

    public String getPrivilegeProjectNames() {
        return StringUtils.join(getPrivilegeProjectNameList(), ",");
    }

    public boolean hasPrivilege(Long itemId) {
        return getPrivilegeList().contains(itemId);
    }
}
