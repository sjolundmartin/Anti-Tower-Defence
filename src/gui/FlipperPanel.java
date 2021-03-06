package gui;

import gameLogic.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Flipper panel is a panel in the gui that contains all the flipper buttons
 * above the game panel (game field).
 * @author oi16ohn, oi16jsn
 * @since 2018-12-10
 */

public class FlipperPanel extends JPanel {

    private ArrayList<JButton> flipperButtons = new ArrayList<>();
    private ArrayList<Position> positions;
    private int buttonWidth;
    private ActionListener flipperPressed;

    /**
     * Constructor of class.
     *
     * @param positions List of flipper tile positions.
     * @param flipperPressed ActionListener for the flipper tiles.
     * @param panelWidth Width for a JPanel.
     * @param buttonWidth Width for the button.
     */
    public FlipperPanel(ArrayList<Position> positions,
                        ActionListener flipperPressed, int panelWidth,
                        int buttonWidth) {
        this.flipperPressed = flipperPressed;
        this.buttonWidth = buttonWidth;
        this.positions = positions;
        setBounds(0,0,panelWidth,panelWidth);
        setPreferredSize(new Dimension(panelWidth, panelWidth));
        setOpaque(false);
        setLayout(null);
        setButtons();
    }

    /**
     * Sets the flipper buttons to the right position of the flipptiles.
     */
    private void setButtons() {
        FlipperButton flipperButton;
        Position pos;
        for (int i = 0; i < positions.size(); i++) {
            flipperButton = new FlipperButton(positions.get(i));
            pos = new Position(positions.get(i).getX(),
                    positions.get(i).getY());
            pos.addVector(new Position(-buttonWidth/2,-buttonWidth/2));
            flipperButton.setBorder(null);
            flipperButton.setBounds(pos.getX(),pos.getY(),
                    buttonWidth, buttonWidth);
            flipperButton.addActionListener(flipperPressed);
            add(flipperButton);
            flipperButtons.add(flipperButton);
        }
    }

    /**
     * Updates the flipper panel when a new map is placed in the game panel.
     */
    public void updateFlippers(ArrayList<Position> positions) {
        for (JButton flipper  : flipperButtons ) {
            this.remove(flipper);
        }
        flipperButtons = new ArrayList<>();
        this.positions = positions;
        setButtons();
    }
}
