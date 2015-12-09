/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systemj.signals.SOA;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import systemj.common.SJMessageConstants;
import systemj.common.SJRequestMessage;
import systemj.common.SJResponseMessage;
import systemj.common.SJServiceRegistry;
import systemj.common.SOAFacility.Support.SOABuffer;
import systemj.interfaces.GenericSignalSender;

/**
 *
 * @author Udayanto
 */
public class InvokeActuatorServices extends GenericSignalSender implements Serializable{

    private String servName,action,targetSignalName;
    //private int confirmable;
    //private int ACK2timeout;
    //private int shorttimeout;
   
    @Override
    public void configure(Hashtable data) throws RuntimeException {

        //put confirmable or not in the signal config??
        
        /*
        if (data.containsKey("confirmable")){
            confirmable = Integer.parseInt((String)data.get("confirmable")); //0 for non confirmable, 1 for short confirmable, 2 for longer confirmable--> 2 requires timeout value defined by the designer
            
            if (confirmable==2){
             
                if (data.containsKey("FirstACKTimeOut")){
                    shorttimeout=Integer.parseInt((String)data.get("FirstACKTimeOut"));
                } else {
                    throw new RuntimeException("'FirstACKTimeOut' is needed. define in integer number, for timeout duration require to wait for longer operation to finish before resending request if the response is lost");
                }
                
               // if (data.containsKey("longtimeout")){
               //     longtimeout=Integer.parseInt((String)data.get("longtimeout"));
               // } else {
               //     throw new RuntimeException("'longtimeout' is needed. define in integer number, for timeout duration require to wait for longer operation to finish before resending request if the response is lost");
              //  }
                
               // if (data.containsKey("mediumtimeout")){
              //      mediumtimeout=Integer.parseInt((String)data.get("mediumtimeout"));
              //  } else {
               //     throw new RuntimeException("'mediumtimeout' is needed. define in integer number, for timeout duration require to wait for longer operation to finish before resending request if the response is lost");
              //  }
                
            } else if (confirmable==1){
                if (data.containsKey("FirstACKTimeOut")){
                    shorttimeout=Integer.parseInt((String)data.get("FirstACKTimeOut"));
                } else {
                    throw new RuntimeException("'FirstACKTimeOut' is needed. define in integer number, for timeout duration required to wait for longer operation to finish before resending request if the response is lost");
                }
            }
            
        } else {
            throw new RuntimeException("'confirmable' is needed. choose '0' for NON confirmable, 1 for shorter confirmable (+-1 seconds), and 2 for longer confirmable with timeout value");
        }
        */
        
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
        
        //if (data.containsKey("actionName")){
        //    action = (String)data.get("actionName");
       // } else if (data.containsKey("actionType")) {
       //     actionType = (String)data.get("actionType");
       // } else {
       //      throw new RuntimeException("either 'actionName' or 'actionType' attribute is needed. 'actionName' if the method name is known, otherwise use a particular 'agreed' keywords to invoke specific action");
       // }
        
    }

