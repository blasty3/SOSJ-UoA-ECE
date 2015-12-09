/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commthread;

import SOAHandler.Support.SJRegistryEntry;
import SOAHandler.Support.SOSJDiscovery;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;

/**
 *
 * @author Atmojo
 */
public class SOSJCommGUIQueryTh implements Runnable{

    String DevelAddr;
    String currReg;
    String currCDStats;
    
    public SOSJCommGUIQueryTh(String DevelAddr, String currReg, String currCDStats){
        this.DevelAddr = DevelAddr;
        this.currReg = currReg;
        this.currCDStats = currCDStats;
    }
    
    @Override
    public void run() {
        
        JSONObject jsReg = SJRegistryEntry.GetRegistryFromEntry();
        
        JSONObject jsTempServReg = new JSONObject();
        JSONObject jsTempRegCDStats = new JSONObject();
        try {
            
            if(currReg.isEmpty()){
                currReg = "{}";
            } 
            
            if(currCDStats.isEmpty()){
                currCDStats = "{}";
            } 
            
            jsTempServReg = new JSONObject(new JSONTokener((currReg)));
            jsTempRegCDStats = new JSONObject(new JSONTokener((currCDStats)));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        
       Enumeration keysJsReg = jsReg.keys();
       
       while(keysJsReg.hasMoreElements()){
           String regID = keysJsReg.nextElement().toString();
           try {
               
               String regAddr = jsReg.getString(regID);
               
               JSONObject jsIndivRetServReg = SOSJDiscovery.TransceiveDiscMsg(DevelAddr, regID, regAddr);
               
               if(jsTempServReg.isEmpty()){
                   
                   jsTempServReg = jsIndivRetServReg.getJSONObject("serviceList");
                   
               } else {
                   
                   JSONObject jsAcquiredServ  = jsIndivRetServReg.getJSONObject("serviceList");
                   Enumeration keysIndivRetServReg = jsAcquiredServ.keys();
                   
                   while(keysIndivRetServReg.hasMoreElements()){
                       
                       String keyJsAcquiredServ = keysIndivRetServReg.nextElement().toString();
                       
                       jsTempServReg.put(keyJsAcquiredServ, jsAcquiredServ.getJSONObject(keyJsAcquiredServ));
                       
                   }
                   
                   
                   
               }
               
               if(jsTempRegCDStats.isEmpty()){
                   jsTempRegCDStats = jsIndivRetServReg.getJSONObject("CDStats");
                   
               } else {
                   
                   JSONObject jsAcquiredRegCD  = jsIndivRetServReg.getJSONObject("CDStats");
                   
                   Enumeration keysIndivRetRegCD = jsAcquiredRegCD.keys();
                   
                   while(keysIndivRetRegCD.hasMoreElements()){
                       
                       String keyIndivRetRegCD = keysIndivRetRegCD.nextElement().toString();
                       
                       jsTempRegCDStats.put(keyIndivRetRegCD, jsAcquiredRegCD.getJSONObject(keyIndivRetRegCD));
                       
                   }
               }
               
               SOSJCommandGUIMailbox.SetQueriedServRegRes(jsTempServReg);
               SOSJCommandGUIMailbox.SetQueriedRegCDRes(jsTempRegCDStats);
               
               
           } catch (JSONException ex) {
              ex.printStackTrace();
           }
           
       }
       
       SOSJCommandGUIMailbox.SetQueryFinishedStat(true);
        
    }
    
}
