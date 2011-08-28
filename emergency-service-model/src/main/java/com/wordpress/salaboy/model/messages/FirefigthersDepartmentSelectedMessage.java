/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.model.messages;

import com.wordpress.salaboy.model.FirefightersDepartment;
import com.wordpress.salaboy.model.Hospital;
import java.io.Serializable;

/**
 *
 * @author esteban
 */
public class FirefigthersDepartmentSelectedMessage implements Serializable, EmergencyInterchangeMessage{
    private FirefightersDepartment firefigthersDepartment;
    private Long callId;

    public FirefigthersDepartmentSelectedMessage(Long callId, FirefightersDepartment firefigthersDepartment) {
        this.firefigthersDepartment = firefigthersDepartment;
        this.callId = callId;
    }

    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }

    public FirefightersDepartment getFirefigthersDepartment() {
        return firefigthersDepartment;
    }

    public void setFirefigthersDepartment(FirefightersDepartment firefigthersDepartment) {
        this.firefigthersDepartment = firefigthersDepartment;
    }

    @Override
    public String toString() {
        return "FireFighterDepartmentSelectedMessage{" + "firefigthersDepartment=" + firefigthersDepartment + ", callId=" + callId + '}';
    }
    
    
    
    
}
