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

import org.apache.commons.lang.StringUtils;

public enum Format {
    SD_480P(640, 480),
    HD_720P(1280, 720),
    HD_1080I(1920, 1080),
    HD_1080P(1920, 1080);

    private int width;
    private int height;

    Format(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Format get(String name) {
        if (StringUtils.equalsIgnoreCase(name, "1080p") || StringUtils.equalsIgnoreCase(name, "MPEG2HD")) {
            return HD_1080P;
        }
        else if (StringUtils.contains(name, "1080i")) {
            return HD_1080I;
        }
        else if (StringUtils.contains(name, "720p")) {
            return HD_720P;
        }
        else {
            return SD_480P;
        }
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }
}
