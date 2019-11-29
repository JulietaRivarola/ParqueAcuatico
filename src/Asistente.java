import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class Asistente implements Runnable {
    private final Reloj reloj;
    private ArrayBlockingQueue<EquipoSnorkel> zonaDeEquipos;
    ArrayBlockingQueue<Visitante> colaSnorkel;
    private EquipoSnorkel[] equipoSnorkels;


    public Asistente(ArrayBlockingQueue<EquipoSnorkel> zona, ArrayBlockingQueue<Visitante> cola, Reloj r, EquipoSnorkel[] e) {
        this.zonaDeEquipos = zona;
        this.reloj = r;
        this.colaSnorkel = cola;
        this.equipoSnorkels = e;
    }

    @Override
    public void run() {
        //reloj.getHora()>=9 && reloj.getHora()<18
        while (true) {
            try {
                entregarSnorkel();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void entregarSnorkel() throws InterruptedException {
        Visitante v = colaSnorkel.poll(2000, TimeUnit.MILLISECONDS);
        if (v != null) {
            EquipoSnorkel equipo = new EquipoSnorkel();
            zonaDeEquipos.put(equipo);
            Thread.sleep(1000);
        }
    }
}
