package cn.offway.apollo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiOperation;

/**
 * 首页
 * @author wn
 *
 */
@Controller
public class IndexController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@ResponseBody
	@GetMapping("/")
	@ApiOperation(value = "欢迎页")
	public String index() {
		return "欢迎访问OFFWAY ShowRoom API服务";
	}
}
