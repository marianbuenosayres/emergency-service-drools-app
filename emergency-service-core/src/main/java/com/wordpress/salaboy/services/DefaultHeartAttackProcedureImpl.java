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
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.io.ResourceFactory;

import com.wordpress.salaboy.model.Procedure;
import com.wordpress.salaboy.model.events.EmergencyEndsEvent;
import com.wordpress.salaboy.model.events.VehicleHitsEmergencyEvent;
import com.wordpress.salaboy.model.events.VehicleHitsHospitalEvent;
import com.wordpress.salaboy.services.workitemhandlers.ProcedureReportWorkItemHandler;
import com.wordpress.salaboy.workitemhandlers.DispatchVehicleWorkItemHandler;
import com.wordpress.salaboy.workitemhandlers.NotifyEndOfProcedureWorkItemHandler;

/**
 *
 * @author salaboy
 */
public class DefaultHeartAttackProcedureImpl implements DefaultHeartAttackProcedure {

    private String emergencyId;
    private KieSession internalSession;
    private String procedureName;
    
    public DefaultHeartAttackProcedureImpl() {
        this.procedureName = "com.wordpress.salaboy.bpmn2.DefaultHeartAttackProcedure";
    }

    private KieSession createDefaultHeartAttackProcedureSession(String emergencyId) throws IOException {
        System.out.println(">>>> I'm creating the "+"DefaultHeartAttackProcedure"+" procedure for emergencyId = "+emergencyId);

        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write("src/main/resources/MultipleVehicleProcedure.bpmn2", ResourceFactory.newClassPathResource("processes/procedures/MultiVehicleProcedure.bpmn"));
        kfs.write("src/main/resources/DefaultHeartAttackProcedure.bpmn2", ResourceFactory.newClassPathResource("processes/procedures/DefaultHeartAttackProcedure.bpmn"));
        kfs.write("src/main/resources/select_hospital.drl", ResourceFactory.newClassPathResource("rules/select_hospital.drl"));
        kfs.write("src/main/resources/defaultHeartAttackProcedureEventHandling.drl", ResourceFactory.newClassPathResource("rules/defaultHeartAttackProcedureEventHandling.drl"));
        
        KieBuilder kbuilder = ks.newKieBuilder(kfs);
        kbuilder.buildAll();
        Results res = kbuilder.getResults();
        if (res != null && res.hasMessages(Message.Level.ERROR)) {
            System.out.println(">>>>>>> Error: " + res);
            throw new IllegalStateException("Failed to parse knowledge!");
        }

        KieBaseConfiguration kbaseConf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        kbaseConf.setOption(EventProcessingOption.STREAM);
        KieContainer kcontainer = ks.newKieContainer(kbuilder.getKieModule().getReleaseId());
        KieBase kbase = kcontainer.newKieBase(kbaseConf);
        KieSession session = kbase.newKieSession();
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
    public void patientAtHospitalNotification(VehicleHitsHospitalEvent event) {
        internalSession.signalEvent("com.wordpress.salaboy.model.events.PatientAtHospitalEvent", event);
    }

    @Override
    public void patientPickUpNotification(VehicleHitsEmergencyEvent event) {
        internalSession.insert(event);
    }

    @Override
    public void procedureEndsNotification(EmergencyEndsEvent event) {
    }
    
    @Override
    public void configure( String emergencyId, Procedure procedure, Map<String, Object> parameters) {
        this.emergencyId = emergencyId;
        try {
            internalSession = createDefaultHeartAttackProcedureSession(this.emergencyId);
        } catch (IOException ex) {
            Logger.getLogger(DefaultHeartAttackProcedureImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        setWorkItemHandlers(internalSession);

        new Thread(new Runnable() {

            public void run() {
                internalSession.fireUntilHalt();
            }
        }).start();

        parameters.put("concreteProcedureId", this.procedureName);
        parameters.put("procedure", procedure);
        
        ProcessInstance pi = internalSession.startProcess("com.wordpress.salaboy.bpmn2.MultiVehicleProcedure",parameters);
        internalSession.insert(pi);    

        procedure.setProcessInstanceId(pi.getId());
    }

}
