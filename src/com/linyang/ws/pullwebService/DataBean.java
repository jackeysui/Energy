/**
 * DataBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.linyang.ws.pullwebService;

public class DataBean  implements java.io.Serializable {
    private java.util.Calendar dataTime;

    private java.lang.Double dataValue;

    private java.lang.Double dataValueA;

    private java.lang.Double dataValueB;

    private java.lang.Double dataValueC;

    private java.lang.Long pointId;

    public DataBean() {
    }

    public DataBean(
           java.util.Calendar dataTime,
           java.lang.Double dataValue,
           java.lang.Double dataValueA,
           java.lang.Double dataValueB,
           java.lang.Double dataValueC,
           java.lang.Long pointId) {
           this.dataTime = dataTime;
           this.dataValue = dataValue;
           this.dataValueA = dataValueA;
           this.dataValueB = dataValueB;
           this.dataValueC = dataValueC;
           this.pointId = pointId;
    }


    /**
     * Gets the dataTime value for this DataBean.
     * 
     * @return dataTime
     */
    public java.util.Calendar getDataTime() {
        return dataTime;
    }


    /**
     * Sets the dataTime value for this DataBean.
     * 
     * @param dataTime
     */
    public void setDataTime(java.util.Calendar dataTime) {
        this.dataTime = dataTime;
    }


    /**
     * Gets the dataValue value for this DataBean.
     * 
     * @return dataValue
     */
    public java.lang.Double getDataValue() {
        return dataValue;
    }


    /**
     * Sets the dataValue value for this DataBean.
     * 
     * @param dataValue
     */
    public void setDataValue(java.lang.Double dataValue) {
        this.dataValue = dataValue;
    }


    /**
     * Gets the dataValueA value for this DataBean.
     * 
     * @return dataValueA
     */
    public java.lang.Double getDataValueA() {
        return dataValueA;
    }


    /**
     * Sets the dataValueA value for this DataBean.
     * 
     * @param dataValueA
     */
    public void setDataValueA(java.lang.Double dataValueA) {
        this.dataValueA = dataValueA;
    }


    /**
     * Gets the dataValueB value for this DataBean.
     * 
     * @return dataValueB
     */
    public java.lang.Double getDataValueB() {
        return dataValueB;
    }


    /**
     * Sets the dataValueB value for this DataBean.
     * 
     * @param dataValueB
     */
    public void setDataValueB(java.lang.Double dataValueB) {
        this.dataValueB = dataValueB;
    }


    /**
     * Gets the dataValueC value for this DataBean.
     * 
     * @return dataValueC
     */
    public java.lang.Double getDataValueC() {
        return dataValueC;
    }


    /**
     * Sets the dataValueC value for this DataBean.
     * 
     * @param dataValueC
     */
    public void setDataValueC(java.lang.Double dataValueC) {
        this.dataValueC = dataValueC;
    }


    /**
     * Gets the pointId value for this DataBean.
     * 
     * @return pointId
     */
    public java.lang.Long getPointId() {
        return pointId;
    }


    /**
     * Sets the pointId value for this DataBean.
     * 
     * @param pointId
     */
    public void setPointId(java.lang.Long pointId) {
        this.pointId = pointId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DataBean)) return false;
        DataBean other = (DataBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dataTime==null && other.getDataTime()==null) || 
             (this.dataTime!=null &&
              this.dataTime.equals(other.getDataTime()))) &&
            ((this.dataValue==null && other.getDataValue()==null) || 
             (this.dataValue!=null &&
              this.dataValue.equals(other.getDataValue()))) &&
            ((this.dataValueA==null && other.getDataValueA()==null) || 
             (this.dataValueA!=null &&
              this.dataValueA.equals(other.getDataValueA()))) &&
            ((this.dataValueB==null && other.getDataValueB()==null) || 
             (this.dataValueB!=null &&
              this.dataValueB.equals(other.getDataValueB()))) &&
            ((this.dataValueC==null && other.getDataValueC()==null) || 
             (this.dataValueC!=null &&
              this.dataValueC.equals(other.getDataValueC()))) &&
            ((this.pointId==null && other.getPointId()==null) || 
             (this.pointId!=null &&
              this.pointId.equals(other.getPointId())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDataTime() != null) {
            _hashCode += getDataTime().hashCode();
        }
        if (getDataValue() != null) {
            _hashCode += getDataValue().hashCode();
        }
        if (getDataValueA() != null) {
            _hashCode += getDataValueA().hashCode();
        }
        if (getDataValueB() != null) {
            _hashCode += getDataValueB().hashCode();
        }
        if (getDataValueC() != null) {
            _hashCode += getDataValueC().hashCode();
        }
        if (getPointId() != null) {
            _hashCode += getPointId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DataBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://wsimport.ws.linyang.com/", "dataBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataValueA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataValueA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataValueB");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataValueB"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataValueC");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataValueC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pointId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pointId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
