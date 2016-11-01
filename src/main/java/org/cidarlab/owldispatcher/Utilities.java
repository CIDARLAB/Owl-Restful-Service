/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prash
 */
public class Utilities {
    
    public static List<String> getFileLines(InputStream inp){
        BufferedReader br = new BufferedReader(new InputStreamReader(inp));
        List<String> lines = new ArrayList<String>();
        String line = "";
        try {
            while((line = br.readLine()) != null){
                if(!line.trim().equals("")){
                    lines.add(line);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
    
    // takes string representations of fasta files in JSON request
    public static List<String> getFastaLines(String filepath){
        List<String> lines = new ArrayList<String>();
        Scanner sc = new Scanner(filepath);
		String line = "";
		while (sc.hasNextLine()) {
			  line = sc.nextLine();
			  if(!line.trim().equals("")){
		      	System.out.println(line);
		      	lines.add(line);
		      }
			}
			sc.close();
        return lines;
    }
    
    public static List<String> getFileLines(String filepath){
        List<String> lines = new ArrayList<String>();
        File file = new File(filepath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while((line = br.readLine()) != null){
                if(!line.trim().equals("")){
                	System.out.println(line);
                	lines.add(line);
                }
            }
            br.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, e);
        }
        return lines;
    }
    
    public static boolean isSolaris() {
        String os = System.getProperty("os.name");
        return isSolaris(os);
    }

    public static boolean isSolaris(String os) {
        if (os.toLowerCase().indexOf("sunos") >= 0) {
            return true;
        }
        return false;
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return isWindows(os);
    }

    public static boolean isWindows(String os) {
        if (os.toLowerCase().indexOf("win") >= 0) {
            return true;
        }
        return false;
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name");
        return isLinux(os);
    }

    public static boolean isLinux(String os) {
        if ((os.toLowerCase().indexOf("nix") >= 0) || (os.indexOf("nux") >= 0) || (os.indexOf("aix") > 0)) {
            return true;
        }
        return false;
    }
    
    public static boolean isMac() {
        String os = System.getProperty("os.name");
        return isMac(os);
    }

    public static boolean isMac(String os) {
        if (os.toLowerCase().indexOf("mac") >= 0) {
            return true;
        }
        return false;
    }
    
    public static String getFilepath() {
        String _filepath = Utilities.class.getClassLoader().getResource(".").getPath();
        if (_filepath.contains("/target/")) {
            _filepath = _filepath.substring(0, _filepath.lastIndexOf("/target/"));
        } else if (_filepath.contains("/src/")) {
            _filepath = _filepath.substring(0, _filepath.lastIndexOf("/src/"));
        } else if (_filepath.contains("/build/classes/")) {
            _filepath = _filepath.substring(0, _filepath.lastIndexOf("/build/classes/"));
        }
        if (Utilities.isWindows()) {
            try {
                _filepath = URLDecoder.decode(_filepath, "utf-8");
                _filepath = new File(_filepath).getPath();
                if (_filepath.contains("\\target\\")) {
                    _filepath = _filepath.substring(0, _filepath.lastIndexOf("\\target\\"));
                } else if (_filepath.contains("\\src\\")) {
                    _filepath = _filepath.substring(0, _filepath.lastIndexOf("\\src\\"));
                } else if (_filepath.contains("\\build\\classes\\")) {
                    _filepath = _filepath.substring(0, _filepath.lastIndexOf("\\build\\classes\\"));
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (_filepath.contains("/target/")) {
                _filepath = _filepath.substring(0, _filepath.lastIndexOf("/target/"));
            } else if (_filepath.contains("/src/")) {
                _filepath = _filepath.substring(0, _filepath.lastIndexOf("/src/"));
            } else if (_filepath.contains("/build/classes/")) {
                _filepath = _filepath.substring(0, _filepath.lastIndexOf("/build/classes/"));
            }
        }
       
        return _filepath;
    }
    
    public static String getResourcesFilepath() {
        String _filepath = getFilepath();
        System.out.println(_filepath);
        if (Utilities.isWindows()) {
            _filepath += "\\src\\main\\resources\\";
        } else {
            _filepath += "/src/main/resources/";
        }
        return _filepath;
    }
    
    public static String getDefautltEugeneRootDirectory(){
        String _filepath = getResourcesFilepath();
        
        if(Utilities.isWindows()){
            _filepath += "sampleEugene\\";
        }
        else{
            _filepath += "sampleEugene/";
        }
        return _filepath;
    }
    
    public static String getOutputFilepath() {
        String _filepath = getFilepath();
        if (Utilities.isWindows()) {
            _filepath += "\\src\\main\\resources\\outputs\\";
        } else {
            _filepath += "/src/main/resources/outputs/";
        }
        return _filepath;
    }
    
    public static void setEugeneRootDirectory(String filepath){
        Args.eugeneRootDirectory = filepath;
    }
    
}
