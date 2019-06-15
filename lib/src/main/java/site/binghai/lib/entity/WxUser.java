package site.binghai.lib.entity;

import lombok.Data;
import site.binghai.lib.interfaces.SessionPersistent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@Entity
public class WxUser extends BaseEntity implements SessionPersistent {
    @Id
    @GeneratedValue
    private Long id;
    private Long bindTokenId;
    private String tokenNo;
    private String password;
    private String avatar;
    private String userName;
    private String phone;
    private String email;
    private String myDeclaration;
    private String openId;
    private String region; // 所在区域
    private Long refereeId; //推荐人id
    private Boolean hiden;

}
