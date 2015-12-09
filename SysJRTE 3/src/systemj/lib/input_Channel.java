package systemj.lib;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Hashtable;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import systemj.bootstrap.ClockDomain;
import systemj.common.CDLCBuffer;
import systemj.common.IMBuffer;
import systemj.common.Interconnection;
import systemj.common.InterfaceManager;
import systemj.common.SJSSCDSignalChannelMap;
import systemj.common.SJServiceRegistry;

import systemj.interfaces.*;

public class input_Channel extends GenericChannel implements Serializable{
    
        //private String CDNameLoc;
        private String CDState="Created";
        private boolean terminated = false;
        
	private int preempted = 0;
	private int r_r = 0;
	private int r_s = 0;
	public output_Channel partner;
	public input_Channel(){}
	public void set_partner(output_Channel partner){this.partner = partner;}
	public Object get_value(){
		Object ret = this.value;
                this.value = null;
                return ret;
	}
	/**
	 * This method now just equalize r_r with r_s, not using 'int in' at all.. (which is ++ in the code)
	 * @param in
	 */
	public void set_r_r(int in){this.r_r  = /*in*/ this.r_s; this.modified = true; }
	public void get_val(){
            
		if(init)
			this.value = partner.get_value();
	}
	public int get_r_r(){
            //System.out.println("r_r val: " +this.r_r); 
            return this.r_r; 
        }
	public int get_r_s(){
            //System.out.println("r_s val: " +this.r_s); 
            return this.r_s; 
        }
        
        public void setDistStateChanged(boolean stat){
            this.distStateChanged=stat;
        }
        
        public boolean IsInputChannelTerminated(){
            return terminated;
        }
        
        public void terminate_blocking_local_inchan(){
            
           // set_preempted();
            
                     if(get_r_s()<=get_r_r()){
                
                        //make sure that the r_r value is reset, not more than 0 for initial state, ensuring this value persists unti the CD read the channel status
                        if(get_r_r()>0){
                            //this.r_r=0;
                            set_r_r(0);
                        }

                        set_r_s(this.r_r++);

                        
                        if(!terminated){
                            terminated = true;
                        }
                        //this.r_s = this.r_r;
                        //this.r_s++;

                    }
                 
        }
        
        /*
        public void migration_forced_terminate_blocking(){
            if(partner_ref.getChannelCDState()!=null){
                if(partner_ref.getChannelCDState().equals("Killed")){
                    terminate_blocking_r_r_r_s();
                }
            }
        }
        */
        
        
        
        public void resetLocalPartnerStateAfterMigration(){
            
            //this it will be updated remotely by the partner via link
            
            ///System.out.println("inchan resetting partner! resetLocalPartnerAfterMigration");
            
            partner.set_w_s(0);
            partner.set_w_r(0);
            partner.set_value(null);
        }
        
	public void set_r_s(int in){this.r_s = in; this.modified = true;}
        
	private int get_w_s(){
		return init ? partner.get_w_s() : 0; 
	}
	public int get_preempted_val(){return this.preempted; }
	public void set_preempted() {++this.preempted; ; this.modified = true;}
        
        
	public void update_r_s(){ 
            
		if(init){
                    
			if(partner.get_preempted_val() == this.preempted){ //still considering local only
                            
                            if(isLocal){
                                
                                if(partner_ref.getChannelCDState()!=null){
                                
                                    if(!partner_ref.getChannelCDState().equals("Killed")){
                                        this.r_s = get_w_s(); //if partner is killed, the r_s is not updated
                                    } 

                                } else {
                                    this.r_s = get_w_s();
                                }
                                
                                //System.out.println("Inchan, partner w_s" +get_w_s()+" and r_s value: " +get_r_s()+ " and r_r value:" +get_r_r());
                                
                            } else {
                                
                                if(partner.getChannelCDState()!=null){
                                
                                    if(!partner.getChannelCDState().equalsIgnoreCase("Killed")){
                                        this.r_s = get_w_s(); //if partner is killed, the r_s is not updated
                                    } 

                                } else {
                                    this.r_s = get_w_s();
                                }
                                
                            }
                            
                            
                        }
		}
	}
	/**
	 * Tests whether partner output channel is preempted or re-initialized
	 * @return <b>true</b> - when partner is preempted or re-initialized <br> <b>false</b> - otherwise
	 */
	public boolean get_preempted() {
		// Now input channel is preempted when the output channel is re-initialized (i.e. r_s < r_r)
                
                //need to check if partner is not terminated, if terminated, return true;
            
                //System.out.println("State : " +partner.getChannelCDState());
            
                if(init){
                        //System.out.println("State : " +partner.getChannelCDState());
			if(partner.get_preempted_val() > this.preempted || this.r_s < this.r_r)
				return true; 
		}
		return false;
	}
	
