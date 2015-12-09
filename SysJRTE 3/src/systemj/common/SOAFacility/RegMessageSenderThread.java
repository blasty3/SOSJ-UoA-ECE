/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systemj.common.SOAFacility;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;
import systemj.common.CDLCBuffer;
import systemj.common.SJSOAMessage;
import systemj.common.SJSSCDSignalChannelMap;
import systemj.common.SJServiceRegistry;
import systemj.common.SOAFacility.Support.NetworkConnCheckSimple;
import systemj.common.SOAFacility.Support.SOABuffer;

 
    
    
/**
 *
 * @author Udayanto
 */
public class RegMessageSenderThread implements Runnable {

    //NetworkConnCheckSimple netcheck = new NetworkConnCheckSimple();

    
    @Override
    public void run() {
        
        SJSOAMessage sjdisc = new SJSOAMessage();
        
        String GtwyAddr = SOABuffer.getGatewayAddr();
        String SubnetMask = SOABuffer.getSubnetMaskAddr();
        
        String broadcastAddr = getBroadcastAddress(GtwyAddr,SubnetMask);
        
        //String broadcastAddr = "192.168.1.255";
        
        System.out.println("RegMessageSender thread started");
        
        NetworkConnCheckSimple netcheck = new NetworkConnCheckSimple();
        
        long lastReqAdvTransmittedTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();
        
        while (true) {
            
           //System.out.println("MessageSender thread executed");
            
      //     if (SJServiceRegistry.getParsingStatus()) {

                String connStat = netcheck.CheckNetworkConn(SOABuffer.getGatewayAddr(), 1400);
                
                //System.out.println("MessageSender, ConnectionStat: " +connStat);
                
                //if (SJServiceRegistry.HasServiceConsumerAndProvider()){
                    
                    //need to send disc, disc reply, req adv, and adv message
                    
                
                
                    if (connStat.equalsIgnoreCase("Connected")){

                        // disc and adv
                        
                        //System.out.println("Adv and Disc status: " +SOABuffer.getIsInitAdvDone()+ " and " +SOABuffer.getIsInitDiscDone());
                        
                     //   if (!SOABuffer.getIsInitDiscDone() || !SOABuffer.getIsInitAdvDone()){
                        
                            //System.out.println("Adv and Disc status: " +SOABuffer.getIsInitDiscDone()+ " and " +SOABuffer.getIsInitDiscDone());
                        //System.out.println("Registry connected!");
                            

                            //adv transmission
                            
                            if (!SOABuffer.getIsInitAdvDone()){
                                System.out.println("RegMessageSender transmitting adv!");
                                /*
                                int j = 0 ;
                                
                                JSONObject jsAllServAvail = SOABuffer.getAdvModBuffer();
                                
                                Enumeration keysAvail = jsAllServAvail.keys();
                                
                                while (keysAvail.hasMoreElements()){
                                    String keyServ = keysAvail.nextElement().toString();
                                    
                                    try {
                                        String avail = jsAllServAvail.getString(keyServ);
                                        
                                        if (avail.equalsIgnoreCase("available") || avail.equalsIgnoreCase("visible")){
                                            j++;
                                        }
                                        
                                        
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                    
                                }
                                */
                                
                               // if(j>0){
                                    
                                    //String LocalSSName = SJSSCDSignalChannelMap.getLocalSSName();
                                    
                                    //String AdvMsg = sjdisc.ConstructRegistryReAdvertisementMessage(Long.toString(SJServiceRegistry.getOwnAdvertisementTimeLimit()),SOABuffer.getSOSJRegID(),SOABuffer.getSOSJRegAddr());
                                    String AdvMsg= sjdisc.ConstructRegistryReAdvertisementMessage(SOABuffer.getSOSJRegExpiryTime(),SOABuffer.getSOSJRegID(), SOABuffer.getSOSJRegAddr());
                                    SendAdvMsg(broadcastAddr,AdvMsg);
                                    
                                    SJServiceRegistry.RecordAdvertisementTimeStamp(); 
                                    
                               // }
                                
                                SOABuffer.setIsInitAdvDone(true);
                                
                                
                            } 

                           
                            Vector discMsgs = SOABuffer.getAllDiscMsgFromBuffer();
                            
                            for (int i=0;i<discMsgs.size();i++){
                                JSONObject discMsg = (JSONObject)discMsgs.get(i);
                                
                                try {
                                    String discReplyMsg = ProcessMessage(discMsg, SJServiceRegistry.obtainCurrentRegistry());
                                    
                                    if(!discReplyMsg.equalsIgnoreCase("{}")){
                                        SendDiscReplyMsg(discReplyMsg);
                                    }
                                    
                                } catch (JSONException ex) {
                                   ex.printStackTrace();
                                }
                                
                                
                            }
                           

                            //adv transmission from req adv
                        
                            //send registry adv
                           
                            
                            Vector allContentReqAdvBuffer = SOABuffer.getAllContentReqAdvBuffer();
                            
                            if(allContentReqAdvBuffer.size()>0){
                                
                                for (int i=0;i<allContentReqAdvBuffer.size();i++){
                                    
                                     JSONObject jsReqAdvMsg = (JSONObject) allContentReqAdvBuffer.get(i);
                                     
                                          JSONObject generatedRespAdvMsgJSON = ProcessMessageInJSON(jsReqAdvMsg.toString(), SJServiceRegistry.obtainInternalRegistryProviderOnly().toString());     
                                          
                                            //String destAddr=null;
                                            try {
                                                String destAddr = jsReqAdvMsg.getString("sourceAddress");
                                                
                                                if(destAddr.equalsIgnoreCase(SOABuffer.getSOSJRegAddr())){
                                                    SendAdvMsg("224.0.0.100", generatedRespAdvMsgJSON.toString());
                                                } else {
                                                    SendAdvMsg(destAddr, generatedRespAdvMsgJSON.toString());
                                                }
                                                
                                                //destAddr = generatedRespAdvMsgJSON.getString("destIPAddr");
                                                
                    //String respAdvMsg = sjdisc.ConstructReAdvertisementMessage(Long.toString(SJServiceRegistry.getOwnAdvertisementTimeLimit()));
                                            } catch (JSONException ex) {
                                            System.out.println("cannot find destination Address in ProcessMessageInJSON");
                                        } 
                                         
                                       
                                     
                                    
                
                                       // if (destAddr!=null){
                                            
                                        //}
                                     
                                }
                                
                            }
                            
                            
                        /* 
                        JSONObject jsReqAdvMsg = SOABuffer.getReqAdvBuffer();   
                        
                        if (!jsReqAdvMsg.isEmpty()){
                            
                            JSONObject generatedRespAdvMsgJSON = ProcessMessageInJSON(jsReqAdvMsg.toString(), SJServiceRegistry.obtainInternalRegistryVisibleProviderOnly().toString());     
                            
                            String destAddr=null;
                            try {
                            
                                destAddr = generatedRespAdvMsgJSON.getString("destIPAddr");
                    
                    //String respAdvMsg = sjdisc.ConstructReAdvertisementMessage(Long.toString(SJServiceRegistry.getOwnAdvertisementTimeLimit()));
                            } catch (JSONException ex) {
                                System.out.println("cannot find destination Address in ProcessMessageInJSON");
                            } 
                
                            if (destAddr!=null){
                                SendAdvMsg(destAddr, generatedRespAdvMsgJSON.toString());
                            }
                            
                        }
                        */
          
                            
                 //   }
                    //    else
                        
                       // {
 
                            
                        
                                //JSONObject jsDiscReq = SOABuffer.getDiscReqBuffer();
                                
                               // String expiredServAddr = CheckAlmostExpiredAdv();
                            
                            //req adv transmission

                        /*        
                                
                        currentTime = System.currentTimeMillis();
                        
                        if (currentTime-lastReqAdvTransmittedTime>500){
                            
                            String expiredServAddr = CheckAlmostExpiredAdv();
                
                            if (expiredServAddr.equalsIgnoreCase("nothing")){
                    
                            } else {
                    
                                String ReqAdvMsg = sjdisc.ConstructRequestAdvertisementMessage(expiredServAddr);
                    
                                SendReqAdvMsg(expiredServAddr,ReqAdvMsg);
                                lastReqAdvTransmittedTime = System.currentTimeMillis();
                            }
                            
                        }
                                
                                
                                
                            // end req adv tranmission
                                
                                //Disc transmission

                        //if (needDisc || !jsDiscReq.isEmpty()){
                        
                                //28 Dec 2014 - Discovery should be application controlled
                                
                                /*
                                
                                boolean needDisc = SJServiceRegistry.CheckUnavailableService();
                                
                                if (needDisc){
                                    JSONObject jsServList = SJServiceRegistry.getUnavailableServiceReturnTypeList();
                                    String DiscMsg = sjdisc.ConstructDiscoveryMessageOfExpServType(jsServList);
                                 
                                    SendDiscMsg(broadcastAddr, DiscMsg);
                                 
                                } 
                                */
                                    
                                  /*  
                                if (!jsDiscReq.isEmpty()){

                                    try {
                                        String servType = jsDiscReq.getString("servName");
                                    
                                        JSONObject js1 = new JSONObject();
                                    
                                        String LocalSSName = SJSSCDSignalChannelMap.getLocalSSName();
                                        
                                        js1.put("1", servType);
                                    
                                        String DiscMsg = sjdisc.ConstructDiscoveryMessageOfExpServType(js1,LocalSSName);
                                        SendDiscMsg(broadcastAddr, DiscMsg);
                                    } catch (JSONException ex) {
                                        System.out.println("MessageSenderThread JSONException: " +ex.getMessage());
                                    }

                                
                            }
                                */
                                /*
                                else if (needDisc && !jsDiscReq.isEmpty() ){
                                
                                    JSONObject jsServList = SJServiceRegistry.getUnavailableServiceReturnTypeList();
                                    String DiscMsg = sjdisc.ConstructDiscoveryMessageOfExpServType(jsServList);
                                 
                                    SendDiscMsg(broadcastAddr, DiscMsg);
                                    
                                    
                                    try {
                                        String servType = jsDiscReq.getString("servName");
                                    
                                        JSONObject js1 = new JSONObject();
                                    
                                        js1.put("1", servType);
                                    
                                        String DiscMsg2 = sjdisc.ConstructDiscoveryMessageOfExpServType(js1);
                                        SendDiscMsg(broadcastAddr, DiscMsg2);
                                    } catch (JSONException ex) {
                                        System.out.println("MessageSenderThread JSONException: " +ex.getMessage());
                                    }
                                
                            }
                             */   
                                //regular adv
                            
                            boolean startRegAdv = CheckOwnAdv();
                            //boolean startAdv = SOABuffer.getAdvTransmissionRequest();

                                //int j = 0 ;
                                
                                /*
                                
                                JSONObject jsAllServAvail = SOABuffer.getAdvModBuffer();
                                
                                Enumeration keysAvail = jsAllServAvail.keys();
                                
                                while (keysAvail.hasMoreElements()){
                                    String keyServ = keysAvail.nextElement().toString();
                                    
                                    try {
                                        String avail = jsAllServAvail.getString(keyServ);
                                        
                                        if (avail.equalsIgnoreCase("available") || avail.equalsIgnoreCase("visible")){
                                            j++;
                                        }
                                        
                                        
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                    
                                }
                            */
                            
                            if (startRegAdv){
                                
                                //String LocalSSName = SJSSCDSignalChannelMap.getLocalSSName();
                                
                                String AdvMsg = sjdisc.ConstructRegistryReAdvertisementMessage(SOABuffer.getSOSJRegExpiryTime(), SOABuffer.getSOSJRegID(), SOABuffer.getSOSJRegAddr());
                                
                                //System.out.println("sent Reg adv: " +AdvMsg);
                                //String AdvMsg = sjdisc.ConstructReAdvertisementMessageOfInclServ(Long.toString(SJServiceRegistry.getOwnAdvertisementTimeLimit()),SOABuffer.getAllAdvVisib(),LocalSSName);
                                SendAdvMsg(broadcastAddr, AdvMsg);
                                SJServiceRegistry.RecordAdvertisementTimeStamp();
                                //SOABuffer.SetAdvTransmissionRequest(false);
                                //System.out.println("MessageSenderThread, AdvMsg: " +AdvMsg);
                                //System.out.println("MessageSenderThread, VisibAdv: " +SOABuffer.getAllAdvVisib().toString());
                            }
    
                            //end regular adv
    
                    //}       
                        // adv transmission responding req adv
                        
              //      jsReqAdvMsg = SOABuffer.getReqAdvBuffer();     
                    
              //      if (!jsReqAdvMsg.isEmpty()){
              //          JSONObject generatedRespAdvMsgJSON = ProcessMessageInJSON(jsReqAdvMsg.toString(), SJServiceRegistry.obtainInternalRegistryVisibleProviderOnly().toString());     
                            
              //          String destAddr=null;
              //          try {
              //              destAddr = generatedRespAdvMsgJSON.getString("destIPAddr");
                    
                    //String respAdvMsg = sjdisc.ConstructReAdvertisementMessage(Long.toString(SJServiceRegistry.getOwnAdvertisementTimeLimit()));
             //           } catch (JSONException ex) {
             //               System.out.println("cannot find destination Address in ProcessMessageInJSON" +ex.getMessage());
             //           } 
                
              //          if (destAddr!=null){
             //               SendAdvMsg(destAddr, generatedRespAdvMsgJSON.toString());
             //           }

             //         }
                    
                     // end adv transmission responding req adv

                    //req adv check and transmission
                            
                            
                            Hashtable expiredServAddrs = CheckAlmostExpiredAdv();
                
                            if (!expiredServAddrs.isEmpty()){
                                
                                    Enumeration keysExpServAddrs = expiredServAddrs.keys();
                                    
                                    currentTime = System.currentTimeMillis();
                                    
                                    if(currentTime-lastReqAdvTransmittedTime>500){
                                        
                                        while(keysExpServAddrs.hasMoreElements()){
                                            String index = keysExpServAddrs.nextElement().toString();
                                        
                                            //String LocalSSName = SJSSCDSignalChannelMap.getLocalSSName();
                                            
                                            String destSSName = (String) expiredServAddrs.get(index);
                                        
                                            String destAddr = SJServiceRegistry.getAddrOfSS(destSSName);
                                            
                                            String ReqAdvMsg = sjdisc.ConstructNoP2PRegToProvReqAdvertisementMessage(SOABuffer.getSOSJRegID(), SOABuffer.getSOSJRegAddr(), destSSName);
                                            //String ReqAdvMsg = sjdisc.ConstructRequestAdvertisementMessage(addr, LocalSSName);
                    
                                            if(destAddr.equalsIgnoreCase(SOABuffer.getSOSJRegAddr())){
                                                
                                                boolean stat = SendReqAdvMsg("224.0.0.100",ReqAdvMsg);
                                                
                                            } else {
                                                
                                                boolean stat = SendReqAdvMsg(destAddr,ReqAdvMsg);
                                            
                                                
                                            }
                                            
                                            
                                            
                                            //if not available, then remove from registry
                                        
                                        }
                               
                                    lastReqAdvTransmittedTime = System.currentTimeMillis();
                                    }
                                    
                            }
                            
                        /*
                        String answer = SJServiceRegistry.checkServiceExpiryForAdvertiseRequest();

                        if (!answer.equalsIgnoreCase("nothing")){

                            String message2 = sjdisc.ConstructRequestAdvertisementMessage(answer);
                            SendReqAdvMsg(answer, message2);
                        } 
                            */
                         // end req adv check and transmission

                      } else {
                        //    System.out.println("No connection");
                      }
                   
                   //} 
                 
                
                // CD migration... for all conditions whether consumer only, prov only, or both
                
                /*
                Vector allMigrationReqMsg = CDLCBuffer.getMigrationRequestMsg();
                
                if (allMigrationReqMsg.size()>0) {
                    
                    //start migration msg receiver thread
                    
                    int recMsgAmount = allMigrationReqMsg.size();
                    
                    for (int j=0;j<recMsgAmount;j++){
                        
                        //the first message is responded with ACK OK, and then needs to initiate code n sigchan mapping det, the rest of the message responded with ACK NOT OK. Opposite party needs to resend migration req if they wish
                        
                        JSONObject reqMsg = (JSONObject)allMigrationReqMsg.get(j);
                        
                        if(j==0){
                            
                            boolean migPortFree = CheckMigrationRecPortFree();
                            
                            if(CDLCBuffer.getMigrationBusyFlag() || !migPortFree){
                                try {
                                    String destAddress = reqMsg.getString("sourceAddress");
                                
                                    String message = sjdisc.ConstructResponseMigrationMessage("NOT OK", SJSSCDSignalChannelMap.getLocalSSName());
                                
                                    SendRespServMigration(destAddress, message);
                                }   catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                try {
                                String destAddress = reqMsg.getString("sourceAddress");
                                String sourceDestSS = reqMsg.getString("destinationSubsystem");
                                String migType = reqMsg.getString("discMsgType");
                                
                                if(SJSSCDSignalChannelMap.getLocalSSName().equals(sourceDestSS)){
                                    
                                    String message = sjdisc.ConstructResponseMigrationMessage("OK", SJSSCDSignalChannelMap.getLocalSSName());
                                
                                    if(migType.equals("strong")){
                                        //Thread CodeOnlyMigMsgRecThr = new Thread(new StrongMigrationMsgReceiverThread());
                    
                                        //CodeOnlyMigMsgRecThr.start();
                                    } else if(migType.equals("weak")){
                                        //Thread WeakMigMsgRecThr = new Thread(new WeakMigrationMsgReceiverThread());
                    
                                        //WeakMigMsgRecThr.start();
                                    }
                                    
                                    
                                
                                    SendRespServMigration(destAddress, message);
                                    
                                } else {
                                    String message = sjdisc.ConstructResponseMigrationMessage("NOT OK", SJSSCDSignalChannelMap.getLocalSSName());
                                
                                    //Thread MigMsgRecThr = new Thread(new MigrationMsgReceiverThread());
                    
                                    //MigMsgRecThr.start();
                                
                                    SendRespServMigration(destAddress, message);
                                }
                                
                                
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            
                        } else {
                            
                            try {
                                
                                 String LocalSSName = SJSSCDSignalChannelMap.getLocalSSName();
                                
                                String destAddress = reqMsg.getString("sourceAddress");
                                
                                String message = sjdisc.ConstructResponseMigrationMessage("NOT OK", LocalSSName);
                                
                                SendRespServMigration(destAddress, message);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                            
                        }
                        
                        
                        
                    }
                    
                }
                */
     
       //  }     
            
           
       }
    }
    
