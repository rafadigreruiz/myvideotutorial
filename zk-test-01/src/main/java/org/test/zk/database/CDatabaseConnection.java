package org.test.zk.database;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.DriverManager;

public class CDatabaseConnection implements Serializable {
    
    private static final long serialVersionUID = 4223658996656423487L;
    
    static final String _DB_URL = "jdbc:mysql://localhost/TestDB";
    
    //Credenciales de la BD
    static final String _USER = "root";
    static final String _PASS = "";
    
    protected Connection dbConnection = null;
    
    public Connection getDBConnection() {
        
        return dbConnection;
        
    }

    
    public void setDBConnection( Connection dbConnection ) {
        
        this.dbConnection = dbConnection;
        
    }
    
    public boolean makeConnectionToDatabase() {
        
        boolean bResult = false;
        
        try {
            
            Class.forName( "com.mysql.jdbc.Driver" );
            
            dbConnection = DriverManager.getConnection( _DB_URL, _USER, _PASS );
            
            dbConnection.setTransactionIsolation( Connection.TRANSACTION_READ_COMMITTED );
            
            dbConnection.setAutoCommit( false );
            
            bResult = true;
            
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
