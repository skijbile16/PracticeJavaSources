/*
 * Copyright (c) 2014, JMR Infotech and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of JMR or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jmr.framework.util;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class UserContext implements Serializable {
	@Resource(name = "jmrLogger")
	private JMRLogger jmrLogger;

	private static final long serialVersionUID = 1L;

	public UserContext() {
		super();
	}

	private String first_name = "";
	private String middle_name = "";
	private String last_name = "";
	private String username = "";
	private String branchcode = "";
	private boolean isBrnHoliday = false;
	private String strTxnDate = "";
	private String homeBranch = "";
	private String strBrnDate = "";
	private String lastLoginTime = "";
	private String scrnName = "";
	private String alrtMsg = "";
	private String remipaddress = "";
	private String locipaddress = "";
	private String sessionid = "";
	private String usertype = "";
	private String screenaccess = "";
	private String userlanguage = "";

	public void setUserName(String username) {
		this.username = username;

	}

	public void setBranchCode(String branchcode) {
		this.branchcode = branchcode;

	}

	public void setScreenaccess(String screenaccess) {
		this.screenaccess = screenaccess;

	}
	public void setRemipaddress(String remipaddress) {
		this.remipaddress = remipaddress;
	}

	public void setLocipaddress(String locipaddress) {
		this.locipaddress = locipaddress;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;

	}

	public void setScrnName(String scrnName) {
		this.scrnName = scrnName;

	}

	public void setAlrtMsg(String alrtMsg) {
		this.alrtMsg = alrtMsg;

	}

	public String getUserName() {
		return this.username;
	}
	public String getScreenaccess() {
		return this.screenaccess;
	}
	
	public String getRemipaddress() {
		return this.remipaddress;
	}

	public String getLocipaddress() {
		return this.locipaddress;
	}

	public String getBranchCode() {
		return this.branchcode;
	}

	public void setHomeBranch(String homeBranch) {
		this.homeBranch = homeBranch;
	}

	public String getHomeBranch() {
		return this.homeBranch;
	}

	public String getBrnDate() {
		return strBrnDate;
	}

	public void setBrnDate(String strBrnDate) {
		this.strBrnDate = strBrnDate;
	}

	public void setLogger(JMRLogger log) {
		this.jmrLogger = log;
	}

	public JMRLogger getLogger() {
		return this.jmrLogger;
	}

	

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public String getScrnName() {
		return scrnName;
	}

	public String getAlrtMsg() {
		return alrtMsg;
	}

	public void setBrnHoliday(boolean isBrnHoliday) {
		this.isBrnHoliday = isBrnHoliday;
	}

	public boolean isBrnHoliday() {
		return this.isBrnHoliday;
	}

	public void setTxnDate(String strTxnDate) {
		this.strTxnDate = strTxnDate;
	}

	public String getTxnDate() {
		return this.strTxnDate;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getSessionid() {
		return this.sessionid;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getUsertype() {
		return this.usertype;
	}
	public void setUserlanguage(String userlanguage) {
		this.userlanguage = userlanguage;
	}

	public String getUserlanguage() {
		return this.userlanguage;
	}
	
	
	public JMRLogger getJmrLogger() {
		return jmrLogger;
	}

	public void setJmrLogger(JMRLogger jmrLogger) {
		this.jmrLogger = jmrLogger;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBranchcode() {
		return branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	public String getStrTxnDate() {
		return strTxnDate;
	}

	public void setStrTxnDate(String strTxnDate) {
		this.strTxnDate = strTxnDate;
	}

	public String getStrBrnDate() {
		return strBrnDate;
	}

	public void setStrBrnDate(String strBrnDate) {
		this.strBrnDate = strBrnDate;
	}

	
}
