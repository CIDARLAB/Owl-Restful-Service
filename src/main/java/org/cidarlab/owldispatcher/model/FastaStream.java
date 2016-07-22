/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author prash
 */
public class FastaStream {
    
    // @Getter
    // @Setter
    private String device;
    
    // @Getter
    // @Setter
    private String fastafile = "";
    
    
    public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getFastafile() {
		return fastafile;
	}
	public void setFastafile(String fastafile) {
		this.fastafile = fastafile;
	}
	public FastaStream(String _device, List<String> fileLines){
        this.device = _device;
        for(int i=0;i<fileLines.size()-1;i++){
            this.fastafile += (fileLines.get(i) + "\n");
        }
        this.fastafile += fileLines.get(fileLines.size()-1);
    }
    public FastaStream(String _device, String fileLines){
        this.device = _device;
        this.fastafile = fileLines;
    }
    
}
