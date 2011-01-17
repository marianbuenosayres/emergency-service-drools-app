/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.events;

import javax.swing.JComponent;

/**
 *
 * @author salaboy
 */
public class TaskListUIAmbulancePositionUpdatedEventNotifier implements WorldEventNotifier {

    
    private JComponent frame;
    private String lastPositionText = "";
    public TaskListUIAmbulancePositionUpdatedEventNotifier(JComponent frame) {
        this.frame = frame;
    }
    
    @Override
    public void notify(NotifierEvent event) {
        Long ambulanceId = ((PositionUpdatedNotifierEvent)event).getAmbulanceId();
        //@TODO: recieve a message(using HornetQ) sent by the MapUI to get the Ambulance Position
//        GraphicableAmbulance ambulance = UserTaskListUI.getInstance().getGame().getGraphicableAmbulanceById(ambulanceId);
//        int newX = Math.round(ambulance.getPolygon().getX() / 16);
//        int newY = Math.round(ambulance.getPolygon().getY() / 16); 
//        String text = CityEntitiesUtils.translatePosition( newX, newY );
//        if (!lastPositionText.equals(text) && !text.equals("N/A")){
//            ((CurrentEmergenciesPanel)frame).getEmergencyFrameById(ambulance.getAmbulance().getId()).getTxtPosition().insert(text+"\n", 0);
//            lastPositionText = text;
//        }
        
    }

}
