package com.linyang.energy.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.linyang.energy.model.EasyAdviceScoreBean;

/**
 * Created by Administrator on 16-12-21.
 */
public interface WechatAssessment {
	
	/**
	 * 获取评分
	 * 
	 * @param accountId
	 * @return
	 */
	public void assessment(Long accountId);

    /**
     * 评分最低三项
     * 
     */
	public List<EasyAdviceScoreBean> wechatAssessment(Long accountId, Map<Integer, Integer> weight);

	/**
	 * 获取评分最低三项缓存
	 * 
	 */
	public ConcurrentHashMap<Long, Object> getAssessmentCache();
}
