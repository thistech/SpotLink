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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Interface to instruct objects on how to maintain data relevant to a PlacementRequest during its SpotLink lifetime.
 *
 * @author <a href="mailto:matt@thistech.com">Matt Narrell</a>
 *         Created on: 5/10/11
 */
public interface RequestContext {

    /**
     * @return The message id of the originating message.
     */
    String getOriginatorMessageId();

    /**
     * @return The identity attribute of the originating message.
     */
    String getOriginatorIdentity();

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
     * @param key - The key of the {@link Map.Entry}
     * @param value - The value of the {@link Map.Entry}
     * @return - This {@code RequestContext} implementation.
     */
    RequestContext addAttribute(String key, Object value);

    /**
     * @return The entire map of request attributes.
     */
    Map<String, Object> getAttributes();

    /**
     * Adds a populated {@link TrackingEvents} object to the {@code RequestContext} implementation.
     * @param trackingEvents  - The populated {@link TrackingEvents}
     * @return - This {@code RequestContext} implementation.
     */
    RequestContext addTrackingData(TrackingEvents trackingEvents);

    /**
     * @return - The assigned {@link List} of {@link TrackingEvents}.
     */
    List<TrackingEvents> getTrackingData();
}