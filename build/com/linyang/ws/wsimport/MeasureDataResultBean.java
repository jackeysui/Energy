
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for measureDataResultBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="measureDataResultBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="exceptionFlag" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="levelFrameCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="measureEnd" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="resultJsonStr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="timeOut" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "measureDataResultBean", propOrder = {
    "exceptionFlag",
    "levelFrameCount",
    "measureEnd",
    "resultJsonStr",
    "statusCode",
    "timeOut"
})
public class MeasureDataResultBean {

    protected long exceptionFlag;
    protected int levelFrameCount;
    protected boolean measureEnd;
    protected String resultJsonStr;
    protected int statusCode;
    protected boolean timeOut;

    /**
     * Gets the value of the exceptionFlag property.
     * 
     */
    public long getExceptionFlag() {
        return exceptionFlag;
    }

    /**
     * Sets the value of the exceptionFlag property.
     * 
     */
    public void setExceptionFlag(long value) {
        this.exceptionFlag = value;
    }

    /**
     * Gets the value of the levelFrameCount property.
     * 
     */
    public int getLevelFrameCount() {
        return levelFrameCount;
    }

    /**
     * Sets the value of the levelFrameCount property.
     * 
     */
    public void setLevelFrameCount(int value) {
        this.levelFrameCount = value;
    }

    /**
     * Gets the value of the measureEnd property.
     * 
     */
    public boolean isMeasureEnd() {
        return measureEnd;
    }

    /**
     * Sets the value of the measureEnd property.
     * 
     */
    public void setMeasureEnd(boolean value) {
        this.measureEnd = value;
    }

    /**
     * Gets the value of the resultJsonStr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultJsonStr() {
        return resultJsonStr;
    }

    /**
     * Sets the value of the resultJsonStr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultJsonStr(String value) {
        this.resultJsonStr = value;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     */
    public void setStatusCode(int value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the timeOut property.
     * 
     */
    public boolean isTimeOut() {
        return timeOut;
    }

    /**
     * Sets the value of the timeOut property.
     * 
     */
    public void setTimeOut(boolean value) {
        this.timeOut = value;
    }

}
