/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.cidarlab.eugene.dom.Component;
import org.cidarlab.eugene.dom.Device;
import org.cidarlab.eugene.dom.NamedElement;
import org.cidarlab.eugene.dom.PartType;
import org.cidarlab.eugene.Eugene;
import org.cidarlab.eugene.dom.imp.container.EugeneArray;
import org.cidarlab.eugene.dom.imp.container.EugeneCollection;
import org.cidarlab.eugene.dom.rules.ArrangementConstraint;
import org.cidarlab.eugene.dom.rules.ArrangementOperand;
import org.cidarlab.eugene.dom.rules.LogicalAnd;
import org.cidarlab.eugene.dom.rules.Rule;
import org.cidarlab.eugene.exception.EugeneException;
import org.cidarlab.owldispatcher.Args;
import org.cidarlab.owldispatcher.DOM.ComponentType;
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
    
    // @Getter
    private EugeneArray result;
    
    
    public EugeneArray getResult() {
		return result;
	}

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

                //this.collection
                EugeneCollection results
                        = (EugeneCollection) object;

                if(results != null) {
                    System.out.println("Works!");
                    //System.out.println("Eugene Collection :: " + results.toString());
                } else {
                    System.out.println("Eugene Collection :: ERROR; the collection is empty!!!");
                }

                Device monocistronic = (Device)results.get("Monocistronic_prgt");

                Rule ruleOnParts = (Rule)results.get("r1");

                Rule ruleOnPartTypePositioning = this.deriveRuleFromStructure(monocistronic);

                System.out.println(ruleOnParts);
                System.out.println(ruleOnPartTypePositioning);

                    /*
                     * concatenate both rules using a logical and
                     */

                    Rule concatenated = this.and(ruleOnParts, ruleOnPartTypePositioning);
                    System.out.println(concatenated);

                result
                        = (EugeneArray) results.get("lod");
                
                

                // Would be nice to get SBOL files and Pigeon images for each device in EugeneArray

                // process the result array
                System.out.println("\n\nThe total number of constraints-compliant devices is: " + result.size());

            }
      	}
    }

    public Rule and(final Rule r1, final Rule r2) {

  		LogicalAnd andR1 = r1.getPredicates();
  		LogicalAnd andR2 = r2.getPredicates();

  		LogicalAnd and = new LogicalAnd();
  		and.getPredicates().addAll(andR1.getPredicates());
  		and.getPredicates().addAll(andR2.getPredicates());

  		Rule r = new Rule(r1.getName() + "_AND_" + r2.getName(), r1.getDevice());
  		r.setLogicalAnd(and);
  		return r;
  	}

  	public Rule deriveRuleFromStructure(Device device)
  			throws EugeneException {

  		/*
  		 * Positioning Rule
  		 *
  		 * the i-th component must be positioned BEFORE the (i+1)-th component
  		 *
  		 * Assumption:
  		 * -- the device consists of part types only
  		 */

  		// the constraints are conjuctive
  		LogicalAnd positioning = new LogicalAnd();

  		// iterate over the device's components
  		for(int i=0; i<device.getComponents().size()-1; i++) {

  			// get the i-th part type
  			PartType ptLeftOp = (PartType)device.getComponents().get(i).get(0);

  			// get the (i+1)-th part type
  			PartType ptRightOp = (PartType)device.getComponents().get(i+1).get(0);

  			// generate a new <i-th part type> BEFORE <(i+1)-th part type> predicate
  			positioning.getPredicates().add(
      				new ArrangementConstraint(
      						new ArrangementOperand(ptLeftOp, 0, 0),
      						"BEFORE",
      						new ArrangementOperand(ptRightOp, 0, 0)));
  		}

  		// lastly, package the LogicalAnd into a Rule object
  		Rule structuralRule = new Rule("deviceStructure", device);
  		structuralRule.setLogicalAnd(positioning);

  		// return the rule object
  		return structuralRule;
  	}


