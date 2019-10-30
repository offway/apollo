package cn.offway.apollo.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 杂志模版2
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Entity
@Table(name = "ph_template_2")
public class PhTemplate2 implements Serializable {

    /** id **/
    private Long id;

    /** 所属杂志ID **/
    private Long goodsId;

    /** 展示类型[0-效果图,1-文字] **/
    private String type;

    /** 操作方式[0-左滑,1-自动] **/
    private String way;

    /** 图片链接 **/
    private String imageUrl;

    /** 提示文案 **/
    private String promptText;

    /** 位置[0-文字左上滑出,1-文字右上滑出,2-文字左下滑出,3-文字右下滑出,4-文字居中显示,5-全屏居中显示] **/
    private String location;

    /** 创建时间 **/
    private Date createTime;

    /** 备注 **/
    private String remark;

    /** pid **/
    private Long pid;


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

    @Column(name = "way", length = 2)
    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    @Column(name = "image_url", length = 200)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "prompt_text", length = 200)
    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    @Column(name = "location", length = 2)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

}
