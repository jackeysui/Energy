package com.linyang.energy.service.impl;

import java.math.BigDecimal;import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.common.Log;
import com.linyang.energy.mapping.authmanager.UserBeanMapper;
import com.linyang.energy.mapping.eleAssessment.EleAssessmentMapper;
import com.linyang.energy.mapping.energyanalysis.SchedulingMapper;
import com.linyang.energy.mapping.energysavinganalysis.CostMapper;
import com.linyang.energy.model.EasyAdviceScoreBean;
import com.linyang.energy.model.LineLossBean;
import com.linyang.energy.model.RateSectorBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.WechatAssessment;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.HarConstant;
import com.linyang.util.DoubleUtils;

/**
 * Created by Administrator on 16-12-21.
 */
@Service
public class WechatAssessmentImpl implements WechatAssessment {

	@Autowired
	private EleAssessmentMapper eleAssessmentMapper;
	@Autowired
	private UserBeanMapper userBeanMapper;
	@Autowired
	private SchedulingMapper schedulingMapper;
	@Autowired
	private CostMapper costMapper;

	private ConcurrentHashMap<Long, Object> assessmentCache = new ConcurrentHashMap<Long, Object>();

	@Override
	public List<EasyAdviceScoreBean> wechatAssessment(Long accountId,
			Map<Integer, Integer> weight) {
		//评分list
		List<EasyAdviceScoreBean> scoreList = new ArrayList<EasyAdviceScoreBean>();
		//评分最低三项list
		List<EasyAdviceScoreBean> lowscList = null;
		UserBean userBean = this.userBeanMapper.getUserByAccountId(accountId);
		if (userBean != null && userBean.getLedgerId() != null) {
			Long ledgerId = userBean.getLedgerId();
			Date endTime = DateUtil.clearDate(new Date()); // 结束时间
			Date beginTime = DateUtil.getDateBetween(endTime, -30); // 起始时间
			List<Long> meterIds = this.eleAssessmentMapper
					.getMeterIdsByLedgerId(ledgerId);
			
			long powerExceedSum = 0;
			long powerExceedNum = 0;
			long curExceedSum = 0;
			long curExceedNum = 0;
			long volDeviationSum = 0;
			long volDeviationNum = 0;
			long volBalanSum = 0;
			long volBalanNum = 0;
			long iBalanSum = 0;
			long iBalanNum = 0;
			long maxLoadSum = 0;
			long maxLoadNum = 0;

			// 各项比较值
			double powerExceedDVal = 0;
			double curExceedDVal = 0;
			double volDeviationDVal = 0;
			double volBalanDVal = 0;
			double iBalanDVal = 0;
			double maxLoadDVal = 0;

			EasyAdviceScoreBean powerExceedBean = null;
			EasyAdviceScoreBean curExceedBean = null;
			EasyAdviceScoreBean volDeviationBean = null;
			EasyAdviceScoreBean volBalanBean = null;
			EasyAdviceScoreBean iBalanBean = null;
			EasyAdviceScoreBean maxLoadBean = null;

			long minPfSum = 0;
			long minPfNum = 0;
			long vBalanSum = 0;
			long vBalanNum = 0;
			long vBalanTimeSum = 0;
			long vBalanTimeNum = 0;
			long iBalanAvgSum = 0;
			long iBalanAvgNum = 0;
			double tdisvSum = 0;
			double disvSum = 0;
			double disiSum = 0;
			long volSum = 0;
			long volNum = 0;
			long tdisvNum = 0;
			long disvNum = 0;
			long disiNum = 0;

			// 各项比较值
			double minPfDVal = 1;
			double volDVal = 0;
			double vBalanDVal = 0;
			double tdisVTDVal = 100d;
			double disVTDVal = 100d;
			double disITDVal = 100d;
			double vBalanTimeDVal = 0;
			double iBalanAvgDVal = 0;

			EasyAdviceScoreBean minPfBean = null;
			EasyAdviceScoreBean volBean = null;
			EasyAdviceScoreBean vBalanBean = null;
			EasyAdviceScoreBean tdisVTBean = null;
			EasyAdviceScoreBean disVTBean = null;
			EasyAdviceScoreBean disITBean = null;
			EasyAdviceScoreBean vBalanTimeBean = null;
			EasyAdviceScoreBean iBalanAvgBean = null;

			long pfSum = 0;
			long pfNum = 0;
			long rpSum = 0;
			long rpNum = 0;
			long rpTimeSum = 0;
			long rpTimeNum = 0;
			long basicFeeSum = 0;
			long basicFeeNum = 0;
			long loadSum = 0;
			long loadNum = 0;

			// 各项比较值
			double pfDVal = 1;
			double rpDVal = 0;
			double rpTimeDVal = 0;
			double basicFeeDVal = 100d;
			double loadDVal = 0;

			EasyAdviceScoreBean pfBean = null;
			EasyAdviceScoreBean rpBean = null;
			EasyAdviceScoreBean rpTimeBean = null;
			EasyAdviceScoreBean basicFeeBean = null;
			EasyAdviceScoreBean loadBean = null;

			for (int i = 0,size = meterIds.size(); i < size; i++) {
				Long meterId = meterIds.get(i);
				// 功率越限
				EasyAdviceScoreBean powerExceedbean = calculatePowerExceed(
						meterId, beginTime, endTime);
				if (powerExceedbean != null) {
					powerExceedSum += powerExceedbean.getScore();
					powerExceedNum += 1;
					if (powerExceedbean.getdValDouble() >= powerExceedDVal) {
						powerExceedBean = powerExceedbean;
						powerExceedBean.setScoreName("功率越限");
						powerExceedDVal = powerExceedbean.getdValDouble();
					}
				}
				// 电流越限
				EasyAdviceScoreBean curExceedbean = calculateCurExceed(meterId,
						beginTime, endTime);
				if (curExceedbean != null) {
					curExceedSum += curExceedbean.getScore();
					curExceedNum += 1;
					if (curExceedbean.getdValDouble() >= curExceedDVal) {
						curExceedBean = curExceedbean;
						curExceedBean.setScoreName("电流越限");
						curExceedDVal = curExceedbean.getdValDouble();
					}
				}
				// 电压允许偏差
				EasyAdviceScoreBean volDeviationbean = calculateVolDeviation(
						meterId, beginTime, endTime);
				if (volDeviationbean != null) {
					volDeviationSum += volDeviationbean.getScore();
					volDeviationNum += 1;
					if (volDeviationbean.getdValDouble() >= volDeviationDVal) {
						volDeviationBean = volDeviationbean;
						volDeviationBean.setScoreName("电压允许偏差");
						volDeviationDVal = volDeviationbean.getdValDouble();
					}
				}
				// 最大电压不平衡度
				EasyAdviceScoreBean volBalanbean = calculateVolBanlan(meterId,
						beginTime, endTime);
				if (volBalanbean != null) {
					volBalanSum += volBalanbean.getScore();
					volBalanNum += 1;
					if (volBalanbean.getdValDouble() >= volBalanDVal) {
						volBalanBean = volBalanbean;
						volBalanBean.setScoreName("最大电压不平衡度");
						volBalanDVal = volBalanbean.getdValDouble();
					}
				}
				// 最大电流不平衡度
				EasyAdviceScoreBean iBalanbean = calculateIBanlan(meterId,
						beginTime, endTime);
				if (iBalanbean != null) {
					iBalanSum += iBalanbean.getScore();
					iBalanNum += 1;
					if (iBalanbean.getdValDouble() >= iBalanDVal) {
						iBalanBean = iBalanbean;
						iBalanBean.setScoreName("最大电流不平衡度");
						iBalanDVal = iBalanbean.getdValDouble();
					}
				}
				// 最大负载率
				EasyAdviceScoreBean maxLoadbean = calculateMaxLoad(meterId,
						beginTime, endTime);
				if (maxLoadbean != null) {
					maxLoadSum += maxLoadbean.getScore();
					maxLoadNum += 1;
					if (maxLoadbean.getdValDouble() >= maxLoadDVal) {
						maxLoadBean = maxLoadbean;
						maxLoadBean.setScoreName("最大负载率");
						maxLoadDVal = maxLoadbean.getdValDouble();
					}
				}

				// 功率因数最小值
				EasyAdviceScoreBean minPfbean = calculateMinPf(ledgerId,
						meterId, beginTime, endTime);
				if (minPfbean != null) {
					minPfSum += minPfbean.getScore();
					minPfNum += 1;
					if (minPfbean.getdValDouble() <= minPfDVal) {
						minPfBean = minPfbean;
						minPfBean.setScoreName("功率因数最小值");
						minPfDVal = minPfbean.getdValDouble();
					}
				}
				// 电压越限率
				EasyAdviceScoreBean volbean = calculateCurVol(meterId,
						beginTime, endTime);
				if (volbean != null) {
					volSum += volbean.getScore();
					volNum++;
					if (volbean.getdValDouble() >= volDVal) {
						volBean = volbean;
						volBean.setScoreName("电压越限率");
						volDVal = volbean.getdValDouble();
					}
				}
				
				// 电压不平衡度日均值
				EasyAdviceScoreBean vBalanbean = calculateVBalanAvg(meterId,
						beginTime, endTime);
				if (vBalanbean != null) {
					vBalanSum += vBalanbean.getScore();
					vBalanNum += 1;
					if (vBalanbean.getdValDouble() >= vBalanDVal) {
						vBalanBean = vBalanbean;
						vBalanBean.setScoreName("电压不平衡度日均值");
						vBalanDVal = vBalanbean.getdValDouble();
					}
				}

				Integer volLevel = eleAssessmentMapper
						.getMeterVolLevel(meterId);
				if (volLevel != null) {
					if (volLevel.intValue() == 1)
						volLevel = 380;
					else if (volLevel.intValue() == 3)
						volLevel = 6;
					else if (volLevel.intValue() == 20)
						volLevel = 10;
					else if (volLevel.intValue() > 110)
						volLevel = 110;

					//电压总谐波畸变率最大值
					EasyAdviceScoreBean tdisVTbean = calculateTotalDisV(
							volLevel, meterId, beginTime, endTime);
					if (tdisVTbean != null) {
						tdisvSum = DataUtil.doubleAdd(tdisvSum, tdisVTbean.getScore());
						tdisvNum++;
						if (tdisVTbean.getdValDouble() <= tdisVTDVal) {
							tdisVTBean = tdisVTbean;
							tdisVTBean.setScoreName("电压总谐波畸变率最大值");
							tdisVTDVal = tdisVTbean.getdValDouble();
						}
					}
					//各奇次谐波电压含有率最大值
					EasyAdviceScoreBean disVTbean = calculateDisV(volLevel,
							meterId, beginTime, endTime);
					if (disVTbean != null) {
						disvSum = DataUtil.doubleAdd(disvSum, disVTbean.getScore());
						disvNum++;
						if (disVTbean.getdValDouble() <= disVTDVal) {
							disVTBean = disVTbean;
							disVTBean.setScoreName("各奇次谐波电压含有率最大值");
							disVTDVal = disVTbean.getdValDouble();
						}
					}
					//各奇次谐波电流最大值
					EasyAdviceScoreBean disITbean = calculateDisI(volLevel,
							meterId, beginTime, endTime);
					if (disITbean != null) {
						disiSum = DataUtil.doubleAdd(disiSum, disITbean.getScore());
						disiNum++;
						if (disITbean.getdValDouble() <= disITDVal) {
							disITBean = disITbean;
							disITBean.setScoreName("各奇次谐波电流最大值");
							disITDVal = disITbean.getdValDouble();
						}
					}
				}

				// 电压不平衡度日均时间
				EasyAdviceScoreBean vBalanTimebean = calculateVBalanTimeAvg(
						meterId, beginTime, endTime);
				if (vBalanTimebean != null) {
					vBalanTimeSum += vBalanTimebean.getScore();
					vBalanTimeNum += 1;
					if (vBalanTimebean.getdValDouble() >= vBalanTimeDVal) {
						vBalanTimeBean = vBalanTimebean;
						vBalanTimeBean.setScoreName("电压不平衡度日均时间");
						vBalanTimeDVal = vBalanTimebean.getdValDouble();
					}
				}
				// 电流不平衡度
				EasyAdviceScoreBean iBalanAvgbean = calculateIBalanAvg(meterId,
						beginTime, endTime);
				if (iBalanAvgbean != null) {
					iBalanAvgSum += iBalanAvgbean.getScore();
					iBalanAvgNum += 1;
					if (iBalanAvgbean.getdValDouble() >= iBalanAvgDVal) {
						iBalanAvgBean = iBalanAvgbean;
						iBalanAvgBean.setScoreName("电流不平衡度");
						iBalanAvgDVal = iBalanAvgbean.getdValDouble();
					}
				}

				// 专变月平均功率因数
				EasyAdviceScoreBean pfbean = calculatePF(ledgerId, meterId,
						beginTime, endTime);
				if (pfbean != null) {
					pfSum += pfbean.getScore();
					pfNum++;
					if (pfbean.getdValDouble() <= pfDVal) {
						pfBean = pfbean;
						pfBean.setScoreName("专变月平均功率因数");
						pfDVal = pfbean.getdValDouble();
					}
				}

				// 无功倒送功率
				EasyAdviceScoreBean rpbean = calculateRP(meterId, beginTime,
						endTime);
				if (rpbean != null) {
					rpSum += rpbean.getScore();
					rpNum++;
					if (rpbean.getdValDouble() >= rpDVal) {
						rpBean = rpbean;
						rpBean.setScoreName("无功倒送功率");
						rpDVal = rpbean.getdValDouble();
					}
				}

				// 无功倒送时间
				EasyAdviceScoreBean rpTimebean = calculateRPTime(meterId,
						beginTime, endTime);
				if (rpTimebean != null) {
					rpTimeSum += rpTimebean.getScore();
					rpTimeNum++;
					if (rpTimebean.getdValDouble() >= rpTimeDVal) {
						rpTimeBean = rpTimebean;
						rpTimeBean.setScoreName("无功倒送时间");
						rpTimeDVal = rpTimebean.getdValDouble();
					}
				}

				// 基本电费
				EasyAdviceScoreBean basicFeebean = calculateBasicFee(ledgerId,
						meterId, beginTime, endTime);
				if (basicFeebean != null) {
					basicFeeSum += basicFeebean.getScore();
					basicFeeNum++;
					if (basicFeebean.getdValDouble() <= basicFeeDVal) {
						basicFeeBean = basicFeebean;
						basicFeeBean.setScoreName("基本电费");
						basicFeeDVal = basicFeebean.getdValDouble();
					}
				}

				// 负载率
				EasyAdviceScoreBean loadbean = calculateLoad(meterId,
						beginTime, endTime);
				if (loadbean != null) {
					loadSum += loadbean.getScore();
					loadNum++;
					if (loadbean.getdValDouble() >= loadDVal) {
						loadBean = loadbean;
						loadBean.setScoreName("负载率");
						loadDVal = loadbean.getdValDouble();
					}
				}
			}
			if(powerExceedBean!=null){scoreList.add(powerExceedBean);}		if(rpTimeBean!=null){scoreList.add(rpTimeBean);}    if(tdisVTBean!=null){scoreList.add(tdisVTBean);}	if(curExceedBean!=null){scoreList.add(curExceedBean);}
			// 功率越限
			if (powerExceedNum != 0) {
				powerExceedBean.setScore(powerExceedSum / powerExceedNum);
				powerExceedBean.setWeight(weight.get(1));
				scoreList.add(powerExceedBean);
			}
			// 电流越限
			if (curExceedNum != 0) {
				curExceedBean.setScore(curExceedSum / curExceedNum);
				curExceedBean.setWeight(weight.get(2));
				scoreList.add(curExceedBean);
			}if(null != volDeviationBean)scoreList.add(volDeviationBean);
			// 电压允许偏差
			if (volDeviationNum != 0) {
				volDeviationBean.setScore(volDeviationSum / volDeviationNum);
				volDeviationBean.setWeight(weight.get(3));
				scoreList.add(volDeviationBean);
			}if(volBalanBean!=null){scoreList.add(volBalanBean);}
			// 最大电压不平衡度
			if (volBalanNum != 0) {
				volBalanBean.setScore(volBalanSum / volBalanNum);
				volBalanBean.setWeight(weight.get(4));
				scoreList.add(volBalanBean);
			}	if(null != iBalanBean)scoreList.add(iBalanBean);
			// 最大电流不平衡度
			if (iBalanNum != 0) {
				iBalanBean.setScore(iBalanSum / iBalanNum);
				iBalanBean.setWeight(weight.get(5));
				scoreList.add(iBalanBean);
			} if(null != maxLoadBean)scoreList.add(maxLoadBean);
			// 最大负载率
			if (maxLoadNum != 0) {
				maxLoadBean.setScore(maxLoadSum / maxLoadNum);
				maxLoadBean.setWeight(weight.get(6));
				scoreList.add(maxLoadBean);
			}	if(null != minPfBean)scoreList.add(minPfBean);
			// 功率因数最小值
			if (minPfNum != 0) {
				minPfBean.setScore(minPfSum / minPfNum);
				minPfBean.setWeight(weight.get(7));
				scoreList.add(minPfBean);
			}			
			// 功率因数越限率
			EasyAdviceScoreBean pfExceedBean = calculatePfExceed(ledgerId, beginTime, endTime);			
			if (pfExceedBean != null) {
				pfExceedBean.setScoreName("功率因数越限率");
				pfExceedBean.setWeight(weight.get(23));
				scoreList.add(pfExceedBean);
			}if(volBean!=null){scoreList.add(volBean);}
			// 电压越限率
			if (volNum != 0) {
				volBean.setScore(volSum / volNum);
				volBean.setWeight(weight.get(9));
				scoreList.add(volBean);
			}
			
			// 电压总谐波畸变率最大值
			if (tdisvNum != 0) {
				tdisVTBean.setScore(Math.round(DataUtil.doubleDivide(tdisvSum, tdisvNum, 2)));
				tdisVTBean.setWeight(weight.get(11));
				scoreList.add(tdisVTBean);
			}if(null != disVTBean){scoreList.add(disVTBean);}
			// 各奇次谐波电压含有率最大值
			if (disvNum != 0) {
				disVTBean.setScore(Math.round(DataUtil.doubleDivide(disvSum, disvNum, 2)));
				disVTBean.setWeight(weight.get(12));
				scoreList.add(disVTBean);
			}if(disITBean!=null){scoreList.add(disITBean);}
			// 各奇次谐波电流最大值
			if (disiNum != 0) {
				disITBean.setScore(Math.round(DataUtil.doubleDivide(disiSum, disiNum, 2)));
				disITBean.setWeight(weight.get(13));
				scoreList.add(disITBean);
			}if(null != vBalanBean){scoreList.add(vBalanBean);}
			// 电压不平衡度
			if (vBalanNum != 0) {
				vBalanBean.setScore(vBalanSum / vBalanNum);
				vBalanBean.setWeight(weight.get(14));
				scoreList.add(vBalanBean);
			}if(null != vBalanTimeBean){scoreList.add(vBalanTimeBean);}
			// 电压不平衡度日均时间
			if (vBalanTimeNum != 0) {
				vBalanTimeBean.setScore(vBalanTimeSum / vBalanTimeNum);
				vBalanTimeBean.setWeight(weight.get(15));
				scoreList.add(vBalanTimeBean);
			}	if(null != iBalanAvgBean)scoreList.add(iBalanAvgBean);
			// 电流不平衡度
			if (iBalanAvgNum != 0) {
				iBalanAvgBean.setScore(iBalanAvgSum / iBalanAvgNum);
				iBalanAvgBean.setWeight(weight.get(16));
				scoreList.add(iBalanAvgBean);
			}
			// 电度电费合理性
			EasyAdviceScoreBean eleBean = calculateEle(ledgerId, beginTime,
					endTime);
			if (eleBean != null) {
				eleBean.setScoreName("电度电费合理性");
				eleBean.setWeight(weight.get(17));
				scoreList.add(eleBean);
			} if (null != pfBean){scoreList.add(pfBean);}
			// 专变月平均功率因数
			if (pfNum != 0) {
				pfBean.setScore(pfSum / pfNum);
				pfBean.setWeight(weight.get(18));
				scoreList.add(pfBean);
			}if(null != rpBean){scoreList.add(rpBean);}
			// 无功倒送功率
			if (rpNum != 0) {
				rpBean.setScore(rpSum / rpNum);
				rpBean.setWeight(weight.get(19));
				scoreList.add(rpBean);
			}
			// 无功倒送时间
			if (rpTimeNum != 0) {
				rpTimeBean.setScore(rpTimeSum / rpTimeNum);
				rpTimeBean.setWeight(weight.get(20));
				scoreList.add(rpTimeBean);
			} if(null != basicFeeBean)scoreList.add(basicFeeBean);
			// 基本电费
			if (basicFeeNum != 0) {
				basicFeeBean.setScore(basicFeeSum / basicFeeNum);
				basicFeeBean.setWeight(weight.get(21));
				scoreList.add(basicFeeBean);
			} if (null != loadBean){scoreList.add(loadBean);}
			// 负载率
			if (loadNum != 0) {
				loadBean.setScore(loadSum / loadNum);
				loadBean.setWeight(weight.get(22));
				scoreList.add(loadBean);
			}
			// 线损
			EasyAdviceScoreBean lineLossBean = calculateLineloss(ledgerId,
					beginTime, endTime);
			if (lineLossBean != null) {
				lineLossBean.setScoreName("线损");
				lineLossBean.setWeight(weight.get(23));
				scoreList.add(lineLossBean);
			}

			/**
			 * 对评分项进行排序 1.分数小的在前 2.分数相同则通过权重排序，权重相同则随机
			 */
			Collections.sort(scoreList, new Comparator<EasyAdviceScoreBean>() {
				@Override
				public int compare(EasyAdviceScoreBean sb1,
						EasyAdviceScoreBean sb2) {
					if (sb1.getScore() > sb2.getScore()) {
						return 1;
					} else if (sb1.getScore().equals(sb2.getScore())) {
						if (sb1.getScore() * sb1.getWeight() > sb2.getScore()
								* sb2.getWeight()) {
							return 1;
						} else if (sb1.getScore() * sb1.getWeight() < sb2
								.getScore()
								* sb2.getWeight()) {
							return -1;
						} else {
							return 0;
						}
					}
					return -1;
				}

			});

			//将list中最低的三项取出
			if (scoreList.size() > 2) {
				lowscList = new ArrayList<EasyAdviceScoreBean>();
				lowscList.add(scoreList.get(0));
				lowscList.add(scoreList.get(1));
				lowscList.add(scoreList.get(2));
			}

		}

		return lowscList;
	}

