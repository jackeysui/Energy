
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sendMeasureParamsWithFormatTimeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendMeasureParamsWithFormatTimeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sendMeasureParamsWithFormatTime2Second" type="{http://wsimport.ws.linyang.com/}sendResultBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendMeasureParamsWithFormatTimeResponse", propOrder = {
    "sendMeasureParamsWithFormatTime2Second"
})
public class SendMeasureParamsWithFormatTimeResponse {

    protected SendResultBean sendMeasureParamsWithFormatTime2Second;

    /**
     * Gets the value of the sendMeasureParamsWithFormatTime2Second property.
     * 
     * @return
     *     possible object is
     *     {@link SendResultBean }
     *     
     */
    public SendResultBean getSendMeasureParamsWithFormatTime2Second() {
        return sendMeasureParamsWithFormatTime2Second;
    }

    /**
     * Sets the value of the sendMeasureParamsWithFormatTime2Second property.
     * 
     * @param value
     *     allowed object is
     *     {@link SendResultBean }
     *     
     */
    public void setSendMeasureParamsWithFormatTime2Second(SendResultBean value) {
        this.sendMeasureParamsWithFormatTime2Second = value;
    }

}
