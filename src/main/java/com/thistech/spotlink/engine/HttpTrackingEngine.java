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
import com.thistech.spotlink.persistence.ITrackingEventsDao;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.scte.schemas._130_3._2008a.adm.EventBaseType;
import org.scte.schemas._130_3._2008a.adm.PlacementStatusEventType;
import org.scte.schemas._130_3._2008a.adm.PlacementStatusNotificationType;
import org.scte.schemas._130_3._2008a.adm.PlayDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

public class HttpTrackingEngine extends AbstractCachingTrackingEngine implements TrackingEngine {
    private static final Logger log = LoggerFactory.getLogger(HttpTrackingEngine.class);

    @Resource(name = "com.thistech.spotlink.HttpClient")
    protected HttpClient httpClient = null;

    public HttpTrackingEngine(Properties properties) {
        super(properties);
    }

    @Override
    public void trackEvent(PlacementStatusEventType event) {
        if (event.getSpot() != null
                && event.getSpot().getContent() != null
                && event.getSpot().getContent().getTracking() != null) {
            String trackingId = event.getSpot().getContent().getTracking().getValue();
            String eventType = event.getType();
            this.trackEvent(trackingId, eventType);
        }
    }

    protected void trackEvent(String trackingId, String placementStatusEventType) {
        TrackingEvents tracking = this.getTrackingEvents(trackingId);
        if (tracking == null) {
            log.error(String.format("No Tracking data for %s", trackingId));
            return;
        }

        String url = tracking.getEventUrl(placementStatusEventType);
        if (StringUtils.isEmpty(url)) {
            log.info("Tacking %s does not contain a tracking url for Event '%s'", trackingId, placementStatusEventType);
            return;
        }

        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.error(String.format("HTTP GET failed on %s with HttpStatus %s", url, response.getStatusLine().getStatusCode()));
            }
            HttpEntity entity = response.getEntity();
            entity.consumeContent();
        }
        catch (Exception e) {
            log.error(String.format("HTTP GET on %s failed. Exception:", url), e);
        }
    }
}
