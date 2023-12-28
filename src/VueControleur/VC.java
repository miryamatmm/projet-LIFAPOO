package VueControleur;

import Modele.EcranTetris;
import Modele.Tetris;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;
import java.util.Observable;
import java.util.Observer;

import static Modele.EcranTetris.*;

public class VC extends JFrame implements Observer {

    private Tetris modele;
    private Observer VueMenu;
    private Observer VueSolo;
    private Observer VueMulti;
    private JPanel cardPanel;

    private Musique musique_menu;
    private Musique musique_solo;
    private Musique musique_multi;

    private boolean musique_on;

    public VC(Tetris _modele) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        modele = _modele;

        setTitle("Tetris");
        setSize(1280, 800);

        JPanel jpMenu = new JPanel(new BorderLayout());
        jpMenu.setPreferredSize(new Dimension(1280, 800));

        JPanel jpSolo = new JPanel(new BorderLayout());
        jpSolo.setPreferredSize(new Dimension(1280, 800));

        JPanel jpMulti = new JPanel(new BorderLayout());
        jpMulti.setPreferredSize(new Dimension(1280, 800));


        VueMenu = new VueMenu(modele);
        VueSolo = new VueSolo(modele);
        VueMulti = new VueMulti(modele);

        musique_menu = new Musique("src/data/musiques/chill.wav");
        musique_solo = new Musique("src/data/musiques/game.wav");
        musique_multi = new Musique("src/data/musiques/multi.wav");
        musique_menu.play();
        musique_on = true;

