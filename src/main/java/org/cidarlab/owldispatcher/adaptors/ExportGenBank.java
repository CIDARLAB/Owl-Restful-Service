package org.cidarlab.owldispatcher.adaptors;

/*
 * @author Lloyd M.
 * @author Yury V. Ivanov
*/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.ProteinSequence;
import org.cidarlab.eugene.dom.Component;
import org.cidarlab.eugene.dom.Device;
import org.cidarlab.eugene.dom.NamedElement;
import org.cidarlab.eugene.exception.EugeneException;
import org.cidarlab.owldispatcher.Utilities;
import org.cidarlab.owldispatcher.DOM.GenBankFeature;

import com.google.common.base.Splitter;

public class ExportGenBank {
public final static String uniqueId = "F" + System.currentTimeMillis();
	
	public static final String writeGenBank(List<GenBankFeature> parts, String project) {
		String sequence = parts.get(0).getFullSequence();
        String lowCasSeq = sequence.toLowerCase();
        String sequenceLength = Integer.toString(sequence.length());
        //String sanitizedName = name.replaceAll("[^a-zA-Z0-9.-]", "_");
        String gbkFlatFile = "";
        String desc = "";
        
        if(parts.get(0).getAccession() != null){
        	desc = parts.get(0).getAccession() + " device of the project: " + parts.get(0).getOldAccession();
        } else {
        	desc = ".";
        }

        int spaces = 28 - uniqueId.length() - sequenceLength.length();
        String spacer = String.format("%"+spaces+"s", "");
        gbkFlatFile += "LOCUS       " + uniqueId + spacer + sequenceLength + " bp    DNA     linear   UNK " + getDate();
        gbkFlatFile += "\n"+"DEFINITION  "+ desc;
        gbkFlatFile += "\nACCESSION   " + uniqueId;
        gbkFlatFile += "\n" + "VERSION     "+".";
        gbkFlatFile += "\nKEYWORDS    "+".";
        gbkFlatFile += "\nSOURCE      "+".";
        gbkFlatFile += "\n  ORGANISM  "+".";
        gbkFlatFile +="\nFEATURES             Location/Qualifiers";
        gbkFlatFile +="\n     source          1.."+sequenceLength;
        if(parts.get(0).getAccession() != null){
        	gbkFlatFile +="\n                     /note=\""+ parts.get(0).getAccession() + " device of the project " + parts.get(0).getOldAccession()+"\"";
        } else {
        	gbkFlatFile +="\n                     /note=\"an unknown device or a derivative of: " + parts.get(0).getOldAccession() + "\"";
        }
        for(GenBankFeature part : parts){
        	if(part.getFeatureType().matches("CDS")){
	        	if(!part.isReverseComplement()){
	        		gbkFlatFile +="\n     gene            " + part.getStartx() + ".." + part.getEndx();
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		if(part.getGenBankId() != null){
	        			gbkFlatFile +="\n                     /db_xref=\"" + part.getGenBankId() + "\"";
	        		}
	        		gbkFlatFile +="\n     " + part.getFeatureType() + "             " + part.getStartx() + ".." + part.getEndx();
	        	} else{
	        		gbkFlatFile +="\n     gene            complement(" + part.getStartx() + ".." + part.getEndx() + ")";
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		if(part.getGenBankId() != null){
	        			gbkFlatFile +="\n                     /db_xref=\"" + part.getGenBankId() + "\"";
	        		}
	        		gbkFlatFile +="\n     " + part.getFeatureType() + "             complement(" + part.getStartx() + ".." + part.getEndx() + ")";
	        	}
        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
        		if(part.getGenBankId() != null){
        			gbkFlatFile +="\n                     /db_xref=\"" + part.getGenBankId() + "\"";
        		}
	        	gbkFlatFile +="\n                     /product=\"gp" + part.getName() +"\"";
	        	
	        	ProteinSequence protein = new DNASequence(part.getDnaSequence()).getRNASequence().getProteinSequence();
	        	System.out.println("type: "+part.getFeatureType()+"  -  "+part.getDnaSequence() + " - "+protein.getSequenceAsString() + " " + protein.getLength());
	        	
	        	if(protein.getSequenceAsString().contains("*")){
	        		gbkFlatFile +="\n                     /partial";
	        		gbkFlatFile +="\n                     /note=\"partial gene\"";
	        	} else {
	        		//generate /translation="<ProteinSequence>" annotation
	        		gbkFlatFile += getProteinToAnnotation(protein);
	        	}
	        	
        	} else if(part.getFeatureType().matches("promoter")){
	        	if(!part.isReverseComplement()){
	        		gbkFlatFile +="\n     promoter        " + part.getStartx() + ".." + part.getEndx();
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		if(part.getGenBankId() != null){
	        			gbkFlatFile +="\n                     /db_xref=\"" + part.getGenBankId() + "\"";
	        		}
	        	} else{
	        		gbkFlatFile +="\n     promoter        complement(" + part.getStartx() + ".." + part.getEndx() + ")";
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		if(part.getGenBankId() != null){
	        			gbkFlatFile +="\n                     /db_xref=\"" + part.getGenBankId() + "\"";
	        		}
	        	}        	
        	} else if (part.getFeatureType().matches("terminator")){
	        	if(!part.isReverseComplement()){
	        		gbkFlatFile +="\n     terminator      " + part.getStartx() + ".." + part.getEndx();
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		if(part.getGenBankId() != null){
	        			gbkFlatFile +="\n                     /db_xref=\"" + part.getGenBankId() + "\"";
	        		}
	        	} else{
	        		gbkFlatFile +="\n     terminator      complement(" + part.getStartx() + ".." + part.getEndx() + ")";
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		if(part.getGenBankId() != null){
	        			gbkFlatFile +="\n                     /db_xref=\"" + part.getGenBankId() + "\"";
	        		}
	        	}  
        	} else if (part.getFeatureType().matches("ncRNA")){
	        	if(!part.isReverseComplement()){
	        		gbkFlatFile +="\n     ncRNA           " + part.getStartx() + ".." + part.getEndx();
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		if(part.getGenBankId() != null){
	        			gbkFlatFile +="\n                     /db_xref=\"" + part.getGenBankId() + "\"";
	        		}
	        	} else{
	        		gbkFlatFile +="\n     ncRNA           complement(" + part.getStartx() + ".." + part.getEndx() + ")";
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		if(part.getGenBankId() != null){
	        			gbkFlatFile +="\n                     /db_xref=\"" + part.getGenBankId() + "\"";
	        		}
	        	}  
        	} else if (part.getFeatureType().matches("RBS")){
	        	if(!part.isReverseComplement()){
	        		gbkFlatFile +="\n     RBS             " + part.getStartx() + ".." + part.getEndx();
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		if(part.getGenBankId() != null){
	        			gbkFlatFile +="\n                     /db_xref=\"" + part.getGenBankId() + "\"";
	        		}
	        	} else{
	        		gbkFlatFile +="\n     RBS             complement(" + part.getStartx() + ".." + part.getEndx() + ")";
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		if(part.getGenBankId() != null){
	        			gbkFlatFile +="\n                     /db_xref=\"" + part.getGenBankId() + "\"";
	        		}
	        	}  
        	}
        } 
        	
        gbkFlatFile +="\nORIGIN\n";
        Iterable<String> seqArray = Splitter.fixedLength(60).split(lowCasSeq);
        int linecounter = 0;
        for(Iterator<String> it = seqArray.iterator(); it.hasNext();) {
            int spc = 9 - Integer.toString(linecounter*60).length();
            String s = String.format("%"+spc+"s", "");
            gbkFlatFile += s+Integer.toString((linecounter*60)+1);
            Iterable<String> chunk = Splitter.fixedLength(10).split(it.next());
            for(Iterator<String> iter = chunk.iterator(); iter.hasNext();) {
                gbkFlatFile += " " + iter.next();
            }
            gbkFlatFile += "\n";
            linecounter++;
        }    
        gbkFlatFile += "//";
        System.out.println("==========GenBank exporter===========");
        System.out.println(gbkFlatFile);
        
        return gbkFlatFile;
    }
	
