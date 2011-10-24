/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/*
 * EmergencyMonitorPanel.java
 *
 * Created on Apr 28, 2011, 10:43:44 PM
 */
package com.wordpress.salaboy.emergencyservice.monitor;

import com.wordpress.salaboy.context.tracking.ContextTrackingProvider;
import com.wordpress.salaboy.context.tracking.ContextTrackingService;
import com.wordpress.salaboy.emergencyservice.util.AlertsIconListRenderer;
import com.wordpress.salaboy.messaging.MessageConsumerWorker;
import com.wordpress.salaboy.messaging.MessageConsumerWorkerHandler;
import com.wordpress.salaboy.messaging.MessageFactory;
import com.wordpress.salaboy.model.messages.*;
import com.wordpress.salaboy.model.messages.patient.HeartBeatMessage;
import com.wordpress.salaboy.model.messages.patient.PatientMonitorAlertMessage;
import com.wordpress.salaboy.model.serviceclient.PersistenceService;
import com.wordpress.salaboy.model.serviceclient.PersistenceServiceProvider;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import org.hornetq.api.core.HornetQException;

/**
 *
 * @author esteban
 * @author salaboy
 */
public class EmergencyMonitorPanel extends javax.swing.JPanel {

    private ImageIcon map;
    private MessageConsumerWorker gpsWorker;
    private MessageConsumerWorker heartBeatWorker;
    private MessageConsumerWorker patientMonitorAlertWorker;
    private MessageConsumerWorker vehicleHitsEmergencyWorker;
    private MessageConsumerWorker vehicleHitsHospitalWorker;
    private MessageConsumerWorker waterTankDecreaseWorker;
    private MessageConsumerWorker waterTankIncreaseWorker;
    private MessageConsumerWorker waterPumpTiltWorker;
    private MessageConsumerWorker outOfWaterWorker;
    private MessageConsumerWorker waterRefillWorker;
    private String callId;
    private List<String> alerts = new ArrayList<String>();
    private Map<String, Boolean> vehicleHitEmergency = new HashMap<String, Boolean>();
    private Map<String, Boolean> vehicleHitHospital = new HashMap<String, Boolean>();
    private final PersistenceService persistenceService;
    private final ContextTrackingService trackingService;
    private Map<String, FireTruckStatusJPanel> fireTruckPanels = new HashMap<String, FireTruckStatusJPanel>();
    private String emergencyId;
    
    

