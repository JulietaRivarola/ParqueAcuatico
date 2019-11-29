
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdministradorDeCola implements Runnable {
    
   private Tobogan[] toboganes;
    private SynchronousQueue colaTobogan1;
    private SynchronousQueue colaTobogan2;

    private ArrayBlockingQueue<Visitante> colaEscalera;
    private Visitante visitante;
    private Random rnd;
    private Reloj reloj;
    
    AdministradorDeCola(Tobogan[] t, ArrayBlockingQueue<Visitante> colaE,SynchronousQueue colaT1,SynchronousQueue colaT2, Reloj r) {
        this.toboganes = t;
        this.reloj = r;
        this.colaEscalera = colaE;
        this.rnd = new Random();
        this.colaTobogan1=colaT1;
        this.colaTobogan2=colaT2;

    }

    
    @Override
    public void run() {
        while (true) {
            try {
                    visitante = colaEscalera.take();
                    System.out.println("El siguiente en descender por el tobogan es " + visitante.getID());
                    if(visitante.getTobogan()==0) {
                        colaTobogan1.put(visitante);
                    }else{
                        colaTobogan2.put(visitante);
                    }
                
            } catch (InterruptedException ex) {
                Logger.getLogger(AdministradorDeCola.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
