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

import com.thistech.spotlink.model.Ad;
import com.thistech.spotlink.model.MediaFile;
import org.apache.commons.lang.StringUtils;
import vast.ImpressionType;
import vast.TrackingEventsType;
import vast.VAST;

import java.util.ArrayList;
import java.util.List;

public class VastParser {
    private static VastParser instance;

    protected VastParser() {
    }

    public static VastParser instance() {
        if (instance == null) {
            instance = new VastParser();
        }
        return instance;
    }

    public List<Ad> parse(VAST vast) {
        List<Ad> ads = new ArrayList<Ad>();

        for (int i = 0; i < vast.getAd().size(); i++) {
            VAST.Ad vastAd = vast.getAd().get(i);
            Ad ad = getAd(ads, i);

            if (vastAd.getInLine() != null) {
                if (vastAd.getInLine().getCreatives() != null) {
                    TrackingEventsType trackingEvents = null;
                    for (VAST.Ad.InLine.Creatives.Creative creative : vastAd.getInLine().getCreatives().getCreative()) {
                        if (creative.getLinear() != null) {
                            if (creative.getLinear().getMediaFiles() != null) {
                                for (VAST.Ad.InLine.Creatives.Creative.Linear.MediaFiles.MediaFile vastMediaFile : creative.getLinear().getMediaFiles().getMediaFile()) {
                                    ad.getMediaFiles().add(new MediaFile(vastMediaFile));
                                }
                            }
                            trackingEvents = creative.getLinear().getTrackingEvents();
                        }
                    }
                    for (ImpressionType impression : vastAd.getInLine().getImpression()) {
                        ad.addTrackingEventUrl("impression", impression.getValue());
                    }
                    if (trackingEvents != null) {
                        for (TrackingEventsType.Tracking tracking : trackingEvents.getTracking()) {
                            ad.addTrackingEventUrl(tracking.getEvent(), StringUtils.trimToNull(tracking.getValue()));
                        }
                    }
                }
            }
        }

        return ads;
    }

    protected Ad getAd(List<Ad> ads, int i) {
        if (ads == null) {
            return null;
        }
        while (ads.size() <= i) {
            ads.add(new Ad());
        }
        return ads.get(i);
    }

}
