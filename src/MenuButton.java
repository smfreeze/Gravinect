package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class MenuButton extends JButton {
    public MenuButton(Dimension dimensions, Dimension position, String path) {
        Border emptyBorder = BorderFactory.createEmptyBorder();
        try {
            InputStream resourceBuffered = MenuButton.class.getResourceAsStream(path);
            BufferedImage bufferedImage = ImageIO.read(resourceBuffered);
            ImageIcon image = new ImageIcon(bufferedImage);
            Image real_image = image.getImage();
            Image newimg = real_image.getScaledInstance((int) dimensions.getWidth(), (int) dimensions.getHeight(),
                    java.awt.Image.SCALE_SMOOTH);
            image = new ImageIcon(newimg);
            this.setOpaque(false);
            this.setContentAreaFilled(false);
            this.setBorderPainted(false);
            this.setIcon(image);
            this.setBorder(emptyBorder);
            this.setBounds((int) position.getWidth(), (int) position.getHeight(), (int) dimensions.getWidth(),
                    (int) dimensions.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}