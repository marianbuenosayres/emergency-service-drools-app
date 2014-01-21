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
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import com.wordpress.salaboy.model.Procedure;
import com.wordpress.salaboy.model.events.EmergencyEndsEvent;

/**
 *
 * @author salaboy
 */
public class DumbProcedureImpl implements DumbProcedure {
    private String id;
    private String callId;
    private KieSession internalSession;
    private String procedureName;

    public DumbProcedureImpl() {
        this.procedureName = "DumbProcedure";
    }

    private KieSession createDumbProcedureSession(String callId) throws IOException {
        System.out.println(">>>> I'm creating the "+"DumbProcedure"+" procedure for emergencyId = "+callId);
        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write("src/main/resources/dumb-proc.bpmn2", ResourceFactory.newClassPathResource("processes/procedures/DumbProcedure.bpmn"));
        KieBuilder kbuilder = ks.newKieBuilder(kfs);
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^Building Resources");
        kbuilder.buildAll();
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^Finish Building Resources");
        Results res = kbuilder.getResults();
        if (res != null && res.hasMessages(Message.Level.ERROR)) {
            System.out.println(">>>>>>> Error: " + res);
            throw new IllegalStateException("Failed to parse knowledge!");
        }
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^Configure KIE BASE");
        KieBaseConfiguration kbaseConf = ks.newKieBaseConfiguration();
        kbaseConf.setOption(org.kie.api.conf.EventProcessingOption.STREAM);
        KieContainer kcontainer = ks.newKieContainer(kbuilder.getKieModule().getReleaseId());
        KieBase kbase = kcontainer.newKieBase(kbaseConf);
        
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^Finishing Configure KIE BASE");
        KieSession session = kbase.newKieSession();
        return session;

    }

    @Override
    public void procedureEndsNotification(EmergencyEndsEvent event) {
    }
   
    @Override 
    public void configure(String callId, Procedure procedure, Map<String, Object> parameters) {
        this.callId = callId;
        try {
            internalSession = createDumbProcedureSession(this.callId);
        } catch (IOException ex) {
            Logger.getLogger(DumbProcedureImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Thread(new Runnable() {

            public void run() {
                internalSession.fireUntilHalt();
            }
        }).start();
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^Starting DUMB PROCEDURE");
        internalSession.startProcess("com.wordpress.salaboy.bpmn2.DumbProcedure", parameters);
    }

}
