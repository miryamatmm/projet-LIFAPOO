package VueControleur;

import Modele.EcranTetris;
import Modele.JeuSolo;
import Modele.Tetris;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;
import java.awt.Font;

public class VueSolo extends JPanel implements Observer{
    private Tetris tetris;
    private Canvas c;
    private Image fond;
    private Image fondGolmon;
    private Image case1;
    private Image case2;
    private Image case3;
    private Image case4;
    private Image case5;
    private Image case6;
    private Image case7;
    private Image gameOver;
    private Image menuPause;
    private Image options;
    private Image fleche;
    private final static int app_width = 1280;
    private final static int app_height = 800;
    private int taille_w;
    private int taille_h;
    private Font pressStartFont;

    public VueSolo(Tetris _modele){
        tetris = _modele;
        JeuSolo modele = tetris.getSolo();

        setLayout(new BorderLayout());

        ImageIcon fondIcon = new ImageIcon(getClass().getResource("../data/images/jeu1.png"));
        fond = fondIcon.getImage();

        ImageIcon fondGolmonIcon = new ImageIcon(getClass().getResource("../data/images/jeu1Golmon.png"));
        fondGolmon = fondGolmonIcon.getImage();

        ImageIcon case1Icon = new ImageIcon(getClass().getResource("../data/images/case1.png"));
        case1 = case1Icon.getImage();

        ImageIcon case2Icon = new ImageIcon(getClass().getResource("../data/images/case2.png"));
        case2 = case2Icon.getImage();

        ImageIcon case3Icon = new ImageIcon(getClass().getResource("../data/images/case3.png"));
        case3 = case3Icon.getImage();

        ImageIcon case4Icon = new ImageIcon(getClass().getResource("../data/images/case4.png"));
        case4 = case4Icon.getImage();

        ImageIcon case5Icon = new ImageIcon(getClass().getResource("../data/images/case5.png"));
        case5 = case5Icon.getImage();

        ImageIcon case6Icon = new ImageIcon(getClass().getResource("../data/images/case6.png"));
        case6 = case6Icon.getImage();

        ImageIcon case7Icon = new ImageIcon(getClass().getResource("../data/images/case7.png"));
        case7 = case7Icon.getImage();

        ImageIcon GameOverIcon = new ImageIcon(getClass().getResource("../data/images/gameOver.png"));
        gameOver = GameOverIcon.getImage();

        ImageIcon menuPauseIcon = new ImageIcon(getClass().getResource("../data/images/pause.png"));
        menuPause = menuPauseIcon.getImage();

        ImageIcon OptionsIcon = new ImageIcon(getClass().getResource("../data/images/options.png"));
        options = OptionsIcon.getImage();

        ImageIcon flecheIcon = new ImageIcon(getClass().getResource("../data/images/fleche.png"));
        fleche = flecheIcon.getImage();

        pressStartFont = loadFont("src/data/PressStart2P-Regular.ttf").deriveFont(40f);

        c = new Canvas() {
            public void paint(Graphics g) {
                double temps = Math.sin(System.currentTimeMillis()*200);

                if(tetris.getEcran()==EcranTetris.JeuSolo){
                    if(!modele.getGolmon()){
                        g.drawImage(fond, 0, 0, getWidth(), getHeight(), this);
                    }
                    else {
                        g.drawImage(fondGolmon, 0, 0, getWidth(), getHeight(), this);
                    }
                    taille_w = (32 * getWidth()) / app_width;
                    taille_h = (32 * getHeight()) / app_height;

                    int origine_grille_x = (275 * getWidth()) / app_width;
                    int origine_grille_y = (109 * getHeight()) / app_height;

                    // AFFICHAGE GRILLE
                    for (int i = 0; i < Modele.Grille.TAILLE_GRILLE_W; i++) {
                        for (int j = 0; j < Modele.Grille.TAILLE_GRILLE_H; j++) {
                            if (!modele.getGrille().getCase(i, j).estVide()) {
                                Image image = choixImage(modele.getGrille().getCase(i, j));
                                if (image != null) {
                                    g.drawImage(image, origine_grille_x + i * taille_w, origine_grille_y + j * taille_h, taille_w, taille_h, this);
                                }
                            }
                        }
                    }
                    // AFFICHAGE PIECE ACTUELLE + PROJECTION + PIECE SUIVANTE
                    Graphics2D g2d = (Graphics2D) g;

                    int ajoutX = (715 * getWidth()) / app_width;
                    int ajoutY = ((117 + 58) * getHeight()) / app_height;

                    for (int i = 0; i < Modele.Piece.TAILLE_PIECE; i++) {
                        Image image = choixImage(modele.getPieceActuelle().getCase(i));
                        if (image != null) {
                            g2d.drawImage(image, origine_grille_x + modele.getPieceActuelle().getCase(i).getX() * taille_w, origine_grille_y + modele.getPieceActuelle().getCase(i).getY() * taille_h, taille_w, taille_h, this);
                            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.30f)); // Changer 0.5f pour l'opacité désirée
                            g2d.fillRect(origine_grille_x + modele.getProjection().getCase(i).getX() * taille_w, origine_grille_y + modele.getProjection().getCase(i).getY() * taille_h, taille_w, taille_h);
                            // g2d.drawImage(image, origine_grille_x + modele.getProjection().getCase(i).getX() * taille_w, origine_grille_y + modele.getPieceActuelle().getCase(i).getY() * taille_h, taille_w, taille_h, this);
                            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Rétablir l'opacité normale
                        }
                        Image image2 = choixImage(modele.getFuturPiece().getCase(i));
                        if (image2 != null) {
                            g2d.drawImage(image2, ajoutX + modele.getFuturPiece().getCase(i).getX() * taille_w, ajoutY + modele.getFuturPiece().getCase(i).getY() * taille_h, taille_w, taille_h, this);
                        }

                    }

                    // AFFICHAGE SCORE ET LEVEL

                    int scoreX = (815 * getWidth()) / app_width;
                    int scoreY = (575 * getHeight()) / app_height;
                    dessinerTexte(g,String.valueOf(modele.getScore()),scoreX,scoreY,Color.white,pressStartFont);

                    int levelX = (910 * getWidth()) / app_width;
                    int levelY = (415 * getHeight()) / app_height;
                    dessinerTexte(g,String.valueOf(modele.getLevel()),levelX,levelY,Color.white,pressStartFont);
                }
                else if(tetris.getEcran()==EcranTetris.GameOver){
                    g.drawImage(gameOver, 0, 0, getWidth(), getHeight(), this);
                    dessinerTexte(g,String.valueOf(modele.getScore()),((580 * getWidth()) / app_width),((400 * getHeight()) / app_height),Color.white,pressStartFont);
                    afficherBouton(temps,g,435,566);
                }
                else if(tetris.getEcran()==EcranTetris.PauseSolo) {
                    g.drawImage(menuPause,0,0,getWidth(),getHeight(),this);
                    switch (tetris.getChoix_option_ecran())
                    {
                        case 0 -> { afficherBouton(temps,g,475,300);}
                        case 1 -> { afficherBouton(temps,g,460,384);}
                        case 2 -> { afficherBouton(temps,g,460,477);}
                        case 3 -> { afficherBouton(temps,g,435,566);}
                    }
                }
                else if(tetris.getEcran()==EcranTetris.OptionsSolo){
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

    private Image choixImage(Modele.Case c){
        Image im = null;
        switch(c.getCouleur()){
            case 1 -> {
                im = case1;
            }
            case 2 -> {
                im = case2;
            }
            case 3 -> {
                im = case3;
            }
            case 4 -> {
                im = case4;
            }
            case 5 -> {
                im = case5;
            }
            case 6 -> {
                im = case6;
            }
            case 7 -> {
                im = case7;
            }
            default -> {}
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
