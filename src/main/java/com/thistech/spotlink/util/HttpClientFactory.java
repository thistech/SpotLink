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

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Properties;

public class HttpClientFactory {
    private static final Logger log = LoggerFactory.getLogger(HttpClientFactory.class);

    @Resource(name = "com.thistech.spotlink.Properties")
    private Properties properties = null;

    public HttpClient newInstance() {
        HttpClient client = this.instantiateClass();
        HttpParams httpParams = client.getParams();
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        if (this.properties.containsKey("httpclient.timeout")) {
            int timeout = Integer.parseInt(this.properties.getProperty("httpclient.timeout"));
            HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
            HttpConnectionParams.setSoTimeout(httpParams, timeout);
        } // defaults to implementation

        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRoute() {
            public int getMaxForRoute(HttpRoute route) {
                if (properties.containsKey("httpclient.conn-per-route")) {
                    return Integer.parseInt(properties.getProperty("httpclient.conn-per-route"));
                } else {
                    return 5;
                }
            }
        });

        int totalConnections = 100;
        if (this.properties.containsKey("httpclient.total-connections")) {
            totalConnections = Integer.parseInt(this.properties.getProperty("httpclient.total-connections"));
        }
        ConnManagerParams.setMaxTotalConnections(httpParams, totalConnections);

        String userAgent = "Mozilla/5.0";
        if (this.properties.containsKey("httpclient.user-agent")) {
            userAgent = this.properties.getProperty("httpclient.user-agent");
        }
        HttpProtocolParams.setUserAgent(httpParams, userAgent);

        String charset = "UTF-8";
        if (this.properties.containsKey("httpclient.content-charset")) {
            charset = this.properties.getProperty("httpclient.content-charset");
        }
        HttpProtocolParams.setContentCharset(httpParams, charset);

        return client;
    }

    private HttpClient instantiateClass() {
        HttpClient client;
        if (this.properties.containsKey("httpclient.classname")) {
            try {
                @SuppressWarnings("unchecked")
                Class<HttpClient> clazz =
                        (Class<HttpClient>) Class.forName(this.properties.getProperty("httpclient.classname"));
                Class[] classParams = null;
                Object[] objectParams = null;
                client = clazz.getConstructor(classParams).newInstance(objectParams);
            } catch (Exception e) {
                String warnMsg = String.format("Unable to create HttpClient from %s: %s.  " +
                        "Defaulting to org.apache.http.impl.client.DefaultHttpClient",
                        this.properties.getProperty("httpclient.classname"),
                        e.getLocalizedMessage());
                log.warn(warnMsg, e);
                client = new DefaultHttpClient();
            }

        } else {
            client = new DefaultHttpClient();
        }
        return client;
    }
}
