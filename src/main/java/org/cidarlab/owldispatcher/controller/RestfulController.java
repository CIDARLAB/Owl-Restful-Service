    package org.cidarlab.owldispatcher.controller;
    
    /*
     * @author Yury V. Ivanov
    */

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.biojava3.core.sequence.DNASequence;
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
import org.cidarlab.owldispatcher.adaptors.LatexAdaptor;
import org.cidarlab.owldispatcher.adaptors.PigeonClient;
import org.cidarlab.owldispatcher.adaptors.ShellExec;
import org.cidarlab.owldispatcher.adaptors.ZipFileUtil;
import org.cidarlab.owldispatcher.exception.BadRequestException;
import org.cidarlab.owldispatcher.model.DataStreamJira;
import org.cidarlab.owldispatcher.model.OwlData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    public class RestfulController {
    	
    	@Value("${security.user.name}")
    	private String username;
    	@Value("${security.user.password}")
    	private String password;
    	
        @RequestMapping(value = "/json")
        public ResponseEntity<DataStreamJira> getJson() {
        	DataStreamJira dataStreamJira = new DataStreamJira();
            
            dataStreamJira.setDesignMethod("Monocistronic_prgt");
            dataStreamJira.setWithRibozyme(false);
            dataStreamJira.setInputPromotersFasta(">pT7\nATGCGATCGATCGATCG\n>pBla\nATCGTAGCTAGCTAGCTA");
            dataStreamJira.setInputRbsFasta(">RBS_1\nATGCTAGCTGATCGTA");
            dataStreamJira.setInputRibozymesFasta(">ri1\nATGATCGATCGATCGGCTAGCTA");
            dataStreamJira.setInputProteinsFasta(">gene1\nATGGCTAAGCAAGATTATTACGAGATTTTAGGCGTTTCCAAAACAGCGGAAGAGCGTGAAATCAAAAAGG"
            		+ "CCTACAAACGCCTGGCCATGAAATACCACCCGGACCGTAACCAGGGTGACAAAGAGGCCGAGGCGAAATT"
            		+ "TAAAGAGATCAAGGAAGCTTATGAAGTTCTGACCGACTCGCAAAAACGTGCGGCATACGATCAGTATGGT"
            		+ "CATGCTGCGTTTGAGCAAGGTGGCATGGGCGGCGGCGGTTTTGGCGGCGGCGCAGACTTCAGCGATATTT"
            		+ "TTGGTGACGTTTTCGGCGATATTTTTGGCGGCGGACGTGGTCGTCAACGTGCGGCGCGCGGTGCTGATTT"
            		+ "ACGCTATAACATGGAGCTCACCCTCGAAGAAGCTGTACGTGGCGTGACCAAAGAGATCCGCATTCCGACT"
            		+ "CTGGAAGAGTGTGACGTTTGCCACGGTAGCGGTGCAAAACCAGGTACACAGCCGCAGACCTGTCCGACCT"
            		+ "GTCATGGTTCTGGTCAGGTGCAGATGCGCCAGGGTTTCTTTGCCGTGCAGCAGACCTGTCCACACTGTCA"
            		+ "GGGCCGCGGTACGCTGATCAAAGATCCGTGCAACAAATGTCATGGTCATGGTCGTGTTGAGCGCAGCAAA"
            		+ "ACGCTGTCCGTTAAAATCCCGGCAGGGGTGGACACTGGAGACCGCATCCGTCTTGCGGGCGAAGGTGAAG"
            		+ "TAA\n>gene2\nATGAATGTTAATTACCTGAATGATTCAGATCTGGATTTTCTCCAGCATTGTAGTGAGGAACAGTTAGCAA"
            		+ "ATTTCGCCCGGCTGCTCACCCATAATGAAAAAGGCAAAACTCGCCTCTCCAGCGTACTGATGCGCAACGA"
            		+ "ACTGTTTAAATCGATGGAAGGACATCCCGAGCAACATCGCCGCAACTGGCAGCTGATTGCTGGAGAATTA"
            		+ "CAGCATTTTGGTGGCGATAGTATCGCCAACAAACTGCGCGGACACGGTAAATTGTATCGGGCCATTTTGC"
            		+ "TCGATGTTTCAAAACGATTGAAGCTGAAAGCCGACAAAGAGATGTCTACGTTTGAAATTGAGCAACAGTT"
            		+ "ACTGGAACAATTTCTGCGTAATACCTGGAAGAAAATGGACGAGGAACATAAGCAGGAGTTTCTGCACGCG"
            		+ "GTCGATGCCAGGGTGAATGAGCTGGAAGAGCTGCTGCCGCTGCTGATGAAAGACAAATTATTGGCAAAAG"
            		+ "GCGTGTCGCATCTGCTTTCCAGCCAACTGACCCGCATTTTACGCACCCACGCAGCAATGAGCGTACTTGG"
            		+ "GCATGGTTTGCTGCGCGGCGCGGGGCTGGGAGGCCCTGTAGGCGCGGCACTAAATGGGGTTAAAGCGGTC"
            		+ "AGCGGCAGCTCCTATCGCGTGACGATTCCAGCCGTACTGCAAATCGCCTGCCTGCGCCGGATGGTTAGCG"
            		+ "CCACTCAGGTCTGA\n>gene3\nATGAGTGACTATAAATCAACCCTGAATTTGCCGGAAACAGGGTTCC"
            		+ "CGATGCGTGGCGATCTCGCCAAGCGCGAACCGGGAATGCTGGCGCGTTGGACTGATGATGATCTGTACGG"
            		+ "CATCATTCGTGCGGCTAAAAAAGGCAAAAAAACCTTCATTCTGCATGATGGCCCTCCTTATGCGAATGGC"
            		+ "AGCATTCATATTGGTCACTCGGTTAACAAGATTCTGAAAGACATTATCGTGAAGTCCAAAGGGCTTTCCG"
            		+ "GTTATGACTCGCCGTATGTGCCTGGCTGGGACTGCCACGGTCTGCCGATCGAGCTGAAAGTAGAGCAAGA"
            		+ "ATACGGTAAGCCGGATGAGAAATTCACCGCCGCCGAGTTCCGCGCCAAGTGCCGCGAATACGCGGCGACC"
            		+ "CAGGTTGACGGTCAACGCAAAGACTTTATCCGTCTGGGCGTGCTGGGCGACTGGTCGCACCCGTACCTGA"
            		+ "CCATGGACTTCAAAACTGAAGCCAATATCATCCGCGCGCTGGGCAAAATCATCGGCAATGGTCACCTGCA"
            		+ "CAAAGGCGCGAAGCCGGTGCACTGGTGCGTAGACTGCCGTTCTGCACTGGCAGAAGCGGAAGTTGAGTAT"
            		+ "TACGACAAAACTTCTCCGTCCATTGACGTCGCTTTCCAGGCGGTCGATCAGGATGCGCTGAAAGCGAAAT"
            		+ "TTGGCGTAAGCAACGTTAACGGCCCAATCTCGCTGGTGATCTGGACCACTACGCCGTGGACTCTGCCTGC"
            		+ "GAACCGCGCAATCTCTATTGCACCTGATTTCGACTATGCGCTGGTGCAGATCGACGGTCAGGCCGTGATT"
            		+ "CTGGCGAAAGATCTGGTTGAAAGCGTAATGCAGCGTATCGGCGTGACCGATTACACCATTCTCGGCACGG"
            		+ "TAAAAGGTGCGGAGCTTGAGCTGCTGCGCTTTGCCCATCCGTTTATGGGCTTCGACGTCCCGGCAATCC");
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
        
        private static String getLogPrefix(String project) {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            return "[JIRA: " + project +"] " + formatter.format(new Date(System.currentTimeMillis())) + " ";

        }
        
        @RequestMapping(value = "/run", method = RequestMethod.POST, produces="application/octet-stream")
        public ResponseEntity<byte[]> update(@RequestBody DataStreamJira dataStreamJira, HttpServletResponse resp) {
            if (dataStreamJira == null) {
                throw new BadRequestException();
            } else {
            	try {
            		String project = dataStreamJira.getMyProjectId();
            		String promoterfilepath = dataStreamJira.getInputPromotersFasta();
                    String ribozymefilepath = dataStreamJira.getInputRibozymesFasta();
                    String rbsfilepath = dataStreamJira.getInputRbsFasta();
                    String genefilepath = dataStreamJira.getInputProteinsFasta();
                    String terminatorfilepath = dataStreamJira.getInputTerminatorsFasta();
                    Map<String,String> images = new LinkedHashMap<String, String>();
                    Map<String, String> gcContentMap = new HashMap<>();
                    Map<String, Integer> deviceLengths = new HashMap<>();
                    Map<String, String> deviceCompositions = new HashMap<>();
                    
                    System.out.println(getLogPrefix(project) + "\n\n######################## Fasta To Clotho Promoters");
                    System.out.println(getLogPrefix(project) + FastaAdaptor.fastaToClotho(username, password, promoterfilepath, project, ComponentType.PROMOTER));
                    System.out.println(getLogPrefix(project) + "\n\n######################## Fasta To Clotho Ribozymes");
                    System.out.println(getLogPrefix(project) + FastaAdaptor.fastaToClotho(username, password, ribozymefilepath, project, ComponentType.RIBOZYME));
                    System.out.println(getLogPrefix(project) + "\n\n######################## Fasta To Clotho RBS");
                    System.out.println(getLogPrefix(project) + FastaAdaptor.fastaToClotho(username, password, rbsfilepath, project, ComponentType.RBS));
                    System.out.println(getLogPrefix(project) + "\n\n######################## Fasta To Clotho Genes");
                    System.out.println(getLogPrefix(project) + FastaAdaptor.fastaToClotho(username, password, genefilepath, project, ComponentType.GENE));
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
                    System.out.println("\n\n################### Eugene Specification ###################");
                    System.out.println(script);

                    EugeneAdaptor eugAdp;
                    System.out.println("\n\n################### Run Eugene via XmlRpc ##################");
                    try {
                        eugAdp = new EugeneAdaptor();
                        System.out.println(getLogPrefix(project) + "Eugene Adaptor before execution");
                        eugAdp.startEugeneXmlRpc(script);
                        System.out.println("######################## " + getLogPrefix(project) + "Eugene Adaptor finished");
                        EugeneArray result = eugAdp.getResult();
                        
                        
                        //Generates Pigeon images and saves them in the output folder
                        System.out.println(getLogPrefix(project) + "################ Generating Pigeon code and requesting images ###############");
                        Map<String,String> pigeonMap = new LinkedHashMap<>();
                    	//pigeonMap is a Map<String,String> with <deviceName>:<pigeonScript>
                        pigeonMap = PigeonClient.generatePigeonScript(result);
                    	images = PigeonClient.generateFile(pigeonMap, project);
                        
                        System.out.println(getLogPrefix(project) + "\n\n################ Parsing Eugene array ##################");
                        
                        
                        //Generates fasta files for each device and saves them in output/projectName/ folder
                        for(NamedElement ne:result.getElements()){
                            Device device = (Device)ne;
                            System.out.println(getLogPrefix(project) + "Generating FASTA file for " + device.getName() +" device");
                            System.out.println(getLogPrefix(project) + FastaAdaptor.createDeviceFastaFile(device, project));
                            System.out.println(getLogPrefix(project) + "Generating GenBank file for " + device.getName() +" device");
                            String genBankContents = ExportGenBank.deviceToGenBank(project, device);
                            Utilities.writeToFile(Utilities.getProjectFolderPath(project)+device.getName()+".gb", genBankContents);
                            
                            //get lengths of devices
                            deviceLengths.put(device.getName(), device.getSequence().length());
                            
                            //get device components for PDF datasheet
                            deviceCompositions.put(device.getName(), LatexAdaptor.getDeviceComponents(device));
                            
                            //Calculate %GC for the device
                            DNASequence dna = new DNASequence(device.getSequence());
                            double percentGc = dna.getGCCount()*100.00/dna.getLength();
                            gcContentMap.put(device.getName(), String.format("%.2f", percentGc));
                        }

                    } catch (Exception ex) {
                    	System.out.println(getLogPrefix(project) + "Eugene failed with: " + ex.getMessage());
                        Logger.getLogger(RestfulController.class.getName()).log(Level.SEVERE, null, ex);
                    }	
                
                 // PDF DATASHEET
                    System.out.println(getLogPrefix(project) + "\n\n################ PDF Datasheet ###############");
                    OwlData owl = new OwlData(images, gcContentMap, deviceLengths, deviceCompositions);
                    owl.setMyProjectId(project);
                    owl.setPathToTexFile(owl.getPathToTexFolder()+Utilities.getFileDivider()+owl.getMyProjectId()+".tex");
                    
                    String texFile = LatexAdaptor.makeTexFile(owl);
                    System.out.println(getLogPrefix(project) + texFile+" file was successfully created.");
                    System.out.println(getLogPrefix(project) + "Generating PDF...");
                    ShellExec exec = new ShellExec(true, false);
                    try {
                        int exitCode = exec.execute("pdflatex", Utilities.getProjectFolderPath(owl), true, owl.getPathToTexFile());
                        if(exitCode == 0){
                        	System.out.println(getLogPrefix(project) + Utilities.getProjectFolderPath(owl)+project+".pdf was successfully generated");
                        } else {
                        	System.out.println(getLogPrefix(project) + "pdflatex returned exit code = "+exitCode);
                        }
                    } catch (IOException e) {
                        System.out.println(getLogPrefix(project) + "PDFlatex failed. Reason: " + e.getMessage());
                    }
                    //System.out.println(exec.getOutput());
                    
                    Utilities.removeFile(Utilities.getProjectFolderPath(owl)+owl.getMyProjectId()+".aux");
                    Utilities.removeFile(Utilities.getProjectFolderPath(owl)+owl.getMyProjectId()+".log");
                    Utilities.removeFile(Utilities.getProjectFolderPath(owl)+owl.getMyProjectId()+".out");
                    
                    System.out.println(getLogPrefix("Owl") + "is attempting to zip " + Utilities.getProjectFolderPath(owl) + " folder.");
            		/*try{
            			ZipFileUtil.zipDirectory(new File(Utilities.getProjectFolderPath(owl)), new File(Utilities.getOutputFilepath()+project+".zip"));
            		} catch (IOException ex) {
            			System.out.println(getLogPrefix(project) + "Something went wrong while attempting to zip the project folder.");
            			ex.printStackTrace();
            		}
            		
            		File folder = new File(Utilities.getOutputFilepath()+project);
            		boolean deleted = Utilities.deleteFolder(folder);
            		if(deleted){
            			System.out.println(getLogPrefix(project) + folder.getCanonicalPath()+" was successfully deleted");
            		} else {
            			System.out.println("Something went wrong while deleting "+folder.getCanonicalPath()+" folder");
            		}*/
            		
                    System.out.println(getLogPrefix(project) + "Wrapping up...");
                    
            	} catch (Throwable e){
            	System.out.println(getLogPrefix(dataStreamJira.getMyProjectId()) + "Owl failed. Reason: " + e.getMessage());
            	}
            	
            	//Send out zip folder with HttpStatus
            	final HttpHeaders headers = new HttpHeaders();
            	byte[] zipByteArray = null;
            	
            	try{
            		zipByteArray = ZipFileUtil.giveByteArray(new File(Utilities.getProjectFolderPath(dataStreamJira.getMyProjectId())));
        		} catch (IOException ex) {
        			String msg = "ERROR: Could not generate zip folder.";
                    headers.setContentType(MediaType.TEXT_PLAIN);
                    return new ResponseEntity<byte[]>(msg.getBytes(), headers, HttpStatus.NOT_FOUND);
        		}
            	
            	resp.setHeader("Content-Disposition", "attachment; filename=\""+dataStreamJira.getMyProjectId()+ ".zip\"");
            	
            	System.out.println(getLogPrefix(dataStreamJira.getMyProjectId()) + "\n\n########## Project is done");
            	ResponseEntity<byte[]> res = new ResponseEntity<byte[]>(zipByteArray,headers,HttpStatus.OK);
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
        
        // this is just an example how to parse GenBank file into separate parts and assemble GenBank file from parts
        @RequestMapping(value = "/genbank")
        private String genbank() {
        	String filepath = Utilities.getResourcesFilepath() + "genbank.gb";
    		List<String> _input = Utilities.getFileLines(filepath);
    		String input = GenBankImporter.stringifyList(_input);
    		System.out.println("\n\n==============INPUT STRING BEGINS=================");
    		System.out.println(input);
    		System.out.println("\n\n==============INPUT STRING ENDS=================");
    		List<GenBankFeature> parts = GenBankImporter.analyzeGenBank(input);
    		System.out.println("\n\n============parts imported=================");
    		System.out.println(getLogPrefix("Owl") + "parsed a total of" + parts.size() + " parts");
    		for(GenBankFeature part : parts){
    			if(part.getDnaSequence().length() > 100){
    				System.out.println("Name: " + part.getName() + "\t" + "Sequence_substring(1-100): " + part.getDnaSequence().substring(0, 100));
    			} else{
    				System.out.println("Name: " + part.getName() + "\t" + "Sequence: " + part.getDnaSequence());
    			}
    		};
    		
    		System.out.println(getLogPrefix("Owl") + "writing GenBank file...");
    		ExportGenBank.writeGenBank(parts, "OwlProject");
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
        
        //Simple greeting tests
        @RequestMapping(value = "/")
        private String viaGet() {
            return "Hello from OwlDispatcher via GET request";
        }

        @RequestMapping(value = "/", method = RequestMethod.POST)
        private String viaPost() {
            return "Hello from OwlDispatcher via POST request";
        }
    }
