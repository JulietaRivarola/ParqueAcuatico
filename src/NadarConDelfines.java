import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class NadarConDelfines {
    private Pileta[] piletas;
    private Reloj reloj;


    private int CAPACIDAD_PILETA;
    private int CANT_PILETAS;
    private int contPiletas;
    private int turno;
    private int contador;
    private int contNadando;
    private boolean inicio;
    private boolean inicioActividad;
    private Semaphore administrador;
    private int contPersonas;
    private boolean tiempoEspera;
    private boolean formandoGrupo;
    private boolean incompleto = false;

    public NadarConDelfines(Pileta[] piletas, Reloj reloj) {
        this.CANT_PILETAS = 4;
        this.contPiletas = 0;
        this.contNadando = 0;
        this.administrador = new Semaphore(0);
        this.contador = 0;
        this.tiempoEspera = true;
        this.formandoGrupo = true;
        this.inicio = true;
        this.turno=1;

    }



    public void nadarConDelfines(Visitante v) throws InterruptedException {
        determinarHorarios(v);
        System.out.println(v.getID()+" ingresa a nadar con delfines");
        verificarCantidad(v);

    }

    private synchronized void determinarHorarios(Visitante v) throws InterruptedException {
        while (v.gethorarioPileta() != this.turno && v.gethorarioPileta() <=3) {
            wait();
        }
        System.out.println(v.getID() + " tiene el turno " + v.gethorarioPileta() + " y es el turno " + turno);
    }

    public synchronized void verificarCantidad(Visitante v) throws InterruptedException {
        if (this.contador < 40 && (this.inicio)) {
            this.contador++;
            if (this.contador == 1) {
                this.administrador.release();
            }
            if (this.contador % 10 == 0) {
                System.out.println(" Se completo una pileta");
                this.contPiletas++;
            }
            while (this.formandoGrupo) {
                this.wait();
            }

            this.contador--;
            if (this.inicioActividad) {
                if (this.contador == 0) {
                    this.inicioActividad = false;
                    System.out.println("FINALIZO El TURNO");
                }
            } else {
                if (this.contador == 0) {
                    System.out.println("No hubo suficientes personas para ingresar a las piletas");
                }
            }
        }
    }

    public void habilitarAdministrador() throws InterruptedException {
        this.administrador.acquire();
        System.out.println("--Inicio la actividad Nado con Delfines--");

    }

    public synchronized void verificar() {
        this.inicio = false;
        if (this.contador > 30) {
            this.inicioActividad = true;
        } else {
            this.inicioActividad = false;
            this.formandoGrupo = false;
            this.incompleto = true;
            this.notifyAll();

        }

    }


    public synchronized void finalizar() {
        if (!this.incompleto) {
            this.formandoGrupo = false;
            this.inicioActividad = true;
            this.notifyAll();
        } else {
            this.incompleto = false;
        }
    }

    public synchronized void actualizar() {
        this.inicio = true;
        this.formandoGrupo = true;
        this.contPiletas = 0;
        this.tiempoEspera = true;
        this.inicioActividad=false;
       // administrador.release();
        this.contador=0;
        turno++;
        this.notifyAll();
    }

}



