/**
 * <p>
 * 描述：
 * </p>

 * @package ：com.andy.demo.attach.bean<br>
 * @author ：wanglongjie<br>
 */
package com.changhongit.attach.ws.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 描述：上传附件Bean
 * </p>
 * 
 * @author wanglongjie<br>
 * @version v1.0 2017年3月2日下午4:16:18
 */
public class AttachBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AttachBean() {
		super();
	}

	/**
	 * @param originalName
	 */
	public AttachBean(String originalName) {
		super();
		this.originalName = originalName;
	}

	/**
	 * @param id
	 * @param originalName
	 * @param aliasName
	 */
	public AttachBean(long id, String originalName, String aliasName) {
		super();
		this.id = id;
		this.originalName = originalName;
		this.aliasName = aliasName;
	}

	/**
	 * 附件ID
	 */
	private long id;
	/**
	 * 附件原名称
	 */
	private String originalName;
	/**
	 * 附件别名（id + originalName），确保唯一
	 */
	private String aliasName;
	/**
	 * 上传 年
	 */
	private int year;
	/**
	 * 上传 月
	 */
	private int month;
	/**
	 * 上传 日
	 */
	private int day;
	/**
	 * 上传 路径
	 */
	private String path;
	/**
	 * 状态（1：可用；0：不可用）默认为1
	 */
	private int status;
	/**
	 * 上传时间
	 */
	private Date uploadDate;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the originalName
	 */
	public String getOriginalName() {
		return originalName;
	}

	/**
	 * @param originalName
	 *            the originalName to set
	 */
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	/**
	 * @return the aliasName
	 */
	public String getAliasName() {
		return aliasName;
	}

	/**
	 * @param aliasName
	 *            the aliasName to set
	 */
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param day
	 *            the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the uploadDate
	 */
	public Date getUploadDate() {
		return uploadDate;
	}

	/**
	 * @param uploadDate
	 *            the uploadDate to set
	 */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AttachBean [id=" + id + ", originalName=" + originalName
				+ ", aliasName=" + aliasName + ", year=" + year + ", month="
				+ month + ", day=" + day + ", path=" + path + ", status="
				+ status + ", uploadDate=" + uploadDate + "]";
	}

}
