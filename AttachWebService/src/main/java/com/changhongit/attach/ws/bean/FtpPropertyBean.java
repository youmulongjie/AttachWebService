package com.changhongit.attach.ws.bean;

/**
 * FTP配置信息BEAN
 * 
 * @author junxiang
 */
public class FtpPropertyBean {
	private String userName;

	private String password;

	private String ftpIp;

	private String savePath;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFtpIp() {
		return ftpIp;
	}

	public void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String toSmbPath() {
		return "smb://" + userName + ":" + password + "@" + ftpIp + "/"
				+ savePath;
	}
}
