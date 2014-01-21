/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy;

import org.junit.Ignore;
import org.drools.runtime.rule.FactHandle;
import org.drools.SystemEventListenerFactory;
import org.drools.grid.conf.GridPeerServiceConfiguration;
import org.drools.grid.conf.impl.GridPeerConfiguration;
import org.drools.grid.impl.MultiplexSocketServerImpl;
import org.drools.grid.io.impl.MultiplexSocketServiceCongifuration;
import org.drools.grid.remote.mina.MinaAcceptorFactoryService;
import org.drools.grid.service.directory.impl.CoreServicesLookupConfiguration;
import org.drools.grid.service.directory.impl.WhitePagesLocalConfiguration;
import org.drools.grid.timer.impl.CoreServicesSchedulerConfiguration;
import java.util.HashMap;
import org.drools.grid.ConnectionFactoryService;
import org.drools.grid.GridConnection;
import org.drools.grid.SocketService;
import org.drools.grid.impl.GridImpl;
import org.drools.grid.service.directory.WhitePages;
import junit.framework.Assert;
import org.drools.common.DefaultFactHandle;
import java.util.Map;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactoryService;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactoryService;
import org.drools.builder.ResourceType;
import org.drools.grid.Grid;
import org.drools.grid.GridNode;
import org.drools.grid.GridServiceDescription;
import org.drools.io.impl.ByteArrayResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author salaboy
 */
public class DroolsGridServiceTest {

    private Map<String, GridServiceDescription> coreServicesMap;
    protected Grid grid1;
    protected GridNode remoteN1;
    
    public DroolsGridServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        this.coreServicesMap = new HashMap();
        createRemoteNode();
    }

    @After
    public void tearDown() {
        remoteN1.dispose();
        grid1.get(SocketService.class).close();
    }

    @Ignore
    public void helloIgnore() {
        StatefulKnowledgeSession ksession = createSession();
        
        ksession.setGlobal("myGlobalObj", new MyObject("myglobalObj"));
        
        FactHandle handle = ksession.insert(new MyObject("obj1"));
        Assert.assertNotNull(handle);
        Assert.assertEquals(true, ((DefaultFactHandle)handle).isDisconnected());
        
        int fired = ksession.fireAllRules();
        
        Assert.assertEquals(fired, 1);
    }
    @Test
    public void hello(){}
    
    protected StatefulKnowledgeSession createSession(){
        KnowledgeBuilder kbuilder = remoteN1.get( KnowledgeBuilderFactoryService.class ).newKnowledgeBuilder();

        assertNotNull( kbuilder );

         String rule = "package test\n"
                 + "import com.wordpress.salaboy.MyObject;\n"
                 + "global MyObject myGlobalObj;\n"
                 + "rule \"test\""
                 + "  when"
                 + "       $o: MyObject()"
                 + "  then"
                 + "      System.out.println(\"My Global Object -> \"+myGlobalObj.getName());"
                 + "      System.out.println(\"Rule Fired! ->\"+$o.getName());"
                 + " end";

        kbuilder.add( new ByteArrayResource( rule.getBytes() ),
                      ResourceType.DRL );

        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if ( errors != null && errors.size() > 0 ) {
            for ( KnowledgeBuilderError error : errors ) {
                System.out.println( "Error: " + error.getMessage() );

            }
            fail("KnowledgeBase did not build");
        }

        KnowledgeBase kbase = remoteN1.get( KnowledgeBaseFactoryService.class ).newKnowledgeBase();

        assertNotNull( kbase );

        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );

        StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
        return session;
    
    }
    
    private void createRemoteNode(){
        grid1 = new GridImpl( new HashMap<String, Object>() );
        configureGrid1( grid1,
                        8000,
                        null );

        Grid grid2 = new GridImpl( new HashMap<String, Object>() );
        configureGrid1( grid2,
                        -1,
                        grid1.get( WhitePages.class ) );

        GridNode n1 = grid1.createGridNode( "n1" );
        grid1.get( SocketService.class ).addService( "n1", 8000, n1 );
               
        GridServiceDescription<GridNode> n1Gsd = grid2.get( WhitePages.class ).lookup( "n1" );
        GridConnection<GridNode> conn = grid2.get( ConnectionFactoryService.class ).createConnection( n1Gsd );
        remoteN1 = conn.connect();
    
    }
    
    
    
    private void configureGrid1(Grid grid,
                                int port,
                                WhitePages wp) {

        //Local Grid Configuration, for our client
        GridPeerConfiguration conf = new GridPeerConfiguration();

        //Configuring the Core Services White Pages
        GridPeerServiceConfiguration coreSeviceWPConf = new CoreServicesLookupConfiguration( coreServicesMap );
        conf.addConfiguration( coreSeviceWPConf );

        //Configuring the Core Services Scheduler
        GridPeerServiceConfiguration coreSeviceSchedulerConf = new CoreServicesSchedulerConfiguration();
        conf.addConfiguration( coreSeviceSchedulerConf );

        //Configuring the WhitePages 
        WhitePagesLocalConfiguration wplConf = new WhitePagesLocalConfiguration();
        wplConf.setWhitePages( wp );
        conf.addConfiguration( wplConf );


        
        if ( port >= 0 ) {
            //Configuring the SocketService
            MultiplexSocketServiceCongifuration socketConf = new MultiplexSocketServiceCongifuration( new MultiplexSocketServerImpl( "127.0.0.1",
                                                                                                                              new MinaAcceptorFactoryService(),
                                                                                                                              SystemEventListenerFactory.getSystemEventListener(),
                                                                                                                              grid) );
            socketConf.addService( WhitePages.class.getName(), wplConf.getWhitePages(), port );
            conf.addConfiguration( socketConf );
        }
        conf.configure( grid );

    }
    
    

}