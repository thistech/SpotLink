package com.thistech.spotlink.model;

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

import java.util.*;

/**
 * @author <a href="mailto:matt@thistech.com">Matt Narrell</a>
 *         Created on: 5/5/11
 */
public class RequestContext {

    private final Date requestTime = new Date();
    private final UUID uuid = UUID.randomUUID();
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private List<TrackingEvents> trackingData = new LinkedList<TrackingEvents>();
    private String originatorMessageId = null;
    private String originatorIdentity = null;

    public RequestContext(String originatorIdentity, String originatorMessageId) {
        this.originatorIdentity = originatorIdentity;
        this.originatorMessageId = originatorMessageId;
    }

    public String getOriginatorMessageId() {
        return this.originatorMessageId;
    }

    public String getOriginatorIdentity() {
        return this.originatorIdentity;
    }

    public RequestContext addTrackingData(TrackingEvents trackingEvents) {
        this.trackingData.add(trackingEvents);
        return this;
    }

    public List<TrackingEvents> getTrackingData() {
        return trackingData;
    }

    public Date getRequestTime() {
        return this.requestTime;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public RequestContext addAttribute(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}
