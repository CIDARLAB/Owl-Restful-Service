package org.cidarlab.owldispatcher.DOM;

/*
 * @author Lloyd M.
*/


public class GenBankFeature extends GenBankFile {
	private String name;
	   private boolean reverseComplement;
	   private boolean isSource;
	   private String forwardColor;
	   private String reverseColor;
	   private int startx;
	   private int endx;
	   private String dnaSequence;
	   private String featureType;
	   private String sourceId;
	   private String genBankId;
	   private String partId;
	   
	   public GenBankFeature() {
	       
	   }
	   
	   public GenBankFeature(String name, boolean reverseComplement, boolean isSource, String forwardColor, String reverseColor, int startx, int endx, String dnaSequence, String featureType, String sourceId, String genBankId, String partId) {
	       super();
		   this.name = name;
	       this.reverseComplement = reverseComplement;
	       this.isSource = isSource;
	       this.forwardColor = forwardColor;
	       this.reverseColor = reverseColor;
	       this.startx = startx;
	       this.endx = endx;
	       this.dnaSequence = dnaSequence;
	       this.featureType = featureType;
	       this.sourceId = sourceId;
	       this.genBankId = genBankId;
	       this.partId = partId;
	   }
	   public void setName(String name) {
	       this.name = name;
	   }
	   
	   public String getName() {
	       return name;
	   }
	   
	   public void setReverseComplement(boolean reverseComplement) {
	       this.reverseComplement = reverseComplement;
	   }
	   
	   public boolean getReverseComplement() {
	       return reverseComplement;
	   }
	   
	   public void setIsSource(boolean isSource) {
	       this.isSource = isSource;
	   }
	   
	   public boolean getIsSource() {
	       return isSource;
	   }
	   
	   public void setForwardColor(String forwardColor) {
	       this.forwardColor = forwardColor;
	   }
	   
	   public String getForwardColor() {
	       return forwardColor;
	   }
	   
	   public void setReverseColor(String newRevCol) {
	       this.reverseColor = newRevCol;
	   }
	   
	   public String getReverseColor() {
	       return reverseColor;
	   }
	   
	   public void setStartx(int startx) {
	       this.startx = startx;
	   }
	   
	   public int getStartx() {
	       return startx;
	   }
	   
	   public void setEndx(int endx) {
	       this.endx = endx;
	   }
	   
	   public int getEndx() {
	       return endx;
	   }
	   
	   public void setDnaSequence(String dnaSequence) {
	       this.dnaSequence = dnaSequence;
	   }
	   
	   public String getDnaSequence() {
	       return dnaSequence;
	   }
	   
	   public void setFeatureType(String featureType) {
	       this.featureType = featureType;
	   }
	   
	   public String getFeatureType() {
	       return featureType;
	   }
	   
	   public void setSourceId(String sourceId) {
	       this.sourceId = sourceId;
	   }
	   
	   public String getSourceId() {
	       return sourceId;
	   }
	 
	   public void setGenBankId(String genBankId) {
	       this.genBankId = genBankId;
	   }
	   
	   public String getGenBankId() {
	       return genBankId;
	   }
	   
	   public void setPartId(String partId) {
	       this.partId = partId;
	   }
	   
	   public String getPartId() {
	       return partId;
	   }
}