	public static String deviceToGenBank(String project, Device device) throws EugeneException{
		List<GenBankFeature> gbList = new ArrayList<>();
            
            GenBankFeature gbf = new GenBankFeature();
            gbf.setAccession(device.getName());
            gbf.setOldAccession(project);
            gbf.setFeatureType("source");
            gbf.setStartx(1);
            gbf.setDnaSequence(getDeviceDnaSequence(device));
            gbf.setFullSequence(getDeviceDnaSequence(device));
            gbf.setEndx(gbf.getFullSequence().length());
            gbf.setReverseComplement(false);
            gbList.add(gbf);
            for (int j = 0; j < device.getComponents().size(); j++) {
                String part = device.getComponents().get(j).get(0).toString();
                GenBankFeature partFeature = new GenBankFeature(); 
                partFeature.setDnaSequence(device.getComponents().get(j).get(0).getElement("SEQUENCE").toString().replaceAll("\"", ""));
                partFeature.setName(device.getComponents().get(j).get(0).getElement("name").toString().replaceAll("\"", ""));
                
                if (part.startsWith("Promoter")) {
                	partFeature.setFeatureType("promoter");
                } else if (part.startsWith("RBS")) {
                	partFeature.setFeatureType("RBS");
                } else if (part.startsWith("Ribozyme")) {
                	partFeature.setFeatureType("ncRNA");
                } else if (part.startsWith("CDS")) {
                	partFeature.setFeatureType("CDS");
                	ProteinSequence p = new DNASequence(partFeature.getDnaSequence()).getRNASequence().getProteinSequence();
                	partFeature.setProteinSequence(p.getSequenceAsString());
                } else if (part.startsWith("Terminator")) {
                	partFeature.setFeatureType("terminator");
                } else {
                    System.out.println("Unrecognized part found  in EugeneArray: "+part);
                }
                               
                //TEST 
               partFeature.setStartx(1);
               partFeature.setEndx(102);
               partFeature.setReverseComplement(false);
               
               gbList.add(partFeature);
            }

            String gbk = writeGenBank(gbList, project);
  
		return gbk;
	}
	
