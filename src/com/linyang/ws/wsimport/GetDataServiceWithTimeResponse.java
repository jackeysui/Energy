
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getDataServiceWithTimeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getDataServiceWithTimeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getDataServiceWithTimeResult" type="{http://wsimport.ws.linyang.com/}resultBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDataServiceWithTimeResponse", propOrder = {
    "getDataServiceWithTimeResult"
})
public class GetDataServiceWithTimeResponse {

    protected ResultBean getDataServiceWithTimeResult;

    /**
     * Gets the value of the getDataServiceWithTimeResult property.
     * 
     * @return
     *     possible object is
     *     {@link ResultBean }
     *     
     */
    public ResultBean getGetDataServiceWithTimeResult() {
        return getDataServiceWithTimeResult;
    }

    /**
     * Sets the value of the getDataServiceWithTimeResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultBean }
     *     
     */
    public void setGetDataServiceWithTimeResult(ResultBean value) {
        this.getDataServiceWithTimeResult = value;
    }

}
