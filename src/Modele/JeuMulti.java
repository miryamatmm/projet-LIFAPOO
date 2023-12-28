package Modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class JeuMulti extends Observable implements Observer{

    private Grille grille1;
    private Grille grille2;

    private Piece[] pieces1 ; // tab de 3 pieces (actuelle,projection_actuelle,suivante)
    private Piece[] pieces2 ; // tab de 3 pieces (actuelle,projection_actuelle,suivante)
    private int nbPiecePosee1;
    private int nbPiecePosee2;
    private int nbRotationPiece1;
    private int nbRotationPiece2;
    private long lastRotation1;
    private long lastRotation2;
    private long vitessePiece;
    private static final int vitessePieceInitiale = 700;

    private int nbLigneDetruite1;
    private int nbLigneDetruite2;

    private boolean fin;
    private int resultatPartie;
    private long lastUpdate;
    public static final int nbLigneGagnant = 15;
    private Tools tools;

    private boolean golmon;
    private List<Piece> positionPossibles;
    private int meilleurePos;


    public JeuMulti(){
        grille1 = new Grille();
        grille2 = new Grille();
        pieces1 = new Piece[3];
        pieces2 = new Piece[3];
        tools = new Tools();
        golmon = false;
        initialiser();
    }

    public void initialiser(){
        grille1.initialiser();
        pieces1[0] = tirerPiece();
        projeterPiece(1);
        pieces1[2] = tirerPiece();
        nbPiecePosee1 = 0;
        nbRotationPiece1 = 0;
        lastRotation1 = System.currentTimeMillis();
        nbLigneDetruite1 = 0;

        grille2.initialiser();
        pieces2[0] = tirerPiece();
        trouverPosGolmon();
        projeterPiece(2);
        pieces2[2] = tirerPiece();
        nbPiecePosee2 = 0;
        nbRotationPiece2 = 0;
        lastRotation2 = System.currentTimeMillis();
        nbLigneDetruite2 = 0;

        vitessePiece = vitessePieceInitiale;
        lastUpdate = System.currentTimeMillis();
        fin = false;
        resultatPartie = 0;
    }

    public Grille getGrille1(){ return grille1; }
    public Grille getGrille2(){ return grille2; }

    public Piece getPieceActuelle1(){ return pieces1[0]; }
    public Piece getProjection1(){return pieces1[1]; }
    public Piece getFuturPiece1(){ return pieces1[2]; }

    public Piece getPieceActuelle2(){ return pieces2[0]; }
    public Piece getProjection2(){return pieces2[1]; }
    public Piece getFuturPiece2(){ return pieces2[2]; }

    public int getNbLigneDetruite1(){ return nbLigneDetruite1; }
    public int getNbLigneDetruite2(){ return nbLigneDetruite2; }
    public boolean getFin(){ return fin; }

    public int getResultatPartie() { return resultatPartie; }

    public boolean getGolmon() { return golmon; }
    public void setGolmon(boolean g) { golmon = g; }

    public void deplacerPiece(int joueur,int choix){
        if(!fin){
            if(joueur == 1){
                Piece newPiece = pieces1[0].clone();
                newPiece.deplacer(choix);
                if (!grille1.collisionPiece(newPiece)){
                    pieces1[0] = newPiece;
                    projeterPiece(1);
                }else{
                    // System.out.println("déplacement impossible j1");
                }
            }
            if(joueur == 2){
                Piece newPiece = pieces2[0].clone();
                newPiece.deplacer(choix);
                if (!grille2.collisionPiece(newPiece)){
                    pieces2[0] = newPiece;
                    projeterPiece(2);
                }else{
                    // System.out.println("déplacement impossible j2");
                }
            }
            setChanged();
            notifyObservers();
        }
    }

    public void rotationPiece(int joueur){
        if(!fin){
            if(joueur == 1){
                Piece newPiece = pieces1[0].clone();
                newPiece.rotation();
                if (!grille1.collisionPiece(newPiece)){
                    pieces1[0] = newPiece;
                    projeterPiece(1);
                    nbRotationPiece1 ++;
                    lastRotation1 = System.currentTimeMillis();
                }else{
                    // System.out.println("rotation impossible j1");
                }
            }
            if(joueur == 2){
                Piece newPiece = pieces2[0].clone();
                newPiece.rotation();
                if (!grille2.collisionPiece(newPiece)){
                    pieces2[0] = newPiece;
                    projeterPiece(2);
                    nbRotationPiece2 ++;
                    lastRotation2 = System.currentTimeMillis();
                }else{
                    // System.out.println("rotation impossible j2");
                }
            }
            setChanged();
            notifyObservers();
        }
    }

    public void tomberPiece(int joueur){
        if(!fin){
            if(joueur == 1){
                Piece newPiece = pieces1[0].clone();
                newPiece.tomber();
                if (!grille1.collisionPiece(newPiece)) {
                    pieces1[0] = newPiece;
                    projeterPiece(1);
                } else {
                    grille1.placerPiece(pieces1[0]);
                    nbPiecePosee1++;
                    int n = grille1.updateLignes();
                    nbLigneDetruite1 += n;
                    pieces1[0] = pieces1[2];
                    if(golmon){
                        trouverPosGolmon();
                    }
                    projeterPiece(1);
                    pieces1[2] = tirerPiece();
                }
            }
            if(joueur == 2){
                Piece newPiece = pieces2[0].clone();
                newPiece.tomber();
                if (!grille2.collisionPiece(newPiece)) {
                    pieces2[0] = newPiece;
                    projeterPiece(2);
                } else {
                    grille2.placerPiece(pieces2[0]);
                    nbPiecePosee2++;
                    int n = grille2.updateLignes();
                    nbLigneDetruite2 += n;
                    pieces2[0] = pieces2[2];
                    projeterPiece(2);
                    pieces2[2] = tirerPiece();
                }
            }
            setChanged();
            notifyObservers();
        }
    }
    public void update(){
        if(!fin){
            if ((System.currentTimeMillis() - lastUpdate) - vitessePiece > 0) {
                if(nbRotationPiece1 > 4 || lastRotation1 > 200){
                    if(!grille1.collisionPiece(pieces1[0])){
                        tomberPiece(1);
                        nbRotationPiece1 = 0;
                    }
                    else{
                        fin = true;
                        resultatPartie = 2;
                        setChanged();
                        notifyObservers();
                    }
                }
                if(nbRotationPiece2 > 4 || lastRotation2 > 200){
                    if(!grille2.collisionPiece(pieces2[0])){
                        if(golmon){
                            golmonPlay();
                        }
                        tomberPiece(2);
                        nbRotationPiece2 = 0;
                    }
                    else{
                        fin = true;
                        resultatPartie = 1;
                        setChanged();
                        notifyObservers();
                    }
                }
                if(nbLigneDetruite1 >= nbLigneGagnant){
                    fin = true;
                    if(nbLigneDetruite2 >= nbLigneGagnant){
                        resultatPartie = 3;
                    }
                    else{
                        resultatPartie = 1;
                    }
                    setChanged();
                    notifyObservers();
                }
                else if(nbLigneDetruite2 >= nbLigneGagnant){
                    resultatPartie = 2;
                    fin = true;
                    setChanged();
                    notifyObservers();
                }

                lastUpdate = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void update(Observable o,Object arg){
        setChanged();
        notifyObservers();
    }

    private void projeterPiece(int joueur){
        if(joueur == 1){
            Piece newProj = pieces1[0].clone();
            newProj.tomber();
            if(!grille1.collisionPiece(newProj)){
                do{
                    pieces1[1] = newProj.clone();
                    newProj.tomber();
                }
                while(!grille1.collisionPiece(newProj));
            }
        }
        else if(joueur == 2){
            Piece newProj = pieces2[0].clone();
            newProj.tomber();
            if(!grille2.collisionPiece(newProj)){
                do{
                    pieces2[1] = newProj.clone();
                    newProj.tomber();
                }
                while(!grille2.collisionPiece(newProj));
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


    private void golmonPlay(){
        modifierPieceGolmon(positionPossibles.get(meilleurePos));
    }
    private void modifierPieceGolmon(Piece p_voulu){
        if(pieces1[0].direction != p_voulu.direction){
            rotationPiece(1);
        }
        else if(pieces1[0].cases_piece[0].getX() < p_voulu.cases_piece[0].getX()){
            deplacerPiece(1,1);
        }
        else if(pieces1[0].cases_piece[0].getX() > p_voulu.cases_piece[0].getX()){
            deplacerPiece(1,2);
        }
        else{
            tomberPiece(1);
        }
    }

    private void trouverPosGolmon(){
        positionPossibles = genererPositionsPossibles(pieces1[0],grille1);
        //for(int j=0; j<positionPossibles.size();j++) System.out.println(positionPossibles.get(j).toString());
        meilleurePos = choisirMeilleurePosition(positionPossibles,grille1);
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


