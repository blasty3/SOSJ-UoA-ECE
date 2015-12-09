/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systemj.common.SOAFacility.Support;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import systemj.common.SJServiceRegistry;

/**
 * contain all SOA msg buffer for data exchange between SOA thread
 * @author Udayanto
 */
public class SOABuffer {
    
    // Lock
    
    private static final Object SOSJRegistryIDLock = new Object();
    private static String SOSJRegistryID;
    private static String RegAdvExpiryTime;
            
    private static final Object SOSJRegistryAddrLock = new Object();
    private static String SOSJRegistryAddr;
    
    private static final Object RegAdvExpiryTimeLock = new Object();
    
    private static String Addr;
    private static final Object AddrLock = new Object();
    
    private static String SubnetMaskAddr;
    private static final Object SubnetMaskAddrLock = new Object();
    
    private static final Object ReqAdvMsgBufferLock = new Object();
    private static final Object DiscMsgBufferLock = new Object();
    
    
    private static final Object DiscReqLock = new Object();
   
    
    private static final Object IsDiscDoneLock = new Object();
    private static final Object IsAdvDoneLock = new Object();

    private static final Object AdvVisibListLock = new Object();
    
    private static final Object ConsInvAddrPortLock = new Object();
    
    private static final Object ConsMultipleInvAddrPortLock = new Object();

    private static final Object SJInValLock = new Object();
    
    //Buffer
    
    private static JSONObject SJInVal = new JSONObject();
    
    private static Vector DiscMsgBuffer = new Vector();
    private static Vector ReqAdvMsgBuffer = new Vector();
    
    private static Vector RegToProvReqAdvMsgBuffer = new Vector();
    private final static Object RegToProvReqAdvMsgBufferLock = new Object();
    
    private static JSONObject AdvVisibList = new JSONObject();
    
    private static String EmptySSName = null;
    private static final Object EmptySSNameLock = new Object();
   
    
    private static boolean IsAdvDone=false;
    private static boolean IsDiscDone=false;
    private static Hashtable servNameSetInvokeList = new Hashtable();
    
    
    private static Vector DiscReqBuffer = new Vector();
    
    private static JSONObject ServiceProviderOccupancy = new JSONObject();
    
    private static final Object ServiceProviderOccupancyLock = new Object();
    
    
    private static JSONObject ConsumerSensorInvocationAddrPortBuffer = new JSONObject();
    
    private static Hashtable ConsumerMultipleInvocationAddrPortBuffer = new Hashtable();
    
    private static JSONObject OrchestratedServiceList = new JSONObject();
    
    private static final Object OrchestratedServiceListLock = new Object();
    
    private static boolean RequestForAdvTransmission = false;
    private static final Object RequestForAdvTransmissionLock = new Object();
    
    private static JSONObject Timeout2Values = new JSONObject();
    
    private static final Object Timeout2ValLock = new Object();
    
    public static void AddEmptySSName(String SSName){
        synchronized(EmptySSNameLock){
            EmptySSName = SSName;
        }
    }
    
    public static String GetEmptySSName(){
        synchronized(EmptySSNameLock){
            return EmptySSName;
        }
    }
    
    public static void AddRegToProvReqAdv (JSONObject jsMsg){
        synchronized(RegToProvReqAdvMsgBufferLock){
            RegToProvReqAdvMsgBuffer.addElement(jsMsg);
        }
    }
    
    public static Vector GetRegToProvReqAdv (){
        synchronized(RegToProvReqAdvMsgBufferLock){
            
            Vector vec = new Vector();
            
            for (int buffsize=0;buffsize<RegToProvReqAdvMsgBuffer.size();buffsize++){
                vec.addElement(RegToProvReqAdvMsgBuffer.get(buffsize));
            }
            
            RegToProvReqAdvMsgBuffer = new Vector();
            
            return vec;
        }
    }
    
