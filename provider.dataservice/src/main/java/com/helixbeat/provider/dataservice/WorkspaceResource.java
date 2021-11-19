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
 
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "workspaces")
@Path("/workspaces")
public class WorkspaceResource extends AbstractResource
{
    private static Map<Integer, Workspace> WS_DB = new HashMap<>(); 
     
    @GET
    @Produces("application/json")
    @Path("/all")
    public Workspaces getAllWorkspaces() {
        Workspaces workspaces = new Workspaces();
        workspaces.setWorkspaces(new ArrayList<>(WS_DB.values()));
        return workspaces;
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/create")
    public Response create(String str) throws URISyntaxException 
    {
    	GsonJsonParser parser = new GsonJsonParser();
    	Map data = parser.parseMap(str);
    	System.out.println("OrganizationResource.createOld() " + data);
    	Workspace workspace = new Workspace();
    	workspace.setId(WS_DB.size());
    	workspace.setName((String) data.get("name"));
        if (workspace.getName() == null) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
        workspace.setId(WS_DB.values().size()+1);
        WS_DB.put(workspace.getId(), workspace);
        
        String template = getResponse("workspaces-create");
        template = template.replaceAll("[$]TS", new Timestamp(System.currentTimeMillis()).toString());
        template = template.replace("$UUID", UUID.randomUUID().toString());
        template = template.replace("$WS_ID", Integer.toString(workspace.getId()));
        template = template.replace("$ORG_ID", Integer.toString(5));
        
        return Response.status(200).entity(template).build();
    }

    @POST
    @Consumes("application/json")
    public Response createWorkspace(Workspace workspace) throws URISyntaxException 
    {
        if(workspace.getName() == null) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
        workspace.setId(WS_DB.values().size()+1);
        WS_DB.put(workspace.getId(), workspace);
        return Response.status(201).contentLocation(new URI(workspace.getName())).build();
    }
 
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getWorkspaceById(@PathParam("id") int id) throws URISyntaxException 
    {
    	
        String template = getResponse("workspaces-get");
        template = template.replaceAll("[$]ORG_TS", new Timestamp(System.currentTimeMillis()).toString());
        template = template.replaceAll("[$]WS_TS", new Timestamp(System.currentTimeMillis()).toString());
        template = template.replace("$UUID", UUID.randomUUID().toString());
        template = template.replace("$WS_ID", Integer.toString(id));
        template = template.replace("$ORG_ID", Integer.toString(5));
    	
        return Response
                .status(200)
                .entity(template)
                .contentLocation(new URI("/workspaces/"+id)).build();
    }
 
    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateWorkspace(@PathParam("id") int id, Workspace workspace) throws URISyntaxException 
    {
        Workspace temp = WS_DB.get(id);
        if (workspace == null) {
            return Response.status(404).build();
        }
        temp.setName(workspace.getName());
        WS_DB.put(temp.getId(), temp);
        return Response.status(200).entity(temp).build();
    }
 
    @DELETE
    @Path("/{id}")
    public Response deleteWorkspace(@PathParam("id") int id) throws URISyntaxException {
        Workspace workspace = WS_DB.get(id);
        if (workspace != null) {
            WS_DB.remove(workspace.getId());
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }
     
    static
    {
        Workspace ws1 = new Workspace();
        ws1.setId(1);
        ws1.setName("Apple");
 
        Workspace ws2 = new Workspace();
        ws2.setId(2);
        ws2.setName("IBM");
         
        WS_DB.put(ws1.getId(), ws1);
        WS_DB.put(ws2.getId(), ws2);
    }
}