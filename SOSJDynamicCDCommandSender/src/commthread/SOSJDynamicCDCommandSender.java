package commthread;


import SOAHandler.NoP2PLocalServMessageReceiverThread;
import SOAHandler.NoP2PRemoteServMessageReceiverThread;
import SOAHandler.NoP2PServRegExpiryCheckerThread;
import commthread.CDLCMapParser;
import commthread.CommandSender;
import commthread.MessageGenerator;
import commthread.ServDescParser;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Atmojo
 */
public class SOSJDynamicCDCommandSender {

    /**
     * @param args the command line arguments
     */
    
    private static String XMLSigChanFilename;
    private static String XMLServDescFilename;
    private static String SSName;
    private static String Address;
    private static String DevelAddress;
    private static String CDName;
    private static String ChanName;
    private static String ChanDir;
    private static String PartnerChanCDName;
    private static String PartnerChanName;
    private static String PartnerChanSSName;
    
    private static String MigSSDest;
    private static String MigType;
    
    
    public static void main(String[] args) {
        
        parseOptionAndData(args);
        
        // TODO code application logic here
        
        
    }
    
    private static void printUsage(){
                System.out.println("SOSJ (SOA + SystemJ) Dynamic CD Command Sender by Udayanto Dwi Atmojo, Dept of Electrical and Computer Eng, The University of Auckland, New Zealand");
		System.out.println("Usage: 1st arg - Address of the SS for the command to be sent");
                System.out.println("2nd arg - Subsystem name for the command to be sent");
                System.out.println("3st arg - Command for dynamic behavior of CD. Can be of 'CreateCD','KillCD','SuspendCD','WakeUpCD', and 'MigrateCD'");
		System.out.println("'CreateCD' is followed by three more arguments. First is the name of the CD, the second is the name of the xml file that describes the signal and channel mapping, the third one is the name of the xml file that provides the service description of the CD ");
                System.out.println("'KillCD' is followed by one argument, which is the name of the CD to be killed");
                System.out.println("'SuspendCD' is followed by one argument, which is the name of the CD to be suspended");
                System.out.println("'WakeUpCD' is followed by one argument, which is the name of the CD to be resumed of execution");
                System.out.println("'MigrateCD' is followed by five more arguments. First is the name of the CD, the second is the name of the xml file that describes the signal and channel mapping, the third one is the name of the xml file that provides the service description of the CD, fourth is the name of the destination subsystem of migration, and the fifth is the migration type, can be either 'strong' or 'weak'");
                System.out.println("'ReconfigChan'");
		//System.out.println("Options:");
		//System.out.println("\t-version\tprint version");
		//System.out.println("\t-x\t\tgenerate bootstrap file");
		//System.out.println("\t-xsp\t\tgenerate bootstrap file for sunspot");
    }
    
