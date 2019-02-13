package cn.offway.apollo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.offway.apollo.dto.sf.ReqAddOrder;
import cn.offway.apollo.service.SfExpressService;
import cn.offway.apollo.utils.JsonResult;
import io.swagger.annotations.ApiOperation;

/**
 * 首页
 * @author wn
 *
 */
@Controller
public class IndexController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SfExpressService sfExpressService;
	
	@ResponseBody
	@GetMapping("/")
	@ApiOperation(value = "欢迎页")
	public String index() {
		return "欢迎访问OFFWAY ShowRoom API服务";
	}
	
	@ResponseBody
	@GetMapping("/t")
	public JsonResult t() {
		ReqAddOrder addOrder = new ReqAddOrder();
		addOrder.setD_province("上海市");
		addOrder.setD_city("上海市");
		addOrder.setD_county("青浦区");
		addOrder.setD_address("华徐公路888号");
		addOrder.setD_contact("张三");
		addOrder.setD_tel("18666666666");
		addOrder.setJ_province("上海市");
		addOrder.setJ_city("上海市");
		addOrder.setJ_county("闵行区");
		addOrder.setJ_address("虹桥天地");
		addOrder.setJ_contact("李四");
		addOrder.setJ_tel("13333333333");
		addOrder.setOrder_source("OFFWAY");
		addOrder.setRemark("");
		addOrder.setSendstarttime("");
		
		return sfExpressService.addOrder(addOrder);
	}
}
