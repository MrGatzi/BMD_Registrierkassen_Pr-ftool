
import java.awt.*;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class Application {

  /**
   * Construct and show the application.
   */
	public MainFrame frame = new MainFrame();
  public Application() {
   
    // Validate frames that have preset sizes
    frame.validate();

    // Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation( (screenSize.width - frameSize.width) / 2,
                      (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }

  /**
   * Application entry point.
   *
   * @param args String[]
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new Application();
      }
    });
  };

}
