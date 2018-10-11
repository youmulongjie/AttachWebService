/**
 * <p>
 * 描述：
 * </p>

 * @package ：com.andy.demo.test<br>
 * @author ：wanglongjie<br>
 */
package com.andy.demo.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.changhongit.attach.ws.bean.CxfFileWrapper;
import com.changhongit.attach.ws.service.AttachWS;

/**
 * <p>
 * 描述：测试
 * </p>
 * 
 * @author wanglongjie<br>
 * @version v1.0 2017年3月2日上午10:49:15
 */
public class AttachTest {

	/**
	 * 测试上传
	 */
	@SuppressWarnings("resource")
	// @Test
	public void springClientUpload() throws Exception {
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"cxf-client.xml");
			AttachWS attachWS = context.getBean("attachWS", AttachWS.class);
			// d盘根目录下文件
			String name = "demo.zip";
			System.out.println("name = " + name);
			long a = System.currentTimeMillis();
			CxfFileWrapper fileWrapper = new CxfFileWrapper();
			fileWrapper.setFileName(name);
			DataSource ds = new FileDataSource(new File("d:\\" + name));

			long size = new FileInputStream(new File("d:\\" + name))
					.available();
			System.out.println("size = " + size + " 字节, 共" + size / 1024 / 1024
					+ "M");
			fileWrapper.setFile(new DataHandler(ds));
			long res = attachWS.upload(fileWrapper);
			System.out.println("res = " + res);

			long b = System.currentTimeMillis();
			System.out.println((b - a) / 1000 + "秒");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 测试下载
	 */
	@SuppressWarnings("resource")
	// @Test
	public void springClientDown() throws Exception {
		try {
			long a = System.currentTimeMillis();
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"cxf-client.xml");
			AttachWS attachWS = context.getBean("attachWS", AttachWS.class);

			// 附件ID
			long id = 47;
			CxfFileWrapper cxfFileWrapper = attachWS.download(id);

			String name = cxfFileWrapper.getFileName();
			System.out.println("文件名：" + name);

			DataHandler dataHandler = cxfFileWrapper.getFile();
			DataSource ds = dataHandler.getDataSource();
			InputStream is = ds.getInputStream();

			// 下载文件保存到e盘
			OutputStream os = new FileOutputStream(new File("e:\\" + name));
			int BUFFER_SIZE = 1024 * 100;
			byte[] by = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = is.read(by, 0, BUFFER_SIZE)) != -1) {
				os.write(by, 0, len);
			}

			os.flush();
			os.close();
			is.close();
			ds = null;

			long b = System.currentTimeMillis();
			System.out.println((b - a) / 1000 + "秒");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 测试 删除
	 */
	@SuppressWarnings("resource")
	// @Test
	public void springClientDelete() throws Exception {
		try {
			long a = System.currentTimeMillis();
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"cxf-client.xml");
			AttachWS attachWS = context.getBean("attachWS", AttachWS.class);
			attachWS.delete(3l);
			long b = System.currentTimeMillis();
			System.out.println((b - a) / 1000 + "秒");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
