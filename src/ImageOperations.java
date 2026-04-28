import java.awt.*;
import java.awt.image.BufferedImage;

class ImageOperations {

    /**
     * removes the red channel from every pixel in the image
     * @param img the original image
     * @return a new image with the red component of each pixel set to 0
     */

    static BufferedImage zeroRed(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = new Color(img.getRGB(col, row));
                Color out = new Color(0, c.getGreen(), c.getBlue());
                newImg.setRGB(col, row, out.getRGB());
            }
        }
        return newImg;
    }

    /**
     * converts the image to grayscale by averaging the red, green, and blue values
     * of each pixel and setting all three channels to that average
     * @param img the original image
     * @return a new grayscale image
     */

    static BufferedImage grayscale(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = new Color(img.getRGB(col, row));
                int gray = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                Color out = new Color(gray, gray, gray);
                newImg.setRGB(col, row, out.getRGB());
            }
        }
        return newImg;
    }

    /**
     * inverts the colors of the image by subtracting each RGB value from 255
     * @param img the original image
     * @return a new image with inverted colors
     */

    static BufferedImage invert(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = new Color(img.getRGB(col, row));
                Color out = new Color(
                        255 - c.getRed(),
                        255 - c.getGreen(),
                        255 - c.getBlue()
                );
                newImg.setRGB(col, row, out.getRGB());
            }
        }
        return newImg;
    }

    /**
     * mirrors image exactly either horizontally or vertically
     * @param img the image that is being mirrored
     * @param dir direction that the image is being mirrored (horizontal or vertical)
     * @return a new mirrored image
     */

    static BufferedImage mirror(BufferedImage img, MirrorMenuItem.MirrorDirection dir) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage newImg = null;

        if (dir == MirrorMenuItem.MirrorDirection.VERTICAL) {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int mirrorCol = width - 1 - col;
                    result.setRGB(col, row, img.getRGB(mirrorCol, row));}
            }
        }
        else if (dir == MirrorMenuItem.MirrorDirection.HORIZONTAL) {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int mirrorRow = height - 1 - row;
                    result.setRGB(col, row, img.getRGB(col, mirrorRow));}
            }
        }return result;
    }

    /**
     * Rotates the image 90 degrees clockwise or counterclockwise
     * @param img the image to be rotated
     * @param dir the direction of rotation (either counterclockwise or clockwise)
     * @return the rotated image
     */

    static BufferedImage rotate(BufferedImage img, RotateMenuItem.RotateDirection dir) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage result = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                if (dir == RotateMenuItem.RotateDirection.CLOCKWISE) {
                    int newRow = col;
                    int newCol = height - 1 - row;
                    result.setRGB(newCol, newRow, img.getRGB(col, row));

                } else {
                    int newRow = width - 1 - col;
                    int newCol = row;
                    result.setRGB(newCol, newRow, img.getRGB(col, row));
                }
            }
        }
        return result;
    }

    /**
     * repeats the image multiple times either horizontally or vertically
     * @param img the original image
     * @param n the number of times the image should be repeated
     * @param dir the direction of repetition (horizontal/vertical)
     * @return a new image with the original image repeated n times in the given direction
     */

    static BufferedImage repeat(BufferedImage img, int n, RepeatMenuItem.RepeatDirection dir) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImg;

        if (dir == RepeatMenuItem.RepeatDirection.HORIZONTAL) {
            newImg = new BufferedImage(width * n, height, BufferedImage.TYPE_INT_RGB);

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int pixel = img.getRGB(col, row);
                    for (int i = 0; i < n; i++) {
                        newImg.setRGB(col + i * width, row, pixel);
                    }
                }
            }
        } else {
            newImg = new BufferedImage(width, height * n, BufferedImage.TYPE_INT_RGB);

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int pixel = img.getRGB(col, row);
                    for (int i = 0; i < n; i++) {
                        newImg.setRGB(col, row + i * height, pixel);
                    }
                }
            }
        }
        return newImg;
    }

    /**
     * Zooms in on the image. The zoom factor increases in multiplicatives of 10% and
     * decreases in multiplicatives of 10%.
     *
     * @param img        the original image to zoom in on. The image cannot be already zoomed in
     *                   or out because then the image will be distorted.
     * @param zoomFactor The factor to zoom in by.
     * @return the zoomed in image.
     */

    static BufferedImage zoom(BufferedImage img, double zoomFactor) {
        int newImageWidth = (int) (img.getWidth() * zoomFactor);
        int newImageHeight = (int) (img.getHeight() * zoomFactor);
        BufferedImage newImg = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = newImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, newImageWidth, newImageHeight, null);
        g2d.dispose();
        return newImg;
    }
}