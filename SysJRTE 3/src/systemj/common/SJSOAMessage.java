/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systemj.common;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author Udayanto
 */
public class SJSOAMessage{
    
    private JSONObject responseAdvertisementMessage,advertisementMessage,broadcastDiscoveryMessage,DiscoveryReplyMessage,requestAdvertiseMessage,ReqWeakMigrationMessage,ReqStrongMigrationMessage,RespMigrationMessage;
    
    public String ConstructNoP2PServToRegDiscoveryMessage(String SSOrigin, String regID){
       
        JSONObject jsDiscMsg = new JSONObject();
        
        try {
            
            jsDiscMsg = new JSONObject();
            jsDiscMsg.put("sourceAddress",SJServiceRegistry.getOwnIPAddressFromRegistry());
            jsDiscMsg.put("MsgType","discovery");
            jsDiscMsg.put("sourceSS",SSOrigin);
            jsDiscMsg.put("regID", regID);
            //JSONObject jsExp = SJServiceRegistry.getConsumerExpectedServiceType();
            
            //broadcastDiscoveryMessage.put("expServiceType",jsExp.getJSONObject("expectedServiceType"));
            
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructBroadcastDiscoveryMessage: " +ex.getMessage());
        }
        return jsDiscMsg.toString();
    }
    
     public JSONObject ConstructNoP2PProvToRegResponseReAdvertisementMessageInJSON(String expiryTime, String destAddr, String SSOrigin){
        
         JSONObject NoP2PProvToRegRespAdvMsg = new JSONObject();
         
         try {
            NoP2PProvToRegRespAdvMsg = new JSONObject();
            NoP2PProvToRegRespAdvMsg.put("MsgType","responseRegToProvReqAdvertise");
            NoP2PProvToRegRespAdvMsg.put("destAddr",destAddr);
            SJServiceRegistry.AppendSourceIPAddressToMessage(NoP2PProvToRegRespAdvMsg);
            NoP2PProvToRegRespAdvMsg.put("sourceSS",SSOrigin);
            //responseAdvertisementMessage.put("advertisementExpiry", expiryTime);   
            //responseAdvertisementMessage.put("portForResponse","7777");
            NoP2PProvToRegRespAdvMsg.put("serviceList", SJServiceRegistry.obtainInternalRegistryProviderOnly());
            //helloMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0")); //perhaps this isn't needed at all
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructReadvertisementMessage: " +ex.getMessage());
        }
        return NoP2PProvToRegRespAdvMsg;
    }
    
    public String ConstructNoP2PRegToProvReqAdvertisementMessage(String regID, String regAddr, String destSS){  //uses port 8888 now
        
        JSONObject regRequestAdvertiseMessage = new JSONObject();
        
        try {
            
            regRequestAdvertiseMessage.put("MsgType","regToProvRequestAdvertise");
            //SJServiceRegistry.AppendSourceIPAddressToMessage(requestAdvertiseMessage);
            regRequestAdvertiseMessage.put("regID", regID);
            regRequestAdvertiseMessage.put("destSS", destSS);
            regRequestAdvertiseMessage.put("regAddr",regAddr);
            regRequestAdvertiseMessage.put("sourceAddress", regAddr);
            //regRequestAdvertiseMessage.put("destAddr",destAddress);
            //regRequestAdvertiseMessage.put("expServiceType",SJServiceRegistry.getConsumerExpectedServiceType());
            
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructRegReqReadvertisementMessage: " +ex.getMessage());
        }
        return regRequestAdvertiseMessage.toString();
    }
    
