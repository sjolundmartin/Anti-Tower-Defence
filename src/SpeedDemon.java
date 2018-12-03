public class SpeedDemon extends DefaultCreature {

    public static final int MAXHEALTH = 100;
    public static final int SPEED = 10;
    public static final int COST = 10;

    public SpeedDemon(Position pos) {
        super(pos);
        super.setCurrentHealth(MAXHEALTH);
        resetStats();
    }

    public void resetStats() {
        super.setCurrentSpeed(SPEED);
    }
}