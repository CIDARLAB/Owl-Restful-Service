/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

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
        
        latex += "\\title{Datasheet summary - Project " + project.getMyProjectId() + "}\n";
        
        latex += "\\begin{document}\n"
                + "\\maketitle\n"
                + "\\section{Devices}\n"
                + "\\begin{itemize}\n";

        for(String deviceName:project.getPigeonFilepath().keySet()){
            latex += "\\item " + deviceName.replaceAll("_", "\\_") + "\\\\\n";
            latex += "Blah blah blah\\\\\n";
            latex += getImageMinibox(project.getPigeonFilepath().get(deviceName));
        }
        
        latex += "\\end{itemize}\n"
                + "\\end{document}";
        
        return latex;
    }
    
    private static String getImageMinibox(String filepath){
        String latex = "";
        latex += "\\begin{minipage}[t]{1.2\\linewidth}\n"
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
    
}
