package Creatures;

import Main.Direction;
import Main.Position;

public interface CreatureInterface {

    void setDirection(Direction direction);

    void setCurrentHealth(int newHealth);
    int getCurrentHealth();

    void setCurrentSpeed(int newSpeed);
    int getCurrentSpeed();

    boolean isDead();

    void move();

    void setGoaled();

    boolean inGoal();
}