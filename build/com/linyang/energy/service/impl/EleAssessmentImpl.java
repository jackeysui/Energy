package com.linyang.energy.service.impl;

import java.math.BigDecimal;import java.math.RoundingMode;import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.common.SpringContextHolder;
import com.linyang.energy.common.CommonResource;
import com.linyang.energy.mapping.authmanager.UserBeanMapper;
import com.linyang.energy.mapping.eleAssessment.EleAssessmentMapper;
import com.linyang.energy.mapping.energyanalysis.SchedulingMapper;
import com.linyang.energy.mapping.energysavinganalysis.CostMapper;
import com.linyang.energy.mapping.phone.PhoneMapper;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.LineLossBean;
import com.linyang.energy.model.RateSectorBean;
import com.linyang.energy.model.ScoreItemBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.EleAssessment;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.HarConstant;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.DoubleUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by Administrator on 15-10-13.
 */
@Service
public class EleAssessmentImpl implements EleAssessment {

	@Autowired
	private EleAssessmentMapper eleAssessmentMapper;
	@Autowired
	private UserBeanMapper userBeanMapper;
	@Autowired
	private PhoneMapper phoneMapper;
	@Autowired
	private SchedulingMapper schedulingMapper;
	@Autowired
	private CostMapper costMapper;

	private ConcurrentHashMap<Long, Object> assessmentCache = new ConcurrentHashMap<Long, Object>();

	@Override
	public Map<String, Object> safetyAssessment(Long accountId, Map<Integer, Integer> weight) {
		Map<String, Object> result = new HashMap<String, Object>();
		String errMessage = "";
		UserBean userBean = this.userBeanMapper.getUserByAccountId(accountId);
		if (userBean != null && userBean.getLedgerId() != null) {
			Long ledgerId = userBean.getLedgerId();
			LedgerBean ledger = this.phoneMapper.getLedgerById(ledgerId);
			if (ledger != null && ledger.getAnalyType() == 102) {
				Date endTime = DateUtil.clearDate(new Date()); // 结束时间
				Date beginTime = DateUtil.getDateBetween(endTime, -30); // 起始时间
				List<Long> meterIds = this.eleAssessmentMapper.getMeterIdsByLedgerId(ledgerId);
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
				int score = 0;
				for (int i = 0; i < meterIds.size(); i++) {
					Long meterId = meterIds.get(i);
					// 功率越限
					Long powerExceed = calculatePowerExceed(meterId, beginTime, endTime);
					if (powerExceed != null) {
						powerExceedSum += powerExceed;
						powerExceedNum += 1;
					}
					// 电流越限
					Long curExceed = calculateCurExceed(meterId, beginTime, endTime);
					if (curExceed != null) {
						curExceedSum += curExceed;
						curExceedNum += 1;
					}
					// 电压允许偏差
					Long volDeviation = calculateVolDeviation(meterId, beginTime, endTime);
					if (volDeviation != null) {
						volDeviationSum += volDeviation;
						volDeviationNum += 1;
					}
					// 最大电压不平衡度
					Long volBalan = calculateVolBanlan(meterId, beginTime, endTime);
					if (volBalan != null) {
						volBalanSum += volBalan;
						volBalanNum += 1;
					}
					// 最大电流不平衡度
					Long iBalan = calculateIBanlan(meterId, beginTime, endTime);
					if (iBalan != null) {
						iBalanSum += iBalan;
						iBalanNum += 1;
					}
					// 最大负载率
					Long maxLoad = calculateMaxLoad(meterId, beginTime, endTime);
					if (maxLoad != null) {
						maxLoadSum += maxLoad;
						maxLoadNum += 1;
					}
				}
                List<ScoreItemBean> list = new ArrayList<ScoreItemBean>();
                int w = 0;
				if (powerExceedNum != 0) {
                    list.add(new ScoreItemBean(1, "功率越限", powerExceedSum / powerExceedNum));
					score += (powerExceedSum / powerExceedNum) * weight.get(1);
					w += weight.get(1);
				}
				if (curExceedNum != 0) {
					list.add(new ScoreItemBean(2, "电流越限", curExceedSum / curExceedNum));
					score += (curExceedSum / curExceedNum) * weight.get(2);
					w += weight.get(2);
				}
				if (volDeviationNum != 0) {
					list.add(new ScoreItemBean(3, "电压允许偏差", volDeviationSum / volDeviationNum));
					score += (volDeviationSum / volDeviationNum) * weight.get(3);
					w += weight.get(3);
				}
				if (volBalanNum != 0) {
                    list.add(new ScoreItemBean(4, "最大电压不平衡度", volBalanSum / volBalanNum));
					score += (volBalanSum / volBalanNum) * weight.get(4);
					w += weight.get(4);
				}
				if (iBalanNum != 0) {
                    list.add(new ScoreItemBean(5, "最大电流不平衡度", iBalanSum / iBalanNum));
					score += (iBalanSum / iBalanNum) * weight.get(5);
					w += weight.get(5);
				}
				if (maxLoadNum != 0) {
                    list.add(new ScoreItemBean(6, "最大负载率", maxLoadSum / maxLoadNum));
					score += (maxLoadSum / maxLoadNum) * weight.get(6);
					w += weight.get(6);
				}
				result.put("safetyAssess", list);
				// 计算安全性总分(按权重)
                double safetyScore = 0;
                if(w != 0){
                    safetyScore = DataUtil.doubleDivide(score, w);
                }
				result.put("safetyScore", safetyScore);

			} else {
				errMessage = "该用户不是企业用户！";
			}
		} else {
			errMessage = "该用户不存在！";
		}
		result.put("errMessage", errMessage);
		return result;
	}

