/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorfx;

import java.io.InputStream;

/**
 *
 * @author AZUS
 */
public class ListnerMensagemCliente implements Runnable {
    
    private InputStream mensagem;
    private TelaServidorController servidor;
    
    public ListnerMensagemCliente(InputStream mensagem, TelaServidorController servidor) {
      this.mensagem = mensagem;
      this.servidor = servidor;
    }
    
    public void run(){
        // quando chega uma mensagem, distribui para todos os clientes
        // quando chegar uma msg, distribui pra todos
//        Scanner s = new Scanner(this.cliente);
//        while (s.hasNextLine()) {
//          servidor.distribuiMensagem(s.nextLine());
//        }
//        s.close();
    }
    
}
