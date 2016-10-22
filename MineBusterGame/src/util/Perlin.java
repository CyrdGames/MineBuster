package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Perlin {

    int seed = (int) (Math.random() * 10000000) + 1000000;
    int width = 32;
    int height = 32;
    int octaves = 4;
    float normalise = 0.60f;

    public Perlin(int width, int height, int octaves, float normalise) {
        this.width = width;
        this.height = height;
        this.octaves = octaves;
        this.normalise = normalise;
    }

    public int[][] paintImage(float[][] grid) {
        BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();

//        paintTransition(g, width, height, grid);
        int[][] mask = paintSolid(g, width, height, grid);
        
        try {
            ImageIO.write(im, "png", new File("booleanNoise.png"));
        } catch (IOException ex) {
            System.out.println("error in drawing");
        }

        return mask;
    }

    public float[][] getData() {
        float[][] noise = randomNoise(width, height);
        float[][] grid = getPerlin(noise);
        return grid;
    }

    private float getAverage(float[][] grid) {
        float avg = 0;

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                avg += grid[row][col];
            }
        }

        return avg / width / height;
    }

    public void paintTransition(Graphics2D g, int width, int height, float[][] grid) {
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                g.setPaint(getColour(grid[row][col]));
                g.fillRect(row, col, 1, 1);
            }
        }
    }

    public int[][] paintSolid(Graphics2D g, int width, int height, float[][] grid) {
        float avg = getAverage(grid);
        int[][] mask = new int[width][height];

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                if (grid[row][col] > avg) {
                    mask[row][col] = 0;
                    g.setPaint(Color.ORANGE);
                } else {
                    mask[row][col] = 1;
                    g.setPaint(Color.GREEN);
                }

                g.fillRect(row, col, 1, 1);
            }
        }

        return mask;
    }

    public Color getColour(float col) {
        float c = 1 - col;

        Color start = new Color(0x606060);
        Color end = new Color(0xbebebe);

        int r = (int) (start.getRed() * c + end.getRed() * col);
        int g = (int) (start.getGreen() * c + end.getGreen() * col);
        int b = (int) (start.getBlue() * c + end.getBlue() * col);

        int color = (r << 16) | (g << 8) | (b);

        Color colour = new Color(color);
        return colour;
    }

    public float[][] randomNoise(int width, int height) {
        Random random = new Random(width + height * 104513 + seed);
        float[][] noise = new float[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                noise[i][j] = (float) random.nextFloat();
            }
        }

        return noise;
    }

    public float[][] smooth(float[][] noise, int octave) {
        float[][] smoothNoise = new float[width][height];

        int period = (int) Math.pow(2, octave);
        float frequency = 1.0f / period;
        for (int i = 0; i < width; i++) {

            int i0 = (i / period) * period;
            int i1 = (i0 + period) % width;
            float hb = (i - i0) * frequency;

            for (int j = 0; j < height; j++) {
                int j0 = (j / period) * period;
                int j1 = (j0 + period) % height;
                float vb = (j - j0) * frequency;

                float top = linearInterpolate(noise[i0][j0], noise[i1][j0], hb);
                float bottom = linearInterpolate(noise[i0][j1], noise[i1][j1], hb);

                smoothNoise[i][j] = linearInterpolate(top, bottom, vb);
            }
        }

        return smoothNoise;
    }

    public float[][] getPerlin(float[][] noise) {
        float[][][] smoothNoise = new float[octaves][][];

        for (int i = 0; i < octaves; i++) {
            smoothNoise[i] = smooth(noise, i);
        }

        float[][] perlinNoise = new float[width][height];
        float amplitude = 1.0f;
        float totalAmplitude = 0.0f;

        for (int oct = octaves - 1; oct >= 0; oct--) {
//        for(int oct = 0; oct < octave; oct++) {
            amplitude *= normalise;
            totalAmplitude += amplitude;

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    perlinNoise[i][j] += smoothNoise[oct][i][j] * amplitude;
                }
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                perlinNoise[i][j] /= totalAmplitude;
            }
        }

        return perlinNoise;
    }

    public float linearInterpolate(float x0, float x1, float a) {
        return x0 * (1 - a) + a * x1;
    }
}
