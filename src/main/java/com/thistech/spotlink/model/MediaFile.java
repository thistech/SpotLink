package com.thistech.spotlink.model;

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

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;
import vast.VAST;

public class MediaFile implements Serializable {

    private String assetId;
    private Integer height;
    private String providerId;
    private String type;
    private String url;
    private Integer width;

    public MediaFile() {
    }

    public MediaFile(VAST.Ad.InLine.Creatives.Creative.Linear.MediaFiles.MediaFile vastMediaFile) {
        if (vastMediaFile != null) {
            if (vastMediaFile.getHeight() != null) { this.height = vastMediaFile.getHeight().intValue(); }
            if (vastMediaFile.getWidth() != null) { this.width = vastMediaFile.getWidth().intValue(); }
            this.type = vastMediaFile.getType();
            this.url = StringUtils.trimToNull(vastMediaFile.getValue());
        }
    }

    public String getAssetId() { return assetId; }
    public MediaFile setAssetId(String value) { this.assetId = value; return this; }

    public int getHeight() { return height == null ? 0 : height; }
    public MediaFile setHeight(int value) { this.height = value; return this; }

    public String getProviderId() { return providerId; }
    public MediaFile setProviderId(String value) { this.providerId = value; return this; }

    public String getType() { return type; }
    public MediaFile setType(String value) { this.type = value; return this; }

    public String getUrl() { return url; }
    public MediaFile setUrl(String value) { this.url = value; return this; }

    public int getWidth() { return width == null ? 0 : width; }
    public MediaFile setWidth(int value) { this.width = value; return this; }
}