    public String ConstructRegRequestAdvertisementMessage(String targetRegID, String srcAddr){  //uses port 8888 now
        
        JSONObject regRequestAdvertiseMessage = new JSONObject();
        
        try {
            
            regRequestAdvertiseMessage.put("MsgType","regRequestAdvertise");
            //SJServiceRegistry.AppendSourceIPAddressToMessage(requestAdvertiseMessage);
            regRequestAdvertiseMessage.put("regID", targetRegID);
            //regRequestAdvertiseMessage.put("regAddr",regAddr);
            regRequestAdvertiseMessage.put("sourceAddress", srcAddr);
            //regRequestAdvertiseMessage.put("destAddr",destAddress);
            //regRequestAdvertiseMessage.put("expServiceType",SJServiceRegistry.getConsumerExpectedServiceType());
            
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructRegReqReadvertisementMessage: " +ex.getMessage());
        }
        return regRequestAdvertiseMessage.toString();
    }
    
    public JSONObject ConstructRegResponseReAdvertisementMessageInJSON(String expiryTime, String regID, String regAddr){
        
        JSONObject regRespAdvMsg = new JSONObject();
        
        try {
           
            regRespAdvMsg.put("MsgType","regRespReqAdvertise");
            regRespAdvMsg.put("regID",regID);
            regRespAdvMsg.put("regAddr", regAddr);
            regRespAdvMsg.put("advertisementExpiry", expiryTime);
            //regRespAdvMsg.put("sourceAddress", jsRegReqAdvMsg.getString("sourceAddress"));
            //SJServiceRegistry.AppendSourceIPAddressToMessage(regRespAdvMsg);
            //regRespAdvMsg.put("sourceSS",SSOrigin);
            //responseAdvertisementMessage.put("advertisementExpiry", expiryTime);   
            //responseAdvertisementMessage.put("portForResponse","7777");
            //responseAdvertisementMessage.put("serviceList", SJServiceRegistry.obtainInternalRegistryProviderOnly());
            //helloMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0")); //perhaps this isn't needed at all
        } catch (JSONException ex) {
           ex.printStackTrace();
        }
        return regRespAdvMsg;
    }
    
    public String ConstructRegistryDiscoveryReplyMessage(String RegistryID, String RegAddr){
        
        JSONObject RegDiscReplyMessage = new JSONObject();
        
        try {
            
             RegDiscReplyMessage.put("MsgType","discReply");
            //SJServiceRegistry.AppendSourceIPAddressToMessage(RegDiscReplyMessage);
             RegDiscReplyMessage.put("regID",RegistryID);
             RegDiscReplyMessage.put("regAddr", RegAddr);
            // RegDiscReplyMessage.put("targetSS", targetSS);
             //DiscoveryReplyMessage.put("discoveryTarget",target);
           // unicastDiscoveryMessage.put("delayResponse","1200"); //should be between 1-5 seconds max
            //discoveryMessage.put("portForTransmission","1900");
             //RegDiscReplyMessage.put("portForResponse","77"); //as this uses broadcast, so listening response on the same port will cause the node to receive its own sent message
             RegDiscReplyMessage.put("serviceList", SJServiceRegistry.obtainCurrentRegistry());
             //discoveryMessage.put("expiry", expiryTime);
            //discoveryMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0"));
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructUnicastDiscoveryMessage: " +ex.getMessage());
        }
        return RegDiscReplyMessage.toString();
    }
    
    public String ConstructRegistryReAdvertisementMessage(String expiryTime, String RegistryID, String RegAddr){
       
        JSONObject RegAdvMessage = new JSONObject();
        
        try {
            
            RegAdvMessage.put("MsgType","RegReAdvertise");
            //SJServiceRegistry.AppendSourceIPAddressToMessage(advertisementMessage);
            RegAdvMessage.put("regID",RegistryID);
            RegAdvMessage.put("regAddr", RegAddr);
            RegAdvMessage.put("advertisementExpiry", expiryTime);
            //advertisementMessage.put("advertisementExpiry", expiryTime);   
            //advertisementMessage.put("portForResponse","7777");
            //RegAdvMessage.put("serviceList", SJServiceRegistry.obtainCurrentRegistry());
            //helloMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0")); //perhaps this isn't needed at all
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructReadvertisementMessage: " +ex.getMessage());
        }
        return RegAdvMessage.toString();
    }
    
