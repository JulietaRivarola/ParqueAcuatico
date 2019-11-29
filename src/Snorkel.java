import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Snorkel {
    //private SynchronousQueue<EquipoSnorkel> stand;
    private ArrayBlockingQueue<EquipoSnorkel> zonaDeEquipos;

    ArrayBlockingQueue<Visitante> colaSnorkel;
    private ReentrantLock cerrojo=new ReentrantLock();


    public Snorkel( ArrayBlockingQueue<EquipoSnorkel> zona,  ArrayBlockingQueue<Visitante> cola) {
        this.zonaDeEquipos=zona;
        this.colaSnorkel=cola;

    }


    public void irASnorkel(Visitante v) throws InterruptedException {
        System.out.println(v.getID()+" se pone en la cola del stand");
        colaSnorkel.offer(v);
        EquipoSnorkel equipo=adquirirEquipo(v);
        boolean realizoAct=false;
        if(equipo!=null) {
            empezarActividad(v);
            devolverEquipo(v, equipo);
            realizoAct=true;
        }
        salir(v, realizoAct);
    }



    private void devolverEquipo(Visitante v, EquipoSnorkel equipo) throws InterruptedException {
        zonaDeEquipos.put(equipo);
        System.out.println(v.getID()+" devuelve el equipamiento.");

    }

    private void salir(Visitante v, Boolean realizoAct) {
        if(realizoAct) {
            System.out.println(v.getID()+" se retira de la actividad.");
        }else{
            System.out.println(v.getID()+" por falta de equipamiento no pudo realizar Snorkel y se va.");
        }
    }

    private void empezarActividad(Visitante v) throws InterruptedException {
        System.out.println(v.getID()+" realiza Snorkel");
        Thread.sleep(2000);
    }

    private EquipoSnorkel adquirirEquipo(Visitante v) throws InterruptedException {

        cerrojo.lock();
        EquipoSnorkel equipo = zonaDeEquipos.poll(1000, TimeUnit.MILLISECONDS );
        cerrojo.unlock();

        if(equipo!=null) {
            System.out.println(v.getID() + " toma el equipo de Snorkel, salvavidas y patas de ranas del stand.");

        }
        return equipo;
    }

}
