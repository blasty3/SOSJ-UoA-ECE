/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systemj.signals.SOA;


import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;
import systemj.common.SJMessageConstants;
import systemj.common.SJRequestMessage;
import systemj.common.SJResponseMessage;

import systemj.common.SJServiceRegistry;
import systemj.common.SOAFacility.Support.NetworkConnCheckSimple;
import systemj.common.SOAFacility.Support.SOABuffer;
import systemj.interfaces.GenericSignalReceiver;


/**
 *
 * @author Udayanto
 */
public class ObtainInvokedSensorServices extends GenericSignalReceiver implements Serializable{

    //int timeout
            int attempt;
    
    String serviceName,signalName;
    
    //String status;
    
    @Override
    public void configure(Hashtable data) throws RuntimeException {
        
        if (data.containsKey("associatedServiceName")){
            
            serviceName = (String)data.get("associatedServiceName");
            
        } else {
            throw new RuntimeException("attribute 'associatedServiceName' is needed");
        }
        
        /*if (data.containsKey("Name")){
            
            signalName = (String)data.get("Name");
            
        } else {
            throw new RuntimeException("attribute 'Name' is needed");
        }
        */
        if(data.containsKey("Name")){
            this.name = (String)data.get("Name");
            signalName = this.name;
        }
        
        /*
        if (data.containsKey("timeout")){
            
            timeout = Integer.parseInt((String)data.get("timeout"));
            
        } else {
            throw new RuntimeException("attribute 'timeout' is needed for the transmit and receive operation");
        }
        */
        
        /*
        if (data.containsKey("retryAttemptAmount")){
            
            attempt = Integer.parseInt((String)data.get("retryAttemptAmount"));
            
        } else {
            throw new RuntimeException("attribute 'retryAttemptAmount' is needed for the transmit and receive operation");
        }
        */
       

        //else {
          //  throw new RuntimeException("attribute 'action' is needed");
        //}
        
    }
    
    /**
     * Method to check network connection availability. This method is used since it responds quicker compared to iterating all connection, unless if the machine is only connected to one type of network 
     * @return 
     */
    
    

    @Override
    public void run() {
        
        /*
        NetworkConnCheckSimple netcheck = new NetworkConnCheckSimple();
        
        boolean printedOnce=false;
        boolean invokestat=false;
        
        String reading="";
        
        while(active){
            
            int debug=1;
            
            Object[] list = new Object[2]; 
            
            if (SJServiceRegistry.getParsingStatus())
            {
                String connectionStatus = netcheck.CheckNetworkConn("192.168.1.1",1300);
                
                if (connectionStatus.equalsIgnoreCase("Connected")){
                    
                    reading = SOABuffer.getObtainedInVal(serviceName,signalName);
                    
                    if(reading.equalsIgnoreCase("")){
                        list[0]=Boolean.FALSE;
                    } else {
                        list[0]=Boolean.TRUE;
                        list[1]=reading;
                    }
                    
                } else {

                    list[0]=Boolean.FALSE;
                    
                    if (debug==1) System.out.println("InvokeSensorServices: No connection detected");
                }

            } else {
                //Object[] list = new Object[2];
                list[0]=Boolean.FALSE;
                
                if (debug==1) System.out.println("InvokeSensorServices: Parsing process incomplete");
     
            }
            invokestat=false;
            super.setBuffer(list);
        }
        */
    }

    public ObtainInvokedSensorServices(){
        super();  //init the buffer
    }
    
