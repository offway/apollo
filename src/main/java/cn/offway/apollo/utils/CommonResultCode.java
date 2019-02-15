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