/**
 * <p>
 * 描述：
 * </p>

 * @package ：com.andy.demo.attach.service.impl<br>
 * @author ：wanglongjie<br>
 */
package com.changhongit.attach.ws.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import com.changhongit.attach.ws.bean.AttachBean;
import com.changhongit.attach.ws.bean.CxfFileWrapper;
import com.changhongit.attach.ws.bean.FtpPropertyBean;
import com.changhongit.attach.ws.service.AttachWS;
import com.changhongit.attach.ws.util.AttachUtil;
import com.google.gson.Gson;
import com.sun.istack.ByteArrayDataSource;

/**
 * <p>
 * 描述：附件接口实现类：文件保存到FTP
 * </p>
 * 
 * @author wanglongjie<br>
 * @version v1.0 2017年3月15日上午9:35:21
 */
@WebService
@Component("attachFtpWS")
// 测试，正式发布时注释掉（为了看传输的SOAP消息是采取base64还是二进制流）
// @HandlerChain(file="/handler_chains.xml")
public class AttachFTPWSImpl implements AttachWS {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private FtpPropertyBean ftpPropertyBean;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.changhongit.attach.ws.service.AttachWS#upload(com.changhongit.attach
	 * .ws.bean.CxfFileWrapper)
	 */
	@Override
	public long upload(CxfFileWrapper file) throws Exception {
		// TODO 文件上传

		// 获取上传序列
		long sequence = querySequence();
		// 设置别名（保存别名，去除名称重复文件）
		String aliasName = sequence + "-" + file.getFileName();

		// 上传到FTP服务器
		boolean uploadFtpFlag = true;
		try {
			AttachUtil.writeFtpFile(file.getFile().getInputStream(),
					ftpPropertyBean.toSmbPath(), aliasName);
		} catch (Exception e) {
			uploadFtpFlag = false;
			throw new Exception(e);
		}

		long attachId = 0;
		if (uploadFtpFlag) {
			try {
				AttachBean attachBean = new AttachBean(sequence,
						file.getFileName(), aliasName);
				attachId = save(attachBean);
			} catch (Exception e) {
				throw new Exception(e);
			}
		} else {
			throw new Exception("文件保存失败");
		}
		return attachId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.changhongit.attach.ws.service.AttachWS#download(long)
	 */
	@Override
	public CxfFileWrapper download(long attachId) throws Exception {
		// TODO 文件下载
		AttachBean attachBean = query(attachId);
		if (null == attachBean) {
			throw new Exception("找不到id为【" + attachId + "】对应的文件");
		}

		CxfFileWrapper fileWrapper = new CxfFileWrapper();
		// 原附件名称
		fileWrapper.setFileName(attachBean.getOriginalName());
		// 附件二进制流

		byte[] buf = AttachUtil.smbGet(attachBean.getPath());
		DataSource source = new ByteArrayDataSource(buf,
				"application/octet-stream");
		fileWrapper.setFile(new DataHandler(source));

		return fileWrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.changhongit.attach.ws.service.AttachWS#delete(long)
	 */
	@Override
	public boolean delete(long attachId) throws Exception {
		// TODO 删除文件（非物理删除）
		String delete = "update cux.attach_upload t set t.status = 0 where t.id = ?";
		try {
			jdbcTemplate.update(delete, attachId);
			return true;
		} catch (Exception e) {
			throw new Exception("删除数据库失败", e);
		}
	}

	/******************************************************************************/
	// 查询索引
	private long querySequence() throws Exception {
		long querySequence = 1;
		try {
			String querySql = "select cux.attach_upload_seq.nextval num from dual t";
			querySequence = Long.parseLong(jdbcTemplate.queryForMap(querySql)
					.get("num").toString());
		} catch (Exception e) {
			throw new Exception("获取索引失败", e);
		}
		return querySequence;
	}

	// 保存
	private long save(AttachBean attachBean) throws Exception {
		// TODO 保存文件

		// 设置 年、月、日 路径
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		// 设置路径
		String path = ftpPropertyBean.toSmbPath() + "/" + year + "/" + month
				+ "/" + day + "/" + attachBean.getAliasName();

		String insert = "insert into cux.attach_upload(id, original_name, alias_name, year, month, day, path) values (?,?,?,?,?,?,?)";
		Object[] obj = new Object[] { attachBean.getId(),
				attachBean.getOriginalName(), attachBean.getAliasName(), year,
				month, day, path };

		try {
			jdbcTemplate.update(insert, obj);
		} catch (Exception e) {
			throw new Exception("插入数据库失败", e);
		}

		return attachBean.getId();
	}

	// 查询
	private AttachBean query(long id) throws Exception {
		// TODO 查询文件
		final AttachBean attachBean = new AttachBean();
		String find = "select * from cux.attach_upload where id = ? and status = 1";
		jdbcTemplate.query(find, new Object[] { id }, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				do {
					attachBean.setId(rs.getLong("ID"));
					attachBean.setOriginalName(rs.getString("ORIGINAL_NAME"));
					attachBean.setAliasName(rs.getString("ALIAS_NAME"));
					attachBean.setYear(rs.getInt("YEAR"));
					attachBean.setMonth(rs.getInt("MONTH"));
					attachBean.setDay(rs.getInt("DAY"));
					attachBean.setPath(rs.getString("PATH"));
				} while (rs.next());
			}
		});
		return attachBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.changhongit.attach.ws.service.AttachWS#queryAll()
	 */
//	@Override
	public String queryAll() {
		// TODO Auto-generated method stub
		final List<AttachBean> list = new ArrayList<AttachBean>();
		String find = "select * from cux.attach_upload where status = 1 order by id desc";
		jdbcTemplate.query(find, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				do {
					AttachBean attachBean = new AttachBean();

					attachBean.setId(rs.getLong("ID"));
					attachBean.setOriginalName(rs.getString("ORIGINAL_NAME"));
					attachBean.setAliasName(rs.getString("ALIAS_NAME"));
					attachBean.setYear(rs.getInt("YEAR"));
					attachBean.setMonth(rs.getInt("MONTH"));
					attachBean.setDay(rs.getInt("DAY"));
					attachBean.setPath(rs.getString("PATH"));
					attachBean.setUploadDate(rs.getTimestamp("UPLOAD_DATE"));

					list.add(attachBean);
				} while (rs.next());
			}
		});
		Gson gson = new Gson();

		return gson.toJson(list);
	}

}