	public int refresh(){
            
		this.value = null;    this.r_r = 0;
		this.r_s = 0;
		set_preempted();
		this.modified = true;
                
		return 1;
	}
        
        
	
	// Modular
	public void set_preempted(int num){this.preempted=num; this.modified = true;}
	public void gethook(){
           
		if(init){
			if(isLocal){
                            
				partner_ref.updateLocalPartner(this.partner);
                        
                              //  if(partner.getChannelCDState()!=null){ //if still uninitialized
                                
                                //    if(partner.getChannelCDState().equals("Killed")){
                                    
                                //        terminate_blocking_r_r_r_s();
                                        //this.modified=true;
                                 //   }
                                
                                //}
                                
                                //System.out.println("gethook inchan first, r_s val:" +get_r_s()+", r_r val: "+get_r_r()+", partner state: " +partner.getChannelCDState() );

                                
                                //System.out.println("gethook inchan after, r_s val:" +get_r_s()+", r_r val: "+get_r_r()+", partner state: " +partner.getChannelCDState() );
                        }
			else {
                            if(incoming){
                                
				this.getBuffer();
				// Little trick to make sure that partner channel knows preemption status of this channel
				if(partner.get_preempted_val() < this.preempted) 
					modified = true;
                            }
                            
                            //if(stateChangeIncoming){
                           //     this.getStateChangeBuffer();
                           // }
                        }
                            
		}
	}
	
