package org.battleship.model;

public class Cell {

    private boolean maybeShip;
    private boolean state;


    public boolean isMaybeShip() {
        return maybeShip;
    }
    public void setMaybeShip(boolean maybeShip) {
        this.maybeShip = maybeShip;
    }
    public boolean isState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }
}