    private JSONObject ProcessMessageInJSON(String SJMessage,String OfferedServices)
    {
        JSONObject serviceList = new JSONObject();
        JSONObject processedSJMessage = new JSONObject();
        SJSOAMessage sjdisc = new SJSOAMessage();
        int i = 1;
        try {
            //JSONObject jsAllIntServ = SJServiceRegistry.obtainInternalRegistry();
            JSONObject jsAllIntServ = new JSONObject(new JSONTokener(OfferedServices));
            JSONObject jsMsg = new JSONObject(new JSONTokener(SJMessage));
            
            if (jsMsg.getString("MsgType").equalsIgnoreCase("discovery")){ //if handling discovery request message
                
                Enumeration keysjsAllIntServ = jsAllIntServ.keys();
                
                while (keysjsAllIntServ.hasMoreElements()){
                    
                    Object keyAllIntServ = keysjsAllIntServ.nextElement();
                    
                    JSONObject jsIndivIntServ = jsAllIntServ.getJSONObject(keyAllIntServ.toString());
                    
                    //JSONObject jsExpectedServiceType = jsMsg.getJSONObject("expServiceType");
                    
                    //Enumeration keysjsExpectedServiceType = jsExpectedServiceType.keys();
                    
                    //while (keysjsExpectedServiceType.hasMoreElements()){
                        //Object keyExpectedServiceType = keysjsExpectedServiceType.nextElement();
                        
                       // if (jsIndivIntServ.getString("serviceType").equalsIgnoreCase(jsExpectedServiceType.getString(keyExpectedServiceType.toString()))){
                            //i++;
                            serviceList.put(jsIndivIntServ.getString("serviceName"),jsIndivIntServ);
                       // }
                    //}  
                }
                
                if (!serviceList.toString().equalsIgnoreCase("{}")){
                    processedSJMessage = new JSONObject(new JSONTokener(SJMessage));
                    //processedSJMessage = new JSONObject(new JSONTokener(sjdisc.ConstructDiscoveryReplyMessage(serviceList))); <-- need to switch this in the future, 15 April 2014
                    processedSJMessage.put("serviceList",serviceList);
                    processedSJMessage.put("sourceSS",jsMsg.getString("sourceSS"));
                    //processedSJMessage.remove("expecServiceList");
                    processedSJMessage.put("MsgType","discReply");
                } else {
                    processedSJMessage = new JSONObject();
                    
                }
                
            }
            
            /*
            else if (jsMsg.getString("discMsgType").equalsIgnoreCase("discReply")){
                //serviceList = SJServiceRegistry.obtainInternalRegistry();
                if (!jsAllIntServ.toString().equalsIgnoreCase("{}"))
                {
                    processedSJMessage = new JSONObject(new JSONTokener(SJMessage));
                    processedSJMessage.put("serviceList",jsAllIntServ);
                    processedSJMessage.remove("expecServiceList");
                }
            } 
            */
            else if (jsMsg.getString("MsgType").equalsIgnoreCase("regRequestAdvertise")){
                
                //processedSJMessage = new JSONObject(new JSONTokener(SJMessage));
                /*
                Enumeration keysjsAllIntServ = jsAllIntServ.keys();
                
                while (keysjsAllIntServ.hasMoreElements()){
                    
                    Object keyAllIntServ = keysjsAllIntServ.nextElement();
                    
                    JSONObject jsIndivIntServ = jsAllIntServ.getJSONObject(keyAllIntServ.toString());
                    
                    //JSONObject jsExpectedServiceType = jsMsg.getJSONObject("expServiceType");
                    
                    //Enumeration keysjsExpectedServiceType = jsExpectedServiceType.keys();
                    
                   // while (keysjsExpectedServiceType.hasMoreElements()){
                       // Object keyExpectedServiceType = keysjsExpectedServiceType.nextElement();
                        
                      //  if (jsIndivIntServ.getString("serviceType").equalsIgnoreCase(jsExpectedServiceType.getString(keyExpectedServiceType.toString()))){
                            //serviceList.put(jsIndivIntServ.getString("serviceName"),jsIndivIntServ);
                            //i++;
                       // }
                   // } 
                }
                
                 //String LocalSSName = SJSSCDSignalChannelMap.getLocalSSName();
                
                //String destIPAddr = jsMsg.getString("sourceAddress");
                */
                processedSJMessage = sjdisc.ConstructRegResponseReAdvertisementMessageInJSON(SOABuffer.getSOSJRegExpiryTime(), SOABuffer.getSOSJRegID(), SOABuffer.getSOSJRegAddr());
                
                //processedSJMessage = sjdisc.ConstructResponseReAdvertisementMessageInJSON(Long.toString(SJServiceRegistry.getOwnAdvertisementTimeLimit()), destIPAddr, LocalSSName);
   
            }
            
        } catch (JSONException ex) {
            ex.printStackTrace();
            //System.out.println("Error SJMessageHandler Message Process:" +ex.printStackTrace());
            System.exit(1);
        }
        
        return processedSJMessage;
 
    }
    
    
    private String ProcessMessage(JSONObject jsMsg,JSONObject jsAllServ)
    {
        JSONObject serviceList = new JSONObject();
        JSONObject processedSJMessage = new JSONObject();
        SJSOAMessage sjdisc = new SJSOAMessage();
        int i = 1;
        
        System.out.println("RegMessageSenderThread, responding to Msg: " +jsMsg);
        
        try {
            //JSONObject jsAllIntServ = SJServiceRegistry.obtainInternalRegistry();
            //JSONObject jsAllIntServ = new JSONObject(new JSONTokener(OfferedServices));
            
            //JSONObject jsMsg = new JSONObject(new JSONTokener(SJMessage));
            
            
            
            if (jsMsg.getString("MsgType").equalsIgnoreCase("discovery")){ //if handling discovery request message
                
                Enumeration keysSSName = jsAllServ.keys();
                
                while (keysSSName.hasMoreElements()){
                    
                    String keySSName = keysSSName.nextElement().toString();
                    
                    JSONObject jsAllSSServ = jsAllServ.getJSONObject(keySSName);
                    
                    
                    
                    //JSONObject jsExpectedServiceType = jsMsg.getJSONObject("expServiceType");
                    
                    //Enumeration keysjsExpectedServiceType = jsExpectedServiceType.keys();
                    
                   // while (keysjsExpectedServiceType.hasMoreElements()){
                    //    Object keyExpectedServiceType = keysjsExpectedServiceType.nextElement();
                        
                    //    if (jsIndivIntServ.getString("serviceType").equalsIgnoreCase(jsExpectedServiceType.getString(keyExpectedServiceType.toString()))){
                            //i++;
                            serviceList.put(keySSName,jsAllSSServ);
                    //    }
                    //}  
                }
                
                if (!serviceList.toString().equalsIgnoreCase("{}")){
                    
                     //processedSJMessage = new JSONObject(new JSONTokener(SJMessage));
                        //processedSJMessage = jsMsg;
                    
                    if(jsMsg.getString("sourceAddress").equals(SOABuffer.getSOSJRegAddr())){
                        processedSJMessage.put("destinationAddress","224.0.0.100");
                        processedSJMessage.put("sourceAddress","224.0.0.100");
                    } else {
                        processedSJMessage.put("destinationAddress",jsMsg.getString("sourceAddress"));
                        processedSJMessage.put("sourceAddress",SOABuffer.getSOSJRegAddr());
                    }
                    
                   
                    //processedSJMessage = new JSONObject(new JSONTokener(sjdisc.ConstructDiscoveryReplyMessage(serviceList))); <-- need to switch this in the future, 15 April 2014
                    processedSJMessage.put("serviceList",serviceList);
                    processedSJMessage.put("CDStats", CDLCBuffer.GetAllCDMacroState());
                    //processedSJMessage.remove("expecServiceList");
                    processedSJMessage.put("MsgType","discReply");
                    
                    if(jsMsg.has("sourceSS")){
                        processedSJMessage.put("destSS",jsMsg.getString("sourceSS"));
                    }
                    
                   
                } else {
                    processedSJMessage = new JSONObject();
                    
                }
                
            }
           
            else if (jsMsg.getString("MsgType").equalsIgnoreCase("regRequestAdvertise")){
                
                //processedSJMessage = new JSONObject(new JSONTokener(SJMessage));

                /*
                Enumeration keysjsAllIntServ = jsAllIntServ.keys();
                
                while (keysjsAllIntServ.hasMoreElements()){
                    
                    Object keyAllIntServ = keysjsAllIntServ.nextElement();
                    
                    JSONObject jsIndivIntServ = jsAllIntServ.getJSONObject(keyAllIntServ.toString());
                    
                    //JSONObject jsExpectedServiceType = jsMsg.getJSONObject("expServiceType");
                    
                    //Enumeration keysjsExpectedServiceType = jsExpectedServiceType.keys();
                    
                    //while (keysjsExpectedServiceType.hasMoreElements()){
                    //    Object keyExpectedServiceType = keysjsExpectedServiceType.nextElement();
                        
                    //    if (jsIndivIntServ.getString("serviceType").equalsIgnoreCase(jsExpectedServiceType.getString(keyExpectedServiceType.toString()))){
                            //serviceList.put(jsIndivIntServ.getString("serviceName"),jsIndivIntServ);
                            //i++;
                    //    }
                   // } 
                }
                */
                
                String LocalSSName = SJSSCDSignalChannelMap.getLocalSSName();
                
                String regAddr = SOABuffer.getSOSJRegAddr();
                
                if(regAddr.equals(jsMsg.getString("sourceAddress"))){
                    
                    String destIPAddr = "224.0.0.100";
                
                    processedSJMessage = sjdisc.ConstructResponseReAdvertisementMessageInJSON(Long.toString(SJServiceRegistry.getOwnAdvertisementTimeLimit()), destIPAddr, LocalSSName);
                    
                } else {
                    
                    String destIPAddr = jsMsg.getString("sourceAddress");
                
                    processedSJMessage = sjdisc.ConstructResponseReAdvertisementMessageInJSON(Long.toString(SJServiceRegistry.getOwnAdvertisementTimeLimit()), destIPAddr, LocalSSName);
                    
                }
                
                
   
            }
            
        } catch (JSONException ex) {
            
            ex.printStackTrace();
            System.exit(1);
        }
        
        return processedSJMessage.toString();
 
    }
    
    
    
