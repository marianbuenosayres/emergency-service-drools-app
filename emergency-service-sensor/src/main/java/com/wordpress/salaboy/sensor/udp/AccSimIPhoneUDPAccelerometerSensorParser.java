/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.sensor.udp;

import com.wordpress.salaboy.sensor.SensorDataParser;


/**
 *
 * @author esteban
 */
public class AccSimIPhoneUDPAccelerometerSensorParser implements SensorDataParser  {
    
    @Override
    public double parseData(String data){
        
        if (data.contains(",")){
            //split the data using ',' as a separator
            String[] split = data.split(",");

            //get the last value of the string
            return Double.parseDouble(split[split.length-1]);
        }
        
        return Double.parseDouble(data);
    }

    @Override
    public boolean isValidData(String data) {
        double value= this.parseData(data);
        return (value < -1.5 || value > 1.5); 
    }
    
}
