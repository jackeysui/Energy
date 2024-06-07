
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sendMeasureParamsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendMeasureParamsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sendMeasureParams" type="{http://wsimport.ws.linyang.com/}sendResultBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendMeasureParamsResponse", propOrder = {
    "sendMeasureParams"
})
public class SendMeasureParamsResponse {

    protected SendResultBean sendMeasureParams;

    /**
     * Gets the value of the sendMeasureParams property.
     * 
     * @return
     *     possible object is
     *     {@link SendResultBean }
     *     
     */
    public SendResultBean getSendMeasureParams() {
        return sendMeasureParams;
    }

    /**
     * Sets the value of the sendMeasureParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link SendResultBean }
     *     
     */
    public void setSendMeasureParams(SendResultBean value) {
        this.sendMeasureParams = value;
    }

}
