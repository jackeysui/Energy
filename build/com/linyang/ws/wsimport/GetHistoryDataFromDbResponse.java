
package com.linyang.ws.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getHistoryDataFromDbResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getHistoryDataFromDbResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getHistoryDataFromDb" type="{http://wsimport.ws.linyang.com/}dataBean" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getHistoryDataFromDbResponse", propOrder = {
    "getHistoryDataFromDb"
})
public class GetHistoryDataFromDbResponse {

    protected List<DataBean> getHistoryDataFromDb;

    /**
     * Gets the value of the getHistoryDataFromDb property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the getHistoryDataFromDb property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGetHistoryDataFromDb().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataBean }
     * 
     * 
     */
    public List<DataBean> getGetHistoryDataFromDb() {
        if (getHistoryDataFromDb == null) {
            getHistoryDataFromDb = new ArrayList<DataBean>();
        }
        return this.getHistoryDataFromDb;
    }

}
