/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 *
 * @author Udayanto
 */
public class PhysicalLocationFinder {
    
    
    //return hashtable with key xAxis, yAxis, and zAxis
     public Hashtable FindPhysicalLocationWithKeywordAndLeastParamValue(String servName, String servType, String keyword, String paramName, String targetSignalName, String RegistryContent)
    {
        
        //Hashtable result = new Hashtable();
        
        Hashtable result = new Hashtable();
        
        Hashtable resultList = new Hashtable();
        
        Hashtable finalResult = new Hashtable();
        //System.out.println("Matching Algo reg: " +RegistryContent);
        Vector num = new Vector();
        
        int indexResult=0;
        
        Hashtable allValue = new Hashtable();
        
        Hashtable allLocation = new Hashtable();

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
                     
                     int i=1;
                     
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
                                    
                                    boolean stat = false;
                                    
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
  
                                            System.out.println(servName+"paramName match");
                                            stat=true;
                                            allValue.put(Integer.toString(i),jsIndivServActParam.getString("value"));
                                            
                                               
                                        }
                                        
                                        if (stat && jsIndivServActParam.getString("name").equalsIgnoreCase("PhysicalLocation")){
                                            
                                            Hashtable val = new Hashtable();
                                            
                                            val.put("xAxis", jsIndivServActParam.getString("xAxis"));
                                            val.put("yAxis", jsIndivServActParam.getString("yAxis"));
                                            val.put("zAxis", jsIndivServActParam.getString("zAxis"));
                                            
                                            allLocation.put(Integer.toString(i),val);
                                            
                                        }
    
                                 }
                                    
                                    i++;
                                    
                                    
                                 
                             }
                             
                         }
                         
                     }
                     
                   }
                     
                 }
                 
            }
            
            
            
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
                       
                                                    double compared = Double.parseDouble(allValue.get(Integer.toString(k)).toString());
                
                                                    if (compared==min){
                                                        //finalResult = (Hashtable)allValue.get(Integer.toString(k));
                                                        indexResult=k;
                                                    }
                
                                                }
                                                
                                                
                                                

                //System.out.println(servName+"result content: " +result.get(Double.toString(min)));
    
            //System.out.println(servName+"finalresult: " +finalResult.toString());
        
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if (indexResult==0){
            return new Hashtable();
        } else {
            return (Hashtable) allLocation.get(indexResult);
        }
        
    }
     
     
     public Hashtable FindAllNodesPositionWithKeywords(String[] keywords, String RegistryContent)
     {
         
         
        //System.out.println("FindAllNodes, used registry content: " +RegistryContent);
        
        Hashtable result = new Hashtable();
        
        Hashtable oneKeywordResult = new Hashtable();
        
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        
        StringSplitter strsplit = new StringSplitter();
        
        try {

            JSONObject js  = new JSONObject(new JSONTokener(RegistryContent));
            
            for (int i=0;i<keywords.length;i++){
                
                int j=1;
                
                String keyword = keywords[i];
                
                Enumeration keysIndivNode = js.keys();
            
                while (keysIndivNode.hasMoreElements()){
                 Object key1 = keysIndivNode.nextElement();
                 
                 JSONObject jsIndivNode = js.getJSONObject(key1.toString());
                 
                 Enumeration keysIndivServ = jsIndivNode.keys();
                 
                 while (keysIndivServ.hasMoreElements()){
                     Object key2 = keysIndivServ.nextElement();
                     
                     JSONObject jsIndivServ = jsIndivNode.getJSONObject(key2.toString());
                     
                     if (jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider")){
                     
                        //if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                            JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                            Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                            while (keysActionIndex.hasMoreElements()){
                             
                                Object keyActIndex = keysActionIndex.nextElement();

                                JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                                //what if it has semicolons?
                                
                                if (jsActParam.getString("keyword").contains(";")){
                                    String[] splitted = strsplit.split(jsActParam.getString("keyword"), ";");
                                    
                                    for (int u=0;u<splitted.length;u++){
                                        if (keyword.equalsIgnoreCase(splitted[u])){
                                            
                                            if (jsIndivServ.has("physicalDescription")){
                                            JSONObject jsPhyDesc = jsIndivServ.getJSONObject("physicalDescription");
                                            
                                             Enumeration keysjsPhyDescInd = jsPhyDesc.keys();
                                             
                                             while (keysjsPhyDescInd.hasMoreElements()){
                                                 
                                                  String keyjsPhyDescInd = keysjsPhyDescInd.nextElement().toString();
                                                 
                                                  JSONObject jsPhyDescrParam = jsPhyDesc.getJSONObject(keyjsPhyDescInd);
                                                  
                                                  //Enumeration keysjsPhyDescParam = jsPhyDescrParam.keys();
                                                  
                                               //   while (keysjsPhyDescParam.hasMoreElements()){
                                                 //     String keyjsPhyDescParam = keysjsPhyDescParam.nextElement().toString();
                                                      
                                                      if (jsPhyDescrParam.getString("name").equalsIgnoreCase("Position")){

                                            //if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){

                                                    //if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                                result = new Hashtable();
                                                       
                                                                result.put("xAxis",jsPhyDescrParam.getString("xAxis"));
                                                                result.put("yAxis",jsPhyDescrParam.getString("yAxis"));
                                                                result.put("zAxis",jsPhyDescrParam.getString("zAxis"));
                                                                oneKeywordResult.put(jsIndivServ.getString("serviceName"),result);
                                                                //j++;
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                     //    }
                                                           
                                                           
                                                       
                                                        
                                                    //} 
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            //}

                                                        }
                                                      
                                               //   }
                                                 
                                             }
                                             
                                            
                                        }
                                            
                                        }
                                    }
                                    
                                } else {
                                    if (jsActParam.getString("keyword").equalsIgnoreCase(keyword)){
                                        
                                        if (jsIndivServ.has("physicalDescription")){
                                            JSONObject jsPhyDesc = jsIndivServ.getJSONObject("physicalDescription");
                                            
                                             Enumeration keysjsPhyDescInd = jsPhyDesc.keys();
                                             
                                             while (keysjsPhyDescInd.hasMoreElements()){
                                                 
                                                  String keyjsPhyDescInd = keysjsPhyDescInd.nextElement().toString();
                                                 
                                                  JSONObject jsPhyDescrParam = jsPhyDesc.getJSONObject(keyjsPhyDescInd);
                                                  
                                                  //Enumeration keysjsPhyDescParam = jsPhyDescrParam.keys();
                                                  
                                               //   while (keysjsPhyDescParam.hasMoreElements()){
                                                 //     String keyjsPhyDescParam = keysjsPhyDescParam.nextElement().toString();
                                                      
                                                      if (jsPhyDescrParam.getString("name").equalsIgnoreCase("Position")){

                                            //if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){

                                                    //if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                                result = new Hashtable();
                                                       
                                                                result.put("xAxis",jsPhyDescrParam.getString("xAxis"));
                                                                result.put("yAxis",jsPhyDescrParam.getString("yAxis"));
                                                                result.put("zAxis",jsPhyDescrParam.getString("zAxis"));
                                                                oneKeywordResult.put(jsIndivServ.getString("serviceName"),result);
                                                                //j++;
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                     //    }
                                                           
                                                           
                                                       
                                                        
                                                    //} 
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            //}

                                                        }
                                                      
                                               //   }
                                                 
                                             }
                                             
                                            
                                        }
                                        
                                    }
                                }
                                
                             
                         }
                         
                     //}
                     
                   }
                     
                 }
                 
               }
                
               //allKeywordResult.put(keyword,oneKeywordResult);
            
            }
            
            
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (oneKeywordResult.isEmpty()){
            return new Hashtable();
        } else{
            return oneKeywordResult;
        }
  
    }
     
     public Hashtable FindActionsFromNodes(Hashtable ConvDiv, String servName, String targetSignalName, String timeout){
        
         Hashtable actList = new Hashtable();
         Hashtable allActs = new Hashtable();
         Hashtable allActsInServ = new Hashtable();
       //  try {
       //      JSONObject jsReg = new JSONObject(new JSONTokener(RegistryContent));
       //  } 
         //catch (JSONException ex) {
       //      Logger.getLogger(PhysicalLocationFinder.class.getName()).log(Level.SEVERE, null, ex);
       //  }
          
         Hashtable indivActDet = new Hashtable();
         //Route1 Nodes
         //SingleLoaderService1,EnergizeDiverterStart,EnergizeDiverterEnd
         //MidConveyorServiceForwardStart,MidConveyorServiceForwardEnd,CappingService,StorageService
         
         Hashtable route1 = new Hashtable();
         Hashtable indivXY1 = new Hashtable();
         
         int actIndex = 1;
         
         indivXY1.put("xAxis","180.00");
         indivXY1.put("yAxis","60.00");
         route1.put("1",indivXY1);
         indivXY1 = new Hashtable();
         indivXY1.put("xAxis","150.00");
         indivXY1.put("yAxis","60.00");
         route1.put("2",indivXY1);
         indivXY1 = new Hashtable();
         indivXY1.put("xAxis","150.00");
         indivXY1.put("yAxis","55.00");
         route1.put("3",indivXY1);
         indivXY1 = new Hashtable();
         indivXY1.put("xAxis","150.00");
         indivXY1.put("yAxis","0.00");
         route1.put("4",indivXY1);
         indivXY1 = new Hashtable();
         indivXY1.put("xAxis","220.00");
         indivXY1.put("yAxis","0.00");
         route1.put("5",indivXY1);
         
         
         //Route2 (Longer) Nodes
         //SingleLoaderService1,UnenergizeDivStart,UnenergizeDiverterEnd,
         //TConvForwardEnd,BLConvForwardStart,CappingService,StorageService
         
         Hashtable route2 = new Hashtable();
         Hashtable indivXY2 = new Hashtable();
         
         /*
         indivXY2.put("xAxis","180.00");
         indivXY2.put("yAxis","60.00");
         route2.put("1",indivXY2);
         indivXY2 = new Hashtable();
         indivXY2.put("xAxis","150.00");
         indivXY2.put("yAxis","60.00");
         route2.put("2",indivXY2);
         indivXY2 = new Hashtable();
         indivXY2.put("xAxis","145.00");
         indivXY2.put("yAxis","60.00");
         route2.put("3",indivXY2);
         indivXY2 = new Hashtable();
         indivXY2.put("xAxis","0.00");
         indivXY2.put("yAxis","60.00");
         route2.put("4",indivXY2);
         indivXY2 = new Hashtable();
         indivXY2.put("xAxis","0.00");
         indivXY2.put("yAxis","0.00");
         route2.put("5",indivXY2);
         indivXY2 = new Hashtable();
         indivXY2.put("xAxis","180.00");
         indivXY2.put("yAxis","0.00");
         route2.put("6",indivXY2);
         */
         
         indivXY2.put("xAxis","180.00");
         indivXY2.put("yAxis","60.00");
         route2.put("1",indivXY2);
         indivXY2 = new Hashtable();
         indivXY2.put("xAxis","0.00");
         indivXY2.put("yAxis","60.00");
         route2.put("2",indivXY2);
         indivXY2 = new Hashtable();
         indivXY2.put("xAxis","0.00");
         indivXY2.put("yAxis","0.00");
         route2.put("3",indivXY2);
         indivXY2 = new Hashtable();
         indivXY2.put("xAxis","180.00");
         indivXY2.put("yAxis","0.00");
         route2.put("4",indivXY2);
         
         //check if midConv exist
         
         if (ConvDiv.containsKey("midConveyorService1")){
             
             //go via route1
             
             Enumeration keysConvDiv = ConvDiv.keys();
             
             while (keysConvDiv.hasMoreElements()){
                 String keyConvDivServName = keysConvDiv.nextElement().toString();
                 
                 Hashtable ConvDivActs = (Hashtable)ConvDiv.get(keyConvDivServName);
                 
                 Enumeration keysConvDivActs = ConvDivActs.keys();
                 
                 while (keysConvDivActs.hasMoreElements()){
                     String keyConvDivActs = keysConvDivActs.nextElement().toString();
                     
                     Hashtable ConvStartEndXY = (Hashtable) ConvDivActs.get(keyConvDivActs);
                     
                     for (int j=1;j<route1.size();j++){
                         
                         Hashtable XYNodeStart = (Hashtable) route1.get(Integer.toString(j));
                         Hashtable XYNodeEnd = (Hashtable) route1.get(Integer.toString(j+1));
                         
                         Hashtable XYConvStart = (Hashtable) ConvStartEndXY.get("StartPoint");
                         Hashtable XYConvEnd = (Hashtable) ConvStartEndXY.get("EndPoint");
                         
                         double XAxisConvEnd = Double.parseDouble(XYConvEnd.get("xAxis").toString());
                                 
                         double XAxisNodeStart = Double.parseDouble(XYNodeStart.get("xAxis").toString());
                                 
                         double XAxisConvStart = Double.parseDouble(XYConvStart.get("xAxis").toString());
                                         
                         double XAxisNodeEnd = Double.parseDouble(XYNodeEnd.get("xAxis").toString());
                                 
                         double YAxisConvEnd = Double.parseDouble(XYConvEnd.get("yAxis").toString());
                                 
                         double YAxisNodeStart = Double.parseDouble(XYNodeStart.get("yAxis").toString());
                                 
                         double YAxisConvStart = Double.parseDouble(XYConvStart.get("yAxis").toString());
                                         
                         double YAxisNodeEnd = Double.parseDouble(XYNodeEnd.get("yAxis").toString());
                     
                         //check if conveyor's start point value is smaller than end point value, to obtain direction
                         
                          if (XAxisNodeStart>=XAxisNodeEnd){
                             
                           //  if (Double.parseDouble(XYConvStart.get("xAxis").toString())>Double.parseDouble(XYConvEnd.get("xAxis").toString()))
                            // {
                               
                                 if ((((XAxisConvEnd<=XAxisNodeStart) && (XAxisNodeStart<=XAxisConvStart)) && ((XAxisConvEnd<=XAxisNodeEnd && XAxisNodeEnd<=XAxisConvStart))) && (((YAxisConvEnd<=YAxisNodeStart) && (YAxisNodeStart<=YAxisConvStart)) && ((YAxisConvEnd<=YAxisNodeEnd && YAxisNodeEnd<=YAxisConvStart)))){
                                    
                                     //compare if action has the same direction
                                     if (XAxisConvStart>=XAxisConvEnd || YAxisConvStart>=YAxisConvEnd){
                                         
                                         //put action here
                                         indivActDet = new Hashtable();
                                         indivActDet.put("actionName",keyConvDivActs);
                                         indivActDet.put("nodeAddress",ConvStartEndXY.get("nodeAddress").toString());
                                         indivActDet.put("requestPort",ConvStartEndXY.get("requestPort").toString());
                                         if(ConvStartEndXY.containsKey("responsePort")){
                                             indivActDet.put("responsePort",ConvStartEndXY.get("responsePort").toString());
                                         }
                                         indivActDet.put("time",timeout);
                                         //indivActDet.put("signalName",targetSignalName);
                                         //indivActDet.put("serviceName",servName);
                                         actList.put(Integer.toString(actIndex),indivActDet);
                                         actIndex++;
                                         
                                     }
                                     
                                 }
                                 
                             //}
                             
                         } else if (XAxisNodeStart<=XAxisNodeEnd){
                             
                            // if (((XAxisConvEnd>=XAxisNodeStart && XAxisNodeStart>=XAxisConvStart) && (XAxisConvEnd>=XAxisNodeEnd && XAxisNodeEnd>=XAxisConvStart)) && ((YAxisConvEnd>=YAxisNodeStart && YAxisNodeStart>=YAxisConvStart) && (YAxisConvEnd>=YAxisNodeEnd && YAxisNodeEnd>=YAxisConvStart))){
                                    if (((XAxisConvEnd>=XAxisNodeStart && XAxisNodeStart>=XAxisConvStart) && (XAxisConvEnd>=XAxisNodeEnd && XAxisNodeEnd>=XAxisConvStart)) && ((YAxisConvEnd<=YAxisNodeStart && YAxisNodeStart<=YAxisConvStart) && (YAxisConvEnd<=YAxisNodeEnd && YAxisNodeEnd<=YAxisConvStart))){
                                     //compare if action has the same direction
                                     if (XAxisConvEnd<=XAxisConvStart || YAxisConvEnd<=YAxisConvStart){
                                         
                                         //put action here
                                         
                                         indivActDet = new Hashtable();
                                         indivActDet.put("actionName",keyConvDivActs);
                                         indivActDet.put("nodeAddress",ConvStartEndXY.get("nodeAddress").toString());
                                         indivActDet.put("requestPort",ConvStartEndXY.get("requestPort").toString());
                                         if(ConvStartEndXY.containsKey("responsePort")){
                                             indivActDet.put("responsePort",ConvStartEndXY.get("responsePort").toString());
                                         }
                                         indivActDet.put("time",timeout);
                                         //indivActDet.put("signalName",targetSignalName);
                                         //indivActDet.put("serviceName",servName);
                                         
                                         actList.put(Integer.toString(actIndex),indivActDet);
                                         actIndex++;
                                         
                                     }
                                     
                                 }
                             
                         }
                         //i++;
                     }
                     
                     if (!actList.isEmpty()){
                         allActs.put(targetSignalName, actList);
                         allActsInServ.put(servName, allActs);
                     }
                     
                    
                 }
                 
             }
             
         } else {
            
             //go via route2
             
             Enumeration keysConvDiv = ConvDiv.keys();
             
             while (keysConvDiv.hasMoreElements()){
                 String keyConvDivServName = keysConvDiv.nextElement().toString();
                 
                 Hashtable ConvDivActs = (Hashtable)ConvDiv.get(keyConvDivServName);
                 
                 Enumeration keysConvDivActs = ConvDivActs.keys();
                 
                 while (keysConvDivActs.hasMoreElements()){
                     String keyConvDivActs = keysConvDivActs.nextElement().toString();
                     
                     Hashtable ConvStartEndXY = (Hashtable) ConvDivActs.get(keyConvDivActs);
                     
                     for (int j=1 ; j<route2.size(); j++){
                         Hashtable XYNodeStart = (Hashtable) route2.get(Integer.toString(j));
                         Hashtable XYNodeEnd = (Hashtable) route2.get(Integer.toString(j+1));
                         
                         Hashtable XYConvStart = (Hashtable) ConvStartEndXY.get("StartPoint");
                         Hashtable XYConvEnd = (Hashtable) ConvStartEndXY.get("EndPoint");
                         
                                 double XAxisConvEnd = Double.parseDouble(XYConvEnd.get("xAxis").toString());
                                 
                                 double XAxisNodeStart = Double.parseDouble(XYNodeStart.get("xAxis").toString());
                                 
                                 double XAxisConvStart = Double.parseDouble(XYConvStart.get("xAxis").toString());
                                         
                                 double XAxisNodeEnd = Double.parseDouble(XYNodeEnd.get("xAxis").toString());
                                 
                                 double YAxisConvEnd = Double.parseDouble(XYConvEnd.get("yAxis").toString());
                                 
                                 double YAxisNodeStart = Double.parseDouble(XYNodeStart.get("yAxis").toString());
                                 
                                 double YAxisConvStart = Double.parseDouble(XYConvStart.get("yAxis").toString());
                                         
                                 double YAxisNodeEnd = Double.parseDouble(XYNodeEnd.get("yAxis").toString());
                     
                         //check if conveyor's start point value is smaller than end point value
                                 
                                 
                                 if(XAxisNodeStart==XAxisNodeEnd){
                             
                             if(((YAxisConvEnd<=YAxisNodeStart) && (YAxisNodeStart<=YAxisConvStart)) && ((YAxisConvEnd<=YAxisNodeEnd && YAxisNodeEnd<=YAxisConvStart))){
                                 
                                 indivActDet = new Hashtable();
                                         indivActDet.put("actionName",keyConvDivActs);
                                         indivActDet.put("nodeAddress",ConvStartEndXY.get("nodeAddress").toString());
                                         indivActDet.put("requestPort",ConvStartEndXY.get("requestPort").toString());
                                         if(ConvStartEndXY.containsKey("responsePort")){
                                             indivActDet.put("responsePort",ConvStartEndXY.get("responsePort").toString());
                                         }
                                         indivActDet.put("time",timeout);
                                         //indivActDet.put("signalName",targetSignalName);
                                         //indivActDet.put("serviceName",servName);
                                         actList.put(Integer.toString(actIndex),indivActDet);
                                         actIndex++;
                                 
                             }
                             
                         }
                         
                         else
                                 
                         if (XAxisNodeStart>XAxisNodeEnd){
                             
                           //  if (Double.parseDouble(XYConvStart.get("xAxis").toString())>Double.parseDouble(XYConvEnd.get("xAxis").toString()))
                            // {
                               
                                 if ((((XAxisConvEnd<=XAxisNodeStart) && (XAxisNodeStart<=XAxisConvStart)) && ((XAxisConvEnd<=XAxisNodeEnd && XAxisNodeEnd<=XAxisConvStart))) && (((YAxisConvEnd<=YAxisNodeStart) && (YAxisNodeStart<=YAxisConvStart)) && ((YAxisConvEnd<=YAxisNodeEnd && YAxisNodeEnd<=YAxisConvStart)))){
                                    
                                     //compare if action has the same direction
                                     if (XAxisConvStart>=XAxisConvEnd || YAxisConvStart>=YAxisConvEnd ){
                                         
                                         /*
                                         boolean put = true;
                                         //put action here
                                         Enumeration keysActList = actList.keys();
                                         
                                         while(keysActList.hasMoreElements()){
                                             String key = keysActList.nextElement().toString();
                                             
                                             Hashtable indivAct = (Hashtable) actList.get(key);
                                             
                                             if(ConvStartEndXY.get("controlPort").toString().equalsIgnoreCase((String)indivAct.get("controlPort"))){
                                                 put=false;
                                                 System.out.println("equal control port: " +ConvStartEndXY.get("controlPort").toString()+ "and" +(String)indivAct.get("controlPort"));
                                             }
                                             
                                         }
                                         */
                                         
                                            indivActDet = new Hashtable();
                                            indivActDet.put("actionName",keyConvDivActs);
                                            indivActDet.put("nodeAddress",ConvStartEndXY.get("nodeAddress").toString());
                                           indivActDet.put("requestPort",ConvStartEndXY.get("requestPort").toString());
                                         if(ConvStartEndXY.containsKey("responsePort")){
                                             indivActDet.put("responsePort",ConvStartEndXY.get("responsePort").toString());
                                         }
                                            indivActDet.put("time",timeout);
                                         //indivActDet.put("signalName",targetSignalName);
                                         //indivActDet.put("serviceName",servName);
                                            actList.put(Integer.toString(actIndex),indivActDet);
                                            actIndex++;
                                         
                                         
                                     }
                                     
                                 }
                                 
                             //}
                             
                         } else if (XAxisNodeStart<XAxisNodeEnd){
                             
                             if ((((XAxisConvEnd>=XAxisNodeStart) && (XAxisNodeStart>=XAxisConvStart)) && ((XAxisConvEnd>=XAxisNodeEnd && XAxisNodeEnd>=XAxisConvStart))) && (((YAxisConvEnd>=YAxisNodeStart) && (YAxisNodeStart>=YAxisConvStart)) && ((YAxisConvEnd>=YAxisNodeEnd && YAxisNodeEnd>=YAxisConvStart)))){
                                    
                                     //compare if action has the same direction
                                     if (XAxisConvEnd<=XAxisConvStart || YAxisConvEnd<=YAxisConvStart){
                                         
                                         //put action here
                                         
                                         /*
                                         boolean put = true;
                                         //put action here
                                         Enumeration keysActList = actList.keys();
                                         
                                         while(keysActList.hasMoreElements()){
                                             
                                             String key = keysActList.nextElement().toString();
                                             
                                             Hashtable indivAct = (Hashtable) actList.get(key);
                                             
                                             if(ConvStartEndXY.get("controlPort").toString().equalsIgnoreCase((String)indivAct.get("controlPort"))){
                                                 put=false;
                                                 System.out.println("equal control port: " +ConvStartEndXY.get("controlPort").toString()+ "and" +(String)indivAct.get("controlPort"));
                                             }
                                             
                                         }
                                         */
                                         
                                            indivActDet = new Hashtable();
                                            indivActDet.put("actionName",keyConvDivActs);
                                            indivActDet.put("nodeAddress",ConvStartEndXY.get("nodeAddress").toString());
                                            indivActDet.put("requestPort",ConvStartEndXY.get("requestPort").toString());
                                         if(ConvStartEndXY.containsKey("responsePort")){
                                             indivActDet.put("responsePort",ConvStartEndXY.get("responsePort").toString());
                                         }
                                            indivActDet.put("time",timeout);
                                         //indivActDet.put("signalName",targetSignalName);
                                         //indivActDet.put("serviceName",servName);
                                            actList.put(Integer.toString(actIndex),indivActDet);
                                            actIndex++;
                                         
                                         
                                     }
                                     
                                 }
                             
                         }
                     }   
                     
                     if(keyConvDivActs.equalsIgnoreCase("UnenergizeDiverter")){
                         indivActDet = new Hashtable();
                         indivActDet.put("actionName",keyConvDivActs);
                         indivActDet.put("nodeAddress",ConvStartEndXY.get("nodeAddress").toString());
                         indivActDet.put("requestPort",ConvStartEndXY.get("requestPort").toString());
                                         if(ConvStartEndXY.containsKey("responsePort")){
                                             indivActDet.put("responsePort",ConvStartEndXY.get("responsePort").toString());
                                         }
                         indivActDet.put("time",timeout);
                         actList.put(Integer.toString(actIndex),indivActDet);
                         actIndex++;
                     }
                     
                     if (!actList.isEmpty()){
                         allActs.put(targetSignalName, actList);
                         allActsInServ.put(servName, allActs);
                     }
                     
                     
                 }
                 
             }
             
           }
         
       //  } catch (JSONException ex) {
             
       //      Logger.getLogger(PhysicalLocationFinder.class.getName()).log(Level.SEVERE, null, ex);
       //  }
         
         return allActsInServ;
     }
     
     public int GetRouteActuatorActionListSize(Hashtable actList){
         
         int actSize=0;
         
         Enumeration keysServ = actList.keys();
         
         while(keysServ.hasMoreElements()){
             
             String keyServ = keysServ.nextElement().toString();
             
             Hashtable Sig = (Hashtable) actList.get(keyServ);
             
             Enumeration keysSig = Sig.keys();
             
             while(keysSig.hasMoreElements()){
                 
                 String keySig = keysSig.nextElement().toString();
                 
                 Hashtable allActs = (Hashtable) Sig.get(keySig);
                 
                 actSize = allActs.size();
                 
             }
             
         }
         
         return actSize;
         
     }
     
     
     
     /*
      private Hashtable ObtainStartEndAllNodesAndExtractActions(Hashtable Nodes, Hashtable ConvLimit){
         
         Hashtable Combined = Nodes;
         
         Enumeration keysConv = ConvLimit.keys();
         
         while(keysConv.hasMoreElements()){
             String keyConv = keysConv.nextElement().toString();
             
             Hashtable ConvStartEnd = (Hashtable)ConvLimit.get(keyConv);
             
             Enumeration keysConvStartEnd = ConvStartEnd.keys();
             
             while (keysConvStartEnd.hasMoreElements()){
                 String keyConvStartEnd = keysConv.nextElement().toString();
                 
                 if (keyConvStartEnd.equalsIgnoreCase("StartPoint")){
                     Combined.put(keyConv+"Start", (Hashtable) ConvStartEnd.get(keyConvStartEnd));
                 } else if (keyConvStartEnd.equalsIgnoreCase("EndPoint")){
                     Combined.put(keyConv+"End", (Hashtable) ConvStartEnd.get(keyConvStartEnd));
                 }
                 
             }
             
         }
         
         return Combined;
         
     }
     */
     
     
     public Hashtable ObtainConveyorServiceReachWithKeywordAndParamName(String keyword1, String keyword2, String RegistryContent)
     {
        
         //System.out.println("ObtainConveyorService, used registry content: " +RegistryContent);
         
        Hashtable result = new Hashtable();
        
        Hashtable oneKeywordResult = new Hashtable();
        
        Hashtable allKeywordResult = new Hashtable();
        Hashtable allActionResult = new Hashtable();
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        
        StringSplitter strsplit = new StringSplitter();
        
        try {

            JSONObject js  = new JSONObject(new JSONTokener(RegistryContent));
            
           // for (int i=0;i<keywords.length;i++){
                
                oneKeywordResult = new Hashtable();
                
                int j=1;
                
              //  String keyword = keywords[i];
                
                Enumeration keysIndivNode = js.keys();
            
                while (keysIndivNode.hasMoreElements()){
                 Object key1 = keysIndivNode.nextElement();
                 
                 JSONObject jsIndivNode = js.getJSONObject(key1.toString());
                 
                 Enumeration keysIndivServ = jsIndivNode.keys();
                 
                 while (keysIndivServ.hasMoreElements()){
                     Object key2 = keysIndivServ.nextElement();
                     
                     JSONObject jsIndivServ = jsIndivNode.getJSONObject(key2.toString());
                     
                     if (jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider")){
                     
                        //if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                            JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                            Enumeration keysActionIndex = jsIndivServAct.keys();
                            
                            int actIterat = 1;
                         
                            while (keysActionIndex.hasMoreElements()){
                             
                                Object keyActIndex = keysActionIndex.nextElement();

                                JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                                    if (jsActParam.getString("keyword").contains(";")){
                                        
                                        String[] splitted = strsplit.split(jsActParam.getString("keyword"), ";");
                                    
                                        for (int u=0;u<splitted.length;u++){
                                        
                                            if (splitted[u].equalsIgnoreCase(keyword1) || splitted[u].equalsIgnoreCase(keyword2)){
                                                
                                                JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                                Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                                oneKeywordResult = new Hashtable();
                                                
                                                boolean one = false;
                                                boolean two = false;
                                                
                                                while (keysServParamInd.hasMoreElements()){
                                     
                                                    Object keyParamInd = keysServParamInd.nextElement();
                                     
                                                    JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                                    if (jsIndivServActParam.getString("name").equalsIgnoreCase("StartPoint")){

                                            //if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){

                                                    //if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                       result = new Hashtable();
                                                       
                                                       result.put("xAxis",jsIndivServActParam.getString("xAxis"));
                                                       result.put("yAxis",jsIndivServActParam.getString("yAxis"));
                                                       result.put("zAxis",jsIndivServActParam.getString("zAxis"));
                                                       
                                                       oneKeywordResult.put("StartPoint",result);
                                                       one=true;
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                     //    }
                                                           
                                                           
                                                       
                                                        
                                                    //} 
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            //}

                                                     } else if (jsIndivServActParam.getString("name").equalsIgnoreCase("EndPoint")) {
                                                
                                                       result = new Hashtable();
                                                         
                                                       result.put("xAxis",jsIndivServActParam.getString("xAxis"));
                                                       result.put("yAxis",jsIndivServActParam.getString("yAxis"));
                                                       result.put("zAxis",jsIndivServActParam.getString("zAxis"));
                                                       
                                                       oneKeywordResult.put("EndPoint",result);
                                                       two=true;
                                                    }
                                                
                                                        if (one && two){
                                                            oneKeywordResult.put("nodeAddress", jsIndivServ.getString("nodeAddress"));
                                                            oneKeywordResult.put("requestPort", jsIndivServ.getString("requestPort"));
                                                            if(jsIndivServ.has("responsePort")){
                                                                oneKeywordResult.put("responsePort", jsIndivServ.getString("responsePort"));
                                                            }
                                                            //allActionResult = new Hashtable();
                                                            //allKeywordResult.put(jsIndivServ.getString("serviceName"),oneKeywordResult);
                                                            allActionResult.put(jsActParam.getString("name"),oneKeywordResult);
                                                        }
    
                                                }
                                                
                                                if (!allActionResult.isEmpty() && actIterat==jsIndivServAct.length()){
                                                     allKeywordResult.put(jsIndivServ.getString("serviceName"),allActionResult);
                                                     allActionResult = new Hashtable();
                                                 }
                                                
                                            }
                                            
                                        }
                                        
                                    } else {
                                        
                                        if (jsActParam.getString("keyword").equalsIgnoreCase(keyword1) || jsActParam.getString("keyword").equalsIgnoreCase(keyword2)){
                                 
                                            JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                            Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                            boolean one = false;
                                            boolean two = false;
                                        
                                            oneKeywordResult=new Hashtable();
                                            
                                            while (keysServParamInd.hasMoreElements()){
                                     
                                                Object keyParamInd = keysServParamInd.nextElement();
                                     
                                                JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                                 if (jsIndivServActParam.getString("name").equalsIgnoreCase("StartPoint")){

                                            //if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){

                                                    //if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                       result = new Hashtable();
                                                       
                                                       result.put("xAxis",jsIndivServActParam.getString("xAxis"));
                                                       result.put("yAxis",jsIndivServActParam.getString("yAxis"));
                                                       result.put("zAxis",jsIndivServActParam.getString("zAxis"));
                                                       oneKeywordResult.put("StartPoint",result);
                                                       one=true;
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                     //    }
                                                           
                                                           
                                                       
                                                        
                                                    //} 
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            //}

                                                } else if (jsIndivServActParam.getString("name").equalsIgnoreCase("EndPoint")) {
                                                
                                                       result = new Hashtable();
                                                    
                                                       result.put("xAxis",jsIndivServActParam.getString("xAxis"));
                                                       result.put("yAxis",jsIndivServActParam.getString("yAxis"));
                                                       result.put("zAxis",jsIndivServActParam.getString("zAxis"));
                                                       oneKeywordResult.put("EndPoint",result);
                                                       two=true;
                                             
                                                }
                                                
                                                if (one && two){
                                                   // allActionResult = new Hashtable();
                                                    oneKeywordResult.put("nodeAddress", jsIndivServ.getString("nodeAddress"));
                                                    oneKeywordResult.put("requestPort", jsIndivServ.getString("requestPort"));
                                                            if(jsIndivServ.has("responsePort")){
                                                                oneKeywordResult.put("responsePort", jsIndivServ.getString("responsePort"));
                                                            }
                                                    allActionResult.put(jsActParam.getString("name"),oneKeywordResult);
                                                    
                                                }
                                            
                                            }
                                 
                                            if (!allActionResult.isEmpty() && actIterat==jsIndivServAct.length()){
                                                allKeywordResult.put(jsIndivServ.getString("serviceName"),allActionResult);
                                                allActionResult = new Hashtable();
                                            }
                                            
                                        }
                                        
                                    }
                                    actIterat++;
                                
                         }
                         
                     //}
                       
                   }
                     
                     //if (!allActionResult.isEmpty()){
                    //     allKeywordResult.put(jsIndivServ.getString("serviceName"),allActionResult);
                    // }
                     
                 }
                 
               }
                
               
            
           // }
            
            
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (allKeywordResult.isEmpty()){
            return new Hashtable();
        } else{
            return allKeywordResult;
        }
  
    }
     
     public Hashtable ObtainConveyorDimensionWithKeywordAndParamName(String keyword, String RegistryContent)
     {
        
        Hashtable result = new Hashtable();
        
        Hashtable oneKeywordResult = new Hashtable();
        
        
        
        Hashtable allKeywordResult = new Hashtable();
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        
        StringSplitter strsplit = new StringSplitter();
        
        try {

            JSONObject js  = new JSONObject(new JSONTokener(RegistryContent));
            
           // for (int i=0;i<keywords.length;i++){
                
                oneKeywordResult = new Hashtable();
                
                int j=1;
                
              //  String keyword = keywords[i];
                
                Enumeration keysIndivNode = js.keys();
            
                while (keysIndivNode.hasMoreElements()){
                 Object key1 = keysIndivNode.nextElement();
                 
                 JSONObject jsIndivNode = js.getJSONObject(key1.toString());
                 
                 Enumeration keysIndivServ = jsIndivNode.keys();
                 
                 while (keysIndivServ.hasMoreElements()){
                     Object key2 = keysIndivServ.nextElement();
                     
                     JSONObject jsIndivServ = jsIndivNode.getJSONObject(key2.toString());
                     
                     result = new Hashtable();
        
                     oneKeywordResult = new Hashtable();
                     
                     if (jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider")){
                     
                        //if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                         if (jsIndivServ.has("physicalDescription")){
                             JSONObject jsIndivPhyDesc = jsIndivServ.getJSONObject("physicalDescription");
                             
                             Enumeration keysjsPhyDescInd =jsIndivPhyDesc.keys();
                                             
                                             while (keysjsPhyDescInd.hasMoreElements()){
                                                 
                                                  String keyjsPhyDescInd = keysjsPhyDescInd.nextElement().toString();
                                                 
                                                  JSONObject jsPhyDescrParam = jsIndivPhyDesc.getJSONObject(keyjsPhyDescInd);
                                                  
                                                  //Enumeration keysjsPhyDescParam = jsPhyDescrParam.keys();
                                                  
                                               //   while (keysjsPhyDescParam.hasMoreElements()){
                                                 //     String keyjsPhyDescParam = keysjsPhyDescParam.nextElement().toString();
                                                      
                                                      if (jsPhyDescrParam.getString("name").equalsIgnoreCase("Position")){

                                            //if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){
                                                                result = new Hashtable();  
                                                          
                                                          
                                                                result.put("from",jsPhyDescrParam.getString("from"));
                                                                result.put("to",jsPhyDescrParam.getString("to"));
                                                                //result.put("description",jsPhyDescrParam.getString("description"));
                                                                oneKeywordResult.put(jsPhyDescrParam.getString("description"),result);
                                                            
                                                    //if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                                
                                                                
                                                                
//j++;
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                     //    }
                                                           
                                                    //} 
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
                                            //}

                                                        }
                                                      
                                               //   }
                                                 
                                             }
                             allKeywordResult.put(jsIndivServ.getString("serviceName"),oneKeywordResult);
                         }
                         
                     //}
                       
                   }
                     
                 }
                 
               }
                
               
            
           // }
            
            
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (result==null){
            return new Hashtable();
        } else{
            return allKeywordResult;
        }
  
    }
     
     //generate result of Nodelocation in their particular conveyors
     public Hashtable GenerateNodeLocationOnConveyor(Hashtable NodeLoc, Hashtable ConvDimension){
         
         Hashtable axis = new Hashtable();
         
         Hashtable nodelocOnConv = new Hashtable();
         
         Enumeration keysNodeLoc = NodeLoc.keys();
         
         while (keysNodeLoc.hasMoreElements()){
             String keyNodeLoc = keysNodeLoc.nextElement().toString();
             Hashtable EachNodeLoc = (Hashtable) NodeLoc.get(keyNodeLoc);
             
             Enumeration keysNodeLocAxis = EachNodeLoc.keys();
             
              while (keysNodeLocAxis.hasMoreElements()){
                  
                  String keyNodeLocAxis = keysNodeLocAxis.nextElement().toString();
                  
                  //got value of physical location of a device, then after this compared with conveyor position
                  
                  String valAxis = EachNodeLoc.get(keyNodeLocAxis).toString();
                  
                  axis.put(keyNodeLocAxis, valAxis);
                  
                  if (axis.containsKey("xAxis") && axis.containsKey("yAxis") && axis.containsKey("zAxis")){
                      
                      //now start extracting info from conveyor dimension
                      Enumeration keysConvDimension = ConvDimension.keys();
                      
                      while (keysConvDimension.hasMoreElements()){
                          
                          String keyConvDimension = keysConvDimension.nextElement().toString();
                          
                          Hashtable AllAxisDimension = (Hashtable)ConvDimension.get(keyConvDimension);
                          
                        //  Enumeration keysAllAxisDimension = AllAxisDimension.keys();
                          
                         // while (keysAllAxisDimension.hasMoreElements()){
                              
                            //  String AxisDimension = keysAllAxisDimension.nextElement().toString();
                              
                              Hashtable xAxis = (Hashtable)AllAxisDimension.get("xAxis"); 
                              Hashtable yAxis = (Hashtable)AllAxisDimension.get("yAxis"); 
                              
                              double minXAxis = Double.parseDouble(xAxis.get("from").toString());
                              double maxXAxis = Double.parseDouble(xAxis.get("to").toString());
                              double minYAxis = Double.parseDouble(yAxis.get("from").toString());
                              double maxYAxis = Double.parseDouble(yAxis.get("to").toString());
                                      
                                      if ((Double.parseDouble(axis.get("xAxis").toString())<=maxXAxis || Double.parseDouble(axis.get("xAxis").toString())>=minXAxis) && (Double.parseDouble(axis.get("yAxis").toString())<=maxYAxis || Double.parseDouble(axis.get("yAxis").toString())>=minYAxis)){
                                          nodelocOnConv.put(keyNodeLoc,keysConvDimension );
                                      }
                                    
                         // }
                          
                          
                      }
                      
                  } 
                  
                  
              }
             
         }
         return nodelocOnConv;
     }
     
     private Hashtable FindActionOnTheNode(String xAxis, String yAxis, String RegistryContent){
         
         Hashtable result = new Hashtable();
         Hashtable actDetail = new Hashtable();
         int i=1;
        
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
                             
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    double startPointXAxis=-1.00;
                                    double startPointYAxis=-1.00;
                                    double endPointXAxis=-1.00;
                                    double endPointYAxis=-1.00;
                                    
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    boolean XExist=false;
                                    boolean YExist=false;
                                    
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        
                                        
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase("StartPoint")){

                                            startPointXAxis = Double.parseDouble(jsIndivServActParam.getString("xAxis"));
                                            startPointYAxis = Double.parseDouble(jsIndivServActParam.getString("yAxis"));
                                            XExist=true;

                                        } else if (jsIndivServActParam.getString("name").equalsIgnoreCase("EndPoint")){
                                            
                                            endPointXAxis = Double.parseDouble(jsIndivServActParam.getString("xAxis"));
                                            endPointYAxis = Double.parseDouble(jsIndivServActParam.getString("yAxis"));
                                            YExist=true;
                                        }
                                        
                                        if (XExist && YExist){
                                            
                                            if ((Double.parseDouble(xAxis)>=startPointXAxis && Double.parseDouble(xAxis)<=endPointXAxis) && (Double.parseDouble(yAxis)>=startPointYAxis && Double.parseDouble(yAxis)<=endPointYAxis)){
                                               
                                                actDetail.put("actionName",jsActParam.getString("name"));
                                                actDetail.put("serviceName",jsIndivServ.getString("serviceName"));
                                                actDetail.put("xAxisOrigin",startPointXAxis);
                                                actDetail.put("xAxisDestination",endPointXAxis);
                                                actDetail.put("yAxisOrigin",startPointYAxis);
                                                actDetail.put("yAxisDestination",endPointYAxis);
                                                //action name obtained
                                                result.put(Integer.toString(i),actDetail);
                                                
                                                //get closest node???
                                                
                                                
                                            }
                                            
                                            XExist=false;
                                            YExist=false;
                                            
                                        }
    
                                 }
                                 
                             
                             
                         }
                         
                     
                     
                   }
                     
                 }
                 
            }
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
         
     }
     
     
     
     // this is the actual service matching, resulting in all service actions details for service invocation. Gotta be the most complex 
     //construct all possible paths, should generate also the total distance
    
     public Hashtable GenerateActionList (String startPointKeyword, String endPointKeyword, Hashtable AllNodePhysicalLocation, String RegistryContent, String serviceConsName, String targetSignalName){
         
         //find the serviceName of the startpoint from keyword, to find the start PhysicalNodePosition
         
         Hashtable result = new Hashtable();
         Hashtable indivActList = new Hashtable();
         Hashtable actList = new Hashtable();
         
         
        
        String startPointServNameResult = "";
        String endPointServNameResult = "";
        //System.out.println("Matching Algo reg: " +RegistryContent);
        
        
        StringSplitter strsplit = new StringSplitter();
        
        try {

            JSONObject js  = new JSONObject(new JSONTokener(RegistryContent));
            
         //   for (int i=0;i<keywords.length;i++){
                
                //oneKeywordResult = new Hashtable();
                
                int j=1;
                
               // String keyword = keywords[i];
                
                Enumeration keysIndivNode = js.keys();
            
                while (keysIndivNode.hasMoreElements()){
                 Object key1 = keysIndivNode.nextElement();
                 
                 JSONObject jsIndivNode = js.getJSONObject(key1.toString());
                 
                 Enumeration keysIndivServ = jsIndivNode.keys();
                 
                 while (keysIndivServ.hasMoreElements()){
                     Object key2 = keysIndivServ.nextElement();
                     
                     JSONObject jsIndivServ = jsIndivNode.getJSONObject(key2.toString());
                     
                     if (jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider")){
                     
                        //if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                            JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                            Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                            while (keysActionIndex.hasMoreElements()){
                             
                                Object keyActIndex = keysActionIndex.nextElement();

                                JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                                    if (jsActParam.getString("keyword").contains(startPointKeyword)){
                                 
                                        if (jsIndivServ.has("physicalDescription")){
                                            JSONObject jsPhyDesc = jsIndivServ.getJSONObject("physicalDescription");
                                            
                                             Enumeration keysjsPhyDescInd = jsPhyDesc.keys();
                                             
                                             while (keysjsPhyDescInd.hasMoreElements()){
                                                 
                                                  String keyjsPhyDescInd = keysjsPhyDescInd.nextElement().toString();
                                                 
                                                  JSONObject jsPhyDescrParam = jsPhyDesc.getJSONObject(keyjsPhyDescInd);
                                                  
                                                  //Enumeration keysjsPhyDescParam = jsPhyDescrParam.keys();
                                                  
                                               //   while (keysjsPhyDescParam.hasMoreElements()){
                                                 //     String keyjsPhyDescParam = keysjsPhyDescParam.nextElement().toString();
                                                      
                                                      if (jsPhyDescrParam.getString("name").equalsIgnoreCase("Position")){

                                            //if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){

                                                    //if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       
                                                               
                                                                
                                                                startPointServNameResult = jsIndivServ.getString("serviceName");
                                                                break; 
//j++;
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                     //    }
                                                           
                                                           
                                                       
                                                        
                                                    //} 
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            //}

                                                        }
                                                      
                                               //   }
                                                 
                                             }
                                             
                                            
                                        }
                                        
                             }
                                    
                                    
                                
                                
                                
                             
                         }
                         
                     //}
                     
                   }
                     
                 }
                 
               }
                
               //allKeywordResult.put(keyword,oneKeywordResult);
            
           // }
            
            
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         //find the serviceName of the endpoint from keyword, to find the final PhsyicalNodePosition
         
        try {

            JSONObject js  = new JSONObject(new JSONTokener(RegistryContent));
            
         //   for (int i=0;i<keywords.length;i++){
                
                //oneKeywordResult = new Hashtable();
                
                int j=1;
                
               // String keyword = keywords[i];
                
                Enumeration keysIndivNode = js.keys();
            
                while (keysIndivNode.hasMoreElements()){
                 Object key1 = keysIndivNode.nextElement();
                 
                 JSONObject jsIndivNode = js.getJSONObject(key1.toString());
                 
                 Enumeration keysIndivServ = jsIndivNode.keys();
                 
                 while (keysIndivServ.hasMoreElements()){
                     Object key2 = keysIndivServ.nextElement();
                     
                     JSONObject jsIndivServ = jsIndivNode.getJSONObject(key2.toString());
                     
                     if (jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider")){
                     
                        //if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                            JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                            Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                            while (keysActionIndex.hasMoreElements()){
                             
                                Object keyActIndex = keysActionIndex.nextElement();

                                JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                                    if (jsActParam.getString("keyword").contains(endPointKeyword)){
                                 
                                        if (jsIndivServ.has("physicalDescription")){
                                            JSONObject jsPhyDesc = jsIndivServ.getJSONObject("physicalDescription");
                                            
                                             Enumeration keysjsPhyDescInd = jsPhyDesc.keys();
                                             
                                             while (keysjsPhyDescInd.hasMoreElements()){
                                                 
                                                  String keyjsPhyDescInd = keysjsPhyDescInd.nextElement().toString();
                                                 
                                                  JSONObject jsPhyDescrParam = jsPhyDesc.getJSONObject(keyjsPhyDescInd);
                                                  
                                                  //Enumeration keysjsPhyDescParam = jsPhyDescrParam.keys();
                                                  
                                               //   while (keysjsPhyDescParam.hasMoreElements()){
                                                 //     String keyjsPhyDescParam = keysjsPhyDescParam.nextElement().toString();
                                                      
                                                      if (jsPhyDescrParam.getString("name").equalsIgnoreCase("Position")){

                                            //if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){

                                                    //if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       
                                                               
                                                                
                                                                endPointServNameResult = jsIndivServ.getString("serviceName");
                                                                break; 
//j++;
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                     //    }
                                                           
                                                           
                                                       
                                                        
                                                    //} 
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            //}

                                                        }
                                                      
                                               //   }
                                                 
                                             }
                                             
                                            
                                        }
                                        
                             }
                                
                             
                         }
                         
                     //}
                     
                   }
                     
                 }
                 
               }
                
               //allKeywordResult.put(keyword,oneKeywordResult);
            
           // }
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // start creating path from nodes, from start to the endpoint. Get the physical location of the start
       
        Hashtable StartNodeLoc = (Hashtable) AllNodePhysicalLocation.get(startPointServNameResult);
        
        Hashtable EndNodeLoc = (Hashtable) AllNodePhysicalLocation.get(endPointServNameResult);
        
        Hashtable RecursivedNodeLoc = AllNodePhysicalLocation;
        
        Hashtable AllPath = new Hashtable();
        
        Hashtable SequencedNodePath = new Hashtable();
        
        Hashtable currPoint = new Hashtable();
        
        SequencedNodePath.put("1",StartNodeLoc);
        
        //currPoint = (Hashtable )AllNodePhysicalLocation.get(startPointServNameResult);
        
        RecursivedNodeLoc.remove(startPointServNameResult);
        
        boolean developNode=true;
        
        //priority finding node = find closest node that has an action to deliver product to that node in one action. If for some reason the built path encounter a dead en
        // retrace step back to the very last node located on the latest previously used conveyor (not even node created by diverter!)--> DIFFICULT!
        
        int i=2;
        /*
        //this end when the current point node is the same location as the endpoint node location
        while(developNode){
            
            
            String currPointxAxis = (currPoint.get("xAxis").toString());
            String currPointyAxis = (currPoint.get("yAxis").toString());
            
            Double doubleCurrPointxAxis = Double.parseDouble(currPoint.get("xAxis").toString());
            Double doubleCurrPointyAxis = Double.parseDouble(currPoint.get("yAxis").toString());
            
            //find all action that deliver product from the particular currPoint first. And from the reach of the found action, find the closest node reachable by that action
            
            Hashtable actionOneNode = FindActionOnTheNode(currPointxAxis, currPointyAxis, RegistryContent);
            
            if (actionOneNode.size()==1){
                Hashtable actDetail = (Hashtable) actionOneNode.get("1");
                
                Enumeration keysRecursivedNodeLoc= RecursivedNodeLoc.keys();
                
               
                
                while (keysRecursivedNodeLoc.hasMoreElements()){
                   
                    String keyRecursiveNL = keysRecursivedNodeLoc.nextElement().toString();
                    
                    Hashtable RecNLXY = (Hashtable)RecursivedNodeLoc.get(keyRecursiveNL);
                    //check if node position is within reach of the action
                    if ((Double.parseDouble(RecNLXY.get("xAxis").toString())<=Double.parseDouble(actDetail.get("xAxisOrigin").toString()) && (Double.parseDouble(RecNLXY.get("xAxis").toString())<=Double.parseDouble(actDetail.get("xAxisOrigin").toString()))) && (Double.parseDouble(RecNLXY.get("yAxis").toString())<=Double.parseDouble(actDetail.get("yAxisOrigin").toString()) && (Double.parseDouble(RecNLXY.get("yAxis").toString())<=Double.parseDouble(actDetail.get("yAxisOrigin").toString())))){
                        
                        SequencedNodePath.put(Integer.toString(i), (Hashtable) RecursivedNodeLoc.get(keyRecursiveNL));
                        
                        if ((RecNLXY.get("xAxis").toString().equalsIgnoreCase(EndNodeLoc.get("xAxis").toString())) && (RecNLXY.get("yAxis").toString().equalsIgnoreCase(EndNodeLoc.get("yAxis").toString())) ){
                            developNode=false;
                            
                            break;
                        } else {
                            i++;
                            currPoint = RecNLXY;
                        }
                        
                        
                    }
                    
                }
                
            }
            
        }
        */
        
        //for now, hard code the route, if action PE mid exist, go through middle conv, otherwise go through longer route
        boolean midConvExist=false;
        
        
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
                     
                     //if ((startPointServNameResult.equalsIgnoreCase(jsIndivServ.getString("serviceName"))) && (jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider"))){
                     
                     
                     
                     JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                            
                     
                            boolean xOk=false;
                            boolean yOk=false;
                            
                            Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                            while (keysActionIndex.hasMoreElements()){
                             
                                Object keyActIndex = keysActionIndex.nextElement();

                                JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                               // if (jsActParam.getString("keyword").contains(convKeyword)){
                                 
                                    JSONObject jsPhysDesc = jsIndivServ.getJSONObject("physicalDescription");
                         
                                    Enumeration keysjsPhyDescInd = jsPhysDesc.keys();
                                             
                                    
                                    
                                             while (keysjsPhyDescInd.hasMoreElements()){
                                                 
                                                  String keyjsPhyDescInd = keysjsPhyDescInd.nextElement().toString();
                                                 
                                                  JSONObject jsPhyDescrParam = jsPhysDesc.getJSONObject(keyjsPhyDescInd);
                                                  
                                                  //Enumeration keysjsPhyDescParam = jsPhyDescrParam.keys();
                                                  
                                               //   while (keysjsPhyDescParam.hasMoreElements()){
                                                 //     String keyjsPhyDescParam = keysjsPhyDescParam.nextElement().toString();
                                                      
                                                      if (jsPhyDescrParam.getString("name").equalsIgnoreCase("Dimension") && jsPhyDescrParam.getString("description").equalsIgnoreCase("xAxis")){

                                                          
                                                          if (jsPhyDescrParam.getString("from").equalsIgnoreCase("150.00") && jsPhyDescrParam.getString("to").equalsIgnoreCase("150.00")){
                                                              xOk=true;
                                                          }
                                                          
                                            //if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){

                                                    //if (jsIndivServActParam.getString("associatedActionName").contains(";")){
                                                        
                                                       
                                                               
                                                                
                                                                 
//j++;
                                                        // if(jsActParam.getString("keyword").contains(keyword)){
                                                             
                                                     //    }
                                                           
                                                           
                                                       
                                                        
                                                    //} 
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                            //}

                                                        } else if (jsPhyDescrParam.getString("name").equalsIgnoreCase("Dimension") && jsPhyDescrParam.getString("description").equalsIgnoreCase("yAxis")){
                                                            
                                                            if (jsPhyDescrParam.getString("from").equalsIgnoreCase("60.00") && jsPhyDescrParam.getString("to").equalsIgnoreCase("0.00")){
                                                              yOk=true;
                                                           }
                                                            
                                                        }
                                                      
                                                        if (xOk && yOk){
                                                            
                                                            midConvExist=true;
                                                            
                                                        }
                                                      
                                                      //specific comparison
                                                          
                                                      
                                               //   }
                                                 
                                             }
                                 
                               // }
                             
                            }
                     
                        //if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                                
                         
                     //}
                     
                    //}
                     
                 }
                 
            }
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Hashtable nodePath= new Hashtable();
        Hashtable indivNode= new Hashtable();
        
        int num=1;
        
        if (midConvExist){
            
            
            
            //Generate node path via middle conveyor
            nodePath.put("1",StartNodeLoc);
            indivNode.put("xAxis","150.00");
            indivNode.put("yAxis","60.00");
            nodePath.put("2",indivNode);
            indivNode = new Hashtable();
            indivNode.put("xAxis","150.00");
            indivNode.put("yAxis","55.00");
            nodePath.put("3", indivNode);
            indivNode = new Hashtable();
            indivNode.put("xAxis","150.00");
            indivNode.put("yAxis","30.00");
            nodePath.put("4", indivNode);
            indivNode = new Hashtable();
            indivNode.put("xAxis","150.00");
            indivNode.put("yAxis","0.00");
            //indivNode = new Hashtable();
            nodePath.put("5",EndNodeLoc);
            
            
            
            
            
            //actuate diverter
            
            
            
            
            
            //do matching here, find conv, diverters and actuate
            
            
        } else {
            
            
            nodePath.put("1",StartNodeLoc);
            indivNode.put("xAxis","150.00");
            indivNode.put("yAxis","60.00");
            nodePath.put("2",indivNode);
            indivNode = new Hashtable();
            indivNode.put("xAxis","145.00");
            indivNode.put("yAxis","60.00");
            nodePath.put("3", indivNode);
            indivNode = new Hashtable();
            indivNode.put("xAxis","0.00");
            indivNode.put("yAxis","60.00");
            nodePath.put("4", indivNode);
            indivNode = new Hashtable();
            indivNode.put("xAxis","0.00");
            indivNode.put("yAxis","0.00");
            nodePath.put("5", indivNode);
            
            //indivNode = new Hashtable();
            nodePath.put("6",EndNodeLoc);
            
            
            //unenergize diverter
            
            //stop midConv
            
        }
        
        for (int x=1;x<nodePath.size();x++){
                Hashtable Node1 = (Hashtable) nodePath.get(Integer.toString(x));
                Hashtable Node2 = (Hashtable) nodePath.get(Integer.toString(x+1));
                
                
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
                     
                        //if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                            JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                            Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                            while (keysActionIndex.hasMoreElements()){
                             
                                Object keyActIndex = keysActionIndex.nextElement();

                                JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                                //if (jsActParam.getString("keyword").contains(keyword)){
                                 
                                    JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                    Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                    boolean startTrue=false;
                                    boolean endTrue=false;
                                    
                                    double startX = -1.00;
                                    double endX = -1.00;
                                    double startY = -1.00;
                                    double endY = -1.00;
                                    
                                    while (keysServParamInd.hasMoreElements()){
                                     
                                        Object keyParamInd = keysServParamInd.nextElement();
                                     
                                        JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                        if (jsIndivServActParam.getString("name").equalsIgnoreCase("StartPoint")){

                                            //if (jsIndivServActParam.getString("value").equalsIgnoreCase(value)){
                                                startX=Double.parseDouble(jsIndivServActParam.getString("xAxis"));
                                                startY=Double.parseDouble(jsIndivServActParam.getString("yAxis"));
                                                startTrue=true;
                                                    
                                                    
                                                    //result.put("actionName",jsIndivServActParam.getString("associatedActionName"));
                                                   
  
                                                    //}

                                        } else if (jsIndivServActParam.getString("name").equalsIgnoreCase("EndPoint")){
                                            
                                            endX=Double.parseDouble(jsIndivServActParam.getString("xAxis"));
                                            endY=Double.parseDouble(jsIndivServActParam.getString("yAxis"));
                                            endTrue=true;
                                        }
                                        
                                        boolean xOK=false;
                                        boolean yOK=false;
                                        
                                        if (startTrue && endTrue){
                                           
                                            if (startX>=endX){
                                                
                                                if (Double.parseDouble(Node1.get("xAxis").toString())<=startX && Double.parseDouble(Node2.get("xAxis").toString())>=endX ){
                                                    //jsActParam.getString("name");
                                                    xOK=true;
                                                }
                                                
                                            } else {
                                                
                                                if (Double.parseDouble(Node1.get("xAxis").toString())>=startX && Double.parseDouble(Node2.get("xAxis").toString())<=endX ){
                                                    //jsActParam.getString("name");
                                                    xOK=true;
                                                }
                                                
                                            }
                                            
                                            if (startY>=endY){
                                                if (Double.parseDouble(Node1.get("yAxis").toString())<=startY && Double.parseDouble(Node2.get("yAxis").toString())>=endY ){
                                                    //jsActParam.getString("name");
                                                    yOK=true;
                                                }
                                                
                                            } else{
                                                
                                                if (Double.parseDouble(Node1.get("yAxis").toString())>=startY && Double.parseDouble(Node2.get("yAxis").toString())<=endY ){
                                                    //jsActParam.getString("name");
                                                    yOK=true;
                                                }
                                                
                                            }
                                            
                                            if (xOK && yOK){
                                                
                                                if (!indivActList.isEmpty()){
                                                    Enumeration keysActList = indivActList.keys();
                                                    
                                                    while (keysActList.hasMoreElements()){
                                                        String keyActList = keysActList.nextElement().toString();
                                                        
                                                        Hashtable comparedActList = (Hashtable) indivActList.get(keyActList);
                                                        
                                                        if (jsIndivServ.getString("nodeAddress").equalsIgnoreCase(comparedActList.get("nodeAddress").toString()) && jsIndivServ.getString("controlPort").equalsIgnoreCase(comparedActList.get("controlPort").toString()) && jsIndivServ.getString("actionName").equalsIgnoreCase(comparedActList.get("actionName").toString())){
                                                            
                                                        } else {
                                                            
                                                            indivActList.put("nodeAddress",jsIndivServ.getString("nodeAddress"));
                                                            indivActList.put("requestPort",jsIndivServ.getString("requestPort"));
                                                            if(jsIndivServ.has("responsePort")){
                                                                indivActList.put("responsePort",jsIndivServ.getString("responsePort"));
                                                            }
                                                            indivActList.put("actionName",jsActParam.getString("name"));
                                                            actList.put(Integer.toString(num), indivActList);
                                                            num++;
                                                            
                                                        }
                                                        
                                                    }
                                                    
                                                }
                                                
                                                
                                                                 
                                                                 
                                            }
                                           
                                        }
    
                                     }
                                 
                                    //}
                             
                                    }
                         
                                //}
                     
                            }
                     
                        }
                 
                    }
                    
                    result.put("serviceName", serviceConsName);
                    result.put("targetSignalName", targetSignalName);
                    result.put("actionList",actList);
            
                } catch (JSONException ex) {
                    Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        
        //actuate top
        
        //actuate bottom
        
        
        /*
        Enumeration keysAllNodePhysicalLocation = AllNodePhysicalLocation.keys();
        
        while (keysAllNodePhysicalLocation.hasMoreElements()){
            
            String keyAllNodePhysicalLoction = keysAllNodePhysicalLocation.nextElement().toString();
            
            Hashtable XYAxisNodeLocation = (Hashtable) AllNodePhysicalLocation.get(keyAllNodePhysicalLoction);
            
            
            if (!(Double.parseDouble(StartNodeLoc.get("xAxis").toString()) == Double.parseDouble(XYAxisNodeLocation.get("xAxis").toString())) && !(Double.parseDouble(StartNodeLoc.get("yAxis").toString()) == Double.parseDouble(XYAxisNodeLocation.get("yAxis").toString()))){
                
                
                
                Double.parseDouble(XYAxisNodeLocation.get("xAxis").toString());
                
            }
            
            
        }
        
        //find the conveyor action here
        
        
        
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
                     
                     if ((startPointServNameResult.equalsIgnoreCase(jsIndivServ.getString("serviceName"))) && (jsIndivServ.getString("serviceRole").equalsIgnoreCase("provider"))){
                     
                        //if (jsIndivServ.getString("serviceType").equalsIgnoreCase(servType)){
                         
                         JSONObject jsIndivServAct = jsIndivServ.getJSONObject("action");
                         
                         Enumeration keysActionIndex = jsIndivServAct.keys();
                         
                         while (keysActionIndex.hasMoreElements()){
                             
                             Object keyActIndex = keysActionIndex.nextElement();

                             JSONObject jsActParam = jsIndivServAct.getJSONObject(keyActIndex.toString());
                             
                             //if (jsActParam.getString("keyword").contains(keyword)){
                                 
                                 JSONObject jsIndivServParam = jsActParam.getJSONObject("actionParameters");
                                 
                                 Enumeration keysServParamInd = jsIndivServParam.keys();
                                 
                                 while (keysServParamInd.hasMoreElements()){
                                     
                                     Object keyParamInd = keysServParamInd.nextElement();
                                     
                                     JSONObject jsIndivServActParam = jsIndivServParam.getJSONObject(keyParamInd.toString());
                                     
                                     
                                     if (jsIndivServActParam.has("StartPoint") &&  jsIndivServActParam.has("EndPoint")){
                                         
                                         //should compare next point of destination if it matches with this one
                                         if (){
                                             
                                         }
                                         
                                     }
                                    // if (jsIndivServActParam.getString("name").equalsIgnoreCase(paramName)){
                                     
                                        
                                   //  }
                                     
                                 }
                                 
                             //}
                             
                         }
                         
                     //}
                     
                    }
                     
                 }
                 
            }
            
        } catch (JSONException ex) {
            Logger.getLogger(MatchingAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        return result;
        
     }
     
}
