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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ClinicalVisitClosureResourceTest extends MainResourceControllerTest {

	private static final int VISIT_ID = 1;

	@Override
	public String getURI() {
		return "clinicalvisitclosure";
	}

	@Override
	public String getUuid() {
		return Context.getVisitService().getVisit(VISIT_ID).getUuid();
	}

	@Override
	public long getAllCount() {
		return 0;
	}

	@Test
	public void shouldCloseVisitThroughRestEndpoint() throws Exception {
		Visit visit = Context.getVisitService().getVisit(VISIT_ID);
		Date stopDatetime = new Date();

		SimpleObject payload = new SimpleObject();
		payload.put("visitUuid", visit.getUuid());
		payload.put("stopDatetime", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(stopDatetime));
		MockHttpServletRequest request = newPostRequest(getURI(), payload);
		MockHttpServletResponse response = handle(request);
		SimpleObject result = deserialize(response);

		assertThat(response.getStatus(), is(201));
		assertThat(result.get("visitUuid"), is(visit.getUuid()));
		assertThat(result.get("stopDatetime"), is(notNullValue()));
		assertThat(Context.getVisitService().getVisit(VISIT_ID).getStopDatetime(), is(notNullValue()));
	}
}
