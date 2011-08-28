/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.emergencyservice.worldui.slick;

import com.wordpress.salaboy.emergencyservice.worldui.slick.graphicable.Graphicable;
import com.wordpress.salaboy.emergencyservice.worldui.slick.graphicable.GraphicableAmbulance;
import com.wordpress.salaboy.emergencyservice.worldui.slick.graphicable.GraphicableEmergency;
import com.wordpress.salaboy.emergencyservice.worldui.slick.graphicable.GraphicableFactory;
import com.wordpress.salaboy.emergencyservice.worldui.slick.graphicable.GraphicableHighlightedFirefighterDepartment;
import com.wordpress.salaboy.emergencyservice.worldui.slick.graphicable.GraphicableHighlightedHospital;
import com.wordpress.salaboy.emergencyservice.worldui.slick.graphicable.GraphicableVehicle;
import com.wordpress.salaboy.messaging.MessageFactory;
import com.wordpress.salaboy.model.Ambulance;
import com.wordpress.salaboy.model.FireTruck;
import com.wordpress.salaboy.model.FirefightersDepartment;
import com.wordpress.salaboy.model.Hospital;
import com.wordpress.salaboy.model.PoliceCar;
import com.wordpress.salaboy.model.Vehicle;
import com.wordpress.salaboy.model.messages.FirefigthersDepartmentSelectedMessage;
import com.wordpress.salaboy.model.messages.HospitalSelectedMessage;
import com.wordpress.salaboy.model.messages.VehicleDispatchedMessage;
import com.wordpress.salaboy.model.messages.patient.HeartBeatMessage;
import com.wordpress.salaboy.model.messages.VehicleHitsCornerMessage;
import com.wordpress.salaboy.model.messages.VehicleHitsEmergencyMessage;
import com.wordpress.salaboy.model.messages.VehicleHitsHospitalMessage;
import com.wordpress.salaboy.model.serviceclient.DistributedPeristenceServerService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hornetq.api.core.HornetQException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;

/**
 *
 * @author esteban
 */
public class ParticularEmergencyRenderer implements EmergencyRenderer {

    private final WorldUI ui;
    private GraphicableEmergency emergency;
    private GraphicableVehicle activeGraphicableVehicle;
    private List<GraphicableVehicle> graphicableVehicles;
    private Vehicle activeVehicle;
    private Map<Graphicable, Vehicle> vehicles;
    private GraphicableHighlightedHospital selectedHospital;
    private GraphicableHighlightedFirefighterDepartment selectedFirefighterDepartment;
    private boolean turbo;
    private boolean hideEmergency;

    public ParticularEmergencyRenderer(WorldUI ui, GraphicableEmergency emergency) {
        this.emergency = emergency;
        this.ui = ui;
        this.vehicles = new HashMap<Graphicable, Vehicle>();
        this.graphicableVehicles = new ArrayList<GraphicableVehicle>();
    }

    /**
     * 
     * @param ui 
     */
    @Override
    public void renderPolygon(GameContainer gc, Graphics g) {
        
        if (!hideEmergency){
            g.draw(emergency.getPolygon());
        }
        
        for (Graphicable vehicle : graphicableVehicles) {
            g.draw(vehicle.getPolygon());
        }
        if (selectedHospital != null) {
            g.draw(selectedHospital.getPolygon());
        }
        if (selectedFirefighterDepartment != null) {
            g.draw(selectedFirefighterDepartment.getPolygon());
        }
    }

