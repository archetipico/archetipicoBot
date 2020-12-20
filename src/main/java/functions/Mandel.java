package functions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Mandel {

    private final int height;
    private final int width;
    private final int maxit;
    private final double centering;
    private final double zoom;
    private final BufferedImage bi;
    private final String model;

    public Mandel(int maxit, double centering, double zoom, BufferedImage bi) {
        this.height = bi.getHeight();
        this.width = bi.getWidth();
        this.maxit = maxit;
        this.centering = centering;
        this.zoom = zoom;
        this.bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_4BYTE_ABGR);
        this.model = "normal";
    }

    public Mandel(int maxit, double centering, double zoom, BufferedImage bi, String model) {
        this.height = bi.getHeight();
        this.width = bi.getWidth();
        this.maxit = maxit;
        this.centering = centering;
        this.zoom = zoom;
        this.bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_4BYTE_ABGR);
        this.model = model;
    }

    public BufferedImage getPlot() {
        Random rnd = new Random();
        int v = rnd.nextInt();
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                double re = (x - this.width/this.centering) * this.zoom/this.width;
                double im = (y - this.height/2.0) * this.zoom/this.width;
                double xx = 0;
                double yy = 0;
                int it = 0;
                while (xx * xx + yy * yy <= this.zoom && it < this.maxit) {
                    double n = xx * xx - yy * yy + re;
                    double xx2;
                    switch (this.model) {
                        case "sin":
                            xx2 = Math.sin(n);
                            break;

                        case "cos":
                            xx2 = Math.cos(n);
                            break;

                        case "tan":
                            xx2 = Math.tan(n);
                            break;

                        case "sqrt":
                            xx2 = Math.sqrt(n);
                            break;

                        case "cbrt":
                            xx2 = Math.cbrt(n);
                            break;

                        case "rand":
                            xx2 = Math.pow(n, new Random().nextInt());
                            break;

                        default:
                            xx2 = n;
                            break;
                    }

                    yy = 2.0 * xx * yy + im;
                    xx = xx2;
                    it++;
                }

                if (it < this.maxit && it > 0) {
                    int i = it % 16;
                    switch (i) {
                        case 0:
                            this.bi.setRGB(x, y, new Color(66, 30, 15).getRGB());
                            break;

                        case 1:
                            this.bi.setRGB(x, y, new Color(25, 7, 26).getRGB());
                            break;

                        case 2:
                            this.bi.setRGB(x, y, new Color(9, 1, 47).getRGB());
                            break;

                        case 3:
                            this.bi.setRGB(x, y, new Color(4, 4, 73).getRGB());
                            break;

                        case 4:
                            this.bi.setRGB(x, y, new Color(0, 7, 100).getRGB());
                            break;

                        case 5:
                            this.bi.setRGB(x, y, new Color(12, 44, 138).getRGB());
                            break;

                        case 6:
                            this.bi.setRGB(x, y, new Color(24, 82, 177).getRGB());
                            break;

                        case 7:
                            this.bi.setRGB(x, y, new Color(57, 125, 209).getRGB());
                            break;

                        case 8:
                            this.bi.setRGB(x, y, new Color(134, 181, 229).getRGB());
                            break;

                        case 9:
                            this.bi.setRGB(x, y, new Color(211, 236, 248).getRGB());
                            break;

                        case 10:
                            this.bi.setRGB(x, y, new Color(241, 233, 191).getRGB());
                            break;

                        case 11:
                            this.bi.setRGB(x, y, new Color(248, 201, 95).getRGB());
                            break;

                        case 12:
                            this.bi.setRGB(x, y, new Color(255, 170, 0).getRGB());
                            break;

                        case 13:
                            this.bi.setRGB(x, y, new Color(204, 128, 0).getRGB());
                            break;

                        case 14:
                            this.bi.setRGB(x, y, new Color(153, 87, 0).getRGB());
                            break;

                        case 15:
                            this.bi.setRGB(x, y, new Color(106, 52, 3).getRGB());
                            break;

                        default:
                            break;
                    }
                }

                else {
                    this.bi.setRGB(x, y, new Color(0, 0, 0).getRGB());
                }
            }
        }

        return this.bi;
    }

    @Override
    public String toString() {
        return this.maxit + ", " + this.centering + ", " + this.zoom;
    }
}