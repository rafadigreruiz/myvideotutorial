package org.test.zk.database.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.datamodel.TBLOperator;

import commonlibs.commonclasses.CLanguage;
import commonlibs.extendedlogger.CExtendedLogger;

public class OperatorDAO {
    
    public static TBLOperator loadData( final CDatabaseConnection databaseConnection, final String strId, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        TBLOperator result = null;
        
        try {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                Statement statement = databaseConnection.getDBConnection().createStatement();
                
                ResultSet resultSet = statement.executeQuery( "Select * from tblOperator where id = '" + strId + "'" );
                
                if ( resultSet.next() ) {
                    
                    result = new TBLOperator();
                    
                    result.setId( resultSet.getString( "id" ) );
                    result.setName( resultSet.getString( "Name" ) );
                    result.setPassword( resultSet.getString( "Password" ) );
                    result.setComment( resultSet.getString( "Comment" ) );
                    
                    //Los siguientes metodos vienen de la clase CAuditableDataModel
                    result.setCreatedBy( resultSet.getString( "CreatedBy" ) );
                    result.setCreatedAtDate( resultSet.getDate( "CreatedAtDate" ).toLocalDate() );
                    result.setCreatedAtTime( resultSet.getTime( "CreatedAtTime" ).toLocalTime() );
                    result.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    result.setUpdatedAtDate( resultSet.getDate( "UpdatedAtDate" ) != null ? resultSet.getDate( "UpdatedAtDate" ).toLocalDate() : null );
                    result.setUpdatedAtTime( resultSet.getTime( "UpdatedAtTime" ) != null ? resultSet.getTime( "UpdatedAtTime" ).toLocalTime() : null );
                    
                    result.setDisabledBy( resultSet.getString( "DisabledBy" ) );
                    result.setDisabledAtDate( resultSet.getDate( "DisabledAtDate" ) != null ? resultSet.getDate( "DisabledAtDate" ).toLocalDate() : null );
                    result.setDisabledAtTime( resultSet.getTime( "DisabledAtTime" ) != null ? resultSet.getTime( "DisabledAtTime" ).toLocalTime() : null );
                    
                    result.setLastLoginAtDate( resultSet.getDate( "LastLoginAtDate" ) != null ? resultSet.getDate( "LastLoginAtDate" ).toLocalDate() : null );
                    result.setLastLoginAtTime( resultSet.getTime( "LastLoginAtTime" ) != null ? resultSet.getTime( "LastLoginAtTime" ).toLocalTime() : null );
                    
                }
                
                //Cuando se termina debemos cerrar los recursos
                resultSet.close();
                statement.close();
                
                //NO cerramos la conexión. La mantenemos abierta para usarla en otras operaciones
                
            }
            
        }
        catch ( Exception ex ) {
            
            if ( localLogger != null )   
                localLogger.logException( "-1021", ex.getMessage(), ex ); 
            
        }
        
