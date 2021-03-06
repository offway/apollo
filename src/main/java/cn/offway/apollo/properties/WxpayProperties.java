package cn.offway.apollo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wxpay")
public class WxpayProperties {

    /**
     * APPID即创建应用后生成
     **/
    private String appid;

    /**
     * 小程序ID
     **/
    private String miniAppid;

    /**
     * 商户号
     **/
    private String mchId;

    /**
     * 密钥
     **/
    private String paternerKey;

    /**
     * 是否沙箱环境
     **/
    private boolean isSandbox;


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPaternerKey() {
        return paternerKey;
    }

    public void setPaternerKey(String paternerKey) {
        this.paternerKey = paternerKey;
    }

    public String getMiniAppid() {
        return miniAppid;
    }

    public void setMiniAppid(String miniAppid) {
        this.miniAppid = miniAppid;
    }

    public boolean getIsSandbox() {
        return isSandbox;
    }

    public void setIsSandbox(boolean sandbox) {
        isSandbox = sandbox;
    }
}
