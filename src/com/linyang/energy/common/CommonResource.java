package com.linyang.energy.common;

/**
 * @Description 公共资源, 主要用于在java代码中国际化
 * @author Leegern
 * @date 2013-8-7 下午04:53:10
 */
public class CommonResource {
	
	/**
	 * 换算单位:电
	 */
	private double elecUnit;
	
	/**
	 * 换算单位:水
	 */
	private double waterUnit;
	
	/**
	 * 换算单位:气
	 */
	private double gasUnit;

    /**
     * 换算单位: 热
     */
    private double hotUnit;
	
	/**
	 * 建筑节能潜力排名, 能耗标准值
	 */
	private double energyValue = 2;
	
	/**
	 * 建筑节能潜力排名, 空调标准值
	 */
	private double airValue = 2;
	
	
	/**
	 * @return the elecUnit
	 */
	public double getElecUnit() {
		return elecUnit;
	}

	/**
	 * @param elecUnit the elecUnit to set
	 */
	public void setElecUnit(double elecUnit) {
		this.elecUnit = elecUnit;
	}

	/**
	 * @return the waterUnit
	 */
	public double getWaterUnit() {
		return waterUnit;
	}

	/**
	 * @param waterUnit the waterUnit to set
	 */
	public void setWaterUnit(double waterUnit) {
		this.waterUnit = waterUnit;
	}

	/**
	 * @return the gasUnit
	 */
	public double getGasUnit() {
		return gasUnit;
	}

	/**
	 * @param gasUnit the gasUnit to set
	 */
	public void setGasUnit(double gasUnit) {
		this.gasUnit = gasUnit;
	}

    /**
     * @return the hotUnit
     */
    public double getHotUnit() {
        return hotUnit;
    }

    /**
     * @param hotUnit the hotUnit to set
     */
    public void setHotUnit(double hotUnit) {
        this.hotUnit = hotUnit;
    }

	/**
	 * @return the energyValue
	 */
	public double getEnergyValue() {
		return energyValue;
	}

	/**
	 * @param energyValue the energyValue to set
	 */
	public void setEnergyValue(double energyValue) {
		this.energyValue = energyValue;
	}

	/**
	 * @return the airValue
	 */
	public double getAirValue() {
		return airValue;
	}

	/**
	 * @param airValue the airValue to set
	 */
	public void setAirValue(double airValue) {
		this.airValue = airValue;
	}
}
