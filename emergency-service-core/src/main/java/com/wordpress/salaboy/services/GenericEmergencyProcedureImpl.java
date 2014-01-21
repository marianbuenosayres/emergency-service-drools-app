/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.services;

import java.io.IOException;
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
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.internal.io.ResourceFactory;

import com.wordpress.salaboy.model.Call;
import com.wordpress.salaboy.model.ProcedureCompleted;
import com.wordpress.salaboy.model.events.AllProceduresEndedEvent;
import com.wordpress.salaboy.services.workitemhandlers.AsyncStartProcedureWorkItemHandler;
import com.wordpress.salaboy.workitemhandlers.MyReportingWorkItemHandler;

/**
 *
 * @author salaboy
 */
public class GenericEmergencyProcedureImpl implements GenericEmergencyProcedure {
    private static GenericEmergencyProcedureImpl instance;
    private KieSession genericEmergencySession;
    private GenericEmergencyProcedureImpl() {
        configure();
        setWorkItemHandlers();
    }
    
    
    public static GenericEmergencyProcedureImpl getInstance(){
        if(instance == null){
            instance = new GenericEmergencyProcedureImpl();
        }
        return instance;
    }

    private EntryPoint getPhoneCallsEntryPoint(){
        return genericEmergencySession.getEntryPoint("phoneCalls stream");
    }
    
    @Override
    public void newPhoneCall(Call call){
        //Track new call
        getPhoneCallsEntryPoint().insert(call);
    }
    
    private KieSession createGenericEmergencyServiceSession() throws IOException{

    	KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write("src/main/resources/emergency-proc.bpmn2", ResourceFactory.newClassPathResource("processes/procedures/GenericEmergencyProcedure.bpmn"));
        kfs.write("src/main/resources/phoneCallsManagement.drl", ResourceFactory.newClassPathResource("rules/phoneCallsManagement.drl"));
        kfs.write("src/main/resources/procedureSuggestions.drl", ResourceFactory.newClassPathResource("rules/procedureSuggestions.drl"));
        kfs.write("src/main/resources/emergencyLifeCycle.drl", ResourceFactory.newClassPathResource("rules/emergencyLifeCycle.drl"));
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
        return session;
        
    }

    private void setWorkItemHandlers() {
        genericEmergencySession.getWorkItemManager().registerWorkItemHandler("Human Task", 
        		new NonManagedLocalHTWorkItemHandler(genericEmergencySession,HumanTaskServerService.getInstance().getTaskService()));
        genericEmergencySession.getWorkItemManager().registerWorkItemHandler("Start Procedure", new AsyncStartProcedureWorkItemHandler());
        genericEmergencySession.getWorkItemManager().registerWorkItemHandler("Reporting", new MyReportingWorkItemHandler());
        
    }

    @Override
    public void allProceduresEnededNotification(AllProceduresEndedEvent event) {
        genericEmergencySession.signalEvent("com.wordpress.salaboy.model.events.AllProceduresEndedEvent", event);
    }

    

    
    public void configure() {
        System.out.println(">>> Initializing Generic Emergency Procedure Service ...");
        try {
            genericEmergencySession = createGenericEmergencyServiceSession();
        } catch (IOException ex) {
            Logger.getLogger(GenericEmergencyProcedureImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread(new Runnable(){

            @Override
            public void run() {
                genericEmergencySession.fireUntilHalt();
            }
        }).start();
        System.out.println(">>> Initializing Generic Emergency Procedure Service Running ...");
    }

    @Override
    public void procedureCompletedNotification(String emergencyId, String procedureId) {
        genericEmergencySession.insert(new ProcedureCompleted(emergencyId, procedureId));
    }
    
}
