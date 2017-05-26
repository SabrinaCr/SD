/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientefx;

import java.io.InputStream;
import java.util.Scanner;
import javafx.scene.control.Alert;

/**
 *
 * @author AZUS
 */
public class Listner implements Runnable{
    private InputStream servidor;
 
    public Listner(InputStream servidor) {
      this.servidor = servidor;
    }

    public void run() {
        // recebe msgs do servidor e imprime na tela
        Scanner s = new Scanner(this.servidor);
        String ss = ""; // teste
        while (s.hasNextLine()) {
            ss += s.nextLine();
        }
        
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(ss);
        a.showAndWait();
    }
    
}
