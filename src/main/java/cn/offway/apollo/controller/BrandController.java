package cn.offway.apollo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.offway.apollo.service.PhBrandService;
import cn.offway.apollo.utils.JsonResult;
import cn.offway.apollo.utils.JsonResultHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/brand")
public class BrandController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JsonResultHelper jsonResultHelper;
	
	@Autowired
	private PhBrandService phBrandService;
	
	
	@ApiOperation("品牌详情")
	@GetMapping("/info")
	public JsonResult info(@ApiParam("品牌ID") @RequestParam Long id){
		return jsonResultHelper.buildSuccessJsonResult(phBrandService.findOne(id));
	}

	@ApiOperation("全部品牌列表")
	@GetMapping("/list")
	public JsonResult list(){
		return jsonResultHelper.buildSuccessJsonResult(phBrandService.findAll());
	}
	
}
