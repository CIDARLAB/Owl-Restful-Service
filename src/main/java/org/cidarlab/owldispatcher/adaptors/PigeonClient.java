package org.cidarlab.owldispatcher.adaptors;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class PigeonClient {
	
	//private static String pigeonCode = "p p1 2%0D%0Ar r2 3%0d%0ag g3 6%0D%0At t4 13%0d%0a# Arcs";

	public static String requestPigeon(String pigeonCodeEncoded) throws UnirestException{
		HttpResponse<String> response = Unirest.post("http://synbiotools.bu.edu:5801/dev/perch2.php")
				  .header("content-type", "application/x-www-form-urlencoded")
				  .body("specification="+ pigeonCodeEncoded)
				  .asString();
		
		System.out.println("============testing Pigeon API=============");
		System.out.println(response.getBody().toString());
		
		JsonNode body = new JsonNode(response.getBody());
		String pigeonImageUrl = body.getObject().get("fileURL").toString();
		
		System.out.println(pigeonImageUrl);
		return pigeonImageUrl;
	}
}
