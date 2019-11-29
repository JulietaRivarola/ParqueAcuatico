
import java.util.Random;

import java.util.concurrent.Semaphore;

public class Parque {


    private final int CANT_CAJAS;
    private int molinetes;
    private Semaphore pulsera;
    private Random rnd;
    private CarreraGomones carrera;
    private Tren tren;
    private Restaurante[] restaurante;
    private FaroMirador faro;
    private Reloj reloj;
    private NadarConDelfines nadoConDelfines;
    private Snorkel snorkel;


    public Parque(int cantVisitantes, int molinetes, int cajas,
                  Tren t, Bolso[] b, Gomon[] g, Reloj reloj, Restaurante[] r, FaroMirador f, NadarConDelfines nado, Snorkel s) {

        this.molinetes = molinetes;
        this.pulsera = new Semaphore(0, true);
        this.rnd = new Random();
        this.CANT_CAJAS = cajas;
        this.carrera = new CarreraGomones(g, b, reloj);
        this.restaurante = r;
        this.tren = t;
        this.faro = f;
        this.reloj = reloj;
        this.nadoConDelfines = nado;
        this.snorkel = s;
    }

    public void arribar(Visitante v) throws InterruptedException {
        if (v.enParque()) {
            pulsera.release();
            System.out.println(v.getID() + " recibe pulsera.");
        }

    }

    public void entrar(Visitante v) throws InterruptedException {
        reloj.dormirReloj(1000);
        boolean puedenEntrar = pulsera.tryAcquire();
        int nMolinete = rnd.nextInt(this.molinetes);
        if (puedenEntrar) {
            System.out.println(v.getID() + " ingresa al parque por el molinete " + nMolinete);

            if (rnd.nextBoolean()) {
                System.out.println(v.getID() + " visita el SHOP");
                irShop(v);
            } else {
               // System.out.println(v.getID() + " opta por ACTIVIDADES");
                hacerActividades(v);
            }
        }

    }

    private void irShop(Visitante v) throws InterruptedException {
        reloj.dormirReloj(1000);
        System.out.println(v.getID() + " adquiere souvenirs");
        int caja = rnd.nextInt(this.CANT_CAJAS);
        System.out.println(v.getID() + " abona en la caja " + caja);
        Thread.sleep(1000);
    }

    public void hacerActividades(Visitante v) {
        try {
            if (reloj.getHora() < 18) {
               irCarreraGomones(v);
               // carrera.participarEnCarrera(v);
                //restaurante[rnd.nextInt(restaurante.length)].visitarRestaurante(v);
                //faro.visitarFaro(v);
                //restaurante[rnd.nextInt(restaurante.length)].visitarRestaurante(v);
               // snorkel.irASnorkel(v);
                //nadoConDelfines.nadarConDelfines(v);
            }

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }


    }

    public CarreraGomones getCarrera() {
        return this.carrera;
    }

    private void irCarreraGomones(Visitante v) throws InterruptedException {
        reloj.dormirReloj(1000);
        if (rnd.nextBoolean() && tren.hayLugar()) {
            System.out.println(v.getID() + " va a la CARRERA en TREN");

            Thread.sleep(1000);
        } else {
            System.out.println(v.getID() + " va a la CARRERA en BICICLETA.");
            Thread.sleep(1000);
        }

    }


}
