/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SuggestedProceduresPanel.java
 *
 * Created on Apr 21, 2011, 7:47:07 PM
 */
package com.wordpress.salaboy.emergencyservice.taskforms;

import com.wordpress.salaboy.api.HumanTaskService;
import com.wordpress.salaboy.emergencyservice.tasklists.ControlSuggestedProceduresTaskListPanel;
import com.wordpress.salaboy.emergencyservice.main.UserTaskListUI;
import com.wordpress.salaboy.model.Call;
import com.wordpress.salaboy.model.Emergency;
import com.wordpress.salaboy.model.Patient;
import com.wordpress.salaboy.model.SelectedProcedures;
import com.wordpress.salaboy.model.SuggestedProcedures;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
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
 * @author salaboy
 */
public class SuggestedProceduresTaskFormPanel extends javax.swing.JPanel {

    private ControlSuggestedProceduresTaskListPanel parent;
    private HumanTaskService taskClient;
    private List<String> suggestedProcedures;
    private String emergencyId;
    private String taskId;

    /** Creates new form SuggestedProceduresPanel */
    public SuggestedProceduresTaskFormPanel(ControlSuggestedProceduresTaskListPanel parent, HumanTaskService taskClient, String id) {
        this.parent = parent;
        this.taskClient = taskClient;
        this.taskId = id;

        initComponents();
        configurePanel();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        emergencyTimejTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        emergencyLocationjTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        emergencyTypejTextField = new javax.swing.JTextField();
        nroOfPeoplejTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        patientAgejTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        patientGenderjTextField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        suggestedProceduresjList = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        taskActionjButton = new javax.swing.JButton();
        selectProcedurejButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(374, 566));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Emergency Provide Info", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel6.setText("Time:");

        emergencyTimejTextField.setEditable(false);

        jLabel2.setText("Location:");

        emergencyLocationjTextField.setEditable(false);

        jLabel3.setText("Type:");

        jLabel4.setText("People Involved (Aprox):");

        emergencyTypejTextField.setEditable(false);

        nroOfPeoplejTextField.setEditable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Single Person Info"));

        jLabel8.setText("Age:");

