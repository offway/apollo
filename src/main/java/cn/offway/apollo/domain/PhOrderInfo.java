package cn.offway.apollo.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 订单
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Entity
@Table(name = "ph_order_info")
public class PhOrderInfo implements Serializable {

    /** ID **/
    private Long id;

    /** 订单号 **/
    private String orderNo;

    /** 使用日期 **/
    private Date useDate;

    /** 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。 **/
    private String unionid;

    /** 品牌ID **/
    private Long brandId;

    /** 品牌名称 **/
    private String brandName;

    /** 品牌LOGO **/
    private String brandLogo;

    /** 是否自营[0-否,1-是] **/
    private String isOffway;

    /** 状态[0-已下单,1-已发货,2-已归还,3-已晒图,4-已拒绝,5-已通过] **/
    private String status;

    /** 创建时间 **/
    private Date createTime;

    /** 归还时间 **/
    private Date returnTime;

    /** 晒图时间 **/
    private Date showTime;

    /** 审核人 **/
    private String checkName;

    /** 审核时间 **/
    private Date checkTime;

    /** 拒绝原因 **/
    private String checkContent;

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

    @Column(name = "order_no", length = 50)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "use_date")
    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    @Column(name = "unionid", length = 200)
    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Column(name = "brand_id", length = 11)
    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    @Column(name = "brand_name", length = 50)
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Column(name = "brand_logo", length = 50)
    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    @Column(name = "is_offway", length = 2)
    public String getIsOffway() {
        return isOffway;
    }

    public void setIsOffway(String isOffway) {
        this.isOffway = isOffway;
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "return_time")
    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "show_time")
    public Date getShowTime() {
        return showTime;
    }

    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }

    @Column(name = "check_name", length = 50)
    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "check_time")
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    @Column(name = "check_content", length = 200)
    public String getCheckContent() {
        return checkContent;
    }

    public void setCheckContent(String checkContent) {
        this.checkContent = checkContent;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
