package App.controller;

import App.model.CourseCatalog;
import App.model.QuizQuestionBank;
import App.model.UserSession;
import App.util.InvalidMenuChoiceException;
import App.ui.ClassroomPanel;
import App.ui.AlgorithmBenchmarkPanel;
import App.ui.CoursePanel;
import App.ui.DashboardPanel;
import App.ui.PracticePanel;
import App.ui.QuizPlayPanel;
import App.ui.SidebarNav;
import App.ui.TeacherDashboardPanel;
import App.ui.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ApplicationController extends JFrame {
    private CardLayout rootScreenLayout = new CardLayout();
    private JPanel rootScreenContainer = new JPanel(rootScreenLayout);

    private CardLayout studentScreenLayout = new CardLayout();
    private JPanel studentScreenContainer = new ResponsiveContentPanel(studentScreenLayout);

    private CardLayout teacherScreenLayout = new CardLayout();
    private JPanel teacherScreenContainer = new ResponsiveContentPanel(teacherScreenLayout);

    private UserSession userSession = new UserSession();
    private DashboardPanel studentDashboardPanel;
    private CoursePanel studentCoursePanel;
    private ClassroomPanel studentClassroomPanel;
    private QuizPlayPanel quizPlayPanel;
    private TeacherDashboardPanel teacherDashboardPanel;
    private ClassroomPanel teacherClassroomPanel;
    private AlgorithmBenchmarkPanel teacherBenchmarkPanel;

    private static class ResponsiveContentPanel extends JPanel implements Scrollable {
        private ResponsiveContentPanel(LayoutManager layout) {
            super(layout);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return orientation == SwingConstants.VERTICAL ? visibleRect.height : visibleRect.width;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    public ApplicationController() {
        setTitle("Learn to Code");

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width >= 1200 ? 900 : screen.width >= 800 ? 760 : Math.min(430, screen.width);
        int height = screen.height >= 900 ? 780 : screen.height >= 700 ? 720 : Math.min(780, screen.height);
        setSize(width, height);
        setMinimumSize(new Dimension(360, 600));
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        rootScreenContainer.add(createWelcomeScreen(), "WELCOME");

        studentDashboardPanel = new DashboardPanel(this);
        studentCoursePanel = new CoursePanel(this);
        studentClassroomPanel = new ClassroomPanel(this, false);
        quizPlayPanel = new QuizPlayPanel(this);

        studentScreenContainer.add(studentDashboardPanel, "STUDENT_DASH");
        studentScreenContainer.add(new PracticePanel(this), "PRACTICE");
        studentScreenContainer.add(studentCoursePanel, "COURSES");
        studentScreenContainer.add(studentClassroomPanel, "STUDENT_CLASS");
        studentScreenContainer.add(quizPlayPanel, "QUIZ_PLAY");
        rootScreenContainer.add(
                buildMobileShell(new SidebarNav(this, false), studentScreenContainer), "STUDENT_APP");

        teacherDashboardPanel = new TeacherDashboardPanel(this);
        teacherClassroomPanel = new ClassroomPanel(this, true);
        teacherBenchmarkPanel = new AlgorithmBenchmarkPanel(this);
        teacherScreenContainer.add(teacherDashboardPanel, "TEACHER_DASH");
        teacherScreenContainer.add(teacherClassroomPanel, "TEACHER_CLASS");
        teacherScreenContainer.add(teacherBenchmarkPanel, "TEACHER_BENCHMARK");
        rootScreenContainer.add(
                buildMobileShell(new SidebarNav(this, true), teacherScreenContainer), "TEACHER_APP");

        add(rootScreenContainer);
        Theme.applyTextStyling(rootScreenContainer);
    }

    private JPanel buildMobileShell(SidebarNav sidebar, JPanel contentArea) {
        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(Theme.PALE_LIME);

        JPanel topBar = new JPanel(new BorderLayout());    
        topBar.setBackground(Theme.DEEP_TEAL);
        topBar.setBorder(new EmptyBorder(8, 12, 8, 14));

        JButton menuToggleButton = Theme.createWhiteButton("\u22EE");
        menuToggleButton.setFont(new Font(Theme.UI_FONT, Font.BOLD, 20));
        menuToggleButton.setPreferredSize(new Dimension(38, 32));
        menuToggleButton.setToolTipText("Toggle navigation menu");

        JLabel appTitle = new JLabel("Learn to Code", SwingConstants.RIGHT);
        appTitle.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
        appTitle.setForeground(Theme.BRIGHT_LIME);

        JPanel logoBadge = Theme.createRoundedCard(Theme.WHITE, 2);
        logoBadge.setLayout(new BorderLayout());
        logoBadge.setPreferredSize(new Dimension(34, 32));
        JLabel logoMark = new JLabel("", SwingConstants.CENTER);
        ImageIcon logoIcon = Theme.loadAssetIcon("assets/logo/logo.png", 28, 28);
        if (logoIcon != null) {
            logoMark.setIcon(logoIcon);
        } else {
            logoMark.setText("LC");
        }
        logoMark.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
        logoMark.setForeground(Theme.DEEP_TEAL);
        logoBadge.add(logoMark, BorderLayout.CENTER);

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        brand.setOpaque(false);
        brand.add(logoBadge);
        brand.add(appTitle);

        Runnable updateBrand = () -> {
            appTitle.setVisible(sidebar.isVisible());
            brand.revalidate();
            brand.repaint();
        };
        sidebar.setVisible(getWidth() >= 700);
        updateBrand.run();

        menuToggleButton.addActionListener(event -> {
            sidebar.setVisible(!sidebar.isVisible());
            updateBrand.run();
            shell.revalidate();
            shell.repaint();
        });

        topBar.add(menuToggleButton, BorderLayout.WEST);
        topBar.add(brand, BorderLayout.EAST);

        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setBorder(null);
        contentScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        shell.add(topBar, BorderLayout.NORTH);
        shell.add(sidebar, BorderLayout.WEST);
        shell.add(contentScrollPane, BorderLayout.CENTER);

        shell.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                boolean wideLayout = shell.getWidth() >= 700;
                sidebar.setVisible(wideLayout);
                updateBrand.run();
                shell.revalidate();
            }
        });
        return shell;
    }

    public void start() {
        setVisible(true);
        userSession.logout();
        rootScreenLayout.show(rootScreenContainer, "WELCOME");
    }

    public void logout() {
        int confirmationResult = JOptionPane.showConfirmDialog(
                this,
                "Sign out of the current session?",
                "Sign Out",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (confirmationResult != JOptionPane.YES_OPTION) {
            return;
        }
        userSession.logout();
        rootScreenLayout.show(rootScreenContainer, "WELCOME");
    }

    public UserSession getSession() {
        return userSession;
    }

    public void resetAllDemoData() {
        userSession.resetAllDemoData();
        teacherClassroomPanel.clearDraftQuiz();
    }

    public void launchQuizSession(int questionCount) {
        if (!requireStudentSession()) return;
        quizPlayPanel.startStandardQuiz(questionCount);
        showStudentScreen("QUIZ_PLAY");
    }

    public void launchQuickPractice(String topic, int questionCount) {
        if (!requireStudentSession()) return;
        int availableQuestions = QuizQuestionBank.getQuickPracticeQuestionCount(topic);
        if (questionCount < 1 || questionCount > availableQuestions) {
            showError("Choose between 1 and " + availableQuestions + " questions.");
            return;
        }
        quizPlayPanel.startQuickPractice(topic, questionCount);
        showStudentScreen("QUIZ_PLAY");
    }

    public void launchTimedQuiz(int questionCount) {
        if (!requireStudentSession()) return;
        quizPlayPanel.startTimedQuiz(questionCount);
        showStudentScreen("QUIZ_PLAY");
    }

    public void launchFinalBossQuiz(int questionCount) {
        if (!requireStudentSession()) return;
        quizPlayPanel.startFinalBossQuiz(questionCount);
        showStudentScreen("QUIZ_PLAY");
    }

    public void launchPartyQuiz(int questionCount) {
        if (!requireStudentSession()) return;
        quizPlayPanel.startPartyQuiz(questionCount);
        showStudentScreen("QUIZ_PLAY");
    }

    public void launchTeacherQuiz(UserSession.TeacherQuiz quiz) {
        if (!requireStudentSession()) return;
        quizPlayPanel.startTeacherQuiz(quiz);
        showStudentScreen("QUIZ_PLAY");
    }

    public void showStudentScreen(String screenName) {
        if (!requireStudentSession()) return;
        studentDashboardPanel.refresh();
        studentCoursePanel.refresh();
        studentClassroomPanel.refresh();
        rootScreenLayout.show(rootScreenContainer, "STUDENT_APP");
        studentScreenLayout.show(studentScreenContainer, screenName);
    }

    public void showTeacherScreen(String screenName) {
        teacherDashboardPanel.refresh();
        teacherClassroomPanel.refresh();
        if ("TEACHER_BENCHMARK".equals(screenName)) {
            teacherBenchmarkPanel.refresh();
        }
        rootScreenLayout.show(rootScreenContainer, "TEACHER_APP");
        teacherScreenLayout.show(teacherScreenContainer, screenName);
    }

    private JPanel createWelcomeScreen() {
        JPanel panel = new JPanel();
        panel.setBackground(Theme.PALE_LIME);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(75, 35, 75, 35));

        JPanel logoBox = Theme.createRoundedCard(Theme.DEEP_TEAL, 0);
        logoBox.setPreferredSize(new Dimension(75, 75));
        logoBox.setMaximumSize(new Dimension(75, 75));
        logoBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Learn to Code.");
        title.setFont(new Font(Theme.UI_FONT, Font.BOLD, 34));
        title.setForeground(Theme.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Your coding journey starts here");
        subtitle.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 14));
        subtitle.setForeground(Theme.TEXT_DARK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel signInForm = new JPanel(new GridLayout(4, 1, 0, 6));
        signInForm.setBackground(Theme.PALE_LIME);
        signInForm.setMaximumSize(new Dimension(280, 130));

        JLabel displayNameLabel = new JLabel("Display Name (Guest):");
        displayNameLabel.setForeground(Theme.TEXT_DARK);
        displayNameLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));

        JLabel sectionCodeLabel = new JLabel("Class Section Code:");
        sectionCodeLabel.setForeground(Theme.TEXT_DARK);
        sectionCodeLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));

        JTextField displayNameField = new JTextField("Angel");
        JTextField sectionCodeField = new JTextField("OOP101");
        Theme.styleTextField(displayNameField);
        Theme.styleTextField(sectionCodeField);
        displayNameField.setToolTipText("Guest names are not verified accounts in this local prototype.");

        signInForm.add(displayNameLabel);
        signInForm.add(displayNameField);
        signInForm.add(sectionCodeLabel);
        signInForm.add(sectionCodeField);

        JPanel signInButtons = new JPanel(new GridLayout(2, 1, 0, 12));
        signInButtons.setBackground(Theme.PALE_LIME);
        signInButtons.setMaximumSize(new Dimension(280, 105));

        JButton studentSignInButton = Theme.createPrimaryButton("SIGN IN AS STUDENT");
        studentSignInButton.addActionListener(
                event -> handleLogin(displayNameField.getText(), sectionCodeField.getText(), false));

        JButton teacherSignInButton = Theme.createWhiteButton("SIGN IN AS TEACHER");
        teacherSignInButton.setEnabled(true);
        teacherSignInButton.setToolTipText("Presentation teacher access.");
        teacherSignInButton.addActionListener(
                event -> handleLogin(displayNameField.getText(), sectionCodeField.getText(), true));

        JButton resetDemoButton = Theme.createWhiteButton("Reset Demo Data");
        resetDemoButton.setFont(new Font(Theme.UI_FONT, Font.BOLD, 10));
        resetDemoButton.setPreferredSize(new Dimension(120, 28));
        resetDemoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetDemoButton.setToolTipText("Clear all students, activities, questions, and course progress");
        resetDemoButton.addActionListener(event -> confirmResetDemoData());

        signInButtons.add(studentSignInButton);
        signInButtons.add(teacherSignInButton);

        panel.add(logoBox);
        panel.add(Box.createVerticalStrut(18));
        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(22));
        panel.add(signInForm);
        panel.add(Box.createVerticalStrut(22));
        panel.add(signInButtons);
        panel.add(Box.createVerticalStrut(10));
        panel.add(resetDemoButton);
        return panel;
    }

    private void confirmResetDemoData() {
        int confirmationResult = JOptionPane.showConfirmDialog(
                this,
                "Clear all students, activities and questions, and saved course progress?\nThis cannot be undone.",
                "Reset All Demo Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirmationResult != JOptionPane.YES_OPTION) return;

        try {
            resetAllDemoData();
            showAlert("All demo data has been cleared.");
        } catch (IllegalStateException stateException) {
            showError(stateException.getMessage());
        }
    }

    private void handleLogin(String rawName, String rawSectionCode, boolean asTeacher) {
        try {
            String userName = rawName.trim();
            String sectionCode = rawSectionCode.trim().toUpperCase();
            if (asTeacher) {
                if (userName.isEmpty() || sectionCode.isEmpty()) {
                    throw new InvalidMenuChoiceException("Please enter both your Name and Class Section Code.");
                }
                userSession.loginDemoTeacher(userName, sectionCode);
                showTeacherScreen("TEACHER_DASH");
                return;
            }
            if (userName.isEmpty() || sectionCode.isEmpty()) {
                throw new InvalidMenuChoiceException("Please enter both your Name and Class Section Code.");
            }
            userSession.loginStudent(userName, sectionCode);
            showStudentScreen("STUDENT_DASH");
            showStudentTutorialIfNeeded();
        } catch (InvalidMenuChoiceException invalidChoiceException) {
            showError(invalidChoiceException.getMessage());
        }
    }

    private void showStudentTutorialIfNeeded() {
        if (userSession.hasSeenStudentTutorial()) return;

        SwingUtilities.invokeLater(() -> {
            JDialog studentTutorialDialog = new JDialog(this, "Student Quick Tour", true);
            studentTutorialDialog.setResizable(false);
            studentTutorialDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            String[] tutorialPageTitles = {
                    "Welcome to Learn to Code", "Choose an activity", "Earn XP and track progress"};
            String[] tutorialPageDescriptions = {
                "<html><div style='width:300px;'>Hi, "
                        + userSession.getCurrentUserName()
                        + "! This quick tour will show you where to start.</div></html>",
                "<html><div style='width:300px;'>Use <b>Practice</b>, <b>Timed Quiz</b>, "
                        + "<b>Final Boss</b>, or <b>Party Mode</b> from your dashboard.</div></html>",
                "<html><div style='width:300px;'>Complete lessons and quizzes to earn XP. "
                        + "Your progress and continue card update automatically.</div></html>"
            };

            JPanel tutorialPageCards = new JPanel(new CardLayout());
            tutorialPageCards.setBackground(Theme.WHITE);
            JLabel[] pageIndicatorLabels = new JLabel[tutorialPageTitles.length];

            for (int pageIndex = 0; pageIndex < tutorialPageTitles.length; pageIndex++) {
                JPanel tutorialPage = new JPanel();
                tutorialPage.setLayout(new BoxLayout(tutorialPage, BoxLayout.Y_AXIS));
                tutorialPage.setBackground(Theme.WHITE);
                tutorialPage.setBorder(new EmptyBorder(20, 24, 16, 24));

                JLabel pageNumberLabel = new JLabel(
                        String.valueOf(pageIndex + 1), SwingConstants.CENTER);
                pageNumberLabel.setOpaque(true);
                pageNumberLabel.setBackground(Theme.BRIGHT_LIME);
                pageNumberLabel.setForeground(Theme.TEXT_DARK);
                pageNumberLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 22));
                pageNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                pageNumberLabel.setPreferredSize(new Dimension(54, 54));
                pageNumberLabel.setMaximumSize(new Dimension(54, 54));

                JLabel pageTitleLabel = new JLabel(
                        tutorialPageTitles[pageIndex], SwingConstants.CENTER);
                pageTitleLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 18));
                pageTitleLabel.setForeground(Theme.TEXT_DARK);
                pageTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel pageDescriptionLabel = new JLabel(
                        tutorialPageDescriptions[pageIndex], SwingConstants.CENTER);
                pageDescriptionLabel.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 13));
                pageDescriptionLabel.setForeground(Theme.TEXT_DARK);
                pageDescriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                tutorialPage.add(pageNumberLabel);
                tutorialPage.add(Box.createVerticalStrut(14));
                tutorialPage.add(pageTitleLabel);
                tutorialPage.add(Box.createVerticalStrut(8));
                tutorialPage.add(pageDescriptionLabel);
                tutorialPageCards.add(tutorialPage, "STEP_" + pageIndex);
            }

            JPanel footer = new JPanel(new BorderLayout(8, 8));
            footer.setBackground(Theme.WHITE);
            footer.setBorder(new EmptyBorder(8, 20, 16, 20));

            JPanel pageIndicators = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            pageIndicators.setBackground(Theme.WHITE);
            for (int pageIndex = 0; pageIndex < pageIndicatorLabels.length; pageIndex++) {
                JLabel pageIndicator = new JLabel("\u2022");
                pageIndicator.setFont(new Font(Theme.UI_FONT, Font.BOLD, 18));
                pageIndicator.setForeground(
                        pageIndex == 0 ? Theme.DEEP_TEAL : new Color(150, 170, 150));
                pageIndicatorLabels[pageIndex] = pageIndicator;
                pageIndicators.add(pageIndicator);
            }

            JButton backButton = Theme.createWhiteButton("Back");
            backButton.setPreferredSize(new Dimension(82, 34));
            JButton nextButton = Theme.createPrimaryButton("Next");
            nextButton.setPreferredSize(new Dimension(82, 34));
            JButton skipButton = Theme.createWhiteButton("Skip");
            skipButton.setPreferredSize(new Dimension(82, 34));

            JPanel actionRow = new JPanel(new GridLayout(1, 3, 8, 0));
            actionRow.setBackground(Theme.WHITE);
            actionRow.add(backButton);
            actionRow.add(skipButton);
            actionRow.add(nextButton);
            footer.add(pageIndicators, BorderLayout.NORTH);
            footer.add(actionRow, BorderLayout.CENTER);

            final int[] currentPageIndex = {0};
            Runnable updateTutorialPage = () -> {
                ((CardLayout) tutorialPageCards.getLayout())
                        .show(tutorialPageCards, "STEP_" + currentPageIndex[0]);
                backButton.setEnabled(currentPageIndex[0] > 0);
                nextButton.setText(currentPageIndex[0] == tutorialPageTitles.length - 1
                        ? "Got It" : "Next");
                for (int pageIndex = 0; pageIndex < pageIndicatorLabels.length; pageIndex++) {
                    pageIndicatorLabels[pageIndex].setForeground(
                            pageIndex == currentPageIndex[0]
                                    ? Theme.DEEP_TEAL : new Color(150, 170, 150));
                }
            };

            backButton.addActionListener(actionEvent -> {
                if (currentPageIndex[0] > 0) {
                    currentPageIndex[0]--;
                    updateTutorialPage.run();
                }
            });
            nextButton.addActionListener(actionEvent -> {
                if (currentPageIndex[0] < tutorialPageTitles.length - 1) {
                    currentPageIndex[0]++;
                    updateTutorialPage.run();
                } else {
                    studentTutorialDialog.dispose();
                }
            });
            skipButton.addActionListener(actionEvent -> studentTutorialDialog.dispose());
            studentTutorialDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent event) {
                    userSession.markStudentTutorialSeen();
                }
            });

            JPanel tutorialContent = new JPanel(new BorderLayout());
            tutorialContent.setBackground(Theme.WHITE);
            tutorialContent.add(tutorialPageCards, BorderLayout.CENTER);
            tutorialContent.add(footer, BorderLayout.SOUTH);
            studentTutorialDialog.setContentPane(tutorialContent);
            studentTutorialDialog.pack();
            studentTutorialDialog.setSize(390, 330);
            studentTutorialDialog.setLocationRelativeTo(this);
            updateTutorialPage.run();
            studentTutorialDialog.setVisible(true);
        });
    }

    private boolean requireStudentSession() {
        if (!userSession.isStudentSession()) {
            showError("Log in as a student before opening student features.");
            return false;
        }
        return true;
    }

    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message, "Learn to Code", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
