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

import com.thistech.spotlink.engine.TestableDecisionEngine;
import com.thistech.spotlink.model.TrackingEvents;
import com.thistech.spotlink.persistence.ITrackingEventsDao;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;

public class TrackingServiceTest {

    @InjectMocks
    private TrackingService trackingService = null;
    @Mock
    private ITrackingEventsDao mockTrackingEventsDao = null;
    @Mock
    private HttpClient mockHttpClient = Mockito.mock(DefaultHttpClient.class, Mockito.RETURNS_DEEP_STUBS);

    @BeforeTest
    public void setupTest() {
        this.trackingService = new TrackingService();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTrackEvent() throws Exception {
        TrackingEvents trackingEvents = TestableDecisionEngine.buildTrackingEvents();

        Mockito.when(this.mockTrackingEventsDao.get(Mockito.anyString())).thenReturn(trackingEvents);
        Mockito.when(this.mockHttpClient.execute(Mockito.<HttpUriRequest>any()))
                .thenReturn(this.prepareResponse(HttpStatus.SC_OK, "someBody"));

        this.trackingService.trackEvent(trackingEvents.getId(), "event_0");
    }

    @Test
    public void testBadHttpResponse() throws Exception {
        TrackingEvents trackingEvents = TestableDecisionEngine.buildTrackingEvents();

        Mockito.when(this.mockTrackingEventsDao.get(Mockito.anyString())).thenReturn(trackingEvents);
        Mockito.when(this.mockHttpClient.execute(Mockito.<HttpUriRequest>any()))
                .thenReturn(this.prepareResponse(HttpStatus.SC_BAD_REQUEST, "BadRequest"));

        this.trackingService.trackEvent(trackingEvents.getId(), "event_0");
    }

    private HttpResponse prepareResponse(int expectedResponseStatus, String expectedResponseBody) {
        HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
                new ProtocolVersion("HTTP", 1, 1), expectedResponseStatus, ""));
        response.setStatusCode(expectedResponseStatus);
        try {
            response.setEntity(new StringEntity(expectedResponseBody));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        return response;
    }
}
