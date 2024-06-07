package com.linyang.energy.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.common.LanguageEnum;
import com.linyang.energy.mapping.staticdata.StaticDataMapper;
import com.linyang.energy.service.StaticDataService;

@Service
public class StaticDataServiceImpl implements StaticDataService {
	
	@Autowired
	StaticDataMapper staticDataMapper;
	
	@Override
	public List<Map<String, Object>> initFirstLevelData(LanguageEnum language) {
		return staticDataMapper.initFirstLevelData(language.getLanguageType());
	}

	@Override
	public List<Map<String, Object>> initSecondLevelData(LanguageEnum language) {
		return staticDataMapper.initSecondLevelData(language.getLanguageType());
	}
	
	
	@Override
	public List<Map<String, Object>> handleSecondLevelData(String sql) {
		return staticDataMapper.handleSecondLevelData(sql);
	}
	
	@Override
	public List<Map<String, Object>> flushStaticData(String dataCode,String dataType) {
		return staticDataMapper.flushStaticData(dataCode, dataType);
	}

}
