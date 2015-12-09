package systemj.lib;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Hashtable;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import systemj.bootstrap.ClockDomain;
import systemj.common.CDLCBuffer;
import systemj.common.CDObjectsBuffer;
import systemj.common.IMBuffer;
import systemj.common.Interconnection;
import systemj.common.InterfaceManager;
import systemj.common.SJSSCDSignalChannelMap;
import systemj.common.SJServiceRegistry;

import systemj.interfaces.*;

public class output_Channel extends GenericChannel implements Serializable{
        //private String CDNameLoc;
        private String CDState="Created";
        private boolean terminated = false;
        
	public input_Channel partner;
	private int preempted = 0;
	private int w_s = 0;
	private int w_r = 0;
	public output_Channel(){}
	public void set_value(Object in){
            this.value = in; 
            this.modified = true;
        }
	public Object get_value(){return this.value; }
	public int refresh(){
		this.value = null;
		this.w_s = 0;
		this.w_r = 0;
		set_preempted();
		this.modified = true;
		return 1;
	}
        
        private void clean_value_buffer(){
            this.value=null;
        }
        
	public void set_w_s(int in){
            this.w_s = in; 
            this.modified = true;
        }
	public void set_w_r(int in){this.w_r = in; this.modified = true;}
	public int get_w_s(){return this.w_s;}
	public int get_w_r(){return this.w_r;}
        
        //Uday added
        public void terminate_blocking_ochan(){ //forcing blocking to terminate??
            
            //reset to 0?
            
            if(get_w_r()!=0){
                this.w_r=0;
            }
            if(get_w_s()!=0){
                this.w_s=0;
            }
            
            if(!terminated){
                terminated=true;
            }
            
            //set_preempted();
            
        }
        
        public void setChanReconfigureFlag(boolean stat){
            this.chanReconfigureFlag = stat;
        }
        
        public void setDistStateChanged(boolean stat){
            this.distStateChanged = stat;
        }
        
     //   public void terminate_blocking_phase_1(){
     //       if(partner_ref.getChannelCDState()!=null && init){
     //           if(partner_ref.getChannelCDState().equals("Killed")){
     //               terminate_blocking_w_s_w_r();
     //           }
      //      }
      //  } 
        
	private int get_r_r(){return init ? partner.get_r_r() : 0;}
	public void update_w_r(){
            
		if(init){
			if(this.preempted == partner.get_preempted_val()){
                            
                            if(isLocal){
                                
                                if(partner_ref.getChannelCDState()!=null ){  //to avoid uninitialized partner state data, if its uninit'd, then proceed with copying
                                    if(!partner_ref.getChannelCDState().equals("Killed")){
                                        this.w_r = get_r_r();
                                    } else {
                                        terminate_blocking_ochan();
                                    }
                                } else {
                                    this.w_r = get_r_r();
                                }
                                
                            } else {
                                
                                if(partner.getChannelCDState()!=null ){  //to avoid uninitialized partner state data, if its uninit'd, then proceed with copying
                                    if(!partner.getChannelCDState().equals("Killed")){
                                        this.w_r = get_r_r();
                                    } else {
                                        terminate_blocking_ochan();
                                    }
                                } else {
                                    this.w_r = get_r_r();
                                }
                                
                            }
                            
                            
                            
                        }
		}
	}
	public void set_partner(input_Channel partner){this.partner = partner;}
	public int get_preempted_val(){return this.preempted; }
	public void set_preempted() {++this.preempted; ; this.modified = true;}
	
	/**
	 * Tests whether partner input channel is preempted or re-initialized
	 * @return <b>true</b> - when partner is preempted or re-initialized <br> <b>false</b> - otherwise
	 */
	public boolean get_preempted(){
		// Now output channel is preempted when the input channel is re-initialized (i.e. w_s < w_r)
		
                //need to check if partner is not terminated, if terminated, return true;
            
                if(init){
                    //System.out.println("State : " +partner.getChannelCDState());
			if(partner.get_preempted_val() > this.preempted || this.w_s < this.w_r) 
				return true; 
		}
		return false;
	}

