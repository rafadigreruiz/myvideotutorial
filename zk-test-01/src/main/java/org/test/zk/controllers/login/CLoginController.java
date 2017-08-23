package org.test.zk.controllers.login;

import java.io.File;
import java.time.LocalDateTime;

import org.test.zk.constants.SystemConstants;
import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.CDatabaseConnectionConfig;
import org.test.zk.database.dao.OperatorDAO;
import org.test.zk.database.datamodel.TBLOperator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import commonlibs.commonclasses.CLanguage;
import commonlibs.commonclasses.ConstantsCommonClasses;
import commonlibs.extendedlogger.CExtendedLogger;
import commonlibs.utils.ZKUtilities;


public class CLoginController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 2607061613647188753L;
    
    protected CExtendedLogger controllerLogger = null;
    
    protected CLanguage controllerLanguage = null;
    
    @Wire
    Textbox textboxOperator;
    
    @Wire
    Textbox textboxPassword;
    
    //Constructor
    @Override
    public void doAfterCompose( Component comp ) {
        
        try {
            
            super.doAfterCompose( comp );
            
            //Obtenemos el logger del objeto webApp y guardamos una referencia en la variable de clase controllerLogger
            controllerLogger = ( CExtendedLogger ) Sessions.getCurrent().getWebApp().getAttribute( ConstantsCommonClasses._Webapp_Logger_App_Attribute_Key );
            
        }
        catch ( Exception e ) {
            
            if ( controllerLogger != null )   
                controllerLogger.logException( "-1021", e.getMessage(), e );
            
        }
        
    }

    
    @Listen( "onClick=#buttonLogin" )
    public void onClickButtonLogin( Event event ) {
        
        try {
            
            //Aquí vamos a conectarnos a la bd, y verificar si los valores introducidos son válidos
            
            final String strOperator = ZKUtilities.getTextBoxValue( textboxOperator, controllerLogger );
            final String strPassword = ZKUtilities.getTextBoxValue( textboxPassword, controllerLogger );

            if ( !strOperator.isEmpty() && !strPassword.isEmpty() ) {
                
                CDatabaseConnection databaseConnection = new CDatabaseConnection();
                
                CDatabaseConnectionConfig databaseConnectionConfig = new CDatabaseConnectionConfig();
                
                //En esta línea obtenemos la ruta completa del archivo de configuración incluido el /config/
                String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator + SystemConstants._Config_Dir + File.separator;
                
                if ( databaseConnectionConfig.loadConfig( strRunningPath + SystemConstants._Database_Connection_Config_File_Name, controllerLogger, controllerLanguage ) ) { //Vamos a pasarle null al CLanguage que ahorita no es relevante
                    
                    if ( databaseConnection.makeConnectionToDB( databaseConnectionConfig, controllerLogger, controllerLanguage ) ) {
                        
                        TBLOperator tblOperator = OperatorDAO.checkValid( databaseConnection, strOperator, strPassword, controllerLogger, controllerLanguage );
                        
                        if ( tblOperator != null ) {
                           
                            //Si es válido guardamos la conexión creada en la sesión y guardamos la identidad del operador en la sesión
                            
                            Session currentSession = Sessions.getCurrent();
                            
                            //Salvamos la conexion a la sesión actual del usuario, cada usuario/pestaña tiene su sesión
                            currentSession.setAttribute( SystemConstants._DB_Connection_Session_Key, databaseConnection ); //La sesion no es mas que un arreglo asociativo
                            
                            //Salvamos la entidad del operador en la sesión
                            currentSession.setAttribute( SystemConstants._User_Credential__Session_Key, tblOperator );
                            
                            //Salvamos la fecha y hora del inicio de sesión
                            currentSession.setAttribute( SystemConstants._Login_Date_Time_Session_Key, LocalDateTime.now().toString() );
                            
                            //Actualizamos en bd el último inicio de sesión
                            OperatorDAO.updateLastLogin( databaseConnection, tblOperator.getId(), controllerLogger, controllerLanguage );
                            
                            //Redireccionamos hacia el home.zul
                            Executions.sendRedirect( "/views/home/home.zul" );
                            
                        }
                        else {
                           
                            Messagebox.show( "Invalid operator name and/or password" );
                              
                        }
                    }
                    else {
                        
                        Messagebox.show( "Connection failed" );
                        
                    }
                    
                }
                else {
                    
                    Messagebox.show( "Error to read the config file" );
                    
                }
                
            }
            
        }
        catch ( Exception e ) {
            
            if ( controllerLogger != null )   
                controllerLogger.logException( "-1021", e.getMessage(), e );
            
        }
        
    }
    
}
