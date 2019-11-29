
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/*Faro-Mirador con vista  a  40  m de altura  y  descenso  en tobogán :  Admira desde lo  alto  todo  el                   
esplendor  des  necesario  subir  por  una escalera caracol,  que tiene capacidad  para n  personas.
Al  llegar  a la cima                   
nos  encontraremos  con  dos  toboganes  para descender,  la elección  del  tobogán  es  realizada por  un                
administrador  de  cola  que  indica  que  persona  de  la  fila  va  a  un  tobogán  y  cuál  va  al  otro.  Es                     
importante destacar  que una persona no  se tira por  el  tobogán  hasta que la anterior no  haya llegado  a                    
la   pileta,   es   decir,   sobre   cada   tobogán   siempre   hay   a   lo   sumo   una   persona.  
 e una maravilla natural  y  desciende en  tobogán  hasta una pileta.  Para acceder  al  tobogán.
 */
public class FaroMirador {

    private Semaphore semSubir;
    private Reloj reloj;
    private int CAPACIDAD_ESCALERA;
    private int contSubiendo;
    private Tobogan[] toboganes;
    private ArrayBlockingQueue<Visitante> colaEscalera;
    private SynchronousQueue colaTobogan1;
    private SynchronousQueue colaTobogan2;

    public FaroMirador(Reloj r, Tobogan[] tob, int capacidadEsc, ArrayBlockingQueue<Visitante> colaEsc, SynchronousQueue colaT1, SynchronousQueue colaT2) {
        this.reloj = r;
        this.CAPACIDAD_ESCALERA = capacidadEsc;
        this.semSubir = new Semaphore(1, true);
        this.contSubiendo = 0;
        this.colaEscalera = colaEsc;
        this.toboganes = tob;
        this.colaTobogan1=colaT1;
        this.colaTobogan2=colaT2;
    }

    public void visitarFaro(Visitante v) throws InterruptedException {

        subirEscaleraCaracol(v);
        descenderTobogan(v);
        salir(v);
    }

    private void subirEscaleraCaracol(Visitante v) throws InterruptedException {
        System.out.println(v.getID() + " subiendo escalera...");
        this.colaEscalera.put(v);

    }

    private void salir(Visitante v) throws InterruptedException {
        System.out.println(v.getID() + " sale de la piscina y se va.");
        Thread.sleep(1000);
    }

    private void descenderTobogan(Visitante v) throws InterruptedException {
        if(v.getTobogan()==0) {
            colaTobogan1.take();
        }else{
            colaTobogan2.take();
        }
        System.out.println(v.getID() + " llega a la cima y admira todo el esplendor de lo natural...");
        System.out.println(v.getID() + " desciende por el tobogan ");

    }



}