    private void SendAdvMsg(String ipAddr, String message){
        
        int infoDebug=0;
        int Debug=1;
        
        try
                                   {
                                       
                                       InetAddress ipAddress = InetAddress.getByName(ipAddr);
                                       
                                       if (infoDebug ==1 ) System.out.println("BroadcastMessage send stage 1");
                                       byte[] msg = new byte[8096];
                                      //ipAddress=InetAddress.getByName("192.168.1.255"); //assumed broadcast address
                                      //ipAddress = InetAddress.getByName(super.buffer[1].toString());
                                       
                                       if (infoDebug ==1 ) System.out.println("BroadcastMessage send stage 2");
                                       
                                       
                                       if (infoDebug ==1 ) System.out.println("BroadcastMessage send stage 3");
                                               //SJServiceRegistry.ConstructBroadcastDiscoveryMessage("AllNodes").toString();
                                       
                                       //MulticastSocket s = new MulticastSocket(SJServiceRegistry.getMessageTransmissionPort(SJServiceRegistry.getMessage("BroadcastDiscoveryMessage")));
                                       
                                       MulticastSocket s = new MulticastSocket(177);
                                       
                                       //s.setLoopbackMode(true);
                                       
                                       
                                       ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                                       
                                       //DatagramSocket s = new DatagramSocket(SJServiceRegistry.getMessageTransmissionPort(SJServiceRegistry.getMessage("BroadcastDiscoveryMessage")));
                                      
                                       //DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, Integer.parseInt(str[1]));
                                       
                                       ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                                     //out.writeObject(SJServiceRegistry.obtainInternalRegistry().toString());
                                       out.writeObject(message); //put service description to be sent to remote devices
                                       out.flush();
                                       if (infoDebug ==1 ) System.out.println("BroadcastMessage send stage 4");
                                       msg = byteStream.toByteArray();
                                       out.close();
                                       if (infoDebug ==1 ) System.out.println("BroadcastMessage send stage 5");
                                       
                                       DatagramPacket hi = new DatagramPacket(msg, msg.length, ipAddress, 177);
                                       if (infoDebug ==1 ) System.out.println("Sending AdvMessage");
                                       s.send(hi);
                                       if (Debug ==1 )System.out.println("AdvMessage has been sent!");
                                       s.close();
                                       
                                          
                               }
                               catch (java.net.SocketTimeoutException e)
                               {
                                       System.out.println("Timeout when connecting to ip: " + ipAddr + " port :" + 177);
                               }
                               catch (Exception e)
                               {
                                       System.out.println("Problem when connecting to ip: " + ipAddr + " port :" + 177);
                                       e.printStackTrace();
                               }
        
    }
    
    
    private boolean SendReqAdvMsg(String ipAddr, String message){
        
        int infoDebug=0;
        
        try
                                   {
                                       
                                       InetAddress ipAddress = InetAddress.getByName(ipAddr);
                                       
                                       if (infoDebug ==1 ) System.out.println("BroadcastMessage send stage 1");
                                       byte[] msg = new byte[8096];
                                      //ipAddress=InetAddress.getByName("192.168.1.255"); //assumed broadcast address
                                      //ipAddress = InetAddress.getByName(super.buffer[1].toString());
                                       
                                       if (infoDebug ==1 ) System.out.println("BroadcastMessage send stage 2");
                                       
                                       
                                       if (infoDebug ==1 ) System.out.println("BroadcastMessage send stage 3");
                                               //SJServiceRegistry.ConstructBroadcastDiscoveryMessage("AllNodes").toString();
                                       
                                       //MulticastSocket s = new MulticastSocket(SJServiceRegistry.getMessageTransmissionPort(SJServiceRegistry.getMessage("BroadcastDiscoveryMessage")));
                                       
                                       MulticastSocket s = new MulticastSocket(177);
                                       
                                       //s.setLoopbackMode(true);
                                       
                                       ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                                       
                                       //DatagramSocket s = new DatagramSocket(SJServiceRegistry.getMessageTransmissionPort(SJServiceRegistry.getMessage("BroadcastDiscoveryMessage")));
                                      
                                       //DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, Integer.parseInt(str[1]));
                                       
                                       ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                                     //out.writeObject(SJServiceRegistry.obtainInternalRegistry().toString());
                                       out.writeObject(message); //put service description to be sent to remote devices
                                       out.flush();
                                       if (infoDebug ==1 ) System.out.println("BroadcastMessage send stage 4");
                                       msg = byteStream.toByteArray();
                                       out.close();
                                       if (infoDebug ==1 ) System.out.println("BroadcastMessage send stage 5");
                                       
                                       DatagramPacket hi = new DatagramPacket(msg, msg.length, ipAddress, 177);
                                       if (infoDebug ==1 ) System.out.println("Sending BroadcastDiscoveryMessage");
                                       s.send(hi);
                                       if (infoDebug ==1 )System.out.println("data has been sent!");
                                       s.close();
                                       return true;
                                           
                               }
                               catch (java.net.SocketTimeoutException e)
                               {
                                       System.out.println("Timeout when connecting to ip: " + ipAddr + " port :" + 77);
                                       return false;
                               }
                               catch (java.net.UnknownHostException ex){
                                   System.out.println(" ip: " + ipAddr + " port :" + 77 + "Unavailable");
                                   return false;
                               }
        
                               catch (Exception e)
                               {
                                       System.out.println("Problem when connecting to ip: " + ipAddr + " port :" + 77);
                                       e.printStackTrace();
                                       return false;
                               }
        
    }
    
