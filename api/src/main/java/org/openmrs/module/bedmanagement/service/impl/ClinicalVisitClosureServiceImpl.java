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
package org.openmrs.module.bedmanagement.service.impl;

import java.util.Date;

import org.openmrs.Visit;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.bedmanagement.service.ClinicalVisitClosureService;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ClinicalVisitClosureServiceImpl extends BaseOpenmrsService implements ClinicalVisitClosureService {

	@Override
	public Visit endVisit(String visitUuid, Date stopDatetime) {
		if (visitUuid == null || visitUuid.trim().isEmpty()) {
			throw new APIException("A visit UUID is required");
		}
		if (stopDatetime == null) {
			throw new APIException("A visit stop datetime is required");
		}

		Visit visit = Context.getVisitService().getVisitByUuid(visitUuid);
		if (visit == null) {
			throw new APIException("No visit exists with UUID " + visitUuid);
		}
		if (visit.getStopDatetime() != null) {
			return visit;
		}

		// VisitValidator reads the overlap setting through AdministrationService. That
		// implementation detail must not force clinicians to read every global property.
		boolean addGlobalPropertyProxy = !Context.hasPrivilege(PrivilegeConstants.GET_GLOBAL_PROPERTIES);
		if (addGlobalPropertyProxy) {
			Context.addProxyPrivilege(PrivilegeConstants.GET_GLOBAL_PROPERTIES);
		}

		try {
			return Context.getVisitService().endVisit(visit, stopDatetime);
		}
		finally {
			if (addGlobalPropertyProxy) {
				Context.removeProxyPrivilege(PrivilegeConstants.GET_GLOBAL_PROPERTIES);
			}
		}
	}
}
