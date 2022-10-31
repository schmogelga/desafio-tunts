package com.tunts.challenge.service;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.tunts.challenge.data.Student;
import com.tunts.challenge.data.Student.Situation;
import com.tunts.challenge.util.ApplicationContext;
import com.tunts.challenge.util.Logger;

public class StudentController 
{
    
    private static StudentController defaultInstance;
    private final int CLASSES = 60;
    private final double MAX_ABSENCE = 0.25;

    private StudentController() {}
    
    /** 
     * @return StudentController
     */
    public static StudentController getInstance() 
    {
        if ( defaultInstance == null ) 
        {
            defaultInstance = new StudentController();
        }

        return defaultInstance;
    }    
    
    /** 
     * @param valueRange
     * @return List<Student>
     */
    private List<Student> getStudents( ValueRange valueRange )
    {
        List<Student> students = null;
        List<List<Object>> values = valueRange.getValues();

        if( values != null )
        {
            int count = 0;
            students = new ArrayList<Student>();
            for( List<Object> row : values )
            {   
                try
                {
                    students.add( buildStudent( row ) );
                    count++;
                }
                catch( Exception e )
                {
                    //treatment for empty or lines skipped data exception  
                    students.add( null );
                    Logger.logError( "Unable to build student at line " + count + ": " + e.getMessage() );
                }
            } 

            Logger.logInfo( "" );
            Logger.logInfo( "Found " + count + " students: " );
        }

        return students;
    }

    /** 
     * @param row
     * @return Student
     * @throws Exception
     */
    private Student buildStudent( List<Object> row ) throws Exception
    {
        Student student = new Student( getInt( row.get( ApplicationContext.REGISTRATION_HEADER ) ) );

        student.setName( getString( row.get( ApplicationContext.NAME_HEADER ) ) );
        student.setAbsences( getInt( row.get( ApplicationContext.ABSENCES_HEADER) ) );

        for( int index : ApplicationContext.P_HEADERS )
        {
            student.addGrade( getInt( row.get( index ) ) );
        }

        return student;
    }
    
    /** 
     * @param cell
     * @return int
     * @throws Exception
     */
    private int getInt( Object cell ) throws Exception
    {
        if( cell == null)
        {
            throw new IllegalArgumentException( "cell cannot be null!" );
        }

        return Integer.parseInt( cell.toString() );
    }
    
    /** 
     * @param cell
     * @return String
     * @throws Exception
     */
    private String getString( Object cell ) throws Exception
    {
        if( cell == null)
        {
            throw new IllegalArgumentException( "cell cannot be null!" );
        }

        return cell.toString();
    }

    /** 
     * @param students
     */
    private void calculateSituations( List<Student> students )
    {
        for( Student student : students )
        {
            try
            {
                //keep the null for skip the row in the write step
                if( student == null ) continue;

                double situation = student.getAbsences() > (CLASSES * MAX_ABSENCE) ? -1 : student.getGradeAverage(); 
                student.setSituation( Situation.get( (int) Math.ceil( situation ) ) );

                if( student.getSituation().equals( Situation.FINAL_EXAM ) )
                {
                    student.setNaf( calculateNaf( student.getGradeAverage() ) );
                }
                else
                {
                    student.setNaf( 0 );
                }

                Logger.logInfo( student.toString() );
            }
            catch( Exception e )
            {
                Logger.logError( "Unable to calculate situation at student " + student + ": " + e.getMessage() );

            }
        }

        Logger.logInfo( "" );
    }
    
    /** 
     * @param studentAverage
     * @return int
     */
    private int calculateNaf( double studentAverage )
    {
        return Double.valueOf( Math.ceil( 100 - studentAverage ) ).intValue();
    }

    /** 
     * @throws Exception
     */
    public void doWork() throws Exception
    {

        Logger.logInfo( "Sending request to google API - spreadsheetID: " + ApplicationContext.SPREADSHEETS_ID );
        ValueRange result = SheetController.getValues( ApplicationContext.SPREADSHEETS_ID, ApplicationContext.READ_RANGE );
        
        if( result != null )
        {   
            Logger.logInfo( "Building Student objects" );
            List<Student> students = getStudents( result );
            
            Logger.logInfo( "Calculating students situations" );
            calculateSituations( students );

            List<List<Object>> data = new ArrayList<>();
            for( Student student : students )
            {       
                //treatment for blank lines
                if( student != null )
                {
                    List<Object> row = new ArrayList<>();
                    row.add( student.getSituation().toString() );
                    row.add( student.getNaf() );

                    data.add( row );
                }
                else
                {
                    data.add( new ArrayList<>() );
                }
            }

            Logger.logInfo( "Writing values in the spreadsheet" );
            SheetController.updateValues( ApplicationContext.SPREADSHEETS_ID, ApplicationContext.WRITE_RANGE, data );
        }
    }
}
