package com.linyang.energy.service;

import java.util.Date;
import java.util.Map;

import com.linyang.energy.model.LoadPredictSetBean;

/**
 * 大数据预测
 * @author Administrator
 *
 */
public interface BigDataService {

	/**
	 * 最大负荷时间预测
	 * @param param
	 * @return
	 */
	Map<String, Object> loadPredict(Map<String, Object> param);

	/**
	 * 把天气情况存入数据库
	 */
	void insertWeatherToDB();

	/**
	 * 得到预测的设置数据
	 * @return
	 */
	LoadPredictSetBean getSetInfo();

	/**
	 * 保存配置信息
	 * @param bean
	 * @return
	 */
	boolean saveSetInfo(LoadPredictSetBean bean);
	
	/**
	 * 数据预测
	 * @param param
	 * @return
	 */
	Map<String, Object> dataPredict(Map<String, Object> param);
	
	/**
	 * 判断日期类型：1-工作日；2-休息日
	 * @param date
	 * @param ledgerId
	 * @return
	 */
	public int judgeDayType(Date date, Long ledgerId);
	/**
	 * 计算数据模型
	 */
	void CalPredictModel();
	
	/**
	 * 当日预测数据
	 * @param legerId
	 * @return
	 */
	double getDayPredictData(Long ledgerId);
	
	/**
	 * 当月预测数据
	 * @param legerId
	 * @return
	 */
	double getMonthPredictData(Long ledgerId);

}
