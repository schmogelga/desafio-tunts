package com.tunts.challenge.util;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class Handler {
    
    /** 
     * @param e
     */
    public static void handle( Exception e )
    {
        if( e instanceof GoogleJsonResponseException )
        {
            GoogleJsonError error = ( (GoogleJsonResponseException ) e ).getDetails();
            Logger.logError( error.getCode() + " - " + error.getMessage() );
        }
        else
        {   
            e.printStackTrace();
            
            Logger.logError( "Critical error, closing application: " + e.getMessage() );
            Logger.dump();
            
            System.exit( 1 );
        }
    }
}
