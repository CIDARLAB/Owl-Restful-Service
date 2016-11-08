/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.model;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public class OwlData extends AbstractData{
    
    @Getter
    @Setter
    private Map<String,String> pigeonFilepath;
    
    
    public void addPigeonFilepath(String filename, String filepath){
        pigeonFilepath.put(filename, filepath);
    }
    
    public OwlData(){
        pigeonFilepath = new LinkedHashMap<String,String>();
        
    }
    
    
}
