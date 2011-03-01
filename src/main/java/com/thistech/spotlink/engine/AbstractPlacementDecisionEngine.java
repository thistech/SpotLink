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

import com.thistech.schemasupport.scte130.builder.adm.PlacementBuilder;
import com.thistech.schemasupport.scte130.builder.adm.PlacementDecisionBuilder;
import com.thistech.schemasupport.scte130.builder.core.ContentBuilder;
import com.thistech.spotlink.SpotLinkException;
import com.thistech.spotlink.model.Ad;
import com.thistech.spotlink.model.MediaFile;
import com.thistech.spotlink.model.TrackingEvents;
import com.thistech.spotlink.util.ListUtil;
import com.thistech.spotlink.util.XmlUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.scte.schemas._130_2._2008a.core.ContentType;
import org.scte.schemas._130_3._2008a.adm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class AbstractPlacementDecisionEngine implements PlacementDecisionEngine {
    private static final Logger log = LoggerFactory.getLogger(AbstractPlacementDecisionEngine.class);

    @Resource(name = "trackingEngine")
    protected TrackingEngine trackingEngine = null;
    @Resource(name = "com.thistech.spotlink.JAXBContext")
    private JAXBContext jaxbContext;
    protected Properties properties;

    protected AbstractPlacementDecisionEngine(Properties props) {
        this.properties = props;
    }

    /**
     * Get the endpoint uri
     * @return The endpoint uri
     */
    public abstract URI getEndpoint();

    /**
     * Get the response as a raw string
     * @param entity The HttpEntity
     * @return The response as a String
     */
    protected String getStringResponse(HttpEntity entity) {
        if (entity == null) { return null; }
        try {
            InputStream stream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            stream.close();
            entity.consumeContent(); // release everything
            return sb.toString();
        }
        catch (IOException ioe) {
        }
        return null;
    }

    /**
     * Build a Content from an Ad (for the PlacementResponse)
     * @param placementRequest The original PlacementRequest
     * @param ad The Ad
     * @return A Content
     */
    protected ContentType buildContent(PlacementRequestType placementRequest, Ad ad) {
        if (ad == null || ad.getMediaFiles().isEmpty()) {
            return null;
        }
        MediaFile mediaFile = ad.getMediaFiles().get(0);
        String providerId = StringUtils.substringBeforeLast(mediaFile.getUrl(), "/");
        String assetId = StringUtils.substringAfterLast(mediaFile.getUrl(), "/");

        return new ContentBuilder()
                .withTracking(ad.getTrackingEvents().getId())
                .withAssetRef(providerId, assetId)
                .build();
    }

    /**
     * Build an HttpPost with the
     * @param body The body
     * @return The HttpPost
     */
    protected HttpPost buildHttpPost(Object body) {
        HttpPost post = new HttpPost(getEndpoint());
        post.setHeader("Content-type", "application/xml");
        post.setHeader("accept", "application/xml");

        try {
            if (!(body instanceof String)) {
                body = XmlUtil.marshalToString(this.getJaxbContext(), body);
            }
            // freewheel requests need namespace removed
            body = StringUtils.replace((String) body, "fwns:", "");
            log.info(String.format("request body: %s", body));
            StringEntity bodyEntity = new StringEntity((String) body);
            bodyEntity.setContentType("text/xml");
            post.setEntity(bodyEntity);
            return post;
        }
        catch (Exception e) {
            throw new SpotLinkException(e);
        }
    }

    /**
     * Build a Placement that will be included in the PlacementResponse
     * @param placementRequest The original PlacementRequest
     * @param placementControl The corresponding PlacementControl
     * @param ad the Ad (may be null)
     * @return A Placement
     */
    protected PlacementType buildPlacement(PlacementRequestType placementRequest, PlacementControlType placementControl, Ad ad) {
        PlacementBuilder builder = new PlacementBuilder(placementControl);
        if (ad != null) {
            builder.withContent(buildContent(placementRequest, ad));
        }
        return builder.build();
    }

    /**
     * Fill a single PlacementOpportunity, removing Ads from the list of ads as they are used. <br/>
     * @param placementRequest The original PlacementRequest.
     * @param placementOpportunity The PlacementOpportunity to fill.
     * @param ads The list of Ads.
     * @return The PlacementsDecision
     */
    protected PlacementDecisionType fillPlacementOpportunity(PlacementRequestType placementRequest, PlacementOpportunityType placementOpportunity, List<Ad> ads) {
        PlacementDecisionBuilder placementDecisionBuilder = new PlacementDecisionBuilder(placementOpportunity);

        for (PlacementControlType placementControl : placementOpportunity.getPlacementControl()) {
            Ad ad = null;
            if (!StringUtils.equalsIgnoreCase(placementControl.getAction(), "delete")) {
                ad = (Ad) ListUtil.pop(ads);
            }
            if (ad != null) {
                saveTrackingEvents(ad.getTrackingEvents());
            }
            placementDecisionBuilder.withPlacement(buildPlacement(placementRequest, placementControl, ad));
        }

        return placementDecisionBuilder.build();
    }

    /**
     * Construct a list of Placements to best fill the PlacementOpportunity / PlacementControl from the supplied list
     * of PlacementDecisions
     * @param placementRequest The PlacementRequest
     * @param ads The list of Ads
     * @return The Placements
     */
    protected List<PlacementDecisionType> fillPlacementRequest(PlacementRequestType placementRequest, List<Ad> ads) {
        List<PlacementDecisionType> placementDecisions = new ArrayList<PlacementDecisionType>();
        for (JAXBElement<? extends PlacementOpportunityType> element : placementRequest.getPlacementOpportunity()) {
            PlacementOpportunityType placementOpportunity = element.getValue();
            placementDecisions.add(fillPlacementOpportunity(placementRequest, placementOpportunity, ads));
        }
        return placementDecisions;
    }

    /**
     * Save a TrackingEvents
     * @param trackingEvents The TrackingEvents
     */
    protected void saveTrackingEvents(TrackingEvents trackingEvents) {
        this.trackingEngine.saveTrackingEvents(trackingEvents);
    }

    protected JAXBContext getJaxbContext() {
        return jaxbContext;
    }
}
