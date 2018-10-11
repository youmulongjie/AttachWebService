/**
 * <p>
 * 描述：
 * </p>

 * @package ：com.andy.demo.attach.handlers<br>
 * @author ：wanglongjie<br>
 */
package com.changhongit.attach.ws.handlers;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * <p>
 * 描述：演示访问整个SOAP消息的Handler的用法
 * </p>
 * 
 * @author wanglongjie<br>
 * @version v1.0 2017年3月6日上午10:03:12
 */
public class LogHandler implements SOAPHandler<SOAPMessageContext> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.
	 * MessageContext)
	 */
	// 
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		System.out.println("LogHandler:handleMessage(context)...");
		
		// 判断是出站消息还是入站消息
//		boolean outbound = (boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		boolean outbound = false;
		if(outbound){
			System.out.println("这是从Endpoint 返回到客户端的SOAP消息！");
		} else {
			System.out.println("这是客户端发送请求的SOAP消息！");
		}
		
		SOAPMessage message = context.getMessage();

		try {
			message.writeTo(System.out);
			System.out.println();
		} catch (Exception e) {
			System.err
					.println("Exception occured when invoke LogHandler:handleMessage(context)");
			e.printStackTrace();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext
	 * )
	 */
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		System.out.println("LogHandler:handleFault(context)...");
		SOAPMessage message = context.getMessage();

		try {
			message.writeTo(System.out);
			System.out.println();
		} catch (Exception e) {
			System.err
					.println("Exception occured when invoke LogHandler:handleFault(context)");
			e.printStackTrace();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
		System.out.println("LogHandler:close(context)...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
	 */
	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

}
