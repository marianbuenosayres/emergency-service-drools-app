/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.emergencyservice.worldui.slick.listener;

import com.wordpress.salaboy.emergencyservice.worldui.slick.WorldUI;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

/**
 *
 * @author esteban
 */
public class WorldKeyListener implements KeyListener {

    private final WorldUI worldUI;

    public WorldKeyListener(WorldUI cityMapUI) {
        this.worldUI = cityMapUI;
    }

    public void keyPressed(int i, char c) {
        this.worldUI.getCurrentRenderer().onKeyPressed(i, c);
    }

    public void keyReleased(int i, char c) {
        this.worldUI.getCurrentRenderer().onKeyReleased(i, c);
    }

    public void setInput(Input input) {
    }

    public boolean isAcceptingInput() {
        return true;
    }

    public void inputEnded() {
    }

    public void inputStarted() {
    }
}
