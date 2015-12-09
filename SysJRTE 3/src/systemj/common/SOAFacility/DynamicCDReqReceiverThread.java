/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package systemj.common.SOAFacility;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;
import static sysj.SJ.ParseCDMap;
import static sysj.SJ.ParseServDesc;
import systemj.common.CDLCBuffer;
import systemj.common.SJSSCDSignalChannelMap;
import systemj.common.SJServiceRegistry;
import systemj.common.SOAFacility.Support.SOABuffer;

/**
 *
 * @author Udayanto
 */
public class DynamicCDReqReceiverThread implements Runnable{

    @Override
    public void run() {
        
        ReceiveReqMsg();
    }
    
    private void ReceiveReqMsg(){
        
        //MulticastSocket socket = null;
            
        JSONObject js = new JSONObject();
        
            int debug = 1;
		int infoDebug = 0;
                
                            try
                            {

                                //while(true){

                                    //byte packet[] = new byte[8096];
                                    
                                    MulticastSocket socket = new MulticastSocket(212);
                                    //socket.joinGroup(InetAddress.getByName("224.0.0.100"));
                                    socket.joinGroup(InetAddress.getByName("224.0.0.100"));
                                    //socket.joinGroup(InetAddress.getByName("127.0.0.1"));
                                        //if (socket==null ) {
                                        //socket.setLoopbackMode(true);
                                       // }
                                        if (debug==1) System.out.println("DynamicCDReqReceiverThread waits for message");
                                        DatagramPacket pack;//= new DatagramPacket(packet, packet.length);
                                        
                                        //System.out.println("Own IP: "+SJServiceRegistry.getOwnIPAddressFromRegistry());
                                        while (true){
                                            
                                            byte data[];
                                            
                                            byte packet[] = new byte[8096];
                                            
                                            pack = new DatagramPacket(packet, packet.length);
                                           
                                            socket.receive(pack);
                                            
                                            System.out.println("DynamicCDReqReceiverThread receive packet from: "+pack.getAddress().getHostAddress());
                                           // if (!pack.getAddress().getHostAddress().equalsIgnoreCase(SJServiceRegistry.getOwnIPAddressFromRegistry())){
                                            //    break;
                                            //}
                                            String msgOrigin = pack.getAddress().getHostAddress();
                                        // packet[] is received here
                                        
                                        
                                        
                                        //need to ignore message coming from its own
                                        
                                   //    if (!pack.getAddress().toString().contains(SJServiceRegistry.getOwnIPAddressFromRegistry())){
                                            
                                                if(infoDebug == 1) System.out.println("SOA MessageReceiverThread received pack length = " + pack.getLength() + ", from " + pack.getSocketAddress()+ "port" +pack.getPort());
                                                data = new byte[pack.getLength()];
                                                
                                                //String localSSName = SJSSCDSignalChannelMap.getLocalSSName();
                                                
                                                System.arraycopy(packet, 0, data, 0, pack.getLength());
     
                                                if(data.length > 0)
                                                {
                                                        if(((int)data[0] == -84) && ((int)data[1] == -19))
                                                        {
                                                                try
                                                                {
                                                                        if(infoDebug == 1) System.out.println("Java built-in deserializer is used");
                                                                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                                                                        Object mybuffer = ois.readObject();              
                                                                        if(infoDebug == 1) System.out.println(mybuffer);
                                                                        if(infoDebug == 1) System.out.println((mybuffer.getClass()).getName());
                                                                        if((mybuffer.getClass()).getName().compareTo("[Ljava.lang.Object;") == 0)
                                                                        {
                                                                                Object mybufferArray[] = (Object[])mybuffer;
                                                                                // System.out.println(mybufferArray.length);	
                                                                        }
                                                                        else
                                                                        {
                                                                           if(infoDebug == 1) System.out.println("Direct assign the received byffer to the value 3");


                                                                            //expected format in JSON String
                                                                             if (debug==1) System.out.println("DynamicCDReqReceiverThread receives message: " +mybuffer.toString().trim()+"\n");
                                                                              
                                                                              js = new JSONObject(new JSONTokener(mybuffer.toString().trim()));
                                                                              
                                                                              String msgType = getMsgType(js);
                                                                              
                                                                              String SSName = getTargetSSName(js);
                                                                              
                                                                              String DevelAddr = js.getString("DevelAddr");
                                                                              
                                                                              if (SSName.equals(SJSSCDSignalChannelMap.getLocalSSName())){
                                                                                  
                                                                                  String CDName = js.getString("CDName");
                                                                              
                                                                               if (msgType.equalsIgnoreCase("CreateCD")){
                                                                                  
                                                                                   //String filenameCDMapXML = js.getString("XMLConfigFileName");
                                                                                   
                                                                                   //if(js.has("CDServDesc")){
                                                                                       
                                                                                        //String filenameCDServDescXML = js.getString("XMLServDescFileName");
                                                                                   
                                                                                        //JSONObject CDServDesc = ParseServDesc(filenameCDServDescXML);
                                                                                       JSONObject CDMap = js.getJSONObject("CDMap");
                                                                                       JSONObject CDServDesc = js.getJSONObject("CDServDesc");
                                                                                       
                                                                                        //CDLCBuffer.putUpdateServiceDescription(CDServDesc);
                                                                                       
                                                                                       if(!CDLCBuffer.GetDevelThreadBusyFlag()){
                                                                                           
                                                                                           
                                                                                           if(DevelAddr.equalsIgnoreCase(SJServiceRegistry.getLocalSSAddr())){
                                                                                               sendCreateCDReplyMessage("224.0.0.100", "OK");
                                                                                           } else {
                                                                                               sendCreateCDReplyMessage(DevelAddr, "OK");
                                                                                           }
                                                                                           
                                                                                           
                                                                                    //}
                                                                                   
                                                                                            CDLCBuffer.SetDevelThreadBusyFlag(true);

                                                                                            Thread dcdcoderec = new Thread(new DevelCDCodeTransferReceiverThread(CDName, CDMap, CDServDesc));
                                                                                            dcdcoderec.start();
                                                                                           
                                                                                       } else {
                                                                                            if(DevelAddr.equalsIgnoreCase(SJServiceRegistry.getLocalSSAddr())){
                                                                                               sendCreateCDReplyMessage("224.0.0.100", "NOT OK");
                                                                                           } else {
                                                                                               sendCreateCDReplyMessage(DevelAddr, " NOT OK");
                                                                                           }
                                                                                       }
                                                                                       
                                                                                   //}
                                                                                   
                                                                                  // JSONObject CDMap = ParseCDMap(filenameCDMapXML);
                                                                                    
                                                                                   
                                                                                   
                                                                                    //System.out.println("parsed CDMap: " +CDMap.toString());
                                                                                    //Vector vec = new Vector();
                                                                                    //vec.addElement(CDName);

                                                                                    //CDLCBuffer.TransferRequestCreateCDToBuffer(CDName);
                                                                                    //CDLCBuffer.AddTempSigChanMapCD(CDMap);
                                                                                    
                                                                                   
                    
                                                                                } else if (msgType.equalsIgnoreCase("SuspendCD")){

                                                                                   // Vector vec = new Vector();
                                                                                    //vec.addElement(CDName);
                                                                                    CDLCBuffer.TransferRequestHibernateCDToBuffer(CDName);

                                                                                } else if (msgType.equalsIgnoreCase("WakeUpCD")){

                                                                                    //Vector vec = new Vector();
                                                                                    //vec.addElement(CDName);
                                                                                    CDLCBuffer.TransferRequestWakeUpCDToBuffer(CDName);

                                                                                } else if (msgType.equalsIgnoreCase("MigrateCD")){

                                                                                    String MigType = js.getString("MigType");
                                                                                    
                                                                                    String DestinationSS = js.getString("DestSS");
                                                                                    
                                                                                    if(MigType.equals("strong") || MigType.equals("weak")){
           
                                                                                        //String fileNameCDMap = js.getString("XMLConfigFileName");
                                                                                        
                                                                                        //String fileNameCDServDesc = js.getString("XMLServDescFileName");
                                                                                        
                                                                                        //JSONObject CDMap = ParseCDMap(fileNameCDMap);
                                                                                        //JSONObject CDServDesc = ParseServDesc(fileNameCDServDesc);

                                                                                        JSONObject CDMap = js.getJSONObject("CDMap");
                                                                                        JSONObject CDServDesc = js.getJSONObject("CDServDesc");
                                                                                        
                                                                                        //CDLCBuffer.AddTempMigrateSignalChannelMap(CDMap);
                                                                                        //CDLCBuffer.AddMigrateServiceDescription(CDServDesc);

                                                                                        CDLCBuffer.AddMigratingCDNameToBuffer(CDName);

                                                                                        CDLCBuffer.AddRequestMigrate(CDName, DestinationSS, CDMap, CDServDesc,MigType);

                                                                                    //sjsoamsg.ConstructReqStrongMigrationMessage(DestinationSS, SJSSCDSignalChannelMap.getLocalSSName());

                                                                                    } else {

                                                                                        throw new RuntimeException("Unknown migration type, choose either 'weak' or 'strong'");
                                                                                    }


                                                                                } else if(msgType.equalsIgnoreCase("KillCD")){
                                                                                    
                                                                                    Vector vec = new Vector();
                                                                                    vec.addElement(CDName);

                                                                                    CDLCBuffer.TransferRequestKillCDToBuffer(vec);
                                                                                    
                                                                                } else if(msgType.equalsIgnoreCase("ReconfigChan")){
                                                                                    String ChanName = js.getString("ChanName");
                                                                                    String ChanDir = js.getString("ChanDir");
                                                                                    String PartnerChanName = js.getString("PartnerChanName");
                                                                                    String PartnerChanCDName = js.getString("PartnerChanCDName");
                                           
                                                                                    CDLCBuffer.AddChanReconfig(CDName,ChanName,ChanDir, PartnerChanCDName, PartnerChanName);
                                                                                              
                                                                                }
                                                                                  
                                                                              } else {
                                                                                  
                                                                                  //if this message is received by different SS, then bounce, resend the message local multicast to the rest of the SS in the same machine
                                                                                  //ignore own message
                                                                                  if(!msgOrigin.equalsIgnoreCase(SJServiceRegistry.getLocalSSAddr())){
                                                                                      sendDevelMessageToLocal(mybuffer.toString());
                                                                                  }
                                                                                  
                                                                              }
                                                                              
                                                                              
                                                                              
                                                                              
                                                                              /*
                                                                              if(SJSSCDSignalChannelMap.hasLocalSSName()){
                                                                                  if(js.has("sourceSS")){
                                                                                  String sourceSS = js.getString("sourceSS");
                                                                                  String localSSName = SJSSCDSignalChannelMap.getLocalSSName();
                                                                                    if(!sourceSS.equals(localSSName)){
                                                                                            break;
                                                                                        }

                                                                                    } else {
                                                                                        break;
                                                                                    }
                                                                              } else {
                                                                                  break;
                                                                              }
                                                                              */
                                                                              
                                                                              
                                                                              
                                                                              //String sourceSS = js.getString("sourceSS");
                                                                              
                                                                              //if (debug==1) System.out.println("UDPSOAReceiver, from:" +sourceSS+" Received service:" +js.toPrettyPrintedString(2, 0));
                                                                              
                                                                              //System.out.println("MessageReceiverThread, sourceSS:" +sourceSS+ "LocalSSName: " +localSSName+ " Receive msg: " +js.toPrettyPrintedString(2, 0));
                                                                              
                                                                              //if(!sourceSS.equals(localSSName)){
                                                                                  
                                                                                  //break;
                                                                              //}
                                                                              
                                                                                       //list[1]="{}";

                                                                                     //automatically service registry update --> registry of external service
                                                                                     // SJServiceRegistry.AppendNodeServicesToCurrentRegistry(jsData, false);

                                                                        }

                                                                }
                                                                catch(Exception e)
                                                                {
                                                                    //System.out.println(e.getCause());
                                                                        e.printStackTrace();
                                                                }
                                                                
                                                        }
                                                        
                                                }
                                         }
                                                
                                            
                                        
                                       
                                        // own-IP check ends here
                                        
                                             
                                                //socket.close();
                                        
                                //} // end of main while loop
                        }
                        catch (SocketException se)
                        {
                                se.printStackTrace();
                                //SOABuffer.setIsInitAdvDone(false);
                                //SOABuffer.setIsInitDiscDone(false);
                        }
                        catch (Exception e)
                        {
                                e.printStackTrace();
                                //SOABuffer.setIsInitAdvDone(false);
                                //SOABuffer.setIsInitDiscDone(false);
                        }
                            
                        //return js;
        
    }
    