        patientAgejTextField.setEditable(false);
        patientAgejTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientAgejTextFieldActionPerformed(evt);
            }
        });

        jLabel9.setText("Gender:");

        patientGenderjTextField.setEditable(false);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel8)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(patientAgejTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel9)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(patientGenderjTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(patientAgejTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel8)
                    .add(jLabel9)
                    .add(patientGenderjTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .add(jLabel4)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(nroOfPeoplejTextField))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jLabel2)
                                        .add(jLabel3)))
                                .add(jPanel2Layout.createSequentialGroup()
                                    .add(20, 20, 20)
                                    .add(jLabel6)))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(emergencyTypejTextField)
                                .add(emergencyTimejTextField)
                                .add(emergencyLocationjTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))))
                    .add(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 290, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(emergencyTimejTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(emergencyLocationjTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(emergencyTypejTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(nroOfPeoplejTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Suggested Procedures", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        suggestedProceduresjList.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(suggestedProceduresjList);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Task Actions", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        taskActionjButton.setText("Start");
        taskActionjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taskActionjButtonActionPerformed(evt);
            }
        });

        selectProcedurejButton.setText("Select Procedure(s)");
        selectProcedurejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectProcedurejButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(taskActionjButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(selectProcedurejButton)
                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(taskActionjButton)
                .add(selectProcedurejButton))
        );

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 374, Short.MAX_VALUE)
            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, 0, 334, Short.MAX_VALUE)))
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 566, Short.MAX_VALUE)
            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 291, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectProcedurejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectProcedurejButtonActionPerformed


        int[] selected = suggestedProceduresjList.getSelectedIndices();
        SelectedProcedures selectedProcedures = new SelectedProcedures(emergencyId);
        for (int i = 0; i < selected.length; i++) {
            selectedProcedures.addSelectedProcedureName((String) suggestedProceduresjList.getModel().getElementAt(selected[i]));
        }
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("selectedProcedures", selectedProcedures);



        getTaskClient().setAuthorizedEntityId("control");
        try {
            getTaskClient().complete(this.taskId, info);
        } catch (IllegalArgumentFault ex) {
            Logger.getLogger(SuggestedProceduresTaskFormPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateFault ex) {
            Logger.getLogger(SuggestedProceduresTaskFormPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessFault ex) {
            Logger.getLogger(SuggestedProceduresTaskFormPanel.class.getName()).log(Level.SEVERE, null, ex);
        }


        this.parent.hideDialog();


    }//GEN-LAST:event_selectProcedurejButtonActionPerformed

    private void taskActionjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taskActionjButtonActionPerformed
        try {
            TTask task = getTaskClient().getTaskInfo(taskId);
            TStatus status = task.getStatus();
            String statusName = status.name();
            System.out.println("Status of the TASK = " + statusName);
            //@TODO: check the status and show or not the button!
        } catch (IllegalArgumentFault ex) {
            Logger.getLogger(EmergencyMinimalQuestionnaireTaskFormPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        getTaskClient().setAuthorizedEntityId("control");
        try {
            getTaskClient().start(taskId);
        } catch (IllegalArgumentFault ex) {
            Logger.getLogger(EmergencyMinimalQuestionnaireTaskFormPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateFault ex) {
            Logger.getLogger(EmergencyMinimalQuestionnaireTaskFormPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessFault ex) {
            Logger.getLogger(EmergencyMinimalQuestionnaireTaskFormPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        taskActionjButton.setText("Started...");
        taskActionjButton.setEnabled(false);
    }//GEN-LAST:event_taskActionjButtonActionPerformed

    private void patientAgejTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientAgejTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_patientAgejTextFieldActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField emergencyLocationjTextField;
    private javax.swing.JTextField emergencyTimejTextField;
    private javax.swing.JTextField emergencyTypejTextField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nroOfPeoplejTextField;
    private javax.swing.JTextField patientAgejTextField;
    private javax.swing.JTextField patientGenderjTextField;
    private javax.swing.JButton selectProcedurejButton;
    private javax.swing.JList suggestedProceduresjList;
    private javax.swing.JButton taskActionjButton;
    // End of variables declaration//GEN-END:variables

    private void configurePanel() {
        Map taskinfo = null;

        try {
            ObjectInputStream ois = null;

            List<TAttachmentInfo> attachmentsInfo = getTaskClient().getAttachmentInfos(this.taskId);
            TAttachmentInfo firstAttachmentInfo = attachmentsInfo.get(0);
            TAttachment attachment = getTaskClient().getAttachments(this.taskId, firstAttachmentInfo.getName()).get(0);

            taskinfo = (Map)attachment.getValue();
        } catch (Exception ex) {
            Logger.getLogger(UserTaskListUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("TaskInfo = " + taskinfo);
        Emergency emergency = (Emergency) taskinfo.get("emergency");
        emergencyId = emergency.getId();
        String emergencyType = emergency.getType().name();
        String emergencyLocationX = emergency.getLocation().getLocationX().toString();
        String emergencyLocationY = emergency.getLocation().getLocationY().toString();
        String emergencyDate = emergency.getCall().getDate().toString();
        String emergencyNroOfPeople = String.valueOf(emergency.getNroOfPeople());
        emergencyTimejTextField.setText(emergencyDate);
        emergencyLocationjTextField.setText("X: " + emergencyLocationX + " - Y:" + emergencyLocationY);
        emergencyTypejTextField.setText(emergencyType);
        nroOfPeoplejTextField.setText(emergencyNroOfPeople);

        String patientAge = "";
        String patientGender = "";

        if (emergencyNroOfPeople.equals("1")) {
        	Patient patient = (Patient) taskinfo.get("patient");
            patientAge = String.valueOf(patient.getAge());
            patientGender = patient.getGender();
            patientAgejTextField.setText(patientAge);
            patientGenderjTextField.setText(patientGender);
        }

        //TODO: put this in a global area:
        String[] procedures = new String[]{
            "DefaultFireAttackProcedure",
            "DefaultHeartAttackProcedure",
            "DefaultPoliceAttackProcedure"
        };

        for (String procedure : procedures) {
            ((DefaultListModel) suggestedProceduresjList.getModel()).addElement(procedure);
        }


        String suggestedProceduresString = ((SuggestedProcedures)taskinfo.get("suggestedProcedures")).getSuggestedProceduresString();
        if (suggestedProceduresString != null && !suggestedProceduresString.equals("") && !suggestedProceduresString.trim().startsWith("#{")) {


            suggestedProcedures = getSuggestedProceduresNames(suggestedProceduresString);
            int[] selectedIndices = new int[suggestedProcedures.size()];
            int j = 0;
            for (String suggestedProcedure : suggestedProcedures) {
                for (int i = 0; i < procedures.length; i++) {
                    String procedure = procedures[i];
                    if (procedure.equals(suggestedProcedure)) {
                        selectedIndices[j++] = i;
                    }
                }
            }

            suggestedProceduresjList.setSelectedIndices(selectedIndices);
        }



    }

    public List<String> getSuggestedProceduresNames(String suggestedProceduresString) {
        suggestedProceduresString = suggestedProceduresString.trim();
        if (suggestedProceduresString.startsWith("[")) {
            suggestedProceduresString = suggestedProceduresString.substring(1);
        }
        if (suggestedProceduresString.endsWith("]")) {
            suggestedProceduresString = suggestedProceduresString.substring(0, suggestedProceduresString.length() - 1);
        }
        suggestedProceduresString = suggestedProceduresString.trim();
        String[] namesArray = suggestedProceduresString.split(":");
        return Arrays.asList(namesArray);
    }

    private HumanTaskService getTaskClient() {
        return this.taskClient;
    }
}
