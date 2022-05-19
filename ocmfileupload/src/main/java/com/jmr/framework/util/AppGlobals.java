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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import org.springframework.stereotype.Component;

@Component
public class AppGlobals implements Serializable {
	
	@Resource(name = "servletContext")
	private ServletContext servletContext;
	private static final long serialVersionUID = 1L;
	private String LOG_FILES_PATH = "";
	public static String CONTEXT_PATH = "";
	public static String CONTEXT_REAL_PATH = "";
	private String REPORTS_SAVE_PATH = "";
	private String DOC_SAVE_PATH = "";
	private String GEN_FILE_SAVE_PATH = "";
	private String SESSION_TIME_OUT = "";
	private String FCUBSCustomerServiceURL = "";
	private String FCUBSQueryCustomer = "";
	private String FCUBSCreateCustomer = "";
	private String FCUBSCLServiceURL = "";
	private String FCUBSCL_SaveAccount = "";
	private String FCUBS_VERSION = "";
	private String FCUBS_EXTSYS = "";
	private String EXTSYS_USERID = "SYSTEM";
	private String FLEXCUBE_INTERFACE_URL = "";
	private String JNDI_POOL = "";
	private String JNDI_TIMEOUT = "";
	private String LDAP_CONN_TIMEOUT = "";
	private String AUTH_TYPE = "";
	private String OBJECTCLASS = "";
	private String INITIAL_CONTEXT_FACTORY = "";
	private int NO_OF_LDAP_TRIES = 0;
	private String GROUPDN_USER = "";
	private String USER = "";
	private String USER_ID = "";
	private String DISTINGUISHED_NAME = "";
	private String LDAPSERVERURL = "";
	private String ROOTCONTEXT = "";
	private String ROOTDN = "";
	private String ROOTPASS = "";
	private String LDAP_CONNECTION = "";
	private String CUST_APP_MODE = "";

	// For Tally Webservice
	private String TALLY_COMPANY_WSDL_FILE = "";
	private String TALLY_SERVER_PORT = "";
	private String TALLY_RESPONSE_FILE_PATH = "";
	private String TALLY_SERVER_IP = "";
	private String TALLY_COMPANY_NAME = "";

	// For testing Only
	public static int openedConnections = 0;
	public static int closedConnections = 0;
	private static AppGlobals instance = null;

	/* Ftp Global Variables */
	private String FTPIPADDRESS = "";
	private String FTPUSERNAME = "";
	private String FTPPASSWORD = "";
	private String FTPACTIVEPATH = "";
	private String FTPSTANDBYPATH = "";
	private String FTPARCHIVEPATH = "";
	private String FTPRELESD_ARCHIVEPATH = "";
	private String FTPEXTRACTIONPATH = "";
	private String FTPREMOTESEPARATOR = "";
	private String FTPSTANDBYFLAG = "";
	private String FTPARCHIVEFLAG = "";
	private int FTPARCHIVEAFTER = 0;
	private int FTPSTANDBYAFTER = 0;

	/** BASIS Interface attributes */
	private String wsdlUrl = "";
	private String nameSpace = "";
	private String serviceNm = "";
	private String portNm = "";
	private String LOGGER_LEVEL = "0";

	/* Server And Interface Attributes */
	private String SERVER_NAME = "";
	private String JDBC_INTERFACE = "";
	private String JAVA_INSTALL_PATH = "";

	// CBC Attributes
	private String sslCertificatePath = "";

	public AppGlobals() {
		super();
	}

	public static synchronized AppGlobals getInstance() {
		return instance;
	}

	public static void initInstance(String contextPath,ServletContext servletContext) throws IOException {
		if (instance == null) {
			instance = new AppGlobals(contextPath,servletContext);
		}
	}
	private AppGlobals(String strContextPath,ServletContext servletContext) {
		CONTEXT_PATH = strContextPath;
		this.servletContext = servletContext;
		String strContextRealPath = servletContext.getRealPath("/Config.properties"); 
		Properties props = null;
		try {
			props = new Properties();
			// System.out.println("Props  Files path is " + strContextRealPath);
			File file = new File(strContextRealPath);
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			props.load(bis);
			setParams(props);
			if (bis != null)
				bis.close();
		} catch (Exception E) {
			E.printStackTrace();
		} // catch

	}

