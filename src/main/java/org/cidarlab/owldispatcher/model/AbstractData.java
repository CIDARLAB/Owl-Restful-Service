/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.owldispatcher.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public abstract class AbstractData {
    
    @Getter
    @Setter
    protected String myProjectId;
    
    @Getter
    @Setter
    protected Boolean withRibozyme;
    
    @Getter
    @Setter
    protected String designMethod;
    
    
}
