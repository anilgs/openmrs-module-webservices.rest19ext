/**
 * The contents of this file are subject to the OpenMRS Public License Version
 * 1.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.webservices.rest19ext.web.v1_0.resource;

import org.junit.Before;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResourceTest;
import org.openmrs.module.webservices.rest19ext.test.Rest19ExtTestConstants;

public class LocationAttributeTypeResourceTest extends BaseDelegatingResourceTest<LocationAttributeTypeResource, LocationAttributeType> {
	
	@Before
	public void before() throws Exception {
		executeDataSet(Rest19ExtTestConstants.TEST_DATASET);
	}
	
	@Override
	public LocationAttributeType newObject() {
		return Context.getLocationService().getLocationAttributeTypeByUuid(getUuidProperty());
	}
	
	@Override
	public void validateDefaultRepresentation() throws Exception {
		super.validateDefaultRepresentation();
		assertPropEquals("name", getObject().getName());
		assertPropEquals("description", getObject().getDescription());
		assertPropEquals("datatypeClassname", getObject().getDatatypeClassname());
		assertPropEquals("preferredHandlerClassname", getObject().getPreferredHandlerClassname());
		assertPropEquals("retired", getObject().getRetired());
	}
	
	@Override
	public void validateFullRepresentation() throws Exception {
		super.validateFullRepresentation();
		assertPropEquals("name", getObject().getName());
		assertPropEquals("description", getObject().getDescription());
		assertPropEquals("minOccurs", getObject().getMinOccurs());
		assertPropEquals("maxOccurs", getObject().getMaxOccurs());
		assertPropEquals("datatypeClassname", getObject().getDatatypeClassname());
		assertPropEquals("datatypeConfig", getObject().getDatatypeConfig());
		assertPropEquals("preferredHandlerClassname", getObject().getPreferredHandlerClassname());
		assertPropEquals("handlerConfig", getObject().getHandlerConfig());
		assertPropEquals("retired", getObject().getRetired());
		assertPropPresent("auditInfo");
	}
	
	@Override
	public String getDisplayProperty() {
		return "Audit Date - ";
	}
	
	@Override
	public String getUuidProperty() {
		return Rest19ExtTestConstants.LOCATION_ATTRIBUTE_TYPE_UUID;
	}
}
