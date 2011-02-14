package com.thistech.spotlink;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStreamReader;

@ContextConfiguration
public class AbstractSpotlinkTest extends AbstractTestNGSpringContextTests {
    protected static final Logger log = LoggerFactory.getLogger(AbstractSpotlinkTest.class);

    @Autowired
    public JAXBContext jaxbContext;

    protected Object unmarshal(Class clazz, String filename) {
        try {

            Object object = this.jaxbContext.createUnmarshaller().unmarshal(
                    new StreamSource(new InputStreamReader(clazz.getResourceAsStream(filename), "UTF-8"))
            );
            if (object instanceof JAXBElement) {
                return ((JAXBElement) object).getValue();
            }
            return object;
        } catch (Exception e) {
            return null;
        }
    }
}
