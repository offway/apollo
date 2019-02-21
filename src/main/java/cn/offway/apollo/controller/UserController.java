package cn.offway.apollo.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.offway.apollo.domain.PhAuth;
import cn.offway.apollo.domain.PhOrderGoods;
import cn.offway.apollo.domain.PhOrderInfo;
import cn.offway.apollo.dto.AuthDto;
import cn.offway.apollo.dto.OrderInfoDto;
import cn.offway.apollo.service.PhAuthService;
import cn.offway.apollo.service.PhCodeService;
import cn.offway.apollo.service.PhOrderGoodsService;
import cn.offway.apollo.service.PhOrderInfoService;
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
	
	@Autowired
	private PhOrderInfoService phOrderInfoService;
	
	@Autowired
	private PhOrderGoodsService phOrderGoodsService;
	
	
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
	
	@ApiOperation(value="订单查询")
	@GetMapping("/order")
	public JsonResult order(
			@ApiParam("用户ID") @RequestParam String unionid,
			@ApiParam("类型[0-发货中,1-使用中,2-归还中,3-已完成]") @RequestParam String type,
			@ApiParam("页码,从0开始") @RequestParam int page,
		    @ApiParam("页大小") @RequestParam int size){
		
		Page<PhOrderInfo> page2 = phOrderInfoService.findByPage(unionid.trim(), type.trim(), new PageRequest(page*size, (page+1)*size));
		List<PhOrderInfo> phOrderInfos = page2.getContent();
		List<OrderInfoDto> dtos = new ArrayList<>();
		for (PhOrderInfo phOrderInfo : phOrderInfos) {
			OrderInfoDto dto = new OrderInfoDto();
			List<PhOrderGoods> goods = phOrderGoodsService.findByOrderNo(phOrderInfo.getOrderNo());
			BeanUtils.copyProperties(phOrderInfo, dto);
			dto.setGoods(goods);
			dtos.add(dto);
		}
		
		Page<OrderInfoDto> page3 = new PageImpl<>(dtos, new PageRequest(page*size, (page+1)*size), page2.getTotalElements());
		return jsonResultHelper.buildSuccessJsonResult(page3);
	}
	
	
}
