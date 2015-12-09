/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systemj.common;

//import org.json.simple.*;
//import net.minidev.json.*; 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.*;
import systemj.common.SOAFacility.Support.SOABuffer;
import systemj.common.*;

/**
 *
 * @author Udayanto
 */
public class SJServiceRegistry {
    
    //service available external
    
    private static JSONObject migTempcurrentDetailedServiceRegistry = new JSONObject();
    private static Object migTempcurrentDetailedServiceRegistryLock = new Object(); 
    
    private static JSONObject currentDetailedServiceRegistry = new JSONObject();
    private static JSONObject currentShortServiceRegistry = new JSONObject();
    private static JSONObject historyServiceRegistry = new JSONObject();
    
    private static JSONObject serviceExpiryLength = new JSONObject();
    private static JSONObject serviceAdvertisementReceivedTime = new JSONObject();
    private static long lastAdvertisementTime=System.currentTimeMillis();
    
    private static JSONObject registeredNodeAvailability = new JSONObject();
    
    private static JSONObject AdvVisibList = new JSONObject();
    
    
    //Declare locks for shared variables here
    private final static Object CurrentRegistryLock = new Object();
    
    private final static Object AdvVisibListLock = new Object();
    
    private final static Object HistoryRegistryLock=new Object();
    private final static Object ServExpiryLengthLock = new Object();
    private final static Object RegistryParsingStatLock=new Object();
    
    private final static Object advTimeStampLock = new Object();
    private final static Object serviceAdvertisementReceivedTimeLock = new Object();
    private final static Object messageTokenDatabaseLock = new Object();
    private static final Object regNodeAvailLock = new Object();
            
     //end Declaring locks     
    
    //private static long min=0;
    private static boolean parsingDone=false;
    
    private static JSONObject messageTokensDatabase = new JSONObject(); //message tokens being handled within a time frame, and each time a response is retrieved with a particular token matching the database, database will be updataed and the entry is removed
   // private static JSONObject nodeStatus = new JSONObject();
    
    private static JSONObject subscriptionRequestMessage = new JSONObject();
    
    //history service may need a timestamp
    
    private static int currentServiceIndex=0,internalServiceIndex=0,historyServiceIndex=0,nodeIndex=0,replyCount=0,actualReplyCount=0;
    
    //private final static  Object lock1 = new Object();
    //private final static  Object lock2 = new Object();
   // private SJServiceRegistry(){
        
   // }

   
    
    //method to obtain the status of the information parsing, done or not
    
   
    
    
    /*
    public static JSONObject getInitAdvVisib(){
        
        JSONObject result = new JSONObject();
        
        try {
            JSONObject jsInt = obtainInternalRegistryProviderOnly();
            
            Enumeration keysJsInt = jsInt.keys();
            
            while(keysJsInt.hasMoreElements()){
                
                Object keyJsInt = keysJsInt.nextElement();
                
                JSONObject jsIndivServ = jsInt.getJSONObject(keyJsInt.toString());
                
                result.put(jsIndivServ.getString("serviceName"),jsIndivServ.getString("advVisibility"));
 
            }
            
        } catch (JSONException ex) {
            //Logger.getLogger(SJServiceRegistry.class.getName()).log(Level.SEVERE, null, ex);
            
            System.out.println("SJServiceRegistry,getAdvVisib JSONException:" +ex.getCause());
            
        }
        
        return result;
        
    }
    */
    
    public static void putMigTempServRegistry(JSONObject js){
        synchronized(migTempcurrentDetailedServiceRegistryLock){
            migTempcurrentDetailedServiceRegistry = js;
        }
    }
    
    public static JSONObject getMigTempServRegistry(){
        synchronized (migTempcurrentDetailedServiceRegistryLock){
            return migTempcurrentDetailedServiceRegistry;
        }
    }
    
    public static JSONObject getVisibServ(){
        
        JSONObject result = new JSONObject();
        
        try {
            JSONObject jsInt = obtainInternalRegistryProviderOnly();
            
            Enumeration keysJsInt = jsInt.keys();
            
            int i=1;
            
            while(keysJsInt.hasMoreElements()){
                
                Object keyJsInt = keysJsInt.nextElement();
                
                JSONObject jsIndivServ = jsInt.getJSONObject(keyJsInt.toString());
                
                if (jsIndivServ.getString("serviceVisibility").equalsIgnoreCase("available")){
                    
                    result.put(jsIndivServ.getString("serviceName"), jsIndivServ);
                    i++;
                }
                
                //result.put(jsIndivServ.getString("serviceName"),jsIndivServ.getString("initAdvVisibility"));
 
            }
            
        } catch (JSONException ex) {
            //Logger.getLogger(SJServiceRegistry.class.getName()).log(Level.SEVERE, null, ex);
            
            System.out.println("SJServiceRegistry,getAdvVisib JSONException:" +ex.getCause());
            
        }
        
        return result;
        
    }
    
    public static void setAllServAdvVisib(JSONObject jsServNVisib){
        
        try{
            JSONObject jsIntProv = obtainInternalRegistry();
        
            Enumeration keysJsIntProv = jsIntProv.keys();
        
            while (keysJsIntProv.hasMoreElements()){
            
                Object keyJsIntProv = keysJsIntProv.nextElement();
            
                JSONObject jsIndivServ = jsIntProv.getJSONObject(keyJsIntProv.toString());
                
                Enumeration keysJsServNVisib = jsServNVisib.keys();
                
                while (keysJsServNVisib.hasMoreElements()){
                    
                    Object keyJsServNVisib = keysJsServNVisib.nextElement();
                    
                    if (jsIndivServ.getString("serviceName").equalsIgnoreCase(keysJsServNVisib.toString())){
                        
                         jsIndivServ.put("serviceVisibility",jsServNVisib.getString(keyJsServNVisib.toString()));
                         
                    }
  
                }
                
                jsIntProv.put(keyJsIntProv.toString(),jsIndivServ);
                
            
            }
            
            
            
          } catch (JSONException jex){
            System.out.println("SJServiceRegistry,setAllServAdvVisib JSONException: " +jex.getCause());
        }
    }
    
    
    
    public static void setParsingStatus(boolean stat){
        
        synchronized(RegistryParsingStatLock){
            parsingDone=stat;
        }
        
    }
    
    public static boolean getParsingStatus(){
        
        
        synchronized (RegistryParsingStatLock){
            
            return parsingDone;
            
        }
        
    }

    //method to save registry to a file, features MAY NOT be available on smaller java platform
    public static void WriteCurrentServiceRegistryToFile(JSONObject registry) throws IOException{

		FileWriter file = new FileWriter("CurrentReg.txt");
                
		file.write(registry.toString());
		file.flush();
		file.close();
    }
    
    
    
    public static JSONObject ReadCurrentServiceRegistryFromFile() throws IOException {
        JSONObject js;
        BufferedReader br = new BufferedReader(new FileReader("CurrentReg.txt"));
        try {
        StringBuilder sb = new StringBuilder();
        String line;
        
            line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
        js = (JSONObject) (Object)sb;
        
        //String everything = sb.toString();
        } finally {
            br.close();
        }
        return js;
    }
    