    private void SendDiscReplyMsg(String message){
        
        //System.out.println("RegMessageSenderThread, SendDiscReplyMsg input message: "+message );
        
        int infoDebug=0;
        JSONObject js2 = new JSONObject();
        String Addr=null;
        
        try {
            js2 = new JSONObject(new JSONTokener(message));
            Addr = js2.getString("destinationAddress");
            //js2.remove("destinationAddress");
            //js2.remove("destinationAddress");
        } catch (JSONException ex) {
            
            ex.printStackTrace();
        }
        
        
        try
                                {
                                    
                                    byte[] msg = new byte[8096];
                                   //ipAddress=InetAddress.getByName("192.168.1.255"); //assumed broadcast address
                                   //ipAddress = InetAddress.getByName(super.buffer[1].toString());
                                    //ipAddress = getBroadcastAddress();
                                    
                                    //JSONObject js = new JSONObject(new JSONTokener(message));
                                    
                                    InetAddress ipAddress = InetAddress.getByName(Addr);
                                    

                                    MulticastSocket s = new MulticastSocket(198);
                                    
                                    //s.setLoopbackMode(true);
                                    
                                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                                    ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                                  //out.writeObject(SJServiceRegistry.obtainInternalRegistry().toString());
                                    out.writeObject(message); //put service description to be sent to remote devices
                                    out.flush();
                                    msg = byteStream.toByteArray();
                                    out.close();
                                    
                                    //DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, Integer.parseInt(str[1]));
                                    DatagramPacket hi = new DatagramPacket(msg, msg.length, ipAddress, 198);
                                    if (infoDebug ==1 ) System.out.println("Sending data...");
                                    s.send(hi);
                                    System.out.println("MsgSenderThread,ServDiscReply msg sent: " +message);
                                    if (infoDebug ==1 ) System.out.println("data has been sent!");
                                    s.close();
                            }
                            catch (java.net.SocketTimeoutException e)
                            {
                                    System.out.println("Timeout when connecting to ip: "  +Addr);
                            }
                            catch (Exception e)
                            {
                                    System.out.println("Problem when connecting to ip: " +Addr);
                                    e.printStackTrace();
                            }
    }
    
