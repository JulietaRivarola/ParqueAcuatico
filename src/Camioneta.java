public class Camioneta implements Runnable{
    private CarreraGomones carrera;
    Reloj reloj;

    public Camioneta(Parque parque, Reloj r) {
        this.carrera = parque.getCarrera();
        this.reloj=r;
    }

    @Override
    public void run() {
  
        try {
            reloj.dormirReloj(7000);
            carrera.camionetaEsperar();
            carrera.llevarBolsos();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}


