package minebustergame;

import util.Perlin;

public class MineBusterGame {

    public static void main(String[] args) {
                int octaves = 2;
        float normalise = 0.62f;
        Perlin perlin = new Perlin(32,32,5,0.62f);
        perlin.paintImage(new float[32][32]);
    }
}
