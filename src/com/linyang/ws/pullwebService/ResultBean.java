/**
 * ResultBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.linyang.ws.pullwebService;

public class ResultBean  implements java.io.Serializable {
    private long exceptionFlag;

    private java.lang.String msg;

    private java.lang.String resultJsonStr;

    private int statusCode;

    private com.linyang.ws.pullwebService.StatusEnum statusEnum;

    public ResultBean() {
    }

    public ResultBean(
           long exceptionFlag,
           java.lang.String msg,
           java.lang.String resultJsonStr,
           int statusCode,
           com.linyang.ws.pullwebService.StatusEnum statusEnum) {
           this.exceptionFlag = exceptionFlag;
           this.msg = msg;
           this.resultJsonStr = resultJsonStr;
           this.statusCode = statusCode;
           this.statusEnum = statusEnum;
    }


    /**
     * Gets the exceptionFlag value for this ResultBean.
     * 
     * @return exceptionFlag
     */
    public long getExceptionFlag() {
        return exceptionFlag;
    }


    /**
     * Sets the exceptionFlag value for this ResultBean.
     * 
     * @param exceptionFlag
     */
    public void setExceptionFlag(long exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }


    /**
     * Gets the msg value for this ResultBean.
     * 
     * @return msg
     */
    public java.lang.String getMsg() {
        return msg;
    }


    /**
     * Sets the msg value for this ResultBean.
     * 
     * @param msg
     */
    public void setMsg(java.lang.String msg) {
        this.msg = msg;
    }


    /**
     * Gets the resultJsonStr value for this ResultBean.
     * 
     * @return resultJsonStr
     */
    public java.lang.String getResultJsonStr() {
        return resultJsonStr;
    }


    /**
     * Sets the resultJsonStr value for this ResultBean.
     * 
     * @param resultJsonStr
     */
    public void setResultJsonStr(java.lang.String resultJsonStr) {
        this.resultJsonStr = resultJsonStr;
    }


    /**
     * Gets the statusCode value for this ResultBean.
     * 
     * @return statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }


    /**
     * Sets the statusCode value for this ResultBean.
     * 
     * @param statusCode
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    /**
     * Gets the statusEnum value for this ResultBean.
     * 
     * @return statusEnum
     */
    public com.linyang.ws.pullwebService.StatusEnum getStatusEnum() {
        return statusEnum;
    }


    /**
     * Sets the statusEnum value for this ResultBean.
     * 
     * @param statusEnum
     */
    public void setStatusEnum(com.linyang.ws.pullwebService.StatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResultBean)) return false;
        ResultBean other = (ResultBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.exceptionFlag == other.getExceptionFlag() &&
            ((this.msg==null && other.getMsg()==null) || 
             (this.msg!=null &&
              this.msg.equals(other.getMsg()))) &&
            ((this.resultJsonStr==null && other.getResultJsonStr()==null) || 
             (this.resultJsonStr!=null &&
              this.resultJsonStr.equals(other.getResultJsonStr()))) &&
            this.statusCode == other.getStatusCode() &&
            ((this.statusEnum==null && other.getStatusEnum()==null) || 
             (this.statusEnum!=null &&
              this.statusEnum.equals(other.getStatusEnum())));
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
        _hashCode += new Long(getExceptionFlag()).hashCode();
        if (getMsg() != null) {
            _hashCode += getMsg().hashCode();
        }
        if (getResultJsonStr() != null) {
            _hashCode += getResultJsonStr().hashCode();
        }
        _hashCode += getStatusCode();
        if (getStatusEnum() != null) {
            _hashCode += getStatusEnum().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResultBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://wsimport.ws.linyang.com/", "resultBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exceptionFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("", "exceptionFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "msg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultJsonStr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resultJsonStr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "statusCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("", "statusEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://wsimport.ws.linyang.com/", "statusEnum"));
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
