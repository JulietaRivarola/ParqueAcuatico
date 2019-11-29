public class Transporte {
    protected final int CAPACIDAD;
    protected int contPasajeros;
    protected boolean estaCompleto, finRecorrido, inicioRecorrido;
    protected int contEsperando;

    public Transporte(int cap) {
        this.CAPACIDAD = cap;
        this.estaCompleto = false;
        this.contPasajeros = 0;
        this.finRecorrido = false;
        this.contEsperando = -1;
    }
    public synchronized void esperarColectivo(Visitante v) throws InterruptedException{
        if (contPasajeros == 0) {
            contEsperando = 0;
        }
        this.contEsperando++;
        subir(v);
        this.contEsperando--;
        
    }

    public synchronized boolean hayLugar() {
        return !this.estaCompleto;
    }

    public synchronized void subir(Visitante v) throws InterruptedException {
                
        this.contPasajeros++;
        if (this.contPasajeros == this.CAPACIDAD) {
            this.estaCompleto = true;
        }       
        
    }

    public boolean hayMasPasajeros() throws InterruptedException {
        if(this.contEsperando == 0){
            Thread.sleep(2000);
        }
        return !(this.contEsperando==0);
    }

    public synchronized void trasladar() throws InterruptedException {

        Thread.sleep(1000);
    }

    public synchronized void finalizarRecorrido() throws InterruptedException {
        this.finRecorrido = true;
    }

    public synchronized boolean arribo() {
        return this.finRecorrido;
    }


}
