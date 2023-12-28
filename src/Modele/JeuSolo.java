package Modele;

import java.awt.desktop.SystemSleepEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

public class JeuSolo extends Observable{

    public static final int NB_SCORE_SAUV = 5;
    private Grille grille;

    private Piece[] pieces ; // tab de 3 pieces (actuelle,projection_actuelle,suivante)
    private int nbPiecePosee;
    private int nbRotationPiece;
    private long lastRotation;
    private long vitessePiece;
    private static final int vitessePieceInitiale = 700;

    private int nbLigneDetruite;
    private long score;
    private int level;

    private boolean fin;
    private long lastUpdate;

    private Tools tools;

    private boolean golmon;
    private List<Piece> positionPossibles;
    private int meilleurePos;


    public JeuSolo(){
        grille = new Grille();
        pieces = new Piece[3];
        tools = new Tools();
        golmon = false;
        initialiser();
    }

    public void initialiser(){
        grille.initialiser();
        pieces[0] = tirerPiece();
        trouverPosGolmon();
        projeterPiece();
        pieces[2] = tirerPiece();
        nbPiecePosee = 0;
        nbRotationPiece = 0;
        lastRotation = System.currentTimeMillis();
        vitessePiece = vitessePieceInitiale;
        nbLigneDetruite = 0;
        score = 0;
        level = 0;
        fin = false;
        lastUpdate = System.currentTimeMillis();
    }

    public Grille getGrille(){ return grille; }

    public Piece getPieceActuelle(){ return pieces[0]; }
    public Piece getProjection(){return pieces[1]; }
    public Piece getFuturPiece(){ return pieces[2]; }

    public boolean getFin(){ return fin; }
    public int getNbLigneDetruite(){ return nbLigneDetruite; }

    public long getScore(){ return score; }
    public int getLevel(){ return level; }

    public boolean getGolmon() { return golmon; }
    public void setGolmon(boolean g) { golmon = g; }

    public void deplacerPiece(int choix){
        if(!fin){
            Piece newPiece = pieces[0].clone();
            newPiece.deplacer(choix);
            if (!grille.collisionPiece(newPiece)){
                pieces[0] = newPiece;
                projeterPiece();
            }
            setChanged();
            notifyObservers();
        }
    }

    public void rotationPiece(){
        if(!fin){
            Piece newPiece = pieces[0].clone();
            newPiece.rotation();
            if (!grille.collisionPiece(newPiece)){
                pieces[0] = newPiece;
                projeterPiece();
                nbRotationPiece ++;
                lastRotation = System.currentTimeMillis();
            }
            setChanged();
            notifyObservers();
        }
    }

    public void tomberPiece(){
        if(!fin){
            Piece newPiece = pieces[0].clone();
            newPiece.tomber();
            if (!grille.collisionPiece(newPiece)) {
                pieces[0] = newPiece;
                projeterPiece();
            } else {
                grille.placerPiece(pieces[0]);
                nbPiecePosee++;
                int n = grille.updateLignes();
                updateScoreAndLevel(n);
                nbLigneDetruite += n;
                pieces[0] = pieces[2];
                if(golmon){
                    trouverPosGolmon();
                }
                projeterPiece();
                pieces[2] = tirerPiece();

            }
            setChanged();
            notifyObservers();
        }
    }

    public void hard_drop() { // faire tomber la piece et poser direct
        Piece newProj = pieces[0].clone();
        newProj.tomber();
        if (!grille.collisionPiece(newProj)) {
            do {
                pieces[0] = newProj.clone();
                newProj.tomber();
                setChanged();
                notifyObservers();
            }
            while (!grille.collisionPiece(newProj));
            grille.placerPiece(pieces[0]);
            nbPiecePosee++;
            int n = grille.updateLignes();
            updateScoreAndLevel(n);
            nbLigneDetruite += n;
            pieces[0] = pieces[2];
            projeterPiece();
            pieces[2] = tirerPiece();
            setChanged();
            notifyObservers();
        }
    }

