package org.cidarlab.owldispatcher.model;

import java.util.ArrayList;
import java.util.List;

public class DataStreamJira {


	private String myProjectId;
	private Boolean withRibozyme;
	private String designMethod;
	private String inputPromotersFasta;
	private String inputRbsFasta;
	private String inputRibozymesFasta;
	private String inputProteinsFasta;
	private String inputTerminatorsFasta;
	private String array;
	private List<FastaStream> fastaFiles = new ArrayList<FastaStream>();
        
        public List<FastaStream> getFastaFiles(){
            return fastaFiles;
        }
        
        public void setFastaFiles(List<FastaStream> _fastaFiles){
            this.fastaFiles = _fastaFiles;
        }
        
        public void addFastaFile(FastaStream fasta){
            this.fastaFiles.add(fasta);
        }
        
        /*private List<String> fastaFiles = new ArrayList<String>();
	
        public List<String> getFastaFiles(){
            return fastaFiles;
        }
	
        public void setFastaFiles(List<String> _fastaFiles){
            this.fastaFiles = _fastaFiles;
        }
        
        public void addFastaFile(String fastaFile){
            this.fastaFiles.add(fastaFile);
        }*/
        
	public String getArray() {
		return array;
	}

	public void setArray(String array) {
		this.array = array;
	}

	public String getMyProjectId() {
		return myProjectId;
	}

	public void setMyProjectId(String myProjectId) {
		this.myProjectId = myProjectId;
	}

	public Boolean getWithRibozyme() {
		return withRibozyme;
	}

	public void setWithRibozyme(Boolean withRibozyme) {
		this.withRibozyme = withRibozyme;
	}

	public String getDesignMethod() {
		return designMethod;
	}

	public void setDesignMethod(String designMethod) {
		this.designMethod = designMethod;
	}

	public String getInputPromotersFasta() {
		return inputPromotersFasta;
	}

	public void setInputPromotersFasta(String inputPromotersFasta) {
		this.inputPromotersFasta = inputPromotersFasta;
	}

	public String getInputRbsFasta() {
		return inputRbsFasta;
	}

	public void setInputRbsFasta(String inputRbsFasta) {
		this.inputRbsFasta = inputRbsFasta;
	}

	public String getInputRibozymesFasta() {
		return inputRibozymesFasta;
	}

	public void setInputRibozymesFasta(String inputRibozymesFasta) {
		this.inputRibozymesFasta = inputRibozymesFasta;
	}

	public String getInputProteinsFasta() {
		return inputProteinsFasta;
	}

	public void setInputProteinsFasta(String inputProteinsFasta) {
		this.inputProteinsFasta = inputProteinsFasta;
	}

	public String getInputTerminatorsFasta() {
		return inputTerminatorsFasta;
	}

	public void setInputTerminatorsFasta(String inputTerminatorsFasta) {
		this.inputTerminatorsFasta = inputTerminatorsFasta;
	}
	
}
