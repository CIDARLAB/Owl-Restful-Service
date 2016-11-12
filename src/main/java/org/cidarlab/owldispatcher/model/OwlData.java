package org.cidarlab.owldispatcher.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.cidarlab.owldispatcher.Utilities;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 * @author Yury V. Ivanov
 */
public class OwlData extends AbstractData{

    @Getter @Setter private Map<String,String> pigeonFilepath;
    @Getter 		private final String pathToTexFolder = Utilities.getOutputFilepath()+"tex";
    @Getter @Setter private String pathToTexFile;
    @Getter @Setter private Map<String, String> deviceCompositions;
    @Getter @Setter private Map<String, String> gcContents;
    @Getter @Setter private Map<String, Integer> deviceLengths;
    
    public void addPigeonFilepath(String filename, String filepath){
        pigeonFilepath.put(filename, filepath);
    }
    
    public OwlData(){
        pigeonFilepath = new LinkedHashMap<String,String>();
        gcContents = new HashMap<>();
        deviceLengths = new HashMap<>();
        deviceCompositions = new HashMap<>();
    }
    
    public OwlData(Map<String,String> pigeonFilepath, Map<String, String> gcContent, Map<String, Integer> deviceLengths, Map<String, String> deviceCompositions){
    	this.pigeonFilepath = pigeonFilepath;
    	this.gcContents = gcContent;
    	this.deviceLengths = deviceLengths;
    	this.deviceCompositions = deviceCompositions;
    }
}
