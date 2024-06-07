package com.linyang.ws.wsimport;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for QueryCurETwoResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="queryCurETwoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="queryCurETwo" type="{http://wsimport.ws.linyang.com/}DataBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryCurETwoResponse", propOrder = { "queryCurETwo" })
public class QueryCurETwoResponse {
	protected List<DataBean> queryCurETwo;

	public List<DataBean> getQueryCurETwo() {
		if (queryCurETwo == null) {
			queryCurETwo = new ArrayList<DataBean>();
		}
		return this.queryCurETwo;
	}

}
