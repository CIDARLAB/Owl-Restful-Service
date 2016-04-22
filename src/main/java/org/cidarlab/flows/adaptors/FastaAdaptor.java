/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.flows.adaptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cidarlab.flows.Args;
import org.cidarlab.flows.DOM.ComponentType;
import org.cidarlab.flows.DOM.DNAcomponent;
import org.cidarlab.flows.Utilities;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;

/**
 *
 * @author prash
 */
public class FastaAdaptor {
    
    public static void FastaToClotho(String username, String password, String filepath){
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        clothoObject.login(username,password);
        List<DNAcomponent> list = new ArrayList<>();
        list = FastaToComponents(Utilities.getFileLines(filepath));
        
        for(DNAcomponent comp:list){
            clothoObject.create(comp.getMap());
        }
        clothoObject.logout();
        conn.closeConnection();
    }
    
    public static List<DNAcomponent> FastaToComponents(List<String> lines){
        List<DNAcomponent> list = new ArrayList();
        String name="";
        String seq="";
        for(String line:lines){
            
            if(line.startsWith(">") && (line.trim().length()>1)){
                if(!seq.isEmpty() && !name.isEmpty()){
                    DNAcomponent comp = new DNAcomponent(name,ComponentType.gene,seq);
                    list.add(comp);
                    seq = "";
                }
                name = line.substring(1);
            }else{
                seq += line;
            }
        }
        if(!name.isEmpty() && !seq.isEmpty()){
            DNAcomponent comp = new DNAcomponent(name,ComponentType.gene,seq);
            list.add(comp);
        }
        return list;
    } 
    
}
