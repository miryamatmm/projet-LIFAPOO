package VueControleur;

import Modele.EcranTetris;
import Modele.JeuMulti;
import Modele.JeuSolo;
import Modele.Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;
import java.awt.Font;

public class VueMulti extends JPanel implements Observer{
    private Tetris tetris;
    private Canvas c;
    private Image fond;
    private Image case1P1;
    private Image case2P1;
    private Image case3P1;
    private Image case4P1;
    private Image case5P1;
    private Image case6P1;
    private Image case7P1;
    private Image case1P2;
    private Image case2P2;
    private Image case3P2;
    private Image case4P2;
    private Image case5P2;
    private Image case6P2;
    private Image case7P2;
    private Image finmulti;
    private Image menuPause;
    private Image options;
    private Image fleche;
    private Image battle;
    private Image fondGolmon;
    private Font pressStartFont1;
    private Font pressStartFont2;
    private Color rouge;
    private Color bleu;
    private final static int app_width = 1280;
    private final static int app_height = 800;
    private int taille_w;
    private int taille_h;

    public VueMulti(Tetris _modele){
        tetris = _modele;
        JeuMulti modele = tetris.getMulti();
        setLayout(new BorderLayout());

        ImageIcon fondIcon = new ImageIcon(getClass().getResource("../data/images/jeu2.png"));
        fond = fondIcon.getImage();

        ImageIcon fondGolmonIcon = new ImageIcon(getClass().getResource("../data/images/jeu2Golmon.png"));
        fondGolmon = fondGolmonIcon.getImage();

        ImageIcon case1P1Icon = new ImageIcon(getClass().getResource("../data/images/case1P1.png"));
        case1P1 = case1P1Icon.getImage();

        ImageIcon case2P1Icon = new ImageIcon(getClass().getResource("../data/images/case2P1.png"));
        case2P1 = case2P1Icon.getImage();

        ImageIcon case3P1Icon = new ImageIcon(getClass().getResource("../data/images/case3P1.png"));
        case3P1 = case3P1Icon.getImage();

        ImageIcon case4P1Icon = new ImageIcon(getClass().getResource("../data/images/case4P1.png"));
        case4P1 = case4P1Icon.getImage();

        ImageIcon case5P1Icon = new ImageIcon(getClass().getResource("../data/images/case5P1.png"));
        case5P1 = case5P1Icon.getImage();

        ImageIcon case6P1Icon = new ImageIcon(getClass().getResource("../data/images/case6P1.png"));
        case6P1 = case6P1Icon.getImage();

        ImageIcon case7P1Icon = new ImageIcon(getClass().getResource("../data/images/case7P1.png"));
        case7P1 = case7P1Icon.getImage();

        ImageIcon case1P2Icon = new ImageIcon(getClass().getResource("../data/images/case1P2.png"));
        case1P2 = case1P2Icon.getImage();

        ImageIcon case2P2Icon = new ImageIcon(getClass().getResource("../data/images/case2P2.png"));
        case2P2 = case2P2Icon.getImage();

        ImageIcon case3P2Icon = new ImageIcon(getClass().getResource("../data/images/case3P2.png"));
        case3P2 = case3P2Icon.getImage();

        ImageIcon case4P2Icon = new ImageIcon(getClass().getResource("../data/images/case4P2.png"));
        case4P2 = case4P2Icon.getImage();

        ImageIcon case5P2Icon = new ImageIcon(getClass().getResource("../data/images/case5P2.png"));
        case5P2 = case5P2Icon.getImage();

        ImageIcon case6P2Icon = new ImageIcon(getClass().getResource("../data/images/case6P2.png"));
        case6P2 = case6P2Icon.getImage();

        ImageIcon case7P2Icon = new ImageIcon(getClass().getResource("../data/images/case7P2.png"));
        case7P2 = case7P2Icon.getImage();

        ImageIcon FinMultiIcon = new ImageIcon(getClass().getResource("../data/images/finMulti.png"));
        finmulti = FinMultiIcon.getImage();

        ImageIcon menuPauseIcon = new ImageIcon(getClass().getResource("../data/images/pause.png"));
        menuPause = menuPauseIcon.getImage();

        ImageIcon OptionsIcon = new ImageIcon(getClass().getResource("../data/images/options.png"));
        options = OptionsIcon.getImage();

        ImageIcon flecheIcon = new ImageIcon(getClass().getResource("../data/images/fleche.png"));
        fleche = flecheIcon.getImage();

        pressStartFont1 = loadFont("src/data/PressStart2P-Regular.ttf").deriveFont(40f);
        pressStartFont2 = loadFont("src/data/PressStart2P-Regular.ttf").deriveFont(25f);

        rouge = new Color(255, 49, 49);
        bleu = new Color(12, 192, 223);

        c = new Canvas() {
            public void paint(Graphics g) {
                double temps = Math.sin(System.currentTimeMillis()*200);

                if(tetris.getEcran()==EcranTetris.JeuMulti){
                    if(!modele.getGolmon()){
                        g.drawImage(fond, 0, 0, getWidth(), getHeight(), this);
                    }
                    else {
                        g.drawImage(fondGolmon, 0, 0, getWidth(), getHeight(), this);
                    }
                    taille_w = (32 * getWidth()) / app_width;
                    taille_h = (32 * getHeight()) / app_height;

                    int origine_grille1_x = (222 * getWidth()) / app_width;
                    int origine_grille1_y = (113 * getHeight()) / app_height;

                    int origine_grille2_x = (735 * getWidth()) / app_width;
                    int origine_grille2_y = (113 * getHeight()) / app_height;

                    // AFFICHAGE GRILLES sans le ! pr verifier
                    for (int i = 0; i < Modele.Grille.TAILLE_GRILLE_W; i++) {
                        for (int j = 0; j < Modele.Grille.TAILLE_GRILLE_H; j++) {
                            if (!modele.getGrille1().getCase(i, j).estVide()) {
                                Image image = choixImage1(modele.getGrille1().getCase(i, j));
                                if (image != null) {
                                    g.drawImage(image, origine_grille1_x + i * taille_w, origine_grille1_y + j * taille_h, taille_w, taille_h, this);
                                }
                            }
                            if (!modele.getGrille2().getCase(i, j).estVide()) {
                                Image image = choixImage2(modele.getGrille2().getCase(i, j));
                                if (image != null) {
                                    g.drawImage(image, origine_grille2_x + i * taille_w, origine_grille2_y + j * taille_h, taille_w, taille_h, this);
                                }
                            }
                        }
                    }

                    // AFFICHAGE DES PIECES ACTUELLES + PROJECTIONS

                    // JOUEUR 1 (gauche)
                    Graphics2D g2d = (Graphics2D) g;
                    for (int i = 0; i < Modele.Piece.TAILLE_PIECE; i++) {
                        Image image = choixImage1(modele.getPieceActuelle1().getCase(i));
                        if (image != null) {
                            g2d.drawImage(image, origine_grille1_x + modele.getPieceActuelle1().getCase(i).getX() * taille_w, origine_grille1_y + modele.getPieceActuelle1().getCase(i).getY() * taille_h, taille_w, taille_h, this);
                            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.30f)); // Changer 0.5f pour l'opacité désirée
                            g2d.fillRect(origine_grille1_x + modele.getProjection1().getCase(i).getX() * taille_w, origine_grille1_y + modele.getProjection1().getCase(i).getY() * taille_h, taille_w, taille_h);
                            // g2d.drawImage(image, origine_grille_x + modele.getProjection().getCase(i).getX() * taille_w, origine_grille_y + modele.getPieceActuelle().getCase(i).getY() * taille_h, taille_w, taille_h, this);
                            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Rétablir l'opacité normale
                        }
                    }

                    // JOUEUR 2 (droite)
                    Graphics2D g2d_ = (Graphics2D) g;
                    for (int i = 0; i < Modele.Piece.TAILLE_PIECE; i++) {
                        Image image = choixImage2(modele.getPieceActuelle2().getCase(i));
                        if (image != null) {
                            g2d_.drawImage(image, origine_grille2_x + modele.getPieceActuelle2().getCase(i).getX() * taille_w, origine_grille2_y + modele.getPieceActuelle2().getCase(i).getY() * taille_h, taille_w, taille_h, this);
                            g2d_.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.30f)); // Changer 0.5f pour l'opacité désirée
                            g2d_.fillRect(origine_grille2_x + modele.getProjection2().getCase(i).getX() * taille_w, origine_grille2_y + modele.getProjection2().getCase(i).getY() * taille_h, taille_w, taille_h);
                            // g2d.drawImage(image, origine_grille_x + modele.getProjection().getCase(i).getX() * taille_w, origine_grille_y + modele.getPieceActuelle().getCase(i).getY() * taille_h, taille_w, taille_h, this);
                            g2d_.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Rétablir l'opacité normale
                        }
                    }

