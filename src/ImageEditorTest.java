import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImageEditorTest {

    private int rgb(int r, int g, int b) {
        return new Color(r, g, b).getRGB();
    }

    @Test
    void readPpmImage() {
        ImageEditor editor = new ImageEditor();

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, rgb(100, 150, 200));

        editor.addImage(img);

        String filename = "test.ppm";
        editor.writePpmImage(filename);

        editor.readPpmImage(filename);

        BufferedImage result = editor.getImage();

        assertEquals(rgb(100, 150, 200), result.getRGB(0, 0));

        new File(filename).delete();
    }

    @Test
    void writePpmImage() {
        ImageEditor editor = new ImageEditor();

        BufferedImage img = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, rgb(10, 20, 30));
        img.setRGB(1, 0, rgb(40, 50, 60));

        editor.addImage(img);

        String filename = "test2.ppm";
        editor.writePpmImage(filename);

        editor.readPpmImage(filename);
        BufferedImage result = editor.getImage();

        assertEquals(rgb(10, 20, 30), result.getRGB(0, 0));
        assertEquals(rgb(40, 50, 60), result.getRGB(1, 0));

        new File(filename).delete();
    }

    @Test
    void addImage() {
        ImageEditor editor = new ImageEditor();

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, rgb(50, 60, 70));

        editor.addImage(img);

        assertEquals(rgb(50, 60, 70), editor.getImage().getRGB(0, 0));
    }

    @Test
    void undoImage() {
        ImageEditor editor = new ImageEditor();

        BufferedImage img1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img1.setRGB(0, 0, rgb(1, 1, 1));

        BufferedImage img2 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img2.setRGB(0, 0, rgb(2, 2, 2));

        editor.addImage(img1);
        editor.addImage(img2);

        editor.undoImage();

        assertEquals(rgb(1, 1, 1), editor.getImage().getRGB(0, 0));
    }

    @Test
    void redoImage() {
        ImageEditor editor = new ImageEditor();

        BufferedImage img1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img1.setRGB(0, 0, rgb(1, 1, 1));

        BufferedImage img2 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img2.setRGB(0, 0, rgb(2, 2, 2));

        editor.addImage(img1);
        editor.addImage(img2);

        editor.undoImage();
        editor.redoImage();

        assertEquals(rgb(2, 2, 2), editor.getImage().getRGB(0, 0));
    }

    @Test
    void getImage() {
        ImageEditor editor = new ImageEditor();

        assertNull(editor.getImage());

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, rgb(9, 9, 9));

        editor.addImage(img);

        assertEquals(rgb(9, 9, 9), editor.getImage().getRGB(0, 0));
    }

    @Test
    void writePpm_matchesExpectedFile() throws Exception {
        ImageEditor editor = new ImageEditor();

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, rgb(100, 150, 200));

        editor.addImage(img);

        String outFile = "test_write.out";

        editor.writePpmImage(outFile);

        List<String> expected = Files.readAllLines(Path.of("test_write.exp"));
        List<String> actual = Files.readAllLines(Path.of(outFile));

        assertEquals(expected, actual);

        Files.deleteIfExists(Path.of(outFile)); // cleanup
    }
}