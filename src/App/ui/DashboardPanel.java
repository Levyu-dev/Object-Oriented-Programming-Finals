package App.ui;

import App.controller.ApplicationController;
import App.model.UserSession;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPanel extends BaseScreen {
    private JLabel welcomeLabel;
    private JLabel xpLabel;
    private JProgressBar xpBar;
    private JLabel continueTitleLabel;
    private JProgressBar continueProgress;
    private JButton continueCourseButton;
    private JPanel continuePanel;

    public DashboardPanel(ApplicationController controller) {
        super(controller);
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.PALE_LIME);
        setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setBackground(Theme.PALE_LIME);

        welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 20));
        welcomeLabel.setForeground(Theme.TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel xpPanel = Theme.createRoundedCard(Theme.DEEP_TEAL, 12);
        xpPanel.setLayout(new BorderLayout(6, 6));
        xpPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        xpLabel = new JLabel("Level 1 - 0 XP");
        xpLabel.setForeground(Theme.BRIGHT_LIME);
        xpLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 13));
        xpBar = Theme.createProgressBar(0, 2000, Theme.BRIGHT_LIME, new Color(0, 50, 58));
        xpPanel.add(xpLabel, BorderLayout.NORTH);
        xpPanel.add(xpBar, BorderLayout.CENTER);

        continuePanel = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
        continuePanel.setLayout(new BorderLayout(8, 8));
        continuePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        continuePanel.setVisible(false);

        continueTitleLabel = new JLabel("Continue OOP Basics Roadmap");
        continueTitleLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 13));
        continueTitleLabel.setForeground(Theme.TEXT_DARK);

        continueProgress = Theme.createProgressBar(
                0, 10, Theme.DEEP_TEAL, Theme.WHITE);

        continueCourseButton = Theme.createPrimaryButton("Continue Course");
        continueCourseButton.setPreferredSize(new Dimension(0, 34));
        continueCourseButton.addActionListener(event -> {
            UserSession userSession = controller.getSession();
            String courseName = userSession.getDashboardCourse();
            if (courseName != null) controller.showStudentScreen("COURSES");
        });

        continuePanel.add(continueTitleLabel, BorderLayout.NORTH);
        continuePanel.add(continueProgress, BorderLayout.CENTER);
        continuePanel.add(continueCourseButton, BorderLayout.SOUTH);

        topBox.add(welcomeLabel);
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(xpPanel);
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(continuePanel);

        JPanel activityButtonGrid = new JPanel(new GridLayout(1, 4, 6, 0));
        activityButtonGrid.setBackground(Theme.PALE_LIME);
        activityButtonGrid.setPreferredSize(new Dimension(0, 135));
        activityButtonGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 135));

        JButton practiceButton = Theme.createPrimaryButton("<html><center><b>Practice</b><br><small>Own pace</small></center></html>");
        practiceButton.addActionListener(event -> controller.showStudentScreen("PRACTICE"));

        JButton timedQuizButton = Theme.createPrimaryButton("<html><center><b>Timed Quiz</b><br><small>Beat clock</small></center></html>");
        timedQuizButton.addActionListener(event -> controller.launchTimedQuiz(5));

        JButton finalBossButton = Theme.createPrimaryButton("<html><center><b>Final Boss</b><br><small>Big test</small></center></html>");
        finalBossButton.addActionListener(event -> controller.launchFinalBossQuiz(10));

        JButton partyModeButton = Theme.createPrimaryButton("<html><center><b>Party Mode</b><br><small>With friends</small></center></html>");
        partyModeButton.addActionListener(event -> controller.launchPartyQuiz(5));

        activityButtonGrid.add(practiceButton);
        activityButtonGrid.add(timedQuizButton);
        activityButtonGrid.add(finalBossButton);
        activityButtonGrid.add(partyModeButton);

        JButton studyBuddyButton = Theme.createWhiteButton("<html><center><b>StudyBuddy:</b> Every bug you fix makes you better!</center></html>");
        studyBuddyButton.setPreferredSize(new Dimension(0, 46));
        studyBuddyButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        studyBuddyButton.addActionListener(event -> controller.showAlert("StudyBuddy Tip: Keep fields private and use getters/setters!"));

        JPanel studyBuddyPanel = new JPanel(new BorderLayout());
        studyBuddyPanel.setBackground(Theme.PALE_LIME);
        studyBuddyPanel.setBorder(new EmptyBorder(0, 12, 0, 12));
        studyBuddyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        studyBuddyPanel.add(studyBuddyButton, BorderLayout.CENTER);

        JPanel activityArea = new JPanel();
        activityArea.setLayout(new BoxLayout(activityArea, BoxLayout.Y_AXIS));
        activityArea.setBackground(Theme.PALE_LIME);
        activityArea.add(activityButtonGrid);
        activityArea.add(Box.createVerticalStrut(6));
        activityArea.add(studyBuddyPanel);
        activityArea.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                int width = activityArea.getWidth();
                int availableHeight = activityArea.getHeight() - studyBuddyButton.getPreferredSize().height - 6;
                if (width <= 0 || availableHeight <= 0) return;
                int height = Math.min(180, Math.min(
                        (int) Math.round(width * 0.45), availableHeight));
                if (activityButtonGrid.getPreferredSize().height != height) {
                    activityButtonGrid.setPreferredSize(new Dimension(0, height));
                    activityButtonGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
                    activityArea.revalidate();
                }
            }
        });

        add(topBox, BorderLayout.NORTH);
        add(activityArea, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        UserSession.StudentRecord studentRecord = controller.getSession().getCurrentStudent();
        int level = (studentRecord.getXp() / 250) + 1;
        welcomeLabel.setText("Hi, @" + studentRecord.getName() + " (" + studentRecord.getSection() + ")");
        xpLabel.setText("Level " + level + " - " + studentRecord.getXp() + " XP");
        xpBar.setValue(Math.min(2000, studentRecord.getXp()));

        UserSession session = controller.getSession();
        String courseName = session.getDashboardCourse();
        if (courseName == null) {
            continuePanel.setVisible(false);
            revalidate();
            repaint();
            return;
        }

        int completed = session.getCompletedCourseLessons(courseName);
        int total = session.getCourseCatalog().getLessonCount(courseName);

        continuePanel.setVisible(true);
        continuePanel.getParent().revalidate();

        if (completed >= total) {
            continueTitleLabel.setText("Roadmap Complete: " + courseName + " (" + completed + "/" + total + " lessons)");
            continueCourseButton.setText("VIEW ROADMAP");
            continueCourseButton.setEnabled(true);
        } else {
            continueTitleLabel.setText("Continue: " + courseName + " (" + completed + "/" + total + " lessons)");
            continueCourseButton.setText("CONTINUE ROADMAP");
            continueCourseButton.setEnabled(true);
        }
        continueProgress.setMaximum(total);
        continueProgress.setValue(completed);
    }
}
