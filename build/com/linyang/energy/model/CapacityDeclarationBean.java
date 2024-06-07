package com.linyang.energy.model;/*
 *                        _oo0oo_
 *                       o8888888o
 *                       88" . "88
 *                       (| -_- |)
 *                       0\  =  /0
 *                     ___/`---'\___
 *                   .' \\|     |// '.
 *                  / \\|||  :  |||// \
 *                 / _||||| -:- |||||- \
 *                |   | \\\  - /// |    |
 *                | \_|  ''\---/''  |_/ |
 *                \  .-\__  '-'  ___/-. /
 *              ___'. .'  /--.--\  `. .'___
 *           ."" '<  `.___\_<|>_/___.'  >' "".
 *          | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *          \  \ `_.   \_ __\ /__ _/   .-` /  /
 *      =====`-.____`.___ \_____/___.-`___.-'=====
 *                        `=---='
 *
 *
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *            佛祖保佑     永不宕机     永无BUG
 */

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 13:10 2020/9/16
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public class CapacityDeclarationBean {
	
	private String id;
	private String name;
	private Double value98;
	private Double value97;
	private Double value96;
	private Double valueOther;
	private Double valueTotal;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Double getValue98() {
		return value98;
	}
	
	public void setValue98(Double value98) {
		this.value98 = value98;
	}
	
	public Double getValue97() {
		return value97;
	}
	
	public void setValue97(Double value97) {
		this.value97 = value97;
	}
	
	public Double getValue96() {
		return value96;
	}
	
	public void setValue96(Double value96) {
		this.value96 = value96;
	}
	
	public Double getValueOther() {
		return valueOther;
	}
	
	public void setValueOther(Double valueOther) {
		this.valueOther = valueOther;
	}
	
	public Double getValueTotal() {
		return valueTotal;
	}
	
	public void setValueTotal(Double valueTotal) {
		this.valueTotal = valueTotal;
	}
}
