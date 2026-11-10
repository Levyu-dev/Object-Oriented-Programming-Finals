import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel(ApplicationController controller) {
        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.WHITE);
        setBorder(new EmptyBorder(20, 28, 20, 28));

        // Top: Welcome + XP Bar + Continue Learning Bar
        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setBackground(Theme.WHITE);

        JLabel welcome = new JLabel("Welcome! @user");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcome.setForeground(Theme.TEXT_DARK);

        JPanel xpPanel = new JPanel(new BorderLayout(6, 6));
        xpPanel.setBackground(Theme.CARD_RED); 
        xpPanel.setBorder(new EmptyBorder(12, 15, 12, 15));
        JLabel xpLabel = new JLabel("Your current level: 1,200 XP (250 XP until Level 6)");
        xpLabel.setForeground(Theme.WHITE);
        xpLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        xpPanel.add(xpLabel, BorderLayout.NORTH);
        xpPanel.add(Theme.createProgressBar(1200, 1450, Theme.WHITE, new Color(140, 35, 12)), BorderLayout.CENTER);

        JPanel continuePanel = new JPanel(new BorderLayout(10, 6));
        continuePanel.setBackground(Theme.WHITE);
        continuePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.CARD_RED, 2, true), new EmptyBorder(10, 14, 10, 14)
        ));
        JPanel progressInfo = new JPanel(new BorderLayout(5, 5));
        progressInfo.setBackground(Theme.WHITE);
        progressInfo.add(new JLabel("Continue Learning: Object-Oriented Programming (6/10)"), BorderLayout.NORTH);
        progressInfo.add(Theme.createProgressBar(6, 10, Theme.CARD_RED, Theme.BG_YELLOW), BorderLayout.CENTER);

        JButton resumeBtn = Theme.createRedButton("Resume");
        resumeBtn.addActionListener(e -> controller.showAlert("Resuming OOP Quiz at Question 7 of 10..."));
        continuePanel.add(progressInfo, BorderLayout.CENTER);
        continuePanel.add(resumeBtn, BorderLayout.EAST);

        topBox.add(welcome);
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(xpPanel);
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(continuePanel);

        // Center: Quick Access Grid
        JPanel quickGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        quickGrid.setBackground(Theme.WHITE);
        JButton b1 = Theme.createRedButton("Practice");
        b1.addActionListener(e -> controller.showStudentScreen("PRACTICE"));       
        JButton b2 = Theme.createRedButton("Timed Quiz");
        b2.addActionListener(e -> controller.showAlert("Connecting to Timed Quiz..."));
        JButton b3 = Theme.createRedButton("Final Boss");
        b3.addActionListener(e -> controller.showAlert("Connecting to Final Boss..."));
        JButton b4 = Theme.createRedButton("Party Mode");
        b4.addActionListener(e -> controller.showAlert("Connecting to Party Mode..."));
        quickGrid.add(b1); quickGrid.add(b2); quickGrid.add(b3); quickGrid.add(b4);

        // Bottom: Study Buddy Tip
        JButton buddyBtn = Theme.createWhiteButton("StudyBuddy: \"Every bug you fix makes you a better programmer.\"");
        buddyBtn.setBorder(new LineBorder(Theme.CARD_RED, 2, true));
        buddyBtn.addActionListener(e -> controller.showAlert("StudyBuddy Tip: Keep fields private and use getters/setters!"));

        add(topBox, BorderLayout.NORTH);
        add(quickGrid, BorderLayout.CENTER);
        add(buddyBtn, BorderLayout.SOUTH);
    }
}
