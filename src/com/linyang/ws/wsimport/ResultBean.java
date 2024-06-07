
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resultBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resultBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="exceptionFlag" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="msg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultJsonStr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="statusEnum" type="{http://wsimport.ws.linyang.com/}statusEnum" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultBean", propOrder = {
    "exceptionFlag",
    "msg",
    "resultJsonStr",
    "statusCode",
    "statusEnum"
})
public class ResultBean {

    protected long exceptionFlag;
    protected String msg;
    protected String resultJsonStr;
    protected int statusCode;
    protected StatusEnum statusEnum;

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
     * Gets the value of the msg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets the value of the msg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsg(String value) {
        this.msg = value;
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
     * Gets the value of the statusEnum property.
     * 
     * @return
     *     possible object is
     *     {@link StatusEnum }
     *     
     */
    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    /**
     * Sets the value of the statusEnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusEnum }
     *     
     */
    public void setStatusEnum(StatusEnum value) {
        this.statusEnum = value;
    }

}
