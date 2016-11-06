package org.cidarlab.owldispatcher.adaptors;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.cidarlab.owldispatcher.Utilities;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class PigeonClient {

	public static void requestPigeon(String pigeonCodeEncoded) throws UnirestException, IOException{
		HttpResponse<String> response = Unirest.post("http://synbiotools.bu.edu:5801/dev/perch2.php")
				  .header("content-type", "application/x-www-form-urlencoded")
				  .body("specification="+ pigeonCodeEncoded)
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
		
	    File outputFile = new File(Utilities.getOutputFilepath()+"image.png");
	    System.out.println("saving an image...");
	    ImageIO.write(image, "png", outputFile);
	    System.out.println(outputFile.toString()+" was saved successfully.");
	}
	
}
