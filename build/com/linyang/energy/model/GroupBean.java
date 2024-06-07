package com.linyang.energy.model;

import java.io.Serializable;
import java.util.List;


/** 

* @ClassName: GroupBean 

* @Description: 群组bean

* @author Yaojiawei

* @date 2014-8-12 上午09:45:45 

* 
 

*/ 
public class GroupBean implements Serializable {
	
	private static final long serialVersionUID = -1388595046570269524L;
	
	public static final Integer TYPE_PRIVATE=0;//私有
	public static final Integer TYPE_PUBLIC=1;//公开
	
	public static final Integer GROUP_LEDGER=1;//客户组
	public static final Integer GROUP_MPED=2;//计量点组
	
	private Long accountId;//用户号
	private Long groupId;//群组ID
	private String groupName;//群组名
	private Integer publicType;//公开性，0 私有，1 公开
	private Integer groupType;//组类型，1 客户组，2计量点组
	private String groupRemark;//组描述
	private boolean ifCurrentUserGroup;//是否是当前用户创建的组, add by yaojiawei at 2014-09-03
	private String accountName;//用户名，冗余字段， add by yaojiawei at 2014-09-29
	
	private List<LedgerBean> ledgerList;//所包含的客户
	private List<MeterBean> meterList;//所包含的计量点
	
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Long getAccountId() {
		return accountId;
	}
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Integer getPublicType() {
		return publicType;
	}
	public void setPublicType(Integer publicType) {
		this.publicType = publicType;
	}
	public Integer getGroupType() {
		return groupType;
	}
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}
	public String getGroupRemark() {
		return groupRemark;
	}
	public void setGroupRemark(String groupRemark) {
		this.groupRemark = groupRemark;
	}

	public GroupBean(Long accountId, Long groupId, String groupName,
			Integer publicType, Integer groupType, String groupRemark) {
		super();
		this.accountId = accountId;
		this.groupId = groupId;
		this.groupName = groupName;
		this.publicType = publicType;
		this.groupType = groupType;
		this.groupRemark = groupRemark;
	}
	
	public GroupBean() {
		super();
	}
	@Override
	public String toString() {
		return "GroupBean [groupId=" + groupId + ", groupName=" + groupName
				+ "]";
	}
	public void setLedgerList(List<LedgerBean> ledgerList) {
		this.ledgerList = ledgerList;
	}
	public List<LedgerBean> getLedgerList() {
		return ledgerList;
	}
	public void setMeterList(List<MeterBean> meterList) {
		this.meterList = meterList;
	}
	public List<MeterBean> getMeterList() {
		return meterList;
	}
	public void setIfCurrentUserGroup(boolean ifCurrentUserGroup) {
		this.ifCurrentUserGroup = ifCurrentUserGroup;
	}
	public boolean isIfCurrentUserGroup() {
		return ifCurrentUserGroup;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountName() {
		return accountName;
	}

}
