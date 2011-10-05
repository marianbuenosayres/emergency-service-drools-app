/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.procedures;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.grid.SocketService;
import org.example.ws_ht.api.TTaskAbstract;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import com.wordpress.salaboy.api.HumanTaskService;
import com.wordpress.salaboy.api.HumanTaskServiceFactory;
import com.wordpress.salaboy.conf.HumanTaskServiceConfiguration;
import com.wordpress.salaboy.context.tracking.ContextTrackingServiceImpl;
import com.wordpress.salaboy.grid.GridBaseTest;
import com.wordpress.salaboy.messaging.MessageConsumerWorker;
import com.wordpress.salaboy.messaging.MessageConsumerWorkerHandler;
import com.wordpress.salaboy.messaging.MessageServerSingleton;
import com.wordpress.salaboy.model.Call;
import com.wordpress.salaboy.model.Emergency;
import com.wordpress.salaboy.model.FireTruck;
import com.wordpress.salaboy.model.FirefightersDepartment;
import com.wordpress.salaboy.model.Location;
import com.wordpress.salaboy.model.Vehicle;
import com.wordpress.salaboy.model.messages.FireExtinctedMessage;
import com.wordpress.salaboy.model.messages.FireTruckOutOfWaterMessage;
import com.wordpress.salaboy.model.messages.ProcedureCompletedMessage;
import com.wordpress.salaboy.model.messages.VehicleHitsEmergencyMessage;
import com.wordpress.salaboy.model.serviceclient.DistributedPeristenceServerService;
import com.wordpress.salaboy.services.HumanTaskServerService;
import com.wordpress.salaboy.services.ProceduresMGMTService;
import com.wordpress.salaboy.smarttasks.jbpm5wrapper.conf.JBPM5HornetQHumanTaskClientConfiguration;
import java.util.ArrayList;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author esteban
 */
public class DefaultFireProcedureTest extends GridBaseTest {

    private HumanTaskService humanTaskServiceClient;

    public DefaultFireProcedureTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        HumanTaskServerService.getInstance().initTaskServer();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {

        HumanTaskServerService.getInstance().stopTaskServer();
    }
    private Emergency emergency = null;
    private FireTruck fireTruck = null;
    private Call call = null;
    private FirefightersDepartment firefightersDepartment = null;
    private MessageConsumerWorker procedureEndedWorker;
    private int proceduresEndedCount;

    @Before
    public void setUp() throws Exception {

        String emergencyId = ContextTrackingServiceImpl.getInstance().newEmergencyId();
        emergency = new Emergency();
        emergency.setId(emergencyId);

        String fireTruckId = ContextTrackingServiceImpl.getInstance().newVehicleId();
        fireTruck = new FireTruck("FireTruck 1");
        fireTruck.setId(fireTruckId);

        call = new Call(1, 2, new Date());

        String callId = ContextTrackingServiceImpl.getInstance().newCallId();
        call.setId(callId);
        emergency.setCall(call);
        emergency.setLocation(new Location(1, 2));
        emergency.setType(Emergency.EmergencyType.FIRE);
        emergency.setNroOfPeople(1);

        firefightersDepartment = new FirefightersDepartment("Firefighter Department 1", 12, 1);

        DistributedPeristenceServerService.getInstance().storeFirefightersDepartment(firefightersDepartment);
        DistributedPeristenceServerService.getInstance().storeEmergency(
                emergency);
        DistributedPeristenceServerService.getInstance().storeVehicle(fireTruck);
        MessageServerSingleton.getInstance().start();

        this.coreServicesMap = new HashMap();
        createRemoteNode();

        HumanTaskServiceConfiguration taskClientConf = new HumanTaskServiceConfiguration();

        taskClientConf.addHumanTaskClientConfiguration("jBPM5-HT-Client",
                new JBPM5HornetQHumanTaskClientConfiguration(
                "127.0.0.1", 5446));

        humanTaskServiceClient = HumanTaskServiceFactory.newHumanTaskService(taskClientConf);
        humanTaskServiceClient.initializeService();


        //Procedure Ended Worker
        procedureEndedWorker = new MessageConsumerWorker("ProcedureEndedCoreServer", new MessageConsumerWorkerHandler<ProcedureCompletedMessage>() {

            @Override
            public void handleMessage(ProcedureCompletedMessage procedureEndsMessage) {
                proceduresEndedCount++;
            }
        });

        procedureEndedWorker.start();
    }

