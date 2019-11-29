
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Restaurante {

    private int CAPACIDAD;
    private String nombre;
    private int cantAct;
    private ArrayBlockingQueue<Visitante> colaVisitantes;
    private Reloj reloj;
    private Lock cerrojo = new ReentrantLock();
    private Condition serAtendido = cerrojo.newCondition();
    private Condition entrar = cerrojo.newCondition();
    private Condition salir = cerrojo.newCondition();

    private boolean comio;
    private int contVisitantes;

    public Restaurante(String nombre, int cap, Reloj r, ArrayBlockingQueue<Visitante> cola) {
        this.nombre = nombre;
        cantAct = 0;
        this.CAPACIDAD = cap;
        this.colaVisitantes = cola;
        this.reloj = r;
        this.contVisitantes = 0;

    }

    public void visitarRestaurante(Visitante v) throws InterruptedException {
        reloj.dormirReloj(5000);

        if (!v.tomoAlmuerzo()) {
            entrar(v);
            Thread.sleep(1000);
            hacerFila(v);
            Thread.sleep(1000);

            recibirAlmuerzo(v);

        } else if (!v.tomoMerienda()) {
            entrar(v);
            Thread.sleep(1000);
            hacerFila(v);
            Thread.sleep(1000);
            recibirMerienda(v);
        }
        Thread.sleep(1000);

        comer(v);
        salir(v);
    }

    public void hacerFila(Visitante v) throws InterruptedException {
        System.out.println(v.getID() + " esperando a ser atendido...");
        this.colaVisitantes.put(v);
        Thread.sleep(1000);
    }

    public void entrar(Visitante v) throws InterruptedException {
        cerrojo.lock();

        try {
            while (this.contVisitantes >= CAPACIDAD) {
                entrar.await();
            }
            this.contVisitantes++;

            System.out.println(v.getID() + " entro al restaurante " + this.nombre);
        } finally {
            cerrojo.unlock();
        }

    }

    public void recibirMerienda(Visitante v) throws InterruptedException {
        cerrojo.lock();
        try {
            while (!v.tomoMerienda()) {
                                

                serAtendido.await();
            }
            System.out.println(v.getID() + " recibe su merienda");

        } finally {
            cerrojo.unlock();
        }
    }

    public String getNOmbre() {
        return this.nombre;
    }

    

    public void recibirAlmuerzo(Visitante v) throws InterruptedException {
        cerrojo.lock();
        try {
            while (!v.tomoAlmuerzo()) {
                serAtendido.await();
            }
            System.out.println(v.getID() + " recibe su almuerzo");
            
        } finally {
            cerrojo.unlock();
        }
    }

    public void informarFueraHorario(Visitante v) throws InterruptedException {
        if (!v.tomoAlmuerzo()) {
            System.out.println(v.getID() + " llego tarde para el almuerzo. ");
        } else if (!v.tomoMerienda()) {
            System.out.println(v.getID() + " llego temprano para tomar la merienda. ");
        }
    }

    private void salir(Visitante v) {
        cerrojo.lock();
        try {
             System.out.println(v.getID() + " sale del restaurante.");
            this.contVisitantes--;
            entrar.signalAll();
        
        } finally {
            cerrojo.unlock();
        }
    }

    void darAlmuerzo(Visitante v) {
        cerrojo.lock();
        try {
            v.setTomoAlmuerzo(true);
            System.out.println("dar almuerzo "+v.tomoAlmuerzo());
            serAtendido.signalAll();
        } finally {
            cerrojo.unlock();
        }

    }

    void darMerienda(Visitante v) {
        cerrojo.lock();
        try {
            v.setTomoMerienda(true);
            serAtendido.signalAll();
        } finally {
            cerrojo.unlock();
        }

    }

    private void comer(Visitante v) throws InterruptedException {
        System.out.println(v.getID() + " esta comiendo...");

        Thread.sleep(1000);
    }

}
