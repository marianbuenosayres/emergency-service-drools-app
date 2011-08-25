/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.model.events;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author esteban
 */
public class VehicleHitsEmergencyEvent implements CallEvent, Serializable {
    private String callId;
    private Long vehicleId;
    
    private Date time;

    public VehicleHitsEmergencyEvent() {
    }

    public VehicleHitsEmergencyEvent(Date time) {
        this.time = time;
    }
    
    public VehicleHitsEmergencyEvent(String callId, Long vehicleId, Date time) {
        this.callId = callId;
        this.vehicleId = vehicleId;
        this.time = time;
    }

    @Override
    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    
    
    

}
