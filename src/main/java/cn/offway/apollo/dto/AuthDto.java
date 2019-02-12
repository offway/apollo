package cn.offway.apollo.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 认证
 * @author wn
 *
 */
public class AuthDto {

	@ApiModelProperty(required = true, value ="只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。")
    private String unionid;

    @ApiModelProperty(required = true, value ="手机")
    private String phone;

    @ApiModelProperty(required = true, value ="企业名称")
    private String companyName;

    @ApiModelProperty(required = true, value ="职位")
    private String position;

    @ApiModelProperty(required = true, value ="姓名")
    private String realName;

    @ApiModelProperty(required = true, value ="身份证正面")
    private String idcardPositive;

    @ApiModelProperty(required = true, value ="身份证反面")
    private String idcardObverse;

    @ApiModelProperty(required = true, value ="在职证明")
    private String inCert;

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdcardPositive() {
		return idcardPositive;
	}

	public void setIdcardPositive(String idcardPositive) {
		this.idcardPositive = idcardPositive;
	}

	public String getIdcardObverse() {
		return idcardObverse;
	}

	public void setIdcardObverse(String idcardObverse) {
		this.idcardObverse = idcardObverse;
	}

	public String getInCert() {
		return inCert;
	}

	public void setInCert(String inCert) {
		this.inCert = inCert;
	}
    
    
}
