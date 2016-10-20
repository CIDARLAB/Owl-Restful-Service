package org.cidarlab.owldispatcher.DOM;

/*import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothocad.model.Feature;
import org.clothocad.model.Feature.FeatureRole;
import org.clothocad.model.Person;
import org.clothocad.model.Sequence;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;*/


public class ClothoDataModel {
/*public static void instantiate (String partname, FeatureRole type, String sequence, String outputFileUrl, Clotho clothoObject, String username, boolean orientation) {
        
        Person user = new Person (username);
        
        try {
            FileWriter seqJSONfile = new FileWriter(outputFileUrl + "-sequence.txt");
            FileWriter feaJSONfile = new FileWriter(outputFileUrl + "-feature.txt");
            
            //one JSON object container for each table, one JSON array for all table entries, one JSON object for each entry
            JSONObject seqJSON = new JSONObject();
            JSONArray seqArr = new JSONArray();
            
            JSONObject feaJSON = new JSONObject();
            JSONArray feaArr = new JSONArray();
            
            //counter for clotho
            int[] clothoCount = new int[2];
                
            //sequence

            String seqname = "seq" + System.currentTimeMillis(); //sequence id is generated
            Sequence newSeq = new Sequence (seqname, "", sequence, user);

            JSONObject seqObj = newSeq.getJSON();
            Map seqMap = newSeq.getMap();
            String seqClo = (String) clothoObject.create(seqMap);
            if (!seqClo.equals(null)) {
                clothoCount[0]++;
            }
            seqArr.add(seqObj);
            

            //feature

            String feaname = "fea" + System.currentTimeMillis(); //sequence id is generated
            Feature.FeatureRole role = type;
            Feature newFeature = new Feature (feaname, "", newSeq, role, user);

            JSONObject feaObj = newFeature.getJSON();
            Map feaMap = newFeature.getMap();
            String feaClo = (String) clothoObject.create(feaMap);
            if (!feaClo.equals(null)) {
                clothoCount[1]++;
            }
            feaArr.add(feaObj);
            
            
            System.out.println("Created " + clothoCount[0] + " Sequence objects" + "\n" +
                                "Created " + clothoCount[1] + " Feature objects");
            
            seqJSON.put("Name", "Sequence");
            seqJSON.put("Entries", seqArr);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(seqJSON);
            seqJSONfile.write (prettyJson);
            
            feaJSON.put("Name", "Feature");
            feaJSON.put("Entries", feaArr);

            prettyJson = gson.toJson(feaJSON);
            feaJSONfile.write (prettyJson);
            
            System.out.println("Successfully wrote JSON objects entries ************");

            seqJSONfile.close();
            feaJSONfile.close();
            
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/
}
