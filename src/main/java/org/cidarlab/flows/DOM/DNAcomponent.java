/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.flows.DOM;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 *
 * @author prash
 */
public class DNAcomponent {
    
    @Getter 
    @Setter
    private String name;
    
    @Getter 
    @Setter
    private ComponentType type;
    
    @Getter 
    @Setter
    private String sequence;
    
    public DNAcomponent(String _name,ComponentType _type, String _sequence){
        this.name = _name;
        this.type = _type;
        this.sequence = _sequence;
    }
    
    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        obj.put("name", this.name);
        obj.put("schema", DNAcomponent.class.getCanonicalName());
        obj.put("sequence", this.sequence);
        obj.put("type", this.type.toString());
        return obj;
    }
    
    public Map getMap(){
        Map obj = new HashMap();
        obj.put("name", this.name);
        obj.put("schema", DNAcomponent.class.getCanonicalName());
        obj.put("sequence", this.sequence);
        obj.put("type", this.type.toString());
        return obj;
    }
}
