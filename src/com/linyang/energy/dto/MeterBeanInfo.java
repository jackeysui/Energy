package com.linyang.energy.dto;
public  class MeterBeanInfo{
		
		private long meterId;
		private String meterName;
		
		
		public MeterBeanInfo(long meterId, String meterName) {
			super();
			this.meterId = meterId;
			this.meterName = meterName;
		}
		public long getMeterId() {
			return meterId;
		}
		public void setMeterId(long meterId) {
			this.meterId = meterId;
		}
		public String getMeterName() {
			return meterName;
		}
		public void setMeterName(String meterName) {
			this.meterName = meterName;
		}
		
		
		
	}