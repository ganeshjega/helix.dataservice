package com.helixbeat.provider.dataservice;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.boot.json.GsonJsonParser;

import com.google.gson.JsonObject;
import com.helixbeat.provider.util.Utils;
 
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "organizations")
@Path("/organizations")
public class OrganizationResource extends AbstractResource
{
    private static Map<Integer, Organization> ORG_DB = new HashMap<>(); 
     
    @GET
    @Produces("application/json")
    @Path("/all")
    public Organizations getAllOrganizations() {
        Organizations organizations = new Organizations();
        organizations.setOrganizations(new ArrayList<>(ORG_DB.values()));
        return organizations;
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/create")
    public Response create(String str) throws URISyntaxException 
    {
System.out.println("OrganizationResource.create() Loading from payload dump -> " + new String(Utils.getBytes(Utils.getResourceAsStream("/payloads/1-request.json"))));
    	GsonJsonParser parser = new GsonJsonParser();
    	Map data = parser.parseMap(str);
    	System.out.println("OrganizationResource.createOld() " + data.get("attributes"));
    	Organization organization = new Organization();
    	organization.setId(ORG_DB.size());
    	organization.setName((String) data.get("name"));
        if (organization.getName() == null) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
        organization.setId(ORG_DB.values().size()+1);
        organization.setUri("/org-management/"+organization.getId());
        ORG_DB.put(organization.getId(), organization);
        JsonObject outputObject = new JsonObject();
        String deletedAt = null;
        outputObject.addProperty("DeletedAt", deletedAt);
        outputObject.addProperty("ID", organization.getId());
        outputObject.addProperty("UUID", "org-" + UUID.randomUUID().toString());
        outputObject.addProperty("Name", organization.getName());
        JsonObject attributesObject = new JsonObject();
        attributesObject.addProperty("description", "desc for " + organization.getName());
        attributesObject.addProperty("city", "Pune");
        outputObject.add("Attributes", attributesObject);
        outputObject.addProperty("CreatedAt", new Timestamp(System.currentTimeMillis()).toString());
        outputObject.addProperty("UpdatedAt", new Timestamp(System.currentTimeMillis()).toString());
        
        String output = outputObject.toString();
        outputObject = null;
        return Response.status(200).entity(output).build();
    }

    @POST
    @Consumes("application/json")
    public Response createOrganization(Organization organization) throws URISyntaxException 
    {
        if(organization.getName() == null || organization.getGstin() == null) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
        organization.setId(ORG_DB.values().size()+1);
        organization.setUri("/org-management/"+organization.getId());
        ORG_DB.put(organization.getId(), organization);
        return Response.status(201).contentLocation(new URI(organization.getUri())).build();
    }
 
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getOrganizationById(@PathParam("id") int id) throws URISyntaxException 
    {
        Organization organization = ORG_DB.get(id);
        if (organization == null) {
            return Response.status(404).build();
        }
        return Response
                .status(200)
                .entity(organization)
                .contentLocation(new URI("/org-management/"+id)).build();
    }
 
    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateOrganization(@PathParam("id") int id, Organization organization) throws URISyntaxException 
    {
        Organization temp = ORG_DB.get(id);
        if (organization == null) {
            return Response.status(404).build();
        }
        temp.setName(organization.getName());
        temp.setGstin(organization.getGstin());
        ORG_DB.put(temp.getId(), temp);
        return Response.status(200).entity(temp).build();
    }
 
    @DELETE
    @Path("/{id}")
    public Response deleteOrganization(@PathParam("id") int id) throws URISyntaxException {
        Organization organization = ORG_DB.get(id);
        if (organization != null) {
            ORG_DB.remove(organization.getId());
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }
     
    static
    {
        Organization org1 = new Organization();
        org1.setId(1);
        org1.setName("Apple");
        org1.setGstin("27ALSJFLEWODSS");
        org1.setUri("/org-management/1");
 
        Organization org2 = new Organization();
        org2.setId(2);
        org2.setName("IBM");
        org2.setGstin("27BLSJFLEWODSS");
        org2.setUri("/org-management/1");
         
        ORG_DB.put(org1.getId(), org1);
        ORG_DB.put(org2.getId(), org2);
    }
}