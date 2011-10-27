/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.wordpress.salaboy.services;

import com.wordpress.salaboy.model.FireTruck;
import com.wordpress.salaboy.model.Vehicle;
import com.wordpress.salaboy.model.events.EmergencyVehicleEvent;
import com.wordpress.salaboy.model.events.FireTruckDecreaseWaterLevelEvent;
import com.wordpress.salaboy.model.events.FireTruckWaterRefilledEvent;
import com.wordpress.salaboy.model.serviceclient.PersistenceServiceProvider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.conf.EventProcessingOption;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

/**
 * @author esteban
 */
public class FireTruckMonitorService implements VehicleMonitorService{

    private StatefulKnowledgeSession session;
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
        sessionThread.stop();
    }
    
    public void processFireTruckDecreaseWaterLevelEvent(FireTruckDecreaseWaterLevelEvent event){
        session.insert(event);
    }

    private StatefulKnowledgeSession createFireTruckMonitorSession(String vehicleId) throws IOException {
//        Map<String, GridServiceDescription> coreServicesMap = new HashMap<String, GridServiceDescription>();
//        GridServiceDescriptionImpl gsd = new GridServiceDescriptionImpl(WhitePages.class.getName());
//        Address addr = gsd.addAddress("socket");
//        addr.setObject(new InetSocketAddress[]{new InetSocketAddress("localhost", 8000)});
//        coreServicesMap.put(WhitePages.class.getCanonicalName(), gsd);
//
//        GridImpl grid = new GridImpl(new ConcurrentHashMap<String, Object>());
//
//        GridPeerConfiguration conf = new GridPeerConfiguration();
//        GridPeerServiceConfiguration coreSeviceConf = new CoreServicesLookupConfiguration(coreServicesMap);
//        conf.addConfiguration(coreSeviceConf);
//
//        GridPeerServiceConfiguration wprConf = new WhitePagesRemoteConfiguration();
//        conf.addConfiguration(wprConf);
//
//        conf.configure(grid);
//
//        GridServiceDescription<GridNode> n1Gsd = grid.get(WhitePages.class).lookup("n1");
//        GridConnection<GridNode> conn = grid.get(ConnectionFactoryService.class).createConnection(n1Gsd);
//        GridNode remoteN1 = conn.connect();
//
//
//        KnowledgeBuilder kbuilder = remoteN1.get(KnowledgeBuilderFactoryService.class).newKnowledgeBuilder();
//
//        kbuilder.add(new ByteArrayResource(IOUtils.toByteArray(new ClassPathResource("rules/fireTruck.drl").getInputStream())), ResourceType.DRL);
//
//        KnowledgeBuilderErrors errors = kbuilder.getErrors();
//        if (errors != null && errors.size() > 0) {
//            for (KnowledgeBuilderError error : errors) {
//                System.out.println(">>>>>>> Error: " + error.getMessage());
//
//            }
//            throw new IllegalStateException("Failed to parse knowledge!");
//        }
//        KnowledgeBaseConfiguration kbaseConf = remoteN1.get(KnowledgeBaseFactoryService.class).newKnowledgeBaseConfiguration();
//        kbaseConf.setOption(EventProcessingOption.STREAM);
//        KnowledgeBase kbase = remoteN1.get(KnowledgeBaseFactoryService.class).newKnowledgeBase(kbaseConf);
        
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules/fireTruck.drl"), ResourceType.DRL);
        
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors != null && errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(">>>>>>> Error: " + error.getMessage());

            }
            throw new IllegalStateException("Failed to parse knowledge!");
        }
        
        KnowledgeBaseConfiguration kbaseConf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        kbaseConf.setOption(EventProcessingOption.STREAM);
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseConf);
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);
        //remoteN1.set("FireTruckMonitorSession" + vehicleId, session);

        return session;

    }

    private void processFireTruckWaterRefilledEvent(FireTruckWaterRefilledEvent fireTruckWaterRefilledEvent) {
        System.out.println(">>>>Updating Truck Water Refilled!!!");
        FireTruck truck = (FireTruck)PersistenceServiceProvider.getPersistenceService().loadVehicle(fireTruckWaterRefilledEvent.getVehicleId());
        session.update(truckFactHandle, truck);
    }
}
