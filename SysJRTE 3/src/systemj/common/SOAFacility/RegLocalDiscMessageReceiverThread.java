/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systemj.common.SOAFacility;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;
import systemj.common.CDLCBuffer;
import systemj.common.IMBuffer;
import systemj.common.InterfaceManager;
import systemj.common.SJSOAMessage;
import systemj.common.SJSSCDSignalChannelMap;
import systemj.common.SJServiceRegistry;
import systemj.common.SOAFacility.Support.NetworkConnCheckSimple;
import systemj.common.SOAFacility.Support.SOABuffer;

/**
 *
 * @author Udayanto
 */
public class RegLocalDiscMessageReceiverThread implements Runnable{

    MulticastSocket socket = null;
    
    @Override
    public void run() {
        
        NetworkConnCheckSimple netcheck = new NetworkConnCheckSimple();
        
        System.out.println("RegMessageReceiver thread started");
        
        SJSOAMessage sjsoa = new SJSOAMessage();
        
        while(true){
            
          //  if (SJServiceRegistry.getParsingStatus()){

            //System.out.println("MessageReceiver thread executed");
            
                String connStat = netcheck.CheckNetworkConn(SOABuffer.getGatewayAddr(), 1800);
                
                //System.out.println("MessageReceiver, ConnectionStat: " +connStat);
                
                if (connStat.equalsIgnoreCase("Connected")){
                    
                    //JSONObject jsMsg = ReceiveSOAMsg();
                
                    ReceiveSOAMsg();
                    
                    //if (!jsMsg.isEmpty()) {
                    
                        
                        
                        /*
                    if (msgType.equalsIgnoreCase("discovery")){
                    
                    //put the disc msg to the suitable buffer for accordingly response by the sender thread
                    
                        //this return all service registered in the registry
                            System.out.println("MsgReceiverThread, received discovery message");
                            JSONObject jsDiscRep = new JSONObject();
                    
                        try {
                        
                            jsDiscRep.put("MsgType", "discovery");
                            jsDiscRep.put("sourceAddress", jsMsg.getString("sourceAddress"));
                            //jsDiscRep.put("expServiceType", jsMsg.getJSONObject("expServiceType"));
                        } catch (JSONException ex) {
                            System.out.println("Exception MessageReceiverThread : " +ex.getMessage());
                        }
                    
                            SOABuffer.putDiscMsgToDiscBuffer(jsDiscRep);
                          
                    } 
                    */
                    /*
                    else if (msgType.equalsIgnoreCase("discReply")){
                    
                        System.out.println("MessageReceiver, discReply message received");
                        
                        SJServiceRegistry.SaveJoiningNodesServicesInfoOfSOAMsg(jsMsg);
                        
                            

                    }
                    */
                    
                   
                  //}
                    
                } else {
                    
                    if(socket!=null){
                        socket.close();
                    }
                    
                    
                }
             
        }
        
    }
    
    private void ReceiveSOAMsg(){
        
        //MulticastSocket socket = null;
            
        JSONObject js = new JSONObject();
        
            int debug = 1;
		int infoDebug = 0;
                
                            try
                            {

                                //while(true){

                                    //byte data[];
                                    
                                    
                                    
                                    //MulticastSocket socket = new MulticastSocket(199);
                                    socket = new MulticastSocket(199);
                                    socket.joinGroup(InetAddress.getByName("224.0.0.100"));
                                        //if (socket==null ) {
                                        //socket.setLoopbackMode(true);
                                       // }
                                        if (infoDebug==1) System.out.println("wait for message");
                                        DatagramPacket pack; //= new DatagramPacket(packet, packet.length);
                                        
                                        //System.out.println("Own IP: "+SJServiceRegistry.getOwnIPAddressFromRegistry());
                                        while (true){
                                            
                                            byte data[];
                                            
                                            byte packet[] = new byte[8096];
                                            
                                            pack = new DatagramPacket(packet, packet.length);
                                           
                                            socket.receive(pack);
                                            
                                           // if (!pack.getAddress().getHostAddress().equalsIgnoreCase(SJServiceRegistry.getOwnIPAddressFromRegistry())){
                                            //    break;
                                            //}
                                            
                                        
                                        
                                        
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
                                                                             if (infoDebug==1) System.out.println("received info in SOA receiver: " +mybuffer.toString().trim()+"\n");
                                                                              
                                                                              js = new JSONObject(new JSONTokener(mybuffer.toString().trim()));
                                                                              
                                                                              
                                                                              //String msgType = getRcvdMsgType(js);
                        
                                                                                System.out.println("RegLocalDiscMessageReceiver, RcvdMsg: " +js);

                                                                               if(js.getString("regID").equals(SOABuffer.getSOSJRegID()))
                                                                               {
                                                                                  SOABuffer.putDiscMsgToDiscBuffer(js);
                                                                               } else {
                                                                                                
                                                                                                if(!pack.getAddress().getHostName().equalsIgnoreCase("224.0.0.100")){

                                                                                                             Thread pcktbounce = new Thread(new SOAPacketBouncer(socket, mybuffer.toString().trim()));
                                                                                                             pcktbounce.start();

                                                                                                } 
                                                                                   
                                                                                                //msg received by the wrong SS in the same machine, bouncing the message to the other local SS in the same machine.
                                                                                                
                                                                                               
                                                                                               
                                                                                            }
                                                                              
                                                                                  //break;
                                                                              
                                                                              
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
                                        //socket.close();
                                //} // end of main while loop
                        }
                        catch (SocketException se)
                        {
                                se.printStackTrace();
                                SOABuffer.setIsInitAdvDone(false);
                                SOABuffer.setIsInitDiscDone(false);
                        }
                        catch (Exception e)
                        {
                                e.printStackTrace();
                                SOABuffer.setIsInitAdvDone(false);
                                SOABuffer.setIsInitDiscDone(false);
                        }
                            
                        //return js;
        
    }
    
    
    private String getRcvdMsgType(JSONObject jsMsg){
        
        int debug=0;
        
        String msgType=null;
        
        try {
            msgType = jsMsg.getString("MsgType");
                                                                                      
                                                                
        } catch (JSONException ex) {
           ex.printStackTrace();
        }
        
        return msgType;
        
    }
    
     
    
    
    
}
