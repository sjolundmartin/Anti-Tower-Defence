package Creatures;

import Main.Direction;
import Main.Position;
import Tiles.EntryTeleporterTile;
import Tiles.ExitTeleporterTile;
import Tiles.Tile;
import formatters.ImageLoader;

import java.awt.*;

/**
 * Created by jontor on 2018-12-10.
 */
public class PortalusTotalus extends Creature {
    public static final int MAXHEALTH = 100;
    public static final int SPEED = 2;
    public static final int COST = 10;
    public static final int teleportDistance = 100;

    public static final Image image = ImageLoader.getImageLoader().getImage(
            "portalusTotalus.png");

    private EntryTeleporterTile entryTeleporterTile;
    private ExitTeleporterTile exitTeleporterTile;
    int teleportCountDown = teleportDistance;

    public PortalusTotalus(Position pos, Direction dir) {
        super(pos, image, dir, 0, MAXHEALTH);
    }

    public Tile createEntryTeleporterTile(){
        if(entryTeleporterTile == null) {
            entryTeleporterTile = new EntryTeleporterTile(getDirection(), getPosition());
        }
        return entryTeleporterTile;

    }
    @Override
    public void move() {
        getPosition().addVector(getDirection().asVector());
        getHealthbar().setPosition(getPosition());
        if(entryTeleporterTile != null){
            teleportCountDown--;
        }
    }

    public int getTeleportCountDown() {
        return teleportCountDown;
    }

    public Tile createExitTeleporterTile(){
        entryTeleporterTile.setPortalLocation(getPosition());
        exitTeleporterTile = new ExitTeleporterTile(getDirection(),getPosition());
        setCurrentHealth(0);
        return exitTeleporterTile;
    }

}
