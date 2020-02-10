package cn.offway.apollo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.offway.apollo.service.*;
import cn.offway.apollo.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
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

	@Autowired
	private PhWardrobeAuditService phWardrobeAuditService;



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
			@ApiParam("类型[类型[0-发货中,1-使用中,2-归还中,3-已完成,4-待晒图,5-待发货（不判断使用时间）,6-已寄出（不判断使用时间）]") @RequestParam String type,
			@ApiParam("页码,从0开始") @RequestParam int page,
		    @ApiParam("页大小") @RequestParam int size){

//		Page<PhOrderInfo> page2 = phOrderInfoService.findByPage(unionid.trim(), type.trim(), new PageRequest(page,size));
//		List<PhOrderInfo> phOrderInfos = page2.getContent();
//		List<OrderInfoDto> dtos = new ArrayList<>();
//		for (PhOrderInfo phOrderInfo : phOrderInfos) {
//			OrderInfoDto dto = new OrderInfoDto();
//			List<PhOrderGoods> goods = phOrderGoodsService.findByOrderNo(phOrderInfo.getOrderNo());
//			BeanUtils.copyProperties(phOrderInfo, dto);
//			dto.setGoods(goods);
//			dtos.add(dto);
//		}
//
//		Page<OrderInfoDto> page3 = new PageImpl<>(dtos, new PageRequest(page,size), page2.getTotalElements());
//		return jsonResultHelper.buildSuccessJsonResult(page3);
		Page<PhOrderInfo> page2 = phOrderInfoService.findByPage(unionid.trim(), type.trim(), new PageRequest(page, size));
		List<PhOrderInfo> phOrderInfos = page2.getContent();
		List<OrderInfoDto> dtos = new ArrayList<>();
		for (PhOrderInfo phOrderInfo : phOrderInfos) {
			List<String> sum = phOrderGoodsService.orderSum(phOrderInfo.getOrderNo());

			List<PhOrderGoods> goods = phOrderGoodsService.findByOrderNo(phOrderInfo.getOrderNo());

			for (String s : sum) {//String s : sum
				OrderInfoDto dto = new OrderInfoDto();
				BeanUtils.copyProperties(phOrderInfo, dto);
				List<PhOrderGoods> goodsList = new ArrayList<>();
				List<PhOrderGoods> goodsList1 = new ArrayList<>();
				for (PhOrderGoods good : goods) {
					if (null == good.getBatch()) {
						goodsList1.add(good);
					} else if (s.equals(good.getBatch())) {
						goodsList.add(good);
					}
				}
				if (goodsList.size() > 0) {
					if (!"0".equals(s)) {
						dto.setOrderNo(dto.getOrderNo() + "-" + s);
					}
					dto.setGoods(goodsList);
					dtos.add(dto);
				}
				if (goodsList1.size() > 0) {
					dto.setGoods(goodsList1);
					dtos.add(dto);
				}
			}
		}

		Page<OrderInfoDto> page3 = new PageImpl<>(dtos, new PageRequest(page, size), page2.getTotalElements());
		return jsonResultHelper.buildSuccessJsonResult(page3);
	}
	
	@ApiOperation(value="订单商品查询")
	@GetMapping("/orderGoods")
	public JsonResult orderGoods(@ApiParam("订单号") @RequestParam String orderNo,@ApiParam("批次号") @RequestParam String batch){
		List<PhOrderGoods> goods = phOrderGoodsService.findByOrderNo(orderNo,batch);
		return jsonResultHelper.buildSuccessJsonResult(goods);
	}


	private String queryExpress(String expressCode, String mailNo) {
		String key = "uyUDaSuE5009";
		String customer = "28B3DE9A2485E14FE0DAD40604A8922C";
		Map<String, String> innerParam = new HashMap<>();
		innerParam.put("com", expressCode);
		innerParam.put("num", mailNo);
		String innerParamStr = JSON.toJSONString(innerParam);
		String signStr = innerParamStr + key + customer;
		String sign = DigestUtils.md5Hex(signStr.getBytes()).toUpperCase();
		Map<String, String> param = new HashMap<>();
		param.put("customer", customer);
		param.put("param", innerParamStr);
		param.put("sign", sign);
		return HttpClientUtil.post("https://poll.kuaidi100.com/poll/query.do", param);
	}

	@ApiOperation(value="快递路由查询")
	@GetMapping("/route")
	public JsonResult route(@ApiParam("订单号") @RequestParam String orderNo,
			@ApiParam("类型[0-寄,1-返]") @RequestParam String type,
							@ApiParam("批次号") @RequestParam String batch){
		
//		Map<String, Object> map = new HashMap<>();
//		PhOrderInfo phOrderInfo = phOrderInfoService.findByOrderNo(orderNo);
//		map.put("orderNo", orderNo);
//		map.put("useDate", phOrderInfo.getUseDate());
//		PhOrderExpressInfo phOrderExpressInfo = phOrderExpressInfoService.findByOrderNoAndType(orderNo, type);
//		if(null != phOrderExpressInfo){
//			String mailno = phOrderExpressInfo.getMailNo();
//			map.put("toRealName", phOrderExpressInfo.getToRealName());
//			map.put("toPhone", phOrderExpressInfo.getToPhone());
//			map.put("toContent", phOrderExpressInfo.getToContent());
//			List<PhOrderExpressDetail> expressDetails = phOrderExpressDetailService.findByMailNoOrderByAcceptTimeDesc(mailno);
//			map.put("expressDetails", expressDetails);
//		}
//		return jsonResultHelper.buildSuccessJsonResult(map);
		Map<String, Object> map = new HashMap<>();
		String mailNo = null;
		PhOrderInfo phOrderInfo = phOrderInfoService.findByOrderNo(orderNo);
		map.put("orderNo", orderNo);
		map.put("useDate", phOrderInfo.getUseDate());
		PhOrderExpressInfo phOrderExpressInfo = phOrderExpressInfoService.findByOrderNoAndType(orderNo, type);
		if (null == phOrderExpressInfo.getMailNo() && !"0".equals(phOrderExpressInfo.getStatus())){
			mailNo = phOrderGoodsService.GetMailNo(orderNo,batch);
		}

		if (null != phOrderExpressInfo) {
			String json = null;
			String mailno = phOrderExpressInfo.getMailNo();
			map.put("toRealName", phOrderExpressInfo.getToRealName());
			map.put("toPhone", phOrderExpressInfo.getToPhone());
			map.put("toContent", phOrderExpressInfo.getToContent());
			map.put("toProvince", phOrderExpressInfo.getToProvince());
			map.put("toCity", phOrderExpressInfo.getToCity());
			map.put("toCounty", phOrderExpressInfo.getToCounty());
			if (mailNo!= null){
				json = queryExpress("shunfeng", mailNo);
			}else {
				json = queryExpress("shunfeng", phOrderExpressInfo.getMailNo());
			}
			JSONObject jsonObject = JSONObject.parseObject(json);
			map.put("details", jsonObject);
		}
		return jsonResultHelper.buildSuccessJsonResult(map);
	}
	
	@ApiOperation(value="快递状态查询",notes="返回 status [1-已下单,2-已接单,3-运送中,4-已签收]")
	@GetMapping("/express")
	public JsonResult express(@ApiParam("订单号") @RequestParam String orderNo,
							  @ApiParam("批次") @RequestParam String batch,
			@ApiParam("类型[0-寄,1-返]") @RequestParam String type){

//		PhOrderExpressInfo phOrderExpressInfo = phOrderExpressInfoService.findByOrderNoAndType(orderNo, type);
//		return jsonResultHelper.buildSuccessJsonResult(phOrderExpressInfo);
		if ("0".equals(type)){
			Map<String,Object> map = new HashMap<>();
			PhOrderExpressInfo phOrderExpressInfo = phOrderExpressInfoService.findByOrderNoAndType(orderNo, type);
			map.put("phOrderExpressInfo",phOrderExpressInfo);
			return jsonResultHelper.buildSuccessJsonResult(map);
		}else {
			PhOrderExpressInfo phOrderExpressInfo = phOrderExpressInfoService.findByOrderNoAndType(orderNo, type);
			return jsonResultHelper.buildSuccessJsonResult(phOrderExpressInfo);
		}
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
								@ApiParam("批次") @RequestParam String batch,
		    @ApiParam("使用明星") @RequestParam String starName){
		
//		PhOrderInfo phOrderInfo = phOrderInfoService.findByOrderNo(orderNo);
//
//		PhShowImage phShowImage = new PhShowImage();
//		phShowImage.setBrandId(phOrderInfo.getBrandId());
//		phShowImage.setBrandLogo(phOrderInfo.getBrandLogo());
//		phShowImage.setBrandName(phOrderInfo.getBrandName());
//		String unionid = phOrderInfo.getUnionid();
//		phShowImage.setUnionid(unionid);
//		PhUserInfo phUserInfo = phUserInfoService.findByUnionid(unionid);
//		phShowImage.setRealName(phUserInfo.getRealName());
//		phShowImage.setPosition(phUserInfo.getPosition());
//		phShowImage.setCreateTime(new Date());
//		phShowImage.setUrl(url);
//		phShowImage.setIsOffway(phOrderInfo.getIsOffway());
//		phShowImage.setOrderNo(orderNo);
//		phShowImage.setShowImage(images);
//		phShowImage.setContent(content);
//		phShowImage.setStatus("0");
//		phShowImage.setStarName(starName);
//		phShowImageService.save(phShowImage);
//
//		phOrderInfo.setIsUpload("1");
//		phOrderInfoService.save(phOrderInfo);
//
//		return jsonResultHelper.buildSuccessJsonResult(null);
		PhOrderInfo phOrderInfo = phOrderInfoService.findByOrderNo(orderNo);

		PhShowImage phShowImage = new PhShowImage();
		phShowImage.setBrandId(phOrderInfo.getBrandId());
		phShowImage.setBrandLogo(phOrderInfo.getBrandLogo());
		phShowImage.setBrandName(phOrderInfo.getBrandName());
		String unionid = phOrderInfo.getUnionid();
		phShowImage.setUnionid(unionid);
		PhUserInfo phUserInfo = phUserInfoService.findByUnionid(unionid);
		phShowImage.setRealName(phUserInfo.getRealName());
		phShowImage.setPosition(phUserInfo.getPosition());
		phShowImage.setCreateTime(new Date());
		phShowImage.setUrl(url);
		phShowImage.setIsOffway(phOrderInfo.getIsOffway());
		phShowImage.setOrderNo(orderNo);
		phShowImage.setShowImage(images);
		phShowImage.setContent(content);
		phShowImage.setStatus("0");
		phShowImage.setStarName(starName);
		if (null == batch) {
			phShowImage.setBatch("0");
		} else {
			phShowImage.setBatch(batch);
		}
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
		resultMap.put("sendout", phOrderInfoService.findAll(unionid, "5").size());
		resultMap.put("use", phOrderInfoService.findAll(unionid, "1").size());
		resultMap.put("return", phOrderInfoService.findAll(unionid, "2").size());
		resultMap.put("show", phOrderInfoService.findAll(unionid, "4").size());
		resultMap.put("send", phOrderInfoService.findAll(unionid, "6").size());
		resultMap.put("audit", String.valueOf(phWardrobeAuditService.auditCount(unionid)));
		resultMap.put("position", phUserInfo.getPosition());

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
			@ApiParam("批次") @RequestParam String batch,
			@ApiParam("地址ID") @RequestParam(required = false) Long addrId){
		return phOrderInfoService.saveOrder(orderNo, null==sendstarttime?"":sendstarttime, mailNo,addrId,batch);
	}
	
}
