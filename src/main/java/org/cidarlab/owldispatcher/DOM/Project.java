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
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 *
 * @author prash
 */
public class Project {
    
    @Getter @Setter
    private String projectName;
    
    @Getter @Setter
    private String clothoId;
    
    @Getter @Setter
    private String projectOwner;
    
    /*
    PROTEIN,
    GENE,
    PROMOTER,
    RIBOZYME,
    RBS,
    TERMINATOR
    */
    
    @Getter @Setter
    private List<String> promoters;
    
    @Getter @Setter
    private List<String> ribozymes;
    
    @Getter @Setter
    private List<String> rbs;
    
    @Getter @Setter
    private List<String> genes;
    
    @Getter @Setter
    private List<String> terminators;
    
    
    public Project(String projName, String owner){
        this.projectName = projName;
        this.projectOwner = owner;
        this.promoters = new ArrayList<String>();
        this.ribozymes = new ArrayList<String>();
        this.rbs = new ArrayList<String>();         
        this.genes = new ArrayList<String>();
        this.terminators = new ArrayList<String>();
    }
    
    public List<String> getAllDNAcomponentIds(){
        List<String> ids = new ArrayList<String>();
        ids.addAll(this.promoters);
        ids.addAll(this.ribozymes);
        ids.addAll(this.rbs);
        ids.addAll(this.genes);
        ids.addAll(this.terminators);
        return ids;
    }
    
    public static Project fromMap(Map map){
        Project project = new Project((String)map.get("name"),(String)map.get("owner"));
        project.clothoId = (String)map.get("id");
        project.promoters = (List<String>)map.get("promoter");
        project.ribozymes = (List<String>)map.get("ribozyme");
        project.rbs = (List<String>)map.get("rbs");
        project.genes = (List<String>)map.get("gene");
        project.terminators = (List<String>)map.get("terminator");
        return project;
    }
    
    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        obj.put("name", this.projectName);
        obj.put("schema", Project.class.getCanonicalName());
        obj.put(ComponentType.PROMOTER.toString().toLowerCase(), this.promoters);
        obj.put(ComponentType.RIBOZYME.toString().toLowerCase(), this.ribozymes);
        obj.put(ComponentType.RBS.toString().toLowerCase(), this.rbs);
        obj.put(ComponentType.GENE.toString().toLowerCase(), this.genes);
        obj.put(ComponentType.TERMINATOR.toString().toLowerCase(), this.terminators);
        obj.put("owner", this.projectOwner);
        if(this.clothoId != null){
            obj.put("id", this.clothoId);
        }
        return obj;
    }
    
    public Map getMap(){
        Map map = new HashMap();
        map.put("name", this.projectName);
        map.put("schema", Project.class.getCanonicalName());
        map.put(ComponentType.PROMOTER.toString().toLowerCase(), this.promoters);
        map.put(ComponentType.RIBOZYME.toString().toLowerCase(), this.ribozymes);
        map.put(ComponentType.RBS.toString().toLowerCase(), this.rbs);
        map.put(ComponentType.GENE.toString().toLowerCase(), this.genes);
        map.put(ComponentType.TERMINATOR.toString().toLowerCase(), this.terminators);
        map.put("owner", this.projectOwner);
        if(this.clothoId != null){
            map.put("id", this.clothoId);
        }
        return map;
    }
}
