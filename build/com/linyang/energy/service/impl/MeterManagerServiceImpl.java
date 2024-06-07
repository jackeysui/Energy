package com.linyang.energy.service.impl;

import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.model.LineLossMeterBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.service.MeterManagerService;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeterManagerServiceImpl implements MeterManagerService {
	@Autowired
	private MeterManagerMapper meterManagerMapper;

	@Override
	public List<Map<String,Object>> getMeterPageList(Page page, Map<String, Object> queryMa) {
		queryMa.put(Dialect.pageNameField, page);
		List<Map<String,Object>> list = meterManagerMapper.getMeterPageList(queryMa);
		return list == null ? new ArrayList<Map<String,Object>>() : list;
	}

    @Override
    public List<Map<String,Object>> getVirtualMeterPageList(Page page, Map<String, Object> queryMa) {
        queryMa.put(Dialect.pageNameField, page);
        List<Map<String,Object>> list = meterManagerMapper.getVirtualMeterPageList(queryMa);
        return list == null ? new ArrayList<Map<String,Object>>() : list;
    }

    @Override
    public String getVirtualContains(Long meterId, int idFlag) {
        return this.meterManagerMapper.getVirtualContains(meterId, idFlag);
    }
    @Override
	public List<String> getVirtualContainss(Long meterId, int idFlag) {
		return this.meterManagerMapper.getVirtualContainss(meterId, idFlag);
	}

    @Override
    public Map<String, Object> getVirtualMeterData(Long meterId){
        Map<String, Object> result = new HashMap<String, Object>();

        MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(meterId);
        result.put("meter",meterBean);
        List<Map<String, Object>> list = this.meterManagerMapper.getVirtualRelations(meterId);
        result.put("list", list);

        return result;
    }

	@Override
	public boolean deleteMeterData(List<Long> meterIds){
		boolean flag = false;
		if(CommonMethod.isCollectionNotEmpty(meterIds)){
			meterManagerMapper.deleteMeterData(meterIds);		//原	meterManagerMapper.deleteMeterData(CommonMethod.getSeparaterStringNumber(meterIds));
			Long meterId = meterIds.get(0);
			MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
			long ledgerId = meter.getLedgerId();
			meterManagerMapper.delMeterNum(ledgerId);
			if(meter.getMeterType()==1) {//表类型为
				meterManagerMapper.deleteDetailData(meterId);
			}
			//删除T_LEDGER_METER表的关联数据；
			meterManagerMapper.deleteLedgerMeter(meterId);
			//删除T_LEDGER_RELATION表的关联数据；
			meterManagerMapper.deleteLedgerRelation(meterId);
			//删除T_LEDGER_SHOW表的关联数据；
			meterManagerMapper.deleteLedgerShow(meterId);
			flag = true;
		}
		return flag;
	}

    @Override
    public boolean deleteVirtualMeterData(List<Long> meterIds){
        boolean flag = false;
        if(CommonMethod.isCollectionNotEmpty(meterIds)){
        	this.meterManagerMapper.deleteVirtualMeter(meterIds);		// 原           this.meterManagerMapper.deleteVirtualMeter(CommonMethod.getSeparaterStringNumber(meterIds));
        	 this.meterManagerMapper.deleteVirtualRelation(meterIds);	// 原	    	 this.meterManagerMapper.deleteVirtualRelation(CommonMethod.getSeparaterStringNumber(meterIds));
            flag = true;
        }
        return flag;
    }


	@Override
	public List<Map<String, Object>> getTerminalData() {
		List<Map<String,Object>> list = meterManagerMapper.getTerminalData();
		return list == null ? new ArrayList<Map<String,Object>>() : list;
	}

    @Override
    public List<Map<String, Object>> getHdWaterMeterData() {
        List<Map<String,Object>> list = meterManagerMapper.getHdWaterMeterData();
        return list == null ? new ArrayList<Map<String,Object>>() : list;
    }

	@Override
	public long checkMeterName(Map<String, Object> map) {
		return meterManagerMapper.checkMeterName(map);
	}
	
	@Override
	public List<Map<String,Object>> getLedgerData(long ledgerId) {
		if(CommonMethod.isNotEmpty(ledgerId)) {
			List<Map<String,Object>> list = meterManagerMapper.getLedgerData(ledgerId);
			return list == null ? new ArrayList<Map<String,Object>>() : list;
		} else {
			return new ArrayList<Map<String,Object>> ();
		}
	}

	@Override
	public List<Map<String, Object>> getMpedDataByTerId(Map<String, Object> map) {
		if(CommonMethod.isNotEmpty(map)) {
			List<Map<String,Object>> list = meterManagerMapper.getMpedDataByTerId(map);
			return list == null ? new ArrayList<Map<String,Object>>() : list;
		} else {
			return new ArrayList<Map<String,Object>> ();
		}
	}

	@Override
	public List<Map<String, Object>> getDeviceData() {
		List<Map<String,Object>> list = meterManagerMapper.getDeviceData();
		return list == null ? new ArrayList<Map<String,Object>>() : list;
	}

	@Override
	public boolean insertMeterInfo(MeterBean meter) {
		boolean isSuccess = false;
		if(meter!=null) {
			Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("meterName", meter.getMeterName());
            queryMap.put("meterId",meter.getMeterId());
           
            List<MeterBean> meterList = meterManagerMapper.getFilteredMeter(queryMap);
            if(meterList.size()>0)
            {
            	isSuccess = false;
            }
            else
            {
			
			meterManagerMapper.insertMeterInfo(meter);
			long ledgerId = meter.getLedgerId();
			meterManagerMapper.addMeterNum(ledgerId);
			if(meter.getMeterType()==1) {//只有电有额定电压、额定电流等
				Map<String,Object> map = new HashMap<String, Object> ();
				map.put("meterId", meter.getMeterId());
				for(int i=1;i<5;i++) {
					map.put("thresholdId", i);
					if(i==1) {//额定电压
						map.put("thresholdValue", meter.getRatedVol());
					} else if(i==2) {//电压下限
						map.put("thresholdValue", meter.getLowerVol());
					} else if(i==3) {//额定电流
						map.put("thresholdValue", meter.getRatedCur());
					} else if(i==4) {//额定功率
						map.put("thresholdValue", meter.getRatedPower());
					}
					if(map.get("thresholdValue")!= null && map.get("thresholdValue").toString().length()>0) {//不为空才插入关联表
						meterManagerMapper.insertThresholdInfo(map);
					}
				}
			}
			// add or update method by catkins.
			// date 2019/10/8
			// Modify the content: 
			else if (meter.getMeterType()==2){
				Map<String,Object> map = new HashMap<String, Object> ();
				map.put("meterId", meter.getMeterId());
				map.put("thresholdId", "5");
				map.put("thresholdValue", meter.getWaterLimitThres());
				if(map.get("thresholdValue")!= null && map.get("thresholdValue").toString().length()>0) {//不为空才插入关联表
					meterManagerMapper.insertThresholdInfo(map);
				}
			}
			//end
			saveLineLossMeter(meter.getLineLoss(),meter.getMeterId(),1); 
			isSuccess = true;
            }
		} 
		return isSuccess;
	}

    @Override
    public Map<String, Object> insertUpdateVirtualMeter(Long meterId, String meterName, Long ledgerId, Integer meterType, String relations){
        Map<String, Object> result = new HashMap<String, Object>();

        //判断“虚拟采集点”包含的“真实采集点”是否合法
        String[] relationIds = relations.split(",");
        Long parementMeterId = 0L;
        Integer meterLevel = 0;
        if(relationIds.length <= 1){
            result.put("message", "请至少选择两个真实电类采集点");
            return result;
        }
        for(int i = 0; i < relationIds.length; i++){
            Long relationId = Long.valueOf(relationIds[i]);
            MeterBean meter = this.meterManagerMapper.getMeterDataByPrimaryKey(relationId);
            LineLossMeterBean lineLoss = this.meterManagerMapper.getLineLossMeter(relationId);
            if(!ledgerId.equals(meter.getLedgerId()) || meter.getIsVirtual() == 1){
                result.put("message", "请选择该企业下的真实电类采集点");
                return result;
            }
            if(i == 0){
                parementMeterId = lineLoss.getParementMeterId();
                meterLevel = lineLoss.getMeterLevel();
            }
            else if(!parementMeterId.equals(lineLoss.getParementMeterId())){
                result.put("message", "请选择该企业下同一级的电类采集点");
                return result;
            }
            Integer num = this.meterManagerMapper.getVirtualExistMeterNum(meterId, relationId);
            if(num > 0){
                result.put("message", "所选采集点已包含在其它虚拟采集点中");
                return result;
            }
        }

        //入库
        if(meterId == -1){
            meterId = SequenceUtils.getDBSequence();
            
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("meterName", meterName);
           
            List<MeterBean> meterList = meterManagerMapper.getFilteredMeter(queryMap);
            if(meterList.size()==0)
            {
            	 //插入t_meter
                this.meterManagerMapper.insertVirtualMeter(meterId, meterName, meterType, ledgerId);
                //插入T_VIRTUAL_METER_RELATION
                for(int i = 0; i < relationIds.length; i++){
                    Long relationId = Long.valueOf(relationIds[i]);
                    this.meterManagerMapper.insertVirtualMeterRelation(meterId, relationId);
                }

                meterManagerMapper.addMeterNum(ledgerId);//采集点数+1
            }
                  
        }
        else if(meterId > 0){
            MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(meterId);
            Long oldLedgerId = meterBean.getLedgerId();
            if(!oldLedgerId.equals(ledgerId)) {
                meterManagerMapper.delMeterNum(oldLedgerId);//采集点数-1
                meterManagerMapper.addMeterNum(ledgerId);//采集点数+1
            }

            //t_meter
            this.meterManagerMapper.updateVirtualMeter(meterId, meterName, meterType, ledgerId);
            //T_VIRTUAL_METER_RELATION
            this.meterManagerMapper.deleteVirtualMeterRelation(meterId);
            for(int i = 0; i < relationIds.length; i++){
                Long relationId = Long.valueOf(relationIds[i]);
                this.meterManagerMapper.insertVirtualMeterRelation(meterId, relationId);
            }
        }
        //插入t_lineloss_meter_info
        this.meterManagerMapper.deleteLineLossMeter(meterId);
        LineLossMeterBean lineLossMeterBean = new LineLossMeterBean();
        lineLossMeterBean.setMeterId(meterId);
        lineLossMeterBean.setParementMeterId(parementMeterId);
        lineLossMeterBean.setMeterLevel(meterLevel);
        this.meterManagerMapper.addLineLossMeter(lineLossMeterBean);

        result.put("message", "");
        return result;
    }

	@Override
	public boolean updateMeterInfo(MeterBean meter) {
		boolean isSuccess = false;
		if(meter!=null) {
			meterManagerMapper.updateMeterInfo(meter);
			meterManagerMapper.updateRelationMeterName(meter);

			long oldLedgerId = Long.parseLong(meter.getOldLedgerId().length()>0?meter.getOldLedgerId():"0");//原所属分户ID
			long ledgerId = meter.getLedgerId();//修改后的ID
			if(oldLedgerId!=ledgerId) {
				meterManagerMapper.delMeterNum(oldLedgerId);//原分户的采集点数-1
				meterManagerMapper.addMeterNum(ledgerId);//修改后的分户的采集点数+1
			}
			if(meter.getMeterType()==1) {
				Map<String,Object> map = new HashMap<String, Object> ();
				long meterId = meter.getMeterId();
				List<Map<String, Object>> dataList = meterManagerMapper.getMeterInfoById(meterId);//获取配置信息
				Map<String,Map<String, Object>> dataMap = new HashMap<String,Map<String, Object>> ();
				for(Map<String, Object> data : dataList) {
					dataMap.put(data.get("THRESHOLD_ID").toString(), data);
				}
				map.put("meterId", meterId);
				for(int i=1;i<5;i++) {
					map.put("thresholdId", i);
					if(i==1) {
						map.put("thresholdValue", meter.getRatedVol());
					} else if(i==2) {
						map.put("thresholdValue", meter.getLowerVol());
					} else if(i==3) {
						map.put("thresholdValue", meter.getRatedCur());
					} else if(i==4) {
						map.put("thresholdValue", meter.getRatedPower());
					}
					if(dataMap.containsKey(i+"")) {
						if(CommonMethod.isNotEmpty(map.get("thresholdValue"))) {
							meterManagerMapper.updateByPrimaryKeySelective(map);
						} else {
							Map<String,Object> param = new HashMap<String,Object> ();
							param.put("meterId", meterId);
							param.put("thresholdId",i);
							meterManagerMapper.deleteMeterDetail(param);
						}
					} else if(CommonMethod.isNotEmpty(map.get("thresholdValue"))){
						meterManagerMapper.insertThresholdInfo(map);
					}
				}
			}
			// add or update method by catkins.
			// date 2019/10/8
			// Modify the content: 
			else if (meter.getMeterType()==2) {
				Map<String,Object> map = new HashMap<> ();
				long meterId = meter.getMeterId();
				List<Map<String, Object>> dataList = meterManagerMapper.getMeterInfoById(meterId);//获取配置信息
				Map<String,Map<String, Object>> dataMap = new HashMap<> ();
				for(Map<String, Object> data : dataList) {
					dataMap.put(data.get("THRESHOLD_ID").toString(), data);
				}
				map.put("meterId", meterId);
				map.put("thresholdId", 5);
				map.put("thresholdValue", meter.getWaterLimitThres());
				if(dataMap.containsKey("5")) {
					if(CommonMethod.isNotEmpty(map.get("thresholdValue"))) {
						meterManagerMapper.updateByPrimaryKeySelective(map);
					} else {
						Map<String,Object> param = new HashMap<> ();
						param.put("meterId", meterId);
						param.put("thresholdId",5);
						meterManagerMapper.deleteMeterDetail(param);
					}
				} else if(CommonMethod.isNotEmpty(map.get("thresholdValue"))){
					meterManagerMapper.insertThresholdInfo(map);
				}
			}
			//end
			saveLineLossMeter(meter.getLineLoss(),meter.getMeterId(),2); 
			
			isSuccess = true;
		}
		return isSuccess;
	}
	
	/**
	 * 保存LineLossMeter
	 * @param lineLoss 父级测量点ID
	 * @param meterId 测量点ID
	 * @param type
	 */
	private void saveLineLossMeter(Long lineLoss, Long meterId,int type) {
		if(lineLoss != null){
			if(type ==2){
				this.meterManagerMapper.deleteLineLossMeter(meterId);
			}
			if(lineLoss != -1){
				int meterLevel = 0;
				if(lineLoss ==0){
					meterLevel = 1;
				}else {
					meterLevel = this.meterManagerMapper.getMeterLevel(lineLoss) + 1;
				}
				LineLossMeterBean lineLossMeter =new LineLossMeterBean();
				lineLossMeter.setMeterId(meterId);
				lineLossMeter.setParementMeterId(lineLoss);
				lineLossMeter.setMeterLevel(meterLevel);
				this.meterManagerMapper.addLineLossMeter(lineLossMeter);
			}
		}
	}
	

	@Override
	public MeterBean getMeterDataById(long meterId) {
		MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
		if(meter != null && meter.getMeterType()==1) {
			List<Map<String, Object>> dataList = meterManagerMapper.getMeterInfoById(meterId);
			for(Map<String, Object> map : dataList) {
				if("1".equals(map.get("THRESHOLD_ID").toString())){
					meter.setRatedVol(map.get("THRESHOLD_VALUE").toString());
				} else if("2".equals(map.get("THRESHOLD_ID").toString())) {
					meter.setLowerVol(map.get("THRESHOLD_VALUE").toString());
				} else if("3".equals(map.get("THRESHOLD_ID").toString())) {
					meter.setRatedCur(map.get("THRESHOLD_VALUE").toString());
				} else if("4".equals(map.get("THRESHOLD_ID").toString())) {
					meter.setRatedPower(map.get("THRESHOLD_VALUE").toString());
				}
			}
		} else if (meter != null && meter.getMeterType()==2){
			List<Map<String, Object>> dataList = meterManagerMapper.getMeterInfoById(meterId);
			for(Map<String, Object> map : dataList) {
				if ("5".equals( map.get( "THRESHOLD_ID" ).toString() )) {
					meter.setWaterLimitThres( Float.parseFloat( map.get( "THRESHOLD_VALUE" ).toString() ) );
				}
			}
		}
		LineLossMeterBean lineLossMeter = this.meterManagerMapper.getLineLossMeter(meterId);
		if(lineLossMeter != null && meter != null){
			Long lineLossId = lineLossMeter.getParementMeterId();
			String lineLossName = lineLossMeter.getParementMeterName();
			if(lineLossId == 0){
				lineLossName = "进线总表";
			}
			meter.setLineLoss(lineLossId);
			meter.setLineLossName(lineLossName);
		}
		return meter;
	}

	@Override
	public List<MeterBean> queryMeterList(Map<String, Object> param) {
		if(param.get("page")!=null)
			return this.meterManagerMapper.queryMeterPageList(param);
		else 
			return this.meterManagerMapper.queryMeterList(param);
	}

	@Override
	public MeterBean getMpedInfoById(long mpedId) {
		return meterManagerMapper.getMpedInfoById(mpedId);
	}

	@Override
	public Integer getCommModeByMeterId(Long meterId) {
		return this.meterManagerMapper.getCommModeByMeterId(meterId);
	}

	@Override
	public String getThresholdById(Long meterId) {
		return meterManagerMapper.getThresholdById(meterId);
	}
	
	//add by qwt 2015-6-4
	//add for 添加获取所有电表信息函数
	@Override
	public List<MeterBean> getAllMeter()
	{
		return meterManagerMapper.getAllMeter();
	}
	//end

	@Override
	public List<Map<String, Object>> getLineLossMeterList(Long ledgerId, Long meterId) {
		return meterManagerMapper.getLineLossMeterList(ledgerId,meterId);
	}

	@Override
	public Integer getCommModeByLedgerId(Long ledgerId) {
		Integer commMode = 2;
		List<Integer> commModes = this.meterManagerMapper.getCommModeByLedgerId(ledgerId);
		if(commModes != null && commModes.size() > 1){
			 commMode = 2;
		}else if (commModes != null && commModes.size() == 1){
			commMode = commModes.get(0);
		}
		return commMode;
	}

	@Override
	public Long getMeterParentByMeterId(Long meterId) {
		return meterManagerMapper.getMeterParentByMeterId(meterId);
	}

	/**
	 * 工程师界面定位节点
	 */
	@Override
	public Map<String, Object> getMeterInfoByLedgerId(Long ledgerId,Integer selectType) {
		Long rootId = null;
		Long meterId = null;
		Long companyId = null;//能管对象所属企业id
		Long meterRootId = null;
		boolean hasTree = true;
		List<Long> meterIds = null; 
		List<Long> parentMeterIds = null;
		List<Long> parentLedgerIds = null;
		Map<String, Object> map = new HashMap<String,Object>();
		if(selectType == 1) {
			meterIds = meterManagerMapper.getMeterInfo1(ledgerId);//判断T_LEDGER_RELATION有没有测量点id信息   改能管对象下的测量点 
			if(null == meterIds || meterIds.size() == 0){
				meterId = meterManagerMapper.getCheckedMeterIdByLegerId(ledgerId);//判断T_LEDGER_RELATION有没有测量点id信息   能管对象下总加组下关联的测量点
			}
			
			if(null == meterId){//若关联的总加组对象均是区域对象  走这一层
				List<Long> relationIds = null;
				relationIds = meterManagerMapper.getRelationIdsByLedgerId(ledgerId);
				if(null != relationIds && relationIds.size() > 0){
					for (int i = 0; i < relationIds.size(); i++) {
						List<Long> ids = null;  //公司id集合
						ids = meterManagerMapper.getCompanyIdsByLedgerId(relationIds.get(i));
						if(ids.size() > 0){
							meterId = meterManagerMapper.getCheckedMeterIdByLegerId2(ids);
						}
						if(null != meterId) break;
					}
				}
			}
			
			
			if((null == meterId || meterId == 0) && (null == meterIds || meterIds.size() == 0)){
				meterIds = meterManagerMapper.getMeterInfo(ledgerId);//判断从T_LEDGER_SHOW有没有测量点id信息
				if(null == meterIds || meterIds.size() == 0){
					companyId = meterManagerMapper.getCompanyIdByLedgerId(ledgerId);
					if(null != companyId){
						meterIds =  meterManagerMapper.getMeterInfo2(companyId);//根据所属企业id,最后再次判断T_LEDGER_RELATION有没有测量点id信息
					}
				}
			}
		}
		if((null == meterId || meterId == 0) && null != meterIds && meterIds.size() > 0) meterId = meterIds.get(0);
		if(selectType == 2){
			meterId = ledgerId;
			rootId = meterManagerMapper.getRootIdByMeterId(ledgerId);
			parentMeterIds = meterManagerMapper.getParentMeterIdsById(ledgerId);
			parentLedgerIds = meterManagerMapper.getParentLedgerIdsById(ledgerId);
		}else if(selectType == 1){
			rootId = meterManagerMapper.getRootIdByLegerId(ledgerId);
			if(null != meterId ){
				parentMeterIds = meterManagerMapper.getParentMeterIdsById(meterId);
				meterRootId = meterManagerMapper.getMeterParentRootIdByMeterId(meterId);
			}
			
			if(null == meterRootId ){
				hasTree = false;
			}else{
				parentLedgerIds = meterManagerMapper.getParentLedgerIdsById(meterRootId); 
			}
		}
		
		map.put("hasTree", hasTree);
		map.put("meterId", meterId);
		map.put("rootId", rootId);
		map.put("parentMeterIds", parentMeterIds);
		map.put("parentLedgerIds", parentLedgerIds);
		return 	map;
	}

	/**
	 * 工程师页面加载第一个节点
	 */
	@Override
	public Map<String, Object> elePageLoadFirstMeter(Long ledgerId) {
		Long meterId = null; 
		int  nanlyType = 0;
		List<Long> parentLedgerIds = null;
		List<Long> childIds = null;
		Map<String, Object> map = new HashMap<String,Object>();
		nanlyType = meterManagerMapper.getLedgerAnalyType(ledgerId);
		if(nanlyType == 102){ //企事业单位
			List<Long> ids = new ArrayList<Long>(); 
			ids.add(ledgerId);
			if(ids.size() > 0)
				meterId = meterManagerMapper.getCheckedMeterIdByLegerId2(ids);
			if(null != meterId)
				parentLedgerIds = meterManagerMapper.getParentLedgerIdsById(meterId);
		}else{ //区域
			childIds = meterManagerMapper.getChildIdsByLegerId(ledgerId);
			if(null != childIds && childIds.size() > 0){
				for (int i = 0; i < childIds.size(); i++) {
					List<Long> ids = null;  //公司id集合
					ids = meterManagerMapper.getCompanyIdsByLedgerId(childIds.get(i));
					if(ids.size() > 0)
						meterId = meterManagerMapper.getCheckedMeterIdByLegerId2(ids);
					if(null != meterId)
						parentLedgerIds = meterManagerMapper.getParentLedgerIdsById(meterId);
					if(null != meterId && null !=parentLedgerIds) break;
				}
			}
		}
		map.put("parentLedgerIds", parentLedgerIds);
		map.put("meterId", meterId);
		return map;
	}
}
