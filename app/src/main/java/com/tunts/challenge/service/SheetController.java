package com.tunts.challenge.service;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.tunts.challenge.util.Handler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SheetController 
{
    /**
     * Returns a range of values from a spreadsheet.
     *
     * @param spreadsheetId - Id of the spreadsheet.
     * @param range         - Range of cells of the spreadsheet.
     * @return Values in the range
     * @throws IOException - if credentials file not found.
     */
    public static ValueRange getValues( String spreadsheetId, String range ) throws IOException 
    {
        ValueRange result = null;
        try 
        {
            // Gets the values of the cells in the specified range.
            result = getSheet().spreadsheets().values().get( spreadsheetId, range ).execute();
        } 
        catch ( Exception e ) 
        {
            Handler.handle( e );
        }
        
        return result;
    }
    
    /**
    * Sets values in a range of a spreadsheet.
    *
    * @param spreadsheetId    - Id of the spreadsheet.
    * @param range            - Range of cells of the spreadsheet.
    * @param valueInputOption - Determines how input data should be interpreted.
    * @param values           - List of rows of values to input.
    * @return spreadsheet with updated values
    * @throws IOException - if credentials file not found.
    */
     public static UpdateValuesResponse updateValues( String spreadsheetId, String range, List<List<Object>> values ) throws IOException 
    {        
        UpdateValuesResponse result = null;
        try 
        {
             // Updates the values in the specified range.
             ValueRange body = new ValueRange().setValues( values );
             result = getSheet().spreadsheets().values().update( spreadsheetId, range, body ).setValueInputOption( "USER_ENTERED" ) .execute();
        } 
        catch ( Exception e ) 
        {
            Handler.handle( e );
        }
            
        return result;
    }
    
    /** 
     * @return HttpRequestInitializer
     * @throws IOException
     */
    private static Sheets getSheet() throws IOException
    {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault().createScoped( Collections.singleton( SheetsScopes.SPREADSHEETS ) );
        HttpRequestInitializer httpRequestInitializer = new HttpCredentialsAdapter(credentials);

        // Create the sheets API client
        return new Sheets.Builder( new NetHttpTransport(), GsonFactory.getDefaultInstance(), httpRequestInitializer ).setApplicationName( "Sheets samples" ).build();
    }
}    

