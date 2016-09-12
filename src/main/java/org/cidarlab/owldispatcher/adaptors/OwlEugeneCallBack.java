/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.adaptors;

import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;
import org.apache.xmlrpc.client.TimingOutCallback;
import org.cidarlab.eugene.dom.imp.container.EugeneArray;
import org.cidarlab.eugene.dom.imp.container.EugeneCollection;
import org.cidarlab.owldispatcher.Args;
import org.cidarlab.owldispatcher.controller.RestfulController;

public class OwlEugeneCallBack implements AsyncCallback {

    private EugeneArray result;
    
    private boolean ready;
    
    public OwlEugeneCallBack(){
        ready = false;
    }
    
    public EugeneArray getResult() {
        return result;
    }
    
    
    public boolean waitForResult(long startTime){
        while(!this.ready){
            if((System.currentTimeMillis() - startTime) > Args.callbacktime){
                return this.ready;
            }
        }
        
        return this.ready;
    }
    
    @Override
    public void handleResult(XmlRpcRequest xrr, Object object) {
        System.out.println(object.toString());

        if (null != object) {

            // the received object, is actually a EugeneCollection object
            if (object instanceof EugeneCollection) {

                //this.collection
                EugeneCollection results
                        = (EugeneCollection) object;
                
                if (results != null) {
                    System.out.println("Works!");
                    //System.out.println("Eugene Collection :: " + results.toString());
                } else {
                    System.out.println("Eugene Collection :: ERROR; the collection is empty!!!");
                }

                /*Device monocistronic = (Device)results.get("Monocistronic_prgt");

                 Rule ruleOnParts = (Rule)results.get("r1");

                 Rule ruleOnPartTypePositioning = this.deriveRuleFromStructure(monocistronic);

                 System.out.println(ruleOnParts);
                 System.out.println(ruleOnPartTypePositioning);

                    
                 * concatenate both rules using a logical and
                     

                 Rule concatenated = this.and(ruleOnParts, ruleOnPartTypePositioning);
                 System.out.println(concatenated);*/
                result
                        = (EugeneArray) results.get("lod");

                // Would be nice to get SBOL files and Pigeon images for each device in EugeneArray
                // process the result array
                System.out.println("\n\nThe total number of constraints-compliant devices is: " + result.size());
                this.ready = true;
            }
        } else {
            System.out.println("ERROR: Eugene failed to execute async call. Some problem with the callback.");
        }

    }

    @Override
    public void handleError(XmlRpcRequest xrr, Throwable thrwbl) {
        System.out.println("ERROR: XmlRpc throwable error. See OwlEugeneCallBack class");
    }

}
