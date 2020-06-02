package com.yango.common.web.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ResultVo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SUCCESS_FLAG = "0";
	public static final String FAIL_FLAG = "1";

	public static final String OK_CODE = "0000";// 成功提交
	public static final String EX_CODE = "0001";// 请求失败(前端提示错误消息,但是不做其他操作)
	public static final String UNAUTH_CODE = "0002";// 权限不足code
	
	public static final String HAS_NO_TOKEN = "0003";// token不存在(jwt专用)
	
	// uc
	public static final String UC_CODE_UNKNOWN_ACCOUNT = "1001";// 账户不存在
	public static final String UC_CODE_INCORRECT_CREDENTIALS = "1002";// 用户名密码不匹配
	public static final String UC_CODE_LOCKED_ACCOUNT = "1003";// 账户已经被锁定
	public static final String UC_CODE_DISABLED_ACCOUNT = "1004";// 账户已经被注销

	private String flag; // 0:请求成功,1:请求异常
	private String code;// 0000:操作成功,其它失败 具体自定义
	private String message;// 消息
	private Object attachment;// 备用内容

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getAttachment() {
		return attachment;
	}

	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

	private ResultVo(String flag, String code) {
		this(flag, code, null);
	}

	private ResultVo(String flag, String code, String message) {
		this(flag, code, message, null);
	}

	private ResultVo(String flag, String code, String message, Object attachment) {
		this.flag = flag;
		this.code = code;
		this.message = message;
		this.attachment = attachment;
	}
	
	@JsonIgnore
	public boolean isSuccess() {
		return SUCCESS_FLAG.equals(flag);
	}
	
	/**
	 * 自定义code时 使用,默认请求成功
	 * 
	 * @param code
	 * @param message
	 * @return
	 */
	public static ResultVo val(String code, String message) {
		return val(code, message, null);
	}

	/**
	 * 自定义code时 使用,默认请求成功
	 * 
	 * @param code
	 * @param message
	 * @param attachment
	 * @return
	 */
	public static ResultVo val(String code, String message, Object attachment) {
		return new ResultVo(SUCCESS_FLAG, code, message, attachment);
	}

	/**
	 * 请求成功
	 * 
	 * @return
	 */
	public static ResultVo ok() {
		return ok(null);
	}

	/**
	 * 请求成功
	 * 
	 * @param attachment
	 *            返回对象
	 * @return
	 */
	public static ResultVo ok(Object attachment) {
		return new ResultVo(SUCCESS_FLAG, OK_CODE, "请求成功", attachment);
	}

	/**
	 * 请求失败
	 * 
	 * @param message
	 *            错误消息
	 * @return
	 */
	public static ResultVo fail(String message) {
		return new ResultVo(SUCCESS_FLAG, EX_CODE, message);
	}
	
	
	/**
	 * 请求异常
	 * 
	 * @return
	 */
	public static ResultVo ex() {
		return new ResultVo(FAIL_FLAG, EX_CODE, "请求异常");
	}

	/**
	 * 请求权限不足
	 * 
	 * @return
	 */
	public static ResultVo unAuth() {
		return new ResultVo(SUCCESS_FLAG, UNAUTH_CODE, "请求权限不足");
	}
	
}
