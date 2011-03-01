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

package com.thistech.spotlink;

import com.thistech.schemasupport.scte130.builder.core.AssetRefBuilder;
import com.thistech.spotlink.model.Ad;
import com.thistech.spotlink.model.MediaFile;
import com.thistech.spotlink.model.TrackingEvents;
import org.scte.schemas._130_2._2008a.core.AssetRefType;
import org.scte.schemas._130_2._2008a.core.ContentType;
import org.scte.schemas._130_2._2008a.core.TrackingType;
import org.scte.schemas._130_3._2008a.adm.PlacementStatusEventType;
import org.scte.schemas._130_3._2008a.adm.SpotType;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {
    public final static int COLLECTION_SIZE = 6;

    public static List<Ad> buildAdList() {
        List<Ad> adList = new ArrayList<Ad>(COLLECTION_SIZE);

        for (int i = 0; i < COLLECTION_SIZE; i++) {
            Ad ad = new Ad();
            ad.setMediaFiles(buildMediaFilesList());
            ad.setTrackingEvents(buildTrackingEvents());
            adList.add(ad);
        }

        return adList;
    }

    public static TrackingEvents buildTrackingEvents() {
        TrackingEvents events = new TrackingEvents();
        events.setId("be1bcf17-cc88-42fe-87d0-915edbec8b7d");
        events.addEventUrl("endPlacement", "http://www.url.com/");

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

    public static PlacementStatusEventType buildStatusEvent() {
        TrackingType tt = new TrackingType();
        tt.setValue("12345");
        AssetRefType assetRef = new AssetRefBuilder().withAssetId("assetId").withProviderId("prov").build();
        ContentType ct = new ContentType();
        ct.setTracking(tt);
        ct.setAssetRef(assetRef);
        SpotType spot = new SpotType();
        spot.setContent(ct);
        PlacementStatusEventType event = new PlacementStatusEventType();
        event.setSpot(spot);
        return event;
    }
}
