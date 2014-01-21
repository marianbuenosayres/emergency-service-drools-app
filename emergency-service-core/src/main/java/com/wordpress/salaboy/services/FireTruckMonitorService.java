/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.wordpress.salaboy.services;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.event.KieRuntimeEventManager;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.io.ResourceFactory;

import com.wordpress.salaboy.model.FireTruck;
import com.wordpress.salaboy.model.Vehicle;
import com.wordpress.salaboy.model.events.EmergencyVehicleEvent;
import com.wordpress.salaboy.model.events.FireTruckDecreaseWaterLevelEvent;
import com.wordpress.salaboy.model.events.FireTruckWaterRefilledEvent;
import com.wordpress.salaboy.model.serviceclient.PersistenceServiceProvider;

/**
 * @author esteban
 */
public class FireTruckMonitorService implements VehicleMonitorService{

    private KieSession session;
    private Thread sessionThread;
    private FactHandle truckFactHandle;
    public FireTruckMonitorService(String vehicleId) {
        try {
            this.session = createFireTruckMonitorSession(vehicleId);
        } catch (IOException ex) {
            Logger.getLogger(FireTruckMonitorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void newVehicleDispatched(final String emergencyId, final String vehicleId) {
        
            Vehicle vehicle = PersistenceServiceProvider.getPersistenceService().loadVehicle(vehicleId);
            if (vehicle == null){
                throw new IllegalArgumentException("Unknown Vehicle "+vehicleId);
            }
            
            
            session.setGlobal("emergencyId", emergencyId);
            truckFactHandle = session.insert(vehicle);
            
            
            sessionThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    session.fireUntilHalt();
                }
            });
            
            sessionThread.start();
            
    }
    
    @Override
    public void processEvent(EmergencyVehicleEvent event){
        if (event instanceof FireTruckDecreaseWaterLevelEvent){
            this.processFireTruckDecreaseWaterLevelEvent((FireTruckDecreaseWaterLevelEvent)event);
        }else if (event instanceof FireTruckWaterRefilledEvent){
            this.processFireTruckWaterRefilledEvent((FireTruckWaterRefilledEvent)event);
        }
    }

    @Override
    public void vehicleRemoved() {
        session.dispose();
        sessionThread.interrupt();
    }
    
    public void processFireTruckDecreaseWaterLevelEvent(FireTruckDecreaseWaterLevelEvent event){
        session.insert(event);
    }

    private KieSession createFireTruckMonitorSession(String vehicleId) throws IOException {
    	KieServices ks = KieServices.Factory.get();
    	KieFileSystem kfs = ks.newKieFileSystem();
    	kfs.write("src/main/resources/fireTruck.drl", ResourceFactory.newClassPathResource("rules/fireTruck.drl"));
    	
    	KieBuilder kbuilder = ks.newKieBuilder(kfs);
    	kbuilder.buildAll();
    	
        Results res = kbuilder.getResults();
        if (res != null && res.hasMessages(Message.Level.ERROR)) {
            System.out.println(">>>>>>> Error: " + res);
            throw new IllegalStateException("Failed to parse knowledge!");
        }
        KieBaseConfiguration kbaseConf = ks.newKieBaseConfiguration();
        kbaseConf.setOption(EventProcessingOption.STREAM);
        KieContainer kcontainer = ks.newKieContainer(kbuilder.getKieModule().getReleaseId());
        KieBase kbase = kcontainer.newKieBase(kbaseConf);
        KieSession session = kbase.newKieSession();
        ks.getLoggers().newConsoleLogger((KieRuntimeEventManager) session);
        return session;

    }

    private void processFireTruckWaterRefilledEvent(FireTruckWaterRefilledEvent fireTruckWaterRefilledEvent) {
        System.out.println(">>>>Updating Truck Water Refilled!!!");
        FireTruck truck = (FireTruck)PersistenceServiceProvider.getPersistenceService().loadVehicle(fireTruckWaterRefilledEvent.getVehicleId());
        session.update(truckFactHandle, truck);
    }
}
