package com.linyang.energy.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import com.leegern.util.CollectionUtil;
import com.linyang.energy.mapping.authmanager.RateBeanMapper;
import com.linyang.energy.mapping.phone.screendatasync.EnterpriseMapper;
import com.linyang.energy.model.*;
import com.linyang.energy.model.modelscreen.ConsDayEnergy;
import com.linyang.energy.model.modelscreen.ConsMonthAmt;
import com.linyang.energy.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.linyang.common.web.common.Log;
import com.linyang.energy.mapping.IndexMapper;
//import com.linyang.energy.mapping.eleAssessment.EleAssessmentMapper;
import com.linyang.energy.mapping.energyanalysis.SchedulingMapper;
import com.linyang.energy.mapping.energydataquery.EventQueryMapper;
import com.linyang.energy.mapping.energysavinganalysis.CostMapper;
import com.linyang.energy.mapping.message.MessageMapper;
import com.linyang.energy.mapping.phone.PhoneMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.service.CostService;
import com.linyang.energy.service.EventQueryService;
import com.linyang.energy.service.IndexService;
import com.linyang.energy.service.PhoneService;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;
import com.linyang.util.DoubleUtils;
import com.linyang.ws.service.DataCollectionWebService;
import com.linyang.ws.wsimport.DataBean;

/**
 * 手机接口业务逻辑层接口实现类
 * 
 * @author:gaofeng
 * @date:2014.8.18
 */
@Service
public class PhoneServiceImpl implements PhoneService {

	@Autowired
	private PhoneMapper phoneMapper;
	@Autowired
	private MeterManagerMapper meterManagerMapper;
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	@Autowired
	private IndexMapper indexMapper;
	@Autowired
	private SchedulingMapper schedulingMapper;
	@Autowired
	private CostService costService;
	@Autowired
	private CostMapper costMapper;
	@Autowired
	private EventQueryMapper eventQueryMapper;
	@Autowired
	private EventQueryService eventQueryService;
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private IndexService indexService;
	@Autowired
	private RateBeanMapper rateBeanMapper;
	@Autowired
	private EnterpriseMapper enterpriseMapper;
	
	private String defaultRateNameSuffer = ChartNameUtils.getChartNameBean()
			.getRateNameSuffer();

