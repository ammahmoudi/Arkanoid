import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public
class Bonus extends Thing {
    int dy = 2;
    int type;
//    1 fireball
//    2 multi ball
//    3 long paddle
//    4 short paddle
//    5 fast ball
//    6 slow ball
//    7 crazy paddle
//    8 random

    public
    Bonus(int type) {
        this.type = type;
        switch (type) {
            case 1:
                this.image = new ImageIcon("src/main/resources/bonus_fireball.png").getImage();
                getDimensions();
                break;
            case 2:
                this.image = new ImageIcon("src/main/resources/bonus_multiball.png").getImage();
                getDimensions();
                break;
            case 3:
                this.image = new ImageIcon("src/main/resources/bonus_longpaddle.png").getImage();
                getDimensions();
                break;
            case 4:
                this.image = new ImageIcon("src/main/resources/bonus_shortpaddle.png").getImage();
                getDimensions();
                break;
            case 5:
                this.image = new ImageIcon("src/main/resources/bonus_fastball.png").getImage();
                getDimensions();
                break;
            case 6:
                this.image = new ImageIcon("src/main/resources/bonus_slowball.png").getImage();
                getDimensions();
                break;
            case 7:
                this.image = new ImageIcon("src/main/resources/bonus_crazypaddle.png").getImage();
                getDimensions();
                break;
            case 8:
                this.image = new ImageIcon("src/main/resources/bonus_random.png").getImage();
                getDimensions();
                break;
        }
    }

    void move() {

        y += dy;

    }

    public
    void act(PlayPanel playPanel, boolean random) {
        Timer tempTimer = null;
        Paddle paddle = playPanel.paddle;
        LinkedList <Ball> balls = playPanel.balls;

        int cases = this.getType();
        if (random) cases = ThreadLocalRandom.current().nextInt(1, 8);

        switch (cases) {
            case 1:

                for (Ball ball : balls) {
                    playPanel.timers.add(ball.fire(playPanel, this));
                }
                break;
            case 2:
                Ball ball_2 = new Ball();
                Ball ball_3 = new Ball();
                balls.add(ball_2);
                balls.add(ball_3);
                break;

            case 3:
                playPanel.timers.add(paddle.changeSize(1, playPanel, this));

                tempTimer = new Timer(10000, new ActionListener() {
                    @Override
                    public
                    void actionPerformed(ActionEvent e) {
                        playPanel.activeBonuses.remove(this);
                    }
                });

                break;
            case 4:
                playPanel.timers.add(paddle.changeSize(0, playPanel, this));
                break;

            case 5:

                for (Ball ball : balls) {

                    playPanel.timers.add(ball.tempChangeSpeed(1.01, playPanel, this));
                }

                break;
            case 6:

                for (Ball ball : balls) {
                    playPanel.timers.add(ball.tempChangeSpeed(0.99, playPanel, this));
                }

                break;
            case 7:
                playPanel.timers.add(paddle.crazy(playPanel, this));

                break;
            case 8:
                this.act(playPanel, true);
                break;

        }
        if (tempTimer != null) {
            playPanel.timers.add(tempTimer);
            tempTimer.setInitialDelay(1);
            tempTimer.setRepeats(false);
            tempTimer.start();
        }
    }

    Ellipse2D getCircle() {
        return new Ellipse2D.Double(x, y, image.getWidth(null), image.getHeight(null));

    }

    public
    int getType() {
        return type;
    }

    public
    Bonus setType(int type) {
        this.type = type;
        return this;
    }


    @Override
    public
    String toString() {
        return
                type +
                        "," + x +
                        "," + y
                ;
    }
}
