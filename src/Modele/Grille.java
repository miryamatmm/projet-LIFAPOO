package Modele;

public class Grille implements Cloneable {
    public static final int TAILLE_GRILLE_W = 10 ;
    public static final int TAILLE_GRILLE_H = 18 ;
    private Case[][] cases_grille ; // cases de la grille

    Grille(){
        cases_grille = new Case[TAILLE_GRILLE_W][TAILLE_GRILLE_H] ;
        for (int i = 0; i < TAILLE_GRILLE_W; i++){
            for(int j = 0; j < TAILLE_GRILLE_H; j++) {
                cases_grille[i][j] = new Case(i,j);
            }
        }
    }

    public Case getCase(int i,int j){
        return  cases_grille[i][j];
    }

    public void initialiser(){
        for(int i = 0; i < TAILLE_GRILLE_W; i++){
            for(int j = 0; j < TAILLE_GRILLE_H; j++){
                cases_grille[i][j].initCase(i,j);
            }
        }
    }

    public boolean collisionPiece (Piece p){
        for (int i = 0; i < Piece.TAILLE_PIECE; i++){
            if((p.cases_piece[i].getY()<0 || p.cases_piece[i].getY()>=Grille.TAILLE_GRILLE_H)
                    ||(p.cases_piece[i].getX()<0 || p.cases_piece[i].getX()>=Grille.TAILLE_GRILLE_W)
                    ||(!(cases_grille[p.cases_piece[i].getX()][p.cases_piece[i].getY()].estVide()))){
                return true;
            }
        }
        return false;
    }

    public int updateLignes(){
        int n = 0;
        for(int i = 0; i < TAILLE_GRILLE_H; i++){
            int s = 0;
            for(int j = 0; j < TAILLE_GRILLE_W; j++){
                if(!cases_grille[j][i].estVide()){
                    s++;
                }
            }
            if(s==TAILLE_GRILLE_W){
                supprimerLignes(i);
                n++;
            }
        }
        return n;
    }

    public void supprimerLignes(int y){
        for(int j = y ; j > 0; j--){
            for(int i = 0; i < TAILLE_GRILLE_W; i ++){
                int c = cases_grille[i][j - 1].getCouleur();
                boolean v = cases_grille[i][j - 1].estVide();
                cases_grille[i][j].setCase(i,j,c,v);
            }
        }
        for(int t = 0; t < TAILLE_GRILLE_W; t++){
            cases_grille[t][0].initCase(t,0);
        }
    }

    public void placerPiece(Piece p){
        for (int i = 0; i < Piece.TAILLE_PIECE; i++){
            cases_grille[p.cases_piece[i].getX()][p.cases_piece[i].getY()].setVide(false);
            cases_grille[p.cases_piece[i].getX()][p.cases_piece[i].getY()].setCouleur(p.getCouleur());
        }
    }

    public String toString(){
        String s = "";
        for (int i = 0; i < TAILLE_GRILLE_H; i++){
            for (int j = 0; j < TAILLE_GRILLE_W; j++){
                s = s + cases_grille[j][i].toString();
            }
            s = s + "\n";
        }
        return s;
    }

    public Grille clone(){
        Grille newGrille = new Grille();
        for (int i = 0; i < TAILLE_GRILLE_W; i++){
            for (int j = 0; j < TAILLE_GRILLE_H; j++){
                newGrille.cases_grille[i][j].setCase(i,j,this.cases_grille[i][j].getCouleur(),this.cases_grille[i][j].estVide());
            }
        }
        return newGrille;
    }
}
