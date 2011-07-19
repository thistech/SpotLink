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
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import javax.annotation.Resource;
import java.util.Properties;

public class HttpClientFactory {

    @Resource(name = "com.thistech.spotlink.Properties")
    private Properties properties = null;

    public HttpClient newInstance() {
        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        if (this.properties.containsKey("httpclient.timeout")) {
            int timeout = Integer.parseInt(this.properties.getProperty("httpclient.timeout"));
            HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
            HttpConnectionParams.setSoTimeout(httpParams, timeout);
        }

        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRoute() {
            public int getMaxForRoute(HttpRoute route) {
                return Integer.parseInt(properties.getProperty("httpclient.conn-per-route", "5"));
            }
        });

        int totalConnections = Integer.parseInt(this.properties.getProperty("httpclient.total-connections", "100"));
        ConnManagerParams.setMaxTotalConnections(httpParams, totalConnections);

        String userAgent = this.properties.getProperty("httpclient.user-agent", "Mozilla/5.0");
        HttpProtocolParams.setUserAgent(httpParams, userAgent);

        String charset = this.properties.getProperty("httpclient.content-charset", "UTF-8");
        HttpProtocolParams.setContentCharset(httpParams, charset);

        ClientConnectionManager mgr = client.getConnectionManager();
        SchemeRegistry schemeRegistry = mgr.getSchemeRegistry();
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, schemeRegistry), httpParams);
        return client;
    }
}
