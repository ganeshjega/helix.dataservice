package com.helixbeat.provider.dataservice;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
  
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "workspaces")
public class Workspaces {
  
    @XmlElement(name="workspaces")
    private ArrayList<Workspace> workspaces;
  
    public ArrayList<Workspace> getWorkspaces() {
        return workspaces;
    }
  
    public void setWorkspaces(ArrayList<Workspace> workspaces) {
        this.workspaces = workspaces;
    }
}