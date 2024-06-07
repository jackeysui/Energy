package com.linyang.ws.handler;

import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import com.esotericsoftware.minlog.Log;
import com.linyang.energy.utils.CipherUtil;
import com.linyang.energy.utils.WebConstant;

public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {
	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		try {
			String ns = "http://service.ws.linyang.com/";			
			Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (out) {
				SOAPMessage message = context.getMessage();
				SOAPEnvelope enve = message.getSOAPPart().getEnvelope();
				SOAPHeader header = enve.getHeader();
				if (header == null){
					header = enve.addHeader();
					QName qnameUsername = new QName(ns, "username", "authInfo");
					header.addHeaderElement(qnameUsername).setValue(WebConstant.wsUsername);
					QName qnamePawword = new QName(ns, "password", "authInfo");
					header.addHeaderElement(qnamePawword).setValue(CipherUtil.encodeBySHA256(WebConstant.wsPassword));
					message.saveChanges();  
				}
				message.writeTo(System.out);
			}
		} catch (SOAPException e) {
			Log.error(this.getClass() + ".handleMessage()--soap出错");
		} catch (IOException e) {
			Log.error(this.getClass() + ".handleMessage()--IO出错");
		}
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		/*System.out.println("error");*/Log.error("error");
		return false;
	}

	@Override
	public void close(MessageContext context) {

	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}
	
}