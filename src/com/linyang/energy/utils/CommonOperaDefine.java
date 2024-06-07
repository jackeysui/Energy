package com.linyang.energy.utils;

/**
 * @类功能说明：存放操作类型静态字典和模块静态字典的类，用来记录日志必填字段的
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-12 下午02:41:11  
 * @版本：V1.0
 */
public class CommonOperaDefine {
	
	/**
	 * 新增
	 */
	public static final int LOG_TYPE_INSERT = 1;
	
	/**
	 * 编辑
	 */
	public static final int LOG_TYPE_UPDATE = 2;
	
	/**
	 * 删除
	 */
	public static final int LOG_TYPE_DELETE = 3;


	
	
	/*========================操作结果================================*/
	/**
	 * 操作成功
	 */
	public static final int OPRATOR_SUCCESS = 1;
	
	/**
	 * 操作失败
	 */
	public static final int OPRATOR_FAIL = 2;
	
	
	/*========================操作对象类型================================*/
	
	/**
	 * 操作对象类型--能管对象
	 */
	public static final int OPRATOR_OBJECT_TYPE_LEDGER = 1;
	
	/**
	 * 操作对象类型--采集点
	 */
	public static final int OPRATOR_OBJECT_TYPE_POINT = 2;
	
	/**
	 * 操作对象类型--模块
	 */
	public static final int OPRATOR_OBJECT_TYPE_MODULE = 3;
	
	
	/*========================模块名称================================*/
	/**
	 * 模块ID=--用户管理ID
	 */
	public static final int MODULE_NAME_USER_ID = 6;
	/**
	 * 模块名称=--用户管理
	 */
	public static final String MODULE_NAME_USER = "用户管理";
    /***
     * 模块名称--群组管理
     * @author Yaojiawei
     * @date 2014-08-13
     */
    public static final String MODULE_NAME_GROUP = "群组管理";
	
	/**
	 * 模块ID=--角色管理ID
	 */
	public static final int MODULE_NAME_ROLE_ID = 7;
    /**
     * 模块名称=--角色管理
     */
    public static final String MODULE_NAME_ROLE = "角色管理";
	
	/**
	 * 模块ID=--采集点管理ID
	 */
	public static final int MODULE_NAME_METER_ID = 8;
    /**
     * 模块名称=--采集点管理
     */
    public static final String MODULE_NAME_METER = "采集点管理";

    /**
     * 模块ID=--费率价格配置ID
     */
    public static final int MODULE_NAME_RATE_ID = 30;
    /**
     * 模块名称=--费率价格配置
     */
    public static final String MODULE_NAME_RATE = "费率价格配置";

    /**
     * 模块ID=--能管对象管理ID
     */
    public static final int MODULE_NAME_LEDGER_ID = 35;
    /**
     * 模块名称=--能管对象管理
     */
    public static final String MODULE_NAME_LEDGER = "能管对象管理";

    /**
     * 模块ID=--能管对象模型配置ID
     */
    public static final int MODULE_ID_LEDGER_MODEL = 117;
    /**
     * 模块名称=--能管对象模型配置
     */
    public static final String MODULE_NAME_LEDGER_MODEL = "能管对象模型配置";

    /**
     * 模块ID=--电力拓扑模型配置ID
     */
    public static final int MODULE_ID_ELECTRICITY_MODEL = 118;
    /**
     * 模块名称=--电力拓扑模型配置
     */
    public static final String MODULE_NAME_ELECTRICITY_MODEL = "电力拓扑模型配置";

    /**
     * 模块ID=--越限参数设置模块ID
     */
    public static final int MODULE_ID_EVENT_EXCEED_SET_MODEL = 204;
    
    /**
     * 模块名称=--越限参数设置
     */
    public static final String MODULE_NAME_EVENT_EXCEED_SET_MODEL = "越限参数设置";