    public void update(){
        if(!fin){
            if ((System.currentTimeMillis() - lastUpdate) - vitessePiece > 0) {
                if(nbRotationPiece > 4 || lastRotation > 200){
                    if(!grille.collisionPiece(pieces[0])){
                        if(golmon){
                            golmonPlay();
                        }
                        tomberPiece();
                        nbRotationPiece = 0;
                    }
                    else{
                        fin = true;
                        setChanged();
                        notifyObservers();
                    }
                }
                lastUpdate = System.currentTimeMillis();
            }
        }
    }

    private Piece tirerPiece(){
        Piece p;
        int i = tools.randomInt(0,6);
        int x = 4;
        int y = 1;
        p = switch (i) {
            case 0 -> new PieceI(x, y);
            case 1 -> new PieceJ(x, y);
            case 2 -> new PieceL(x, y);
            case 3 -> new PieceO(x, y);
            case 4 -> new PieceS(x, y);
            case 5 -> new PieceT(x, y);
            case 6 -> new PieceZ(x, y);
            default -> throw new IllegalStateException("Numéro de piece inconnu : " + i);
        };
        return p;
    }

    private void projeterPiece(){
        Piece newProj = pieces[0].clone();
        newProj.tomber();
        if(!grille.collisionPiece(newProj)){
            do{
                pieces[1] = newProj.clone();
                newProj.tomber();
            }
            while(!grille.collisionPiece(newProj));
        }
    }

    private void updateScoreAndLevel(int n){
        switch (n){
            case 1 -> { score += 40L * (level+1);}
            case 2 -> { score += 100L * (level+1);}
            case 3 -> { score += 300L * (level+1);}
            case 4 -> { score += 1200L * (level+1);}
            default -> {}
        }
        if(score > 1000){
            level = 1;
        }
        if(score > 2000){
            level = 2;
        }
        if(score > 3000){
            level = 3;
        }
        if(score > 4000){
            level = 4;
        }
        vitessePiece = tools.max(0,vitessePieceInitiale - (level*150));
    }


    public int[] lireScore() {
        int[] tab = new int[5];
        try {
            File file = new File("src/data/scores.txt");
            Scanner sc = new Scanner(file) ;
            String line = sc.nextLine() ;
            String[] tabScore = line.split(" ") ;

            for (int i=0; i<NB_SCORE_SAUV; ++i) {
                tab[i]  = Integer.parseInt(tabScore[i]) ;
            }
            sc.close();

        } catch (FileNotFoundException e) {
            System.out.println("Erreur lors de l'ouverture du fichier : ../data/scores.txt" );
            e.printStackTrace();
        }
        return tab;
    }