	public void setParams(Properties props) throws IOException {
		// System.out.println("Application Configuration Properties KKKKK:"+props);
		LOG_FILES_PATH = props.getProperty("LOG_FILES_PATH");
		REPORTS_SAVE_PATH = props.getProperty("REPORTS_SAVE_PATH");
		DOC_SAVE_PATH = props.getProperty("DOC_SAVE_PATH");
		JAVA_INSTALL_PATH = props.getProperty("JAVA_INSTALL_PATH");
		SESSION_TIME_OUT = props.getProperty("SESSION_TIME_OUT");
		GEN_FILE_SAVE_PATH = props.getProperty("FILE_CREATED");
		LOGGER_LEVEL = props.getProperty("LOGGER_LEVEL");
		CUST_APP_MODE = props.getProperty("CUST_APP_MODE");
		// System.out.println("LOG_FILES_PATH :"+LOG_FILES_PATH+" REPORTS_SAVE_PATH :"+REPORTS_SAVE_PATH+" DOC_SAVE_PATH:"+DOC_SAVE_PATH+" "+JAVA_INSTALL_PATH+" SESSION_TIME_OUT:"+SESSION_TIME_OUT+" GEN_FILE_SAVE_PATH :"+GEN_FILE_SAVE_PATH);
		setFCUBSCustomerServiceURL(props.getProperty("FCUBSCUSTOMERSERVICE"));
		setFCUBSQueryCustomer(props.getProperty("QUERYCUSTOMER_OPERATION"));
		setFCUBSCreateCustomer(props.getProperty("CREATECUSTOMER_OPERATION"));
		setFCUBSCLServiceURL(props.getProperty("FCUBSCLSERVICE"));
		setFCUBSCL_SaveAccount(props.getProperty("SAVEACCOUNT_CL_OPERATION"));
		setFCUBS_VERSION(props.getProperty("FCUBS_VERSION"));
		setFCUBS_EXTSYS(props.getProperty("EXTSYS"));
		setEXTSYS_USERID(props.getProperty("EXTSYS_USERID"));
		setLoggerLevel(LOGGER_LEVEL);
		setSession_Time_Out(SESSION_TIME_OUT);
		setCustAppMode(CUST_APP_MODE);

		/* Ftp Karel */
		setFTPIPAddress(props.getProperty("FTPIPADDRESS"));
		setFTPUserName(props.getProperty("FTPUSERNAME"));
		setFTPPassword(props.getProperty("FTPPASSWORD"));
		setFTPActivePath(props.getProperty("FTPACTIVEPATH"));
		setFTPStandbyPath(props.getProperty("FTPSTANDBYPATH"));
		setFTPArchivePath(props.getProperty("FTPARCHIVEPATH"));
		setFTPReleasedArchivePath(props.getProperty("FTPRELESD_ARCHIVEPATH"));
		setFTPExtractionPath(props.getProperty("FTPEXTRACTIONPATH"));
		setFTPRemoteSeparator(props.getProperty("FTPREMOTESEPARATOR"));
		setFTPStandByFlag(props.getProperty("FTPSTANDBYFLAG"));
		setFTPArchiveFlag(props.getProperty("FTPARCHIVEFLAG"));
		setFTPArchiveAfter(Integer.parseInt(props
				.getProperty("FTPARCHIVEAFTER")));
		setFTPStandByAfter(Integer.parseInt(props
				.getProperty("FTPSTANDBYAFTER")));
		/* Ftp Karel */

		/** BASIS interface attributes */
		setWsdlUrl(props.getProperty("WSDL_LOC"));
		setNameSpace(props.getProperty("NAME_SPACE"));
		setServiceNm(props.getProperty("SERVICE"));
		setPortNm(props.getProperty("PORT_NAME"));
		/** BASIS interface attributes */

		/* Server Name and Interfaces attributes */
		setServerName(props.getProperty("SERVER_NAME"));
		setJDBCInterface(props.getProperty("JDBC_INTERFACE"));
		setFlexCubeInterFaceUrl(props.getProperty("FLEXCUBE_INTERFACE_URL"));

		// added by alok on 30-11-2016

		setJndiPool(props.getProperty("JNDI_POOL"));
		setJndiTimeout(props.getProperty("JNDI_TIMEOUT"));
		setLdapConnTimeout(props.getProperty("LDAP_CONN_TIMEOUT"));
		setAuthType(props.getProperty("AUTH_TYPE"));
		setObjectclass(props.getProperty("OBJECTCLASS"));
		setInitialContextFactory(props.getProperty("INITIAL_CONTEXT_FACTORY"));
		setNoOfLdapTries(Integer
				.parseInt(props.getProperty("NO_OF_LDAP_TRIES")));
		setGroupdnUser(props.getProperty("GROUPDN_USER"));
		setUser(props.getProperty("USER"));
		setUserId(props.getProperty("USER_ID"));
		setDistinguishedName(props.getProperty("DISTINGUISHED_NAME"));
		setLdapserverurl(props.getProperty("LDAPSERVERURL"));
		setRootcontext(props.getProperty("ROOTCONTEXT"));
		setRootdn(props.getProperty("ROOTDN"));
		setRootpass(props.getProperty("ROOTPASS"));

		// CBC File path attrs
		setSslCertificatePath(props.getProperty("CERTIFICATE_PATH"));

	}
	public void setMailParams(HashMap hmMailConfig) throws IOException {
		if (hmMailConfig.get("LOG_FILES_PATH") != null)
			setLog_Files_Path((String) hmMailConfig.get("LOG_FILES_PATH"));
		// System.out.println("LOG_FILES_PATH : " + getLog_Files_Path());
		if (hmMailConfig.get("REPORTS_SAVE_PATH") != null)
			setReports_Save_Path((String) hmMailConfig.get("REPORTS_SAVE_PATH"));
		// System.out.println("REPORTS_SAVE_PATH : " + getReports_Save_Path());
		if (hmMailConfig.get("DOC_SAVE_PATH") != null)
			setDoc_Save_Path((String) hmMailConfig.get("DOC_SAVE_PATH"));
		// System.out.println("DOC_SAVE_PATH : " + getDoc_Save_Path());
		if (hmMailConfig.get("JAVA_INSTALL_PATH") != null)
			setJava_install_Path((String) hmMailConfig.get("JAVA_INSTALL_PATH"));
		// System.out.println("JAVA_INSTALL_PATH : " + getJava_install_Path());
		if (hmMailConfig.get("SESSION_TIME_OUT") != null)
			setSession_Time_Out((String) hmMailConfig.get("SESSION_TIME_OUT"));
		// System.out.println("SESSION_TIME_OUT : " + getSession_Time_Out());
		if (hmMailConfig.get("GEN_FILE_SAVE_PATH") != null)
			setGen_File_Save_Path((String) hmMailConfig
					.get("GEN_FILE_SAVE_PATH"));
		// System.out.println("GEN_FILE_SAVE_PATH : " + getGen_File_Save_Path());
		if (hmMailConfig.get("LOGGER_LEVEL") != null)
			setLoggerLevel((String) hmMailConfig.get("LOGGER_LEVEL"));
		if (hmMailConfig.get("FCUBSCUSTOMERSERVICE") != null)
			setFCUBSCustomerServiceURL((String) hmMailConfig
					.get("FCUBSCUSTOMERSERVICE"));
		if (hmMailConfig.get("QUERYCUSTOMER_OPERATION") != null)
			setFCUBSQueryCustomer((String) hmMailConfig
					.get("QUERYCUSTOMER_OPERATION"));
		if (hmMailConfig.get("CREATECUSTOMER_OPERATION") != null)
			setFCUBSCreateCustomer((String) hmMailConfig
					.get("CREATECUSTOMER_OPERATION"));
		if (hmMailConfig.get("FCUBSCLSERVICE") != null)
			setFCUBSCLServiceURL((String) hmMailConfig.get("FCUBSCLSERVICE"));
		if (hmMailConfig.get("SAVEACCOUNT_CL_OPERATION") != null)
			setFCUBSCL_SaveAccount((String) hmMailConfig
					.get("SAVEACCOUNT_CL_OPERATION"));
		if (hmMailConfig.get("FCUBS_VERSION") != null)
			setFCUBS_VERSION((String) hmMailConfig.get("FCUBS_VERSION"));
		if (hmMailConfig.get("EXTSYS") != null)
			setFCUBS_EXTSYS((String) hmMailConfig.get("EXTSYS"));
		if (hmMailConfig.get("EXTSYS_USERID") != null)
			setEXTSYS_USERID((String) hmMailConfig.get("EXTSYS_USERID"));
		/* Ftp Karel */
		if (hmMailConfig.get("FTPIPADDRESS") != null)
			setFTPIPAddress((String) hmMailConfig.get("FTPIPADDRESS"));
		if (hmMailConfig.get("FTPUSERNAME") != null)
			setFTPUserName((String) hmMailConfig.get("FTPUSERNAME"));
		if (hmMailConfig.get("FTPPASSWORD") != null)
			setFTPPassword((String) hmMailConfig.get("FTPPASSWORD"));
		if (hmMailConfig.get("FTPACTIVEPATH") != null)
			setFTPActivePath((String) hmMailConfig.get("FTPACTIVEPATH"));
		if (hmMailConfig.get("FTPSTANDBYPATH") != null)
			setFTPStandbyPath((String) hmMailConfig.get("FTPSTANDBYPATH"));
		if (hmMailConfig.get("FTPARCHIVEPATH") != null)
			setFTPArchivePath((String) hmMailConfig.get("FTPARCHIVEPATH"));
		if (hmMailConfig.get("FTPRELESD_ARCHIVEPATH") != null)
			setFTPReleasedArchivePath((String) hmMailConfig
					.get("FTPRELESD_ARCHIVEPATH"));
		if (hmMailConfig.get("FTPEXTRACTIONPATH") != null)
			setFTPExtractionPath((String) hmMailConfig.get("FTPEXTRACTIONPATH"));
		if (hmMailConfig.get("FTPREMOTESEPARATOR") != null)
			setFTPRemoteSeparator((String) hmMailConfig
					.get("FTPREMOTESEPARATOR"));
		if (hmMailConfig.get("FTPSTANDBYFLAG") != null)
			setFTPStandByFlag((String) hmMailConfig.get("FTPSTANDBYFLAG"));
		if (hmMailConfig.get("FTPARCHIVEFLAG") != null)
			setFTPArchiveFlag((String) hmMailConfig.get("FTPARCHIVEFLAG"));
		if (hmMailConfig.get("FTPARCHIVEAFTER") != null)
			setFTPArchiveAfter(Integer.parseInt((String) hmMailConfig
					.get("FTPARCHIVEAFTER")));
		if (hmMailConfig.get("FTPSTANDBYAFTER") != null)
			setFTPStandByAfter(Integer.parseInt((String) hmMailConfig
					.get("FTPSTANDBYAFTER")));
		/** BASIS interface attributes */
		if (hmMailConfig.get("WSDL_LOC") != null)
			setWsdlUrl((String) hmMailConfig.get("WSDL_LOC"));
		if (hmMailConfig.get("NAME_SPACE") != null)
			setNameSpace((String) hmMailConfig.get("NAME_SPACE"));
		if (hmMailConfig.get("SERVICE") != null)
			setServiceNm((String) hmMailConfig.get("SERVICE"));
		if (hmMailConfig.get("PORT_NAME") != null)
			setPortNm((String) hmMailConfig.get("PORT_NAME"));

		/* Server Name and Interfaces attributes */
		if (hmMailConfig.get("SERVER_NAME") != null)
			setServerName((String) hmMailConfig.get("SERVER_NAME"));
		if (hmMailConfig.get("JDBC_INTERFACE") != null)
			setJDBCInterface((String) hmMailConfig.get("JDBC_INTERFACE"));
		if (hmMailConfig.get("FLEXCUBE_INTERFACE_URL") != null)
			setFlexCubeInterFaceUrl((String) hmMailConfig
					.get("FLEXCUBE_INTERFACE_URL"));

		if (hmMailConfig.get("JNDI_POOL") != null)
			setJndiPool((String) hmMailConfig.get("JNDI_POOL"));
		if (hmMailConfig.get("JNDI_TIMEOUT") != null)
			setJndiTimeout((String) hmMailConfig.get("JNDI_TIMEOUT"));
		if (hmMailConfig.get("LDAP_CONN_TIMEOUT") != null)
			setLdapConnTimeout((String) hmMailConfig.get("LDAP_CONN_TIMEOUT"));
		if (hmMailConfig.get("AUTH_TYPE") != null)
			setAuthType((String) hmMailConfig.get("AUTH_TYPE"));
		if (hmMailConfig.get("OBJECTCLASS") != null)
			setObjectclass((String) hmMailConfig.get("OBJECTCLASS"));
		if (hmMailConfig.get("INITIAL_CONTEXT_FACTORY") != null)
			setInitialContextFactory((String) hmMailConfig
					.get("INITIAL_CONTEXT_FACTORY"));
		if (hmMailConfig.get("NO_OF_LDAP_TRIES") != null)
			setNoOfLdapTries(Integer.parseInt((String) hmMailConfig
					.get("NO_OF_LDAP_TRIES")));
		if (hmMailConfig.get("GROUPDN_USER") != null)
			setGroupdnUser((String) hmMailConfig.get("GROUPDN_USER"));
		if (hmMailConfig.get("USER") != null)
			setUser((String) hmMailConfig.get("USER"));
		if (hmMailConfig.get("USER_ID") != null)
			setUserId((String) hmMailConfig.get("USER_ID"));
		if (hmMailConfig.get("DISTINGUISHED_NAME") != null)
			setDistinguishedName((String) hmMailConfig
					.get("DISTINGUISHED_NAME"));
		if (hmMailConfig.get("LDAPSERVERURL") != null)
			setLdapserverurl((String) hmMailConfig.get("LDAPSERVERURL"));
		if (hmMailConfig.get("ROOTCONTEXT") != null)
			setRootcontext((String) hmMailConfig.get("ROOTCONTEXT"));
		if (hmMailConfig.get("ROOTDN") != null)
			setRootdn((String) hmMailConfig.get("ROOTDN"));
		if (hmMailConfig.get("ROOTPASS") != null)
			setRootpass((String) hmMailConfig.get("ROOTPASS"));

		/* Added on 6-3-2017 */
		if (hmMailConfig.get("LDAP_CONNECTION") != null)
			setLdapConnection((String) hmMailConfig.get("LDAP_CONNECTION"));

		/* Added on 15-3-2017 */
		if (hmMailConfig.get("TALLY_COMPANY_WSDL_FILE") != null)
			setTallyCompanyWsdlFile((String) hmMailConfig
					.get("TALLY_COMPANY_WSDL_FILE"));

		if (hmMailConfig.get("TALLY_SERVER_PORT") != null)
			setTallyServerPort((String) hmMailConfig.get("TALLY_SERVER_PORT"));

		if (hmMailConfig.get("TALLY_RESPONSE_FILE_PATH") != null)
			setTallyResponseFilePath((String) hmMailConfig
					.get("TALLY_RESPONSE_FILE_PATH"));

		if (hmMailConfig.get("TALLY_SERVER_IP") != null)
			setTallyServerIp((String) hmMailConfig.get("TALLY_SERVER_IP"));

		if (hmMailConfig.get("TALLY_COMPANY_NAME") != null)
			setTallyCompanyName((String) hmMailConfig.get("TALLY_COMPANY_NAME"));

		// CBC File path attrs
		if (hmMailConfig.get("CERTIFICATE_PATH") != null)
			setSslCertificatePath((String) hmMailConfig.get("CERTIFICATE_PATH"));
	}

