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

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface to instruct objects on how to maintain data relevant to a PlacementRequest during its SpotLink lifetime.
 *
 * @author <a href="mailto:matt@thistech.com">Matt Narrell</a>
 *         Created on: 5/10/11
 */
public interface RequestContext extends Serializable{

    /**
     * @return The message id of the originating message.
     */
    String getOriginatorMessageId();

    /**
     * @return The identity attribute of the originating message.
     */
    String getOriginatorIdentity();

    String getOriginatorSystem();

    RequestContext setOriginatorSystem(String originatorSystem);

    /**
     * @return The {@link Date} of the incoming PlacementRequest.
     */
    Date getRequestTime();

    /**
     * @return An identifier of this PlacementRequest.
     */
    String getId();

    /**
     * Puts an arbitrary object into the attribute map.
     *
     * @param key   - The key of the {@link Map.Entry}
     * @param value - The value of the {@link Map.Entry}
     * @return - This {@code RequestContext} implementation.
     */
    RequestContext addAttribute(String key, Object value);

    /**
     * @return The entire map of request attributes.
     */
    Map<String, Object> getAttributes();

    /**
     * Adds a populated {@link BasicTrackingEvents} object to the {@code RequestContext} implementation.
     *
     * @param trackingEvents - The populated {@link BasicTrackingEvents}
     * @return - This {@code RequestContext} implementation.
     */
    RequestContext addTrackingData(TrackingEvents trackingEvents);

    /**
     * @return - The assigned {@link List} of {@link BasicTrackingEvents}.
     */
    List<TrackingEvents> getTrackingData();

    /**
     * Allows for overriding the entire attribute map.
     *
     * @param attributes - The attribute map to replace.
     * @return - This {@code RequestContext} implementation.
     */
    RequestContext setAttributes(Map<String, Object> attributes);

    /**
     * Allows for overriding of the entire {@link BasicTrackingEvents} List.
     *
     * @param trackingData - List of {@link BasicTrackingEvents} to replace.
     * @return - This {@code RequestContext} implementation.
     */
    RequestContext setTrackingData(List<TrackingEvents> trackingData);

    /**
     * Setter
     *
     * @param originatorMessageId - The message id value.
     * @return - This {@code RequestContext} implementation.
     */
    RequestContext setOriginatorMessageId(String originatorMessageId);

    /**
     * Setter
     *
     * @param originatorIdentity - The identity value.
     * @return - This {@code RequestContext} implementation.
     */
    RequestContext setOriginatorIdentity(String originatorIdentity);

    List<String> getPreRolls();
    List<String> getMidRolls();
    List<String> getPostRolls();

    RequestContext setPreRolls(List<String> preRolls);
    RequestContext setMidRolls(List<String> midRolls);
    RequestContext setPostRolls(List<String> postRolls);

    RequestContext addPreRoll(String url);
    RequestContext addMidRoll(String url);
    RequestContext addPostRoll(String url);
}
