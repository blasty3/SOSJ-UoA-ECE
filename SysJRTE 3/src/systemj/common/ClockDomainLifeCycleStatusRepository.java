/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package systemj.common;

import java.util.Hashtable;

/**
 *
 * @author Udayanto
 */
public class ClockDomainLifeCycleStatusRepository {
    
    private static Hashtable CDNameAndStatusList = new Hashtable();
    private final static Object CDNameAndStatusListLock = new Object();
    
    public static void AddCDNameAndStatus(String CDName, String Status){
        synchronized(CDNameAndStatusListLock){
            if(!CDNameAndStatusList.containsKey(CDName)){
                CDNameAndStatusList.put(CDName, Status);
            }
        }
    }
    
    public static boolean IsCDStatusRepositoryHas(String CDName){
        synchronized(CDNameAndStatusListLock){
            if(CDNameAndStatusList.containsKey(CDName)){
               return true;
            } else {
               return false;
            }
        }
    }
    
    public static void ModifyCDStatus(String CDName, String NewStatus){
        synchronized(CDNameAndStatusListLock){
            if(CDNameAndStatusList.containsKey(CDName)){
                CDNameAndStatusList.put(CDName, NewStatus);
            }
        }
    }
    
    public static String GetCDStatus(String CDName){
        synchronized(CDNameAndStatusListLock){
            if(CDNameAndStatusList.containsKey(CDName)){
                return CDNameAndStatusList.get(CDName).toString();
            } else {
                return "CD doesn't exist";
            }
            
        }
    }
    
    public static Hashtable GetAllCDStatuses(){
        synchronized(CDNameAndStatusListLock){
            return CDNameAndStatusList;
        }
    } 
    
    public static void RemoveCDStatus(String CDName){
        CDNameAndStatusList.remove(CDName);
    }
    
}
