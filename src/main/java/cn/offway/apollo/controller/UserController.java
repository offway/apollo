package cn.offway.apollo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.offway.apollo.domain.PhAuth;
import cn.offway.apollo.dto.AuthDto;
import cn.offway.apollo.service.PhAuthService;
import cn.offway.apollo.service.PhCodeService;
import cn.offway.apollo.utils.CommonResultCode;
import cn.offway.apollo.utils.JsonResult;
import cn.offway.apollo.utils.JsonResultHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JsonResultHelper jsonResultHelper;
	
	@Autowired
	private PhAuthService phAuthService;
	
	@Autowired
	private PhCodeService phCodeService;
	
	
	@ApiOperation("校验邀请码")
	@GetMapping("/code")
	public JsonResult code(@ApiParam("邀请码") @RequestParam String code){
		
		int count = phCodeService.countByCodeAndStatus(code,"0");
		if(count == 0){
			//邀请码无效
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.CODE_ERROR);
		}
		
		return jsonResultHelper.buildSuccessJsonResult(null);

	}
	
	@ApiOperation(value="查询认证详情")
	@GetMapping("/auth")
	public JsonResult auth(@RequestParam String unionid){
		PhAuth phAuth = phAuthService.findByUnionid(unionid);
		return jsonResultHelper.buildSuccessJsonResult(phAuth);
	}
	
	@ApiOperation("认证")
	@PostMapping("/auth")
	public JsonResult auth(@RequestBody @ApiParam("认证信息") AuthDto authDto){
		return phAuthService.auth(authDto);
	}
}
