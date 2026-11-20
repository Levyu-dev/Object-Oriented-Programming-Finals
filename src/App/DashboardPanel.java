package App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPanel extends BaseScreen {
    private JLabel welcomeLabel;
    private JLabel xpLabel;
    private JProgressBar xpBar;

    public DashboardPanel(ApplicationController controller) {
        super(controller);
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.PALE_LIME);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setBackground(Theme.PALE_LIME);

        welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcomeLabel.setForeground(Theme.TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel xpPanel = Theme.createRoundedCard(Theme.DEEP_TEAL, 12);
        xpPanel.setLayout(new BorderLayout(6, 6));
        xpPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        xpLabel = new JLabel("Level 1 — 500 XP");
        xpLabel.setForeground(Theme.BRIGHT_LIME);
        xpLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        xpBar = Theme.createProgressBar(500, 2000, Theme.BRIGHT_LIME, new Color(0, 50, 58));
        xpPanel.add(xpLabel, BorderLayout.NORTH);
        xpPanel.add(xpBar, BorderLayout.CENTER);

        JPanel continuePanel = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
        continuePanel.setLayout(new BorderLayout(8, 8));
        continuePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel contTitle = new JLabel("Continue: OOP Basics (6/10)");
        contTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        contTitle.setForeground(Theme.TEXT_DARK);

        JButton resumeBtn = Theme.createPrimaryButton("Resume (+100 XP)");
        resumeBtn.setPreferredSize(new Dimension(0, 34));
        resumeBtn.addActionListener(e -> {
            controller.getSession().addXP(100);
            refresh();
            controller.showAlert("Lesson completed! +100 XP added.");
        });

        continuePanel.add(contTitle, BorderLayout.NORTH);
        continuePanel.add(Theme.createProgressBar(6, 10, Theme.DEEP_TEAL, Theme.WHITE), BorderLayout.CENTER);
        continuePanel.add(resumeBtn, BorderLayout.SOUTH);

        topBox.add(welcomeLabel);
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(xpPanel);
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(continuePanel);

        JPanel quickGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        quickGrid.setBackground(Theme.PALE_LIME);

        JButton b1 = Theme.createPrimaryButton("<html><center><b>Practice</b><br><small>Own pace</small></center></html>");
        b1.addActionListener(e -> controller.showStudentScreen("PRACTICE"));

        JButton b2 = Theme.createPrimaryButton("<html><center><b>Timed Quiz</b><br><small>Beat clock</small></center></html>");
        b2.addActionListener(e -> controller.launchQuizSession(5));

        JButton b3 = Theme.createPrimaryButton("<html><center><b>Final Boss</b><br><small>Big test</small></center></html>");
        b3.addActionListener(e -> controller.launchQuizSession(10));

        JButton b4 = Theme.createPrimaryButton("<html><center><b>Party Mode</b><br><small>With friends</small></center></html>");
        b4.addActionListener(e -> controller.showAlert("Starting Party Mode..."));

        quickGrid.add(b1);
        quickGrid.add(b2);
        quickGrid.add(b3);
        quickGrid.add(b4);

        JButton buddyBtn = Theme.createWhiteButton("<html><center><b>StudyBuddy:</b> Every bug you fix makes you better!</center></html>");
        buddyBtn.setPreferredSize(new Dimension(0, 46));
        buddyBtn.addActionListener(e -> controller.showAlert("StudyBuddy Tip: Keep fields private and use getters/setters!"));

        add(topBox, BorderLayout.NORTH);
        add(quickGrid, BorderLayout.CENTER);
        add(buddyBtn, BorderLayout.SOUTH);
    }

    @Override
    public void refresh() {
        UserSession.StudentRecord s = controller.getSession().getCurrentStudent();
        int level = (s.getXp() / 250) + 1;
        welcomeLabel.setText("Hi, @" + s.getName() + " (" + s.getSection() + ")");
        xpLabel.setText("Level " + level + " — " + s.getXp() + " XP");
        xpBar.setValue(Math.min(2000, s.getXp()));
    }
}
