/**
 * MeasureDataResultBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.linyang.ws.pullwebService;

public class MeasureDataResultBean  implements java.io.Serializable {
    private long exceptionFlag;

    private int levelFrameCount;

    private boolean measureEnd;

    private java.lang.String resultJsonStr;

    private int statusCode;

    private boolean timeOut;

    public MeasureDataResultBean() {
    }

    public MeasureDataResultBean(
           long exceptionFlag,
           int levelFrameCount,
           boolean measureEnd,
           java.lang.String resultJsonStr,
           int statusCode,
           boolean timeOut) {
           this.exceptionFlag = exceptionFlag;
           this.levelFrameCount = levelFrameCount;
           this.measureEnd = measureEnd;
           this.resultJsonStr = resultJsonStr;
           this.statusCode = statusCode;
           this.timeOut = timeOut;
    }


    /**
     * Gets the exceptionFlag value for this MeasureDataResultBean.
     * 
     * @return exceptionFlag
     */
    public long getExceptionFlag() {
        return exceptionFlag;
    }


    /**
     * Sets the exceptionFlag value for this MeasureDataResultBean.
     * 
     * @param exceptionFlag
     */
    public void setExceptionFlag(long exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }


    /**
     * Gets the levelFrameCount value for this MeasureDataResultBean.
     * 
     * @return levelFrameCount
     */
    public int getLevelFrameCount() {
        return levelFrameCount;
    }


    /**
     * Sets the levelFrameCount value for this MeasureDataResultBean.
     * 
     * @param levelFrameCount
     */
    public void setLevelFrameCount(int levelFrameCount) {
        this.levelFrameCount = levelFrameCount;
    }


    /**
     * Gets the measureEnd value for this MeasureDataResultBean.
     * 
     * @return measureEnd
     */
    public boolean isMeasureEnd() {
        return measureEnd;
    }


    /**
     * Sets the measureEnd value for this MeasureDataResultBean.
     * 
     * @param measureEnd
     */
    public void setMeasureEnd(boolean measureEnd) {
        this.measureEnd = measureEnd;
    }


    /**
     * Gets the resultJsonStr value for this MeasureDataResultBean.
     * 
     * @return resultJsonStr
     */
    public java.lang.String getResultJsonStr() {
        return resultJsonStr;
    }


    /**
     * Sets the resultJsonStr value for this MeasureDataResultBean.
     * 
     * @param resultJsonStr
     */
    public void setResultJsonStr(java.lang.String resultJsonStr) {
        this.resultJsonStr = resultJsonStr;
    }


    /**
     * Gets the statusCode value for this MeasureDataResultBean.
     * 
     * @return statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }


    /**
     * Sets the statusCode value for this MeasureDataResultBean.
     * 
     * @param statusCode
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    /**
     * Gets the timeOut value for this MeasureDataResultBean.
     * 
     * @return timeOut
     */
    public boolean isTimeOut() {
        return timeOut;
    }


    /**
     * Sets the timeOut value for this MeasureDataResultBean.
     * 
     * @param timeOut
     */
    public void setTimeOut(boolean timeOut) {
        this.timeOut = timeOut;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MeasureDataResultBean)) return false;
        MeasureDataResultBean other = (MeasureDataResultBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.exceptionFlag == other.getExceptionFlag() &&
            this.levelFrameCount == other.getLevelFrameCount() &&
            this.measureEnd == other.isMeasureEnd() &&
            ((this.resultJsonStr==null && other.getResultJsonStr()==null) || 
             (this.resultJsonStr!=null &&
              this.resultJsonStr.equals(other.getResultJsonStr()))) &&
            this.statusCode == other.getStatusCode() &&
            this.timeOut == other.isTimeOut();
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
        _hashCode += getLevelFrameCount();
        _hashCode += (isMeasureEnd() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getResultJsonStr() != null) {
            _hashCode += getResultJsonStr().hashCode();
        }
        _hashCode += getStatusCode();
        _hashCode += (isTimeOut() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MeasureDataResultBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://wsimport.ws.linyang.com/", "measureDataResultBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exceptionFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("", "exceptionFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("levelFrameCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "levelFrameCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("measureEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("", "measureEnd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("timeOut");
        elemField.setXmlName(new javax.xml.namespace.QName("", "timeOut"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
