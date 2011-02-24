package com.thistech.spotlink.client;

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
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.scte.wsdl._130_3._2009.adm.ADMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Properties;

import static org.mockito.Mockito.*;

public class AdmClientFactoryTest extends AbstractSpotlinkTest {

    @InjectMocks
    private AdmClientFactory admClientFactory = null;
    @Mock
    private Properties properties = null;

    @BeforeTest
    public void setup() {
        this.admClientFactory = AdmClientFactory.instance();
        MockitoAnnotations.initMocks(this);

        when(this.properties.containsKey("cxf.service.timeout")).thenReturn(true);
        when(this.properties.getProperty("cxf.service.timeout")).thenReturn("1000");
    }

    @Test
    public void testInstantiation() {
        this.admClientFactory = AdmClientFactory.instance();
        Assert.assertNotNull(admClientFactory);
        Object admClient = admClientFactory.create("http://localhost:8080/spotlink/services/adm");
        Client client = ClientProxy.getClient(admClient);
        HTTPConduit conduit = (HTTPConduit) client.getConduit();
        HTTPClientPolicy policy = conduit.getClient();
        long propertyValue = Long.parseLong(this.properties.getProperty("cxf.service.timeout"));
        long connectionTimeout = policy.getConnectionTimeout();
        long receiveTimeout = policy.getReceiveTimeout();
        Assert.assertTrue(propertyValue == connectionTimeout &&
                          propertyValue == receiveTimeout);
    }
}
