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
	
	public static final String writeGenBank(List<GenBankFeature> parts) {
		String sequence = parts.get(0).getFullSequence();
        String lowCasSeq = sequence.toLowerCase();
        String sequenceLength = Integer.toString(sequence.length());
        //String sanitizedName = name.replaceAll("[^a-zA-Z0-9.-]", "_");
        String gbkFlatFile = "";
        String desc = ".";

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
	        	if(!part.getReverseComplement()){
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
	        	
	        	//translate nucleotide sequence into protein and print into CDS annotation in genBank file
	        	/*System.out.println("+++++++++++++++++++++++");
	        	//System.out.println(part.getDnaSequence());
	        	System.out.println("type: "+part.getFeatureType()+"  -  "+part.getDnaSequence());*/
	        	ProteinSequence protein = new DNASequence(part.getDnaSequence()).getRNASequence().getProteinSequence();
	        	/*System.out.println("type: "+part.getFeatureType()+"  -  "+part.getDnaSequence());*/
	        	
	        	if(protein.toString().contains("*")){
	        		gbkFlatFile +="\n                     /partial";
	        		gbkFlatFile +="\n                     /note=\"partial gene\"";
	        	} else {
	        	
		        	int j=58;
		        	int v=44;
		        	for(int i=0;i*j+v<=protein.getLength();i++){
		        		if(i==0){
		        			gbkFlatFile +="\n                     /translation=\"" + protein.toString().substring(i, v);
		        		} else if(i==1){
		        			gbkFlatFile +="\n                     " + protein.toString().substring(i*v, i*j+v);
		        		} else if(protein.getLength()-(i*j+v) < 58){
		        			gbkFlatFile +="\n                     " + protein.toString().substring((i-1)*j+v, i*j+v);
		        			gbkFlatFile +="\n                     " + protein.toString().substring(i*j+v, protein.getLength()) + "\"";
		        		} else {
		        			gbkFlatFile +="\n                     " + protein.toString().substring((i-1)*j+v, (i-1)*j+j+v);
		        		}
		        	}
	        	}
	        	
        	} else if(part.getFeatureType().matches("promoter")){
	        	if(!part.getReverseComplement()){
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
	        	if(!part.getReverseComplement()){
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
	        	if(!part.getReverseComplement()){
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
	        	if(!part.getReverseComplement()){
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
        try {
			WriteGenBank(gbkFlatFile);
		} catch (IOException e) {
			System.out.println("Cannot create GenBank file. Reason: "+e.getMessage());
		}
        return gbkFlatFile;
    }
	
	public static String deviceToGenBank(String project, Device device) throws EugeneException{
		List<GenBankFeature> gbList = new ArrayList<>();
            String deviceName = device.getName();
            
            GenBankFeature gbf = new GenBankFeature();
            gbf.setAccession(deviceName);
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
                
                String partSequence = device.getComponents().get(j).get(0).getElement("SEQUENCE").toString().replaceAll("\"", "");
                partFeature.setDnaSequence(partSequence);
                
                String partName = device.getComponents().get(j).get(0).getElement("name").toString().replaceAll("\"", "");
                partFeature.setName(partName);
                System.out.println(partFeature.getName() + "  :  "+partFeature.getDnaSequence());
                if (part.startsWith("Promoter")) {
                	partFeature.setFeatureType("promoter");
                } else if (part.startsWith("RBS")) {
                	partFeature.setFeatureType("RBS");
                } else if (part.startsWith("Ribozyme")) {
                	partFeature.setFeatureType("ncRNA");
                } else if (part.startsWith("CDS")) {
                	partFeature.setFeatureType("CDS");
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

            String gbk = writeGenBank(gbList);
  
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
	
	
    private static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        return formatter.format(new Date(System.currentTimeMillis())).toUpperCase();

    }
    
    private static void WriteGenBank(String genBank) throws IOException {
    	final String pathToFolder = Utilities.getOutputFilepath() + uniqueId;
    	final String pathToFile = pathToFolder + "\\" + uniqueId + ".gb";
        File file = new File(pathToFolder);
        file.mkdir();
    	try (FileWriter locFile = new FileWriter(pathToFile);){
    		locFile.write(genBank);
    		System.out.println(pathToFile + " file was successfully created.");
    	}
    }
}
