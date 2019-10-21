package cn.offway.apollo.controller;

import cn.offway.apollo.domain.*;
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
    private PhTemplateService templateService;

    @Autowired
    private PhTemplateConfigService templateConfigService;

    @Autowired
    private PhLockService lockService;

    @Autowired
    private PhTemplate1Service template1Service;

    @Autowired
    private PhTemplate2Service template2Service;

    @Autowired
    private PhTemplate3Service template3Service;

    @Autowired
    private PhTemplate4Service template4Service;

    @Autowired
    private PhTemplate5Service template5Service;


    @ApiOperation("杂志列表")
    @GetMapping("/list")
    public JsonResult list (@ApiParam("杂志id") @RequestParam Long id) {
        PhTemplate template = templateService.findOne(id);
        List<PhTemplateConfig> templateConfigs = templateConfigService.findByGoodsId(template.getId());
        Map<String,Object> objectMap = new HashMap<>();
        List<Map<String,Object>> list = new ArrayList<>();
        for (PhTemplateConfig templateConfig : templateConfigs) {
            Map<String,Object> map = new HashMap<>();
            PhLock lock;
            switch (templateConfig.getName()){
                case "template1":
                    PhTemplate1 template1 = template1Service.findOne(templateConfig.getTemplateId());
                    lock = lockService.findByGoodsidAndTemplateTypeAndTemplateId(template.getId(),"0",template1.getId());
                    map.put("template1",template1);
                    map.put("lock",lock);
                    break;
                case "template2":
                    PhTemplate2 template2 = template2Service.findOne(templateConfig.getTemplateId());
                    lock = lockService.findByGoodsidAndTemplateTypeAndTemplateId(template.getId(),"1",template2.getId());
                    map.put("template2",template2);
                    map.put("lock",lock);
                    break;
                case "template3":
                    PhTemplate3 template3 = template3Service.findOne(templateConfig.getTemplateId());
                    lock = lockService.findByGoodsidAndTemplateTypeAndTemplateId(template.getId(),"2",template3.getId());
                    map.put("template3",template3);
                    map.put("lock",lock);
                    break;
                case "template4":
                    PhTemplate4 template4 = template4Service.findOne(templateConfig.getTemplateId());
                    lock = lockService.findByGoodsidAndTemplateTypeAndTemplateId(template.getId(),"4",template4.getId());
                    map.put("template4",template4);
                    map.put("lock",lock);
                    break;
                case "template5":
                    PhTemplate5 template5 = template5Service.findOne(templateConfig.getTemplateId());
                    lock = lockService.findByGoodsidAndTemplateTypeAndTemplateId(template.getId(),"5",template5.getId());
                    map.put("template5",template5);
                    map.put("lock",lock);
                    break;
                default:
                    break;
            }
            list.add(map);
        }
        return jsonResultHelper.buildSuccessJsonResult(list);
    }

}