    @After
    public void tearDown() throws Exception {
        MessageServerSingleton.getInstance().stop();
        if (remoteN1 != null) {
            remoteN1.dispose();
        }
        if (grid1 != null) {
            grid1.get(SocketService.class).close();
        }
        if (procedureEndedWorker != null) {
            procedureEndedWorker.stopWorker();
        }
        this.humanTaskServiceClient.cleanUpService();
    }

    @Test
    public void defaultFireSimpleTest() throws Exception {
        //start the process
        this.startProcess(call, emergency, fireTruck);

        //Because of the emergency, a new Task is ready for garage: pick the corresponding vehicle/s
        this.testGarageTask(emergency, fireTruck);

        // The fire truck doesn't reach the emergency yet. No task for
        // the firefighter.
        Map<String, String> firefighterTasks = this.getFirefighterTasks();
        Assert.assertTrue(firefighterTasks.isEmpty());

        // Now the fire truck arrives to the emergency
        ProceduresMGMTService.getInstance().notifyProcedures(
                new VehicleHitsEmergencyMessage(fireTruck.getId(),
                emergency.getId(), new Date()));

        Thread.sleep(2000);

        // A new task for the firefighter should be there now
        firefighterTasks = this.getFirefighterTasks();
        Assert.assertEquals(1, firefighterTasks.size());

        //The firefighter completes the task
        String firefighterTaskId = firefighterTasks.keySet().iterator().next();
        this.completeTask("firefighter", firefighterTaskId);

        // Becasuse the fire truck still got enough water, no "Water Refill"
        // task exists
        firefighterTasks = this.getFirefighterTasks();
        Assert.assertTrue(firefighterTasks.isEmpty());

        //The process didn't finish yet
        Assert.assertEquals(0, proceduresEndedCount);

        // Ok, no more fire!
        ProceduresMGMTService.getInstance().notifyProcedures(
                new FireExtinctedMessage(emergency.getId(), new Date()));

        Thread.sleep(5000);

        //The emergency has ended
        Assert.assertEquals(1,proceduresEndedCount);

    }

    @Test
    public void fireTruckOutOfWaterx2Test() throws Exception {

        //start the process
        this.startProcess(call, emergency, fireTruck);

        //Because of the emergency, a new Task is ready for garage: pick the corresponding vehicle/s
        this.testGarageTask(emergency, fireTruck);

        // The fire truck doesn't reach the emergency yet. No task for
        // the firefighter.
        Map<String, String> firefighterTasks = this.getFirefighterTasks();
        Assert.assertTrue(firefighterTasks.isEmpty());

        // Now the fire truck arrives to the emergency
        ProceduresMGMTService.getInstance().notifyProcedures(
                new VehicleHitsEmergencyMessage(fireTruck.getId(),
                emergency.getId(), new Date()));

        Thread.sleep(2000);

        // A new task for the firefighter should be there now
        firefighterTasks = this.getFirefighterTasks();
        Assert.assertEquals(1, firefighterTasks.size());

        //The firefighter completes the task
        String firefighterTaskId = firefighterTasks.keySet().iterator().next();
        this.completeTask("firefighter", firefighterTaskId);

        // Becasuse the fire truck still got enough water, no "Water Refill"
        // task exists
        firefighterTasks = this.getFirefighterTasks();
        Assert.assertTrue(firefighterTasks.isEmpty());

        // Sudenly, the fire truck runs out of water
        ProceduresMGMTService.getInstance().notifyProcedures(
                new FireTruckOutOfWaterMessage(emergency.getId(), fireTruck.getId(),
                new Date()));

        Thread.sleep(5000);

        //Now, the firefighter has a new task
        firefighterTasks = this.getFirefighterTasks();
        Assert.assertEquals(1, firefighterTasks.size());

        firefighterTaskId = firefighterTasks.keySet().iterator().next();
        String firefighterTaskName = firefighterTasks.values().iterator().next();

        Assert.assertEquals(
                "Water Refill: go to ( " + firefightersDepartment.getX() + ", "
                + firefightersDepartment.getY() + " )", firefighterTaskName);

        // The firefighter completes the task
        this.completeTask("firefighter", firefighterTaskId);

        // No more tasks for firefighter
        firefighterTasks = this.getFirefighterTasks();
        Assert.assertTrue(firefighterTasks.isEmpty());

        // The Fire Truck returns to the emergency
        ProceduresMGMTService.getInstance().notifyProcedures(
                new VehicleHitsEmergencyMessage(fireTruck.getId(),
                emergency.getId(), new Date()));

        Thread.sleep(2000);

        // A new task for the firefighter should be there now
        firefighterTasks = this.getFirefighterTasks();
        Assert.assertEquals(1, firefighterTasks.size());

        firefighterTaskId = firefighterTasks.keySet().iterator().next();

        // The firefighter completes the task
        this.completeTask("firefighter", firefighterTaskId);

        // Becasuse the fire truck still got enough water, no "Water Refill"
        // task exists
        firefighterTasks = this.getFirefighterTasks();
        Assert.assertTrue(firefighterTasks.isEmpty());

        // Again, the fire truck runs out of water
        ProceduresMGMTService.getInstance().notifyProcedures(
                new FireTruckOutOfWaterMessage(emergency.getId(), fireTruck.getId(),
                new Date()));

        Thread.sleep(5000);

        firefighterTasks = this.getFirefighterTasks();
        Assert.assertEquals(1, firefighterTasks.size());

        firefighterTaskId = firefighterTasks.keySet().iterator().next();
        firefighterTaskName = firefighterTasks.values().iterator().next();

        Assert.assertEquals(
                "Water Refill: go to ( " + firefightersDepartment.getX() + ", "
                + firefightersDepartment.getY() + " )", firefighterTaskName);

        // The firefighter completes the task
        this.completeTask("firefighter", firefighterTaskId);

        // No more tasks for firefighter
        firefighterTasks = this.getFirefighterTasks();
        Assert.assertTrue(firefighterTasks.isEmpty());

        //The process didn't finish yet
        Assert.assertEquals(0,proceduresEndedCount);

        // Ok, no more fire!
        ProceduresMGMTService.getInstance().notifyProcedures(
                new FireExtinctedMessage(emergency.getId(), new Date()));

        Thread.sleep(5000);

        //The emergency has ended
        Assert.assertEquals(1,proceduresEndedCount);

    }

