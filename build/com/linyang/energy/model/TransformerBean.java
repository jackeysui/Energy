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
public class TransformerBean {
	
	private String TRANNAME;// 名称
	private int RUNCAP;// 额定容量
	private String RUNSTATUS;// 运行状态
	private String STOPDATE;// 停用日期
	private String STARTDATE;// 启用日期
	private int EQUIPID;
	
	public String getTRANNAME() {
		return TRANNAME;
	}
	
	public void setTRANNAME(String TRANNAME) {
		this.TRANNAME = TRANNAME;
	}
	
	public int getRUNCAP() {
		return RUNCAP;
	}
	
	public void setRUNCAP(int RUNCAP) {
		this.RUNCAP = RUNCAP;
	}
	
	public String getRUNSTATUS() {
		return RUNSTATUS;
	}
	
	public void setRUNSTATUS(String RUNSTATUS) {
		this.RUNSTATUS = RUNSTATUS;
	}
	
	public String getSTOPDATE() {
		return STOPDATE;
	}
	
	public void setSTOPDATE(String STOPDATE) {
		this.STOPDATE = STOPDATE;
	}
	
	public String getSTARTDATE() {
		return STARTDATE;
	}
	
	public void setSTARTDATE(String STARTDATE) {
		this.STARTDATE = STARTDATE;
	}
	
	public int getEQUIPID() {
		return EQUIPID;
	}
	
	public void setEQUIPID(int EQUIPID) {
		this.EQUIPID = EQUIPID;
	}
}
