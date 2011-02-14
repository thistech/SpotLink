package com.thistech.spotlink.util;

/*
 * “The contents of this file are subject to the SpotLink Public License,
 * version 1.0 (the “License”); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.thistech.com/spotlink/spl.
 *
 * Software distributed under the License is distributed on an “AS IS”
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.  See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is SpotLink Server Code, release date February 14, 2011
 * The Initial Developer of the Original Code is This Technology, LLC.
 * Copyright (C) 2010-2011, This Technology, LLC
 * All Rights Reserved.
 */

import java.util.*;
import org.apache.http.HttpVersion;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpotLinkHttpClient extends DefaultHttpClient {
    private static final Logger log = LoggerFactory.getLogger(SpotLinkHttpClient.class);

    public SpotLinkHttpClient() {
        super(new ConnectionManager(), new HttpParams());
    }

    static final class ConnectionManager extends ThreadSafeClientConnManager {
        ConnectionManager() {
            super(new BasicHttpParams(), new SchemeRegistry());
            getSchemeRegistry().register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        }
    }

    static final class HttpParams extends AbstractHttpParams {
        final HashMap parameters = new HashMap();

        HttpParams() {
            super();
            ConnManagerParams.setMaxConnectionsPerRoute(this, new ConnPerRoute() {
                public int getMaxForRoute(HttpRoute route) {
                    return 5;
                }
            });
            ConnManagerParams.setMaxTotalConnections(this, 100);
            HttpProtocolParams.setUserAgent(this, "Mozilla/5.0");
            HttpProtocolParams.setContentCharset(this, "UTF-8");
            HttpProtocolParams.setVersion(this, HttpVersion.HTTP_1_1);
        }

        public Object getParameter(String name) {
            return this.parameters.get(name);
        }

        public org.apache.http.params.HttpParams setParameter(String name, Object value) {
            this.parameters.put(name, value);
            return this;
        }

        public org.apache.http.params.HttpParams copy() {
            BasicHttpParams clone = new BasicHttpParams();
            copyParams(clone);
            return clone;
        }

        public boolean removeParameter(String name) {
            if (this.parameters.containsKey(name)) {
                this.parameters.remove(name);
                return true;
            }
            else {
                return false;
            }
        }

        private void copyParams(org.apache.http.params.HttpParams target) {
            Iterator iter = parameters.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry me = (Map.Entry) iter.next();
                if (me.getKey() instanceof String) { target.setParameter((String) me.getKey(), me.getValue()); }
            }
        }
    }
}
