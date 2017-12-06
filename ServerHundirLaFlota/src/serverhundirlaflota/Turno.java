/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverhundirlaflota;

/**
 *
 * @author manrique
 */
public class Turno {
    protected boolean quienJuega;
    
    public Turno() {
        quienJuega=true;
    }
    public boolean isQuienJuega() {
        return quienJuega;      //true= jugador1 | false=jugador2
    }

    public void setQuienJuega(boolean quienJuega) {
        this.quienJuega = quienJuega;
    }
    
    
}//end class
