/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import org.cidarlab.eugene.dom.Component;
import org.cidarlab.eugene.dom.Device;
import org.cidarlab.eugene.dom.NamedElement;
import org.cidarlab.eugene.exception.EugeneException;
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
    
    
    public static List<String> getFastaFileLines(Device device){
        List<String> lines = new ArrayList<String>();
        for(List<NamedElement> listnamedElement : device.getComponents()){
                for(NamedElement ne : listnamedElement){
                    Component component = (Component)ne;
                    
                    try {
                        String componentName = component.getPropertyValues().get("name").toString().replaceAll("\"", "");
                        System.out.println(componentName);
                        lines.add(">" + componentName);
                        lines.add(component.getSequence());
                        
                    } catch (EugeneException ex) {
                        Logger.getLogger(FastaAdaptor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        }
        return lines;
    }
    
    public static String createFastaFile(Device device,String filepath){
        String fastafilepath = filepath + device.getName() + ".fasta";
        File file = new File(fastafilepath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for(List<NamedElement> listnamedElement : device.getComponents()){
                for(NamedElement ne : listnamedElement){
                    Component component = (Component)ne;
                    writer.write(">"+component.getPropertyValues().get("name"));
                    writer.newLine();
                    writer.write(component.getSequence());
                    writer.newLine();
                }
            }
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(FastaAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EugeneException ex) {
            Logger.getLogger(FastaAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return fastafilepath;
    }
    
    private static void setProjCompIds(List<String> finalIdList, ComponentType type, Project proj){
        switch (type) {
            case PROMOTER:
                proj.setPromoters(finalIdList);
                break;
            case RIBOZYME:
                proj.setRibozymes(finalIdList);
                break;
            case RBS:
                proj.setRbs(finalIdList);
                break;
            case GENE:
                proj.setGenes(finalIdList);
                break;
            case TERMINATOR:
                proj.setTerminators(finalIdList);
                break;
            default:
                break;
        }
    }
    
    public static boolean fastaToClotho(String username, String password, List<DNAcomponent> list, String projectId, ComponentType type) {
        ComponentType modifiedType;
        if(type.equals(ComponentType.PROTEIN)){
            modifiedType = ComponentType.GENE;
        }else{
            modifiedType = type;
        }
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Object loginRet = clothoObject.login(username, password);
        if (loginRet == null) {
            conn.closeConnection();
            return false;
        }
        if (loginRet.toString().equals("null")) {
            conn.closeConnection();
            return false;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return false;
        }
        Map projQuery = new HashMap();
        projQuery.put("name", projectId);
        projQuery.put("owner", username);
        projQuery.put("schema", Project.class.getCanonicalName());
        JSONArray projQueryResults = (JSONArray) clothoObject.query(projQuery);

        if (projQueryResults == null) {
            //Is this supposed to happen?
            System.out.println("Project Query Returned a null in fasta To Clotho");
            return false;
        }
        if (projQueryResults.size() > 1) {
            System.out.println("User has more than 1 project with the same Project Id. Please provide a unique project name");
            return false;
        }
        boolean newProject = false;
        if (projQueryResults.isEmpty()) {
            newProject = true;
        }

        boolean typeExists = false;
        List<DNAcomponent> existingComponents = new ArrayList<DNAcomponent>();
        Set<String> existingNames = new HashSet<String>();
        Set<String> existingIds = new HashSet<String>();
        if (!newProject) {
            List<String> typeIds = new ArrayList<String>();
            typeIds = (List<String>) (((Map) projQueryResults.get(0)).get(modifiedType.toString().toLowerCase()));
            if (!typeIds.isEmpty()) {
                for (String id : typeIds) {
                    Map compMap = new HashMap();
                    compMap = (Map) clothoObject.get(id);
                    existingComponents.add(DNAcomponent.fromMap(compMap));
                    existingNames.add((String) compMap.get("name"));
                    existingIds.add((String)compMap.get("id"));
                }
                typeExists = true;
            }
        }
        
        if(newProject){
            Project proj = new Project(projectId,username);
            List<String> finalIdList = new ArrayList<String>();
            for (DNAcomponent comp : list) {
                finalIdList.add((String) clothoObject.create(comp.getMap()));
            }
            setProjCompIds(finalIdList, modifiedType,proj);
            clothoObject.create(proj.getMap());
        }
        else{
            Project proj = Project.fromMap((Map)(projQueryResults.get(0)));
            List<String> dnacompids = new ArrayList<String>();
            List<String> finalIdList = new ArrayList<String>();
            if (typeExists) {
                for (DNAcomponent comp : list) {
                    if (!existingNames.contains(comp.getName())) {
                        dnacompids.add((String) clothoObject.create(comp.getMap()));
                    }
                }
                finalIdList.addAll(dnacompids);
                finalIdList.addAll(existingIds);
            } else {
                for (DNAcomponent comp : list) {
                    finalIdList.add((String) clothoObject.create(comp.getMap()));
                }
            }
            setProjCompIds(finalIdList, modifiedType,proj);
            clothoObject.set(proj.getMap());
        }
        
        clothoObject.logout();
        conn.closeConnection();
        return true;
    }

    public static boolean fastaToClotho(String username, String password, InputStream fasta, String projectId, ComponentType type) {

        List<DNAcomponent> list = new ArrayList<>();
        list = fastaToComponents(Utilities.getFileLines(fasta), type);
        return fastaToClotho(username, password, list, projectId, type);
    }

    public static boolean fastaToClotho(String username, String password, String filepath, String projectId, ComponentType type) {
        
        List<DNAcomponent> list = new ArrayList<>();
        list = fastaToComponents(Utilities.getFileLines(filepath), type);
        return fastaToClotho(username, password, list, projectId, type);
    }

    public static List<DNAcomponent> getAllDNAComponents(String username, String password, String projectId, List<String> DNAcomponentNames) {
        List<DNAcomponent> components = new ArrayList<DNAcomponent>();
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Object loginRet = clothoObject.login(username, password);
        if (loginRet == null) {
            conn.closeConnection();
            return null;
        }
        if (loginRet.toString().equals("null")) {
            conn.closeConnection();
            return null;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return null;
        }
        
        Map projQuery = new HashMap();
        projQuery.put("name", projectId);
        projQuery.put("owner",username);
        projQuery.put("schema", Project.class.getCanonicalName());
        Map projObj = (Map) clothoObject.queryOne(projQuery);
        if (projObj == null) {
            System.out.println("PROJECT DOES NOT EXIST!!!");
            clothoObject.logout();
            conn.closeConnection();
            return null;
        }
        
        //System.out.println("Project Object :: " + projObj.toString());
        List<String> componentids = new ArrayList<String>();
        List<String> promoterIds = new ArrayList<String>();
        List<String> ribozymeIds = new ArrayList<String>();
        List<String> rbsIds = new ArrayList<String>();
        List<String> geneIds = new ArrayList<String>();
        List<String> terminatorIds = new ArrayList<String>();
        promoterIds = (List<String>) projObj.get(ComponentType.PROMOTER.toString().toLowerCase());
        ribozymeIds = (List<String>) projObj.get(ComponentType.RIBOZYME.toString().toLowerCase());
        rbsIds = (List<String>) projObj.get(ComponentType.RBS.toString().toLowerCase());
        geneIds = (List<String>) projObj.get(ComponentType.GENE.toString().toLowerCase());
        terminatorIds = (List<String>) projObj.get(ComponentType.TERMINATOR.toString().toLowerCase());
        
        componentids.addAll(promoterIds);
        componentids.addAll(ribozymeIds);
        componentids.addAll(rbsIds);
        componentids.addAll(geneIds);
        componentids.addAll(terminatorIds);
        
        
        for (String componentid : componentids) {
            Map dnaObj = (Map) clothoObject.get(componentid);
            if (DNAcomponentNames.contains((String) dnaObj.get("name"))) {
                components.add(DNAcomponent.fromMap(dnaObj));
            }
        }
        clothoObject.logout();
        conn.closeConnection();
        return components;
    }

    public static List<DNAcomponent> getAllDNAComponents(String username, String password, String projectId, ComponentType type) {
        List<DNAcomponent> components = new ArrayList<DNAcomponent>();
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Object loginRet = clothoObject.login(username, password);
        if (loginRet == null) {
            conn.closeConnection();
            return null;
        }
        if (loginRet.toString().equals("null")) {
            conn.closeConnection();
            return null;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return null;
        }
        
        Map projQuery = new HashMap();
        projQuery.put("name", projectId);
        projQuery.put("owner",username);
        projQuery.put("schema", Project.class.getCanonicalName());
        Map projObj = (Map) clothoObject.queryOne(projQuery);
        if (projObj == null) {
            System.out.println("PROJECT DOES NOT EXIST!!!");
            clothoObject.logout();
            conn.closeConnection();
            return null;
        }
        
        //System.out.println("Project Object :: " + projObj.toString());
        List<String> componentids = new ArrayList<String>();
        componentids = (List<String>) projObj.get(type.toString().toLowerCase());
        
        for (String componentid : componentids) {
            Map dnaObj = (Map) clothoObject.get(componentid);
                components.add(DNAcomponent.fromMap(dnaObj));
        }
        clothoObject.logout();
        conn.closeConnection();
        return components;
    }
    
    public static List<DNAcomponent> getAllDNAComponents(String username, String password, String projectId) {
        List<DNAcomponent> components = new ArrayList<DNAcomponent>();
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Object loginRet = clothoObject.login(username, password);
        if (loginRet == null) {
            conn.closeConnection();
            return null;
        }
        if (loginRet.toString().equals("null")) {
            conn.closeConnection();
            return null;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return null;
        }

        Map projQuery = new HashMap();
        projQuery.put("name", projectId);
        projQuery.put("owner",username);
        projQuery.put("schema", Project.class.getCanonicalName());
        Map projObj = (Map) clothoObject.queryOne(projQuery);
        if (projObj == null) {
            System.out.println("PROJECT DOES NOT EXIST!!!");
            clothoObject.logout();
            conn.closeConnection();
            return null;
        }

        //System.out.println("Project Object :: " + projObj.toString());
        List<String> componentids = new ArrayList<String>();
        List<String> promoterIds = new ArrayList<String>();
        List<String> ribozymeIds = new ArrayList<String>();
        List<String> rbsIds = new ArrayList<String>();
        List<String> geneIds = new ArrayList<String>();
        List<String> terminatorIds = new ArrayList<String>();
        promoterIds = (List<String>) projObj.get(ComponentType.PROMOTER.toString().toLowerCase());
        ribozymeIds = (List<String>) projObj.get(ComponentType.RIBOZYME.toString().toLowerCase());
        rbsIds = (List<String>) projObj.get(ComponentType.RBS.toString().toLowerCase());
        geneIds = (List<String>) projObj.get(ComponentType.GENE.toString().toLowerCase());
        terminatorIds = (List<String>) projObj.get(ComponentType.TERMINATOR.toString().toLowerCase());
        
        componentids.addAll(promoterIds);
        componentids.addAll(ribozymeIds);
        componentids.addAll(rbsIds);
        componentids.addAll(geneIds);
        componentids.addAll(terminatorIds);

        for (String componentid : componentids) {
            Map dnaObj = (Map) clothoObject.get(componentid);
            components.add(DNAcomponent.fromMap(dnaObj));
        }
        clothoObject.logout();
        conn.closeConnection();
        return components;
    }

    public static List<String> listAllDNAComponents(String username, String password, String projectId) {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        Object loginRet = clothoObject.login(username, password);
        if (loginRet == null) {
            conn.closeConnection();
            return null;
        }
        if (loginRet.toString().equals("null")) {
            conn.closeConnection();
            return null;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return null;
        }

        Map projQuery = new HashMap();
        projQuery.put("name", projectId);
        projQuery.put("owner",username);
        projQuery.put("schema", Project.class.getCanonicalName());
        Map projObj = (Map) clothoObject.queryOne(projQuery);
        if (projObj == null) {
            System.out.println("PROJECT DOES NOT EXIST!!!");
            clothoObject.logout();
            conn.closeConnection();
            return null;
        }

        //System.out.println("Project Object :: " + projObj.toString());
        List<String> componentids = new ArrayList<String>();
        List<String> promoterIds = new ArrayList<String>();
        List<String> ribozymeIds = new ArrayList<String>();
        List<String> rbsIds = new ArrayList<String>();
        List<String> geneIds = new ArrayList<String>();
        List<String> terminatorIds = new ArrayList<String>();
        promoterIds = (List<String>) projObj.get(ComponentType.PROMOTER.toString().toLowerCase());
        ribozymeIds = (List<String>) projObj.get(ComponentType.RIBOZYME.toString().toLowerCase());
        rbsIds = (List<String>) projObj.get(ComponentType.RBS.toString().toLowerCase());
        geneIds = (List<String>) projObj.get(ComponentType.GENE.toString().toLowerCase());
        terminatorIds = (List<String>) projObj.get(ComponentType.TERMINATOR.toString().toLowerCase());
        
        componentids.addAll(promoterIds);
        componentids.addAll(ribozymeIds);
        componentids.addAll(rbsIds);
        componentids.addAll(geneIds);
        componentids.addAll(terminatorIds);
        List<String> componentNames = new ArrayList<String>();

        for (String componentid : componentids) {
            Map dnaObj = (Map) clothoObject.get(componentid);
            componentNames.add((String) dnaObj.get("name"));
        }
        clothoObject.logout();
        conn.closeConnection();
        return componentNames;
    }

    public static List<DNAcomponent> fastaToComponents(List<String> lines, ComponentType type) {
        List<DNAcomponent> list = new ArrayList();
        String name = "";
        String seq = "";
        for (String line : lines) {

            if (line.startsWith(">") && (line.trim().length() > 1)) {
                if (!seq.isEmpty() && !name.isEmpty()) {
                    if (type.equals(ComponentType.PROTEIN)) {
                        String dnaSeq = ReverseTranslate.translate(seq);
                        DNAcomponent comp = new DNAcomponent(name, ComponentType.GENE, dnaSeq);
                        list.add(comp);
                    }
                    else{
                        DNAcomponent comp = new DNAcomponent(name, type, seq);
                        list.add(comp);
                    
                    }
                    seq = "";
                }
                name = line.substring(1);
            } else {
                seq += line;
            }
        }
        if (!name.isEmpty() && !seq.isEmpty()) {
            if (type.equals(ComponentType.PROTEIN)) {
                String dnaSeq = ReverseTranslate.translate(seq);
                DNAcomponent comp = new DNAcomponent(name, ComponentType.GENE, dnaSeq);
                list.add(comp);
            } else {
                DNAcomponent comp = new DNAcomponent(name, type, seq);
                list.add(comp);

            }
        }
        return list;
    }

}