// pass Boolean withRybozyme
// pass designMethod


    public static String createEugeneScript(Map<ComponentType,List<DNAcomponent>> componentmap,boolean withRibozyme,String designMethod){
        return createEugeneScript(componentmap.get(ComponentType.PROMOTER),componentmap.get(ComponentType.RIBOZYME),componentmap.get(ComponentType.RBS),componentmap.get(ComponentType.GENE),componentmap.get(ComponentType.TERMINATOR),withRibozyme,designMethod);
    }



    public static String createEugeneScript(
		List<DNAcomponent> promoters,
		List<DNAcomponent> ribozymes,
		List<DNAcomponent> rbsList,
		List<DNAcomponent> genes,
		List<DNAcomponent> terminators,
		boolean withRibozyme,
		String designMethod) {
    	
        String script = "";
        script += "//COMMON PARTS AND PROPERTIES\n"
                + "Property name(txt);\n"
                + "Property SO(txt);\n"
                + "\n"
                + "PartType Promoter(name, SO);\n"
                + "PartType Ribozyme(name, SO);\n"
                + "PartType RBS(name, SO);\n"
                + "PartType CDS(name, SO);\n"
                + "PartType Terminator(name, SO);\n";

        int count = 1;
        for (DNAcomponent gene : genes) {
            script += "CDS g" + count++ + "(.SEQUENCE(\"" + gene.getSequence() + "\"), .name(\"" + gene.getName() + "\"), .SO(\"SO_0000316\"));";
            script += "\n";
        }

        script += "num N = " + genes.size() + ";\n";

        count = 1;
        for (DNAcomponent promoter : promoters) {
            script += "Promoter p" + count++ + "(.SEQUENCE(\"" + promoter.getSequence() + "\"), .name(\"" + promoter.getName() + "\"), .SO(\"SO_0000167\"));";
            script += "\n";
        }

        // Ribozyme is optional and may not be in the project! Needs a check to see if components exist in Clotho?
        count = 1;
        for (DNAcomponent ribozyme : ribozymes) {
            script += "Ribozyme ri" + count++ + "(.SEQUENCE(\"" + ribozyme.getSequence() + "\"), .name(\"" + ribozyme.getName() + "\"), .SO(\"SO_0000627\"), .PIGEON(\"z ri 13\"));";
            script += "\n";
        }

        count = 1;
        for (DNAcomponent rbs : rbsList) {
            script += "RBS rbs" + count++ + "(.SEQUENCE(\"" + rbs.getSequence() + "\"), .name(\"" + rbs.getName() + "\"), .SO(\"SO_0000139\"));";
            script += "\n";
        }

        count = 1;
        for (DNAcomponent terminator : terminators) {
            script += "Terminator t" + count++ + "(.SEQUENCE(\"" + terminator.getSequence() + "\"), .name(\"" + terminator.getName() + "\"), .SO(\"SO_0000141\"));";
            script += "\n";
        }

        script += "// Ribozyme (TRUE is with; FALSE is without); Default is TRUE.\n"
                + "boolean riboz = " + withRibozyme + ";\n"
                //=============================================================================
                + "//==========================================\n"
                + "\n"

                //MONOCISTRONIC promoter-rbs-gene-terminator DEVICE TEMPLATE
                
                + "Device Monocistronic_prgt();\n"
                + "Rule r1(on Monocistronic_prgt: ALL_FORWARD);\n"
                + "\n"
                + "for(num i=1; i<=N; i=i+1) {\n"
                + "  if(riboz == false) {\n"
                + "  	Monocistronic_prgt = Monocistronic_prgt + Promoter + RBS + CDS + Terminator;\n"
                + "  } else {\n"
                + "   Monocistronic_prgt = Monocistronic_prgt + Promoter + Ribozyme + RBS + CDS + Terminator;\n"
                + "  }\n"
                + "  Promoter${\"p\"+i};\n"
                + "  Ribozyme${\"ri\"+i};\n"
                + "  RBS${\"rbs\"+i};\n"
                + "  CDS${\"g\"+i};\n"
                + "  AND(r1, ${\"g\"+i} EXACTLY 1);\n"
                + "  if(i>=2) {\n"
                + "    AND(r1, ${\"g\"+(i-1)} BEFORE ${\"g\"+i}); \n"
                + "  } \n"
                + "  Terminator${\"t\"+i};\n"
                + "}\n"
                + "\n"
                
                // MONOCISTRONIC promoter-gene-terminator DEVICE TEMPLATE
                
                + "Device Monocistronic_pgt();\n"
                + "Rule r2(on Monocistronic_pgt: ALL_FORWARD);\n"

                + "for(num i=1; i<=N; i=i+1) {\n"
                + "  if(riboz == false) {\n"
                + "  	Monocistronic_pgt = Monocistronic_pgt + Promoter + CDS + Terminator;\n"
                + "  } else {\n"
                + "    Monocistronic_pgt = Monocistronic_pgt + Promoter + Ribozyme + CDS + Terminator;\n"
                + "  }\n"
                + "  Promoter${\"p\" + i};\n"
                + "  Ribozyme${\"ri\"+i};\n"
                + "  CDS${\"g\"+i}; AND(r2, ${\"g\"+i} EXACTLY 1);\n"
                + "  if(i>=2) {\n"
                + "    AND(r2, ${\"g\"+(i-1)} BEFORE ${\"g\"+i});\n" 
                + "  }\n"
                + "  Terminator${\"t\"+i};\n"
                + "}\n"
			    
			    //POLYCISTRONIC DEVICE TEMPLATE
			    + "Device Polycistronic(Promoter);\n"
			
			    + "Rule R(on Polycistronic: ALL_FORWARD);\n"
			
			
			    + "for(num i=1; i<=N; i=i+1) {\n"
			    +   	"if (i == 10 || i == 20 || i == 30 || i == 40 || i == 50 || i == 60 || i == 70) {\n"
			    +   	    "Polycistronic = Polycistronic + RBS + CDS + Promoter;\n"
			    +           "CDS${\"g\"+i}; AND(R, ${\"g\"+i} EXACTLY 1);\n"
			    +           "RBS${\"rbs\" + i};\n"
			    +           "Promoter${\"p\"+i};\n"
			    +       "} else {\n"
			    +   	    "Polycistronic = Polycistronic + RBS + CDS;\n"
			    + "         RBS${\"rbs\" + i};\n"
			    + "         CDS${\"g\"+i}; AND(R, ${\"g\"+i} EXACTLY 1);\n"
			    + "      }\n"
			       
			    + "   if(i>=2) {\n"
			    + "     AND(R, ${\"g\"+(i-1)} BEFORE ${\"g\"+i});\n"
			    + "   }\n"
			  
			    + " }\n"
			    + "   Polycistronic = Polycistronic + Terminator;\n"
			    
	
			    // GENERATE the DEVICE
			    + " lod = product(" + designMethod + ");\n";			
			
			        return script;
			    }

}
