/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.eugene.Eugene;
import org.cidarlab.eugene.exception.EugeneException;

/**
 *
 * @author prash
 */
public class EugeneAdaptor {
    
    public static void runEugene(String filepath){
        File file = new File(filepath);
        try {
            Eugene eugene = new Eugene();
            eugene.setRootDirectory(filepath);
            eugene.executeFile(file);
        } catch (EugeneException ex) {
            Logger.getLogger(EugeneAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public static void main(String[] args) {
        String file = "app.eug";
        runEugene(file);
    }
    
}
