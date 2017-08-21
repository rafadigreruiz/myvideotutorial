package org.test.zk.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.test.zk.database.CDatabaseConnection;
import org.test.zk.datamodel.TBLPerson;

public class TBLPersonDAO {
    
    public static TBLPerson loadData( final CDatabaseConnection databaseConnection, final String strId ) {
        
        TBLPerson result = null;
        
        return result;
        
    }
    
    public static boolean deleteData( final CDatabaseConnection databaseConnection, final String strId ) {
        
        boolean bResult = false;
        
        return bResult;
        
    }
    
    public static boolean insertData( final CDatabaseConnection databaseConnection, final TBLPerson tblPerson ) {
        
        boolean bResult = false;
        
        return bResult;
        
    }
    
    public static boolean updateData( final CDatabaseConnection databaseConnection, final TBLPerson tblPerson ) {
        
        boolean bResult = false;
        
        return bResult;
        
    }
    
    public static List<TBLPerson> searchData( final CDatabaseConnection databaseConnection ) {
        
        List<TBLPerson> result = new ArrayList<TBLPerson>();
        
        try {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                Statement statement = databaseConnection.getDBConnection().createStatement();
                
                ResultSet resultSet = statement.executeQuery( "Select * from tblPerson" );
                
                while ( resultSet.next() ) {
                    
                    TBLPerson tblPerson = new TBLPerson();
                    
                    tblPerson.setId( resultSet.getString( "ID" ) );
                    tblPerson.setFirstName( resultSet.getString( "firstName" ) );
                    tblPerson.setLastName( resultSet.getString( "LastName" ) );
                    tblPerson.setGender( resultSet.getInt( "Gender" ) );
                    tblPerson.setBirthDate( resultSet.getDate( "BirthDate" ).toLocalDate() );
                    tblPerson.setComment( resultSet.getString( "Comment" ) );
                    
                    //Los siguientes metodos vienen de la clase CAuditableDataModel
                    tblPerson.setCreatedBy( resultSet.getString( "CreatedBy" ) );
                    tblPerson.setCreatedAtDate( resultSet.getDate( "CreatedAtDate" ).toLocalDate() );
                    tblPerson.setCreatedAtTime( resultSet.getTime( "CreatedAtTime" ).toLocalTime() );
                    tblPerson.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    tblPerson.setUpdatedAtDate( resultSet.getDate( "UpdatedAtDate" ).toLocalDate() != null ? resultSet.getDate( "UpdatedAtDate" ).toLocalDate() : null );
                    tblPerson.setUpdatedAtTime( resultSet.getTime( "UpdatedAtTime" ).toLocalTime() != null ? resultSet.getTime( "UpdatedAtTime" ).toLocalTime() : null );
                    
                    result.add( tblPerson );
                    
                }
                
            }
            
        }
        catch (Exception ex) {
            
           ex.printStackTrace(); 
            
        }
        
        return result;
        
    }
    
}
