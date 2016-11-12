package org.cidarlab.owldispatcher.model;

import java.util.LinkedHashMap;
import java.util.Map;
import org.cidarlab.owldispatcher.Utilities;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public class OwlData extends AbstractData{

    @Getter @Setter private Map<String,String> pigeonFilepath;
    @Getter 		private final String pathToTexFolder = Utilities.getOutputFilepath()+"tex";
    @Getter @Setter private String pathToTexFile;
    @Getter @Setter private String deviceComposition;
    @Getter @Setter private int gcContent;
    
    public void addPigeonFilepath(String filename, String filepath){
        pigeonFilepath.put(filename, filepath);
    }
    
    public OwlData(){
        pigeonFilepath = new LinkedHashMap<String,String>();        
    }
    
    public OwlData(Map<String,String> pigeonFilepath){
    	this.pigeonFilepath = pigeonFilepath;
    }
}
