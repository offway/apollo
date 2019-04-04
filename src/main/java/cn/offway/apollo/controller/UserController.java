package cn.offway.apollo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.offway.apollo.domain.PhOrderExpressDetail;
import cn.offway.apollo.domain.PhOrderExpressInfo;
import cn.offway.apollo.domain.PhOrderGoods;
import cn.offway.apollo.domain.PhOrderInfo;
import cn.offway.apollo.domain.PhShowImage;
import cn.offway.apollo.domain.PhUserInfo;
import cn.offway.apollo.dto.AuthDto;
import cn.offway.apollo.dto.OrderInfoDto;
import cn.offway.apollo.service.PhAuthService;
import cn.offway.apollo.service.PhCodeService;
import cn.offway.apollo.service.PhCreditDetailService;
import cn.offway.apollo.service.PhOrderExpressDetailService;
import cn.offway.apollo.service.PhOrderExpressInfoService;
import cn.offway.apollo.service.PhOrderGoodsService;
import cn.offway.apollo.service.PhOrderInfoService;
import cn.offway.apollo.service.PhShowImageService;
import cn.offway.apollo.service.PhUserInfoService;
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
	
	@Autowired
	private PhOrderExpressDetailService phOrderExpressDetailService;
	
	@Autowired
	private PhOrderExpressInfoService phOrderExpressInfoService;
	
	@Autowired
	private PhShowImageService phShowImageService;
	
	@Autowired
	private PhUserInfoService phUserInfoService;
	
	@Autowired
	private PhCreditDetailService phCreditDetailService;
	
	
	
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
			@ApiParam("类型[0-发货中,1-使用中,2-归还中,3-已完成,4-待晒图]") @RequestParam String type,
			@ApiParam("页码,从0开始") @RequestParam int page,
		    @ApiParam("页大小") @RequestParam int size){
		
		Page<PhOrderInfo> page2 = phOrderInfoService.findByPage(unionid.trim(), type.trim(), new PageRequest(page,size));
		List<PhOrderInfo> phOrderInfos = page2.getContent();
		List<OrderInfoDto> dtos = new ArrayList<>();
		for (PhOrderInfo phOrderInfo : phOrderInfos) {
			OrderInfoDto dto = new OrderInfoDto();
			List<PhOrderGoods> goods = phOrderGoodsService.findByOrderNo(phOrderInfo.getOrderNo());
			BeanUtils.copyProperties(phOrderInfo, dto);
			dto.setGoods(goods);
			dtos.add(dto);
		}
		
		Page<OrderInfoDto> page3 = new PageImpl<>(dtos, new PageRequest(page,size), page2.getTotalElements());
		return jsonResultHelper.buildSuccessJsonResult(page3);
	}
	
	@ApiOperation(value="订单商品查询")
	@GetMapping("/orderGoods")
	public JsonResult orderGoods(@ApiParam("订单号") @RequestParam String orderNo){
		List<PhOrderGoods> goods = phOrderGoodsService.findByOrderNo(orderNo);
		return jsonResultHelper.buildSuccessJsonResult(goods);
	}
	
	@ApiOperation(value="快递路由查询")
	@GetMapping("/route")
	public JsonResult route(@ApiParam("订单号") @RequestParam String orderNo,
			@ApiParam("类型[0-寄,1-返]") @RequestParam String type){
		
		Map<String, Object> map = new HashMap<>();
		PhOrderInfo phOrderInfo = phOrderInfoService.findByOrderNo(orderNo);
		map.put("orderNo", orderNo);
		map.put("useDate", phOrderInfo.getUseDate());
		PhOrderExpressInfo phOrderExpressInfo = phOrderExpressInfoService.findByOrderNoAndType(orderNo, type);
		if(null != phOrderExpressInfo){
			String mailno = phOrderExpressInfo.getMailNo();
			map.put("toRealName", phOrderExpressInfo.getToRealName());
			map.put("toPhone", phOrderExpressInfo.getToPhone());
			map.put("toContent", phOrderExpressInfo.getToContent());
			List<PhOrderExpressDetail> expressDetails = phOrderExpressDetailService.findByMailNoOrderByAcceptTimeDesc(mailno);
			map.put("expressDetails", expressDetails);
		}
		return jsonResultHelper.buildSuccessJsonResult(map);
	}
	
	@ApiOperation(value="快递状态查询",notes="返回 status [1-已下单,2-已接单,3-运送中,4-已签收]")
	@GetMapping("/express")
	public JsonResult express(@ApiParam("订单号") @RequestParam String orderNo,
			@ApiParam("类型[0-寄,1-返]") @RequestParam String type){
		
		PhOrderExpressInfo phOrderExpressInfo = phOrderExpressInfoService.findByOrderNoAndType(orderNo, type);
		return jsonResultHelper.buildSuccessJsonResult(phOrderExpressInfo);
	}
	
	@ApiOperation(value="已晒图列表")
	@GetMapping("/showimage")
	public JsonResult showimage(@ApiParam("用户ID") @RequestParam String unionid,
			@ApiParam("页码,从0开始") @RequestParam int page,
		    @ApiParam("页大小") @RequestParam int size){
		return jsonResultHelper.buildSuccessJsonResult(phShowImageService.findByPage(unionid, new PageRequest(page,size)));
	}
	
	@ApiOperation(value="晒图详情")
	@GetMapping("/showimageInfo")
	public JsonResult showimageInfo(@ApiParam("晒图ID") @RequestParam Long id){
		return jsonResultHelper.buildSuccessJsonResult(phShowImageService.findOne(id));
	}
	
	@ApiOperation(value="晒图")
	@PostMapping("/showimage")
	public JsonResult showimage(@ApiParam("订单号") @RequestParam String orderNo,
			@ApiParam("图片地址，多个用英文逗号相隔") @RequestParam String images,
		    @ApiParam("网页链接") @RequestParam(required = false) String url,
		    @ApiParam("描述") @RequestParam(required = false) String content,
		    @ApiParam("使用明星") @RequestParam String starName){
		
		PhOrderInfo phOrderInfo = phOrderInfoService.findByOrderNo(orderNo);

		PhShowImage phShowImage = new PhShowImage();
		phShowImage.setBrandId(phOrderInfo.getBrandId());
		phShowImage.setBrandLogo(phOrderInfo.getBrandLogo());
		phShowImage.setBrandName(phOrderInfo.getBrandName());
		phShowImage.setUnionid(phOrderInfo.getUnionid());
		phShowImage.setCreateTime(new Date());
		phShowImage.setUrl(url);
		phShowImage.setIsOffway(phOrderInfo.getIsOffway());
		phShowImage.setOrderNo(orderNo);
		phShowImage.setShowImage(images);
		phShowImage.setContent(content);
		phShowImage.setStatus("0");
		phShowImage.setStarName(starName);
		phShowImageService.save(phShowImage);
		
		phOrderInfo.setIsUpload("1");
		phOrderInfoService.save(phOrderInfo);
		
		return jsonResultHelper.buildSuccessJsonResult(null);
	}
	
	@ApiOperation(value="我的")
	@GetMapping("/index")
	public JsonResult index(@ApiParam("用户ID") @RequestParam String unionid){
		
		Map<String, Object> resultMap = new HashMap<>();
		//0-发货中,1-使用中,2-归还中,3-已完成,4-待晒图
		PhUserInfo phUserInfo = phUserInfoService.findByUnionid(unionid);
		resultMap.put("nickname", phUserInfo.getNickname());
		resultMap.put("headimgurl", phUserInfo.getHeadimgurl());
		resultMap.put("creditScore", phUserInfo.getCreditScore());
		resultMap.put("sendout", phOrderInfoService.findAll(unionid, "0").size());
		resultMap.put("use", phOrderInfoService.findAll(unionid, "1").size());
		resultMap.put("return", phOrderInfoService.findAll(unionid, "2").size());
		resultMap.put("show", phOrderInfoService.findAll(unionid, "4").size());
		
		return jsonResultHelper.buildSuccessJsonResult(resultMap); 
	}
	
	@ApiOperation(value="信用记录")
	@GetMapping("/creditDetail")
	public JsonResult creditDetail(@ApiParam("用户ID") @RequestParam String unionid,
			@ApiParam("页码,从0开始") @RequestParam int page,
		    @ApiParam("页大小") @RequestParam int size){
		return jsonResultHelper.buildSuccessJsonResult(phCreditDetailService.findByPage(unionid, new PageRequest(page,size)));
	} 
	
	@ApiOperation(value="一键下单")
	@PostMapping("/addOrder")
	public JsonResult addOrder(
			@ApiParam("订单号") @RequestParam String orderNo,
			@ApiParam("要求上门取件开始时间，格式：YYYY-MM-DD HH24:MM:SS，示例：2012-7-30 09:30:00。两小时内上门不传该字段") @RequestParam(required = false) String sendstarttime,
			@ApiParam("快递单号,自行投递必传") @RequestParam(required = false) String mailNo,
			@ApiParam("地址ID") @RequestParam(required = false) Long addrId){
		return phOrderInfoService.saveOrder(orderNo, null==sendstarttime?"":sendstarttime, mailNo,addrId);
	}
	
}
