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

import com.thistech.schemasupport.scte130.util.ObjectFactoryProxy;
import com.thistech.spotlink.AbstractSpotlinkTest;
import com.thistech.spotlink.SpotLinkException;
import com.thistech.spotlink.TestHelper;
import com.thistech.spotlink.model.BasicRequestContext;
import org.apache.http.client.methods.HttpPost;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.scte.schemas._130_3._2008a.adm.PlacementDecisionType;
import org.scte.schemas._130_3._2008a.adm.PlacementRequestType;
import org.scte.schemas._130_3._2008a.adm.PlacementType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;
import static org.testng.Assert.*;

public class PlacementDecisionEngineTest extends AbstractSpotlinkTest {

    // Using concrete object here to test utility methods.
    @InjectMocks
    private TestableDecisionEngine placementDecisionEngine = null;
    @Mock
    private TrackingEngine mockTrackingService = null;
    @Mock
    private JAXBContext mockJaxbContext = null;
    @Mock
    private Marshaller mockMarshaller = null;

    @BeforeTest
    public void setup() throws Exception {
        this.placementDecisionEngine = new TestableDecisionEngine(new Properties());

        initMocks(this);
        when(this.mockJaxbContext.createMarshaller()).thenReturn(this.mockMarshaller);
    }

    @Test()
    public void testGetPlacementDecisions() {
        for (int i = 0; i < TestHelper.COLLECTION_SIZE; i++) {
            this.mockTrackingService.saveTrackingEvents(null);
        }

        PlacementRequestType placementRequest =
                (PlacementRequestType) this.unmarshal(this.getClass(), "/sample_placement_request.xml");
        List<PlacementDecisionType> decisions =
                this.placementDecisionEngine.getPlacementDecisions(placementRequest,
                                                                   new BasicRequestContext("unitTest", "12345"));

        assertTrue(6 == decisions.size(), "Expecting 6 placement decisions");

        for (PlacementDecisionType decision : decisions) {
            assertNotNull(decision.getId());
            assertNotNull(decision.getPlacementOpportunityRef());

            List<PlacementType> placements = decision.getPlacement();
            int index = 0;
            for (PlacementType placement : placements) {
                assertEquals("be1bcf17-cc88-42fe-87d0-915edbec8b7d", placement.getContent().getTracking().getValue());
                assertEquals(String.valueOf(index), placement.getContent().getAssetRef().getAssetID());
                assertEquals("http://www.url.com", placement.getContent().getAssetRef().getProviderID());
                index++;
            }

        }
    }

    @Test
    public void testHttpHelpers() {
        PlacementRequestType placementRequest =
                (PlacementRequestType) this.unmarshal(this.getClass(), "/sample_placement_request.xml");
        HttpPost post = this.placementDecisionEngine.buildHttpPost(
                ObjectFactoryProxy.ADM.createPlacementRequest(placementRequest));
        assertNotNull(post);
        assertEquals(this.placementDecisionEngine.getEndpoint(), post.getURI());
        try {
            assertNotNull(post.getEntity().getContent());
        }
        catch (IOException e) {
            fail();
        }

        String response = this.placementDecisionEngine.getStringResponse(post.getEntity());
        assertNotNull(response);
    }

    @Test(expectedExceptions = SpotLinkException.class)
    public void testBuildHttpPostException() throws Exception {
        this.placementDecisionEngine.buildHttpPost(null);
    }

    @Test
    public void testGetStringResponseException() throws Exception {
        assertNull(this.placementDecisionEngine.getStringResponse(null));
    }

    @Test
    public void testBuildContentException() throws Exception {
        assertNull(this.placementDecisionEngine.buildContent(null, null));
    }
}
