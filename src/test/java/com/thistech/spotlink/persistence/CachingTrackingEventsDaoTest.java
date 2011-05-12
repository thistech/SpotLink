package com.thistech.spotlink.persistence;

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

import com.thistech.spotlink.model.TrackingEvents;
import com.thistech.spotlink.model.BasicTrackingEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration
public class CachingTrackingEventsDaoTest extends AbstractTestNGSpringContextTests {

    @Autowired @Qualifier("com.thistech.spotlink.TrackingEventsDao")
    private CachingTrackingEventsDao trackingEventsDao = null;

    @Test
    public void testSave() {
        int initialSize = this.trackingEventsDao.getCache().getSize();
        this.trackingEventsDao.save(this.getTrackingEvents());
        Assert.assertEquals(initialSize + 1, this.trackingEventsDao.getCache().getSize());
    }

    @Test
    public void testGet() {
        TrackingEvents events = this.getTrackingEvents();
        events.setId("FINDME");
        this.trackingEventsDao.save(events);

        TrackingEvents foundEvent = this.trackingEventsDao.get("FINDME");
        Assert.assertNotNull(foundEvent);
        Assert.assertEquals(events.getId(), foundEvent.getId());

        for(String key : events.getEventUrls().keySet())
        {
            Assert.assertEquals(events.getEventUrls().get(key), foundEvent.getEventUrls().get(key));
        }
    }

    @Test
    public void testDelete() {
        TrackingEvents events = this.getTrackingEvents();
        events.setId("FINDME");
        this.trackingEventsDao.save(events);
        int size = this.trackingEventsDao.getCache().getSize();

        this.trackingEventsDao.delete(events);
        Assert.assertEquals(size - 1, this.trackingEventsDao.getCache().getSize());

        TrackingEvents notFound = this.trackingEventsDao.get("FINDME");
        Assert.assertNull(notFound);
    }

    private TrackingEvents getTrackingEvents() {
        TrackingEvents events = new BasicTrackingEvents();
        events.setId(String.format("RandomTrackingEvents_%s", System.currentTimeMillis()));

        for (int i = 0; i < 2; i++) {
            events.addEventUrl(String.format("event_%s", i), String.format("http://www.url.com/%s", i));
        }
        return events;
    }
}
