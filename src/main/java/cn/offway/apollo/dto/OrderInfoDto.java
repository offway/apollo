package cn.offway.apollo.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.offway.apollo.domain.PhOrderGoods;

public class OrderInfoDto implements Serializable{

	/** ID **/
    private Long id;

    /** 订单号 **/
    private String orderNo;

    /** 使用日期 **/
    private Date useDate;
    
    /** 使用者 **/
    private String users;

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

    /** 状态[0-已下单,1-已发货,2-已寄回,3-已收货] **/
    private String status;
    
    /** 是否晒图[0-否,1-是]**/
    private String isUpload;

    /** 创建时间 **/
    private Date createTime;

    /** 寄回时间 **/
    private Date returnTime;
    
    /** 收货时间 **/
    private Date receiptTime;

    /** 晒图时间 **/
    private Date showTime;
    
    /** 备注 **/
    private String remark;
    
    private List<PhOrderGoods> goods;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getUseDate() {
		return useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandLogo() {
		return brandLogo;
	}

	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}

	public String getIsOffway() {
		return isOffway;
	}

	public void setIsOffway(String isOffway) {
		this.isOffway = isOffway;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public Date getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<PhOrderGoods> getGoods() {
		return goods;
	}

	public void setGoods(List<PhOrderGoods> goods) {
		this.goods = goods;
	}
    
}
