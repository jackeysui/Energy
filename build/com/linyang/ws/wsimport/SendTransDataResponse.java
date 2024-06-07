
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sendTransDataResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendTransDataResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sendTransData" type="{http://wsimport.ws.linyang.com/}resultBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendTransDataResponse", propOrder = {
    "sendTransData"
})
public class SendTransDataResponse {

    protected ResultBean sendTransData;

    /**
     * Gets the value of the sendTransData property.
     * 
     * @return
     *     possible object is
     *     {@link ResultBean }
     *     
     */
    public ResultBean getSendTransData() {
        return sendTransData;
    }

    /**
     * Sets the value of the sendTransData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultBean }
     *     
     */
    public void setSendTransData(ResultBean value) {
        this.sendTransData = value;
    }

}
