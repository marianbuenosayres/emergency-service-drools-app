/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.services;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbpm.services.task.wih.NonManagedLocalHTWorkItemHandler;
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
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.io.ResourceFactory;

import com.wordpress.salaboy.model.Procedure;
import com.wordpress.salaboy.model.events.EmergencyEndsEvent;
import com.wordpress.salaboy.model.events.FireExtinctedEvent;
import com.wordpress.salaboy.model.events.FireTruckOutOfWaterEvent;
import com.wordpress.salaboy.model.events.VehicleHitsEmergencyEvent;
import com.wordpress.salaboy.model.events.VehicleHitsFireDepartmentEvent;
import com.wordpress.salaboy.services.workitemhandlers.ProcedureReportWorkItemHandler;
import com.wordpress.salaboy.workitemhandlers.DispatchVehicleWorkItemHandler;
import com.wordpress.salaboy.workitemhandlers.NotifyEndOfProcedureWorkItemHandler;

/**
 *
 * @author esteban
 */
public class DefaultFireProcedureImpl implements DefaultFireProcedure {
    private String emergencyId;
    private KieSession internalSession;
    private String procedureName;
    private ProcessInstance processInstance;

    public DefaultFireProcedureImpl() {
        this.procedureName = "com.wordpress.salaboy.bpmn2.DefaultFireProcedure";
    }

    private KieSession createDefaultFireProcedureSession(String emergencyId) throws IOException {
        System.out.println(">>>> I'm creating the DefaultFireProcedure procedure for emergencyId = "+emergencyId);

        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();
        
        kfs.write("src/main/resources/MultiVehicleProcedure.bpmn2", ResourceFactory.newClassPathResource("processes/procedures/MultiVehicleProcedure.bpmn"));
        kfs.write("src/main/resources/DefaultFireProcedure.bpmn2", ResourceFactory.newClassPathResource("processes/procedures/DefaultFireProcedure.bpmn"));
        kfs.write("src/main/resources/select_water_refill_destination.drl", ResourceFactory.newClassPathResource("rules/select_water_refill_destination.drl"));
        kfs.write("src/main/resources/defaultFireProcedureEventHandling.drl", ResourceFactory.newClassPathResource("rules/defaultFireProcedureEventHandling.drl"));
        
        KieBuilder kbuilder = ks.newKieBuilder(kfs);
        kbuilder.buildAll();

        Results res = kbuilder.getResults();
        if (res != null && res.hasMessages(Message.Level.ERROR)) {
            System.out.println(">>>>>>> Error: " + res);
            throw new IllegalStateException("Failed to parse knowledge!");
        }
        
        KieContainer kcontainer = ks.newKieContainer(kbuilder.getKieModule().getReleaseId());
        KieBaseConfiguration kbaseConf = ks.newKieBaseConfiguration();
        kbaseConf.setOption(EventProcessingOption.STREAM);
        KieBase kbase = kcontainer.newKieBase(kbaseConf);


        KieSession session = kbase.newKieSession();
        ks.getLoggers().newConsoleLogger((KieRuntimeEventManager) session);

        return session;

    }

    private void setWorkItemHandlers(KieSession session) {
        session.getWorkItemManager().registerWorkItemHandler("Report", new ProcedureReportWorkItemHandler());
        session.getWorkItemManager().registerWorkItemHandler("DispatchSelectedVehicle", new DispatchVehicleWorkItemHandler());
        session.getWorkItemManager().registerWorkItemHandler("NotifyEndOfProcedure", new NotifyEndOfProcedureWorkItemHandler());
        session.getWorkItemManager().registerWorkItemHandler("Human Task", 
        		new NonManagedLocalHTWorkItemHandler(session, HumanTaskServerService.getInstance().getTaskService()));

    }

    @Override
    public void vehicleReachesEmergencyNotification(VehicleHitsEmergencyEvent event) {
        internalSession.insert(event);
    }

    @Override
    public void fireTruckOutOfWaterNotification(FireTruckOutOfWaterEvent event) {
        //we need the event as a fact in order to make inference.
        System.out.println(">>>>>>>> Inserting FireTruckOutOfWaterEvent ");
        internalSession.insert(event);
    }

    @Override
    public void configure(String emergencyId, Procedure procedure, Map<String, Object> parameters) {
	if (!parameters.containsKey("emergency")){
            throw new IllegalStateException("Trying to start DefaultFireProcedure wihtout passing an Emergency!");
        }

        this.emergencyId = emergencyId;
        try {
            internalSession = createDefaultFireProcedureSession(this.emergencyId);
        } catch (IOException ex) {
            Logger.getLogger(DefaultFireProcedureImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        setWorkItemHandlers(internalSession);

        new Thread(new Runnable() {
            @Override
            public void run() {
                internalSession.fireUntilHalt();
            }
        }).start();
        
        parameters.put("concreteProcedureId", this.procedureName);
        parameters.put("procedure", procedure);
        
        internalSession.insert(parameters.get("emergency"));
        processInstance = internalSession.startProcess("com.wordpress.salaboy.bpmn2.MultiVehicleProcedure", parameters);
        
        
        procedure.setProcessInstanceId(processInstance.getId());
    }
    
    @Override
    public void procedureEndsNotification(EmergencyEndsEvent event) {
        internalSession.signalEvent("com.wordpress.salaboy.model.events.EmergencyEndsEvent", event);
    }

    @Override
    public void fireExtinctedNotification(FireExtinctedEvent event) {
        internalSession.signalEvent("com.wordpress.salaboy.model.events.FireExtinctedEvent", event);
    }

    @Override
    public void vehicleHitsFireDepartmentEventNotification(VehicleHitsFireDepartmentEvent vehicleHitsFireDepartmentEvent) {
        internalSession.insert(vehicleHitsFireDepartmentEvent);
    }
}