                    // AFFICHAGE PIECES SUIVANTES

                    // JOUEUR 1 (en bas)
                    int ajout1X = (540 * getWidth()) / app_width;
                    int ajout1Y = ((117 + 58) * getHeight()) / app_height;
                    for (int i = 0; i < Modele.Piece.TAILLE_PIECE; i++) {
                        Image image = choixImage1(modele.getFuturPiece1().getCase(i));
                        if (image != null) {
                            g2d.drawImage(image, ajout1X + modele.getFuturPiece1().getCase(i).getX() * (2*taille_w/3), ajout1Y + modele.getFuturPiece1().getCase(i).getY() * (2*taille_h/3), (2*taille_w/3), (2*taille_h/3), this);
                        }
                    }

                    // JOUEUR 2 (en haut)
                    int ajout2X = (540 * getWidth()) / app_width;
                    int ajout2Y = ((117 + 475) * getHeight()) / app_height;
                    for (int i = 0; i < Modele.Piece.TAILLE_PIECE; i++) {
                        Image image = choixImage2(modele.getFuturPiece2().getCase(i));
                        if (image != null) {
                            g2d_.drawImage(image, ajout2X + modele.getFuturPiece2().getCase(i).getX() * (2*taille_w/3), ajout2Y + modele.getFuturPiece2().getCase(i).getY() * (2*taille_h/3), (2*taille_w/3), (2*taille_h/3), this);
                        }
                    }


