/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.workitemhandlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hornetq.api.core.HornetQException;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import com.wordpress.salaboy.messaging.MessageFactory;
import com.wordpress.salaboy.model.Emergency;
import com.wordpress.salaboy.model.Vehicle;
import com.wordpress.salaboy.model.messages.VehicleDispatchedMessage;

/**
 *
 * @author esteban
 */
public class DispatchVehicleWorkItemHandler implements WorkItemHandler{

    
    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        String emergencyId = ((Emergency) workItem.getParameter("emergency")).getId();
        Vehicle vehicle = (Vehicle) workItem.getParameter("vehicle");
        try {
            MessageFactory.sendMessage(new VehicleDispatchedMessage(emergencyId, vehicle.getId()));
        } catch (HornetQException ex) {
            Logger.getLogger(DispatchVehicleWorkItemHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        manager.completeWorkItem(workItem.getId(), null);
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        
    }
    
}
