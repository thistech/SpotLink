package com.thistech.spotlink.job;

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

import com.thistech.schemasupport.scte130.builder.adm.*;
import com.thistech.schemasupport.scte130.builder.core.CalloutBuilder;
import com.thistech.spotlink.SpotLinkException;
import com.thistech.spotlink.client.AdmClientFactory;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.scte.schemas._130_2._2008a.core.CalloutType;
import org.scte.schemas._130_3._2008a.adm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class AdmRegistrationJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(AdmRegistrationJob.class);

    private AdmClientFactory admClientFactory;
    public void setAdmClientFactory(AdmClientFactory factory) { this.admClientFactory = factory; }

    private String[] admEndpoints;
    public void setAdmEndpoints(String value) { this.admEndpoints = StringUtils.split(value, ","); }

    private String identity;
    public void setIdentity(String value) { this.identity = value; }

    private String system;
    public void setSystem(String value) { this.system = value; }

    private String version;
    public void setVersion(String value) { this.version = value; }

    private String publicEndpoint;
    public void setPublicEndpoint(String value) { this.publicEndpoint = value; }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (admEndpoints == null || admEndpoints.length == 0) {
            log.info("No ADMs to register with (no adm.endpoints specified in spotlink.propeties)");
            return;
        }

        // we could slightly optimize the creation of ADM clients but probably not worth the effort. This process need not be particularly efficient
        for (String admEndpoint : admEndpoints) {
            ListADMServicesResponseType admServices = admClientFactory.create(admEndpoint).listADMServicesRequest(new ListAdmServicesRequestBuilder(identity, system, version).build());
            if (!isRegistered(admServices)) {
                register(admServices);
            }
        }
    }

    private String getEndpoint(ListADMServicesResponseType response, String method) {
        if (response != null) {
            for (CalloutType callout : response.getCallout()) {
                if (StringUtils.isBlank(callout.getMessage()) || StringUtils.equalsIgnoreCase(callout.getMessage(), method)) {
                    if (callout.getAddress().size() > 0) {
                        return callout.getAddress().get(0).getValue();
                    }
                }
            }
        }
        throw new SpotLinkException(String.format("Could not find endpoint for %s from ListADMServicesResponseType", method));
    }

    private void register(ListADMServicesResponseType admServices) {
        String adsRegistrationRequestEndpoint = getEndpoint(admServices, "ADSRegistrationRequest");

        log.info(String.format("Registering with %s", adsRegistrationRequestEndpoint));
        ADSRegistrationResponseType response = admClientFactory.create(adsRegistrationRequestEndpoint).adsRegistrationRequest(new AdsRegistrationRequestBuilder(identity, system, version)
                .withServiceDescriptions(admServices.getServiceDescription())
                .withCallout(new CalloutBuilder()
                        .withAddress("SOAP1.1", publicEndpoint)
                        .build())
                .build());
        log.info(String.format("Response from %s was %s", adsRegistrationRequestEndpoint, response.getStatusCode().getClazz()));
    }

    private boolean isRegistered(ListADMServicesResponseType admServices) {
        String listAdsRegistrationRequestEndpoint = getEndpoint(admServices, "ListADSRegistrationRequest");

        ListADSRegistrationResponseType admRegistrations = admClientFactory.create(listAdsRegistrationRequestEndpoint).listADSRegistrationRequest(new ListAdsRegistrationRequestBuilder(identity, system, version).build());
        for (ADSRegistrationRequestType registration : admRegistrations.getADSRegistrationRequest()) {
            if (StringUtils.equals(registration.getIdentity(), identity)) {
                return true;
            }
        }
        return false;
    }
}
