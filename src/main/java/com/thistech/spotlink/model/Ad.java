package com.thistech.spotlink.model;

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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class Ad implements Serializable {
    private List<MediaFile> mediaFiles;
    private TrackingEvents trackingEvents;

    public List<MediaFile> getMediaFiles() {
        if (mediaFiles == null) { mediaFiles = new ArrayList<MediaFile>(); }
        return mediaFiles;
    }
    public Ad setMediaFiles(List<MediaFile> value) { this.mediaFiles = value; return this; }

    public TrackingEvents getTrackingEvents() {
        if (trackingEvents == null) { trackingEvents = new BasicTrackingEvents(); }
        return trackingEvents;
    }
    public Ad setTrackingEvents(TrackingEvents value) { this.trackingEvents = value; return this; }

    public Ad addTrackingEventUrl(String event, String url) {
        if (StringUtils.isNotEmpty(event) && StringUtils.isNotEmpty(url)) {
            getTrackingEvents().getEventUrls().put(StringUtils.trimToNull(event), StringUtils.trimToNull(url));
        }
        return this;
    }
}
