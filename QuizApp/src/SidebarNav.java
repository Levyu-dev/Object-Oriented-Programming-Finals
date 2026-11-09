import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SidebarNav extends JPanel {

    public SidebarNav(ApplicationController controller) {
        setBackground(Theme.BG_YELLOW);
        setPreferredSize(new Dimension(85, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(25, 10, 25, 10));

        // 6 Circular Navigation Buttons matching Page 4 of your slides
        JButton homeBtn = Theme.createCircleButton("Home", "Dashboard");
        homeBtn.addActionListener(e -> controller.showAppScreen("DASHBOARD"));

        JButton practiceBtn = Theme.createCircleButton("Prac", "Practice Mode");
        practiceBtn.addActionListener(e -> controller.showAppScreen("PRACTICE"));

        JButton gameBtn = Theme.createCircleButton("Game", "Game Modes");
        gameBtn.addActionListener(e -> controller.showAppScreen("GAME_MODE"));

        JButton buddyBtn = Theme.createCircleButton("Bot", "Study Buddy");
        buddyBtn.addActionListener(e -> controller.showAppScreen("STUDY_BUDDY"));

        JButton statsBtn = Theme.createCircleButton("XP", "Student Progress");
        statsBtn.addActionListener(e -> controller.showAppScreen("DASHBOARD"));

        JButton exitBtn = Theme.createCircleButton("Exit", "Back to Start");
        exitBtn.addActionListener(e -> controller.showRootScreen("WELCOME"));

        add(homeBtn);
        add(Box.createVerticalStrut(18));
        add(practiceBtn);
        add(Box.createVerticalStrut(18));
        add(gameBtn);
        add(Box.createVerticalStrut(18));
        add(buddyBtn);
        add(Box.createVerticalStrut(18));
        add(statsBtn);
        add(Box.createVerticalGlue());
        add(exitBtn);
    }
}
