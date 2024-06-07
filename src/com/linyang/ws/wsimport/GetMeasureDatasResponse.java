
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getMeasureDatasResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getMeasureDatasResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getMeasureDatas" type="{http://wsimport.ws.linyang.com/}measureDataResultBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMeasureDatasResponse", propOrder = {
    "getMeasureDatas"
})
public class GetMeasureDatasResponse {

    protected MeasureDataResultBean getMeasureDatas;

    /**
     * Gets the value of the getMeasureDatas property.
     * 
     * @return
     *     possible object is
     *     {@link MeasureDataResultBean }
     *     
     */
    public MeasureDataResultBean getGetMeasureDatas() {
        return getMeasureDatas;
    }

    /**
     * Sets the value of the getMeasureDatas property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasureDataResultBean }
     *     
     */
    public void setGetMeasureDatas(MeasureDataResultBean value) {
        this.getMeasureDatas = value;
    }

}
