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

import com.thistech.spotlink.TestHelper;
import com.thistech.spotlink.model.RequestContext;
import org.scte.schemas._130_3._2008a.adm.PlacementDecisionType;
import org.scte.schemas._130_3._2008a.adm.PlacementRequestType;

import java.net.URI;
import java.util.List;
import java.util.Properties;

public class TestableDecisionEngine extends AbstractPlacementDecisionEngine implements PlacementDecisionEngine {

    @Override
    public URI getEndpoint() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TestableDecisionEngine(Properties properties) {
        super(properties);
    }

    @Override
    public List<PlacementDecisionType> getPlacementDecisions(PlacementRequestType placementRequest,
                                                             RequestContext requestContext) {
        return this.fillPlacementRequest(placementRequest, TestHelper.buildAdList());
    }

}
