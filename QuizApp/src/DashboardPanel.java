import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private ApplicationController controller;
    private JLabel welcomeLabel;
    private JLabel xpLabel;
    private JProgressBar xpBar;

    public DashboardPanel(ApplicationController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG_CREAM);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setBackground(Theme.BG_CREAM);

        welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcomeLabel.setForeground(Theme.TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel xpPanel = Theme.createRoundedCard(Theme.CARD_RED, 12);
        xpPanel.setLayout(new BorderLayout(6, 6));
        xpPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        xpLabel = new JLabel("Level 1 — 500 XP");
        xpLabel.setForeground(Theme.WHITE);
        xpLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        xpBar = Theme.createProgressBar(500, 2000, Theme.WHITE, new Color(140, 35, 12));
        xpPanel.add(xpLabel, BorderLayout.NORTH);
        xpPanel.add(xpBar, BorderLayout.CENTER);

        JPanel continuePanel = Theme.createRoundedCard(Theme.BG_YELLOW, 12);
        continuePanel.setLayout(new BorderLayout(8, 8));
        continuePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel contTitle = new JLabel("Continue: OOP Basics (6/10)");
        contTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        contTitle.setForeground(Theme.TEXT_DARK);

        JButton resumeBtn = Theme.createRedButton("Resume (+100 XP)");
        resumeBtn.setPreferredSize(new Dimension(0, 34));
        resumeBtn.addActionListener(e -> {
            controller.getSession().addXP(100);
            refresh();
            controller.showAlert("Lesson completed! +100 XP added.");
        });

        continuePanel.add(contTitle, BorderLayout.NORTH);
        continuePanel.add(Theme.createProgressBar(6, 10, Theme.CARD_RED, Theme.WHITE), BorderLayout.CENTER);
        continuePanel.add(resumeBtn, BorderLayout.SOUTH);

        topBox.add(welcomeLabel);
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(xpPanel);
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(continuePanel);

        JPanel quickGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        quickGrid.setBackground(Theme.BG_CREAM);

        JButton b1 = Theme.createRedButton("<html><center><b>Practice</b><br><small>Own pace</small></center></html>");
        b1.addActionListener(e -> controller.showStudentScreen("PRACTICE"));

        JButton b2 = Theme.createRedButton("<html><center><b>Timed Quiz</b><br><small>Beat clock</small></center></html>");
        b2.addActionListener(e -> controller.showAlert("Starting Timed Quiz..."));

        JButton b3 = Theme.createRedButton("<html><center><b>Final Boss</b><br><small>Big test</small></center></html>");
        b3.addActionListener(e -> controller.showAlert("Starting Final Boss..."));

        JButton b4 = Theme.createRedButton("<html><center><b>Party Mode</b><br><small>With friends</small></center></html>");
        b4.addActionListener(e -> controller.showAlert("Starting Party Mode..."));

        quickGrid.add(b1); quickGrid.add(b2); quickGrid.add(b3); quickGrid.add(b4);

        JButton buddyBtn = Theme.createWhiteButton("<html><center><b>StudyBuddy:</b> Every bug you fix makes you better!</center></html>");
        buddyBtn.setPreferredSize(new Dimension(0, 46));
        buddyBtn.addActionListener(e -> controller.showAlert("StudyBuddy Tip: Keep fields private and use getters/setters!"));

        add(topBox, BorderLayout.NORTH);
        add(quickGrid, BorderLayout.CENTER);
        add(buddyBtn, BorderLayout.SOUTH);
    }

    public void refresh() {
        UserSession.StudentRecord s = controller.getSession().getCurrentStudent();
        int level = (s.xp / 250) + 1;
        welcomeLabel.setText("Hi, @" + s.name + " (" + s.section + ")");
        xpLabel.setText("Level " + level + " — " + s.xp + " XP");
        xpBar.setValue(Math.min(2000, s.xp));
    }
}
