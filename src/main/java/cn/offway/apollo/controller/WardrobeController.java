package cn.offway.apollo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.offway.apollo.service.PhWardrobeService;
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
	public JsonResult del(@ApiParam("衣柜ID") @RequestParam Long wardrobeId) throws Exception{
		phWardrobeService.delete(wardrobeId);
		return jsonResultHelper.buildSuccessJsonResult(null);
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
			@ApiParam("使用者") @RequestParam String users) throws Exception{
		
		return phWardrobeService.addOrder(unionid,wardrobeIds, addrId, users);
	}
	
}
