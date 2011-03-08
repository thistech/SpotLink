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

import com.thistech.spotlink.AbstractSpotlinkTest;
import com.thistech.spotlink.TestHelper;
import com.thistech.spotlink.model.TrackingEvents;
import com.thistech.spotlink.persistence.ITrackingEventsDao;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.scte.schemas._130_3._2008a.adm.PlacementStatusNotificationType;
import org.testng.annotations.Test;

import java.util.Properties;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class TrackingEngineTest extends AbstractSpotlinkTest {

    @InjectMocks
    private TrackingEngine trackingEngine = null;
    @Mock
    private ITrackingEventsDao mockTrackingEventsDao = null;
    @Mock
    private HttpClient mockHttpClient = mock(DefaultHttpClient.class, RETURNS_DEEP_STUBS);
    @Mock
    private Properties properties = null;

    @Test
    public void testTrackPsn() throws Exception {
        this.trackingEngine = new HttpTrackingEngine(this.properties);
        initMocks(this);

        when(this.mockTrackingEventsDao.get(anyString())).thenReturn(TestHelper.buildTrackingEvents());
        when(this.mockHttpClient.execute(any(HttpUriRequest.class)))
                .thenReturn(this.prepareResponse(HttpStatus.SC_OK, "someBody"));

        PlacementStatusNotificationType psn =
                (PlacementStatusNotificationType) this.unmarshal(this.getClass(), "/sample_placement_status_notification.xml");

        this.trackingEngine.track(psn);
    }

    @Test
    public void testBadHttpResponse() throws Exception {
        this.trackingEngine = new HttpTrackingEngine(this.properties);
        initMocks(this);

        when(this.mockTrackingEventsDao.get(anyString())).thenReturn(TestHelper.buildTrackingEvents());
        when(this.mockHttpClient.execute(any(HttpUriRequest.class)))
                .thenReturn(this.prepareResponse(HttpStatus.SC_BAD_REQUEST, "BadRequest"));

        PlacementStatusNotificationType psn =
                (PlacementStatusNotificationType) this.unmarshal(this.getClass(), "/sample_placement_status_notification.xml");

        this.trackingEngine.track(psn);
    }

    @Test
    public void testTrackingNotFound() throws Exception {
        this.trackingEngine = new HttpTrackingEngine(this.properties);
        initMocks(this);

        PlacementStatusNotificationType psn =
                (PlacementStatusNotificationType) this.unmarshal(this.getClass(), "/sample_placement_status_notification.xml");

        this.trackingEngine.track(psn);
    }

    @Test
    public void testTrackingUrlNotFound() throws Exception {
        this.trackingEngine = new HttpTrackingEngine(this.properties);
        initMocks(this);

        when(this.mockTrackingEventsDao.get(anyString())).thenReturn(new TrackingEvents());
        when(this.mockHttpClient.execute(any(HttpUriRequest.class)))
                .thenReturn(this.prepareResponse(HttpStatus.SC_BAD_REQUEST, "BadRequest"));

        PlacementStatusNotificationType psn =
                (PlacementStatusNotificationType) this.unmarshal(this.getClass(), "/sample_placement_status_notification.xml");

        this.trackingEngine.track(psn);
    }

    @Test
    public void testHttpException() throws Exception {
        this.trackingEngine = new HttpTrackingEngine(this.properties);
        initMocks(this);

        when(this.mockTrackingEventsDao.get(anyString())).thenReturn(TestHelper.buildTrackingEvents());
        when(this.mockHttpClient.execute(any(HttpUriRequest.class))).thenThrow(new RuntimeException());

        PlacementStatusNotificationType psn =
                (PlacementStatusNotificationType) this.unmarshal(this.getClass(), "/sample_placement_status_notification.xml");

        this.trackingEngine.track(psn);
    }
}
