/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.DOM;

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
    
    @Getter
    @Setter
    private String clothoId;
    
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
        if(this.clothoId != null){
            if(!this.clothoId.isEmpty()){
                obj.put("id", this.clothoId);
            }
        }
        return obj;
    }
    
    public Map getMap(){
        Map map = new HashMap();
        map.put("name", this.name);
        map.put("schema", DNAcomponent.class.getCanonicalName());
        map.put("sequence", this.sequence);
        map.put("type", this.type.toString());
        if(this.clothoId != null){
            if(!this.clothoId.isEmpty()){
                map.put("id", this.clothoId);
            }
        }
        return map;
    }
    
    @Override
    public String toString(){
        String str = "";
        str += "Name : " + this.name + "\n";
        str += "Sequence :\n" + this.sequence;
        return str;
    }
    
    public static DNAcomponent fromMap(Map map){        
        DNAcomponent comp = new DNAcomponent((String)map.get("name"),ComponentType.valueOf((String)map.get("type")),(String)map.get("sequence"));
        if(map.containsKey("id")){
            comp.clothoId = (String)map.get("id");
        }
        return comp;
    }
}