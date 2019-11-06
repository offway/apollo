package cn.offway.apollo.utils;

/**
 * 通用结果枚举
 * @author wn
 *
 */
public enum CommonResultCode implements ResultCode {

    /** 成功 */
    SUCCESS("200", "SUCCESS"),

    /** 系统错误 */
    SYSTEM_ERROR("1000", "SYSTEM_ERROR"),
    
    /** 请求参数不完整 */
    PARAM_MISS("1001", "PARAM_MISS"),
    
    /** 请求参数错误 */
    PARAM_ERROR("1002", "PARAM_ERROR"),
    
    /** 邀请码无效 */
    CODE_ERROR("1003", "CODE_ERROR"),
    
    /** 活动已参加 */
    ACTIVITY_PARTICIPATED("1004", "ACTIVITY_PARTICIPATED"),
    
    /** 活动已上限 */
    ACTIVITY_LIMIT("1005", "ACTIVITY_LIMIT"),
    
    /** 用户不存在 */
    USER_NOT_EXISTS("1006", "USER_NOT_EXISTS"),
    
    /** 中奖信息不存在 */
    PRIZE_NOT_EXISTS("1007", "PRIZE_NOT_EXISTS"),
    
    /** 该订单已寄回  */
    ORDER_BACK("1008", "ORDER_BACK"),
    
    /** 衣柜调价衣物以至8件上限  */
    WARDROBE_LIMIT("1009", "WARDROBE_LIMIT"),
   
    /** 您的信用分太低，不能再借衣服  */
    CREDITSCORE_LESS("1010", "CREDITSCORE_LESS"),
    
    /** 您有一批订单反馈图未上传，上传后即可借衣  */
    NO_SHOW_IMAGE("1011", "NO_SHOW_IMAGE"),
    
    /** 有一笔订单未归还  */
    NO_RETURN_IMAGE("1012", "NO_RETURN_IMAGE"),
    
    /** 修改库存失败  */
    UPDATE_STOCK_ERROR("1013", "UPDATE_STOCK_ERROR"),

    /** 修改使用时间失败  */
    UPDATE_DATE_ERROR("1014", "UPDATE_DATE_ERROR"),

    /** 暂无可使用阅读码  */
    USER_CODE_ERROR("1015", "USER_CODE_ERROR"),

    /** 无阅读权限  */
    USER_PERMISSIONS_ERROR("1016", "USER_PERMISSIONS_ERROR"),

    ;
	
	
    private String errorCode;

    private String statusCode;

    CommonResultCode(String statusCode, String errorCode) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getStatusCode() {
        return statusCode;
    }
}