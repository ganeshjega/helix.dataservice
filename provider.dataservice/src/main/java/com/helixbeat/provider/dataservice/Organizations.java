package com.helixbeat.provider.dataservice;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
  
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "organizations")
public class Organizations {
  
    @XmlElement(name="organization")
    private ArrayList<Organization> organizations;
  
    public ArrayList<Organization> getOrganizations() {
        return organizations;
    }
  
    public void setOrganizations(ArrayList<Organization> organizations) {
        this.organizations = organizations;
    }
}