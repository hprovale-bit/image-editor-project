import java.awt.*;
import java.awt.image.BufferedImage;

class ImageOperations {

    /**
     *
     *
     * @param img TODO.
     * @return TODO.
     */
    static BufferedImage zeroRed(BufferedImage img) {
        // TODO.
        BufferedImage newImg = null;
        return newImg;
    }

    /**
     * TODO.
     *
     * @param img TODO.
     * @return TODO.
     */
    static BufferedImage grayscale(BufferedImage img) {
        // TODO.
        BufferedImage newImg = null;
        return newImg;
    }

    /**
     * TODO.
     * @param img TODO.
     * @return TODO.
     */
    static BufferedImage invert(BufferedImage img) {
        // TODO.
        BufferedImage newImg = null;
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
     * TODO.
     *
     * @param img TODO.
     * @param n   TODO.
     * @param dir TODO.
     * @return TODO.
     */
    static BufferedImage repeat(BufferedImage img, int n, RepeatMenuItem.RepeatDirection dir) {
        BufferedImage newImg = null;
        // newImg must be instantiated in both branches with the correct dimensions.
        if (dir == RepeatMenuItem.RepeatDirection.HORIZONTAL) {
            // TODO repeat the image horizontally.
        } else {
            // TODO repeat the image vertically.
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
