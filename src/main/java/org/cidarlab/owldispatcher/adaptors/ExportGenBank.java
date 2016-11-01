package org.cidarlab.owldispatcher.adaptors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.ProteinSequence;
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
/*        if (description.isEmpty()) {
            desc = ".";
        } else {
            desc = description; 
        }*/
        
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
        gbkFlatFile +="\n                     /note=\"unknown device\"";
        for(GenBankFeature part : parts){
        	if(part.getFeatureType().matches("CDS")){
	        	if(!part.getReverseComplement()){
	        		gbkFlatFile +="\n     gene            " + part.getStartx() + ".." + part.getEndx();
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		gbkFlatFile +="\n     " + part.getFeatureType() + "             " + part.getStartx() + ".." + part.getEndx();
	        	} else{
	        		gbkFlatFile +="\n     gene            complement(" + part.getStartx() + ".." + part.getEndx() + ")";
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        		gbkFlatFile +="\n     " + part.getFeatureType() + "             complement(" + part.getStartx() + ".." + part.getEndx() + ")";
	        	}
        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        	gbkFlatFile +="\n                     /product=\"gp" + part.getName() +"\"";
	        	
	        	//translate nucleotide sequence into protein and print into CDS annotation in genBank file
	        	ProteinSequence protein = new DNASequence(part.getDnaSequence()).getRNASequence().getProteinSequence();
	        	
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
	        	} else{
	        		gbkFlatFile +="\n     promoter        complement(" + part.getStartx() + ".." + part.getEndx() + ")";
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        	}        	
        	} else if (part.getFeatureType().matches("terminator")){
	        	if(!part.getReverseComplement()){
	        		gbkFlatFile +="\n     terminator      " + part.getStartx() + ".." + part.getEndx();
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        	} else{
	        		gbkFlatFile +="\n     terminator      complement(" + part.getStartx() + ".." + part.getEndx() + ")";
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        	}  
        	} else if (part.getFeatureType().matches("ncRNA")){
	        	if(!part.getReverseComplement()){
	        		gbkFlatFile +="\n     ncRNA           " + part.getStartx() + ".." + part.getEndx();
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
	        	} else{
	        		gbkFlatFile +="\n     ncRNA           complement(" + part.getStartx() + ".." + part.getEndx() + ")";
	        		gbkFlatFile +="\n                     /gene=\"" + part.getName() +"\"";
	        		gbkFlatFile +="\n                     /locus_tag=\"" + uniqueId + "_" + parts.indexOf(part) + "\"";
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
			e.printStackTrace();
		}
        return gbkFlatFile;
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
    		System.out.println(pathToFile + " file was successfully created");
    	}
    }
}