    public JSONObject ConstructProvToRegistryResponseReAdvertisementMessageInJSON(String expiryTime, String destAddr, String sourceSS){
       
        JSONObject RegRespReqAdvMessage = new JSONObject();
        
        try {
            //responseAdvertisementMessage = new JSONObject();
            RegRespReqAdvMessage.put("MsgType","responseReqAdvertise");
            RegRespReqAdvMessage.put("destAddr",destAddr);
            //SJServiceRegistry.AppendSourceIPAddressToMessage(RegRespReqAdvMessage);
            RegRespReqAdvMessage.put("sourceSS",sourceSS);
            //RegRespReqAdvMessage.put("regAddr", RegAddr);
            RegRespReqAdvMessage.put("advertisementExpiry", expiryTime);   
            //responseAdvertisementMessage.put("portForResponse","7777");
            RegRespReqAdvMessage.put("serviceList", SJServiceRegistry.obtainInternalRegistryProviderOnly());
            //helloMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0")); //perhaps this isn't needed at all
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructRespReqReadvertisementMessage: " +ex.getMessage());
        }
        return RegRespReqAdvMessage;
    }
    
    /*
     public String ConstructReAdvertisementMessage(String expiryTime, String SSOrigin){
        
         try {
            advertisementMessage = new JSONObject();
            advertisementMessage.put("MsgType","reAdvertise");
            SJServiceRegistry.AppendSourceIPAddressToMessage(advertisementMessage);
            advertisementMessage.put("sourceSS",SSOrigin);
            //advertisementMessage.put("advertisementExpiry", expiryTime);   
            //advertisementMessage.put("portForResponse","7777");
            advertisementMessage.put("serviceList", SJServiceRegistry.obtainInternalRegistryProviderOnly());
            //helloMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0")); //perhaps this isn't needed at all
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructReadvertisementMessage: " +ex.getMessage());
        }
        return advertisementMessage.toString();
    }
     
     public JSONObject ConstructReAdvertisementMessageInJSON(String expiryTime, String SSOrigin){
        try {
            advertisementMessage = new JSONObject();
            advertisementMessage.put("MsgType","reAdvertise");
            SJServiceRegistry.AppendSourceIPAddressToMessage(advertisementMessage);
            advertisementMessage.put("sourceSS",SSOrigin);
            //advertisementMessage.put("advertisementExpiry", expiryTime);   
            //advertisementMessage.put("portForResponse","7777");
            //advertisementMessage.put("serviceList", SJServiceRegistry.obtainInternalRegistryProviderOnly());
            advertisementMessage.put("serviceList", SJServiceRegistry.obtainInternalRegistry());
            //helloMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0")); //perhaps this isn't needed at all
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructReadvertisementMessage: " +ex.getMessage());
        }
        return advertisementMessage;
    }
    */
     
      public JSONObject ConstructResponseReAdvertisementMessageInJSON(String expiryTime, String destAddr, String SSOrigin){
        try {
            responseAdvertisementMessage = new JSONObject();
            responseAdvertisementMessage.put("MsgType","responseReqAdvertise");
            responseAdvertisementMessage.put("destAddr",destAddr);
            SJServiceRegistry.AppendSourceIPAddressToMessage(responseAdvertisementMessage);
            responseAdvertisementMessage.put("sourceSS",SSOrigin);
            //responseAdvertisementMessage.put("advertisementExpiry", expiryTime);   
            //responseAdvertisementMessage.put("portForResponse","7777");
            responseAdvertisementMessage.put("serviceList", SJServiceRegistry.obtainInternalRegistryProviderOnly());
            //helloMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0")); //perhaps this isn't needed at all
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructReadvertisementMessage: " +ex.getMessage());
        }
        return responseAdvertisementMessage;
    }
     /*
     public String ConstructReAdvertisementMessage(String expiryTime, String serviceList, String SSOrigin){
        try {
            advertisementMessage = new JSONObject();
            advertisementMessage.put("MsgType","reAdvertise");
            SJServiceRegistry.AppendSourceIPAddressToMessage(advertisementMessage);
            advertisementMessage.put("sourceSS",SSOrigin);
            //advertisementMessage.put("advertisementExpiry", expiryTime);   
            //advertisementMessage.put("portForResponse","7777");
            advertisementMessage.put("serviceList", serviceList);
            //helloMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0")); //perhaps this isn't needed at all
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructReadvertisementMessage: " +ex.getMessage());
        }
        return advertisementMessage.toString();
    }
     */
     
