package com.linyang.energy.mapping.projectManager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.InvestmentBean;

public interface InvestmentMapper {
	 int deleteByPrimaryKey(List<String> projectIdList);//原int deleteByPrimaryKey(@Param("projectId")String projectId);

    int insert(InvestmentBean record);

    int insertSelective(InvestmentBean record);

    InvestmentBean selectByPrimaryKey(Long projectId);

    int updateByPrimaryKeySelective(InvestmentBean record);

    int updateByPrimaryKey(InvestmentBean record);
}