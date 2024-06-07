
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getDataServiceSwitchResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getDataServiceSwitchResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getDataSwitchResult" type="{http://wsimport.ws.linyang.com/}resultBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDataServiceSwitchResponse", propOrder = {
    "getDataSwitchResult"
})
public class GetDataServiceSwitchResponse {

    protected ResultBean getDataSwitchResult;

    /**
     * Gets the value of the getDataSwitchResult property.
     * 
     * @return
     *     possible object is
     *     {@link ResultBean }
     *     
     */
    public ResultBean getGetDataSwitchResult() {
        return getDataSwitchResult;
    }

    /**
     * Sets the value of the getDataSwitchResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultBean }
     *     
     */
    public void setGetDataSwitchResult(ResultBean value) {
        this.getDataSwitchResult = value;
    }

}
