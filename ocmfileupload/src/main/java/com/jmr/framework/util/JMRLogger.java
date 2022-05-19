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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Component;
@Component
public class JMRLogger implements Serializable {
	private static final long serialVersionUID = 1L;

	public JMRLogger() {
		super();
	}

	String logPath = "";
	String strUserID;
	String strBranchCode;
	String strSessionID;
	StringBuffer strExpMsgBuff;
	private File file = null;
	private FileOutputStream fos = null;
	private BufferedOutputStream bos = null;
	private PrintWriter writer = null;
	private StringBuffer logstr = new StringBuffer();
	public static final String LINE_SEPERATOR = System
			.getProperty("line.separator");
	public static final int TraceLevel = 0;
	public static final int debugLevel = 1;
	public static final int InofLevel = 2;
	public static final int WarnLevel = 3;
	public static final int ErrorLevel = 4;
	public static final int FatalLevel = 5;
	public int currentLevel = getDebugLevel();
	private String time_log = null;
	private String strFileAbsPath = "";

	public void initialize() {
		Calendar cl = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MMM/dd");
		String dateStr = dateFormat.format(cl.getTime());
		String strYear = dateStr.substring(0, 4);
		String strMonth = dateStr.substring(5, 8);
		String strDay = dateStr.substring(9, 11);
		String fileSeparator = File.separator;
		String strDirPath = File.separator + strYear + File.separator
				+ strMonth + File.separator + strDay;
		if (getLogPath().length() == 0) {
			setLogPath("\\Logging\\Logging");
		}
		String strFinalPath = getLogPath() + strDirPath;
		File fDirs = new File(strFinalPath);
		if (!fDirs.exists()) {
			if (fDirs.mkdirs())
				;
		} else
			;
		String strFileName = getStrUserID() + "_" + getBranchCode() + "_"
				+ getStrSessionID() + ".log";
		strFileAbsPath = strFinalPath + fileSeparator + strFileName;
		createFile(strFileAbsPath);
	}

	public void createFile(String path) {
		try {
			file = new File(path);
			if (file.exists())
				;
			if (file.createNewFile())
				;
			fos = new FileOutputStream(file, true);
			bos = new BufferedOutputStream(fos);
			writer = new PrintWriter(bos, true);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	public void writeExceptionLog(Exception exp, Class cls, String message) {
		
		logstr.append(") - " + LINE_SEPERATOR);
		logstr.append(message + LINE_SEPERATOR);
		strExpMsgBuff = new StringBuffer();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = null;
		ps = new PrintStream(bos);
		exp.printStackTrace(ps);
		strExpMsgBuff.append("\n" + bos.toString());
		exception(strExpMsgBuff.toString());
		try {
			if (bos != null)
				bos.close();
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			warn(e.getMessage());
		}
	}

	public void closeStream() {
		try {
			if (fos != null) {
				fos.flush();
			}
			if (bos != null) {
				bos.flush();
			}
			if (writer != null) {
				writer.flush();
			}
			if (writer != null) {
				writer.close();
			}
			if (bos != null) {
				bos.close();
			}
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void finalize() throws Throwable {
		closeStream();
	}

	public final void debug(String strMessage) {

		if (debugLevel >= currentLevel) {
			logstr.append("DEBUG : " + strMessage + LINE_SEPERATOR);
			flush();
		}
	}

	public final void info(String strMessage) {
		if (InofLevel >= currentLevel) {
			logstr.append("INFO : " + strMessage + LINE_SEPERATOR);
			flush();
		}
	}

	public final void info(String classNm, String strMessage) {
		info(classNm + ": --> " + strMessage);
	}

	public final void info(String classNm, String mthdNm, String strMessage) {
		info(classNm + "." + mthdNm + ": --> " + strMessage);
	}

	public final void warn(String strMessage) {

		if (WarnLevel >= currentLevel) {
			logstr.append("WARN : " + strMessage + LINE_SEPERATOR);
			flush();
		}
	}

	public final void error(String strMessage) {
		if (ErrorLevel >= currentLevel) {
			logstr.append("ERROR : " + strMessage + LINE_SEPERATOR);
			flush();
		}
	}

	public final void fatal(String strMessage) {
		if (FatalLevel >= currentLevel) {
			logstr.append("FATAL : " + strMessage + LINE_SEPERATOR);
			flush();
		}
	}

	public final void fatal(String classNm, String strMessage) {
		fatal(classNm + ": --> " + strMessage);
	}

	public final void fatal(String classNm, String mthdNm, String strMessage) {
		fatal(classNm + "." + mthdNm + ": --> " + strMessage);
	}

	public final void trace(String strMessage) {
		if (TraceLevel >= currentLevel) {
			logstr.append(" TRACE : " + strMessage + LINE_SEPERATOR);
			flush();
		}
	}

	private final void exception(String strMessage) {
		logstr.append(" EXCEPTION : " + strMessage + LINE_SEPERATOR);
		flush();
	}

	public void flush() {
		try {
			createFile(strFileAbsPath);
			if (writer != null)
				;
			else
				;

			writer.println(logstr.toString());

			writer.flush();
			logstr.setLength(0);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	public String getStrUserID() {
		return strUserID;
	}

	public void setStrUserID(String strUserID) {
		this.strUserID = strUserID;
	}

	public String getStrSessionID() {
		return strSessionID;
	}

	public void setStrSessionID(String strSessionID) {
		this.strSessionID = strSessionID;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public void setBranchCode(String strBranchCode) {
		this.strBranchCode = strBranchCode;
	}

	public String getBranchCode() {
		return this.strBranchCode;
	}

	public int getDebugLevel() {
		return this.currentLevel;
	}

	public void setDebugLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

}
