
package com.linyang.ws.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getDataServiceWithFormatTime2Second complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getDataServiceWithFormatTime2Second">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pointId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="dataItems" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="formatTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDataServiceWithFormatTime2Second", propOrder = {
    "pointId",
    "dataItems",
    "formatTime"
})
public class GetDataServiceWithFormatTime2Second {

    protected long pointId;
    @XmlElement(type = Long.class)
    protected List<Long> dataItems;
    protected String formatTime;

    /**
     * Gets the value of the pointId property.
     * 
     */
    public long getPointId() {
        return pointId;
    }

    /**
     * Sets the value of the pointId property.
     * 
     */
    public void setPointId(long value) {
        this.pointId = value;
    }

    /**
     * Gets the value of the dataItems property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataItems property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataItems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getDataItems() {
        if (dataItems == null) {
            dataItems = new ArrayList<Long>();
        }
        return this.dataItems;
    }

    /**
     * Gets the value of the formatTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormatTime() {
        return formatTime;
    }

    /**
     * Sets the value of the formatTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormatTime(String value) {
        this.formatTime = value;
    }

}