	public String getLog_Files_Path() {
		return LOG_FILES_PATH;
	}

	public void setLog_Files_Path(String LOG_FILES_PATH) {
		this.LOG_FILES_PATH = LOG_FILES_PATH;
	}

	public String getContextPath() {
		return CONTEXT_PATH;
	}
	public String getContextRealPath(String FileName) {
		return servletContext.getRealPath("/"+FileName);
	}
	public String getReports_Save_Path() {
		return REPORTS_SAVE_PATH;
	}

	public void setReports_Save_Path(String REPORTS_SAVE_PATH) {
		this.REPORTS_SAVE_PATH = REPORTS_SAVE_PATH;
	}

	public String getGen_File_Save_Path() {
		return GEN_FILE_SAVE_PATH;
	}

	public void setGen_File_Save_Path(String GEN_FILE_SAVE_PATH) {
		this.GEN_FILE_SAVE_PATH = GEN_FILE_SAVE_PATH;
	}

	public String getDoc_Save_Path() {
		return DOC_SAVE_PATH;
	}

	public void setDoc_Save_Path(String DOC_SAVE_PATH) {
		this.DOC_SAVE_PATH = DOC_SAVE_PATH;
	}

	public String getJava_install_Path() {
		return JAVA_INSTALL_PATH;
	}

