package cn.offway.apollo.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 解锁条件表
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Entity
@Table(name = "ph_lock")
public class PhLock implements Serializable {

    /** Id **/
    private Long id;

    /** 所属杂志ID **/
    private Long goodsId;

    /** 模版类型:[0-1号模板,1-2号模板,2-3号模板,3-4号模板,4-5号模板] **/
    private String templateType;

    /** 模版id **/
    private Long templateId;

    /** 是否解锁[0-否,1-是] **/
    private String islock;

    /** 解锁订阅数 **/
    private Long subscribeCount;

    /** 提示文字 **/
    private String promptText;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;

    /** pid **/
    private Long pid;

    /** 内容类型[0-文字,1-图片] **/
    private String contentType;

    /** 提示图片 **/
    private String promptImage;


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

    @Column(name = "template_type", length = 2)
    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    @Column(name = "template_id", length = 11)
    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    @Column(name = "islock", length = 2)
    public String getUnlock() {
        return islock;
    }

    public void setUnlock(String islock) {
        this.islock = islock;
    }

    @Column(name = "subscribe_count", length = 11)
    public Long getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(Long subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    @Column(name = "prompt_text", length = 100)
    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
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

    @Column(name = "pid", length = 11)
    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }


    @Column(name = "content_type", length = 2)
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Column(name = "prompt_image", length = 200)
    public String getPromptImage() {
        return promptImage;
    }

    public void setPromptImage(String promptImage) {
        this.promptImage = promptImage;
    }

}
