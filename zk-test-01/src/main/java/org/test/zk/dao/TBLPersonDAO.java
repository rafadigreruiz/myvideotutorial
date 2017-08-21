package org.test.zk.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.test.zk.database.CDatabaseConnection;
import org.test.zk.datamodel.TBLPerson;

public class TBLPersonDAO {
    
    public static TBLPerson loadData( final CDatabaseConnection databaseConnection, final String strId ) {
        
        TBLPerson result = null;
        
        try {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                Statement statement = databaseConnection.getDBConnection().createStatement();
                
                ResultSet resultSet = statement.executeQuery( "Select * from tblPerson where ID = '" + strId + "'" );
                
                if ( resultSet.next() ) {
                    
                    result = new TBLPerson();
                    
                    result.setId( resultSet.getString( "ID" ) );
                    result.setFirstName( resultSet.getString( "firstName" ) );
                    result.setLastName( resultSet.getString( "LastName" ) );
                    result.setGender( resultSet.getInt( "Gender" ) );
                    result.setBirthDate( resultSet.getDate( "BirthDate" ).toLocalDate() );
                    result.setComment( resultSet.getString( "Comment" ) );
                    
                    //Los siguientes metodos vienen de la clase CAuditableDataModel
                    result.setCreatedBy( resultSet.getString( "CreatedBy" ) );
                    result.setCreatedAtDate( resultSet.getDate( "CreatedAtDate" ).toLocalDate() );
                    result.setCreatedAtTime( resultSet.getTime( "CreatedAtTime" ).toLocalTime() );
                    result.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    result.setUpdatedAtDate( resultSet.getDate( "UpdatedAtDate" ).toLocalDate() != null ? resultSet.getDate( "UpdatedAtDate" ).toLocalDate() : null );
                    result.setUpdatedAtTime( resultSet.getTime( "UpdatedAtTime" ).toLocalTime() != null ? resultSet.getTime( "UpdatedAtTime" ).toLocalTime() : null );
                    
                }
                
                //Cuando se termina debemos cerrar los recursos
                resultSet.close();
                statement.close();
                
                //NO cerramos la conexión. La mantenemos abierta para usarla en otras operaciones
                
            }
            
        }
        catch ( Exception ex ) {
            
           ex.printStackTrace(); 
            
        }
        
        return result;
        
    }
    
    public static boolean insertData( final CDatabaseConnection databaseConnection, final TBLPerson tblPerson ) {
        
        boolean bResult = false;
        
        try {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                Statement statement = databaseConnection.getDBConnection().createStatement();
                
                final String strSQL = "Insert Into TblPerson(ID, FirstName, LastName, Gender, BirthDate, Comment, CreatedBy, "
                        + "CreatedAtDate, CreatedAtTime, UpdatedBy, UpdatedAtDate, UpdatedAtTime) values ('"
                        + tblPerson.getId() + "', '" + tblPerson.getFirstName() + "', '" + tblPerson.getLastName() + "', '"
                        + tblPerson.getGender() + "', '" + tblPerson.getBirthDate().toString() + "', '" + tblPerson.getComment()
                        + "', 'test', '" + LocalDate.now().toString() + "', '" + LocalTime.now().toString() + "', null, null, null)";
                
                statement.executeUpdate( strSQL );
                databaseConnection.getDBConnection().commit();
                statement.close();
                
                bResult = true;
                
            }
            
        }
        catch ( Exception ex ) {
            
            ex.printStackTrace();
            
        }
        
        return bResult;
        
    }
    
    public static boolean updateData( final CDatabaseConnection databaseConnection, final TBLPerson tblPerson ) {
        
        boolean bResult = false;
        
        return bResult;
        
    }
    
    public static boolean deleteData( final CDatabaseConnection databaseConnection, final String strId ) {
        
        boolean bResult = false;
        
        try {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                Statement statement = databaseConnection.getDBConnection().createStatement();
                
                final String strSQL = "Delete from tblPerson where ID = '" + strId + "'";
                
                statement.executeUpdate( strSQL );
                databaseConnection.getDBConnection().commit();
                statement.close();
                
                bResult = true;
                
            }
            
        }
        catch ( Exception ex ) {
            
            ex.printStackTrace();
            
        }
        
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
                
                //Cuando se termina debemos cerrar los recursos
                resultSet.close();
                statement.close();
                
                //NO cerramos la conexión. La mantenemos abierta para usarla en otras operaciones
                
            }
            
        }
        catch ( Exception ex ) {
            
           ex.printStackTrace(); 
            
        }
        
        return result;
        
    }
    
}
