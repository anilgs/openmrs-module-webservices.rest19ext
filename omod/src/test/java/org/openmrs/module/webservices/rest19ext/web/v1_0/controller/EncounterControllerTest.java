package org.openmrs.module.webservices.rest19ext.web.v1_0.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.Visit;
import org.openmrs.api.EncounterService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.test.Util;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.v1_0.resource.ResourceTestConstants;
import org.openmrs.module.webservices.rest19ext.test.Rest19ExtTestConstants;
import org.openmrs.util.Format;
import org.openmrs.util.Format.FORMAT_TYPE;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Contains tests for the 19 ext {@link EncounterController} Overrides the failing test methods from
 * the EncounterControllerTest in the rest web services modules in order to make them pass and adds
 * tests specific to the visit property
 */
public class EncounterControllerTest extends org.openmrs.module.webservices.rest.web.v1_0.controller.EncounterControllerTest {
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.EncounterControllerTest#createEncounter_shouldCreateANewEncounterWithObs()
	 */
	@Override
	public void createEncounter_shouldCreateANewEncounterWithObs() throws Exception {
		int before = Context.getEncounterService().getAllEncounters(null).size();
		SimpleObject post = createEncounterWithObs();
		SimpleObject newEncounter = (SimpleObject) new EncounterController().create(post, new MockHttpServletRequest(),
		    new MockHttpServletResponse());
		Assert.assertNotNull(newEncounter);
		Assert.assertEquals(before + 1, Context.getEncounterService().getAllEncounters(null).size());
		
		Util.log("created encounter with obs", newEncounter);
		List<SimpleObject> obs = (List<SimpleObject>) newEncounter.get("obs");
		Assert.assertEquals(4, obs.size());
		Set<String> obsDisplayValues = new HashSet<String>();
		for (SimpleObject o : obs) {
			obsDisplayValues.add((String) o.get("display"));
		}
		Assert.assertTrue(obsDisplayValues.contains("CIVIL STATUS = MARRIED"));
		Assert.assertTrue(obsDisplayValues.contains("FAVORITE FOOD, NON-CODED = fried chicken"));
		Assert.assertTrue(obsDisplayValues.contains("WEIGHT (KG) = 70.0"));
		
		// obs.getValueAsString() uses application Locale and hence have to do this
		Calendar cal = Calendar.getInstance();
		cal.set(2011, Calendar.JUNE, 21, 0, 0, 0);
		String format = Format.format(cal.getTime(), Context.getLocale(), FORMAT_TYPE.TIMESTAMP);
		Assert.assertTrue(obsDisplayValues.contains("DATE OF FOOD ASSISTANCE = " + format));
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.EncounterControllerTest#getEncounter_shouldGetAFullRepresentationOfAEncounterIncludingObsGroups()
	 */
	@Override
	public void getEncounter_shouldGetAFullRepresentationOfAEncounterIncludingObsGroups() throws Exception {
		executeDataSet("19EncounterWithObsGroup.xml");
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addParameter(RestConstants.REQUEST_PROPERTY_FOR_REPRESENTATION, RestConstants.REPRESENTATION_FULL);
		
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		
		SimpleObject result = (SimpleObject) new EncounterController().retrieve("62967e68-96bb-11e0-8d6b-9b9415a91465", req);
		Util.log("full", result);
		Assert.assertNotNull(result);
		Assert.assertEquals("62967e68-96bb-11e0-8d6b-9b9415a91465", result.get("uuid"));
		Assert.assertNotNull(result.get("obs"));
		Assert.assertEquals("0f97e14e-cdc2-49ac-9255-b5126f8a5147", Util.getByPath(result, "obs[0]/concept/uuid"));
		Assert.assertEquals("96408258-000b-424e-af1a-403919332938", Util.getByPath(result,
		    "obs[0]/groupMembers[0]/concept/uuid"));
		Assert.assertEquals("Some text", Util.getByPath(result, "obs[0]/groupMembers[0]/value"));
		Assert.assertEquals("11716f9c-1434-4f8d-b9fc-9aa14c4d6126", Util.getByPath(result,
		    "obs[0]/groupMembers[1]/concept/uuid"));
		Assert.assertEquals(ConversionUtil.convertToRepresentation(ymd.parse("2011-06-12"), Representation.DEFAULT), Util
		        .getByPath(result, "obs[0]/groupMembers[1]/value"));
		// make sure there's a group in the group
		Assert.assertEquals("0f97e14e-cdc2-49ac-9255-b5126f8a5147", Util.getByPath(result,
		    "obs[0]/groupMembers[2]/concept/uuid"));
		Assert.assertEquals("96408258-000b-424e-af1a-403919332938", Util.getByPath(result,
		    "obs[0]/groupMembers[2]/groupMembers[0]/concept/uuid"));
		Assert.assertEquals("Some text", Util.getByPath(result, "obs[0]/groupMembers[2]/groupMembers[0]/value"));
		Assert.assertEquals("11716f9c-1434-4f8d-b9fc-9aa14c4d6126", Util.getByPath(result,
		    "obs[0]/groupMembers[2]/groupMembers[1]/concept/uuid"));
		Assert.assertEquals(ConversionUtil.convertToRepresentation(ymd.parse("2011-06-12"), Representation.DEFAULT), Util
		        .getByPath(result, "obs[0]/groupMembers[2]/groupMembers[1]/value"));
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.EncounterControllerTest#shouldCreateAnEncounterWithObsAndOrdersOfDifferentTypes()
	 */
	@Override
	public void shouldCreateAnEncounterWithObsAndOrdersOfDifferentTypes() throws Exception {
		String foodAssistanceUuid = "0dde1358-7fcf-4341-a330-f119241a46e8";
		String lunchOrderUuid = "e23733ab-787e-4096-8ba2-577a902d2c2b";
		String lunchInstructions = "Give them yummy food please";
		String triomuneConceptUuid = "d144d24f-6913-4b63-9660-a9108c2bebef";
		String triomuneDrugUuid = "3cfcf118-931c-46f7-8ff6-7b876f0d4202";
		
		int before = Context.getEncounterService().getAllEncounters(null).size();
		SimpleObject post = createEncounterWithObs();
		List<SimpleObject> orders = new ArrayList<SimpleObject>();
		orders.add(SimpleObject.parseJson("{ \"type\": \"order\", \"concept\": \"" + foodAssistanceUuid
		        + "\", \"orderType\": \"" + lunchOrderUuid + "\", \"instructions\": \"" + lunchInstructions + "\" }"));
		orders.add(SimpleObject.parseJson("{ \"type\": \"drugorder\", \"concept\": \"" + triomuneConceptUuid
		        + "\", \"drug\": \"" + triomuneDrugUuid + "\", \"dose\": \"1\", \"units\": \"tablet\" }"));
		post.add("orders", orders);
		SimpleObject newEncounter = (SimpleObject) new EncounterController().create(post, new MockHttpServletRequest(),
		    new MockHttpServletResponse());
		Assert.assertNotNull(newEncounter);
		Assert.assertEquals(before + 1, Context.getEncounterService().getAllEncounters(null).size());
		Util.log("created encounter with obs and orders", newEncounter);
		
		List<SimpleObject> newOrders = (List<SimpleObject>) newEncounter.get("orders");
		Assert.assertEquals(2, newOrders.size());
		List<String> lookFor = new ArrayList<String>(Arrays.asList("FOOD ASSISTANCE", "Triomune-30: 1.0 tablet"));
		for (SimpleObject o : newOrders) {
			lookFor.remove(o.get("display"));
		}
		Assert.assertEquals("Did not find: " + lookFor, 0, lookFor.size());
	}
	
	@Test
	public void createEncounter_shouldCreateANewEncounterWithAVisitProperty() throws Exception {
		int before = Context.getEncounterService().getAllEncounters(null).size();
		final String visitUuid = "1e5d5d48-6b78-11e0-93c3-18a905e044dc";
		String json = "{\"visit\":\""
		        + visitUuid
		        + "\",\"location\":\"9356400c-a5a2-4532-8f2b-2361b3446eb8\", \"encounterType\": \"61ae96f4-6afe-4351-b6f8-cd4fc383cce1\", \"encounterDatetime\": \"2011-01-15\", \"patient\": \"da7f524f-27ce-4bb2-86d6-6d1d05312bd5\", \"provider\":\"ba1b19c2-3ed6-4f63-b8c0-f762dc8d7562\"}";
		SimpleObject post = new ObjectMapper().readValue(json, SimpleObject.class);
		Object newEncounterObject = new EncounterController().create(post, new MockHttpServletRequest(),
		    new MockHttpServletResponse());
		Assert.assertNotNull(newEncounterObject);
		Encounter newEncounter = Context.getEncounterService().getEncounterByUuid(
		    ((SimpleObject) newEncounterObject).get("uuid").toString());
		Assert.assertEquals(before + 1, Context.getEncounterService().getAllEncounters(null).size());
		//the encounter should have been assigned to the visit
		Assert.assertNotNull(newEncounter);
		Assert.assertNotNull(newEncounter.getVisit());
		Assert.assertEquals(visitUuid, newEncounter.getVisit().getUuid());
	}
	
	@Test
	public void createEncounter_shouldEditVisitPropertyForAnExisitingEncounter() throws Exception {
		EncounterService es = Context.getEncounterService();
		VisitService vs = Context.getVisitService();
		Encounter encounter = es.getEncounterByUuid(ResourceTestConstants.ENCOUNTER_UUID);
		Visit newVisit = new Visit(encounter.getPatient(), vs.getVisitTypeByUuid(Rest19ExtTestConstants.VISIT_TYPE_UUID),
		        new SimpleDateFormat("yyyy-MM-dd").parse("2008-08-01"));
		vs.saveVisit(newVisit);
		
		String json = "{\"visit\":\"" + newVisit.getUuid() + "\"}";
		SimpleObject post = new ObjectMapper().readValue(json, SimpleObject.class);
		Object newEncounterObject = new EncounterController().update(ResourceTestConstants.ENCOUNTER_UUID, post,
		    new MockHttpServletRequest(), new MockHttpServletResponse());
		Assert.assertNotNull(newEncounterObject);
		Encounter update = es.getEncounterByUuid(ResourceTestConstants.ENCOUNTER_UUID);
		//the encounter should have been res assigned to the new visit
		Assert.assertEquals(newVisit, update.getVisit());
	}
	
	/**
	 * Copied from The EncounterControllerTest class from the rest web services module
	 * 
	 * @return
	 * @throws Exception
	 */
	private SimpleObject createEncounterWithObs() throws Exception {
		
		List<SimpleObject> obs = new ArrayList<SimpleObject>();
		// weight in kg = 70
		obs.add(SimpleObject.parseJson("{ \"concept\": \"c607c80f-1ea9-4da3-bb88-6276ce8868dd\", \"value\": 70 }"));
		// civil status = married
		obs
		        .add(SimpleObject
		                .parseJson("{ \"concept\": \"89ca642a-dab6-4f20-b712-e12ca4fc6d36\", \"value\": \"92afda7c-78c9-47bd-a841-0de0817027d4\" }"));
		// favorite food, non-coded = fried chicken
		obs.add(SimpleObject
		        .parseJson("{ \"concept\": \"96408258-000b-424e-af1a-403919332938\", \"value\": \"fried chicken\" }"));
		// date of food assistance = 2011-06-21
		obs.add(SimpleObject
		        .parseJson("{ \"concept\": \"11716f9c-1434-4f8d-b9fc-9aa14c4d6126\", \"value\": \"2011-06-21 00:00\" }"));
		
		return new SimpleObject().add("location", "9356400c-a5a2-4532-8f2b-2361b3446eb8").add("encounterType",
		    "61ae96f4-6afe-4351-b6f8-cd4fc383cce1").add("encounterDatetime", "2011-01-15").add("patient",
		    "da7f524f-27ce-4bb2-86d6-6d1d05312bd5").add("provider", "ba1b19c2-3ed6-4f63-b8c0-f762dc8d7562").add("obs", obs);
	}
	
}
