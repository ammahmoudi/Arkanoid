import javax.swing.*;
import java.util.concurrent.ThreadLocalRandom;

public
class Brick extends Thing {
    int blood = 1;
    int type = 1;
    boolean visible = true;

    public
    Brick(int type, int x, int y) {
        this.x = x;
        this.y = y;
        this.type = type;
        switch (type) {
            case 1:
                this.image = new ImageIcon("src/main/resources/brick_glass.png").getImage();
                getDimensions();
                this.blood = 1;
                break;
            case 2:
                this.image = new ImageIcon("src/main/resources/brick_wood.png").getImage();
                getDimensions();
                this.blood = 2;
                break;
            case 3:
                this.image = new ImageIcon("src/main/resources/brick_invisible.png").getImage();
                getDimensions();
                this.blood = 1;
                break;
            case 4:
                this.image = new ImageIcon("src/main/resources/brick_blink.png").getImage();
                getDimensions();
                this.blood = 1;
                break;
            case 5:
                this.image = new ImageIcon("src/main/resources/brick_bonus.png").getImage();
                getDimensions();
                this.blood = 1;

                break;
        }

    }

    public
    int getBlood() {
        return blood;
    }

    public
    Brick setBlood(int blood) {
        this.blood = blood;
        return this;
    }

    public
    int getType() {
        return type;
    }

    public
    Brick setType(int type) {
        this.type = type;
        return this;
    }


    // 1 glass 2 wood 3invisible 4 blink 5 bonus

    public
    void brakeWood() {
        this.image = new ImageIcon("src/main/resources/brick_wood_1.png").getImage();
        //getDimensions();
        // this.blood=1;
    }

    public
    void bleed(Ball ball) {
        int blood;
        blood = this.getBlood() - ball.getPower();
        this.setBlood(Math.max(blood, 0));
    }

    public
    boolean isVisible() {
        return visible;
    }

    public
    Brick setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public
    void intersects(Ball ball, PlayPanel playPanel) {
        if (this.getBlood() != 0 && this.isVisible()) {
            if (ball.getCircle().intersects(this.getRectangle())) {
                playPanel.score++;
                if (ball.getPower() != 2) {

                    ball.setDy(ball.getDy() * (-1));
                }
                if (this.getType() == 5) {
                    Bonus bonus;
                    bonus = new Bonus(ThreadLocalRandom.current().nextInt(1, 9));
                    bonus.setX(this.getX());
                    bonus.setY(this.getY());
                    playPanel.floatingBonuses.add(bonus);
                }

                if (this.getType() == 2) {
                    this.brakeWood();

                }
                if (this.getType() == 3) {


                }

                this.bleed(ball);


            }
        }
    }

    @Override
    public
    String toString() {
        return
                blood +
                        "," + type +
                        "," + visible +
                        "," + x +
                        "," + y
                ;
    }
}
