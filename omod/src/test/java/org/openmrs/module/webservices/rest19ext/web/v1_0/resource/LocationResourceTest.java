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
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.webservices.rest19ext.web.v1_0.resource;

import org.openmrs.module.webservices.rest19ext.web.v1_0.resource.LocationResource;
import org.junit.Before;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResourceTest;
import org.openmrs.module.webservices.rest19ext.test.Rest19ExtTestConstants;

public class LocationResourceTest extends BaseDelegatingResourceTest<LocationResource, Location> {
	
	@Before
	public void before() throws Exception {
		executeDataSet(Rest19ExtTestConstants.TEST_DATASET);
	}
	
	@Override
	public Location newObject() {
		return Context.getLocationService().getLocationByUuid(getUuidProperty());
	}
	
	@Override
	public void validateRefRepresentation() throws Exception {
		super.validateRefRepresentation();
	}
	
	@Override
	public void validateDefaultRepresentation() throws Exception {
		super.validateDefaultRepresentation();
		assertPropEquals("name", getObject().getName());
		assertPropEquals("description", getObject().getDescription());
		assertPropEquals("address1", getObject().getAddress1());
		assertPropEquals("address2", getObject().getAddress2());
		assertPropEquals("cityVillage", getObject().getCityVillage());
		assertPropEquals("stateProvince", getObject().getStateProvince());
		assertPropEquals("country", getObject().getCountry());
		assertPropEquals("postalCode", getObject().getPostalCode());
		assertPropEquals("latitude", getObject().getLatitude());
		assertPropEquals("longitude", getObject().getLongitude());
		assertPropEquals("countyDistrict", getObject().getCountyDistrict());
		assertPropEquals("address3", getObject().getAddress3());
		assertPropEquals("address4", getObject().getAddress4());
		assertPropEquals("address5", getObject().getAddress5());
		assertPropEquals("address6", getObject().getAddress6());
		assertPropPresent("tags");
		assertPropPresent("attributes");
		assertPropPresent("parentLocation");
		assertPropPresent("childLocations");
		assertPropEquals("retired", getObject().isRetired());
	}
	
	@Override
	public void validateFullRepresentation() throws Exception {
		super.validateFullRepresentation();
		assertPropEquals("name", getObject().getName());
		assertPropEquals("description", getObject().getDescription());
		assertPropEquals("address1", getObject().getAddress1());
		assertPropEquals("address2", getObject().getAddress2());
		assertPropEquals("cityVillage", getObject().getCityVillage());
		assertPropEquals("stateProvince", getObject().getStateProvince());
		assertPropEquals("country", getObject().getCountry());
		assertPropEquals("postalCode", getObject().getPostalCode());
		assertPropEquals("latitude", getObject().getLatitude());
		assertPropEquals("longitude", getObject().getLongitude());
		assertPropEquals("countyDistrict", getObject().getCountyDistrict());
		assertPropEquals("address3", getObject().getAddress3());
		assertPropEquals("address4", getObject().getAddress4());
		assertPropEquals("address5", getObject().getAddress5());
		assertPropEquals("address6", getObject().getAddress6());
		assertPropPresent("tags");
		assertPropPresent("attributes");
		assertPropPresent("parentLocation");
		assertPropPresent("childLocations");
		assertPropEquals("retired", getObject().isRetired());
		assertPropPresent("auditInfo");
	}
	
	@Override
	public String getDisplayProperty() {
		return "Mumbai - test description";
	}
	
	@Override
	public String getUuidProperty() {
		return Rest19ExtTestConstants.LOCATION_UUID;
	}
	
}
