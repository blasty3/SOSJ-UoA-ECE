/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commthread;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;

/**
 *
 * @author Atmojo
 */
public class CommandSenderThread implements Runnable {

    String ipAddr;
    JSONObject jsMessage;
    //JSONObject jsCDMap;
    //JSONObject jsServDesc;
    
    public CommandSenderThread(String ipAddr, JSONObject jsMessage){
        this.ipAddr = ipAddr;
        this.jsMessage = jsMessage;
    }
    
    
    public void run() {
        
        JSONObject jsMsg = jsMessage;
        
        try {
            String cmdType = jsMsg.getString("MsgType");
            
            if(cmdType.equalsIgnoreCase("CreateCD")){
                String resp = transceiveCommand(ipAddr, jsMsg.toString());
                
                if(resp.equalsIgnoreCase("OK")){
                    
                    JSONObject jsCDMap = jsMsg.getJSONObject("CDMap");
                    JSONObject jsServDesc = jsMsg.getJSONObject("CDServDesc");
                    
                    CodeSenderThread codthr = new CodeSenderThread(ipAddr, jsCDMap, jsServDesc);
                    codthr.run();
                }
                
            } else {
                sendControlMessage(ipAddr, jsMsg.toString());
            }
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        
    }
    
    public String transceiveCommand(String ipAddr, String message)
    {
        String answer = "";
        //synchronized (lock)
        //{
            int debug=0;
            int infoDebug=1;
            
                MulticastSocket s = null;
            //MulticastSocket s2 = null;
                    try
                    {
                        InetAddress ipAddress = InetAddress.getByName(ipAddr);
                        byte[] msg = new byte[1024];
                        byte packet[] = new byte[8096];
                        //MulticastSocket s = new MulticastSocket(controlPort);
                        s = new MulticastSocket(212);
                        //s2 = new MulticastSocket(responsePort);
                        //InetAddress ipAddress = SJServiceRegistry.getServicesIPLocationOfType(serviceType);
                        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                        out.writeObject(message);
                        out.flush();
                        msg = byteStream.toByteArray();
                        out.close();
                        
                        //DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, Integer.parseInt(str[1]));
                        //DatagramPacket req = new DatagramPacket(msg, msg.length, ipAddress,port);
                        DatagramPacket req = new DatagramPacket(msg, msg.length, ipAddress,212);
                        if (infoDebug==1) System.out.println("TrncvReqMessage sending data to IP " +ipAddr+"port:" +212);
                        s.send(req);
                        if (infoDebug==1)System.out.println("data has been sent! Now wait for response");
                        
                        //DatagramPacket resp = new DatagramPacket(packet, packet.length,ipAddress,responsePort);
                        DatagramPacket resp = new DatagramPacket(packet, packet.length);
                        
                        s.setSoTimeout(3000);
                        
                        //do {
                        
                        s.receive(resp);
                        
                       // } while (resp.getAddress().getHostAddress().equalsIgnoreCase(SJServiceRegistry.getOwnIPAddressFromRegistry()));
                        
                        byte[] data;
                        if(infoDebug == 1) System.out.println("TrncvReqMessage rcvd msg length = " + resp.getLength() + ", from " + resp.getSocketAddress()+ "port" +resp.getPort());
                        data = new byte[resp.getLength()];
                                                
                        System.arraycopy(packet, 0, data, 0, resp.getLength());

                                                // time to decode make use of data[]
                        //Object[] list = new Object[2];
                         //list[0] = Boolean.TRUE;        
                          if(data.length > 0)
                          {
                             if(((int)data[0] == -84) && ((int)data[1] == -19))
                             {
                                try
                                {
                                     if(debug == 1) System.out.println("Java built-in deserializer is used");
                                     ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                                     Object mybuffer = ois.readObject();              
                                     if(debug == 1) System.out.println(mybuffer);
                                     if(debug == 1) System.out.println((mybuffer.getClass()).getName());
                                    
                                        if(debug == 1) System.out.println("Direct assign the received byffer to the value 3");
                                     
                                        if (debug==1)  System.out.println("ControlMessageTransceive Message: " +mybuffer.toString().trim()+"\n");
                                        answer = mybuffer.toString().trim();
                                        
                                       

                                 }
                                 catch(Exception e)
                                 {
                                      e.printStackTrace();
                                 }
                              }
                              
                                   }
                                   
                                   
                                   s.close();
                                   //s2.close();

                    }
                    catch (java.net.SocketTimeoutException e)
                    {
                        //*this cant be used to declare whether service is unreachable
			System.out.println("ControlMessageTimeout,transceiveRequestMessageNoServiceTypeShortTimeout: " +e.getMessage());
                        s.close();
                        
                    }catch (java.net.BindException bex){
                        System.out.println("ControlMessage: Address cannot bound ");
                       bex.printStackTrace();
                        s.close();
                    }
                    catch (Exception e)
                    {
			System.out.println("ControlMessage: Problem when connecting : "+ e.getMessage());
			e.printStackTrace();
                        s.close();
                    }
                
                   return answer;
                    
      //  }

    }
    
    private void sendControlMessage(String ipAddr, String message){
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
    
}
