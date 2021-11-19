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
import org.springframework.web.bind.annotation.CrossOrigin;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "dashboard")
@Path("/dashboard")
public class DashboardGraphResource extends AbstractResource {
	private static Map<Integer, DashboardGraph> DASHBOARD_GRAPH_DB = new HashMap<>();

	@GET
	@Produces("application/json")
	@Path("/all")
	@CrossOrigin(origins = "*")
	public DashboardGraphs getAllDashboardGraphs() {
		DashboardGraphs dashboardGraphs = new DashboardGraphs();
		dashboardGraphs.setDashboardGraphs(new ArrayList<>(DASHBOARD_GRAPH_DB.values()));
		return dashboardGraphs;
	}

	@GET
	@Produces("application/json")
	@Path("/labels")
	public Response getAllLabels() {
		return Response.status(200).entity(getResponseAbsolute("dashboardgraph-getAllLabels")).build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/create")
	public Response create(String str) throws URISyntaxException {
		GsonJsonParser parser = new GsonJsonParser();
		Map data = parser.parseMap(str);
		System.out.println("DashboardGraphResource.createOld() " + data);
		DashboardGraph dashboardGraph = new DashboardGraph();
		dashboardGraph.setId(DASHBOARD_GRAPH_DB.size());
		dashboardGraph.setName((String) data.get("name"));
		if (dashboardGraph.getName() == null) {
			return Response.status(400).entity("Please provide all mandatory inputs").build();
		}
		dashboardGraph.setId(DASHBOARD_GRAPH_DB.values().size() + 1);
		DASHBOARD_GRAPH_DB.put(dashboardGraph.getId(), dashboardGraph);

		String template = getResponse("dashboardGraph-create");
		String dashboardGraphUUID = UUID.randomUUID().toString();
		String jobUUID = UUID.randomUUID().toString();
		String workspaceUUID = UUID.randomUUID().toString();

		template = template.replace("$ID", Integer.toString(0));
		template = template.replace("$id", Integer.toString(26));
		template = template.replace("$UUID", dashboardGraphUUID);
		template = template.replaceAll("[$]WS_ID", Integer.toString(30));
		template = template.replace("$WS_UUID", workspaceUUID);
		template = template.replaceAll("[$]ORG_ID", Integer.toString(0));
		template = template.replaceAll("[$]ORG_TS", new Timestamp(System.currentTimeMillis()).toString());
		template = template.replaceAll("[$]WS_TS", new Timestamp(System.currentTimeMillis()).toString());
		template = template.replace("$JOB_UUID", UUID.randomUUID().toString());
		template = template.replace("$JOB_NO", Integer.toString(0));
		template = template.replaceAll("[$]RUN_TS", new Timestamp(System.currentTimeMillis()).toString());

		return Response.status(200).entity(template).build();
	}

	@POST
	@Consumes("application/json")
	public Response createDashboardGraph(DashboardGraph dashboardGraph) throws URISyntaxException {
		if (dashboardGraph.getName() == null) {
			return Response.status(400).entity("Please provide all mandatory inputs").build();
		}
		dashboardGraph.setId(DASHBOARD_GRAPH_DB.values().size() + 1);
		DASHBOARD_GRAPH_DB.put(dashboardGraph.getId(), dashboardGraph);
		return Response.status(201).contentLocation(new URI(dashboardGraph.getName())).build();
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getDashboardGraphByWorkspaceId(@PathParam("id") int id) throws URISyntaxException {

		String template = getResponse("dashboardGraph-get");
		template = template.replaceAll("[$]ORG_TS", new Timestamp(System.currentTimeMillis()).toString());
		template = template.replaceAll("[$]WS_TS", new Timestamp(System.currentTimeMillis()).toString());
		template = template.replace("$UUID", UUID.randomUUID().toString());
		template = template.replace("$WS_ID", Integer.toString(id));
		template = template.replace("$ORG_ID", Integer.toString(5));

		return Response.status(200).entity(template).contentLocation(new URI("/dashboardGraphs/" + id)).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateDashboardGraph(@PathParam("id") int id, DashboardGraph dashboardGraph)
			throws URISyntaxException {
		DashboardGraph temp = DASHBOARD_GRAPH_DB.get(id);
		if (dashboardGraph == null) {
			return Response.status(404).build();
		}
		temp.setName(dashboardGraph.getName());
		DASHBOARD_GRAPH_DB.put(temp.getId(), temp);
		return Response.status(200).entity(temp).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteDashboardGraph(@PathParam("id") int id) throws URISyntaxException {
		DashboardGraph dashboardGraph = DASHBOARD_GRAPH_DB.get(id);
		if (dashboardGraph != null) {
			DASHBOARD_GRAPH_DB.remove(dashboardGraph.getId());
			return Response.status(200).build();
		}
		return Response.status(404).build();
	}

	static {
		DashboardGraph d1 = new DashboardGraph();
		d1.setId(1);
		d1.setName("Apple");

		DashboardGraph d2 = new DashboardGraph();
		d2.setId(2);
		d2.setName("IBM");

		DASHBOARD_GRAPH_DB.put(d1.getId(), d1);
		DASHBOARD_GRAPH_DB.put(d2.getId(), d2);
	}
}