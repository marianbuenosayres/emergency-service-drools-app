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
public class FireTruckDecreaseWaterLevelEvent implements EmergencyVehicleEvent, Serializable {
    private String emergencyId;
    private String vehicleId;
    private Date time;
    private boolean processed;
    
    public FireTruckDecreaseWaterLevelEvent() {
    }

    public FireTruckDecreaseWaterLevelEvent(Date time) {
        this.time = time;
    }
    
    public FireTruckDecreaseWaterLevelEvent(String emergencyId, String vehicleId, Date time) {
        this.emergencyId = emergencyId;
        this.vehicleId = vehicleId;
        this.time = time;
    }

    @Override
    public String getEmergencyId() {
        return emergencyId;
    }

   

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "FireTruckDecreaseWaterLevelEvent{" + "emergencyId=" + emergencyId + ", vehicleId=" + vehicleId + ", time=" + time + ", processed=" + processed + '}';
    }
    
    


}
