package com.thistech.spotlink.engine;

import com.thistech.spotlink.model.RequestContext;
import org.scte.schemas._130_3._2008a.adm.PlacementDecisionType;
import org.scte.schemas._130_3._2008a.adm.PlacementRequestType;

import java.util.List;

/**
 * <p>User: Nick Heudecker</p>
 * <p>Date: 2/11/11</p>
 * <p>Time: 3:32 PM</p>
 */
public interface PlacementDecisionEngine {

    /**
     * Get the List of Placements for a PlacementRequest
     *
     * @param placementRequest The PlacementRequest
     * @return The Placements
     */
    List<PlacementDecisionType> getPlacementDecisions(PlacementRequestType placementRequest,
                                                      RequestContext requestContext);
}
