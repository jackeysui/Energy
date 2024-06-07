
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getDataServiceWithFormatTime2SecondResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getDataServiceWithFormatTime2SecondResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getDataServiceWithFormatTime2Second" type="{http://wsimport.ws.linyang.com/}resultBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDataServiceWithFormatTime2SecondResponse", propOrder = {
    "getDataServiceWithFormatTime2Second"
})
public class GetDataServiceWithFormatTime2SecondResponse {

    protected ResultBean getDataServiceWithFormatTime2Second;

    /**
     * Gets the value of the getDataServiceWithFormatTime2Second property.
     * 
     * @return
     *     possible object is
     *     {@link ResultBean }
     *     
     */
    public ResultBean getGetDataServiceWithFormatTime2Second() {
        return getDataServiceWithFormatTime2Second;
    }

    /**
     * Sets the value of the getDataServiceWithFormatTime2Second property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultBean }
     *     
     */
    public void setGetDataServiceWithFormatTime2Second(ResultBean value) {
        this.getDataServiceWithFormatTime2Second = value;
    }

}
