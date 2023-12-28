package VueControleur;

import Modele.EcranTetris;
import Modele.JeuSolo;
import Modele.Tetris;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;


public class VueMenu extends JPanel implements Observer{

    private Tetris modele;
    private Canvas c;
    private Image menuPrincipal;
    private Image menuPlayer;
    private Image options;
    private Image leaderboard;
    private Image run;
    private Image fleche;
    private Image classic;
    private Image battle;
    private Font pressStartFont;
    private final static int app_width = 1280;
    private final static int app_height = 800;

    private Color vert;
    public VueMenu(Tetris _modele){
        modele = _modele;
        setLayout(new BorderLayout());

        ImageIcon runIcon = new ImageIcon(getClass().getResource("../data/images/run.png"));
        run = runIcon.getImage();

        ImageIcon menuPrincipalIcon = new ImageIcon(getClass().getResource("../data/images/menu.png"));
        menuPrincipal = menuPrincipalIcon.getImage();

        ImageIcon menuPlayerIcon = new ImageIcon(getClass().getResource("../data/images/player.png"));
        menuPlayer = menuPlayerIcon.getImage();

        ImageIcon LeaderBoardIcon = new ImageIcon(getClass().getResource("../data/images/leaderboard.png"));
        leaderboard = LeaderBoardIcon.getImage();

        ImageIcon OptionsIcon = new ImageIcon(getClass().getResource("../data/images/options.png"));
        options = OptionsIcon.getImage();

        ImageIcon flecheIcon = new ImageIcon(getClass().getResource("../data/images/fleche.png"));
        fleche = flecheIcon.getImage();

        ImageIcon classicIcon = new ImageIcon(getClass().getResource("../data/images/classic.png"));
        classic = classicIcon.getImage();

        ImageIcon battleIcon = new ImageIcon(getClass().getResource("../data/images/battle.png"));
        battle = battleIcon.getImage();


        pressStartFont = loadFont("src/data/PressStart2P-Regular.ttf").deriveFont(40f);
        vert = new Color(37, 195, 41);

        c = new Canvas() {
            public void paint(Graphics g) {
                double temps = Math.sin(System.currentTimeMillis()*200);

                switch(modele.getEcran()){
                    case Run : {
                        g.drawImage(run,0,0,getWidth(),getHeight(),this);
                    }
                        break;
                    case MainMenu : {
                        g.drawImage(menuPrincipal,0,0,getWidth(),getHeight(),this);
                        switch (modele.getChoix_option_ecran())
                       {
                           case 0 -> { afficherBouton(temps,g,515,343);}
                           case 1 -> { afficherBouton(temps,g,380,410);}
                           case 2 -> { afficherBouton(temps,g,453,474);}
                           case 3 -> { afficherBouton(temps,g,518,538);}
                       }
                    }
                        break;
                    case PlayMenu : {
                        g.drawImage(menuPlayer,0,0,getWidth(),getHeight(),this);
                        switch (modele.getChoix_option_ecran())
                        {
                            case 0 -> { afficherBouton(temps,g,451,318);}
                            case 1 -> { afficherBouton(temps,g,460,492);}
                        }
                    }
                        break;
                    case Classic: {
                        g.drawImage(classic,0,0,getWidth(),getHeight(),this);
                        switch (modele.getChoix_option_ecran())
                        {
                            case 0 -> { afficherBouton(temps,g,505,313);}
                            case 1 -> { afficherBouton(temps,g,350,492);}
                        }
                    }
                    break;
                    case Battle: {
                        g.drawImage(battle,0,0,getWidth(),getHeight(),this);
                        switch (modele.getChoix_option_ecran())
                        {
                            case 0 -> { afficherBouton(temps,g,300,313);}
                            case 1 -> { afficherBouton(temps,g,285,492);}
                        }
                    }
                    break;
                    case Options :  {
                        g.drawImage(options,0,0,getWidth(),getHeight(),this);
                        switch (modele.getChoix_option_ecran())
                        {
                            case 0 -> { afficherBouton(temps,g,503,370);}
                            case 1 -> { afficherBouton(temps,g,490,420);}
                            case 2 -> { afficherBouton(temps,g,547,471);}
                            case 3 -> { afficherBouton(temps,g,467,611);}
                        }
                    }
                        break;
                    case LeaderBoard : {
                        g.drawImage(leaderboard,0,0,getWidth(),getHeight(),this);
                        for(int i=0; i< JeuSolo.NB_SCORE_SAUV; i++){
                            dessinerTexte(g,String.valueOf(modele.getTabScoreSolo(i)),(560 * getWidth())/app_width,((349 + i * 66)*getHeight())/app_height,Color.white,pressStartFont);
                        }
                        afficherBouton(temps,g,470,681);
                    }
                        break;
                }
            }
        };
        add(c, BorderLayout.CENTER);
    }

    @Override
    public void update(Observable o, Object arg) {
        BufferStrategy bs = c.getBufferStrategy(); // bs + dispose + show : double buffering pour éviter les scintillements
        if (bs == null) {
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

    private static Font loadFont(String path) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new java.io.File(path));
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, 12); // Utiliser une police de secours
        }
    }

}