    public void renderAnimation(GameContainer gc, Graphics g) {
        if (!hideEmergency){        
            g.drawAnimation(emergency.getAnimation(), emergency.getPolygon().getX(), emergency.getPolygon().getY());
        }
        
        for (Graphicable vehicle : graphicableVehicles) {
            //the active vehicle is rendered at the end
            if (activeGraphicableVehicle == null || activeGraphicableVehicle != vehicle) {
                g.drawAnimation(vehicle.getAnimation(), vehicle.getPolygon().getX(), vehicle.getPolygon().getY());
            }
        }

        if (activeGraphicableVehicle != null) {
            g.drawAnimation(activeGraphicableVehicle.getAnimation(), activeGraphicableVehicle.getPolygon().getX(), activeGraphicableVehicle.getPolygon().getY());
        }
        if (selectedHospital != null) {
            g.drawAnimation(selectedHospital.getAnimation(), selectedHospital.getPolygon().getX() - 32, selectedHospital.getPolygon().getY() - 80);
        }
        if (selectedFirefighterDepartment != null) {
            g.drawAnimation(selectedFirefighterDepartment.getAnimation(), selectedFirefighterDepartment.getPolygon().getX() - 32, selectedFirefighterDepartment.getPolygon().getY() - 80);
        }
    }

    public void addVehicle(Vehicle vehicle) {

        this.activeGraphicableVehicle = GraphicableFactory.newVehicle(vehicle);

        vehicle.setPositionX(this.activeGraphicableVehicle.getPolygon().getX());
        vehicle.setPositionY(this.activeGraphicableVehicle.getPolygon().getY());

        this.vehicles.put(activeGraphicableVehicle, vehicle);
        this.graphicableVehicles.add(activeGraphicableVehicle);

        this.activeVehicle = vehicle;
    }

    public void selectHospital(Hospital hospital) {
        selectedHospital = GraphicableFactory.newHighlightedHospital(hospital);
    }
    
    public void selectFirefighterDepartment(FirefightersDepartment firefigthersDepartment) {
        selectedFirefighterDepartment = GraphicableFactory.newHighlightedFirefighterDepartment(firefigthersDepartment);
    }

    public void onKeyPressed(int code, char key) {
        if (Input.KEY_ESCAPE == code) {
            this.ui.goToGlobalMap();
        } else if (Input.KEY_Q == code) {
            this.sendHeartBeat(new Random().nextInt(50));
        } else if (Input.KEY_W == code) {
            this.sendHeartBeat(-1 * new Random().nextInt(50));
        } else if (Input.KEY_F1 == code) {
            addMockAmbulance();
        } else if (Input.KEY_F2 == code) {
            addMockFireTruck();
        } else if (Input.KEY_F3 == code) {
            addMockPoliceCar();
        } else if (Input.KEY_F4 == code) {
            selectMockHospital(0L);
        } else if (Input.KEY_F5 == code) {
            selectMockHospital(1L);
        } else if (Input.KEY_F6 == code) {
            selectMockHospital(2L);
        } else if (Input.KEY_F7 == code) {
            selectMockFireDepartment(0L);
        } else if (Input.KEY_LSHIFT == code) {
            this.turbo = true;
        }

    }

    public void onKeyReleased(int code, char key) {
        if (Input.KEY_LSHIFT == code) {
            this.turbo = false;
        }
    }

    public void onClick(int button, int x, int y, int count) {
        for (GraphicableVehicle graphicable : graphicableVehicles) {
            if (graphicable.getPolygon().contains(x, y)) {
                this.activeGraphicableVehicle = graphicable;
                this.activeVehicle = vehicles.get(this.activeGraphicableVehicle);
                return;
            }
        }
    }