	// Modular
	public void set_preempted(int num){this.preempted=num; this.modified = true;}
	public void gethook(){
            
            //System.out.println("get hook");
            
		if(init){
			if(isLocal) {
                            
                          //  if(!partner.getChannelCDState().equals("Killed")){
                                partner_ref.updateLocalPartner(this.partner);
                          //  } else {
                                
                            //    terminate_blocking_w_s_w_r();
                
                                //refresh();
                         //   }
                            
                        } else {
                            
                            if(incoming){
				this.getBuffer();
				// Little trick to make sure that partner channel knows preemption status of this channel
				if(partner.get_preempted_val() < this.preempted) 
					modified = true;
                            } 
                            
                          //  if (stateChangeIncoming){
                          //      this.getStateChangeBuffer();
                           // }
                            
                        }
                            
                            
                            
                            
		}
	}
	public synchronized void getBuffer(){
            
            //also updates remote (distributed) partner state
            
                //partner.setChannelCDState((String)toReceive[6]);
            //System.out.println("getBuffer executed");
            if(toReceive.length==6){
                
            
		partner.set_r_s(((Integer)toReceive[1]).intValue());
		partner.set_r_r(((Integer)toReceive[2]).intValue());
		partner.set_preempted(((Integer)toReceive[3]).intValue());
                
            } else if(toReceive.length==7){
                    
                    String partnerName = (String)toReceive[5];
                    
                    String partnerCDName = partnerName.split("\\.")[0];
                    
                    //InterfaceManager im = super.getInterfaceManager();
                    
                    InterfaceManager im = IMBuffer.getInterfaceManagerConfig();
                    
                    System.out.println("OChan buffer received, SSLoc : " +toReceive[6]+ "partnerCDName: " +partnerCDName);
                    
                    im.addCDLocation((String)toReceive[6], partnerCDName);
                    
                    IMBuffer.SaveInterfaceManagerConfig(im);
                    
                    super.setInterfaceManager(im);
                    
                    this.modified = true;
                    
                } else if(toReceive.length==3){
                    
                    partner.setChannelCDState((String)stateChangetoReceive[2]);
                    
                    String CDPartnerState = (String)stateChangetoReceive[2];
                    
                    if(CDPartnerState.equalsIgnoreCase("Killed")){
                        
                        InterfaceManager IM = IMBuffer.getInterfaceManagerConfig();
                        
                        Interconnection IC = IM.getInterconnection();
                        
                        String partnerCDName = toReceive[5].toString().split("\\.")[0];
                    
                        //String partnerCDName = partnerName.split("\\.")[0];
                        
                        String ChanName = this.getName().split("\\.")[1];
                        String CDName = this.getName().split("\\.")[1];
                        
                        String SSLoc;
                        
                        if(IM.hasCDLocation(partnerCDName)){
                            SSLoc = IM.getCDLocation(partnerCDName);
                        } else {
                            SSLoc = SJServiceRegistry.GetCDRemoteSSLocation(partnerCDName);
                        }
                        
                        SJSSCDSignalChannelMap.removeChanPartner(SSLoc, CDName, "output", ChanName);
                        
                        IM.setInterconnection(IC);
                        
                        IMBuffer.SaveInterfaceManagerConfig(IM);
                        
                        SJSSCDSignalChannelMap.SetCheckLinkToRemove();
                    }
                    
                } else if (toReceive.length==2){
                    
                    String changeInPartner = (String)toReceive[0];
                    
                    String CDPartName = changeInPartner.split("\\.")[0];
                    String ChanPartName = changeInPartner.split("\\.")[1].split("_")[0];
                    
                    PartnerName = CDPartName+"."+ChanPartName+"_in";
                    
                } else if(toReceive.length==4) {
                    
                    if(toReceive[2].toString().equalsIgnoreCase("ReconfigChan")){
                        
                        String changeInPartner = (String)toReceive[3];
                    
                        String oldPartnerChan = PartnerName;
                        
                        if(!PartnerName.equalsIgnoreCase(".")){
                            
                            String OldCDPartName = oldPartnerChan.split("\\.")[0];
                            String OldChanPartName = oldPartnerChan.split("\\.")[1].split("_")[0];
                        
                            CDLCBuffer.AddOldPartnerChanReconfig("input", OldCDPartName, OldChanPartName);
                            
                        }
                        
                         if(!changeInPartner.equalsIgnoreCase(".")){
                            
                            String NewCDPartName = changeInPartner.split("\\.")[0];
                            String NewChanPartName = changeInPartner.split("\\.")[1].split("_")[0];
                            
                            PartnerName = NewCDPartName+"."+NewChanPartName+"_in";
                            
                        } else {
                            PartnerName = ".";
                        }
                          
                    
                    //reconfigure old partner's partner, if any
                    
                        
                        
                    } else if(toReceive[2].toString().equalsIgnoreCase("ReconfigPartnerChan")){
                        
                        String changeInPartnersPartner = (String)toReceive[3];
                    
                        
                        if(!changeInPartnersPartner.equalsIgnoreCase(".")){
                            
                            String NewCDPartName = changeInPartnersPartner.split("\\.")[0];
                            String NewChanPartName = changeInPartnersPartner.split("\\.")[1].split("_")[0];
                    
                            
                            PartnerName = NewCDPartName+"."+NewChanPartName+"_in";
                            
                            
                        } else {
                            PartnerName = ".";
                        }
                    
                        
                    
                        //String OldCDPartName = oldPartnerChan.split("\\.")[0];
                        //String OldChanPartName = oldPartnerChan.split("\\.")[1].split("_")[0];
                        
                        
                        
                    }
                    
                    
                   
                    
                    
                    
                    //reconfigure old partner, if any
                    
                    
                       
                            
                    
                }
                
		incoming = false;
	}
        
