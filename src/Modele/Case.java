package Modele;

public class Case {
    private int x; // position en X
    private int y; // position en Y
    private int couleur; // code couleur
    private boolean vide; // case vide ou occupée

    public Case(){
        x = 0 ;
        y = 0 ;
        couleur = 0 ;
        vide = true ;
    }

    public Case(int _x, int _y){
        x = _x ;
        y = _y ;
        couleur = 0 ;
        vide = true ;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getCouleur(){
        return couleur;
    }
    public boolean estVide(){
        return vide;
    }

    public void setX(int _x){ x = _x ; }
    public void setY(int _y){ y = _y ; }
    public void setCouleur(int _c){ couleur = _c; }
    public void setVide(boolean _v){ vide = _v; }

    public void setCase(int _x, int _y, int _c, boolean _v){
        setX(_x);
        setY(_y);
        setCouleur(_c);
        setVide(_v);
    }
    public void initCase(){ setCase(0,0,0,true);}
    public void initCase(int _x, int _y){ setCase(_x,_y,0,true);}

    public String toString(){
        return "(" + x + "," + y + "," + couleur + "," + vide + ")";
    }
}
