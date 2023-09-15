import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;


public
class PlayPanel extends JPanel {
    String name = "";
    int score;
    int blood = 3;
    Paddle paddle;
    LinkedList <Brick> bricks;
    LinkedList <Bonus> floatingBonuses;
    LinkedList <Ball> balls;
    LinkedList <Timer> timers = new LinkedList <>();
    LinkedList <Bonus> activeBonuses = new LinkedList <>();
    Path path;
    Timer timer;
    Timer brickTimer;
    Timer invisibleBricksTimer;





    public
    PlayPanel() {
        initialize();
    }

    public
    int getBlood() {
        return blood;
    }

    public
    PlayPanel setBlood(int blood) {
        this.blood = blood;
        return this;
    }

    public
    void bleed() {
        this.setBlood(this.getBlood() - 1);
    }

    public
    void initialize() {
        addKeyListener(new Adapter());
        setFocusable(true);
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        this.setBackground(new Color(0xFFEBEB));

        paddle = new Paddle();
        balls = new LinkedList <>();
        floatingBonuses = new LinkedList <>();
        bricks = new LinkedList <>();
        path = null;

        if (balls.isEmpty()) {
            Ball ball = new Ball();
            balls.add(ball);
        }

        timer = new Timer(5, new PlayProcess());
        timer.start();
        timers.add(timer);

        brickTimer = new Timer(3000, new ActionListener() {
            @Override
            public
            void actionPerformed(ActionEvent e) {
                for (Brick brick : bricks) {
                    brick.setY(brick.getY() + 56);
                }

                for (int i = 0; i < 5; i++) {
                    Brick tempBrick;
                    tempBrick = new Brick(ThreadLocalRandom.current().nextInt(1, 6), i * 116 + 16, 40);
                    bricks.add(tempBrick);

                }
            }
        });
        timers.add(brickTimer);
        brickTimer.start();

        invisibleBricksTimer = new Timer(1400, new ActionListener() {
            @Override
            public
            void actionPerformed(ActionEvent e) {
                for (Brick brick : bricks) {
                    if (brick.getType() == 4) {
                        brick.setVisible(!brick.isVisible());
                    }
                }


            }
        });
        invisibleBricksTimer.start();
        timers.add(invisibleBricksTimer);


    }


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

