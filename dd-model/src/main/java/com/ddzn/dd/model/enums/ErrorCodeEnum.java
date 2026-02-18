package com.ddzn.dd.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码
 *
 * @author jmking
 */
@Getter
public enum ErrorCodeEnum {

    /**
     * 未知错误
     */
    UNKNOWN_ERROR("-999", "未知错误"),
    OPERATION_ERROR("1000", "操作错误"),

    /**
     * token
     */
    UN_LOGIN("2000", "未登录"),
    REFRESH_FAILURE("2001", "刷新错误"),
    UNAUTHORIZED("2002", "未授权", 401),
    VERIFY_ERROR("2003", "验证失败"),
    DENIED("2004", "拒绝访问", 401),

    /**
     * 通用
     */
    MEMBER_INFO_ERROR("3001", "用户信息有误"),
    TRY_LOCK_ERROR("3002", "系统繁忙，请稍后再试~"),
    COMPANY_INFO_ERROR("3003", "运营商信息有误"),
    DATA_FORMAT_ERROR("3004", "时间格式化有误"),
    USER_DOES_NOT_EXIST("3005", "用户不存在"),
    PARAMETER_EXCEPTION("3006", "参数异常"),
    ROLE_NAME("3007", "角色名称重复"),
    RECOURSE_NULL("3008", "资源ID有误"),
    SYS_ADMIN_DEL_ERROR("3007", "系统管理员无法删除"),
    SYS_ADMIN_DISABLE_ERROR("3008", "管理员禁止禁用"),
    USER_TREATMENT_PROJECT("3009", "请先移除该用户的治疗动作"),
    USER_SCHEDULE("3010", "用户有排班信息，不可禁用"),
    USER_DISABLED("3011", "用户已被禁用"),
    USER_INVOKE("3012", "用户绑定的科室仍在禁用中"),
    USER_DEPART_DISABLED("3013", "用户绑定的科室仍在禁用中"),
    PHONE_ERROR("3014", "请输入正确的电话号码"),
    USER_TITLE("3015", "职称不能为空"),
    USER_RANK("3016", "职级不能为空"),
    PHONE_REPEAT("3017", "电话号码重复"),
    ROLE_DEL_ERROR("3018", "角色仍有用户绑定，无法删除"),
    USER_NULL("3019", "用户不存在，请确认输入是否正确！"),
    LOGIN_ERROR("3020", "账号无法登录"),
    USER_DISABLE("3021", "账号已被禁用！"),
    PWD_ERROR("3022", "密码不正确，请检查密码！"),
    PWD_REPEAT("3023", "新旧密码不得相同"),
    PWD_CHANGE_ERROR("3025", "密码修改出现异常"),
    ROLE_NULL("3026", "该身份不存在"),
    USER_ADD_ERROR("3027", "该身份不存在"),
    USER_ROLE_NOT_NULL("3028", "角色权限不能为空"),
    ROLE_DISABLE("3030", "角色已经禁用"),
    WECHAT_ACCESS_TOKEN("3031", "微信 access_token 获取异常"),
    SCENE_TO_LONG("3032", "微信二维码链接参数 scene 超长"),
    RESOURCE_EMPTY("3033", "无可用菜单"),
    USER_CANNOT_UPDATE_HIMSELF("3034", "禁止修改自身账号"),
    USER_ACCOUNT("3035", "账号重复"),
    COMMENT_NOT_EXIST("3036", "评论不存在"),
    SECOND_COMMENT_CAN_NOT_HIDE("3037", "二级评论不能设置隐藏"),
    ANSWER_NOT_EXIST("3038", "回答不存在"),


    /**
     * 菜单资源
     */
    PARENT_IS_NULL("4001", "父级资源不存在"),
    PERM_IS_REPEAT("4002", "资源权限标识重复"),
    RESOURCE_ID_IS_NULL("4003", "资源Id不能为空"),
    RESOURCE_HAVEN_CHILD("4004", "资源存在子级不能被删除"),
    ROOT_CANNOT_UPDATE("4005", "根目录不能被修改"),


    /**
     * 角色
     */
    ROLE_KEY_IS_REPEAT("5001", "角色标识重复"),
    ROLE_RESOURCES_ERROR("5002", "角色关联资源异常"),
    ROLE_HAVEN_USERS("5003", "角色已经配置用户，不能被删除"),
    ROLE_IS_NULL("5004", "角色不存在"),
    ROLE_ID_IS_NULL("5005", "角色ID不能为空"),
    ROLE_NAME_IS_REPEAT("5006", "角色名称重复"),

    /**
     * 社区
     */
    CONTENT_DICT_NULL("6001", "内容分类不能为空"),
    HELP_TYPE_NULL("6002", "帮助类型不存在"),
    DYNAMIC_NULL("6003", "动态不存在"),
    COMMENT_NULL("6004", "评论不存在"),
    DYNAMIC_IS_DELETED("6005", "动态已经被删除"),
    DYNAMIC_NO_PERMISSION("6006", "不能操作非官方用户的动态"),
    TOPIC_NULL("6007", "话题不存在"),

    /**
     * 文件
     */
    FILE_UPLOAD_FAIL("14001", "图片上传失败"),
    FILE_NOT_EXIST("14002", "文件不存在"),
    FILE_OVERSIZE("14003", "文件过大"),
    FILE_UNSUPPORTED("14004", "目前不支持此图片格式");

    private String errCode;
    private String errMsg;
    private int httpStatus;

    // 手动定义双参构造器（对应大部分无httpStatus的枚举常量）
    ErrorCodeEnum(String errCode, String errMsg) {
        this(errCode, errMsg, 500); // 调用三参构造器，默认httpStatus为500
    }

    // 手动定义三参构造器（对应需要指定httpStatus的枚举常量，如UNAUTHORIZED、DENIED）
    ErrorCodeEnum(String errCode, String errMsg, int httpStatus) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.httpStatus = httpStatus;
    }
}
