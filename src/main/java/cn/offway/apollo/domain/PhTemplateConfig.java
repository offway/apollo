package cn.offway.apollo.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 杂志模板配置
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Entity
@Table(name = "ph_template_config")
public class PhTemplateConfig implements Serializable {

    /** id **/
    private Long id;

    /** 所属杂志ID **/
    private Long goodsId;

    /** 模版名称 **/
    private String name;

    /** 排序 **/
    private Long sort;

    /** 发布状态[0-公开,1-解锁] **/
    private String lock;

    /** 解锁条件[0-公开,1-解锁] **/
    private String conditions;

    /** 解锁说明 **/
    private String conditionsRemark;

    /** 动画样式 **/
    private String type;

    /** 启用状态[0-启用,1-不启用] **/
    private String status;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "goods_id", length = 11)
    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "sort", length = 11)
    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    @Column(name = "lock", length = 2)
    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    @Column(name = "conditions", length = 2)
    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    @Column(name = "conditions_remark", length = 200)
    public String getConditionsRemark() {
        return conditionsRemark;
    }

    public void setConditionsRemark(String conditionsRemark) {
        this.conditionsRemark = conditionsRemark;
    }

    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
