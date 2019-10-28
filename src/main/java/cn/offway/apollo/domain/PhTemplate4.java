package cn.offway.apollo.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 杂志模版4
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Entity
@Table(name = "ph_template_4")
public class PhTemplate4 implements Serializable {

    /** id **/
    private Long id;

    /** 所属杂志ID **/
    private Long goodsId;

    /** 类型[0-类型1(两张图片),1-类型2(三张图片),2-类型3(三张图片),3-类型4(三张图片),4-类型5(两张图片),5-类型6(两张图片),6-类型7(两张图片),7-类型8(两张图片)] **/
    private String type;

    /** 图片1 **/
    private String imageOneUrl;

    /** 图片2 **/
    private String imageTwoUrl;

    /** 图片3 **/
    private String imageThreeUrl;

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

    @Column(name = "image_one_url", length = 200)
    public String getImageOneUrl() {
        return imageOneUrl;
    }

    public void setImageOneUrl(String imageOneUrl) {
        this.imageOneUrl = imageOneUrl;
    }

    @Column(name = "image_two_url", length = 200)
    public String getImageTwoUrl() {
        return imageTwoUrl;
    }

    public void setImageTwoUrl(String imageTwoUrl) {
        this.imageTwoUrl = imageTwoUrl;
    }

    @Column(name = "image_three_url", length = 200)
    public String getImageThreeUrl() {
        return imageThreeUrl;
    }

    public void setImageThreeUrl(String imageThreeUrl) {
        this.imageThreeUrl = imageThreeUrl;
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

}
