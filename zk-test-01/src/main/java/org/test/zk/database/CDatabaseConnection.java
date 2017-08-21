package org.test.zk.database;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.DriverManager;

public class CDatabaseConnection implements Serializable {
    
    private static final long serialVersionUID = 4223658996656423487L;
    
    //static final String _DB_URL = "jdbc:mysql://localhost/TestDB";
    
    //Credenciales de la BD
    //static final String _USER = "root";
    //static final String _PASS = "";
    
    protected Connection dbConnection = null;
    
    public Connection getDBConnection() {
        
        return dbConnection;
        
    }

    
    public void setDBConnection( Connection dbConnection ) {
        
        this.dbConnection = dbConnection;
        
    }
    
    public boolean makeConnectionToDatabase( CDatabaseConnectionConfig databaseConnectionConfig ) {
        
        boolean bResult = false;
        
        try {
            
            if ( databaseConnectionConfig != null ) {
                
                Class.forName( databaseConnectionConfig.getDriver() );
                
                final String strDBURL = databaseConnectionConfig.getPrefix() + databaseConnectionConfig.getHost() + ":" + databaseConnectionConfig.getPort() + "/" + databaseConnectionConfig.getDatabase();
                
                dbConnection = DriverManager.getConnection( strDBURL, databaseConnectionConfig.getUser(), databaseConnectionConfig.getPassword() );
                
                dbConnection.setTransactionIsolation( Connection.TRANSACTION_READ_COMMITTED );
                
                dbConnection.setAutoCommit( false );
                
                bResult = true;
                
            }
            
        }
        catch ( Exception ex ) {
            
            ex.printStackTrace();
            
        }
        
        return bResult;
        
    }
    
    public boolean closeConnectionToDatabase() {
        
        boolean bResult = false;
        try {
            
            dbConnection.close(); //Liberar recursos de la conexión
            
            dbConnection = null;
            
            bResult = true;
            
        }
        catch ( Exception ex ) {
            
            ex.printStackTrace();
            
        }
        
        return bResult;
        
    }
    
}
