/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.cidarlab.owldispatcher.Args;
import org.cidarlab.owldispatcher.DOM.ComponentType;
import org.cidarlab.owldispatcher.DOM.DNAcomponent;
import org.cidarlab.owldispatcher.DOM.Project;
import org.cidarlab.owldispatcher.Utilities;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.json.JSONObject;


/**
 *
 * @author prash
 */
public class FastaAdaptor {
    
    
    /*private static void storeDNAcomponentInClotho(List<DNAcomponent> list, Clotho clothoObject){
        
    }*/
    
    public static boolean fastaToClotho(String username, String password, InputStream fasta,String projectId){
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Object loginRet = clothoObject.login(username,password);
        if(loginRet == null){
            conn.closeConnection();
            return false;
        }
        if(loginRet.toString().equals("null")){
            conn.closeConnection();
            return false;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return false;
        }
        
        
        Object projObj = clothoObject.get(projectId);
        if(projObj != null){
            System.out.println("PROJECT EXISTS!!!");
            clothoObject.logout();
            conn.closeConnection();
            return false;
        }
        

        //System.out.println("Proj :: " + projObj.toString());
        
        List<DNAcomponent> list = new ArrayList<>();
        list = fastaToComponents(Utilities.getFileLines(fasta));
        List<String> dnacompids = new ArrayList<String>();
        for(DNAcomponent comp:list){
            dnacompids.add((String)clothoObject.create(comp.getMap()));
        }
        Project proj = new Project(projectId,dnacompids);
        clothoObject.create(proj.getMap());
        clothoObject.logout();
        conn.closeConnection();
        return true;
    }
    
    
    public static boolean fastaToClotho(String username, String password, InputStream fasta,String projectId, ComponentType type){
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Object loginRet = clothoObject.login(username,password);
        if(loginRet == null){
            conn.closeConnection();
            return false;
        }
        if(loginRet.toString().equals("null")){
            conn.closeConnection();
            return false;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return false;
        }
        
        
        Object projObj = clothoObject.get(projectId);
        if(projObj != null){
            System.out.println("PROJECT EXISTS!!!");
            clothoObject.logout();
            conn.closeConnection();
            return false;
        }
        

        //System.out.println("Proj :: " + projObj.toString());
        
        List<DNAcomponent> list = new ArrayList<>();
        list = fastaToComponents(Utilities.getFileLines(fasta));
        List<String> dnacompids = new ArrayList<String>();
        for(DNAcomponent comp:list){
            dnacompids.add((String)clothoObject.create(comp.getMap()));
        }
        Project proj = new Project(projectId,dnacompids);
        clothoObject.create(proj.getMap());
        clothoObject.logout();
        conn.closeConnection();
        return true;
    }
    
    
    public static boolean fastaToClotho(String username, String password, String filepath,String projectId){
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Object loginRet = clothoObject.login(username,password);
        if(loginRet == null){
            conn.closeConnection();
            return false;
        }
        if(loginRet.toString().equals("null")){
            conn.closeConnection();
            return false;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return false;
        }
        
        
        Object projObj = clothoObject.get(projectId);
        if(projObj != null){
            System.out.println("PROJECT EXISTS!!!");
            clothoObject.logout();
            conn.closeConnection();
            return false;
        }
        

        //System.out.println("Proj :: " + projObj.toString());
        
        List<DNAcomponent> list = new ArrayList<>();
        list = fastaToComponents(Utilities.getFileLines(filepath));
        List<String> dnacompids = new ArrayList<String>();
        for(DNAcomponent comp:list){
            dnacompids.add((String)clothoObject.create(comp.getMap()));
        }
        Project proj = new Project(projectId,dnacompids);
        clothoObject.create(proj.getMap());
        clothoObject.logout();
        conn.closeConnection();
        return true;
    }
    
    
    public static List<DNAcomponent> getAllDNAComponents(String username, String password,String projectId,List<String> DNAcomponentNames){
        List<DNAcomponent> components = new ArrayList<DNAcomponent>();
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Object loginRet = clothoObject.login(username,password);
        if(loginRet == null){
            conn.closeConnection();
            return null;
        }
        if(loginRet.toString().equals("null")){
            conn.closeConnection();
            return null;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return null;
        }
        
        Map projObj = (Map)clothoObject.get(projectId);
        if(projObj == null){
            System.out.println("PROJECT DOES NOT EXIST!!!");
            clothoObject.logout();
            conn.closeConnection();
            return null;
        }
        
        //System.out.println("Project Object :: " + projObj.toString());
        List<String> componentids = new ArrayList<String>();
        componentids = (List<String>)projObj.get("dnacomponentIds");
        
        for(String componentid:componentids){
            Map dnaObj = (Map)clothoObject.get(componentid);
            if(DNAcomponentNames.contains((String)dnaObj.get("name"))){
                    components.add(DNAcomponent.fromMap(dnaObj));
            }
        }
        clothoObject.logout();
        conn.closeConnection();
        return components;
    }
    
    
    public static List<DNAcomponent> getAllDNAComponents(String username, String password,String projectId){
        List<DNAcomponent> components = new ArrayList<DNAcomponent>();
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Object loginRet = clothoObject.login(username,password);
        if(loginRet == null){
            conn.closeConnection();
            return null;
        }
        if(loginRet.toString().equals("null")){
            conn.closeConnection();
            return null;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return null;
        }
        
        Map projObj = (Map)clothoObject.get(projectId);
        if(projObj == null){
            System.out.println("PROJECT DOES NOT EXIST!!!");
            clothoObject.logout();
            conn.closeConnection();
            return null;
        }
        
        //System.out.println("Project Object :: " + projObj.toString());
        List<String> componentids = new ArrayList<String>();
        componentids = (List<String>)projObj.get("dnacomponentIds");
        
        for(String componentid:componentids){
            Map dnaObj = (Map)clothoObject.get(componentid);
            components.add(DNAcomponent.fromMap(dnaObj));
        }
        clothoObject.logout();
        conn.closeConnection();
        return components;
    }
    
    public static List<String> listAllDNAComponents(String username, String password,String projectId){
        List<String> componentids = new ArrayList<String>();
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        Object loginRet = clothoObject.login(username,password);
        if(loginRet == null){
            conn.closeConnection();
            return null;
        }
        if(loginRet.toString().equals("null")){
            conn.closeConnection();
            return null;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return null;
        }
        
        Map projObj = (Map)clothoObject.get(projectId);
        if(projObj == null){
            System.out.println("PROJECT DOES NOT EXIST!!!");
            clothoObject.logout();
            conn.closeConnection();
            return null;
        }
        
        //System.out.println("Project Object :: " + projObj.toString());
        componentids = (List<String>)projObj.get("dnacomponentIds");
        List<String> componentNames = new ArrayList<String>();
        
        for(String componentid:componentids){
            Map dnaObj = (Map)clothoObject.get(componentid);
            componentNames.add((String)dnaObj.get("name"));
        }
        clothoObject.logout();
        conn.closeConnection();
        return componentNames;
    }
    
    public static List<DNAcomponent> fastaToComponents(List<String> lines){
        List<DNAcomponent> list = new ArrayList();
        String name="";
        String seq="";
        for(String line:lines){
            
            if(line.startsWith(">") && (line.trim().length()>1)){
                if(!seq.isEmpty() && !name.isEmpty()){
                    String dnaSeq = ReverseTranslate.translate(seq);
                    DNAcomponent comp = new DNAcomponent(name,ComponentType.gene,dnaSeq);
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