        drawObjects(g2d);
        Toolkit.getDefaultToolkit().sync();
    }

    private
    void
    drawObjects(Graphics2D g2d) {

        g2d.drawImage(paddle.getImage(),
                paddle.getX(),
                paddle.getY(),
                paddle.getWidth(),
                paddle.getHeight(),
                this);
        for (Ball ball : balls) {
            g2d.drawImage(ball.getImage(),
                    ball.getX(),
                    ball.getY(),
                    ball.getWidth(),
                    ball.getHeight(),
                    this);
        }
        for (Bonus bonus : floatingBonuses) {
            g2d.drawImage(bonus.getImage(),
                    bonus.getX(),
                    bonus.getY(),
                    bonus.getWidth(),
                    bonus.getHeight(),
                    this);
        }
        for (Brick brick : bricks) {
            if (brick.getBlood() != 0 && brick.isVisible()) {
                g2d.drawImage(brick.getImage(),
                        brick.getX(),
                        brick.getY(),
                        brick.getWidth(),
                        brick.getHeight(),
                        this);
            }
        }
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g2d.drawImage(Constants.HEART,
               3,
                Constants.HEIGHT-35,
               16,
               16,
                this);
        g2d.drawImage(Constants.STAR,
                33,
                Constants.HEIGHT-35,
                16,
                16,
                this);

        g2d.drawImage(Constants.USER,
                3,
                5,
                16,
                16,
                this);

        g2d.drawString(String.valueOf(this.getBlood()), 20, Constants.HEIGHT-20);
        g2d.drawString(name, 20, 20);
        g2d.drawString(String.valueOf(score), 52, Constants.HEIGHT-20);
    }

    private
    void runPlayProcess() {
        paddle.move();
        for (Ball ball : balls) {
            ball.move();
            ball.changeSpeed(1.0001);
        }
        for (Bonus bonus : floatingBonuses) {
            bonus.move();
        }
        collision();
        ballDieChecker();
        gameOverChecker();

        repaint();

    }

    void collision() {


        for (Iterator <Bonus> iterator = floatingBonuses.iterator(); iterator.hasNext(); ) {
            Bonus bonus = iterator.next();
            if (bonus.reachSouth()) iterator.remove();
            if (bonus.getCircle().intersects(paddle.getRectangle())) {
                activeBonuses.add(bonus);

                bonus.act(this, false);
                iterator.remove();


            }
        }
        for (Ball ball : balls) {
            int bound = bricks.size();
            for (Brick brick : bricks) {
                brick.intersects(ball, this);
            }
            if (ball.getCircle().intersects(paddle.getRectangle())) {
                double paddleCorner = paddle.getRectangle().getMinX();
                double ballCorner = ball.getCircle().getMinX();
                double collisionRatio = (ballCorner - paddleCorner) / paddle.getWidth();
                if (collisionRatio <= 0.3) {

                    ball.setDy(ball.getDy() * (-1));
                    ball.setDx(ball.getDx() - 1);
                }
                if (collisionRatio > 0.3 && collisionRatio < 0.7) {
                    ball.setDy(ball.getDy() * (-1));

                }

                if (collisionRatio >= 0.7) {
                    ball.setDx(ball.getDx() + 1);
                    ball.setDy(ball.getDy() * (-1));

                }
            }
        }
    }

    void ballDieChecker() {
        LinkedList <Ball> temptBalls = new LinkedList <>(balls);
        for (Ball ball : temptBalls) {
            if (ball.getY() >= Constants.HEIGHT - ball.getHeight()) {
                ball.resetPosition();
                if (balls.size() > 1) {
                    balls.remove(ball);
                } else {
                    bleed();

                }


            }
        }

    }

    public
    void pause() {
        if (this.timer.isRunning()) {
            for (Timer timer : timers) {
                timer.stop();
            }
        } else {
            this.setFocusable(true);
            for (Timer timer : timers) {
                timer.start();
            }

        }
    }

    public
    void gameOverChecker() {
        for (Brick brick : bricks) {
            if (brick.getBlood() > 0) {
                if (brick.reachSouth()) {
                    this.pause();
                    JOptionPane.showMessageDialog(this, "Game over!\n Your score: " + this.score,"Game Over",JOptionPane.ERROR_MESSAGE);
                    PlayerScore playerScore = new PlayerScore(this.name, this.score);
                    MainPage.mainframe.scores.add(playerScore);
                    DataManager.scoreSaver(MainPage.mainframe.scores);
                    MainPage.mainframe.homeButtons.exitButton.doClick();
                }
            }
        }
        if (blood == 0) {

            this.pause();
            JOptionPane.showMessageDialog(this, "Game over!\n Your score: " + this.score,"Game Over",JOptionPane.ERROR_MESSAGE);
            PlayerScore playerScore = new PlayerScore(this.name, this.score);
            MainPage.mainframe.scores.add(playerScore);
            DataManager.scoreSaver(MainPage.mainframe.scores);
            MainPage.mainframe.homeButtons.exitButton.doClick();


        }
    }

    public
    void reset(String name) {
        if (name == null) {
            name = "";
        }
        this.name = name;
        this.blood = 3;
        this.activeBonuses.clear();
        this.bricks.clear();
        this.balls.clear();
        this.balls.add(new Ball());
        this.pause();
        this.timers.clear();
        this.paddle = new Paddle();
        timer.restart();
        brickTimer.restart();
        invisibleBricksTimer.restart();
        this.timers.add(timer);
        this.timers.add(brickTimer);
        this.timers.add(invisibleBricksTimer);

        this.floatingBonuses.clear();
        this.score = 0;

    }

    private
    class Adapter extends KeyAdapter {
        @Override
        public
        void keyPressed(KeyEvent e) {
            paddle.keyPressed(e);
        }

        @Override
        public
        void keyReleased(KeyEvent e) {
            paddle.keyReleased(e);
        }
    }

    private
    class PlayProcess implements ActionListener {
        @Override
        public
        void actionPerformed(ActionEvent e) {
            runPlayProcess();
        }
    }
}
