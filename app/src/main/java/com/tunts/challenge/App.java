/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.tunts.challenge;

import com.google.api.services.sheets.v4.model.ValueRange;

import io.github.cdimascio.dotenv.Dotenv;

public class App {

    private static Dotenv dotenv = Dotenv.load();

    public static void main( String[] args ) 
    {

        try 
        {
            ValueRange values = SheetController.getValues( dotenv.get( "SPREADSHEETS_ID" ), "A:D" );
            
            System.out.println( values );
        } 
        catch( Exception e ) 
        {
            Handler.Handle( e );
        }

    }
}
