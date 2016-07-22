    package org.cidarlab.owldispatcher.controller;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.logging.Level;
    import java.util.logging.Logger;
    import org.cidarlab.eugene.dom.Component;
    import org.cidarlab.eugene.dom.Device;
    import org.cidarlab.eugene.dom.NamedElement;

    import org.cidarlab.eugene.dom.imp.container.EugeneArray;
    import org.cidarlab.eugene.dom.imp.container.EugeneCollection;
    import org.cidarlab.owldispatcher.Args;
    import org.cidarlab.owldispatcher.Utilities;
    import org.cidarlab.owldispatcher.DOM.ComponentType;
    import org.cidarlab.owldispatcher.DOM.DNAcomponent;
    import org.cidarlab.owldispatcher.adaptors.EugeneAdaptor;
    import org.cidarlab.owldispatcher.adaptors.FastaAdaptor;

    import org.cidarlab.owldispatcher.adaptors.ReverseTranslate;
    import org.cidarlab.owldispatcher.exception.BadRequestException;
    import org.cidarlab.owldispatcher.model.DataStream;
    import org.cidarlab.owldispatcher.model.DataStreamJira;
import org.cidarlab.owldispatcher.model.FastaStream;
    import org.clothoapi.clotho3javaapi.Clotho;
    import org.clothoapi.clotho3javaapi.ClothoConnection;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    public class RestfulController {

        private static String testUser = "testUserOwl";
        private static String testProject = "testProjectOwl";
        private static String testPassword = "testPasswordOwl";
        
        @RequestMapping(value = "/api")
        public ResponseEntity<DataStream> get() {

            DataStream dataStream = new DataStream();
            dataStream.setFasta(">name\nATGCTAGCA");
            dataStream.setWithRybozyme(false);

            return new ResponseEntity<DataStream>(dataStream, HttpStatus.OK);

        }

        @RequestMapping(value = "/create-test-user")
        public String stTest() {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            clothoObject.createUser(testUser, testPassword);
            System.out.println("Username :: " + testUser + "\nPassword :: " + testPassword);
            clothoObject.logout();
            conn.closeConnection();
            return "Username :: " + testUser + "\nPassword :: " + testPassword + "\nProject :: " + testProject;
        }

/*        @RequestMapping(value = "/create-user")
        public String st() {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            user = "user" + System.currentTimeMillis();
            proj = "project" + System.currentTimeMillis();
            clothoObject.createUser(user, pass);
            System.out.println("Username :: " + user + "\nPassword :: " + pass);
            clothoObject.logout();
            conn.closeConnection();
            return "Username :: " + user + "\nPassword :: " + pass + "\nProject :: " + proj;
        }*/

        @RequestMapping(value = "/example")
        public ResponseEntity<DataStreamJira> exampl() {

            DataStreamJira dataStreamJira = new DataStreamJira();
            dataStreamJira.setDesignMethod("Exhaustive");
            dataStreamJira.setWithRybozyme(false);
            dataStreamJira.setInputPromotersFasta(">pT7\nATGCGATCGATCGATCG\n>pBla\nATGCTAGCTAGCTAGCTTAA");
            dataStreamJira.setInputRbsFasta(">RBS_1\nATGCTAGCTGATCGTA\n>RBS_2\nATGCTGATCGATCGATCGAT>");
            dataStreamJira.setInputRybozymesFasta(">ri1\nATGATCGATCGATCGGCTAGCTA");
            dataStreamJira.setInputProteinsFasta(">gene1\nATGCTAGCTAGCTA\n>gene2\nTGATCGATCGATCAC");
            dataStreamJira.setInputTerminatorsFasta(">t1\nATCGATCGATCGATCGAT\n>t2\nATCGATCGATCGATC");
            
            return new ResponseEntity<DataStreamJira>(dataStreamJira, HttpStatus.OK);

        }
        
        @RequestMapping(value = "/example", method = RequestMethod.POST)
        public ResponseEntity<DataStreamJira> update(@RequestBody DataStreamJira dataStreamJira) {
            if (dataStreamJira == null) {
                throw new BadRequestException();
            } else {
            	
            	//  TO-DO need to extend fastaToComponents to take fasta string 
                //   FastaAdaptor.fastaToComponents(dataStreamJira.getInputPromotersFasta(), ComponentType.PROMOTER);
            	
            	return new ResponseEntity<DataStreamJira>(dataStreamJira, HttpStatus.OK);
            }
        }
        
        @RequestMapping(value = "/eugene")
        public ResponseEntity<DataStreamJira> jira() {
            String promoterfilepath = Utilities.getResourcesFilepath() + "promoters.fasta";
            String ribozymefilepath = Utilities.getResourcesFilepath() + "ribozymes.fasta";
            String rbsfilepath = Utilities.getResourcesFilepath() + "rbs.fasta";
            String genefilepath = Utilities.getResourcesFilepath() + "proteins.fasta";
            String terminatorfilepath = Utilities.getResourcesFilepath() + "terminators.fasta";

            String username = testUser;
            String project = testProject;
            String password = testPassword;

            DataStreamJira dataStreamJira = new DataStreamJira();
            dataStreamJira.setWithRybozyme(false);
            dataStreamJira.setDesignMethod("Exhaustive");
                    //dataStreamJira.setInputFasta(">name\nATGCTGATCATCGATC");

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
            //System.out.println("\n\n######################## Run Eugene Locally");
            //EugeneCollection collection = EugeneAdaptor.runEugene(script);
            //System.out.println("Collection :: \n" + collection.toString());
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
        }

        @RequestMapping(value = "/reversetranslate")
        public ResponseEntity<DataStream> getReverse() {

            String p = "MTYSRSNITLALLANICAFFLWSLATLIFNALSTIDNLQVLAFRIIFSM";
            //return "Original protein sequence: " + p + '\n' + "Reverse-translated sequence: " + ReverseTranslate.translate(p);
            DataStream dataStream = new DataStream();
            dataStream.setFasta(">name\n" + p);
            dataStream.setWithRybozyme(false);
            dataStream.setReverseTranslate(ReverseTranslate.translate(p));

            return new ResponseEntity<DataStream>(dataStream, HttpStatus.OK);

        }

        @RequestMapping(value = "/api", method = RequestMethod.POST)
        public ResponseEntity<DataStream> update(@RequestBody DataStream dataStream) {

            if (dataStream == null) {
                throw new BadRequestException();
            } else {
                return new ResponseEntity<DataStream>(dataStream, HttpStatus.OK);
            }
        }

        /*	@RequestMapping(value = "/api2", method = RequestMethod.POST)
         public Map<String, Boolean> in(@RequestParam(value="rybozyme", required=false, defaultValue = "false") boolean rybozyme){
         Map<String, Boolean> result = new HashMap<String, Boolean>();
         result.put("withRybozyme", rybozyme);
         return result;
         }*/
    //Simple greeting tests
        @RequestMapping(value = "/")
        private String viaGet() {
            return "Hello from OwlDispatcher using GET request";
        }

        @RequestMapping(value = "/", method = RequestMethod.POST)
        private String viaPost() {
            return "Hello from OwlDispatcher using POST request";
        }
    }
