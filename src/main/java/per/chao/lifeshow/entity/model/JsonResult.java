package per.chao.lifeshow.entity.model;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/2/28 18:47
 **/
public class JsonResult {
	private boolean success;
	private String msg;
	private int code;
	private Object data;

	public JsonResult() {
	}

	public JsonResult(boolean success, String msg, int code, Object data) {
		this.success = success;
		this.msg = msg;
		this.code = code;
		this.data = data;
	}

	public JsonResult(boolean success, String msg) {
		this(success);
		this.msg = msg;
	}

	public JsonResult(boolean success) {
		this();
		this.success = success;
	}

	public JsonResult(boolean success, String msg, Object data) {
		this(success, msg);
		this.data = data;
	}

	public JsonResult(boolean success, String msg, int code) {
		this.success = success;
		this.msg = msg;
		this.code = code;
	}

	public static JsonResult okNonData() {
		return new JsonResult(true, "all is ok", 200);
	}

	public static JsonResult okNonData(String msg) {
		return new JsonResult(true, msg, 200);
	}

	public static JsonResult ok(Object data) {
		return new JsonResult(true, "all is ok", 200, data);
	}

	public static JsonResult error(String msg) {
		return new JsonResult(false, msg, 500, null);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public JsonResult put(String key, Object value) {
		if (data == null) {
			data = new HashMap<>();
		}
		((HashMap) data).put(key, value);
		return this;
	}

	public JsonResult putAll(Map<String, Object> m) {
		if (data == null) {
			data = new HashMap<>();
		}
		((HashMap) data).putAll(m);
		return this;
	}
}
