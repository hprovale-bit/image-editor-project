import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class ImageOperationsTest {

    private int rgb(int r, int g, int b) {
        return new Color(r, g, b).getRGB();
    }

    @org.junit.jupiter.api.Test
    void zeroRed() {
        BufferedImage img1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img1.setRGB(0, 0, rgb(100, 150, 200));

        BufferedImage result1 = ImageOperations.zeroRed(img1);
        assertEquals(rgb(0, 150, 200), result1.getRGB(0, 0));

        BufferedImage img2 = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        img2.setRGB(0, 0, rgb(255, 0, 0));
        img2.setRGB(1, 0, rgb(10, 20, 30));

        BufferedImage result2 = ImageOperations.zeroRed(img2);
        assertEquals(rgb(0, 0, 0), result2.getRGB(0, 0));
        assertEquals(rgb(0, 20, 30), result2.getRGB(1, 0));
    }

    @org.junit.jupiter.api.Test
    void grayscale() {
        BufferedImage img1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img1.setRGB(0, 0, rgb(90, 150, 210));

        BufferedImage result1 = ImageOperations.grayscale(img1);
        int gray = (90 + 150 + 210) / 3;
        assertEquals(rgb(gray, gray, gray), result1.getRGB(0, 0));

        BufferedImage img2 = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        img2.setRGB(0, 0, rgb(0, 0, 0));
        img2.setRGB(1, 0, rgb(255, 255, 255));

        BufferedImage result2 = ImageOperations.grayscale(img2);
        assertEquals(rgb(0, 0, 0), result2.getRGB(0, 0));
        assertEquals(rgb(255, 255, 255), result2.getRGB(1, 0));
    }

    @org.junit.jupiter.api.Test
    void invert() {
        BufferedImage img1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img1.setRGB(0, 0, rgb(10, 20, 30));

        BufferedImage result1 = ImageOperations.invert(img1);
        assertEquals(rgb(245, 235, 225), result1.getRGB(0, 0));

        BufferedImage img2 = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        img2.setRGB(0, 0, rgb(0, 0, 0));
        img2.setRGB(1, 0, rgb(255, 255, 255));

        BufferedImage result2 = ImageOperations.invert(img2);
        assertEquals(rgb(255, 255, 255), result2.getRGB(0, 0));
        assertEquals(rgb(0, 0, 0), result2.getRGB(1, 0));
    }

    @org.junit.jupiter.api.Test
    void mirror() {
        BufferedImage img1 = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        img1.setRGB(0, 0, rgb(1, 0, 0));
        img1.setRGB(1, 0, rgb(2, 0, 0));

        BufferedImage result1 = ImageOperations.mirror(
                img1,
                MirrorMenuItem.MirrorDirection.VERTICAL
        );

        assertEquals(rgb(2, 0, 0), result1.getRGB(0, 0));
        assertEquals(rgb(1, 0, 0), result1.getRGB(1, 0));

        BufferedImage img2 = new BufferedImage(1, 2, BufferedImage.TYPE_INT_RGB);
        img2.setRGB(0, 0, rgb(1, 0, 0));
        img2.setRGB(0, 1, rgb(2, 0, 0));

        BufferedImage result2 = ImageOperations.mirror(
                img2,
                MirrorMenuItem.MirrorDirection.HORIZONTAL
        );

        assertEquals(rgb(2, 0, 0), result2.getRGB(0, 0));
        assertEquals(rgb(1, 0, 0), result2.getRGB(0, 1));
    }

    @org.junit.jupiter.api.Test
    void rotate() {
        BufferedImage img = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, rgb(1, 0, 0));
        img.setRGB(1, 0, rgb(2, 0, 0));

        BufferedImage cw = ImageOperations.rotate(
                img,
                RotateMenuItem.RotateDirection.CLOCKWISE
        );

        assertEquals(1, cw.getWidth());
        assertEquals(2, cw.getHeight());
        assertEquals(rgb(1, 0, 0), cw.getRGB(0, 0));
        assertEquals(rgb(2, 0, 0), cw.getRGB(0, 1));

        BufferedImage ccw = ImageOperations.rotate(
                img,
                RotateMenuItem.RotateDirection.COUNTER_CLOCKWISE
        );

        assertEquals(rgb(2, 0, 0), ccw.getRGB(0, 0));
        assertEquals(rgb(1, 0, 0), ccw.getRGB(0, 1));
    }

    @org.junit.jupiter.api.Test
    void repeat() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, rgb(50, 60, 70));

        BufferedImage h = ImageOperations.repeat(
                img, 3,
                RepeatMenuItem.RepeatDirection.HORIZONTAL
        );

        assertEquals(3, h.getWidth());
        for (int i = 0; i < 3; i++) {
            assertEquals(rgb(50, 60, 70), h.getRGB(i, 0));
        }

        BufferedImage v = ImageOperations.repeat(
                img, 3,
                RepeatMenuItem.RepeatDirection.VERTICAL
        );

        assertEquals(3, v.getHeight());
        for (int i = 0; i < 3; i++) {
            assertEquals(rgb(50, 60, 70), v.getRGB(0, i));
        }
    }

    @org.junit.jupiter.api.Test
    void zoom() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        BufferedImage bigger = ImageOperations.zoom(img, 2.0);
        assertEquals(4, bigger.getWidth());
        assertEquals(4, bigger.getHeight());

        BufferedImage smaller = ImageOperations.zoom(img, 0.5);
        assertEquals(1, smaller.getWidth());
        assertEquals(1, smaller.getHeight());
    }
}