package org.test.zk.manager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.test.zk.constants.SystemConstants;
import org.test.zk.dao.TBLPersonDAO;
import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.CDatabaseConnectionConfig;
import org.test.zk.datamodel.TBLPerson;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

//Clase ManagerController
public class CManagerController extends SelectorComposer<Component> {

    private static final long serialVersionUID = -1591648938821366036L;
    
    protected ListModelList<TBLPerson> dataModel = new ListModelList<TBLPerson>();
    
    public class rendererHelper implements ListitemRenderer<TBLPerson> {
        
        /*public void render( Listitem listitem, Object data, int index ) {
            
            Listcell cell = new Listcell();
            listitem.appendChild( cell );
            
            if ( data instanceof String[] ) {
                cell.appendChild( new Label( ( ( String[] ) data )[ 0 ].toString() ) );
            }
            else if ( data instanceof String ) {
                
                cell.appendChild( new Label( data.toString() ) );
                
            }
            else {
                
                cell.appendChild( new Label( "UNKNOW:" + data.toString() ) );
                
            }
            
        }
*/
        @Override
        public void render( Listitem listitem, TBLPerson person, int intIndex ) throws Exception {
             
             try {
                 
                 Listcell cell = new Listcell();
                 
                 cell.setLabel( person.getId() );
                 
                 listitem.appendChild( cell );
                 
                 cell = new Listcell();
                 
                 cell.setLabel( person.getFirstName() );
                 
                 listitem.appendChild( cell );
                 
                 cell = new Listcell();
                 
                 cell.setLabel( person.getLastName() );
                 
                 listitem.appendChild( cell );
                 
                 cell = new Listcell();
                 
                 cell.setLabel( person.getGender() == 0 ? "Femenino" : "Masculino" );
                 
                 listitem.appendChild( cell );
                 
                 cell = new Listcell();
                 
                 cell.setLabel( person.getBirthDate().toString() );
                 
                 listitem.appendChild( cell );
                 
                 cell = new Listcell();
                 
                 cell.setLabel( person.getComment() );
                 
                 listitem.appendChild( cell );
                 
             }
             catch( Exception ex ) {
                 
                 ex.printStackTrace();
                 
             }
                
        }
        
    }
    
    @Wire
    Listbox listboxPersons;
    
    @Wire
    Button buttonConnectionToDB;
    
    @Wire
    Button buttonRefresh;
    
    @Wire
    Button buttonAdd;
    
    @Wire
    Button buttonModify;
    
    protected CDatabaseConnection databaseConnection = null;
    
