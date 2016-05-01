/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.DOM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author prash
 */
public class Project {
    public String projectName;
    public List<String> dnacomponentIds;
    
    public Project(String projName){
        projectName = projName;
        dnacomponentIds = new ArrayList<String>();
    }
    
    public Project(String projName,List<String> ids){
        projectName = projName;
        dnacomponentIds = new ArrayList<String>(ids);
    }
    
    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        obj.put("name", this.projectName);
        obj.put("schema", Project.class.getCanonicalName());
        obj.put("dnacomponentIds", this.dnacomponentIds);
        obj.put("id", this.projectName);
        return obj;
    }
    
    public Map getMap(){
        Map map = new HashMap();
        map.put("name", this.projectName);
        map.put("schema", Project.class.getCanonicalName());
        map.put("dnacomponentIds", this.dnacomponentIds);
        map.put("id", this.projectName);
        return map;
    }
}
