package com.helixbeat.provider.dataservice;

import java.io.Serializable;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
  
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "organization")
public class Organization implements Serializable {
  
    private static final long serialVersionUID = 1L;
    
    private HashMap<String,String> attributes = new HashMap<String,String>();
  
    @XmlAttribute(name = "id")
    private int id;
  
    @XmlAttribute(name="uri")
    private String uri;
  
    @XmlElement(name = "name")
    private String name;
  
    @XmlElement(name = "gstin")
    private String gstin;
  
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGstin() {
		return gstin;
	}
	public void setGstin(String gstin) {
		this.gstin = gstin;
	}
	public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public void setAttribute(String name, String value) {
    	attributes.put(name, value);
    }
    
    public HashMap<String,String> getAttributes() {
    	return attributes;
    }
    
    public String getAttribute(String name) {
    	return attributes.get(name);
    }
}