     public String ConstructServToRegReAdvertisementMessageOfInclServ(String expiryTime, String SSOrigin, String regID){
        try {
            
            JSONObject inclAdv = new JSONObject();
            
            //JSONObject jsIntServ = SJServiceRegistry.obtainInternalRegistryProviderOnly();
            JSONObject jsIntServ = SJServiceRegistry.obtainInternalRegistryProviderOnly();
            
            Enumeration keysjsIntServ = jsIntServ.keys();
            
            int i=0;
            
            while (keysjsIntServ.hasMoreElements()){
                
                Object keyjsIndivServ = keysjsIntServ.nextElement();
                
                JSONObject jsIndivServ = jsIntServ.getJSONObject(keyjsIndivServ.toString());
                
                inclAdv.put(jsIndivServ.getString("serviceName"), jsIndivServ);
                /*
                Enumeration keysAdvVisib = advVisib.keys();
                
                while (keysAdvVisib.hasMoreElements()){
                    
                    Object keyAdvVisib = keysAdvVisib.nextElement();
                    
                    if (jsIndivServ.getString("serviceName").equalsIgnoreCase(keyAdvVisib.toString())){
                        
                        if (advVisib.getString(keyAdvVisib.toString()).equalsIgnoreCase("visible") || advVisib.getString(keyAdvVisib.toString()).equalsIgnoreCase("available")){
                            
                            inclAdv.put(jsIndivServ.getString("serviceName"), jsIndivServ);
                            i++;
                            
                        }
  
                    }
                    
                }
                */
                
            }
            
            advertisementMessage = new JSONObject();
            advertisementMessage.put("MsgType","reAdvertise");
            SJServiceRegistry.AppendSourceIPAddressToMessage(advertisementMessage);
            advertisementMessage.put("sourceSS",SSOrigin);
            advertisementMessage.put("regID", regID);
            advertisementMessage.put("CDStats", CDLCBuffer.GetAllCDMacroState());
            //advertisementMessage.put("advertisementExpiry", expiryTime);   
            //advertisementMessage.put("portForResponse","7777");
            advertisementMessage.put("serviceList", inclAdv);
            //helloMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0")); //perhaps this isn't needed at all
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructReadvertisementMessage: " +ex.getMessage());
        }
        return advertisementMessage.toString();
    }
     
     public String ConstructReAdvertisementMessageOfInclServ(String expiryTime, JSONObject advVisib, String SSOrigin){
        try {
            
            JSONObject inclAdv = new JSONObject();
            
            //JSONObject jsIntServ = SJServiceRegistry.obtainInternalRegistryProviderOnly();
            JSONObject jsIntServ = SJServiceRegistry.obtainInternalRegistry();
            
            Enumeration keysjsIntServ = jsIntServ.keys();
            
            int i=0;
            
            while (keysjsIntServ.hasMoreElements()){
                
                Object keyjsIndivServ = keysjsIntServ.nextElement();
                
                JSONObject jsIndivServ = jsIntServ.getJSONObject(keyjsIndivServ.toString());
                
                //Enumeration keysAdvVisib = advVisib.keys();
                
                //while (keysAdvVisib.hasMoreElements()){
                    
                    //Object keyAdvVisib = keysAdvVisib.nextElement();
                    
                    //if (jsIndivServ.getString("serviceName").equalsIgnoreCase(keyAdvVisib.toString())){
                        
                       // if (advVisib.getString(keyAdvVisib.toString()).equalsIgnoreCase("visible") || advVisib.getString(keyAdvVisib.toString()).equalsIgnoreCase("available")){
                            
                
                
                            inclAdv.put(jsIndivServ.getString("serviceName"), jsIndivServ);
                            //i++;
                            
                        //}
  
                    //}
                    
                //}
                
            }
            
            advertisementMessage = new JSONObject();
            advertisementMessage.put("MsgType","reAdvertise");
            SJServiceRegistry.AppendSourceIPAddressToMessage(advertisementMessage);
            advertisementMessage.put("sourceSS",SSOrigin);
            //advertisementMessage.put("advertisementExpiry", expiryTime);   
            //advertisementMessage.put("portForResponse","7777");
            advertisementMessage.put("serviceList", inclAdv);
            //helloMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0")); //perhaps this isn't needed at all
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructReadvertisementMessage: " +ex.getMessage());
        }
        return advertisementMessage.toString();
    }
     
