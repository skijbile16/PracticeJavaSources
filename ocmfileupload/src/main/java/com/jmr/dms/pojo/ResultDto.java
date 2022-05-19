package com.jmr.dms.pojo;

public class ResultDto {
	private int returnCode;
	private String returnMsg;
	private boolean errorMsg;
	private Object returnData;
	
	public boolean isErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(boolean errorMsg) {
		this.errorMsg = errorMsg;
	}
	public int getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public Object getReturnData() {
		return returnData;
	}
	public void setReturnData(Object returnData) {
		this.returnData = returnData;
	}
}