    /** Creates new form EmergencyMonitorPanel */
    public EmergencyMonitorPanel(String callId) throws IOException {

        this.callId = callId;
        
        persistenceService = PersistenceServiceProvider.getPersistenceService();

        trackingService = ContextTrackingProvider.getTrackingService();
        
        emergencyId = trackingService.getEmergencyAttachedToCall(callId);
        initComponents();

        loadMapImage();

        startQueueListeners();

        startPulseEmulator();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstAlerts = new javax.swing.JList();
        btnClear1 = new javax.swing.JButton();
        jEmergencyTabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        lblMap = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        auditLogjTextArea = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Alerts"));

        lstAlerts.setPreferredSize(new java.awt.Dimension(200, 0));
        jScrollPane3.setViewportView(lstAlerts);
        lstAlerts.setCellRenderer(new AlertsIconListRenderer());

        btnClear1.setText("Clear");
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClear1)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear1))
        );

        jPanel1.setName("GPS"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 200));

        lblMap.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblMap.setPreferredSize(new java.awt.Dimension(305, 208));

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(lblMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(161, 161, 161)
                        .addComponent(btnClear)))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear)
                .addContainerGap())
        );

        jEmergencyTabbedPane.addTab("GPS", jPanel1);

        auditLogjTextArea.setColumns(20);
        auditLogjTextArea.setRows(5);
        jScrollPane1.setViewportView(auditLogjTextArea);
        auditLogjTextArea.setText(persistenceService.loadReport(this.callId).getReportString());

        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        jEmergencyTabbedPane.addTab("Live Report", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jEmergencyTabbedPane)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jEmergencyTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        this.alerts.clear();
        DefaultListModel emptyModel = new DefaultListModel();
        this.lstAlerts.setModel(emptyModel);
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.out.println("Get REPORT FOR CALL ID="+this.callId);
        auditLogjTextArea.setText(persistenceService.loadReport(this.callId).getReportString());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed

        this.loadMapImage();     }//GEN-LAST:event_btnClearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea auditLogjTextArea;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClear1;
    private javax.swing.JButton jButton1;
    private javax.swing.JTabbedPane jEmergencyTabbedPane;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblMap;
    private javax.swing.JList lstAlerts;
    // End of variables declaration//GEN-END:variables

    private void startQueueListeners() {
        gpsWorker = new MessageConsumerWorker("vehicleGPS" + callId, new MessageConsumerWorkerHandler<VehicleHitsCornerMessage>() {

            @Override
            public void handleMessage(VehicleHitsCornerMessage message) {
                System.out.println("CallId = "+callId +" -> Vehicle Id= "+message.getVehicleId());
                if (message.getEmergencyId().equals(callId)) {
                    paintVehiclePosition(message.getVehicleId(), message.getCornerX(), message.getCornerY());
                }
            }
        });

        heartBeatWorker = new MessageConsumerWorker("vehicleHeartBeat" + callId, new MessageConsumerWorkerHandler<HeartBeatMessage>() {

            @Override
            public void handleMessage(HeartBeatMessage message) {
                if (message.getEmergencyId().equals(callId)) {
                    processHeartBeat(message.getVehicleId(), message.getHeartBeatValue(), message.getTime());
                }
            }
        });

        patientMonitorAlertWorker = new MessageConsumerWorker("patientMonitorAlerts" + callId, new MessageConsumerWorkerHandler<PatientMonitorAlertMessage>() {

            @Override
            public void handleMessage(PatientMonitorAlertMessage message) {
                if (message.getEmergencyId().equals(callId)) {
                    processPatientAlert(message.getVehicleId(), message.getTime(), message.getMessage());
                }
            }
        });

        //Vehicle Hits an Emergency Selected Worker
        vehicleHitsEmergencyWorker = new MessageConsumerWorker("vehicleHitsEmergencyMonitorPanel" + callId, new MessageConsumerWorkerHandler<VehicleHitsEmergencyMessage>() {

            @Override
            public void handleMessage(VehicleHitsEmergencyMessage vehicleHitsEmergencyMessage) {
                vehicleHitEmergency.put(vehicleHitsEmergencyMessage.getVehicleId(), Boolean.TRUE);
                //ProceduresMGMTService.getInstance().patientPickUpNotification(new PatientPickUpEvent(vehicleHitsEmergencyMessage.getCallId(), vehicleHitsEmergencyMessage.getVehicleId(), vehicleHitsEmergencyMessage.getTime()));
            }
        });


        //Vehicle Hits an Hospital Selected Worker
        vehicleHitsHospitalWorker = new MessageConsumerWorker("vehicleHitsHospitalMonitorPanel" + callId, new MessageConsumerWorkerHandler<VehicleHitsHospitalMessage>() {

            @Override
            public void handleMessage(VehicleHitsHospitalMessage vehicleHitsHospitalMessage) {
                vehicleHitHospital.put(vehicleHitsHospitalMessage.getVehicleId(), Boolean.TRUE);
                cleanupPanel();
            }
        });


        waterTankDecreaseWorker = new MessageConsumerWorker("FTwaterDecreaseMonitor"+callId, new MessageConsumerWorkerHandler<FireTruckDecreaseWaterLevelMessage>() {

            @Override
            public void handleMessage(FireTruckDecreaseWaterLevelMessage message) {
                if (message.getEmergencyId().equals(emergencyId)) {
                    getFireTruckPanel(message.getVehicleId()).decreaseWaterLevel();
                }
            }
        });
        outOfWaterWorker = new MessageConsumerWorker("FTOutOfWaterMonitor"+callId, new MessageConsumerWorkerHandler<FireTruckOutOfWaterMessage>() {

            @Override
            public void handleMessage(FireTruckOutOfWaterMessage message) {
                if (message.getEmergencyId().equals(emergencyId)) {
                    getFireTruckPanel(message.getVehicleId()).fireTruckOutOfWater();
                }
            }
        });

        waterPumpTiltWorker = new MessageConsumerWorker("FTTiltMonitor"+callId, new MessageConsumerWorkerHandler<FireTruckWaterPumpTiltMessage>() {

            @Override
            public void handleMessage(FireTruckWaterPumpTiltMessage message) {
                if (message.getEmergencyId().equals(emergencyId)) {
                    getFireTruckPanel(message.getVehicleId()).waterPumpOverHeat();
                }
            }
        });
        
         waterRefillWorker = new MessageConsumerWorker("FTWaterRefillMonitor"+callId, new MessageConsumerWorkerHandler<FireTruckWaterRefilledMessage>() {

            @Override
            public void handleMessage(FireTruckWaterRefilledMessage message) {
                if (message.getEmergencyId().equals(emergencyId)) {
                    getFireTruckPanel(message.getVehicleId()).waterRefilled();
                }
            }
        });
         
        waterRefillWorker.start();
        waterPumpTiltWorker.start();
        outOfWaterWorker.start();
        waterTankDecreaseWorker.start();

        vehicleHitsEmergencyWorker.start();
        vehicleHitsHospitalWorker.start();
        patientMonitorAlertWorker.start();
        gpsWorker.start();
        heartBeatWorker.start();
    }
    private Map<String, Point> lastVehiclePosition = new HashMap<String, Point>();
    private Map<String, Color> vehicleColors = new HashMap<String, Color>();
    private Color[] colors = {Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN, Color.ORANGE, Color.PINK};

    private void paintVehiclePosition(String vehicleId, int x, int y) {
        x = x / 2;
        y = y / 2;

        if (!lastVehiclePosition.containsKey(vehicleId)) {
            lastVehiclePosition.put(vehicleId, new Point(x, y));
            vehicleColors.put(vehicleId, colors[vehicleColors.size()]);
            return;
        }


        Point lastPoint = lastVehiclePosition.get(vehicleId);
        Graphics graphics = lblMap.getGraphics();
        graphics.setColor(vehicleColors.get(vehicleId));
        graphics.drawLine(lastPoint.x, lastPoint.y, x, y);


        lastVehiclePosition.put(vehicleId, new Point(x, y));
    }

    public void cleanupPanel() {

        stopPulseEmulator = true;
        if (waterRefillWorker != null) {
            waterRefillWorker.stopWorker();
        }
        if (waterPumpTiltWorker != null) {
            waterPumpTiltWorker.stopWorker();
        }
        if (outOfWaterWorker != null) {
            outOfWaterWorker.stopWorker();
        }
        if (waterTankDecreaseWorker != null) {
            waterTankDecreaseWorker.stopWorker();
        }
        if (vehicleHitsEmergencyWorker != null) {
            vehicleHitsEmergencyWorker.stopWorker();
        }
        if (vehicleHitsHospitalWorker != null) {
            vehicleHitsHospitalWorker.stopWorker();
        }
        if (patientMonitorAlertWorker != null) {
            patientMonitorAlertWorker.stopWorker();
        }
        if (heartBeatWorker != null) {
            heartBeatWorker.stopWorker();
        }
        if (gpsWorker != null) {
            gpsWorker.stopWorker();
        }
    }

    private void loadMapImage() {
        map = new ImageIcon(this.getClass().getClassLoader().getResource("data/png/CityMap.png"));
        this.lblMap.setIcon(map);
    }
    private Map<String, HeartBeatWidget> heartBeatWidgets = new ConcurrentHashMap<String, HeartBeatWidget>();

    private void processHeartBeat(String vehicleId, double heartBeatValue, Date time) {
        //only if the vehicle already hit the emergency, but it doesnt
        //hit the hospital yet;
        Boolean hitEmergency = vehicleHitEmergency.get(vehicleId);
        Boolean hitHospital = vehicleHitHospital.get(vehicleId);

        if (hitEmergency == null) {
            hitEmergency = false;
        }

        if (hitHospital == null) {
            hitHospital = false;
        }

        if (!hitEmergency) {
            return;
        }

        if (hitHospital) {
            return;
        }


        if (!heartBeatWidgets.containsKey(vehicleId)) {
            HeartBeatWidget widget = new HeartBeatWidget();
            jEmergencyTabbedPane.add("Ambulance " + vehicleId, widget.getChartPanel());
            jEmergencyTabbedPane.setSelectedComponent(widget.getChartPanel());
            heartBeatWidgets.put(vehicleId, widget);
        }

        heartBeatWidgets.get(vehicleId).updateMonitorGraph(heartBeatValue);
    }

    private void processPatientAlert(String vehicleId, Date time, String message) {
        alerts.add(0, vehicleId + " - " + message);

        DefaultListModel model = new DefaultListModel();
        for (String alert : alerts) {
            model.addElement(alert);
        }
        lstAlerts.setModel(model);
        this.validate();
    }
    private boolean stopPulseEmulator = false;

    private void startPulseEmulator() {
        new Thread() {

            @Override
            public void run() {
                while (!stopPulseEmulator) {
                    try {
                        for (String vehicleId : heartBeatWidgets.keySet()) {
                            try {
                                MessageFactory.sendMessage(new HeartBeatMessage(callId, vehicleId, 0, new Date()));


                            } catch (HornetQException ex) {
                                Logger.getLogger(EmergencyMonitorPanel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        Thread.sleep(1000);


                    } catch (InterruptedException ex) {
                        Logger.getLogger(EmergencyMonitorPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
    
    private FireTruckStatusJPanel getFireTruckPanel(String vehicleId){
        if(fireTruckPanels.get(vehicleId) == null){
            FireTruckStatusJPanel fireTruckStatusJPanel = new FireTruckStatusJPanel(vehicleId);
            fireTruckPanels.put(vehicleId,fireTruckStatusJPanel);
            jEmergencyTabbedPane.add(fireTruckStatusJPanel);
            jEmergencyTabbedPane.setSelectedComponent(fireTruckStatusJPanel);
        }
        return fireTruckPanels.get(vehicleId);
        
    }
}
