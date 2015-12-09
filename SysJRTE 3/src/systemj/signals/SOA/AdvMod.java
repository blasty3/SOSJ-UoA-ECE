/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systemj.signals.SOA;

import java.io.Serializable;
import java.util.Hashtable;
import systemj.common.SOAFacility.Support.SOABuffer;
import systemj.interfaces.GenericSignalSender;

/**
 *
 * @author Udayanto
 */
public class AdvMod extends GenericSignalSender implements Serializable{

    String servName;
    
    @Override
    public void configure(Hashtable data) throws RuntimeException {
       
        if (data.containsKey("associatedServiceName")){
            servName = (String)data.get("associatedServiceName");
        }  else {
            throw new RuntimeException("Signal name:" +data.get("Name")+" need 'associatedServiceName' attribute defined");
        }
    }

    @Override
    public void run() {
        
        Object[] obj = super.buffer;
	String visibStat = (String) obj[1];
        
        
        if (visibStat.equalsIgnoreCase("Available") || visibStat.equalsIgnoreCase("Invisible") || visibStat.equalsIgnoreCase("Failed") || visibStat.equalsIgnoreCase("unavailable") ){
        
            SOABuffer.putAdvModToBuffer(servName,visibStat);
            SOABuffer.SetAdvTransmissionRequest(true);
        
        }
        
        
        
    }
    
    public AdvMod(){
		super();
	}
    
}
