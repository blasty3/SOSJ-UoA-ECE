/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systemj.signals.SOA;

import java.net.InetAddress;
import java.util.Hashtable;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import systemj.common.SJServiceRegistry;
import systemj.common.SOAFacility.Support.SOABuffer;
import systemj.interfaces.GenericSignalSender;

/**
 *
 * @author Udayanto
 */
public class SubscribeNotification extends GenericSignalSender {

    private String servName,action,targetSignalName;
    private int confirmable;
    private int timeout;
    
    @Override
    public void configure(Hashtable data) throws RuntimeException {
        
        // confirmable by default is 1, since it only requires acknowledge of request being received
        
         if (data.containsKey("confirmable")){
            confirmable = Integer.parseInt((String)data.get("confirmable")); //0 for non confirmable, 1 for short confirmable, 2 for longer confirmable--> 2 requires timeout value defined by the designer
            
            if (confirmable==1){
                if (data.containsKey("1stACKTimeout")){
                    timeout=Integer.parseInt((String)data.get("1stACKTimeout"));
                } else {
                    throw new RuntimeException("'1stACKTimeout' is needed. define in integer number, for timeout duration require to wait for longer operation to finish before resending request if the response is lost");
                }
            }
            
        } else {
            throw new RuntimeException("'confirmable' is needed. choose '0' for NON confirmable, 1 for shorter confirmable (+-1 seconds), and 2 for longer confirmable with timeout value");
        }
        
      //  if (data.containsKey("serviceType")){
      //      servType = ((String)data.get("serviceType"));
      //  } else {
      //      throw new RuntimeException("'serviceType' is needed.");
      //  }
        
        if (data.containsKey("associatedServiceName")){
            servName = (String)data.get("associatedServiceName");
        } else {
            throw new RuntimeException("'associatedServiceName' is needed.");
        }
        
        if (data.containsKey("Name")){
            targetSignalName = (String)data.get("Name");
        } else {
            throw new RuntimeException("'Name' is needed.");
        }
 
    }

     public SubscribeNotification(){
		super();
     }
    
    @Override
    public void run() {
        
        int debug=0;
        
        JSONObject jsMachineLoc = SOABuffer.getMatchingProvider(servName,targetSignalName);
        
        ControlMessageComm cntrlMsg = new ControlMessageComm();
        
        JSONObject jsData = new JSONObject();
 
        InetAddress srcIPAddress=null;
        
        //srcIPAddress = SJServiceRegistry.getSourceIPAddress();
        
        try {
                    action = jsMachineLoc.getString("action");
                    if (action!=null){
                        jsData.put("action",action);
                    }
                //jsData.put("serviceType","");
                    
                    System.out.println("SubscribeNotification, buffer length: " +super.buffer.length);
                    
                    if (super.buffer[1]!=null){
                        String value = super.buffer[1].toString();
                        jsData.put("data",value);
                    } else {
                        jsData.put("data","");
                    }
                
            if (debug==1) System.out.println("InvokeActuatorServices:" +jsData.toPrettyPrintedString(2, 0));
            } catch (JSONException ex) {
                System.err.println(ex.getMessage());
            }
        
    }
    
}
