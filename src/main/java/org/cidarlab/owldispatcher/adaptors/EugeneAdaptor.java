/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.eugene.Eugene;
import org.cidarlab.eugene.dom.imp.container.EugeneCollection;
import org.cidarlab.eugene.exception.EugeneException;
import org.cidarlab.owldispatcher.Args;
import org.cidarlab.owldispatcher.DOM.DNAcomponent;
import org.cidarlab.owldispatcher.Utilities;

/**
 *
 * @author prash
 */
public class EugeneAdaptor {
    
    public static EugeneCollection runEugene(String script){
        
        try {
            Eugene eugene = new Eugene();
            eugene.setRootDirectory(Args.eugeneRootDirectory);
            return eugene.executeScript(script);
        } catch (EugeneException ex) {
            Logger.getLogger(EugeneAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static String createEugeneScript(List<DNAcomponent> genes){
        String script = "";
        script += "//COMMON PARTS AND PROPERTIES\n"
                + "Property name(txt);\n"
                + "\n"
                + "PartType Promoter(name);\n"
                + "PartType Insulator(name);\n"
                + "PartType RBS(name);\n"
                + "PartType CDS(name);\n"
                + "PartType Terminator(name);\n";
        
        int count =1;
        for(DNAcomponent gene:genes){
            String cds = "CDS g" + count++ +"(.SEQUENCE(\""+gene.getSequence()+"\"), .name(\""+gene.getName()+"\"));";
            cds += "\n";
            script += cds;
        }
        
        script += "num N = " + genes.size() + ";\n";
        
        script += "Promoter pBla(.SEQUENCE(\"TGTAAGTTTATACATAGGCGAGTACTCTGTTATGG\"), .name(\"BBa_I14018\"));\n"
                + "Promoter p2(.SEQUENCE(\"TTGACGGCTAGCTCAGTCCTAGGTACAGTGCTAGC\"), .name(\"BBa_J23100\"));\n"
                + "\n"
                + "RBS rbs1(.SEQUENCE(\"GAAAGAGGGGACAA\"), .name(\"rbs1\"));\n"
                + "RBS rbs2(.SEQUENCE(\"GAAAGACAGGACCC\"), .name(\"rbs2\"));\n"
                + "RBS rbs3(.SEQUENCE(\"GAAAGATCCGATGT\"), .name(\"rbs3\"));\n"
                + "RBS rbs4(.SEQUENCE(\"GAAAGATTAGACAA\"), .name(\"rbs4\"));\n"
                + "\n"
                + "Terminator t1(.SEQUENCE(\"TCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATA\"), .name(\"BBa_B0012\"));\n"
                + "\n"
                + "Insulator ri(.SEQUENCE(\"AAAAAAAAAAAAA\"), .name(\"Rybozyme_insulator\"), .PIGEON(\"z ri 13\"));\n"
                + "\n"
                + "\n"
                + "// Rybozyme (TRUE is with; FALSE is without); Default is TRUE.\n"
                + "boolean rybozyme = true;\n"
                + "//==========================================\n"
                + "//==========================================\n"
                + "\n"
                + "// Define partTypes. \n"
                + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/common.h\";\n"
                + "// PART LIBRARY\n"
                + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/promoter_library.eug\";\n"
                + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/insulator_library.eug\";\n"
                + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/RBS_library.eug\";\n"
                + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/gene_library.eug\";\n"
                + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/terminator_library.eug\";\n"
                + "\n"
                + "Device Exhaustive();\n"
                + "Rule r1(on Exhaustive: ALL_FORWARD);\n"
                + "\n"
                + "//this example is for 3 genes:\n"
                + "\n"
                + "\n"
                + "for(num i=1; i<=N; i=i+1) {\n"
                + "  if(rybozyme == false) {\n"
                + "  	Exhaustive = Exhaustive + Promoter + RBS + CDS + Terminator;\n"
                + "  } else {\n"
                + "    Exhaustive = Exhaustive + Promoter + Insulator + RBS + CDS + Terminator;\n"
                + "  }\n"
                + "  Promoter${\"p\"+i};\n"
                + "  Insulator${\"ri\"+i};\n"
                + "  RBS${\"rbs\"+i};\n"
                + "  CDS${\"g\"+i}; AND(r1, ${\"g\"+i} EXACTLY 1);\n"
                + "  if(i>=2) {\n"
                + "    AND(r1, ${\"g\"+(i-1)} BEFORE ${\"g\"+i}); \n"
                + "  } \n"
                + "  Terminator${\"t\"+i};\n"
                + "}\n"
                + "\n"
                + "lod = product(Exhaustive);\n"
                + "//for (num i=0; i<sizeof(lod); i=i+1) {\n"
                + "//  println(sequence_of(lod[i]));\n"
                + "//}\n"
                + "\n"
                + "println(\"The number of all possible devices: \" + SIZEOF(lod));\n"
                + "println(lod);";
        
        return script;
    }
    
    public static void main(String[] args) {
        Utilities.setEugeneRootDirectory(Utilities.getDefautltEugeneRootDirectory());
        String filepath = Utilities.getDefautltEugeneRootDirectory() + "app.eug";
        System.out.println("File path :: " + filepath);
        EugeneCollection collection = runEugene(filepath);
        System.out.println("Result :: " + collection.toString());
    }
    
}
