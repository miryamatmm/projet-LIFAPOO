package Modele;
import java.util.Random;

public class Tools {
    public int randomInt(int min, int max){
        if (min > max) {
            throw new IllegalArgumentException("Le minimum doit être inférieur ou égal au maximum.");
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;

    }

    public int max(int x, int y){
        if(x > y){ return x; }
        else { return y; }
    }

    public int min(int x, int y){
        if(x < y){ return x; }
        else { return y; }
    }
}
