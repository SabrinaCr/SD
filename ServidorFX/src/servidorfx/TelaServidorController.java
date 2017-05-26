/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorfx;

import cliente.ClienteTela;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author AZUS
 */
public class TelaServidorController implements Initializable {
    
    private Label label;
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnEncerrar;
    @FXML
    private Button btnSair;
    @FXML
    private TableView<MensagemTela> tabela;
    @FXML
    private TableColumn colNome;
    @FXML
    private TableColumn colMensagem;
    @FXML
    private TableView<ClienteTela> tabelaOnline;
    @FXML
    private TableColumn colIP;
    @FXML
    private TableColumn colPorta;
    @FXML
    private TableColumn colNomeOn;
    @FXML
    private Label lblStatus;
    
    private int porta;
    boolean ativo;
    
    Thread novaThread;
    ServerSocket servidor;
    ListnerNovoCliente listnerNovoCliente;
    ArrayList<ClienteTela> clientes = new ArrayList<ClienteTela>(); // qualquer coisa muda pra cliente
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        colMensagem.setCellValueFactory(new PropertyValueFactory<>("mensagem"));
        colNome.setCellValueFactory(new PropertyValueFactory<> ("nome"));
        colNomeOn.setCellValueFactory(new PropertyValueFactory<> ("nome"));
        colPorta.setCellValueFactory(new PropertyValueFactory<> ("porta"));
        colIP.setCellValueFactory(new PropertyValueFactory<>("IP"));
    }    

    @FXML
    private void evtIniciar(ActionEvent event) {
        try 
        {
            porta = 12345;
            this.servidor = new ServerSocket(12345, 100);
            lblStatus.setText("Servidor em " + servidor.getLocalPort());
            //listnerNovoCliente = new ListnerNovoCliente(this, servidor);
            
            System.out.println("Porta do servidor: " + servidor.getLocalPort());
            
            this.ativo = true;
            
            // novos clientes
            ListnerNovoCliente th = new ListnerNovoCliente(this, this.servidor);
            Thread newThrd = new Thread(th);
            newThrd.start();

            btnIniciar.setDisable(true);
            btnEncerrar.setDisable(false);
            lblStatus.setText("Servidor em " + servidor.getLocalPort());
            
            
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        } 
    }

    @FXML
    private void evtEncerrar(ActionEvent event) {
        try {
            servidor.close();
            novaThread.interrupt();
            
            ativo = false;
            
            btnIniciar.setDisable(false);
            btnEncerrar.setDisable(true);
            
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private void evtSair(ActionEvent event) {
        if(this.servidor != null) 
            evtEncerrar(event);
        Platform.exit();
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public TableView getTabelaMensagens()
    {  
        return tabela;
    }
    
    public TableView getTabelaConectados()
    {  
        return tabelaOnline;
    }

    public void setTabelaConectados(ArrayList<ClienteTela> clientes)
    {
        ObservableList<ClienteTela> oListStavaka = FXCollections.observableArrayList(clientes);
        tabelaOnline.getItems().clear();
        tabelaOnline.setItems(oListStavaka);
    }
    
    public void distribuiMensagem(String msg) {
      // envia msg para todo mundo
      // atualiza listde mensagens de cada tela dos clientes
//      for (PrintStream cliente : this.clientes) {
//        cliente.println(msg);
//      }
    }
}
