/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package systemj.common.SOAFacility;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author Udayanto
 */
public class SOAPacketBouncer implements Runnable{

    private MulticastSocket socket;
    private String message;
    
    
    SOAPacketBouncer(MulticastSocket socket, String message){
        this.socket=socket;
        this.message = message;
    }
    
    @Override
    public void run() {
        
        int infoDebug=0;
        
        try{
            
            byte[] msg = new byte[8096];
                                                                                                
                                                                                                InetAddress ipAddress = InetAddress.getByName("224.0.0.100");
                                                                                                
                                                                                                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                                                                                                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                                                                                              //out.writeObject(SJServiceRegistry.obtainInternalRegistry().toString());
                                                                                                out.writeObject(message); //put service description to be sent to remote devices
                                                                                                out.flush();
                                                                                                msg = byteStream.toByteArray();
                                                                                                out.close();

                                                                                                //DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, Integer.parseInt(str[1]));
                                                                                                DatagramPacket hi = new DatagramPacket(msg, msg.length, ipAddress, 177);
                                                                                                if (infoDebug ==1 ) System.out.println("Sending data...");
                                                                                                socket.send(hi);
                                                                                                System.out.println("RegRemoteMessageReceiverThread, bouncing ServDiscReply to dest (local) SS, msg sent: " +message);
                                                                                                if (infoDebug ==1 ) System.out.println("data has been sent!");
                                                                                                
            
        } catch (Exception ex){
            ex.printStackTrace();
        }
        
         
        
    }
    
}