    private String getMsgType(JSONObject jsMsg){
        
        int debug=0;
        
        String msgType=null;
        
        try {
            msgType = jsMsg.getString("MsgType");
                                                                                      
                                                                
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return msgType;
        
    }
    
     private String getTargetSSName(JSONObject jsMsg){
        
        int debug=0;
        
        String SSName=null;
        
        try {
            SSName = jsMsg.getString("SSName");
                                                                                      
                                                                
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return SSName;
        
    }
    
    private static JSONObject ParseCDMap(String filename){
        
        CDLCMapParser cdpars = new CDLCMapParser();
        
        JSONObject js = new JSONObject();
        try {
            js = cdpars.parse(filename);
            
            //System.out.println("Parsed New CDMap: " +js.toPrettyPrintedString(2, 0));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return js;
    }
    
    private static JSONObject ParseServDesc(String filename){
        
        ServDescParser cdsdparse = new ServDescParser();
        
        JSONObject js = new JSONObject();
        try {
            js = cdsdparse.parse(filename);
            
            //System.out.println("Parsed New SD: " +js.toPrettyPrintedString(2, 0));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return js;
        
    }
    
    private void sendCreateCDReplyMessage(String ipAddr, String message){
        try {
            byte[] msg = new byte[1024];
            InetAddress ipAddress = InetAddress.getByName(ipAddr);
            int infoDebug=1;
            //JSONObject js = new JSONObject(message);
              
               ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
               ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(byteStream));
              //out.writeObject(SJServiceRegistry.obtainInternalRegistry().toString());
               out.writeObject(message);
               //out.writeObject(SJServiceRegistry.AdvertiseNodeServices("HelloMessage").toString()); //put service description to be sent to remote devices
               out.flush();
                msg = byteStream.toByteArray();
                out.close(); 
                MulticastSocket s = new MulticastSocket(212);
                DatagramPacket hi = new DatagramPacket(msg, msg.length, ipAddress, 212);
                if (infoDebug ==1 )System.out.println("Sending control message:" +message+ "to: " +ipAddress );
                s.send(hi);
                if (infoDebug ==1 ) System.out.println("data has been sent!");
                                          
                //SJServiceRegistry.AcknowledgeHelloMessageSent(true);
                 // if (infoDebug ==1 ) System.out.println("Status acknowledge in sender:" +SJServiceRegistry.getAcknowledgeHelloMessageSent());
                                         // SJServiceRegistry.RecordAdvertisementTimeStamp();
                s.close();
        } catch (UnknownHostException hex) {
            
            System.err.println("ControlMessage, problem IOException: " +hex.getMessage());
        } catch (Exception e){
            System.err.println("ControlMessage, problem Exception: " +e.getMessage());
        }
    }
    
    public void sendDevelMessageToLocal(String message){
        try {
            byte[] msg = new byte[1024];
            InetAddress ipAddress = InetAddress.getByName("224.0.0.100");
            int infoDebug=1;
            //JSONObject js = new JSONObject(message);
              
               ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
               ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(byteStream));
              //out.writeObject(SJServiceRegistry.obtainInternalRegistry().toString());
               out.writeObject(message);
               //out.writeObject(SJServiceRegistry.AdvertiseNodeServices("HelloMessage").toString()); //put service description to be sent to remote devices
               out.flush();
                msg = byteStream.toByteArray();
                out.close(); 
                MulticastSocket s = new MulticastSocket(212);
                DatagramPacket hi = new DatagramPacket(msg, msg.length, ipAddress, 212);
                if (infoDebug ==1 )System.out.println("Sending control message:" +message+ "to: " +ipAddress );
                s.send(hi);
                if (infoDebug ==1 ) System.out.println("data has been sent!");
                                          
                //SJServiceRegistry.AcknowledgeHelloMessageSent(true);
                 // if (infoDebug ==1 ) System.out.println("Status acknowledge in sender:" +SJServiceRegistry.getAcknowledgeHelloMessageSent());
                                         // SJServiceRegistry.RecordAdvertisementTimeStamp();
                s.close();
        } catch (UnknownHostException hex) {
            
            System.err.println("ControlMessage, problem IOException: " +hex.getMessage());
        } catch (Exception e){
            System.err.println("ControlMessage, problem Exception: " +e.getMessage());
        }
    }
    
}
