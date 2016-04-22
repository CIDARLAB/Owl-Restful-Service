/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.flows.adaptors;

import java.util.List;
import java.util.Map;
import org.cidarlab.flows.Args;
import org.cidarlab.flows.DOM.DNAcomponent;
import org.cidarlab.flows.Utilities;
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
    
    public FastaAdaptorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        user = "user" + System.currentTimeMillis();
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
        String filepath = Utilities.getResourcesFilepath() + "test.fasta";
        FastaAdaptor.FastaToClotho(username, password, filepath);
        
    }
    
}
