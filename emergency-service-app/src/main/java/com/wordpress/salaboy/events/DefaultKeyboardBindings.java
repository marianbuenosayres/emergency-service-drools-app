/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.events;

import com.wordpress.salaboy.CityEntitiesUtils;
import com.wordpress.salaboy.events.keyboard.KeyboardPulseEventGenerator;
import com.wordpress.salaboy.model.Ambulance;
import java.util.List;

/**
 *
 * @author esteban
 */
public class DefaultKeyboardBindings {
    private static char[][] defaultKeys = {{'q','w'}, {'a','s'}, {'z','x'}, {'e','r'}, {'d','f'}, {'c','v'}, {'t','y'}, {'g','h'}, {'b','n'}, {'u','i'}, {'j','k'}, {'o','p'}};
    

    /**
     * Bind default keys to each registered Ambulance. 
     */
    public void applyDefaultConfiguration(){
        List<Ambulance> ambulances = CityEntitiesUtils.getAmbulances();
        int i = 0;
        for (Ambulance ambulance : ambulances) {
           if (i < defaultKeys.length){
               char[] keys = defaultKeys[i];
               KeyboardPulseEventGenerator.getInstance().addConfiguration(keys[0], keys[1], ambulance);
           }
           i++;
        }
    }
}
