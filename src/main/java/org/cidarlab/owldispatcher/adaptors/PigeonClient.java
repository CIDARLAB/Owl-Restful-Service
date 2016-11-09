package org.cidarlab.owldispatcher.adaptors;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.cidarlab.eugene.dom.Device;
import org.cidarlab.eugene.dom.imp.container.EugeneArray;
import org.cidarlab.eugene.exception.EugeneException;
import org.cidarlab.owldispatcher.Utilities;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/*
 * @author Yury V. Ivanov
 */
public class PigeonClient {
	
	@Getter
	@Setter
	private static Map<String, Integer> colors = new HashMap<>();

    public static void requestPigeon(String pigeonCodeEncoded) throws UnirestException, IOException {
        HttpResponse<String> response = Unirest.post("http://synbiotools.bu.edu:5801/dev/perch2.php")
                .header("content-type", "application/x-www-form-urlencoded")
                .body("specification=" + pigeonCodeEncoded)
                .asString();

        System.out.println("============testing Pigeon API=============");
        System.out.println(response.getBody().toString());

        JsonNode body = new JsonNode(response.getBody());
        String pigeonImageUrl = body.getObject().get("fileURL").toString();

        System.out.println(pigeonImageUrl);

        System.out.println("downloading a Pigeon image...");
        BufferedImage image = null;
        URL url = new URL(pigeonImageUrl);
        image = ImageIO.read(url);
        File outputFile = new File(Utilities.getOutputFilepath() + "image.png");

        System.out.println("saving an image...");
        ImageIO.write(image, "png", outputFile);
        System.out.println(outputFile.toString() + " was saved successfully.");
    }

    public static Map<String, String> generatePigeonScript(EugeneArray array) throws EugeneException {
        String script = "";
        String deviceName = "";
        Map<String, String> pigeonMap = new LinkedHashMap<>();
        //System.out.println(array.size());
        for (int i = 0; i < array.size(); i++) {
            Device myDevice = (Device) array.getElement(i);
            deviceName = myDevice.getName();

			//System.out.println(myDevice.toString());
            //System.out.println(myDevice.getComponents().size());
            for (int j = 0; j < myDevice.getComponents().size(); j++) {
                String part = myDevice.getComponents().get(j).get(0).toString();
                //System.out.println(part);
                String partName = myDevice.getComponents().get(j).get(0).getElement("name").toString().replaceAll("\"", "");
                //System.out.println(partName);
                if (part.startsWith("Promoter")) {
                    script += "p p " + getColor(partName) + "%0D%0A";
                } else if (part.startsWith("RBS")) {
                    script += "r rbs "+ getColor(partName) + "%0D%0A";
                } else if (part.startsWith("Ribozyme")) {
                    script += "z ri "+ getColor(partName) + "%0D%0A";
                } else if (part.startsWith("CDS")) {
                    script += "c g "+ getColor(partName) + "%0D%0A";
                } else if (part.startsWith("Terminator")) {
                    script += "t t "+ getColor(partName) + "%0D%0A";
                } else {
                    System.out.println("Urecognized part type: " + part);
                }
            }
            script += "# Arcs";
           
            pigeonMap.put(deviceName, script);
            
            script = "";
        }
        return pigeonMap;
    }

    public static Map<String,String> generateFile(Map<String, String> devicePigeonMap, String projectName) throws UnirestException, IOException {
        
        Map<String,String> pigeonFilepath = new LinkedHashMap<String,String>();
        
        for (String key : devicePigeonMap.keySet()) {
            System.out.println(key + " : " + devicePigeonMap.get(key));
            HttpResponse<String> response = Unirest.post("http://synbiotools.bu.edu:5801/dev/perch2.php")
                    .header("content-type", "application/x-www-form-urlencoded")
                    .body("specification=" + devicePigeonMap.get(key))
                    .asString();

            JsonNode body = new JsonNode(response.getBody());
            String pigeonImageUrl = body.getObject().get("fileURL").toString();

            System.out.println(pigeonImageUrl);

            System.out.println("downloading a Pigeon image...");
            BufferedImage image = null;
            URL url = new URL(pigeonImageUrl);
            image = ImageIO.read(url);
            final String pathToFolder = Utilities.getOutputFilepath() + projectName;
            final String pathToFile = pathToFolder + Utilities.getFileDivider() + key + ".png";
            File file = new File(pathToFolder);
            file.mkdir();
            File outputFile = new File(pathToFile);

            System.out.println("saving " + key + ".png image...");
            ImageIO.write(image, "png", outputFile);
            System.out.println(outputFile.toString() + " was saved successfully.");
            pigeonFilepath.put(key, outputFile.getAbsolutePath());
        }
        return pigeonFilepath;
    }
     
    /**
	 * 
	 * @param s  ... the name of a component
	 * @return   the color code of the component
	 */
	private static int getColor(String s) {
		if(colors.containsKey(s)) {
			int color = colors.get(s);
			if(color <= 1) {
				return 1;
			} else if(color >= 14) {
				return 14;
			}
			return color;
		}

		/*
		 * otherwise, we put the name into the coloring map
		 */
		int color = getRandomColor();
		colors.put(s, color);
		return color;
	}
	
	private static final int COLOR_MIN = 1;
	private static final int COLOR_MAX = 14;
	
	private static int getRandomColor() {
		return COLOR_MIN + (int)(Math.random() * ((COLOR_MAX - COLOR_MIN) + 1));
	}
    
}