    public static void setSOSJRegID(String regID){
        synchronized(SOSJRegistryIDLock){
            SOSJRegistryID = regID;
        }
        
    }
    
    public static String getSOSJRegID(){
        synchronized(SOSJRegistryIDLock){
            return SOSJRegistryID;
        }
    }
    
    public static void setSOSJRegAddr(String regaddr){
        synchronized(SOSJRegistryAddrLock){
            SOSJRegistryAddr=regaddr;
        }
        
    }
    
    public static String getSOSJRegAddr(){
        synchronized(SOSJRegistryAddrLock){
            return SOSJRegistryAddr;
        }
    }
    
    public static void setGatewayAddr(String addr){
        synchronized(AddrLock){
            Addr=addr;
        }
        
    }
    
    public static String getGatewayAddr(){
        synchronized(AddrLock){
            return Addr;
        }
    }
    
    public static void setSubnetMaskAddr(String addr1){
        synchronized(SubnetMaskAddrLock){
            SubnetMaskAddr=addr1;
        }
        
    }
    
    public static String getSubnetMaskAddr(){
        synchronized(SubnetMaskAddrLock){
            return SubnetMaskAddr;
        }
    }
    
    public static void setSOSJRegExpiryTime(String addr){
        synchronized(RegAdvExpiryTimeLock){
            RegAdvExpiryTime=addr;
        }
        
    }
    
    public static String getSOSJRegExpiryTime(){
         synchronized(RegAdvExpiryTimeLock){
            return RegAdvExpiryTime;
        }
    }
    
    public static void putDiscMsgToDiscBuffer(JSONObject jsMsg){

        synchronized (DiscMsgBufferLock){
            DiscMsgBuffer.addElement(jsMsg);
        }

    }
    
    public static Vector getAllDiscMsgFromBuffer(){
        synchronized (DiscMsgBufferLock){
            
            Vector vec = DiscMsgBuffer;
            DiscMsgBuffer = new Vector();
            
            return vec;
        }
    }
    
    public static JSONObject getDiscMsgBuffer(){

        synchronized (DiscMsgBufferLock){
            if (!DiscMsgBuffer.isEmpty()){
                
                return (JSONObject) DiscMsgBuffer.remove(0);
                
            } else {
                
                return new JSONObject();
		//throw new RuntimeException("DiscMsgBuffer Empty");
            }
        }

    }
    
    public static void putAdvModToBuffer(String servName, String advVisib){

        try{
        
            synchronized(AdvVisibListLock){
                
                //if(AdvVisibList.has(servName)){
                 //   AdvVisibList.remove(servName);
                //}
                
                AdvVisibList.put(servName, advVisib);
            }
          } catch (JSONException jex){
             System.out.println("SOABuffer putAdvModToBuffer:" +jex.getCause());
          }
    }
    
