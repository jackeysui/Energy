package com.linyang.energy.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 15-10-13.
 */
public interface EleAssessment {
	
	/**
	 * 总评分
	 * 
	 * @param accountId
	 * @return
	 */
	public void assessment(Long accountId);

    /**
     *   "安全性"评估
     * */
	public Map<String, Object> safetyAssessment(Long accountId, Map<Integer, Integer> weight);

    /**
     *   "电能质量"评估
     * */
    public Map<String, Object> qualityAssessment(Long accountId, Map<Integer, Integer> weight);

    /**
     *   "平台使用"评估
     * */
    public Map<String, Object> useAssessment(Long accountId, Map<Integer, Integer> weight);

	/**
	 * 经济性评估
	 *
	 * @return
	 */
	public Map<String, Object> economyAssessment(Long accountId, Map<Integer, Integer> weight);
	
	/**
	 * 获取评分缓存
	 * 
	 * @return
	 */
	public ConcurrentHashMap<Long, Object> getAssessmentCache();
}
