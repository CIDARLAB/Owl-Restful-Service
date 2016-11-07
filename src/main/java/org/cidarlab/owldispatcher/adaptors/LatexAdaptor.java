/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.cidarlab.owldispatcher.Utilities;
import org.cidarlab.owldispatcher.model.DataStreamJira;
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
            latex += "Blah blah blah\\\\\n";
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
        latex += "           \\includegraphics[width=\\linewidth]{" + filepath + "}\n";
        latex += "          }\n"
                + "          \\end{minipage}\n";
        return latex;
    }
    
    public static String getLatexFilepath(OwlData project){
        String filepath = Utilities.getOutputFilepath() + project.getMyProjectId() + Utilities.getFileDivider() + project.getMyProjectId() + ".tex";
        Utilities.writeToFile(filepath, generateLatexCode(project));
        return filepath;
    }
    
    public static void runPDFlatex(OwlData project){
        String texPath = getLatexFilepath(project);
        String outputDir = Utilities.getOutputFilepath() + project.getMyProjectId();
        
        String command = "";
        if(Utilities.isWindows()){
            
        }
        if(Utilities.isMac()){
            
        }
        if(Utilities.isLinux()){
            command += "pdflatex -aux-directory=" + outputDir + " -output-directory=" + outputDir + " " + texPath;
        }
        Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        try {
            proc = runtime.exec(command);
            proc.waitFor();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(LatexAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
    
}