    public void ecrireScore(){
        try {
            int[] tab = lireScore() ;
            int[] newTab = new int [6];
            for(int i=0; i< NB_SCORE_SAUV; i++){
                newTab[i] = tab[i];
            }
            if(!scoreDejaPresent(newTab,(int)score)){
                newTab[5] = (int)score;
            }
            else{
                newTab[5] = 0;
            }
            trierTableau(newTab);
            FileWriter writer = new FileWriter("src/data/scores.txt",false) ;
            for (int i=0; i<NB_SCORE_SAUV; ++i) {  // ecris les 5 premiers
                writer.write(String.valueOf(newTab[i]));
                if (i!=NB_SCORE_SAUV-1)
                    writer.write(" ");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture du fichier :../data/scores.txt" );
            e.printStackTrace();
        }
    }


    public static void trierTableau(int[] tableau) {
        int n = tableau.length;
        boolean estEchange;

        do {
            estEchange = false;

            for (int i = 0; i < n - 1; i++) {
                if (tableau[i] < tableau[i + 1]) {
                    if (tableau[i] == tableau[i + 1]) {
                        tableau[i + 1] = 0;
                    }

                    int temp = tableau[i];
                    tableau[i] = tableau[i + 1];
                    tableau[i + 1] = temp;

                    estEchange = true;
                }
            }
            n--;
        } while (estEchange);
    }

    private boolean scoreDejaPresent(int[] tab, int nouveauScore) {
        for (int score : tab) {
            if (score == nouveauScore) {
                return true;
            }
        }
        return false;
    }


    private void golmonPlay(){
        modifierPieceGolmon(positionPossibles.get(meilleurePos));
    }
    private void modifierPieceGolmon(Piece p_voulu){
        if(pieces[0].direction != p_voulu.direction){
            rotationPiece();
        }
        else if(pieces[0].cases_piece[0].getX() < p_voulu.cases_piece[0].getX()){
            deplacerPiece(1);
        }
        else if(pieces[0].cases_piece[0].getX() > p_voulu.cases_piece[0].getX()){
            deplacerPiece(2);
        }
        else{
            tomberPiece();
        }
    }

    private void trouverPosGolmon(){
        positionPossibles = genererPositionsPossibles(pieces[0],grille);
        //for(int j=0; j<positionPossibles.size();j++) System.out.println(positionPossibles.get(j).toString());
        meilleurePos = choisirMeilleurePosition(positionPossibles,grille);
        //System.out.println(bestPos);
    }

    private static List<Piece> genererPositionsPossibles(Piece piece, Grille grille) {
        List<Piece> positionsFinale = new ArrayList<>();

        if(piece instanceof PieceI){
            for (int x = 1; x < grille.TAILLE_GRILLE_W-2; x++) { // EST OUEST
                Piece pieceClone = piece.clone();
                pieceClone.cases_piece[0].setCase(x,0,pieceClone.couleur,true);
                pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation();
                while (!grille.collisionPiece(pieceClone)) {
                    pieceClone.tomber();
                }
                pieceClone.remonter();
                positionsFinale.add(pieceClone);
            }
            for (int x = 0; x < grille.TAILLE_GRILLE_W; x++) { // NORD SUD
                Piece pieceClone = piece.clone();
                pieceClone.rotation();
                pieceClone.cases_piece[0].setCase(x,1,pieceClone.couleur,true);
                pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation();
                while (!grille.collisionPiece(pieceClone)) {
                    pieceClone.tomber();
                }
                pieceClone.remonter();
                positionsFinale.add(pieceClone);
            }
        }
        else if(piece instanceof PieceJ || piece instanceof PieceL){
            for (int x = 1; x < grille.TAILLE_GRILLE_W-1; x++) { // EST
                Piece pieceClone = piece.clone();
                pieceClone.cases_piece[0].setCase(x,1,pieceClone.couleur,true);
                pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation();
                while (!grille.collisionPiece(pieceClone)) {
                    pieceClone.tomber();
                }
                pieceClone.remonter();
                positionsFinale.add(pieceClone);
            }
            for (int x = 1; x < grille.TAILLE_GRILLE_W; x++) { // NORD
                Piece pieceClone = piece.clone();
                pieceClone.rotation();
                pieceClone.cases_piece[0].setCase(x,1,pieceClone.couleur,true);
                pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation();
                while (!grille.collisionPiece(pieceClone)) {
                    pieceClone.tomber();
                }
                pieceClone.remonter();
                positionsFinale.add(pieceClone);
            }
            for (int x = 1; x < grille.TAILLE_GRILLE_W-1; x++) { // OUEST
                Piece pieceClone = piece.clone();
                pieceClone.rotation(); pieceClone.rotation();
                pieceClone.cases_piece[0].setCase(x,0,pieceClone.couleur,true);
                pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation();
                while (!grille.collisionPiece(pieceClone)) {
                    pieceClone.tomber();
                }
                pieceClone.remonter();
                positionsFinale.add(pieceClone);
            }
            for (int x = 0; x < grille.TAILLE_GRILLE_W-1; x++) { // SUD
                Piece pieceClone = piece.clone();
                pieceClone.rotation();pieceClone.rotation();pieceClone.rotation();
                pieceClone.cases_piece[0].setCase(x,1,pieceClone.couleur,true);
                pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation(); pieceClone.rotation();
                while (!grille.collisionPiece(pieceClone)) {
                    pieceClone.tomber();
                }
                pieceClone.remonter();
                positionsFinale.add(pieceClone);
            }
        }
        else if(piece instanceof PieceO){
            for (int x = 0; x < grille.TAILLE_GRILLE_W-1; x++) { // TOUTES DIRECTIONS
                Piece pieceClone = piece.clone();
                pieceClone.cases_piece[0].setCase(x,0,pieceClone.couleur,true);
                pieceClone.rotation();
                while (!grille.collisionPiece(pieceClone)) {
                    pieceClone.tomber();
                }
                pieceClone.remonter();
                positionsFinale.add(pieceClone);
            }
        }
        else if(piece instanceof PieceS || piece instanceof PieceZ){
            for (int x = 1; x < grille.TAILLE_GRILLE_W-1; x++) { // EST OUEST
                Piece pieceClone = piece.clone();
                pieceClone.cases_piece[0].setCase(x,1,pieceClone.couleur,true);
                pieceClone.rotation();pieceClone.rotation();pieceClone.rotation();pieceClone.rotation();
                while (!grille.collisionPiece(pieceClone)) {
                    pieceClone.tomber();
                }
                pieceClone.remonter();
                positionsFinale.add(pieceClone);
            }
            for (int x = 1; x < grille.TAILLE_GRILLE_W; x++) { // NORD SUD
                Piece pieceClone = piece.clone();
                pieceClone.rotation();
                pieceClone.cases_piece[0].setCase(x,1,pieceClone.couleur,true);
                pieceClone.rotation();pieceClone.rotation();pieceClone.rotation();pieceClone.rotation();
                while (!grille.collisionPiece(pieceClone)) {
                    pieceClone.tomber();
                }
                pieceClone.remonter();
                positionsFinale.add(pieceClone);
            }
        }
        else if(piece instanceof PieceT){
            for (int x = 1; x < grille.TAILLE_GRILLE_W-1; x++) { // EST SUD
                Piece pieceClone = piece.clone();
                pieceClone.cases_piece[0].setCase(x,1,pieceClone.couleur,true);
                pieceClone.rotation();pieceClone.rotation();pieceClone.rotation();pieceClone.rotation();
                while (!grille.collisionPiece(pieceClone)) {
                    pieceClone.tomber();
                }
                pieceClone.remonter();
                positionsFinale.add(pieceClone);

                pieceClone = piece.clone();
                pieceClone.rotation();pieceClone.rotation();pieceClone.rotation();
                pieceClone.cases_piece[0].setCase(x,1,pieceClone.couleur,true);
                pieceClone.rotation();pieceClone.rotation();pieceClone.rotation();pieceClone.rotation();
                while (!grille.collisionPiece(pieceClone)) {
                    pieceClone.tomber();
                }
                pieceClone.remonter();
                positionsFinale.add(pieceClone);
            }
        }
        return positionsFinale;
    }

    private int choisirMeilleurePosition(List<Piece> lp, Grille g){
        int index = 0;
        int[] hauteurPossibles = new int[lp.size()];
        for(int i = 0; i < lp.size(); i++){
            Grille g_possible = g.clone();
            g_possible.placerPiece(lp.get(i));
            hauteurPossibles[i] = calculHauteurGrille(g_possible);
        }
        for(int i = 0; i < lp.size(); i++){
            if(hauteurPossibles[i]<hauteurPossibles[index]) index = i;
        }
        return index;
    }

    private int calculHauteurGrille(Grille grille){
        int h = 0;
        for(int i = 0; i < Grille.TAILLE_GRILLE_W; i++){
            for(int j = 0; j < Grille.TAILLE_GRILLE_H; j++){
                if(!grille.getCase(i,j).estVide()){
                    h += (Grille.TAILLE_GRILLE_H - grille.getCase(i,j).getY());
                }
            }
        }
        return h;
    }

}