    private static JSONObject parseOptionAndData(String[] args){
        
        JSONObject jsMsg = new JSONObject();
        
        MessageGenerator msggen = new MessageGenerator();
        
        System.out.println("Total Args : " +args.length);
        
		if(args.length == 0){
			printUsage();
			System.exit(1);
		}
                
                //for(int i=0;i<args.length;i++){
                        
                        if(args[0].equals("CreateCD")){
                            Address = args[3];
                        DevelAddress = args[2];
                        SSName = args[1];
				CDName = args[4];
                                XMLSigChanFilename = args[5];
                                XMLServDescFilename = args[6];
                                
                                JSONObject jsCDMap = ParseCDMap(CDName, XMLSigChanFilename);
                                JSONObject jsCDServDesc = ParseServDesc(XMLServDescFilename);
                               
                                jsMsg = msggen.GenerateMessageOfJSON(args[0], DevelAddress,SSName,CDName, jsCDMap, jsCDServDesc);
                                
                                CommandSender commsend = new CommandSender(Address, jsMsg);
                                commsend.run();
                                
			} else if(args[0].equals("SuspendCD")){
                            Address = args[3];
                        DevelAddress = args[2];
                        SSName = args[1];
				CDName = args[4];
                                jsMsg = msggen.GenerateMessageOfJSON(args[0], DevelAddress,SSName, CDName);
                                CommandSender commsend = new CommandSender(Address, jsMsg);
                                commsend.run();
			} else if(args[0].equals("WakeUpCD")){
                            Address = args[3];
                        DevelAddress = args[2];
                        SSName = args[1];
				CDName = args[4];
                                jsMsg = msggen.GenerateMessageOfJSON(args[0], DevelAddress,SSName, CDName);
                                 CommandSender commsend = new CommandSender(Address, jsMsg);
                                commsend.run();
			} else if(args[0].equals("KillCD")){
                            Address = args[3];
                        DevelAddress = args[2];
                        SSName = args[1];
				CDName = args[4];
                                jsMsg = msggen.GenerateMessageOfJSON(args[0], DevelAddress,SSName,CDName);
                                 CommandSender commsend = new CommandSender(Address, jsMsg);
                                commsend.run();
			} else if(args[0].equals("MigrateCD")){
                            Address = args[3];
                        DevelAddress = args[2];
                        SSName = args[1];
				CDName = args[4];
                                XMLSigChanFilename = args[5];
                                XMLServDescFilename = args[6];
                                MigSSDest = args[7];
                                MigType = args[8];
                                
                                JSONObject jsCDMap = ParseCDMap(CDName, XMLSigChanFilename);
                                JSONObject jsCDServDesc = ParseServDesc(XMLServDescFilename);
                                
                                jsMsg = msggen.GenerateMessageOfJSON(args[0], DevelAddress,SSName, CDName,MigSSDest, MigType, jsCDMap, jsCDServDesc);
                                 CommandSender commsend = new CommandSender(Address, jsMsg);
                                commsend.run();
                                
			} else if(args[0].equals("ReconfigChan")){
                            Address = args[3];
                        DevelAddress = args[2];
                        SSName = args[1];
                            
                            if(args.length==10){
                                CDName = args[4];
                                ChanName = args[5];
                                ChanDir = args[6];
                                PartnerChanSSName = args[7];
                                PartnerChanCDName = args[8];
                                PartnerChanName = args[9];
                                
                                jsMsg = msggen.GenerateReconfigChanMessageOfJSON(args[0], DevelAddress,SSName, CDName, ChanName, ChanDir,PartnerChanSSName, PartnerChanCDName, PartnerChanName);
                                 CommandSender commsend = new CommandSender(Address, jsMsg);
                                commsend.run();
                            
                            } else{
                                CDName = args[4];
                                ChanName = args[5];
                                ChanDir = args[6];
                                PartnerChanCDName = args[7];
                                PartnerChanName = args[8];
                                jsMsg = msggen.GenerateReconfigChanMessageOfJSON(args[0], DevelAddress,SSName, CDName, ChanName, ChanDir, PartnerChanCDName, PartnerChanName);
                                CommandSender commsend = new CommandSender(Address, jsMsg);
                                commsend.run();
                            }
                            
                        } else if (args[0].equalsIgnoreCase("-gui")){
                            DevelAddress = args[1];
                            Thread LocRegMsgRec = new Thread(new NoP2PLocalServMessageReceiverThread());
                            Thread RemRegMsgRec = new Thread(new NoP2PRemoteServMessageReceiverThread());
                            Thread RegExpTh = new Thread(new NoP2PServRegExpiryCheckerThread());
                            LocRegMsgRec.start();
                            RemRegMsgRec.start();
                            RegExpTh.start();
                            JFrame sosjguif = new SOSJGUI(DevelAddress);
                            sosjguif.setSize(825,700);
                            
                            sosjguif.setVisible(true);
                            System.out.println("Starting GUI");
                            
                        }
                            else {
                            System.out.println("Incorrect command for dynamic behavior given, exiting program");
                            System.exit(1);
                        }
                        
                //}
                
                return jsMsg;
                
    }
    
    public static JSONObject ParseCDMap(String filename){
        
        CDLCMapParser cdpars = new CDLCMapParser();
        
        JSONObject js = new JSONObject();
        try {
            js = cdpars.parse(filename);
            
            System.out.println("Parsed New CDMap: " +js.toPrettyPrintedString(2, 0));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return js;
    }
    
    public static JSONObject ParseCDMap(String CDName, String filename){
        
        CDLCMapParser cdpars = new CDLCMapParser();
        
        JSONObject js = new JSONObject();
        try {
            js = cdpars.parse(CDName,filename);
            
            System.out.println("Parsed New CDMap: " +js.toPrettyPrintedString(2, 0));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return js;
    }
    
    public static JSONObject ParseServDesc(String filename){
        
        ServDescParser cdsdparse = new ServDescParser();
        
        JSONObject js = new JSONObject();
        try {
            js = cdsdparse.parse(filename);
            
            System.out.println("Parsed New SD: " +js.toPrettyPrintedString(2, 0));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return js;
        
    }
    
}
