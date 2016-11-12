/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

import java.io.File;
import java.util.regex.Matcher;

import org.apache.commons.lang.WordUtils;
import org.cidarlab.eugene.dom.Device;
import org.cidarlab.eugene.dom.NamedElement;
import org.cidarlab.eugene.exception.EugeneException;
import org.cidarlab.owldispatcher.Utilities;
import org.cidarlab.owldispatcher.model.OwlData;

/**
 *
 * @author prash
 */
public class LatexAdaptor {
    
    public static String generateLatexCode(OwlData project){
        String latex = "";
        latex += "\\documentclass[12pt]{article}\n"
                + "\\usepackage{fullpage}\n"
                + "\\usepackage{hyperref}\n"
                + "\\usepackage{mathtools}\n"
                + "\\usepackage[usenames, dvipsnames]{color}\n"
                + "\\usepackage{adjustbox}\n"
                + "\\usepackage[section]{placeins}\n";
        String newProjectName = project.getMyProjectId().replaceAll("_", Matcher.quoteReplacement("\\_"));
        
        latex += "\\title{Datasheet summary - Project " + newProjectName + "}\n";
        
        latex += "\\begin{document}\n"
                + "\\maketitle\n"
                + "\\section{Devices}\n"
                + "\\begin{itemize}\n";

        for(String deviceName:project.getPigeonFilepath().keySet()){
            String newDeviceName = deviceName.replaceAll("_", Matcher.quoteReplacement("\\_"));
            latex += "\\item " + newDeviceName + "\\\\\n";
            latex += "Length of the device (bp): " + String.format("%,d", project.getDeviceLengths().get(deviceName)) + "\\\\\n";
            latex += "\\%GC = " + project.getGcContents().get(deviceName) + "\\\\\n";
            latex += "Composition (5' to 3'): " + project.getDeviceCompositions().get(deviceName) + "\\\\\n";
            latex += getImageMinibox(project.getPigeonFilepath().get(deviceName));
        }
        
        latex += "\\end{itemize}\n"
                + "\\end{document}";
        
        return latex;
    }
    
    private static String getImageMinibox(String filepath){
        String latex = "";
        latex += "\\begin{minipage}[t]{\\linewidth}\n"
                + "          \\adjustbox{valign=t}{\n";
        if(Utilities.isWindows()){
        	String newFilePath = filepath.replaceAll("\\\\","/");
        	latex += "           \\includegraphics[width=\\linewidth]{" + newFilePath + "}\n";
        }else {
        	latex += "           \\includegraphics[width=\\linewidth]{" + filepath + "}\n";
        }
        latex += "          }\n"
                + "          \\end{minipage}\n";
        return latex;
    }
    
    public static String getDeviceComponents(Device device){
    	String deviceComp = "";
        for (NamedElement nel : device.getComponentList()){
        	try {
				deviceComp += nel.getElement("name").toString().replaceAll("\"", "") + " ; ";
			} catch (EugeneException e) {
				System.out.println("Error processing device components for the LatexAdaptor. Reason: " + e.getMessage());
			}
        }
    	return WordUtils.wrap(deviceComp.replaceAll("_", "\\\\_"), 70, "\n", true);
    }
    
    public static String makeTexFile(OwlData project){
        File texFolder = new File(project.getPathToTexFolder());
        texFolder.mkdir();
        Utilities.writeToFile(project.getPathToTexFile(), generateLatexCode(project));
        return project.getPathToTexFile();
    }
           
}
    
