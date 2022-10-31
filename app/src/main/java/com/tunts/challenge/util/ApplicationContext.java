package com.tunts.challenge.util;

import java.util.ArrayList;

import io.github.cdimascio.dotenv.Dotenv;

public class ApplicationContext {
    
    private static final Dotenv dotenv = Dotenv.load();

    public static final String SPREADSHEETS_ID         = dotenv.get( "SPREADSHEETS_ID" );
    public static final String READ_RANGE              = dotenv.get( "READ_RANGE" );
    public static final String WRITE_RANGE             = dotenv.get( "WRITE_RANGE" );

    public static final int REGISTRATION_HEADER        = Integer.parseInt( dotenv.get( "REGISTRATION_HEADER", "0" ) );
    public static final int NAME_HEADER                = Integer.parseInt( dotenv.get( "NAME_HEADER",         "1" ) );
    public static final int ABSENCES_HEADER            = Integer.parseInt( dotenv.get( "ABSENCES_HEADER",     "2" ) );
    
    public static final ArrayList<Integer>P_HEADERS    = new ArrayList<Integer>();

    static
    {
        String temp = dotenv.get( "P_HEADERS",    "3,4,5" );

        for( String P_HEADER : temp.split( "," ) )
        {
            P_HEADERS.add( Integer.parseInt( P_HEADER ) );
        }
    }
}
