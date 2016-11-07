    package org.cidarlab.owldispatcher.controller;

    import java.io.File;
import java.io.IOException;

/*
     * @author Yury V. Ivanov
    */

    
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.eugene.dom.Device;
import org.cidarlab.eugene.dom.NamedElement;
import org.cidarlab.eugene.dom.imp.container.EugeneArray;
import org.cidarlab.owldispatcher.Utilities;
import org.cidarlab.owldispatcher.DOM.ComponentType;
import org.cidarlab.owldispatcher.DOM.DNAcomponent;
import org.cidarlab.owldispatcher.DOM.GenBankFeature;
import org.cidarlab.owldispatcher.adaptors.EugeneAdaptor;
import org.cidarlab.owldispatcher.adaptors.ExportGenBank;
import org.cidarlab.owldispatcher.adaptors.FastaAdaptor;
import org.cidarlab.owldispatcher.adaptors.GenBankImporter;
import org.cidarlab.owldispatcher.adaptors.PigeonClient;
import org.cidarlab.owldispatcher.adaptors.ZipFileUtil;
import org.cidarlab.owldispatcher.exception.BadRequestException;
import org.cidarlab.owldispatcher.model.DataStreamJira;
import org.cidarlab.owldispatcher.model.FastaStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    public class RestfulController {

        private static String testUser = "Foundry";
        //private static String testProject = "testProjectNew";
        private static String testPassword = "Broadies2016";

        @RequestMapping(value = "/json")
        public ResponseEntity<DataStreamJira> getJson() {
        	DataStreamJira dataStreamJira = new DataStreamJira();
            
            dataStreamJira.setDesignMethod("Monocistronic_prgt");
            dataStreamJira.setWithRibozyme(false);
            dataStreamJira.setInputPromotersFasta(">pT7\nATGCGATCGATCGATCG\n>pBla\nATCGTAGCTAGCTAGCTA");
            dataStreamJira.setInputRbsFasta(">RBS_1\nATGCTAGCTGATCGTA");
            dataStreamJira.setInputRibozymesFasta(">ri1\nATGATCGATCGATCGGCTAGCTA");
            dataStreamJira.setInputProteinsFasta(">gene1\nATGCTAGCTAGCTA\n>gene2\nTGATCGATCGATCAC\n>gene3\nATGCTAGCTAGCTA\n>gene4\nTGATCGATCGATCAC\n"
            									+ ">gene5\nATGCTAGCTAGCTA\n>gene6\nTGATCGATCGATCAC\n>gene7\nATGCTAGCTAGCTA\n>gene8\nTGATCGATCGATCAC\n");
            dataStreamJira.setInputTerminatorsFasta(">t1\nATCGATCGATCGATCGAT");
            	
            return new ResponseEntity<DataStreamJira>(dataStreamJira, HttpStatus.OK);
            }
        
/*        @RequestMapping(value = "/create-test-user")
        public String stTest() {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            clothoObject.createUser(testUser, testPassword);
            System.out.println("Username :: " + testUser + "\nPassword :: " + testPassword);
            clothoObject.logout();
            conn.closeConnection();
            return "Username :: " + testUser + "\nPassword :: " + testPassword + "\nProject :: " + testProject;
        }
*/

/*        @RequestMapping(value = "/example")
        public ResponseEntity<DataStreamJira> exampl() {
        	
            DataStreamJira dataStreamJira = new DataStreamJira();
            
            dataStreamJira.setDesignMethod("Exhaustive");
            dataStreamJira.setWithRybozyme(false);
            dataStreamJira.setInputPromotersFasta(">pT7\nATGCGATCGATCGATCG\n>pBla\nATGCTAGCTAGCTAGCTTAA");
            dataStreamJira.setInputRbsFasta(">RBS_1\nATGCTAGCTGATCGTA\n>RBS_2\nATGCTGATCGATCGATCGAT>");
            dataStreamJira.setInputRybozymesFasta(">ri1\nATGATCGATCGATCGGCTAGCTA");
            dataStreamJira.setInputProteinsFasta(">gene1\nATGCTAGCTAGCTA\n>gene2\nTGATCGATCGATCAC");
            dataStreamJira.setInputTerminatorsFasta(">t1\nATCGATCGATCGATCGAT\n>t2\nATCGATCGATCGATC");
            
            String promoterfilepath = dataStreamJira.getInputPromotersFasta();
            String ribozymefilepath = dataStreamJira.getInputRybozymesFasta();
            String rbsfilepath = dataStreamJira.getInputRbsFasta();
            String genefilepath = dataStreamJira.getInputProteinsFasta();
            String terminatorfilepath = dataStreamJira.getInputTerminatorsFasta();
            
            String username = testUser;
            String project = testProject;
            String password = testPassword;
            
            System.out.println("\n\n######################## Fasta To Clotho Promoters");
            System.out.println(FastaAdaptor.fastaToClotho(username, password, promoterfilepath, project, ComponentType.PROMOTER));
            System.out.println("\n\n######################## Fasta To Clotho Ribozymes");
            System.out.println(FastaAdaptor.fastaToClotho(username, password, ribozymefilepath, project, ComponentType.RIBOZYME));
            System.out.println("\n\n######################## Fasta To Clotho RBS");
            System.out.println(FastaAdaptor.fastaToClotho(username, password, rbsfilepath, project, ComponentType.RBS));
            System.out.println("\n\n######################## Fasta To Clotho Proteins");
            System.out.println(FastaAdaptor.fastaToClotho(username, password, genefilepath, project, ComponentType.PROTEIN));
            System.out.println("\n\n######################## Fasta To Clotho Terminators");
            System.out.println(FastaAdaptor.fastaToClotho(username, password, terminatorfilepath, project, ComponentType.TERMINATOR));
            
            List<DNAcomponent> promoters = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.PROMOTER);
            List<DNAcomponent> genes = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.GENE);
            List<DNAcomponent> ribozymes = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.RIBOZYME);
            List<DNAcomponent> rbs = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.RBS);
            List<DNAcomponent> terminators = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.TERMINATOR);

            Map<ComponentType, List<DNAcomponent>> map = new HashMap<ComponentType, List<DNAcomponent>>();
            map.put(ComponentType.PROMOTER, promoters);
            map.put(ComponentType.RIBOZYME, ribozymes);
            map.put(ComponentType.RBS, rbs);
            map.put(ComponentType.GENE, genes);
            map.put(ComponentType.TERMINATOR, terminators);

            String script = EugeneAdaptor.createEugeneScript(map, dataStreamJira.getWithRybozyme(), dataStreamJira.getDesignMethod());
            System.out.println("\n\n######################## Script");
            System.out.println(script);

            EugeneAdaptor eugAdp;
            System.out.println("\n\n######################## Run Eugene via XmlRpc");
            try {
                eugAdp = new EugeneAdaptor();
                eugAdp.startEugeneXmlRpc(script);
                EugeneArray result = eugAdp.getResult();
                dataStreamJira.setArray(result.toString());

                for(NamedElement ne:result.getElements()){
                    Device device = (Device)ne;
                    dataStreamJira.addFastaFile(new FastaStream(device.getName(),FastaAdaptor.getFastaFileLines(device)));
                    //dataStreamJira.addFastaFile(FastaAdaptor.createFastaFile(device, Utilities.getResourcesFilepath()));
                }

            } catch (Exception ex) {
                Logger.getLogger(RestfulController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return new ResponseEntity<DataStreamJira>(dataStreamJira, HttpStatus.OK);

        }*/
        
        private static String getLogPrefix(String project) {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            return "[JIRA: " + project +"] " + formatter.format(new Date(System.currentTimeMillis())) + " ";

        }
        
        @RequestMapping(value = "/run", method = RequestMethod.POST)
        public ResponseEntity<DataStreamJira> update(@RequestBody DataStreamJira dataStreamJira) {
            if (dataStreamJira == null) {
                throw new BadRequestException();
            } else {
            	try {
            		String promoterfilepath = dataStreamJira.getInputPromotersFasta();
                    String ribozymefilepath = dataStreamJira.getInputRibozymesFasta();
                    String rbsfilepath = dataStreamJira.getInputRbsFasta();
                    String genefilepath = dataStreamJira.getInputProteinsFasta();
                    String terminatorfilepath = dataStreamJira.getInputTerminatorsFasta();
                    
                    String username = testUser;
                    String project = dataStreamJira.getMyProjectId();
                    String password = testPassword;
                    
                    System.out.println(getLogPrefix(project) + "\n\n######################## Fasta To Clotho Promoters");
                    System.out.println(getLogPrefix(project) + FastaAdaptor.fastaToClotho(username, password, promoterfilepath, project, ComponentType.PROMOTER));
                    System.out.println(getLogPrefix(project) + "\n\n######################## Fasta To Clotho Ribozymes");
                    System.out.println(getLogPrefix(project) + FastaAdaptor.fastaToClotho(username, password, ribozymefilepath, project, ComponentType.RIBOZYME));
                    System.out.println(getLogPrefix(project) + "\n\n######################## Fasta To Clotho RBS");
                    System.out.println(getLogPrefix(project) + FastaAdaptor.fastaToClotho(username, password, rbsfilepath, project, ComponentType.RBS));
                    System.out.println(getLogPrefix(project) + "\n\n######################## Fasta To Clotho Proteins");
                    System.out.println(getLogPrefix(project) + FastaAdaptor.fastaToClotho(username, password, genefilepath, project, ComponentType.PROTEIN));
                    System.out.println(getLogPrefix(project) + "\n\n######################## Fasta To Clotho Terminators");
                    System.out.println(getLogPrefix(project) + FastaAdaptor.fastaToClotho(username, password, terminatorfilepath, project, ComponentType.TERMINATOR));
                    
                    List<DNAcomponent> promoters = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.PROMOTER);
                    List<DNAcomponent> genes = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.GENE);
                    List<DNAcomponent> ribozymes = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.RIBOZYME);
                    List<DNAcomponent> rbs = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.RBS);
                    List<DNAcomponent> terminators = FastaAdaptor.getAllDNAComponents(username, password, project, ComponentType.TERMINATOR);

                    Map<ComponentType, List<DNAcomponent>> map = new HashMap<ComponentType, List<DNAcomponent>>();
                    map.put(ComponentType.PROMOTER, promoters);
                    map.put(ComponentType.RIBOZYME, ribozymes);
                    map.put(ComponentType.RBS, rbs);
                    map.put(ComponentType.GENE, genes);
                    map.put(ComponentType.TERMINATOR, terminators);

                    String script = EugeneAdaptor.createEugeneScript(map, dataStreamJira.getWithRibozyme(), dataStreamJira.getDesignMethod());
                    System.out.println("\n\n######################## Script");
                    System.out.println(script);

                    EugeneAdaptor eugAdp;
                    System.out.println("\n\n######################## Run Eugene via XmlRpc");
                    try {
                        eugAdp = new EugeneAdaptor();
                        System.out.println(getLogPrefix(project) + "Eugene Adaptor before execution");
                        eugAdp.startEugeneXmlRpc(script);
                        System.out.println(getLogPrefix(project) + "Eugene Adaptor finished");
                        EugeneArray result = eugAdp.getResult();
                        
                        
                        //Generates Pigeon images and saves them in the output folder
                        System.out.println(getLogPrefix(project) + "Generating Pigeon images...");
                        Map<String,String> pigeonMap = new LinkedHashMap<>();
                    	pigeonMap = PigeonClient.generatePigeonScript(result);
                    	PigeonClient.generateFile(pigeonMap, project);
                        
                        System.out.println(getLogPrefix(project) + "Parsing Eugene array begin...");
                        
                        
                        //Generates fasta files for each device and saves them in output/projectName/ folder
                        for(NamedElement ne:result.getElements()){
                            Device device = (Device)ne;
                            System.out.println(device.getName());
                            System.out.println(getLogPrefix(project) + FastaAdaptor.createDeviceFastaFile(device, project));
                            
                            //used to create fasta file and put it in JSON response<DataStreamJira>.
                            //dataStreamJira.addFastaFile(new FastaStream(device.getName(),FastaAdaptor.getFastaFileLines(device)));
                            
                        }
                        System.out.println(getLogPrefix(project) + "Parsing Eugene array end.");

                    } catch (Exception ex) {
                    	System.out.println(getLogPrefix(project) + "Eugene failed with: " + ex.getMessage());
                        Logger.getLogger(RestfulController.class.getName()).log(Level.SEVERE, null, ex);
                    }	 
            	} catch (Throwable e){
            	System.out.println(getLogPrefix(dataStreamJira.getMyProjectId()) + "Owl failed. Reason: " + e.getMessage());
            }
            	System.out.println(getLogPrefix(dataStreamJira.getMyProjectId()) + "Job is done");
            	ResponseEntity<DataStreamJira> res = new ResponseEntity<DataStreamJira>(dataStreamJira, HttpStatus.OK);
            	System.out.println(getLogPrefix(dataStreamJira.getMyProjectId()) + "Returning result");
            	return res;
            	
            }

        }
        

 
        // example of sending key / value pairs with optional parameters
        /*	@RequestMapping(value = "/api2", method = RequestMethod.POST)
         public Map<String, Boolean> in(@RequestParam(value="ribozyme", required=false, defaultValue = "false") boolean ribozyme){
         Map<String, Boolean> result = new HashMap<String, Boolean>();
         result.put("withRibozyme", ribozyme);
         return result;
         }*/
    //Simple greeting tests
        @RequestMapping(value = "/")
        private String viaGet() {
            return "Hello from OwlDispatcher via GET request";
        }

        @RequestMapping(value = "/", method = RequestMethod.POST)
        private String viaPost() {
            return "Hello from OwlDispatcher via POST request";
        }
        
        // this is just an example how to parse GenBank file into separate parts and assemble GenBank file from parts
        @RequestMapping(value = "/genbank")
        private String genbank() {
        	String filepath = Utilities.getResourcesFilepath() + "genbank.gb";
    		List<String> _input = Utilities.getFileLines(filepath);
    		String input = GenBankImporter.stringifyList(_input);
    		System.out.println("==============INPUT STRING BEGINS=================");
    		System.out.println(input);
    		System.out.println("==============INPUT STRING ENDS=================");
    		List<GenBankFeature> parts = GenBankImporter.analyzeGenBank(input);
    		System.out.println("============parts imported=================");
    		System.out.println(getLogPrefix("Owl") + "parsed a total of" + parts.size() + " parts");
    		for(GenBankFeature part : parts){
    			if(part.getDnaSequence().length() > 100){
    				System.out.println("Name: " + part.getName() + "\t" + "Sequence_substring(1-100): " + part.getDnaSequence().substring(0, 100));
    			} else{
    				System.out.println("Name: " + part.getName() + "\t" + "Sequence: " + part.getDnaSequence());
    			}
    		};
    		
    		System.out.println(getLogPrefix("Owl") + "writing GenBank file...");
    		ExportGenBank.writeGenBank(parts);
    		System.out.println(getLogPrefix("Owl") + "GenBank file was created.");
    		
    		System.out.println(getLogPrefix("Owl") + "is attempting to zip " + Utilities.getOutputFilepath() + " folder.");
    		try{
    			ZipFileUtil.zipDirectory(new File(Utilities.getOutputFilepath()), new File("output.zip"));
    		} catch (IOException ex) {
    			System.out.println(getLogPrefix("Owl") + "Something went wrong while attempting to zip files.");
    			ex.printStackTrace();
    		}
    			
            return "GenBank export/import is complete; see the terminal window in IDE";
        }
    }
