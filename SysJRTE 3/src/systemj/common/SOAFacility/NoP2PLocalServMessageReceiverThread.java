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
import systemj.common.SJRegistryEntry;
import systemj.common.SJSSCDSignalChannelMap;
import systemj.common.SJServiceRegistry;
import systemj.common.SOAFacility.Support.NetworkConnCheckSimple;
import systemj.common.SOAFacility.Support.SOABuffer;

/**
 *
 * @author Udayanto
 */
public class NoP2PLocalServMessageReceiverThread implements Runnable{

    MulticastSocket socket=null;
    
    
    @Override
    public void run() {
        
        NetworkConnCheckSimple netcheck = new NetworkConnCheckSimple();
        
        System.out.println("NoP2PLocalServMessageReceiver thread started");
        
        while(true){
            
           
          //  if (SJServiceRegistry.getParsingStatus()){

            //System.out.println("MessageReceiver thread executed");
            
                String connStat = netcheck.CheckNetworkConn(SOABuffer.getGatewayAddr(), 2500);
                
                //System.out.println("MessageReceiver, ConnectionStat: " +connStat);
                
                if (connStat.equalsIgnoreCase("Connected")){
                    
                    ReceiveSOAMsg();
                
                    //if (!jsMsg.isEmpty()) {
                    
                        
                        //else if (msgType.equalsIgnoreCase("discReply")){
                    
                         //   System.out.println("MessageReceiver, discReply message received");
                        
                       //     SJServiceRegistry.SaveJoiningNodesServicesInfoOfSOAMsgNoP2P(jsMsg);
                        
                       // }
                   
                  //}
                    
                } else {
                    
                    SOABuffer.setIsInitAdvDone(false);
                    
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

                                    byte data[];
                                    
                                    byte packet[] = new byte[8096];
                                    
                                    //MulticastSocket socket = new MulticastSocket(177);
                                    
                                    socket = new MulticastSocket(177);
                                    
                                    socket.joinGroup(InetAddress.getByName("224.0.0.100"));
                                        //if (socket==null ) {
                                        //socket.setLoopbackMode(true);
                                       // }
                                        if (infoDebug==1) System.out.println("wait for message");
                                        DatagramPacket pack = new DatagramPacket(packet, packet.length);
                                        
                                        //System.out.println("Own IP: "+SJServiceRegistry.getOwnIPAddressFromRegistry());
                                        while (true){
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
                                                                              
                                                                              //String sourceSS = js.getString("sourceSS");
                                                                              
                                                                              String msgType = getRcvdMsgType(js);
                        
                                                                                System.out.println("MessageReceiver, RcvdMsgType: " +msgType);
                                                                                //System.out.println("NoP2PLocalServMsgRecThread, received: " +js);

                                                                                if(msgType.equalsIgnoreCase("regToProvRequestAdvertise")){
                                                                                    try {
                                                                                        //response with service adv

                                                                                        String destSS = js.getString("destSS");

                                                                                        if(destSS.equals(SJSSCDSignalChannelMap.getLocalSSName())){
                                                                                            SOABuffer.AddRegToProvReqAdv(js);
                                                                                        }  else {
                                                                                            
                                                                                            //bounce to the local SS that the right one sees
                                                                                            if(!pack.getAddress().getHostName().equalsIgnoreCase("224.0.0.100")){
                                                                                                
                                                                                                Thread pcktbounce = new Thread(new SOAPacketBouncer(socket, mybuffer.toString().trim()));
                                                                                                pcktbounce.start();
                                                                                                
                                                                                            }
                                                                                            
                                                                                            /*
                                                                                            byte[] msg = new byte[8096];
                                                                                                
                                                                                                InetAddress ipAddress = InetAddress.getByName("224.0.0.100");
                                                                                                
                                                                                                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                                                                                                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                                                                                              //out.writeObject(SJServiceRegistry.obtainInternalRegistry().toString());
                                                                                                out.writeObject(mybuffer.toString().trim()); //put service description to be sent to remote devices
                                                                                                out.flush();
                                                                                                msg = byteStream.toByteArray();
                                                                                                out.close();

                                                                                                //DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, Integer.parseInt(str[1]));
                                                                                                DatagramPacket hi = new DatagramPacket(msg, msg.length, ipAddress, 177);
                                                                                                if (infoDebug ==1 ) System.out.println("Sending data...");
                                                                                                socket.send(hi);
                                                                                                System.out.println("RegRemoteMessageReceiverThread, bouncing ServDiscReply to dest (local) SS, msg sent: " +mybuffer.toString().trim());
                                                                                                if (infoDebug ==1 ) System.out.println("data has been sent!");
                                                                                                */
                                                                                        }

                                                                                    } catch (JSONException ex) {
                                                                                        ex.printStackTrace();
                                                                                    }


                                                                                } else if(msgType.equalsIgnoreCase("RegReAdvertise")){

                                                                                     //System.out.println("Saving to registry entry" +jsMsg);

                                                                                    SJRegistryEntry.AddRegistryToEntry(js);
                                                                                    SJRegistryEntry.UpdateRegistryExpiry(js);


                                                                                }
                                                                              
                                                                              /*
                                                                              if(js.has("sourceSS")){
                                                                                  String sourceSS = js.getString("sourceSS");
                                                                                  
                                                                                    if(!sourceSS.equals(localSSName)){
                                                                                  
                                                                                        break;
                                                                                    }
                                                                                  
                                                                              } else {
                                                                                  break;
                                                                              }
                                                                              */
                                                                              
                                                                              //if (debug==1) System.out.println("UDPSOAReceiver, from:" +sourceSS+" Received service:" +js.toPrettyPrintedString(2, 0));
                                                                              
                                                                              //System.out.println("MessageReceiverThread, sourceSS:" +sourceSS+ "LocalSSName: " +localSSName+ " Receive msg: " +js.toPrettyPrintedString(2, 0));
                                                                              
                                                                              //if(!sourceSS.equals(localSSName)){
                                                                                  
                                                                                  
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
                                //se.printStackTrace();
                                SOABuffer.setIsInitAdvDone(false);
                                //SOABuffer.setIsInitDiscDone(false);
                        }
                        catch (Exception e)
                        {
                                e.printStackTrace();
                                SOABuffer.setIsInitAdvDone(false);
                                //SOABuffer.setIsInitDiscDone(false);
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
