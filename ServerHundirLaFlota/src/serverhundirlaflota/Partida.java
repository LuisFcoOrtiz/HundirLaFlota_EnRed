/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverhundirlaflota;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manrique
 */
public class Partida {
    
    protected String nameJugador;
    protected int[] barcosEscogidos;    //array of boats
    
    public Partida() {
        barcosEscogidos = new int[3];
    }//end constructor
    
    public synchronized  void cargarBarcos(int posicionArray, int posicionDeBarco) {
        barcosEscogidos[posicionArray]=posicionDeBarco;
    }//Load the boats synchronized method
    
    public synchronized boolean meDisparan(int posicionDeDisparo) throws InterruptedException {                 
        for (int i=0; i<3; i++) {
            if (barcosEscogidos[i]==posicionDeDisparo) {
                barcosEscogidos[i]=0;   //delete possition                
                return true;
            }
        }//loop in all array                
        return false;
        
    }
    
    public synchronized boolean hePerdido(){
        int contador=0;
        for (int i=0; i<3; i++) {
            if (barcosEscogidos[i]==0) {
                contador++;
            }
        }
        if (contador==3) {
            return true;           
        } else {
            return false;   
        }
    }//check the game
    
    public void printBarcos() {
        for (int i=0; i<3; i++) {
            System.out.println(barcosEscogidos[i]);
        }
    }//print in console the boats
    
    public int[] getArrayBarcos() {
        return barcosEscogidos;
    }//get the whole list
    
}//end class
