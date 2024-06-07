
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sendMeasureParamsWithTimeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendMeasureParamsWithTimeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sendMeasureParamsWithTime" type="{http://wsimport.ws.linyang.com/}sendResultBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendMeasureParamsWithTimeResponse", propOrder = {
    "sendMeasureParamsWithTime"
})
public class SendMeasureParamsWithTimeResponse {

    protected SendResultBean sendMeasureParamsWithTime;

    /**
     * Gets the value of the sendMeasureParamsWithTime property.
     * 
     * @return
     *     possible object is
     *     {@link SendResultBean }
     *     
     */
    public SendResultBean getSendMeasureParamsWithTime() {
        return sendMeasureParamsWithTime;
    }

    /**
     * Sets the value of the sendMeasureParamsWithTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link SendResultBean }
     *     
     */
    public void setSendMeasureParamsWithTime(SendResultBean value) {
        this.sendMeasureParamsWithTime = value;
    }

}