	public void setJava_install_Path(String JAVA_INSTALL_PATH) {
		this.JAVA_INSTALL_PATH = JAVA_INSTALL_PATH;
	}

	public String getSession_Time_Out() {
		return SESSION_TIME_OUT;
	}

	public void setSession_Time_Out(String SESSION_TIME_OUT) {
		this.SESSION_TIME_OUT = SESSION_TIME_OUT;
	}


	// FCUBS Interface Accessors

	public void setFCUBSCustomerServiceURL(String FCUBSCustomerServiceURL) {
		this.FCUBSCustomerServiceURL = FCUBSCustomerServiceURL;
	}

	public String getFCUBSCustomerServiceURL() {
		return FCUBSCustomerServiceURL;
	}

	public void setFCUBSQueryCustomer(String FCUBSQueryCustomer) {
		this.FCUBSQueryCustomer = FCUBSQueryCustomer;
	}

	public String getFCUBSQueryCustomer() {
		return FCUBSQueryCustomer;
	}

	public void setFCUBSCreateCustomer(String FCUBSCreateCustomer) {
		this.FCUBSCreateCustomer = FCUBSCreateCustomer;
	}

	public String getFCUBSCreateCustomer() {
		return FCUBSCreateCustomer;
	}

	public void setFCUBSCLServiceURL(String FCUBSCLServiceURL) {
		this.FCUBSCLServiceURL = FCUBSCLServiceURL;
	}