	@Override
	public Map<String, Object> qualityAssessment(Long accountId, Map<Integer, Integer> weight) {
		Map<String, Object> result = new HashMap<String, Object>();
		String errMessage = "";
		UserBean userBean = this.userBeanMapper.getUserByAccountId(accountId);
		if (userBean != null && userBean.getLedgerId() != null) {
			Long ledgerId = userBean.getLedgerId();
			LedgerBean ledger = this.phoneMapper.getLedgerById(ledgerId);
			if (ledger != null && ledger.getAnalyType() == 102) {
				Date endTime = DateUtil.clearDate(new Date()); // 结束时间
				Date beginTime = DateUtil.getDateBetween(endTime, -30); // 起始时间
				List<Long> meterIds = this.eleAssessmentMapper.getMeterIdsByLedgerId(ledgerId);
				long minPfSum = 0;
				long minPfNum = 0;
				long pfExceedSum = 0;
				long pfExceedNum = 0;
				long volDeviationSum = 0;
				long volDeviationNum = 0;
				long vBalanSum = 0;
				long vBalanNum = 0;
				long vBalanTimeSum = 0;
				long vBalanTimeNum = 0;
				long iBalanSum = 0;
				long iBalanNum = 0;
				double tdisvSum = 0;
				double disvSum = 0;
				double disiSum = 0;
				long volSum = 0;
				long volNum = 0;
				long tdisvNum = 0;
				long disvNum = 0;
				long disiNum = 0;
				double score = 0;
				for (int i = 0; i < meterIds.size(); i++) {
					Long meterId = meterIds.get(i);
					// 功率因数最小值
					Long minPf = calculateMinPf(ledgerId, meterId, beginTime, endTime);
					if (minPf != null) {
						minPfSum += minPf;
						minPfNum += 1;
					}
					// 功率因数越限率
					Long pfExceed = calculatePfExceed(ledgerId, meterId, beginTime, endTime);
					if (pfExceed != null) {
						pfExceedSum += pfExceed;
						pfExceedNum += 1;
					}
					// 电压越限率
					Long vol = calculateCurVol(meterId, beginTime, endTime);
					if (vol != null) {
						volSum += vol;
						volNum++;
					}
					// 电压允许偏差
					Long volDeviation = calculateVolDeviation(meterId, beginTime, endTime);
					if (volDeviation != null) {
						volDeviationSum += volDeviation;
						volDeviationNum += 1;
					}
					// 电压不平衡度日均值
					Long vBalan = calculateVBalanAvg(meterId, beginTime, endTime);
					if (vBalan != null) {
						vBalanSum += vBalan;
						vBalanNum += 1;
					}
					
					Integer volLevel = eleAssessmentMapper.getMeterVolLevel(meterId);
					if (volLevel != null) {
						if (volLevel.intValue() == 1)
							volLevel = 380;
						else if (volLevel.intValue() == 3)
							volLevel = 6;
						else if (volLevel.intValue() == 20)
							volLevel = 10;
						else if (volLevel.intValue() > 110)
							volLevel = 110;
						Double tdisVT = calculateTotalDisV(volLevel, meterId, beginTime, endTime);
						if (tdisVT != null) {
							tdisvSum = DataUtil.doubleAdd(tdisvSum, tdisVT);
							tdisvNum++;
						}
						Double disVT = calculateDisV(volLevel, meterId, beginTime, endTime);
						if (disVT != null) {
							disvSum = DataUtil.doubleAdd(disvSum, disVT);
							disvNum++;
						}
						Double disIT = calculateDisI(volLevel, meterId, beginTime, endTime);
						if (disIT != null) {
							disiSum = DataUtil.doubleAdd(disiSum, disIT);
							disiNum++;
						}
					}
					
					// 电压不平衡度日均时间
					Long vBalanTime = calculateVBalanTimeAvg(meterId, beginTime, endTime);
					if (vBalanTime != null) {
						vBalanTimeSum += vBalanTime;
						vBalanTimeNum += 1;
					}
					// 电流不平衡度日均值
					Long iBalan = calculateIBalanAvg(meterId, beginTime, endTime);
					if (iBalan != null) {
						iBalanSum += iBalan;
						iBalanNum += 1;
					}

				}

				int w = 0;
                List<ScoreItemBean> list = new ArrayList<ScoreItemBean>();
				if (minPfNum != 0) {
					list.add(new ScoreItemBean(7, "功率因数最小值", minPfSum / minPfNum));
					score = DataUtil.doubleAdd(minPfSum / minPfNum * weight.get(7), score).doubleValue();
					w += weight.get(7);
				}
				if (pfExceedNum != 0) {
                    list.add(new ScoreItemBean(8, "功率因数越限率", pfExceedSum / pfExceedNum));
					score = DataUtil.doubleAdd(pfExceedSum / pfExceedNum * weight.get(8), score).doubleValue();
					w += weight.get(8);
				}
				if (volNum != 0) {
                    list.add(new ScoreItemBean(9, "电压越限率", volSum / volNum));
					score = DataUtil.doubleAdd(volSum / volNum * weight.get(9), score).doubleValue();
					w += weight.get(9);
				}
				if (volDeviationNum != 0) {
                    list.add(new ScoreItemBean(10, "电压允许偏差", volDeviationSum / volDeviationNum));
					score = DataUtil.doubleAdd(volDeviationSum / volDeviationNum * weight.get(10), score).doubleValue();
					w += weight.get(10);
				}
				
				if (tdisvNum != 0) {
					list.add(new ScoreItemBean(11, "电压总谐波畸变率最大值", Math.round(DataUtil.doubleDivide(tdisvSum, tdisvNum))));
					score = new BigDecimal(weight.get(11)).multiply(new BigDecimal(tdisvSum)).divide(new BigDecimal(tdisvNum), 2, BigDecimal.ROUND_HALF_DOWN).add(new BigDecimal(score)).doubleValue();
					w += weight.get(11);
				}
				
				if (disvNum != 0) {
					list.add(new ScoreItemBean(12, "各奇次谐波电压含有率最大值", Math.round(DataUtil.doubleDivide(disvSum, disvNum))));
					score = new BigDecimal(weight.get(12)).multiply(new BigDecimal(disvSum)).divide(new BigDecimal(disvNum), 2, BigDecimal.ROUND_HALF_DOWN).add(new BigDecimal(score)).doubleValue();
					w += weight.get(12);
				}
				
				if (disiNum != 0) {
					list.add(new ScoreItemBean(13, "各奇次谐波电流最大值", Math.round(DataUtil.doubleDivide(disiSum, disiNum))));
					score = new BigDecimal(weight.get(13)).multiply(new BigDecimal(disiSum)).divide(new BigDecimal(disiNum), 2, BigDecimal.ROUND_HALF_DOWN).add(new BigDecimal(score)).doubleValue();
					w += weight.get(13);
				}
				
				if (vBalanNum != 0) {
                    list.add(new ScoreItemBean(14, "电压不平衡度", vBalanSum / vBalanNum));
					score = new BigDecimal((vBalanSum / vBalanNum) * weight.get(14)).add(new BigDecimal(score)).doubleValue();
					w += weight.get(14);
				}
				if (vBalanTimeNum != 0) {
                    list.add(new ScoreItemBean(15, "电压不平衡度日均时间", vBalanTimeSum / vBalanTimeNum));
					score = DataUtil.doubleAdd((vBalanTimeSum / vBalanTimeNum) * weight.get(15), score);
					w += weight.get(15);
				}
				if (iBalanNum != 0) {
                    list.add(new ScoreItemBean(16, "电流不平衡度", iBalanSum / iBalanNum));
					score = DataUtil.doubleAdd((iBalanSum / iBalanNum) * weight.get(16), score);
					w += weight.get(16);
				}
				result.put("qualityAssess", list);
				// 计算电能质量总分(按权重)
                double qualityScore = 0;
                if(w != 0){
                    qualityScore = DataUtil.doubleDivide(score, w);
                }
				result.put("qualityScore", qualityScore);

			} else {
				errMessage = "该用户不是企业用户！";
			}
		} else {
			errMessage = "该用户不存在！";
		}
		result.put("errMessage", errMessage);
		return result;
	}