                    // AFFICHAGE LIGNES

                    int ligne1_x = (620 * getWidth()) / app_width;
                    int ligne1_y = (345 * getHeight()) / app_height;
                    dessinerTexte(g,String.valueOf(JeuMulti.nbLigneGagnant - modele.getNbLigneDetruite1()),ligne1_x,ligne1_y,Color.white,pressStartFont2);

                    int ligne2_x = (620 * getWidth()) / app_width;
                    int ligne2_y = (485 * getHeight()) / app_height;
                    dessinerTexte(g,String.valueOf(JeuMulti.nbLigneGagnant - modele.getNbLigneDetruite2()),ligne2_x,ligne2_y,Color.white,pressStartFont2);

                }
                else if (tetris.getEcran()==EcranTetris.FinMulti){ // AFFICHAGE FIN DE LA PARTIE
                    g.drawImage(finmulti, 0, 0, getWidth(), getHeight(), this);
                    afficherBouton(temps,g,431,566);

                    if(modele.getResultatPartie() == 1){
                        if(modele.getGolmon()){
                            dessinerTexte(g,"GOLMON",(500*getWidth())/app_width,(400*getHeight())/app_height,bleu,pressStartFont1);
                        }
                        else{
                            dessinerTexte(g,"PLAYER 1",(500*getWidth())/app_width,(400*getHeight())/app_height,bleu,pressStartFont1);
                        }

                    }
                    else if(modele.getResultatPartie() == 2){
                        if(modele.getGolmon()){
                            dessinerTexte(g,"PLAYER",(500*getWidth())/app_width,(400*getHeight())/app_height,bleu,pressStartFont1);
                        }
                        else{
                            dessinerTexte(g,"PLAYER 2",(500*getWidth())/app_width,(400*getHeight())/app_height,bleu,pressStartFont1);
                        }
                    }
                    else {
                        dessinerTexte(g,"PAFWAX",(510*getWidth())/app_width,(400*getHeight())/app_height,Color.white,pressStartFont1);
                    }
                }
                else if(tetris.getEcran()==EcranTetris.PauseMulti) {
                    g.drawImage(menuPause,0,0,getWidth(),getHeight(),this);
                    switch (tetris.getChoix_option_ecran())
                    {
                        case 0 -> { afficherBouton(temps,g,475,300);}
                        case 1 -> { afficherBouton(temps,g,460,384);}
                        case 2 -> { afficherBouton(temps,g,460,477);}
                        case 3 -> { afficherBouton(temps,g,435,566);}
                    }
                }
                else if(tetris.getEcran()==EcranTetris.OptionsMulti){
                    g.drawImage(options,0,0,getWidth(),getHeight(),this);
                    switch (tetris.getChoix_option_ecran())
                    {
                        case 0 -> { afficherBouton(temps,g,503,370);}
                        case 1 -> { afficherBouton(temps,g,490,420);}
                        case 2 -> { afficherBouton(temps,g,547,471);}
                        case 3 -> { afficherBouton(temps,g,467,611);}
                    }
                }
            }
        };
        add(c, BorderLayout.CENTER);

    }

    @Override
    public void update(Observable o, Object arg) {
        BufferStrategy bs = c.getBufferStrategy(); // bs + dispose + show : double buffering pour éviter les scintillements
        if(bs == null) {
            c.createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        c.paint(g);
        g.dispose();
        bs.show();
    }

    private void afficherBouton(double temps,Graphics g, int x, int y){
        if(temps>0) g.drawImage(fleche,(x * getWidth())/app_width,(y * getHeight())/app_height,(32 * getWidth())/app_width,(32 * getHeight())/app_height,this);
        else g.drawImage(fleche,((x-5) * getWidth())/app_width,(y * getHeight())/app_height,(32 * getWidth())/app_width,(32 * getHeight())/app_height,this);
    }

    private void dessinerTexte(Graphics g, String texte, int x, int y, Color couleur, Font font) {
        g.setColor(couleur);
        g.setFont(font);
        g.drawString(texte, x, y);
    }

    private Image choixImage1(Modele.Case c){
        Image im = null;
        switch(c.getCouleur()){
            case 1 -> {
                im = case1P1;
            }
            case 2 -> {
                im = case2P1;
            }
            case 3 -> {
                im = case3P1;
            }
            case 4 -> {
                im = case4P1;
            }
            case 5 -> {
                im = case5P1;
            }
            case 6 -> {
                im = case6P1;
            }
            case 7 -> {
                im = case7P1;
            }
            default ->  {}
        }
        return im;
    }

    private Image choixImage2(Modele.Case c){
        Image im = null;
        switch(c.getCouleur()){
            case 1 -> {
                im = case1P2;
            }
            case 2 -> {
                im = case2P2;
            }
            case 3 -> {
                im = case3P2;
            }
            case 4 -> {
                im = case4P2;
            }
            case 5 -> {
                im = case5P2;
            }
            case 6 -> {
                im = case6P2;
            }
            case 7 -> {
                im = case7P2;
            }
            default ->  {}
        }
        return im;
    }

    private static Font loadFont(String path) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new java.io.File(path));
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, 12); // Utiliser une police de secours
        }
    }
}