        /*
        public synchronized void getStateChangeBuffer(){
            
            //also updates remote (distributed) partner state
            
                partner.setChannelCDState((String)stateChangetoReceive[2]);
		
		stateChangeIncoming = false;
	}
        */
	
	public void sethook(){
            
            
            if(init){
                
                if(isLocal){
                    
                    if(partner_ref.getChannelCDState()!=null){
                         if(partner_ref.getChannelCDState().equals("Killed")){
                            terminate_blocking_ochan(); // already reset the communication status
                            clean_value_buffer(); //clear the value buffer
                         } else {
                             if(terminated){
                                 terminated = false;
                             }
                         }
                    }
                    
                } 
                else {
                    
                    if(partner.getChannelCDState()!=null){
                        if(partner.getChannelCDState().equals("Killed")){
                            terminate_blocking_ochan(); // already reset the communication status
                            clean_value_buffer(); //clear the value buffer
                        } else{
                            if(terminated){
                                 terminated = false;
                            }
                        }
                            
                    }
                    
                }
                
            }
           
             //System.out.println("set hook");
		if(init && (this.modified || this.modified_chan)){
			if(isLocal){
                            //System.out.println("State : " +get_preempted());
                            //System.out.println("w_s: " +get_w_s()+"w_r: " +get_w_r());
                            //String destcd = ((String)PartnerName).substring(0, ((String)PartnerName).indexOf(".")); // CD name //o[5]
                            //String dest = IMBuffer.getInterfaceManagerConfig().getCDLocation(destcd);
                        //    if(partner.getChannelCDState().equals("Killed")){
                        //       terminate_blocking_w_s_w_r();
                       //     } 
                             //if(partner_ref.getChannelCDState()!=null && init){
                             //   if(partner_ref.getChannelCDState().equals("Killed")){
                             //       terminate_blocking_w_s_w_r();
                             //   }
                            // }
                             
                                //refresh();
                            
				
                                
                                //if (PartnerName==null){
                                   // refresh();
                                //}
                            
                            
                            
                                updateLocalCopy();
				// Maybe modified = false?
                                //System.out.println("set hook update local copy");
			}
			else{
                            
                    //search CDs in the same SS, with mapping!
                    // 1. Check if partner exist!
                            
                           // if(PartnerName!=null){
                                
                             //   String destcd = ((String)PartnerName).substring(0, ((String)PartnerName).indexOf(".")); // CD name //o[5]
                             //   String dest = IMBuffer.getInterfaceManagerConfig().getCDLocation(destcd);
                            System.out.println("OChan trying to send data via LINK");
                             //   if(dest!=null || destcd!=null){
                                            Object[] toSend = new Object[6];  // Creating an Object!!
                                            toSend[0] = PartnerName;
                                            toSend[1] = new Integer(this.get_w_s()); // Creating an Object!!
                                            toSend[2] = new Integer(this.get_w_r());
                                            toSend[3] = new Integer(this.get_preempted_val());
                                            toSend[5] = Name;
                                            //toSend[6] = getChannelCDState();
                                            if(value != null)
                                                    toSend[4] = value;
                                            if(super.pushToQueue(toSend))
                                                    this.modified = false; // This is set to false ONLY if the data is received by other side
                             
				
			}
                }
                
                if(init && this.distStateChanged && !isLocal){
                    Object[] toSend = new Object[3];  // Creating an Object!!
                                    toSend[0] = PartnerName;
                                    toSend[1] = Name;  // Creating an Object!!
                                    toSend[2] = getChannelCDState();
                                    if(super.pushToQueue(toSend))
                                            this.distStateChanged = false;
                }
                
                if(init && !isLocal && this.chanReconfigureFlag){
                    TransmitReconfigChanChanges();
                }
	}
        
        

