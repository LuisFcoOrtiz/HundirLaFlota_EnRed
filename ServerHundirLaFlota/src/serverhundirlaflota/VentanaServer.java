/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverhundirlaflota;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
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
public class VentanaServer extends javax.swing.JFrame {
    
    //Variables    
    protected int port=6000, port2=6002;    //default port
    //server socket
    protected ServerSocket serverSocket, serverSocket2;
    //data inputstream
    protected DataInputStream dataInput, dataInput2;
    //client socket
    protected Socket clientSocket=null, clientSocket2=null;
    //data outputStream to send client
    protected DataOutputStream outputStream, outputStream2;
    //thread
    protected StartThreadToRecieve startThread;    
    protected SecondThreadToRecieve start2Thread;
    //to play
    protected Partida jugador1, jugador2;    
    protected int[] barcosEscogidos2;    //array of boats
    protected int possitionArray=0, possitionArray2=0;
    //the player is playing
    protected boolean player1Playing = false, player2Playing=false;
    protected Turno turno;
    /**
     * Creates new form VentanaServer
     */
    public VentanaServer() {
        initComponents();
        startThread = new StartThreadToRecieve();
        start2Thread = new SecondThreadToRecieve();
        //class to partida                                
        barcosEscogidos2 = new int[3];        
        //second thread        
        startThread.start();  
        start2Thread.start();
        //turno
        turno = new Turno();
        
    } //
    
    public synchronized void winTheGame() throws InterruptedException {
        //wait();
        if (jugador1.hePerdido()) {
            JOptionPane.showMessageDialog(this, "¡¡Jugador 2 gana la partida!!");
            closeGame();            
        } else if (jugador2.hePerdido()){
            JOptionPane.showMessageDialog(this, "¡¡Jugador 1 gana la partida!!");
            closeGame();            
        }
        //notify();
    }//check who wins
    
    public void closeGame() {
        try {
            serverSocket.close();
            serverSocket2.close();
            clientSocket2.close();
            clientSocket.close();
            startThread=null;
            start2Thread=null;
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(VentanaServer.class.getName()).log(Level.SEVERE, null, ex);
        }                
    }//close all
    
