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
 * Basic implementation of {@link com.thistech.spotlink.model.RequestContext}
 *
 * @author <a href="mailto:matt@thistech.com">Matt Narrell</a>
 *         Created on: 5/5/11
 */
public class BasicRequestContext implements RequestContext {

    private final Date requestTime = new Date();
    private final String id = UUID.randomUUID().toString();
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private List<TrackingEvents> trackingData = new LinkedList<TrackingEvents>();
    private String originatorMessageId = null;
    private String originatorIdentity = null;

    /**
     * @see com.thistech.spotlink.model.RequestContext#getOriginatorMessageId()
     */
    @Override
    public String getOriginatorMessageId() {
        return this.originatorMessageId;
    }

    /**
     * @see com.thistech.spotlink.model.RequestContext#getOriginatorIdentity()
     */
    @Override
    public String getOriginatorIdentity() {
        return this.originatorIdentity;
    }

    /**
     * @see com.thistech.spotlink.model.RequestContext#setAttributes(java.util.Map)
     */
    @Override
    public RequestContext setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    /**
     * @see com.thistech.spotlink.model.RequestContext#setTrackingData(java.util.List)
     */
    @Override
    public RequestContext setTrackingData(List<TrackingEvents> trackingData) {
        this.trackingData = trackingData;
        return this;
    }

    /**
     * @see com.thistech.spotlink.model.RequestContext#setOriginatorMessageId(String)
     */
    @Override
    public RequestContext setOriginatorMessageId(String originatorMessageId) {
        this.originatorMessageId = originatorMessageId;
        return this;
    }

    /**
     * @see com.thistech.spotlink.model.RequestContext#setOriginatorIdentity(String)
     */
    @Override
    public RequestContext setOriginatorIdentity(String originatorIdentity) {
        this.originatorIdentity = originatorIdentity;
        return this;
    }

    /**
     * @see com.thistech.spotlink.model.RequestContext#addTrackingData(TrackingEvents)
     */
    @Override
    public RequestContext addTrackingData(TrackingEvents trackingEvents) {
        this.trackingData.add(trackingEvents);
        return this;
    }

    /**
     * @see com.thistech.spotlink.model.RequestContext#getTrackingData()
     */
    @Override
    public List<TrackingEvents> getTrackingData() {
        return trackingData;
    }

    /**
     * @see com.thistech.spotlink.model.RequestContext#getRequestTime()
     */
    @Override
    public Date getRequestTime() {
        return this.requestTime;
    }

    /**
     * @see com.thistech.spotlink.model.RequestContext#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * @see com.thistech.spotlink.model.RequestContext#addAttribute(String, Object)
     */
    @Override
    public RequestContext addAttribute(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    /**
     * @see com.thistech.spotlink.model.RequestContext#getAttributes()
     */
    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}
