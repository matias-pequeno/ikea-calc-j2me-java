package app;

/**************************
 * MainMIDlet class
 * 
 * MIDlet entry point.
 * 
 *
 *     
 */
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class MainMIDlet extends MIDlet
{
    public final MainCanvas canvas;

    public MainMIDlet()
    {
        canvas = new MainCanvas(this);
        resumeRequest();
    }

    public void startApp() throws MIDletStateChangeException
    {
        MainCanvas.display = Display.getDisplay(this);
        MainCanvas.display.setCurrent(canvas);
    }

    public void pauseApp()
    {
        canvas.hideNotify();
        notifyPaused();
    }

    public void destroyApp(boolean unconditional)
    {
        notifyDestroyed();
    }

}
