/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algo;


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
public class MatchingAlgo2 {
    
    /**
     * 
     */
    public Hashtable FindMatchWithKeywordAndParamNameOfValue(String servName, String servType, String keyword, String paramName, String value, String targetSignalName, String RegistryContent)
    {
        
        Hashtable result = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                                 
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){

                                            if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){

                                                   // if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                   //    actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                    //   for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                    //         String actName = jsActParam.getString("name"); 
                                                             
                                                    //         if (actName.equalsIgnoreCase(actList[i])){
                                                    //             result.put("serviceName",servName);
                                                    //             result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                    //             result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                     //            result.put("actionName",actName);
                                                    //             result.put("signalName",targetSignalName);
                                                    //         }
                                                             
                                                     //    }
                                                           
                                                           
                                                    //   }
                                                        
                                                    //} else 
                                                    {
                                                        
                                                        result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if (jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsActParam.getString("name") );
                                                                 result.put("signalName",targetSignalName);
                                                        
                                                    }
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
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
    
    public Hashtable FindMatchWithKeywordAndParamNameOfPosition(String servName, String servType, String keyword, String xAxisVal, String yAxisVal, String targetSignalName, String RegistryContent)
    {
        
        Hashtable result = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                            
                            if (jsIndivServ.has("physicalDescription")){
                                
                                JSONObject jsIndivPhyDesc = jsIndivServ.getJSONObject("physicalDescription");
                            
                             Enumeration keysPhyIndex = jsIndivPhyDesc.keys();
                             
                             
                             while (keysPhyIndex.hasMoreElements()){
                             
                                Object keyPhyIndex = keysPhyIndex.nextElement();

                                JSONObject jsIndivPhyParam = jsIndivPhyDesc.getJSONObject(keyPhyIndex.toString());
                             
                                if ( jsIndivPhyParam.getString("name").equalsIgnoreCase("Position")){
                                 
                                    
                                    if ( jsIndivPhyParam.getString("xAxis").equalsIgnoreCase(xAxisVal) &&   jsIndivPhyParam.getString("yAxis").equalsIgnoreCase(yAxisVal)){
                                    
                                        JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                                        Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                                        while (keysActionIndex.hasMoreElements()){
                             
                                            Object keyActIndex = keysActionIndex.nextElement();

                                            JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                                      //      if (jsActParam.getString("keyword").contains(keyword)){
                                 
                                    //JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    if (jsActParam.getString("keyword").contains(";")){
                                        
                                        String[] splitted = strsplit.split(jsActParam.getString("keyword"), ";");
                                    
                                        for (int u=0;u<splitted.length;u++){
                                        
                                            if (splitted[u].equalsIgnoreCase(keyword)){
                                                result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if (jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsActParam.getString("name") );
                                                                 result.put("signalName",targetSignalName);
                                            }
                                        }
                                    } else {
                                        if (jsActParam.getString("keyword").equalsIgnoreCase(keyword)){
                                            result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if (jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsActParam.getString("name") );
                                                                 result.put("signalName",targetSignalName);
                                        }
                                    }
                                 
                                    
                                     
                                        

                                                   // if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                   //    actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                    //   for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                    //         String actName = jsActParam.getString("name"); 
                                                             
                                                    //         if (actName.equalsIgnoreCase(actList[i])){
                                                    //             result.put("serviceName",servName);
                                                    //             result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                    //             result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                     //            result.put("actionName",actName);
                                                    //             result.put("signalName",targetSignalName);
                                                    //         }
                                                             
                                                     //    }
                                                           
                                                           
                                                    //   }
                                                        
                                                    //} else 
                                                  //  {
                                                        
                                                        
                                                        
                                                    //}
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            

                                         
    
                                 
                                 
                                      //      }
                             
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
    
    public Hashtable FindMatchWithKeywordAndParamName(String servName, String servType, String keyword, String paramName, String targetSignalName, String RegistryContent)
    {
        
        Hashtable result = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                                 
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){

                                            
                                                    /*
                                                    if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                       for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                             String actName = jsActParam.getString("name"); 
                                                             
                                                             if (actName.equalsIgnoreCase(actList[i])){
                                                                 result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",actName);
                                                                 result.put("signalName",targetSignalName);
                                                             }
                                                             
                                                     //    }
                                                           
                                                           
                                                       }
                                                        
                                                    } else
                                            */
                                                    {
                                                        
                                                       result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if (jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsActParam.getString("name") );
                                                                 result.put("signalName",targetSignalName);
                                                        
                                                    }
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            

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
    
    public Hashtable FindMatchWithKeyword(String servName, String keyword, String targetSignalName, String RegistryContent)
    {
        
        Hashtable result = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                     
                        
                         
                            JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                            Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                            while (keysActionIndex.hasMoreElements()){
                             
                                Object keyActIndex = keysActionIndex.nextElement();

                                JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                                if (jsActParam.getString("keyword").contains(keyword)){
                                 
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        //if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){

                                            
                                                    /*
                                                    if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                       for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                             String actName = jsActParam.getString("name"); 
                                                             
                                                             if (actName.equalsIgnoreCase(actList[i])){
                                                                 result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",actName);
                                                                 result.put("signalName",targetSignalName);
                                                             }
                                                             
                                                     //    }
                                                           
                                                           
                                                       }
                                                        
                                                    } else
                                            */
                                                    {
                                                        
                                                        result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if (jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsActParam.getString("name") );
                                                                 result.put("signalName",targetSignalName);
                                                        
                                                    }
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            

                                         //}
    
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
    
    public Hashtable FindMatchWithKeywordAndConvDirection(String servName, String servType, String keyword, String convDirection, String targetSignalName, String RegistryContent)
    {
        
        Hashtable result = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                                 
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase("Direction")){
                                            
                                            if (jsIndivServActParam.getString("value").equalsIgnoreCase(convDirection)){
                                                
                                                result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if (jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsActParam.getString("name") );
                                                                 result.put("signalName",targetSignalName);
                                                
                                            }
                                            
                                        }

                                            
                                                    /*
                                                    if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                       for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                             String actName = jsActParam.getString("name"); 
                                                             
                                                             if (actName.equalsIgnoreCase(actList[i])){
                                                                 result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",actName);
                                                                 result.put("signalName",targetSignalName);
                                                             }
                                                             
                                                     //    }
                                                           
                                                           
                                                       }
                                                        
                                                    } else
                                            */
                                                    
                                                        
                                                        
                                                        
                                                    
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            

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
    
    public Hashtable FindMatchWithKeywordWithTimeout(String servName, String servType, String keyword, String targetSignalName, String timeout,String RegistryContent)
    {
        
        Hashtable result = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                                 
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        //if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){

                                            
                                                    /*
                                                    if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                       for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                             String actName = jsActParam.getString("name"); 
                                                             
                                                             if (actName.equalsIgnoreCase(actList[i])){
                                                                 result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",actName);
                                                                 result.put("signalName",targetSignalName);
                                                             }
                                                             
                                                     //    }
                                                           
                                                           
                                                       }
                                                        
                                                    } else
                                            */
                                                    {
                                                        
                                                        result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if (jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsActParam.getString("name") );
                                                                 result.put("signalName",targetSignalName);
                                                                 result.put("time",timeout);
                                                        
                                                    }
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            

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
                                 
                                 JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                 Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                 while (keysServParamInd.hasMoreElements()){
                                     
                                     Object keyParamInd = keysServParamInd.nextElement();
                                     
                                     JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                     if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
                                     
                                        if (jsIndivServActParam.has("minValue")){
                                         
                                          if (Double.parseDouble(jsIndivServActParam.getString("minValue"))<value){
                                              
                                              result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if (jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsActParam.getString("name") );
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
                                 
                                 JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                 Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                 while (keysServParamInd.hasMoreElements()){
                                     
                                     Object keyParamInd = keysServParamInd.nextElement();
                                     
                                     JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                     if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
                                     
                                     if (jsIndivServActParam.has("minValue") && jsIndivServActParam.has("maxValue") ){
                                         
                                          if (Double.parseDouble(jsIndivServActParam.getString("maxValue"))<maxValue && Double.parseDouble(jsIndivServActParam.getString("minValue"))>minValue ){
                                              
                                              result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if (jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsActParam.getString("name") );
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
                                 
                                 JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                 Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                 while (keysServParamInd.hasMoreElements()){
                                     
                                     Object keyParamInd = keysServParamInd.nextElement();
                                     
                                     JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                     if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
                                         
                                         if ((jsIndivServActParam.getString("value")).equalsIgnoreCase(value)){
                                              
                                              result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if (jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsActParam.getString("name") );
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
    
    
    public Hashtable FindMatchWithLeastTimeAndPESensorConsideration(String servName, String servType, String keyword, String paramName, int value, String targetSignalName, String RegistryContent, String PEMid, String PEIn){
        
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
                                 
                                 JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                 Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                 while (keysServParamInd.hasMoreElements()){
                                     
                                     Object keyParamInd = keysServParamInd.nextElement();
                                     
                                     JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                     if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
                                     
                                        if (jsIndivServActParam.has("minValue")){
                                         
                                          if (Double.parseDouble(jsIndivServActParam.getString("minValue"))<value){
                                              
                                              result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if (jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsActParam.getString("name") );
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

    public Hashtable FindMatchWithKeywordAndLeastParamValue(String servName, String servType, String keyword, String paramName, String targetSignalName, String RegistryContent)
    {
        
        //Hashtable result = new Hashtable();
        
        Hashtable result = new Hashtable();
        
        Hashtable resultList = new Hashtable();
        
        Hashtable finalResult = new Hashtable();
        //System.out.println("Matching Algo reg: " +RegistryContent);
        Vector num = new Vector();
        
        Hashtable allValue = new Hashtable();

        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                                 
                                    System.out.println(servName+"keyword match");
                                    
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
  
                                            System.out.println(servName+"paramName match");
                                            
                                            if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                
                                                actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"), ";");
                                                
                                                for (int j=0;j<actList.length;j++){
                                                    
                                                   // if (jsActParam.getString("keyword").contains(keyword)){
                                                            
                                                        String actName = jsActParam.getString("name"); 
                                                             
                                                        System.out.println(servName+"actName :" +actName);
                                                        
                                                             if (actName.equalsIgnoreCase(actList[j])){
                                                                 
                                                                 resultList=new Hashtable();
                                                                 
                                                                 resultList.put("serviceName",servName);
                                                                 resultList.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 resultList.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     resultList.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 resultList.put("actionName",actName);
                                                                 resultList.put("signalName",targetSignalName);
                                                                 result.put(Integer.toString(j),resultList);
                                                                 allValue.put(Integer.toString(j),jsIndivServActParam.getString("value"));
                                                             }
                                                        
                                                    //}
                                                    
                                                }
                                                
                                                
                                                System.out.println(servName+"result hashtable: " +result);
            
                Enumeration valueKeys = allValue.keys();
            
            double min=0;
           
            while (valueKeys.hasMoreElements()){
                
               String val = valueKeys.nextElement().toString();
                
               num.addElement(allValue.get(val).toString());

            }

            for (int ktr = 0; ktr < num.size(); ktr++) {
                             
                            if (ktr==0){
                                min = Double.valueOf(num.get(ktr).toString());
                            } else if (Double.valueOf((num.get(ktr)).toString()) < min) {
                                min = Double.valueOf(num.get(ktr).toString());
                            } 
            }
            
            System.out.println(servName+"result min: " +min);
 
            for (int k=0;k<allValue.size();k++){
                       
                double compared = Double.parseDouble(result.get(Integer.toString(k)).toString());
                
                if (min<=compared){
                    finalResult = (Hashtable)result.get(Integer.toString(k));
                }
                
            }
                                                
                                                
                                            } else {
                                                
                                                
                                                
                                                 resultList.put("serviceName",servName);
                                                                 resultList.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 resultList.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     resultList.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 resultList.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                                 resultList.put("signalName",targetSignalName);
                                                                 //result.put(jsIndivServActParam.getString("value"),resultList);
                                                
                                                                 finalResult = resultList;
                                            }
                                    
  
                                         }
    
                                 }  
                                 
                             }
                             
                         }
                         
                     }
                     
                   }
                     
                 }
                 
            }

                //System.out.println(servName+"result content: " +result.get(Double.toString(min)));
    
            System.out.println(servName+"finalresult: " +finalResult.toString());
        
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if (finalResult==null){
            return new Hashtable();
        } else {
            return finalResult;
        }
        
    }
    
    public Hashtable FindMatchWithKeywordAndLeastParamValueWithExtractedTime(String servName, String servType, String keyword, String paramName, String targetSignalName, String RegistryContent)
    {
        
        //Hashtable result = new Hashtable();
        
        Hashtable result = new Hashtable();
        
        Hashtable resultList = new Hashtable();
        
        Hashtable finalResult = new Hashtable();
        //System.out.println("Matching Algo reg: " +RegistryContent);
        Vector num = new Vector();
        
        Hashtable allValue = new Hashtable();

        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                                 
                                    System.out.println(servName+"keyword match");
                                    
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
  
                                            System.out.println(servName+"paramName match");
                                            
                                            if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                
                                                actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"), ";");
                                                
                                                for (int j=0;j<actList.length;j++){
                                                    
                                                   // if (jsActParam.getString("keyword").contains(keyword)){
                                                            
                                                        String actName = jsActParam.getString("name"); 
                                                             
                                                        System.out.println(servName+"actName :" +actName);
                                                        
                                                             if (actName.equalsIgnoreCase(actList[j])){
                                                                 
                                                                 resultList=new Hashtable();
                                                                 
                                                                 resultList.put("serviceName",servName);
                                                                 resultList.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 resultList.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     resultList.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 resultList.put("actionName",actName);
                                                                 resultList.put("signalName",targetSignalName);
                                                                 resultList.put("time",jsIndivServActParam.getString("value"));
                                                                 result.put(Integer.toString(j),resultList);
                                                                 allValue.put(Integer.toString(j),jsIndivServActParam.getString("value"));
                                                             }
                                                        
                                                    //}
                                                    
                                                }
                                                
                                                
                                                System.out.println(servName+"result hashtable: " +result);
            
                Enumeration valueKeys = allValue.keys();
            
            double min=0;
           
            while (valueKeys.hasMoreElements()){
                
               String val = valueKeys.nextElement().toString();
                
               num.addElement(allValue.get(val).toString());

            }

            for (int ktr = 0; ktr < num.size(); ktr++) {
                             
                            if (ktr==0){
                                min = Double.valueOf(num.get(ktr).toString());
                            } else if (Double.valueOf((num.get(ktr)).toString()) < min) {
                                min = Double.valueOf(num.get(ktr).toString());
                            } 
            }
            
            System.out.println(servName+"result min: " +min);
 
            for (int k=0;k<allValue.size();k++){
                       
                double compared = Double.parseDouble(result.get(Integer.toString(k)).toString());
                
                if (min<=compared){
                    finalResult = (Hashtable)result.get(Integer.toString(k));
                }
                
            }
                                                
                                                
                                            } else {
                                                
                                                
                                                
                                                 resultList.put("serviceName",servName);
                                                                 resultList.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 resultList.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     resultList.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 resultList.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                                 resultList.put("signalName",targetSignalName);
                                                                 resultList.put("time",jsIndivServActParam.getString("value"));
                                                                 //result.put(jsIndivServActParam.getString("value"),resultList);
                                                
                                                                 finalResult = resultList;
                                            }
                                    
  
                                         }
    
                                 }  
                                 
                             }
                             
                         }
                         
                     }
                     
                   }
                     
                 }
                 
            }

                //System.out.println(servName+"result content: " +result.get(Double.toString(min)));
    
            System.out.println(servName+"finalresult: " +finalResult.toString());
        
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if (finalResult==null){
            return new Hashtable();
        } else {
            return finalResult;
        }
        
    }
    
    public Hashtable FindMatchWithKeywordAndLeastParamValueWithTimeout(String servName, String servType, String keyword, String paramName, String timeout,String targetSignalName, String RegistryContent)
    {
        
        //Hashtable result = new Hashtable();
        
        Hashtable result = new Hashtable();
        
        Hashtable resultList = new Hashtable();
        
        Hashtable finalResult = new Hashtable();
        //System.out.println("Matching Algo reg: " +RegistryContent);
        Vector num = new Vector();
        
        Hashtable allValue = new Hashtable();

        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                                 
                                    System.out.println(servName+"keyword match");
                                    
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
  
                                            System.out.println(servName+"paramName match");
                                            
                                            if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                
                                                actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"), ";");
                                                
                                                for (int j=0;j<actList.length;j++){
                                                    
                                                   // if (jsActParam.getString("keyword").contains(keyword)){
                                                            
                                                        String actName = jsActParam.getString("name"); 
                                                             
                                                        System.out.println(servName+"actName :" +actName);
                                                        
                                                             if (actName.equalsIgnoreCase(actList[j])){
                                                                 
                                                                 resultList=new Hashtable();
                                                                 
                                                                 resultList.put("serviceName",servName);
                                                                 resultList.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 resultList.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     resultList.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 resultList.put("actionName",actName);
                                                                 resultList.put("signalName",targetSignalName);
                                                                 resultList.put("time",timeout);
                                                                 result.put(Integer.toString(j),resultList);
                                                                 allValue.put(Integer.toString(j),jsIndivServActParam.getString("value"));
                                                             }
                                                        
                                                    //}
                                                    
                                                }
                                                
                                                
                                                System.out.println(servName+"result hashtable: " +result);
            
                Enumeration valueKeys = allValue.keys();
            
            double min=0;
           
            while (valueKeys.hasMoreElements()){
                
               String val = valueKeys.nextElement().toString();
                
               num.addElement(allValue.get(val).toString());

            }

            for (int ktr = 0; ktr < num.size(); ktr++) {
                             
                            if (ktr==0){
                                min = Double.valueOf(num.get(ktr).toString());
                            } else if (Double.valueOf((num.get(ktr)).toString()) < min) {
                                min = Double.valueOf(num.get(ktr).toString());
                            } 
            }
            
            System.out.println(servName+"result min: " +min);
 
            for (int k=0;k<allValue.size();k++){
                       
                double compared = Double.parseDouble(result.get(Integer.toString(k)).toString());
                
                if (min<=compared){
                    finalResult = (Hashtable)result.get(Integer.toString(k));
                }
                
            }
                                                
                                                
                                            } else {
                                                
                                                
                                                
                                                 resultList.put("serviceName",servName);
                                                                 resultList.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 resultList.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     resultList.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 resultList.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                                 resultList.put("signalName",targetSignalName);
                                                                 resultList.put("time",timeout);
                                                                 //result.put(jsIndivServActParam.getString("value"),resultList);
                                                
                                                                 finalResult = resultList;
                                            }
                                    
  
                                         }
    
                                 }  
                                 
                             }
                             
                         }
                         
                     }
                     
                   }
                     
                 }
                 
            }

                //System.out.println(servName+"result content: " +result.get(Double.toString(min)));
    
            //System.out.println(servName+"finalresult: " +finalResult.toString());
        
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if (finalResult==null){
            return new Hashtable();
        } else {
            return finalResult;
        }
        
    }
    
    
    public Hashtable FindMatchWithKeywordAndLeastParamValueAndPEInPEMidSensor(String servName, String servType, String keyword, String paramName, String targetSignalName, String RegistryContent, String PEIn, String PEMid, String TimeLimit){
        
        Hashtable result = new Hashtable();
        
        Hashtable resultList = new Hashtable();
        
        Hashtable finalResult = new Hashtable();
        //System.out.println("Matching Algo reg: " +RegistryContent);
        //double num[]= new double[0];
        
        Vector num = new Vector();
        
        Hashtable allValue = new Hashtable();
        
        int i=0;
        
        try {

            JSONObject js  = new JSONObject(new JSONTokener(RegistryContent));

            Enumeration keysIndivNode = js.keys();
            
            int j=0;
            
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
                                 
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        resultList=new Hashtable();
                                        
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){

                                            //if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){
                                 
                                                    resultList.put("serviceName",servName);
                                                    resultList.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                    resultList.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     resultList.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                    resultList.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                    resultList.put("signalName",targetSignalName);
                                                    resultList.put("value",jsIndivServActParam.getString("value"));
                                                    
                                                    result.put("res"+Integer.toString(j),resultList);
                                                    allValue.put("res"+Integer.toString(j),jsIndivServActParam.getString("value"));
                                                    j++;
                                                   
                                            //}

                                         }
    
                                 }
                                    
                                 
                                 
                             }
                             
                         }
                         
                     }
                     
                   }
                     
                 }
                 
            }
            
            //System.out.println("FindMatchWithKeywordAndLeastParamValueAndPEInPEMidSensor, all result: " +result);
            
            Enumeration valueKeys = allValue.keys();
            
            double min=0.00;
            
            while (valueKeys.hasMoreElements()){
                
               String val = valueKeys.nextElement().toString();
                
               num.addElement(allValue.get(val).toString());
              
   
            }
            //System.out.println("FindMatchWithKeywordAndLeastParamValueAndPEInPEMidSensor, all allValue num: " +num);
            int ktr;
            
            int minKtr=999999;
            
            
           

            if (Long.parseLong(PEMid)-Long.parseLong(PEIn)<Long.parseLong(TimeLimit)){
                
                //System.out.println("FindMatchWithKeywordAndLeastParamValueAndPEInPEMidSensor, PEMid-PEIn :  " +(Long.parseLong(PEMid)-Long.parseLong(PEIn)));
                
                for (ktr = 0; ktr < num.size(); ktr++) {
                             
                            if (ktr==0){
                                min = Double.valueOf(num.get(ktr).toString());
                            } else if (Double.valueOf((num.get(ktr)).toString()) < min) {
                                min = Double.valueOf(num.get(ktr).toString());  
                            }               
            }
            
            for (ktr=0;ktr < allValue.size() ; ktr++){
                
                if (Double.valueOf(allValue.get("res"+ktr).toString()) <= min){
                    minKtr = ktr;
                    
                    //System.out.println("FindMatchWithKeywordAndLeastParamValueAndPEInPEMidSensor, num.size():" +num.size()+" minKtr :  " +minKtr+ "min val: " +min + "all result: "+result+ "result: "+result.get("res"+minKtr));
 
                }
                
            }
                
               
                
            } else {
                
                 if (minKtr!=999999){
                    result.remove("res"+minKtr);
                }

                minKtr=999999;
                
                for (ktr = 0; ktr < num.size(); ktr++) {
                             
                            if (ktr==0){
                                min = Double.valueOf(num.get(ktr).toString());
                                //minKtr=ktr;
                            } else if (Double.valueOf((num.get(ktr)).toString()) < min) {
                                min = Double.valueOf(num.get(ktr).toString());
                                //minKtr=ktr;
                            }
                }
                
                for (ktr=0;ktr < allValue.size() ; ktr++){
                
                if (Double.valueOf(allValue.get("res"+ktr).toString()) <= min){
                    minKtr = ktr;
                    
                    //System.out.println("FindMatchWithKeywordAndLeastParamValueAndPEInPEMidSensor, num.size():" +num.size()+" minKtr :  " +minKtr+ "min val: " +min + "all result: "+result+ "result: "+result.get("res"+minKtr));
 
                }
                
              }
                
            }

            finalResult = (Hashtable)result.get("res"+minKtr);
            
            //System.out.println("FindMatchWithKeywordAndLeastParamValueAndPEInPEMidSensor, all result: " +result.get("res"+minKtr));
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       if (finalResult==null){
            return new Hashtable();
        }else{
            return finalResult;
        }
        
    }
    
    
     public Hashtable FindMatchWithKeywordAndParamNameOfValueAndXAxisValueOfBetweenMaxAndMin(String servName, String servType, String keyword, String paramName1, String value, String paramName2, String xAxisMin, String xAxisMax, String targetSignalName, String RegistryContent){
        
        Hashtable result = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        boolean req1=false;
        boolean req2=false;
        
        String resActName1="";
        String resActName2="";
        //String[] actList;
        //StringSplitter strsplit = new StringSplitter();
        
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
                                 
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName1)){

                                            if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){
                                                
                                                req1=true;
                                                
                                                resActName1 = jsIndivServActParam.getString("associatedActionName");
                                                //System.out.println("req1 true!");
                                                    /*
                                                    if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                       for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                             String actName = jsActParam.getString("name"); 
                                                             
                                                             if (actName.equalsIgnoreCase(actList[i])){
                                                                 result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",actName);
                                                                 result.put("signalName",targetSignalName);
                                                             }
                                                             
                                                     //    }
                                                           
                                                           
                                                       }
                                                        
                                                    } else {
                                                        
                                                        result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                                 result.put("signalName",targetSignalName);
                                                        
                                                    }
                                                    */
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            }

                                        }
                                       
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName2)){

                                           // System.out.println("found relative position param name!");
                                            
                                            //System.out.println("xAxis relative position :" +Double.valueOf(jsIndivServActParam.getString("xAxis")));
                                            
                                            if ((Double.valueOf(xAxisMin) < Double.valueOf(jsIndivServActParam.getString("xAxis"))) && (Double.valueOf(jsIndivServActParam.getString("xAxis")) < Double.valueOf(xAxisMax))){
                                                
                                                req2=true;
                                                
                                                resActName2 = jsIndivServActParam.getString("associatedActionName");
                                              //  System.out.println("req2 true!");
                                                    /*
                                                    if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                       for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                             String actName = jsActParam.getString("name"); 
                                                             
                                                             if (actName.equalsIgnoreCase(actList[i])){
                                                                 result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",actName);
                                                                 result.put("signalName",targetSignalName);
                                                             }
                                                             
                                                     //    }
                                                           
                                                           
                                                       }
                                                        
                                                    } else {
                                                        
                                                        result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                                 result.put("signalName",targetSignalName);
                                                        
                                                    }
                                                    */
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            }

                                         }
                                        
                                        
                                        
    
                                 }
                                 
                             }
                             
                                
                                
                                
                                
                         }
                
                     }
                     
                   }
                     
                   if (req1 && !req2){
                       req1=false;
                   } else if (!req1 && req2){
                       req2=false;
                   }
                   
                   if (req1 && req2 && resActName1.equalsIgnoreCase(resActName2)){
                       result.put("serviceName",servName);
                       result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                       result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                       result.put("actionName",resActName1);
                       result.put("signalName",targetSignalName);
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
     
    public Hashtable FindMatchesRouting(String servName, String servType, String targetSignalName, String RegistryContent){
        
        Hashtable result = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        //first, find the start point, which is the loader to use. 
        
        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                             
                             //   if (jsActParam.getString("keyword").contains(keyword)){
                                 
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                    //    if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){

                                         //   if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){

                                                    if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                       for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                             String actName = jsActParam.getString("name"); 
                                                             
                                                             if (actName.equalsIgnoreCase(actList[i])){
                                                                 result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",actName);
                                                                 result.put("signalName",targetSignalName);
                                                             }
                                                             
                                                     //    }
                                                           
                                                           
                                                       }
                                                        
                                                    } else {
                                                        
                                                        result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                                 result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                                 result.put("signalName",targetSignalName);
                                                        
                                                    }
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                        //    }

                                     //    }
    
                                 }
                                 
                           //  }
                             
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
    
    public Hashtable FindConveyorMatchToStopWithPosition(String servName, String servType, String keyword, String xAxisVal, String yAxisVal, String targetSignalName, String RegistryContent)
    {
        
        Hashtable result = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                            
                            if (jsIndivServ.has("physicalDescription")){
                                
                                JSONObject jsIndivPhyDesc = jsIndivServ.getJSONObject("physicalDescription");
                            
                             Enumeration keysPhyIndex = jsIndivPhyDesc.keys();
                             
                             boolean xTrue=false;
                             boolean yTrue=false;
                             
                             while (keysPhyIndex.hasMoreElements()){
                                 
                                 Object keyPhyIndex = keysPhyIndex.nextElement();

                                JSONObject jsIndivPhyParam = jsIndivPhyDesc.getJSONObject(keyPhyIndex.toString());
                             
                                if ( jsIndivPhyParam.getString("name").equalsIgnoreCase("Dimension")){
                                    
                                    if (jsIndivPhyParam.getString("description").equalsIgnoreCase("xAxis")){
                                        
                                        if (Double.parseDouble(jsIndivPhyParam.getString("from"))<=Double.parseDouble(xAxisVal) && (Double.parseDouble(xAxisVal)<=Double.parseDouble(jsIndivPhyParam.getString("to")))){
                                            xTrue=true;
                                        }
                                        
                                    } else if (jsIndivPhyParam.getString("description").equalsIgnoreCase("yAxis")){
                                        
                                        if (Double.parseDouble(jsIndivPhyParam.getString("from"))<=Double.parseDouble(yAxisVal) && (Double.parseDouble(yAxisVal)<=Double.parseDouble(jsIndivPhyParam.getString("to")))){
                                            yTrue=true;
                                        }
                                        
                                    }
                                    
                                }
                                 
                             }
                             
                                    if (xTrue && yTrue){
                                        
                                        JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                                        Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                                        while (keysActionIndex.hasMoreElements()){
                             
                                            Object keyActIndex = keysActionIndex.nextElement();

                                            JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                                      //      if (jsActParam.getString("keyword").contains(keyword)){
                                 
                                    //JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                            if (jsActParam.getString("keyword").contains(";")){
                                        
                                                String[] splitted = strsplit.split(jsActParam.getString("keyword"), ";");
                                    
                                                for (int u=0;u<splitted.length;u++){
                                        
                                                    if (splitted[u].equalsIgnoreCase(keyword)){
                                                
                                                
                                                        JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                                        Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                                        while (keysServParamInd.hasMoreElements()){
                                     
                                                            Object keyParamInd = keysServParamInd.nextElement();
                                     
                                                            JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                                            if (jsIndivServActParam.getString("name").equalsIgnoreCase("Direction")){

                                                                if (jsIndivServActParam.getString("value").equalsIgnoreCase("stop")){
                                                
                                                                    result.put("serviceName",servName);
                                                                    result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                    result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                    if(jsIndivServ.has("responsePort")){
                                                                        result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                    }
                                                                    result.put("actionName",jsActParam.getString("name") );
                                                                    result.put("signalName",targetSignalName);
                                                //System.out.println("req1 true!");
                                                    /*
                                                    if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                       for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                             String actName = jsActParam.getString("name"); 
                                                             
                                                             if (actName.equalsIgnoreCase(actList[i])){
                                                                 result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",actName);
                                                                 result.put("signalName",targetSignalName);
                                                             }
                                                             
                                                     //    }
                                                           
                                                           
                                                       }
                                                        
                                                    } else {
                                                        
                                                        result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                                 result.put("signalName",targetSignalName);
                                                        
                                                    }
                                                    */
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                                  }

                                               }
                                       
    
                                             }
                                                
                                                
                                                
                                            }
                                        }
                                    } else {
                                        if (jsActParam.getString("keyword").equalsIgnoreCase(keyword)){
                                            result.put("serviceName",servName);
                                                        result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                        result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                        result.put("actionName",jsActParam.getString("name") );
                                                        result.put("signalName",targetSignalName);
                                        }
                                    }
                                 
                                    
                                     
                                        

                                                   // if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                   //    actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                    //   for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                    //         String actName = jsActParam.getString("name"); 
                                                             
                                                    //         if (actName.equalsIgnoreCase(actList[i])){
                                                    //             result.put("serviceName",servName);
                                                    //             result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                    //             result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                     //            result.put("actionName",actName);
                                                    //             result.put("signalName",targetSignalName);
                                                    //         }
                                                             
                                                     //    }
                                                           
                                                           
                                                    //   }
                                                        
                                                    //} else 
                                                  //  {
                                                        
                                                        
                                                        
                                                    //}
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            

                                         
    
                                 
                                 
                                      //      }
                             
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
    
    public Hashtable FindConveyorMatchToResumeWithPosition(String servName, String servType, String keyword, String xAxisVal, String yAxisVal, String targetSignalName, String RegistryContent)
    {
        
        Hashtable result = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        String[] actList;
        StringSplitter strsplit = new StringSplitter();
        
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
                            
                            if (jsIndivServ.has("physicalDescription")){
                                
                                JSONObject jsIndivPhyDesc = jsIndivServ.getJSONObject("physicalDescription");
                            
                             Enumeration keysPhyIndex = jsIndivPhyDesc.keys();
                             
                             
                             boolean xTrue=false;
                             boolean yTrue=false;
                             
                             while (keysPhyIndex.hasMoreElements()){
                                 
                                 Object keyPhyIndex = keysPhyIndex.nextElement();

                                JSONObject jsIndivPhyParam = jsIndivPhyDesc.getJSONObject(keyPhyIndex.toString());
                             
                                if ( jsIndivPhyParam.getString("name").equalsIgnoreCase("Dimension")){
                                    
                                    if (jsIndivPhyParam.getString("description").equalsIgnoreCase("xAxis")){
                                    
                                        if (Double.parseDouble(jsIndivPhyParam.getString("from"))<=Double.parseDouble(xAxisVal) && Double.parseDouble(xAxisVal)<=Double.parseDouble(jsIndivPhyParam.getString("to"))){
                                            xTrue=true;
                                        }
                                        
                                    } else if (jsIndivPhyParam.getString("description").equalsIgnoreCase("yAxis")){
                                    
                                        if (Double.parseDouble(jsIndivPhyParam.getString("from"))<=Double.parseDouble(yAxisVal) && Double.parseDouble(yAxisVal)<=Double.parseDouble(jsIndivPhyParam.getString("to"))){
                                            yTrue=true;
                                        }
                                        
                                    }
                                    
                                }
                                 
                             }
                             
                                    
                                    if (xTrue && yTrue) {
                                        
                                        JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                                        Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                                        while (keysActionIndex.hasMoreElements()){
                             
                                            Object keyActIndex = keysActionIndex.nextElement();

                                            JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                                      //      if (jsActParam.getString("keyword").contains(keyword)){
                                 
                                    //JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    if (jsActParam.getString("keyword").contains(";")){
                                        
                                        String[] splitted = strsplit.split(jsActParam.getString("keyword"), ";");
                                    
                                        for (int u=0;u<splitted.length;u++){
                                        
                                            if (splitted[u].equalsIgnoreCase(keyword)){
                                                
                                                
                                                JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase("Direction")){

                                            if (jsIndivServActParam.getString("value").equalsIgnoreCase("Resume")){
                                                
                                                result.put("serviceName",servName);
                                                        result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                        result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                        result.put("actionName",jsActParam.getString("name") );
                                                        result.put("signalName",targetSignalName);
                                                //System.out.println("req1 true!");
                                                    /*
                                                    if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                       for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                             String actName = jsActParam.getString("name"); 
                                                             
                                                             if (actName.equalsIgnoreCase(actList[i])){
                                                                 result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",actName);
                                                                 result.put("signalName",targetSignalName);
                                                             }
                                                             
                                                     //    }
                                                           
                                                           
                                                       }
                                                        
                                                    } else {
                                                        
                                                        result.put("serviceName",servName);
                                                                 result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                                 result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                                 result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                                 result.put("signalName",targetSignalName);
                                                        
                                                    }
                                                    */
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            }

                                        }
                                       
                                        
                                        
                                        
                                        
    
                                 }
                                                
                                                
                                                
                                            }
                                        }
                                    } else {
                                        if (jsActParam.getString("keyword").equalsIgnoreCase(keyword)){
                                            result.put("serviceName",servName);
                                                        result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                       result.put("requestPort",jsIndivServ.getString("requestPort"));
                                                                 if(jsIndivServ.has("responsePort")){
                                                                     result.put("responsePort",jsIndivServ.getString("responsePort"));
                                                                 }
                                                        result.put("actionName",jsActParam.getString("name") );
                                                        result.put("signalName",targetSignalName);
                                        }
                                    }
                                 
                                    
                                     
                                        

                                                   // if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                   //    actList = strsplit.split(jsIndivServActParam.getString("associatedActionName"),";");
                                                       
                                                    //   for (int i=0;i<actList.length;i++){
                                                  
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                    //         String actName = jsActParam.getString("name"); 
                                                             
                                                    //         if (actName.equalsIgnoreCase(actList[i])){
                                                    //             result.put("serviceName",servName);
                                                    //             result.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                    //             result.put("controlPort",jsIndivServ.getString("controlPort"));
                                                     //            result.put("actionName",actName);
                                                    //             result.put("signalName",targetSignalName);
                                                    //         }
                                                             
                                                     //    }
                                                           
                                                           
                                                    //   }
                                                        
                                                    //} else 
                                                  //  {
                                                        
                                                        
                                                        
                                                    //}
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            

                                         
    
                                 
                                 
                                      //      }
                             
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
    
}
