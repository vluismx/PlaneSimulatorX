package es.pagoru.planesimulatorx.input.keys;

import es.pagoru.planesimulatorx.input.KeyInterface;
import es.pagoru.planesimulatorx.windows.MenuWindows;

import java.awt.event.KeyEvent;

/**
 * Created by Pablo on 13/11/2016.
 */
public class KeyLeft implements KeyInterface {
    @Override
    public int getKeyCode() {
        return KeyEvent.VK_LEFT;
    }

    @Override
    public void executePressed() {
    }

    /**
     * Una vegada la flecha cap a l'esquerra es deixa anar,
     * es mou l'opció actual del menu cap a la anterior.
     */
    @Override
    public void executeReleased() {
        MenuWindows.getCurrentMenu().moveSelection(true);
    }
}
