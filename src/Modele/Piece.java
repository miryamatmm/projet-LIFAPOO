package Modele;

public abstract class Piece implements Cloneable{

    public static final int TAILLE_PIECE = 4 ;
    protected Case[] cases_piece ; 
    protected int couleur ; // couleur de la piece
    protected DirectionPiece direction; 

    public Piece(){
        cases_piece = new Case[TAILLE_PIECE] ;
        for(int i = 0; i < TAILLE_PIECE ; i++){
            cases_piece[i] = new Case();
        }
        direction = DirectionPiece.Est;
    }

    Piece(int _x,int _y){
        cases_piece = new Case[TAILLE_PIECE] ;
        for(int i = 0; i < TAILLE_PIECE ; i++){
            cases_piece[i] = new Case();
        }
        cases_piece[0].setCase(_x,_y,0,true);
        direction = DirectionPiece.Est;
    }

    public Case getCase(int i){
        return cases_piece[i];
    }
    public int getCouleur(){
        return couleur;
    }

    public void tomber(){
        for(int i = 0; i < TAILLE_PIECE ; i++){
            cases_piece[i].setY(cases_piece[i].getY()+1);
        }
    }

    public void remonter(){
        for(int i = 0; i < TAILLE_PIECE ; i++){
            cases_piece[i].setY(cases_piece[i].getY()-1);
        }
    }

    public String toString(){
        String s = "";
        for(int i = 0; i < TAILLE_PIECE ; i++){
            s = s + "[" + cases_piece[i].getX() + "," + cases_piece[i].getY() + "," ;
            s = s + cases_piece[i].getCouleur() + "]";
        }
        return s;
    }
    @Override
    public Piece clone() {
        Piece p = null;
        if(this instanceof PieceI){
            p = new PieceI(this.cases_piece[0].getX(),this.cases_piece[0].getY());
        }
        else if(this instanceof PieceJ){
            p = new PieceJ(this.cases_piece[0].getX(),this.cases_piece[0].getY());
        }
        else if(this instanceof PieceL){
            p = new PieceL(this.cases_piece[0].getX(),this.cases_piece[0].getY());
        }
        else if(this instanceof PieceO){
            p = new PieceO(this.cases_piece[0].getX(),this.cases_piece[0].getY());
        }
        else if(this instanceof PieceS){
            p = new PieceS(this.cases_piece[0].getX(),this.cases_piece[0].getY());
        }
        else if(this instanceof PieceT){
            p = new PieceT(this.cases_piece[0].getX(),this.cases_piece[0].getY());
        }
        else if(this instanceof PieceZ){
            p = new PieceZ(this.cases_piece[0].getX(),this.cases_piece[0].getY());
        }
        p.direction = this.direction; 
        for(int i = 0; i < TAILLE_PIECE; i++){ // Pour mettre les même coord dans la nouvelle pièce
            p.cases_piece[i].setCase(this.cases_piece[i].getX(),this.cases_piece[i].getY(),this.cases_piece[i].getCouleur(),true);
        }
        return p;
    }

    public void deplacer(int choix){
        switch (choix){
            case 1 -> { // droite
                for (int i = 0; i < TAILLE_PIECE ; i ++){
                    cases_piece[i].setCase(cases_piece[i].getX() + 1,cases_piece[i].getY(),cases_piece[i].getCouleur(),true);
                }
            }
            case 2 -> { // gauche
                for (int i = 0; i < TAILLE_PIECE ; i ++){
                    cases_piece[i].setCase(cases_piece[i].getX() - 1,cases_piece[i].getY(),cases_piece[i].getCouleur(),true);
                }
            }
        }
    }

    public void rotation(){
        for (int i = 0; i < TAILLE_PIECE; i++) cases_piece[i].setCouleur(couleur);
    }
}
