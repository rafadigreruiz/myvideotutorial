package org.test.zk.dialog;

import org.test.zk.dao.CPerson;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


public class CDialogController extends SelectorComposer<Component> {

    private static final long serialVersionUID = -8977563222707532143L;
    
    protected ListModelList<String> dataModel = new ListModelList<String>();
    
    protected Component callerComponent = null; //Variable de clase de tipo protegida
    
    protected CPerson personToModify = null; //Guarda la persona a ser modificada
    
    protected CPerson personToAdd = null; //Guarda la persona a ser agregada
    
    @Wire
    Window windowPerson;
    
    @Wire
    Label labelId;
    
    @Wire
    Textbox textboxId;
    
    @Wire
    Label labelFirstName;
    
    @Wire
    Textbox textboxFirstName;
    
    @Wire
    Label labelLastName;
    
    @Wire
    Textbox textboxLastName;
    
    @Wire
    Label labelGender;
    
    @Wire
    Selectbox selectboxGender;
    
    @Wire
    Label labelBirdDate;
    
    @Wire
    Datebox dateboxBirdDate;
    
    @Wire
    Label labelComment;
    
    @Wire
    Textbox textboxComment;
    
    //Constructor
    @Override
    public void doAfterCompose( Component comp ) {
        
        try {
            
            super.doAfterCompose( comp );
            
            dateboxBirdDate.setFormat( "dd/MM/yyyy" );
            
            dataModel.add( "Femenino" );
            dataModel.add( "Masculino" );
            
            selectboxGender.setModel(  dataModel );
            selectboxGender.setSelectedIndex( 0 );
            
            dataModel.addToSelection( "Femenino" );
            
            final Execution execution = Executions.getCurrent();
            
            //Recibimos la persona a modificar y la guardamos en la variable de la clase
            personToModify = (CPerson) execution.getArg().get( "personToModify" );
            
            //Cuando se esta creando una nueva persona, no hay personToModify. Es igual a nulo. Debemos verificar esto
            
            if ( personToModify != null ) {
                
                textboxId.setValue( personToModify.getId() );
                textboxFirstName.setValue( personToModify.getFirstName() );
                textboxLastName.setValue( personToModify.getLastName() );
                
                if ( personToModify.getGender() == 0 ) {
                    
                    dataModel.addToSelection( "Femenino" ); //Seleccionamos en el modelo al genero
                    
                }
                else {
                    
                    dataModel.addToSelection( "Masculino" ); //Seleccionamos en el modelo al genero
                    
                }
                
                dateboxBirdDate.setValue( java.sql.Date.valueOf( personToModify.getBirthDate().toString() ) );
                
                textboxComment.setValue( personToModify.getComment() );
                
            }
            
            //Debemos guardar la referencia al componente que nos envia el controlador del manager .zul
            
            callerComponent = (Component) execution.getArg().get( "callerComponent" ); //Usamos un typecast al Component que es el padre de todos los elementos visuales de zk
            
        }
        
        catch ( Exception e ) {
            
            e.printStackTrace();
            
        }
       
     }
    
    @Listen( "onClick=#buttonAccept" )
    public void onClickButtonAceptar( Event event ) {
        
        //Messagebox.show( "ID=" + textboxId.getValue() + " firstName=" + textboxFirstName.getValue() + " LastName=" + textboxLastName.getValue() + " comment=" + textboxComment.getValue(), "Aceptar", Messagebox.OK, Messagebox.INFORMATION );
        
        //System.out.println( "Hello Accept" );
        
        windowPerson.detach();
        
        if ( personToModify != null ) {
            
            personToModify.setId( textboxId.getValue() );
            personToModify.setFirstName( textboxFirstName.getValue() );
            personToModify.setLastName( textboxLastName.getValue() );
            personToModify.setGender( selectboxGender.getSelectedIndex() );
            
            //Los datebox de zk retornan el tipo Date de java y no un String como Textbox normales. Son clases hermanas pero el .getValue() retorna tipos distintos
            //El .getTime() es un metodo de la clase Date de Java
            personToModify.setBirthDate( new java.sql.Date( dateboxBirdDate.getValue().getTime() ).toLocalDate() );
            
            personToModify.setComment( textboxComment.getValue() );
            
            //Lanzamos el evento, retornando la persona a modificar
            Events.echoEvent( new Event ( "onDialogFinished", callerComponent, personToModify ) ); //Suma importancia que los nombres de los eventos coincidan
            
        }
        else {
            
            personToAdd = new CPerson(); //Usamos el constructor sin parametros
            
            //Usamos los métodos setter
            personToAdd.setId( textboxId.getValue() );
            personToAdd.setFirstName( textboxFirstName.getValue() );
            personToAdd.setLastName( textboxLastName.getValue() );
            personToAdd.setGender( selectboxGender.getSelectedIndex() );
            personToAdd.setBirthDate( new java.sql.Date( dateboxBirdDate.getValue().getTime() ).toLocalDate() );
            personToAdd.setComment( textboxComment.getValue() );
            
            //Lanzamos el evento, retornando la persona a agregar
            Events.echoEvent( new Event ( "onDialogFinished", callerComponent, personToAdd ) ); //Suma importancia que los nombres de los eventos coincidan
            
        }
        
    }
    
    @Listen( "onClick=#buttonCancel" )
    public void onClickButtonCancelar( Event event ) {
        
        //Messagebox.show( "Cancelar", "Cancelar", Messagebox.OK, Messagebox.EXCLAMATION );
        
        //System.out.println( "Hello Cancel" );
        
        windowPerson.detach();
        
    }
    
}
