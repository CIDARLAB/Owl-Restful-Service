package org.cidarlab.owldispatcher.DOM;

/*
 * @author Yury V. Ivanov
*/


public class GenBankFile {
	
	private String fullSequence;
	private String accession;
	private String oldAccession;
	
	
	
	public String getOldAccession() {
		return oldAccession;
	}

	public void setOldAccession(String oldAccession) {
		this.oldAccession = oldAccession;
	}

	public String getFullSequence() {
		return fullSequence;
	}

	public void setFullSequence(String fullSequence) {
		this.fullSequence = fullSequence;
	}

	public String getAccession() {
		return accession;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}
}
