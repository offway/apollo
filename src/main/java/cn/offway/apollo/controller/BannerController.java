package cn.offway.apollo.controller;

import cn.offway.apollo.domain.PhBanner;
import cn.offway.apollo.service.PhBannerService;
import cn.offway.apollo.utils.JsonResult;
import cn.offway.apollo.utils.JsonResultHelper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/banner")
public class BannerController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JsonResultHelper jsonResultHelper;

    @Autowired
    private PhBannerService phBannerService;

    @ApiOperation("品牌跳转")
    @GetMapping("/bannerid")
    public JsonResult bannerid() {
        List<PhBanner> banners = phBannerService.findAll();
        Map<String, Object> bid = new HashMap<>();
        List<Object> maps = new ArrayList<>();
        for (PhBanner banner : banners) {
            Map<String, Object> map = new HashMap<>();
            map.put("images", banner.getBanner());
            map.put("id", banner.getRedirectIdl());
            maps.add(map);
        }
        bid.put("bannerid", maps);
        return jsonResultHelper.buildSuccessJsonResult(bid);
    }

}