     /*
     public String ConstructReAdvertisementMessageOfInclServ(String expiryTime, JSONObject advVisib, String SSOrigin){
        try {
            
            JSONObject inclAdv = new JSONObject();
            
            JSONObject jsIntServ = SJServiceRegistry.obtainInternalRegistryProviderOnly();
            
            Enumeration keysjsIntServ = jsIntServ.keys();
            
            int i=0;
            
            while (keysjsIntServ.hasMoreElements()){
                
                Object keyjsIndivServ = keysjsIntServ.nextElement();
                
                JSONObject jsIndivServ = jsIntServ.getJSONObject(keyjsIndivServ.toString());
                
                Enumeration keysAdvVisib = advVisib.keys();
                
                while (keysAdvVisib.hasMoreElements()){
                    
                    Object keyAdvVisib = keysAdvVisib.nextElement();
                    
                    if (jsIndivServ.getString("serviceName").equalsIgnoreCase(keyAdvVisib.toString())){
                        
                        if (advVisib.getString(keyAdvVisib.toString()).equalsIgnoreCase("visible") || advVisib.getString(keyAdvVisib.toString()).equalsIgnoreCase("available")){
                            
                            inclAdv.put(jsIndivServ.getString("serviceName"), jsIndivServ);
                            //i++;
                            
                        }
  
                    }
                    
                }
                
            }
            
            advertisementMessage = new JSONObject();
            advertisementMessage.put("MsgType","reAdvertise");
            SJServiceRegistry.AppendSourceIPAddressToMessage(advertisementMessage);
            advertisementMessage.put("sourceSS",SSOrigin);
            //advertisementMessage.put("advertisementExpiry", expiryTime);   
            //advertisementMessage.put("portForResponse","7777");
            advertisementMessage.put("serviceList", inclAdv);
            //helloMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0")); //perhaps this isn't needed at all
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructReadvertisementMessage: " +ex.getMessage());
        }
        return advertisementMessage.toString();
    }
     */
     
      public String ConstructDiscoveryMessage(String SSOrigin){
        try {
            
            broadcastDiscoveryMessage = new JSONObject();
            broadcastDiscoveryMessage.put("sourceAddress",SJServiceRegistry.getOwnIPAddressFromRegistry());
            broadcastDiscoveryMessage.put("MsgType","discovery");
            broadcastDiscoveryMessage.put("sourceSS",SSOrigin);
            JSONObject jsExp = SJServiceRegistry.getConsumerExpectedServiceType();
            
            broadcastDiscoveryMessage.put("expServiceType",jsExp.getJSONObject("expectedServiceType"));
            
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructBroadcastDiscoveryMessage: " +ex.getMessage());
        }
        return broadcastDiscoveryMessage.toString();
    }
      
