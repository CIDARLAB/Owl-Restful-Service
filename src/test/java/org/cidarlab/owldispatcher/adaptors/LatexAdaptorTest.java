/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

import java.io.IOException;

import org.cidarlab.owldispatcher.Utilities;
import org.cidarlab.owldispatcher.model.OwlData;
import org.junit.Test;

/**
 *
 * @author prash
 * @author Yury V. Ivanov
 */
public class LatexAdaptorTest {

    @Test
    public void testLatexFileCreation() {
        OwlData owl = new OwlData();
        owl.addPigeonFilepath("Monocistronic_prgt_1", Utilities.getOutputFilepath() + "new-test2" + Utilities.getFileDivider() + "Monocistronic_prgt_1.png");
        owl.addPigeonFilepath("Monocistronic_prgt_2", Utilities.getOutputFilepath() + "new-test2" + Utilities.getFileDivider() + "Monocistronic_prgt_2.png");
        owl.addPigeonFilepath("Monocistronic_prgt_3", Utilities.getOutputFilepath() + "new-test2" + Utilities.getFileDivider() + "Monocistronic_prgt_3.png");
        owl.setMyProjectId("new-test2");
        owl.setPathToTexFile(owl.getPathToTexFolder()+Utilities.getFileDivider()+owl.getMyProjectId()+".tex");

        String texFile = LatexAdaptor.makeTexFile(owl);
        System.out.println(texFile+" file was successfully created.");

        String pathToWorkDir = Utilities.getOutputFilepath() + owl.getMyProjectId();

        ShellExec exec = new ShellExec(true, false);
        try {
            exec.execute("pdflatex", pathToWorkDir, true, owl.getPathToTexFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(exec.getOutput());
        
        Utilities.removeFile(Utilities.getProjectFolderPath(owl)+owl.getMyProjectId()+".aux");
        Utilities.removeFile(Utilities.getProjectFolderPath(owl)+owl.getMyProjectId()+".log");
        Utilities.removeFile(Utilities.getProjectFolderPath(owl)+owl.getMyProjectId()+".out");

    }

}

