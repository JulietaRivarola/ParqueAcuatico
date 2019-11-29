
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Caja implements Runnable {

    private ArrayBlockingQueue<Visitante> colaVisitantes;
    Visitante comensal;
    Reloj reloj;
    private Restaurante restaurante;

    public Caja(ArrayBlockingQueue<Visitante> cola, Reloj r, Restaurante resto) {
        this.colaVisitantes = cola;
        this.reloj = r;
        this.restaurante = resto;
    }

    @Override
    public void run() {
        while (true) {

            try {
                comensal = colaVisitantes.take();
                System.out.println(comensal.getID() + " es atendido en el restaurante " + restaurante.getNOmbre());
                if (!comensal.tomoAlmuerzo()) {
                 
                    restaurante.darAlmuerzo(comensal);

                } else if (!comensal.tomoMerienda()) {
                    restaurante.darMerienda(comensal);

                } else {
                    restaurante.informarFueraHorario(comensal);
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(Caja.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
