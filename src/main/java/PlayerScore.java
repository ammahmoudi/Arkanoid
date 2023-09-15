public
class PlayerScore {
    String name;
    int score;

    public
    PlayerScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public
    String getName() {
        return name;
    }

    public
    PlayerScore setName(String name) {
        this.name = name;
        return this;
    }

    public
    int getScore() {
        return score;
    }

    public
    PlayerScore setScore(int score) {
        this.score = score;
        return this;
    }

    @Override
    public
    String toString() {
        return
        name +
                "," + score;
    }
}
