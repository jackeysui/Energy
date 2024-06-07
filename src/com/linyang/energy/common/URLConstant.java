package com.linyang.energy.common;

/**
 * @Description URL常量类
 * @author Leegern
 * @date Dec 9, 2013 1:51:03 PM
 */
public class URLConstant {
	
	/**
	 * 数据同步URL
	 */
	public static final String DATA_SYNC_MANAGER = "energy/datamanager/data_sync";
	
	/**
	 * 分户列表URL
	 */
	public static final String URL_LEDGER_LIST = "energy/recordmanager/ledger_list";

	
	/**
	 * 采集点URL
	 */
	public static final String URL_METER_LIST = "energy/recordmanager/meter_list";

    /**
     * 虚拟采集点URL
     */
    public static final String URL_VIRTUAL_METER_LIST = "energy/recordmanager/virtual_meter_list";

	
	/**
	 * 能耗分析统计URL
	 */
	public static final String URL_ENERGY_STATS = "energy/energyanalysis/energy_stats_analyse";
	
	/**
	 * 单位产品能耗URL
	 */
	public static final String URL_PRODUCTS_ENERGY = "energy/energyanalysis/products_energy";
	
	/**
	 * 同比分析URL
	 */
	public static final String URL_ENERGY_ANNULUS = "energy/contrastanalysis/energy_annulus";
	
	/**
	 * 分项占比URL
	 */
	public static final String URL_PARTIAL_SCALE = "energy/contrastanalysis/partial_scale";
	/**
	 * 排班管理
	 */
	public static final String URL_SCHEDULE_LIST = "energy/system/scheduling_list";
	/**
	 * 费率管理
	 */
	public static final String URL_RATE_LIST = "energy/system/rate_list";
	/**
	 * 产品管理
	 */
	public static final String URL_PRODUCT_LIST = "energy/system/product_list";
	
	/**
	 * 生产计划
	 */
	public static final String URL_DESIGN_LIST = "energy/system/product_design";
	
	/**
	 * 能耗排名URL
	 */
	public static final String URL_ENERGY_RANKING = "energy/contrastanalysis/energy_ranking";
    public static final String URL_ENERGY_RANKING_NEW = "energy/contrastanalysis/energy_ranking_new";

    /**
     * 需求响应URL
     */
    public static final String URL_ENERGY_DEMAND = "energy/contrastanalysis/energy_demand";

	/**
	 * 分户对比URL
	 */
	public static final String URL_ENERGY_HOUSEHOLD = "energy/contrastanalysis/energy_household";
	
	/**
	 * 节能潜力排名URL
	 */
	public static final String URL_SAVING_RANKING = "energy/scheduleanalysis/energy_saving_ranking";
	
	/**
	 * 事件查询URL
	 */
	public static final String URL_EVENT_QUERY = "energy/dataquery/event_query";
	
	/**
	 * 电价费率时段管理URL
	 */
	public static final String URL_RATE_MANAGEMENT = "energy/rate/rate_management";

	/**
	 * 电价管理URL
	 */
	public static final String URL_PRICE_MANAGEMENT = "energy/rate/price_management";

    public static final String OTHER_PRICE_MANAGEMENT = "energy/rate/otherPrice_management";
	
	/**
	 * 需量申报URL
	 */
	public static final String URL_DEMAND_DECLARE = "/energy/system/demand_declare";
	public static final String URL_DEMAND_DECLARE_EMO = "/energy/system/demand_declare_emo";
	
	/**
	 * 日需量分析
	 */
	public static final String URL_DAY_MD = "/energy/scheduleanalysis/day_MD_analysis";
	
	/**
	 * 日负载率
	 */
	public static final String URL_DAY_LOAD_RATIO = "/energy/scheduleanalysis/day_load_ratio";
	
	/**
	 * 日不平衡分析
	 */
	public static final String URL_DAY_UNBALANCE = "/energy/scheduleanalysis/unbalance_analysis";

	/**
	 * 线损分析
	 */
	public static final String URL_LINE_LOSS = "/energy/scheduleanalysis/line_loss_analysis";

    /**
     * 节能量统计
     */
    public static final String URL_SAVE_ENERGY = "/energy/scheduleanalysis/save_energy_analysis";
	
	/**
	 * 批量订阅
	 */
	public static final String BATCH_ORDER = "/energy/message/batch_order";
	
	/**
	 * 客户订阅
	 */
	public static final String CUSTOMER_ORDER = "/energy/message/customer_order";
	
	/**
	 * 自定义信息推送
	 */
	public static final String CUSTOM_PUSH = "/energy/message/custom_push";
	
	/**
	 * 新闻政策发布
	 */
	public static final String PRESS_POLICY = "/energy/message/press_policy";
	
	/**
	 * 服务报告信息
	 */
	public static final String SERVICE_REPORT = "/energy/message/service_report";
	
	/**
	 * 服务报告信息列表
	 */
	public static final String SERVICE_REPORT_LIST = "/energy/message/service_report_list";
	