	@Override
	public Map<String, Object> useAssessment(Long accountId, Map<Integer, Integer> weight) {
		Map<String, Object> result = new HashMap<String, Object>();
		String errMessage = "";
		UserBean userBean = this.userBeanMapper.getUserByAccountId(accountId);
		if (userBean != null && userBean.getLedgerId() != null) {
			Long ledgerId = userBean.getLedgerId();
			LedgerBean ledger = this.phoneMapper.getLedgerById(ledgerId);
			if (ledger != null && ledger.getAnalyType() == 102) {
				Date endTime = DateUtil.clearDate(new Date()); // 结束时间
				Date beginTime = DateUtil.getDateBetween(endTime, -30); // 起始时间

				int s;
				int score = 0;
				int w = 0;
                List<ScoreItemBean> list = new ArrayList<ScoreItemBean>();
				s = loginFrequency(accountId, beginTime, endTime);
				list.add(new ScoreItemBean(24, "登陆频率", s));// 登陆频率(分)
				score += s * weight.get(24);
				w += weight.get(24);

				s = businessCoverage(accountId, beginTime, endTime);
                list.add(new ScoreItemBean(25, "业务覆盖度", s));// 业务覆盖度
				score += s * weight.get(25);
				w += weight.get(25);

				s = clientUse(accountId, beginTime, endTime);
                list.add(new ScoreItemBean(26, "客户端使用", s));// 客户端使用(分)
				score += s * weight.get(26);
				w += weight.get(26);

				s = functionClick(accountId, beginTime, endTime);
                list.add(new ScoreItemBean(27, "功能点击次数", s));// 功能点击次数
				score += s * weight.get(27);
				w += weight.get(27);

				result.put("useAssess", list);
				// 计算平台使用总分(按权重)
                double useScore = 0;
                if(w != 0){
                    useScore = DataUtil.doubleDivide(score, w, 2);
                }
				result.put("useScore", useScore);
			} else {
				errMessage = "该用户不是企业用户！";
			}
		} else {
			errMessage = "该用户不存在！";
		}
		result.put("errMessage", errMessage);
		return result;
	}

	/**
	 * 登陆频率
	 * */
	private int loginFrequency(Long accountId, Date beginTime, Date endTime) {
		int result = 0;
		int loginNum = this.eleAssessmentMapper.getLoginNum(accountId, beginTime, endTime, 0);
		if (loginNum <= 1) {
			result = 20;
		} else if (loginNum > 1 && loginNum <= 4) {
			result = 40;
		} else if (loginNum > 4 && loginNum <= 20) {
			result = 60;
		} else if (loginNum > 20 && loginNum <= 40) {
			result = 80;
		} else {
			result = 100;
		}

		return result;
	}

	/**
	 * 业务覆盖度
	 * */
	private int businessCoverage(Long accountId, Date beginTime, Date endTime) {
		int result = 0;
		int coverOperNum = this.eleAssessmentMapper.getCoverOperNum(accountId, beginTime, endTime);
		int totalNum = this.eleAssessmentMapper.getTotalOperNum(accountId);
        if(totalNum > 0){
            int percent = (coverOperNum * 100) / totalNum;
            if (percent >= 90) {
                result = 100;
            } else if (percent >= 80 && percent < 90) {
                result = 80;
            } else if (percent >= 60 && percent < 80) {
                result = 60;
            } else if (percent >= 30 && percent < 60) {
                result = 40;
            } else {
                result = 20;
            }
        }
		return result;
	}

	/**
	 * 客户端使用
	 * */
	private int functionClick(Long accountId, Date beginTime, Date endTime) {
		int result = 0;
		int clickNum = this.eleAssessmentMapper.getUserClickNum(accountId, beginTime, endTime);
		if (clickNum >= 1000) {
			result = 100;
		} else if (clickNum >= 600 && clickNum < 1000) {
			result = 80;
		} else if (clickNum >= 300 && clickNum < 600) {
			result = 60;
		} else if (clickNum >= 100 && clickNum < 300) {
			result = 40;
		} else {
			result = 20;
		}

		return result;
	}

	/**
	 * 功能点击次数
	 * */
	private int clientUse(Long accountId, Date beginTime, Date endTime) {
		int result = 0;
		int pcUse = this.eleAssessmentMapper.getLoginNum(accountId, beginTime, endTime, 1);
		int clientUse = this.eleAssessmentMapper.getLoginNum(accountId, beginTime, endTime, 2);
		if (pcUse > 0 && clientUse > 0) {
			result = 100;
		} else if (pcUse > 0 && clientUse == 0) {
			result = 80;
		} else if (pcUse == 0 && clientUse > 0) {
			result = 60;
		} else if (pcUse == 0 && clientUse == 0) {
			result = 20;
		}

		return result;
	}

	/**
	 * 功率越限
	 * */
	private Long calculatePowerExceed(Long meterId, Date beginTime, Date endTime) {
		Long result = null;
		Double basePower = this.schedulingMapper.getPointEPwr(meterId);
		if (basePower != null && basePower > 0) {
			Double maxPower = this.eleAssessmentMapper.getMaxPowerByMeter(meterId, beginTime, endTime);
            if(maxPower != null){ BigDecimal maxPowerBigDecm = new BigDecimal(maxPower);BigDecimal basePowerBigDecm = new BigDecimal(basePower);
                if (maxPowerBigDecm.compareTo(new BigDecimal(1.2).multiply(basePowerBigDecm)) >= 0) {
                    result = 20L;
                } else if (maxPowerBigDecm.compareTo(new BigDecimal(1.1).multiply(basePowerBigDecm)) >= 0 && maxPowerBigDecm.compareTo(new BigDecimal(1.2).multiply(basePowerBigDecm)) < 0) {
                    result = 40L;
                } else if (maxPowerBigDecm.compareTo(new BigDecimal(0.9).multiply(basePowerBigDecm)) >= 0 && maxPowerBigDecm.compareTo(new BigDecimal(1.1).multiply(basePowerBigDecm)) < 0) {
                    result = 60L;
                } else if (maxPowerBigDecm.compareTo(new BigDecimal(0.8).multiply(basePowerBigDecm)) >= 0 && maxPowerBigDecm.compareTo(new BigDecimal(0.9).multiply(basePowerBigDecm)) < 0) {
                    result = 80L;
                } else if (maxPowerBigDecm.compareTo(new BigDecimal(0.8).multiply(basePowerBigDecm)) < 0) {
                    result = 100L;
                }
            }
		}
		return result;
	}

