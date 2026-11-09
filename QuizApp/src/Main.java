import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ApplicationController app = new ApplicationController();
            app.start();
        });
    }
}
