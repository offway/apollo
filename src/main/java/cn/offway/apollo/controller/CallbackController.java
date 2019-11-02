package cn.offway.apollo.controller;

import cn.offway.apollo.domain.*;
import cn.offway.apollo.properties.WxpayProperties;
import cn.offway.apollo.service.PhOrderService;
import cn.offway.apollo.service.PhReadcodeService;
import cn.offway.apollo.service.PhTemplateService;
import cn.offway.apollo.service.PhUserService;
import cn.offway.apollo.utils.JsonResultHelper;
import com.jpay.ext.kit.PaymentKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags = {"系统通知对外服务"})
@RestController
@RequestMapping("/callback")
public class CallbackController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhUserService userService;
    @Autowired
    private PhOrderService orderService;
    @Autowired
    private PhReadcodeService readcodeService;
    @Autowired
    private JsonResultHelper jsonResultHelper;
    @Autowired
    private WxpayProperties wxpayProperties;
    @Autowired
    private PhTemplateService phTemplateService;

    @ApiOperation("微信支付结果通知")
    @PostMapping("/wxpay")
    public String notify(@RequestBody String xmlMsg) {
        // 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
        try {
            System.out.println("支付通知=" + xmlMsg);
            Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
            String result_code = params.get("result_code");
            //校验返回来的支付结果,根据已经配置的密钥
            if (PaymentKit.verifyNotify(params, wxpayProperties.getPaternerKey())) {
                //校验通过. 更改订单状态为已支付
                String out_trade_no = params.get("out_trade_no"); //商户订单号
                /*
                 SUCCESS	交易支付成功
                 FAIL	失败
                 */
                if ("SUCCESS".equals(result_code)) {
                    PhOrder order = orderService.findOne(out_trade_no);
                    /* 状态[0-已下单,1-已付款,2-取消] **/
                    if (order != null && "0".equals(order.getStatus())) {
                        order.setStatus("1");
                        orderService.save(order);
                        //TODO 生成优惠券，写入排行榜
                        List<PhReadcode> codeList = new ArrayList<>();
                        for (int i = 0;i<Integer.parseInt(order.getSum());i++){
                            String uuid = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
                            PhReadcode code = new PhReadcode();
                            code.setBooksId(order.getTemplateId());
                            code.setState("0");
                            code.setCode(uuid);
                            code.setBuyersId(order.getUserId());
                            code.setCreateTime(new Date());
                            codeList.add(code);
                        }
                        readcodeService.save(codeList);
                        PhTemplate template = phTemplateService.findOne(order.getTemplateId());
                        template.setSubscribeSum(template.getSubscribeSum()+Integer.parseInt(order.getSum()));
                        phTemplateService.save(template);
                    }
                }
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            }
        } catch (Exception e) {
            logger.error("微信支付结果通知异常:" + xmlMsg, e);
        }
        return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[ERROR]]></return_msg></xml>";
    }
}