	public String getFCUBSCLServiceURL() {
		return FCUBSCLServiceURL;
	}

	public void setFCUBSCL_SaveAccount(String FCUBSCL_SaveAccount) {
		this.FCUBSCL_SaveAccount = FCUBSCL_SaveAccount;
	}

	public String getFCUBSCL_SaveAccount() {
		return FCUBSCL_SaveAccount;
	}

	public void setFCUBS_VERSION(String FCUBS_VERSION) {
		this.FCUBS_VERSION = FCUBS_VERSION;
	}

	public String getFCUBS_VERSION() {
		return FCUBS_VERSION;
	}

	public void setFCUBS_EXTSYS(String FCUBS_EXTSYS) {
		this.FCUBS_EXTSYS = FCUBS_EXTSYS;
	}

	public String getFCUBS_EXTSYS() {
		return FCUBS_EXTSYS;
	}

	public void setEXTSYS_USERID(String EXTSYS_USERID) {
		this.EXTSYS_USERID = EXTSYS_USERID;
	}

	public String getEXTSYS_USERID() {
		return EXTSYS_USERID;
	}

	public String getLogerLevel() {
		return LOGGER_LEVEL;
	}

	public String getFTPIPAddress() {
		return FTPIPADDRESS;
	}

	public String getFTPUserName() {
		return FTPUSERNAME;
	}

