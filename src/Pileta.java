import java.util.concurrent.*;

public class Pileta {

    private final int CANT_POR_PILETA;
    private String id;
    private Delfin[] delfines;
    private int tiempoDeNado;
    private int contPersonas;
    private CyclicBarrier esperarAtodos;
    private CountDownLatch nadando;


    public Pileta(String id, int cantPersonas) {
        this.CANT_POR_PILETA = cantPersonas;
        this.id = id;
        this.delfines = new Delfin[2];
        this.tiempoDeNado = 45; //System.currentTimeMillis() + 270000;
        this.contPersonas = 0;
        this.nadando = new CountDownLatch(tiempoDeNado);
        this.esperarAtodos = new CyclicBarrier(CANT_POR_PILETA);

    }
    public int getCapacidad() {
        return this.CANT_POR_PILETA;
    }

    public synchronized boolean estaLlena() {
        return this.contPersonas == CANT_POR_PILETA;
    }


    public String getId() { return this.id; }





}
