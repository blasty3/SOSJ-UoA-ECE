/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commthread;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author Atmojo
 */
public class SOSJCommandGUIMailbox {
    
    private static boolean autoQueryEnabled = false;
    private final static Object autoQueryEnabledLock = new Object();
    private static boolean queryFinished = false;
    private final static Object queryFinishedLock = new Object();
    private static JSONObject QueriedServRegRes = new JSONObject();
    private final static Object QueriedServRegResLock = new Object();
    private static JSONObject QueriedRegCDRes = new JSONObject();
    private final static Object QueriedRegCDResLock = new Object();
    
    public static void SetAutoQueryEnabledStat(boolean stat){
        synchronized(autoQueryEnabledLock){
            autoQueryEnabled = stat;
        }
    }
    
    public static boolean GetAutoQueryEnabledStat(){
        synchronized(autoQueryEnabledLock){
            return autoQueryEnabled;
        }
    }
    
    public static void SetQueryFinishedStat(boolean stat){
        synchronized(queryFinishedLock){
            queryFinished = stat;
        }
    }
    
    public static boolean GetQueryFinishedStat(){
        synchronized(queryFinishedLock){
            return queryFinished;
        }
    }
    
     public static void SetQueriedServRegRes(JSONObject jsRes){
        synchronized(QueriedServRegResLock){
            Enumeration keysJsRes = jsRes.keys();
            
            while(keysJsRes.hasMoreElements()){
                String keyJSRes = keysJsRes.nextElement().toString();
                
                try {
                    QueriedServRegRes.put(keyJSRes, jsRes.getJSONObject(keyJSRes));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                
            }
        }
    }
    
    public static JSONObject GetQueriedServRegRes(){
        synchronized(QueriedServRegResLock){
            return QueriedServRegRes;
        }
    }
    
     public static void SetQueriedRegCDRes(JSONObject jsRegCDRes){
        synchronized(QueriedRegCDResLock){
            Enumeration keysJsRegCDRes = jsRegCDRes.keys();
            
            while(keysJsRegCDRes.hasMoreElements()){
                String keyJSRes = keysJsRegCDRes.nextElement().toString();
                
                try {
                    QueriedRegCDRes.put(keyJSRes, jsRegCDRes.getJSONObject(keyJSRes));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                
            }
        }
    }
    
    public static JSONObject GetQueriedRegCDRes(){
         synchronized(QueriedRegCDResLock){
            return QueriedRegCDRes;
        }
    }
}