    private void startProcess(Call call, Emergency emergency, Vehicle fireTruck) throws InterruptedException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("call", call);
        parameters.put("emergency", emergency);
        parameters.put("vehicle", fireTruck);

        ProceduresMGMTService.getInstance().newRequestedProcedure(emergency.getId(),
                "DefaultFireProcedure", parameters);
        Thread.sleep(2000);
    }

    private void testGarageTask(Emergency emergency, Vehicle selectedVehicle) throws Exception {
        List<TTaskAbstract> taskAbstracts = humanTaskServiceClient.getMyTaskAbstracts("", "garage_emergency_service", "", null, "", "", "", 0, 0);
        Assert.assertNotNull(taskAbstracts);
        Assert.assertEquals(1, taskAbstracts.size());
        TTaskAbstract taskAbstract = taskAbstracts.get(0); // getting the first task
        Assert.assertEquals(" Select Vehicle For " + emergency.getId() + " ", taskAbstract.getName().getLocalPart());

        //Garage team starts working on the task
        humanTaskServiceClient.setAuthorizedEntityId("garage_emergency_service");
        humanTaskServiceClient.start(taskAbstract.getId());


        //A Firetruck is selected
        Map<String, Object> info = new HashMap<String, Object>();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        vehicles.add(selectedVehicle);
        info.put("emergency.vehicles", vehicles);

        //Garage team completes the task
        humanTaskServiceClient.complete(taskAbstract.getId(), info);

        Thread.sleep(2000);
    }

    private Map<String, String> getFirefighterTasks() throws Exception {
        humanTaskServiceClient.setAuthorizedEntityId("firefighter");
        List<TTaskAbstract> abstracts = humanTaskServiceClient.getMyTaskAbstracts("", "firefighter", "", null, "", "", "", 0,
                0);
        Map<String, String> ids = new HashMap<String, String>();
        if (abstracts != null) {
            for (TTaskAbstract tTaskAbstract : abstracts) {
                ids.put(tTaskAbstract.getId(), tTaskAbstract.getName().toString());
            }
        }

        return ids;
    }

    private void completeTask(String user, String taskId) throws Exception {
        humanTaskServiceClient.setAuthorizedEntityId(user);

        Map<String, Object> info = new HashMap<String, Object>();
        info.put("emergency.priority", 1);
        humanTaskServiceClient.start(taskId);
        humanTaskServiceClient.complete(taskId, info);

        Thread.sleep(2000);
    }
}