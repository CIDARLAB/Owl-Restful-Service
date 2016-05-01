/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

import org.cidarlab.owldispatcher.adaptors.FastaAdaptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.cidarlab.owldispatcher.Args;
import org.cidarlab.owldispatcher.DOM.DNAcomponent;
import org.cidarlab.owldispatcher.Utilities;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prash
 */
public class FastaAdaptorTest {
    
    private static String user;
    private static String pass = "pass";
    private static String proj;
    public FastaAdaptorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        user = "user" + System.currentTimeMillis();
        proj = "project" + System.currentTimeMillis();
        clothoObject.createUser(user,pass);
        clothoObject.logout();
        conn.closeConnection();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testFastaToClotho(){
        String username = user;
        String password = pass;
        System.out.println("Username :: " + user + "\nPassword :: " + password);
        String filepath = Utilities.getResourcesFilepath() + "test.fasta";
        String project = proj;
        List<String> dnacomponentNames = new ArrayList<String>();
        dnacomponentNames.add("gene1");
        dnacomponentNames.add("gene2");
        
        System.out.println("\n\n######################## Fasta To Clotho");
        System.out.println(FastaAdaptor.fastaToClotho(username, password, filepath,project));
        System.out.println("\n\n######################## Fasta To Clotho");
        System.out.println(FastaAdaptor.fastaToClotho(username, password, filepath,project));
        System.out.println("\n\n######################## List all DNA Components");
        System.out.println(FastaAdaptor.listAllDNAComponents(username, password, project));
        System.out.println("\n\n######################## Get all DNA Components");
        System.out.println(FastaAdaptor.getAllDNAComponents(username, password, project));
        System.out.println("\n\n######################## Get all DNA Component - Names");
        System.out.println(FastaAdaptor.getAllDNAComponents(username, password, project,dnacomponentNames));
        
    }
    
    //@Test
    public void testGetAllDNAComponents(){
        String username = user;
        String password = pass;
        List<DNAcomponent> components = new ArrayList<DNAcomponent>();
        components = FastaAdaptor.getAllDNAComponents(username, password,proj);
        for(DNAcomponent component:components){
            System.out.println(component.getJSON().toString(4));
            System.out.println("\n");
        }
        
    }
    
}