    private Hashtable CheckAlmostExpiredAdv(){
        
        
                        Hashtable answer = SJServiceRegistry.checkServiceExpiryForAdvertiseRequest();

                        if (answer.isEmpty()){
                            return new Hashtable();
                        } else {
                            
                            return answer;
                            //list[0]=Boolean.TRUE;
                            //String message2 = sjdisc.ConstructRequestAdvertisementMessage(answer);
                            //list[1]=message2;
                        }
                     
    }
    
    /*
    private Vector getAlmostExpiredAdvServNames(){
        
        Vector allNames = new Vector();
        
        try {
            String answer = SJServiceRegistry.checkServiceExpiryForAdvertiseRequest();
            JSONObject jsCurrReg = SJServiceRegistry.obtainCurrentRegistry();
            
            JSONObject almostExpNode = jsCurrReg.getJSONObject(answer);
            
            Enumeration allServInd = almostExpNode.keys();
            
            while (allServInd.hasMoreElements()){
                
                String oneServInd = allServInd.nextElement().toString();
                
                JSONObject oneServ = almostExpNode.getJSONObject(oneServInd);
                
                String servName = oneServ.getString("serviceName");
                
                allNames.addElement(servName);
                
            }
            
        } catch (JSONException ex) {
            Logger.getLogger(MessageSenderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
                        
        return allNames;             
                     
    }
    */
    
