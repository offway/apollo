package cn.offway.apollo.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 杂志模版5
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 11:31:24 Exp $
 */
@Entity
@Table(name = "ph_template_5")
public class PhTemplate5 implements Serializable {

    /** id **/
    private Long id;

    /** 所属杂志ID **/
    private Long goodsId;

    /** 类型[0-底图,1-无边框] **/
    private String type;

    /** 底图 **/
    private String imageUnderUrl;

    /** 视频 **/
    private String videoUrl;

    /** 视频封面 **/
    private String videoCoverUrl;

    /** 提示文字 **/
    private String promptText;

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

    @Column(name = "type", length = 255)
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

    @Column(name = "video_url", length = 200)
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Column(name = "video_cover_url", length = 255)
    public String getVideoCoverUrl() {
        return videoCoverUrl;
    }

    public void setVideoCoverUrl(String videoCoverUrl) {
        this.videoCoverUrl = videoCoverUrl;
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

}
