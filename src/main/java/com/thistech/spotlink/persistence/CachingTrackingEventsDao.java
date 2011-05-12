package com.thistech.spotlink.persistence;

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

import java.io.Serializable;

import com.thistech.spotlink.model.TrackingEvents;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;

public class CachingTrackingEventsDao extends AbstractCacheDao<TrackingEvents> implements TrackingEventsDao {

    public CachingTrackingEventsDao() {
        super("TrackingEvents");
    }

    public TrackingEvents get(String id) {
        Element element = getCache().get(id);
        return element == null ? null : (TrackingEvents) element.getValue();
    }

    @Override
    protected Serializable getKey(TrackingEvents object) {
        return StringUtils.upperCase(object.getId());
    }
}