	public String getFTPPassword() {
		return FTPPASSWORD;
	}

	public String getFTPActivePath() {
		return FTPACTIVEPATH;
	}

	public String getFTPStandbyPath() {
		return FTPSTANDBYPATH;
	}

	public String getFTPArchivePath() {
		return FTPARCHIVEPATH;
	}

	public String getFTPRelesdArchivePath() {
		return FTPRELESD_ARCHIVEPATH;
	}

	public String getFTPExtractionPath() {
		return FTPEXTRACTIONPATH;
	}

	public String getFTPRemoteSeparator() {
		return FTPREMOTESEPARATOR;
	}

	public String getFTPStandByFlag() {
		return FTPSTANDBYFLAG;
	}

	public String getFTPArchiveFlag() {
		return FTPARCHIVEFLAG;
	}

	public int getFTPStandByAfter() {
		return FTPSTANDBYAFTER;
	}

	public int getFTPArchiveAfter() {
		return FTPARCHIVEAFTER;
	}

	public String getServerName() {
		return SERVER_NAME;
	}

	public String getJDBCInterface() {
		return JDBC_INTERFACE;
	}

	public String getFlexCubeInterFaceUrl() {
		return FLEXCUBE_INTERFACE_URL;
	}

	public void setServerName(String SERVER_NAME) {
		this.SERVER_NAME = SERVER_NAME;
	}