    //Constructor
    @Override
    public void doAfterCompose( Component comp ) {
        
        try {
            
            super.doAfterCompose( comp );
            
            /*TBLPerson person01 = new TBLPerson( "1111", "Juan", "Rojas", 1, LocalDate.parse( "1990-01-01" ), "Sin comentarios" );
            TBLPerson person02 = new TBLPerson( "2222", "Jose", "Gonzales", 1, LocalDate.parse( "1960-11-01" ), "Sin comentarios" );
            TBLPerson person03 = new TBLPerson( "3333", "Jose", "Rodriguez", 1, LocalDate.parse( "1970-01-21" ), "Sin comentarios" );
            TBLPerson person04 = new TBLPerson( "4444", "Tom�s", "Moreno", 1, LocalDate.parse( "1982-07-13" ), "Sin comentarios" );
            TBLPerson person05 = new TBLPerson( "5555", "Loly", "G�mez", 0, LocalDate.parse( "1980-01-16" ), "Sin comentarios" );
            
            dataModel.add( person01 );
            dataModel.add( person02 );
            dataModel.add( person03 );
            dataModel.add( person04 );
            dataModel.add( person05 );*/
            
            
            
            //Activa la seleccion multiple de elementos. Util para operacion de borrado de multiples elementos a la vez
            dataModel.setMultiple( true );
            
            listboxPersons.setModel( dataModel );
            
            listboxPersons.setItemRenderer( new rendererHelper() ); //Aqui lo asociamos a listbox
            
            //Verificamos si el usuario esta conectado o no
            
            Session currentSession = Sessions.getCurrent();
            
            if ( currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key ) instanceof CDatabaseConnection ) {
                
                //Recuperamos de la sesion la anterior conexion
                databaseConnection = (CDatabaseConnection) currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key ); //Aqui vamos de nuevo con el typecast, tambien llamado conversion de tipos forzado
                
                buttonConnectionToDB.setLabel( "Disconnect" ); //Indicamos en el boton que estamos conectados y listos para desconectarnos
                
            }
            
        }
        
        catch ( Exception e ) {
            
            e.printStackTrace();
        }
        
    }

    @Listen( "onClick=#buttonConnectionToDB" )
    public void onClickbuttonConnectionToDB( Event event ) {
        
        Session currentSession = Sessions.getCurrent();
        
        //Usamos la conexion como bandera, ya que esta persistida por la sesion
        if ( databaseConnection == null ) { //( buttonConnectionToDB.getLabel().equalsIgnoreCase( "Connect" ) ) {
            
            databaseConnection = new CDatabaseConnection();
            
            CDatabaseConnectionConfig databaseConnectionConfig = new CDatabaseConnectionConfig();
            
            //En esta l�nea obtenemos la ruta completa del archivo de configuraci�n incluido el /config/
            String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator + SystemConstants._Config_Dir + File.separator;
            
            if ( databaseConnectionConfig.loadConfig( strRunningPath + SystemConstants._Database_Connection_Config_File_Name ) ) {
                
                if ( databaseConnection.makeConnectionToDatabase( databaseConnectionConfig ) ) {
                    
                    //Salvamos la conexion a la sesi�n actual del usuario, cada usuario/pesta�a tiene su sesi�n
                    currentSession.setAttribute( SystemConstants._DB_Connection_Session_Key, databaseConnection ); //La sesion no es mas que un arreglo asociativo
                    
                    buttonConnectionToDB.setLabel( "Disconnect" );
                    
                    Messagebox.show( "Conexi�n exitosa" );
                    
                }
                else {
                    
                    Messagebox.show( "Conexi�n fallida" );
                    
                }
                
            }
            else {
                
                Messagebox.show( "Error al leer archivo de configuraci�n" );
                
            }
            
        }
        else {
            
            if ( databaseConnection != null ) {
                
               if ( databaseConnection.closeConnectionToDatabase() ) {
                   
                   databaseConnection = null;
                   
                   buttonConnectionToDB.setLabel( "Connect" );
                   
                   Messagebox.show( "Conexi�n cerrada" );
                   
                   //Borramos la variable de sesi�n
                   //currentSession.setAttribute( _DATABASE_CONNECTION_KEY, null ); //La sesion no es mas que un arreglo asociativo
                   currentSession.removeAttribute( SystemConstants._DB_Connection_Session_Key ); //La sesion no es mas que un arreglo asociativo
                   
               }
               else {
                   
                   Messagebox.show( "Falla al cerrar conexi�n" ); 
                   
               }
                
            }
            else {
                
                Messagebox.show( "�No est�s conectado!" ); 
                
            }
            
        }
        
        //Forzamos que refresque la lista
        Events.echoEvent( new Event ( "onClick", buttonRefresh ) ); //Lanzamos el evento click de zk
        
    }
    
    @Listen( "onClick=#buttonRefresh" )
    public void onClickbuttonRefresh( Event event ) {
        
        //Aqu� vamos a cargar el modelo con la data de la bd
        //Este evento se ejecuta no tan solo con el click del mouse del usuario, si no cuando recibimos onDialogFinished
        listboxPersons.setModel( ( ListModelList<?> ) null ); //Limpiamos el anterior modelo
        
        Session currentSession = Sessions.getCurrent();
        
        if ( currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key ) instanceof CDatabaseConnection ) {
            
            //Recuperamos la conexion a bd de la sesion
            databaseConnection = (CDatabaseConnection) currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key ); //Aqui vamos de nuevo con el typecast, tambien llamado conversion de tipos forzado
            
            //Aqu� vamos a cargar el modelo con la data de la bd
            List<TBLPerson> listData = TBLPersonDAO.searchData( databaseConnection );
            
            //Recreamos el modelo nuevamente
            dataModel = new ListModelList<TBLPerson>( listData ); //Creamos el modelo a partir de la lista que nos retorna la bd
            
            //Activa la seleccion multiple de elementos. Util para operacion de borrado de multiples elementos a la vez
            dataModel.setMultiple( true );
            
            listboxPersons.setModel( dataModel ); //Asignamos el modelo de nuevo
            
            listboxPersons.setItemRenderer( new rendererHelper() ); //Aqui lo asociamos a listbox
            
        }
        
    }
    
    @Listen( "onClick=#buttonAdd" )
    public void onClickbuttonAdd( Event event ) {
        
        //Primero pasamos la referencia el buttonAdd
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "callerComponent", listboxPersons ); //buttonAdd );
        Window win = ( Window ) Executions.createComponents( "/dialog.zul", null, params ); //attach to page as root if parent is null
        
        win.doModal();
    }
    
    @Listen( "onClick=#buttonModify" )
    public void onClickbuttonModify( Event event ) {
        
        Set<TBLPerson> selectedItems = dataModel.getSelection(); 
        
        if ( selectedItems != null && selectedItems.size() > 0 ) {
        
            TBLPerson person = selectedItems.iterator().next(); //El primero de la selecci�n

            Map<String, Object> params = new HashMap<String, Object>();
            
            params.put( "IdPerson", person.getId() );
            params.put( "callerComponent", listboxPersons ); //buttonModify );
            
            Window win = ( Window ) Executions.createComponents( "/dialog.zul", null, params ); //attach to page as root if parent is null
            
            win.doModal();
            
        }
 
        else {
            
            Messagebox.show( "No hay selecci�n" );
            
        }
        
    }

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Listen( "onClick=#buttonDelete" )
    public void onClickbuttonDelete( Event event ) {
        
        Set<TBLPerson> selectedItems = dataModel.getSelection(); 
        
        if ( selectedItems != null && selectedItems.size() > 0 ) {
            
           //Obtenemos el primero de la lista. Es una lista, porque se puede tener seleccion multiple
            
           String strBuffer = null;
           
           for ( TBLPerson person : selectedItems ) {
               
               if ( strBuffer == null ) {
                   
                   strBuffer = person.getId() + " " + person.getFirstName() + " " + person.getLastName(); 
                   
               }
               else {
                   
                   strBuffer += "\n" + person.getId() + " " + person.getFirstName() + " " + person.getLastName();
               
               }
               
           }
           
            Messagebox.show( "�Seguro que desea eliminar los " + Integer.toString( selectedItems.size() ) + " registros?\n" + strBuffer, "Eliminar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
                
                public void onEvent( Event evt ) throws InterruptedException {
                    
                    if ( evt.getName().equals( "onOK" ) ) {
                        
                        //Eliminar los registros seleccionados
                        
                        while ( selectedItems.iterator().hasNext() ) {
                        
                            TBLPerson person = selectedItems.iterator().next();
                            
                            //selectedItems.iterator().remove();
                            
                            TBLPersonDAO.deleteData( databaseConnection, person.getId() );
                            
                            dataModel.remove( person );
                            
                        }
                        
                        //Forzamos que refresque la lista
                        Events.echoEvent( new Event ( "onClick", buttonRefresh ) ); //Lanzamos el evento click de zk
                        
                    }

                }
                
            } );
            
           //Messagebox.show( strBuffer );
            
        }
        else {
            
            Messagebox.show( "No hay selecci�n" );
            
        }
        
    }
    
    @Listen( "onDialogFinished=#listboxPersons" ) //Notifica que se agreg� o modific� una entidad y que debe refrescar la lista, el modelo
    public void onDialogFinishedlistboxPersons( Event event ) {
        
        //En zk es posible lanzar eventos, no tan solo definidos por nosotros mismos sino tambien los estandares de zk como el click
        
        //Forzamos refrescar la lista
        
        Events.echoEvent( new Event ( "onClick", buttonRefresh ) ); //Lanzamos el evento click de zk
    }
    
    /*
    @Listen( "onDialogFinished=#buttonAdd" ) //Solo funciona para cuando se agrega buttonAdd
    public void onDialogFinishedbuttonAdd( Event event ) {
    
       //Este evento recibe el controlador del dialog.zul 
        
        System.out.println( "Evento recibido add" );
        
        //La clase event tiene un metodo .getData()
        
        if ( event.getData() != null ) {
            
            TBLPerson person = (TBLPerson) event.getData(); //Otra vez el typecast
            
            /***System.out.println( person.getId() );
            System.out.println( person.getFirstName() );
            System.out.println( person.getLastName() );
            System.out.println( person.getGender() );
            System.out.println( person.getBirthDate() );*** /
            
            dataModel.add( person ); //Cuando se agrega al modelo un elemento, deberia actualizarse sola la lista
            
            //TBLPersonDAO.insertData( databaseConnection, person );
            
        }
        
    }
    
    @Listen( "onDialogFinished=#buttonModify" ) //Solo funciona para cuando se modifica buttonModify
    public void onDialogFinishedbuttonModify( Event event ) {
    
       //Este evento recibe el controlador del dialog.zul 
        
        System.out.println( "Evento recibido modify" );
        
        //La clase event tiene un metodo .getData()
        
        if ( event.getData() != null ) {
            
            TBLPerson person = (TBLPerson) event.getData(); //Otra vez el typecast
            
            System.out.println( person.getId() );
            System.out.println( person.getFirstName() );
            System.out.println( person.getLastName() );
            System.out.println( person.getGender() );
            System.out.println( person.getBirthDate() );
           
            //Forma 1
            dataModel.notifyChange( person ); //Decirle al modelo que este elemento ha cambiado y que lo modifique en la lista Listbox
            
            //Forma 2
            //dataModel.set( dataModel.indexOf( dataModel.getSelection().iterator().next() ), person );
            
        }
        
        //Forma 3
        //listboxPersons.setModel( ( (ListModelList<?>) null ) ); //El null confunde a eclipse y el tipo a ser usado
        //listboxPersons.setModel( dataModel );
        
    }
    */
    
}
