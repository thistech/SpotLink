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

import org.apache.commons.lang.StringUtils;
import org.scte.schemas._130_3._2008a.adm.PlacementRequestType;
import org.scte.schemas._130_3._2008a.adm.TargetCode;

public class PlacementRequestUtil {

    public static String getTargetCodeValue(PlacementRequestType placementRequest, String key) {
        if (placementRequest != null && placementRequest.getClient() != null && placementRequest.getClient().getTargetCode() != null) {
            for (TargetCode targetCode : placementRequest.getClient().getTargetCode()) {
                if (StringUtils.equalsIgnoreCase(targetCode.getKey(), key)) {
                    if (targetCode.getContent() != null) {
                        return (String) targetCode.getContent().iterator().next();
                    }
                }
            }
        }
        return null;
    }
}
