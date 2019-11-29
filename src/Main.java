import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class Main {
    public static void main(String[] args) {
        int CANT_VISITANTES = 80;
        int CANT_RESTAURANTES = 3;
        int CANT_TOBOGANES = 2;
        int CANT_MOLINETES = 7;
        int CANT_CAJAS = 2;
        int CANT_PILETAS = 4;
        int CANT_DELFINES = CANT_PILETAS * 2;
        int CANT_EQUIPOS = 1;
        int CAPACIDAD_COLECTIVO = 25;
        int CAPACIDAD_TREN = 15;
        int CAPACIDAD_ESCALERA = 20;
        int CAPACIDAD_PILETA = 10;
        int CAPACIDAD_RESTAURANTES = 4;
        int horarioPileta = 0;
        Random rnd = new Random();

        ArrayBlockingQueue[] colaVisitantes = new ArrayBlockingQueue[CANT_RESTAURANTES];
        ArrayBlockingQueue<Visitante> colaEscalera = new ArrayBlockingQueue<>(CAPACIDAD_ESCALERA);
        SynchronousQueue colaTobogan1 = new SynchronousQueue();
        SynchronousQueue colaTobogan2 = new SynchronousQueue();

        ArrayBlockingQueue<EquipoSnorkel> zonaEquipos = new ArrayBlockingQueue<>(CANT_EQUIPOS);
        ArrayBlockingQueue<Visitante> cola=new ArrayBlockingQueue(CANT_VISITANTES);


        Visitante[] visitantes = new Visitante[CANT_VISITANTES];
        Bolso[] bolso = new Bolso[CANT_VISITANTES];
        Gomon[] gomones = new Gomon[CANT_VISITANTES];
        Caja[] caja = new Caja[CANT_RESTAURANTES];
        Pileta[] piletas = new Pileta[CANT_PILETAS];
        Delfin[] delfines = new Delfin[CANT_DELFINES];
        Tobogan[] toboganes = new Tobogan[CANT_TOBOGANES];
        Restaurante[] restaurantes = new Restaurante[CANT_RESTAURANTES];
        EquipoSnorkel[] equipoSnorkels=new EquipoSnorkel[CANT_EQUIPOS];

        Reloj reloj = new Reloj();
        Colectivo colectivo = new Colectivo(CAPACIDAD_COLECTIVO);
        Tren tren = new Tren(CAPACIDAD_TREN);
        FaroMirador faro = new FaroMirador(reloj, toboganes, CAPACIDAD_ESCALERA, colaEscalera, colaTobogan1, colaTobogan2);
        AdministradorDeCola admiCola = new AdministradorDeCola(toboganes, colaEscalera, colaTobogan1,colaTobogan2, reloj);
        NadarConDelfines nadoConDelfines = new NadarConDelfines(piletas, reloj);
        AdministradorPiletas administradorPiletas= new AdministradorPiletas(""+1, nadoConDelfines, reloj);
        Snorkel snorkel = new Snorkel(zonaEquipos, cola);
        Asistente asistente = new Asistente(zonaEquipos,cola, reloj, equipoSnorkels );
        Parque parque = new Parque(CANT_VISITANTES, CANT_MOLINETES, CANT_CAJAS, tren,
                bolso, gomones, reloj, restaurantes, faro, nadoConDelfines, snorkel);
        Camioneta camioneta = new Camioneta(parque, reloj);

        Thread time = new Thread(reloj);
        time.start();

        Thread c = new Thread(colectivo);
        c.start();

        Thread camio = new Thread(camioneta);
        camio.start();

        Thread administrador = new Thread(admiCola);
        administrador.start();

        Thread asist = new Thread(asistente);
        asist.start();

        Thread admi= new Thread(administradorPiletas);
        admi.start();

        for (int i = 0; i < visitantes.length; i++) {
            if (i % 40 == 0) {
                horarioPileta++;
            }
            visitantes[i] = new Visitante("v" + i, parque, colectivo, horarioPileta, reloj, toboganes);
            Thread visitante = new Thread(visitantes[i]);
            visitante.start();
        }

        for (int i = 0; i < restaurantes.length; i++) {
            colaVisitantes[i] = new ArrayBlockingQueue<>(CAPACIDAD_RESTAURANTES);
            restaurantes[i] = new Restaurante("" + i, CAPACIDAD_RESTAURANTES, reloj, colaVisitantes[i]);
            caja[i] = new Caja(colaVisitantes[i], reloj, restaurantes[i]);
            Thread caj = new Thread(caja[i]);
            caj.start();
        }

        for (int i = 0; i < gomones.length; i++) {
            gomones[i] = new Gomon(rnd.nextInt(2) + 1, i);
        }

        for (int j = 0; j < bolso.length; j++) {
            bolso[j] = new Bolso(j);
        }
        for (int i = 0; i < equipoSnorkels.length; i++) {
            equipoSnorkels[i] = new EquipoSnorkel();
        }

        for (int i = 0; i < toboganes.length; i++) {

            toboganes[i] = new Tobogan(i);
        }

        for (int i = 0; i < piletas.length; i++) {
            piletas[i] = new Pileta("" + i, CAPACIDAD_PILETA);
        }

    }
}