    @Override
    public void run() {
        
        int reqPort=0;
            int respPort=0;
        
        int debug=0;
    //    try {
            //Vector info = (Vector) super.buffer[1];  //index 0 is addr, index 1 is port number, index 2 is action name??, index 3 is the value, if any.
            
   //         System.out.println("InvokeActuatorService, ConInvocAddr: " +SOABuffer.getAllConsInvocAddrPortBuffer().toPrettyPrintedString(2, 0));
    //    } catch (JSONException ex) {
    //        Logger.getLogger(InvokeActuatorServices.class.getName()).log(Level.SEVERE, null, ex);
    //    }
        
        JSONObject jsMachineLoc = SOABuffer.getMatchingProvider(servName,targetSignalName);
        
        System.out.println("InvokeActuatorServices, getConsInvocAddrPortBuffer result: " +jsMachineLoc);
        
    //    try {
    //        System.out.println("InvokeActuatorService, InvocServDet: " +jsMachineLoc.toPrettyPrintedString(2, 0));
   //     } catch (JSONException ex) {
   //         Logger.getLogger(InvokeActuatorServices.class.getName()).log(Level.SEVERE, null, ex);
   //     }
        
        //String Addr = info.get(0).toString();
        //int controlPort = Integer.parseInt(info.get(1).toString());
        //action = info.get(2).toString();
        //if (info.size()>3){
        //String value = info.get(3).toString();    
        //}
        
        //String value = super.buffer[1].toString();
 
        ControlMessageComm cntrlMsg = new ControlMessageComm();
        
        JSONObject jsData = new JSONObject();

        //if (action==null){
            //action = SJServiceRegistry.getServicesActionNameOfServiceTypeAndActionType(servType,actionType);
            
                try {
                    
                    int confirmable = Integer.parseInt(jsMachineLoc.getString("conf"));
                    
                    action = jsMachineLoc.getString("action");
                    if (action!=null){
                        jsData.put("action",action);
                    }
                //jsData.put("serviceType","");
                    
                    System.out.println("InvokeActuatorServices, buffer length: " +super.buffer.length);
                    
                    if (super.buffer[1]!=null){
                        String value = super.buffer[1].toString();
                        jsData.put("data",value);
                    } else {
                        jsData.put("data","");
                    }
                
            if (debug==1) System.out.println("InvokeActuatorServices:" +jsData.toPrettyPrintedString(2, 0));
            
            if (confirmable==0)
                {
                    try{
                        InetAddress srcIPAddress=null;

                        srcIPAddress = InetAddress.getByName(SJServiceRegistry.getSourceIPAddress());
                    String addr = jsMachineLoc.getString("addr");
                    reqPort = Integer.parseInt(jsMachineLoc.getString("requestPort"));
                    
                    if(jsMachineLoc.has("responsePort")){
                        respPort = Integer.parseInt(jsMachineLoc.getString("responsePort"));
                    } else {
                        respPort = reqPort;
                    }
                    
                   
                    //InetAddress ipAddr = SJServiceRegistry.getServicesIPLocationOfType(servType);
                    //String ipAddr = SJServiceRegistry.getServicesIPLocationOfTypeAndAction(servType,action);
                    //srcIPAddress = SJServiceRegistry.getSourceIPAddress();
                    //int controlPort = SJServiceRegistry.getServicesControlPortOfTypeAndAction(servType,action);

                     if (reqPort!=0)
                     {
                        SJRequestMessage sjreq = new SJRequestMessage(SJMessageConstants.MessageCode.POST, SJMessageConstants.MessageType.NON);
                        sjreq.setSourceAddress(srcIPAddress.getHostAddress());
                        //sjreq.setDestinationAddress(SJServiceRegistry.getServicesIPLocationOfType(servType));
                        //sjreq.setSourceAddress(srcIPAddress.getHostAddress());
                        sjreq.setDestinationPort(reqPort);
                        
                        
                        if(respPort == reqPort){
                            sjreq.setIncomingPort(reqPort);
                        } else {
                            sjreq.setIncomingPort(respPort);
                        }
                        
                        sjreq.setMessageID();
                        sjreq.setConfirmable(0);
                        //sjreq.setMessageToken();
                        sjreq.setRequestMessagePayloadActuator(jsData);
                        String reqMsg = sjreq.createActuatorRequestMessage();
                        cntrlMsg.sendControlMessage(addr, reqPort, reqMsg);     
                     }

                    } catch (JSONException jex){
                        
                        jex.printStackTrace();
                    } catch (UnknownHostException uex){
                        uex.printStackTrace();
                    }

                }
            
            else if (confirmable==1){
                try {
                
                    InetAddress srcIPAddress=null;
        
                    srcIPAddress = InetAddress.getByName(SJServiceRegistry.getSourceIPAddress());
                
                    String addr = jsMachineLoc.getString("addr");
                    
                    reqPort = Integer.parseInt(jsMachineLoc.getString("requestPort"));
                    
                    if(jsMachineLoc.has("responsePort")){
                        respPort = Integer.parseInt(jsMachineLoc.getString("responsePort"));
                    } else {
                        respPort = reqPort;
                    }
               
                //InetAddress ipAddr = SJServiceRegistry.getServicesIPLocationOfType(servType);
                
                JSONObject jsResp = new JSONObject();
                
                jsResp.put("msgType","");
                
                boolean repeat = true;
                
                int iterate0=0;
                
                while(repeat){
                    
                    SJRequestMessage sjreq = new SJRequestMessage(SJMessageConstants.MessageCode.POST, SJMessageConstants.MessageType.SINGLECON);

                    
                        sjreq.setSourceAddress(srcIPAddress.getHostAddress());
                        sjreq.setDestinationPort(reqPort);
                        if(respPort == reqPort){
                            sjreq.setIncomingPort(reqPort);
                        } else {
                            sjreq.setIncomingPort(respPort);
                        }
                        
                        sjreq.setMessageID();
                        sjreq.setConfirmable(1);
                        //sjreq.setMessageToken();
                        sjreq.setRequestMessagePayloadActuator(jsData);
                        String reqMsg = sjreq.createActuatorRequestMessage();
                    
                        jsResp = cntrlMsg.transceiveRequestMessageShortTimeout(reqMsg, reqPort, respPort, addr);
                        
                        iterate0++;
                        
                        if (jsResp.isEmpty()){
                            jsResp.put("msgType","");
                        } 
                        
                        if(jsResp.getString("msgType").equalsIgnoreCase(SJMessageConstants.MessageType.ACK.toString()) && jsResp.getInt("msgID")==sjreq.getMessageID()){
                            repeat = false;
                        }
                        
                        if(iterate0>2){
                            repeat = false;
                        }
                    
                }
                
                if(iterate0>2){
                    System.out.println("InvokeActuator: Actuator not responsive");
                }
                
                 
                    //} 
                
                /*
                
                do {
                    
                    SJRequestMessage sjreq = new SJRequestMessage(SJMessageConstants.MessageCode.POST, SJMessageConstants.MessageType.SINGLECON);

                    
                        sjreq.setSourceAddress(srcIPAddress.getHostAddress());
                        sjreq.setDestinationPort(reqPort);
                        if(respPort == reqPort){
                            sjreq.setIncomingPort(reqPort);
                        } else {
                            sjreq.setIncomingPort(respPort);
                        }
                        
                        sjreq.setMessageID();
                        sjreq.setConfirmable(1);
                        //sjreq.setMessageToken();
                        sjreq.setRequestMessagePayloadActuator(jsData);
                        String reqMsg = sjreq.createActuatorRequestMessage();
                    
                        jsResp = cntrlMsg.transceiveRequestMessageShortTimeout(reqMsg, reqPort, respPort, addr);
                        
                        if (jsResp.isEmpty()){
                            jsResp.put("msgType","");
                        } 
                    //} 
 
                } while (!jsResp.getString("msgType").equalsIgnoreCase(SJMessageConstants.MessageType.ACK.toString()));
                  
                */
                
                    System.out.println("InvokeActuatorServices, Confirmable message has received acknowledge!");
                
                
            } catch (JSONException ex) {
                ex.printStackTrace();
            } catch (UnknownHostException uex){
                uex.printStackTrace();
            }
            
        } else if (confirmable==2){
            try {
                
                //if (jsMachineLoc.has("time")){
                 //   jsData.put("time", jsMachineLoc.getString("time"));
                //}
                
                String addr = jsMachineLoc.getString("addr");
               // int controlPort = Integer.parseInt(jsMachineLoc.getString("port"));
                
                reqPort = Integer.parseInt(jsMachineLoc.getString("requestPort"));
                    
                    if(jsMachineLoc.has("responsePort")){
                        respPort = Integer.parseInt(jsMachineLoc.getString("responsePort"));
                    } else {
                        respPort = reqPort;
                    }
                
                //InetAddress ipAddr = SJServiceRegistry.getServicesIPLocationOfType(servType);
               
                JSONObject jsResp = new JSONObject();
                
                jsResp.put("msgType","");
                //jsResp.put("rspCode","");
 
                //first step, get ACK that request is received before actuator commences to service the request
                
                SJRequestMessage sjreq = new SJRequestMessage(SJMessageConstants.MessageCode.POST, SJMessageConstants.MessageType.DUALCON);
                
                //if (controlPort!=0)
                // {
                int iterate1=0;
                
                boolean repeat = true;
                
                while(repeat){
                    
                    System.out.println("Do sent Actuator!");
                   
                            sjreq.setSourceAddress(SJServiceRegistry.getOwnIPAddressFromRegistry());
                            
                            //sjreq.setSourceAddress();
                            
                            //sjreq.setDestinationAddress(ipAddr);
                            sjreq.setDestinationPort(reqPort);
                            sjreq.setIncomingPort(respPort);
                            sjreq.setMessageID();
                            sjreq.setConfirmable(2);
                            //sjreq.setMessageToken();
                            sjreq.setRequestMessagePayloadActuator(jsData);
                            String reqMsg = sjreq.createActuatorRequestMessage();
                    
                        //jsResp = cntrlMsg.transceiveRequestMessageLongTimeout(servType, reqMsg, controlPort, ipAddr,timeout );
                            jsResp = cntrlMsg.transceiveRequestMessageShortTimeout(reqMsg, reqPort, respPort, addr);
                            
                            System.out.println("InvokeActuatorServices,transceiveRequestMessageShortTimeout Received: " +jsResp.toPrettyPrintedString(2, 0));
                         
                            iterate1++;
                            
                            if(jsResp.getString("msgType").equalsIgnoreCase(SJMessageConstants.MessageType.ACK.toString()) && jsResp.getInt("msgID")==sjreq.getMessageID()){
                                repeat = false;
                            }
                            
                            if(iterate1>2){
                                repeat = false;
                            }
                            
                    
                }
                
                /*
                        do {
                            
                            System.out.println("Do sent Actuator!");
                   
                            sjreq.setSourceAddress(SJServiceRegistry.getOwnIPAddressFromRegistry());
                            
                            //sjreq.setSourceAddress();
                            
                            //sjreq.setDestinationAddress(ipAddr);
                            sjreq.setDestinationPort(reqPort);
                            sjreq.setIncomingPort(respPort);
                            sjreq.setMessageID();
                            sjreq.setConfirmable(2);
                            //sjreq.setMessageToken();
                            sjreq.setRequestMessagePayloadActuator(jsData);
                            String reqMsg = sjreq.createActuatorRequestMessage();
                    
                        //jsResp = cntrlMsg.transceiveRequestMessageLongTimeout(servType, reqMsg, controlPort, ipAddr,timeout );
                            jsResp = cntrlMsg.transceiveRequestMessageShortTimeout(servName,reqMsg, reqPort, respPort, addr);
                            
                            System.out.println("InvokeActuatorServices,transceiveRequestMessageShortTimeout Received: " +jsResp.toPrettyPrintedString(2, 0));
                         
                            iterate1++;
 
                        } while ((!jsResp.getString("msgType").equalsIgnoreCase(SJMessageConstants.MessageType.ACK.toString()) && jsResp.getInt("msgID")==sjreq.getMessageID()) && iterate1<=5);
                */
                    //   }
                   
                   //System.out.println("InvokeActuatorServices, Confirmable message has received acknowledge (1stStep!)!");
                  
                    if(iterate1<=2){
                        //wait til actuator finish
                        cntrlMsg.receiveDoubleACKMessage(respPort, addr);

                        System.out.println("Actuator service (action) responds with ACK!");
                        
                    } else {
                        System.out.println("Actuator service (action) is not responding");
                    }
                    
                    
                    
                //Second step (second ACK informing that actuator has finished its operation), long wait for actuator to finish, timeout is determined by approximate time that an actuator will finish the job, after that this method will regularly request for response (if none has been received during this long wait period)
                
                //do {
                    //*
                    //JSONObject jsPayload = jsResp.getJSONObject("payload");
                
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                    } 
        
                } catch (JSONException ex) {
                    System.err.println(ex.getMessage());
                }
           
        
            }

        public InvokeActuatorServices(){
		super();
	}
    
}
