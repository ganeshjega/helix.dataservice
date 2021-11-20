package com.helixbeat.provider.dataservice.jobs;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.helixbeat.provider.dataservice.AppConstants;

public class ProviderDataObject {
	
	protected JSONObject source = null;
	
	public ProviderDataObject(JSONObject source) {
		setSource(source);
	}
	
	public JSONObject toSasFormat() {
		JSONObject sasObject = new JSONObject();
		sasObject.put(AppConstants.KEYWORD_KEY, "helix_provider");
		sasObject.put(AppConstants.SECRET_KEY, AppConstants.DEFAULT_SECRET_KEY);
		JSONArray dataArray = new JSONArray();
		JSONArray entryArray = (JSONArray) source.get("entry");
		if (entryArray != null && entryArray.size() > 0) {
			for (int i=0;i<entryArray.size();i++) {
				JSONObject entryObject = (JSONObject) entryArray.get(i);
				JSONObject entryResourceObject = (JSONObject) entryObject.get("resource");
				JSONObject addressObject = (JSONObject) entryResourceObject.get("address");
				JSONObject helixProviderObject = new JSONObject();
				helixProviderObject.put("full_url", entryObject.get("fullUrl"));
				if (addressObject != null) {
					helixProviderObject.put("addr_city", addressObject.get("city"));
					if (addressObject.get("line") != null && ((JSONArray) addressObject.get("line")).size() > 0) {
						helixProviderObject.put("addr_line", ((JSONArray) addressObject.get("line")).get(0));
					}
					helixProviderObject.put("addr_postalcode", addressObject.get("postalCode"));
					helixProviderObject.put("addr_state", addressObject.get("state"));
					helixProviderObject.put("addr_name", addressObject.get("name"));
				}
				if (entryResourceObject.get("position") != null && ((JSONObject) entryResourceObject.get("position")).get("latitude") != null) {
					helixProviderObject.put("position_latitude", String.valueOf(((JSONObject) entryResourceObject.get("position")).get("latitude")));
				}
				if (entryResourceObject.get("position") != null && ((JSONObject) entryResourceObject.get("position")).get("longitude") != null) {
					helixProviderObject.put("position_longitude", String.valueOf(((JSONObject) entryResourceObject.get("position")).get("longitude")));
				}
				helixProviderObject.put("resource_identifier", entryResourceObject.get("id"));
				helixProviderObject.put("status", entryResourceObject.get("status"));
				
				JSONArray hoursOfOperationSourceArray = (JSONArray) entryResourceObject.get("hoursOfOperation");
				JSONArray hoursOfOperationTargetArray = new JSONArray();
				if (hoursOfOperationSourceArray != null && hoursOfOperationSourceArray.size() > 0) {
					for (int j=0;j<hoursOfOperationSourceArray.size();j++) {
						JSONObject hoursOfOperationSourceObject = (JSONObject) hoursOfOperationSourceArray.get(j);
						JSONObject hoursOfOperationTargetObject = new JSONObject();
						hoursOfOperationTargetObject.put("full_url", entryObject.get("fullUrl"));
						hoursOfOperationTargetObject.put("all_day", String.valueOf(hoursOfOperationSourceObject.get("allDay")));
						hoursOfOperationTargetObject.put("opening_time", hoursOfOperationSourceObject.get("openingTime"));
						hoursOfOperationTargetObject.put("closing_time", hoursOfOperationSourceObject.get("closingTime"));
						hoursOfOperationTargetObject.put("daysofweek","");
						JSONArray daysOfWeekArray = (JSONArray) hoursOfOperationSourceObject.get("daysOfWeek");
						if (daysOfWeekArray != null && daysOfWeekArray.size() > 0) {
							for (int k=0;k<daysOfWeekArray.size();k++) {
								hoursOfOperationTargetObject.put("daysofweek", hoursOfOperationTargetObject.get("daysofweek") + "," + daysOfWeekArray.get(k));
							}
						}
						hoursOfOperationTargetArray.add(hoursOfOperationTargetObject);
					}
				}
				helixProviderObject.put("provider_hours_of_opn", hoursOfOperationTargetArray);
				
				JSONArray providerTelecomSourceArray = (JSONArray) entryResourceObject.get("telecom");
				JSONArray providerTelecomTargetArray = new JSONArray();
				if (providerTelecomSourceArray != null && providerTelecomSourceArray.size() > 0) {
					for (int j=0;j<providerTelecomSourceArray.size();j++) {
						JSONObject providerTelecomSourceObject = (JSONObject) providerTelecomSourceArray.get(j);
						JSONObject providerTelecomTargetObject = new JSONObject();
						providerTelecomTargetObject.put("full_url", entryObject.get("fullUrl"));
						providerTelecomTargetObject.put("comm_system", providerTelecomSourceObject.get("system"));
						providerTelecomTargetObject.put("use", providerTelecomSourceObject.get("use"));
						providerTelecomTargetObject.put("value", providerTelecomSourceObject.get("value"));
						providerTelecomTargetArray.add(providerTelecomTargetObject);
					}
				}
				helixProviderObject.put("provider_telecom", providerTelecomTargetArray);
				dataArray.add(helixProviderObject);
			}
		}
		sasObject.put("data", dataArray);
		return sasObject;
	}

	public JSONObject getSource() {
		return source;
	}

	public void setSource(JSONObject source) {
		this.source = source;
	}
}