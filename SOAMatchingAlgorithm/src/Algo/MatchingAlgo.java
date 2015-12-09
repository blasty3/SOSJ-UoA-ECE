/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algo;

import java.net.InetSocketAddress;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;

/**
 * This class is an example of service matching algorithm implemented as Java methods invokable by SYstemJ CD
 * TO suit with the RTS format of service location, such matching algorithms should return the matching result in Java Vector format, formatted as the physical node address as the 1st elem, the port number as the 2nd elem, and the action Name (based on the requested parameter by consumer) as the 3rd elem.
 * There should be a certain standard template for the naming of parameters and the associated attributes naming in service description, agreed and known by both provider and consumer side (designer), thus the consumer can find and look for specific requirements based on the parameters described in the service description
 * @author Udayanto
 */
public class MatchingAlgo {
    
    /**
     * 
     */
    public Hashtable FindMatchMoreThanValue(String servName, String servType, String keyword, String paramName, int value, String targetSignalName, String RegistryContent){
        
        Hashtable result = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        try {
            
            
            
            JSONObject js  = new JSONObject(new JSONTokener(RegistryContent));
            
            
            Enumeration keysIndivNode = js.keys();
            
            while (keysIndivNode.hasMoreElements()){
                 Object key1 = keysIndivNode.nextElement();
                 
                 JSONObject jsIndivNode = js.getJSONObject(key1.toString());
                 
                 Enumeration keysIndivServ = jsIndivNode.keys();
                 
                 while (keysIndivServ.hasMoreElements()){
                     Object key2 = keysIndivServ.nextElement();
                     
                     JSONObject jsIndivServ = jsIndivNode.getJSONObject(key2.toString());
                     
                     if (jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider")){
                     
                        if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                            JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                            Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                            while (keysActionIndex.hasMoreElements()){
                             
                                Object keyActIndex = keysActionIndex.nextElement();

                                JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                                if (jsActParam.getString("keyword").contains(keyword)){
                                 
                                    JSONObject jsIndivServParam = jsIndivServ.getJSONObject("serviceParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
                                         
                                            if (jsIndivServActParam.has("maxValue")){
                                         
                                                if (Double.parseDouble(jsIndivServActParam.getString("maxValue"))>value){
                                              
                                                    result.put("serviceName",servName);
                                                    result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                    result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                    result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                    result.put("signalName",targetSignalName);
                                              
                                              //result.addElement(servName);
                                              //result.addElement(jsIndivServ.getString("nodeAddress"));
                                              //result.addElement(jsIndivServ.getString("controlPort"));
                                              //result.addElement(jsIndivServActParam.getString("associatedActionName"));
                                              //result.addElement(targetSignalName);
                                            }
                                                 
                                        }
                                         
                                     }
    
                                 }
                                 
                             }
                             
                         }
                         
                     }
                     
                   }
                     
                 }
                 
            }
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (result==null){
            return new Hashtable();
        }else{
            return result;
        }
        
    }
    
    public Hashtable FindMatchLessThanValue(String servName, String servType, String keyword, String paramName, int value, String targetSignalName, String RegistryContent){
        
        Hashtable result = new Hashtable();
        
        try {
            JSONObject js  = new JSONObject(new JSONTokener(RegistryContent));
            
            Enumeration keysIndivNode = js.keys();
            
            while (keysIndivNode.hasMoreElements()){
                 Object key1 = keysIndivNode.nextElement();
                 
                 JSONObject jsIndivNode = js.getJSONObject(key1.toString());
                 
                 Enumeration keysIndivServ = jsIndivNode.keys();
                 
                 while (keysIndivServ.hasMoreElements()){
                     Object key2 = keysIndivServ.nextElement();
                     
                     JSONObject jsIndivServ = jsIndivNode.getJSONObject(key2.toString());
                     
                     if (jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider")){
                     
                        if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                         JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                         Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                         while (keysActionIndex.hasMoreElements()){
                             
                             Object keyActIndex = keysActionIndex.nextElement();

                             JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                             if (jsActParam.getString("keyword").contains(keyword)){
                                 
                                 JSONObject jsIndivServParam = jsIndivServ.getJSONObject("serviceParameters");
                                 
                                 Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                 while (keysServParamInd.hasMoreElements()){
                                     
                                     Object keyParamInd = keysServParamInd.nextElement();
                                     
                                     JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                     if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
                                     
                                        if (jsIndivServActParam.has("minValue")){
                                         
                                          if (Double.parseDouble(jsIndivServActParam.getString("minValue"))<value){
                                              
                                              result.put("serviceName",servName);
                                              result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                              result.put("controlPort",jsIndivServ.getString("controlPort"));
                                              result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                              result.put("signalName",targetSignalName);
                                              
                                              //result.addElement(servName);
                                              //result.addElement(jsIndivServ.getString("nodeAddress"));
                                             // result.addElement(jsIndivServ.getString("controlPort"));
                                             // result.addElement(jsIndivServActParam.getString("associatedActionName"));
                                             // result.addElement(targetSignalName);
                                          }
                                                 
                                        }
                                     }
                                     
                                 }
                                 
                             }
                             
                         }
                         
                     }
                     
                    }
                     
                 }
                 
            }
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (result==null){
            return new Hashtable();
        }else{
            return result;
        }
        
    }
    
    public Hashtable FindMatchBetweenValues(String servName, String servType, String keyword, String paramName, int minValue, int maxValue, String targetSignalName, String RegistryContent){
        
        Hashtable result = new Hashtable();
        
        try {
            JSONObject js  = new JSONObject(new JSONTokener(RegistryContent));
            
            Enumeration keysIndivNode = js.keys();
            
            while (keysIndivNode.hasMoreElements()){
                 Object key1 = keysIndivNode.nextElement();
                 
                 JSONObject jsIndivNode = js.getJSONObject(key1.toString());
                 
                 Enumeration keysIndivServ = jsIndivNode.keys();
                 
                 while (keysIndivServ.hasMoreElements()){
                     Object key2 = keysIndivServ.nextElement();
                     
                     JSONObject jsIndivServ = jsIndivNode.getJSONObject(key2.toString());
                     
                     if (jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider")){
                     
                     if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                         JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                         Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                         while (keysActionIndex.hasMoreElements()){
                             
                             Object keyActIndex = keysActionIndex.nextElement();

                             JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                             if (jsActParam.getString("keyword").contains(keyword)){
                                 
                                 JSONObject jsIndivServParam = jsIndivServ.getJSONObject("serviceParameters");
                                 
                                 Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                 while (keysServParamInd.hasMoreElements()){
                                     
                                     Object keyParamInd = keysServParamInd.nextElement();
                                     
                                     JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                     if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
                                     
                                     if (jsIndivServActParam.has("minValue") && jsIndivServActParam.has("maxValue") ){
                                         
                                          if (Double.parseDouble(jsIndivServActParam.getString("maxValue"))<maxValue && Double.parseDouble(jsIndivServActParam.getString("minValue"))>minValue ){
                                              
                                              result.put("serviceName",servName);
                                              result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                              result.put("controlPort",jsIndivServ.getString("controlPort"));
                                              result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                              result.put("signalName",targetSignalName);
                                              
                                             // result.addElement(servName);
                                             // result.addElement(jsIndivServ.getString("nodeAddress"));
                                             // result.addElement(jsIndivServ.getString("controlPort"));
                                             // result.addElement(jsIndivServActParam.getString("associatedActionName"));
                                             // result.addElement(targetSignalName);
                                          }
                                                 
                                        }
                                     }
                                     
                                 }
                                 
                             }
                             
                         }
                         
                     }
                     
                   }
                     
                 }
                 
            }
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (result==null){
            return new Hashtable();
        }else{
            return result;
        }
        
    }
    
    public Hashtable FindMatchWithValue(String servName,String servType, String keyword, String paramName, String value, String targetSignalName, String RegistryContent){
        
        Hashtable result = new Hashtable();
        
        try {
            JSONObject js  = new JSONObject(new JSONTokener(RegistryContent));
            
            Enumeration keysIndivNode = js.keys();
            
            while (keysIndivNode.hasMoreElements()){
                 Object key1 = keysIndivNode.nextElement();
                 
                 JSONObject jsIndivNode = js.getJSONObject(key1.toString());
                 
                 Enumeration keysIndivServ = jsIndivNode.keys();
                 
                 while (keysIndivServ.hasMoreElements()){
                     Object key2 = keysIndivServ.nextElement();
                     
                     JSONObject jsIndivServ = jsIndivNode.getJSONObject(key2.toString());
                     
                     if (jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider")){
                     
                     if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                         JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                         Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                         while (keysActionIndex.hasMoreElements()){
                             
                             Object keyActIndex = keysActionIndex.nextElement();

                             JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                             if (jsActParam.getString("keyword").contains(keyword)){
                                 
                                 JSONObject jsIndivServParam = jsIndivServ.getJSONObject("serviceParameters");
                                 
                                 Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                 while (keysServParamInd.hasMoreElements()){
                                     
                                     Object keyParamInd = keysServParamInd.nextElement();
                                     
                                     JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                     if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
                                         
                                         if ((jsIndivServActParam.getString("value")).equalsIgnoreCase(value)){
                                              
                                              result.put("serviceName",servName);
                                              result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                              result.put("controlPort",jsIndivServ.getString("controlPort"));
                                              result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                              result.put("signalName",targetSignalName);
                                             
                                            
                                            //  result.addElement(servName);
                                            //  result.addElement(jsIndivServ.getString("nodeAddress"));
                                            //  result.addElement(jsIndivServ.getString("controlPort"));
                                            //  result.addElement(jsIndivServActParam.getString("associatedActionName"));
                                            //  result.addElement(targetSignalName);
                                              
                                          }
                                     
                                     //if (jsIndivServActParam.has("minValue") && jsIndivServActParam.has("maxValue") ){
                                         
                                          
                                                 
                                        }
                                     //}
                                     
                                 }
                                 
                             }
                             
                         }
                         
                     }
                     
                    }
                     
                 }
                 
            }
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (result==null){
            return new Hashtable();
        }else{
            return result;
        }
        
    }
    
    public String getNodeAddress(Vector vec){
        
        if (!vec.isEmpty()){
            String nodeAddr = vec.get(1).toString();
            return nodeAddr;
        } else{
            return "";
        }
        
        //String nodeAddr = vec.get(0).toString();
    
    }
    
    public String getControlPort(Vector vec){
        
        if(!vec.isEmpty()){
            String controlPort = vec.get(2).toString();
            return controlPort;
        } else{
            return "";
        }
 
    }
    
}