      public String ConstructDiscoveryMessageOfExpServType(JSONObject jsServList, String SSOrigin){
        try {
            
            broadcastDiscoveryMessage = new JSONObject();
            
            broadcastDiscoveryMessage.put("MsgType","discovery");
            broadcastDiscoveryMessage.put("sourceAddress", SJServiceRegistry.getOwnIPAddressFromRegistry());
            broadcastDiscoveryMessage.put("sourceSS",SSOrigin);
            broadcastDiscoveryMessage.put("expServiceType",jsServList);
            
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructBroadcastDiscoveryMessage: " +ex.getMessage());
        }
        return broadcastDiscoveryMessage.toString();
    }

      public String ConstructRequestAdvertisementMessage(String destAddress, String SSOrigin){  //uses port 8888 now
        try {
            requestAdvertiseMessage = new JSONObject();
            requestAdvertiseMessage.put("MsgType","requestAdvertise");
            SJServiceRegistry.AppendSourceIPAddressToMessage(requestAdvertiseMessage);
            requestAdvertiseMessage.put("destAddress", destAddress);
            requestAdvertiseMessage.put("sourceSS",SSOrigin);
            requestAdvertiseMessage.put("expServiceType",SJServiceRegistry.getConsumerExpectedServiceType());
            
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructReadvertisementMessage: " +ex.getMessage());
        }
        return requestAdvertiseMessage.toString();
    }
 
      public String ConstructDiscoveryReplyMessage(JSONObject serviceList, String SSOrigin){
        try {
            DiscoveryReplyMessage = new JSONObject();
             DiscoveryReplyMessage.put("MsgType","discReply");
            SJServiceRegistry.AppendSourceIPAddressToMessage(DiscoveryReplyMessage);
            DiscoveryReplyMessage.put("sourceSS",SSOrigin);
             //DiscoveryReplyMessage.put("discoveryTarget",target);
           // unicastDiscoveryMessage.put("delayResponse","1200"); //should be between 1-5 seconds max
            //discoveryMessage.put("portForTransmission","1900");
             DiscoveryReplyMessage.put("portForResponse","77"); //as this uses broadcast, so listening response on the same port will cause the node to receive its own sent message
             DiscoveryReplyMessage.put("serviceList", serviceList);
             //discoveryMessage.put("expiry", expiryTime);
            //discoveryMessage.put("serviceList",currentDetailedServiceRegistry.getString("Node0"));
        } catch (JSONException ex) {
            System.out.println("What happens in ConstructUnicastDiscoveryMessage: " +ex.getMessage());
        }
        return DiscoveryReplyMessage.toString();
    }
    
      /*
    public String ConstructReqCodeOnlyMigrationMessage(String destinationSubsystem, String SSOrigin){
        try {
            
            String sourceAddress = null;
            //to get address, must access Serv registry
            JSONObject jsInt = SJServiceRegistry.obtainInternalRegistry();
            
            Enumeration keysJsInt = jsInt.keys();
            
            while(keysJsInt.hasMoreElements()){
                String servName = keysJsInt.nextElement().toString();
                
                JSONObject jsDet = jsInt.getJSONObject(servName);
                
                sourceAddress = jsDet.getString("nodeAddress");
                break;
            }
            
            ReqCodeMigrationMessage = new JSONObject();
            
            ReqCodeMigrationMessage.put("discMsgType","requestCodeOnlyServiceMigration");
            ReqCodeMigrationMessage.put("sourceAddress",sourceAddress);
            ReqCodeMigrationMessage.put("sourceSS",SSOrigin);
            ReqCodeMigrationMessage.put("destinationSubsystem",destinationSubsystem);
            //ReqMigrationMessage.put("destAddress",destAddress);
            
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return ReqCodeMigrationMessage.toString();
    }
    */
    
