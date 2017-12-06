/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverhundirlaflota;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author manrique
 */
public class ServerHundirLaFlota {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        VentanaServer ventana = new VentanaServer();
        ventana.setVisible(true);
    }        
    
    

}//end class
