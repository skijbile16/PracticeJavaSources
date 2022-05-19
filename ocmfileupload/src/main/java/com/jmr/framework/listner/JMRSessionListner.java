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
package com.jmr.framework.listner;


import com.jmr.framework.util.JMRLogger;
import com.jmr.framework.util.UserContext;
//import com.jmr.common.util.UserLogoutStatusChange;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;

public class JMRSessionListner implements HttpSessionListener,
		HttpSessionAttributeListener {
	private HttpSession session = null;
	private String name = null;
	private int sessionCount = 0;
	private String id;
	private String message;

	public void sessionCreated(HttpSessionEvent event) {
		session = event.getSession();
		id = session.getId();
		synchronized (this) {
			sessionCount++;
		}
		message = "Session created" + "\nNew session's ID is " + id
				+ "\nLive Session count=" + sessionCount + "\n";
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		try {
			session = event.getSession();
			Object ObjectusrContext = session.getAttribute("USER_CONTEXT");
			if (ObjectusrContext == null)
				return;
			UserContext usrContext = (UserContext) ObjectusrContext;
			String userId = usrContext.getUserName();
			// UserLogoutStatusChange logout = new UserLogoutStatusChange();
		
			JMRLogger log = usrContext.getLogger();
			if (log == null)
				return;
			id = session.getId();
			synchronized (this) {
				sessionCount--;
			}
			message = "Session dest";

			if (log != null) {
				log.info(message);
				log.flush();
				log.closeStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void attributeAdded(HttpSessionBindingEvent event) {
		try {
			name = event.getName();
			session = event.getSession();
			id = session.getId();
			message = "Attribute added" + "\nSession ID: " + id
					+ "\nAttribute name: " + name + "\n";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void attributeRemoved(HttpSessionBindingEvent event) {
		try {
			name = event.getName();
			session = event.getSession();
			id = session.getId();
			message = "Attribute Removed" + "\nSession ID: " + id
					+ "\nAttribute name: " + name + "\n";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void attributeReplaced(HttpSessionBindingEvent event) {
		try {
			name = event.getName();
			session = event.getSession();

			message = "Attribute Replaced" + "\nSession ID: " + id
					+ "\nAttribute name: " + name + "\n";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