	/**
	 * 功率越限
	 * @param meterId 计量点id
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	private EasyAdviceScoreBean calculatePowerExceed(Long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//计量点额定功率
		Double basePower = this.schedulingMapper.getPointEPwr(meterId);
		if (basePower != null && basePower > 0) {
			Double maxPower = this.eleAssessmentMapper.getMaxPowerByMeter(
					meterId, beginTime, endTime);
			if (maxPower != null) {
				result = new EasyAdviceScoreBean();
				double dVal = Math.abs(DataUtil.doubleSubtract(maxPower, basePower));
				if (maxPower >= DataUtil.doubleMultiply(1.2, basePower)) {
					rules = "功率超过额定功率的20%。";
					score = 20L;
				} else if (maxPower >= DataUtil.doubleMultiply(1.1, basePower)
						&& maxPower < DataUtil.doubleMultiply(1.2, basePower)) {
					rules = "功率超过额定功率的10%。";
					score = 40L;
				} else if (maxPower >= DataUtil.doubleMultiply(0.9, basePower)
						&& maxPower < DataUtil.doubleMultiply(1.1, basePower)) {
					rules = "功率超过额定功率的0%。";
					score = 60L;
				} else if (maxPower >= DataUtil.doubleMultiply(0.8, basePower)
						&& maxPower < DataUtil.doubleMultiply(0.9, basePower)) {
					rules = "功率低于额定功率的10%。";
					score = 80L;
				} else if (maxPower < DataUtil.doubleMultiply(0.8, basePower)) {
					rules = "功率低于额定功率的20%。";
					score = 100L;
				}

				result.setScore(score);
				result.setStValStr(basePower.toString() + "kW");
				result.setAcValStr(maxPower.toString() + "kW");
				result.setRules(rules);
				result.setdValDouble(dVal);
			}
		}

		return result;
	}

	/**
	 * 电流越限
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateCurExceed(Long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//额定电流
		Double baseI = this.schedulingMapper.getPointECur(meterId);
		if (baseI != null && baseI > 0) {
			Double maxI = this.eleAssessmentMapper.getMaxIbyMeter(meterId,
					beginTime, endTime);
			if (maxI != null) {
				result = new EasyAdviceScoreBean();
				double dVal = new BigDecimal(maxI).subtract(new BigDecimal(baseI)).abs().doubleValue();
				if (maxI >= DataUtil.doubleMultiply(1.2, baseI)) {
					rules = "电流超过额定电流的20%。";
					score = 20L;
				} else if (maxI >= DataUtil.doubleMultiply(1.1, baseI) && maxI < DataUtil.doubleMultiply(1.2, baseI)) {
					rules = "电流超过额定电流的10%。";
					score = 40L;
				} else if (maxI >= DataUtil.doubleMultiply(0.9, baseI) && maxI < DataUtil.doubleMultiply(1.1, baseI)) {
					rules = "电流超过额定电流的0%。";
					score = 60L;
				} else if (maxI >= DataUtil.doubleMultiply(0.8, baseI) && maxI < DataUtil.doubleMultiply(0.9, baseI)) {
					rules = "电流低于额定电流的10%。";
					score = 80L;
				} else if (maxI < DataUtil.doubleMultiply(0.8, baseI)) {
					rules = "电流低于额定电流的10%。";
					score = 100L;
				}

				result.setScore(score);
				result.setStValStr(baseI.toString() + "kA");
				result.setAcValStr(maxI.toString() + "kA");
				result.setRules(rules);
				result.setdValDouble(dVal);
			}
		}
		return result;
	}

	/**
	 * 电压偏差
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateVolDeviation(Long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//额定电压
		Double baseV = this.schedulingMapper.getPointEVol(meterId);
		if (baseV != null && baseV > 0) {
			//电压最大值
			Double maxV = this.eleAssessmentMapper.getMaxVbyMeter(meterId,
					beginTime, endTime);
			//电压最小值
			Double minV = this.eleAssessmentMapper.getMinVbyMeter(meterId,
					beginTime, endTime);
			if (maxV != null && minV != null) {
				//偏差
				double dVal = new BigDecimal(maxV).subtract(new BigDecimal(baseV)).abs().add(new BigDecimal(minV).subtract(new BigDecimal(baseV)).abs()).doubleValue();
				result = new EasyAdviceScoreBean();
				if (baseV >= 35000) {
					Double deviation = dVal;
					
					Double rate = new BigDecimal(deviation).multiply(new BigDecimal(1000)).divide(new BigDecimal(baseV), 0, BigDecimal.ROUND_HALF_DOWN).doubleValue(); // 电压最大、最小值偏差绝对值和的千分比
					if (rate <= 80) {
						score = 100L;
					} else if (rate <= 85 && rate > 80) {
						score = 80L;
					} else if (rate <= 95 && rate > 85) {
						score = 60L;
					} else if (rate <= 100 && rate > 95) {
						score = 40L;
					} else if (rate > 100) {
						score = 20L;
					}
				} else {
					Double z = DataUtil.doubleSubtract(maxV, baseV);
					Double f = DataUtil.doubleSubtract(minV, baseV);
					Double zRate = new BigDecimal(z).multiply(new BigDecimal(1000)).divide(new BigDecimal(baseV), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();; // 电压最大值偏差千分比
					Double fRate = new BigDecimal(f).multiply(new BigDecimal(1000)).divide(new BigDecimal(baseV), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue(); // 电压最小值偏差千分比
					if (baseV <= 20000 && baseV > 220) {
						long zResult = compMeterVolDevitation_1(zRate);
						long fResult = compMeterVolDevitation_1(fRate);
						score = Math.min(zResult, fResult);
					} else if (baseV.equals(220d)) {
						long zResult = compMeterVolDevitation_2(zRate);
						long fResult = compMeterVolDevitation_2(fRate);
						score = Math.min(zResult, fResult);
					}
				}

				if (score == 100L) {
					rules = "35kV 及以上供电电压正、负偏差的绝对值之和不超过额定电压的8%。/n"
							+ "20kV 及以下三相供电电压允许偏差为额定电压的±5.6%。/n"
							+ "220V 单相供电电压允许偏差为额定电压的+5.6%、-8%。/n";
				} else if (score == 80L) {
					rules = "35kV 及以上供电电压正、负偏差的绝对值之和不超过额定电压的8.5%。\n"
							+ "20kV 及以下三相供电电压允许偏差为额定电压的±5.95%。\n"
							+ "220V 单相供电电压允许偏差为额定电压的+5.95%、-8.5%。\n";
				} else if (score == 60L) {
					rules = "35kV 及以上供电电压正、负偏差的绝对值之和不超过额定电压的9%。\n"
							+ "20kV 及以下三相供电电压允许偏差为额定电压的±6.3%。\n"
							+ "220V 单相供电电压允许偏差为额定电压的+6.3%、-9%。\n";
				} else if (score == 40L) {
					rules = "35kV 及以上供电电压正、负偏差的绝对值之和不超过额定电压的9.5%。\n"
							+ "20kV 及以下三相供电电压允许偏差为额定电压的±6.65%。\n"
							+ "220V 单相供电电压允许偏差为额定电压的+6.65%、-9.5%。\n";
				} else if (score == 20L) {
					rules = "35kV 及以上供电电压正、负偏差的绝对值之和不超过额定电压的10%。\n"
							+ "20kV 及以下三相供电电压允许偏差为额定电压的±7%。\n"
							+ "220V 单相供电电压允许偏差为额定电压的+7%、-10%。\n";
				}

				result.setScore(score);
				result.setStValStr(baseV + "kV");
				result.setAcValStr("电压最大值：" + maxV.toString() + "kV；电压最小值："
						+ minV.toString() + "kV");
				result.setRules(rules);
				result.setdValDouble(dVal);
			}
		}
		return result;
	}

	/**
	 * 最大电压不平衡度
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateVolBanlan(Long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//电压不平衡度的最大值
		Double volBanlance = this.eleAssessmentMapper.getVolBalance(meterId,
				beginTime, endTime);
		if (volBanlance != null) {
			result = new EasyAdviceScoreBean();
			if (volBanlance < 2) {
				rules = "最大电压不平衡度小于2%。";
				score = 100L;
			} else if (volBanlance >= 2 && volBanlance < 2.5) {
				rules = "最大电压不平衡度大于2%小于2.5%。";
				score = 80L;
			} else if (volBanlance >= 2.5 && volBanlance < 3) {
				rules = "最大电压不平衡度大于2.5%小于3%。";
				score = 60L;
			} else if (volBanlance >= 3 && volBanlance < 4) {
				rules = "最大电压不平衡度大于3%小于4%。";
				score = 40L;
			} else {
				rules = "最大电压不平衡度大于4%。";
				score = 20L;
			}

			result.setScore(score);
			result.setStValStr("0%-2%");
			result.setAcValStr(volBanlance.toString() + "%");
			result.setRules(rules);
			result.setdValDouble(volBanlance);
		}

		return result;
	}

	/**
	 * 最大电流不平衡度
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateIBanlan(Long meterId, Date beginTime,
			Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//电流不平衡度的最大值
		Double iBanlance = this.eleAssessmentMapper.getIBalance(meterId,
				beginTime, endTime);
		if (iBanlance != null) {
			result = new EasyAdviceScoreBean();
			if (iBanlance < 10) {
				rules = "最大电流不平衡度小于10%。";
				score = 100L;
			} else if (iBanlance >= 10 && iBanlance < 15) {
				rules = "最大电流不平衡度大于10%小于15%。";
				score = 80L;
			} else if (iBanlance >= 15 && iBanlance < 20) {
				rules = "最大电流不平衡度大于15%小于20%。";
				score = 60L;
			} else if (iBanlance >= 20 && iBanlance < 25) {
				rules = "最大电流不平衡度大于20%小于25%。";
				score = 40L;
			} else {
				rules = "最大电流不平衡度大于25%。";
				score = 20L;
			}

			result.setScore(score);
			result.setStValStr("0%-10%");
			result.setAcValStr(iBanlance.toString() + "%");
			result.setRules(rules);
			result.setdValDouble(iBanlance);
		}

		return result;
	}

	/**
	 * 最大负载率
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateMaxLoad(Long meterId, Date beginTime,
			Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//最大负载率
		Double maxLoad = this.eleAssessmentMapper.getMaxLoad(meterId,
				beginTime, endTime);
		if (maxLoad != null) {
			result = new EasyAdviceScoreBean();
			if (maxLoad < 60) {
				rules = "负载率大于60%小于70%。";
				score = 100L;
			} else if (maxLoad >= 60 && maxLoad < 80) {
				rules = "负载率大于70%小于80%。";
				score = 80L;
			} else if (maxLoad >= 80 && maxLoad < 90) {
				rules = "负载率大于80%小于90%。";
				score = 60L;
			} else if (maxLoad >= 90 && maxLoad < 100) {
				rules = "负载率大于90%小于100%。";
				score = 40L;
			} else {
				rules = "负载率大于100%。";
				score = 20L;
			}

			result.setScore(score);
			result.setStValStr("60%-70%");
			result.setAcValStr(maxLoad.toString() + "%");
			result.setRules(rules);
			result.setdValDouble(maxLoad);
		}

		return result;
	}

	/**
	 * 最小功率因数
	 * @param ledgerId
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateMinPf(Long ledgerId, Long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//最小功率因数
		Double minPf = this.eleAssessmentMapper.getMinPf(meterId, beginTime,
				endTime);
		//标准功率因数
		Double factor = schedulingMapper.getThresholdValue(ledgerId);
		if (minPf != null && factor != null) {
			result = new EasyAdviceScoreBean();
			Double stdPF = DataUtil.doubleMultiply(factor, 100);
			Double minPfD = DataUtil.doubleDivide(minPf, 100);
			if (minPf >= DataUtil.doubleMultiply(stdPF, 1.04)) {
				rules = "功率因数最小值大于标准值+4%。";
				score = 100L;
			} else if (minPf >= stdPF
					&& minPf < DataUtil.doubleMultiply(stdPF, 1.04)) {
				rules = "功率因数最小值大于标准值小于标准值+4%。";
				score = 80L;
			} else if (minPf >= DataUtil.doubleMultiply(stdPF, 0.9)
					&& minPf < stdPF) {
				rules = "功率因数最小值小于标准值大于标准值-10%。";
				score = 60L;
			} else if (minPf >= DataUtil.doubleMultiply(stdPF, 0.8)
					&& minPf < DataUtil.doubleMultiply(stdPF, 0.9)) {
				rules = "功率因数最小值小于标准值-10%大于标准值-20%。";
				score = 40L;
			} else if (minPf < new BigDecimal(stdPF).multiply(new BigDecimal(stdPF))
					.multiply(new BigDecimal(0.8)).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue()) {
				rules = "功率因数最小值小于标准值-20%。";
				score = 20L;
			}
			result.setScore(score);
			result.setStValStr(factor.toString());
			result.setAcValStr(minPfD.toString());
			result.setRules(rules);
			result.setdValDouble(minPfD);
		}
		return result;
	}

	/**
	 * 功率因数越限率
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculatePfExceed(Long ledgerId, Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		int totalNumSum = 0;
		int exceedNumSum = 0;
		//标准功率因数
		Double factor = schedulingMapper.getThresholdValue(ledgerId);
		if (DataUtil.parseDouble2BigDecimal(factor) == null) {
			return result;
		}
		Double stdPF = DataUtil.doubleMultiply(factor, 100);
		List<Long> meterIds = this.eleAssessmentMapper.getMeterIdsByLedgerId(ledgerId);
		if (meterIds == null || meterIds.size() == 0) {
			return result;
		}
		for (int i = 0, size=meterIds.size(); i < size; i++) {
			Long meterId = meterIds.get(i);
			//功率因数点数
			int totalNum = this.eleAssessmentMapper.getPfTotalNum(meterId,
					beginTime, endTime);
			//功率因数越限点数
			int exceedNum = this.eleAssessmentMapper.getPfExceedNum(meterId,
					beginTime, endTime, stdPF);
			totalNumSum += totalNum;
			exceedNumSum += exceedNum;
		}
		if (totalNumSum > 0 && exceedNumSum > 0) {
			result = new EasyAdviceScoreBean();
			int percent = (exceedNumSum * 100) / totalNumSum;
			if (percent == 0) {
				rules = "功率因数越限率等于0%。";
				score = 100L;
			} else if (percent > 0 && percent < 5) {
				rules = "功率因数越限率大于0%小于5%。";
				score = 80L;
			} else if (percent >= 5 && percent < 10) {
				rules = "功率因数越限率大于5%小于10%。";
				score = 60L;
			} else if (percent >= 10 && percent < 20) {
				rules = "功率因数越限率大于10小于20%。";
				score = 40L;
			} else if (percent >= 20) {
				rules = "功率因数越限率大于20%。";
				score = 20L;
			}
			
			result.setScore(score);
			result.setStValStr("0%");
			result.setAcValStr(String.valueOf(percent) + "%");
			result.setRules(rules);
		}
		return result;
	}

	/**
	 * 电压不平衡度日均值
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateVBalanAvg(Long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//电压不平衡度
		Double vBalance = this.eleAssessmentMapper.getVBalanAvg(meterId,
				beginTime, endTime);
		if (vBalance != null) {
			result = new EasyAdviceScoreBean();
			if (vBalance < 1.5) {
				rules = "电压不平衡度小于1.5%。";
				score = 100L;
			} else if (vBalance >= 1.5 && vBalance < 2) {
				rules = "电压不平衡度大于1.5%小于2%。";
				score = 80L;
			} else if (vBalance >= 2 && vBalance < 3) {
				rules = "电压不平衡度大于2%小于3%。";
				score = 60L;
			} else if (vBalance >= 3 && vBalance < 4) {
				rules = "电压不平衡度大于3%小于4%。";
				score = 40L;
			} else {
				rules = "电压不平衡度大于4%。";
				score = 20L;
			}

			result.setScore(score);
			result.setStValStr("0%-1.5%");
			result.setAcValStr(vBalance.toString() + "%");
			result.setRules(rules);
			result.setdValDouble(vBalance);
		}

		return result;
	}

	/**
	 * 电压不平衡度日均时间
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateVBalanTimeAvg(Long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//电压不平衡度日均时间
		Long vBalanTime = this.eleAssessmentMapper.getVBalanTimeAvg(meterId,
				beginTime, endTime);
		if (vBalanTime != null) {
			result = new EasyAdviceScoreBean();
			if (vBalanTime < 27) {
				rules = "电压不平衡度越限日均时间小于27分钟。";
				score = 100L;
			} else if (vBalanTime >= 27 && vBalanTime < 36) {
				rules = "电压不平衡度越限日均时间大于27分钟小于36分钟。";
				score = 80L;
			} else if (vBalanTime >= 36 && vBalanTime < 72) {
				rules = "电压不平衡度越限日均时间大于27分钟小于72分钟。";
				score = 60L;
			} else if (vBalanTime >= 72 && vBalanTime < 180) {
				rules = "电压不平衡度越限日均时间大于72分钟小于180分钟。";
				score = 40L;
			} else {
				rules = "电压不平衡度越限日均时间大于360分钟。";
				score = 20L;
			}

			result.setScore(score);
			result.setStValStr("27分钟");
			result.setAcValStr(vBalanTime.toString() + "分钟");
			result.setRules(rules);
			result.setdValDouble(vBalanTime);
		}

		return result;
	}

	/**
	 * 电流不平衡度
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateIBalanAvg(Long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//电流不平衡度日均值
		Double iBalanceAvg = this.eleAssessmentMapper.getIBalanAvg(meterId,
				beginTime, endTime);
		if (iBalanceAvg != null) {
			result = new EasyAdviceScoreBean();
			if (iBalanceAvg < 10) {
				rules = "电流不平衡度小于10%。";
				score = 100L;
			} else if (iBalanceAvg >= 10 && iBalanceAvg < 15) {
				rules = "电流不平衡度大于10%小于15%。";
				score = 80L;
			} else if (iBalanceAvg >= 15 && iBalanceAvg < 20) {
				rules = "电流不平衡度大于15%小于20%。";
				score = 60L;
			} else if (iBalanceAvg >= 20 && iBalanceAvg < 25) {
				rules = "电流不平衡度大于20%小于25%。";
				score = 40L;
			} else {
				rules = "电流不平衡度大于25%。";
				score = 20L;
			}
			
			Double iBalanceAvgD = DoubleUtils.getDoubleValue(iBalanceAvg, 2);
			result.setScore(score);
			result.setStValStr("0%-10%");
			result.setAcValStr(iBalanceAvgD.toString() + "%");
			result.setRules(rules);
			result.setdValDouble(iBalanceAvg);
		}

		return result;
	}

	/**
	 * 计算20kV及以下电压的最大值偏差、最小值偏差(千分数偏差) 所对应的分数
	 * */
	private long compMeterVolDevitation_1(Double rate) {
		long result = 0;
		if (rate <= 56 && DataUtil.doubleAdd(rate, 56) >= 0) { // 100
			result = 100;
		} else if ((rate > 56 && rate <= 59.5) || (DataUtil.doubleAdd(rate, 56) < 0 && DataUtil.doubleAdd(rate, 59.5) >= 0)) { // 80
			result = 80;
		} else if ((rate > 59.5 && rate <= 66.5)
				|| (new BigDecimal(rate).compareTo(new BigDecimal(-59.5)) < 0  && new BigDecimal(rate).compareTo(new BigDecimal(-66.5)) >= 0)) { // 60
			result = 60;
		} else if ((rate > 66.5 && rate <= 70) || (new BigDecimal(rate).compareTo(new BigDecimal(-66.5)) < 0 && new BigDecimal(rate).compareTo(new BigDecimal(-70)) >= 0)) { // 40
			result = 40;
		} else { // 20
			result = 20;
		}
		return result;
	}

