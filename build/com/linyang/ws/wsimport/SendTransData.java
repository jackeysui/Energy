
package com.linyang.ws.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sendTransData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendTransData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="terminalId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="dataItems" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="comPort" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="comControl" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="msgTimeout" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="byteTimeout" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendTransData", propOrder = {
    "terminalId",
    "dataItems",
    "comPort",
    "comControl",
    "msgTimeout",
    "byteTimeout",
    "content"
})
public class SendTransData {

    protected long terminalId;
    @XmlElement(type = Long.class)
    protected List<Long> dataItems;
    protected long comPort;
    protected long comControl;
    protected long msgTimeout;
    protected long byteTimeout;
    protected String content;

    /**
     * Gets the value of the terminalId property.
     * 
     */
    public long getTerminalId() {
        return terminalId;
    }

    /**
     * Sets the value of the terminalId property.
     * 
     */
    public void setTerminalId(long value) {
        this.terminalId = value;
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
     * Gets the value of the comPort property.
     * 
     */
    public long getComPort() {
        return comPort;
    }

    /**
     * Sets the value of the comPort property.
     * 
     */
    public void setComPort(long value) {
        this.comPort = value;
    }

    /**
     * Gets the value of the comControl property.
     * 
     */
    public long getComControl() {
        return comControl;
    }

    /**
     * Sets the value of the comControl property.
     * 
     */
    public void setComControl(long value) {
        this.comControl = value;
    }

    /**
     * Gets the value of the msgTimeout property.
     * 
     */
    public long getMsgTimeout() {
        return msgTimeout;
    }

    /**
     * Sets the value of the msgTimeout property.
     * 
     */
    public void setMsgTimeout(long value) {
        this.msgTimeout = value;
    }

    /**
     * Gets the value of the byteTimeout property.
     * 
     */
    public long getByteTimeout() {
        return byteTimeout;
    }

    /**
     * Sets the value of the byteTimeout property.
     * 
     */
    public void setByteTimeout(long value) {
        this.byteTimeout = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

}
