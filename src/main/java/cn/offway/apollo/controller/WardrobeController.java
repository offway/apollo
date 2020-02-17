package cn.offway.apollo.controller;

import java.text.ParseException;
import java.util.*;

import cn.offway.apollo.domain.*;
import cn.offway.apollo.service.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.offway.apollo.utils.CommonResultCode;
import cn.offway.apollo.utils.JsonResult;
import cn.offway.apollo.utils.JsonResultHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 衣柜
 * @author wn
 *
 */
@RestController
@RequestMapping("/wardrobe")
public class WardrobeController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JsonResultHelper jsonResultHelper;
	
	@Autowired
	private PhWardrobeService phWardrobeService;

	@Autowired
	private PhWardrobeAuditService phWardrobeAuditService;

	@Autowired
	private PhGoodsService goodsService;

	@Autowired
	private PhBrandService brandService;

	@Autowired
	private SmsService smsService;


	@ApiOperation(value="加入衣柜",notes="返回码：200=成功； 1009=衣柜调价衣物以至8件上限；1010=您的信用分太低，不能再借衣服；1011=您有一批订单反馈图未上传，上传后即可借衣；1012=有一笔订单未归还")
	@PostMapping("/add")
	public JsonResult add(@ApiParam("unionid") @RequestParam String unionid,
			@ApiParam("商品ID") @RequestParam Long goodsId,
			@ApiParam("颜色") @RequestParam String color,
			@ApiParam("尺码") @RequestParam String size,
			@ApiParam("使用日期,格式yyyy-MM-dd") @RequestParam String useDate) throws Exception{
		
		return phWardrobeService.add(unionid, goodsId, color, size, useDate);
	}
	
	@ApiOperation("衣柜列表")
	@GetMapping("/list")
	public JsonResult list(@ApiParam("unionid") @RequestParam String unionid){
		
		return jsonResultHelper.buildSuccessJsonResult(phWardrobeService.list(unionid));
	}
	
	@ApiOperation("确认订单-查询衣柜")
	@GetMapping("/listByIds")
	public JsonResult wardrobelist(@ApiParam("衣柜ID,多个用逗号隔开") @RequestParam String wardrobeIds){
		
		List<String> a =  Arrays.asList(wardrobeIds.split(","));
		List<Long> b = new ArrayList<>();
		for (String s : a) {
			b.add(Long.parseLong(s));
		}
		return jsonResultHelper.buildSuccessJsonResult(phWardrobeService.list(b));
	}
	
	@ApiOperation("删除")
	@PostMapping("/del")
	public JsonResult del(@ApiParam("衣柜ID") @RequestParam Long[] wardrobeIds) throws Exception{
		List<Long> wrIds = Arrays.asList(wardrobeIds);
		for (Long wrId : wrIds) {
			PhWardrobeAudit wardrobeAudit = phWardrobeAuditService.findByWardrobeId(wrId);
			if(null != wardrobeAudit){
				wardrobeAudit.setIsDel("1");
				phWardrobeAuditService.save(wardrobeAudit);
			}
			phWardrobeService.delete(wrId);
		}		return jsonResultHelper.buildSuccessJsonResult(null);
	}
	
	@ApiOperation("清除失效物件")
	@PostMapping("/delInvalid")
	public JsonResult delInvalid(@ApiParam("unionid") @RequestParam String unionid) throws Exception{
		phWardrobeService.delInvalid(unionid);
		return jsonResultHelper.buildSuccessJsonResult(null);
	}
	
	@ApiOperation("下订单")
	@PostMapping("/addOrder")
	public JsonResult addOrder(
			@ApiParam("unionid") @RequestParam String unionid,
			@ApiParam("衣柜ID") @RequestParam Long[] wardrobeIds,
			@ApiParam("地址ID") @RequestParam Long addrId,
			@ApiParam("使用者") @RequestParam String users){
		
		try {
			return phWardrobeService.addOrder(unionid,wardrobeIds, addrId, users);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("下订单异常，unionid:{},wardrobeIds:{},addrId:{},users:{}",unionid,wardrobeIds, addrId, users,e);
			if("减库存失败".equals(e.getMessage())){
				return jsonResultHelper.buildFailJsonResult(CommonResultCode.UPDATE_STOCK_ERROR);
			}else{
				return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
			}
		}
	}

	@ApiOperation("修改使用时间")
	@PostMapping("/deitByIds")
	public JsonResult deitByIds(@ApiParam("衣柜ID,多个用逗号隔开") @RequestParam String wardrobeIds,@ApiParam("使用日期,格式yyyy-MM-dd") @RequestParam String useDate) throws ParseException {
		List<String> a =  Arrays.asList(wardrobeIds.split(","));
		for (String s : a) {
			PhWardrobe wardrobe = phWardrobeService.findOne(Long.valueOf(s));
			wardrobe.setUseDate(DateUtils.parseDate(useDate, "yyyy-MM-dd"));
			phWardrobeService.save(wardrobe);
		}
		return jsonResultHelper.buildFailJsonResult(CommonResultCode.SUCCESS);
	}

	@ApiOperation("申请使用")
	@GetMapping("/audit")
	public JsonResult audit(@ApiParam("unionid") @RequestParam String unionid,
							@ApiParam("商品ID") @RequestParam Long goodsId,
							@ApiParam("颜色") @RequestParam String color,
							@ApiParam("尺码") @RequestParam String size,
							@ApiParam("使用日期,格式yyyy-MM-dd") @RequestParam String useDate,
							@ApiParam("使用艺人") @RequestParam String useName,
							@ApiParam("使用用途") @RequestParam String content,
							@ApiParam("归还时间") @RequestParam String returnDate,
							@ApiParam("返图时间") @RequestParam String photoDate,
							@ApiParam("表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id") @RequestParam(required = false, defaultValue = "") String formId) {
		try {
			phWardrobeAuditService.add(unionid, goodsId, color, size, useDate, useName, content, returnDate, photoDate, formId);
			PhGoods goods = goodsService.findOne(goodsId);
			PhBrand brand = brandService.findOne(goods.getBrandId());
			//短信通知商家
			if ("".equals(brand.getPhone())){
				smsService.sendMsgBatch("13524430033" , "【OFFWAY】您有一件OFFWAY SHOWROOM的借衣商品待审核，请及时进入后台查看。");
			}else {
				smsService.sendMsgBatch("13524430033," + brand.getPhone() , "【OFFWAY】您有一件OFFWAY SHOWROOM的借衣商品待审核，请及时进入后台查看。");
			}
			logger.info("短信通知商户发送手机号=13524430033,"+brand.getPhone());
			return jsonResultHelper.buildSuccessJsonResult(null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("短信通知商户异常unionid=" + unionid, e);
			return jsonResultHelper.buildFailJsonResult(CommonResultCode.SYSTEM_ERROR);
		}
	}

	@ApiOperation("查询状态")
	@PostMapping("/auditState")
	public JsonResult auditState(@ApiParam("unionid") @RequestParam String unionid,
								 @ApiParam("状态[0-待审核,1-审核通过,2-审核不通过]") @RequestParam String state){
		List<Object> list = new ArrayList<>();
		List<PhWardrobe> wardrobe = phWardrobeService.findState(unionid,state);
		for (PhWardrobe phWardrobe : wardrobe) {
			Map<String,Object> map = new HashMap<>();
			PhWardrobeAudit wardrobeAudit = phWardrobeAuditService.findByWardrobeId(phWardrobe.getId());
			map.put("returnDate", DateFormatUtils.format(wardrobeAudit.getReturnDate(), "yyyy-MM-dd"));
			map.put("useDate",DateFormatUtils.format(wardrobeAudit.getUseDate(), "yyyy-MM-dd"));
			map.put("useName",wardrobeAudit.getUseName());
			map.put("photoDate", DateFormatUtils.format(wardrobeAudit.getPhotoDate(), "yyyy-MM-dd"));
			map.put("content",wardrobeAudit.getContent());
			map.put("wardrobeData",phWardrobe);
			list.add(map);
		}
		return jsonResultHelper.buildSuccessJsonResult(list);
	}

	@ApiOperation("删除审核记录")
	@GetMapping("/delAudit")
	public JsonResult delAudit(@ApiParam("unionid") @RequestParam String unionid,
							   @ApiParam("衣柜ID") @RequestParam Long wardrobeId,
							   @ApiParam("状态[0-审核中,1-审核成功,2-审核失败]") @RequestParam String state){
		PhWardrobeAudit wardrobeAudit = phWardrobeAuditService.findByWardrobeId(wardrobeId);
		wardrobeAudit.setIsDel("1");
		phWardrobeAuditService.save(wardrobeAudit);
		phWardrobeService.delete(wardrobeId);
		return jsonResultHelper.buildSuccessJsonResult(null);
	}
}
