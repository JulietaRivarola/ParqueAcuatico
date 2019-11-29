public class Reloj implements Runnable {
    private boolean abierto, activas;
    private int hora;
    private int minutos;


    public Reloj() {
        abierto = false;
        hora = 8;
        activas = false;
        this.minutos=55;

    }

    public void run() {


        try {
            while (/*this.getHora()!=0*/ true) {
                if(minutos<10){
                    System.out.println(hora+":0"+minutos+ " hs.");
                }else{
                    System.out.println(hora+":"+minutos+ " hs.");
                }
                Thread.sleep(1000);
                aumentarMinutos();
                
                switch (this.getHora()) {
                    case 9:
                        this.abierto = true;
                        this.activas = true;
                        break;
                    case 17:
                        this.abierto = false;
                        break;
                    case 18:
                        this.activas = false;
                        break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean estadoParque() {
        return abierto;
    }

    public int getHora() {
        return hora;
    }

    public void aumentarHora() {
        this.hora = (this.hora + 1) % 24;
    }
    public void aumentarMinutos(){
        this.minutos=(this.minutos+1)%60;
        if(this.minutos==0){
            aumentarHora();
        }
    }
    public boolean estadoActividades() {
        return this.activas;
    }

    public void dormirReloj(int cantTiempo) {
        try {
            Thread.sleep(cantTiempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