    /*PLAYER 1*/
    class StartThreadToRecieve extends Thread {
        @Override
        public void run() {
            try {                   
                serverSocket = new ServerSocket(port);
                //espera al cliente
                clientSocket = serverSocket.accept();
                //start the player
                jugador1 = new Partida();
                jugador1Label.setText("Jugador 1 conectado");
                player1Playing=true;
                dataInput = new DataInputStream(clientSocket.getInputStream());
                //to send message to client
                outputStream =new DataOutputStream(clientSocket.getOutputStream());                
                //Read from the client                
                while (this.isAlive()) {                                        
                    if (possitionArray<3) {
                        jugador1.cargarBarcos(possitionArray, dataInput.readInt());                    
                        possitionArray++;
                    } else {  
                        synchronized(turno) {
                            if (player2Playing  && turno.isQuienJuega()) {
                                                            
                                if ( jugador2.meDisparan (dataInput.readInt()) ) {
                                    player2Label.append("Tocado\n");
                                    System.out.println("acertaste el disparo al jugador 2"); 
                                    winTheGame();
                                    turno.setQuienJuega(false);
                                    turno.notify();
                                } else {
                                    player2Label.append("Agua\n");
                                    System.out.println("Fallaste el tiro");
                                    turno.setQuienJuega(false);
                                    turno.notify();
                                }  //read the shoot 
                                
                            }//end sync
                        }//player 2 is playing
                        //possitionArray=0;
                    } //restart the possition array
                }                
                //startSocketServer(port);
            } catch (EOFException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(ServerHundirLaFlota.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(VentanaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//
        
        public void setPort(int port) {
            port=port;
        }        
        
        public void sendMessage(String message) throws IOException {
            outputStream.writeUTF(message);
        }                
        
    }//thread
    
    /*PLAYER 2*/    
    class SecondThreadToRecieve extends Thread {
        @Override
        public void run() {
            try {                   
                serverSocket2 = new ServerSocket(port2);
                //espera al cliente
                clientSocket2 = serverSocket2.accept();
                //start player2
                jugador2 = new Partida();
                jugador2Label.setText("Jugador 2 conectado");
                player2Playing=true;    //enable player 2 to play
                dataInput2 = new DataInputStream(clientSocket2.getInputStream());
                //para enviar al cliente 
                outputStream2 =new DataOutputStream(clientSocket2.getOutputStream());                
                //lee del cliente                
                while (this.isAlive()) {                    
                    //load the boats
                    if (possitionArray2<3) {
                        jugador2.cargarBarcos(possitionArray2, dataInput2.readInt());                                        
                        possitionArray2++;                                                                        
                    } else {
                        synchronized(turno) {
                            while (turno.isQuienJuega()) {
                                dataInput2.readInt();
                                turno.wait();
                            }
                            if (player1Playing && turno.isQuienJuega()==false ) {                            
                                
                                if ( jugador1.meDisparan (dataInput2.readInt()) ) {
                                    player1Label.append("Tocado\n");
                                    System.out.println("acertaste el disparo al jugador 1");
                                    winTheGame();
                                    turno.setQuienJuega(true);
                                } else {
                                    player1Label.append("Agua\n");
                                    System.out.println("Fallaste el tiro");
                                    turno.setQuienJuega(true);
                                }  //read the shoot   
                                                                
                                
                            }
                        }//player 2 is playing
                        //possitionArray=0;
                    } //restart the possition array
                }                
                //startSocketServer(port);
            } catch (EOFException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(ServerHundirLaFlota.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(VentanaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//
        
        public void setPort(int port) {
            port=port;
        }        
        
        public void sendMessage(String message) throws IOException {
            outputStream.writeUTF(message);
        }                        
    }//end 2nd thread
    
    //to use wait and notify
    
    public void showTheBoats1() {
        int[] boatArray;        
        boatTextArea.append("Barcos Jugador 1\n");
        boatArray = jugador1.getArrayBarcos();
        for (int i=0; i<3; i++) {
            boatTextArea.append("Posicion: "+boatArray[i]+"\n");                        
        }        
    }//end show the boats
    
    public void showTheBoats2() {        
        int[] boatArray;        
        boatTextArea.append("Barcos Jugador 2\n");
        boatArray = jugador2.getArrayBarcos();
        for (int i=0; i<3; i++) {
            boatTextArea.append("Posicion: "+boatArray[i]+"\n");
        }          
    }//end show the boats
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jugador1Label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        player1Label = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jugador2Label = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        player2Label = new javax.swing.JTextArea();
        none = new javax.swing.JScrollPane();
        boatTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel1.setText("Servidor Hundir la flota");

        jButton1.setText("Mostrar botones");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jugador1Label.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jugador1Label.setText("No conectado");

        player1Label.setEditable(false);
        player1Label.setColumns(20);
        player1Label.setRows(5);
        jScrollPane1.setViewportView(player1Label);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jugador1Label)
                .addGap(96, 96, 96))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jugador1Label)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jugador2Label.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jugador2Label.setText("No conectado");

        player2Label.setEditable(false);
        player2Label.setColumns(20);
        player2Label.setRows(5);
        jScrollPane2.setViewportView(player2Label);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jugador2Label)
                        .addGap(78, 78, 78))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jugador2Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        boatTextArea.setEditable(false);
        boatTextArea.setColumns(20);
        boatTextArea.setRows(5);
        none.setViewportView(boatTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(223, 223, 223)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(none)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(none, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            jugador1.printBarcos();
            jugador2.printBarcos();
            showTheBoats1();
            showTheBoats2();
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "No hay jugadores conectados aun");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaServer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea boatTextArea;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jugador1Label;
    private javax.swing.JLabel jugador2Label;
    private javax.swing.JScrollPane none;
    private javax.swing.JTextArea player1Label;
    private javax.swing.JTextArea player2Label;
    // End of variables declaration//GEN-END:variables
}
