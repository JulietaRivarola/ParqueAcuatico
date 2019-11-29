public class Gomon {
    private int id;
    private int capacidad, contLugarLibres, totalEnrio;
    Visitante v1, v2;
    private boolean llego;

    public Gomon(int cap, int n) {
        this.id = n;
        this.capacidad = cap;
        contLugarLibres = cap;
        this.totalEnrio = 0;
        this.v1=null;
        this.v2=null;
        this.llego=false;
    }

    public  boolean estaLleno() {
        return contLugarLibres == 0;
    }

    public int getCapacidad() {
        return this.capacidad;
    }

    public synchronized void tomar() {
        if (!this.estaLleno()) {
            contLugarLibres--;
        }
    }

    public synchronized void dejar() {
        if (contLugarLibres > 0) {
            contLugarLibres++;
        }
    }
    public synchronized void setCompañero(Visitante v){

        if(this.getCapacidad()>2){
            if(v1==null){
                v1=v;
            }else if(v2==null){
                v2=v;
            }
        }
    }
    public synchronized Visitante  getCompañero(Visitante v){
        Visitante compañero=null;
        if(this.getCapacidad()>2){
            if(v1.getID()!=v.getID()){
                compañero=v1;
            }else if(v2.getID()!=v.getID()){
                compañero=v2;
            }
        }
        return compañero;
    }

    public int getId() {
        return id;
    }

    public  boolean enAgua() {
        return this.totalEnrio == getCapacidad();
    }

    public void llevarRio() {
        if (totalEnrio < getCapacidad()) {
            totalEnrio++;
        }
    }

    public synchronized void salirAgua() {
        this.totalEnrio--;
    }
    public boolean getLlego(){
        return this.llego;
    }

    public synchronized void setLlego(boolean llego) {
        this.llego=llego;
    }
}

