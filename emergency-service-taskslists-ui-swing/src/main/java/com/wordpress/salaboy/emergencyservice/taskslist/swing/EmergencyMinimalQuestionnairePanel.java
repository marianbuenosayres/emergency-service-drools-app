/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EmergencyMinimalQuestionnairePanel.java
 *
 * Created on Nov 24, 2010, 4:00:11 PM
 */
package com.wordpress.salaboy.emergencyservice.taskslist.swing;

import com.wordpress.salaboy.api.HumanTaskService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.example.ws_ht.api.TAttachment;
import org.example.ws_ht.api.TAttachmentInfo;
import org.example.ws_ht.api.TStatus;
import org.example.ws_ht.api.TTask;
import org.example.ws_ht.api.wsdl.IllegalAccessFault;
import org.example.ws_ht.api.wsdl.IllegalArgumentFault;
import org.example.ws_ht.api.wsdl.IllegalStateFault;
import org.jbpm.task.AccessType;
import org.jbpm.task.Content;
import org.jbpm.task.service.ContentData;

/**
 *
 * @author esteban
 * @author salaboy
 */
public class EmergencyMinimalQuestionnairePanel extends javax.swing.JPanel {

    private final HumanTaskService taskClient;
    private String taskId;
    private PhoneCallsPanel parent;

    public EmergencyMinimalQuestionnairePanel(PhoneCallsPanel parent, HumanTaskService taskClient, String id) {
        this.taskClient = taskClient;
        this.taskId = id;
        this.parent = parent;
        initComponents();
        configure();
        
    }
    
    
    private HumanTaskService getTaskClient(){
        return this.taskClient;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel12 = new javax.swing.JLabel();
        locationxjTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        genderjComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        emergencyTypeJComboBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        ageJTextField = new javax.swing.JTextField();
        suggestProcedureJButton = new javax.swing.JButton();
        locationyjTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        nroOfPeoplejFormattedTextField = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        taskActionjButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        phoneCalljLabel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(300, 480));
        setName("Emergency Questionaire"); // NOI18N

        jLabel12.setText("Location:");

        jLabel2.setText("Gender:");

        genderjComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MALE", "FEMALE" }));
        genderjComboBox.setEnabled(false);

        jLabel3.setText("Emergency Type:");

        emergencyTypeJComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FIRE", "CAR_CRASH", "HEART_ATTACK", "ROBBERY" }));

        jLabel5.setText("Age:");

        ageJTextField.setEnabled(false);