	/**
	 * 电流越限
	 * */
	private Long calculateCurExceed(Long meterId, Date beginTime, Date endTime) {
		Long result = null;
		Double baseI = this.schedulingMapper.getPointECur(meterId);
		if (baseI != null && baseI > 0) {
			Double maxI = this.eleAssessmentMapper.getMaxIbyMeter(meterId, beginTime, endTime);
            if(maxI != null){
                if (maxI >= DataUtil.doubleMultiply(1.2, baseI)) {
                    result = 20L;
                } else if (maxI >= DataUtil.doubleMultiply(1.1, baseI) && maxI < DataUtil.doubleMultiply(1.2, baseI)) {
                    result = 40L;
                } else if (maxI >= DataUtil.doubleMultiply(0.9, baseI) && maxI < DataUtil.doubleMultiply(1.1, baseI)) {
                    result = 60L;
                } else if (maxI >= DataUtil.doubleMultiply(0.8, baseI) && maxI < DataUtil.doubleMultiply(0.9, baseI)) {
                    result = 80L;
                } else if (maxI < DataUtil.doubleMultiply(0.8, baseI)) {
                    result = 100L;
                }
            }
		}
		return result;
	}

	/**
	 * 电压偏差
	 * */
	private Long calculateVolDeviation(Long meterId, Date beginTime, Date endTime) {
		Long result = null;
		Double baseV = this.schedulingMapper.getPointEVol(meterId);
		if (baseV != null && baseV > 0) {
			Double maxV = this.eleAssessmentMapper.getMaxVbyMeter(meterId, beginTime, endTime);
			Double minV = this.eleAssessmentMapper.getMinVbyMeter(meterId, beginTime, endTime);
            if(maxV != null && minV != null){
                if (baseV >= 35000) {
                    Double deviation = DataUtil.doubleAdd(Math.abs(DataUtil.doubleSubtract(maxV, baseV)), Math.abs(DataUtil.doubleSubtract(minV, baseV)));
                    Double rate = new BigDecimal(deviation).multiply(new BigDecimal(1000)).divide(new BigDecimal(baseV), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue(); // 电压最大、最小值偏差绝对值和的千分比
                    if (rate <= 80) {
                        result = 100L;
                    } else if (rate <= 85 && rate > 80) {
                        result = 80L;
                    } else if (rate <= 95 && rate > 85) {
                        result = 60L;
                    } else if (rate <= 100 && rate > 95) {
                        result = 40L;
                    } else if (rate > 100) {
                        result = 20L;
                    }
                } else {
                    Double z = DataUtil.doubleSubtract(maxV, baseV);
                    Double f = DataUtil.doubleSubtract(minV, baseV);
                    Double zRate = new BigDecimal(z).multiply(new BigDecimal(1000)).divide(new BigDecimal(baseV), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue(); // 电压最大值偏差千分比
                    Double fRate = new BigDecimal(f).multiply(new BigDecimal(1000)).divide(new BigDecimal(baseV), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue(); // 电压最小值偏差千分比
                    if (baseV <= 20000 && baseV > 220) {
                        long zResult = compMeterVolDevitation_1(zRate);
                        long fResult = compMeterVolDevitation_1(fRate);
                        result = Math.min(zResult, fResult);
                    } else if (baseV.equals(220d)) {
                        long zResult = compMeterVolDevitation_2(zRate);
                        long fResult = compMeterVolDevitation_2(fRate);
                        result = Math.min(zResult, fResult);
                    }
                }
            }
		}
		return result;
	}

	/**
	 * 最大电压不平衡度
	 * */
	private Long calculateVolBanlan(Long meterId, Date beginTime, Date endTime) {
		Long result = null;
		Double volBanlance = this.eleAssessmentMapper.getVolBalance(meterId, beginTime, endTime);
        if(volBanlance != null){
            if (volBanlance < 2) {
                result = 100L;
            } else if (volBanlance >= 2 && volBanlance < 2.5) {
                result = 80L;
            } else if (volBanlance >= 2.5 && volBanlance < 3) {
                result = 60L;
            } else if (volBanlance >= 3 && volBanlance < 4) {
                result = 40L;
            } else {
                result = 20L;
            }
        }

		return result;
	}

	/**
	 * 最大电流不平衡度
	 * */
	private Long calculateIBanlan(Long meterId, Date beginTime, Date endTime) {
		Long result = null;
		Double iBanlance = this.eleAssessmentMapper.getIBalance(meterId, beginTime, endTime);
        if(iBanlance != null){
            if (iBanlance < 10) {
                result = 100L;
            } else if (iBanlance >= 10 && iBanlance < 15) {
                result = 80L;
            } else if (iBanlance >= 15 && iBanlance < 20) {
                result = 60L;
            } else if (iBanlance >= 20 && iBanlance < 25) {
                result = 40L;
            } else {
                result = 20L;
            }
        }

		return result;
	}

	/**
	 * 最大负载率
	 * */
	private Long calculateMaxLoad(Long meterId, Date beginTime, Date endTime) {
		Long result = null;
		Double maxLoad = this.eleAssessmentMapper.getMaxLoad(meterId, beginTime, endTime);
        if(maxLoad != null){
            if (maxLoad < 60) {
                result = 100L;
            } else if (maxLoad >= 60 && maxLoad < 80) {
                result = 80L;
            } else if (maxLoad >= 80 && maxLoad < 90) {
                result = 60L;
            } else if (maxLoad >= 90 && maxLoad < 100) {
                result = 40L;
            } else {
                result = 20L;
            }
        }

		return result;
	}

	/**
	 * 最小功率因数
	 * */
	private Long calculateMinPf(Long ledgerId, Long meterId, Date beginTime, Date endTime) {
		Long result = null;
		Double minPf = this.eleAssessmentMapper.getMinPf(meterId, beginTime, endTime);
		Double factor = schedulingMapper.getThresholdValue(ledgerId);
		if (minPf != null && factor != null) {
			Double stdPF = DataUtil.doubleMultiply(factor, 100);
			if (minPf >= DoubleUtils.getDoubleValue(DataUtil.doubleMultiply(stdPF, 1.04), 2)) {
				result = 100L;
			} else if (minPf >= stdPF && minPf < DoubleUtils.getDoubleValue(DataUtil.doubleMultiply(stdPF, 1.04), 2)) {
				result = 80L;
			} else if (minPf >= DoubleUtils.getDoubleValue(DataUtil.doubleMultiply(stdPF, 0.9), 2) && minPf < stdPF) {
				result = 60L;
			} else if (minPf >= DoubleUtils.getDoubleValue(DataUtil.doubleMultiply(stdPF, 0.8), 2) && minPf < DoubleUtils.getDoubleValue(DataUtil.doubleMultiply(stdPF, 0.9), 2)) {
				result = 40L;
			} else if (minPf < DataUtil.doubleMultiply(stdPF, DoubleUtils.getDoubleValue(DataUtil.doubleMultiply(stdPF, 0.8), 2))) {
				result = 20L;
			}
		}
		return result;
	}

	/**
	 * 功率因数越限率
	 * */
	private Long calculatePfExceed(Long ledgerId, Long meterId, Date beginTime, Date endTime) {
		Long result = null;
		int totalNum = this.eleAssessmentMapper.getPfTotalNum(meterId, beginTime, endTime);
		Double factor = schedulingMapper.getThresholdValue(ledgerId);
		if (factor != null && totalNum != 0) {
			Double stdPF = DataUtil.doubleMultiply(factor, 100);
			int exceedNum = this.eleAssessmentMapper.getPfExceedNum(meterId, beginTime, endTime, stdPF);
			int percent = (exceedNum * 100) / totalNum;
			if (percent == 0) {
				result = 100L;
			} else if (percent > 0 && percent < 5) {
				result = 80L;
			} else if (percent >= 5 && percent < 10) {
				result = 60L;
			} else if (percent >= 10 && percent < 20) {
				result = 40L;
			} else if (percent >= 20) {
				result = 20L;
			}
		}
		return result;
	}

	/**
	 * 电压不平衡度日均值
	 * */
	private Long calculateVBalanAvg(Long meterId, Date beginTime, Date endTime) {
		Long result = null;
		Double vBalance = this.eleAssessmentMapper.getVBalanAvg(meterId, beginTime, endTime);
        if(vBalance != null){
            if (vBalance < 1.5) {
                result = 100L;
            } else if (vBalance >= 1.5 && vBalance < 2) {
                result = 80L;
            } else if (vBalance >= 2 && vBalance < 3) {
                result = 60L;
            } else if (vBalance >= 3 && vBalance < 4) {
                result = 40L;
            } else {
                result = 20L;
            }
        }

		return result;
	}

	/**
	 * 电压不平衡度日均时间
	 * */
	private Long calculateVBalanTimeAvg(Long meterId, Date beginTime, Date endTime) {
		Long result = null;
		Long vBalanTime = this.eleAssessmentMapper.getVBalanTimeAvg(meterId, beginTime, endTime);
        if(vBalanTime != null){
            if (vBalanTime < 27) {
                result = 100L;
            } else if (vBalanTime >= 27 && vBalanTime < 36) {
                result = 80L;
            } else if (vBalanTime >= 36 && vBalanTime < 72) {
                result = 60L;
            } else if (vBalanTime >= 72 && vBalanTime < 180) {
                result = 40L;
            } else {
                result = 20L;
            }
        }

		return result;
	}

	/**
	 * 电流不平衡度日均值
	 * */
	private Long calculateIBalanAvg(Long meterId, Date beginTime, Date endTime) {
		Long result = null;
		Double iBalance = this.eleAssessmentMapper.getIBalanAvg(meterId, beginTime, endTime);
        if(iBalance != null){
            if (iBalance < 10) {
                result = 100L;
            } else if (iBalance >= 10 && iBalance < 15) {
                result = 80L;
            } else if (iBalance >= 15 && iBalance < 20) {
                result = 60L;
            } else if (iBalance >= 20 && iBalance < 25) {
                result = 40L;
            } else {
                result = 20L;
            }
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
		} else if ((rate > 59.5 && rate <= 66.5) || (DataUtil.doubleAdd(rate, 59.5) < 0 && DataUtil.doubleAdd(rate, 66.5) >= 0)) { // 60
			result = 60;
		} else if ((rate > 66.5 && rate <= 70) || (DataUtil.doubleAdd(rate, 66.5) < 0 && DataUtil.doubleAdd(rate, 70) >= 0)) { // 40
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
	 * 
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Long calculateCurVol(Long meterId, Date beginTime, Date endTime) {
		int over = eleAssessmentMapper.getMeterOverVol(meterId, beginTime, endTime);
		int all = eleAssessmentMapper.getMeterVolCount(meterId, beginTime, endTime);
		if (all != 0) {
			double r = DataUtil.doubleDivide(over, all, 2);
			if (over == 0)
				return 100L;
			else if (r <= 0.05)
				return 80L;
			else if (r <= 0.1)
				return 60L;
			else if (r <= 0.2)
				return 40L;
			else
				return 20L;
		}

		return null;
	}

	@Override
	public Map<String, Object> economyAssessment(Long accountId, Map<Integer, Integer> weight) {
		Map<String, Object> result = new HashMap<String, Object>();
		String errMessage = "";
		UserBean userBean = userBeanMapper.getUserByAccountId(accountId);
		if (userBean != null && userBean.getLedgerId() != null) {
			Long ledgerId = userBean.getLedgerId();
			LedgerBean ledger = this.phoneMapper.getLedgerById(ledgerId);
			if (ledger != null && ledger.getAnalyType() == 102) {
				Date endTime = DateUtil.clearDate(new Date()); // 结束时间
				Date beginTime = DateUtil.getDateBetween(endTime, -30); // 起始时间

				List<Long> meterIds = eleAssessmentMapper.getTFMeterIdsByLedgerId(ledgerId);
				
				double score = 0;
				int w = 0;
				List<ScoreItemBean> list = new ArrayList<ScoreItemBean>();
				Long ss = calculateEle(ledgerId, beginTime, endTime);
				if (ss != null) {
					list.add(new ScoreItemBean(17, "电度电费合理性", Math.round(ss)));
					score = DataUtil.doubleAdd(ss * weight.get(17), score);
					w += weight.get(17);
				}

				Double sd = calculatePF(ledgerId, meterIds, beginTime, endTime);
				if (sd != null) {
					list.add(new ScoreItemBean(18, "专变月平均功率因数", Math.round(sd)));
					score = new BigDecimal(sd).add(new BigDecimal(weight.get(18))).add(new BigDecimal(score)).doubleValue();
					w += weight.get(18);
				}

				sd = calculateRP(meterIds, beginTime, endTime);
				if (sd != null) {
					list.add(new ScoreItemBean(19, "无功倒送功率", Math.round(sd)));
					score = new BigDecimal(sd).add(new BigDecimal(weight.get(19))).add(new BigDecimal(score)).doubleValue();
					w += weight.get(19);
				}

				sd = calculateRPTime(meterIds, beginTime, endTime);
				if (sd != null) {
					list.add(new ScoreItemBean(20, "无功倒送时间", Math.round(sd)));
					score = new BigDecimal(sd).add(new BigDecimal(weight.get(20))).add(new BigDecimal(score)).doubleValue();
					w += weight.get(20);
				}

				sd = calculateBasicFee(ledgerId, meterIds, beginTime, endTime);
				if (sd != null) {
					list.add(new ScoreItemBean(21, "基本电费", Math.round(sd)));
					score = new BigDecimal(sd).add(new BigDecimal(weight.get(21))).add(new BigDecimal(score)).doubleValue();
					w += weight.get(21);
				}

				sd = calculateLoad(meterIds, beginTime, endTime);
				if (sd != null) {
					list.add(new ScoreItemBean(22, "负载率", Math.round(sd)));
					score = new BigDecimal(sd).add(new BigDecimal(weight.get(22))).add(new BigDecimal(score)).doubleValue();
					w += weight.get(22);
				}

				ss = calculateLineloss(ledgerId, beginTime, endTime);
				if (ss != null) {
					list.add(new ScoreItemBean(23, "线损", Math.round(ss)));
					score = DataUtil.doubleAdd(score, ss * weight.get(23));
					w += weight.get(23);
				}

				result.put("economyAssess", list);
                double economyScore = 0;
                if(w != 0){
                    economyScore = new BigDecimal(score).divide(new BigDecimal(w), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
				result.put("economyScore", economyScore);
			} else {
				errMessage = "该用户不是企业用户！";
			}
		} else {
			errMessage = "该用户不存在！";
		}
		result.put("errMessage", errMessage);
		return result;
	}

	/**
	 * 电度电费合理性
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Long calculateEle(long ledgerId, Date beginTime, Date endTime) {
		List<Map<String, Object>> d = eleAssessmentMapper.getTotalFeeEle(ledgerId, beginTime, endTime);
		double eJ = 0, eG = 0, totalE = 0;
		int sectorId;
		for (Map<String, Object> m : d) {
			sectorId = Integer.valueOf(m.get("SECTORID").toString());
			if (sectorId == 1 || sectorId == 2)
				eJ = DataUtil.doubleAdd(eJ, Double.valueOf(m.get("VALUE").toString()));
			if (sectorId == 4)
				eG = Double.valueOf(m.get("VALUE").toString());
			totalE = DataUtil.doubleAdd(totalE, Double.valueOf(m.get("VALUE").toString()));
		}

		if (DataUtil.parseDouble2BigDecimal(totalE).compareTo(BigDecimal.ZERO) == 0)
			return null;

		double jp = DataUtil.doubleDivide(eJ, totalE, 2); // （尖+峰）电量占比
		double gt = DataUtil.doubleDivide(eG, totalE, 2); // 谷电量占比

		if (jp < 0.2 && gt >= 0.5)
			return 100L;
		if (((jp >= 0.2 && jp < 0.6) || jp < 0.2) && gt < 0.5)
			return 80L;
		if (jp >= 0.6 && jp < 0.7)
			return 60L;
		if (jp >= 0.7 && jp < 0.8)
			return 40L;
		if (jp >= 0.8)
			return 20L;

		return null;
	}

	/**
	 * 专变月平均功率因数
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Double calculatePF(long ledgerId, List<Long> meterIds, Date beginTime, Date endTime) {
		int score = 0;
		int num = 0;
		Double bPf = eleAssessmentMapper.getThresholdValue(ledgerId);

		if (bPf != null) {
			for (long meterId : meterIds) {
				Double q = eleAssessmentMapper.getMeterQ(meterId, beginTime, endTime);
				Double rq = eleAssessmentMapper.getMeterRQ(meterId, beginTime, endTime);
				double pf = DataUtil.getPF(q, rq);
				if (bPf != null && pf > 0) {
					if (pf >= DataUtil.doubleAdd(bPf, 0.08))
						score += 100;
					else if (pf >= DataUtil.doubleAdd(bPf, 0.04) && pf < DataUtil.doubleAdd(bPf, 0.08))
						score += 80;
					else if (pf >= bPf && pf < DataUtil.doubleAdd(bPf, 0.04))
						score += 60;
					else if (pf >= DataUtil.doubleSubtract(bPf, 0.2) && pf < bPf)
						score += 40;
					else if (pf < DataUtil.doubleSubtract(bPf, 0.2))
						score += 20;
					num++;
				}
			}
		}

		if (num != 0)
			return DataUtil.doubleDivide(score, num);
		return null;
	}

	/**
	 * 无功倒送功率
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Double calculateRP(List<Long> meterIds, Date beginTime, Date endTime) {
		int score = 0;
		int num = 0;
		for (long meterId : meterIds) {
			Double rq = eleAssessmentMapper.getMeterMinRP(meterId, beginTime, endTime);
			Double pwr = eleAssessmentMapper.getThresholdPwrValue(meterId);
			if (rq != null && pwr != null) { BigDecimal rqBigDecm = new BigDecimal(rq);BigDecimal pwrBigDecm = new BigDecimal(pwr);
				if (rq >= 0){
                    score += 100;
                }
                else if (rqBigDecm.abs().compareTo(pwrBigDecm.multiply(new BigDecimal(0.1))) <= 0){
                    score += 80;
                }
                else if (rqBigDecm.abs().compareTo(pwrBigDecm.multiply(new BigDecimal(0.2))) <= 0){
                    score += 60;
                }
                else if (rqBigDecm.abs().compareTo(pwrBigDecm.multiply(new BigDecimal(0.4))) <= 0){
                    score += 40;
                }
                else {
                    score += 20;
                }

				num++;
			}
		}

		if (num != 0)
			return (new BigDecimal(score).divide(new BigDecimal(num), 2, BigDecimal.ROUND_DOWN)).doubleValue();
		return null;
	}

	/**
	 * 无功倒送时间
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Double calculateRPTime(List<Long> meterIds, Date beginTime, Date endTime) {
		int score = 0;
		int num = 0;
		for (long meterId : meterIds) {
			int time = eleAssessmentMapper.getMeterMinRPTime(meterId, beginTime, endTime) * 15;
			if (time == 0)
				score += 100;
			else if (time <= 15)
				score += 80;
			else if (time <= 30)
				score += 60;
			else if (time <= 60)
				score += 40;
			else
				score += 20;
			num++;
		}

		if (num != 0)
			return new BigDecimal(score).divide(new BigDecimal(num), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return null;
	}

	/**
	 * 基本电费
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Double calculateBasicFee(long ledgerId, List<Long> meterIds, Date beginTime, Date endTime) {
		long volume = 0;// 变压器容量
		int declareType;// 申报类型;1,容量;2,需量
		double declareValue;// 申报值
		Double maxDemand;
		int count = 0;
		int sc = 0;
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("beginTime", beginTime);
		queryMap.put("endTime", endTime);

		for (long meterId : meterIds) {
			queryMap.put("pointId", meterId);
			List<Map<String, Object>> basicFeeInfos = costMapper.getMeterBasicFeeInfo(queryMap);// 取基本电费信息

			if (basicFeeInfos != null && basicFeeInfos.size() > 0) {
				Map<String, Object> basicFeeInfo = basicFeeInfos.get(0);
				if (basicFeeInfo != null && basicFeeInfo.size() > 0) {
					if (basicFeeInfo.get("VOLUME") != null) {
						volume = Long.valueOf(basicFeeInfo.get("VOLUME").toString());
					}
					if (basicFeeInfo.get("DECLARETYPE") != null) {
						declareType = Integer.valueOf(basicFeeInfo.get("DECLARETYPE").toString());

						maxDemand = costMapper.getMaxDemandValue(queryMap);// 取月最大需量
						if (maxDemand != null) {
							if (declareType == 1) {
								List<RateSectorBean> rates = costMapper.getPointRateInfo(meterId);// 取测量点费率信息
								if (rates != null && rates.size() > 0) {
									Map<String, Object> basicPrice = costMapper.getBasicFeePrice(rates.get(0).getRateId());
									if (basicPrice != null) {
										if (basicPrice.get("VOLRATE") != null && basicPrice.get("DERATE") != null) {
											double j = new BigDecimal(Double.valueOf(basicPrice.get("VOLRATE").toString()))
													.divide(new BigDecimal(Double.valueOf(basicPrice.get("DERATE").toString())), 2, BigDecimal.ROUND_HALF_DOWN)
													.multiply(new BigDecimal(volume)).doubleValue();
											if (maxDemand >= j)
												sc += 100;
											else if (maxDemand >= DataUtil.doubleMultiply(j, 0.8))
												sc += 80;
											else if (maxDemand >= DataUtil.doubleMultiply(j, 0.6))
												sc += 60;
											else if (maxDemand >= DataUtil.doubleMultiply(j, 0.4))
												sc += 40;
											else
												sc += 20;
											count++;
										}
									}
								}
							} else if (declareType == 2 && basicFeeInfo.get("DECLAREVALUE") != null) {
								declareValue = Double.valueOf(basicFeeInfo.get("DECLAREVALUE").toString());
								double p = new BigDecimal(maxDemand).subtract(new BigDecimal(declareValue)).divide(new BigDecimal(declareValue), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
								if (p <= 0.05 && DataUtil.doubleAdd(p, 0.05) >= 0)
									sc += 100;
								else if (p > 0.05 && p <= 0.1)
									sc += 80;
								else if (p > 0.1 && p <= 0.2)
									sc += 60;
								else if ((p > 0.2 && p <= 0.3) || (DataUtil.doubleAdd(p, 0.1) >= 0 && DataUtil.doubleAdd(p, 0.05) < 0))
									sc += 40;
								else
									sc += 20;
								count++;
							}
						}
					}
				}
			}
		}

		if (count == 0)
			return null;
		else
			return DataUtil.doubleDivide(sc, count);
	}

	/**
	 * 负载率
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Double calculateLoad(List<Long> meterIds, Date beginTime, Date endTime) {
		int score = 0;
		int num = 0;
		for (long meterId : meterIds) {
			Double load = eleAssessmentMapper.getMeterAvgLoad(meterId, beginTime, endTime);
			if (load != null) {
				if (load > 70 && load <= 80)
					score += 100;
				else if ((load > 60 && load <= 70) || (load > 80 && load <= 90))
					score += 80;
				else if ((load > 50 && load <= 60) || (load > 90 && load <= 95))
					score += 60;
				else if ((load > 30 && load <= 50) || (load > 95 && load <= 100))
					score += 40;
				else
					score += 20;
				num++;
			}
		}

		if (num != 0)
			return DataUtil.doubleDivide(score, num, 2);
		return null;
	}
	
	/**
	 * 电压总谐波畸变率最大值
	 * 
	 * @param volLevel
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Double calculateTotalDisV(int volLevel, Long meterId, Date beginTime, Date endTime) {
		int score = 0;
		int num = 0;
		double vol;
		
		Double staDisV = HarConstant.getHarVolTotalStand(volLevel);
		if (staDisV != null) {
			List<Map<String, Object>> disv = eleAssessmentMapper.getMeterMaxDisV(meterId, beginTime, endTime);
			if (disv != null) {
				for (Map<String, Object> v : disv) {
					if (v != null){
					if (v.get("VA")!= null) {
						vol = Double.valueOf(v.get("VA").toString());
						score += getDisvScore(staDisV, vol);
						num++;
					}
					if (v.get("VB")!= null) {
						vol = Double.valueOf(v.get("VB").toString());
						score += getDisvScore(staDisV, vol);
						num++;
					}
					if (v.get("VC") != null) {
						vol = Double.valueOf(v.get("VC").toString());
						score += getDisvScore(staDisV, vol);
						num++;
					}
				}
					}
			}
		}

		if (num != 0)
			return DataUtil.doubleDivide(score, num);
		return null;
	}
	
	/**
	 * 各奇次谐波电压含有率最大值
	 * 
	 * @param volLevel
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Double calculateDisV(int volLevel, Long meterId, Date beginTime, Date endTime) {
		int score = 0;
		int num = 0;
		double vol;

		Double staDisV = HarConstant.getHarVolStand(volLevel);
		if (staDisV != null) {
			List<Map<String, Object>> disv = eleAssessmentMapper.getMeterMaxHarV(meterId, beginTime, endTime);
			if (disv != null) {
				for (Map<String, Object> v : disv) {
					if (v != null){
					if (v.get("VA") != null) {
						vol = Double.valueOf(v.get("VA").toString());
						score += getDisvScore(staDisV, vol);
						num++;
					}
					if (v.get("VB")!= null) {
						vol = Double.valueOf(v.get("VB").toString());
						score += getDisvScore(staDisV, vol);
						num++;
					}
					if (v.get("VC")!= null) {
						vol = Double.valueOf(v.get("VC").toString());
						score += getDisvScore(staDisV, vol);
						num++;
					}
				}
				}
			}
		}

		if (num != 0)
			return DataUtil.doubleDivide(score, num, 2);
		return null;
	}
	
	/**
	 * 各奇次谐波电流含有率最大值
	 * 
	 * @param volLevel
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Double calculateDisI(int volLevel, Long meterId, Date beginTime, Date endTime) {
		int score = 0;
		int num = 0;
		double vol;
		int harNo;
		Double staDisI;

		List<Map<String, Object>> disi = eleAssessmentMapper.getMeterMaxHarI(meterId, beginTime, endTime);
		if (disi != null) {
			for (Map<String, Object> v : disi) {
//				if (v == null)
//					continue;
				harNo = Integer.valueOf(v.get("HNUM").toString());
				staDisI = HarConstant.getHarIStand(volLevel, harNo);
				if (staDisI != null) {
					if (v.get("IA") != null) {
						vol = Double.valueOf(v.get("IA").toString());
						score += getDisvScore(staDisI, vol);
						num++;
					}
					if (v.get("IB") != null) {
						vol = Double.valueOf(v.get("IB").toString());
						score += getDisvScore(staDisI, vol);
						num++;
					}
					if (v.get("IC") != null) {
						vol = Double.valueOf(v.get("IC").toString());
						score += getDisvScore(staDisI, vol);
						num++;
					}
				}
			}
		}

		if (num != 0)
			return DataUtil.doubleDivide(score, num, 2);
		return null;
	}
	
	private int getDisvScore(double staDisV, double v) {
		if (new BigDecimal(v).compareTo(new BigDecimal(staDisV).multiply(new BigDecimal(0.4))) < 0)
			return 100;
		else if (new BigDecimal(v).compareTo(new BigDecimal(staDisV).multiply(new BigDecimal(0.8))) < 0)
			return 80;
		else if (new BigDecimal(v).compareTo(new BigDecimal(staDisV)) < 0)
			return 60;
		else if (new BigDecimal(v).compareTo(new BigDecimal(staDisV).multiply(new BigDecimal(1.5))) < 0)
			return 40;
		else
			return 20;
	}

	/**
	 * 线损
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Long calculateLineloss(long ledgerId, Date beginTime, Date endTime) {
		double total = 0;
		double totalUse = 0;
		//查询语句中做了减一天处理，这里不再处理
//		Date endDate = DateUtil.addDateDay(endTime, -1);
//	    Date beginDate = DateUtil.addDateDay(beginTime, -1);
	        
		List<LineLossBean> lineData = new ArrayList<LineLossBean>();
		List<Long> meterLevel1 = schedulingMapper.getLineLossByLevel(ledgerId, 1);
		if (meterLevel1 != null && meterLevel1.size() > 0) {
			String meterIds = processIds(meterLevel1);/*后面为修改*/List<Long> mids = new ArrayList<Long>(); if(meterIds.split(",").length > 0) { String[] ms =meterIds.split(","); for (int i = 0; i <ms.length; i++) {mids.add(Long.parseLong(ms[i]));}};
			lineData = schedulingMapper.getDayLineLossInfo(mids, beginTime, endTime);
			for (LineLossBean lb : lineData) {
				total = DataUtil.doubleAdd(total, lb.getCoul());
				List<Long> meters = schedulingMapper.getLineLossMeters(lb.getMeterId(), 2);
				if (meters == null || meters.size() == 0)
					continue;
				meterIds = processIds(meters);/*后面为修改*/List<Long> mids2 = new ArrayList<Long>(); if(meterIds.split(",").length > 0) { String[] ms =meterIds.split(","); for (int i = 0; i <ms.length; i++) {mids2.add(Long.parseLong(ms[i]));}};
				List<LineLossBean> childLineData = schedulingMapper.getDayLineLossInfo(mids2, beginTime, endTime);
				for (LineLossBean clb : childLineData) {
					totalUse = DataUtil.doubleAdd(totalUse, clb.getCoul());
				}
			}
		}

		if (total != 0 && totalUse != 0) {
			double r = DataUtil.doubleDivide(DataUtil.doubleSubtract(total, totalUse), total, 2);
			if (r <= 0.07)
				return 100L;
			else if (r <= 0.1)
				return 80L;
			else if (r <= 0.15)
				return 60L;
			else if (r <= 0.2)
				return 40L;
			else
				return 20L;
		}

		return null;
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
	public void assessment(Long accountId) {
		//new Thread(new ProcessData(accountId)).start();
		ThreadPoolTaskExecutor executor = SpringContextHolder.getBean(ThreadPoolTaskExecutor.class);
		executor.execute(new ProcessData(accountId));
	}

	@Override
	public ConcurrentHashMap<Long, Object> getAssessmentCache() {
		return assessmentCache;
	}

	class ProcessData implements Runnable {
		private long accountId;

		public ProcessData(long accountId) {
			this.accountId = accountId;
		}

		@Override
		public void run() {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("complete", 0);
			assessmentCache.put(accountId, result);

			try {
				List<Map<String, Object>> items = eleAssessmentMapper.getScoreItemWeight();
				Map<Integer, Integer> weight = processScoreItemWeight(items);

				Map<String, Object> h = new HashMap<String, Object>();
				h.put("scoreId", SequenceUtils.getDBSequence());
				h.put("accountId", accountId);

				double score = 0;
				Map<String, Object> r = safetyAssessment(accountId, weight);
				h.put("score1", ((Double) r.get("safetyScore")).intValue());
				score = new BigDecimal(((Double) r.get("safetyScore"))).multiply(new BigDecimal(0.3)).add(new BigDecimal(score)).doubleValue();
				result.putAll(r);

				r = qualityAssessment(accountId, weight);
				h.put("score2", ((Double) r.get("qualityScore")).intValue());
				score = new BigDecimal(((Double) r.get("qualityScore"))).multiply(new BigDecimal(0.25)).add(new BigDecimal(score)).doubleValue();
				result.putAll(r);

				r = economyAssessment(accountId, weight);
				h.put("score3", ((Double) r.get("economyScore")).intValue());
				score = new BigDecimal(((Double) r.get("economyScore"))).multiply(new BigDecimal(0.15)).add(new BigDecimal(score)).doubleValue();
				result.putAll(r);

				r = useAssessment(accountId, weight);
				h.put("score4", ((Double) r.get("useScore")).intValue());
				score = new BigDecimal(((Double) r.get("useScore"))).multiply(new BigDecimal(0.3)).add(new BigDecimal(score)).doubleValue();
				result.putAll(r);

				result.put("score", (int) score);
				h.put("score", (int) score);

				eleAssessmentMapper.saveAccountScore(h);
			} catch (Exception e) {
				Log.error("assessment--无法保存账户分数");
			}

			result.put("complete", 1);
		}
	}

	private Map<Integer, Integer> processScoreItemWeight(List<Map<String, Object>> items) {
		Map<Integer, Integer> r = new HashMap<Integer, Integer>();

		for (Map<String, Object> m : items) {
			r.put(Integer.valueOf(m.get("ID").toString()), Integer.valueOf(m.get("WEIGHT").toString()));
		}

		return r;
	}
}
