package com.thistech.spotlink.util;

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
import org.scte.schemas._130_3._2008a.adm.ClientType;
import org.scte.schemas._130_3._2008a.adm.PlacementRequestType;
import org.scte.schemas._130_3._2008a.adm.TargetCode;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlacementRequestUtilTest {

    @Test
    public void testGetTargetCodeValue() {
        TargetCode targetCode = ObjectFactoryProxy.ADM.createTargetCode();
        targetCode.setKey("forty-two");
        targetCode.getContent().add("42");

        ClientType client = ObjectFactoryProxy.ADM.createClientType();
        client.getTargetCode().add(targetCode);

        PlacementRequestType placementRequest = ObjectFactoryProxy.ADM.createPlacementRequestType();
        placementRequest.setClient(client);

        Assert.assertEquals("42", PlacementRequestUtil.getTargetCodeValue(placementRequest, "forty-two"));
    }

    // Negative tests

    @Test
    public void testNullPlacementRequest() {
        Assert.assertNull(PlacementRequestUtil.getTargetCodeValue(null, "forty-two"),
                "Expecting null response when PlacementRequestType is null");
    }

    @Test
    public void testNullClient() {
        PlacementRequestType placementRequest = ObjectFactoryProxy.ADM.createPlacementRequestType();

        Assert.assertNull(PlacementRequestUtil.getTargetCodeValue(placementRequest, "forty-two"),
                "Expecting null response when ClientType is null");
    }

    @Test
    public void testNullTargetCode() {
        PlacementRequestType placementRequest = ObjectFactoryProxy.ADM.createPlacementRequestType();
        placementRequest.setClient(ObjectFactoryProxy.ADM.createClientType());

        Assert.assertNull(PlacementRequestUtil.getTargetCodeValue(placementRequest, "forty-two"),
                "Expecting null response when TargetCode is null");
    }

    @Test
    public void testNullTargetCodeContent() {
        TargetCode targetCode = ObjectFactoryProxy.ADM.createTargetCode();
        targetCode.setKey("forty-two");

        ClientType client = ObjectFactoryProxy.ADM.createClientType();
        client.getTargetCode().add(targetCode);

        PlacementRequestType placementRequest = ObjectFactoryProxy.ADM.createPlacementRequestType();
        placementRequest.setClient(client);

        Assert.assertNull(PlacementRequestUtil.getTargetCodeValue(placementRequest, "forty-two"),
                "Expecting null response when TargetCodeContent is null");
    }
}
