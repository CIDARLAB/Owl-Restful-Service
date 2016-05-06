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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.eugene.dom.imp.container.EugeneCollection;
import org.cidarlab.owldispatcher.Args;
import org.cidarlab.owldispatcher.DOM.ComponentType;
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
        String promoterfilepath = Utilities.getResourcesFilepath() + "promoters.fasta";
        String ribozymefilepath = Utilities.getResourcesFilepath() + "ribozymes.fasta";
        String rbsfilepath = Utilities.getResourcesFilepath() + "rbs.fasta";
        String genefilepath = Utilities.getResourcesFilepath() + "proteins.fasta";
        String terminatorfilepath = Utilities.getResourcesFilepath() + "terminators.fasta";
        String project = proj;

        List<String> dnacomponentNames = new ArrayList<String>();
        dnacomponentNames.add("gene1");
        dnacomponentNames.add("gene2");

        System.out.println("\n\n######################## Fasta To Clotho Promoters");
        System.out.println(FastaAdaptor.fastaToClotho(username, password, promoterfilepath,project,ComponentType.PROMOTER));
        System.out.println("\n\n######################## Fasta To Clotho Ribozymes");
        System.out.println(FastaAdaptor.fastaToClotho(username, password, ribozymefilepath,project,ComponentType.RIBOZYME));
        System.out.println("\n\n######################## Fasta To Clotho RBS");
        System.out.println(FastaAdaptor.fastaToClotho(username, password, rbsfilepath,project,ComponentType.RBS));
        System.out.println("\n\n######################## Fasta To Clotho Proteins");
        System.out.println(FastaAdaptor.fastaToClotho(username, password, genefilepath,project,ComponentType.PROTEIN));
        System.out.println("\n\n######################## Fasta To Clotho Terminators");
        System.out.println(FastaAdaptor.fastaToClotho(username, password, terminatorfilepath,project,ComponentType.TERMINATOR));
        System.out.println("\n\n######################## List all DNA Components");
        System.out.println(FastaAdaptor.listAllDNAComponents(username, password, project));
        System.out.println("\n\n######################## Get all DNA Components");
        System.out.println(FastaAdaptor.getAllDNAComponents(username, password, project));
        System.out.println("\n\n######################## Get all DNA Component - Names");
        System.out.println(FastaAdaptor.getAllDNAComponents(username, password, project,dnacomponentNames));


        System.out.println("\n\n######################## Eugene Script");
        
        List<DNAcomponent> promoters = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.PROMOTER);
        List<DNAcomponent> genes = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.GENE);
        List<DNAcomponent> ribozymes = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.RIBOZYME);
        List<DNAcomponent> rbs = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.RBS);
        List<DNAcomponent> terminators = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.TERMINATOR);
        
        
        String script = EugeneAdaptor.createEugeneScript(promoters,ribozymes,rbs,genes,terminators, ribozymes.size() > 0);
        System.out.println("\n\n######################## Script");
        System.out.println(script);
        //System.out.println("\n\n######################## Run Eugene Locally");
        //EugeneCollection collection = EugeneAdaptor.runEugene(script);
        //System.out.println("Collection :: \n" + collection.toString());
        
        System.out.println("\n\n######################## Run Eugene via XmlRpc");
        try {
            new EugeneAdaptor().startEugeneXmlRpc(script);
        } catch (Exception ex) {
            Logger.getLogger(FastaAdaptorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
