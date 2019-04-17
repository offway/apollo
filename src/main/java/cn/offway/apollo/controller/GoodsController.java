package cn.offway.apollo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

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

import com.alibaba.fastjson.JSON;

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
	public Object classification(){
		return JSON.parse(phGoodsService.goodsConfig());
	}
	
	/*@ApiOperation("商品分类")
	@GetMapping("/classification")
	public JsonResult classification(){
		
		List<Object> categorysF = new ArrayList<>();
		List<Object> categorysM = new ArrayList<>();
		
		String[] sizeF = new String[]{"XS","S","M","L","均码"};
		String[] sizeM = new String[]{"M","L","XL","均码"};

		
		Map<String, Object> map = new HashMap<>();
		map.put("category", "T恤");
		map.put("size", sizeF);
		categorysF.add(map);
		
		Map<String, Object> map1 = new HashMap<>();
		map1.put("category", "衬衫");
		map1.put("size", sizeF);
		categorysF.add(map1);
		
		Map<String, Object> map2 = new HashMap<>();
		map2.put("category", "卫衣");
		map2.put("size", sizeF);
		categorysF.add(map2);
		
		Map<String, Object> map3 = new HashMap<>();
		map3.put("category", "夹克");
		map3.put("size", sizeF);
		categorysF.add(map3);
		
		
		
		Map<String, Object> map4 = new HashMap<>();
		map4.put("category", "外套");
		map4.put("size", sizeF);
		categorysF.add(map4);
		
		Map<String, Object> map411 = new HashMap<>();
		map411.put("category", "针织衫");
		map411.put("size", sizeF);
		categorysF.add(map411);
		
		Map<String, Object> a = new HashMap<>();
		a.put("category", "短裤");
		a.put("size", sizeF);
		categorysF.add(a);
		
		Map<String, Object> b = new HashMap<>();
		b.put("category", "长裤");
		b.put("size", sizeF);
		categorysF.add(b);
		
		Map<String, Object> c = new HashMap<>();
		c.put("category", "裙子");
		c.put("size", sizeF);
		categorysF.add(c);
		
		
		Map<String, Object> g = new HashMap<>();
		g.put("category", "T恤");
		g.put("size", sizeM);
		categorysM.add(g);
		
		Map<String, Object> h = new HashMap<>();
		h.put("category", "衬衫");
		h.put("size", sizeM);
		categorysM.add(h);
		
		Map<String, Object> i = new HashMap<>();
		i.put("category", "卫衣");
		i.put("size", sizeM);
		categorysM.add(i);
		
		Map<String, Object> j = new HashMap<>();
		j.put("category", "夹克");
		j.put("size", sizeM);
		categorysM.add(j);
		
		
		
		Map<String, Object> k = new HashMap<>();
		k.put("category", "外套");
		k.put("size", sizeF);
		categorysM.add(k);
		
		Map<String, Object> l = new HashMap<>();
		l.put("category", "针织衫");
		l.put("size", sizeM);
		categorysM.add(l);
		
		Map<String, Object> m = new HashMap<>();
		m.put("category", "短裤");
		m.put("size", sizeM);
		categorysM.add(m);
		
		Map<String, Object> n = new HashMap<>();
		n.put("category", "长裤");
		n.put("size", sizeM);
		categorysM.add(n);
		
		
		
		Map<String, Object> map71 = new HashMap<>();
		map71.put("category", "鞋");
		map71.put("size", new String[]{"36","37","38","39","40","41","42","43","44","45"});
		categorysM.add(map71);
		categorysF.add(map71);
		
		Map<String, Object> map61 = new HashMap<>();
		map61.put("category", "配饰");
		map61.put("size", new String[]{"均码"});
		categorysM.add(map61);
		categorysF.add(map61);
		
		
		Map<String, Object> map8 = new HashMap<>();
		map8.put("category", "未发售");
		map8.put("size", new String[]{});
		categorysM.add(map8);
		categorysF.add(map8);
		
		List<Object> list = new ArrayList<>();
		Map<String, Object> map9 = new HashMap<>();
		map9.put("type", "男装");
		map9.put("categorys", categorysM);
		list.add(map9);
		
		Map<String, Object> map10 = new HashMap<>();
		map10.put("type", "女装");
		map10.put("categorys", categorysF);
		list.add(map10);
		return jsonResultHelper.buildSuccessJsonResult(list);
	}*/
	
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
		
		List<String> sizeAll = new ArrayList<>();
		sizeAll.add("XS");
		sizeAll.add("S");
		sizeAll.add("M");
		sizeAll.add("L");
		sizeAll.add("XL");
		sizeAll.add("36");
		sizeAll.add("36.5");
		sizeAll.add("37");
		sizeAll.add("38");
		sizeAll.add("39");
		sizeAll.add("40");
		sizeAll.add("41");
		sizeAll.add("42");
		sizeAll.add("42.5");
		sizeAll.add("43");
		sizeAll.add("44");
		sizeAll.add("45");
		sizeAll.add("均码");
		
		sizeAll.removeIf(new Predicate<String>() {
		    @Override
		    public boolean test(String size) {
		        return !sizes.contains(size);
		    }
		});
		
		Map<String, Object> stock = new HashMap<>();
		stock.put("sizes", sizeAll);
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
