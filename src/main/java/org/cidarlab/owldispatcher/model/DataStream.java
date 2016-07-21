package org.cidarlab.owldispatcher.model;


public class DataStream {

	private String fasta;
	private Boolean withRybozyme;
	private String reverseTranslate;
	
	
	public String getReverseTranslate() {
		return reverseTranslate;
	}
	public void setReverseTranslate(String reverseTranslate) {
		this.reverseTranslate = reverseTranslate;
	}
	public String getFasta() {
		return fasta;
	}
	public void setFasta(String fasta) {
		this.fasta = fasta;
	}
	public Boolean getWithRybozyme() {
		return withRybozyme;
	}
	public void setWithRybozyme(Boolean withRybozyme) {
		this.withRybozyme = withRybozyme;
	} 	
	
}
