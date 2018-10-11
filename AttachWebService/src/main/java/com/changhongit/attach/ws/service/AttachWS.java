/**
 * <p>
 * 描述：
 * </p>

 * @package ：com.andy.demo.attach.service<br>
 * @author ：wanglongjie<br>
 */
package com.changhongit.attach.ws.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.changhongit.attach.ws.bean.CxfFileWrapper;

/**
 * <p>
 * 描述：附件接口
 * </p>
 * 
 * @author wanglongjie<br>
 * @version v1.0 2017年3月2日上午9:34:28
 */
@WebService(name = "AttachWS", serviceName = "AttachWS")
public interface AttachWS {
	/**
	 * 
	 * <p>
	 * 描述：查询所有
	 * </p>
	 * 
	 * @Date 2017年3月15日下午5:16:27 <br>
	 * @return
	 */
//	@WebMethod
//	String queryAll();

	/**
	 * 文件上传
	 * 
	 * @param file
	 *            文件上传包装类
	 * @return 上传成功返回true，上传失败返回false。
	 */
	@WebMethod
	long upload(
			@WebParam(name = "file", targetNamespace = "http://service.ws.attach.changhongit.com/") CxfFileWrapper file)
			throws Exception;

	/**
	 * 
	 * <p>
	 * 描述：文件下载
	 * </p>
	 * 
	 * @Date 2017年3月3日上午9:17:58 <br>
	 * @param attachId
	 *            附件ID
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	CxfFileWrapper download(
			@WebParam(name = "attachId", targetNamespace = "http://service.ws.attach.changhongit.com/") long attachId)
			throws Exception;

	/**
	 * 
	 * <p>
	 * 描述：文件删除（非物理删除）
	 * </p>
	 * 
	 * @Date 2017年3月2日下午5:41:37 <br>
	 * @param attachId
	 *            附件ID
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	boolean delete(
			@WebParam(name = "attachId", targetNamespace = "http://service.ws.attach.changhongit.com/") long attachId)
			throws Exception;
}
