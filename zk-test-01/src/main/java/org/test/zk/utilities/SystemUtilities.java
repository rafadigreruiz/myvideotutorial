package org.test.zk.utilities;

import org.test.zk.constants.SystemConstants;
import org.zkoss.zk.ui.Session;

import commonlibs.extendedlogger.CExtendedConfigLogger;
import commonlibs.extendedlogger.CExtendedLogger;

public class SystemUtilities {
    
    public static CExtendedConfigLogger initLoggerConfig( String strRunningPath, Session currentSession ) {

        //Tratamos de cargar la configuraci�n de los logger de la sessi�n
        CExtendedConfigLogger result = (CExtendedConfigLogger) currentSession.getAttribute( SystemConstants._Config_Logger_Session_Key );  
                
        if ( result == null ) {
        
            //En caso de no encontrarse en la sesi�n lo leemos del archivo de configuraci�n
            result = new CExtendedConfigLogger();

            String strConfigPath = strRunningPath + SystemConstants._Config_Dir;

            //Localizamos el logger del webAppLogger el cual para este c�digo ya debe estar presente se inicia en CZKSubsystemEvents.java
            CExtendedLogger webAppLogger = CExtendedLogger.getLogger( SystemConstants._Webapp_Logger_Name );
            
            //Leemos la config del archivo
            if ( result.loadConfig( strConfigPath + SystemConstants._Logger_Config_File_Name, webAppLogger, null ) == false ) {
                
                result = null; //En caso de fallar no retornar nada
                
            }
        
        }
        
        return result;
        
    }
    
}    