    private boolean CheckOwnAdv(){
        
        int debug=0;
        
        boolean stat;
        
        long time = System.currentTimeMillis()-SJServiceRegistry.getRecordedAdvertisementTimeStamp();
        
        if (time>=0.5*Long.parseLong(SOABuffer.getSOSJRegExpiryTime())){
                    //list[0] = Boolean.TRUE;
                    stat=true;
                    if (debug==1) System.out.println("AdvertisementChecker: Advertise again");
                   // list[1] = "";
        } else {
                   // list[0]=Boolean.FALSE;
                    if (debug==1) System.out.println("AdvertisementChecker: Advertisement Not yet expired, time:" +time );
                   // list[1] = "";
                    stat=false;
         }
       
            /*
            if (SJServiceRegistry.getParsingStatus()){
                
                if (SJServiceRegistry.HasServiceConsumerOrProvider()){
                    
                    long time = System.currentTimeMillis()-SJServiceRegistry.getRecordedAdvertisementTimeStamp();
                
                if (time>=0.5*SJServiceRegistry.getOwnAdvertisementTimeLimit()){
                    //list[0] = Boolean.TRUE;
                    stat=true;
                    if (debug==1) System.out.println("AdvertisementChecker: Advertise again");
                   // list[1] = "";
                } else {
                   // list[0]=Boolean.FALSE;
                    if (debug==1) System.out.println("AdvertisementChecker: Advertisement Not yet expired, time:" +time );
                   // list[1] = "";
                    stat=false;
                }
                    
                    
                } else {
                    stat=false;
                   
                }
                
                
                
            } else {
                //list[0]=Boolean.FALSE;
               // list[1] ="";
                stat=false;
            }
            */
            return stat;
            
    }
    
