package cn.offway.apollo.controller;

import cn.offway.apollo.domain.PhGoodsCategory;
import cn.offway.apollo.domain.PhGoodsType;
import cn.offway.apollo.dto.GoodsTpyeDto;
import cn.offway.apollo.service.*;
import cn.offway.apollo.utils.JsonResult;
import cn.offway.apollo.utils.JsonResultHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/template")
public class TemplateController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JsonResultHelper jsonResultHelper;

    @Autowired
    private PhGoodsTypeService phGoodsTypeService;

    @Autowired
    private PhGoodsCategoryService phGoodsCategoryService;


    @ApiOperation("模板列表")
    @PostMapping("/list")
    public JsonResult list (@ApiParam("杂志id") @RequestParam Long id) {
		
        return jsonResultHelper.buildSuccessJsonResult("");
    }

}
