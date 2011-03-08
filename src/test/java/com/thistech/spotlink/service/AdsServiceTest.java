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

import com.thistech.spotlink.AbstractSpotlinkTest;
import com.thistech.spotlink.engine.PlacementDecisionEngine;
import com.thistech.spotlink.engine.TrackingEngine;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.scte.schemas._130_2._2008a.core.ServiceCheckRequestType;
import org.scte.schemas._130_2._2008a.core.ServiceCheckResponseType;
import org.scte.schemas._130_3._2008a.adm.PlacementRequestType;
import org.scte.schemas._130_3._2008a.adm.PlacementResponseType;
import org.scte.schemas._130_3._2008a.adm.PlacementStatusAcknowledgementType;
import org.scte.schemas._130_3._2008a.adm.PlacementStatusNotificationType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBElement;
import java.io.StringReader;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class AdsServiceTest extends AbstractSpotlinkTest {

    @InjectMocks
    private AdsService adsService = null;
    @Mock
    private PlacementDecisionEngine mockPlacementDecisionEngine = null;
    @Mock
    private TrackingEngine mockTrackingEngine = null;

    @BeforeTest
    public void setupTest() {
        this.adsService = new AdsService("identity", "system", "version");
        initMocks(this);
    }

    @Test
    public void testPlacementRequest() {

        PlacementResponseType samplePlacementResponse =
                (PlacementResponseType) this.unmarshal(this.getClass(), "/sample_placement_response.xml");

        when(this.mockPlacementDecisionEngine.getPlacementDecisions(any(PlacementRequestType.class)))
                .thenReturn(samplePlacementResponse.getPlacementDecision());

        PlacementResponseType placementResponse = this.adsService.placementRequest(
                (PlacementRequestType) this.unmarshal(this.getClass(), "/sample_placement_request.xml"));

        assertNotNull(placementResponse);
        assertNotNull(placementResponse.getStatusCode());
        assertNotNull(placementResponse.getMessageRef());
        assertEquals("identity", placementResponse.getIdentity());
        assertEquals("system", placementResponse.getSystem());
        assertEquals("version", placementResponse.getVersion());
        assertEquals(6, placementResponse.getPlacementDecision().size());
//TODO:        Assert.assertNotNull(placementResponse.getADMData());
        assertNotNull(placementResponse.getClient());
        assertNotNull(placementResponse.getEntertainment());
//TODO:        Assert.assertNotNull(placementResponse.getInitiatorData());
        assertNotNull(placementResponse.getService());
        assertNotNull(placementResponse.getSystemContext());
        assertNotNull(placementResponse.getMessageId());
    }


    @Test
    public void testPlacementStatusNotification() {
        when(this.mockTrackingEngine.track(any(PlacementStatusNotificationType.class)))
                .thenReturn(UUID.randomUUID().toString());
        PlacementStatusNotificationType psn = (PlacementStatusNotificationType)
                this.unmarshal(this.getClass(), "/sample_placement_status_notification.xml");

        PlacementStatusAcknowledgementType ack = this.adsService.placementStatusNotification(psn);
        assertNotNull(ack);
        assertNotNull(ack.getIdentity());
//TODO:        Assert.assertNotNull(ack.getInitiatorData());
        assertNotNull(ack.getMessageRef());
        assertNotNull(ack.getMessageId());
        assertEquals("identity", ack.getIdentity());
        assertEquals("system", ack.getSystem());
        assertEquals("version", ack.getVersion());
    }

    @Test
    public void testadsDeregistrationNotification() throws Exception {

    }

    @Test
    public void testServiceCheckRequest() throws Exception {
        final String message = "<core:ServiceCheckRequest xmlns:core=\"http://www.scte.org/schemas/130-2/2008a/core\" messageId=\"testmessageid\" version=\"1.0\" identity=\"testidentity\" system=\"soapuitest\"/>";

        ServiceCheckRequestType serviceCheckRequest =
                (ServiceCheckRequestType) ((JAXBElement) this.jaxbContext.createUnmarshaller()
                        .unmarshal(new StringReader(message))).getValue();

        ServiceCheckResponseType serviceCheckResponse = this.adsService.serviceCheckRequest(serviceCheckRequest);
        assertNotNull(serviceCheckResponse);
        assertNotNull(serviceCheckResponse.getIdentity());
//TODO:        Assert.assertNotNull(serviceCheckResponse.getInitiatorData());
        assertNotNull(serviceCheckResponse.getMessageRef());
        assertNotNull(serviceCheckResponse.getMessageId());
        assertEquals("identity", serviceCheckResponse.getIdentity());
        assertEquals("system", serviceCheckResponse.getSystem());
        assertEquals("version", serviceCheckResponse.getVersion());
    }
}
