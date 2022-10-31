package com.tunts.challenge.data;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Student 
{
    private final int registration;
    private String name;
    private int absences;
    private Situation situation;
    private double naf;
    private ArrayList<Integer> grades = new ArrayList<Integer>();
    
    public Student( int registration )
    {
        this.registration = registration;
    }

    /** 
     * @param grade
     */
    public void addGrade( int grade )
    {
        this.grades.add( grade );
    }

    /** 
     * @return double
     */
    public double getGradeAverage()
    {
        double average = -1;

        if( grades.size() > 0 )
        {
            average = this.grades.stream().mapToDouble( Integer::doubleValue ).sum() / grades.size();
        }

        return (double) Math.round( average * 100 ) / 100;
    }
    
    /** 
     * @return String
     */
    @Override
    public String toString()
    {
        return this.name + "(" + getGradeAverage() + ") - " + this.situation;
    }

    public enum Situation {

        ABSENCE_REPROVED( -1, -1, "Reprovado por Falta" ), 

        REPROVED(   0,   49,     "Reprovado por Nota" ), 
        FINAL_EXAM( 50,  69,     "Exame Final" ), 
        APPROVED(   70,  100,    "Aprovado" );
    
        private final int min;
        private final int max;
        private final String desc;
    
        private Situation(int min, int max, String desc ) 
        {
            this.min    = min;
            this.max    = max;
            this.desc   = desc;
        }

        @Override
        public String toString()
        {
            return this.desc;
        }

        public static Situation get( int val ) 
        {
            return  Arrays.stream(values())
                          .filter(r -> val >= r.min && val <= r.max)
                          .findFirst()
                          .orElse(null);            
        }
    }
}
