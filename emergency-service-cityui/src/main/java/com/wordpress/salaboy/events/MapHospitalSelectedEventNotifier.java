/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.events;


import com.wordpress.salaboy.CityEntitiesUtils;
import com.wordpress.salaboy.EmergencyService;
import com.wordpress.salaboy.events.MapEventsNotifier.EventType;
import com.wordpress.salaboy.graphicable.GraphicableFactory;
import com.wordpress.salaboy.ui.Block;
import com.wordpress.salaboy.ui.BlockMap;
import com.wordpress.salaboy.ui.CityMapUI;
import com.wordpress.salaboy.model.Hospital;
import com.wordpress.salaboy.services.AmbulanceMonitorService;

/**
 *
 * @author salaboy
 */
public class MapHospitalSelectedEventNotifier implements WorldEventNotifier{

    

    public MapHospitalSelectedEventNotifier() {
       
    }

    
    
    
    @Override
    public void notify(NotifierEvent event) {
        
        Hospital selectedHospital = null;
        Long hospitalId = ((HospitalSelectedNotifierEvent)event).getHospitalId();
        Long ambulanceId = ((HospitalSelectedNotifierEvent)event).getAmbulanceId();
        Long emergencyId = ((HospitalSelectedNotifierEvent)event).getEmergencyId();
        
        int hospitasquare[] = {1, 1, 15, 1, 15, 15, 1, 15};
        
        for (Hospital hospitalnow : CityEntitiesUtils.hospitals.values()) {
            if (hospitalnow.getId() == hospitalId) {
                selectedHospital = hospitalnow;
            }
        }
        
        EmergencyService.getInstance().getMapEventsNotifier().addWorldEventNotifier(EventType.HOSPITAL_REACHED, new MapHospitalReachedEventNotifier(hospitalId));

        BlockMap.hospitals.add(new Block(Math.round(selectedHospital.getPositionX()) * 16, Math.round(selectedHospital.getPositionY()) * 16, hospitasquare, "hospital"));

        
        EmergencyService.getInstance().getMapEventsNotifier().addWorldEventNotifier(EventType.HEART_BEAT_RECEIVED, new MapPatientVitalSignReceivedEventNotifier());
        
        
        //@TODO: START the monitor service with the Ambulance ID
        AmbulanceMonitorService.getInstance().start();
        CityMapUI.getInstance().addHospital(GraphicableFactory.newHighlightedHospital(CityEntitiesUtils.getHospitalById(hospitalId)));
        CityMapUI.getInstance().removeEmergency(emergencyId);
    }

}
