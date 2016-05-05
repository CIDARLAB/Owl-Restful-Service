/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.cidarlab.eugene.Eugene;
import org.cidarlab.eugene.dom.imp.container.EugeneArray;
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
    
    
    // The XML-RPC client
    private XmlRpcClient client;

    // The configuration of the XML-RPC client
    private XmlRpcClientConfigImpl config;
    
    public EugeneAdaptor()
            throws Exception {

        /*
         * we instantiate a configuration for the XML-RPC client
         */
        this.config = new XmlRpcClientConfigImpl();

        /*
         * we configure the URL of the miniEugene XML-RPC Web service
         */
        this.config.setServerURL(new URL("http://cidar.bu.edu/miniEugeneXmlRpc/xmlrpc"));
    
        /*
         * we enable extensions
         */
        this.config.setEnabledForExtensions(true);
        this.config.setEnabledForExceptions(true);

        /*
         * then we instantiate the XML-RPC Client
         * and configure it 
         */
        this.client = new XmlRpcClient();
        this.client.setConfig(config);
    }
    
    public void startEugeneXmlRpc(String script) throws Exception{
        this.synchronousCall(script);
    }
    
    /**
     * The synchronousCall method invokes the miniEugene XML-RPC web service in
     * a synchronous way. That is, we the client is blocked until the web
     * service response.
     *
     * @param rules ... a set of miniEugene constraints
     */
    private void synchronousCall(String script)
            throws Exception {

        /*
         * here, we invoke the executeEugene/1 method of 
         * the miniEugene XML-RPC Web service
         */
        Object object = client.execute(
                "MiniEugeneXmlRpc.executeEugene",
                new Object[]{script});

        if (null != object) {

            // the received object, is actually a EugeneCollection object
            if (object instanceof EugeneCollection) {

                EugeneCollection results
                        = (EugeneCollection) object;
                if(results != null)
                    System.out.println("Eugene Collection :: " + results.toString());
                else
                    System.out.println("Some problem here..");
                
                /*EugeneArray result
                        = (EugeneArray) results.get("result");

                // process the result array
                System.out.println(result.size());
                */
            }

        }

    }
	
    
    public static EugeneCollection runEugene(String script) {

        try {
            Eugene eugene = new Eugene();
            //eugene.setRootDirectory(Args.eugeneRootDirectory);
            return eugene.executeScript(script);
        } catch (EugeneException ex) {
            Logger.getLogger(EugeneAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

// pass Boolean withRybozyme
// pass String dropdownList
    
    public static String createEugeneScript(List<DNAcomponent> promoters,  List<DNAcomponent> ribozymes, List<DNAcomponent> rbsList,  List<DNAcomponent> genes, List<DNAcomponent> terminators) {
        String script = "";
        script += "//COMMON PARTS AND PROPERTIES\n"
                + "Property name(txt);\n"
                + "\n"
                + "PartType Promoter(name);\n"
                + "PartType Ribozyme(name);\n"
                + "PartType RBS(name);\n"
                + "PartType CDS(name);\n"
                + "PartType Terminator(name);\n";

        int count = 1;
        for (DNAcomponent gene : genes) {
            script += "CDS g" + count++ + "(.SEQUENCE(\"" + gene.getSequence() + "\"), .name(\"" + gene.getName() + "\"));";
            script += "\n";
        }

        script += "num N = " + genes.size() + ";\n";

        count = 1;
        for (DNAcomponent promoter : promoters) {
            script += "Promoter p" + count++ + "(.SEQUENCE(\"" + promoter.getSequence() + "\"), .name(\"" + promoter.getName() + "\"));";
            script += "\n";
        }

        // Ribozyme is optional and may not be in the project! Needs a check to see if components exist in Clotho?
        count = 1;
        for (DNAcomponent ribozyme : ribozymes) {
            script += "Ribozyme ri" + count++ + "(.SEQUENCE(\"" + ribozyme.getSequence() + "\"), .name(\"" + ribozyme.getName() + "\"));";
            script += "\n";
        }

        count = 1;
        for (DNAcomponent rbs : rbsList) {
            script += "RBS rbs" + count++ + "(.SEQUENCE(\"" + rbs.getSequence() + "\"), .name(\"" + rbs.getName() + "\"));";
            script += "\n";
        }

        count = 1;
        for (DNAcomponent terminator : terminators) {
            script += "Terminator t" + count++ + "(.SEQUENCE(\"" + terminator.getSequence() + "\"), .name(\"" + terminator.getName() + "\"));";
            script += "\n";
        }
        /*
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
         */
        script += "// Ribozyme (TRUE is with; FALSE is without); Default is TRUE.\n"
                //=============================================================================
                // DMITRY will pass Boolean withRybozyme, from JIRA, instead of the word "true" here.
                //              + "boolean riboz = " + withRibozyme + ";\n"
                + "boolean riboz = true;\n"
                //=============================================================================
                + "//==========================================\n"
                + "//==========================================\n"
                + "\n"
                /*
                 + "// Define partTypes. \n"
                 + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/common.h\";\n"
                 + "// PART LIBRARY\n"
                 + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/promoter_library.eug\";\n"
                 + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/insulator_library.eug\";\n"
                 + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/RBS_library.eug\";\n"
                 + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/gene_library.eug\";\n"
                 + "//include \"/home/prash/cidar/owlDispatcher/resources/sampleEugene/terminator_library.eug\";\n"
                 + "\n"
                 */
                //==============================================================================
                // TO_DO: This is the place where we will have to check for dropdownList
                //==============================================================================
                + "Device Exhaustive();\n"
                + "Rule r1(on Exhaustive: ALL_FORWARD);\n"
                + "\n"
                + "for(num i=1; i<=N; i=i+1) {\n"
                + "  if(riboz == false) {\n"
                + "  	Exhaustive = Exhaustive + Promoter + RBS + CDS + Terminator;\n"
                + "  } else {\n"
                + "   Exhaustive = Exhaustive + Promoter + Ribozyme + RBS + CDS + Terminator;\n"
                + "  }\n"
                + "  Promoter${\"p\"+i};\n"
                + "  Ribozyme${\"ri\"+i};\n"
                + "  RBS${\"rbs\"+i};\n"
                + "  CDS${\"g\"+i}; AND(r1, ${\"g\"+i} EXACTLY 1);\n"
                + "  if(i>=2) {\n"
                + "    AND(r1, ${\"g\"+(i-1)} BEFORE ${\"g\"+i}); \n"
                + "  } \n"
                + "  Terminator${\"t\"+i};\n"
                + "}\n"
                + "\n"
                // pass String dropdownList instead of "Exhaustive"
                //              + "lod = product(" + dropdownList + ");\n"
                + "lod = product(Exhaustive);\n"
                + "//for (num i=0; i<sizeof(lod); i=i+1) {\n"
                + "//  println(sequence_of(lod[i]));\n"
                + "//}\n"
                + "\n"
                + "println(\"The number of all possible devices: \" + SIZEOF(lod));\n"
                + "println(lod);";

        return script;
    }

}
