/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.bedmanagement.rest.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.bedmanagement.ClinicalVisitClosure;
import org.openmrs.module.bedmanagement.service.ClinicalVisitClosureService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + "/clinicalvisitclosure", supportedClass = ClinicalVisitClosure.class,
        supportedOpenmrsVersions = { "2.0.* - 9.*" })
public class ClinicalVisitClosureResource extends DelegatingCrudResource<ClinicalVisitClosure> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("visitUuid");
		description.addProperty("stopDatetime");
		return description;
	}

	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addRequiredProperty("visitUuid");
		description.addRequiredProperty("stopDatetime");
		return description;
	}

	@Override
	public ClinicalVisitClosure newDelegate() {
		return new ClinicalVisitClosure();
	}

	@Override
	public ClinicalVisitClosure save(ClinicalVisitClosure closure) {
		closure.setStopDatetime(Context.getService(ClinicalVisitClosureService.class)
		        .endVisit(closure.getVisitUuid(), closure.getStopDatetime()).getStopDatetime());
		return closure;
	}

	@Override
	public ClinicalVisitClosure getByUniqueId(String uuid) {
		throw new ResourceDoesNotSupportOperationException("Clinical visit closure is write-only");
	}

	@Override
	protected void delete(ClinicalVisitClosure closure, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException("Clinical visit closure cannot be deleted");
	}

	@Override
	public void purge(ClinicalVisitClosure closure, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException("Clinical visit closure cannot be purged");
	}
}