	public void setLoggerLevel(String LOGGER_LEVEL) {
		this.LOGGER_LEVEL = LOGGER_LEVEL;
	}

	public void setFlexCubeInterFaceUrl(String FLEXCUBE_INTERFACE_URL) {
		this.FLEXCUBE_INTERFACE_URL = FLEXCUBE_INTERFACE_URL;
	}

	public void setJDBCInterface(String JDBC_INTERFACE) {
		this.JDBC_INTERFACE = JDBC_INTERFACE;
	}

	public void setFTPIPAddress(String FTPIPADDRESS) {
		this.FTPIPADDRESS = FTPIPADDRESS;
	}

	public void setFTPUserName(String FTPUSERNAME) {
		this.FTPUSERNAME = FTPUSERNAME;
	}

	public void setFTPPassword(String FTPPASSWORD) {
		this.FTPPASSWORD = FTPPASSWORD;
	}

	public void setFTPActivePath(String FTPACTIVEPATH) {
		this.FTPACTIVEPATH = FTPACTIVEPATH;
	}

	public void setFTPStandbyPath(String FTPSTANDBYPATH) {
		this.FTPSTANDBYPATH = FTPSTANDBYPATH;
	}

	public void setFTPArchivePath(String FTPARCHIVEPATH) {
		this.FTPARCHIVEPATH = FTPARCHIVEPATH;
	}

	public void setFTPExtractionPath(String FTPEXTRACTIONPATH) {
		this.FTPEXTRACTIONPATH = FTPEXTRACTIONPATH;
	}

	public void setFTPRemoteSeparator(String FTPREMOTESEPARATOR) {
		this.FTPREMOTESEPARATOR = FTPREMOTESEPARATOR;
	}

	public void setFTPStandByFlag(String FTPSTANDBYFLAG) {
		this.FTPSTANDBYFLAG = FTPSTANDBYFLAG;
	}

	public void setFTPArchiveFlag(String FTPARCHIVEFLAG) {
		this.FTPARCHIVEFLAG = FTPARCHIVEFLAG;
	}

	public void setFTPStandByAfter(int FTPSTANDBYAFTER) {
		this.FTPSTANDBYAFTER = FTPSTANDBYAFTER;
	}

	public void setFTPArchiveAfter(int FTPARCHIVEAFTER) {
		this.FTPARCHIVEAFTER = FTPARCHIVEAFTER;
	}

	public void setFTPReleasedArchivePath(String FTPRELESD_ARCHIVEPATH) {
		this.FTPRELESD_ARCHIVEPATH = FTPRELESD_ARCHIVEPATH;
	}

	public String getWsdlUrl() {
		return wsdlUrl;
	}

	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getServiceNm() {
		return serviceNm;
	}

	public void setServiceNm(String serviceNm) {
		this.serviceNm = serviceNm;
	}

	public String getPortNm() {
		return portNm;
	}

	public void setPortNm(String portNm) {
		this.portNm = portNm;
	}

	
	public void setSslCertificatePath(String sslCertificatePath) {
		this.sslCertificatePath = sslCertificatePath;
	}

	public String getSslCertificatePath() {
		return sslCertificatePath;
	}
	public String getJndiPool() {
		return JNDI_POOL;
	}

	public void setJndiPool(String jNDI_POOL) {
		JNDI_POOL = jNDI_POOL;
	}

	public String getJndiTimeout() {
		return JNDI_TIMEOUT;
	}

	public void setJndiTimeout(String jNDI_TIMEOUT) {
		JNDI_TIMEOUT = jNDI_TIMEOUT;
	}

