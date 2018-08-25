package site.binghai.biz.entity;


import lombok.Data;
import site.binghai.lib.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class TokenSeed extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long current;
}