    /**
     * 模块ID=--自定义信息推送ID
     */
    public static final int MODULE_ID_USER_DEFINED_MESSAGE = 108;
    /**
     * 模块名称=--自定义信息推送
     */
    public static final String MODULE_NAME_USER_DEFINED_MESSAGE = "自定义信息推送";


    /**
     * 模块ID=--新闻政策发布ID
     */
    public static final int MODULE_ID_NEWS_POLICY_PUBLISH = 109;
    /**
     * 模块名称=--新闻政策发布
     */
    public static final String MODULE_NAME_NEWS_POLICY_PUBLISH = "新闻政策发布";


    /**
     * 模块ID=--自动提醒参数配置ID
     */
    public static final int MODULE_ID_AUTO_REMIND_SET = 114;
    /**
     * 模块名称=--自动提醒参数配置
     */
    public static final String MODULE_NAME_AUTO_REMIND_SET = "自动提醒参数配置";


    /**
     * 模块ID=--免扰设置ID
     */
    public static final int MODULE_ID_NO_DISTURB_SET = 115;
    /**
     * 模块名称=--免扰设置
     */
    public static final String MODULE_NAME_NO_DISTURB_SET = "免扰设置";


    /**
     * 模块ID=--用户反馈ID
     */
    public static final int MODULE_ID_USER_FEEDBACK = 119;
    /**
     * 模块名称=--用户反馈
     */
    public static final String MODULE_NAME_USER_FEEDBACK = "用户反馈";


    /**
     * 模块ID=--需量ID
     */
    public static final int MODULE_ID_DEMAND_ANALYZE = 43;
    /**
     * 模块名称=--需量
     */
    public static final String MODULE_NAME_DEMAND_ANALYZE = "容量、需量申报";


    /**
     * 模块ID=--客户订阅ID
     */
    public static final int MODULE_ID_USER_BOOK = 202;
    /**
     * 模块名称=--客户订阅
     */
    public static final String MODULE_NAME_USER_BOOK = "客户订阅";


    /**
     * 模块ID=--批量订阅ID
     */
    public static final int MODULE_ID_BATCH_BOOK = 201;
    /**
     * 模块名称=--批量订阅
     */
    public static final String MODULE_NAME_BATCH_BOOK = "批量订阅";


    /**
     * 模块ID=--厂休时段设置ID
     */
    public static final int MODULE_ID_REST_TIME = 120;
    /**
     * 模块名称=--厂休时段设置
     */
    public static final String MODULE_NAME_REST_TIME = "厂休时段设置";

    //以下是废弃的模块
    /**
     * 模块ID=--节能项目改造ID
     */
    public static final int MODULE_NAME_PROJECT_ID = 9;
    /**
     *  模块ID=--排班管理ID
     */
    public static final int MODULE_NAME_SCHEDULE_ID = 26;
    /**
	 * 模块ID=--生产计划ID
	 */
	public static final int MODULE_NAME_DESIGN_ID = 32;
    /**
     * 模块ID=--产品配置管理ID
     */
    public static final int MODULE_NAME_PRODUCT_ID = 33;
    /**
	 * 模块ID=--流水线管理ID
	 */
	public static final int MODULE_NAME_LINE_ID = 34;
	/**
	 * 模块ID=--企业管理ID
	 */
	public static final int MODULE_NAME_DEPT_ID = 36;
	/**
	 * 模块名称=--节能项目改造
	 */
	public static final String MODULE_NAME_PROJECT = "节能项目改造";
    /**
     * 模块名称=--排班管理
     */
    public static final String MODULE_NAME_SCHEDULE = "排班管理";
    /**
     * 模块名称=--生产计划
     */
    public static final String MODULE_NAME_DESIGN = "生产计划";
    /**
     * 模块名称=--产品配置管理
     */
    public static final String MODULE_NAME_PRODUCT = "产品配置管理";
    /**
	 * 模块名称=--流水线管理
	 */
	public static final String MODULE_NAME_LINE = "流水线管理";
	/**
	 * 模块名称=--企业管理
	 */
	public static final String MODULE_NAME_DEPT = "企业管理";
}
