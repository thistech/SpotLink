/*
 * The contents of this file are subject to the SpotLink Public License,
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

package com.thistech.spotlink.engine;

import com.thistech.spotlink.model.TrackingEvents;
import com.thistech.spotlink.persistence.TrackingEventsDao;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.scte.schemas._130_3._2008a.adm.PlacementStatusEventType;
import org.scte.schemas._130_3._2008a.adm.PlacementStatusNotificationType;
import org.scte.schemas._130_3._2008a.adm.PlayDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Properties;
import java.util.UUID;

public class HttpTrackingEngine implements TrackingEngine {
    private static final Logger log = LoggerFactory.getLogger(HttpTrackingEngine.class);

    @Resource(name = "trackingEventsDao")
    protected TrackingEventsDao trackingEventsDao = null;
    @Resource(name = "com.thistech.spotlink.HttpClient")
    protected HttpClient httpClient = null;
    protected Properties properties = null;

    public HttpTrackingEngine(Properties properties) {
        this.properties = properties;
    }

    @Override
    public final String track(PlacementStatusNotificationType psn) {
        for (PlayDataType playData : psn.getPlayData()) {
            for (Object obj : playData.getEvents().getPlacementStatusEventOrSessionEventOrSystemEvent()) {
                if (obj instanceof PlacementStatusEventType) {
                    this.trackEvent((PlacementStatusEventType) obj);
                }
            }
        }
        return UUID.randomUUID().toString();
    }

    private void trackEvent(PlacementStatusEventType event) {
        if (event.getSpot() != null
                && event.getSpot().getContent() != null
                && event.getSpot().getContent().getTracking() != null) {

            String trackingId = event.getSpot().getContent().getTracking().getValue();
            String eventType = event.getType();

            TrackingEvents tracking = this.trackingEventsDao.get(trackingId);
            if (tracking == null) {
                log.error(String.format("No Tracking data for %s", trackingId));
                return;
            }

            String url = tracking.getEventUrl(eventType);
            if (StringUtils.isEmpty(url)) {
                log.info("Tracking %s does not contain a tracking url for Event '%s'", trackingId, eventType);
                return;
            }

            HttpGet get = new HttpGet(url);
            try {
                log.info(String.format("Executing ClickTracking GET on: %s", get.getURI()));
                HttpResponse response = this.httpClient.execute(get);
                int statusCode = response.getStatusLine().getStatusCode();
                log.info(String.format("StatusCode %s for %s", statusCode, get.getURI()));
                if (HttpStatus.SC_OK != statusCode) {
                    log.error(String.format("HTTP GET failed on %s with HttpStatus %s", url, statusCode));
                }
                HttpEntity entity = response.getEntity();
                entity.consumeContent();
            }
            catch (Exception e) {
                log.error(String.format("HTTP GET on %s failed. Exception:", url), e);
            }
        }
    }
}
