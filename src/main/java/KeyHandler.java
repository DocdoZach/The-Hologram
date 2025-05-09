import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, ctrlPressed, ePressed, nPressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = true;
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = true;
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = true;
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = true;
        if(code == KeyEvent.VK_CONTROL) ctrlPressed = true;
        if(code == KeyEvent.VK_E) ePressed = true;
        if(code == KeyEvent.VK_N) nPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = false;
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = false;
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = false;
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = false;
        if(code == KeyEvent.VK_CONTROL) ctrlPressed = false;
        if(code == KeyEvent.VK_E) ePressed = false;
        if(code == KeyEvent.VK_N) nPressed = false;
    }
}
