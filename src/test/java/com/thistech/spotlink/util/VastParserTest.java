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

import com.thistech.spotlink.AbstractSpotlinkTest;
import com.thistech.spotlink.model.Ad;
import com.thistech.spotlink.model.MediaFile;
import org.springframework.util.CollectionUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import vast.VAST;

import java.util.List;

public class VastParserTest extends AbstractSpotlinkTest {

    @Test
    public void testParseVast() {
        VAST vastResponse = (VAST) this.unmarshal(this.getClass(), "/sample_vast_response.xml");

        List<Ad> ads = VastParser.instance().parse(vastResponse);
        Assert.assertEquals(6, ads.size(), "Expecting 6 ads.");
        for (Ad ad : ads) {
            Assert.assertTrue(!CollectionUtils.isEmpty(ad.getMediaFiles())); // All the sample ads have media files.
            for (MediaFile mediaFile : ad.getMediaFiles()) {
                Assert.assertTrue((0 != mediaFile.getHeight()), "Expecting non-zero Height property");
                Assert.assertTrue((0 != mediaFile.getWidth()), "Expecting non-zero Width property");
                Assert.assertNotNull(mediaFile.getType(), "Expecting Type property");
                Assert.assertNotNull(mediaFile.getUrl(), "Expecting URL property");
            }
        }
    }
}
