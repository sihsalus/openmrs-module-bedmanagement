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
package org.openmrs.module.bedmanagement;

import java.util.Date;

/**
 * Write-only command used by the REST layer to close a clinical visit.
 */
public class ClinicalVisitClosure {

	private String visitUuid;

	private Date stopDatetime;

	public String getVisitUuid() {
		return visitUuid;
	}

	public void setVisitUuid(String visitUuid) {
		this.visitUuid = visitUuid;
	}

	public Date getStopDatetime() {
		return stopDatetime;
	}

	public void setStopDatetime(Date stopDatetime) {
		this.stopDatetime = stopDatetime;
	}
}
