
package com.linyang.ws.wsimport;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for statusEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="statusEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SUCCESS"/>
 *     &lt;enumeration value="TIMEOUT"/>
 *     &lt;enumeration value="INVALID"/>
 *     &lt;enumeration value="FAILURE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "statusEnum")
@XmlEnum
public enum StatusEnum {

    SUCCESS,
    TIMEOUT,
    INVALID,
    FAILURE;

    public String value() {
        return name();
    }

    public static StatusEnum fromValue(String v) {
        return valueOf(v);
    }

}
