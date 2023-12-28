package Modele;

public class Ordonnanceur extends Thread {

    public Runnable monRunnable;

    public Ordonnanceur(Runnable _monRunnable) {
        monRunnable = _monRunnable;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            monRunnable.run();
        }
    }
}