    /*
    private InetAddress getBroadcastAddress(String Addr){
            
        InetAddress broadcastAddr = null;
             try {
            // TODO code application logic here
                    Enumeration<NetworkInterface> interfaces =
                    NetworkInterface.getNetworkInterfaces();
                    while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = interfaces.nextElement();
                    if (networkInterface.isLoopback())
                        continue;    // Don't want to broadcast to the loopback interface
                        for (InterfaceAddress interfaceAddress :
                            networkInterface.getInterfaceAddresses()) {
                            String addr = interfaceAddress.getAddress().getHostAddress();
                            
                            if(addr.equals(Addr)){
                                 InetAddress broadcast = interfaceAddress.getBroadcast();
                                 
                                  if (broadcast == null) {
                                    continue;
                                  } else {
                                      broadcastAddr = broadcast;
                                  }
                                 
                            }
                            
                           
                            
                            
                          //  if (broadcast == null) {
                         //           continue;
                          //      }
                          //  if (broadcast.toString().contains("192.168.1")) {
                          //          broadcastAddr = broadcast;
                         //       }
                           
                        // Use the address
                         }
                     }
               } catch (SocketException ex) {
                System.out.println("Cannot find address: " +ex.getMessage());
                
            }
             return broadcastAddr;
        }
    */
    
