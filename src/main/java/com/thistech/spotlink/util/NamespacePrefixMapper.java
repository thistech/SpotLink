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

import java.util.*;
import javax.xml.namespace.NamespaceContext;

public class NamespacePrefixMapper extends com.sun.xml.bind.marshaller.NamespacePrefixMapper implements NamespaceContext {

    private static final Map<String, String> NAMESPACES = new HashMap<String, String>() {{
        put("http://www.scte.org/schemas/130-3/2008a/adm", "adm");
        put("http://www.scte.org/schemas/130-3/2008a/adm/podm", "podm");
        put("http://www.scte.org/schemas/130-2/2008a/core", "core");
        put("http://www.scte.org/schemas/130-8/2010a/gis", "gis");
        put("http://www.scte.org/schemas/130-8/2010a/gis", "gis");
        put("http://www.freewheel.tv/smartAdRequest", "fwns");
    }};

    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if (NAMESPACES.containsKey(namespaceUri)) {
            return NAMESPACES.get(namespaceUri);
        }
        else {
            return suggestion;
        }
    }

    public String getNamespaceURI(String s) {
        Set<Map.Entry<String,String>> keys = NAMESPACES.entrySet();
        for (Map.Entry<String,String> entry : keys) {
            if (entry.getValue().equals(s)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getPrefix(String s) {
        return NAMESPACES.get(s);
    }

    public Iterator getPrefixes(String s) {
        List<String> prefixes = new ArrayList<String>(1);
        for (Map.Entry<String,String> entry : NAMESPACES.entrySet()) {
            if (entry.getValue().equals(s)) {
                prefixes.add(entry.getKey());
            }
        }

        return prefixes.iterator();
    }

    public Collection<String> getValues() {
        return NAMESPACES.values();
    }
}