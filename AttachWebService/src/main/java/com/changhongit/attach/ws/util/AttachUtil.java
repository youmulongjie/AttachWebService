package com.changhongit.attach.ws.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

/**
 * <p>
 * 描述：附件工具类
 * </p>
 * 
 * @author wanglongjie<br>
 * @version v1.0 2017年3月2日下午2:43:51
 */
public class AttachUtil {
	/**
	 * 上传文件保存根路径
	 */
	public static final String UPLOAD_PATH_ROOT = "d:\\UPLOAD_FILE_ROOT";
	/**
	 * 上传文件缓冲字节 1024K
	 */
	private static final int BUF_SIZE = 1024 * 1024;
	/**
	 * 上传文件最大限制 200M（需要在 {@link cxf-service.xml} 中设置
	 * <code>org.apache.cxf.stax.maxTextLength</code> 值保持一致）
	 */
	private static final int MAX_SIZE = 1024 * 1024 * 200;

	/**
	 * 初始化 上传根目录
	 */
	static {
		File file = new File(UPLOAD_PATH_ROOT);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	// 获取 年、月、日 文件夹目录
	private static String yearMonthDay() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return year + "/" + month + "/" + day;
	}

	/**
	 * 
	 * <p>
	 * 描述：按年、月、日 保存上传文件目录（本地）
	 * </p>
	 * 
	 * @Date 2017年3月2日下午4:12:36 <br>
	 * @param parent
	 *            本地根目录
	 * @return 年月日 目录
	 */
	public static File mkdirsYearMonthDay(String parent) {
		File file = new File(parent, yearMonthDay());
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 
	 * <p>
	 * 描述：按年、月、日 保存上传文件目录（FTP目录）
	 * </p>
	 * 
	 * @Date 2017年3月20日上午9:31:24 <br>
	 * @param parent
	 *            ftp根目录
	 * @return
	 * @throws Exception
	 */
	public static SmbFile mkdirsYearMonthDayFtp(String parent) throws Exception {
		SmbFile file = new SmbFile(parent, yearMonthDay());
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 
	 * <p>
	 * 描述：保存文件 到本地
	 * </p>
	 * 
	 * @Date 2017年3月2日下午3:07:26 <br>
	 * @param is
	 *            上传文件流
	 * @param fileName
	 *            上传文件名
	 * @throws Exception
	 */
	public static boolean writeFile(InputStream is, String fileName)
			throws Exception {
		Long begin = System.currentTimeMillis();
		System.out.println("本地上传开始...");

		if (null == is) {
			throw new Exception("上传文件异常：上传文件流为null");
		}

		if (null == fileName || fileName.trim().length() == 0) {
			throw new Exception("上传文件异常：上传文件名称为空");
		}

		int actually = is.available();
		if (actually >= MAX_SIZE) {
			throw new Exception("上传文件异常：上传文件为 " + actually / 1024 / 1024
					+ "M，超过上限200M");
		}

		OutputStream os = null;
		BufferedOutputStream bos = null;
		File dest = null;

		// 文件保存是否成功标识
		boolean saveFileFlag = true;
		try {
			// 保存文件
			dest = new File(mkdirsYearMonthDay(UPLOAD_PATH_ROOT), fileName);
			os = new FileOutputStream(dest);
			bos = new BufferedOutputStream(os);

			// 读写文件
			byte[] buffer = new byte[BUF_SIZE];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.flush();
		} catch (Exception e) {
			saveFileFlag = false;
			e.printStackTrace();
			throw new Exception("上传附件异常", e);
		} finally {
			close(bos, os, is);
			dest = null;
		}
		Long end = System.currentTimeMillis();
		System.out.println("本地上传结束，上传结果：" + saveFileFlag + "，共用时 "
				+ (end - begin) + "毫秒！");
		return saveFileFlag;
	}

	/**
	 * 
	 * <p>
	 * 描述：保存文件 到FTP
	 * </p>
	 * 
	 * @Date 2017年3月15日上午9:27:58 <br>
	 * @param is
	 *            上传文件流
	 * @param fileName
	 *            上传文件名
	 * @return
	 * @throws Exception
	 */
	public static boolean writeFtpFile(InputStream is, String ftpServer,
			String fileName) throws Exception {
		Long begin = System.currentTimeMillis();
		System.out.println("FTP上传开始...");

		if (null == is) {
			throw new Exception("上传文件异常：上传文件流为null");
		}

		if (null == fileName || fileName.trim().length() == 0) {
			throw new Exception("上传文件异常：上传文件名称为空");
		}

		int actually = is.available();
		if (actually >= MAX_SIZE) {
			throw new Exception("上传文件异常：上传文件为 " + actually / 1024 / 1024
					+ "M，超过上限200M");
		}
		boolean flag = true;

		OutputStream os = null;
		BufferedOutputStream bos = null;
		SmbFile smbFile = null;
		try {
			// 保存文件
			smbFile = new SmbFile(mkdirsYearMonthDayFtp(ftpServer) + "/"
					+ fileName);
			os = new BufferedOutputStream(new SmbFileOutputStream(smbFile));
			bos = new BufferedOutputStream(os);

			// 读写文件
			byte[] buffer = new byte[BUF_SIZE];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.flush();
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
			e.printStackTrace();
			throw new Exception("上传FTP文件失败", e);
		} finally {
			close(bos, os, is);
			smbFile = null;
		}
		Long end = System.currentTimeMillis();
		System.out.println("FTP上传结束，上传结果：" + flag + "，共用时 " + (end - begin)
				+ "毫秒！");
		return flag;
	}

	/**
	 * 
	 * <p>
	 * 描述：从FTP目录获取文件流（字节）
	 * </p>
	 * 
	 * @Date 2017年3月15日上午10:04:21 <br>
	 * @param remoteUrl
	 * @return
	 * @throws Exception
	 */
	public static byte[] smbGet(String remoteUrl) throws Exception {
		Long begin = System.currentTimeMillis();
		System.out.println("FTP下载开始...");

		InputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			SmbFile remoteFile = new SmbFile(remoteUrl);
			if (!remoteFile.exists()) {
				throw new Exception("ftp上没有找到要下载的文件路径");
			}
			
			in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUF_SIZE];
			while (in.read(buffer) != -1) {
				out.write(buffer);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(null, out, in);
		}
		Long end = System.currentTimeMillis();
		System.out.println("FTP下载结束，共用时 " + (end - begin) + "毫秒！");
		return out.toByteArray();
	}

	/**
	 * 
	 * <p>
	 * 描述：关闭 输入输出流 资源
	 * </p>
	 * 
	 * @Date 2017年3月2日下午3:11:39 <br>
	 * @param bos
	 *            缓冲输出流对象
	 * @param os
	 *            输出流对象
	 * @param is
	 *            输入流对象
	 * @throws Exception
	 */
	private static void close(BufferedOutputStream bos, OutputStream os,
			InputStream is) throws Exception {
		if (bos != null) {
			try {
				bos.close();
			} catch (Exception e) {
				throw new Exception("关闭缓冲流异常", e);
			}
		}
		if (os != null) {
			try {
				os.close();
			} catch (Exception e) {
				throw new Exception("关闭输出流异常", e);
			}
		}
		if (is != null) {
			try {
				is.close();
			} catch (Exception e) {
				throw new Exception("关闭输入流异常", e);
			}
		}
	}

}
