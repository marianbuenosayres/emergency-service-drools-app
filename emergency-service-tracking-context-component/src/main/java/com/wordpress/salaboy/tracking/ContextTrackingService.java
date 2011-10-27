/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.tracking;

/**
 *
 * @author salaboy
 */
public interface ContextTrackingService  {

    //Creation
    public String newCallId();
    
    public String newEmergencyId();
    
    public String newProcedureId();
    
    public String newVehicleId();
    
    public String newServiceChannelId();
    
    public String newPatientId();
    
    
    //Attach
    public void attachEmergency(String callId, String emergencyId);

    public void attachProcedure(String emergencyId, String procedureId);

    public void attachVehicle(String procedureId, String vehicleId);

    public void attachServiceChannel(String emergencyId, String channelId);

    
    //Detach
    
    public void detachVehicle(String vehicleId);

    public void detachProcedure(String procedureId);

    public void detachEmergency(String emergencyId);
    
    public void detachServiceChannel(String serviceChannelId);
    
    public void detachPatient(String patientId);
}
