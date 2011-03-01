package com.thistech.spotlink.service;

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

import com.thistech.schemasupport.scte130.builder.adm.AdsDeregistrationAckBuilder;
import com.thistech.schemasupport.scte130.builder.adm.PlacementResponseBuilder;
import com.thistech.schemasupport.scte130.builder.adm.PlacementStatusAckBuilder;
import com.thistech.schemasupport.scte130.builder.core.ServiceCheckResponseBuilder;
import com.thistech.schemasupport.scte130.builder.core.ServiceStatusAckBuilder;
import com.thistech.spotlink.engine.PlacementDecisionEngine;
import com.thistech.spotlink.engine.TrackingEngine;
import org.scte.schemas._130_2._2008a.core.ServiceCheckRequestType;
import org.scte.schemas._130_2._2008a.core.ServiceCheckResponseType;
import org.scte.schemas._130_2._2008a.core.ServiceStatusAcknowledgementType;
import org.scte.schemas._130_2._2008a.core.ServiceStatusNotificationType;
import org.scte.schemas._130_3._2008a.adm.*;
import org.scte.wsdl._130_3._2010.ads.ADS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.util.List;
import java.util.UUID;

@WebService(serviceName = "ADSService", portName = "ADSPort", endpointInterface = "org.scte.wsdl._130_3._2010.ads.ADS", targetNamespace = "http://www.scte.org/wsdl/130-3/2010/ads", wsdlLocation = "/wsdl/ads_2010.wsdl")
@SuppressWarnings("SpringJavaAutowiringInspection")
public class AdsService implements ADS {
    private static final Logger log = LoggerFactory.getLogger(AdsService.class);

    @Resource(name = "com.thistech.spotlink.PlacementDecisionEngine")
    protected PlacementDecisionEngine placementDecisionEngine;
    @Resource(name = "trackingEngine")
    protected TrackingEngine trackingEngine = null;

    protected String identity;
    protected String system;
    protected String version;

    public AdsService(String identity, String system, String version) {
        this.identity = identity;
        this.system = system;
        this.version = version;
    }

    public ADSDeregistrationAcknowledgementType adsDeregistrationNotification(ADSDeregistrationNotificationType notification) {
        return (ADSDeregistrationAcknowledgementType) new AdsDeregistrationAckBuilder(notification)
                .withMessageId(getMessageId())
                .withIdentity(identity)
                .withSystem(system)
                .withVersion(version)
                .build();
    }

    public PlacementResponseType placementRequest(PlacementRequestType placementRequest) {
        List<PlacementDecisionType> placementDecisions = placementDecisionEngine.getPlacementDecisions(placementRequest);

        PlacementResponseBuilder placementResponseBuilder = (PlacementResponseBuilder) new PlacementResponseBuilder(placementRequest)
                .withMessageId(getMessageId())
                .withIdentity(identity)
                .withSystem(system)
                .withVersion(version);

        for (PlacementDecisionType placementDecision : placementDecisions) {
            placementResponseBuilder.withPlacementDecision(placementDecision);
        }

        return placementResponseBuilder.build();
    }

    public PlacementStatusAcknowledgementType placementStatusNotification(PlacementStatusNotificationType notification) {
        for (PlayDataType playData : notification.getPlayData()) {
            for (Object obj : playData.getEvents().getPlacementStatusEventOrSessionEventOrSystemEvent()) {
                if(obj instanceof PlacementStatusEventType) {
                    this.trackingEngine.trackEvent((PlacementStatusEventType) obj);
                }
            }
        }

        return (PlacementStatusAcknowledgementType) new PlacementStatusAckBuilder(notification)
                .withMessageId(getMessageId())
                .withIdentity(identity)
                .withSystem(system)
                .withVersion(version)
                .build();
    }

    public ServiceCheckResponseType serviceCheckRequest(ServiceCheckRequestType request) {
        return (ServiceCheckResponseType) new ServiceCheckResponseBuilder(request)
                .withMessageId(getMessageId())
                .withIdentity(identity)
                .withSystem(system)
                .withVersion(version)
                .build();
    }

    public ServiceStatusAcknowledgementType serviceStatusNotification(ServiceStatusNotificationType notification) {
        return (ServiceStatusAcknowledgementType) new ServiceStatusAckBuilder(notification)
                .withMessageId(getMessageId())
                .withIdentity(identity)
                .withSystem(system)
                .withVersion(version)
                .build();
    }

    /**
     * Generage a message id.
     * @return The message id
     */
    protected String getMessageId() {
        return UUID.randomUUID().toString();
    }
}