    public void update(GameContainer gc, int delta) {
        if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
            this.moveVehicle(Input.KEY_LEFT);
        } else if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
            this.moveVehicle(Input.KEY_RIGHT);
        } else if (gc.getInput().isKeyDown(Input.KEY_UP)) {
            this.moveVehicle(Input.KEY_UP);
        } else if (gc.getInput().isKeyDown(Input.KEY_DOWN)) {
            this.moveVehicle(Input.KEY_DOWN);
        }

        //check for collisions
        checkCornerCollision();
        checkEmergencyCollision();
        checkHospitalCollision();
    }

    private void moveVehicle(int direction) {
        if (this.activeGraphicableVehicle == null) {
            return;
        }

        int current = this.activeGraphicableVehicle.getAnimation().getFrame();
        int delta = 0;
        switch (direction) {
            case Input.KEY_LEFT:
                if (current < 7) {
                    current += 1;
                } else {
                    current = 4;
                }
                delta = -1;
                break;
            case Input.KEY_RIGHT:
                if (current < 3) {
                    current += 1;
                } else {
                    current = 0;
                }
                delta = +1;
                break;
            case Input.KEY_UP:
                if (current < 15) {
                    current += 1;
                } else {
                    current = 12;
                }
                delta = -1;
                break;
            case Input.KEY_DOWN:
                if (current < 11) {
                    current += 1;
                } else {
                    current = 8;
                }
                delta = +1;
                break;
        }

        this.activeGraphicableVehicle.getAnimation().setCurrentFrame(current);
        Vehicle currentVehicle = this.vehicles.get(this.activeGraphicableVehicle);

        if (turbo) {
            delta *= 5;
        }

        int playerX = (int) currentVehicle.getPositionX();
        int playerY = (int) currentVehicle.getPositionY();

        if (direction == Input.KEY_LEFT || direction == Input.KEY_RIGHT) {
            playerX += delta;
        } else if (direction == Input.KEY_UP || direction == Input.KEY_DOWN) {
            playerY += delta;
        }

        this.activeGraphicableVehicle.getPolygon().setX(playerX);
        this.activeGraphicableVehicle.getPolygon().setY(playerY);
        currentVehicle.setPositionX(playerX);
        currentVehicle.setPositionY(playerY);

        if (checkEntityCollision()) {
            if (direction == Input.KEY_LEFT || direction == Input.KEY_RIGHT) {
                playerX -= delta;
            } else if (direction == Input.KEY_UP || direction == Input.KEY_DOWN) {
                playerY -= delta;
            }
            this.activeGraphicableVehicle.getPolygon().setX(playerX);
            this.activeGraphicableVehicle.getPolygon().setY(playerY);
            currentVehicle.setPositionX(playerX);
            currentVehicle.setPositionY(playerY);
        }
    }

    private synchronized boolean checkEntityCollision() {
        for (int i = 0; i < BlockMap.entities.size(); i++) {
            Block entity1 = (Block) BlockMap.entities.get(i);
            if (this.activeGraphicableVehicle.getPolygon().intersects(entity1.poly)) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean checkEmergencyCollision() {
        //if no vehicle, no collision
        if (this.activeGraphicableVehicle == null) {
            return false;
        }
        
        boolean collides = false;

        collides = this.activeGraphicableVehicle.getPolygon().intersects(emergency.getPolygon());

        if (collides && !this.activeGraphicableVehicle.isIsCollidingWithAnEmergency() && !this.activeGraphicableVehicle.isAlreadyHitAnEmergency()) {
            try {
                this.activeGraphicableVehicle.setAlreadyHitAnEmergency(true);
                System.out.println("EMERGENCY REACHED!");
                MessageFactory.sendMessage(new VehicleHitsEmergencyMessage(this.activeVehicle.getId(), this.emergency.getCallId(), new Date()));
                //hide the emergency graphicable when it is hit
                this.setHideEmergency(true);
            } catch (HornetQException ex) {
                Logger.getLogger(ParticularEmergencyRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.activeGraphicableVehicle.setIsCollidingWithAnEmergency(collides);

        return collides;

    }

    public synchronized boolean checkHospitalCollision() {


        if (this.activeGraphicableVehicle == null) {
            return false;
        }

        if (this.selectedHospital == null) {
            return false;
        }
        Polygon collidesWith = null;
        if (this.activeGraphicableVehicle.getPolygon().intersects(selectedHospital.getPolygon())) {
            collidesWith = selectedHospital.getPolygon();
        }
        boolean collides = collidesWith != null;
        if (collides && !this.activeGraphicableVehicle.isIsCollidingWithAHospital()) {
            System.out.println("Hospital REACHED!");
            try {
                //notify the event
                MessageFactory.sendMessage(new VehicleHitsHospitalMessage(this.activeVehicle.getId(), selectedHospital.getHospital(), this.emergency.getCallId(), new Date()));
                //hide the hospital
                this.selectedHospital = null;
            } catch (HornetQException ex) {
                Logger.getLogger(ParticularEmergencyRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.activeGraphicableVehicle.setIsCollidingWithAHospital(collides);

        return collides;


    }

    public boolean checkCornerCollision() {
        //if no vehicle, no collision
        if (this.activeGraphicableVehicle == null) {
            return false;
        }

        Block collidesWith = null;
        for (int i = 0; i < BlockMap.corners.size(); i++) {
            Block entity1 = (Block) BlockMap.corners.get(i);
            if (this.activeGraphicableVehicle.getPolygon().intersects(entity1.poly)) {
                collidesWith = entity1;
                break;
            }
        }

        boolean collides = collidesWith != null;

        if (collides && !this.activeGraphicableVehicle.isIsCollidingWithACorner()) {
            try {
                System.out.println("CORNER REACHED!");
                MessageFactory.sendMessage(new VehicleHitsCornerMessage(this.emergency.getCallId(), this.activeVehicle.getId(), (int) collidesWith.poly.getX(), (int) collidesWith.poly.getY()));
            } catch (HornetQException ex) {
                Logger.getLogger(ParticularEmergencyRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.activeGraphicableVehicle.setIsCollidingWithACorner(collides);


        return collides;
    }

    private void sendHeartBeat(int pulse) {
        //only if the active vehicle is an ambulance
        if (this.activeGraphicableVehicle == null || !(this.activeGraphicableVehicle instanceof GraphicableAmbulance)) {
            return;
        }

        pulse += 235;
        try {
            this.activeGraphicableVehicle.setAlreadyHitAnEmergency(true);
            MessageFactory.sendMessage(new HeartBeatMessage(this.emergency.getCallId(), this.activeVehicle.getId(), pulse, new Date()));
        } catch (HornetQException ex) {
            Logger.getLogger(ParticularEmergencyRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void removeEmergency(){
        if (this.emergency == null){
            return;
        }
        
        ui.removeEmergency(emergency.getCallId());
        
        this.emergency = null;
        
    }
    
    private void setHideEmergency(boolean hide){
        if (this.emergency == null){
            return;
        }
        
        this.hideEmergency = hide;
        
    }
    
    private void addMockAmbulance() {
        this.addMockVehicle(new Ambulance("Mock Ambulance"));
    }

    private void addMockFireTruck() {
        this.addMockVehicle(new FireTruck("Mock Fire Truck"));
    }

    private void addMockPoliceCar() {
        this.addMockVehicle(new PoliceCar("Mock Police Car"));
    }
    
    private void addMockVehicle(Vehicle vehicle){
        try {
            
            //Dirty way to put a free id to the vehicle
            Long vehicleId = null;
            Random random = new Random(System.currentTimeMillis());
            do{
                vehicleId = random.nextLong(); 
            } while(DistributedPeristenceServerService.getInstance().loadVehicle(vehicleId) != null);
            
            vehicle.setId(vehicleId);
            
            DistributedPeristenceServerService.getInstance().storeVehicle(vehicle);
            MessageFactory.sendMessage(new VehicleDispatchedMessage(this.emergency.getCallId(), vehicle.getId()));
        } catch (HornetQException ex) {
            Logger.getLogger(ParticularEmergencyRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void selectMockHospital(long l) {
        Hospital mock = DistributedPeristenceServerService.getInstance().loadHospital(l);
        try {
            MessageFactory.sendMessage(new HospitalSelectedMessage(this.emergency.getCallId(), mock));
        } catch (HornetQException ex) {
            Logger.getLogger(ParticularEmergencyRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void selectMockFireDepartment(long l) {
        FirefightersDepartment mock = DistributedPeristenceServerService.getInstance().loadFireDepartment(l);
        try {
            MessageFactory.sendMessage(new FirefigthersDepartmentSelectedMessage(this.emergency.getCallId(), mock));
        } catch (HornetQException ex) {
            Logger.getLogger(ParticularEmergencyRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
