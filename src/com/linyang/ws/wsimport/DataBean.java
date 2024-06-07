
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for dataBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dataBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pointId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>	
 *         &lt;element name="dataTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="dataValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="dataValueA" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="dataValueB" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="dataValueC" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataBean", propOrder = {
	"pointId",
    "dataTime",
    "dataValue",
    "dataValueA",
    "dataValueB",
    "dataValueC"
})
public class DataBean {
	
	protected Long pointId;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataTime;
    protected Double dataValue;
    protected Double dataValueA;
    protected Double dataValueB;
    protected Double dataValueC;

    /**
     * Gets the value of the dataTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataTime() {
        return dataTime;
    }

    /**
     * Sets the value of the dataTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataTime(XMLGregorianCalendar value) {
        this.dataTime = value;
    }

    /**
     * Gets the value of the dataValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDataValue() {
        return dataValue;
    }

    /**
     * Sets the value of the dataValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDataValue(Double value) {
        this.dataValue = value;
    }

    /**
     * Gets the value of the dataValueA property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDataValueA() {
        return dataValueA;
    }

    /**
     * Sets the value of the dataValueA property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDataValueA(Double value) {
        this.dataValueA = value;
    }

    /**
     * Gets the value of the dataValueB property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDataValueB() {
        return dataValueB;
    }

    /**
     * Sets the value of the dataValueB property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDataValueB(Double value) {
        this.dataValueB = value;
    }

    /**
     * Gets the value of the dataValueC property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDataValueC() {
        return dataValueC;
    }

    /**
     * Sets the value of the dataValueC property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDataValueC(Double value) {
        this.dataValueC = value;
    }

	public Long getPointId() {
		return pointId;
	}

	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

}
