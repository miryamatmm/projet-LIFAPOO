package Modele;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class Tetris extends Observable implements Observer,Runnable {

    private JeuSolo solo;
    private int[] tabScoreSolo;
    private JeuMulti multi;
    private EcranTetris ecran;
    private int choix_option_ecran;

    private Ordonnanceur ordonnanceur;

    public Tetris(){
        solo = new JeuSolo();
        multi = new JeuMulti();

        tabScoreSolo = getSolo().lireScore();

        solo.addObserver(this);
        multi.addObserver(this);

        ecran = EcranTetris.Run;
        choix_option_ecran = 0;

        ordonnanceur = new Ordonnanceur(this);
        ordonnanceur.start();
    }

    public EcranTetris getEcran(){ return ecran; }

    public void setEcran(EcranTetris ecran) {
        this.ecran = ecran;
        this.choix_option_ecran = 0;
    }
    public void setChoix_option_ecran(int choix) { choix_option_ecran = choix; }
    public int getChoix_option_ecran(){return choix_option_ecran; }

    public JeuSolo getSolo() { return solo; }
    public JeuMulti getMulti() { return multi; }

    public int getTabScoreSolo(int i) { return tabScoreSolo[i]; }
    public void refreshTabScoreSolo() { tabScoreSolo = getSolo().lireScore(); }

    @Override
    public void update(Observable o,Object arg){
        setChanged();
        notifyObservers();
    }

    @Override
    public void run() {
        switch (ecran) {        
            case JeuSolo:
                solo.update();
                break;
            case JeuMulti:
                multi.update();
                break;
            default:
                setChanged();
                notifyObservers();
                break;
        }
    }
}
