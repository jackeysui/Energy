
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getDataServiceSwitch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getDataServiceSwitch">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="terminalId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="switchIntValue" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="isTurnOff" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDataServiceSwitch", propOrder = {
    "terminalId",
    "switchIntValue",
    "isTurnOff"
})
public class GetDataServiceSwitch {

    protected long terminalId;
    protected int switchIntValue;
    protected boolean isTurnOff;

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
     * Gets the value of the switchIntValue property.
     * 
     */
    public int getSwitchIntValue() {
        return switchIntValue;
    }

    /**
     * Sets the value of the switchIntValue property.
     * 
     */
    public void setSwitchIntValue(int value) {
        this.switchIntValue = value;
    }

    /**
     * Gets the value of the isTurnOff property.
     * 
     */
    public boolean isIsTurnOff() {
        return isTurnOff;
    }

    /**
     * Sets the value of the isTurnOff property.
     * 
     */
    public void setIsTurnOff(boolean value) {
        this.isTurnOff = value;
    }

}
