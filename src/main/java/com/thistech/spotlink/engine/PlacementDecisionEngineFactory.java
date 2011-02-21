package com.thistech.spotlink.engine;

import com.thistech.spotlink.SpotLinkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author <a href="mailto:matt@thistech.com">Matt Narrell</a>
 *         Created on: 2/18/11
 */
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
