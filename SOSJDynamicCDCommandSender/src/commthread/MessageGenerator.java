/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commthread;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author Atmojo
 */
public class MessageGenerator {
    
    public JSONObject GenerateMessageOfJSON(String MsgType, String DevelAddr, String SSName, String CDName){
        JSONObject jsMsg = new JSONObject();
        
        try {
            jsMsg.put("DevelAddr",DevelAddr);
            jsMsg.put("MsgType",MsgType);
            jsMsg.put("CDName", CDName);
            jsMsg.put("SSName", SSName);
        } catch (JSONException ex) {
           ex.printStackTrace();
        }
        
        return jsMsg;
        
    }
    
    public JSONObject GenerateMessageOfJSON(String MsgType, String DevelAddr,String SSName,String CDName, JSONObject CDMap, JSONObject ServDesc){
        
        JSONObject jsMsg = new JSONObject();
        
        try {
            jsMsg.put("DevelAddr",DevelAddr);
            jsMsg.put("MsgType",MsgType);
            jsMsg.put("CDName", CDName);
            jsMsg.put("CDMap",CDMap);
            jsMsg.put("SSName", SSName);
            jsMsg.put("CDServDesc", ServDesc);
        } catch (JSONException ex) {
           ex.printStackTrace();
        }
        
        return jsMsg;
    }
    
    public JSONObject GenerateMessageOfJSON(String MsgType, String DevelAddr, String SSName, String CDName, String SSDest, String MigType, JSONObject CDMap, JSONObject ServDesc){
        
        JSONObject jsMsg = new JSONObject();
        
        try {
            jsMsg.put("DevelAddr",DevelAddr);
            jsMsg.put("MsgType",MsgType);
            jsMsg.put("CDName", CDName);
            jsMsg.put("CDMap",CDMap);
            jsMsg.put("CDServDesc", ServDesc);
            jsMsg.put("MigType", MigType);
            jsMsg.put("SSName", SSName);
            jsMsg.put("DestSS", SSDest);
        } catch (JSONException ex) {
           ex.printStackTrace();
        }
        
        return jsMsg;
    }
    
    public JSONObject GenerateReconfigChanMessageOfJSON(String MsgType, String DevelAddr, String SSName, String CDName, String ChanName, String ChanDir,String PartnerChanSSName, String PartnerChanCDName, String PartnerChanName){
        
        JSONObject jsMsg = new JSONObject();
        
        try {
            jsMsg.put("DevelAddr",DevelAddr);
            jsMsg.put("MsgType",MsgType);
            jsMsg.put("CDName", CDName);
            jsMsg.put("SSName",SSName);
            jsMsg.put("ChanDir", ChanDir);
            jsMsg.put("ChanName", ChanName);
            jsMsg.put("PartnerChanSSName", PartnerChanSSName);
            jsMsg.put("PartnerChanCDName", PartnerChanCDName);
            jsMsg.put("PartnerChanName", PartnerChanName);
        } catch (JSONException ex) {
           ex.printStackTrace();
        }
        
        return jsMsg;
    }
    
    public JSONObject GenerateReconfigChanMessageOfJSON(String MsgType, String DevelAddr,String SSName, String CDName, String ChanName, String ChanDir,String PartnerChanCDName, String PartnerChanName){
        
        JSONObject jsMsg = new JSONObject();
        
        try {
            jsMsg.put("DevelAddr",DevelAddr);
            jsMsg.put("MsgType",MsgType);
            jsMsg.put("CDName", CDName);
            jsMsg.put("SSName",SSName);
            jsMsg.put("ChanName", ChanName);
            jsMsg.put("ChanDir", ChanDir);
            //jsMsg.put("PartnerChanSSName", PartnerChanSSName);
            jsMsg.put("PartnerChanCDName", PartnerChanCDName);
            jsMsg.put("PartnerChanName", PartnerChanName);
        } catch (JSONException ex) {
           ex.printStackTrace();
        }
        
        return jsMsg;
    }
    
    
    
}