	// SMCHAN
	// In this case, partner is a local-copy and partner_ref has real reference to the partner object
	private input_Channel partner_ref = null;
	public void setDistributed(){ isLocal = false; partner = new input_Channel(); }
        
        public void setDistributedWeakMigration(){ isLocal = false; partner = new input_Channel(); this.modified = true;}
        
       
        
        public void setDistributedStrongMigration(){
            isLocal=false;
            this.modified = true;
            // if weak migration, any rendezvous attempt needs to be terminated as well!
            
            //
            
        }
        
        public void TransmitCDLocChanges(String newSSLoc){
                    Object[] toSend = new Object[7];  // Creating an Object!!
                                    toSend[0] = PartnerName;
                                    
                                            toSend[1] = new Integer(this.get_w_s()); // Creating an Object!!
                                            toSend[2] = new Integer(this.get_w_r());
                                            toSend[3] = new Integer(this.get_preempted_val());
                                    toSend[5] = Name;
                                    //toSend[6] = getChannelCDState();
                                    if(value != null)
                                            toSend[4] = value;
                                    toSend[6] = newSSLoc;
                                    
                                    super.pushToQueue(toSend);
                                            
        }
        
        public void TransmitReconfigChanChanges(){
                    Object[] toSend = new Object[2];  // Creating an Object!!
                                    toSend[0] = PartnerName;
                                    
                                    toSend[1] = Name;
                                    //toSend[6] = getChannelCDState();
                                    
                                    if(super.pushToQueue(toSend))
                                        this.chanReconfigureFlag = false;
                                           
                                            
        }
        
         public void TransmitPartnerReconfigChanChanges(String NewPartner){
                    Object[] toSend = new Object[4];  // Creating an Object!!
                    toSend[0] = PartnerName;
                                    
                    toSend[1] = Name;
                                    //toSend[6] = getChannelCDState();
                           
                    toSend[2] = "ReconfigChan";
                    toSend[3] = NewPartner;
                    
                    if (super.pushToQueue(toSend))
                        this.chanReconfigureFlag = false;
                                           
        }
        
        public void setLocal(){isLocal=true;}
	// My data-structure copies to be read by partner
	private int w_s_copy = 0;
	private int w_r_copy = 0;
	private int preempted_copy = 0;
        private String CDState_copy;
	public void set_partner_smp(input_Channel partner){this.partner_ref = partner; this.partner = new input_Channel();}
	
        public void set_partner_smp_migration(input_Channel partner){this.partner_ref=partner;this.modified=true;}
        
        //public void clear_partner_smp(){this.partner_ref=null;this.partner=null;}
    
        
        public input_Channel get_partner_smp(){return this.partner_ref;};
        
        protected synchronized void updateLocalPartner(output_Channel p){
		// This copying operation is regarded as an atomic operation
		p.w_s = this.w_s_copy;
		p.w_r = this.w_r_copy;
		p.preempted = this.preempted_copy;
                p.CDState = this.CDState_copy;
                
		if(value!=null){
                    //if(this.CDState_copy.equalsIgnoreCase("Killed")){
                   //     p.value = null;
                   // } else {
			p.value = this.value;
                   // }
                }
	}
	protected synchronized void updateLocalCopy(){
		// This copying operation is regarded as an atomic operation
		this.w_s_copy = get_w_s();
		this.w_r_copy = get_w_r();
		this.preempted_copy = get_preempted_val();
                this.CDState_copy = getChannelCDState();
                this.modified_chan = false;
	}
        
        /*
        public String getChannelCDNameLocation(){
            return this.CDNameLoc;
        }
        
        public void setChannelCDNameLocation(String cdname){
            this.CDNameLoc = cdname;
        }
        
        public String getPartnerCDName(){
            return partner.getChannelCDNameLocation();
        }
        */
        
        public String getChannelCDState(){
            return this.CDState;
        }
        
        public void setChannelCDState(String cdstate){
            this.CDState = cdstate;
        }
        
        public boolean IsOutputChannelTerminated(){
            return terminated;
        }
         
        public boolean IsChannelLocal(){
            return isLocal;
        }
        
}