	public String getLdapConnTimeout() {
		return LDAP_CONN_TIMEOUT;
	}

	public void setLdapConnTimeout(String lDAP_CONN_TIMEOUT) {
		LDAP_CONN_TIMEOUT = lDAP_CONN_TIMEOUT;
	}

	public String getAuthType() {
		return AUTH_TYPE;
	}

	public void setAuthType(String aUTH_TYPE) {
		AUTH_TYPE = aUTH_TYPE;
	}

	public String getObjectclass() {
		return OBJECTCLASS;
	}

	public void setObjectclass(String oBJECTCLASS) {
		OBJECTCLASS = oBJECTCLASS;
	}

	public String getInitialContextFactory() {
		return INITIAL_CONTEXT_FACTORY;
	}

	public void setInitialContextFactory(String iNITIAL_CONTEXT_FACTORY) {
		INITIAL_CONTEXT_FACTORY = iNITIAL_CONTEXT_FACTORY;
	}

	public int getNoOfLdapTries() {
		return NO_OF_LDAP_TRIES;
	}

	public void setNoOfLdapTries(int nO_OF_LDAP_TRIES) {
		NO_OF_LDAP_TRIES = nO_OF_LDAP_TRIES;
	}

	public String getGroupdnUser() {
		return GROUPDN_USER;
	}

	public void setGroupdnUser(String gROUPDN_USER) {
		GROUPDN_USER = gROUPDN_USER;
	}

	public String getUser() {
		return USER;
	}

	public void setUser(String uSER) {
		USER = uSER;
	}

	public String getUserId() {
		return USER_ID;
	}

	public void setUserId(String uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getDistinguishedName() {
		return DISTINGUISHED_NAME;
	}

	public void setDistinguishedName(String dISTINGUISHED_NAME) {
		DISTINGUISHED_NAME = dISTINGUISHED_NAME;
	}

	public String getLdapserverurl() {
		return LDAPSERVERURL;
	}

	public void setLdapserverurl(String lDAPSERVERURL) {
		LDAPSERVERURL = lDAPSERVERURL;
	}

	public String getRootcontext() {
		return ROOTCONTEXT;
	}

	public void setRootcontext(String rOOTCONTEXT) {
		ROOTCONTEXT = rOOTCONTEXT;
	}

	public String getRootdn() {
		return ROOTDN;
	}

	public void setRootdn(String rOOTDN) {
		ROOTDN = rOOTDN;
	}

	public String getRootpass() {
		return ROOTPASS;
	}

	public void setRootpass(String rOOTPASS) {
		ROOTPASS = rOOTPASS;
	}

	public String getLdapConnection() {
		return LDAP_CONNECTION;
	}

	public void setLdapConnection(String ldapconnection) {
		LDAP_CONNECTION = ldapconnection;
	}

	/* Added on 15-3-2017 */
	public String getTallyCompanyWsdlFile() {
		return TALLY_COMPANY_WSDL_FILE;
	}

	public void setTallyCompanyWsdlFile(String tallyCompanyWsdlFile) {
		TALLY_COMPANY_WSDL_FILE = tallyCompanyWsdlFile;
	}

	public String getTallyServerPort() {
		return TALLY_SERVER_PORT;
	}

	public void setTallyServerPort(String tallyServerPort) {
		TALLY_SERVER_PORT = tallyServerPort;
	}

	public String getTallyResponseFilePath() {
		return TALLY_RESPONSE_FILE_PATH;
	}

	public void setTallyResponseFilePath(String tallyResponseFilePath) {
		TALLY_RESPONSE_FILE_PATH = tallyResponseFilePath;
	}

	public String getTallyServerIp() {
		return TALLY_SERVER_IP;
	}

	public void setTallyServerIp(String tallyServerIp) {
		TALLY_SERVER_IP = tallyServerIp;
	}

	public String getTallyCompanyName() {
		return TALLY_COMPANY_NAME;
	}

	public void setTallyCompanyName(String tallyCompanyName) {
		TALLY_COMPANY_NAME = tallyCompanyName;
	}
	
	public String getCustAppMode() {
		return CUST_APP_MODE;
	}

	public void setCustAppMode(String custAppMode) {
		CUST_APP_MODE = custAppMode;
	}
	

}
