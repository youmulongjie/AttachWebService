/**
 * <p>
 * 描述：
 * </p>

 * @package ：com.andy.demo.attach.bean<br>
 * @author ：wanglongjie<br>
 */
package com.changhongit.attach.ws.bean;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlMimeType;

/**
 * <p>
 * 描述：CXF上传和下载文件对象包装类 由于CXF的DataHandler无法获取文件名和文件类型，需要在上传和下载时附带文件名
 * </p>
 * 
 * @author wanglongjie<br>
 * @version v1.0 2017年3月2日上午9:32:19
 */
public class CxfFileWrapper {
	/**
	 * 文件名
	 */
	private String fileName = null;
	/**
	 * 文件二进制数据
	 */
	private DataHandler file = null;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the file
	 */
	// 注解该字段为二进制流
	@XmlMimeType("application/octet-stream")
	public DataHandler getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(DataHandler file) {
		this.file = file;
	}

}
