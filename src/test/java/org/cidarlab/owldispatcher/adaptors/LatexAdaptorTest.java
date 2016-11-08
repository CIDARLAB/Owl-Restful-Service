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
 */
public class LatexAdaptorTest {

    @Test
    public void testLatexFileCreation() {
        OwlData owl = new OwlData();
        owl.addPigeonFilepath("Monocistronic_prgt_1", Utilities.getOutputFilepath() + "new-test2" + Utilities.getFileDivider() + "Monocistronic_prgt_1.png");
        owl.addPigeonFilepath("Monocistronic_prgt_2", Utilities.getOutputFilepath() + "new-test2" + Utilities.getFileDivider() + "Monocistronic_prgt_2.png");
        owl.addPigeonFilepath("Monocistronic_prgt_3", Utilities.getOutputFilepath() + "new-test2" + Utilities.getFileDivider() + "Monocistronic_prgt_3.png");
        owl.setMyProjectId("new-test2");

        LatexAdaptor.getLatexFilepath(owl);

        String pathToWorkDir = Utilities.getOutputFilepath() + owl.getMyProjectId();
        String pathToTex = pathToWorkDir + Utilities.getFileDivider() + owl.getMyProjectId();

        ShellExec exec = new ShellExec(true, false);
        try {
            exec.execute("pdflatex", pathToWorkDir, true, pathToTex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(exec.getOutput());

    }

}

