package com.wordpress.salaboy.model;

import java.util.Date;

/**
 *
 * @author salaboy
 */
public class PoliceCar implements Vehicle {

    private String id;
    private String name;
    private Date departureTime;
    private Date emergencyReachedTime;
    private float positionX;
    private float positionY;

    public PoliceCar() {
    }

    public PoliceCar(String name, Date departureTime) {

        this.name = name;
        this.departureTime = departureTime;
    }

    public PoliceCar(String name) {

        this.name = name;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String description) {
        this.name = description;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public Date getEmergencyReachedTime() {
        return emergencyReachedTime;
    }

    public void setEmergencyReachedTime(Date emergencyReachedTime) {
        this.emergencyReachedTime = emergencyReachedTime;
    }

    @Override
    public String toString() {
        return "PoliceCar{" + "id=" + id + ", name=" + name + '}';
    }
}
