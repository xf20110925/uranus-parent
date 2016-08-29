package com.ptb.uranus.asistant.web.model;

/**
 * Created by eric on 16/8/29.
 */
public class RWbAcctMeta {
	String username;
	String password;
	String volidateCode;
	String cookie;
	boolean isValid;

	public RWbAcctMeta(String username, String password) {
		this.username = username;
		this.password = password;
		volidateCode = "";
		cookie = "";
		isValid = false;
	}

	public RWbAcctMeta() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVolidateCode() {
		return volidateCode;
	}

	public void setVolidateCode(String volidateCode) {
		this.volidateCode = volidateCode;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean valid) {
		isValid = valid;
	}
}
