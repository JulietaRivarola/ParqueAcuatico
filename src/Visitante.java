
import java.util.Random;

public class Visitante implements Runnable {

    private Reloj reloj;
    private String id;
    private Parque parque;
    private boolean visitanteLlego;
    Colectivo colectivo;
    private boolean entro;
    private boolean almorzo, merendo;
    private int tobogan;
    private int HORARIO_PILETA;
    Random rnd=new Random();

    public Visitante(String id, Parque parque, Colectivo cole, int horaP, Reloj r, Tobogan[] toboganes) {
        this.id = id;
        this.parque = parque;
        this.colectivo = cole;
        this.visitanteLlego = false;
        this.reloj = r;
        this.entro = false;
        this.almorzo = false;
        this.merendo = false;
        this.HORARIO_PILETA = horaP;
        this.tobogan=rnd.nextInt(toboganes.length);


    }

    public void run() {

        while (!this.entro) {
            try {

                if (this.reloj.estadoParque()) {
                    entro = true;
                    irAParque(this);
                    boolean arribo = false;
                    while (!arribo) {
                        if (colectivo.arribo()) {
                            visitanteLlego = true;
                            parque.arribar(this);
                            parque.entrar(this);
                            arribo = true;
                        }
                    }

                } else {

                    Thread.sleep(3000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private synchronized void irAParque(Visitante v) throws InterruptedException {
        if (colectivo.hayLugar()) {
            colectivo.esperarColectivo(this);
            System.out.println(v.getID() + " espera el colectivo... visitara el parque por TOUR.");
        } else {
            System.out.println(v.getID() + " visita el parque PARTICULARMENTE.");

        }

    }

    public String getID() {
        return this.id;
    }

    public boolean enParque() {
        return this.visitanteLlego;
    }

    boolean tomoMerienda() {
        return this.merendo;
    }

    boolean tomoAlmuerzo() {
        return this.almorzo;
    }

    void setTomoAlmuerzo(boolean a) {
        this.almorzo = a;
    }

    void setTomoMerienda(boolean m) {
        this.merendo = m;
    }



    public int getTobogan() {

        return this.tobogan;
    }

    public int gethorarioPileta() {
      return this.HORARIO_PILETA;
        
    }

}
