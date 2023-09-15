import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;

public
class MainFrame extends JFrame {

    JPanel poster;
    PlayPanel playPanel;
    HomeButtons homeButtons;
    ClickListener clickListener;
    GridBagConstraints c;
    LinkedList <PlayerScore> scores;
    Image home;

    MainFrame() {
        super("Arkanoid");
        this.setSize(Constants.WIDTH, Constants.HEIGHT + 60);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
        playPanel = new PlayPanel();
        playPanel.pause();
        clickListener = new ClickListener();
        homeButtons = new HomeButtons();
        scores = new LinkedList <>();
        this.setLocationRelativeTo(null);
        DataManager.scoresLoader(scores);

        poster = new JPanel() {
            @Override
            public
            void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(
                        RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);

                g2d.drawImage(home, 0, 0, Constants.WIDTH, Constants.HEIGHT, this);
                Toolkit.getDefaultToolkit().sync();
            }
        };

        poster.revalidate();
        home = new ImageIcon("src/main/resources/home.png").getImage();


        homeButtons.newGameButton.addActionListener(clickListener);
        homeButtons.pauseButton.addActionListener(clickListener);
        homeButtons.loadGameButton.addActionListener(clickListener);
        homeButtons.exitButton.addActionListener(clickListener);
        homeButtons.saveGameButton.addActionListener(clickListener);
        homeButtons.exitButton.addActionListener(clickListener);
        homeButtons.scoresButton.addActionListener(clickListener);

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridLayout gridLayout = new GridLayout();
        this.setLayout(gridBagLayout);
        c = new GridBagConstraints();


        c.weighty = 0.02;
        c.weightx = 1;
        c.gridy = 1;
        this.add(homeButtons, c);


        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0.98;
        c.fill = GridBagConstraints.BOTH;
        this.add(poster, c);

        poster.setFocusable(false);
        this.setVisible(true);
    }

    class ClickListener implements ActionListener {
        public
        void actionPerformed(ActionEvent e) {
            if (e.getSource() == homeButtons.pauseButton) {

                if (playPanel.timer.isRunning()) {
                    playPanel.pause();
                    homeButtons.pauseButton.setText("Resume");
                    homeButtons.remove(homeButtons.loadGameButton);
                    homeButtons.constraints.gridx = 1;
                    homeButtons.add(homeButtons.saveGameButton, homeButtons.constraints);
                    homeButtons.revalidate();

                } else {
                    playPanel.pause();
                    homeButtons.pauseButton.setText("Pause");
                    homeButtons.remove(homeButtons.saveGameButton);
                    homeButtons.constraints.gridx = 1;
                    homeButtons.add(homeButtons.loadGameButton, homeButtons.constraints);
                    homeButtons.revalidate();

                }
            }
            if (e.getSource() == homeButtons.newGameButton) {
                MainPage.mainframe.remove(MainPage.mainframe.poster);
                MainPage.mainframe.add(MainPage.mainframe.playPanel, MainPage.mainframe.c);

                playPanel.name = JOptionPane.showInputDialog(MainPage.mainframe, "Enter your Name:", "Your Name", JOptionPane.PLAIN_MESSAGE);
                homeButtons.remove(homeButtons.newGameButton);
                homeButtons.constraints.gridx = 0;
                homeButtons.add(homeButtons.pauseButton, homeButtons.constraints);
                homeButtons.constraints.gridx = 3;
                homeButtons.add(homeButtons.exitButton, homeButtons.constraints);
                homeButtons.revalidate();
                MainPage.mainframe.revalidate();
                playPanel.requestFocusInWindow();
                playPanel.pause();
                repaint();
            }
            if (e.getSource() == homeButtons.saveGameButton) {
                DataManager.dataSaver(playPanel, playPanel.path);


            }
            if (e.getSource() == homeButtons.loadGameButton) {
                if (playPanel.timer.isRunning()) {

                    playPanel.pause();

                }

                Path result = DataManager.dataLoader(playPanel);
                if (result != null) {

                    MainPage.mainframe.remove(MainPage.mainframe.poster);
                    MainPage.mainframe.add(MainPage.mainframe.playPanel, MainPage.mainframe.c);


                    homeButtons.remove(homeButtons.newGameButton);
                    homeButtons.constraints.gridx = 0;
                    homeButtons.add(homeButtons.pauseButton, homeButtons.constraints);
                    homeButtons.constraints.gridx = 3;
                    homeButtons.add(homeButtons.exitButton, homeButtons.constraints);
                    homeButtons.revalidate();
                    MainPage.mainframe.revalidate();
                    playPanel.requestFocusInWindow();

                    playPanel.pause();
                    repaint();

                }

            }
            if (e.getSource() == homeButtons.scoresButton) {
                scores.sort(Comparator.comparing(PlayerScore::getScore).reversed());

                String list = "";
                for (int i = 0; i < Math.min(6, scores.size()); i++) {
                    PlayerScore playerScore = scores.get(i);
                    list = list.concat(playerScore.getName() + "->" + playerScore.getScore() + "\n");
                }
                JOptionPane.showMessageDialog(
                        MainPage.mainframe,
                        list,
                        "Scores", JOptionPane.PLAIN_MESSAGE);
            }
            if (e.getSource() == homeButtons.exitButton) {
                playPanel.pause();
                playPanel.reset(null);
                playPanel.pause();
                MainPage.mainframe.remove(MainPage.mainframe.playPanel);
                MainPage.mainframe.add(MainPage.mainframe.poster, MainPage.mainframe.c);
                MainPage.mainframe.revalidate();

                repaint();
                homeButtons.remove(homeButtons.pauseButton);
                homeButtons.constraints.gridx = 0;
                homeButtons.add(homeButtons.newGameButton, homeButtons.constraints);
                if (homeButtons.saveGameButton.getParent() == homeButtons) {
                    homeButtons.remove(homeButtons.saveGameButton);
                    homeButtons.constraints.gridx = 1;
                    homeButtons.add(homeButtons.loadGameButton, homeButtons.constraints);
                }
                homeButtons.remove(homeButtons.exitButton);
                MainPage.mainframe.revalidate();
                homeButtons.revalidate();
            }
        }
    }
}


