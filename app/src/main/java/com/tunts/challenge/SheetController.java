package com.tunts.challenge;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.Collections;

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
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault().createScoped( Collections.singleton( SheetsScopes.SPREADSHEETS ) );
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter( credentials );

        //Create the sheets API client
        Sheets service = new Sheets.Builder( new NetHttpTransport(), GsonFactory.getDefaultInstance(), requestInitializer ).setApplicationName( "Sheets samples" ).build();

        ValueRange result = null;
        try 
        {
            // Gets the values of the cells in the specified range.
            result = service.spreadsheets().values().get( spreadsheetId, range ).execute();
        } 
        catch ( Exception e ) 
        {
            Handler.Handle( e );
        }
        return result;
    }
}    

