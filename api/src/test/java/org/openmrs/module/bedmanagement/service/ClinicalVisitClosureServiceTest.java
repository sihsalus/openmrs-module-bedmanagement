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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.context.Context;
import org.openmrs.module.bedmanagement.BedDetails;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class ClinicalVisitClosureServiceTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private ClinicalVisitClosureService clinicalVisitClosureService;

	@Autowired
	private BedManagementService bedManagementService;

	@Before
	public void setUp() throws Exception {
		executeDataSet("testPatientsDataset.xml");
		executeDataSet("bedManagementDAOComponentTestDataset.xml");
		executeDataSet("visitClosurePrivilegesDataset.xml");
	}

	@Test
	public void shouldCloseVisitWithoutExposingGlobalPropertiesOrBedPrivileges() {
		Context.authenticate("normal-user", "normal-password");
		assertFalse(Context.hasPrivilege(PrivilegeConstants.GET_GLOBAL_PROPERTIES));

		Visit visit = Context.getVisitService().getVisit(1001);
		Patient patient = visit.getPatient();
		clinicalVisitClosureService.endVisit(visit.getUuid(), new Date());

		assertFalse(Context.hasPrivilege(PrivilegeConstants.GET_GLOBAL_PROPERTIES));
		Context.authenticate("test-user", "test");
		BedDetails updatedBedDetails = bedManagementService.getBedAssignmentDetailsByPatient(patient);
		assertThat(updatedBedDetails, is(nullValue()));
	}

	@Test(expected = APIAuthenticationException.class)
	public void shouldRejectUsersWithoutClinicalVisitPrivileges() {
		Context.authenticate("test-user", "test");
		clinicalVisitClosureService.endVisit("12345678-6b78-11e0-93c3-18a905e044dc", new Date());
	}
}
