/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientefx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author AZUS
 */
public class TelaClienteController implements Initializable {
    
    private Label label;
    @FXML
    private TextField txNome;
    @FXML
    private Button btnConectar;
    @FXML
    private Button btnDesconectar;
    @FXML
    private Label lblStatus;
    @FXML
    private TextArea txMensagem;
    @FXML
    private Button btnEnviar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ListView<?> list;
    @FXML
    private TableColumn colNome;
    @FXML
    private TableColumn colIP;
    @FXML
    private TableColumn colPorta;
    @FXML
    private TableView<ClienteTela> tabelaClientes;
    
    private String IP, status;
    private int porta;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNome.setCellValueFactory(new PropertyValueFactory<> ("nome"));
        colIP.setCellValueFactory(new PropertyValueFactory<>("IP"));
        txMensagem.setDisable(true);
        btnEnviar.setDisable(true);
        btnDesconectar.setDisable(true);
        btnCancelar.setDisable(true);
        lblStatus.setText("Desconectado");
    }    
    @FXML
    private void evtConectar(ActionEvent event) {
        this.IP = "127.0.0.1";
        this.porta = 12345;
        
        try
        {
            Socket cliente = new Socket(this.IP, this.porta);
            ClienteTela clienteTela = new ClienteTela(txNome.getText(), this.IP, this.porta, cliente);
            
            // controle de tela
            atualizaTelaConectado();
            status = "Conectado";
            System.out.println("Porta Local:" + cliente.getLocalPort() + " - " + cliente.getPort());
            //envia protocolo ao servidor
            String protocolo = "<#@CONNECT#IPCLIENT="+ this.IP 
                + "#NOME="+ txNome.getText() +""
                + "#PORTA="+ this.porta +">";
            
            output = new ObjectOutputStream(cliente.getOutputStream());
            output.writeObject(protocolo);
            
            // teste
            System.out.println("Novo Cliente");
            System.out.println("Protocolo Cliente: " + protocolo);
            
            // thread de comunicação com servidor
            ListnerConectarCliente threadClienteS = new ListnerConectarCliente(cliente, status, this);
            Thread newThrdCS = new Thread(threadClienteS);
            newThrdCS.start();
            
//            if(newThrd.isAlive())
//            {
//                System.out.println("Is alive");
//            }
            //cliente.close();
            
            // Thread para receber mensagens do servidor
//            Listner r = new Listner(cliente.getInputStream());
//            Thread newThread = new Thread(th);
//            newThread.start();
            
        }catch(IOException e){
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(e.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private void evtDesconectar(ActionEvent event) {
        try 
        {
            String protocoloDisconnect = "<#@DISCONNECT#IPCLIENT="+ this.IP +">";
            
            // Cliente envia protocolo para se desconectar do servidor
            output.writeObject(protocoloDisconnect);
            output.flush(); 
            
            // antes de desconectar, acho que ele recebe mensagem do servidor
            output.close(); 
            input.close(); 
            
            atualizaTelaDesconectado();
            
        }catch(IOException e){
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(e.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private void evtEnviar(ActionEvent event) {
        try{
            // Cliente envia mensagem a outro cliente
            String protocoloSend = "<#@MSG#IPORIGEM="+ this.IP + "#TXT="+ txMensagem.getText() +">";
            output.writeObject(protocoloSend);
            output.flush();
            
        }catch(IOException e){
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(e.getMessage());
            a.showAndWait();
        }
        
        txMensagem.clear();
    }
    
    @FXML
    private void evtCancelar(ActionEvent event) {
    }
    
    public void atualizaTelaConectado(){
        btnConectar.setDisable(true);
        btnDesconectar.setDisable(false);
        btnEnviar.setDisable(false);
        txMensagem.setDisable(false);
        lblStatus.setText("Conectado");
    }
    
    public void atualizaTelaDesconectado(){
        btnConectar.setDisable(false);
        btnDesconectar.setDisable(true);
        btnEnviar.setDisable(true);
        txMensagem.setDisable(true);
        lblStatus.setText("Desconectado");
    }
    
    public TableView getTabelaClientesOnline()
    {  
        return tabelaClientes;
    }
}
