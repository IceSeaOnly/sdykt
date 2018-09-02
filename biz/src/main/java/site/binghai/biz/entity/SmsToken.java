package site.binghai.biz.entity;

import lombok.Data;
import site.binghai.lib.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class SmsToken extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String phone;
    private String code;
    private String response;
    private boolean valid; // 验证成功1次后失效
}
