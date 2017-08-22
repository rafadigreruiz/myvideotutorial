package org.test.zk.zksubsystem;

import java.util.List;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.DesktopInit;
import org.zkoss.zk.ui.util.ExecutionCleanup;
import org.zkoss.zk.ui.util.ExecutionInit;
import org.zkoss.zk.ui.util.SessionCleanup;
import org.zkoss.zk.ui.util.SessionInit;
import org.zkoss.zk.ui.util.WebAppCleanup;
import org.zkoss.zk.ui.util.WebAppInit;

import commonlibs.commonclasses.CLanguage;
import commonlibs.commonclasses.ConstantsCommonClasses;
import commonlibs.extendedlogger.CExtendedConfigLogger;
import commonlibs.extendedlogger.CExtendedLogger;

public class CZKSubsystemEvents implements DesktopInit, DesktopCleanup, SessionInit, SessionCleanup, WebAppInit, WebAppCleanup, ExecutionInit, ExecutionCleanup {

    @Override
    public void cleanup( Execution execution0, Execution execution1, List<Throwable> arg2 ) throws Exception {
        
        System.out.println( "Execution cleanup" );
        
    }

    @Override
    public void init( Execution execution0, Execution execution1 ) throws Exception {
        
        System.out.println( "Execution init" );
        
    }

    @Override
    public void cleanup( WebApp webApp ) throws Exception {
        
        System.out.println( "web app cleanup" ); //Este evento se llama cuando el servidor de aplicaciones termina (Civilizadamente) el jetty en nuestro caso
        
        try {
            
            //Obtenermos el logger
            CExtendedLogger webAppLogger = CExtendedLogger.getLogger( ConstantsCommonClasses._Webapp_Logger_Name );

            if ( webAppLogger != null ) {
                
                //Escribimos un mensajes al log
                webAppLogger.logMessage( "1" , CLanguage.translateIf( null, "Webapp ending now." ) );

                //Cerramos el log
                webAppLogger.flushAndClose();
                
            }

            //Eliminamos el atributo del webapp
            webApp.removeAttribute( ConstantsCommonClasses._Webapp_Logger_Name );
            
        }
        catch ( Exception ex ) {
            
            System.out.println( ex.getMessage() );
            
        }
        
    }

    @Override
    public void init( WebApp webApp ) throws Exception {
        
        System.out.println( "web app init" ); //Este evento se llama cuando el servidor de aplicaciones inicia el jetty en nuestro caso
        
        try {
            
            String strRunningPath = webApp.getRealPath( ConstantsCommonClasses._WEB_INF_Dir ) + "/";
            
            //Se encarga de leer el archivo de configuraci�n logger.config.xml
            CExtendedConfigLogger configLogger = new CExtendedConfigLogger();
            
            //Le decimos la ruta del archivo WEB-INF/config/logger.config.xml
            String strConfigPath = strRunningPath + ConstantsCommonClasses._Config_Dir + ConstantsCommonClasses._Logger_Config_File_Name;
            
            if ( configLogger.loadConfig( strConfigPath, null, null ) ) { //Cargamos la configuraci�n
                
                //Aqu� creamos el logger como tal
                CExtendedLogger webAppLogger = CExtendedLogger.getLogger( ConstantsCommonClasses._Webapp_Logger_Name );
                
                if ( webAppLogger.getSetupSet() == false ) { //Preguntamos si todav�a no esta configurado
                    
                    //Aqu� le decimos donde va a crear los archivos de log WEB-INF/logs/system
                    String strLogPath = strRunningPath + ConstantsCommonClasses._Logs_Dir + ConstantsCommonClasses._System_Dir;
                    
                    //Configuramos el logger seg�n los par�metros de el archivo logger.config.xml y la ruta para escribir los archivos de log
                    webAppLogger.setupLogger( configLogger.getInstanceID(), configLogger.getLogToScreen(), strLogPath, ConstantsCommonClasses._Webapp_Logger_File_Log, configLogger.getClassNameMethodName(), configLogger.getExactMatch(), configLogger.getLevel(), configLogger.getLogIP(), configLogger.getLogPort(), configLogger.getHTTPLogURL(), configLogger.getHTTPLogUser(), configLogger.getHTTPLogPassword(), configLogger.getProxyIP(), configLogger.getProxyPort(), configLogger.getProxyUser(), configLogger.getProxyPassword() );
                    
                    //Guardamos el logger principal en un atributo del webapp
                    webApp.setAttribute( ConstantsCommonClasses._Webapp_Logger_App_Attribute_Key, webAppLogger );
                 
                }
                
                //Aqu� escribimos al log en un archivo en WEB-INF/logs/system/webapplogger.log
                //Fijense en la clase CLanguage es otra de mis clases, pero no le presten mucha atenci�n todav�a
                //Basta con decir que es una clase que permite escribir los mensajes del log en varios idiomas
                webAppLogger.logMessage( "1" , CLanguage.translateIf( null, "Webapp logger loaded and configured [%s].", ConstantsCommonClasses._Webapp_Logger_Name ) );
                
            }
            
        }
        catch ( Exception ex ) {
            
            System.out.println( ex.getMessage() ); //Aqu� no queda m�s remedio que usar la salida estandar el sistem la consola, por que todavia no existe el logger
            
        }
        
    }

    @Override
    public void cleanup( Session session ) throws Exception {
        
        System.out.println( "Session cleanup" );
        
    }

    @Override
    public void init( Session session, Object object ) throws Exception {
        
        System.out.println( "Session init" );
        
    }

    @Override
    public void cleanup( Desktop desktop ) throws Exception {
        
        System.out.println( "Desktop cleanup" );
        
    }

    @Override
    public void init( Desktop desktop, Object object ) throws Exception {
        
        System.out.println( "Desktop init" );
        
    }
        
}