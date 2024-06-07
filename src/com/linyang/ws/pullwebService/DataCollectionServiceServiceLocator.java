/**
 * DataCollectionServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.linyang.ws.pullwebService;

import static com.linyang.energy.utils.WebConstant.getString;

public class DataCollectionServiceServiceLocator extends org.apache.axis.client.Service implements com.linyang.ws.pullwebService.DataCollectionServiceService {

    public DataCollectionServiceServiceLocator() {
    }


    public DataCollectionServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DataCollectionServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DataCollectionServicePort
    private java.lang.String DataCollectionServicePort_address = getString("ws_addr");

    public java.lang.String getDataCollectionServicePortAddress() {
        return DataCollectionServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DataCollectionServicePortWSDDServiceName = "DataCollectionServicePort";

    public java.lang.String getDataCollectionServicePortWSDDServiceName() {
        return DataCollectionServicePortWSDDServiceName;
    }

    public void setDataCollectionServicePortWSDDServiceName(java.lang.String name) {
        DataCollectionServicePortWSDDServiceName = name;
    }

    public com.linyang.ws.pullwebService.DataCollectionWebService getDataCollectionServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DataCollectionServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDataCollectionServicePort(endpoint);
    }

    public com.linyang.ws.pullwebService.DataCollectionWebService getDataCollectionServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.linyang.ws.pullwebService.DataCollectionServicePortBindingStub _stub = new com.linyang.ws.pullwebService.DataCollectionServicePortBindingStub(portAddress, this);
            _stub.setPortName(getDataCollectionServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDataCollectionServicePortEndpointAddress(java.lang.String address) {
        DataCollectionServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.linyang.ws.pullwebService.DataCollectionWebService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.linyang.ws.pullwebService.DataCollectionServicePortBindingStub _stub = new com.linyang.ws.pullwebService.DataCollectionServicePortBindingStub(new java.net.URL(DataCollectionServicePort_address), this);
                _stub.setPortName(getDataCollectionServicePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("DataCollectionServicePort".equals(inputPortName)) {
            return getDataCollectionServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.ws.linyang.com/", "DataCollectionServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.ws.linyang.com/", "DataCollectionServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DataCollectionServicePort".equals(portName)) {
            setDataCollectionServicePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
