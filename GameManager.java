import src.LocalGameboard;
import src.MenuButton;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class GameManager {
    static JFrame frame;

    static JLabel tubePanel;
    static JLabel stigPanel;
    static JLabel loadingPanel;
    static JLabel mainMenuPanel;
    static JLabel gameOptionsPanel;
    static JLabel optionsPanel;

    private static ImageIcon tubeImage;
    private static JButton tubeButton;
    private static ImageIcon stigImage;
    private static JButton stigButton;

    private static Clip songsClip;
    private static boolean muteBool = false;

    static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    static int width = (int) size.getWidth();
    static int height = (int) size.getHeight();

    public static void main(String[] args) throws InterruptedException {
        frame = new JFrame("Gravinect");
        frame.setIconImage(image_creator("Assets/logo.ico").getImage());
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        tubePanel = createTubePanel();
        stigPanel = createStigPanel();
        loadingPanel = createLoadingPanel();
        mainMenuPanel = createMainMenuPanel();
        gameOptionsPanel = createGameOptionsPanel();
        optionsPanel = createOptionsPanel();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                tubeImage = image_resizer(new Dimension(width * 100 / 100, height * 90 / 100),
                        image_creator("Assets/Backgrounds/tube.png"));
                frame.add(tubePanel, BorderLayout.CENTER);
                fadeInButtonIcon(tubeButton, tubeImage);
                playSong("src/Assets/Music/tubejingle.wav", false);
                Thread.sleep(4000);
                fadeOutButtonIcon(tubeButton, tubeImage);
                Thread.sleep(1500);
                frame.revalidate();
                frame.repaint();
                frame.remove(tubePanel);

                stigImage = image_resizer(new Dimension(width * 30 / 100, height * 90 / 100),
                        image_creator("Assets/Backgrounds/stig.png"));
                frame.add(stigPanel, BorderLayout.CENTER);
                fadeInButtonIcon(stigButton, stigImage);
                Thread.sleep(800);
                playSong("src/Assets/Music/dastig.wav", false);
                Thread.sleep(1200);
                fadeOutButtonIcon(stigButton, stigImage);
                frame.revalidate();
                frame.repaint();
                Thread.sleep(1000);
                return null;
            }

            @Override
            protected void done() {
                frame.remove(stigPanel);
                frame.add(mainMenuPanel, BorderLayout.CENTER);
                playSong("src/Assets/Music/theme.wav", true);
                frame.revalidate();
                frame.repaint();
            }
        };

        worker.execute();
    }

    // Loading panel
    private static JLabel createLoadingPanel() {
        JLabel panel = new JLabel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        JButton loading = new MenuButton(new Dimension(width * 30 / 100, height * 22 / 100),
                new Dimension(height * 63 / 100, height * 40 / 100), "Assets/Titles/loading.png");
        panel.add(loading);
        return panel;
    }

    // Stig intro panel
    private static JLabel createStigPanel() {
        JLabel panel = new JLabel();
        panel.setOpaque(true);
        panel.setBackground(Color.BLACK);
        panel.setLayout(null);
        stigButton = new MenuButton(new Dimension(width * 30 / 100, height * 90 / 100),
                new Dimension(height * 63 / 100, height * 10 / 100), "Assets/Backgrounds/stig.png");
        stigButton.setVisible(false);
        panel.add(stigButton);
        return panel;
    }

    // Tube intro panel
    private static JLabel createTubePanel() {
        JLabel panel = new JLabel();
        panel.setOpaque(true);
        panel.setBackground(Color.BLACK);
        panel.setLayout(null);
        tubeButton = new MenuButton(new Dimension(width * 100 / 100, height * 90 / 100),
                new Dimension(height * 0 / 100, height * 5 / 100), "Assets/Backgrounds/tube.png");
        tubeButton.setVisible(false);
        panel.add(tubeButton);
        return panel;
    }

    // GAME BOARD
    private static JLabel createLocalGameboardPanel() {
        songsClip.stop();
        playSong("src/Assets/Music/game.wav", true);
        JLabel panel = new LocalGameboard(7, 7, 3);
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setIcon(image_resizer(new Dimension(width, height),
                image_creator("Assets/Backgrounds/background.png")));
        return panel;
    }

    // OPTIONS
    private static JLabel createOptionsPanel() {
        JLabel panel = new JLabel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setIcon(image_resizer(new Dimension(width, height),
                image_creator("Assets/Backgrounds/background.png")));

        JButton title = new MenuButton(
                new Dimension(width * 58 / 100, height * 18 / 100),
                new Dimension(height * 40 / 100, height * 3 / 100),
                "Assets/Titles/options-title.png");

        JButton back = new MenuButton(
                new Dimension(width * 8 / 100, height * 6 / 100),
                new Dimension(height * 82 / 100, height * 85 / 100),
                "Assets/Buttons/back.png");

        JButton musicToggle = new MenuButton(
                new Dimension(width * 20 / 100, height * 10 / 100),
                new Dimension(height * 76 / 100, height * 30 / 100),
                "Assets/Buttons/music-toggle-on.png");

        back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                back.setIcon(image_resizer(new Dimension(width * 8 / 100, height * 6 / 100),
                        image_creator("Assets/Buttons/back-hover.png")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                back.setIcon(image_resizer(new Dimension(width * 8 / 100, height * 6 / 100),
                        image_creator("Assets/Buttons/back.png")));
            }
        });
        back.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                back.setIcon(image_resizer(new Dimension(width * 8 / 100, height * 6 / 100),
                        image_creator("Assets/Buttons/back.png")));
                transition_to_menu(frame, optionsPanel, mainMenuPanel);
            }
        });

        musicToggle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (muteBool) {
                    musicToggle.setIcon(image_resizer(new Dimension(width * 20 / 100, height * 10 / 100),
                            image_creator("Assets/Buttons/music-toggle-off-hover.png")));
                } else {
                    musicToggle.setIcon(image_resizer(new Dimension(width * 20 / 100, height * 10 / 100),
                            image_creator("Assets/Buttons/music-toggle-on-hover.png")));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (muteBool) {
                    musicToggle.setIcon(image_resizer(new Dimension(width * 20 / 100, height * 10 / 100),
                            image_creator("Assets/Buttons/music-toggle-off.png")));
                } else {
                    musicToggle.setIcon(image_resizer(new Dimension(width * 20 / 100, height * 10 / 100),
                            image_creator("Assets/Buttons/music-toggle-on.png")));
                }
            }
        });
        musicToggle.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (muteBool) {
                    muteBool = !muteBool;
                    musicToggle.setIcon(image_resizer(new Dimension(width * 20 / 100, height * 10 / 100),
                            image_creator("Assets/Buttons/music-toggle-on-hover.png")));
                    playSong("src/Assets/Music/theme.wav", true);
                } else {
                    muteBool = !muteBool;
                    musicToggle.setIcon(image_resizer(new Dimension(width * 20 / 100, height * 10 / 100),
                            image_creator("Assets/Buttons/music-toggle-off-hover.png")));
                    songsClip.stop();
                }
            }
        });

        panel.add(musicToggle);
        panel.add(back);
        panel.add(title);

        return panel;
    }

    // GAME OPTIONS
    private static JLabel createGameOptionsPanel() {
        JLabel panel = new JLabel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setIcon(image_resizer(new Dimension(width, height),
                image_creator("Assets/Backgrounds/background.png")));

        JButton title = new MenuButton(
                new Dimension(width * 40 / 100, height * 22 / 100),
                new Dimension(height * 53 / 100, height * 3 / 100),
                "Assets/Titles/game-options-title.png");
        JButton back = new MenuButton(
                new Dimension(width * 8 / 100, height * 6 / 100),
                new Dimension(height * 82 / 100, height * 85 / 100),
                "Assets/Buttons/back.png");
        JButton local = new MenuButton(
                new Dimension(width * 17 / 100, height * 10 / 100),
                new Dimension(height * 75 / 100, height * 38 / 100),
                "Assets/Buttons/local.png");
        JButton online = new MenuButton(
                new Dimension(width * 20 / 100, height * 10 / 100),
                new Dimension(height * 72 / 100, height * 60 / 100),
                "Assets/Buttons/online.png");

        back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                back.setIcon(image_resizer(new Dimension(width * 8 / 100, height * 6 / 100),
                        image_creator("Assets/Buttons/back-hover.png")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                back.setIcon(image_resizer(new Dimension(width * 8 / 100, height * 6 / 100),
                        image_creator("Assets/Buttons/back.png")));
            }
        });
        back.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                back.setIcon(image_resizer(new Dimension(width * 8 / 100, height * 6 / 100),
                        image_creator("Assets/Buttons/back.png")));
                transition_to_menu(frame, gameOptionsPanel, mainMenuPanel);
            }
        });

        online.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                online.setIcon(image_resizer(new Dimension(width * 20 / 100, height * 10 / 100),
                        image_creator("Assets/Buttons/online-hover.png")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                online.setIcon(image_resizer(new Dimension(width * 20 / 100, height * 10 / 100),
                        image_creator("Assets/Buttons/online.png")));
            }
        });
        // ADD ON CLICK LOAD ONLINE

        local.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                local.setIcon(image_resizer(new Dimension(width * 17 / 100, height * 10 / 100),
                        image_creator("Assets/Buttons/local-hover.png")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                local.setIcon(image_resizer(new Dimension(width * 17 / 100, height * 10 / 100),
                        image_creator("Assets/Buttons/local.png")));
            }
        });
        local.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                local.setIcon(image_resizer(new Dimension(width * 17 / 100, height * 10 / 100),
                        image_creator("Assets/Buttons/local.png")));
                JLabel gameboard = createLocalGameboardPanel();
                transition_to_menu(frame, gameOptionsPanel, gameboard);
            }
        });

        panel.add(online);
        panel.add(local);
        panel.add(back);
        panel.add(title);

        return panel;
    }

    // MAIN MENU
    private static JLabel createMainMenuPanel() {
        JLabel panel = new JLabel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setIcon(image_resizer(new Dimension(width, height),
                image_creator("Assets/Backgrounds/background.png")));

        JButton title = new MenuButton(
                new Dimension(width * 60 / 100, height * 23 / 100),
                new Dimension(height * 35 / 100, height * 6 / 100),
                "Assets/Titles/main-title.png");
        JButton play = new MenuButton(
                new Dimension(width * 13 / 100, height * 11 / 100),
                new Dimension(height * 77 / 100, height * 38 / 100),
                "Assets/Buttons/play.png");
        JButton options = new MenuButton(
                new Dimension(width * 23 / 100, height * 11 / 100),
                new Dimension(height * 68 / 100, height * 56 / 100),
                "Assets/Buttons/options.png");
        JButton quit = new MenuButton(
                new Dimension(width * 14 / 100, height * 11 / 100),
                new Dimension(height * 76 / 100, height * 74 / 100),
                "Assets/Buttons/quit.png");

        play.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                play.setIcon(image_resizer(new Dimension(width * 13 / 100, height * 11 / 100),
                        image_creator("Assets/Buttons/play-hover.png")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                play.setIcon(image_resizer(new Dimension(width * 13 / 100, height * 11 / 100),
                        image_creator("Assets/Buttons/play.png")));
            }
        });
        play.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                play.setIcon(image_resizer(new Dimension(width * 13 / 100, height * 11 / 100),
                        image_creator("Assets/Buttons/play.png")));
                transition_to_menu(frame, mainMenuPanel, gameOptionsPanel);
            }
        });

        options.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                options.setIcon(image_resizer(new Dimension(width * 23 / 100, height * 11 / 100),
                        image_creator("Assets/Buttons/options-hover.png")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                options.setIcon(image_resizer(new Dimension(width * 23 / 100, height * 11 / 100),
                        image_creator("Assets/Buttons/options.png")));
            }
        });
        options.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.setIcon(image_resizer(new Dimension(width * 23 / 100, height * 11 / 100),
                        image_creator("Assets/Buttons/options.png")));
                transition_to_menu(frame, mainMenuPanel, optionsPanel);
            }
        });

        quit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                quit.setIcon(image_resizer(new Dimension(width * 14 / 100, height * 11 / 100),
                        image_creator("Assets/Buttons/quit-hover.png")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                quit.setIcon(image_resizer(new Dimension(width * 14 / 100, height * 11 / 100),
                        image_creator("Assets/Buttons/quit.png")));
            }
        });
        quit.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panel.add(title);
        panel.add(play);
        panel.add(options);
        panel.add(quit);

        return panel;
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

    // Transition menus
    private static void transition_to_menu(JFrame frame, JLabel current_panel, JLabel future_panel) {
        frame.remove(current_panel);
        frame.add(future_panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // Transition menus with loading screen
    private static void transition_to_menu(JFrame frame, JLabel current_pannel, JLabel future_panel,
            JLabel loading_panel) {
        frame.remove(current_pannel);
        frame.add(loading_panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        // Simulate loading time whilst processes carry on
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(1000); // Simulate loading time
                return null;
            }

            @Override
            protected void done() {
                frame.remove(loadingPanel);
                frame.add(future_panel, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();
            }
        };

        worker.execute();
    }

    public static void fadeInButtonIcon(JButton button, ImageIcon icon) {
        Timer timer = new Timer(10, new ActionListener() {
            private float alpha = 0.0f;
            private boolean fadingIn = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (fadingIn) {
                    if (alpha != 0.0f) {
                        button.setVisible(true);
                    }
                    alpha += 0.02f;
                    if (alpha >= 1.0f) {
                        alpha = 1.0f;
                        ((Timer) e.getSource()).stop(); // Stop the timer when fully faded in
                    }
                } else {
                    alpha -= 0.02f;
                    if (alpha <= 0.0f) {
                        alpha = 0.0f;
                        ((Timer) e.getSource()).stop(); // Stop the timer when fully faded out
                    }
                }
                Image newImage = changeIconAlpha(icon, alpha);
                button.setIcon(new ImageIcon(newImage));
            }
        });
        timer.start();
    }

    public static void fadeOutButtonIcon(JButton button, ImageIcon icon) {
        Timer timer = new Timer(10, new ActionListener() {
            private float alpha = 1.0f;
            private boolean fadingIn = true; // Initialize as true to start fading in

            @Override
            public void actionPerformed(ActionEvent e) {
                if (fadingIn) {
                    alpha += 0.02f;
                    if (alpha >= 1.0f) {
                        alpha = 1.0f;
                        fadingIn = false;
                    }
                } else {
                    alpha -= 0.02f;
                    if (alpha <= 0.0f) {
                        alpha = 0.0f;
                        ((Timer) e.getSource()).stop(); // Stop the timer when fully faded out
                    }
                }
                Image newImage = changeIconAlpha(icon, alpha);
                button.setIcon(new ImageIcon(newImage));
            }
        });

        timer.start();
    }

    private static Image changeIconAlpha(ImageIcon icon, float alpha) {
        Image image = icon.getImage();
        Image newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) newImage.getGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return newImage;
    }

    private static void playSong(String soundFilePath, boolean loop) {
        if (!muteBool) {
            try {
                InputStream soundStream = GameManager.class.getResourceAsStream(soundFilePath);
                if (soundStream != null) {
                    AudioInputStream audioInputStream = AudioSystem
                            .getAudioInputStream(new BufferedInputStream(soundStream));
                    songsClip = AudioSystem.getClip();
                    songsClip.open(audioInputStream);
                    if (loop) {
                        songsClip.loop(Clip.LOOP_CONTINUOUSLY);
                    } else {
                        songsClip.start();
                    }
                } else {
                    System.err.println("Audio file not found: " + soundFilePath);
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
}