	@Override
	public List<Map<String, Object>> getPFData(long objId, int objType, Date baseDate, int dateType,int pageType) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (pageType == 1) {
			baseDate = DateUtil.getPreMonthLastDay(baseDate);
		}
		param.put("objId", objId);
		param.put("startDate", getStartDateByType(baseDate, dateType));
		param.put("endDate", baseDate);
		param.put("dateType", dateType);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>( 0 );
		if (objType == 2)
			return phoneMapper.getPointCurPFData(param);
		else {
			
			if( dateType  == 1 || dateType == 2 ){
				List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>( 0 );
				Map<String,Object> chartsMap = null;
				maps = phoneMapper.getLedgerPFData(param);
				if(maps != null && maps.size() > 0){
					for (Map<String, Object> map : maps) {
						chartsMap = new HashMap<String,Object>( 0 );
						if (map.containsKey("AP") && map.get( "AP" ) != null && map.containsKey("RP") && map.get( "RP" ) != null) {
							double pf = DataUtil.getPF(Double.parseDouble( map.get( "AP" ).toString() ), Double.parseDouble( map.get( "RP" ).toString() ), 3);
							chartsMap.put( "DATATIME",map.get( "DATATIME" ) );
							chartsMap.put( "DATA",pf );
							result.add( chartsMap );
						}
					}
				}
			} else {
				result = phoneMapper.getLedgerPFData(param);
			}
			return result;
		}
	}

	@Override
	public Double getLedgerPFData(Long objId, Date startDate, Date endDate, int dateType) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objId", objId);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("dateType", dateType);

		return phoneMapper.getLedgerPFData2(param);
	}

	/**
	 * 根据类型获取开始时间
	 * 
	 * @param d
	 * @param dateType
	 * @return
	 */
	private Date getStartDateByType(Date d, int dateType) {

		switch (dateType) {
		case 1:// 日数据
			return DateUtil.clearDate(d);
		case 2:// 3日数据
			return DateUtil.clearDate(DateUtil.getDateBetween(d, -2));
		case 3:// 10日数据
			return DateUtil.clearDate(DateUtil.getDateBetween(d, -9));
		case 4:// 30日数据
			return DateUtil.clearDate(DateUtil.getDateBetween(d, -29));
		case 5:// 年数据
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.set(Calendar.DAY_OF_MONTH, 2);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);

			c.add(Calendar.MONTH, -11);
			c.set(Calendar.DAY_OF_MONTH, 1);
			return new Date(c.getTimeInMillis());
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getAPData(long objId, int objType, Date baseDate, int dateType) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objId", objId);
		param.put("startDate", getStartDateByType(baseDate, dateType));
		param.put("endDate", baseDate);
		param.put("dateType", dateType);
		if (objType == 1)// EMO
			return phoneMapper.getLedgerAPData(param);
		else
			return phoneMapper.getPointCurAPData(param);
	}

	@Override
	public List<Map<String, Object>> getRPData(long objId, int objType, Date baseDate, int dateType) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objId", objId);
		param.put("startDate", getStartDateByType(baseDate, dateType));
		param.put("endDate", baseDate);
		param.put("dateType", dateType);
		if (objType == 1)
			return phoneMapper.getLedgerRPData(param);
		else
			return phoneMapper.getPointCurRPData(param);
	}

	@Override
	public UserBean getUserByUserName(String username) {
		if (CommonMethod.isNotEmpty(username)) {
			return phoneMapper.getUserByUserName(username.trim());
		}
		return null;
	}

	@Override
	public UserBean getUserByAccountId(Long accountId) {
		if (CommonMethod.isNotEmpty(accountId)) {
			return phoneMapper.getUserByAccountId(accountId);
		}
		return null;
	}

	@Override
	public void updateUserInfo(UserBean userBean) {
		phoneMapper.updateUserInfo(userBean);
	}

	@Override
	public List<GroupBean> getAllUserTeam(Map<String, Object> queryInfo) {
		return phoneMapper.getPageUserGroupByType(queryInfo);
	}

	@Override
	public List<LedgerBean> getLedgerByGroupId(Long groupId) {
		return phoneMapper.getLedgerByGroupId(groupId);
	}

	@Override
	public List<MeterBean> getMeterByGroupId(Long groupId) {
		return phoneMapper.getMeterByGroupId(groupId);
	}

	@Override
	public List<LedgerBean> getPageLedger(Map<String, Object> queryInfo) {
		return phoneMapper.getPageLedger(queryInfo);
	}

	@Override
	public LedgerBean getLedgerById(Long ledgerId) {
		return phoneMapper.getLedgerById(ledgerId);
	}

	@Override
	public List<MeterBean> getMeterListByLedgerOrMeterId(Long objId, Integer objType) {
		// edit by chengq 20160112 计算模型的表+分户下所属的表计
		if (objType.intValue() == 2) {
			objId = phoneMapper.getLedgerIdByMeterId(objId);
		}
		List<MeterBean> emo2ModelList = phoneMapper.getEMOModel2(objId); // 下挂显示表
		List<MeterBean> emo1ModelList = phoneMapper.getEMOModel1(objId); // 计算模型表
		// List<MeterBean> resultList =
		// phoneMapper.getMeterListByLedgerOrMeterId(objId,objType);
		emo2ModelList.removeAll(emo1ModelList);
		emo2ModelList.addAll(emo1ModelList);
		return emo2ModelList;
	}

	@Override
	public List<Map<String, Object>> getMeterListByLeger(Long ledgerId, int level) {
		List<Map<String, Object>> meterList = this.phoneMapper.getMeterListByLeger(ledgerId, level);
		return meterList;
	}

	@Override
	public List<Map<String, Object>> getVolCurData(long objId, int objType, Date baseDate, int dateType, int dataType) {
		if (objType == 1)
			return null;

		Map<String, Object> param = new HashMap<String, Object>();
		// 得到电源接线方式1,三相三线;2,三相四线;3:单相表
		Integer commMode = this.phoneMapper.queryCommMode(objId);
		param.put("commMode", commMode);
		param.put("objId", objId);
		param.put("startDate", getStartDateByType(baseDate, dateType));
		param.put("endDate", baseDate);
		if (dataType == 5) {
//			return completionData(commMode, phoneMapper.getPointVol(param));// 电压
			return phoneMapper.getPointVol(param);
		} else
//			return completionData(commMode, phoneMapper.getPointCur(param));// 电流
			return phoneMapper.getPointCur(param);
	}

	/**
	 * 补全数据
	 * 
	 * @author guosen
	 * @param commMode
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> completionData(Integer commMode, List<Map<String, Object>> list) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				if (!map.containsKey("A")) {
					map.put("A", 0);
					list.add(i, map);
				}
                if (!map.containsKey("B")) {
                    map.put("B", 0);
                    list.add(i, map);
                }
				if (!map.containsKey("C")) {
					map.put("C", 0);
					list.add(i, map);
				}
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getEnergyData(long objId, int objType, Date baseDate, int dateType) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objId", objId);
		Date startDate = getStartDateByType(baseDate, dateType);
		Date endDate = baseDate;
		/*if (dateType == 1 || dateType == 2) {// 如果是“日”、“3日”的电量曲线数据，则startDate和endDate各加一秒钟
			startDate = DateUtil.getNextSecondDate(startDate);
			endDate = DateUtil.getNextSecondDate(endDate);
		}*/
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("dateType", dateType);

		if (objType == 1)// 如果是分户ID
		{
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if (dateType == 1 || dateType == 2)// 日数据，3日数据
				return phoneMapper.getLedger96EnergyData(param);
			else if (dateType == 3 || dateType == 4) {
				if (DateUtils.convertTimeToLong(
						DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT) + " 00:00:00") <= endDate.getTime() / 1000) {
					param.put("startDate",
							DateUtils.convertShortDateStr2Date(DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT)));
					list.addAll(phoneMapper.getLedgerEnergyData(param));
				} else {
					return phoneMapper.getLedgerEnergyData(param);
				}
			} else {
				return phoneMapper.getLedgerEnergyData(param);
			}
			param.put("startDate", startDate);
			param.put("endDate", DateUtils.convertShortDateStr2Date(
					DateUtils.addDay(DateUtils.convertTimeToString(endDate.getTime() / 1000), -1)));
			list.addAll(0, phoneMapper.getLedgerEnergyData2(param));
			return list;
		} else// 如果是计量点
		{
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if (dateType == 1 || dateType == 2)// 日数据，3日数据
				return phoneMapper.getPoint96EnergyData(param);
			else if (dateType == 3 || dateType == 4) {
				if (DateUtils.convertTimeToLong(
						DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT) + " 00:00:00") <= endDate.getTime() / 1000) {
					param.put("startDate",
							DateUtils.convertShortDateStr2Date(DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT)));
					list.addAll(phoneMapper.getPointEnergyData(param));
				} else {
					return phoneMapper.getPointEnergyData(param);
				}
			} else {
				return phoneMapper.getPointEnergyData(param);
			}
			param.put("startDate", startDate);
			param.put("endDate", DateUtils.convertShortDateStr2Date(
					DateUtils.addDay(DateUtils.convertTimeToString(endDate.getTime() / 1000), -1)));
			list.addAll(0, phoneMapper.getPointEnergyData2(param));
			return list;
		}
	}

	// yaojiawei
	@Override
	public Double getLedgerEnergyData(Long objId, Date startDate, Date endDate, int dateType) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objId", objId);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("dateType", dateType);

		return phoneMapper.getLedgerSumEnergyData(param);
	}

	@Override
	public Double getRecentAP(long objId) {
		Date date = DateUtil.clearDate(new Date());
		return phoneMapper.getRecentAP(objId, date);
	}

	@Override
	public Double getRecentRP(long objId) {
		Date date = new Date();
		return phoneMapper.getRecentRP(objId, date);
	}

	@Override
	public List<LedgerBean> queryRecursiveLedgerById(Map<String, Object> queryInfo) {
		return phoneMapper.queryPageRecursiveLedgerById(queryInfo);
	}

	/**
	 * 得到VIP数据 数据类型；1、电量数据；2、功率因数数据；3、有功功率；4、无功功率；5、电压；6、电流；
	 * 
	 * @param objId
	 *            查询对象ID
	 * @param objType
	 *            对象类型（1、分户；2、计量点）
	 * @param baseDate
	 *            基准日期（格式为：yyyy-MM-dd）
	 * @param dateType
	 *            日期类型（1、日数据；2、3日数据；3、10日数据；4、30日数据；5、年数据；）
	 * @param list
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPhoneVIPData(int phoneDataType, long objId, int objType, String baseDate,
			int dateType, List<Map<String, Object>> list) {
		try {
			int dataType = 0;
			switch (phoneDataType) {
			case 1:// 电能示值
				dataType = CurveBean.CURVE_TYPE_ELE;
				break;
			case 2:// 功率因数
				dataType = CurveBean.CURVE_TYPE_PF;
				break;
			case 3:// 有功功率
				dataType = CurveBean.CURVE_TYPE_AP;
				break;
			case 4:// 无功功率
				dataType = CurveBean.CURVE_TYPE_RP;
				break;
			case 5:// 电压
				dataType = CurveBean.CURVE_TYPE_V;
				break;
			case 6:// 电流
				dataType = CurveBean.CURVE_TYPE_I;
				break;
			}

			if ((dateType == 1 || dateType == 2) && objType == 2) {// 日数据，3日数据有VIP数据,计量点才能看到VIP数据
				MeterBean meter = this.meterManagerMapper.getMeterDataByPrimaryKey(objId);
				Long ledgerId = meter.getLedgerId();// 分户ID
				LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(ledgerId);
				// 是否为VIP分户
				if (ledger != null && ledger.getLedgerType() != null && ledger.getLedgerType() == LedgerBean.LEDGER_TYPE_VIP) {
					DateFormat df = new SimpleDateFormat(DateUtils.FORMAT_LONG);
					Date endDate = df.parse(baseDate + " 23:59:59");
					Long endTime = endDate.getTime();
					long currentTime = new Date().getTime();
					if (endTime > currentTime) {// 截止日期为今天
						DataCollectionWebService webServie = WebServiceUtil.getInstance().getWebService();
						if (webServie != null) {
							Date startDate = null;
							if (dateType == 1) {// 日数据
								startDate = df.parse(baseDate + " 00:00:00");
							} else {// 3日数据
								startDate = DateUtils.getDateBetween(df.parse(baseDate + " 00:00:00"), -2);
							}
							boolean deleteFirst = false;// 是否删除第一个重复的点；
							if (list != null && list.size() > 0) {// 取到曲线的最后一个点
								Map<String, Object> lastPoint = list.get(list.size() - 1);
								startDate = (Date) lastPoint.get("DATATIME");
								deleteFirst = true;
							}

							long startTime = startDate.getTime();
							List<DataBean> datas = webServie.getHistoryDataFromDb(meter.getMpedId(), dataType,
									startTime, endTime);

							Integer commMode = 0;// 得到电源接线方式1,三相三线;2,三相四线;3:单相表
							if (dataType == CurveBean.CURVE_TYPE_V || dataType == CurveBean.CURVE_TYPE_I) {
								commMode = this.phoneMapper.queryCommMode(objId);
							}
							if (datas != null && datas.size() > 0) {
								if (deleteFirst && dataType != CurveBean.CURVE_TYPE_ELE) {
									datas.remove(0);// 电量删除第一个重复的点
								}
								Date lastFreezeTime = null;
								Double lastIndicationA = null;
								Double pt = meter.getPt() == null ? 1 : meter.getPt();
								Double ct = meter.getCt() == null ? 1 : meter.getCt();
								for (DataBean dataBean : datas) {
									XMLGregorianCalendar cale = dataBean.getDataTime();// 冻结时间
									GregorianCalendar ca = cale.toGregorianCalendar();
									Calendar cal = GregorianCalendar.getInstance();
									Date freezeTime = ca.getTime();
									cal.setTime(freezeTime);
									int minute = cal.get(Calendar.MINUTE);// 分钟
									Map<String, Object> curveMap = new HashMap<String, Object>();
									curveMap.put("DATATIME", freezeTime);

									switch (dataType) {
									case CurveBean.CURVE_TYPE_ELE: // 电量
										Double indicationA = dataBean.getDataValueA() != null ? dataBean.getDataValueA()
												: 0;// 正向有功总电能示值
										if (minute == 0) {// 一个小时一个点
											if (lastFreezeTime != null
													&& (freezeTime.getTime() - lastFreezeTime.getTime()) == 1 * 1000
															* 60 * 60) {
												// 时间要减一个小时
												curveMap.put("DATATIME", new Date(freezeTime.getTime()));
												curveMap.put("DATA",
														DataUtil.retained(new BigDecimal(indicationA)
																.subtract(new BigDecimal(lastIndicationA))
																.multiply(new BigDecimal(pt * ct)).doubleValue(), 2));
												curveMap.put("METER_ID", objId);
												if (list != null)
													list.add(curveMap);
											}
											lastFreezeTime = freezeTime;
											lastIndicationA = indicationA;
										}
										break;
									case CurveBean.CURVE_TYPE_PF:// 功率因数，需要乘以PT,CT
										double data = dataBean.getDataValue() != null ? dataBean.getDataValue() : 0;
										curveMap.put("DATA", DataUtil.doubleDivide(data, 100, 2, RoundingMode.HALF_UP));
										if (list != null)
											list.add(curveMap);
										break;
									case CurveBean.CURVE_TYPE_AP:// 有功功率，需要乘以PT,CT
										curveMap.put("DATA", dataBean.getDataValue() != null
												? DataUtil.doubleMultiply(dataBean.getDataValue(), pt * ct) : 0);
										if (list != null)
											list.add(curveMap);
										break;
									case CurveBean.CURVE_TYPE_RP:// 无功功率，需要乘以PT,CT
										curveMap.put("DATA", dataBean.getDataValue() != null
												? DataUtil.doubleMultiply(dataBean.getDataValue(), pt * ct) : 0);
										if (list != null)
											list.add(curveMap);
										break;
									case CurveBean.CURVE_TYPE_V: // 电压，需要乘以PT
										double a = dataBean.getDataValueA() != null
												? DataUtil.doubleMultiply(dataBean.getDataValueA(), pt) : 0;
										double b = dataBean.getDataValueB() != null
												? DataUtil.doubleMultiply(dataBean.getDataValueB(), pt) : 0;
										double c = dataBean.getDataValueC() != null
												? DataUtil.doubleMultiply(dataBean.getDataValueC(), pt) : 0;
										if (commMode != 0 && commMode != 1) {
											a = DataUtil.doubleMultiply(a, 1.732);
											b = DataUtil.doubleMultiply(b, 1.732);
											c = DataUtil.doubleMultiply(c, 1.732);
										}
										curveMap.put("A", Math.round(a));
										if (commMode != 0 && commMode != 1) {
											curveMap.put("B", Math.round(b));
										}
										curveMap.put("C", Math.round(c));
										if (list != null)
											list.add(curveMap);
										break;
									case CurveBean.CURVE_TYPE_I:// 电流，需要乘以CT
										double ia = dataBean.getDataValueA() != null
												? DataUtil.doubleMultiply(dataBean.getDataValueA(), ct) : 0;
										double ib = dataBean.getDataValueB() != null
												? DataUtil.doubleMultiply(dataBean.getDataValueB(), ct) : 0;
										double ic = dataBean.getDataValueC() != null
												? DataUtil.doubleMultiply(dataBean.getDataValueC(), ct) : 0;
										curveMap.put("A", Math.round(ia));
										if (commMode != 0 && commMode != 1) {
											curveMap.put("B", Math.round(ib));
										}
										curveMap.put("C", Math.round(ic));
										if (list != null)
											list.add(curveMap);
										break;
									}
								}
							}
						}
					}
				}
			}
		} catch (ParseException e) {
			Log.info("getPhoneVIPData error ParseException");
		}
		return list;
	}

	@Override
	public Long getLedgerIdByMeterId(Long meterId) {
		return this.phoneMapper.getLedgerIdByMeterId(meterId);
	}

	@Override
    public Map<String,Object> showPhoneIndex(UserBean userBean,int qType){
        Map<String, Object> result = new HashMap<String, Object>();

        Long ledgerId = userBean.getLedgerId();
        Date baseDate = WebConstant.getChartBaseDate();
        Date preMonthBegin = com.linyang.energy.utils.DateUtil.getPrePreMonthLastDay(baseDate);
        Date preMonthEnd = com.linyang.energy.utils.DateUtil.getPreMonthLastDay(baseDate);
        Date currMonthBegin = com.linyang.energy.utils.DateUtil.getPreMonthLastDay(baseDate);
        Date currMonthEnd = baseDate;
        //上月、本月电费
        LedgerBean ledgerBean= ledgerManagerMapper.selectByLedgerId(ledgerId);
        int analyType = 0;
        if(ledgerBean != null){
            analyType = ledgerBean.getAnalyType();
        }
        if(analyType == 102){
            result.putAll(indexService.queryChart3Data(ledgerId));
        }
        else{
            result.put("preMonFee", 0);
            result.put("currMonFee", 0);
        }
        //上月、本月耗电量
        if(qType == 0){
        	List<Map<String, Object>> preMonQ = indexMapper.queryLedgerFeeMonQ(ledgerId, preMonthBegin, preMonthEnd);
        	result.put("preMonQ",preMonQ);
        } else if(qType == 1){
        	List<Map<String, Object>> currMonQ = indexMapper.queryLedgerFeeMonQ(ledgerId, currMonthBegin,currMonthEnd);
            result.put("currMonQ",currMonQ);
        } else {
        	List<Map<String, Object>> preMonQ = indexMapper.queryLedgerFeeMonQ(ledgerId, preMonthBegin, preMonthEnd);
        	List<Map<String, Object>> currMonQ = indexMapper.queryLedgerFeeMonQ(ledgerId, currMonthBegin,currMonthEnd);
        	result.put("preMonQ",preMonQ);
            result.put("currMonQ",currMonQ);
        }
        // 上月、本月分费率耗电量汇总
        if(qType == 0){
        	List<Map<String, Object>> preMonRateQ = indexMapper.queryLedgerFeeMonRateQ(ledgerId, preMonthBegin, preMonthEnd);
            result.put("currMonRateQ",preMonRateQ);
        } else if(qType == 1){
            List<Map<String, Object>> currMonRateQ = indexMapper.queryLedgerFeeMonRateQ(ledgerId, currMonthBegin,currMonthEnd);
            result.put("currMonRateQ",currMonRateQ);
        } else {
        	List<Map<String, Object>> preMonRateQ = indexMapper.queryLedgerFeeMonRateQ(ledgerId, preMonthBegin, preMonthEnd);
            List<Map<String, Object>> currMonRateQ = indexMapper.queryLedgerFeeMonRateQ(ledgerId, currMonthBegin,currMonthEnd);
            result.put("preMonRateQ",preMonRateQ);
            result.put("currMonRateQ",currMonRateQ);
        }
        //上月、本月最大需量
        BigDecimal preMaxDemand = new BigDecimal(0);
        BigDecimal currMaxDemand = new BigDecimal(0);
        if(qType == 0){
        	List<Map<String, Object>> maxList1 = indexMapper.getMonthMaxDemand(ledgerId, preMonthBegin, preMonthEnd);
            if(!CollectionUtils.isEmpty(maxList1)){
                preMaxDemand = (BigDecimal) maxList1.get(0).get("AP");
            }
            result.put("preMaxDemand",preMaxDemand);
        } else if(qType == 1){
            List<Map<String, Object>> maxList2 = indexMapper.getMonthMaxDemand(ledgerId, currMonthBegin, currMonthEnd);
            if(!CollectionUtils.isEmpty(maxList2)){
                currMaxDemand = (BigDecimal) maxList2.get(0).get("AP");
            }
            result.put("currMaxDemand",currMaxDemand);
        } else {
        	List<Map<String, Object>> maxList1 = indexMapper.getMonthMaxDemand(ledgerId, preMonthBegin, preMonthEnd);
            if(!CollectionUtils.isEmpty(maxList1)){
                preMaxDemand = (BigDecimal) maxList1.get(0).get("AP");
            }
            List<Map<String, Object>> maxList2 = indexMapper.getMonthMaxDemand(ledgerId, currMonthBegin, currMonthEnd);
            if(!CollectionUtils.isEmpty(maxList2)){
                currMaxDemand = (BigDecimal) maxList2.get(0).get("AP");
            }
            result.put("preMaxDemand",preMaxDemand);
            result.put("currMaxDemand",currMaxDemand);
        }
        //标准功率因数
        Double factor = schedulingMapper.getThresholdValue(ledgerId);
        result.put("factor",factor);
        
        //本月、上月功率因数
        if(qType == 0){
        	//上月功率因数
            BigDecimal preFactor = new BigDecimal(0);
            Map<String,Object> queryMap = new HashMap<String, Object>();
            queryMap.put("ledgerId",ledgerId);
            queryMap.put("beginTime",preMonthBegin);
            queryMap.put("endTime",preMonthEnd);
            List<Map<String, Object>> factorList1= phoneMapper.getMonLedgerFactor(queryMap);
            if(!CollectionUtils.isEmpty(factorList1)){
                preFactor = (BigDecimal) factorList1.get(0).get("PF");
            }
            result.put("preFactor",preFactor);
        } else if(qType == 1){
        	//本月功率因数
            String startStr = DateUtil.convertDateToStr(currMonthBegin, DateUtil.SHORT_PATTERN);
            String endStr = DateUtil.convertDateToStr(currMonthEnd, DateUtil.SHORT_PATTERN);
            Date startDate = DateUtil.convertStrToDate(startStr, DateUtil.SHORT_PATTERN);
            Date endDate = DateUtil.convertStrToDate(endStr, DateUtil.SHORT_PATTERN);
            Double monq = costMapper.getLedgerMonQEnd(ledgerId, startDate, endDate);
            Double monrq = costMapper.getLedgerMonRQEnd(ledgerId, startDate, endDate);
            double currFactor = DataUtil.getPF(monq, monrq);
            result.put("currFactor",currFactor);
        } else {
        	//本月功率因数
            String startStr = DateUtil.convertDateToStr(currMonthBegin, DateUtil.SHORT_PATTERN);
            String endStr = DateUtil.convertDateToStr(currMonthEnd, DateUtil.SHORT_PATTERN);
            Date startDate = DateUtil.convertStrToDate(startStr, DateUtil.SHORT_PATTERN);
            Date endDate = DateUtil.convertStrToDate(endStr, DateUtil.SHORT_PATTERN);
            Double monq = costMapper.getLedgerMonQEnd(ledgerId, startDate, endDate);
            Double monrq = costMapper.getLedgerMonRQEnd(ledgerId, startDate, endDate);
            double currFactor = DataUtil.getPF(monq, monrq);
            result.put("currFactor",currFactor);
            //上月功率因数
            BigDecimal preFactor = new BigDecimal(0);
            Map<String,Object> queryMap = new HashMap<String, Object>();
            queryMap.put("ledgerId",ledgerId);
            queryMap.put("beginTime",preMonthBegin);
            queryMap.put("endTime",preMonthEnd);
            List<Map<String, Object>> factorList1= phoneMapper.getMonLedgerFactor(queryMap);
            if(!CollectionUtils.isEmpty(factorList1)){
                preFactor = (BigDecimal) factorList1.get(0).get("PF");
            }
            result.put("preFactor",preFactor);
        }
        return result;
    }

	@Override
	public Map<String, Object> pfAnalysis(Long ledgerId, String flag, String dateStr) {
		Map<String, Object> result = new HashMap<String, Object>();
		Double stdPF = 0D;
		Double faqValue = 0D;
		Double frqValue = 0D;
		Double pf = 0D;
		List<Map<String, Object>> charts = new ArrayList<Map<String, Object>>();

		// queryMap
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("ledgerId", ledgerId);
		String timeType = "1"; // timeType: 1表示日功率因数，2表示月功率因数
		Date strDate = DateUtil.convertStrToDate(dateStr, DateUtil.SHORT_PATTERN);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date endTime = new Date(calendar.getTimeInMillis());
		queryMap.put("endTime", endTime); // 结束时间
		Date beginTime = DateUtil.getDateBetween(strDate, -9); // 起始时间
		if ("0".equals(flag)) {
			beginTime = strDate;
		} else if ("1".equals(flag)) {
			beginTime = DateUtil.getDateBetween(strDate, -2);
		} else if ("2".equals(flag)) {
			beginTime = DateUtil.getDateBetween(strDate, -9);
		} else if ("3".equals(flag)) {
			beginTime = DateUtil.getDateBetween(strDate, -29);
		} else if ("4".equals(flag)) {
			Date monthLastDay = DateUtil.getMonthLastDay(strDate);
			// Date next = DateUtil.getDateBetween(monthLastDay,1);
			beginTime = DateUtil.getLastYearDate(monthLastDay);
			timeType = "2";
		}
		queryMap.put("beginTime", beginTime);
		queryMap.put("timeType", timeType);
		// 有功电量、无功电量，标准cosφ，功率因数
		Double factor = schedulingMapper.getThresholdValue(ledgerId);
		if (factor != null) {
			stdPF = factor;
		}
		FactorBean factorQ = schedulingMapper.getFactorQ(queryMap);
		if (factorQ != null) {
			faqValue = (double) Math.round(factorQ.getTotalFaq());
			frqValue = (double) Math.round(factorQ.getTotalFrq());
			pf = DataUtil.getPF(faqValue, frqValue);
			pf = new BigDecimal(Math.round(DataUtil.doubleMultiply(pf, 100)))
					.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		}

		result.put("stdPF", stdPF);
		result.put("faqValue", faqValue);
		result.put("frqValue", frqValue);
		result.put("pf", pf);
		// 评价
		String comment = "";
		if (pf != null && stdPF != null && pf > 0 && stdPF > 0) {
			if (pf > DataUtil.doubleAdd(stdPF, 0.05)) {
				comment = "1"; // 优
			} else if (pf >= stdPF) {
				comment = "2"; // 良
			} else if (pf < DataUtil.doubleSubtract(stdPF, 0.05)) {
				comment = "3"; // 中
			} else {
				comment = "4"; // 差
			}
		}
		result.put("comment", comment);
		// 图表数据
		if ("4".equals(flag)) {
			charts = this.phoneMapper.getMonLedgerFactor(queryMap); // 年
		} else if ("0".equals(flag) || "1".equals(flag)) {
			List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>( 0 );
			Map<String,Object> chartsMap = null;
			maps = this.phoneMapper.getMinuteLedgerFactor(queryMap); // 日、3日
				if(maps != null && maps.size() > 0){
					for (Map<String, Object> map : maps) {
						chartsMap = new HashMap<String,Object>( 0 );
						if (map.containsKey("APD") && map.get( "APD" ) != null && map.containsKey("RPD") && map.get( "RPD" ) != null) {
							double pfd = DataUtil.getPF(Double.parseDouble( map.get( "APD" ).toString() ), Double.parseDouble( map.get( "RPD" ).toString() ), 3);
							chartsMap.put( "FREEZE_TIME",map.get( "FREEZE_TIME" ) );
							chartsMap.put( "PF",pfd );
							charts.add( chartsMap );
						}
					}
				}
		} else {
			charts = this.phoneMapper.getDayLedgerFactor(queryMap); // 10日、30日
		}
		result.put("charts", charts);

		return result;
	}

	@Override
	public Map<String, Object> demandAnalysis(Long pointId, String flag, String dateStr, Integer objType) {
		Map<String, Object> result = new HashMap<String, Object>();

		Date baseDate = DateUtil.convertStrToDate(dateStr, DateUtil.SHORT_PATTERN);
		// 申报类型;1,容量;2,需量
		int declareType = 0;
		String nowStr = DateUtil.convertDateToStr(baseDate, DateUtil.MONTH_PATTERN);
		List<Integer> declareTypeList = null;
		if (objType == 1) {
			declareTypeList = this.costMapper.getCurrentLedgerDeclareType(pointId, nowStr);
		} else {
			declareTypeList = this.costMapper.getCurrentDeclareType(pointId, nowStr);
		}

		if (declareTypeList != null && declareTypeList.size() > 0) {
			declareType = declareTypeList.get(0);
		}
		result.put("objType", objType);
		result.put("declareType", declareType);
		// 图表所需数据
		List<Map<String, Object>> charts = new ArrayList<Map<String, Object>>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pointId", pointId);
		queryMap.put("objType", objType);
		Long volume = 0L;
		Map<String, Object> feeInfo = null;
		if (objType == 1) {
			feeInfo = costMapper.getLedgerIdBasicFeeInfo(Long.valueOf(queryMap.get("pointId").toString()));
		} else {
			feeInfo = costMapper.getBasicFeeInfo(Long.valueOf(queryMap.get("pointId").toString()));
		}

		if ("0".equals(flag)) {
			Date beginTime = DateUtil.getCurrMonthFirstDay(baseDate);
			Date endTime = DateUtil.getMonthLastDay(baseDate);
			queryMap.put("beginTime", beginTime);
			queryMap.put("endTime", endTime);
			charts = this.phoneMapper.queryDayMDData(queryMap);
			if (objType == 1) {
				List<Long> pointIds = costMapper.getAllottedPointIdsByLedgerId(pointId);
				if (pointIds.size() == 1) {// 分户下只配置一个测量点，取该测量点的信息
					Map<String, Object> tempLastMap = new HashMap<String, Object>();
					tempLastMap.put("pointId", pointIds.get(0));
					tempLastMap.put("beginTime", queryMap.get("beginTime"));
					tempLastMap.put("endTime", queryMap.get("endTime"));
					tempLastMap.put("objType", 2);
					charts = phoneMapper.queryDayMDData(tempLastMap);
				}
			}
//			if (declareType == 1) {// 若是容量申报
				for (int i = 0; i < charts.size(); i++) {
					Double demand = 0D;
					Map<String, Object> map = charts.get(i);
					if (feeInfo != null && feeInfo.get("VOLUME") != null && feeInfo.get("VOLRATE") != null
							&& feeInfo.get("DERATE") != null) {
						volume = Long.valueOf(feeInfo.get("VOLUME").toString());
						Double volPrice = Double.valueOf(feeInfo.get("VOLRATE").toString());
						Double demandPrice = Double.valueOf(feeInfo.get("DERATE").toString());
						if (map.get("MD_DATA") != null) {
							Double mdData = Double.valueOf(map.get("MD_DATA").toString());
							demand = new BigDecimal(volPrice).multiply(new BigDecimal(mdData))
									.divide(new BigDecimal(demandPrice), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
						} else {
							demand = new BigDecimal(volPrice).multiply(new BigDecimal(volume))
									.divide(new BigDecimal(demandPrice), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
						}
					}
					map.put("MD_DATA", demand);
				}
//			}
		} else {
			Date endTime = DateUtil.getMonthLastDay(baseDate);
			Date temp = DateUtil.getDateBetween(endTime, 1);
			Date beginTime = DateUtil.getLastYearDate(temp);
			queryMap.put("beginTime", beginTime);
			queryMap.put("endTime", endTime);
			charts = this.phoneMapper.queryMonMDData(queryMap);
			if (objType == 1) {
				List<Long> pointIds = costMapper.getAllottedPointIdsByLedgerId(pointId);
				if (pointIds.size() == 1) {// 分户下只配置一个测量点，取该测量点的信息
					Map<String, Object> tempLastMap = new HashMap<String, Object>();
					tempLastMap.put("pointId", pointIds.get(0));
					tempLastMap.put("beginTime", queryMap.get("beginTime"));
					tempLastMap.put("endTime", queryMap.get("endTime"));
					tempLastMap.put("objType", 2);
					charts = phoneMapper.queryMonMDData(tempLastMap);
				}
			}
			for (int i = 0; i < charts.size(); i++) {
				Map<String, Object> map = charts.get(i);
//				if (map.get("DECLARE_TYPE") != null && "1".equals(map.get("DECLARE_TYPE"))) { // 若是容量申报
					Double demand = 0D;
					if (feeInfo != null && feeInfo.get("VOLUME") != null && feeInfo.get("VOLRATE") != null
							&& feeInfo.get("DERATE") != null) {
						volume = Long.valueOf(feeInfo.get("VOLUME").toString());
						Double volPrice = Double.valueOf(feeInfo.get("VOLRATE").toString());
						Double demandPrice = Double.valueOf(feeInfo.get("DERATE").toString());
						if (map.get("MD_DATA") != null) {
							Double mdData = Double.valueOf(map.get("MD_DATA").toString());
							demand = new BigDecimal(volPrice).multiply(new BigDecimal(mdData))
									.divide(new BigDecimal(demandPrice), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
						} else {
							demand = new BigDecimal(volPrice).multiply(new BigDecimal(volume))
									.divide(new BigDecimal(demandPrice), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
						}
					}
					map.put("MD_DATA", demand);
//				}
			}
		}
		result.put("charts", charts);
		// 月最大需量波动范围
		Long minMonFad = 0L;
		Long maxMonFad = 0L;
		if ("1".equals(flag) && charts != null && charts.size() > 0) {
			minMonFad = ((BigDecimal) charts.get(0).get("MAXFAD")).longValue();
			maxMonFad = ((BigDecimal) charts.get(0).get("MAXFAD")).longValue();
			for (int i = 0; i < charts.size(); i++) {
				Long maxfad = ((BigDecimal) charts.get(i).get("MAXFAD")).longValue();
				if (maxfad > maxMonFad) {
					maxMonFad = maxfad;
				}
				if (maxfad < minMonFad) {
					minMonFad = maxfad;
				}
			}
		}
		result.put("minMonFad", minMonFad);
		result.put("maxMonFad", maxMonFad);
		// 月最大需量、发生时间
		Long monFad = 0L;
		String monTime = "";
		if ("0".equals(flag) && charts != null && charts.size() > 0) {
			monFad = ((BigDecimal) charts.get(0).get("MAXFAD")).longValue();
			if (charts.get(0).get("OCCURTIME") != null) {
				monTime = charts.get(0).get("OCCURTIME").toString();
			}
			for (int i = 0; i < charts.size(); i++) {
				Long max = ((BigDecimal) charts.get(i).get("MAXFAD")).longValue();
				if (max > monFad) {
					monFad = max;
					if (charts.get(i).get("OCCURTIME") != null) {
						monTime = charts.get(i).get("OCCURTIME").toString();
					}
				}
			}

			Map<String, Object> chart = charts.get(charts.size() - 1);
			if (chart.keySet().contains("MD_DATA")) {
				Double declareMD = Double.valueOf(chart.get("MD_DATA").toString());
				result.put("declareMD", declareMD);
			}
		}
		result.put("monFad", monFad);
		result.put("monTime", monTime);
		// 变压器容量、参考值
		result.put("volume", volume);
		return result;
	}

	@Override
	public Map<String, Object> feeAnalysis(Long objId, Integer objType, String dateStr) {
		int queryType = 1;// 默认查询电费, 0全部费用 1电费
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryType", queryType);
		Date strDate = DateUtil.convertStrToDate(dateStr, DateUtil.SHORT_PATTERN);
		queryMap.put("baseTime", DateUtil.convertDateToStr(strDate, "yyyy-MM"));
		queryMap.put("objId", objId);
		queryMap.put("objType", objType);
		result.putAll(getMonthFee(queryMap));
		// 图表数据(近12个月每个月的总电费)
		List<Map<String, Object>> charts = new ArrayList<Map<String, Object>>();
		String baseTime = processFeeBaseDate(dateStr); // 处理电费分析基准日期
		Date baseDate = DateUtil.convertStrToDate(baseTime + "-01", DateUtil.SHORT_PATTERN);
		for (int i = 0; i < 12; i++) {
			Map<String, Object> temp = new HashMap<String, Object>();

			int n = i - 11;
			Date date = DateUtil.getPreSomeMonthFirstDay(baseDate, n);
			String time = DateUtil.convertDateToStr(date, "yyyy-MM");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("baseTime", time);
			map.put("objId", objId);
			map.put("objType", objType);
			map.put("queryType", queryType);
			Double totalFee = getMonthTotalFee(map);

			temp.put("month", time);
			temp.put("totalFee", totalFee);
			charts.add(temp);
		}
		result.put("charts", charts);

		return result;
	}

	private String processFeeBaseDate(String dateStr) {
		Date date1 = DateUtil.convertStrToDate(dateStr, DateUtil.SHORT_PATTERN);
		Date date2 = new Date();
		int year1 = DateUtil.getYear(date1);
		int year2 = DateUtil.getYear(date2);
		int month1 = DateUtil.getMonth(date1);
		int month2 = DateUtil.getMonth(date2);
		int yearInterval = year2 - year1;
		int monthInterval = month2 - month1;
		if (yearInterval >= 2 || (yearInterval == 1 && monthInterval >= 0)) {
			dateStr = dateStr;
		} else {
			dateStr = DateUtil.convertDateToStr(date2, DateUtil.SHORT_PATTERN);
		}
		Date strDate = DateUtil.convertStrToDate(dateStr, DateUtil.SHORT_PATTERN);
		return DateUtil.convertDateToStr(strDate, "yyyy-MM");
	}

	private Map<String, Double> getMonthFee(Map<String, Object> queryMap) {
		Map<String, Double> map = new HashMap<String, Double>();
		Double jFee = null; // 尖
		Double fFee = null; // 峰
		Double pFee = null; // 平
		Double gFee = null; // 谷
		Double baseFee = null; // 基本
		Double adjustFee = null; // 力调
		Map<String, Object> data = costService.calEmoDcpEleFee(queryMap);
		Set<String> keys = data.keySet();
		if (keys.contains("fee")) {
			Object obj = data.get("fee");
			if (obj instanceof ArrayList) {
				List<RateFeeBean> list = (ArrayList<RateFeeBean>) obj;
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						RateFeeBean rateFeeBean = list.get(i);
						double fee = Math.round(rateFeeBean.getFee());
						String sectorId = rateFeeBean.getSectorId();
						if (StringUtils.isNotEmpty(sectorId)) {
							if ("1".equals(sectorId)) {
								jFee = fee;
							}
							if ("2".equals(sectorId)) {
								fFee = fee;
							}
							if ("3".equals(sectorId)) {
								pFee = fee;
							}
							if ("4".equals(sectorId)) {
								gFee = fee;
							}
						}
						int feeId = rateFeeBean.getFeeId();
						if (feeId == 4) {
							baseFee = fee;
						}
						if (feeId == 5) {
							adjustFee = fee;
						}
					}
				}
			}
		}
		if (jFee != null) {
			map.put("jFee", jFee);
		}
		if (fFee != null) {
			map.put("fFee", fFee);
		}
		if (pFee != null) {
			map.put("pFee", pFee);
		}
		if (gFee != null) {
			map.put("gFee", gFee);
		}
		if (baseFee != null) {
			map.put("baseFee", baseFee);
		}
		if (adjustFee != null) {
			map.put("adjustFee", adjustFee);
		}
		return map;
	}

	private Double getMonthTotalFee(Map<String, Object> queryMap) {
		Double totalFee = 0D;
		Map<String, Object> data = costService.calEmoDcpEleFee(queryMap);
		Set<String> keys = data.keySet();
		if (keys.contains("fee")) {
			Object obj = data.get("fee");
			if (obj instanceof ArrayList) {
				List<RateFeeBean> list = (ArrayList<RateFeeBean>) obj;
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						RateFeeBean rateFeeBean = list.get(i);
						double fee = Math.round(rateFeeBean.getFee());
						totalFee = DataUtil.doubleAdd(totalFee, fee);
					}
				}
			}
		}
		return totalFee;
	}

	@Override
	public Integer getMeterLevelById(Long meterId) {
		return this.phoneMapper.getMeterLevelById(meterId);
	}

	@Override
	public Map<String, Object> lossAnalysis(String beginTime, String endTime, Long objectId, int type) {
		Date startDate = DateUtil.convertStrToDate(beginTime, DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(endTime, DateUtil.SHORT_PATTERN);
		Map<String, Object> result = new HashMap<String, Object>();
		int lineLoss = 1;// 为0表示“该分户没配线损”
		List<Map<String, Object>> charts = new ArrayList<Map<String, Object>>();

		List<LineLossBean> lineData = new ArrayList<LineLossBean>();
		List<Long> meterList = new ArrayList<Long>();
		if (type == 1) {
			meterList = this.schedulingMapper.getLineLossByLevel(objectId, type);
		} else if (type == 2) {
			meterList = this.phoneMapper.getChildMetersByParent(objectId);
		}

		if (meterList != null && meterList.size() > 0) {
			String meterIds = processIds(meterList);
			/* 后面为修改 */
			List<List<Long>> midsArr = new ArrayList<List<Long>>(  );
			List<Long> mids = new ArrayList<Long>();
			
			
			if (meterIds.split(",").length > 0) {
				String[] ms = meterIds.split(",");
				for (int i = 0; i < ms.length; i++) {
					mids.add(Long.parseLong(ms[i]));
					if( mids.size() == 50 ){
						midsArr.add( mids );
						mids = new ArrayList<Long>();
					} else if( ms.length - 1 == i ){
						midsArr.add( mids );
					}
				}
			}
			;
			for ( List<Long> midsList : midsArr ) {
				lineData.addAll( schedulingMapper.getDayLineLossInfo(midsList, startDate, endDate) );
			}
			for (LineLossBean lb : lineData) {
				List<Long> meters = schedulingMapper.getLineLossMeters(lb.getMeterId(), type + 1);
				if (meters == null || meters.size() == 0)
					continue;
				meterIds = processIds(meters);
				/* 后面为修改 */List<Long> mids2 = new ArrayList<Long>();
				if (meterIds.split(",").length > 0) {
					String[] ms = meterIds.split(",");
					for (int i = 0; i < ms.length; i++) {
						mids2.add(Long.parseLong(ms[i]));
					}
				}
				;
				List<LineLossBean> childLineData = schedulingMapper.getDayLineLossInfo(mids2, startDate, endDate);
				lb.setChildMeters(childLineData);
			}
		} else if (type == 1) {
			lineLoss = 0;
		}

		for (int i = 0; i < lineData.size(); i++) {
			LineLossBean lineLossBean = lineData.get(i);
			List<LineLossBean> childs = lineLossBean.getChildMeters();
			if (childs != null && childs.size() > 0) {
				Long meterId = lineLossBean.getMeterId();
				String meterName = lineLossBean.getMeterName();
				double coul = lineLossBean.getCoul();// 供电量
				double use = 0;// 用电量
				// double fa = 0;//发电量
				for (int j = 0; j < childs.size(); j++) {
					LineLossBean oneChild = childs.get(j);
					use = DataUtil.doubleAdd(use, oneChild.getCoul());
					// int attribute = oneChild.getAttributeId();
					// if(attribute == 1){
					// use = use + oneChild.getCoul();
					// }
					// else if(attribute == 2){
					// fa = fa + oneChild.getCoul();
					// }
				}
				double lossEle = Math.abs(DataUtil.doubleSubtract(coul, use));// 损失电量
				double lossRate = DataUtil.doubleDivide(DataUtil.doubleMultiply(100, lossEle),
						(new BigDecimal(coul).compareTo(BigDecimal.ZERO) == 0 ? 1 : coul));// 线损率
				lossRate = DoubleUtils.getDoubleValue(lossRate, 2);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("meterId", meterId);
				map.put("meterName", meterName);
				map.put("coul", Math.round(coul));
				map.put("use", Math.round(use));
				// map.put("fa", Math.round(fa));
				map.put("lossEle", Math.round(lossEle));
				map.put("lossRate", lossRate);
				charts.add(map);
			}
		}
		result.put("charts", charts);
		result.put("lineLoss", lineLoss);
		return result;
	}

	private String processIds(List<Long> meterLevel1) {
		StringBuffer meterIds = new StringBuffer("");
		for (int i = 0; i < meterLevel1.size(); i++) {
			if (i == (meterLevel1.size() - 1))
				meterIds.append(meterLevel1.get(i));
			else
				meterIds.append(meterLevel1.get(i)).append(",");
		}
		return meterIds.toString();
	}

	@Override
	public Map<String, Object> getEventDetail(Map<String, Object> queryMap) {
		List<EventBean> list = this.eventQueryMapper.eventDetail(queryMap);
		if (list != null && list.size() > 0) {
			Map<String, Object> result = new HashMap<String, Object>();

			EventBean eventBean = this.eventQueryService.processEventCtpt(list.get(0));
			result.put("EVENT_START_TIME", eventBean.getEventStartTime());
			result.put("EVENT_NAME", eventBean.getEventName());
			result.put("EVENT_END_TIME", eventBean.getEventEndTime());
			result.put("CONTENT", eventBean.getEventContent());
			int objectType = eventBean.getObjectType();
			Long objectId = eventBean.getObjectId();
			result.put("OBJECT_TYPE", objectType);
			result.put("OBJECT_ID", objectId);
			if (objectType == 1) {
				LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId(objectId);
				String ledgerName = "";
				if (ledgerBean != null) {
					ledgerName = ledgerBean.getLedgerName();
				}
				result.put("LEDGER_NAME", ledgerName);
			} else if (objectType == 2) {
				MeterBean meterBean = meterManagerMapper.getMeterDataByPrimaryKey(objectId);
				String meterName = "";
				String ledgerName = "";
				if (meterBean != null) {
					meterName = meterBean.getMeterName();
					ledgerName = meterBean.getLedgerName();
				}
				result.put("METER_NAME", meterName);
				result.put("LEDGER_NAME", ledgerName);
			}
			return result;
		} else {
			return null;
		}
	}

	@Override
	public Map<String, Object> getNewsDetail(Long infoId) {
		List<Map<String, Object>> list = this.phoneMapper.getNewsById(infoId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> getNewsPageList(Map<String, Object> queryMap) {
		List<Map<String, Object>> list = this.phoneMapper.getNewsPageList(queryMap);
		return list;
	}

	@Override
	public List<EventBean> getEventPageList(Map<String, Object> queryMap) {
		Long accountId = Long.valueOf(queryMap.get("accountId").toString());
		UserBean userBean = getUserByAccountId(accountId);
		Long ledgerId = userBean.getLedgerId();
		queryMap.put("ledgerId", ledgerId);
		List<EventBean> list = this.phoneMapper.getEventPageList(queryMap);
		return list;
	}

	@Override
	public List<Map<String, Object>> getReportPageList(Map<String, Object> queryMap) {
		List<Map<String, Object>> list = this.phoneMapper.reportPageList(queryMap);
		return list;
	}

	@Override
	public List<Map<String, Object>> getMessagePageList(Map<String, Object> queryMap) {
		List<Map<String, Object>> list = this.phoneMapper.messagePageList(queryMap);
		return list;
	}

	@Override
	public List<Map<String, Object>> getMessageReportPageList(Map<String, Object> queryMap) {
		List<Map<String, Object>> list = this.phoneMapper.getMessageReportPageList(queryMap);
		return list;
	}

	@Override
	public Map<String, Object> getReportDetail(Long reportId) {
		List<Map<String, Object>> list = this.phoneMapper.getReportById(reportId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Map<String, Object> getMessageDetail(Long messageId) {
		List<Map<String, Object>> list = this.phoneMapper.getMessagebyId(messageId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> getBookRecord(Long accountId) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> list1 = this.phoneMapper.getBookIdByAccount(accountId, 1);
		result.addAll(list1);
		List<Map<String, Object>> list2 = this.phoneMapper.getBookIdByAccount(accountId, 2);
		Map<String, Object> defindMsgMap = new HashMap<String, Object>();
		if (list2 != null && list2.size() > 0) {
			for (int i = 0; i < list2.size(); i++) {
				Map<String, Object> map = list2.get(i);
				Long bookId = Long.valueOf(map.get("BOOK_ID").toString());
				if (bookId == 1) {
					defindMsgMap.put("BOOK_ID", "definedMsg");
					result.add(defindMsgMap);
				}
				String tagName = getMessageTag(bookId);
				map.put("BOOK_ID", tagName);
			}
		}
		result.addAll(list2);
		return result;
	}

	/**
	 * bookId=1 表示 报告、自定义消息（两者归于一类）； bookId=2 表示 新闻； bookId=3 表示 政策；
	 */
	private String getMessageTag(long bookId) {
		String tagName = "";
		if (bookId == 1) {
			tagName = "report";
		} else if (bookId == 2) {
			tagName = "news";
		} else if (bookId == 3) {
			tagName = "policy";
		}
		return tagName;
	}

	@Override
	public void longTimeNotLogin() {
		List<Long> userIds = this.phoneMapper.getAllCompAccount("user");
		if (userIds == null || userIds.size() == 0) {
			return;
		}
		// 得到提醒周期配置
		List<Integer> cyleList = new ArrayList<Integer>();
		Map<String, Object> remind = this.messageMapper.getAutoReminderSet();
		if (remind == null) {
			return;
		}
		String noLogin = remind.get("NOLOGIN").toString();
		char[] chars = noLogin.toCharArray();
		int[] cyles = { 3, 7, 14, 30, 60, 90 };
		for (int i = 0; i < chars.length; i++) {
			char one = chars[i];
			if (one == '1') {
				cyleList.add(cyles[i]);
			}
		}
		if (cyleList.size() != 0) {

			// 循环企业用户
			for (Long userId : userIds) {
				UserBean userBean = getUserByAccountId(userId);
				Date lastDate = userBean.getLastDate();
				if (lastDate == null) {
					continue;
				}
				Integer interval = DateUtil.daysBetweenDates(new Date(), lastDate);
				if (cyleList.contains(interval)) {
					// 发推送
					List<String> strUserIds = new ArrayList<String>();
					strUserIds.add(userId.toString());
					Map<String, String> para = new HashMap<String, String>();
					String title = "登录提醒";
					String content = "尊敬的" + userBean.getLoginName() + "，您已连续" + interval.toString()
							+ "天未登陆能效平台系统，请及时登录系统！";
					if (JPushUtil.checkSendEnable(userBean.getFreeTimePeriod(), userBean.getIsShield())) { // 不在时段内则立即推送,
																											// TODO
																											// 在时段内暂不处理
						para.put("triggerType", "1");
						JPushUtil.sendPushByAlias(strUserIds, title, content, para);
					}
				}
			}
		}
	}

	@Override
	public void reportNotRead() {
		List<Long> userIds = this.phoneMapper.getAllCompAccount("user");
		if (userIds == null || userIds.size() == 0) {
			return;
		}
		// 得到提醒周期配置
		List<Integer> cyleList = new ArrayList<Integer>();
		Map<String, Object> remind = this.messageMapper.getAutoReminderSet();
		if (remind == null) {
			return;
		}
		String unRead = remind.get("UNREAD").toString();
		char[] chars = unRead.toCharArray();
		int[] cyles = { 1, 2, 3, 5, 10, 15 };
		for (int i = 0; i < chars.length; i++) {
			char one = chars[i];
			if (one == '1') {
				cyleList.add(cyles[i]);
			}
		}
		if (cyleList.size() != 0) {

			// 循环企业用户
			for (Long userId : userIds) {
				UserBean userBean = getUserByAccountId(userId);
				Long ledgerId = userBean.getLedgerId();
				Date lastDate = userBean.getLastDate();
				if (lastDate == null) {
					continue;
				}
				Integer interval = DateUtil.daysBetweenDates(new Date(), lastDate);
				if (cyleList.contains(interval)) {
					Integer reportNum = this.phoneMapper.getReportNumByUser(userId, lastDate, new Date(), ledgerId);// 最后一次登陆时间到现在的报告数
					if (reportNum > 0) {
						// 判断是否在屏蔽时间段内，拼接接收规则
						List<String> strUserIds = new ArrayList<String>();
						strUserIds.add(userId.toString());
						Map<String, String> para = new HashMap<String, String>();
						String title = "服务报告提醒";
						String content = "尊敬的" + userBean.getLoginName() + "，您共有" + reportNum.toString()
								+ "条新报告，敬请查看，可登陆系统进行相关处理!";
						if (JPushUtil.checkSendEnable(userBean.getFreeTimePeriod(), userBean.getIsShield())) { // 不在时段内则立即推送,
																												// TODO
																												// 在时段内暂不处理
							para.put("triggerType", "1");
							JPushUtil.sendPushByAlias(strUserIds, title, content, para);
						}
					}
				}
			}
		}
	}

	@Override
	public void checkUserMessage(List<Long> list) {
		List<Long> userIds = new ArrayList<Long>();
		if (list == null) {
			userIds = this.phoneMapper.getAllCompAccount("user");
		} else {
			userIds = list;
		}

		if (userIds.size() == 0) {
			return;
		}
		// 得到消息条数配置
		List<Integer> cyleList = new ArrayList<Integer>();
		Map<String, Object> remind = this.messageMapper.getAutoReminderSet();
		if (remind == null) {
			return;
		}
		String news = remind.get("NEWS").toString();
		char[] chars = news.toCharArray();
		int[] cyles = { 5, 10, 20, 50, 100, 500 };
		for (int i = 0; i < chars.length; i++) {
			char one = chars[i];
			if (one == '1') {
				cyleList.add(cyles[i]);
			}
		}
		if (cyleList.size() != 0) {

			// 循环用户
			for (Long userId : userIds) {
				UserBean userBean = getUserByAccountId(userId);
				Long ledgerId = userBean.getLedgerId();
				Date lastDate = userBean.getLastDate();
				if (lastDate == null) {
					continue;
				}
				Integer reportNum = this.phoneMapper.getReportNumByUser(userId, lastDate, new Date(), ledgerId);// 报告消息数
				Integer defineMessageNum = this.phoneMapper.getDefineMessageNum(userId, lastDate, new Date());// 自定义消息数
				Integer newsPlicyNum = this.phoneMapper.getNewsPlicyNum(userId, lastDate, new Date());// 新闻、政策消息数
				Integer eventNum = this.phoneMapper.getEventNum(userId, lastDate, new Date(), ledgerId);// 事件消息数
				Integer num = reportNum + defineMessageNum + newsPlicyNum + eventNum; // 总消息条数
				if (cyleList.contains(num)) {
					// 发推送
					List<String> strUserIds = new ArrayList<String>();
					strUserIds.add(userId.toString());
					Map<String, String> para = new HashMap<String, String>();
					String title = "新信息条数提醒";
					String content = "尊敬的" + userBean.getLoginName() + "，您共有" + num.toString()
							+ "条新信息，敬请查看，可登陆系统进行相关处理!";
					if (JPushUtil.checkSendEnable(userBean.getFreeTimePeriod(), userBean.getIsShield())) { // 不在时段内则立即推送,
																											// TODO
																											// 在时段内暂不处理
						para.put("triggerType", "1");
						JPushUtil.sendPushByAlias(strUserIds, title, content, para);
					}
				}
			}
		}
	}

	@Override
	public Map<String, Integer> getLevelHelp(Long accountId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		int curLevel = 0;
		int nextLevel = 1;
		int nextNeedScore = 0;

		Integer curScore = this.phoneMapper.getUserCurScore(accountId);
		if (curScore == null) {
			curScore = 0;
		}
		int[] cyles = WebConstant.cyles;
		for (int i = 0; i < cyles.length; i++) {
			if (i != cyles.length - 1) {
				if (curScore >= cyles[i] && curScore < cyles[i + 1]) {
					curLevel = i + 1;
					nextLevel = i + 2;
					nextNeedScore = cyles[i + 1] - curScore;
					break;
				}
			} else {
				curLevel = i + 1;
				nextLevel = i + 1;
				nextNeedScore = 0;
			}
		}

		map.put("curLevel", curLevel);
		map.put("nextLevel", nextLevel);
		map.put("curScore", curScore);
		map.put("nextNeed", nextNeedScore);
		return map;
	}

	@Override
	public Map<String, Integer> getLevelRegion(int level) {
		int[] cyles = WebConstant.cyles;
		int begin = -1;
		int end = -1;
		if (level >= 1 && level <= cyles.length) {
			begin = cyles[level - 1];
			if (level != cyles.length) {
				end = cyles[level];
			}
		}
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("begin", begin);
		result.put("end", end);
		return result;
	}

	@Override
	public Map<String, Object> getLastAssessment(Long accountId) {
		Map<String, Object> result = new HashMap<String, Object>();
		String message = "";
		Map<String, Object> lastAssessment = new HashMap<String, Object>();
		UserBean userBean = this.phoneMapper.getUserByAccountId(accountId);
		if (userBean.getLedgerId() != null) {
			Long ledgerId = userBean.getLedgerId();
			LedgerBean ledger = this.phoneMapper.getLedgerById(ledgerId);
			if (ledger != null && ledger.getAnalyType() == 102) {
				List<Map<String, Object>> assessmentList = this.phoneMapper.getUserLastAssessment(accountId);
				if (assessmentList != null && assessmentList.size() > 0) {
					lastAssessment = assessmentList.get(0);
					Date scoreTime = (Date) lastAssessment.get("SCORE_TIME");
					int interval = DateUtil.getMinBetweenTime(scoreTime, new Date());
					String interTime = "";
					if (interval >= 24 * 60) {
						int day = interval / (24 * 60);
						interTime = day + "天";
					} else if (interval >= 60 && interval < 24 * 60) {
						int hour = interval / 60;
						interTime = hour + "小时";
					} else if (interval >= 1 && interval < 60) {
						interTime = interval + "分钟";
					} else {
						interTime = "1分钟";
					}
					lastAssessment.put("DAYS", interTime);
				} else {
					message = "您尚未体检,请立即体检！";
				}
			} else {
				message = "该用户不是企业用户！";
			}
		} else {
			message = "该用户不是企业用户！";
		}
		result.put("errMessage", message);
		result.put("lastAssessment", lastAssessment);
		return result;
	}

	@Override
	public int getBeatUserPercent(Long accountId, Integer score) {
		int result = 0;
		int allNum = this.phoneMapper.getAllCheckNum();
		int beatNum = this.phoneMapper.getBeatCheckNum(accountId, score);
		result = (100 * beatNum) / allNum;
		return result;
	}

	@Override
	public Integer getLastAssessScore(Long accountId) {
		Integer result = null;
		List<Map<String, Object>> assessmentList = this.phoneMapper.getUserLastAssessment(accountId);
		if (assessmentList != null && assessmentList.size() > 0) {
			Map<String, Object> lastAssessment = assessmentList.get(0);
			int totalScore = Integer.valueOf(lastAssessment.get("TOTAL_SCORE").toString());
			result = totalScore;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linyang.energy.service.PhoneService#getAllCompAccount(java.lang.
	 * String)
	 */
	@Override
	public List<Long> getAllCompAccount(String user) {
		return this.phoneMapper.getAllCompAccount(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linyang.energy.service.PhoneService#getNextEMO(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getNextEMO(Map<String, Object> queryMap) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		List<LedgerBean> ledgerList = ledgerManagerMapper.getLedgerInfoList(queryMap);
		// 过滤多余字段并判断是否有子节点
		Map<String, Object> ledgerMap;
		for (LedgerBean ledgerBean : ledgerList) {
			ledgerMap = new HashMap<String, Object>();
			ledgerMap.put("id", ledgerBean.getLedgerId());
			ledgerMap.put("name", ledgerBean.getLedgerName());
			ledgerMap.put("depth", ledgerBean.getDepth());
			ledgerMap.put("next", ledgerManagerMapper.getNextLedgerAmount(ledgerBean.getLedgerId()) > 0 ? true : false);
			dataList.add(ledgerMap);
		}
		return dataList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linyang.energy.service.PhoneService#getNextDCP(long)
	 */
	@Override
	public List<Map<String, Object>> getNextDCP(long ledgerId) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		List<MeterBean> meterList = phoneMapper.getEMOModel1(ledgerId);
		Map<String, Object> meterMap;
		for (MeterBean meterBean : meterList) {
			meterMap = new HashMap<String, Object>();
			meterMap.put("id", meterBean.getMeterId());
			meterMap.put("name", meterBean.getMeterName());
			dataList.add(meterMap);
		}
		return dataList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linyang.energy.service.PhoneService#getEMOPath(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getEMOPath(Map<String, Object> queryMap) {
		return ledgerManagerMapper.getEMOPath(queryMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linyang.energy.service.PhoneService#getHeadChartData(int, int)
	 */
	@Override
	public Map<String,Object> getHeadChartData(UserBean userBean) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		// 电量数据汇总
		Map<String,Object> indexMap = this.showPhoneIndex(userBean,1);
		double energySum = 0;
		List<Map<String, Object>> currList = new ArrayList<Map<String,Object>>();  
		List<Map<String, Object>> currMonRateQ = (List<Map<String, Object>>) indexMap.get("currMonRateQ");
		for (Map<String, Object> map : currMonRateQ) {
			if(map == null)
				continue;
			Object objQ = map.get("Q");
			Object objRate = map.get("RATENO");
			if(objQ == null || objRate == null )
				continue;
			energySum = energySum + Math.abs(Double.parseDouble(objQ.toString()));
			currList.add(map);
		}
		// 当月分户总电量、电费、功率因数、最大需量、费率电量占比
		String energySumUnit;
		if(energySum/(10000) > 1){
			energySumUnit = "万kWh";
			resultMap.put("energySum", new DecimalFormat("0.00").format(energySum/10000));
		}
//		if(energySum/(10000) > 1){
//			energySumUnit = "MWh";
//			resultMap.put("energySum", new DecimalFormat("0.00").format(energySum/1000));
//		}
		else {
			energySumUnit = "kWh";
			resultMap.put("energySum", new DecimalFormat("0").format(energySum));
		}
		resultMap.put("energySumUnit", energySumUnit);
		double currMonFee = (Double.parseDouble(indexMap.get("currMonFee")!=null?indexMap.get("currMonFee").toString():"0"))/10000;
		resultMap.put("currMonFee", new DecimalFormat("#.00").format(currMonFee));
		resultMap.put("currMaxDemand", new DecimalFormat("0").format(indexMap.get("currMaxDemand")!=null?indexMap.get("currMaxDemand"):0));
		resultMap.put("factor", indexMap.get("factor"));
		resultMap.put("currFactor", indexMap.get("currFactor"));
		resultMap.put("data", currList);
		return resultMap;
	}


	@Override
	public Map<String, Object> getHeadChartDataByScreen(UserBean userBean, LedgerBean ledgerBean){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		// 电量数据汇总
		Map<String,Object> indexMap = this.showPhoneIndex(userBean,1);

		ConsMonthAmt consMonthAmt = enterpriseMapper.consMonthAmt(ledgerBean.getUserNo(), "2019-09");
		// 基本电费
		consMonthAmt.setBa(consMonthAmt.getBa().divide(new BigDecimal(30),BigDecimal.ROUND_HALF_UP,4).multiply(new BigDecimal(getDate("dd",-1))));
		// 本月电量
		BigDecimal nowMonElec = new BigDecimal(0);
		BigDecimal jElec = new BigDecimal(0);
		BigDecimal fElec = new BigDecimal(0);
		BigDecimal pElec = new BigDecimal(0);
		BigDecimal gElec = new BigDecimal(0);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<ConsDayEnergy> list = enterpriseMapper.consDayEnergy3(ledgerBean.getUserNo(), getDate("yyyy-MM-dd",-30), getDate("yyyy-MM-dd",-1));
		for (ConsDayEnergy dayElec:list) {
			try {
				int nowMon = getMonth(dateFormat.parse(getDate("yyyy-MM-dd",-1)));
				int dateMon = getMonth(dateFormat.parse(dayElec.getDataDate()));
				if(nowMon == dateMon){
					nowMonElec = nowMonElec.add(dayElec.getPapE());
					jElec = jElec.add(dayElec.getPapE1());
					fElec = fElec.add(dayElec.getPapE2());
					pElec = pElec.add(dayElec.getPapE3());
					gElec = gElec.add(dayElec.getPapE4());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		};
		// 电费单价
		BigDecimal elecPrice = consMonthAmt.getPreTAmt().divide(consMonthAmt.getPreTSettlePq(),BigDecimal.ROUND_HALF_UP,4);
		// 本月/峰/平/谷电费
		double nowMonFee = (elecPrice.multiply(nowMonElec).add(consMonthAmt.getBa())).doubleValue();
		List<Map<String, Object>> currList = new ArrayList<Map<String,Object>>();
		Map<String, Object> jmap = new HashMap<String, Object>();
		jmap.put("RATENO",1);
		jmap.put("Q",elecPrice.multiply(jElec));
		currList.add(jmap);
		Map<String, Object> fmap = new HashMap<String, Object>();
		fmap.put("RATENO",2); fmap.put("Q",elecPrice.multiply(fElec));
		currList.add(fmap);
		Map<String, Object> pmap = new HashMap<String, Object>();
		pmap.put("RATENO",3); pmap.put("Q",elecPrice.multiply(pElec));
		currList.add(pmap);
		Map<String, Object> gmap = new HashMap<String, Object>();
		gmap.put("RATENO",4); gmap.put("Q",elecPrice.multiply(gElec));
		currList.add(gmap);

		// 当月分户总电量、电费、功率因数、最大需量、费率电量占比
		double energySum = nowMonElec.doubleValue();
		String energySumUnit;
		if(energySum/(10000) > 1){
			energySumUnit = "万kWh";
			resultMap.put("energySum", new DecimalFormat("0.00").format(energySum/10000));
		}
//		if(energySum/(10000) > 1){
//			energySumUnit = "MWh";
//			resultMap.put("energySum", new DecimalFormat("0.00").format(energySum/1000));
//		}
		else {
			energySumUnit = "kWh";
			resultMap.put("energySum", new DecimalFormat("0").format(energySum));
		}
		resultMap.put("energySumUnit", energySumUnit);
		resultMap.put("currMonFee", new DecimalFormat("#.00").format(nowMonFee/10000));
		resultMap.put("currMaxDemand", new DecimalFormat("0").format(indexMap.get("currMaxDemand")!=null?indexMap.get("currMaxDemand"):0));
		resultMap.put("factor", indexMap.get("factor"));
		resultMap.put("currFactor", indexMap.get("currFactor"));
		resultMap.put("data", currList);
		return resultMap;
	}

	/**
	 * 获取指定日期
	 * @param dateFormat 日期格式
	 * @param dateInterval
	 * @return
	 */
	public String getDate(String dateFormat, int dateInterval){
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		date.add(Calendar.DAY_OF_YEAR, dateInterval);
		String dataDate = df.format(date.getTime());
		return dataDate;
	}

	/**
	 * 获取月份
	 */
	public int getMonth(Date dataDate){
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(dataDate);
		return tempStart.get(Calendar.MONTH) + 1;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linyang.energy.service.PhoneService#modifyAppPrefer(long,
	 * java.lang.String, int)
	 */
	@Override
	public boolean modifyAppPrefer(long accountId, String config, int type) {
		AppPreferBean preferBean = phoneMapper.getAppPrefer(accountId, type);
		if (preferBean == null) { // 无记录
			phoneMapper.insertAppPrefer(accountId, config, type);
		} else { // 记录为空串或非空
			String set = config;
			if (type == 2 && StringUtils.isNotBlank(preferBean.getOptions())) {
				set = preferBean.getOptions() + "," + config;
			}
			phoneMapper.updateAppPrefer(accountId, set, type);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linyang.energy.service.PhoneService#getAppPrefer(long, int)
	 */
	@Override
	public Map<String, Object> getAppPrefer(long accountId, int type) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		AppPreferBean preferBean = phoneMapper.getAppPrefer(accountId, type);
		String oldPrefer;
		if (preferBean == null || preferBean.getOptions() == null)
			oldPrefer = "";
		else
			oldPrefer = preferBean.getOptions();
		if (type == 1) {
			resultMap.put("OPTIONS", oldPrefer);
		} else {
			if (StringUtils.isBlank(oldPrefer)) {
				resultMap.put("OPTIONS", oldPrefer);
			} else {
				StringBuilder newPreferBuild = null;
				String[] oldPreferArray = oldPrefer.split(",");
				LedgerBean ledgerBean;
				for (String singlePrefer : oldPreferArray) {
					ledgerBean = ledgerManagerMapper.selectByLedgerId(Long.parseLong(singlePrefer));
					if (ledgerBean != null) {

						if (newPreferBuild == null) {
							newPreferBuild = new StringBuilder();
							newPreferBuild.append(ledgerBean.getLedgerId()).append("-")
									.append(ledgerBean.getLedgerName());
						} else {
							newPreferBuild.append(",").append(ledgerBean.getLedgerId()).append("-")
									.append(ledgerBean.getLedgerName());
						}
					}
				}
				resultMap.put("OPTIONS", (newPreferBuild == null ? "" : newPreferBuild.toString()));
			}
		}
		return resultMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linyang.energy.service.PhoneService#disAppPrefer(long,long)
	 */
	@Override
	public boolean disAppPrefer(long accountId, long objId) {
		AppPreferBean preferBean = phoneMapper.getAppPrefer(accountId, 2);
		if (preferBean == null || StringUtils.isBlank(preferBean.getOptions()))
			return true;
		String optionArray[] = preferBean.getOptions().split(",");
		optionArray = ArrayUtils.removeElements(optionArray, String.valueOf(objId));
		phoneMapper.updateAppPrefer(accountId, StringUtils.join(optionArray, ","), 2);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linyang.energy.service.PhoneService#getMsgList(int)
	 */
	@Override
	public List<Map<String, Object>> getMsgList(int type) {
		return messageMapper.getMsgList(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linyang.energy.service.PhoneService#getSingleAppPrefer(long,
	 * int)
	 */
	@Override
	public AppPreferBean getAppPreferBean(long accountId, int type) {
		return phoneMapper.getAppPrefer(accountId, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linyang.energy.service.PhoneService#getLedgerQ(java.lang.String,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	public List<Map<String, Object>> getLedgerQ(String ledgerIds, Date startTime, Date endTime) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> data;
		for (String ledgerIdStr : ledgerIds.split(",")) {
			data = new HashMap<String, Object>();
			Double lastMonCountQ = indexMapper.queryLedgerMonQ(Long.parseLong(ledgerIdStr), startTime, endTime);
			LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId(Long.parseLong(ledgerIdStr));
			if (ledgerBean != null)
				data.put("name", ledgerBean.getLedgerName());
			data.put("id", ledgerIdStr);
			//处理数据四舍五入
			if(lastMonCountQ==null) {
				lastMonCountQ=0d;
			}
			if(lastMonCountQ>=10000000) {
				lastMonCountQ=(double)Math.round(lastMonCountQ/1000000*100)*10000;
			}
			else if(lastMonCountQ>10000&&lastMonCountQ<10000000)
			{
				lastMonCountQ=(double)Math.round(lastMonCountQ/1000*100)*10;
			}
			else {
				lastMonCountQ=(double)(Math.round(lastMonCountQ*100))/100;
			}
			
			data.put("value", lastMonCountQ);
			resultList.add(data);
		}
		return resultList;
	}

	@Override
	public Double getLedgerSumRQ(Long objId, Date startDate, Date endDate) {
		return phoneMapper.getLedgerSumRQ(objId, startDate, endDate);
	}

	@Override
	public Integer getEventNumById(Long ledgerId, Date beginDate, Date endDate) {
		return phoneMapper.getEventNumById(ledgerId, beginDate, endDate);
	}

	@Override
	public String getScoreTips(Long accountId, Integer score) {
		String tips = "";
		int firstScore = phoneMapper.getFirstScore();
		if (firstScore == score) {
			tips = "您的能效管理水平为全国冠军，继续保持哦！";
			return tips;
		}
		List<Map<String, Object>> secondScores = phoneMapper.getSecondScores();
		for (Map<String, Object> map : secondScores) {
			Integer scoretemp = Integer.valueOf(map.get("TOTAL_SCORE").toString());
			if (scoretemp.equals(score)) {
				tips = "您的用电水平为全国第二，继续加油哦！";
				return tips;
			}
		}
		List<Map<String, Object>> thirdScores = phoneMapper.getThirdScores();
		for (Map<String, Object> map : thirdScores) {
			Integer scoretemp = Integer.valueOf(map.get("TOTAL_SCORE").toString());
			if (scoretemp.equals(score)) {
				tips = "您的用电水平进入了全国前三，继续加油哦！";
				return tips;
			}
		}
		List<Map<String, Object>> tenthScores = phoneMapper.getTenthScores();
		for (Map<String, Object> map : tenthScores) {
			Integer scoretemp = Integer.valueOf(map.get("TOTAL_SCORE").toString());
			if (scoretemp.equals(score)) {
				tips = "您的用电水平进入了全国十强，继续加油哦！";
				return tips;
			}
		}

		int result = 0;
		int allNum = this.phoneMapper.getAllCheckNum();
		int beatNum = this.phoneMapper.getBeatCheckNum(accountId, score);
		result = (100 * beatNum) / allNum;
		tips = "您目前击败了" + result + "%的用户，请继续加油哦！";

		return tips;
	}

	@Override
	public Map<String, Object> getUserLastScores(Long accountId) {
		return phoneMapper.getUserLastScores(accountId);
	}

	@Override
	public List<Long> getAccountIdByLedger() {
		return phoneMapper.getAccountIdByLedger();
	}

	@Override
	public Double getCurrentPF(long ledgerId) {
		return phoneMapper.getCurrentPF(ledgerId);
	}

	@Override
	public String getRegionById(long ledgerId) {
		return phoneMapper.getRegionById(ledgerId);
	}

	@Override
	public Long getMeterIdByTerId(Long terId) {
		return phoneMapper.getMeterIdByTerId(terId);
	}

	@Override
	public Integer getTerActiveStatus(Long terminalAddress) {
		return phoneMapper.getTerActiveStatus(terminalAddress);
	}

	@Override
	public void updateTerActiveStatus(Long terminalAddress) {
		phoneMapper.updateTerActiveStatus(terminalAddress);
	}

	@Override
	public Long addTerminal(Long terAddress) {
		// 生成主键id
		Long terId = SequenceUtils.getDBSequence();
		Date nowDate = DateUtil.convertStrToDate(DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN),
				DateUtil.SHORT_PATTERN);
		String terminalName = StringUtil.fillWith(terAddress.toString(), '0', 10, false);
		phoneMapper.addTerminal(terId, terAddress, nowDate, terminalName, nowDate);
		// phoneMapper.addEnergyTer(terId, terAddress, terminalName);
		phoneMapper.pCollPar(terId, terAddress);

		return terId;
	}

	@Override
	public void addMeterRelation(Long terId, Long terAddress, Long ledgerId) {
		Long meterId = SequenceUtils.getDBSequence();// 计量点id
		Long recId = SequenceUtils.getDBSequence();// 关联id
		phoneMapper.addMeter(meterId, terAddress.toString(), ledgerId, terId, terId);
		phoneMapper.addLedgerMeter(ledgerId, meterId);
		phoneMapper.addLinelossMeter(meterId);
		phoneMapper.addLedgerRelation(recId, meterId, terAddress.toString(), ledgerId);
		phoneMapper.addLedgerShow(ledgerId, meterId);
	}

	@Override
	public void bindTerminal(String openId, Long terId, Long ledgerId, Long accountId) {
		phoneMapper.bindTerminal(openId, terId, ledgerId, accountId);
	}

	@Override
	public void addOpenInfo(String openId, Long accountId) {
		Integer num = phoneMapper.getOpenNumById(openId);
		if (num == 0) {
			phoneMapper.addOpenInfo(openId, accountId);
		}
	}

	@Override
	public UserTerminalBean updateOpenInfo(String openId, String companyName, String tel, String address) {
		phoneMapper.updateOpenInfo(openId, companyName, tel, address);
		UserTerminalBean newBean = new UserTerminalBean();
		newBean.setOpenId(openId);
		newBean.setCompanyName(companyName);
		newBean.setAddress(address);
		newBean.setTel(tel);

		return newBean;
	}

	@Override
	public UserTerminalBean getOpenInfo(String openId) {
		return phoneMapper.getOpenInfo(openId);
	}

	@Override
	public List<UserTerminalBean> getTerminalInfoList(String openId) {
		List<UserTerminalBean> list = phoneMapper.getTerminalInfoList(openId);
		if (list != null && list.size() > 0) {
			for (UserTerminalBean userTerminalBean : list) {
				userTerminalBean.setTerminalAddress12(
						StringUtil.fillWith(userTerminalBean.getTerminalAddress().toString(), '0', 10, false));
			}
		}
		return phoneMapper.getTerminalInfoList(openId);
	}

	@Override
	public String updateTerminalInfo(Long terId, String terName, Integer shareLimit, Integer pt, Integer ct) {
		String msg = "";
		
		if (!"".equals(terName)) {
			//检查名称是否重复
			Integer num1 = phoneMapper.checkTerName(terName);
			Integer num2 = phoneMapper.checkLedgerName(terName);
			if (num1 > 0 || num2 > 0) {
				msg = "名称已存在,请重新设置";
				return msg;
			}	
			// 终端名称
			phoneMapper.updateTerName(terId, terName);
			//更改终端对应的ledger名称
			phoneMapper.updateTerLedgerName(terId, terName);
		}
						
		// 更改分享限制天数
		phoneMapper.updateShareLimit(terId, shareLimit);
		// 终端电流电压传感器倍率
		phoneMapper.updatePtCt(terId, pt, ct);
		
		return msg;
	}

	@Override
	public Long removeBind(Long terId) {
		UserTerminalBean bean = phoneMapper.getOpenTerInfo(terId);
		// 删除计量点
		phoneMapper.deleteMeter(bean.getLedgerId());
		// 删除用户
		phoneMapper.deleteUser(bean.getAccountId());
		// 删除采集平台mpoint
		phoneMapper.deleteTerminal(terId);
		// 删除终端
		phoneMapper.setTerActiveStatus(bean.getTerminalAddress());
		// 删除微信终端关系
		phoneMapper.deleteOpenTer(terId);

		// 返回应删除的ledgerid
		return bean.getLedgerId();
	}

	@Override
	public void updateTerminalPassword(Long terId, String password) {
		UserTerminalBean bean = phoneMapper.getOpenTerInfo(terId);
		// 终端密码
		phoneMapper.updateTerPassword(bean.getAccountId(), password);
	}

	@Override
	public UserTerminalBean checkTerPassword(Long terAddress, String inputPassword) {
		UserTerminalBean userTerminalBean = null;
		String password = phoneMapper.getPasswordByTer(terAddress);
		if (password.equals(inputPassword)) {
			userTerminalBean = phoneMapper.getTerminalInfo(terAddress);
			userTerminalBean.setTerminalAddress12(
					StringUtil.fillWith(userTerminalBean.getTerminalAddress().toString(), '0', 10, false));
		}
		return userTerminalBean;
	}

	@Override
	public UserTerminalBean getTerminalInfo(Long terAddress) {
		UserTerminalBean userTerminalBean = phoneMapper.getTerminalInfo(terAddress);
		if (userTerminalBean != null) {
			userTerminalBean.setTerminalAddress12(
					StringUtil.fillWith(userTerminalBean.getTerminalAddress().toString(), '0', 10, false));
		}
		return userTerminalBean;
	}

	@Override
	public List<Long> createTerAddress(Integer num, Long cloudId) {
		for (int i = 0; i < num; i++) {
			// 生成地址，插入地址表
			phoneMapper.createTerAddress(cloudId);
		}
		// 获取该id的终端地址列表
		List<Long> addressList = phoneMapper.getCloudAddressList(cloudId);
		return addressList;
	}

	@Override
	public Double getFaeValue(Long objId) {
		//获取云终端当前的总示值
		return phoneMapper.getFaeValue(objId);
	}

    @Override
    public boolean getIfUserCanGetData(UserBean user, Long objId, Integer objType){
        boolean result = false;

        List<Long> legerIds = new ArrayList<>();
        if(null != user.getLedgerId() && user.getLedgerId() != 0){ //非群组
            legerIds.add(user.getLedgerId());
        }
        else {
            legerIds = this.phoneMapper.getGroupUserLedgerIds(user.getAccountId());
        }

        if(CollectionUtil.isNotEmpty(legerIds)){

            if(objType == 1){
                LedgerBean ledgerBean = this.ledgerManagerMapper.selectByLedgerId(objId);
                for(int i = 0; i < legerIds.size(); i++){
                    LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(legerIds.get(i));
                    if(null != ledger && null != ledgerBean && ledgerBean.getLedgerLft() >= ledger.getLedgerLft() && ledgerBean.getLedgerRgt() <= ledger.getLedgerRgt()){
                        result = true;
                        break;
                    }
                }
            }
            else if(objType == 2){
                MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(objId);
                LedgerBean ledgerBean = this.ledgerManagerMapper.selectByLedgerId(meterBean.getLedgerId());
                for(int i = 0; i < legerIds.size(); i++){
                    LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(legerIds.get(i));
                    if(null != ledger && null != ledgerBean && ledgerBean.getLedgerLft() >= ledger.getLedgerLft() && ledgerBean.getLedgerRgt() <= ledger.getLedgerRgt()){
                        result = true;
                        break;
                    }
                }
            }

        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getNeedCurData(long objId, int objType, int dataType, int density, Date beginDate, Date endDate){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //dataType:1.电压;2.电流;3.有功功率;4.无功功率;5.功率因数;6.电量;7.电压相位角;8.电流相位角;9.需量曲线;10.谐波电压;11.谐波电流;12.电能示值;13.水量;14.水示值;
        if(dataType == 1){

            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "曲线电压只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                // 得到电源接线方式1,三相三线;2,三相四线;3:单相表
                Integer commMode = this.phoneMapper.queryCommMode(objId);
                param.put("commMode", commMode);
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = phoneMapper.getNeedPointVol(param);// 电压
            }

        }
        else if(dataType == 2){

            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "曲线电流只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                // 得到电源接线方式1,三相三线;2,三相四线;3:单相表
                Integer commMode = this.phoneMapper.queryCommMode(objId);
                param.put("commMode", commMode);
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = phoneMapper.getNeedPointCur(param);// 电流
            }

        }
        else if(dataType == 3){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("objId", objId);
            param.put("objType", objType);
            param.put("startDate", beginDate);
            param.put("endDate", endDate);
            list = this.phoneMapper.getNeedCurApData(param);
        }
        else if(dataType == 4){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("objId", objId);
            param.put("objType", objType);
            param.put("startDate", beginDate);
            param.put("endDate", endDate);
            list = this.phoneMapper.getNeedCurRpData(param);
        }
        else if(dataType == 5){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("objId", objId);
            param.put("objType", objType);
            param.put("startDate", beginDate);
            param.put("endDate", endDate);
            list = this.phoneMapper.getNeedCurPfData(param);
        }
        else if(dataType == 6){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("objId", objId);
            param.put("objType", objType);
            param.put("startDate", beginDate);
            param.put("endDate", endDate);
            param.put("density", density);
            list = this.phoneMapper.getNeedCurQData(param);
        }
        else if(dataType == 7){

            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "电压相位角曲线只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedCurAngleVData(param);
            }

        }
        else if(dataType == 8){

            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "电流相位角曲线只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedCurAngleIData(param);
            }

        }
        else if(dataType == 9){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("objId", objId);
            param.put("objType", objType);
            param.put("startDate", beginDate);
            param.put("endDate", endDate);
            list = this.phoneMapper.getNeedCurDData(param);
        }
        else if(dataType == 10){

            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "谐波电压曲线只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedCurHarVData(param);
            }

        }
        else if(dataType == 11){

            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "谐波电流曲线只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedCurHarIData(param);
            }

        }
        else if(dataType == 12){

            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "电能示值曲线只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedCurEData(param);
            }

        }
        else if(dataType == 13){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("objId", objId);
            param.put("objType", objType);
            param.put("startDate", beginDate);
            param.put("endDate", endDate);
            list = this.phoneMapper.getNeedCurWflowData(param);
        }
        else if(dataType == 14){

            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "水示值曲线只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedCurWData(param);
            }

        }

        return list;
    }

    @Override
    public List<Map<String, Object>> getNeedDayData(long objId, int objType, int dataType, Date beginDate, Date endDate){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //dataType: 1.电量(总);2.电量(复费率);3.电能示值(总);4.电能示值(复费率);5.日谐波电压含有量;6.日谐波电流极值;7.日最大需量;8.日水量;9.日水量示值;10.日气量;11.日气量示值;
        if(dataType == 1){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("objId", objId);
            param.put("objType", objType);
            param.put("startDate", beginDate);
            param.put("endDate", endDate);
            list = this.phoneMapper.getNeedDayQData(param);
        }
        else if(dataType == 2){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("objId", objId);
            param.put("objType", objType);
            param.put("startDate", beginDate);
            param.put("endDate", endDate);
            list = this.phoneMapper.getNeedDayQRateData(param);
        }
        else if(dataType == 3){
            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "电能示值只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedDayEData(param);
            }
        }
        else if(dataType == 4){
            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "电能示值只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedDayERateData(param);
            }
        }
        else if(dataType == 5){
            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "日谐波电压含有量只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedDayHarVData(param);
            }
        }
        else if(dataType == 6){
            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "日谐波电流极值只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedDayHarIData(param);
            }
        }
        else if(dataType == 7){
            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "日最大需量只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedDayDData(param);
            }
        }
        else if(dataType == 8){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("objId", objId);
            param.put("objType", objType);
            param.put("startDate", beginDate);
            param.put("endDate", endDate);
            list = this.phoneMapper.getNeedDayWData(param);
        }
        else if(dataType == 9){
            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "水量示值只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedDayW2Data(param);
            }
        }
        else if(dataType == 10){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("objId", objId);
            param.put("objType", objType);
            param.put("startDate", beginDate);
            param.put("endDate", endDate);
            list = this.phoneMapper.getNeedDayGData(param);
        }
        else if(dataType == 11){
            if (objType == 1){
                list.add(new HashMap<String, Object>(){{put("msg", "气量示值只针对采集点");}});
            }
            else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("objId", objId);
                param.put("startDate", beginDate);
                param.put("endDate", endDate);
                list = this.phoneMapper.getNeedDayG2Data(param);
            }
        }

        return list;
    }
	
	
	/**
	 * 查询复费率电量(能管对象)
	 * @author catkins.
	 * @param ledgerId
	 * @param dateType	3.10天,4.30天,5.1年
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/2/28 17:18
	 */
	@Override
	public Map<String, Object> queryLedgerFeeData(long ledgerId,Integer dateType,Date baseDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> eleList = new ArrayList<Map<String, Object>>();
		eleList.add(new HashMap<String, Object>( 0 ));
		eleList.add(new HashMap<String, Object>( 0 ));
		LedgerBean bean = phoneMapper.getLedgerById( ledgerId );
		List<RateSectorBean> rateSectorList = this.rateBeanMapper.getSectorData( bean.getRateId() );
		if ( dateType == 3 ) { //10日数据
			Date beginTime = DateUtil.getDateBetween(baseDate, -11);
			Date endTime = DateUtil.getDateBetween(baseDate, -1);
			map.put("list", phoneMapper.queryLedgerFeeCoul(ledgerId, beginTime, endTime,bean.getRateId()));
			map.put("max", phoneMapper.queryLedgerMaxFeeCoul(ledgerId, beginTime, endTime,bean.getRateId()));
			eleList.addAll( phoneMapper.queryLedgerFeeSum(ledgerId,  beginTime, endTime , dateType,bean.getRateId()) );
			map.put("energySum", eleList );
		} else if( dateType == 4 ){	// 月数据
			Date beginTime = DateUtil.getDateBetween(DateUtil.getLastMonthDate(baseDate), -1);
			Date endTime = DateUtil.getDateBetween(baseDate, -1);
			map.put("list", phoneMapper.queryLedgerFeeCoul(ledgerId, beginTime, endTime,bean.getRateId()));
			map.put("max", phoneMapper.queryLedgerMaxFeeCoul(ledgerId, beginTime, endTime,bean.getRateId()));
			eleList.addAll( phoneMapper.queryLedgerFeeSum(ledgerId,  beginTime, endTime ,dateType,bean.getRateId()) );
			map.put("energySum", eleList );
		} else {	//年数据
			Date endDate = DateUtil.getCurrMonthFirstDay( baseDate );
			Date beginDate = DateUtil.getMonthDate( endDate, -11 );
 			map.put("list", phoneMapper.getMonERateData(ledgerId,beginDate,endDate,bean.getRateId()));
			map.put("max", phoneMapper.getMonEMaxRateData(ledgerId, beginDate, endDate,bean.getRateId()));
			eleList.addAll( phoneMapper.queryLedgerFeeSum(ledgerId,  beginDate, endDate ,dateType,bean.getRateId()) );
			map.put("energySum", eleList );
		}
		map.put( "rate",rateSectorList );
		return map;
	}
	
	/**
	 * 查询复费率电量(测量点)
	 * @author catkins.
	 * @param meterId
	 * @param dateType
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/3/2 13:37
	 */
	@Override
	public Map<String, Object> queryMeterFeeData(long meterId, Integer dateType,Date baseDate) {
		List<Map<String, Object>> eleList = new ArrayList<Map<String, Object>>();
		eleList.add(new HashMap<String, Object>( 0 ));
		eleList.add(new HashMap<String, Object>( 0 ));
		Map<String, Object> map = new HashMap<String, Object>();
		Long ledgerId = ledgerManagerMapper.getCompanyIdByMeterId( meterId );
		LedgerBean bean = phoneMapper.getLedgerById( ledgerId );
		List<RateSectorBean> rateSectorList = this.rateBeanMapper.getSectorData( bean.getRateId() );
		if ( dateType == 3 ) { //10日数据
			Date beginTime = DateUtil.getDateBetween(baseDate, -10);
			Date endTime = DateUtil.getDateBetween(baseDate, -1);
			map.put("list", phoneMapper.getMeterDayERateData(meterId, beginTime, endTime));
			map.put("max", phoneMapper.getMeterDayEMaxRateData(meterId, beginTime, endTime));
			eleList.addAll( phoneMapper.queryMeterFeeSum(meterId,  beginTime, endTime , dateType) );
			map.put("energySum", eleList );
		} else if( dateType == 4 ){	// 月数据
			Date beginTime = DateUtil.getDateBetween(DateUtil.getLastMonthDate(baseDate), -1);
			Date endTime = DateUtil.getDateBetween(baseDate, -1);
			map.put("list", phoneMapper.getMeterDayERateData(meterId, beginTime, endTime));
			map.put("max", phoneMapper.getMeterDayEMaxRateData(meterId, beginTime, endTime));
			eleList.addAll( phoneMapper.queryMeterFeeSum(meterId,  beginTime, endTime , dateType) );
			map.put("energySum", eleList );
		} else {	//年数据
			Date endDate = DateUtil.getCurrMonthFirstDay( baseDate );
			Date beginDate = DateUtil.getMonthDate( endDate, -11 );
			map.put("list", phoneMapper.getMeterMonERateData(meterId,beginDate,endDate));
			map.put("max", phoneMapper.getMeterMonEMaxRateData(meterId, beginDate, endDate));
			eleList.addAll( phoneMapper.queryMeterFeeSum(meterId,  beginDate, endDate , dateType) );
			map.put("energySum", eleList );
		}
		map.put( "rate",rateSectorList );
		return map;
	}
	
	@Override
	public List<Map<String, Object>> queryEnergyData(Long ledgerId, String baseDate,Integer dateType,Integer showType) {
		Date endTime = DateUtil.convertStrToDate(baseDate + " 23:59:59");
		Date beginTime = this.getStartDateByType_2(endTime,dateType);
		List<Map<String, Object>> maps = phoneMapper.queryEnergyData( ledgerId, beginTime, endTime ,showType);
		return maps;
	}
	
	@Override
	public List<Map<String, Object>> queryEnergyData4Parent(Long ledgerId, String baseDate,Integer dateType,Integer showType) {
		Date endTime = DateUtil.convertStrToDate(baseDate + " 23:59:59");
		Date beginTime = this.getStartDateByType_2(endTime,dateType);
		List<Map<String, Object>> maps = phoneMapper.queryEnergyData4Parent( ledgerId, beginTime, endTime ,showType);
		return maps;
	}
	
	@Override
	public List<Map<String, Object>> queryTradeBenData(String tradeCode) {
		return phoneMapper.queryTradeBenData(tradeCode);
	}
	
	/**
	 * 根据类型获取开始时间
	 *
	 * @param d
	 * @param dateType
	 * @return
	 */
	private Date getStartDateByType_2(Date d, int dateType) {
		
		switch (dateType) {
			case 1:// 日数据
				return DateUtil.clearDate(d);
			case 2:// 7日数据
				return DateUtil.clearDate(DateUtil.getDateBetween(d, -6));
			case 3:// 10日数据
				return DateUtil.clearDate(DateUtil.getDateBetween(d, -9));
			case 4:// 30日数据
				return DateUtil.clearDate(DateUtil.getDateBetween(d, -29));
			case 5:// 年数据
				Calendar c = Calendar.getInstance();
				c.setTime(d);
				c.set(Calendar.DAY_OF_MONTH, 2);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				
				c.add(Calendar.MONTH, -11);
				c.set(Calendar.DAY_OF_MONTH, 1);
				return new Date(c.getTimeInMillis());
		}
		return null;
	}
	
	@Override
	public LedgerBean queryParentLedger(Long ledgerId) {
		return this.phoneMapper.queryParentLedger(ledgerId);
	}
	
	@Override
	public List<Map<String, Object>> queryTransformerByLedgerId(Long ledgerId, Date beginTime,String showType) {
		List<Map<String, Object>> maps = phoneMapper.queryTransformerByLedgerId( ledgerId, beginTime, showType );
		return maps;
	}
	
	@Override
	public Map<String, Object> queryTransformerData(Long equipId) {
		return phoneMapper.queryTransformerData( equipId );
	}
	
	
	@Override
	public Integer modifyTrans(long objId, String startTime, String stopTime, String runStatus) {
		Date StartDate = DateUtil.convertStrToDate( startTime+" 00:00:00" );
		Date stopDate = DateUtil.convertStrToDate( stopTime +" 00:00:00");
		return phoneMapper.modifyTrans(objId,StartDate,stopDate,runStatus);
	}
	
	@Override
	public List<TransformerBean> queryTransformerByShowType(Long ledgerId, Date beginTime, String showType) {
		return phoneMapper.queryTransformerByShowType(ledgerId,beginTime,showType);
	}
	
	@Override
	public List<Map<String, Object>> queryProductionList(Long objId, Date baseDate, Integer showType) {
		return phoneMapper.queryProductionList( objId,baseDate,showType );
	}
	
	
	@Override
	public List<CapacityDeclarationBean> queryproductionList4APP(Long objId,Date baseDate) {
		return phoneMapper.queryproductionList4APP( objId,baseDate );
	}
	
	@Override
	public Integer declarePorduction(long meterId, long ledgerId, String dataDate, double yield96, double yield97, double yield98, double yieldOther, double yieldTotal) {
		Map<String,Object> param = new HashMap<String,Object>( 0 );
		param.put( "meterId",meterId );
		param.put( "ledgerId",ledgerId );
		param.put( "dataDate",DateUtil.convertStrToDate( dataDate+" 00:00:00" ) );
		param.put( "yield96",yield96 );
		param.put( "yield97",yield97 );
		param.put( "yield98",yield98 );
		param.put( "yieldOther",yieldOther );
		param.put( "yieldTotal",yieldTotal );
		param.put( "chgTime",DateUtil.convertStrToDate( DateUtil.getCurrentDateStr() ) );
		return phoneMapper.declarePorduction(param);
	}
	
	@Override
	public Map<String, Object> queryEleData(Map<String, Object> param) {
		Map<String,Object> result = new HashMap<String,Object>( 0 );
		
		/* result的内部 */
		List<Map<String,Object>> internalList = new ArrayList<>( 0 );
		Map<String,Object> internalMap = null;
		
		Integer dateType = Integer.parseInt( param.get( "dateType" ).toString() );
		//得到12个月前的时间
		String baseTime = param.get( "baseTime" ).toString();
		//转换成时间类型
		Date baseDate = DateUtil.convertStrToDate(baseTime);
		baseDate = DateUtil.getLastYearDate(DateUtil.getNextMonthDate(baseDate));
		
		for ( int i = 0 ; i < 12 ; i ++ ){
			double totalFee = 0.0;
			double otherTotal = 0.0;
			internalMap = new HashMap<>( 0 );
			if ( i > 0 ) // 最后一次不加
				baseDate = DateUtil.getNextMonthDate(baseDate);
			//把转换好的日期放回param
			param.put("baseTime",DateUtil.convertDateToStr(baseDate,DateUtil.MONTH_PATTERN));
			Map<String, Object> map = costService.calEmoDcpEleFee( param );
			if( map != null && map.containsKey( "fee" ) ){
				List<RateFeeBean> beans = (List<RateFeeBean>)map.get( "fee" );
				//这个for就取 month和totalFee
				for ( RateFeeBean bean : beans ){
					if (bean.getSectorId() != null) {
						if( bean.getSectorId().equals( "1" ) ){
							otherTotal += bean.getFee();
						}
						if( bean.getSectorId().equals( "2" ) ){
							otherTotal += bean.getFee();
						}
						if( bean.getSectorId().equals( "3" ) ){
							otherTotal += bean.getFee();
						}
						if( bean.getSectorId().equals( "4" ) ){
							otherTotal += bean.getFee();
						}
					}
					if( bean.getFeeId() == 4 )
						otherTotal += bean.getFee();
					if( bean.getFeeId() == 5 )
						otherTotal += bean.getFee();
				}
				internalMap.put( "month",DateUtil.convertDateToStr(baseDate,DateUtil.MONTH_PATTERN) );
				internalMap.put( "totalFee",otherTotal);
				if( i == 11-dateType ){
					for ( RateFeeBean bean : beans ){
						if (bean.getSectorId() != null) {
							if( bean.getSectorId().equals( "1" ) ){
								result.put("jFee",bean.getFee());
							}
							if( bean.getSectorId().equals( "2" ) ){
								result.put("fFee",bean.getFee());
							}
							if( bean.getSectorId().equals( "3" ) ){
								result.put("pFee",bean.getFee());
							}
							if( bean.getSectorId().equals( "4" ) ){
								result.put("gFee",bean.getFee());
							}
						}
						if( bean.getFeeId() == 4 ){
							result.put("baseFee",bean.getFee());
						}
						if( bean.getFeeId() == 5 ){
							result.put("adjustFee",bean.getFee());
						}
						totalFee += bean.getFee();
					}
					internalMap.put( "totalFee",totalFee);
				}
			}
			internalList.add( internalMap );
		}
		result.put( "systemNo",System.currentTimeMillis() );
		result.put( "charts",internalList );
		return result;
	}

	@Override
	public Map<String, Object> queryEleData2(Map<String, Object> param) {
		Map<String,Object> result = new HashMap<String,Object>( 0 );

		/* result的内部 */
		List<Map<String,Object>> internalList = new ArrayList<>( 0 );
		Map<String,Object> internalMap = null;

		Integer dateType = Integer.parseInt( param.get( "dateType" ).toString() );
		//得到12个月前的时间
		String baseTime = param.get( "baseTime" ).toString();
		//转换成时间类型
		Date baseDate = DateUtil.convertStrToDate(baseTime);
		baseDate = DateUtil.getLastYearDate(DateUtil.getNextMonthDate(baseDate));

		//同步大屏月电费
		ConsMonthAmt consMonthAmt = new ConsMonthAmt();
		BigDecimal elecPrice =new BigDecimal(0);
		LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId(Long.valueOf(String.valueOf(param.get("objId"))));
		if(org.apache.commons.lang3.StringUtils.isNotBlank(ledgerBean.getUserNo())){
			//查询用户最近的月电量电费
			consMonthAmt = enterpriseMapper.consMonthAmt(ledgerBean.getUserNo(), "2019-09");
			// 基本电费
			consMonthAmt.setBa(consMonthAmt.getBa().divide(new BigDecimal(30),BigDecimal.ROUND_HALF_UP,4).multiply(new BigDecimal(getDate("dd",-1))));
			// 计算电费单价
			elecPrice = consMonthAmt.getPreTAmt().divide(consMonthAmt.getPreTSettlePq(),BigDecimal.ROUND_HALF_UP,4);
		}

		for ( int i = 0 ; i < 12 ; i ++ ){
			double totalFee = 0.0;
			double otherTotal = 0.0;
			internalMap = new HashMap<>( 0 );
			if ( i > 0 ) // 最后一次不加
				baseDate = DateUtil.getNextMonthDate(baseDate);
			//把转换好的日期放回param
			param.put("baseTime",DateUtil.convertDateToStr(baseDate,DateUtil.MONTH_PATTERN));
			Map<String, Object> map = costService.calEmoDcpEleFee2( param );
			if( map != null && map.containsKey( "fee" ) ){
				List<RateFeeBean> beans = (List<RateFeeBean>)map.get( "fee" );
				//这个for就取 month和totalFee
				for ( RateFeeBean bean : beans ){
					if (bean.getSectorId() != null) {
						if( bean.getSectorId().equals( "1" ) ){
							otherTotal += bean.getFee();
						}
						if( bean.getSectorId().equals( "2" ) ){
							otherTotal += bean.getFee();
						}
						if( bean.getSectorId().equals( "3" ) ){
							otherTotal += bean.getFee();
						}
						if( bean.getSectorId().equals( "4" ) ){
							otherTotal += bean.getFee();
						}
					}
					if( bean.getFeeId() == 4 )
						otherTotal += bean.getFee();
					if( bean.getFeeId() == 5 )
						otherTotal += bean.getFee();
				}
				internalMap.put( "month",DateUtil.convertDateToStr(baseDate,DateUtil.MONTH_PATTERN) );
				internalMap.put( "totalFee",otherTotal);
				if( i == 11-dateType ){
					for ( RateFeeBean bean : beans ){
						if (bean.getSectorId() != null) {
							if( bean.getSectorId().equals( "1" ) ){
								result.put("jFee",bean.getFee());
							}
							if( bean.getSectorId().equals( "2" ) ){
								result.put("fFee",bean.getFee());
							}
							if( bean.getSectorId().equals( "3" ) ){
								result.put("pFee",bean.getFee());
							}
							if( bean.getSectorId().equals( "4" ) ){
								result.put("gFee",bean.getFee());
							}
						}
						if( bean.getFeeId() == 4 ){
							result.put("baseFee",bean.getFee());
						}
						if( bean.getFeeId() == 5 ){
							result.put("adjustFee",bean.getFee());
						}
						totalFee += bean.getFee();
					}
					internalMap.put( "totalFee",totalFee);
				}
			}
			// elecPrice>0
			if(elecPrice.compareTo(new BigDecimal(0))==1){
				List<ConsDayEnergy> monList = enterpriseMapper.consMonEnergy(ledgerBean.getUserNo(),String.valueOf(param.get("baseTime")));
				if(monList.size()>0)internalMap.put( "totalFee",elecPrice.multiply(monList.get(0).getPapE()).add(consMonthAmt.getBa()));
				if(monList.size()>0 && i == 11 ){
					result.put("jFee", elecPrice.multiply(monList.get(0).getPapE1()));
					result.put("fFee", elecPrice.multiply(monList.get(0).getPapE2()));
					result.put("pFee", elecPrice.multiply(monList.get(0).getPapE3()));
					result.put("gFee", elecPrice.multiply(monList.get(0).getPapE4()));
					result.put("baseFee", consMonthAmt.getBa());
				}
			}
			internalList.add( internalMap );
		}
		result.put( "systemNo",System.currentTimeMillis() );
		result.put( "charts",internalList );
		return result;
	}

	@Override
	public Map<String, Object> queryEleContrastData(Map<String, Object> param) {
		Map<String,Object> result = new HashMap<String,Object>( 0 );
		/* result的内部 */
		List<Map<String,Object>> internalList = new ArrayList<>( 0 );
		Map<String,Object> internalMap = null;
		String baseTime = param.get( "baseTime" ).toString();
		//转换成时间类型
		Date baseDate = DateUtil.convertStrToDate(baseTime);
		for ( int i = 0 ; i < 6 ; i ++ ){
			double totalFee = 0.0;		//容量电费
			double otherTotal = 0.0;	//需量或容量申报的电费
			internalMap = new HashMap<>( 0 );
			if ( i > 0 )
				baseDate = DateUtil.getLastMonthDate(baseDate);
			//把转换好的日期放回param
			param.put("baseTime",DateUtil.convertDateToStr(baseDate,DateUtil.MONTH_PATTERN));
			Map<String, Object> map = costService.calEmoDcpEleFee_new(param);
			if( map != null && map.containsKey( "fee" ) ){
				List<RateFeeBean> beans = (List<RateFeeBean>)map.get( "fee" );
				//这个for就取 month和totalFee
				for ( RateFeeBean bean : beans ){
					if( bean.getFeeId() == 4 )
						otherTotal += bean.getFee();
					if( bean.getFeeId() == 6 )
						totalFee += bean.getFee();
				}
				internalMap.put( "month",DateUtil.convertDateToStr(baseDate,DateUtil.MONTH_PATTERN) );
				internalMap.put( "otherTotal",otherTotal);
				internalMap.put( "totalFee",totalFee);
			}
			internalList.add( internalMap );
		}
		result.put( "charts",internalList );
		return result;
	}
	
	@Override
	public List<Map<String, Object>> queryDetailData(Long objId, Integer objType, String baseDate, Integer dateType) {
		Date endTime = DateUtil.convertStrToDate(baseDate + " 23:59:59");
		Date beginTime = this.getStartDateByType_2(endTime,dateType);
		return phoneMapper.queryDetailData( objId,beginTime ,endTime);
	}
	
	
	/**
	 * 管理员用户查询线损
	 * @param beginTime
	 * @param endTime
	 * @param objectId
	 * @param type
	 * @return
	 */
	@Override
	public Map<String, Object> lossAnalysis_new(String beginTime, String endTime, Long objectId, int type) {
		Date startDate = DateUtil.convertStrToDate(beginTime, DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(endTime, DateUtil.SHORT_PATTERN);
		Map<String, Object> result = new HashMap<String, Object>();
		int lineLoss = 1;// 为0表示“该分户没配线损”
		List<Map<String,Object>> meterMap = this.schedulingMapper.getLineLossByLevel_new(objectId, type);
		
		if (meterMap != null && meterMap.size() > 0) {
			List<Map<String,Object>> meterIds = processMeterIds(meterMap);
			
			for ( Map<String,Object> mids : meterIds ) {
				mids.put( "coul", schedulingMapper.getDayLineLossInfo_new((List<Long>)mids.get( "meterIds" ), startDate, endDate) == null ? 0 : schedulingMapper.getDayLineLossInfo_new((List<Long>)mids.get( "meterIds" ), startDate, endDate) );
			}
			for (Map<String,Object> mids : meterIds) {
				List<Long> meters = schedulingMapper.getLineLossMeters_new((List<Long>)mids.get( "meterIds" ), type + 1);
				mids.remove( "meterIds" );
				if (meters == null || meters.size() == 0){
					mids.put( "use",0);
					mids.put( "lossEle",0);
					continue;
				}
				mids.put( "use", schedulingMapper.getDayLineLossInfo_new(meters, startDate, endDate) );
				double lossEle = Math.abs(DataUtil.doubleSubtract(Double.parseDouble( mids.get( "coul" )==null? "0" : mids.get( "coul" ).toString() ),
						schedulingMapper.getDayLineLossInfo_new(meters, startDate, endDate) == null ? 0 : schedulingMapper.getDayLineLossInfo_new(meters, startDate, endDate)));// 损失电量
				mids.put( "lossEle",lossEle);
			}
			result.put("datas", meterIds);
		} else if (type == 1) {
			lineLoss = 0;
		}
		result.put("lineLoss", lineLoss);
		return result;
	}
	
	
	private List<Map<String,Object>> processMeterIds(List<Map<String,Object>> meterMap){
		List<Map<String,Object>> paramList = new ArrayList<>( 0 );
		Map<String,Object> map = null;
		List<Long> list = null;
		int i = 0;
		for (Map<String,Object> meter:meterMap) {
			list = new ArrayList<>( 0 );
			if( map != null && map.containsKey( "ledgerName" ) && map.get( "ledgerName" ).toString().equals( meter.get( "LEDGER_NAME" ) ) ){
				List<Long> meterIds = (List<Long>)map.get( "meterIds" );
				meterIds.add( Long.parseLong( meter.get( "METER_ID" ).toString() ) );
				map.put( "meterIds",meterIds );
			} else {
				if ( map != null ) {
					paramList.add( map );
				}
				map = new HashMap<>( 0 );
				list.add( Long.parseLong( meter.get( "METER_ID" ).toString() ) );
				map.put( "ledgerId",meter.get( "LEDGER_ID" ) );
				map.put( "ledgerName",meter.get( "LEDGER_NAME" ) );
				map.put( "meterIds",list );
			}
			i++;
			if ( meterMap.size() == i ) {
				paramList.add( map );
			}
		}
		return paramList;
	}
	
	/**
	 *	获取行业对标具体数据
	 * @param ledgerId
	 * @param baseDate
	 * @return
	 */
	@Override
	public Map<String, Object> queryIndustryData(long ledgerId) {
		Map<String,Object> result = new HashMap<>( 0 );
		Date beginTime = DateUtil.getPreMonthFristDay(new Date());
		Date endTime = DateUtil.getMonthLastDay(beginTime);
		//能管对象对标功能数据查询
//		Date beginTime = DateUtil.parseDate("2020-07-01 00:00:00",DateUtil.DEFAULT_PATTERN);	// 测试用时间
//		Date endTime = DateUtil.parseDate("2020-08-01 00:00:00",DateUtil.DEFAULT_PATTERN);	// 测试用时间
		if( phoneMapper.queryLedgerMaxPWIndustryData(ledgerId,beginTime,endTime) != null )
			result.putAll( phoneMapper.queryLedgerMaxPWIndustryData(ledgerId,beginTime,endTime) );
		if( phoneMapper.queryLedgerMinPWIndustryData(ledgerId,beginTime,endTime) != null )
			result.putAll( phoneMapper.queryLedgerMinPWIndustryData(ledgerId,beginTime,endTime) );
		if( phoneMapper.queryLedgerMaxFeeIndustryData(ledgerId,beginTime,endTime) != null )
			result.putAll( phoneMapper.queryLedgerMaxFeeIndustryData(ledgerId,beginTime,endTime) );
		if( phoneMapper.queryLedgerMinFeeIndustryData(ledgerId,beginTime,endTime) != null )
			result.putAll( phoneMapper.queryLedgerMinFeeIndustryData(ledgerId,beginTime,endTime) );
		if( phoneMapper.queryLedgerAvgIndustryData(ledgerId,beginTime,endTime) != null )
			result.putAll( phoneMapper.queryLedgerAvgIndustryData(ledgerId,beginTime,endTime) );
		//能管对象对标功能数据查询 end
		//测量点对标功能数据查询
		if( phoneMapper.queryMeterMaxPWIndustryData(ledgerId,beginTime,endTime) != null )
			result.putAll( phoneMapper.queryMeterMaxPWIndustryData(ledgerId,beginTime,endTime) );
		if( phoneMapper.queryMeterMinPWIndustryData(ledgerId,beginTime,endTime) != null )
			result.putAll( phoneMapper.queryMeterMinPWIndustryData(ledgerId,beginTime,endTime) );
		if( phoneMapper.queryMeterMaxFeeIndustryData(ledgerId,beginTime,endTime) != null )
			result.putAll( phoneMapper.queryMeterMaxFeeIndustryData(ledgerId,beginTime,endTime) );
		if( phoneMapper.queryMeterMinFeeIndustryData(ledgerId,beginTime,endTime) != null )
			result.putAll( phoneMapper.queryMeterMinFeeIndustryData(ledgerId,beginTime,endTime) );
		if( phoneMapper.queryMeterAvgIndustryData(ledgerId,beginTime,endTime) != null )
			result.putAll( phoneMapper.queryMeterAvgIndustryData(ledgerId,beginTime,endTime) );
		//测量点对标功能数据查询 end
		//线损指标
//		beginTime = DateUtil.parseDate("2016-07-01 00:00:00",DateUtil.DEFAULT_PATTERN); // 测试用时间
//		endTime = DateUtil.parseDate("2016-08-01 00:00:00",DateUtil.DEFAULT_PATTERN);	// 测试用时间
		Map<String, Object> map = this.lossAnalysis_new( DateUtil.convertDateToStr( beginTime, DateUtil.SHORT_PATTERN ),
				DateUtil.convertDateToStr( endTime, DateUtil.SHORT_PATTERN ), ledgerId, 1 );
		Map<String, Object> lineLoseData = this.getLineLoseData( map );
		result.putAll( lineLoseData );
		return result;
	}
	
	
	/**
	 * 从查询到的线损结果里拿到需要的数据
	 * @param map
	 * @return
	 */
	private Map<String,Object> getLineLoseData(Map<String,Object> map){
		Map<String,Object> result = new HashMap<>( 0 );
		String maxLedgerName = "-"; 	//最大值企业名称
		String minLedgerName = "-";		//最小值企业名称
		double maxLossEle = 0;			//最大值
		double minLossEle = -1000;		//最小值
		double sumLossEle = 0;			//总损失电量
		double sumCoul = 0;				//总表电量
		if( map != null && map.containsKey( "datas" ) ){
			List<Map<String,Object>> datas = (List<Map<String,Object>>)map.get( "datas" );
			if(datas.size() > 0){
				for ( Map<String,Object> listMap : datas ) {
					if( listMap.containsKey( "lossEle" ) && listMap.get( "lossEle" ) != null ){
						double lossEle = Double.parseDouble( listMap.get( "lossEle" ).toString() );
						double coul = Math.abs(Double.parseDouble( listMap.get( "coul" ).toString() ));
//						Log.error( "getLineLoseData>>>>>>>lossEle:"+lossEle );
//						Log.error( "getLineLoseData>>>>>>>coul:"+coul );
						
						if( minLossEle == -1000 ){
//							minLedgerName = listMap.get( "ledgerName" ).toString();
							minLossEle = coul==0?0:lossEle/coul;
						}
						sumLossEle += lossEle;
						sumCoul += coul;
						if( maxLossEle < (coul==0?0:lossEle/coul) ){
							maxLedgerName = listMap.get( "ledgerName" ).toString();
							maxLossEle = coul==0?0:lossEle/coul;
						}
						if( minLossEle > (coul==0?0:lossEle/coul) ){
							minLedgerName = listMap.get( "ledgerName" ).toString();
							minLossEle = coul==0?0:lossEle/coul;
						}
					}
				}
			}
		}
		result.put( "maxLedgerName" , maxLedgerName );
		result.put( "minLedgerName" , minLedgerName );
		result.put( "maxLossEle" , maxLossEle*100 );
		result.put( "minLossEle" , minLossEle*100 );
		result.put( "avgLossEle" , sumLossEle/sumCoul*100 );
//		Log.error( "getLineLoseData>>>>>>>maxLossEle:"+maxLossEle );
//		Log.error( "getLineLoseData>>>>>>>minLossEle:"+minLossEle );
		return result;
	}
	
	
	
	
}
