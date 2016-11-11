package org.cidarlab.owldispatcher.DOM;

import lombok.Getter;
import lombok.Setter;

/*
 * @author Lloyd M.
 * @author Yury V. Ivanov
*/


public class GenBankFeature extends GenBankFile {
	
	@Getter @Setter
	private String name;
	@Getter @Setter
	private boolean reverseComplement;
	@Getter @Setter
	private boolean isSource;
	@Getter @Setter
	private int startx;
	@Getter @Setter
	private int endx;
	@Getter @Setter
	private String dnaSequence;
	@Getter @Setter
	private String featureType;
	@Getter @Setter
	private String sourceId;
	@Getter @Setter
	private String genBankId;
	@Getter @Setter
	private String partId;
	@Getter @Setter
	private String proteinSequence;
   
	   public GenBankFeature() {
	       
	   }
	   
	   public GenBankFeature(String name, boolean reverseComplement, boolean isSource, int startx, int endx, String dnaSequence, String featureType, String sourceId, String genBankId, String partId, String proteinSequence) {
	       super();
		   this.name = name;
	       this.reverseComplement = reverseComplement;
	       this.isSource = isSource;
	       this.startx = startx;
	       this.endx = endx;
	       this.dnaSequence = dnaSequence;
	       this.featureType = featureType;
	       this.sourceId = sourceId;
	       this.genBankId = genBankId;
	       this.partId = partId;
	       this.proteinSequence = proteinSequence;
	   }
  
}
