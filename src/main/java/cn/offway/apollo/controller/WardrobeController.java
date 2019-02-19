package cn.offway.apollo.controller;

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
	
	@ApiOperation("加入衣柜")
	@PostMapping("/add")
	public JsonResult add(@ApiParam("unionid") @RequestParam String unionid,
			@ApiParam("商品ID") @RequestParam Long goodsId,
			@ApiParam("颜色") @RequestParam String color,
			@ApiParam("尺码") @RequestParam String size,
			@ApiParam("使用日期,格式yyyy-MM-dd") @RequestParam String useDate) throws Exception{
		
		phWardrobeService.add(unionid, goodsId, color, size, useDate);
		return jsonResultHelper.buildSuccessJsonResult(null);
	}
	
	@ApiOperation("衣柜列表")
	@GetMapping("/list")
	public JsonResult list(@ApiParam("unionid") @RequestParam String unionid){
		
		return jsonResultHelper.buildSuccessJsonResult(phWardrobeService.list(unionid));
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
	public JsonResult addOrder(@ApiParam("衣柜ID") @RequestParam Long[] wardrobeIds,
			@ApiParam("地址ID") @RequestParam Long addrId,
			@ApiParam("使用者") @RequestParam String users) throws Exception{
		
		return phWardrobeService.addOrder(wardrobeIds, addrId, users);
	}
	
}
