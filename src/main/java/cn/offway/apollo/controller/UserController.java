package cn.offway.apollo.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.offway.apollo.domain.PhAuth;
import cn.offway.apollo.dto.AuthDto;
import cn.offway.apollo.service.PhAuthService;
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
	
	
	@ApiOperation("认证")
	@PostMapping("/auth")
	public JsonResult auth(@ApiParam("认证信息") AuthDto authDto){
		
		PhAuth phAuth = new PhAuth();
		BeanUtils.copyProperties(authDto, phAuth);
		phAuth.setCreateTime(new Date());
		phAuth.setStatus("0");
		phAuthService.save(phAuth);
		
		return jsonResultHelper.buildSuccessJsonResult(null);
	}
}