    public String ConstructReqWeakMigrationMessage(String destinationSubsystem, String SSOrigin){
        try {
            String sourceAddress = null;
            //to get address, must access Serv registry
            JSONObject jsInt = SJServiceRegistry.obtainInternalRegistry();
            
            Enumeration keysJsInt = jsInt.keys();
            
            while(keysJsInt.hasMoreElements()){
                String servName = keysJsInt.nextElement().toString();
                
                JSONObject jsDet = jsInt.getJSONObject(servName);
                
                sourceAddress = jsDet.getString("nodeAddress");
                break;
            }
            
            ReqWeakMigrationMessage = new JSONObject();
            
            ReqWeakMigrationMessage.put("MsgType","requestWeakServiceMigration");
            ReqWeakMigrationMessage.put("sourceAddress",sourceAddress);
            ReqWeakMigrationMessage.put("sourceSS",SSOrigin);
            ReqWeakMigrationMessage.put("destinationSubsystem",destinationSubsystem);
            //ReqMigrationMessage.put("destAddress",destAddress);
            
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return ReqWeakMigrationMessage.toString();
    }
    
    public String ConstructReqStrongMigrationMessage(String destinationSubsystem, String SSOrigin){
        try {
            
            String sourceAddress = null;
            //to get address, must access Serv registry
            JSONObject jsInt = SJServiceRegistry.obtainInternalRegistry();
            
            Enumeration keysJsInt = jsInt.keys();
            
            while(keysJsInt.hasMoreElements()){
                String servName = keysJsInt.nextElement().toString();
                
                JSONObject jsDet = jsInt.getJSONObject(servName);
                
                sourceAddress = jsDet.getString("nodeAddress");
                break;
            }
            
            ReqStrongMigrationMessage = new JSONObject();
            
            ReqStrongMigrationMessage.put("MsgType","requestStrongServiceMigration");
            ReqStrongMigrationMessage.put("sourceAddress",sourceAddress);
            ReqStrongMigrationMessage.put("sourceSS",SSOrigin);
            ReqStrongMigrationMessage.put("destinationSubsystem",destinationSubsystem);
            //ReqMigrationMessage.put("destAddress",destAddress);
            
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return ReqStrongMigrationMessage.toString();
    }
    
    public String ConstructResponseMigrationMessage(String ACK, String SSOrigin, String SSDest){
        try {
            
            String sourceAddress = null;
            //to get address, must access Serv registry
            JSONObject jsInt = SJServiceRegistry.obtainInternalRegistry();
            
            Enumeration keysJsInt = jsInt.keys();
            
            while(keysJsInt.hasMoreElements()){
                String servName = keysJsInt.nextElement().toString();
                
                JSONObject jsDet = jsInt.getJSONObject(servName);
                
                sourceAddress = jsDet.getString("nodeAddress");
                break;
            }
            
            RespMigrationMessage = new JSONObject();
            RespMigrationMessage.put("sourceSS",SSOrigin);
            RespMigrationMessage.put("MsgType", "responseReqServiceMigration");
            RespMigrationMessage.put("destinationSubsystem",SSDest );
            RespMigrationMessage.put("sourceAddress",sourceAddress);
            RespMigrationMessage.put("data", ACK);
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return RespMigrationMessage.toString();
    }
    
    public String CreateLinkCreationReqMsg(String SSOrigin, String SSDest, String sourceAddress){
        
        JSONObject jsMSg = new JSONObject();
        
        try{
           
            jsMSg.put("sourceSS",SSOrigin);
            jsMSg.put("MsgType","reqLinkCreation");
            jsMSg.put("destinationSubsystem",SSDest );
            jsMSg.put("sourceAddress",sourceAddress);
            
            
        } catch (JSONException jex){
            jex.printStackTrace();
        }
        
        return jsMSg.toString();
        
    }
    
    public String CreateLinkCreationRespMsg(String SSOrigin, String SSDest, String sourceAddress, String ACK){
        
        JSONObject jsMSg = new JSONObject();
        
        try{
           
            jsMSg.put("sourceSS",SSOrigin);
            jsMSg.put("MsgType", "respLinkCreation");
            jsMSg.put("destinationSubsystem",SSDest );
            jsMSg.put("sourceAddress",sourceAddress);
            jsMSg.put("data",ACK);
            
        } catch (JSONException jex){
            jex.printStackTrace();
        }
        
        return jsMSg.toString();
        
    }
    
}