	/**
	 * 计算220V电压的最大值偏差、最小值偏差(千分数偏差) 所对应的分数
	 * */
	private long compMeterVolDevitation_2(Double rate) {
		long result = 0;
		if (rate <= 56 && rate >= -80) { // 100
			result = 100;
		} else if ((rate > 56 && rate <= 59.5) || (rate < -80 && rate >= -85)) { // 80
			result = 80;
		} else if ((rate > 59.5 && rate <= 66.5) || (rate < -85 && rate >= -95)) { // 60
			result = 60;
		} else if ((rate > 66.5 && rate <= 70) || (rate < -95 && rate >= -100)) { // 40
			result = 40;
		} else { // 20
			result = 20;
		}
		return result;
	}

	/**
	 * 电压越限率
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateCurVol(Long meterId, Date beginTime,
			Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		int over = eleAssessmentMapper.getMeterOverVol(meterId, beginTime,
				endTime);
		int all = eleAssessmentMapper.getMeterVolCount(meterId, beginTime,
				endTime);
		if (all != 0) {
			result = new EasyAdviceScoreBean();
			double r = DataUtil.doubleDivide(over, all);
			if (new BigDecimal(r).compareTo(BigDecimal.ZERO) == 0) {
				rules = "电压越限率等于0%。";
				score = 100L;
			} else if (r <= 0.05) {
				rules = "电压越限率小于5%大于0%。";
				score = 80L;
			} else if (r <= 0.1) {
				rules = "电压越限率小于10%大于5%。";
				score = 60L;
			} else if (r <= 0.2) {
				rules = "电压越限率大于10%小于20%。";
				score = 40L;
			} else {
				rules = "电压越限率大于20%。";
				score = 20L;
			}

			Double rD = DoubleUtils.getDoubleValue(new BigDecimal(r).multiply(new BigDecimal(100)).doubleValue(), 2);
			result.setScore(score);
			result.setStValStr("0%");
			result.setAcValStr(rD.toString() + "%");
			result.setRules(rules);
			result.setdValDouble(r);
		}

		return result;
	}

	/**
	 * 电度电费合理性
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateEle(long ledgerId, Date beginTime,
			Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//分费率电量
		List<Map<String, Object>> d = eleAssessmentMapper.getTotalFeeEle(
				ledgerId, beginTime, endTime);
		BigDecimal eJ = BigDecimal.ZERO;BigDecimal eG = BigDecimal.ZERO;BigDecimal totalE = BigDecimal.ZERO;
		int sectorId;
		for (Map<String, Object> m : d) {
			sectorId = Integer.valueOf(m.get("SECTORID").toString());
			if (sectorId == 1 || sectorId == 2)
				eJ = eJ.add(new BigDecimal(Double.valueOf(m.get("VALUE").toString())));
			if (sectorId == 4)
				eG = eG.add(new BigDecimal(Double.valueOf(m.get("VALUE").toString())));
			totalE = totalE.add(new BigDecimal(Double.valueOf(m.get("VALUE").toString())));
		}

		if (totalE.compareTo(BigDecimal.ZERO) == 0)
			return result;

		result = new EasyAdviceScoreBean();
		BigDecimal jp = eJ.divide(totalE, 1, BigDecimal.ROUND_HALF_DOWN); // （尖+峰）电量占比
		BigDecimal gt = eG.divide(totalE, 1, BigDecimal.ROUND_HALF_DOWN); // 谷电量占比

		if (jp.compareTo(new BigDecimal(0.2)) < 0 && gt.compareTo(new BigDecimal(0.5)) >= 0) {
			score = 100L;
			rules = "尖+峰电量占比小于20% 且谷电量占比大于50%。";
		}
		if (((jp.compareTo(new BigDecimal(0.2)) >= 0 && jp.compareTo(new BigDecimal(0.6)) < 0) || jp.compareTo(new BigDecimal(0.2)) < 0) && gt.compareTo(new BigDecimal(0.5)) < 0) {
			score = 80L;
			rules = "尖+峰电量占比大于20%小于60% 且谷电量占比小于50%。";
		}
		if (jp.compareTo(new BigDecimal(0.6)) >= 0 && jp.compareTo(new BigDecimal(0.7)) < 0) {
			score = 60L;
			rules = "尖+峰电量占比大于60% 小于70%。";
		}
		if (jp.compareTo(new BigDecimal(0.7)) >= 0 && jp.compareTo(new BigDecimal(0.8)) < 0) {
			score = 40L;
			rules = "尖+峰电量占比大于70% 小于80%。";
		}
		if (jp.compareTo(new BigDecimal(0.8)) >= 0) {
			score = 20L;
			rules = "尖+峰电量占比大于80%。";
		}

		Double jpD = DoubleUtils.getDoubleValue(jp.multiply(new BigDecimal(100)).doubleValue(), 2);
		Double gtD = DoubleUtils.getDoubleValue(gt.multiply(new BigDecimal(100)).doubleValue(), 2);

		result.setScore(score);
		result.setStValStr("尖+峰电量占比小于20%且谷电量大于50%");
		result.setAcValStr("尖+峰电量占比：" + jpD.toString() + "%；" + "谷电量占比："
				+ gtD.toString() + "%");
		result.setRules(rules);
		result.setdValDouble(jp.doubleValue());

		return result;
	}

	/**
	 * 专变月平均功率因数
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculatePF(long ledgerId, long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//标准功率因数
		Double bPf = eleAssessmentMapper.getThresholdValue(ledgerId);

		if (bPf != null) {
			Double q = eleAssessmentMapper.getMeterQ(meterId, beginTime,
					endTime);
			Double rq = eleAssessmentMapper.getMeterRQ(meterId, beginTime,
					endTime);
			double pf = DataUtil.getPF(q, rq);
			if (bPf != null && pf > 0) {BigDecimal bPfBigDecm = new BigDecimal(bPf);BigDecimal pfBigDecm = new BigDecimal(pf);
				result = new EasyAdviceScoreBean();
				if (pfBigDecm.compareTo(bPfBigDecm.add(new BigDecimal(0.08))) >= 0) {
					rules = "专变月平均功率因数大于标准值+8%。";
					score = 100l;
				} else if (pfBigDecm.compareTo(bPfBigDecm.add(new BigDecimal(0.04))) >= 0 && pfBigDecm.compareTo(bPfBigDecm.add(new BigDecimal(0.08))) < 0) {
					rules = "专变月平均功率因数大于标准值+4%小于标准值+8%。";
					score = 80l;
				} else if (pfBigDecm.compareTo(bPfBigDecm) >= 0 && pfBigDecm.compareTo(bPfBigDecm.add(new BigDecimal(0.04))) < 0) {
					rules = "专变月平均功率因数大于标准值小于+4%。";
					score = 60l;
				} else if (pfBigDecm.compareTo(bPfBigDecm.subtract(new BigDecimal(0.2))) >= 0 && pfBigDecm.compareTo(bPfBigDecm) < 0) {
					rules = "专变月平均功率因数小于标准值大于标准值-20%。";
					score = 40l;
				} else if (pfBigDecm.compareTo(bPfBigDecm.subtract(new BigDecimal(0.2))) < 0) {
					rules = "专变月平均功率因数小于标准值-20%。";
					score = 20l;
				}

				result.setScore(score);
				result.setStValStr(bPf.toString());
				result.setAcValStr(String.valueOf(pf));
				result.setRules(rules);
				result.setdValDouble(pf);
			}

		}

		return result;
	}

	/**
	 * 无功倒送功率
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateRP(long meterId, Date beginTime,
			Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		//最小无功功率
		Double rq = eleAssessmentMapper.getMeterMinRP(meterId, beginTime,
				endTime);
		//额定功率
		Double pwr = eleAssessmentMapper.getThresholdPwrValue(meterId);
		if (rq != null && pwr != null) {
			result = new EasyAdviceScoreBean();
			if (rq >= 0) {
				rules = "无功倒送功率最小值大于0。";
				score = 100l;
			} else if (Math.abs(rq) <= (DataUtil.doubleMultiply(pwr, 0.1))) {
				rules = "无功倒送功率最小值小于0大于额定功率*0.1。";
				score = 80l;
			} else if (Math.abs(rq) <= (DataUtil.doubleMultiply(pwr, 0.2))) {
				rules = "无功倒送功率最小值小于额定功率*0.1大于额定功率*0.2。";
				score = 60l;
			} else if (Math.abs(rq) <= (DataUtil.doubleMultiply(pwr, 0.4))) {
				rules = "无功倒送功率最小值小于额定功率*0.2大于额定功率*0.4。";
				score = 40l;
			} else {
				rules = "无功倒送功率最小值小于额定功率*0.4。";
				score = 20l;
			}

			result.setScore(score);
			result.setStValStr(pwr.toString() + "kW");
			result.setAcValStr(rq.toString() + "kW");
			result.setRules(rules);
			result.setdValDouble(Math.abs(rq));
		}

		return result;

	}

	/**
	 * 无功倒送时间
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateRPTime(long meterId, Date beginTime,
			Date endTime) {
		EasyAdviceScoreBean result = new EasyAdviceScoreBean();
		Long score = null;
		String rules = "";
		//无功倒送时间
		int time = eleAssessmentMapper.getMeterMinRPTime(meterId, beginTime,
				endTime) * 15;

		if (time == 0) {
			rules = "无功倒送时间等于0分钟。";
			score = 100l;
		} else if (time <= 15) {
			rules = "无功倒送时间大于0分钟小于15分钟。";
			score = 80l;
		} else if (time <= 30) {
			rules = "无功倒送时间大于15分钟小于30分钟。";
			score = 60l;
		} else if (time <= 60) {
			rules = "无功倒送时间大于30分钟小于60分钟。";
			score = 40l;
		} else {
			rules = "无功倒送时间大于60分钟。";
			score = 20l;
		}

		result.setScore(score);
		result.setStValStr("0分钟");
		result.setAcValStr(String.valueOf(time) + "分钟");
		result.setRules(rules);
		result.setdValDouble((double) time);

		return result;
	}

	/**
	 * 基本电费
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateBasicFee(long ledgerId, long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		long volume = 0;// 变压器容量
		int declareType;// 申报类型;1,容量;2,需量
		Double declareValue;// 申报值
		Double maxDemand;
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("beginTime", beginTime);
		queryMap.put("endTime", endTime);

		queryMap.put("pointId", meterId);
		List<Map<String, Object>> basicFeeInfos = costMapper
				.getMeterBasicFeeInfo(queryMap);// 取基本电费信息

		if (basicFeeInfos != null && basicFeeInfos.size() > 0) {
			Map<String, Object> basicFeeInfo = basicFeeInfos.get(0);
			if (basicFeeInfo != null && basicFeeInfo.size() > 0) {
				if (basicFeeInfo.get("VOLUME") != null) {
					volume = Long
							.valueOf(basicFeeInfo.get("VOLUME").toString());
				}
				if (basicFeeInfo.get("DECLARETYPE") != null) {
					declareType = Integer.valueOf(basicFeeInfo.get(
							"DECLARETYPE").toString());

					maxDemand = costMapper.getMaxDemandValue(queryMap);// 取月最大需量
					if (maxDemand != null) {
						result = new EasyAdviceScoreBean();
						if (declareType == 1) {
							List<RateSectorBean> rates = costMapper
									.getPointRateInfo(meterId);// 取测量点费率信息
							if (rates != null && rates.size() > 0) {
								Map<String, Object> basicPrice = costMapper
										.getBasicFeePrice(rates.get(0)
												.getRateId());
								if (basicPrice != null) {
									if (basicPrice.get("VOLRATE") != null
											&& basicPrice.get("DERATE") != null) {
										Double j = new BigDecimal(Double.valueOf(basicPrice.get("VOLRATE").toString()))
												.multiply(new BigDecimal(volume))
												.divide(new BigDecimal(Double.valueOf(basicPrice.get("DERATE").toString())),
														2,
														BigDecimal.ROUND_HALF_DOWN).doubleValue();
										if (maxDemand >= j) {
											rules = "采用容量模式：变压器计量点所分析时段的日最大需量最大值大于标准值。";
											score = 100l;
										} else if (maxDemand >= DataUtil.doubleMultiply(j, 0.8)) {
											rules = "采用容量模式：变压器计量点所分析时段的日最大需量最大值大于标准值*0.8小于标准值。";
											score = 80l;
										} else if (maxDemand >= DataUtil.doubleMultiply(j, 0.6)) {
											rules = "采用容量模式：变压器计量点所分析时段的日最大需量最大值大于标准值*0.6小于标准值*0.8。";
											score = 60l;
										} else if (maxDemand >= DataUtil.doubleMultiply(j, 0.4)) {
											rules = "采用容量模式：变压器计量点所分析时段的日最大需量最大值大于标准值*0.4小于标准值*0.6。";
											score = 40l;
										} else {
											rules = "采用容量模式：变压器计量点所分析时段的日最大需量最大值小于标准值*0.4。";
											score = 20l;
										}

										result.setScore(score);
										result.setStValStr(j.toString() + "kW");
										result.setAcValStr(maxDemand.toString()
												+ "kW");
										result.setRules(rules);
										result.setdValDouble((double) score);
									}
								}
							}
						} else if (declareType == 2
								&& basicFeeInfo.get("DECLAREVALUE") != null) {
							declareValue = Double.valueOf(basicFeeInfo.get(
									"DECLAREVALUE").toString());
							double p = new BigDecimal(maxDemand).subtract(new BigDecimal(declareValue))
									.divide(new BigDecimal(declareValue), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
							if (p <= 0.05 && DataUtil.doubleAdd(p, 0.05) >= 0) {
								rules = "采用需量模式：变压器计量点所分析时段的日最大需量最大值与申报值偏差率小于标准值的5%。";
								score = 100l;
							} else if (p > 0.05 && p <= 0.1) {
								rules = "采用需量模式：变压器计量点所分析时段的日最大需量最大值与申报值偏差率大于标准值的5%小于10%。";
								score = 80l;
							} else if (p > 0.1 && p <= 0.2) {
								rules = "采用需量模式：变压器计量点所分析时段的日最大需量最大值与申报值偏差率大于标准值的10%小于20%。";
								score = 60l;
							} else if ((p > 0.2 && p <= 0.3)
									|| (DataUtil.doubleAdd(p, 0.1) >= 0 && DataUtil.doubleAdd(p, 0.05) < 0)) {
								rules = "采用需量模式：变压器计量点所分析时段的日最大需量最大值与申报值偏差率大于标准值的20%小于30%。"
										+ "或大于-10%小于-5%";
								score = 40l;
							} else {
								rules = "采用需量模式：变压器计量点所分析时段的日最大需量最大值与申报值偏差率大于标准值的30%或小于-10%。";
								score = 20l;
							}

							result.setScore(score);
							result.setStValStr(declareValue.toString() + "kW");
							result.setAcValStr(maxDemand.toString() + "kW");
							result.setRules(rules);
							result.setdValDouble((double) score);
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * 负载率
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateLoad(long meterId, Date beginTime,
			Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";

		//负载率
		Double load = eleAssessmentMapper.getMeterAvgLoad(meterId, beginTime,
				endTime);
		if (load != null) {
			result = new EasyAdviceScoreBean();
			if (load > 70 && load <= 80) {
				score = 100l;
				rules = "负载率在70%-80%之间。";
			} else if ((load > 60 && load <= 70) || (load > 80 && load <= 90)) {
				score = 80l;
				rules = "负载率在60%-70%之间或80%-90%之间。";
			} else if ((load > 50 && load <= 60) || (load > 90 && load <= 95)) {
				score = 60l;
				rules = "负载率在50%-60%之间或90-95%之间。";
			} else if ((load > 30 && load <= 50) || (load > 95 && load <= 100)) {
				score = 40l;
				rules = "负载率在30%-50%之间或95%-100%之间。";
			} else {
				score = 20l;
				rules = "负载率低于30%或大于100%。";
			}

			result.setScore(score);
			result.setStValStr("70%-80%");
			result.setAcValStr(load.toString() + "%");
			result.setRules(rules);
			result.setdValDouble(Math.abs(DataUtil.doubleSubtract(load, 60d)));

		}

		return result;
	}

	/**
	 * 电压总谐波畸变率最大值
	 * @param volLevel
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateTotalDisV(int volLevel, Long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		String rules = "";

		long score = 0;
		long num = 0;
		double volA = 0;//A相电压
		double volB = 0;//B相电压
		double volC = 0;//C相电压
		long scoreA = 0;
		long scoreB = 0;
		long scoreC = 0;

		Double staDisV = null;	if(volLevel!=0)
		if (HarConstant.getHarVolTotalStand(volLevel) != null) {staDisV=HarConstant.getHarVolTotalStand(volLevel);
			//电压总谐波畸变率最大值
			List<Map<String, Object>> disv = eleAssessmentMapper
					.getMeterMaxDisV(meterId, beginTime, endTime);
			if (disv != null) {
				for (Map<String, Object> v : disv) {
					if (v == null)
						continue;
					if (v.get("VA") != null) {
						volA = Double.valueOf(v.get("VA").toString());
						scoreA = getDisvScore(staDisV, volA);
						score += scoreA;
						num++;
					}
					if (v.get("VB") != null) {
						volB = Double.valueOf(v.get("VB").toString());
						scoreB = getDisvScore(staDisV, volB);
						score += scoreB;
						num++;
					}
					if (v.get("VC") != null) {
						volC = Double.valueOf(v.get("VC").toString());
						scoreC = getDisvScore(staDisV, volC);
						score += scoreC;
						num++;
					}
				}
			}
		}

		if (num != 0) {
			result = new EasyAdviceScoreBean();
			long scoreL = score / num;
			if (scoreA == 20l) {
				rules = rules + "A相电压大于标准值*1.5。\n";
			} else if (scoreA == 40l) {
				rules = rules + "A相电压大于标准值小于标准值*1.5。\n";
			} else if (scoreA == 60l) {
				rules = rules + "A相电压大于标准值*0.8小于标准值。\n";
			} else if (scoreA == 80l) {
				rules = rules + "A相电压大于标准值*0.4小于标准值*0.8。\n";
			} else if (scoreA == 100l) {
				rules = rules + "A相电压小于标准值*0.4。\n";
			}
			
			if (scoreB == 20l) {
				rules = rules + "B相电压大于标准值*1.5。\n";
			} else if (scoreB == 40l) {
				rules = rules + "B相电压大于标准值小于标准值*1.5。\n";
			} else if (scoreB == 60l) {
				rules = rules + "B相电压大于标准值*0.8小于标准值。\n";
			} else if (scoreB == 80l) {
				rules = rules + "B相电压大于标准值*0.4小于标准值*0.8。\n";
			} else if (scoreB == 100l) {
				rules = rules + "B相电压小于标准值*0.4。\n";
			}
			
			if (scoreC == 20l) {
				rules = rules + "C相电压大于标准值*1.5。\n";
			} else if (scoreC == 40l) {
				rules = rules + "C相电压大于标准值小于标准值*1.5。\n";
			} else if (scoreC == 60l) {
				rules = rules + "C相电压大于标准值*0.8小于标准值。\n";
			} else if (scoreC == 80l) {
				rules = rules + "C相电压大于标准值*0.4小于标准值*0.8。\n";
			} else if (scoreC == 100l) {
				rules = rules + "C相电压小于标准值*0.4。\n";
			}
			result.setScore(scoreL);
			result.setStValStr(staDisV.toString() + "kV");
			result.setAcValStr("A相电压为" + String.valueOf(volA) + "kV；B相电压为"
					+ String.valueOf(volB) + "kV；C相电压为" + String.valueOf(volC)
					+ "kV");
			result.setRules(rules);
			result.setdValDouble((double) scoreL);
		}

		return result;
	}

	/**
	 * 各奇次谐波电压含有率最大值
	 * @param volLevel
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateDisV(int volLevel, Long meterId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		String rules = "";

		long score = 0;
		long num = 0;
		double volA = 0;//A相电压
		double volB = 0;//B相电压
		double volC = 0;//C相电压
		long scoreA = 0;
		long scoreB = 0;
		long scoreC = 0;

		Double staDisV = null;if(volLevel!=0)
		if (HarConstant.getHarVolStand(volLevel) != null) {	staDisV=HarConstant.getHarVolStand(volLevel);
			//各奇次谐波电压含有率最大值
			List<Map<String, Object>> disv = eleAssessmentMapper
					.getMeterMaxHarV(meterId, beginTime, endTime);
			if (disv != null) {
				for (Map<String, Object> v : disv) {
					if (v == null)
						continue;
					if (v.get("VA") != null) {
						volA = Double.valueOf(v.get("VA").toString());
						scoreA = getDisvScore(staDisV, volA);
						score += scoreA;
						num++;
					}
					if (v.get("VB") != null) {
						volB = Double.valueOf(v.get("VB").toString());
						scoreB = getDisvScore(staDisV, volB);
						score += scoreB;
						num++;
					}
					if (v.get("VC") != null) {
						volC = Double.valueOf(v.get("VC").toString());
						scoreC = getDisvScore(staDisV, volC);
						score += scoreC;
						num++;
					}
				}
			}
		}

		if (num != 0) {
			result = new EasyAdviceScoreBean();
			long scoreL = score / num;
			if (scoreA == 20l) {
				rules = rules + "A相电压大于标准值*1.5。\n";
			} else if (scoreA == 40l) {
				rules = rules + "A相电压大于标准值小于标准值*1.5。\n";
			} else if (scoreA == 60l) {
				rules = rules + "A相电压大于标准值*0.8小于标准值。\n";
			} else if (scoreA == 80l) {
				rules = rules + "A相电压大于标准值*0.4小于标准值*0.8。\n";
			} else if (scoreA == 100l) {
				rules = rules + "A相电压小于标准值*0.4。\n";
			}
			
			if (scoreB == 20l) {
				rules = rules + "B相电压大于标准值*1.5。\n";
			} else if (scoreB == 40l) {
				rules = rules + "B相电压大于标准值小于标准值*1.5。\n";
			} else if (scoreB == 60l) {
				rules = rules + "B相电压大于标准值*0.8小于标准值。\n";
			} else if (scoreB == 80l) {
				rules = rules + "B相电压大于标准值*0.4小于标准值*0.8。\n";
			} else if (scoreB == 100l) {
				rules = rules + "B相电压小于标准值*0.4。\n";
			}
			
			if (scoreC == 20l) {
				rules = rules + "C相电压大于标准值*1.5。\n";
			} else if (scoreC == 40l) {
				rules = rules + "C相电压大于标准值小于标准值*1.5。\n";
			} else if (scoreC == 60l) {
				rules = rules + "C相电压大于标准值*0.8小于标准值。\n";
			} else if (scoreC == 80l) {
				rules = rules + "C相电压大于标准值*0.4小于标准值*0.8。\n";
			} else if (scoreC == 100l) {
				rules = rules + "C相电压小于标准值*0.4。\n";
			}

			result.setScore(scoreL);
			result.setStValStr(staDisV.toString() + "kV");
			result.setAcValStr("A相电压为" + String.valueOf(volA) + "kV；B相电压为"
					+ String.valueOf(volB) + "kV；C相电压为" + String.valueOf(volC)
					+ "kV");
			result.setRules(rules);
			result.setdValDouble((double) scoreL);
		}
		return result;
	}

	/**
	 * 各奇次谐波电流含有率最大值
	 * @param volLevel
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateDisI(int volLevel, Long meterId,
			Date beginTime, Date endTime) {

		EasyAdviceScoreBean result = null;
		String rules = "";

		long score = 0;
		long num = 0;
		double volA = 0;//A相电流
		double volB = 0;//B相电流
		double volC = 0;//C相电流
		long scoreA = 0;
		long scoreB = 0;
		long scoreC = 0;
		int harNo;
		Double staDisI = null;

		//各奇次谐波电流含有率最大值
		List<Map<String, Object>> disi = eleAssessmentMapper.getMeterMaxHarI(
				meterId, beginTime, endTime);
		if (disi != null) {
			for (Map<String, Object> v : disi) {
				if (v == null)
					continue;
				harNo = Integer.valueOf(v.get("HNUM").toString());
				staDisI = HarConstant.getHarIStand(volLevel, harNo);
				if (staDisI != null) {
					if (v.get("IA") != null) {
						volA = Double.valueOf(v.get("IA").toString());
						scoreA = getDisvScore(staDisI, volA);
						score += scoreA;
						num++;
					}
					if (v.get("IB") != null) {
						volB = Double.valueOf(v.get("IB").toString());
						scoreB = getDisvScore(staDisI, volB);
						score += scoreB;
						num++;
					}
					if (v.get("IC") != null) {
						volC = Double.valueOf(v.get("IC").toString());
						scoreC = getDisvScore(staDisI, volC);
						score += scoreC;
						num++;
					}
				}
			}
		}

		if (num != 0) {
			result = new EasyAdviceScoreBean();
			long scoreL = score / num;
			if (scoreA == 20l) {
				rules = rules + "A相电流大于标准值*1.5。\n";
			} else if (scoreA == 40l) {
				rules = rules + "A相电流大于标准值小于标准值*1.5。\n";
			} else if (scoreA == 60l) {
				rules = rules + "A相电流大于标准值*0.8小于标准值。\n";
			} else if (scoreA == 80l) {
				rules = rules + "A相电流大于标准值*0.4小于标准值*0.8。\n";
			} else if (scoreA == 100l) {
				rules = rules + "A相电流小于标准值*0.4。\n";
			}
			
			if (scoreB == 20l) {
				rules = rules + "B相电流大于标准值*1.5。\n";
			} else if (scoreB == 40l) {
				rules = rules + "B相电流大于标准值小于标准值*1.5。\n";
			} else if (scoreB == 60l) {
				rules = rules + "B相电流大于标准值*0.8小于标准值。\n";
			} else if (scoreB == 80l) {
				rules = rules + "B相电流大于标准值*0.4小于标准值*0.8。\n";
			} else if (scoreB == 100l) {
				rules = rules + "B相电流小于标准值*0.4。\n";
			}
			
			if (scoreC == 20l) {
				rules = rules + "C相电流大于标准值*1.5。\n";
			} else if (scoreC == 40l) {
				rules = rules + "C相电流大于标准值小于标准值*1.5。\n";
			} else if (scoreC == 60l) {
				rules = rules + "C相电流大于标准值*0.8小于标准值。\n";
			} else if (scoreC == 80l) {
				rules = rules + "C相电流大于标准值*0.4小于标准值*0.8。\n";
			} else if (scoreC == 100l) {
				rules = rules + "C相电流小于标准值*0.4。\n";
			}
			
			result.setScore(scoreL);
			result.setStValStr(staDisI.toString() + "kA");
			result.setAcValStr("A相电流为" + String.valueOf(volA) + "kA；B相电流为"
					+ String.valueOf(volB) + "kA；C相电流为" + String.valueOf(volC)
					+ "kA");
			result.setRules(rules);
			result.setdValDouble((double) scoreL);
		}
		return result;
	}

	//计算得分方法
	private long getDisvScore(double staDisV, double v) {
		if (v < DataUtil.doubleMultiply(staDisV, 0.4))
			return 100l;
		else if (v < DataUtil.doubleMultiply(staDisV, 0.8))
			return 80l;
		else if (v < staDisV)
			return 60l;
		else if (v < DataUtil.doubleMultiply(staDisV, 1.5))
			return 40l;
		else
			return 20l;
	}

	/**
	 * 线损
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private EasyAdviceScoreBean calculateLineloss(long ledgerId,
			Date beginTime, Date endTime) {
		EasyAdviceScoreBean result = null;
		Long score = null;
		String rules = "";
		double total = 0;
		double totalUse = 0;
		// 查询语句中做了减一天处理，这里不再处理
		// Date endDate = DateUtil.addDateDay(endTime, -1);
		// Date beginDate = DateUtil.addDateDay(beginTime, -1);

		List<LineLossBean> lineData = new ArrayList<LineLossBean>();
		List<Long> meterLevel1 = schedulingMapper.getLineLossByLevel(ledgerId,
				1);
		if (meterLevel1 != null && meterLevel1.size() > 0) {
			String meterIds = processIds(meterLevel1);	List<Long> mids = new ArrayList<Long>(); if(meterIds.split(",").length > 0) { String[] ms =meterIds.split(","); for (int i = 0; i <ms.length; i++) {mids.add(Long.parseLong(ms[i]));}};
			lineData = schedulingMapper.getDayLineLossInfo(mids, beginTime,
					endTime);
			for (LineLossBean lb : lineData) {
				total = new BigDecimal(total).add(new BigDecimal(lb.getCoul())).doubleValue();
				//线损表子表配置
				List<Long> meters = schedulingMapper.getLineLossMeters(lb
						.getMeterId(), 2);
				if (meters == null || meters.size() == 0)
					continue;
				meterIds = processIds(meters);  mids = new ArrayList<Long>(); if(meterIds.split(",").length > 0) { String[] ms =meterIds.split(","); for (int i = 0; i <ms.length; i++) {mids.add(Long.parseLong(ms[i]));}};
				List<LineLossBean> childLineData = schedulingMapper
						.getDayLineLossInfo(mids, beginTime, endTime);
				for (LineLossBean clb : childLineData) {
					totalUse = new BigDecimal(totalUse).add(new BigDecimal(clb.getCoul())).doubleValue();
				}
			}
		}

		if (total != 0 && totalUse != 0) {
			result = new EasyAdviceScoreBean();
			double r = new BigDecimal(total).subtract(new BigDecimal(totalUse)).divide(new BigDecimal(total), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
			if (r <= 0.07) {
				score = 100L;
				rules = "线损大于0%小于7%。";
			} else if (r <= 0.1) {
				score = 80L;
				rules = "线损大于7%小于10%。";
			} else if (r <= 0.15) {
				score = 60L;
				rules = "线损大于10%小于15%。";
			} else if (r <= 0.2) {
				score = 40L;
				rules = "线损大于15%小于20%。";
			} else {
				score = 20L;
				rules = "线损大于20%。";
			}
			
			Double rD = DoubleUtils.getDoubleValue(new BigDecimal(r).multiply(new BigDecimal(100)).doubleValue(), 2);
			result.setScore(score);
			result.setStValStr("0%-7%");
			result.setAcValStr(rD.toString() + "%");
			result.setRules(rules);
			result.setdValDouble(rD);
		}

		return result;
	}

	//处理list中的id
	private String processIds(List<Long> meterLevel1) {
		StringBuffer meterIds = new StringBuffer("");
		for (int i = 0,size = meterLevel1.size(); i < size; i++) {
			if (i == (meterLevel1.size() - 1))
				meterIds.append(meterLevel1.get(i));
			else
				meterIds.append(meterLevel1.get(i)).append(",");
		}
		return meterIds.toString();
	}

	//处理各项得分的权重
	private Map<Integer, Integer> processScoreItemWeight(
			List<Map<String, Object>> items) {
		Map<Integer, Integer> r = new HashMap<Integer, Integer>();

		for (Map<String, Object> m : items) {
			r.put(Integer.valueOf(m.get("ID").toString()), Integer.valueOf(m
					.get("WEIGHT").toString()));
		}

		return r;
	}

	@Override
	public void assessment(Long accountId) {
		
		List<EasyAdviceScoreBean> scoreList = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//获取各项得分的权重
			List<Map<String, Object>> items = eleAssessmentMapper.getScoreItemWeight();
			Map<Integer, Integer> weight = processScoreItemWeight(items);
			//获取评分最低的三项
			scoreList = wechatAssessment(accountId, weight);		
		} catch (Exception e) {
			Log.error("WechatAssessment", e);
		}
		if (scoreList != null) {		
			result.put("scoreList", scoreList);		
		}
	
		//将评分信息放入缓存
		assessmentCache.put(accountId, result);
	}

	@Override
	public ConcurrentHashMap<Long, Object> getAssessmentCache() {
		return assessmentCache;
	}
}