        jpMenu.add((JPanel)VueMenu, BorderLayout.CENTER);
        jpSolo.add((JPanel)VueSolo, BorderLayout.CENTER);
        jpMulti.add((JPanel)VueMulti, BorderLayout.CENTER);

        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(jpMenu, "Menu");
        cardPanel.add(jpSolo, "Solo");
        cardPanel.add(jpMulti, "Multi");
        setContentPane(cardPanel);

        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                if(keyEvent.getID() == KeyEvent.KEY_PRESSED){
                    switch (modele.getEcran()) {
                        case Run:{
                            modele.setEcran(MainMenu);
                        }
                        break;
                        case MainMenu:{
                            evenementMainMenu(keyEvent);
                        }
                        break;
                        case PlayMenu:{
                            evenementPlayerMenu(keyEvent);
                        }
                        break;
                        case Classic:{
                            evenementClassicMenu(keyEvent);
                        }
                        break;
                        case Battle:{
                            evenementBattleMenu(keyEvent);
                        }
                        break;
                        case Options,OptionsSolo,OptionsMulti: {
                            evenementOptions(keyEvent);
                        }
                        break;
                        case JeuSolo: {
                            evenementJeuSolo(keyEvent);
                        }
                        break;
                        case PauseSolo,PauseMulti: {
                            evenementPause(keyEvent);
                        }
                        break;
                        case JeuMulti: {
                            evenementJeuMulti(keyEvent);
                        }
                        break;
                        case LeaderBoard,GameOver,FinMulti: {
                            evenementFinJeu(keyEvent);
                        }
                        break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) { // rafraichissement de la vue
        switch (modele.getEcran()) {
            case JeuSolo,PauseSolo,OptionsSolo,GameOver:
                if(modele.getSolo().getFin()){
                    modele.setEcran(GameOver);
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        VueSolo.update(o, arg);
                    }
                });
                break;
            case JeuMulti,PauseMulti,OptionsMulti,FinMulti:
                if(modele.getMulti().getFin()){
                    modele.setEcran(EcranTetris.FinMulti);
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        VueMulti.update(o, arg);
                    }
                });
                break;
            default:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        VueMenu.update(o, arg);
                    }
                });
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                   Tetris tetris = new Tetris();
                   VC vc = new VC(tetris);
                   vc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                   tetris.addObserver(vc);
                   vc.setVisible(true);
               }
           }
        );
    }


    private void defilerBoutons(KeyEvent keyEvent,int nb_btn){
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_UP,KeyEvent.VK_Z ->{
                int c = modele.getChoix_option_ecran();
                if(c == 0){
                    c = nb_btn-1;
                }
                else{
                    c = (c - 1) % nb_btn;
                }
                modele.setChoix_option_ecran(c);
            }
            case KeyEvent.VK_DOWN,KeyEvent.VK_S ->{
                int c = modele.getChoix_option_ecran();
                if(c == nb_btn){
                    c = 0;
                }
                else{
                    c = (c + 1) % nb_btn;
                }
                modele.setChoix_option_ecran(c);
            }
        }
    }
    private void evenementMainMenu(KeyEvent keyEvent){
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            switch (modele.getChoix_option_ecran()) {
                case 0:
                    modele.setEcran(EcranTetris.PlayMenu);
                    break;
                case 1:
                    modele.setEcran(EcranTetris.LeaderBoard);
                    break;
                case 2:
                    modele.setEcran(EcranTetris.Options);
                    break;
                case 3:
                    System.exit(0);
                    break;
            }
        }
        defilerBoutons(keyEvent,4);
    }

    private void evenementPlayerMenu(KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ENTER  -> {
                switch (modele.getChoix_option_ecran()){
                    case 0:
                        modele.setEcran(EcranTetris.Classic);
                        break;
                    case 1:
                        modele.setEcran(EcranTetris.Battle);
                        break;
                }
            }
            case KeyEvent.VK_ESCAPE -> {
                modele.setEcran(EcranTetris.MainMenu);
            }
        }
        defilerBoutons(keyEvent,2);
    }

    private void evenementClassicMenu(KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ENTER  -> {
                switch (modele.getChoix_option_ecran()){
                    case 0: // PAS IA
                        modele.getSolo().setGolmon(false);
                        break;
                    case 1: // IA
                        modele.getSolo().setGolmon(true);
                        break;
                }
                modele.setEcran(JeuSolo);
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "Solo");
                musique_menu.stop();
                if(musique_on)
                    musique_solo.play();
            }
            case KeyEvent.VK_ESCAPE -> {
                modele.setEcran(EcranTetris.MainMenu);
            }
        }
        defilerBoutons(keyEvent,2);
    }

    private void evenementBattleMenu(KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ENTER  -> {
                switch (modele.getChoix_option_ecran()){
                    case 0: // PAS IA
                        modele.getMulti().setGolmon(false);
                        break;
                    case 1: // IA
                        modele.getMulti().setGolmon(true);
                        break;
                }
                modele.setEcran(JeuMulti);
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "Multi");
                musique_menu.stop();
                if(musique_on)
                    musique_multi.play();
            }
            case KeyEvent.VK_ESCAPE -> {
                modele.setEcran(EcranTetris.MainMenu);
            }
        }
        defilerBoutons(keyEvent,2);
    }

    private void evenementOptions(KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ENTER  -> {
                switch (modele.getChoix_option_ecran()){
                    case 0:
                        musique_menu.ajouterVolume();
                        musique_solo.ajouterVolume();
                        musique_multi.ajouterVolume();
                        break;
                    case 1:
                        musique_menu.diminuerVolume();
                        musique_solo.diminuerVolume();
                        musique_multi.diminuerVolume();
                        break;
                    case 2:
                        if(musique_on){
                            musique_menu.stop();
                            musique_solo.stop();
                            musique_multi.stop();
                            musique_on = !musique_on;
                        }
                        else if(!musique_on){
                            switch (modele.getEcran()){
                                case Options -> {
                                    musique_menu.play();
                                }
                                case OptionsSolo -> {
                                    musique_solo.play();
                                }
                                case OptionsMulti -> {
                                    musique_multi.play();
                                }
                            }
                            musique_on = !musique_on;
                        }
                        break;
                    case 3:
                        switch (modele.getEcran()){
                            case Options -> {
                                modele.setEcran(EcranTetris.MainMenu);
                            }
                            case OptionsSolo -> {
                                modele.setEcran(EcranTetris.PauseSolo);
                            }
                            case OptionsMulti -> {
                                modele.setEcran(EcranTetris.PauseMulti);
                            }
                        }
                        break;
                }
            }
            case KeyEvent.VK_ESCAPE -> {
                switch (modele.getEcran()){
                    case Options -> {
                        modele.setEcran(EcranTetris.MainMenu);
                    }
                    case OptionsSolo -> {
                        modele.setEcran(EcranTetris.PauseSolo);
                    }
                    case OptionsMulti -> {
                        modele.setEcran(EcranTetris.PauseMulti);
                    }
                }
            }
        }
        defilerBoutons(keyEvent,4);
    }

    private void evenementJeuSolo(KeyEvent keyEvent){
        if(!modele.getSolo().getGolmon()){
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_RIGHT  -> { modele.getSolo().deplacerPiece(1); }
                case KeyEvent.VK_LEFT   -> { modele.getSolo().deplacerPiece(2); }
                case KeyEvent.VK_UP     -> { modele.getSolo().rotationPiece(); }
                case KeyEvent.VK_DOWN   -> { modele.getSolo().tomberPiece(); }
                case KeyEvent.VK_SPACE  -> { modele.getSolo().hard_drop();  }
                case KeyEvent.VK_ESCAPE -> {
                    modele.setEcran(EcranTetris.PauseSolo);
                }
            }
        }
        else{
            if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
                modele.setEcran(EcranTetris.PauseSolo);
            }
        }
    }

    private void evenementPause(KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ENTER  -> {
                switch (modele.getChoix_option_ecran()){
                    case 0:
                        if(modele.getEcran() == EcranTetris.PauseSolo)
                            modele.setEcran(EcranTetris.JeuSolo);
                        else if (modele.getEcran() == EcranTetris.PauseMulti)
                            modele.setEcran(EcranTetris.JeuMulti);
                        break;
                    case 1:
                        if(modele.getEcran() == EcranTetris.PauseSolo){
                            modele.getSolo().initialiser();
                            modele.setEcran(EcranTetris.JeuSolo);
                        }
                        else if (modele.getEcran() == EcranTetris.PauseMulti){
                            modele.getMulti().initialiser();
                            modele.setEcran(EcranTetris.JeuMulti);
                        }

                        break;
                    case 2:
                        if(modele.getEcran() == EcranTetris.PauseSolo)
                            modele.setEcran(EcranTetris.OptionsSolo);
                        else if (modele.getEcran() == EcranTetris.PauseMulti)
                            modele.setEcran(EcranTetris.OptionsMulti);
                        break;
                    case 3:
                        modele.setEcran(EcranTetris.MainMenu);
                        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                        cardLayout.show(cardPanel, "Menu");
                        modele.getSolo().initialiser();
                        modele.getMulti().initialiser();
                        musique_solo.stop();
                        musique_multi.stop();
                        musique_menu.play();
                        break;
                }
            }
            case KeyEvent.VK_ESCAPE -> {
                if(modele.getEcran() == EcranTetris.PauseSolo)
                    modele.setEcran(EcranTetris.JeuSolo);
                else if (modele.getEcran() == EcranTetris.PauseMulti)
                    modele.setEcran(EcranTetris.JeuMulti);
            }
        }
        defilerBoutons(keyEvent,4);
    }

    private void evenementJeuMulti(KeyEvent keyEvent){
        if(modele.getMulti().getGolmon()){
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_RIGHT  -> { modele.getMulti().deplacerPiece(2,1); }
                case KeyEvent.VK_LEFT   -> { modele.getMulti().deplacerPiece(2,2); }
                case KeyEvent.VK_UP     -> { modele.getMulti().rotationPiece(2); }
                case KeyEvent.VK_DOWN   -> { modele.getMulti().tomberPiece(2); }
                case KeyEvent.VK_ESCAPE -> {
                    modele.setEcran(EcranTetris.PauseMulti);
                }
            }
        }
        else{
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_RIGHT  -> { modele.getMulti().deplacerPiece(2,1); }
                case KeyEvent.VK_LEFT   -> { modele.getMulti().deplacerPiece(2,2); }
                case KeyEvent.VK_UP     -> { modele.getMulti().rotationPiece(2); }
                case KeyEvent.VK_DOWN   -> { modele.getMulti().tomberPiece(2); }
                case KeyEvent.VK_D  -> { modele.getMulti().deplacerPiece(1,1); }
                case KeyEvent.VK_Q   -> { modele.getMulti().deplacerPiece(1,2); }
                case KeyEvent.VK_Z     -> { modele.getMulti().rotationPiece(1); }
                case KeyEvent.VK_S   -> { modele.getMulti().tomberPiece(1); }
                case KeyEvent.VK_ESCAPE -> {
                    modele.setEcran(EcranTetris.PauseMulti);
                }
            }
        }

    }

    private void evenementFinJeu(KeyEvent keyEvent){
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE
                || keyEvent.getKeyCode() == KeyEvent.VK_ENTER
                || keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if(modele.getEcran() == GameOver){
                modele.setEcran(EcranTetris.MainMenu);
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "Menu");
                modele.getSolo().ecrireScore();
                modele.refreshTabScoreSolo();
                modele.getSolo().initialiser();
                musique_solo.stop();
                if(musique_on){
                    musique_menu.play();
                }

            }
            else if (modele.getEcran() == FinMulti){
                modele.setEcran(EcranTetris.MainMenu);
                modele.getMulti().initialiser();
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "Menu");
                musique_multi.stop();
                if(musique_on){
                    musique_menu.play();
                }
            }
            else{
                modele.setEcran(EcranTetris.MainMenu);
            }
        }
    }


}
