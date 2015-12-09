/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commthread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author Atmojo
 */
public class CodeSenderThread {

    JSONObject jsCDMap,jsCDServDesc;
    String Addr;
    
    
    public CodeSenderThread(String Addr, JSONObject jsCDMap, JSONObject jsCDServDesc){
        this.jsCDMap = jsCDMap;
        this.Addr = Addr;
        this.jsCDServDesc = jsCDServDesc;
    }
    
    
    
    public void run() {
        InitTransferProcess(Addr);
    }
    
    private boolean InitTransferProcess(String addr){
        
        boolean TransferStat = false;
        
         ObjectOutputStream sOut;
        Socket socketSend;
        
        ServerSocket ss = null;
        
        /*
        if (ss==null || !ss.isBound()){
                try {
                    ss = new ServerSocket(8887,50,InetAddress.getByName(SJServiceRegistry.getLocalSSAddr()));//new ServerSocket(8888, 50, getLocalHostLANAddress());
                    System.out.println("Migration ServSocket created");
                } catch (BindException bex) {
                    System.out.println("Port is already bound");
                    bex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        */
        
        try{
            
            //String destAddr = (String) hash.get("destinationAddress");
            //String destSS = (String) hash.get("destinationSubsystem");
            //String migType = (String) hash.get("migrationType");
            //String OS = (String) hash.get("OS");
            
            socketSend = new Socket(InetAddress.getByName(addr),9999);
            System.out.println("Connected to host " + socketSend.getInetAddress());
                        
            
            
            sOut = new ObjectOutputStream(socketSend.getOutputStream());
            
            
            
          //  Hashtable fileList = new Hashtable();
            
            //for (int j=0;j<vecAllCD.size();j++){
                
            //    String cdn = vecAllCD.get(j).toString();
            //    
            //    fileList.put(Integer.toString(j),cdn);
            //}
            
            //Hashtable fileList = (Hashtable) hash.get("CDName");
            
             //Enumeration keysHash = fileList.keys();
             
             //int file_amount = fileList.size();
             
            //int file_amount = vecAllCDName.size();
            
             // 1. start message
             sOut.writeObject("START");
             sOut.flush();
             
            // sOut.writeObject(SJServiceRegistry.getLocalSSAddr());
            // sOut.flush();
             
             // send the origin SS name
             
             
             // 2. send total file name to sent
             
        
                   
                  
                   //String keyCDName = keysHash.nextElement().toString();
            
                        JSONObject jsCurrSigChanMapping = jsCDMap;
            
                        Enumeration keysJSCDNames = jsCurrSigChanMapping.keys();
                   
                        while (keysJSCDNames.hasMoreElements()){
                       
                            String CDName = keysJSCDNames.nextElement().toString();
                       
                            //if (SJSSCDSignalChannelMap.IsSSNameLocal(SSName)){
                           
                                JSONObject jsIndivCD = jsCurrSigChanMapping.getJSONObject(CDName);
                           
                                //Enumeration keysJSAllCDs = jsAllCDs.keys();
                           
                                //while (keysJSAllCDs.hasMoreElements()){
                                    //String CDName = keysJSAllCDs.nextElement().toString();
                                    
                                    //JSONObject IndivMap = jsAllCDs.getJSONObject(CDName);
                               
                                  //  if(CDName.equals(vecAllCDName.get(l).toString())){
                                        String fileName =  jsIndivCD.getString("CDClassName")+".class";
                                        
                                        //String fileName = (String)fileList.get(key);
            
                                        String filePath = System.getProperty("user.dir");
                                        filePath = filePath.replace("\\", "/");
                                         String fullFilePath;

                                        File testFile;

                                            fullFilePath = filePath+"/"+fileName;
                                            testFile = new File(fullFilePath);


                                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(testFile));

                                             String[] str = fullFilePath.split("/");

                                            // if (ss==null || !ss.isBound()){
                                            //     ss = new ServerSocket(8887,50,InetAddress.getByName(Addr));//new ServerSocket(8888, 50, getLocalHostLANAddress());
                                            //     System.out.println("Migration ServSocket created");
                                            // }
                                             
                                             
                                             // 3. send file name
                                             
                                            //sOut.writeObject(str[str.length-1]);
                                            sOut.writeObject(fileName);
                                            sOut.flush();
                                            
                                            //Socket socketReceive = ss.accept();
                                            
                                            //ObjectInputStream sInt = new ObjectInputStream(socketReceive.getInputStream());
                                            
                                            //boolean IsTransfer = sInt.readBoolean();
                                            
                                            //ss.close();
                                            //socketReceive.close();
                                            
                                            //if(IsTransfer){
                                                
                                                System.out.println("Preparing file \"" + str[str.length-1] + "\" to be sent");

                                                int file_len = (int) testFile.length();
                                                int buff_size = 50000;
                                                int bytesRead = 0;
                                                int total_read_len = 0;
                                                //byte[] buffer = new byte[buff_size];
                                                byte[] buffer = new byte[file_len];
                                                int file_len_2 = file_len;

                                                //tell server to know size of the file
                                                sOut.writeInt(file_len);
                                                sOut.flush();

                                                System.out.println("file size: " +file_len);

                                                boolean sending=true;

                                    //This one copy the file in exact size
                                    //begin read and send chunks of file in loop
                                                //while( file_len_2 > 0 ){
                                                while(sending){
                                                    //if( file_len_2 < buff_size ){
                                                    //    buffer = new byte[file_len_2];
                                                    //    bytesRead = bis.read(buffer);
                                                    //}else{
                                                        bytesRead = bis.read(buffer);
                                                    //}
                                                    bis.close();

                                                    file_len_2 -= bytesRead;
                                                    total_read_len += bytesRead;
                                                    sOut.writeBoolean(true);
                                                    sOut.flush();
                                                    sOut.writeObject(buffer);
                                                    sOut.flush();
                                                    System.out.println("Sent: " + (float)total_read_len/file_len*100 + "%");

                                                    if((float)total_read_len/file_len*100>=100){
                                                        sending=false;
                                                    }

                                                }

                                                    sOut.writeBoolean(false);
                                                    sOut.flush();

                                    //This one copy a little bit bigger
                                    /*while( (bytesRead = bis.read(buffer)) != -1 ){
                                        total_read_len += bytesRead;
                                        sOut.writeBoolean(true);
                                        sOut.writeObject(buffer);
                                        System.out.println("Sent: " + (float)total_read_len/file_len*100 + "%");
                                    }*/
                                    /*
                                    sOut.writeBoolean(false);
                                    sOut.flush();
                                    */
                                                System.out.println("Done sending file!");
                                                
                                                String fileRootDir = System.getProperty("user.dir");
                                                        fileRootDir = fileRootDir.replace("\\", "/");
                                                        //String fileDir = fileRootDir+"/"+sclazz+".java";
                                                        Path path = FileSystems.getDefault().getPath(fileRootDir,jsIndivCD.getString("CDClassName")+".class");
                                                        
                                                        //Files.deleteIfExists(path);
                                                
                                                
                                            //} 
                                            
                                            
                                            
                                            
                                            
                                            //
                                            
                                            //modify scheduler
                                           
                                            //
                                            
                                            
                                            
                                            
                                   // }
                                    
                                //}
                            //}
                        }
                   
               
               
               
           
             
             //transfer CD objects from the sc directly, which have got modified
             
             
             
             //transfer signal channel mapping
             
             //JSONObject jsCurrSigChanMap = SJSSCDSignalChannelMap.getCurrentSignalChannelMapping();
             
             JSONObject transferredMap = new JSONObject();
             
             //Enumeration keysList = fileList.keys();
             
            // while(keysList.hasMoreElements()){
                 
                 //String SSName = keysList.nextElement().toString();
              
                 //sOut.writeInt(vecAllCDName.size());
                 //sOut.flush();
                 
                 JSONObject jsSSCDs = new JSONObject();
                 
              //   for (int j=1;j<=fileList.size();j++){
                     
                     //Enumeration keysJsCurrSigChanMap = jsCurrSigChanMap.keys();
                     
                    // while (keysJsCurrSigChanMap.hasMoreElements()){
                         //String SSName = keysJsCurrSigChanMap.nextElement().toString();
                         
                         JSONObject allCDsCurrMap = jsCDMap;
                     
                    // sOut.writeObject(SSName);
                     //sOut.flush();
                     
                        Enumeration keysAllCDsCurrMap = allCDsCurrMap.keys();
                     
                        JSONObject jsNewCDList = new JSONObject();
                        
                        while(keysAllCDsCurrMap.hasMoreElements()){
                         
                            String CDName = keysAllCDsCurrMap.nextElement().toString();
                            
                            //for (int j=0;j<vecAllCDName.size();j++){
                                
                                //if(CDName.equals(vecAllCDName.get(j).toString())){
                                    transferredMap.put(CDName, allCDsCurrMap.getJSONObject(CDName));
                                    //im.addCDLocation(CDName, destSS);
                                    //sOut.writeObject(transferredMap.toString());
                                    //sOut.flush();
                                    
                                //need to remove mapping and service description here for each CD? Yes! - 29 Jan 2015
                           
                         //   JSONObject jsEdited = jsCurrSigChanMap;
                        //    JSONObject allCDOneSS = jsEdited.getJSONObject(SSName);
                        //    allCDOneSS.remove(CDName);
                        //    jsEdited.put(SSName, allCDOneSS);
                        //    SJSSCDSignalChannelMap.UpdateAllCurrSignalChannelMapping(jsEdited);
                            
                                
                                //SJSSCDSignalChannelMap.RemoveOneCDCurrSigChannelMapping(CDName, SSName);
                                //} 
                                //else {
                                
                               //     jsNewCDList.put(CDName, allCDsCurrMap.getJSONObject(CDName));
                                
                              //  }
                                
                            //}
                            
                           
                            
                            
                        }
                        
                        //for (int j=0;j<vecAllCDName.size();j++){
                                
                               
                           // String CDName = vecAllCDName.get(j).toString();
                            
                                    
                                       
                                    /*
                                    JSONObject SChanOut = SChanMap.getJSONObject("outputs");
                                    
                                    if(!SChanOut.isEmpty()){
                                        
                                        Enumeration keysSChanOut = SChanOut.keys();
                                        
                                        while(keysSChanOut.hasMoreElements()){
                                            
                                            String OutChanName = keysSChanOut.nextElement().toString();
                                            
                                            JSONObject OutChanDet = SChanOut.getJSONObject(OutChanName);
                                            
                                            String CDNameDestTo = OutChanDet.getString("To");
                                            
                                            String[] pnames = CDNameDestTo.split("\\.");
                                            
                                            String SSLoc;
                                            
                                            if(im.hasCDLocation(pnames[0])){
                                                SSLoc = im.getCDLocation(pnames[0]);
                                            } else {
                                                SSLoc = SJServiceRegistry.GetCDRemoteSSLocation(pnames[0]);
                                            }
                                            
                                            //String SSLoc = im.getCDLocation(pnames[0]);
                                            
                                            if(!SSLoc.equals(destSS) && !SSLoc.equals(SJSSCDSignalChannelMap.getLocalSSName())){
                                                //Need to notify the other side that the location of this CD is changed!
                                                
                                                for(int h=0;h<vecAllCDIns.size();h++){
                                                    
                                                    ClockDomain CDIns = (ClockDomain)vecAllCDIns.get(h);
                                                    
                                                    if(CDIns.getName().equals(CDName)){
                                                        
                                                        String cname = OutChanName+"_out";
                                                        
                                                        Field f = CDIns.getClass().getField(cname);
                                                       
                                                        output_Channel ochan = (output_Channel)f.get(CDIns);
                                                        
                                                        ochan.TransmitCDLocChanges(destSS);
                                                        
                                                    }
                                                    
                                                }
                                                
                                            }
                                            
                                        }
                                        
                                    }
                                    */
                                
                            //}
                        
                        
                        
                     //}
                     
                     
                // }
                 
                 // updating the mapping should also be done when housekeeping    
                     
                 //SJSSCDSignalChannelMap.UpdateAllCurrSignalChannelMapping(jsSSCDs);
                     
                     
                 
             //}
             
             //System.out.println("Done sending SigChan Mapping!");
             
             // transfer service description, if any
             
                
                
                //if(!jsIntReg.isEmpty()){
                    
                    //JSONObject transferredReg  = new JSONObject();
             
                    //Enumeration keysFileList = fileList.keys();

                    
                    int x = 1;
                    int j=1;

                    //JSONObject jsRemainingReg = new JSONObject();

                    //for(int m=0;m<vecAllCDName.size();m++){

                        //String ind = keysFileList.nextElement().toString();

                        //String filename = vecAllCDName.get(m).toString();
                        

                        //Enumeration keysjsIntReg = jsIntReg.keys();

                        //while (keysjsIntReg.hasMoreElements()){
                           //String servIndex = keysjsIntReg.nextElement().toString();

                           //JSONObject indivServ = jsIntReg.getJSONObject(servIndex);

                           //if(filename.equalsIgnoreCase(indivServ.getString("associatedCDName"))){

                          //     transferredReg.put(Integer.toString(x),indivServ);

                          // } else {
                          //     jsRemainingReg.put(indivServ.getString("serviceName"), indivServ);
                          // }

                       //}


                    //}

                    //update internal service description should be during housekeeping, so the remaining reg should be saved somewhere.

                    //SJServiceRegistry.UpdateAllInternalRegistry(jsRemainingReg);

                    
                        
                        //sOut.writeObject(jsCDServDesc);
                        //sOut.flush();
                    
                    
                    //System.out.println("Sending reg:" +transferredReg.toPrettyPrintedString(2, 0));
                    //System.out.println("Registry sent. Updated reg after Migration:" +jsRemainingReg.toPrettyPrintedString(2, 0));
                    
                    //sending Stop migration message to terminate transfer process
                    
                    
                    
                    
                    
                    //CDLCBuffer.updateMigrationStatus("Completed");
                    
                //} 
               
                //IMBuffer.SaveInterfaceManagerConfig(im);
                                            
                
                
                //TransferStat = true;
                                            
                //Check if there is a CD missing out a pair in the local SS and that the pair is sent to the dest SS
                //if no IC physical TCP IP , then create one
                //sOut.writeObject("STOP");
                //sOut.flush();
                
                sOut.close();
                
                
                
                //System.out.println("CDLCMigrationTransferThread, all destSS & migType : " +CDLCBuffer.GetRequestMigrate());
                
                //Vector vecAllCDName = CDLCBuffer.GetAllCDNameOfMigTypeAndDestSS(destSS, migType);
                 
                //CDLCBuffer.RemoveReqMigrate(destSS, migType);
                
                        
                //CDLCBuffer.setChangedFlag();
                //CDLCBuffer.setCDAmountChangedFlag();
                
         
        } catch(FileNotFoundException ex){
            ex.printStackTrace();
            
            
        } catch(IOException ex2){
            ex2.printStackTrace();
            
           
        } catch (JSONException ex3) {
            ex3.printStackTrace();
             
            
        } 
        /*
        catch (ClassNotFoundException ex) {
           ex.printStackTrace();
            CDLCBuffer.SetMigrationStatus(destSS, "FAIL");
        } 
        catch (InstantiationException ex) {
            ex.printStackTrace();
             CDLCBuffer.SetMigrationStatus(destSS, "FAIL");
        } 
        catch (IllegalAccessException ex) {
            ex.printStackTrace();
             CDLCBuffer.SetMigrationStatus(destSS, "FAIL");
        }
        */
        catch (Exception ex){
            ex.printStackTrace();
            
           
        }
        
        //Vector vec = new Vector();
        
        //vec.addElement(im);
        //vec.addElement(sc);
        
        //return vec;
        
        return TransferStat;
        
    }
    
}
