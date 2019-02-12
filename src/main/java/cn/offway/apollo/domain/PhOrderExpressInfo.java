package cn.offway.apollo.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 订单物流
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Entity
@Table(name = "ph_order_express_info")
public class PhOrderExpressInfo implements Serializable {

    /** ID **/
    private Long id;

    /** 订单ID **/
    private Long orderId;

    /** 订单号 **/
    private String orderNo;

    /** 物流订单号 **/
    private String expressOrderNo;

    /** 类型[0-寄,1-退] **/
    private String type;

    /** 快递运单号 **/
    private String mailNo;

    /** 寄件方手机 **/
    private String fromPhone;

    /** 寄件方 **/
    private String fromAddrId;

    /** 收件方手机 **/
    private String toPhone;

    /** 收件方 **/
    private String toAddrId;

    /** 状态[0-已下单,1-已接单,2-运送中,3-已签收] **/
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

    @Column(name = "order_id", length = 11)
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Column(name = "order_no", length = 50)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name = "express_order_no", length = 50)
    public String getExpressOrderNo() {
        return expressOrderNo;
    }

    public void setExpressOrderNo(String expressOrderNo) {
        this.expressOrderNo = expressOrderNo;
    }

    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "mail_no", length = 50)
    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    @Column(name = "from_phone", length = 50)
    public String getFromPhone() {
        return fromPhone;
    }

    public void setFromPhone(String fromPhone) {
        this.fromPhone = fromPhone;
    }

    @Column(name = "from_addr_id", length = 50)
    public String getFromAddrId() {
        return fromAddrId;
    }

    public void setFromAddrId(String fromAddrId) {
        this.fromAddrId = fromAddrId;
    }

    @Column(name = "to_phone", length = 50)
    public String getToPhone() {
        return toPhone;
    }

    public void setToPhone(String toPhone) {
        this.toPhone = toPhone;
    }

    @Column(name = "to_addr_id", length = 50)
    public String getToAddrId() {
        return toAddrId;
    }

    public void setToAddrId(String toAddrId) {
        this.toAddrId = toAddrId;
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