	/**
	 * 服务报告推送页面
	 */
	public static final String SERVICE_REPORT_PUSH = "/energy/message/service_report_push";
	
	/**
	 * 添加订阅者
	 */
	public static final String ADD_SUBSCRIBER = "/energy/message/add_subscriber";
	
	/**
	 * 自动提醒设置
	 */
	public static final String AUTO_REMINDER_SET = "/energy/message/auto_reminder_set";
	/**
	 * 用户反馈
	 */
	public static final String CUSTOMER_CONTACT = "/energy/message/customer_contact";
	
	/**
	 * EMO模型配置
	 */
	public static final String EMO_TREE_SET = "/energy/tree/emo_tree";
	
	/**
	 * 电力拓扑模型配置
	 */
	public static final String ELE_TREE_SET = "/energy/tree/ele_tree";
	
	/**
	 * 越限参数设置
	 */
	public static final String Event_Exceed_Set = "/energy/message/set_event_exceed_parameter";
	
	/**
	 * 休息时间
	 */
	public static final String REST_TIME = "/energy/timeManager/rest_time";
	
	/**
	 * 负荷预测
	 */
	public static final String LOAD_PREDICT = "/energy/bigDataPredict/load_predict";
	
	/**
	 * 负荷预测
	 */
	public static final String SET_PAGE = "/energy/bigDataPredict/set_page";

    /**
     * 班制配置URL
     */
    public static final String URL_CLASS_CONFIG = "energy/classmanager/class_config";

    public static final String URL_CLASS_REST = "energy/classmanager/class_rest";

    /**
     * 生产单元配置URL
     */
    public static final String URL_WORKSHOP_CONFIG = "energy/classmanager/workshop_config";
    
    /**
     * 产品配置URL
     */
    public static final String URL_PRODUCT_CONFIG = "energy/classmanager/product_config";
    
    /**
     * 产量URL
     */
    public static final String URL_PRODUCT_OUTPUT = "energy/classmanager/product_output";
  
    /**
     * 关联分析URL
     */
    public static final String URL_CONNECT_ANALYSIS = "energy/dataquery/connect_analysis";
    
    /**
     * 关联分析URL
     */
    public static final String URL_HEATPUMP_DATAQUERY = "energy/heatpump/heatpump_dataquery";

    /**
     * 模拟响应预览URL
     */
    public static final String URL_DEMAND_SIMULATE = "energy/demand/demand_simulate";

    /**
     * 实际响应生成URL
     */
    public static final String URL_DEMAND_ACTUAL = "energy/demand/demand_actual";

    /**
     * 历史响应查询URL
     */
    public static final String URL_DEMAND_HISTORY = "energy/demand/demand_history";

    /**
     * 电网需求响应 -- 企业负荷数据
     */
    public static final String URL_ELE_DEMAND_DATA = "energy/demand/eleDemand_data";
    
	/**
	 * 数据预测
	 */
	public static final String DATA_PREDICT = "/energy/bigDataPredict/data_predict";

    /**
     * 热泵工艺图URL
     */
    public static final String URL_HEATPUMP_MAP = "energy/heatpump/heatpump_map";
	
    // add or update method by catkins.
    // date 2020/5/28
    // Modify the content: 云南能效部分页面
	/**
	 * 云南能效-计量器具管理
	 */
	public static final String MEASURING_DEVICE = "energy/yunNan/measuring_device";
	
	/**
	 * 云南能效-计量器具检定-校准管理
	 */
	public static final String INSTITUTIONS_DEVICE = "energy/yunNan/institutions_device";
	
	/**
	 * 云南能效-更换记录管理
	 */
	public static final String REPLACERECORD_PAGE = "energy/yunNan/replace_record";
	
	
	/**
	 * 云南能效-人员管理-联系人信息
	 */
	public static final String MANAGEMENT_PAGE = "energy/yunNan/energy_management";
	
	/**
	 * 云南能效-人员管理-管理人员信息
	 */
	public static final String COMPANY_MANAGER = "energy/yunNan/company_manager";
	
	/**
	 * 云南能效-人员管理-项目负责人信息
	 */
	public static final String MONITOR_MANAGER = "energy/yunNan/monitor_manager";
	
	/**
	 * 云南能效-人员管理-计量人员信息
	 */
	public static final String CALCULATER_MANAGER = "energy/yunNan/calculater_manager";
	
	/**
	 * 云南能效-产品项目档案-节能项目管理
	 */
	public static final String ENSAVING_MANAGE = "energy/yunNan/ensaving_manage";
	
	/**
	 * 云南能效-产品项目档案-产品结构管理
	 */
	public static final String PRODUCT_STRUCTURE = "energy/yunNan/product_structure";
	
	/**
	 * 云南能效-产品项目档案-产品结构管理
	 */
	public static final String MATERIEL_PRODUCT = "energy/yunNan/materiel_product";
    //end
    
	

}
