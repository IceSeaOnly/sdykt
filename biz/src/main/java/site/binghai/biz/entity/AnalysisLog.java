package site.binghai.biz.entity;

import lombok.Data;
import site.binghai.lib.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author huaishuo
 * @date 2019/1/3 下午2:59
 **/
@Data
@Entity
public class AnalysisLog extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String tag;
    @Column(columnDefinition = "TEXT")
    private String content;
}
