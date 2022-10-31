package com.tunts.challenge.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Logger 
{
    
    private static Logger defaultInstance = new Logger();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ); 
    private StringBuilder logs = new StringBuilder();

    private Logger(){}

    
    /** 
     * @param message
     */
    public static void logInfo( String message ) 
    {
        message = simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) + ": [INFO] " + message + "\n";

        System.out.print( message );
        defaultInstance.logs.append( message );
    }    

    
    /** 
     * @param message
     */
    public static void logError( String message ) 
    {
        message = simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) + ": [ERROR] " + message + "\n";

        System.out.print( message );
        defaultInstance.logs.append( message );
    }    

    public static void dump()
    {
        write( defaultInstance.logs.toString() );
    }

    
    /** 
     * @param data
     */
    private static void write( String data ) 
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd-hhmmss" );

        OutputStream outputStream = null;
        try 
        {
            outputStream = new FileOutputStream( new File( "logs/"  + dateFormat.format( new Date( System.currentTimeMillis() ) ) + ".txt" ) );
            outputStream.write( data.getBytes(), 0, data.length() );
        } 
        catch ( IOException e ) 
        {
            e.printStackTrace();
        }
        finally
        {
            try 
            {
                outputStream.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }
}