	public static String getDeviceDnaSequence(Device device){
        String sequence = "";
		for(List<NamedElement> listnamedElement : device.getComponents()){
                for(NamedElement ne : listnamedElement){
                    Component component = (Component)ne;
                    
                    try {
                        sequence = sequence.concat(component.getSequence());
                    } catch (EugeneException ex) {
                        System.out.println("ExportGenBank.java error: "+ ex.getMessage());
                    }
                }
        }
        return sequence;
    }
	
	private static String getProteinToAnnotation(ProteinSequence protein){
		String annotation = "";
		int j=58;
    	int v=44;
    	if(protein.getLength()<=v){
    		annotation +="\n                     /translation=\"" + protein.getSequenceAsString()+"\"";
    	} else{
	    	for(int i=0;i<=((protein.getLength()-v)/j);i++){
	    		if(i==0){
	    			annotation +="\n                     /translation=\"" + protein.getSequenceAsString().substring(0, v);
	    			if(protein.getLength()<= v+j){
	    				annotation +="\n                     " + protein.getSequenceAsString().substring(v, protein.getLength())+"\"";
	    			} else {
	    				annotation +="\n                     " + protein.getSequenceAsString().substring(v, j+v);
	    			}	
	    		} else if(protein.getLength()-(i*j+v) < 58){
	    			annotation +="\n                     " + protein.getSequenceAsString().substring(i*j+v, protein.getLength()) + "\"";
	    		} else {
	    			annotation +="\n                     " + protein.getSequenceAsString().substring(i*j+v, i*j+j+v);
	    		}
	    	}
    	}	
		return annotation;
	}
	
    private static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        return formatter.format(new Date(System.currentTimeMillis())).toUpperCase();

    }
       
}
