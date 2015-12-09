/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testsplit;

/**
 *
 * @author Atmojo
 */
public class Testsplit {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String test = "._o";
        
        if(test.equalsIgnoreCase(".")){
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
        
        String dest = ((String)test).substring(0, ((String)test).indexOf("."));
        
        System.out.println("OS: " +System.getProperty("os.name"));
        
    }
    
}
