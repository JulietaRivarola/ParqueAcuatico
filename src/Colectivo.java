public class Colectivo extends Transporte implements Runnable {


    public Colectivo(int cap) {
        super(cap);
    }


    @Override
    public void run() {

        try {
            boolean entro = false;
            while (!entro) {
                if (!this.hayLugar() || !this.hayMasPasajeros()) {
                    entro = true;
                    trasladar();
                    System.out.println("Los visitantes del Tour son tralasladandos en colectivo.");
                    finalizarRecorrido();
                    System.out.println("El colectivo con visitantes ha llegado al estacionamiento. ");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
