package org.cidarlab.owldispatcher.adaptors;

import java.util.ArrayList;
import java.util.List;

import org.cidarlab.eugene.dom.Device;
import org.cidarlab.eugene.dom.NamedElement;
import org.cidarlab.eugene.dom.imp.container.EugeneArray;
import org.cidarlab.owldispatcher.Utilities;
import org.junit.Test;

public class ExportGenbankTest {
	
	private final String project = "Test";
	
	@Test
	public void testExportGenBank(){
		
		String eugeneFile = Utilities.getDefautltEugeneRootDirectory()+"app.eug";
		List<String> scriptList = Utilities.getFileLines(eugeneFile);
		String script = "";
		for(String s: scriptList){
			script += s+"\n";
		}
		
		EugeneAdaptor eugAdp;
        System.out.println("\n\n################### Run Eugene via XmlRpc ##################");
        try {
            eugAdp = new EugeneAdaptor();
            System.out.println("Eugene Adaptor before execution");
            eugAdp.startEugeneXmlRpc(script);
            System.out.println("######################## Eugene Adaptor finished");
            EugeneArray result = eugAdp.getResult();
            
            List<Device> devices = new ArrayList<>();
            for(NamedElement ne:result.getElements()){
                Device device = (Device)ne;
                devices.add(device);
            }
            
            ExportGenBank.deviceToGenBank(project, devices.get(0));
            
        } catch (Exception ex){
        	System.out.println(ex.getMessage());
        }
		
		
		
	}
}
