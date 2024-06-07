package com.linyang.energy.mapping.userAnalysis;

import com.linyang.energy.model.AccountLoginHisBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface AccountLoginHisMapper {
	
	/**
	 * 保存用户登录信息
	 * @param bean
	 */
	public void addAccountLoginHis(AccountLoginHisBean bean);

    public int checkAccountLoginHis(@Param("accountId")Long accountId, @Param("date")Date date);

}