    public static void modifyAdvModBufferOfServiceName(String servName, String stat){
        synchronized(AdvVisibListLock){
            
            if(AdvVisibList.has(servName)){
                if(stat.equalsIgnoreCase("visible") || stat.equalsIgnoreCase("invisible")){
                    try {
                        AdvVisibList.put(servName, stat);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            
        }
    }
    
    public static void removeAdvStatOfServName(String servName){
        synchronized(AdvVisibListLock){
            //jsAdvVisibList = AdvVisibList;
           AdvVisibList.remove(servName);
        }
    }
    
     public static JSONObject getAdvModBuffer(){

        //JSONObject jsAdvVisibList;
         
        synchronized(AdvVisibListLock){
            //jsAdvVisibList = AdvVisibList;
            return AdvVisibList;
        }

        //return jsAdvVisibList;
        
    }
    
   
    
    
    public static void putReqAdvToReqAdvBuffer(JSONObject jsMsg){

        synchronized (ReqAdvMsgBufferLock){
            ReqAdvMsgBuffer.addElement(jsMsg);
        }

    }
    
    public static JSONObject getReqAdvBuffer(){

        synchronized (ReqAdvMsgBufferLock){
            
            if (!ReqAdvMsgBuffer.isEmpty()){
                
                return (JSONObject) ReqAdvMsgBuffer.remove(0);
                
            } else {
                return new JSONObject();
		//throw new RuntimeException("DiscReq Empty");
            }
            
        }

    }
    
    public static Vector getAllContentReqAdvBuffer(){

        Vector allReqAdvs = new Vector();
        
        synchronized (ReqAdvMsgBufferLock){
            
                allReqAdvs = ReqAdvMsgBuffer;
                
                ReqAdvMsgBuffer = new Vector();
                
        }
        
        return allReqAdvs;

    }
    
    
    
    public static void putDiscReqToDiscReqBuffer(JSONObject jsMsg){

        synchronized (DiscReqLock){
            DiscReqBuffer.addElement(jsMsg);
        }

    }
    
    public static JSONObject getDiscReqBuffer(){

        synchronized(DiscReqLock){
            if (!DiscReqBuffer.isEmpty()){
                
                return (JSONObject) DiscReqBuffer.remove(0);
                
            } else {
                return new JSONObject();
		//throw new RuntimeException("DiscReq Empty");
            }
        }

    }

    
    public static void setIsInitDiscDone(boolean stat){
        
        synchronized(IsDiscDoneLock){
            IsDiscDone = stat;
        }
        
    }
    
    public static boolean getIsInitDiscDone(){
        
        boolean result;
        
        synchronized(IsDiscDoneLock){
            result = IsDiscDone;
        }
        
        return result;
        
    }
    
    public static void setIsInitAdvDone(boolean stat){
        synchronized(IsAdvDoneLock){
            IsAdvDone = stat;
        }
    }
    
    public static boolean getIsInitAdvDone(){
        
        boolean result;
        
        synchronized(IsAdvDoneLock){
            result = IsAdvDone;
        }
        
        return result;
        
    }
    
    public static void setAllAdvVisib(JSONObject jsAdvVisibList){

        synchronized (AdvVisibListLock){
            AdvVisibList = jsAdvVisibList;
        }
        
    }
    
    public static void setInitAdvVisibOneByOne(String servName, String VisibStat){
        
        synchronized (AdvVisibListLock){
            try {
                AdvVisibList.put(servName,VisibStat);
            } catch (JSONException ex) {
                System.out.println("SJServiceRegistry, setInitAdvVisbOneByOne JSONexception: " +ex.getCause());
            }
        }
        
    }
    
    public static JSONObject getAllAdvVisib(){
        
        synchronized (AdvVisibListLock){
            return AdvVisibList;
        }
        
    }

    
    public static void setMatchingProvider(String servName, String addr, String reqPort, String actionName, String targetSignalName){
        
        JSONObject jsAddrPort = new JSONObject();
        JSONObject jsTargetSig = new JSONObject();
        try {
            jsAddrPort.put("addr",addr);
            jsAddrPort.put("requestPort",reqPort);
            
            jsAddrPort.put("action",actionName);
            jsTargetSig.put(targetSignalName,jsAddrPort);
            synchronized (ConsInvAddrPortLock){
                if (ConsumerSensorInvocationAddrPortBuffer.has(servName)){
                    
                    JSONObject jsAll = new JSONObject();
                    jsAll = ConsumerSensorInvocationAddrPortBuffer.getJSONObject(servName);
                    
                    jsAll.put(targetSignalName,jsAddrPort);
                    
                    ConsumerSensorInvocationAddrPortBuffer.put(servName,jsAll);
                    
                } else {
                    ConsumerSensorInvocationAddrPortBuffer.put(servName,jsTargetSig);
                }
            } 
        } catch (JSONException ex) {
            System.out.println("SOABuffer JSONException: "+ex.getCause());
        }
     
    }
    
    public static void setMultipleMatchingProvider(String servName, String targetSignalName, Hashtable actDet){
        
        //JSONObject jsAddrPort = new JSONObject();
        //JSONObject jsTargetSig = new JSONObject();
        
        
        
     //   try {
            //jsAddrPort.put("addr",addr);
            //jsAddrPort.put("port",port);
            //jsAddrPort.put("action",actionName);
            //jsTargetSig.put("signalName",targetSignalName);
            synchronized (ConsMultipleInvAddrPortLock){
                
                if (ConsumerMultipleInvocationAddrPortBuffer.containsKey(servName)){
                    
                    Hashtable jsSigList = (Hashtable)ConsumerMultipleInvocationAddrPortBuffer.get(servName);
                    jsSigList.put(targetSignalName,actDet);
                    ConsumerMultipleInvocationAddrPortBuffer.put(servName,jsSigList);
                    
                } else {
                    Hashtable js = new Hashtable();
                    js.put(targetSignalName, actDet);
                    ConsumerMultipleInvocationAddrPortBuffer.put(servName,js);
                }
                
                
            } 
      //  } catch (JSONException ex) {
     //       System.out.println("SOABuffer JSONException: "+ex.getCause());
     //   }
     
    }
    
     public static Hashtable getMultipleMatchingProvider(String servName, String targetSignalName){
        
        Hashtable js = new Hashtable();
        //JSONObject jsResult = new JSONObject();
        
        Hashtable jsResult = new Hashtable();
        
        synchronized (ConsMultipleInvAddrPortLock){
            js = ConsumerMultipleInvocationAddrPortBuffer;
        }
        
        if (js.containsKey(servName)){
            Hashtable jsServName = (Hashtable)js.get(servName);
            
            if (jsServName.containsKey(targetSignalName)){
                jsResult = (Hashtable) jsServName.get(targetSignalName);
            }
            
            
        }
        //try {
            
             
       // } catch (JSONException ex) {
       //     Logger.getLogger(SOABuffer.class.getName()).log(Level.SEVERE, null, ex);
       // }
        
        
        
        return jsResult;
        
    }
    
    public static void setDualACKTimeout2(String servName,int timeout2){
        synchronized (Timeout2ValLock){
            try {
                Timeout2Values.put(servName,timeout2);
            } catch (JSONException ex) {
                System.out.println("SOABUffer setDualACKTimeout2 JSONexception : " +ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    public static int getDualACKTimeout2(String servName){
        
        int timeout2=0;
        
        synchronized (Timeout2ValLock){
            try {
                timeout2 = Timeout2Values.getInt(servName);
            } catch (JSONException ex) {
                System.out.println("SOABUffer getDualACKTimeout2 JSONexception : " +ex.getMessage());
                ex.printStackTrace();
            }
        }
        
        return timeout2;
    }
    
    /*
    public static void setMatchingProvider(String servName, String addr, String reqPort, String respPort, String time,String actionName, String targetSignalName){
        
        JSONObject jsAddrPort = new JSONObject();
        JSONObject jsTargetSig = new JSONObject();
        try {
            jsAddrPort.put("addr",addr);
            jsAddrPort.put("requestPort",reqPort);
            jsAddrPort.put("responsePort",respPort);
            jsAddrPort.put("action",actionName);
            jsAddrPort.put("time",time);
            jsTargetSig.put(targetSignalName,jsAddrPort);
            synchronized (ConsInvAddrPortLock){
                
                if (ConsumerSensorInvocationAddrPortBuffer.has(servName)){
                    
                    JSONObject jsAll = new JSONObject();
                    jsAll = ConsumerSensorInvocationAddrPortBuffer.getJSONObject(servName);
                    
                    jsAll.put(targetSignalName,jsAddrPort);
                    
                    ConsumerSensorInvocationAddrPortBuffer.put(servName,jsAll);
                    
                } else {
                    ConsumerSensorInvocationAddrPortBuffer.put(servName,jsTargetSig);
                }
                
                
            } 
        } catch (JSONException ex) {
            System.out.println("SOABuffer JSONException: "+ex.getCause());
        }
     
    }
    */
    
    public static void setMatchingProvider(String servName, String addr, String reqPort, String respPort, String conf,String actionName, String targetSignalName){
        
        JSONObject jsAddrPort = new JSONObject();
        JSONObject jsTargetSig = new JSONObject();
        try {
            jsAddrPort.put("addr",addr);
            jsAddrPort.put("requestPort",reqPort);
            jsAddrPort.put("responsePort",respPort);
            jsAddrPort.put("action",actionName);
            jsAddrPort.put("conf", conf);
            jsTargetSig.put(targetSignalName,jsAddrPort);
            synchronized (ConsInvAddrPortLock){
                
                if (ConsumerSensorInvocationAddrPortBuffer.has(servName)){
                    
                    JSONObject jsAll = new JSONObject();
                    jsAll = ConsumerSensorInvocationAddrPortBuffer.getJSONObject(servName);
                    
                    jsAll.put(targetSignalName,jsAddrPort);
                    
                    ConsumerSensorInvocationAddrPortBuffer.put(servName,jsAll);
                    
                } else {
                    ConsumerSensorInvocationAddrPortBuffer.put(servName,jsTargetSig);
                }
                
                
            } 
        } catch (JSONException ex) {
            System.out.println("SOABuffer JSONException: "+ex.getCause());
        }
     
    }
    
    public static boolean checkMatchingProvider(String servName, String signalName){
        
        boolean stat=false;
        
        synchronized (ConsInvAddrPortLock){
            if (ConsumerSensorInvocationAddrPortBuffer.has(servName)){
                try {
                    JSONObject jsSig = ConsumerSensorInvocationAddrPortBuffer.getJSONObject(servName);
                    
                    if (jsSig.has(signalName)){
                        stat=true;
                    }
                    
                } catch (JSONException ex) {
                    Logger.getLogger(SOABuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return stat;
    }
    
    public static JSONObject getMatchingProvider(String servName, String targetSignalName){
        
        
        JSONObject jsResult = new JSONObject();
                
        synchronized (ConsInvAddrPortLock){
            //js = ConsumerSensorInvocationAddrPortBuffer;
            
            Enumeration keys = ConsumerSensorInvocationAddrPortBuffer.keys();
        
            while (keys.hasMoreElements()){
            
                Object key = keys.nextElement();
            
                if (key.toString().equalsIgnoreCase(servName)){
                    try { 
                        JSONObject jsServ = ConsumerSensorInvocationAddrPortBuffer.getJSONObject(key.toString());

                        jsResult = jsServ.getJSONObject(targetSignalName);
                    
                        //jsServ.remove(targetSignalName);
                        
                        //ConsumerSensorInvocationAddrPortBuffer.put(servName, jsServ);
   
                    } catch (JSONException ex) {
                        Logger.getLogger(SOABuffer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            
            }
        }
        
        return jsResult;
        
    }
    
    public static void RemoveMatchingProvider(String servName, String SigName){
        synchronized (ConsInvAddrPortLock){
            if (ConsumerSensorInvocationAddrPortBuffer.has(servName)){
                try {
                    JSONObject jsSig = ConsumerSensorInvocationAddrPortBuffer.getJSONObject(servName);
                    
                    if (jsSig.has(SigName)){
                        jsSig.remove(SigName);
                        
                        if(!jsSig.isEmpty()){
                             ConsumerSensorInvocationAddrPortBuffer.put(servName,jsSig);
                        } else {
                            ConsumerSensorInvocationAddrPortBuffer.remove(servName);
                        }
                        
                    }
                    
                } catch (JSONException ex) {
                    Logger.getLogger(SOABuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    } 
    
    public static JSONObject getAllConsInvocAddrPortBuffer(){
        
        JSONObject js = new JSONObject();
        
        synchronized (ConsInvAddrPortLock){
            js = ConsumerSensorInvocationAddrPortBuffer;
        }

        return js;
        
    }
    
    public static void setServiceOccupancy(String servName, String occStatus){
        
        
            try {
                
                    synchronized (ServiceProviderOccupancyLock){
                
                        ServiceProviderOccupancy.put(servName, occStatus);
                
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(SOABuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
     }
    
    
     public static String getServiceOccupancy(String servName){
        
         String servOcc = "";
        
            try {
                    synchronized (ServiceProviderOccupancyLock){
                
                        servOcc = ServiceProviderOccupancy.getString(servName);
                
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(SOABuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            return servOcc;
     }
     
     public static void setOrchestratedService(String servName, String targetSignalName, Vector OrchestratedService){
        
        
        JSONObject jsTargetSig = new JSONObject();
        try {
            
            jsTargetSig.put(targetSignalName,OrchestratedService);
            synchronized (OrchestratedServiceListLock){
                OrchestratedServiceList.put(servName,jsTargetSig);
            } 
        } catch (JSONException ex) {
            System.out.println("SOABuffer JSONException: "+ex.getCause());
        }
     
    }
     
      public static Hashtable getOrchestratedService(String servName, String targetSignalName){
        
        JSONObject js = new JSONObject();
        Hashtable jsResult = new Hashtable();
                
        synchronized (ConsInvAddrPortLock){
            js = ConsumerSensorInvocationAddrPortBuffer;
        }
        
        Enumeration keys = js.keys();
        
        while (keys.hasMoreElements()){
            
            Object key = keys.nextElement();
            
            if (key.toString().equalsIgnoreCase(servName)){
                try { 
                    JSONObject jsServ = js.getJSONObject(key.toString());

                    jsResult = (Hashtable) jsServ.get(targetSignalName);
   
                } catch (JSONException ex) {
                    Logger.getLogger(SOABuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        
        return jsResult;
        
    }
      
      public static void SetAdvTransmissionRequest(boolean stat){
          
          synchronized (RequestForAdvTransmissionLock){
              RequestForAdvTransmission = stat;
          }
          
      }
      
      public static boolean getAdvTransmissionRequest(){
          
          boolean stat = false;
          
          synchronized (RequestForAdvTransmissionLock){
              stat = RequestForAdvTransmission;
          }
          
          return stat;
          
      }
      
      public static void setObtainedInVal(String servName, String InSigName, Object SigValue){
         
          
          JSONObject jsSig = new JSONObject();
          
        try {
            
            
             synchronized(SJInValLock){
                if (SJInVal.has(servName)){
                     jsSig = SJInVal.getJSONObject(servName);
                     jsSig.put(InSigName, SigValue);
                     SJInVal.put(servName,jsSig);
                } else {
                    jsSig.put(InSigName, SigValue);
                    SJInVal.put(servName,jsSig);
                }
             }
            
        } catch (JSONException ex) {
            Logger.getLogger(SOABuffer.class.getName()).log(Level.SEVERE, null, ex);
        }
          
         
      }
      
      public static String getObtainedInVal(String servName, String InSigName){
          
          JSONObject jsSig = new JSONObject();
          String obj = "";
          
          synchronized(SJInValLock){
                  try {
                      
                      
                        if(SJInVal.has(servName)){
                      
                             jsSig = SJInVal.getJSONObject(servName);
                             
                             if (jsSig.has(InSigName)){
                                 obj = jsSig.getString(InSigName);
                             }
                             
                        } 
                      } catch (JSONException ex) {
                         Logger.getLogger(SOABuffer.class.getName()).log(Level.SEVERE, null, ex);
                      }  
              
             }
          
          return obj;
          
      }
      
      
      
}