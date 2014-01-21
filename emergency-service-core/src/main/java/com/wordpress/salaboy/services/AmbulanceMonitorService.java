/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.wordpress.salaboy.services;

import java.io.IOException;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import com.wordpress.salaboy.model.events.EmergencyVehicleEvent;
import com.wordpress.salaboy.model.events.PulseEvent;

/**
 * @author esteban
 */
public class AmbulanceMonitorService implements VehicleMonitorService {

    private KieSession session;
    private Thread sessionThread;

    public AmbulanceMonitorService() {
        
    }

    @Override
    public void newVehicleDispatched(final String emergencyId, final String vehicleId) {
        try {
            session = createAmbulanceMonitorSession(vehicleId);
            session.setGlobal("vehicleId", vehicleId);
            session.setGlobal("emergencyId", emergencyId);

            sessionThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    session.fireUntilHalt();
                }
            });

            sessionThread.start();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void vehicleRemoved() {
        session.dispose();
        sessionThread.interrupt();
    }

    public void newHeartBeatReceived(PulseEvent event) {
        session.getEntryPoint("patientHeartbeats").insert(event);
    }
    
    @Override
    public void processEvent(EmergencyVehicleEvent event){
        if (event instanceof PulseEvent){
            this.newHeartBeatReceived((PulseEvent)event);
        }
    }

    private KieSession createAmbulanceMonitorSession(String vehicleId) throws IOException {
    	
    	KieServices ks = KieServices.Factory.get();
    	KieFileSystem kfs = ks.newKieFileSystem();
    	//kfs.write("src/main/resources/patient.drl", ResourceFactory.newClassPathResource("rules/patient.drl"));
    	kfs.write("src/main/resources/patient.dsl", ResourceFactory.newClassPathResource("rules/patient.dsl"));
    	kfs.write("src/main/resources/patient.dslr", ResourceFactory.newClassPathResource("rules/patient.dslr"));
    	KieBuilder kbuilder = ks.newKieBuilder(kfs);
    	kbuilder.buildAll();
        Results res = kbuilder.getResults();
        if (res != null && res.hasMessages(Message.Level.ERROR)) {
            System.out.println(">>>>>>> Error: " + res);
            throw new IllegalStateException("Failesd to parse knowledge!");
        }
        KieBaseConfiguration kbaseConf = ks.newKieBaseConfiguration();
        kbaseConf.setOption(org.kie.api.conf.EventProcessingOption.STREAM);
        KieContainer kcontainer = ks.newKieContainer(kbuilder.getKieModule().getReleaseId());
        KieBase kbase = kcontainer.newKieBase(kbaseConf);
        KieSession session = kbase.newKieSession();
        return session;

    }
}
