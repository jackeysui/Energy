
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getDataServiceResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getDataServiceResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getDataServiceResult" type="{http://wsimport.ws.linyang.com/}resultBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDataServiceResponse", propOrder = {
    "getDataServiceResult"
})
public class GetDataServiceResponse {

    protected ResultBean getDataServiceResult;

    /**
     * Gets the value of the getDataServiceResult property.
     * 
     * @return
     *     possible object is
     *     {@link ResultBean }
     *     
     */
    public ResultBean getGetDataServiceResult() {
        return getDataServiceResult;
    }

    /**
     * Sets the value of the getDataServiceResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultBean }
     *     
     */
    public void setGetDataServiceResult(ResultBean value) {
        this.getDataServiceResult = value;
    }

}
