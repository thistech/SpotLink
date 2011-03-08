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

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Properties;

public abstract class AbstractCachingTrackingEngine implements TrackingEngine {
    @Resource(name = "com.thistech.spotlink.TrackingEventsDao")
    protected ITrackingEventsDao trackingEventsDao;
    protected Properties properties = null;

    public AbstractCachingTrackingEngine(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void saveTrackingEvents(Serializable trackingEvents) {
        trackingEventsDao.save((TrackingEvents) trackingEvents);
    }

    @Override
    public TrackingEvents getTrackingEvents(String trackingId) {
        return this.trackingEventsDao.get(StringUtils.upperCase(trackingId));
    }
}
