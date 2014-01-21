/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.services;


import java.io.IOException;
import java.util.Map;
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
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.io.ResourceFactory;

import com.wordpress.salaboy.model.Procedure;
import com.wordpress.salaboy.model.events.EmergencyEndsEvent;
import com.wordpress.salaboy.services.workitemhandlers.AsyncStartProcedureWorkItemHandler;

/**
 *
 * @author salaboy
 */
public class AdHocProcedureImpl implements AdHocProcedure{

    private String emergencyId;
    private KieSession internalSession;
    private String procedureName;

    public AdHocProcedureImpl() {
        this.procedureName = "AdHocProcedure";
    }

    private KieSession createAdHocProcedureSession(String callId) throws IOException {
    	KieServices ks = KieServices.Factory.get();
    	KieFileSystem kfs = ks.newKieFileSystem();
    	kfs.write("src/main/resources/ad-hoc.bpmn2", ResourceFactory.newClassPathResource("processes/procedures/AdHocProcedure.bpmn"));
    	System.out.println("Starting Local Session");
    	KieBuilder kbuilder = ks.newKieBuilder(kfs);
    	kbuilder.buildAll();
        Results res = kbuilder.getResults();
        if (res.hasMessages(Message.Level.ERROR)) {
        	System.out.println(">>> Error compiling kie base: \n" + res);
            throw new IllegalStateException("Failed to parse knowledge!");
        }
        KieBaseConfiguration kbaseConf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        kbaseConf.setOption(EventProcessingOption.STREAM);
        KieContainer kcontainer = ks.newKieContainer(kbuilder.getKieModule().getReleaseId());
        KieBase kbase = kcontainer.newKieBase(kbaseConf);
        KieSession ksession = kbase.newKieSession();
        return ksession;

    }
    
    @Override
    public void configure(String emergencyId, Procedure procedure, Map<String, Object> parameters) {
        this.emergencyId = emergencyId;
        try {
            internalSession = createAdHocProcedureSession(this.emergencyId);
        } catch (IOException ex) {
            Logger.getLogger(AdHocProcedureImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        setWorkItemHandlers(internalSession);

        new Thread(new Runnable() {
            public void run() {
                internalSession.fireUntilHalt();
            }
        }).start();
        ProcessInstance pi = internalSession.startProcess("com.wordpress.salaboy.bpmn2.AdHocProcedure", parameters);
        
        procedure.setProcessInstanceId(pi.getId());
    }
    
    
    private void setWorkItemHandlers(KieSession session) {
        session.getWorkItemManager().registerWorkItemHandler("Start Procedure", new AsyncStartProcedureWorkItemHandler());
    }

    @Override
    public void procedureEndsNotification(EmergencyEndsEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