	public synchronized void getBuffer(){
		// Otherwise just store them into the partner copy.
            
            if(toReceive.length==6){
            
                System.out.println("InChan buffer received, partner w_s: " +((Integer)toReceive[1]).intValue()+ "partner w_r: " +((Integer)toReceive[2]).intValue()+" value : " +toReceive[4]);
                
		partner.set_w_s(((Integer)toReceive[1]).intValue());
		partner.set_w_r(((Integer)toReceive[2]).intValue());
		partner.set_preempted(((Integer)toReceive[3]).intValue());
		if(toReceive[4] != null)
			partner.set_value(toReceive[4]);
                
            } else if(toReceive.length==7){
                    
                    String partnerName = (String)toReceive[5];
                    
                    String partnerCDName = partnerName.split("\\.")[0];
                    
                    //InterfaceManager im = super.getInterfaceManager();
                    
                    InterfaceManager im = IMBuffer.getInterfaceManagerConfig();
                    
                    im.addCDLocation((String)toReceive[6], partnerCDName);
                    
                    System.out.println("InChan buffer received, partner w_s: " +((Integer)toReceive[1]).intValue()+ "partner w_r: " +((Integer)toReceive[2]).intValue()+" SSLoc : " +toReceive[6]+ " partnerCDName: " +partnerCDName);
                    
                    super.setInterfaceManager(im);
                    
                    IMBuffer.SaveInterfaceManagerConfig(im);
                    
                    InterfaceManager imTest = super.getInterfaceManager();
                    
                    System.out.println("InChan, Updated IM CD location: "+imTest.getAllCDLocation());
                    
                } else if(toReceive.length==3){
                    partner.setChannelCDState((String)stateChangetoReceive[2]);
                    
                    String CDPartnerState = (String)stateChangetoReceive[2];
                    
                    if(CDPartnerState.equalsIgnoreCase("Killed")){
                        
                        InterfaceManager IM = IMBuffer.getInterfaceManagerConfig();
                        
                        //Interconnection IC = IM.getInterconnection();
                        
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
                        
                        SJSSCDSignalChannelMap.removeChanPartner(SSLoc, CDName, "input", ChanName);
                        
                        //IM.setInterconnection(IC);
                        //
                        //IMBuffer.SaveInterfaceManagerConfig(IM);
                        
                        //super.setInterfaceManager(IM);
                        
                        SJSSCDSignalChannelMap.SetCheckLinkToRemove();
                    }
                    
                } else if(toReceive.length==4) {
                    
                    if(toReceive[2].toString().equalsIgnoreCase("ReconfigChan")){
                        
                        String changeInPartner = (String)toReceive[3];
                    
                        String oldPartnerChan = PartnerName;
                        
                        if(!PartnerName.equalsIgnoreCase(".")){
                            
                            String OldCDPartName = oldPartnerChan.split("\\.")[0];
                            String OldChanPartName = oldPartnerChan.split("\\.")[1].split("_")[0];
                        
                            CDLCBuffer.AddOldPartnerChanReconfig("output", OldCDPartName, OldChanPartName);
                            
                        }
                        
                         if(!changeInPartner.equalsIgnoreCase(".")){
                            
                            String NewCDPartName = changeInPartner.split("\\.")[0];
                            String NewChanPartName = changeInPartner.split("\\.")[1].split("_")[0];
                            
                            PartnerName = NewCDPartName+"."+NewChanPartName+"_o";
                            
                        } else {
                            PartnerName = ".";
                        }
                          
                    
                    //reconfigure old partner's partner, if any
                    
                        
                        
                    } else if(toReceive[2].toString().equalsIgnoreCase("ReconfigPartnerChan")){
                        
                        String changeInPartnersPartner = (String)toReceive[3];
                    
                        
                        if(!changeInPartnersPartner.equalsIgnoreCase(".")){
                            
                            String NewCDPartName = changeInPartnersPartner.split("\\.")[0];
                            String NewChanPartName = changeInPartnersPartner.split("\\.")[1].split("_")[0];
                    
                            
                            PartnerName = NewCDPartName+"."+NewChanPartName+"_o";
                            
                            
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
        
        //public synchronized void getStateChangeBuffer(){
		// Otherwise just store them into the partner copy.
		
	//}
        
	
	/**
	 * As we do not want to use link traffics all the time, chan status is only transferred only if it is modified during the last
	 * tick
	 */
	public void sethook(){
      
            //System.out.println("gethook inchan first, r_s val:" +get_r_s()+", r_r val: "+get_r_r()+", partner state: " +partner.getChannelCDState() );
            
            //if(init){
                //System.out.println("sethook inchan after, r_s val:" +get_r_s()+", r_r val: "+get_r_r()+", partner state: " +partner.getChannelCDState() );
                
            //}
            if(init){
                
                if(isLocal){
                    
                   if(partner_ref.getChannelCDState()!=null){
                
                        if(partner_ref.getChannelCDState().equals("Killed")){

                            //System.out.println("sethook inchan after, r_s val:" +get_r_s()+", r_r val: "+get_r_r()+", partner state: " +partner_ref.getChannelCDState() );
                           
                              terminate_blocking_local_inchan();
                            /*
                             if(get_r_s()<=get_r_r()){
                                 
                                //make sure that the r_r value is reset, not more than 0 for initial state, ensuring this value persists unti the CD read the channel status
                                if(get_r_r()>0){
                                    //this.r_r=0;
                                    set_r_r(0);
                                    
                                }
                                
                                int st = get_r_r()+1;

                                set_r_s(st);

                                //this.r_s = this.r_r;
                                //this.r_s++;

                            }
                             */
                             
                            //System.out.println("sethook inchan after, r_s val:" +get_r_s()+", r_r val: "+get_r_r()+", partner state: " +partner_ref.getChannelCDState() );


                        } else {
                            if(terminated){
                                terminated = false;
                            }
                        }
                    }
                    
                } else {
                    
                    if(partner.getChannelCDState()!=null){
                        
                        if(partner.getChannelCDState().equalsIgnoreCase("Killed")){
                            terminate_blocking_local_inchan();
                        } else {
                            if(terminated){
                                terminated = false;
                            }
                        }
                        
                    }
                    
                }
                
            }
            
                
                
            
		if(init && (this.modified || this.modified_chan)){
                    
			if(isLocal){
                            
				updateLocalCopy();
                                
				// Maybe modified = false?
                                //System.out.println("gethook inchan after, r_s val:" +get_r_s()+", r_r val: "+get_r_r()+", partner state: " +partner.getChannelCDState() );
			}
			else{
                        
                           
                        //search CDs in the same SS, with mapping!
                        // 1. Check if partner exist!
                        //System.out.println("InChan, sending back status");
                            //if partner not exist or state is not active, 
                           
                         //   if(PartnerName!=null){ //partner state information is not available locally
                                
                                //String destcd = ((String)PartnerName).substring(0, ((String)PartnerName).indexOf(".")); // CD name //o[5]
                                //String dest = IMBuffer.getInterfaceManagerConfig().getCDLocation(destcd);
                            
                              //  if(destcd!=null || dest!=null){
                                
                                    Object[] toSend = new Object[6];  // Creating an Object!!
                                    toSend[0] = PartnerName;
                                    toSend[1] = new Integer(this.get_r_s());  // Creating an Object!!
                                    toSend[2] = new Integer(this.get_r_r());
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
                
	}
	
	// SMCHAN
	// In this case, partner is a local-copy and partner_ref has real reference to the partner object
	private output_Channel partner_ref = null;
	public void setDistributed(){ isLocal = false; partner = new output_Channel();}
        
        public void setDistributedWeakMigration(){ isLocal = false; partner = new output_Channel(); this.modified=true;}
        
        public void setDistributedStrongMigration(){
            isLocal=false;
            
            //trick to reverse status back, that it should get an update from partner remotely, otherwise it will be in the same state as if the partner is local and has sent the data
            if(get_r_s()>get_r_r()){
                set_r_s(get_r_r());
            }
            
            this.modified = true;
        }
        
        public void TransmitCDLocChanges(String newSSLoc){
                    Object[] toSend = new Object[7];  // Creating an Object!!
                                    toSend[0] = PartnerName;
                                    toSend[1] = new Integer(this.get_r_s());  // Creating an Object!!
                                    toSend[2] = new Integer(this.get_r_r());
                                    toSend[3] = new Integer(this.get_preempted_val());
                                    toSend[5] = Name;
                                    //toSend[6] = getChannelCDState();
                                    if(value != null)
                                            toSend[4] = value;
                                    toSend[6] = newSSLoc;
                                    
                                    super.pushToQueue(toSend);
                                           
                                            
        }
        
        public void TransmitReconfigChanChanges(String NewPartner){
                    Object[] toSend = new Object[4];  // Creating an Object!!
                    toSend[0] = PartnerName;
                                    
                    toSend[1] = Name;
                    toSend[2] = "ReconfigChan";
                    toSend[3] = NewPartner;
                                    //toSend[6] = getChannelCDState();
                                    
                    if (super.pushToQueue(toSend))
                        this.chanReconfigureFlag = false;
                                           
        }
        
        public void TransmitPartnerReconfigChanChanges(String NewPartner){
                    Object[] toSend = new Object[4];  // Creating an Object!!
                    toSend[0] = PartnerName;
                                    
                    toSend[1] = Name;
                                    //toSend[6] = getChannelCDState();
                           
                    toSend[2] = "ReconfigPartnerChan";
                    toSend[3] = NewPartner;
                    
                    if (super.pushToQueue(toSend))
                        this.chanReconfigureFlag = false;
                                           
        }
        
        public void setLocal(){isLocal=true;}
	// My data-structure copies to be read by partner
	private int r_r_copy = 0;
	private int r_s_copy = 0;
	private int preempted_copy = 0;
        private String CDState_copy="";
	public void set_partner_smp(output_Channel partner){this.partner_ref = partner; this.partner = new output_Channel();}
        
        public void set_partner_smp_migration(output_Channel partner){this.partner_ref = partner;this.modified_chan=true;}
        
        //public void clear_partner_smp(){this.partner_ref = null;this.partner=null;}
        
        public output_Channel get_partner_smp(){return this.partner_ref;}
        
        
        
	protected synchronized void updateLocalPartner(input_Channel p){
		// This copying operation is regarded as an atomic operation
		p.r_s = this.r_s_copy;
		p.r_r = this.r_r_copy;
		p.preempted = this.preempted_copy;
                p.CDState = this.CDState_copy;
		if(value!=null)
			p.value = this.value;
	}
	protected synchronized void updateLocalCopy(){
		// This copying operation is regarded as an atomic operation
		this.r_s_copy = get_r_s();
		this.r_r_copy = get_r_r();
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
        
        public boolean IsChannelLocal(){
            return isLocal;
        }
        
        
}
