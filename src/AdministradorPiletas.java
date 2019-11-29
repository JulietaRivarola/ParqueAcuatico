

public class AdministradorPiletas implements Runnable {
    private String nombre;
    private NadarConDelfines nado;
    private Reloj reloj;

    public AdministradorPiletas(String nom, NadarConDelfines n, Reloj r){
        this.nombre = nom;
        this.nado = n;
        this.reloj = r;
    }
    public void run(){

        while(true){

            try {
                    this.nado.habilitarAdministrador();
                    Thread.sleep(5000);
                    this.nado.verificar();
                    this.nado.finalizar();
                    this.nado.actualizar();
                     System.out.println("--Finalizo la actividad Nado con Delfines--");


            } catch (InterruptedException ex) {

            }

        }


    }
}

