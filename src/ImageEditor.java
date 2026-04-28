import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

class ImageEditor extends JPanel {

    private final Stack<BufferedImage> UNDO_STACK;
    private final Stack<BufferedImage> REDO_STACK;
    private final JPanel IMAGE_PANEL;
    private final JMenuBar MENU_BAR;
    private final JScrollPane SCROLL_PANE;
    private final ShortcutKeyMap SHORTCUT_KEY_MAP;
    private final ZoomMouseEventListener ZOOM_LISTENER;
    private int zoomImageIndex;

    ImageEditor() {
        this.UNDO_STACK = new Stack<>();
        this.REDO_STACK = new Stack<>();
        this.SHORTCUT_KEY_MAP = new ShortcutKeyMap(this);
        this.IMAGE_PANEL = new ImagePanel(this);
        this.SCROLL_PANE = new JScrollPane(this.IMAGE_PANEL, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.MENU_BAR = new MenuBar(this);
        this.ZOOM_LISTENER = new ZoomMouseEventListener(this, this.IMAGE_PANEL);
        this.zoomImageIndex = 0;
        this.setLayout(new BorderLayout());
        this.add(this.MENU_BAR, BorderLayout.NORTH);
        this.add(this.SCROLL_PANE, BorderLayout.CENTER);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.IMAGE_PANEL.repaint();
    }

    @Override
    public void revalidate() {
        super.revalidate();
        if (this.IMAGE_PANEL != null) {
            this.IMAGE_PANEL.revalidate();
        }
    }
    /**
     * Reads a PPM image from a file and stores it in this ImageEditor.
     * @param in the input file name
    */
    void readPpmImage(String in) {
        try {
            //create scanner to read ppm data
            Scanner scanner = new Scanner(new File(in));
            //first value in ppm file is the format type which should be P3
            String type = scanner.next();
            //read width and height of image and width is the number of columns while height is the number of rows
            int width = scanner.nextInt();
            int height = scanner.nextInt();
            //read the max color value which should be 255
            int maxColor = scanner.nextInt();

            //create new bufferedimage with the right dimensions
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //loop through each pixel in image
            //outer loop goes through rows and inner loop goes through columns
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int red = scanner.nextInt();
                    int green = scanner.nextInt();
                    int blue = scanner.nextInt();
                    //create color object using RGB values
                    Color c = new Color(red, green, blue);
                    //Set pixel at the right (col, row) in image by using the color
                    // buffered image uses (x,y) as col, row
                    img.setRGB(col, row, c.getRGB());
                }
            }

            // Do not modify the lines below.
            this.UNDO_STACK.clear();
            this.REDO_STACK.clear();
            this.zoomImageIndex = 0;
            this.addImage(img);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes the current image to a PPM file (p3)
     * The image is converted from a BufferedImage into RGB values and written in PPM format
     * @param out the name of the output file
     * @throws RuntimeException if the file cannot be created or written to
     */
    void writePpmImage(String out) {
        try {
            //get current image from editor
            BufferedImage img = this.getImage();

            //create prntwriter to write to the output file
            PrintWriter writer = new PrintWriter(new File(out));

            //get dimensions of image
            int width = img.getWidth();
            int height = img.getHeight();

            //write ppm header (should be P3 and that means plain text RGB format)
            writer.println("P3");
            //write width and height of the image
            writer.println(width + " " + height);
            //write max color value
            writer.println(255);

            //loop through each pixel in image
            //outer loop is rows and inner loop is columns
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    //get integer representation of color
                    int colorInt = img.getRGB(col, row);
                    //convert integer back into color object
                    Color c = new Color(colorInt);

                    //seperate the individual RGB values from color object
                    int red = c.getRed();
                    int green = c.getGreen();
                    int blue = c.getBlue();

                    //write values to file in the PPM format
                    writer.println(red + " " + green + " " + blue);
                }
            }

            writer.close();
        //if file can't be created or written to through the runtime error
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new image to the editor and the undo stack. It is assumed that the image
     * being passed is not zoomed. If so, use the other addImage method.
     *
     * @param img image to add.
     */
    void addImage(BufferedImage img) {
        this.UNDO_STACK.push(img);
        this.REDO_STACK.clear();
        this.revalidate();
        this.repaint();
        this.zoomImageIndex++;
    }

    /**
     * Adds a new zoomed image to the editor. Because we only want to apply transformations
     * to non-zoomed images, we need to keep track of where the last non-zoomed image is in
     * the undo stack.
     *
     * @param img    image to add.
     * @param zoomed flag indicating whether the image is zoomed. This is always true.
     */
    void addImage(BufferedImage img, boolean zoomed) {
        this.UNDO_STACK.push(img);
        this.REDO_STACK.clear();
        this.revalidate();
        this.repaint();
        if (!zoomed) {
            this.zoomImageIndex++;
        }
    }

    /**
     * Removes the current image from the editor and the undo stack.
     * The undone image is pushed to the redo stack. If there are no images
     * to undo, this method does nothing.
     */
    void undoImage() {
        if (!this.UNDO_STACK.isEmpty()) {
            this.REDO_STACK.push(this.UNDO_STACK.pop());
            this.revalidate();
            this.repaint();
        }
    }

    /**
     * Redoes the last undone image. The redone image is pushed to the undo stack.
     * If there are no images to redo, this method does nothing.
     */
    void redoImage() {
        if (!this.REDO_STACK.isEmpty()) {
            this.UNDO_STACK.push(this.REDO_STACK.pop());
            this.revalidate();
            this.repaint();
        }
    }

    Stack<BufferedImage> getUndoStack() {
        return this.UNDO_STACK;
    }

    Stack<BufferedImage> getRedoStack() {
        return this.REDO_STACK;
    }

    BufferedImage getImage() {
        return this.UNDO_STACK.isEmpty() ? null : this.UNDO_STACK.peek();
    }

    BufferedImage getOriginalImage() {
        if (this.zoomImageIndex < 1 || this.zoomImageIndex >= this.UNDO_STACK.size()) {
            return null;
        } else {
            return this.UNDO_STACK.elementAt(this.zoomImageIndex - 1);
        }
    }

    MenuBar getMenuBar() {
        return (MenuBar) MENU_BAR;
    }

    JScrollPane getScrollPane() {
        return this.SCROLL_PANE;
    }

    ZoomMouseEventListener getZoomListener() {
        return this.ZOOM_LISTENER;
    }
}
