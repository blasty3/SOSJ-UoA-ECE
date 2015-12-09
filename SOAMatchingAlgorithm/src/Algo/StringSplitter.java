/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algo;

import java.util.Vector;

public class StringSplitter{
    
    public synchronized String[] split(String a,String delimiter)
    {
        String c[]=new String[0];
        String b=a;
        while (true)
        {
            int i=b.indexOf(delimiter);
            String d=b;
            if (i>=0)
                d=b.substring(0,i);
            String e[]=new String[c.length+1];
            for (int k=0;k<c.length;k++)
                e[k]=c[k];
            e[e.length-1]=d;
            c=e;
            b=b.substring(i+delimiter.length(),b.length());
            if (b.length()<=0 || i<0 )
            { 
                b=null;
                e=null;
                d=null;
                break;
            }
        }
        
        return c;
        
    }
}




/*
 *
 * public class StringSplitter {
 * public String[] split(String original,String delimiter) {
 * Vector f = new Vector();
 * String separator = delimiter;
 * System.out.println("start the string splitter");
 * int index = original.indexOf(separator);
 * while(index>=0) {
 * f.addElement( original.substring(0, index) );
 * original = original.substring(index+separator.length());
 * index = original.indexOf(separator);
 * }
 * // Get the last node
 * f.addElement( original );
 *
 * // Create splitted string array
 * String[] result = new String[ f.size() ];
 * if( f.size()>0 ) {
 * for(int loop=0; loop<f.size(); loop++)
 * {
 * result[loop] = (String)f.elementAt(loop);
 * System.out.println(result[loop]);
 * }
 *
 * }
 *
 * return result;
 * }
 *
 * }
 */