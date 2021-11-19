package com.helixbeat.provider.dataservice;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
  
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "run")
public class Runs {
  
    @XmlElement(name="run")
    private ArrayList<Run> runs;
  
    public ArrayList<Run> getRuns() {
        return runs;
    }
  
    public void setRuns(ArrayList<Run> runs) {
        this.runs = runs;
    }
}