        return result;
        
    }
    
    public static boolean insertData( final CDatabaseConnection databaseConnection, final TBLOperator tblOperator, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        boolean bResult = false;
        
        try {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                Statement statement = databaseConnection.getDBConnection().createStatement();
                
                final String strSQL = "Insert Into tblOperator(id, Name, Password, Comment, CreatedBy, "
                        + "CreatedAtDate, CreatedAtTime, UpdatedBy, UpdatedAtDate, UpdatedAtTime, DisabledBy, DisabledAtDate, "
                        +"DisabledAtTime, LastLoginAtDate, LastLoginAtTime) values ('" + tblOperator.getId() + "', '" 
                        + tblOperator.getName() + "', '" + tblOperator.getPassword() + "', '"
                        + tblOperator.getComment() + "', '" + "', 'test', '" + LocalDate.now().toString() + "', '" + LocalTime.now().toString()
                        + "', null, null, null, null, null, null, null, null)";
                
                statement.executeUpdate( strSQL );
                
                databaseConnection.getDBConnection().commit(); //Commit la transacción
                
                statement.close();
                
                bResult = true;
                
            }
            
        }
        catch ( Exception ex ) {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                try {
                    
                    databaseConnection.getDBConnection().rollback();
                    
                }
                catch ( Exception e ) {
                    
                    if ( localLogger != null )   
                        localLogger.logException( "-1021", e.getMessage(), e );
                    
                }
                
            }
            
            if ( localLogger != null )   
                localLogger.logException( "-1022", ex.getMessage(), ex );
            
        }
        
        return bResult;
        
    }
    
    public static boolean updateData( final CDatabaseConnection databaseConnection, final TBLOperator tblOperator, final String strID, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        boolean bResult = false;
        
        try {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                Statement statement = databaseConnection.getDBConnection().createStatement();
                
                final String strSQL = "Update tblOperator set id='" + tblOperator.getId() + "', Name='" + tblOperator.getName()
                        + "', Password='" + tblOperator.getPassword() + "', Comment='" + tblOperator.getComment() + "', UpdatedBy='"
                        + tblOperator.getUpdatedBy() + "', UpdatedAtDate='" + LocalDate.now().toString() + "', UpdatedAtTime='"
                        + LocalTime.now().toString() + "', DisabledBy='" + tblOperator.getDisabledBy() + "', DisabledAtDate="
                        + tblOperator.getDisabledBy() != null ? "'" + LocalDate.now().toString() + "'" : "null"
                        + ", DisabledAtTime=" + tblOperator.getDisabledBy() != null ? "'" + LocalTime.now().toString() + "'" : "null"
                        + "where id='" + strID + "'";
                
                statement.executeUpdate( strSQL );
                
                databaseConnection.getDBConnection().commit(); //Commit la transacción
                
                statement.close();
                
                bResult = true;
                
            }
            
        }
        catch ( Exception ex ) {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                try {
                    
                    databaseConnection.getDBConnection().rollback();
                    
                }
                catch ( Exception e ) {
                    
                    if ( localLogger != null )   
                        localLogger.logException( "-1021", e.getMessage(), e );
                    
                }
                
            }
            
            if ( localLogger != null )   
                localLogger.logException( "-1022", ex.getMessage(), ex );
            
        }
        
        return bResult;
        
    }
    
    public static boolean updateLastLogin( final CDatabaseConnection databaseConnection, final String strID, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        boolean bResult = false;
        
        try {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                Statement statement = databaseConnection.getDBConnection().createStatement();
                
                final String strSQL = "Update tblOperator set LastLoginAtDate='" + LocalDate.now().toString()
                        + "', LastLoginAtTime='" + LocalTime.now().toString() + "' where id='" + strID + "'";
                
                statement.executeUpdate( strSQL );
                
                databaseConnection.getDBConnection().commit(); //Commit la transacción
                
                statement.close();
                
                bResult = true;
                
            }
            
        }
        catch ( Exception ex ) {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                try {
                    
                    databaseConnection.getDBConnection().rollback();
                    
                }
                catch ( Exception e ) {
                    
                    if ( localLogger != null )   
                        localLogger.logException( "-1021", e.getMessage(), e );
                    
                }
                
            }
            
            if ( localLogger != null )   
                localLogger.logException( "-1022", ex.getMessage(), ex );
            
        }
        
        return bResult;
        
    }
    
    public static boolean deleteData( final CDatabaseConnection databaseConnection, final String strId, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        boolean bResult = false;
        
        try {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                Statement statement = databaseConnection.getDBConnection().createStatement();
                
                final String strSQL = "Delete from tblOperator where id = '" + strId + "'";
                
                statement.executeUpdate( strSQL );
                
                databaseConnection.getDBConnection().commit(); //Commit la transacción
                
                statement.close();
                
                bResult = true;
                
            }
            
        }
        catch ( Exception ex ) {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                try {
                    
                    databaseConnection.getDBConnection().rollback();
                    
                }
                catch ( Exception e ) {
                    
                    if ( localLogger != null )   
                        localLogger.logException( "-1021", e.getMessage(), e );
                    
                }
                
            }
            
            if ( localLogger != null )   
                localLogger.logException( "-1022", ex.getMessage(), ex );
            
        }
        
        return bResult;
        
    }
    
    public static List<TBLOperator> searchData( final CDatabaseConnection databaseConnection, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        List<TBLOperator> result = new ArrayList<TBLOperator>();
        
        try {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                Statement statement = databaseConnection.getDBConnection().createStatement();
                
                ResultSet resultSet = statement.executeQuery( "Select * from tblOperator" );
                
                while ( resultSet.next() ) {
                    
                    TBLOperator tblOperator = new TBLOperator();
                    
                    tblOperator.setId( resultSet.getString( "id" ) );
                    tblOperator.setName( resultSet.getString( "Name" ) );
                    tblOperator.setPassword( resultSet.getString( "Password" ) );
                    tblOperator.setComment( resultSet.getString( "Comment" ) );
                    
                    //Los siguientes metodos vienen de la clase CAuditableDataModel
                    tblOperator.setCreatedBy( resultSet.getString( "CreatedBy" ) );
                    tblOperator.setCreatedAtDate( resultSet.getDate( "CreatedAtDate" ).toLocalDate() );
                    tblOperator.setCreatedAtTime( resultSet.getTime( "CreatedAtTime" ).toLocalTime() );
                    tblOperator.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    tblOperator.setUpdatedAtDate( resultSet.getDate( "UpdatedAtDate" ) != null ? resultSet.getDate( "UpdatedAtDate" ).toLocalDate() : null );
                    tblOperator.setUpdatedAtTime( resultSet.getTime( "UpdatedAtTime" ) != null ? resultSet.getTime( "UpdatedAtTime" ).toLocalTime() : null );
                    
                    tblOperator.setDisabledBy( resultSet.getString( "DisabledBy" ) );
                    tblOperator.setDisabledAtDate( resultSet.getDate( "DisabledAtDate" ) != null ? resultSet.getDate( "DisabledAtDate" ).toLocalDate() : null );
                    tblOperator.setDisabledAtTime( resultSet.getTime( "DisabledAtTime" ) != null ? resultSet.getTime( "DisabledAtTime" ).toLocalTime() : null );
                    
                    tblOperator.setLastLoginAtDate( resultSet.getDate( "LastLoginAtDate" ) != null ? resultSet.getDate( "LastLoginAtDate" ).toLocalDate() : null );
                    tblOperator.setLastLoginAtTime( resultSet.getTime( "LastLoginAtTime" ) != null ? resultSet.getTime( "LastLoginAtTime" ).toLocalTime() : null );
                    
                    result.add( tblOperator );
                    
                }
                
                //Cuando se termina debemos cerrar los recursos
                resultSet.close();
                statement.close();
                
                //NO cerramos la conexión. La mantenemos abierta para usarla en otras operaciones
                
            }
            
        }
        catch ( Exception ex ) {
            
            if ( localLogger != null )   
                localLogger.logException( "-1021", ex.getMessage(), ex );
            
        }
        
        return result;
        
    }
    
    public TBLOperator checkValid( final CDatabaseConnection databaseConnection, final String strName, final String strPassword, CExtendedLogger localLogger, CLanguage localLanguage ) {
        
        TBLOperator result = null;
        
        try {
            
            if ( databaseConnection != null && databaseConnection.getDBConnection() != null ) {
                
                Statement statement = databaseConnection.getDBConnection().createStatement();
                
                ResultSet resultSet = statement.executeQuery( "Select * from tblOperator where Name = '" + strName + "' and Password = '" + strPassword + "'" );
                
                if ( resultSet.next() ) {
                    
                    result = new TBLOperator();
                    
                    result.setId( resultSet.getString( "id" ) );
                    result.setName( resultSet.getString( "Name" ) );
                    result.setPassword( resultSet.getString( "Password" ) );
                    result.setComment( resultSet.getString( "Comment" ) );
                    
                    //Los siguientes metodos vienen de la clase CAuditableDataModel
                    result.setCreatedBy( resultSet.getString( "CreatedBy" ) );
                    result.setCreatedAtDate( resultSet.getDate( "CreatedAtDate" ).toLocalDate() );
                    result.setCreatedAtTime( resultSet.getTime( "CreatedAtTime" ).toLocalTime() );
                    result.setUpdatedBy( resultSet.getString( "UpdatedBy" ) );
                    result.setUpdatedAtDate( resultSet.getDate( "UpdatedAtDate" ) != null ? resultSet.getDate( "UpdatedAtDate" ).toLocalDate() : null );
                    result.setUpdatedAtTime( resultSet.getTime( "UpdatedAtTime" ) != null ? resultSet.getTime( "UpdatedAtTime" ).toLocalTime() : null );
                    
                    result.setDisabledBy( resultSet.getString( "DisabledBy" ) );
                    result.setDisabledAtDate( resultSet.getDate( "DisabledAtDate" ) != null ? resultSet.getDate( "DisabledAtDate" ).toLocalDate() : null );
                    result.setDisabledAtTime( resultSet.getTime( "DisabledAtTime" ) != null ? resultSet.getTime( "DisabledAtTime" ).toLocalTime() : null );
                    
                    result.setLastLoginAtDate( resultSet.getDate( "LastLoginAtDate" ) != null ? resultSet.getDate( "LastLoginAtDate" ).toLocalDate() : null );
                    result.setLastLoginAtTime( resultSet.getTime( "LastLoginAtTime" ) != null ? resultSet.getTime( "LastLoginAtTime" ).toLocalTime() : null );
                    
                }
                
                //Cuando se termina debemos cerrar los recursos
                resultSet.close();
                statement.close();
                
                //NO cerramos la conexión. La mantenemos abierta para usarla en otras operaciones
                
            }
            
        }
        catch ( Exception ex ) {
            
            if ( localLogger != null )   
                localLogger.logException( "-1021", ex.getMessage(), ex );
            
        }
        
        return result;
          
    }
    
}
