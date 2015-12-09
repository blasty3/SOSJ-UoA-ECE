/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package soart;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;
import systemj.common.CDLCBuffer;
import systemj.common.SJMessageConstants;
import systemj.common.SJResponseMessage;
import systemj.common.SJServiceRegistry;
import systemj.common.SOAFacility.Support.SOABuffer;

/**
 *
 * @author Udayanto
 */
public class SOSJ {
    
    public static synchronized void SetServiceToInvoke(Hashtable result,int conf){
       
            String addr = result.get("nodeAddress").toString();
            String reqPort = result.get("requestPort").toString();
            
            String actName = result.get("actionName").toString();
            String servName = result.get("serviceName").toString();
            String target = result.get("signalName").toString();

            if (result.containsKey("responsePort")){
                 String respPort = result.get("responsePort").toString();
                 SOABuffer.setMatchingProvider(servName,addr, reqPort,respPort, Integer.toString(conf), actName,target);
            } else {
                String respPort = result.get("requestPort").toString();
                SOABuffer.setMatchingProvider(servName,addr, reqPort,respPort, Integer.toString(conf), actName,target);
            }
            //else {
           //     SOABuffer.setMatchingProvider(servName,addr, reqPort, conf, actName,target);
           // }
    }
    
    public static synchronized void SetCDLocation(String CDName, String SSName){
        CDLCBuffer.AddCDLocTempToBuffer(CDName, SSName);
    }
    
