/* Carrera de gomones por el río : los visitantes descienden por el río compitiendo entre ellos.
1. llegar hasta el inicio de la actividad:
En bicicletas que se prestan en un stand de bicicletas,
En un tren interno que tiene una capacidad de 15 personas como máximo.
2.  Al llegar dispondrá de un bolso con llave, en donde podrá guardar todas las pertenencias que no quiera mojar.
Los bolsos están identificados con un número al igual que la llave, al final del recorrido podrán ser retirados por el visitante.
3. Para bajar se utilizan gomones, individuales o con capacidad para 2 personas. La cantidad de gomones de cada tipo es limitada.
Para habilitar una largada es necesario que haya h gomones listos para salir, no importa el tipo.

*/


import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

public class CarreraGomones {

    private int MINIMOS_CARRERA = 10;
    private int contCompetidores;
    private int contBolsos;
    private ArrayList<Bolso> listaBolso = new ArrayList();
    private boolean llevandoBolsos;
    private boolean bolsosListo;
    private Random rnd;
    private CyclicBarrier inicioCompetencia;


    private boolean terminoCarrera;
    private Bolso[] bolso;
    private Gomon[] gomones;
    private Reloj reloj;
    private int enCompetencia;
    private boolean compitiendo;
    private Gomon gomonGanador;
    private CyclicBarrier largada;


    public CarreraGomones(Gomon[] g, Bolso[] b, Reloj r) {
        this.reloj = r;
        this.contCompetidores = 0;
        this.contBolsos = 0;
        this.llevandoBolsos = false;
        this.bolsosListo = false;
        this.rnd = new Random();
        this.inicioCompetencia = new CyclicBarrier(MINIMOS_CARRERA);

        this.terminoCarrera = false;
        this.bolso = b;
        this.gomones = g;
        this.compitiendo = false;
        this.enCompetencia = 0;
        this.gomonGanador = null;

    }


    public void participarEnCarrera(Visitante v) throws InterruptedException {
        Bolso bolso = obtenerBolso();
        entrarCarrera(v);
        guardarBolso(v, bolso);
        reloj.dormirReloj(7000);
        Gomon gomon = tomarGomon(v);
        reloj.dormirReloj(7000);
        prepararse(v, gomon);
        reloj.dormirReloj(10000);
        competir(v);
        reloj.dormirReloj(7000);
        terminarCarrera(v, gomon);
        reloj.dormirReloj(7000);
        buscarBolso(v, bolso);
    }


    private synchronized Bolso obtenerBolso() {
        int numBolso = this.contBolsos;
        listaBolso.add(numBolso, bolso[numBolso]);
        this.contBolsos++;
        return listaBolso.get(numBolso);

    }

    private synchronized void entrarCarrera(Visitante v) throws InterruptedException {

        this.contCompetidores++;
        System.out.println(v.getID() + " llego al inicio de la carrera");

        //Thread.sleep(1000);
    }

    private void esperarAtodos(Visitante v) throws InterruptedException {
        while (this.contCompetidores < this.contBolsos) {
            wait();
        }

    }

    private synchronized void guardarBolso(Visitante v, Bolso bolso) throws InterruptedException {


        System.out.println(v.getID() + " guarda pertenencias en bolso " + bolso.getIdBolso());
        if (contBolsos == contCompetidores) {
            this.bolsosListo = true;
        }
        // Thread.sleep(1000);
        esperarAtodos(v);

    }

    public synchronized void llevarBolsos() throws InterruptedException {

        while (!this.bolsosListo) {
            ;
            wait();
        }
        System.out.println("La camioneta llevo los bolsos");
        this.llevandoBolsos = true;
    }


    public void camionetaEsperar() throws InterruptedException {
        Thread.sleep(10000);
    }


