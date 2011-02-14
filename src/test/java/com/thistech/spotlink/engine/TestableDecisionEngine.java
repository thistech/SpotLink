package com.thistech.spotlink.engine;

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
import com.thistech.spotlink.model.TrackingEvents;
import org.apache.http.client.methods.HttpPost;
import org.scte.schemas._130_3._2008a.adm.PlacementDecisionType;
import org.scte.schemas._130_3._2008a.adm.PlacementRequestType;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TestableDecisionEngine extends AbstractPlacementDecisionEngine implements PlacementDecisionEngine {

    public final static int COLLECTION_SIZE = 6;

    public TestableDecisionEngine(URI endpoint) {
        super(endpoint);
    }

    @Override
    public List<PlacementDecisionType> getPlacementDecisions(PlacementRequestType placementRequest) {
        return this.fillPlacementRequest(placementRequest, TestableDecisionEngine.buildAdList());
    }

    public static List<Ad> buildAdList() {
        List<Ad> adList = new ArrayList<Ad>(COLLECTION_SIZE);

        for (int i = 0; i < COLLECTION_SIZE; i++) {
            Ad ad = new Ad();
            ad.setMediaFiles(TestableDecisionEngine.buildMediaFilesList());
            ad.setTrackingEvents(TestableDecisionEngine.buildTrackingEvents());
            adList.add(ad);
        }

        return adList;
    }

    public static TrackingEvents buildTrackingEvents() {
        TrackingEvents events = new TrackingEvents();
        events.setId("TrackingEvents!");

        for (int i = 0; i < COLLECTION_SIZE; i++) {
            events.addEventUrl(String.format("event_%s", i), String.format("http://www.url.com/%s", i));
        }

        return events;
    }

    public static List<MediaFile> buildMediaFilesList() {
        List<MediaFile> mediaFiles = new ArrayList<MediaFile>(COLLECTION_SIZE);

        for (int i = 0; i < COLLECTION_SIZE; i++) {
            MediaFile mediaFile = new MediaFile();
            mediaFile.setProviderId(String.format("ProviderID_%s", i));
            mediaFile.setAssetId(String.format("AssetID_%s", i));
            mediaFile.setHeight(480 + i);
            mediaFile.setWidth(640 + i);
            mediaFile.setType(String.format("Type_%s", i));
            mediaFile.setUrl(String.format("http://www.url.com/%s", i));
            mediaFiles.add(mediaFile);
        }

        return mediaFiles;
    }
}
