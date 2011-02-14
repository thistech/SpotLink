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

import java.io.Serializable;
import javax.annotation.Resource;
import com.thistech.spotlink.SpotLinkException;
import net.sf.ehcache.*;

@SuppressWarnings("unchecked")
public abstract class AbstractCacheDao<V extends Serializable> {

    @Resource(name = "com.thistech.spotlink.CacheManager")
    private CacheManager cacheManager;

    private final String cacheName;
    private boolean initialized = false;

    public AbstractCacheDao(String cacheName) {
        this.cacheName = cacheName;
    }

    public void delete(V object) {
        if (object == null) { return; }
        getCache().remove(getKey(object));
    }

    public void save(V object) {
        if (object == null) { return; }
        getCache().put(getElement(object));
    }

    protected Cache getCache() {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            throw new SpotLinkException(String.format("%s cache not available", cacheName));
        }
        if (!initialized) {
            synchronized (this) {
                initialized = true;
                initialize();
            }
        }
        return cache;
    }

    protected Element getElement(V object) {
        return new Element(getKey(object), object);
    }

    protected abstract Serializable getKey(V object);

    protected void initialize() {
    }
}
