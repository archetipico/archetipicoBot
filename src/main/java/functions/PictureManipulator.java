package functions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.*;
import javax.imageio.*;

public class PictureManipulator {

    private BufferedImage img = null;
    private final int w;
    private final int h;

    public PictureManipulator(InputStream stream) {
        try {
            this.img = ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Image not found");
        }

        assert img != null;
        this.w = img.getWidth();
        this.h = img.getHeight();
    }

    public void average() throws IOException {
        int r = 0;
        int g = 0;
        int b = 0;
        int pixels = w * h;
        Color color;

        for(int i = 0; i < w; i++) {
            for(int j = 0; j < h; j++) {
                color = new Color(img.getRGB(i, j));
                r += color.getRed();
                g += color.getGreen();
                b += color.getBlue();
            }
        }

        r /= pixels;
        g /= pixels;
        b /= pixels;

        int rgb = new Color(r,g,b).getRGB();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                result.setRGB(x, y, rgb);
            }
        }

        File out = new File("average.png");
        ImageIO.write(result, "png", out);
    }

    public void gradient() throws IOException {
        int r = 0;
        int g = 0;
        int b = 0;
        int pixels = 0;
        Color color;
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < w; i++) {
            for(int j = 0; j < h; j++) {
                pixels++;
                color = new Color(img.getRGB(i, j));
                r += color.getRed();
                g += color.getGreen();
                b += color.getBlue();

                int rOut = r/pixels;
                int gOut = g/pixels;
                int bOut = b/pixels;

                int rgb = new Color(rOut,gOut,bOut).getRGB();
                result.setRGB(i, j, rgb);
            }
        }

        File out = new File("gradient.png");
        ImageIO.write(result, "png", out);
    }

    public void negative() throws IOException {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p = img.getRGB(x,y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                p = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, p);
            }
        }

        File out = new File("negative.png");
        ImageIO.write(img, "png", out);
    }

    public void pixelate(int range) throws IOException {
        Raster src = img.getData();
        WritableRaster dest = src.createCompatibleWritableRaster();

        for (int y = 0; y < src.getHeight(); y += range) {
            for (int x = 0; x < src.getWidth(); x += range) {
                double[] pixel = new double[3];
                pixel = src.getPixel(x, y, pixel);
                for (int yd = y; (yd < y + range) && (yd < dest.getHeight()); yd++) {
                    for (int xd = x; (xd < x + range) && (xd < dest.getWidth()); xd++) {
                        dest.setPixel(xd, yd, pixel);
                    }
                }
            }
        }

        img.setData(dest);

        File out = new File("pixelated.png");
        ImageIO.write(img, "png", out);
    }

    public void simplify() throws IOException {
        int r, g, b;
        int pixels = 0;

        Color color;
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < w; i++) {
            for(int j = 0; j < h; j++) {
                pixels++;
                color = new Color(img.getRGB(i, j));
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();

                int value = Math.round( ((r + g + b) / 3) / 10) * 10;
                int thresholded = Math.min(value, 255);

                int rgb = new Color(thresholded, thresholded, thresholded).getRGB();
                result.setRGB(i, j, rgb);
            }
        }

        File out = new File("simplify.png");
        ImageIO.write(result, "png", out);
    }
}