    private Gomon tomarGomon(Visitante v) throws InterruptedException {
        boolean tomoGomon = false;
        Gomon gomon = null;
        while (!tomoGomon) {
            gomon = gomones[rnd.nextInt(gomones.length)];
            if (!gomon.estaLleno()) {
                if (gomon.getCapacidad() == 2) {
                    gomon.tomar();
                    if (gomon.estaLleno()) {
                        System.out.println(v.getID() + " tomo gomon " + gomon.getId() + " con capacidad 2");
                        tomoGomon = true;
                        gomon.setCompañero(v);
                        esperarCarrera(v, gomon);

                    } else {
                        Thread.sleep(5000);
                        reloj.dormirReloj(5000);
                        if (gomon.estaLleno()) {
                            System.out.println(v.getID() + " tomo gomon " + gomon.getId() + " con capacidad 2");
                            tomoGomon = true;
                            gomon.setCompañero(v);
                            esperarCarrera(v, gomon);
                        } else {
                            gomon.dejar();
                        }
                    }

                } else {

                    gomon.tomar();
                    tomoGomon = true;
                    System.out.println(v.getID() + " tomo gomon " + gomon.getId() + " con capacidad 1");

                    esperarCarrera(v, gomon);
                }
            }
        }
        return gomon;


    }

    public synchronized void esperarCarrera(Visitante v, Gomon gomon) throws InterruptedException {
        while (this.compitiendo || this.enCompetencia >= MINIMOS_CARRERA) {
            wait();
        }

        this.enCompetencia++;

        notifyAll();

    }

    private void prepararse(Visitante v, Gomon gomon) {
        try {
            reloj.dormirReloj(10000);
            inicioCompetencia.await(10000, TimeUnit.MILLISECONDS);

        } catch (InterruptedException e) {
            //e.printStackTrace();
        } catch (BrokenBarrierException e) {
            //e.printStackTrace();
        } catch (TimeoutException e) {
            inicioCompetencia.reset();
        } finally {
            synchronized (this) {
                if (largada == null) {
                    System.out.println("COMENZO UNA CARRERA");
                    reloj.dormirReloj(10000);

                    largada = new CyclicBarrier(this.enCompetencia);
                    this.enCompetencia = 0;
                    compitiendo = true;


                }
                notifyAll();
            }
        }
    }


    public void competir(Visitante v) {

        try {
            reloj.dormirReloj(10000);
            largada.await();
        } catch (InterruptedException e) {
        } catch (BrokenBarrierException e) {
        }
        synchronized (this) {
            this.enCompetencia++;
            if (enCompetencia == 1) {
                largada = null;
            }
            notifyAll();
        }

        System.out.println(v.getID() + " corriendo");

    }

    public synchronized void terminarCarrera(Visitante v, Gomon gomon) {
        if (gomon.getCapacidad() == 2) {
            if (!gomon.getLlego()) {
                gomon.setLlego(true);
            } else {


                if (gomonGanador == null) {
                    System.out.println(v.getID() + " y " + gomon.getCompañero(v).getID() + " GANARON");
                    gomonGanador = gomon;
                } else {
                    System.out.println(v.getID() + " y " + gomon.getCompañero(v).getID() + " llegaron a la final");
                }

                gomon.dejar();
                enCompetencia--;

                if (enCompetencia == 0) {
                    System.out.println("LA CARRERA TERMINO");
                    this.gomonGanador = null;
                    this.compitiendo = false;
                    inicioCompetencia.reset();
                }
                notifyAll();
            }
        } else {
            if (gomonGanador == null) {
                System.out.println(v.getID() + " GANO");
                gomonGanador = gomon;
            } else {
                System.out.println(v.getID() + " llegó al final");
            }
            gomon.dejar();
            enCompetencia--;

            if (enCompetencia == 0) {
                System.out.println("LA CARRERA TERMINO");
                this.gomonGanador = null;
                this.compitiendo = false;
                inicioCompetencia.reset();
            }
            notifyAll();
        }
    }


    public synchronized void buscarBolso(Visitante v, Bolso bolso) throws InterruptedException {
        while (!this.llevandoBolsos && !this.terminoCarrera) {
            wait();
        }
        System.out.println(v.getID() + " retira bolso " + bolso.getIdBolso());
        terminoCarrera = false;


        notifyAll();
    }


}
