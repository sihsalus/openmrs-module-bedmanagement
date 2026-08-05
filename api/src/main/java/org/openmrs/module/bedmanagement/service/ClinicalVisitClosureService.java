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
package org.openmrs.module.bedmanagement.service;

import java.util.Date;

import org.openmrs.Visit;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.util.PrivilegeConstants;

/**
 * Closes a clinical visit without exposing global-property access to the caller.
 */
public interface ClinicalVisitClosureService extends OpenmrsService {

	@Authorized(value = { PrivilegeConstants.GET_VISITS, PrivilegeConstants.EDIT_VISITS,
	        PrivilegeConstants.GET_ENCOUNTERS, PrivilegeConstants.GET_VISIT_ATTRIBUTE_TYPES }, requireAll = true)
	Visit endVisit(String visitUuid, Date stopDatetime);
}