    public static synchronized void SetServiceToInvoke(String jsMatchedService, String signalName, String actName, int conf){
        
        try {
            JSONObject jsServ = new JSONObject(new JSONTokener(jsMatchedService));
            
            Enumeration keysJsServ = jsServ.keys();
            
            while(keysJsServ.hasMoreElements()){
                String servName = keysJsServ.nextElement().toString();
                
                JSONObject jsServDet = jsServ.getJSONObject(servName);
                
                String addr = jsServDet.get("nodeAddress").toString();
                String reqPort = jsServDet.get("requestPort").toString();
            
                if (jsServDet.has("responsePort")){
                    String respPort = jsServDet.get("responsePort").toString();
                    SOABuffer.setMatchingProvider(servName,addr, reqPort,respPort, Integer.toString(conf), actName,signalName);
                } else {
                    //String respPort = jsServDet.get("requestPort").toString();
                    SOABuffer.setMatchingProvider(servName,addr, reqPort,reqPort, Integer.toString(conf), actName,signalName);
                }
                break;
            }
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    public static synchronized String GetPayloadData(String jsString){
        
        String payloadData=null;
        
        System.out.println("ControlMessage,GetPayloadData: " +jsString);
        
        try
        {
            JSONObject js = new JSONObject(new JSONTokener(jsString));
            
            JSONObject jsPyldData = js.getJSONObject("payload");
             
            if (jsPyldData.has("data")){
                payloadData = jsPyldData.getString("data");
            }
            
        } catch (JSONException ex) {
            ex.printStackTrace();
            //System.out.println("MessageExtractor, ExtractControlMessagePayload JSONException: " +ex.getMessage());
        }
        
        if (payloadData==null){
            return "";
        } else {
            return payloadData;
        }
        
        
    }
    
    public static synchronized String GetAction(String jsString){
        
        String action=null;
        
        try
        {
            JSONObject js = new JSONObject(new JSONTokener(jsString));
            
            JSONObject jsPyldData = js.getJSONObject("payload");
            
            if (jsPyldData.has("action")){
               
                action = jsPyldData.getString("action");
            }
            
        } catch (JSONException ex) {
            System.out.println("MessageExtractor, ExtractControlMessagePayload JSONException: " +ex.getMessage());
        }
        
        if (action==null){
            return "";
        } else {
            return action;
        }
        
    }
    
    public static synchronized String GetConfirmable(String jsString){
        
        String conf=null;
        
        try
        {
            JSONObject js = new JSONObject(new JSONTokener(jsString));
            
            JSONObject jsPyldData = js.getJSONObject("payload");
            
            if (jsPyldData.has("conf")){
               
                conf= jsPyldData.getString("conf");
            }
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        if (conf==null){
            return "";
        } else {
            return conf;
        }
        
    }
    
    /*
    public static synchronized String GetSourceAddress(String jsString){
         
        String srcIPAddr=null;
        
        try
        {
            JSONObject js = new JSONObject(new JSONTokener(jsString));
            
            srcIPAddr = js.getString("srcAddr");
                             
            
        } catch (JSONException ex) {
            System.out.println("MessageExtractor, ExtractControlMessageSourceIPAddress JSONException: " +ex.getMessage());
        }
        
        if (srcIPAddr==null){
            return "0.0.0.0";
        } else {
             return srcIPAddr;
        }
 
        
    }
    
    public static synchronized String GetSourcePort(String jsString){
         
        String srcPort=null;
        
        try
        {
            JSONObject js = new JSONObject(new JSONTokener(jsString));
            
            srcPort = js.getString("reqPort");
                             
            
        } catch (JSONException ex) {
            System.out.println("MessageExtractor, ExtractControlMessageSourceIPAddress JSONException: " +ex.getMessage());
        }
        
        if (srcPort==null){
            return "0";
        } else {
             return srcPort;
        }
 
    }
    
    */
    
    public static synchronized void StoreServiceToRegistry(String regJSON){
        try {
            JSONObject serv = new JSONObject(new JSONTokener(regJSON));
            
            SJServiceRegistry.SaveDiscoveredServicesNoP2P(serv);
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public static synchronized String GetLocalRegistry(){
        
        String reg="{}";
        
        try {
             reg = SJServiceRegistry.obtainCurrentRegistryProviderOnly().toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return reg;
        
    }
    
    public static synchronized String CreateResponseWithData(String msg, String data){
        
        try {
            JSONObject js = new JSONObject(new JSONTokener(msg));
            
            JSONObject jsPyld = new JSONObject();
            
            
            
                    SJResponseMessage sjResp = new SJResponseMessage(SJMessageConstants.MessageType.ACK);
                    //sjResp.setMessageToken(Integer.parseInt(js.getString("token")));
            //sjResp.setSourceAddress(InetAddress.getByName(ipAddr));
                    sjResp.setMessageID(js.getInt("msgID"));
            
                    sjResp.setSourceAddress(js.getString("srcAddr"));
                    
                    sjResp.setDestinationPort(Integer.parseInt(js.getString("respPort"))); //port
                    
                    jsPyld.put("data", data);
                    
                    sjResp.setJSONPayload(jsPyld);
            
                    msg = sjResp.createResponseMessage();
            //js.put("payload", jsPyld);
            
            //msg = 
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return msg;
        
    }
    
    public static synchronized String GetMatchingServiceInJSON(Hashtable result, String RegistryContent){
        
        JSONObject res = new JSONObject();
        
        
        String desServName = (String)result.get("serviceName");
        
        try {
            JSONObject jsRegCont = new JSONObject(new JSONTokener(RegistryContent));
            
            Enumeration keysAllSS = jsRegCont.keys();
            
            while(keysAllSS.hasMoreElements()){
                
                String SSName = keysAllSS.nextElement().toString();
                
                JSONObject AllServSS = jsRegCont.getJSONObject(SSName);
                
                JSONObject resServ = new JSONObject();
                
                Enumeration keysAllServSS = AllServSS.keys();
                
                while(keysAllServSS.hasMoreElements()){
                    
                    String servName = keysAllServSS.nextElement().toString();
                    
                    if(servName.equals(desServName)){
                        
                        resServ.put(servName, AllServSS.getJSONObject(servName));
                        
                    }
                }
                res.put(SSName, resServ);
                
            }
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return res.toString();
        
    }
    
    public static void TriggerAdvTransmission(){
        SOABuffer.SetAdvTransmissionRequest(true);
    }
    
    /*
    public static String GetServiceRegistryContent(){
        
        if (SJServiceRegistry.getParsingStatus())
            {
                //System.out.println("SJRegFetcher, Obtaining reg"); 
                try {
                    JSONObject jsAllCurrServ = SJServiceRegistry.obtainCurrentRegistry();
                    
                    //System.out.println("ServiceRegistryFetcher,obtainCurrentRegistry: " +jsAllCurrServ.toPrettyPrintedString(2, 0));
                    
                    //obj[0]=Boolean.TRUE;

                    return jsAllCurrServ.toString();
                    
                } catch (JSONException ex) {
                    
                    System.out.println("Error in grabbing info from service description: " +ex.getMessage());
                    return "{}";
                }
            } else {
                //System.out.println("SJRegFetcher, Parsing incomplete");
                 return "{}";
                
            }
        
    }
    */
    
}
