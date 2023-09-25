package src;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Menu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.TreeUI;

public class LocalGameboard extends JLabel {
    static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    static int width = (int) size.getWidth();
    static int height = (int) size.getHeight();

    static JButton[][] buttonArray;
    static int[][] referenceArray;

    static JButton turnIndicatorGreen;
    static JButton turnIndicatorRed;

    static int turn = 1;
    static int turnCount = 1;
    static int turnsTil;

    public LocalGameboard(int x, int y, int turnsTilM) { // x should equal y, as otherwise rotation doesn't work, but
                                                         // can
                                                         // still draw board of any dimension below:
        buttonArray = new JButton[x][y];
        referenceArray = new int[x][y];

        turnsTil = turnsTilM;

        turnIndicatorGreen = new MenuButton( // YOUR.THEIR TURN IMAGE
                new Dimension(width * 65 / 100, height * 13 / 100),
                new Dimension(height * 30 / 100, height * 6 / 100),
                "Assets/Buttons/game-green-turn.png");
        this.add(turnIndicatorGreen);
        turnIndicatorRed = new MenuButton( // YOUR.THEIR TURN IMAGE
                new Dimension(width * 5578 / 10000, height * 13 / 100),
                new Dimension(height * 464 / 1000, height * 6 / 100),
                "Assets/Buttons/game-red-turn.png");
        turnIndicatorRed.setVisible(false);
        this.add(turnIndicatorRed);

        for (int n = 1; n <= x; n++) { // Draw board with empty tiles, also create reference array.
            for (int m = 1; m <= y; m++) {
                referenceArray[n - 1][m - 1] = 0;
                JButton tile = new MenuButton(
                        new Dimension(width * 28125 / 1000000 * 3 / 2, height * 50 / 1000 * 3 / 2),
                        new Dimension(height * 90 / 100 + height * n * 5 / 100 * 3 / 2,
                                height * 25 / 100 + height * m * 5 / 100 * 3 / 2),
                        "Assets/Tiles/empty-tile.png");
                this.add(tile);
                buttonArray[n - 1][m - 1] = tile;
            }
        }

        updateButtons(buttonArray, referenceArray); // oon't know if I need to do this, but it works when I do and I am
                                                    // lazy...
        addHints(buttonArray, referenceArray); // Draws the light blue tiles where you can place tiles, also adds
                                               // functionality, see addHints function
    }

    public static void playMove(JButton button, int x, int y) {
        // POSSIBLE MOVE VALIDATION
        if (turn == 1) {
            referenceArray[x][y] = 1;
            turn = 2;
        } else if (turn == 2) {
            referenceArray[x][y] = 2;
            turn = 1;
        }
        if (turnCount % 3 == 0 && turnCount != 0) {
            rotateInt90CW(referenceArray);
            updateButtons(buttonArray, referenceArray);
            addHints(buttonArray, referenceArray);
        } else {
            updateButtons(buttonArray, referenceArray);
            addHints(buttonArray, referenceArray);
        }
        turnCount++;
        if (turnCount % 2 == 0) {
            turnIndicatorGreen.setVisible(false);
            turnIndicatorRed.setVisible(true);
        } else {
            turnIndicatorRed.setVisible(false);
            turnIndicatorGreen.setVisible(true);
        }
        // CONSIDER BOARD (see if winner or board is full)
    }

    public static void rotateInt90AntiCW(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] rotatedMatrix = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedMatrix[j][rows - 1 - i] = matrix[i][j];
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rotatedMatrix[i][j];
            }
        }
    }

    public static void rotateInt90CW(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] rotatedMatrix = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedMatrix[j][rows - 1 - i] = matrix[i][j];
            }
        }

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                matrix[i][j] = rotatedMatrix[i][j];
            }
        }
    }

    public static void updateButtons(JButton[][] butMatrix, int[][] refMatrix) { // Updates the buttons based on the
                                                                                 // reference matrix
        for (int n = 1; n <= butMatrix.length; n++) {
            for (int m = 1; m <= butMatrix[0].length; m++) {
                // The following removes listeners
                for (MouseListener listener : butMatrix[n - 1][m - 1].getMouseListeners()) {
                    if (listener instanceof MouseAdapter) {
                        butMatrix[n - 1][m - 1].removeMouseListener(listener);
                    }
                }
                for (ActionListener listener : butMatrix[n - 1][m - 1].getActionListeners()) {
                    butMatrix[n - 1][m - 1].removeActionListener(listener);
                }

                // This adds the correct tile asset
                if (refMatrix[n - 1][m - 1] == 0) { // Empty
                    butMatrix[n - 1][m - 1].setIcon(image_resizer(
                            new Dimension(width * 28125 / 1000000 * 3 / 2, height * 50 / 1000 * 3 / 2),
                            image_creator("Assets/Tiles/empty-tile.png")));
                }
                if (refMatrix[n - 1][m - 1] == 1) { // Green
                    butMatrix[n - 1][m - 1].setIcon(image_resizer(
                            new Dimension(width * 28125 / 1000000 * 3 / 2, height * 50 / 1000 * 3 / 2),
                            image_creator("Assets/Tiles/green-tile.png")));
                }
                if (refMatrix[n - 1][m - 1] == 2) { // Red
                    butMatrix[n - 1][m - 1].setIcon(image_resizer(
                            new Dimension(width * 28125 / 1000000 * 3 / 2, height * 50 / 1000 * 3 / 2),
                            image_creator("Assets/Tiles/red-tile.png")));
                }
            }
        }
    }

    public static void addHints(JButton[][] butMatrix, int[][] refMatrix) {
        for (int o = 1; o <= refMatrix.length; o++) { // for each collumn
            int flag = 999;
            for (int p = 1; p <= refMatrix[0].length; p++) {
                if (refMatrix[o - 1][p - 1] == 0) {
                    flag = p;
                }
            }
            try {
                final int finalO = o;
                final int finalFlag = flag;
                butMatrix[o - 1][flag - 1].setIcon(image_resizer(
                        new Dimension(width * 28125 / 1000000 * 3 / 2, height * 50 / 1000 * 3 / 2),
                        image_creator("Assets/Tiles/hint-tile.png")));
                butMatrix[o - 1][flag - 1].addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        butMatrix[finalO - 1][finalFlag - 1].setIcon(image_resizer(
                                new Dimension(width * 28125 / 1000000 * 3 / 2, height * 50 / 1000 * 3 / 2),
                                image_creator("Assets/Tiles/hint-tile-hover.png")));
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        butMatrix[finalO - 1][finalFlag - 1].setIcon(image_resizer(
                                new Dimension(width * 28125 / 1000000 * 3 / 2, height * 50 / 1000 * 3 / 2),
                                image_creator("Assets/Tiles/hint-tile.png")));
                    }
                });
                butMatrix[o - 1][flag - 1].addActionListener((ActionListener) new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        playMove(butMatrix[finalO - 1][finalFlag - 1], finalO - 1, finalFlag - 1);
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    // Util:
    private static ImageIcon image_creator(String path) {
        try {
            InputStream resourceBuffered = MenuButton.class.getResourceAsStream(path);
            BufferedImage bufferedImage = ImageIO.read(resourceBuffered);
            ImageIcon image = new ImageIcon(bufferedImage);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageIcon();
        }
    }

    private static ImageIcon image_resizer(Dimension dimensions, ImageIcon image) {
        Image real_image = image.getImage();
        Image newimg = real_image.getScaledInstance((int) dimensions.getWidth(), (int) dimensions.getHeight(),
                java.awt.Image.SCALE_SMOOTH);
        image = new ImageIcon(newimg);
        return image;
    }
}
