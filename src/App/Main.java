package App;

import App.controller.ApplicationController;
import javax.swing.SwingUtilities;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ApplicationController applicationController = new ApplicationController();
            applicationController.start();
        });
    }
}
