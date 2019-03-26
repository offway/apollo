package cn.offway.apollo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.offway.apollo.domain.PhGoods;
import cn.offway.apollo.domain.PhGoodsStock;
import cn.offway.apollo.dto.GoodsDto;
import cn.offway.apollo.service.PhGoodsImageService;
import cn.offway.apollo.service.PhGoodsService;
import cn.offway.apollo.service.PhGoodsStockService;
import cn.offway.apollo.service.PhHolidayService;
import cn.offway.apollo.utils.JsonResult;
import cn.offway.apollo.utils.JsonResultHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/goods")
public class GoodsController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JsonResultHelper jsonResultHelper;
	
	@Autowired
	private PhGoodsService phGoodsService;
	
	@Autowired
	private PhGoodsImageService phGoodsImageService;
	
	@Autowired
	private PhGoodsStockService phGoodsStockService;
	
	@Autowired
	private PhHolidayService phHolidayService;
	
	
	@ApiOperation("商品分类")
	@GetMapping("/classification")
	public JsonResult classification(){
		
		List<Object> categorys = new ArrayList<>();
		
		String[] size = new String[]{"XL","S","M","L","均码"};
		
		Map<String, Object> map = new HashMap<>();
		map.put("category", "短袖");
		map.put("size", size);
		categorys.add(map);
		
		Map<String, Object> map1 = new HashMap<>();
		map1.put("category", "长袖");
		map1.put("size", size);
		categorys.add(map1);
		
		Map<String, Object> map2 = new HashMap<>();
		map2.put("category", "外套");
		map2.put("size", size);
		categorys.add(map2);
		
		Map<String, Object> map3 = new HashMap<>();
		map3.put("category", "短裤");
		map3.put("size", size);
		categorys.add(map3);
		
		Map<String, Object> map4 = new HashMap<>();
		map4.put("category", "长裤");
		map4.put("size", size);
		categorys.add(map4);
		
		Map<String, Object> map6 = new HashMap<>();
		map6.put("category", "配饰");
		map6.put("size", new String[]{"均码"});
		categorys.add(map6);
		
		Map<String, Object> map7 = new HashMap<>();
		map7.put("category", "鞋");
		map7.put("size", new String[]{"36","37","38","39","40","41","42","43","44","45"});
		categorys.add(map7);
		
		Map<String, Object> map8 = new HashMap<>();
		map8.put("category", "未发售");
		map8.put("size", new String[]{});
		categorys.add(map8); 
		
		List<Object> a = new ArrayList<>();
		Map<String, Object> map9 = new HashMap<>();
		map9.put("type", "男装");
		map9.put("categorys", categorys);
		a.add(map9);
		
		Map<String, Object> map10 = new HashMap<>();
		map10.put("type", "女装");
		map10.put("categorys", categorys);
		a.add(map10);
		return jsonResultHelper.buildSuccessJsonResult(a);
	}
	
	@ApiOperation("商品列表")
	@PostMapping("/list")
	public JsonResult list(@ApiParam("商品属性") @RequestBody GoodsDto goodsDto){
		
		int page = goodsDto.getPage();
		int size = goodsDto.getPageSize();
		Page<PhGoods> pages = phGoodsService.findByPage(goodsDto, new PageRequest(page, size));
		
		return jsonResultHelper.buildSuccessJsonResult(pages);
	}
	
	@ApiOperation("商品详情")
	@PostMapping("/info")
	public JsonResult info(@ApiParam("商品ID") @RequestParam Long id) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<>();
		PhGoods phGoods = phGoodsService.findOne(id);
		List<String> banners = phGoodsImageService.findByGoodsId(id,"0");
		List<String> contents = phGoodsImageService.findByGoodsId(id,"1");

		List<Map<String, Object>> list = new ArrayList<>();
		List<PhGoodsStock> phGoodsStocks = phGoodsStockService.findByGoodsId(id);
		Set<String> sizes = new HashSet<>();
		Set<String> colors = new HashSet<>();
		//Map<String, String> colorImg = new HashMap<>();
		for (PhGoodsStock phGoodsStock : phGoodsStocks) {
			Map<String, Object> map = new HashMap<>();
			map.put("size", phGoodsStock.getSize());
			map.put("color", phGoodsStock.getColor());
			map.put("stock", phGoodsStock.getStock());
			map.put("img", phGoodsStock.getImage());
			list.add(map);
			sizes.add(phGoodsStock.getSize());
			colors.add(phGoodsStock.getColor());
			
			//colorImg.put(phGoodsStock.getColor(), phGoodsStock.getImage());

		}
		
		//resultMap.put("colorImgs", colorImg);
		resultMap.put("goods", phGoods);
		resultMap.put("banners", banners);
		resultMap.put("contents", contents);
		
		Map<String, Object> stock = new HashMap<>();
		stock.put("sizes", sizes);
		stock.put("colors", colors);
		stock.put("details", list);
		resultMap.put("stock", stock);
		
		Date now = DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
		Date begin = null;
		if("1".equals(phGoods.getIsOffway())){
			begin = DateUtils.addDays(now, 3);
		}else{
			begin = phHolidayService.getNextWorkDay(now);
			begin = DateUtils.addDays(begin, 2);
		}
		Date end = DateUtils.addDays(begin, 4);
		resultMap.put("beginDate", begin);
		resultMap.put("endDate", end);
		
		return jsonResultHelper.buildSuccessJsonResult(resultMap);
	}
	
}
