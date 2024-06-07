/**
 * DataCollectionWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.linyang.ws.pullwebService;

public interface DataCollectionWebService extends java.rmi.Remote {
    public com.linyang.ws.pullwebService.DataBean[] queryCurETwo(long[] pointIds, java.lang.Long startTime, java.lang.Long endTime) throws java.rmi.RemoteException;
    public java.lang.String getForwardActivePower(java.lang.String[] commAddresses, java.lang.String startTime, java.lang.String endTime) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.ResultBean getDataService(long pointId, long[] dataItems) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.SendResultBean sendMeasureParams(long pointId, long[] dataItems) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.ResultBean getSwitchMeasureData(long terminalId, long[] dataItems) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.ResultBean getDataServiceSwitch(long terminalId, int switchIntValue, boolean isTurnOff) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.DataBean[] getHistoryDataFromDb(long pointId, java.lang.Integer dataType, java.lang.Long startTime, java.lang.Long endTime) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.ResultBean sendTransData(long terminalId, long[] dataItems, long comPort, long comControl, long msgTimeout, long byteTimeout, java.lang.String content) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.ResultBean remoteSell(java.lang.String sellParamBean) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.ResultBean getDataServiceWithFormatTime2Second(long pointId, long[] dataItems, java.lang.String formatTime) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.SendResultBean sendMeasureParamsWithFormatTime(long pointId, long[] dataItems, java.lang.String formatTime) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.ResultBean remoteSwitch(java.lang.String userName, java.lang.String switchData) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.ResultBean getDataServiceWithTime(long pointId, long[] dataItems, java.lang.Long startTime) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.SendResultBean sendMeasureParamsWithTime(long pointId, long[] dataItems, java.lang.Long startTime) throws java.rmi.RemoteException;
    public com.linyang.ws.pullwebService.MeasureDataResultBean getMeasureDatas(long frameId) throws java.rmi.RemoteException;
}
