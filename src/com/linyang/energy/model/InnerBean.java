package com.linyang.energy.model;

public class InnerBean
{
    /**
     * 数据采集项ID的分隔符
     */
    public static final String DATACFG_ITEMID_SPLITER = "#";
    
    /**
    * 数据类型：0-实时；1-日数据；2-月数据；3-年数据。
    */
    public static final int REAL_TYPE = 0;
    public static final int DAY_TYPE = 1;
    public static final int MON_TYPE = 2;
    public static final int YEAR_TYPE = 3;

    /**
     * 数据配置项ID集合，id之间，用上方的DATACFG_ITEMID_SPLITER，进行分割。
     */
    private String dataCfgItemIDs = null;

    /**
     * 数据类型，采用上方预定义的数据类型。
     */
    private int dataType = REAL_TYPE;
    
    /**
     * 起始时间
     */
    private long startTimeMS = -1;

    /**
     * 结束时间
     */
    private long endTimeMS = -1;

    public String getDataCfgItemIDs()
    {
        return dataCfgItemIDs;
    }

    public void setDataCfgItemIDs(String dataCfgItemIDs)
    {
        this.dataCfgItemIDs = dataCfgItemIDs;
    }

    public long getStartTimeMS()
    {
        return startTimeMS;
    }

    public void setStartTimeMS(long startTimeMS)
    {
        this.startTimeMS = startTimeMS;
    }

    public long getEndTimeMS()
    {
        return endTimeMS;
    }

    public void setEndTimeMS(long endTimeMS)
    {
        this.endTimeMS = endTimeMS;
    }

    public int getDataType()
    {
        return dataType;
    }

    public void setDataType(int dataType)
    {
        this.dataType = dataType;
    }
    
}