    @Override
	public void getBuffer(Object[] obj){
            
             NetworkConnCheckSimple netcheck = new NetworkConnCheckSimple();
        
        boolean printedOnce=false;
        boolean invokestat=false;
        
        String reading=null;
        
            int debug=1;
            int infoDebug=1;
            int reqPort=0;
            int respPort=0;
            //InetAddress srcIPAddress=null;
            String destIPAddress=null;
           
            //Object[] list = new Object[2]; 
            
            ControlMessageComm trReq = new ControlMessageComm();
           
                String connectionStatus = netcheck.CheckNetworkConn(SOABuffer.getGatewayAddr(),1300);
                
                while(!connectionStatus.equalsIgnoreCase("Connected")){
                    
                }
                
                    JSONObject jsAddrPort = new JSONObject();
                    
                   // System.out.println("InvokeSensorServices, ConsInvocAddr:" +SOABuffer.getAllConsInvocAddrPortBuffer());
                    
                  //  boolean IsSetInvoke = SOABuffer.checkMatchingProvider(serviceName, signalName);
                    
                  //  if (IsSetInvoke){
                        
                        jsAddrPort = SOABuffer.getMatchingProvider(serviceName,signalName);
                    
                        if (!jsAddrPort.isEmpty()){
                        
                        //System.out.println("jsAddrPort: " +jsAddrPort);
                        
                        try {
                            destIPAddress = jsAddrPort.getString("addr");
                            reqPort = Integer.parseInt(jsAddrPort.getString("requestPort"));
                            
                            if(jsAddrPort.has("responsePort")){
                                respPort = Integer.parseInt(jsAddrPort.getString("responsePort"));
                            } else {
                                respPort = reqPort;
                            }
                            
                            //respPort = Integer.parseInt(jsAddrPort.getString("responsePort"));
                            action = jsAddrPort.getString("action");
                         } catch (JSONException ex) {
                                ex.printStackTrace();
                        }
                        
                    } else {
                        //System.out.println("jsAddrPort empty");
                    }
                    
            //if it's invocation, the port will be the control port of the particular services being invoked
               // port = SJServiceRegistry.getServicesControlPortOfType(serviceType);
                

                
                //if (debug==1) System.out.println("InvokeSensorServices: Found service in IP:" +destIPAddress+ "port:" +port);
                
                if (destIPAddress!=null && reqPort!=0)
                {

                 // if (!destIPAddress.equalsIgnoreCase(SJServiceRegistry.getOwnIPAddressFromRegistry()))  
                 // {
                    if (debug==1) System.out.println("InvokeSensorServices, Found match in address:" +destIPAddress+ "port:" +reqPort );
                     SJRequestMessage sjreq = new SJRequestMessage(SJMessageConstants.MessageCode.GET, SJMessageConstants.MessageType.SINGLECON);
        //sjreq.setDestinationAddress(SJServiceRegistry.getServicesIPLocationOfType(serviceType)); //need method that will return IP address of the desired service
        //sjreq.setDestinationPort(SJServiceRegistry.getServicesControlPortOfType(serviceType));
                    sjreq.setSourceAddress(SJServiceRegistry.getSourceIPAddress());
                    
                    if(respPort == reqPort){
                         sjreq.setDestinationPort(reqPort);
                    } else {
                         sjreq.setDestinationPort(respPort);
                    }
                    
                    JSONObject jsData = new JSONObject();
                    
                    try {
                        if (action!=null){
                            jsData.put("action",action);
                        }
                        //jsData.put("serviceType",serviceType);
                        jsData.put("data","");
                        if (debug==1) System.out.println("ObtainInvokedServices:" +jsData.toPrettyPrintedString(2, 0));
                    } catch (JSONException ex) {
                        System.err.println(ex.getMessage());
                    }
                    
                    //* MessagePayload needs to change to action or actionType
                    
                   // if (action==null){
                    //    sjreq.setRequestMessagePayloadActuator(actionType);
                  //  } else if (actionType==null){
                        sjreq.setRequestMessagePayloadActuator(jsData);
                  //  }
                    
                    
                    sjreq.setMessageID();
                    //sjreq.setMessageToken();
                    //token = sjreq.getMessageToken();
                    String req = sjreq.createRequestMessage();
                    if (debug==1) System.out.println("InvokeSensorServices SJREQ:" +req + "send to:" +destIPAddress+ "port:" +reqPort );

                    int attemptTry=0;
                    
                     try
                      {
                        //String response = trReq.transceiveRequestMessage(serviceType,req,port,destIPAddress);  //returned raw response message with JSON format
                        
                        JSONObject js  = new JSONObject();
                          
                        while (attemptTry<5) 
                        { 
                          
                         js = trReq.transceiveRequestMessageShortTimeout(req, reqPort, respPort,destIPAddress);  //returned raw response message with JSON format
                         
                         if (!js.isEmpty())
                         {
                      //  if (token==Integer.parseInt(js.getString("token")))
                      //  {
                            if (js.getString("msgType").equalsIgnoreCase(SJMessageConstants.MessageType.ACK.toString()))
                            {
                                if (Integer.parseInt(js.getString("msgID"))==sjreq.getMessageID()){
                                    JSONObject jsRead = js.getJSONObject("payload");
                                    reading=jsRead.getString("data");
                                    if (debug==1) System.out.println("Invoke sensor,acquired sensor reading:" +reading);
                                    //SOABuffer.setObtainedInVal(serviceName, targetSignalName, reading);
                                    
                                    obj[0]=Boolean.TRUE;
                                    obj[1]=reading;
                                    
                                    invokestat=true;
                                    break;
                                } else {
                                    
                                    
                                    if (infoDebug==1) System.out.println("InvokeSensorServices, response code incorrect or messageID doesnt match");
                                    obj[0]=Boolean.FALSE;
                                }
                                
                            } else {
                                
                                
                                if (infoDebug==1) System.out.println("InvokeSensorServices, MessageType is not ACK ");
                                obj[0]=Boolean.FALSE;
                            }
                        //} else {
                       //     list[0]= Boolean.FALSE;
                       //     list[1]="";
                        //    if (infoDebug==1) System.out.println("InvokeSensorServices, token not the same");
                      //  }
                        } else {
                             obj[0]=Boolean.FALSE;
                        }
                            attemptTry++;
                      }
                        
                      
                    //JSONObject js = new JSONObject(new JSONTokener(response.trim()));
                    /*
                    if (!js.isEmpty())
                    {
                      //  if (token==Integer.parseInt(js.getString("token")))
                      //  {
                            if (js.getString("msgType").equalsIgnoreCase(SJMessageConstants.MessageType.ACK.toString()))
                            {
                                if (js.getString("rspCode").equalsIgnoreCase(SJMessageConstants.ResponseCode.CONTENT.toString())){
                                    JSONObject jsRead = js.getJSONObject("payload");
                                    reading=jsRead.getString("data");
                                    if (debug==1) System.out.println("Invoke sensor,acquired sensor reading:" +reading);
                                    list[0]= Boolean.TRUE;
                                    list[1]= reading;
                                } else if (js.getString("rspCode").equalsIgnoreCase(SJMessageConstants.ResponseCode.SERVICE_UNAVAILABLE.toString())){
                                    list[0]= Boolean.FALSE;
                                    list[1]="";
                                    if (infoDebug==1) System.out.println("InvokeSensorServices, Service Unavailable");
                                }  else if (js.getString("rspCode").equalsIgnoreCase(SJMessageConstants.ResponseCode.VALID.toString())){
                                    JSONObject jsRead = js.getJSONObject("payload");
                                    reading=jsRead.getString("data");
                                    if (debug==1) System.out.println("Invoke sensor,acquired sensor reading:" +reading);
                                    list[0]= Boolean.TRUE;
                                    list[1]= reading;
                                }else {
                                    list[0]= Boolean.FALSE;
                                    list[1]="";
                                    if (infoDebug==1) System.out.println("InvokeSensorServices, response code incorrect");
                                }
                                
                            } else {
                                list[0]= Boolean.FALSE;
                                list[1]="";
                                if (infoDebug==1) System.out.println("InvokeSensorServices, Message not acknowledge ");
                            }
                        //} else {
                       //     list[0]= Boolean.FALSE;
                       //     list[1]="";
                        //    if (infoDebug==1) System.out.println("InvokeSensorServices, token not the same");
                      //  }
                    } else {
                        //list[0]= Boolean.FALSE;
                        //list[1]="";
                        if (infoDebug==1) System.out.println("InvokeSensorServices, by service: "+serviceName+", No response, 1st attempt");
                        
                        js = new JSONObject();
                        
                        js = trReq.transceiveRequestMessageShortTimeout(serviceName,req,port,destIPAddress, timeout);
                        
                        
                        if (!js.isEmpty())
                        {
                        
                            if (js.getString("msgType").equalsIgnoreCase(SJMessageConstants.MessageType.ACK.toString()))
                            {
                                if (js.getString("rspCode").equalsIgnoreCase(SJMessageConstants.ResponseCode.CONTENT.toString())){
                                    
                                    JSONObject jsRead = js.getJSONObject("payload");
                                    reading=jsRead.getString("data");
                                    
                                    //reading=js.getString("payload");
                                    if (debug==1) System.out.println("Invoke sensor,acquired sensor reading:" +reading);
                                    list[0]= Boolean.TRUE;
                                    list[1]= reading;
                                } else if (js.getString("rspCode").equalsIgnoreCase(SJMessageConstants.ResponseCode.SERVICE_UNAVAILABLE.toString())){
                                    list[0]= Boolean.FALSE;
                                    list[1]="";
                                    if (infoDebug==1) System.out.println("InvokeSensorServices, Service Unavailable");
                                } else {
                                    list[0]= Boolean.FALSE;
                                    list[1]="";
                                    if (infoDebug==1) System.out.println("InvokeSensorServices, response code incorrect");
                                }
                                
                            } else {
                                list[0]= Boolean.FALSE;
                                list[1]="";
                                if (infoDebug==1) System.out.println("InvokeSensorServices, Message not acknowledge ");
                            }
                        
                      } else {
                          list[0]= Boolean.FALSE;
                          list[1]="";
                          if (infoDebug==1) System.out.println("InvokeSensorServices, by service: "+serviceName+", No response 2nd attempt");  
                      }
                        
                        
                    }
                    * 
                    */
        //send control request to remote service
    
                    if (reading==null){
                        if (debug==1) System.out.println("InvokeSensorServices, No Reading detected (NULL)");
                        obj[0]= Boolean.FALSE;
                       
                    } 
                    //else {
                   //     if (debug==1) System.out.println("Invoke sensor,acquired sensor reading:" +reading);
                   //     list[0]= Boolean.TRUE;
                   //     list[1]= reading;
                   // }
                
                    
        
                    //super.setBuffer(list);
                    
                    } catch (JSONException jex){
                       jex.printStackTrace();
                        obj[0]=Boolean.FALSE;
                        
                        //super.setBuffer(list);
                      
                   }
                    printedOnce=false;
                 // }
                 //  else {
                 //   list[0]=Boolean.FALSE;
                  //  list[1] ="";
                  //  if (debug==1 && printedOnce) {System.out.println("InvokeSensorServices: No matching address"); printedOnce=true;}
                  
                   // }
                   } else {
                    
                  
                  //list[1] ="";
                    if (debug==1 && printedOnce) {System.out.println("InvokeSensorServices: address NULL"); printedOnce=true;}
                    obj[0]= Boolean.FALSE;
                  //super.setBuffer(list);
                   }
                    
                        
               // } else {
              //      list[0]=Boolean.FALSE;
              //  }
                  
               

            
     
            //super.setBuffer(list);
            invokestat=false;
            
        }
    
    String action;

}