    /**
     * Obtain own IP address from the registry file *after parsing process
     * @return own IP address in string format
     */
    public static String getOwnIPAddressFromRegistry(){
        String Addr = null;
        /*
        try {
            //ipAddr = getLocalHostLANAddress().getHostAddress();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        */
        try {
            JSONObject jsReg = obtainInternalRegistry();
            
            Enumeration keysJSReg = jsReg.keys();
            
            while(keysJSReg.hasMoreElements()){
                String servName = keysJSReg.nextElement().toString();
                
                JSONObject servDet = jsReg.getJSONObject(servName);
                
                if(servDet.has("nodeAddress")){
                     Addr = servDet.getString("nodeAddress");
                     break;
                }
                
            }
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        /*
        try {
            JSONObject js = obtainInternalRegistry();
            
            Enumeration keysJs = js.keys();
            
            while (keysJs.hasMoreElements()){
                Object key = keysJs.nextElement();

                JSONObject js2 = js.getJSONObject(key.toString());
                
                if (js2.has("nodeAddress")){
                    ipAddr = js2.getString("nodeAddress");
                    break;
                } 
            }
            
            //JSONObject js = (JSONObject) currentDetailedServiceRegistry.get("Node0");
            //JSONObject js2 =(JSONObject) js.get("service2"); //taken from service1 is enough, assuming it has a service though
            
        } catch (JSONException jex) {
            System.out.println("Cannot find particular information in getOwnIPAddressFromRegistry " +jex.getMessage());
        }
        */
        if (Addr == null){
            System.out.println("Cannot find particular IPAddress in getOwnIPAddressFromRegistry ");
        }
        
        return Addr;
    }
    
    
    //this needs modification too to comply with SSDP concept used in Upnp -- 14 Nov 2013
    public static void AppendNodeServicesToCurrentRegistry(JSONObject js, boolean IsInternalServ) throws JSONException{
        
        if (IsInternalServ){
            
            synchronized (CurrentRegistryLock){
                //currentDetailedServiceRegistry.put("Node0",js);
                currentDetailedServiceRegistry.put(SJSSCDSignalChannelMap.getLocalSSName(),js);
            }
            
            
            synchronized (HistoryRegistryLock){
                historyServiceRegistry.put(SJSSCDSignalChannelMap.getLocalSSName(),js);
            }
            
        } else {
            if (js!=null)
            {
                
                Enumeration jsKeys = js.keys();
                
                
                while (jsKeys.hasMoreElements()){
                    
                    String ServNamInd = jsKeys.nextElement().toString();
                    
                JSONObject jsCheck = (JSONObject) js.get(ServNamInd); //expect all node will have at least one service available
                //if (!nodeDictionary.has(jsCheck.get("NodeIPAddress").toString())){
                
                if (!obtainCurrentRegistry().has(jsCheck.get("NodeAddress").toString())){
                
                synchronized (CurrentRegistryLock){
                    currentDetailedServiceRegistry.put(jsCheck.get("NodeAddress").toString(), js);
                }
                
                synchronized (HistoryRegistryLock){
                    historyServiceRegistry.put(jsCheck.get("NodeAddress").toString(), js);
                }
                //JSONObject jsTemp = (JSONObject) js.get("service"+i);
                //nodeDictionary.put(jsCheck.get("NodeIPAddress").toString(),"Node"+nodeIndex);
                
                    registeredNodeAvailability.put(jsCheck.get("NodeAddress").toString(), "Available");
                
                } else {
                    System.out.println("Node with IP: " +jsCheck.get("NodeAddress")+" is already registered \n");
                    registeredNodeAvailability.put(jsCheck.get("NodeAddress").toString(), "Available");
                    //this can be the condition where checking a previously registered node availability 
                     //assuming that getting reply from currently 
                }
            //    JSONObject jsTemp = (JSONObject) js.get("service"+i);
                
          //      if (!nodeStatus.has(jsTemp.get("NodeIPAddress").toString())){
           //         nodeStatus.put(jsTemp.get("NodeIPAddress").toString(),"Available");
            //    }
                
                }
                
                
            }
        }
        //System.out.println("Updated registry: " +currentDetailedServiceRegistry.toPrettyPrintedString(3,1) +"\n");
        //System.out.println("Updated node dictionary: " +nodeDictionary+ "\n");
       // System.out.println("Updated registered node: " +registeredNodeAvailability.toPrettyPrintedString(3,1) + "\n");
    }
    
    public static void RemoveServiceOfCD(String CDName){
        try {
            JSONObject jsInt = obtainInternalRegistry();
            
            Enumeration servIndKeys = jsInt.keys();
            
            while(servIndKeys.hasMoreElements()){
                
                String servInd = servIndKeys.nextElement().toString();
                
                JSONObject jsServDet = jsInt.getJSONObject(servInd);
                
                String assocCDName = jsServDet.getString("associatedCDName");
                
                if(assocCDName.equals(CDName)){
                    jsInt.remove(servInd);
                    
                    SOABuffer.removeAdvStatOfServName(servInd);
                    
                }
                
            }
            
            UpdateAllInternalRegistry(jsInt);
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean HasNonLocalServiceCD(String cdname){
        
        boolean stat = false;
        
        try {
            JSONObject jsServ = SJServiceRegistry.obtainCurrentRegistry();
            
            Enumeration keysJsServ = jsServ.keys();
            
            while(keysJsServ.hasMoreElements()){
                
                String keyJsServ = keysJsServ.nextElement().toString();
                
                if(!keyJsServ.equals(SJSSCDSignalChannelMap.getLocalSSName())){
                    JSONObject jsIndivServs = jsServ.getJSONObject(keyJsServ);
                    
                    Enumeration keysServName = jsIndivServs.keys();
                    
                    while(keysServName.hasMoreElements()){
                        
                        String ServName = keysServName.nextElement().toString();
                        
                        JSONObject jsIndivServ = jsIndivServs.getJSONObject(ServName);
                        
                        String CDName = jsIndivServ.getString("associatedCDName");
                        
                        if(CDName.equals(cdname)){
                            stat = true;
                        }
                        
                    }
                    
                }
                
            }
            
        } catch (JSONException ex) {
            ex.printStackTrace();
            
        }
        
        return stat;
    }
    
    public static boolean IsCDFromRemoteSS(String cdname, String SS){
        
        boolean stat = false;
        
        try {
            JSONObject jsServ = SJServiceRegistry.obtainCurrentRegistry();
            
            Enumeration keysJsServ = jsServ.keys();
            
            while(keysJsServ.hasMoreElements()){
                
                String keyJsServ = keysJsServ.nextElement().toString();
                
                if(!keyJsServ.equals(SJSSCDSignalChannelMap.getLocalSSName())){
                    JSONObject jsIndivServs = jsServ.getJSONObject(keyJsServ);
                    
                    Enumeration keysServName = jsIndivServs.keys();
                    
                    while(keysServName.hasMoreElements()){
                        
                        String ServName = keysServName.nextElement().toString();
                        
                        JSONObject jsIndivServ = jsIndivServs.getJSONObject(ServName);
                        
                        String CDName = jsIndivServ.getString("associatedCDName");
                        
                        if(CDName.equals(cdname) && keyJsServ.equals(SS)){
                            stat = true;
                        }
                        
                    }
                    
                }
                
            }
            
        } catch (JSONException ex) {
            ex.printStackTrace();
            
        }
        
        return stat;
    }
    
    public static String GetCDRemoteSSLocation(String cdname){
        
        String stat = "";
        
        try {
            JSONObject jsServ = SJServiceRegistry.obtainCurrentRegistry();
            
            Enumeration keysJsServ = jsServ.keys();
            
            while(keysJsServ.hasMoreElements()){
                
                String keyJsServ = keysJsServ.nextElement().toString();
                
                if(!keyJsServ.equals(SJSSCDSignalChannelMap.getLocalSSName())){
                    JSONObject jsIndivServs = jsServ.getJSONObject(keyJsServ);
                    
                    Enumeration keysServName = jsIndivServs.keys();
                    
                    while(keysServName.hasMoreElements()){
                        
                        String ServName = keysServName.nextElement().toString();
                        
                        JSONObject jsIndivServ = jsIndivServs.getJSONObject(ServName);
                        
                        String CDName = jsIndivServ.getString("associatedCDName");
                        
                        if(CDName.equals(cdname) ){
                            stat = keyJsServ;
                        }
                        
                    }
                    
                }
                
            }
            
        } catch (JSONException ex) {
            ex.printStackTrace();
            
        }
        
        return stat;
    }
    
    public static void RemoveUnavailableNodeServicesFromCurrentRegistryOfSSName(String SSName){
       // try {
            synchronized (CurrentRegistryLock) {
            if (currentDetailedServiceRegistry.has(SSName)){
                
               
                    currentDetailedServiceRegistry.remove(SSName);
                
                
                //synchronized regnodavail here
               
            } else {
                System.out.println("SJServiceRegistry, RemoveUnavailableNodeServicesOfIPAddressFromCurrentRegistry : IP address not listed in the registry, not removing");
            }
            
            }
            
            // synchronized (regNodeAvailLock){
             //    registeredNodeAvailability.put(registeredNodeAvailability.getString(ipAddress),"Unavailable");
             //}
            
            
       // } catch (JSONException ex) {
       //     System.out.println("SJServiceRegistry,RemoveUnavailableNodeServicesOfIPAddress JSONException:" +ex.getMessage());
      //  }
    }
    
    public static void RemoveUnavailableNodeServicesFromCurrentRegistry(){
        if (registeredNodeAvailability != null){
            Enumeration keys = registeredNodeAvailability.keys();
            while (keys.hasMoreElements()){
                
                //'key' here equals to IP address in string format
                String key = keys.nextElement().toString();
                try {
                    if (registeredNodeAvailability.get(key).toString().equalsIgnoreCase("Unavailable")){
                        
                        synchronized(CurrentRegistryLock){
                            currentDetailedServiceRegistry.remove(key);
                        }
                        
                        
                        //if (nodeDictionary.has(key)){
                           // String nodeID = nodeDictionary.get(key).toString();
                            
                            //if (currentServiceRegistry.has(nodeID)) {currentServiceRegistry.remove(nodeID);}
                            //nodeDictionary.remove(key);
                        //}
    
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(SJServiceRegistry.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    //call this just before doing query
    public static void setAllNodeAvailabilityToUnavailable(){
        
        if (registeredNodeAvailability != null){
            Enumeration keys = registeredNodeAvailability.keys();
        
        while (keys.hasMoreElements()){
              Object key = keys.nextElement();
            try {
                registeredNodeAvailability.put(key.toString(), "Unavailable");
                //System.out.println("What: " +key);
            } catch (JSONException ex) {
                Logger.getLogger(SJServiceRegistry.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }     
    }

    
    /**
     * This function obtain expected service type from the consumer
     * @return a list of service type expected by the consumer in JSON to add in the discovery message
     */
    public static JSONObject getConsumerExpectedServiceType(){
        
        int debug=0;
        
        int consumerIndex=0;
        JSONObject js = new JSONObject();
        JSONObject jsIndex = new JSONObject();
        
        try {
            JSONObject ownServiceDesc = obtainInternalRegistry();
            
            if (debug==1) System.out.println("SJServiceRegistry getConsumerExpectedServiceType debug internal service list: " +ownServiceDesc.toPrettyPrintedString(2, 0));
            
                    Enumeration keys = ownServiceDesc.keys();  //service1, service2, and so on
                    while (keys.hasMoreElements()){
                        Object key = keys.nextElement();
                        
                        JSONObject indivServiceDesc = ownServiceDesc.getJSONObject(key.toString());
                        
                        if (indivServiceDesc.getString("serviceRole").equalsIgnoreCase("consumer")){
                            
                            consumerIndex = consumerIndex+1;
                            jsIndex.put(Integer.toString(consumerIndex),indivServiceDesc.getString("serviceType"));
                            js.put("expectedServiceType",jsIndex);
                        }
  
                    }
                    
                    if (!js.has("expectedServiceType")){
                        js.put("expectedServiceType",new JSONObject());
                    }
                    
            
            if (debug==1) System.out.println("SJServiceRegistry getConsumerExpectedServiceType debug: " +js.toPrettyPrintedString(2, 0));
            } catch (JSONException ex) {
                System.out.println("SJServiceRegistry getConsumerExpectedServiceType JSONException: " +ex.getMessage());
            }
           
            return js;
            
    }
    
    
    /**
     * This function obtain expected service type from the consumer
     * @return a list of service type expected by the consumer in JSON to add in the discovery message
     */
    public static JSONObject getConsumerExpectedServiceActionType(){
        int consumerIndex=0;
        JSONObject js = new JSONObject();
        JSONObject jsIndex = new JSONObject();
        
        try {
            JSONObject ownServiceDesc = obtainInternalRegistry();
            
                    Enumeration keys = ownServiceDesc.keys();
                    while (keys.hasMoreElements()){
                        String key = keys.nextElement().toString();
                        
                        JSONObject indivServiceDesc = ownServiceDesc.getJSONObject(key);
                        
                        if (indivServiceDesc.getString("serviceRole").equalsIgnoreCase("Consumer")){
                            
                            consumerIndex = consumerIndex+1;
                            jsIndex.put(Integer.toString(consumerIndex),indivServiceDesc.getString("actionType"));
                            js.put("expectedActionType",jsIndex);
                        }
  
                    }
            
            
            } catch (JSONException ex) {
                System.out.println("SJServiceRegistry, getConsumerExpectedServiceActionType: " +ex.getMessage());
            }
        
            return js;
            
    }
    
   
  
    /* //kept for later use
    public synchronized void RemoveNodeServicesFromCurrentRegistry(JSONObject js, boolean internalServ){
        
        if (internalServ){
            currentServiceRegistry.put("NodeOwn",js);
        } else {
            
            JSONObject jsCheck = (JSONObject) js.get("service1");
            if (!nodeDictionary.containsKey(jsCheck.get("NodeIPAddress").toString())){
                nodeIndex++;
                currentServiceRegistry.put("Node"+nodeIndex, js);
                
                JSONObject jsTemp = (JSONObject) js.get("service1");
                nodeDictionary.put(jsTemp.get("NodeIPAddress"),"Node"+nodeIndex);
            }
      
        }    
    }
    
    public synchronized void RemoveOneServiceFromCurrentRegistry(String NodeID,Object keyObj){
        try {
            //for (int i=0;i<currentServiceRegistry.size();i++){
                JSONObject js = currentServiceRegistry.getJSONObject(NodeID);
                
                if (js.){
                    //currentServiceRegistry.get(keyObj);
                }   
        } catch (JSONException ex) {
            Logger.getLogger(SJServiceRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    * */
    
    //for pattern matching --> need modification after the service interface is resolved
    
    public static boolean IsServicesWithTypeAvailable(String type){
        
        boolean status = false;
        if (currentDetailedServiceRegistry!=null){
           // for (int i=1;i<=currentServiceRegistry.length();i++){
                try {
                    Enumeration keys = currentDetailedServiceRegistry.keys();
                    while (keys.hasMoreElements()){
                        Object key = keys.nextElement();
                        JSONObject jsTemp = (JSONObject) currentDetailedServiceRegistry.get(key.toString());
                        
                        Enumeration keysJSTemp = jsTemp.keys();
                        
                        while (keysJSTemp.hasMoreElements()){
                            
                            String keyJSTemp = keysJSTemp.nextElement().toString();
                            
                            JSONObject jsService = (JSONObject) jsTemp.get(keyJSTemp);
                            if (jsService.hasValue(type)){
                                status=true;
                            } else {
                                status=false;
                            }
                        }
                    }
                    //return false;
                } catch (JSONException ex) {
                    Logger.getLogger(SJServiceRegistry.class.getName()).log(Level.SEVERE, null, ex);
                    //return false;
                }
           // }
            return status;
        } else {
            System.out.println("service registry is still empty!");
            return status;
        } 
        
    }
 
    //update service registry by removing unavailable services from an unavailable node
 //   public static void RemoveNodeServicesFromCurrentRegistry(String IPAddr){
        
            //String nodeID = nodeDictionary.get(IPAddr).toString();
           // if (currentServiceRegistry.hasValue(nodeID)){
   //             currentServiceRegistry.remove(IPAddr);
   //             nodeDictionary.remove(IPAddr);
           // }
        
  //  }
    
    /* future use
    public static void obtainInterfaceFromHistoryRegistry(){
        
    }
    */
    //below is reserved for next development of service registry info retrieval
    
    public static JSONObject obtainHistoryRegistry(){  //where to use this? (usable for consumer which want to check whether it needs the previously available service without having to discover that again)
        return historyServiceRegistry;
    }
    
    public static void setupCurrentRegistry(JSONObject js){
        JSONObject js1 = currentDetailedServiceRegistry;
        js1.clear();
        js1.putAll(js);
        currentDetailedServiceRegistry = js1;
        System.out.println(js1);        
       // currentServiceIndex=internalServiceRegistry.size();       
    }
    
    public static JSONObject AdvertiseNodeServices(String advMsgType) {
        try{
        System.out.println("current internal Registry: " +currentDetailedServiceRegistry.getString(SJSSCDSignalChannelMap.getLocalSSName()));
        
        if (obtainCurrentRegistry()!=null){
            JSONObject jsTemp = new JSONObject();
            //try{
            //JSONObject jsTemp = AppendMessageType((JSONObject) currentDetailedServiceRegistry.get("Node0"), advMsgType);
            jsTemp = AppendMessageType(obtainInternalRegistry(), advMsgType);
            AppendSourceIPAddressToMessage(jsTemp);
            //} catch (JSONException jex){
            //    System.err.println(jex.getMessage());
            //}
            
            
            //return (JSONObject) currentServiceRegistry.get("Node0");
            return jsTemp;
        } else {
            return new JSONObject();
        } 
        } catch (JSONException jex){
            System.err.println(jex.getMessage());
            return new JSONObject();
        }
    }
    
    
    
    /**
     * 
     * @return 
     */
    public static JSONObject obtainInternalRegistryProviderOnly(){
        
        JSONObject jsInt = new JSONObject();
        JSONObject jsIntProviderOnly = new JSONObject();
        
        try {
            jsInt = obtainInternalRegistry();
  
            Enumeration keysJs = jsInt.keys();
            
            while (keysJs.hasMoreElements()){
                
                Object key = keysJs.nextElement();
                
                JSONObject jsIntIndiv = jsInt.getJSONObject(key.toString());
                
                if (jsIntIndiv.getString("serviceRole").equalsIgnoreCase("provider")){
                    //remove
                    
                    
                    jsIntProviderOnly.put(key.toString(),jsInt.getJSONObject(key.toString()));
                }
                
            }
            
            
            
        } catch (JSONException ex) {
            System.out.println("SJServiceRegistry,obtainInternalRegistryProviderOnly JSONException:");
                   
            ex.printStackTrace();
        }
        
        return jsIntProviderOnly;
        
    }
    
    
    public static JSONObject obtainInternalRegistryVisibleProviderOnly(){
        
        JSONObject jsInt = new JSONObject();
        JSONObject jsIntProviderOnly = new JSONObject();
        
        try {
            jsInt = obtainInternalRegistry();
  
            Enumeration keysJs = jsInt.keys();
            
            while (keysJs.hasMoreElements()){
                
                Object key = keysJs.nextElement();
                
                JSONObject jsIntIndiv = jsInt.getJSONObject(key.toString());
                
                if (jsIntIndiv.getString("serviceRole").equalsIgnoreCase("provider")){
                    
                    
                    JSONObject jsAdvVisib = SOABuffer.getAllAdvVisib();
                    
                    if (jsAdvVisib.getString(jsIntIndiv.getString("serviceName")).equalsIgnoreCase("visible")){
                        jsIntProviderOnly.put(key.toString(),jsInt.getJSONObject(key.toString()));
                    }
  
                }
                
            }
            
            
            
        } catch (JSONException ex) {
            System.out.println("SJServiceRegistry,obtainInternalRegistryProviderOnly JSONException:" +ex.getMessage());
        }
        
        return jsIntProviderOnly;
        
    }
    
    
    /**
     * 
     * @return 
     */
    public static JSONObject obtainInternalRegistryConsumerOnly(){
        
        JSONObject jsInt = new JSONObject();
        JSONObject jsIntConsumerOnly = new JSONObject();
        
        try {
            jsInt = obtainInternalRegistry();
            
            
            Enumeration keysJs = jsInt.keys();
            
            while (keysJs.hasMoreElements()){
                
                Object key = keysJs.nextElement();
                
                JSONObject jsIntIndiv = jsInt.getJSONObject(key.toString());
                
                if (jsIntIndiv.getString("serviceRole").equalsIgnoreCase("consumer")){
                    //remove
                    jsIntConsumerOnly.put(key.toString(),jsInt.getJSONObject(key.toString()));
                }
                
            }

            
        } catch (JSONException ex) {
            System.out.println("SJServiceRegistry,obtainInternalRegistryConsumerOnly JSONException:" +ex.getMessage());
        }
        
        return jsIntConsumerOnly;
        
    }
    
    /**
     * Method to separate a service entity with both provider and consumer capability from the consumer only and the provider only --> for future use
     * @return 
     */
    //For future use
    public static JSONObject obtainInternalRegistryBothProviderAndConsumer(){
        
        JSONObject jsInt = new JSONObject();
        JSONObject jsIntProviderOnly = new JSONObject();
        
        try {
            jsInt = obtainInternalRegistry();
  
            Enumeration keysJs = jsInt.keys();
            
            while (keysJs.hasMoreElements()){
                
                Object key = keysJs.nextElement();
                
                JSONObject jsIntIndiv = jsInt.getJSONObject(key.toString());
                
                if (jsIntIndiv.getString("serviceRole").equalsIgnoreCase("providerandconsumer")){
                    //remove
                    jsIntProviderOnly.put(key.toString(),jsInt.getJSONObject(key.toString()));
                }
                
            }
            
            
            
        } catch (JSONException ex) {
            System.out.println("SJServiceRegistry,obtainInternalRegistryProviderAndConsumerOnly JSONException:" +ex.getMessage());
        }
        
        return jsIntProviderOnly;
        
    }
    
    public static JSONObject obtainInternalRegistry() throws JSONException{
        
        JSONObject intReg = new JSONObject();
        
        //System.out.println("obtained internal Registry: " +currentDetailedServiceRegistry.getJSONObject("Node0").toPrettyPrintedString(2, 0));
        if (!currentDetailedServiceRegistry.toString().equalsIgnoreCase("{}")){
            
            synchronized (CurrentRegistryLock){
                intReg= currentDetailedServiceRegistry.getJSONObject(SJSSCDSignalChannelMap.getLocalSSName());
            }
            
            //System.out.println("SJServiceRegistry Current Registry: " +currentDetailedServiceRegistry.toPrettyPrintedString(2, 0));
            return intReg;
        } else {
            return new JSONObject();
        } 
    }
    
    public static JSONObject obtainCurrentRegistry() throws JSONException{
        //System.out.println("current Registry: " +currentDetailedServiceRegistry.toPrettyPrintedString(2, 0));
        
        JSONObject jsAllCurr = new JSONObject();
        
        synchronized (CurrentRegistryLock){
            jsAllCurr = currentDetailedServiceRegistry;
        }
        
        
        return jsAllCurr;
        
    }
    
    public static JSONObject obtainCurrentRegistryVisibleOnly() throws JSONException{
        //System.out.println("current Registry: " +currentDetailedServiceRegistry.toPrettyPrintedString(2, 0));
        
        JSONObject jsAllCurr = new JSONObject();
        JSONObject jsAllIndivSS = new JSONObject();
        
        synchronized (CurrentRegistryLock){
            jsAllCurr = currentDetailedServiceRegistry;
            
            Enumeration keysjsAllCurr = jsAllCurr.keys();
            
            while(keysjsAllCurr.hasMoreElements()){
                
                String keyJSAllCurr = keysjsAllCurr.nextElement().toString();
                
                JSONObject jsIndivSS = jsAllCurr.getJSONObject(keyJSAllCurr);
                
                Enumeration keyjsIndivSSServiceName = jsIndivSS.keys();
                
                JSONObject jsAllIndivServs = new JSONObject();
                
                while(keyjsIndivSSServiceName.hasMoreElements()){
                    
                    String keyServName = keyjsIndivSSServiceName.nextElement().toString();
                    
                    JSONObject jsIndivServ = jsIndivSS.getJSONObject(keyServName);
                    
                    if(jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider")){
                        
                        if(jsIndivServ.getString("serviceVisibility").equalsIgnoreCase("visible") || jsIndivServ.getString("serviceVisibility").equalsIgnoreCase("available")){
                            jsAllIndivServs.put(keyServName, jsIndivServ);
                        }
                        
                    }
                    
                }
                
                if(jsAllIndivServs.length()>0){
                    jsAllIndivSS.put(keyJSAllCurr, jsAllIndivServs);
                }
                
            }
            
        }
        
        return jsAllIndivSS;
        
    }
    
    public static JSONObject obtainCurrentRegistryProviderOnly() throws JSONException{
        //System.out.println("current Registry: " +currentDetailedServiceRegistry.toPrettyPrintedString(2, 0));
        
        JSONObject jsAllCurrProv = new JSONObject();
        
        JSONObject jsIntProv = obtainInternalRegistryProviderOnly();
        
        jsAllCurrProv = obtainCurrentExternalService();
        jsAllCurrProv.put(SJSSCDSignalChannelMap.getLocalSSName(), jsIntProv.get(SJSSCDSignalChannelMap.getLocalSSName()));
        
        return jsAllCurrProv;
        
    }
    
    public static void UpdateAllInternalRegistry(JSONObject js){
        
        synchronized(CurrentRegistryLock){
            try {
                //currentDetailedServiceRegistry.remove(SJSSCDSignalChannelMap.getLocalSSName());
                currentDetailedServiceRegistry.put(SJSSCDSignalChannelMap.getLocalSSName(), js);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    
    public static String returnConsumerServiceTypeOfServiceName(String servName){
   
        String servType=null;
        
        try{
            
            JSONObject jsIntCons = obtainInternalRegistryConsumerOnly();
        
            Enumeration keysJsIntCons = jsIntCons.keys();
        
            while (keysJsIntCons.hasMoreElements()){
            
                Object key = keysJsIntCons.nextElement();
            
                JSONObject jsIndivServ = jsIntCons.getJSONObject(key.toString());
            
                if (jsIndivServ.getString("serviceName").equalsIgnoreCase(servName)){
                    
                    servType = jsIndivServ.getString("serviceType");
                    
                }
                
            }
            
           } catch (JSONException jex){
               System.out.println("SJServiceRegistry, returnConsumerServTypeOfServName :" +jex.getMessage());
           }
        
        if (servType!=null){
            return servType;
        } else {
            return "";
        }
        
        
        
    }
    
    public static JSONObject obtainCurrentExternalService() throws JSONException{
        //System.out.println("current Registry: " +currentDetailedServiceRegistry.toPrettyPrintedString(2, 0));
        
        JSONObject jsAllCurrExt = new JSONObject();
        jsAllCurrExt =  obtainCurrentRegistry();

        jsAllCurrExt.remove(SJSSCDSignalChannelMap.getLocalSSName());
        
        return jsAllCurrExt;
        
    }
    
    
    
    public synchronized Object obtainPastServiceInfo(Object key){
        Object data=null;
        return data;
    }

    
    public synchronized int getTotalInternalService(){
        return internalServiceIndex;
    }

    private static JSONObject AppendMessageType(JSONObject js,String msgType){
        try {
            js.put("messageType", msgType);
        } catch (JSONException ex) {
            System.out.println("What happens in AppendMessageType: " +ex.getMessage());
        }
        return js;
    }
    
    
    
    //mybe better to create different message for each to avoid conflict and race condition
    
    
    public static String getSourceIPAddress(){
        //InetAddress addr=null;
        String addrString="";
        int debug=1;
        try {
            
            JSONObject currReg = obtainCurrentRegistry();
            if (currReg.has(SJSSCDSignalChannelMap.getLocalSSName())){
                //JSONObject js = currentDetailedServiceRegistry.getJSONObject("Node0");
                
                JSONObject js = obtainInternalRegistry();
                
                 Enumeration keys = js.keys();

                  while (keys.hasMoreElements()){
                      Object key = keys.nextElement();
                      JSONObject js2 = js.getJSONObject(key.toString());
                      //  JSONObject js2 = js.getJSONObject("service1");
                      if (js2.has("nodeAddress")){
                         
                          addrString = js2.getString("nodeAddress");
                          
                          
                          
                          //addr = InetAddress.getByName(js2.getString("nodeAddress"));
                          break;
                      } else {
                          System.out.println("No information about Node address in currentDetailedServiceReg");
                      }
                      
                 }

            } else {
                System.out.println("Node doesn't have any services");
            }
            
        } catch (JSONException ex) {
            System.out.println("JSON problem in getSourceIP: " +ex.getMessage());
        } 
        //catch (UnknownHostException uex){
        //    System.out.println("no known host in getSourceIP: " +uex.getMessage());
        //}
        
        return addrString;
    }
   
    
    
    
    /**
     * 
     * @param serviceType
     * @return 
     */
    
    /*
    public static String getServicesAvailabilityOfType(String serviceType)
    {
        int debug=0;
        //InetAddress addr = null;
        StringBuffer addr=new StringBuffer();
        final String avail="Available";
        final String noAvail="Not Available";
        
        try{
            
            JSONObject currReg = obtainCurrentRegistry();
            
            Enumeration keysServ = currReg.keys();
            
            while (keysServ.hasMoreElements()){
            Object key = keysServ.nextElement();
            
            if (!key.toString().equalsIgnoreCase("Node0")) //*temporary, to search service located in other Node
            {
                
                 JSONObject jsServNum = (JSONObject) currReg.get(key.toString());
            
                Enumeration keysServNum = jsServNum.keys();
            
                while (keysServNum.hasMoreElements()){
                
                    Object key2 = keysServNum.nextElement();
                
                    JSONObject jsServNumVar =  jsServNum.getJSONObject(key2.toString());
                
                //Enumeration keysServNumVarList = jsServNumVar.keys();
                
                //while (keysServNumVarList.hasMoreElements()){
                    //Object key3 = keysServNumVarList.nextElement();
                    
                    //JSONObject jsServNumVarList = jsServNumVar.getJSONObject(key3.toString());
                    
                  //  try {
                    if (!jsServNumVar.getString("serviceRole").equalsIgnoreCase("consumer")){
                        if (serviceType.equalsIgnoreCase(jsServNumVar.getString("serviceType")))
                            {
                                //addr = InetAddress.getByName(jsServNumVar.getString("nodeIPAddress"));
                                addr.append(avail);
                                break;
                            } else {
                                addr.append(noAvail);
                            }
                     }
 
            }
                
            }

        }
            
            
        } catch (JSONException jex){
            System.out.println("getServicesAvailabilityOfTypeJSONException: " +jex.getMessage());
        }
        
        if (debug==1) System.out.println("getServicesIPLocationType Node Address: " +addr);
        
        return addr.toString();
    }
    */
  
    public static boolean IsPureConsumer(){
        
        boolean isPureConsumer = true;
        try {
            JSONObject js = obtainInternalRegistry();
            
            Enumeration keysServ = js.keys();
            
            while (keysServ.hasMoreElements()){
                Object key = keysServ.nextElement();
                
                JSONObject jsServList = (JSONObject) js.get(key.toString());
                
               if (jsServList.getString("serviceRole").equalsIgnoreCase("provider")){
                   isPureConsumer = false;
                   break;
               }
                
            }
            
        } catch (JSONException ex) {
            System.out.println("IsPureConsumer problem: " +ex.getMessage());
        }
        return isPureConsumer;
    } 
    
     public static JSONObject AppendSourceIPAddressToMessage(JSONObject message){
        try {
            
            JSONObject currReg = obtainCurrentRegistry();
            
            //Enumeration keysServ = currReg.keys();
            
            if (currReg.has(SJSSCDSignalChannelMap.getLocalSSName())){
                JSONObject js = obtainInternalRegistry();
                
                 Enumeration keys = js.keys();

                  while (keys.hasMoreElements()){
                      Object key = keys.nextElement();
                      JSONObject js2 = js.getJSONObject(key.toString());
                      if (js2.has("nodeAddress")){
                          message.put("sourceAddress", js2.getString("nodeAddress"));
                      } else {
                          System.out.println("No information about Node address in currentDetailedServiceReg");
                      }
                      
                  }

            } else {
                System.out.println("Node doesn't have any services");
            }
            
        } catch (JSONException ex) {
            System.out.println("What happens in AppendSourceIPAddress: " +ex.getMessage());
        }
        return message;
    }
     
    
    
    
  //  public static JSONObject getMessage(String message){
  //      if (message.equalsIgnoreCase("HelloMessage")){
  //          return helloMessage;
  //      } else if (message.equalsIgnoreCase("BroadcastDiscoveryMessage")){
  //          return broadcastDiscoveryMessage;
  //      } else if (message.equalsIgnoreCase("UnicastDiscoveryMessage")){
   //         return unicastDiscoveryMessage;
  //      } else if (message.equalsIgnoreCase("ResponseDiscoveryMessage")){
   //         return replyDiscoveryMessage;
   //     } else {
   //         return new JSONObject();
   //     }
 //   }
    
    
    //*This one is UNFINISHED!
    public static String getServiceParametersOfInternalService(String serviceType){
        try {
            JSONObject servList =  obtainInternalRegistry();
            
            Enumeration keys = servList.keys();
            
            while (keys.hasMoreElements()){
                    Object key = keys.nextElement();

                    JSONObject jsServ = (JSONObject)servList.get(key.toString());
                    
                    
                    JSONObject jsStateVar = (JSONObject) jsServ.get("stateVariable");
                    
                    
                    Enumeration keysStateVar = jsStateVar.keys();
                    
                    while (keysStateVar.hasMoreElements()){
                        Object keyStateVar = keysStateVar.nextElement();

                        JSONObject jsStateVarAttr = (JSONObject)servList.get(keyStateVar.toString());


                        if (jsStateVarAttr.hasValue(serviceType))
                        {
                            
                            //*UNFINISHED* !!!
                            
                        }

                }
                    
                    
                }
            
        } catch (JSONException jex) {
           System.out.println(jex.getMessage());
        }
        
        return String.format("");
    }
    
    public static int getMessageTransmissionPort(JSONObject js){
        int port=0;
        try {
            if (js.has("port")){
                port = Integer.parseInt(js.getString("port"));
            } 
        } catch (JSONException ex) {
            //Logger.getLogger(SJServiceRegistry.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
        return port;
    }
    
    public static int getResponseMessageTransmissionPort(JSONObject js){
        int port=0;
        try {
            if (js.has("portForResponse")){
                port = Integer.parseInt(js.getString("portForResponse"));
            } 
        } catch (JSONException ex) {
            //Logger.getLogger(SJServiceRegistry.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
        return port;
    }
    
    //public static InetAddress getIPUnicastAddress(){
      //  InetAddress ip = InetAddress.getByName(ip);
    //}
    //needs
    
    //Make discovery message, MUST contain type of message "discoveryMessage", service target (if any, choose all, or specific service), portFor response used o receive reply
   

    
    public static void resetExpiryAdvertisedService(JSONObject jsMessage){
        
            if(jsMessage.has("advertisementExpiry"))
            {
                try {
                    
                    synchronized (serviceAdvertisementReceivedTimeLock){
                        serviceAdvertisementReceivedTime.put(jsMessage.getString("SourceIPAddress"), jsMessage.getString("advertisementExpiry"));
                    }
    
                } catch (JSONException ex) {
                     System.out.println("Something happens in resetExpiryAdvertisedService:" +ex.getMessage());
                }
            }
    }
   
    
    /**
     * This method check for any expired service or about to expire service
     * @return the node Address of the machine having services about to expire
     */
    
    public static boolean HasServiceConsumer(){
        
        JSONObject jsInt = obtainInternalRegistryConsumerOnly();
        
        if (jsInt.isEmpty()){
            return false;
        } else {
            return true;
        }
        
    }
    
     public static boolean HasServiceConsumerOnly(){
        
        JSONObject jsIntCon = obtainInternalRegistryConsumerOnly();
        JSONObject jsIntProv = obtainInternalRegistryProviderOnly();
        
        if (!jsIntCon.isEmpty() && jsIntProv.isEmpty()){
            return true;
        } else {
            return false;
        }
        
    }
     
      public static boolean HasServiceProviderOnly(){
        
        JSONObject jsIntCon = obtainInternalRegistryConsumerOnly();
        JSONObject jsIntProv = obtainInternalRegistryProviderOnly();
        
        if (jsIntCon.isEmpty() && !jsIntProv.isEmpty()){
            return true;
        } else {
            return false;
        }
        
    }
    
    public static boolean HasServiceConsumerAndProvider(){
        
        JSONObject jsIntCon = obtainInternalRegistryConsumerOnly();
        JSONObject jsIntProv = obtainInternalRegistryProviderOnly();
        
        
        if (!jsIntCon.isEmpty() && !jsIntProv.isEmpty()){
            return true;
        } else {
            return false;
        }
        
    }
    
    public static boolean HasServiceConsumerOrProvider(){
        
        JSONObject jsIntCon = obtainInternalRegistryConsumerOnly();
        JSONObject jsIntProv = obtainInternalRegistryProviderOnly();
        
        
        if (!jsIntCon.isEmpty() || !jsIntProv.isEmpty()){
            return true;
        } else {
            return false;
        }
        
    }
    
    
    public static boolean HasServiceProvider(){
        
        JSONObject jsInt = obtainInternalRegistryProviderOnly();
        
        if (jsInt.isEmpty()){
            return false;
        } else {
            return true;
        }
        
    }
    
    //need to get node with expired advertisement
    public static Hashtable checkServiceExpiry(){
        
        int index=1;
        
        Hashtable ExpAddrList = new Hashtable();
        
        String expiredServiceAddr =null;

       // long currenttime = System.currentTimeMillis();
        
        JSONObject jsServAdvRecTime = new JSONObject();
        
        synchronized(serviceAdvertisementReceivedTimeLock){
            
            Enumeration keysServAdvRecTime = serviceAdvertisementReceivedTime.keys();
            
            while(keysServAdvRecTime.hasMoreElements()){
                String keyServAdvTime = keysServAdvRecTime.nextElement().toString();
                
                try {
                    jsServAdvRecTime.put(keyServAdvTime, serviceAdvertisementReceivedTime.getLong(keyServAdvTime));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                
            }
            //jsServAdvRecTime = serviceAdvertisementReceivedTime;
        }
        
        
        
        Enumeration keys = jsServAdvRecTime.keys(); //this will get each sourceIPaddress
          
          while (keys.hasMoreElements()){
              Object key = keys.nextElement();
            try {
                long deltaT = System.currentTimeMillis()-jsServAdvRecTime.getLong(key.toString());
                
                //if (deltaT>=0.8*serviceExpiryLength.getLong(key.toString())){
                    
               //     expiredServiceAddr=key.toString();
                    
              //  } else 
                
                JSONObject jsServExpLength = new JSONObject();
                JSONObject currReg = obtainCurrentRegistry();
                
                synchronized (ServExpiryLengthLock){
                    
                    Enumeration keysServExpiryLength = serviceExpiryLength.keys();
                    
                    while(keysServExpiryLength.hasMoreElements()){
                        
                        String keyServExpiry = keysServExpiryLength.nextElement().toString();
                        
                        jsServExpLength.put(keyServExpiry, serviceExpiryLength.getLong(keyServExpiry));
                        
                    }
                    
                     // jsServExpLength = serviceExpiryLength;
                }
                
                

                if (jsServExpLength.has(key.toString())){
                    if (deltaT>jsServExpLength.getLong(key.toString())){
                    
                        if (currReg.has(key.toString())){    
                        //expiredServiceAddr = key.toString();
                        ExpAddrList.put(Integer.toString(index),key.toString());
                        
                        synchronized (ServExpiryLengthLock){
                            serviceExpiryLength.remove(key.toString());
                        }

                        if (jsServAdvRecTime.has(key.toString())){
                        
                            synchronized(serviceAdvertisementReceivedTimeLock){
                                serviceAdvertisementReceivedTime.remove(key.toString());
                            }
   
                        }
                    }
                    //servExpired = true;
                    //registeredNodeAvailability.put(key.toString(), "Unavailable");
                    //currentDetailedServiceRegistry.remove(key.toString());
                    }
                }
                
             }  catch (JSONException ex) {
                //Logger.getLogger(SJServiceRegistry.class.getName()).log(Level.SEVERE, null, ex);
                 System.err.println("SJServiceRegistry,checkServiceExpiry JSONException:  " +ex.getMessage());
                 ex.printStackTrace();
             }
          }
          /*
          if (expiredServiceAddr==null){
              return "nothing";
          } else {
              return expiredServiceAddr;
          }
          */
          return ExpAddrList;
          
    }
    
    /**
     * checking expiry to request advertise
     * @return 
     */
    public static Hashtable checkServiceExpiryForAdvertiseRequest(){
        //String expiredServiceAddr =null;
        Hashtable expiredAdv = new Hashtable();
        
        int ind = 1;
        
        long deltaT=0;
       // long currenttime = System.currentTimeMillis();
        //Enumeration keys = serviceAdvertisementReceivedTime.keys(); //this will get each sourceIPaddress
          
        try {
        
            JSONObject jsServAdvRecTime = new JSONObject();
            JSONObject jsServExpLength = new JSONObject();
            
            synchronized (serviceAdvertisementReceivedTimeLock){
                
                Enumeration keys1 = serviceAdvertisementReceivedTime.keys();
                
                while(keys1.hasMoreElements()){
                    String key1 = keys1.nextElement().toString();
                    jsServAdvRecTime.put(key1, serviceAdvertisementReceivedTime.getLong(key1));
                }
                
                //jsServAdvRecTime = serviceAdvertisementReceivedTime;
            }
            
            synchronized (ServExpiryLengthLock){
                //jsServExpLength = serviceExpiryLength;
                
                Enumeration keys2 = serviceExpiryLength.keys();
                
                while(keys2.hasMoreElements()){
                    String key2 = keys2.nextElement().toString();
                    jsServAdvRecTime.put(key2, serviceExpiryLength.getLong(key2));
                }
                
            }
            
          Enumeration keys = jsServAdvRecTime.keys();
            
          while (keys.hasMoreElements()){
              String key = keys.nextElement().toString();
            
                if (jsServAdvRecTime.has(key)){
                
                    deltaT = System.currentTimeMillis()-jsServAdvRecTime.getLong(key);
                
                //if (deltaT>=0.8*serviceExpiryLength.getLong(key.toString())){
                    
               //     expiredServiceAddr=key.toString();
                    if (jsServExpLength.has(key)){
              //  } else 
                        if (deltaT>=0.6*(jsServExpLength.getLong(key))){
                    
                            expiredAdv.put(Integer.toString(ind),key);
                            ind++;
                    
                        }
                    }
                 }
                
                 }
             }  catch (JSONException ex) {
                 ex.printStackTrace();
                 
                 
             }
          
              return expiredAdv;
          
          
    }
    
          //*this one is still unused??
    
    
          public static void UpdateServiceRegistryBasedOnServiceExpiry(String nodeAddress){
        
       // long currenttime = System.currentTimeMillis();
        //Enumeration keys = serviceAdvertisementReceivedTime.keys(); //this will get each sourceIPaddress
                JSONObject jsServRcvdTime = new JSONObject();
                JSONObject jsServExp = new JSONObject();
             
          
              //Object key = keys.nextElement();
            try {
                
                
                
                synchronized (serviceAdvertisementReceivedTimeLock){
                    jsServRcvdTime = serviceAdvertisementReceivedTime;
                }
                
                
                
                if (jsServRcvdTime.has(nodeAddress)){
                    long deltaT = System.currentTimeMillis()-jsServRcvdTime.getLong(nodeAddress);
                    
                    synchronized (ServExpiryLengthLock){
                        jsServExp = serviceExpiryLength;
                    }
                    
                    if (deltaT>jsServExp.getLong(nodeAddress)){
                    
                        
                            
                          synchronized (regNodeAvailLock){
                            
                                if (registeredNodeAvailability.has(nodeAddress)){
                                
                                    registeredNodeAvailability.put(nodeAddress, "Unavailable");
                            
                                }
                            }
                    
                        synchronized (CurrentRegistryLock){  
                          
                            if (currentDetailedServiceRegistry.has(nodeAddress)){
                                currentDetailedServiceRegistry.remove(nodeAddress);
                            }
                        
                        }
                    
                    
                   }
                }
                
                
                
             }  catch (JSONException ex) {
                
                 System.err.println("SJServiceRegistry,UpdateServiceRegistryBasedOnServiceExpiry JSONException: " +ex.getMessage());
             }
          
      
        
    }

    public static synchronized long getOwnAdvertisementTimeLimit(){
        long[] num = new long[1000];
        long min=0;
        int i=0;
        long t=5000; //just in case tolerance is not yet known, standard minimum for advertisementExpiryTime
        try {
            if(!obtainCurrentRegistry().isEmpty()){
                
                JSONObject js = obtainInternalRegistry();
                //get all services expirytime and choose the least time for advertisement period
                Enumeration keys = js.keys();
          
                while (keys.hasMoreElements()){
                    Object key = keys.nextElement();

                    JSONObject js1 = (JSONObject)js.get(key.toString());
                    //System.out.println("advertisement: " +js1.get("advertisementExpiry").toString());
                    //t=Long.parseLong(js1.get("AdvertisementExpiry").toString());
                    
                    //if (js1.getString("serviceRole").equalsIgnoreCase("provider")){
                    if(js1.has("advertisementExpiry")){
                        num[i] = Long.parseLong(js1.get("advertisementExpiry").toString());
                   // System.out.println("num got " +num[i]);
                        i++;
                    }
                    
                        
                    //}
                    
                    
                }
                
                        for (int ktr = 0; ktr < num.length; ktr++) {
                             
                            if (ktr==0){
                                min = num[ktr];
                            } else if (num[ktr] < min) {
                                min = num[ktr];
                            } 
                        }
                
                //t = minTimeValue(num);
                //System.out.println("Advertisement got " +t);
            }

        } catch (JSONException ex) {
            System.out.println("no advertisement expiry info: " +ex.getMessage());
        }
        
        if (min!=0){
            return min;
        } else {
            return t;
        }
        
        
    }

    public static void RecordAdvertisementTimeStamp(){
        
        synchronized (advTimeStampLock){
            lastAdvertisementTime = System.currentTimeMillis();
        }
        
        
    }
    
    public static long getRecordedAdvertisementTimeStamp(){
        
        long advTimeStamp=0;
        
        synchronized (advTimeStampLock){
            
                advTimeStamp=lastAdvertisementTime;
           
        }
        return advTimeStamp;
    }
    
    public static int GetLocalInternalServicesAmount(){
        
        try {
            
            int amount = obtainInternalRegistry().length();
            return amount;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return 0;
        }
        
    }

    public static void SaveDiscoveredServicesNoP2P(JSONObject js){
       
        
        
        //System.out.println("Advertised service Prior to filtering: " +js);
        
        try {

            //JSONObject jsExpected = getConsumerExpectedServiceType();
           
            //JSONObject jsExp = (JSONObject) jsExpected.get("expectedServiceType");
            
               
               

           
            //System.out.println("Amount of expected service:" +jsExp.length()+ "Amount of MatchingService: " +filteredServList.length());

            //JSONObject servListVar = (JSONObject) servList.get("service1");
            
            //System.out.println("After filtering: " +filteredServList);
            
                if (!js.isEmpty()){
                    
                    InterfaceManager im = IMBuffer.getInterfaceManagerConfig();
                        
                    Interconnection ic = im.getInterconnection();
                    
                    synchronized (CurrentRegistryLock){
                
                        Enumeration keysSS = js.keys();
                        
                        while(keysSS.hasMoreElements()){
                            
                            String SSName = keysSS.nextElement().toString();
                            
                            JSONObject MatchingServsInSS = js.getJSONObject(SSName);
                            
                            Enumeration keysMatchingServs = MatchingServsInSS.keys();
                            
                            //JSONObject js3 = new JSONObject();
                            
                            while(keysMatchingServs.hasMoreElements()){
                                
                                String keyServName = keysMatchingServs.nextElement().toString();
                                
                                if(currentDetailedServiceRegistry.has(SSName)){
                                    JSONObject allServInSS = currentDetailedServiceRegistry.getJSONObject(SSName);
                                    
                                    allServInSS.put(keyServName,MatchingServsInSS.getJSONObject(keyServName));
                                    
                                    currentDetailedServiceRegistry.remove(SSName);
                                    
                                    currentDetailedServiceRegistry.put(SSName, allServInSS);
                                    
                                } else {
                                    
                                    currentDetailedServiceRegistry.put(SSName, MatchingServsInSS);
                                    
                                }
                                
                                //js3.put(keyServName, MatchingServsInSS.getJSONObject(keyServName));
                                
                            }
                            
                            
                            
                        }
                        
                        
                        
                        
                         
                        //else {
                        //    ic.AddAdvertisedSSForLinkToList(js.getString("sourceSS"));
                        //}
                        
                        // notify that SS have been advertised
                        
                        //SJSSCDSignalChannelMap.saveInterConnection(ic);
                        im.setInterconnection(ic);
                        IMBuffer.SaveInterfaceManagerConfig(im);
                        
                        
                    }
                
                    
                    /*
                synchronized (serviceAdvertisementReceivedTimeLock){
                    
                    if(serviceAdvertisementReceivedTime.has(js.getString("sourceSS"))){
                        serviceAdvertisementReceivedTime.remove(js.getString("sourceSS"));
                    }
                
                    serviceAdvertisementReceivedTime.put(js.getString("sourceSS"),System.currentTimeMillis());
                
                }
                
                Enumeration keys2ServList= servAdvList.keys();
                
                while (keys2ServList.hasMoreElements()){
                    Object key2ServList = keys2ServList.nextElement();
                    
                    JSONObject jsIndivServDesc2 = servAdvList.getJSONObject(key2ServList.toString());
                    
                     if (jsIndivServDesc2.getString("serviceRole").equalsIgnoreCase("provider")){
                            
                         if (jsIndivServDesc2.has("advertisementExpiry")){

                            long advertisementLength = jsIndivServDesc2.getLong("advertisementExpiry");
                            
                            synchronized (ServExpiryLengthLock){
                                if(serviceExpiryLength.has(js.getString("sourceSS"))){
                                    serviceExpiryLength.remove(js.getString("sourceSS"));
                                }
                                serviceExpiryLength.put(js.getString("sourceSS"),advertisementLength);
                            }
                            break;
                        }
                         
                     }
                     
                     
                   }
                */
                
                
                }
                
                //System.out.println("Included");
            

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void SaveJoiningNodesServicesInfoOfSOAMsgNoP2P(JSONObject js){
       
        //StringSplitter strsplit = new StringSplitter();
        
        //System.out.println("Advertised service Prior to filtering: " +js);
        
        try {

            //JSONObject jsExpected = getConsumerExpectedServiceType();
            JSONObject servAdvList = (JSONObject) js.get("serviceList");
            
            JSONObject filteredServList = new JSONObject();
            //JSONObject jsExp = (JSONObject) jsExpected.get("expectedServiceType");
            
            int i=0;
         
               Enumeration keysServList = servAdvList.keys();  //service1,service2,service3
               
               while (keysServList.hasMoreElements()){
                   Object key2 = keysServList.nextElement();
                    JSONObject jsIndivServDesc = servAdvList.getJSONObject(key2.toString());
                    
                    boolean IsServRequired = false;
                    
                     //Enumeration keysJsExp = jsExp.keys(); //1,2,3 for expected service Type list
                     
                    // while (keysJsExp.hasMoreElements()){
                    //     Object key = keysJsExp.nextElement();
                         
                    /*
                         if (jsExp.getString(key.toString()).contains(";")){
                             
                             String [] splitted = strsplit.split(jsExp.getString(key.toString()), ";");
                             
                             for (int y=0;y<splitted.length;y++){
                                 
                             if (jsIndivServDesc.getString("serviceType").equalsIgnoreCase(splitted[y])){
                                IsServRequired = true;
                             }
                                 
                             }
                                    
                         } else {
                             
                             if (jsIndivServDesc.getString("serviceType").equalsIgnoreCase(jsExp.getString(key.toString()))){
                                IsServRequired = true;
                             }
                             
                         }
                         */
                         
                    // }
                     
                     //test req
                     
                   //  if (!IsServRequired){
                   //      servAdvList.remove(key2.toString());
                   //  }
                
                     
                     //end test req
               }
               
               filteredServList = servAdvList;

           
            //System.out.println("Amount of expected service:" +jsExp.length()+ "Amount of MatchingService: " +filteredServList.length());

            //JSONObject servListVar = (JSONObject) servList.get("service1");
            
            //System.out.println("After filtering: " +filteredServList);
            
            
                
                if (!filteredServList.isEmpty()){
                    
                    synchronized (CurrentRegistryLock){
                
                        InterfaceManager im = IMBuffer.getInterfaceManagerConfig();
                        
                        Interconnection ic = im.getInterconnection();
                        
                        if (currentDetailedServiceRegistry.has(js.getString("sourceSS"))){
                            currentDetailedServiceRegistry.remove(js.getString("sourceSS"));
                        } 
                        //else {
                        //    ic.AddAdvertisedSSForLinkToList(js.getString("sourceSS"));
                        //}
                        
                        // notify that SS have been advertised
                        
                        currentDetailedServiceRegistry.put(js.getString("sourceSS"),filteredServList);
                        
                        //SJSSCDSignalChannelMap.saveInterConnection(ic);
                        im.setInterconnection(ic);
                        IMBuffer.SaveInterfaceManagerConfig(im);
                        
                        
                        //System.out.println("SJServiceReg, updated serv reg from: " +js.getString("sourceSS"));
                    }
                
                synchronized (serviceAdvertisementReceivedTimeLock){
                    
                    if(serviceAdvertisementReceivedTime.has(js.getString("sourceSS"))){
                        serviceAdvertisementReceivedTime.remove(js.getString("sourceSS"));
                    }
                
                    serviceAdvertisementReceivedTime.put(js.getString("sourceSS"),System.currentTimeMillis());
                
                }
                
                Enumeration keys2ServList= servAdvList.keys();
                
                while (keys2ServList.hasMoreElements()){
                    Object key2ServList = keys2ServList.nextElement();
                    
                    JSONObject jsIndivServDesc2 = servAdvList.getJSONObject(key2ServList.toString());
                    
                     if (jsIndivServDesc2.getString("serviceRole").equalsIgnoreCase("provider")){
                            
                         if (jsIndivServDesc2.has("advertisementExpiry")){

                            long advertisementLength = jsIndivServDesc2.getLong("advertisementExpiry");
                            
                            synchronized (ServExpiryLengthLock){
                                if(serviceExpiryLength.has(js.getString("sourceSS"))){
                                    serviceExpiryLength.remove(js.getString("sourceSS"));
                                }
                                serviceExpiryLength.put(js.getString("sourceSS"),advertisementLength);
                            }
                            break;
                        }
                         
                     }
                     
                     
                   }
                
                
                }
                
                
                
                //System.out.println("SJServiceRegistry, Updated registry: " +SJServiceRegistry.obtainCurrentRegistry());
            

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    /*
    public static void SaveJoiningNodesServicesInfoOfSOAMsg(JSONObject js){
       
        StringSplitter strsplit = new StringSplitter();
        
        //System.out.println("Advertised service Prior to filtering: " +js);
        
        try {

            JSONObject jsExpected = getConsumerExpectedServiceType();
            JSONObject servAdvList = (JSONObject) js.get("serviceList");
            
            JSONObject filteredServList = new JSONObject();
            JSONObject jsExp = (JSONObject) jsExpected.get("expectedServiceType");
            
            int i=0;
         
               Enumeration keysServList = servAdvList.keys();  //service1,service2,service3
               
               while (keysServList.hasMoreElements()){
                   Object key2 = keysServList.nextElement();
                    JSONObject jsIndivServDesc = servAdvList.getJSONObject(key2.toString());
                    
                    boolean IsServRequired = false;
                    
                     Enumeration keysJsExp = jsExp.keys(); //1,2,3 for expected service Type list
                     
                     while (keysJsExp.hasMoreElements()){
                         Object key = keysJsExp.nextElement();
                         
                         if (jsExp.getString(key.toString()).contains(";")){
                             
                             String [] splitted = strsplit.split(jsExp.getString(key.toString()), ";");
                             
                             for (int y=0;y<splitted.length;y++){
                                 
                             if (jsIndivServDesc.getString("serviceType").equalsIgnoreCase(splitted[y])){
                                IsServRequired = true;
                             }
                                 
                             }
                                    
                         } else {
                             
                             if (jsIndivServDesc.getString("serviceType").equalsIgnoreCase(jsExp.getString(key.toString()))){
                                IsServRequired = true;
                             }
                             
                         }
                         
                     }
                     
                     //test req
                     
                   //  if (!IsServRequired){
                   //      servAdvList.remove(key2.toString());
                   //  }
                
                     
                     //end test req
               }
               
               filteredServList = servAdvList;

           
            System.out.println("Amount of expected service:" +jsExp.length()+ "Amount of MatchingService: " +filteredServList.length());

            //JSONObject servListVar = (JSONObject) servList.get("service1");
            
            //System.out.println("After filtering: " +filteredServList);
            
            
                
                if (!filteredServList.isEmpty()){
                    
                    synchronized (CurrentRegistryLock){
                
                        InterfaceManager im = IMBuffer.getInterfaceManagerConfig();
                        
                        Interconnection ic = im.getInterconnection();
                        
                        if (currentDetailedServiceRegistry.has(js.getString("sourceSS"))){
                            currentDetailedServiceRegistry.remove(js.getString("sourceSS"));
                        } 
                        //else {
                        //    ic.AddAdvertisedSSForLinkToList(js.getString("sourceSS"));
                        //}
                        
                        // notify that SS have been advertised
                        
                        currentDetailedServiceRegistry.put(js.getString("sourceSS"),filteredServList);
                        
                        //SJSSCDSignalChannelMap.saveInterConnection(ic);
                        im.setInterconnection(ic);
                        IMBuffer.SaveInterfaceManagerConfig(im);
                        
                        
                        System.out.println("SJServiceReg, updated serv reg from: " +js.getString("sourceSS"));
                    }
                
                synchronized (serviceAdvertisementReceivedTimeLock){
                    
                    if(serviceAdvertisementReceivedTime.has(js.getString("sourceSS"))){
                        serviceAdvertisementReceivedTime.remove(js.getString("sourceSS"));
                    }
                
                    serviceAdvertisementReceivedTime.put(js.getString("sourceSS"),System.currentTimeMillis());
                
                }
                
                Enumeration keys2ServList= servAdvList.keys();
                
                while (keys2ServList.hasMoreElements()){
                    Object key2ServList = keys2ServList.nextElement();
                    
                    JSONObject jsIndivServDesc2 = servAdvList.getJSONObject(key2ServList.toString());
                    
                     if (jsIndivServDesc2.getString("serviceRole").equalsIgnoreCase("provider")){
                            
                         if (jsIndivServDesc2.has("advertisementExpiry")){

                            long advertisementLength = jsIndivServDesc2.getLong("advertisementExpiry");
                            
                            synchronized (ServExpiryLengthLock){
                                if(serviceExpiryLength.has(js.getString("sourceSS"))){
                                    serviceExpiryLength.remove(js.getString("sourceSS"));
                                }
                                serviceExpiryLength.put(js.getString("sourceSS"),advertisementLength);
                            }
                            break;
                        }
                         
                     }
                     
                     
                   }
                
                }
                
                //System.out.println("Included");
            

        } catch (JSONException ex) {
           ex.printStackTrace();
        }
    }
    */
    
    /*
    public static void SaveNodesServicesFromRespReqAdvMsg(JSONObject js){
       
        StringSplitter strsplit = new StringSplitter();
        
        //System.out.println("Advertised service Prior to filtering: " +js);
        
        try {

            JSONObject jsExpected = getConsumerExpectedServiceType();
            JSONObject servAdvList = (JSONObject) js.get("serviceList");
            
            JSONObject filteredServList = new JSONObject();
            JSONObject jsExp = (JSONObject) jsExpected.get("expectedServiceType");
            
            int i=0;
         
               Enumeration keysServList = servAdvList.keys();  //service1,service2,service3
               
               while (keysServList.hasMoreElements()){
                   Object key2 = keysServList.nextElement();
                    JSONObject jsIndivServDesc = servAdvList.getJSONObject(key2.toString());
                    
                    boolean IsServRequired = false;
                    
                     Enumeration keysJsExp = jsExp.keys(); //1,2,3 for expected service Type list
                     
                     while (keysJsExp.hasMoreElements()){
                         Object key = keysJsExp.nextElement();
                         
                         if (jsExp.getString(key.toString()).contains(";")){
                             
                             String [] splitted = strsplit.split(jsExp.getString(key.toString()), ";");
                             
                             for (int y=0;y<splitted.length;y++){
                                 
                             if (jsIndivServDesc.getString("serviceType").equalsIgnoreCase(splitted[y])){
                                IsServRequired = true;
                             }
                                 
                             }
                                    
                         } else {
                             
                             if (jsIndivServDesc.getString("serviceType").equalsIgnoreCase(jsExp.getString(key.toString()))){
                                IsServRequired = true;
                             }
                             
                         }
                         
                     }
                     
                     if (!IsServRequired){
                         servAdvList.remove(key2.toString());
                     }
                     
               }
               
               filteredServList = servAdvList;

           
            System.out.println("Amount of expected service:" +jsExp.length()+ "Amount of MatchingService: " +filteredServList.length());

            //JSONObject servListVar = (JSONObject) servList.get("service1");
            
            //System.out.println("After filtering: " +filteredServList);
            
            
                
                if (!servAdvList.isEmpty()){
                    
                    synchronized (CurrentRegistryLock){
                
                        if (currentDetailedServiceRegistry.has(js.getString("sourceAddress"))){
                            currentDetailedServiceRegistry.remove(js.getString("sourceAddress"));
                        }
                        
                        currentDetailedServiceRegistry.put(js.getString("sourceAddress"),filteredServList);
                
                    }
                
                synchronized (serviceAdvertisementReceivedTimeLock){
                
                    if (serviceAdvertisementReceivedTime.has(js.getString("sourceAddress"))){
                        serviceAdvertisementReceivedTime.remove(js.getString("sourceAddress"));
                    }
                    
                    serviceAdvertisementReceivedTime.put(js.getString("sourceAddress"),System.currentTimeMillis());
                
                }
                
                Enumeration keys2ServList= servAdvList.keys();
                
                while (keys2ServList.hasMoreElements()){
                    Object key2ServList = keys2ServList.nextElement();
                    
                    JSONObject jsIndivServDesc2 = servAdvList.getJSONObject(key2ServList.toString());
                    
                     if (jsIndivServDesc2.getString("serviceRole").equalsIgnoreCase("provider")){
                            
                         if (jsIndivServDesc2.has("advertisementExpiry")){

                            long advertisementLength = jsIndivServDesc2.getLong("advertisementExpiry");
                            
                            synchronized (ServExpiryLengthLock){
                                
                                if(serviceExpiryLength.has(js.getString("sourceAddress"))){
                                    serviceExpiryLength.remove(js.getString("sourceAddress"));
                                }
                                
                                serviceExpiryLength.put(js.getString("sourceAddress"),advertisementLength);
                            }
                            break;
                        }
                         
                     }
                     
                     
                   }
                
                }
                
            

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
   */
    
    
    /* 
   public static void addMessageTokensToBuffer(JSONObject jsReqMsg){
        try {
            JSONObject js = new JSONObject();
             js.put("msgID",jsReqMsg.get("msgID"));
             js.put("srcAddr", jsReqMsg.get("srcAddr"));
             js.put("srcPort",jsReqMsg.get("srcPort"));
             
             synchronized (messageTokenDatabaseLock){
             
                messageTokensDatabase.put(Integer.toString(messageTokensDatabase.length()), js);
             }
        } catch (JSONException ex) {
            System.err.println(ex.getMessage());
        }
    }
   */
    
    public static void removeParticularMessageTokensFromBuffer(int messageToken){
        
        JSONObject jsMsgTkn;
        
        synchronized (messageTokenDatabaseLock){
        
            jsMsgTkn = messageTokensDatabase;
        }
        Enumeration keys = jsMsgTkn.keys();

                while (keys.hasMoreElements()){
                try {
                    Object key = keys.nextElement();
                    JSONObject js = (JSONObject) jsMsgTkn.get(key.toString());
                    if (messageToken==Integer.parseInt(js.getString("msgID")))
                    {
                        
                        synchronized (messageTokenDatabaseLock){
        
                             messageTokensDatabase.remove(key.toString()); 
                        }
  
                    }
                //t=Long.parseLong(js1.get("AdvertisementExpiry").toString());
                //t=Long.parseLong(js1.get("AdvertisementExpiry").toString());
            } catch (JSONException ex) {
                //Logger.getLogger(SJServiceRegistry.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex.getMessage());
            }
                   
            }
    }
 
    
    public static JSONObject obtainAllService(){
        JSONObject js = new JSONObject();
        try {
            
            Enumeration keysJs = obtainCurrentRegistry().keys();
            
            while (keysJs.hasMoreElements()){
                String key1 = keysJs.nextElement().toString();
                
                if (!key1.equalsIgnoreCase(SJSSCDSignalChannelMap.getLocalSSName())){
                    js.put(key1.toString(),obtainCurrentRegistry().getJSONObject(key1.toString()));
                }    
            }
            
        } catch (JSONException ex) {
            
            ex.printStackTrace();
        }
        return js;
    }
    
    
    
    public static boolean CheckUnavailableService(){
        
        int requestedService=0;
        int availService=0;
        
        
        try {
            JSONObject js = obtainAllService();
            
            JSONObject js2 = obtainInternalRegistryConsumerOnly(); //own list of consumer entity
            
            Enumeration keysJs2 = js2.keys();
            
            while (keysJs2.hasMoreElements()){
                
                Object keyIndiv = keysJs2.nextElement();
                
                JSONObject js2IndivServConsumer = js2.getJSONObject(keyIndiv.toString());
                
                if (js2IndivServConsumer.getString("serviceRole").equalsIgnoreCase("consumer")){
                    
                    requestedService++;
                    
                    String expectedServiceType = js2IndivServConsumer.get("serviceType").toString();
                
                    Enumeration keysJs = js.keys();
                
                    while (keysJs.hasMoreElements()){
                    
                        Object key2 = keysJs.nextElement();
                    
                        JSONObject jsAttribServProvider = js.getJSONObject(key2.toString());  
                    
                        Enumeration keysjsAttribServProvider = jsAttribServProvider.keys();
                    
                        while (keysjsAttribServProvider.hasMoreElements()){
                            
                            Object key3 = keysjsAttribServProvider.nextElement();
                        
                            JSONObject jsIndivServProvider = jsAttribServProvider.getJSONObject(key3.toString()); //remove service ID --> service1, service 2 etc
                        
                            if (jsIndivServProvider.getString("serviceRole").equalsIgnoreCase("provider")){
                                
                                String availServiceType = jsIndivServProvider.get("serviceType").toString();
                                
                                if (expectedServiceType.equalsIgnoreCase(availServiceType)){
                                    availService++;
                                }
                                
                            }

                        }
                    
                    }
                    
                 }
   
            }
            
        } catch (JSONException ex) {
            System.out.println("SJServiceRegistry,CheckUnavailableService JSON error: " +ex.getCause());
            ex.printStackTrace();
            System.exit(1);
        }
        
        if (availService<requestedService){
            return true;
        } else {
            return false;
        }
        
    }
    
    public static String getServTypeFromServName(String servName){
       
        try{
        
        if (!HasServiceProviderOnly()){
            JSONObject jsIntCons = obtainInternalRegistryConsumerOnly();
            
            Enumeration keysJsServInd = jsIntCons.keys();
            
            String servType = "";
            
            while (keysJsServInd.hasMoreElements()){
                
                Object keyServInd = keysJsServInd.nextElement();
                
                JSONObject jsIndivServ = jsIntCons.getJSONObject(keyServInd.toString());

                if (jsIndivServ.getString("serviceName").equalsIgnoreCase(servName)){
                    
                    servType = jsIndivServ.getString("serviceType");
                    
                }
                
            }
            
            return servType;
            
        } else {
            return "";
        }
        } catch (JSONException jex){
            System.out.println("SJServiceRegistry,JSONException getServTypeFromServName:" +jex.getMessage());
            return "";
        }
  
    }
    
    
    public static JSONObject getUnavailableServiceReturnTypeList(){

        JSONObject expectedServiceTypeListCheck = new JSONObject();
        
        JSONObject expectedServiceTypeList = new JSONObject();
        
        
        try {
            //JSONObject js = obtainAllService();
            
            JSONObject js = obtainCurrentRegistry();
            
            JSONObject js2 = obtainInternalRegistryConsumerOnly(); //own list of consumer entity
            
            Enumeration keysJs2 = js2.keys();
            
            while (keysJs2.hasMoreElements()){
                Object keyIndiv = keysJs2.nextElement();
                
                JSONObject js2IndivServConsumer = js2.getJSONObject(keyIndiv.toString());
                
                 int servCount=0;
                 
                
                if (js2IndivServConsumer.getString("serviceRole").equalsIgnoreCase("consumer")){
                    
                    //requestedService = requestedService++;
                    
                    String expectedServiceType = js2IndivServConsumer.get("serviceType").toString();
                
                    expectedServiceTypeListCheck.put(expectedServiceType,"0");
                    
                    Enumeration keysJs = js.keys();
  
                    while (keysJs.hasMoreElements()){
                    
                        Object key2 = keysJs.nextElement();
                    
                        JSONObject jsAttribServProvider = js.getJSONObject(key2.toString());  //remove node ID (the address)
                    
                        Enumeration keysjsAttribServProvider = jsAttribServProvider.keys();
                    
                        while (keysjsAttribServProvider.hasMoreElements()){
                            Object key3 = keysjsAttribServProvider.nextElement();
                        
                            JSONObject jsIndivServProvider = jsAttribServProvider.getJSONObject(key3.toString()); //remove service ID --> service1, service 2 etc
                        
                            if (jsIndivServProvider.getString("serviceRole").equalsIgnoreCase("provider")){
                                
                                String availServiceType = jsIndivServProvider.get("serviceType").toString();
                                
                                if (expectedServiceType.equalsIgnoreCase(availServiceType)){
                                    
                                    servCount++;
                                    
                                    expectedServiceTypeListCheck.put(expectedServiceType, Integer.toString(servCount));
                                    
                                }
                                
                            }

                        }
                    
                    }
                    
                 }
   
            }
            
            Enumeration keysJSONExpServList = expectedServiceTypeListCheck.keys();
            
            int i=1;
            
            while (keysJSONExpServList.hasMoreElements()){
                Object keyJSONExpServList = keysJSONExpServList.nextElement();
                
                if (!expectedServiceTypeListCheck.isEmpty()){
                    
                    if (expectedServiceTypeListCheck.getString(keyJSONExpServList.toString()).equalsIgnoreCase("0")){
                          expectedServiceTypeList.put(Integer.toString(i), keyJSONExpServList);
                          i++;
                     }  
                    
                }
                
               
            }
            
        } catch (JSONException ex) {
            System.out.println("SJServiceRegistry,getUnavailableServiceReturnTypeList JSON error: " +ex.getMessage());
            System.exit(1);
        }
  
        return expectedServiceTypeList;
        
    }
    /*
    public static void updateInternalServiceProviderWithSingleService(JSONObject newServiceList){
        try {
            JSONObject jsInt = obtainInternalRegistry();
            
            //int intRegSize=jsInt.length()+1;
            
            jsInt.put("service"+intRegSize,newServiceList);
            
            synchronized (CurrentRegistryLock){
                currentDetailedServiceRegistry.put(SJSSCDSignalChannelMap.getLocalSSName(), jsInt);
            }
            
        } catch (JSONException ex) {
           System.out.println("SJServiceRegistry,updateInternalServiceProvider JSONException: "+ex.getMessage());
        }
      
    }
    */
    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
    try {
        InetAddress candidateAddress = null;
        // Iterate all NICs (network interface cards)...
        for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
            // Iterate all IP addresses assigned to each card...
            for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                if (!inetAddr.isLoopbackAddress()) {

                    if (inetAddr.isSiteLocalAddress()) {
                        // Found non-loopback site-local address. Return it immediately...
                        if (!inetAddr.getHostAddress().equalsIgnoreCase("192.168.7.2")){
                            return inetAddr;
                        } 
                       
                    }
                    else if (candidateAddress == null) {
                        // Found non-loopback address, but not necessarily site-local.
                        // Store it as a candidate to be returned if site-local address is not subsequently found...
                        candidateAddress = inetAddr;
                        // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                        // only the first. For subsequent iterations, candidate will be non-null.
                    }
                }
            }
        }
        if (candidateAddress != null) {
            // We did not find a site-local address, but we found some other non-loopback address.
            // Server might have a non-site-local address assigned to its NIC (or it might be running
            // IPv6 which deprecates the "site-local" concept).
            // Return this non-loopback candidate address...
            return candidateAddress;
        }
        // At this point, we did not find a non-loopback address.
        // Fall back to returning whatever InetAddress.getLocalHost() returns...
        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
        if (jdkSuppliedAddress == null) {
            throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
        }
        return jdkSuppliedAddress;
    }
    catch (Exception e) {
        UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
        unknownHostException.initCause(e);
        throw unknownHostException;
    }
    
    }
    
    public static String getLocalSSAddr(){
        
        String IPAddr = "0.0.0.0";
        
        try {
            JSONObject jsAllServ = currentDetailedServiceRegistry.getJSONObject(SJSSCDSignalChannelMap.getLocalSSName());
            
            Enumeration keysAllServ = jsAllServ.keys();
            
            while(keysAllServ.hasMoreElements()){
                
                String servName = keysAllServ.nextElement().toString();
                
                JSONObject jsOneServ = jsAllServ.getJSONObject(servName);
                
                IPAddr = jsOneServ.getString("nodeAddress");
                
                break;
                
            }
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return IPAddr;
    }
    
    public static String getAddrOfSS(String SSName){
        
        synchronized(CurrentRegistryLock){
            
            String IPAddr = "0.0.0.0";
            
            try {
           
                
             if(currentDetailedServiceRegistry.has(SSName)){
                
                JSONObject jsAllServ = currentDetailedServiceRegistry.getJSONObject(SSName);
            
                Enumeration keysAllServ = jsAllServ.keys();
            
                while(keysAllServ.hasMoreElements()){
                
                    String servName = keysAllServ.nextElement().toString();
                
                    JSONObject jsOneServ = jsAllServ.getJSONObject(servName);
                
                    IPAddr = jsOneServ.getString("nodeAddress");
                
                    break;
                
                }
                
            } 
          
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
            
            return IPAddr;
            
        }
        
        
    }
}