        suggestProcedureJButton.setText("Suggest Procedure");
        suggestProcedureJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suggestProcedureJButtonActionPerformed(evt);
            }
        });

        jLabel13.setText("Number of People:");

        nroOfPeoplejFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        nroOfPeoplejFormattedTextField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                nroOfPeoplejFormattedTextFieldPropertyChange(evt);
            }
        });
        nroOfPeoplejFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nroOfPeoplejFormattedTextFieldKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nroOfPeoplejFormattedTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nroOfPeoplejFormattedTextFieldKeyReleased(evt);
            }
        });

        jLabel4.setText("Single Person Emergency");

        taskActionjButton.setText("Start");
        taskActionjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taskActionjButtonActionPerformed(evt);
            }
        });

        jLabel6.setText("Task Action:");

        jLabel7.setText("Phone Call:");

        phoneCalljLabel.setText("<Call Information>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(taskActionjButton)
                        .addContainerGap(199, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel5))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(genderjComboBox, 0, 228, Short.MAX_VALUE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(ageJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 169, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel12)
                                                .addComponent(jLabel13))
                                            .addGap(13, 13, 13)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(11, 11, 11)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(emergencyTypeJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                            .addGap(8, 8, 8)
                                                            .addComponent(locationxjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(locationyjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addComponent(nroOfPeoplejFormattedTextField)))))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(39, 39, 39)
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(suggestProcedureJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(74, 74, 74))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phoneCalljLabel)
                        .addContainerGap(164, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(taskActionjButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(phoneCalljLabel))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emergencyTypeJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(locationyjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(locationxjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(nroOfPeoplejFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(genderjComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(ageJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(suggestProcedureJButton)
                .addContainerGap(125, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void configure(){
        String taskinfo = "";

        try {
            ObjectInputStream ois = null;


            //List<TTaskAbstract> taskAbstracts = getTaskClient().getMyTaskAbstracts("", "operator", "", null, "", "", "", 0, 0);
            //TTaskAbstract taskAbstract = taskAbstracts.get(0);



            List<TAttachmentInfo> attachmentsInfo = getTaskClient().getAttachmentInfos(this.taskId);
            TAttachmentInfo firstAttachmentInfo = attachmentsInfo.get(0);
            TAttachment attachment = getTaskClient().getAttachments(this.taskId, firstAttachmentInfo.getName()).get(0);

            ByteArrayInputStream bais = new ByteArrayInputStream(((Content) attachment.getValue()).getContent());
            ois = new ObjectInputStream(bais);
            taskinfo = (String) ois.readObject();
        } catch (Exception ex) {
            Logger.getLogger(UserTaskListUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] values= taskinfo.split(",");
        System.out.println("TaskInfo = "+taskinfo);
        String timestamp = values[0].trim(); 
        String phoneNumber = values[1].trim(); 
        String locationX = values[2].trim();
        String locationY = values[3].trim();
        locationxjTextField.setText(locationX);
        locationyjTextField.setText(locationY);
        phoneCalljLabel.setText("Time: "+timestamp +" - Phone Number: "+phoneNumber);
    }
    
    private void suggestProcedureJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suggestProcedureJButtonActionPerformed
        ObjectOutputStream out = null;
        try {

            Map<String, Object> info = new HashMap<String, Object>();
            info.put("emergency.locationx", locationxjTextField.getText());
            info.put("emergency.locationy", locationyjTextField.getText());
            info.put("emergency.type", emergencyTypeJComboBox.getModel().getSelectedItem());
            info.put("emergency.nroofpeople", Integer.valueOf(nroOfPeoplejFormattedTextField.getText()));
            if(nroOfPeoplejFormattedTextField.getText().equals("1")){
                info.put("patient.age", Integer.valueOf(ageJTextField.getText()));
                info.put("patient.gender", genderjComboBox.getModel().getSelectedItem());
            }

            ContentData result = new ContentData();
            result.setAccessType(AccessType.Inline);
            result.setType("java.util.Map");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(info);
            result.setContent(bos.toByteArray());
           
            getTaskClient().setAuthorizedEntityId("operator");
            getTaskClient().complete(this.taskId.toString(), result);
            

            this.parent.hideDialog();

        } catch (IllegalArgumentFault ex) {
            Logger.getLogger(EmergencyMinimalQuestionnairePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateFault ex) {
            Logger.getLogger(EmergencyMinimalQuestionnairePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessFault ex) {
            Logger.getLogger(EmergencyMinimalQuestionnairePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EmergencyMinimalQuestionnairePanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(EmergencyMinimalQuestionnairePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_suggestProcedureJButtonActionPerformed

    private void nroOfPeoplejFormattedTextFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_nroOfPeoplejFormattedTextFieldPropertyChange

    }//GEN-LAST:event_nroOfPeoplejFormattedTextFieldPropertyChange

    private void nroOfPeoplejFormattedTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nroOfPeoplejFormattedTextFieldKeyPressed
             genderjComboBox.setEnabled(isOnePersonEmergency());
             ageJTextField.setEnabled(isOnePersonEmergency());
    }//GEN-LAST:event_nroOfPeoplejFormattedTextFieldKeyPressed

    private void nroOfPeoplejFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nroOfPeoplejFormattedTextFieldKeyReleased
             genderjComboBox.setEnabled(isOnePersonEmergency());
             ageJTextField.setEnabled(isOnePersonEmergency());
    }//GEN-LAST:event_nroOfPeoplejFormattedTextFieldKeyReleased

    private void nroOfPeoplejFormattedTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nroOfPeoplejFormattedTextFieldKeyTyped
        
             genderjComboBox.setEnabled(isOnePersonEmergency());
             ageJTextField.setEnabled(isOnePersonEmergency());
       
        
    }//GEN-LAST:event_nroOfPeoplejFormattedTextFieldKeyTyped

    private void taskActionjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taskActionjButtonActionPerformed
        try {
            TTask task = getTaskClient().getTaskInfo(taskId);
            TStatus status = task.getStatus();
            String statusName = status.name();
            System.out.println("Status of the TASK = "+statusName);
            //@TODO: check the status and show or not the button!
        } catch (IllegalArgumentFault ex) {
            Logger.getLogger(EmergencyMinimalQuestionnairePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        getTaskClient().setAuthorizedEntityId("operator");
        try {
            getTaskClient().start(taskId);
        } catch (IllegalArgumentFault ex) {
            Logger.getLogger(EmergencyMinimalQuestionnairePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateFault ex) {
            Logger.getLogger(EmergencyMinimalQuestionnairePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessFault ex) {
            Logger.getLogger(EmergencyMinimalQuestionnairePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        taskActionjButton.setText("Started...");
        taskActionjButton.setEnabled(false);
       
    }//GEN-LAST:event_taskActionjButtonActionPerformed

    private boolean isOnePersonEmergency(){
        
        String nroOfPeople = nroOfPeoplejFormattedTextField.getText();
        if(nroOfPeople != null && !nroOfPeople.equals("")){
            int nroOfPeopleInt = Integer.valueOf(nroOfPeople);
            if(nroOfPeopleInt == 1){
                return true;
            }
        }
        return false;
        
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ageJTextField;
    private javax.swing.JComboBox emergencyTypeJComboBox;
    private javax.swing.JComboBox genderjComboBox;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField locationxjTextField;
    private javax.swing.JTextField locationyjTextField;
    private javax.swing.JFormattedTextField nroOfPeoplejFormattedTextField;
    private javax.swing.JLabel phoneCalljLabel;
    private javax.swing.JButton suggestProcedureJButton;
    private javax.swing.JButton taskActionjButton;
    // End of variables declaration//GEN-END:variables
}