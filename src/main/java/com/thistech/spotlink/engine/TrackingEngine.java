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
import org.scte.schemas._130_3._2008a.adm.PlacementStatusNotificationType;

import java.io.Serializable;

public interface TrackingEngine {

    String track(PlacementStatusNotificationType psn);

    void saveTrackingEvents(TrackingEvents trackingEvents);

    TrackingEvents getTrackingEvents(String trackingId);
}
