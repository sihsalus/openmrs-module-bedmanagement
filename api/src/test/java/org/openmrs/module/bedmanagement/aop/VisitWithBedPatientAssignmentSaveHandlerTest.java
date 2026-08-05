package org.openmrs.module.bedmanagement.aop;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.PatientService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.bedmanagement.BedDetails;
import org.openmrs.module.bedmanagement.service.BedManagementService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class VisitWithBedPatientAssignmentSaveHandlerTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private BedManagementService bedManagementService;
	
	@Before
	public void beforeAllTests() throws Exception {
		executeDataSet("testPatientsDataset.xml");
		executeDataSet("bedManagementDAOComponentTestDataset.xml");
		executeDataSet("visitClosurePrivilegesDataset.xml");
	}
	
	@Test
	public void testBedAssignmentEndsWhenVisitEnds() {
		VisitService visitService = Context.getVisitService();
		PatientService patientService = Context.getPatientService();
		Patient patient = patientService.getPatient(1001);
		Visit visit = visitService.getVisit(1001);
		
		BedDetails bedDetails = bedManagementService.getBedAssignmentDetailsByPatient(patient);
		
		assertThat("Invalid test data, patient has no bed assigned", bedDetails, is(notNullValue()));
		Date now = new Date();
		visit.setStopDatetime(now);
		visitService.endVisit(visit, now);
		
		BedDetails updatedBedDetails = bedManagementService.getBedAssignmentDetailsByPatient(patient);
		assertThat("Bed failed to unassign when corresponding visit ends", updatedBedDetails, is(nullValue()));
	}

	@Test
	public void shouldEndVisitWithoutBedManagementPrivileges() {
		Context.authenticate("normal-user", "normal-password");

		VisitService visitService = Context.getVisitService();
		Visit visit = visitService.getVisit(1001);
		Patient patient = visit.getPatient();
		Date stopDatetime = new Date();
		Context.addProxyPrivilege(PrivilegeConstants.GET_GLOBAL_PROPERTIES);
		try {
			visitService.endVisit(visit, stopDatetime);
		}
		finally {
			Context.removeProxyPrivilege(PrivilegeConstants.GET_GLOBAL_PROPERTIES);
		}

		Context.authenticate("test-user", "test");
		BedDetails updatedBedDetails = bedManagementService.getBedAssignmentDetailsByPatient(patient);
		assertThat("Bed failed to unassign after a clinician ended the visit", updatedBedDetails, is(nullValue()));
	}

	@Test(expected = APIAuthenticationException.class)
	public void shouldStillProtectDirectBedCleanupFromUsersWithoutBedManagementPrivileges() {
		Context.authenticate("normal-user", "normal-password");

		Visit visit = Context.getVisitService().getVisit(1001);
		bedManagementService.unAssignBedsInEndedVisit(visit);
	}
}
