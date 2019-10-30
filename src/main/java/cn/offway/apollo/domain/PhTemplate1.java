package cn.offway.apollo.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 杂志模版1
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Entity
@Table(name = "ph_template_1")
public class PhTemplate1 implements Serializable {

    /** id **/
    private Long id;

    /** 所属杂志ID **/
    private Long goodsId;

    /** 类型[0-重叠,1-不重叠] **/
    private String type;

    /** 底图 **/
    private String imageUnderUrl;

    /** 文字图 **/
    private String imageTextUrl;

    /** 浮窗位置[0-左下,1-左上,2-正局中,3-右上,4-右下] **/
    private String rollingPosition;

    /** 滚动区域类型[0-图片,1-文字] **/
    private String rollingType;

    /** 滚动区域图片, 滚动区域类型为0时必填 **/
    private String rollingImageUrl;

    /** 滚动区域文字, 滚动区域类型为1时必填 **/
    private String rollingText;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;

    /** 提示 **/
    private String promptText;


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

    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "image_under_url", length = 200)
    public String getImageUnderUrl() {
        return imageUnderUrl;
    }

    public void setImageUnderUrl(String imageUnderUrl) {
        this.imageUnderUrl = imageUnderUrl;
    }

    @Column(name = "image_text_url", length = 200)
    public String getImageTextUrl() {
        return imageTextUrl;
    }

    public void setImageTextUrl(String imageTextUrl) {
        this.imageTextUrl = imageTextUrl;
    }

    @Column(name = "rolling_type", length = 2)
    public String getRollingType() {
        return rollingType;
    }

    public void setRollingType(String rollingType) {
        this.rollingType = rollingType;
    }

    @Column(name = "rolling_image_url", length = 200)
    public String getRollingImageUrl() {
        return rollingImageUrl;
    }

    public void setRollingImageUrl(String rollingImageUrl) {
        this.rollingImageUrl = rollingImageUrl;
    }

    @Column(name = "rolling_text")
    public String getRollingText() {
        return rollingText;
    }

    public void setRollingText(String rollingText) {
        this.rollingText = rollingText;
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

    @Column(name = "prompt_text", length = 100)
    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    @Column(name = "rolling_position", length = 2)
    public String getRollingPosition() {
        return rollingPosition;
    }

    public void setRollingPosition(String rollingPosition) {
        this.rollingPosition = rollingPosition;
    }

}
