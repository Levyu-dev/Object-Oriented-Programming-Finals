package App;

import javax.swing.JPanel;

public abstract class BaseScreen extends JPanel {
    protected ApplicationController controller;

    public BaseScreen(ApplicationController controller) {
        this.controller = controller;
    }

    public abstract void refresh();
}
