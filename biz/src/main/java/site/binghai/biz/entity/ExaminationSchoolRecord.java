package site.binghai.biz.entity;

import lombok.Data;
import site.binghai.lib.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class ExaminationSchoolRecord extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    // 年份，2018年
    private Integer year;
    // 学科，文学编导
    private String subject;
    // 批次名，例:艺术本科统考批
    private String batchName;
    // 艺术文、艺术理
    private String batchType;
    // 院校名称
    private String schoolName;
    // 计划数
    private Integer planNum;
    // 投档比例
    private Double rate;
    // 投出数
    private Double throwNum;
    // 最高分
    private Double maxScore;
    // 最低分
    private Double minScore;
}
