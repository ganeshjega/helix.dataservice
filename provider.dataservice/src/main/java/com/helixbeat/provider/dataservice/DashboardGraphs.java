package com.helixbeat.provider.dataservice;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "dashboardGraphs")
public class DashboardGraphs {

	@XmlElement(name = "dashboardGraph")
	private ArrayList<DashboardGraph> dashboardGraphs;

	public ArrayList<DashboardGraph> getDashboardGraphs() {
		return dashboardGraphs;
	}

	public void setDashboardGraphs(ArrayList<DashboardGraph> dashboardGraphs) {
		this.dashboardGraphs = dashboardGraphs;
	}
}