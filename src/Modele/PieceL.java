package Modele;

public class PieceL extends Piece{

    //          -----
    //          |   |
    //  -------------
    //  |   |   |   |
    //  -------------

    PieceL(int _x, int _y){
        super(_x,_y); // appel du constructeur de la classe mère
        cases_piece[1].initCase(_x + 1 , _y - 1);
        cases_piece[2].initCase(_x + 1, _y);
        cases_piece[3].initCase(_x - 1, _y);
        couleur = 3;
        for (int i = 0; i < TAILLE_PIECE; i++) cases_piece[i].setCouleur(couleur);
    }

    public void rotation(){
        int _x = cases_piece[0].getX();
        int _y = cases_piece[0].getY();
        switch (direction){
            case Est -> {
                direction = DirectionPiece.Nord;
                cases_piece[1].initCase(_x - 1, _y - 1);
                cases_piece[2].initCase(_x , _y - 1);
                cases_piece[3].initCase(_x , _y + 1 );
            }
            case Nord -> {
                direction = DirectionPiece.Ouest;
                cases_piece[1].initCase(_x - 1 , _y + 1);
                cases_piece[2].initCase(_x - 1, _y);
                cases_piece[3].initCase(_x + 1, _y);
            }
            case Ouest -> {
                direction = DirectionPiece.Sud;
                cases_piece[1].initCase(_x + 1, _y + 1);
                cases_piece[2].initCase(_x , _y + 1);
                cases_piece[3].initCase(_x , _y - 1 );
            }
            case Sud -> {
                direction = DirectionPiece.Est;
                cases_piece[1].initCase(_x + 1 , _y - 1);
                cases_piece[2].initCase(_x + 1, _y);
                cases_piece[3].initCase(_x - 1, _y);
            }
        }
        super.rotation();
    }
}