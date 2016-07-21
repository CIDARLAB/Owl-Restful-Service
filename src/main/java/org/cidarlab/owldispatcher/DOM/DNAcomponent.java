/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.DOM;

import java.util.HashMap;
import java.util.Map;
import org.cidarlab.owldispatcher.adaptors.ReverseTranslate;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 *
 * @author prash
 */
@Service
public class DNAcomponent {
    
	public DNAcomponent(){
		
	}

    private String name;
    private ComponentType type;
    private String sequence;
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
    
    public DNAcomponent reverseTranslate(){
    
        if(this.type.equals(ComponentType.PROTEIN)){
            String geneSeq = ReverseTranslate.translate(this.sequence);
            return new DNAcomponent(this.name,ComponentType.GENE,geneSeq);
        }
        else{
            return null;
        }
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ComponentType getType() {
		return type;
	}

	public void setType(ComponentType type) {
		this.type = type;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getClothoId() {
		return clothoId;
	}

	public void setClothoId(String clothoId) {
		this.clothoId = clothoId;
	}
    
}
