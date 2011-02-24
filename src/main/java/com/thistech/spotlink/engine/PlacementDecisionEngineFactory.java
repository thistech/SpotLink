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

import com.thistech.spotlink.SpotLinkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class PlacementDecisionEngineFactory {
    private static final Logger log = LoggerFactory.getLogger(PlacementDecisionEngineFactory.class);
    /**
     * Construct a new AbstractPlacementDecisionEngine
     * @param properties The engine properties
     * @return A AbstractPlacementDecisionEngine
     */
    public PlacementDecisionEngine newInstance(Properties properties) {
        String className = properties.getProperty("placementDecisionEngine");
        try {
            @SuppressWarnings("unchecked")
            Class<PlacementDecisionEngine> clazz = (Class<PlacementDecisionEngine>) Class.forName(className);
            return clazz.getConstructor(Properties.class).newInstance(properties);
        }
        catch (Exception e) {
            log.error(String.format("Could not construct instance of %s", className));
            throw new SpotLinkException(e);
        }
    }
}