    private String getBroadcastAddress(String GatewayAddr, String SubnetAddr){
        
       String[] gtwyaddrSplitted = GatewayAddr.split("\\.");
        String[] subnetmaskSplitted = SubnetAddr.split("\\.");
        
        String[] broadcastaddrString = new String[4];
        String [] flippedsubnetmaskString = new String[4];
        int[] flippedsubnetmasInt = new int[4];
        
        
        String broadcastAddr="";
        
        for(int i=0;i<gtwyaddrSplitted.length;i++){
            
            int[] subnetaddrint = new int[gtwyaddrSplitted.length];
            int[] broadcastaddrint = new int[subnetmaskSplitted.length];
            
            int[] gtwyint = new int[gtwyaddrSplitted.length];
            int[] sbnetmaskint = new int[subnetmaskSplitted.length];
            
            gtwyint[i] = Integer.parseInt(gtwyaddrSplitted[i]);
            sbnetmaskint[i]= Integer.parseInt(subnetmaskSplitted[i]);
            subnetmaskSplitted[i] = String.format("%8s", Integer.toString(sbnetmaskint[i], 2)).replace(' ', '0');
            
            //System.out.println("gtwyint i: " +gtwyint[i]);
            //System.out.println("sbnetmaskint i: " +sbnetmaskint[i]);
            
            subnetaddrint[i] = gtwyint[i] & sbnetmaskint[i];
            
            flippedsubnetmaskString[i] = subnetmaskSplitted[i].replaceAll("0", "x").replaceAll("1", "0").replaceAll("x", "1");
            flippedsubnetmasInt[i] = Integer.parseInt(flippedsubnetmaskString[i],2);
            //broadcastaddrint[i] = subnetaddrint[i] | ~sbnetmaskint[i];
            broadcastaddrint[i] = subnetaddrint[i] | flippedsubnetmasInt[i];
            
            
            //System.out.println("inverted sbnetmaskint i: " +~sbnetmaskint[i]);
            //System.out.println("inverted sbnetmaskint i: " +flippedsubnetmasInt[i]);
            
            //System.out.println("subnetaddrint i: " +subnetaddrint[i]);
            //System.out.println("broadcastaddrintt i: " +broadcastaddrint[i]);
            
            broadcastaddrString[i] = Integer.toString(broadcastaddrint[i]);
            
        }
        
        broadcastAddr = broadcastaddrString[0]+"."+broadcastaddrString[1]+"."+broadcastaddrString[2]+"."+broadcastaddrString[3];
        
        //System.out.println("broadcastAddr: " +broadcastAddr);
        
        return broadcastAddr;
        
    }
    
    private boolean CheckMigrationRecPortFree(){
        
        boolean stat = false;
        
        ServerSocket ss = null;
               
         //   if (ss==null || !ss.isBound()){
                try {
                    ss = new ServerSocket(8888, 50, getLocalHostLANAddress());
                    
                    stat=true;
                    
                    ss.close();
                    
                } catch (BindException bex) {
                    System.out.println("Port is already bound");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
           // } 
            
            return stat;
        
    }
    
    private InetAddress getLocalHostLANAddress() throws UnknownHostException {
    try {
        InetAddress candidateAddress = null;
        // Iterate all NICs (network interface cards)...
        for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
            // Iterate all IP addresses assigned to each card...
            for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                if (!inetAddr.isLoopbackAddress()) {

                    if (inetAddr.isSiteLocalAddress()) {
                        // Found non-loopback site-local address. Return it immediately...
                        if (!inetAddr.getHostAddress().equalsIgnoreCase("192.168.7.2")){
                            return inetAddr;
                        } 
                       
                    }
                    else if (candidateAddress == null) {
                        // Found non-loopback address, but not necessarily site-local.
                        // Store it as a candidate to be returned if site-local address is not subsequently found...
                        candidateAddress = inetAddr;
                        // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                        // only the first. For subsequent iterations, candidate will be non-null.
                    }
                }
            }
        }
        if (candidateAddress != null) {
            // We did not find a site-local address, but we found some other non-loopback address.
            // Server might have a non-site-local address assigned to its NIC (or it might be running
            // IPv6 which deprecates the "site-local" concept).
            // Return this non-loopback candidate address...
            return candidateAddress;
        }
        // At this point, we did not find a non-loopback address.
        // Fall back to returning whatever InetAddress.getLocalHost() returns...
        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
        if (jdkSuppliedAddress == null) {
            throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
        }
        return jdkSuppliedAddress;
    }
    catch (Exception e) {
        UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
        unknownHostException.initCause(e);
        throw unknownHostException;
    }
}
    
 
}  
 


