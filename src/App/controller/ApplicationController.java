package App.controller;

import App.model.CourseCatalog;
import App.model.QuizQuestionBank;
import App.model.UserSession;
import App.util.InvalidMenuChoiceException;
import App.ui.ClassroomPanel;
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
    private CardLayout rootLayout = new CardLayout();
    private JPanel rootContainer = new JPanel(rootLayout);

    private CardLayout studentLayout = new CardLayout();
    private JPanel studentContent = new ResponsiveContentPanel(studentLayout);

    private CardLayout teacherLayout = new CardLayout();
    private JPanel teacherContent = new ResponsiveContentPanel(teacherLayout);

    private UserSession session = new UserSession();
    private DashboardPanel studentDash;
    private CoursePanel studentCourses;
    private ClassroomPanel studentClass;
    private QuizPlayPanel quizPlayPanel;
    private TeacherDashboardPanel teacherDash;
    private ClassroomPanel teacherClass;

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

        rootContainer.add(createWelcomeScreen(), "WELCOME");

        studentDash = new DashboardPanel(this);
        studentCourses = new CoursePanel(this);
        studentClass = new ClassroomPanel(this, false);
        quizPlayPanel = new QuizPlayPanel(this);

        studentContent.add(studentDash, "STUDENT_DASH");
        studentContent.add(new PracticePanel(this), "PRACTICE");
        studentContent.add(studentCourses, "COURSES");
        studentContent.add(studentClass, "STUDENT_CLASS");
        studentContent.add(quizPlayPanel, "QUIZ_PLAY");
        rootContainer.add(buildMobileShell(new SidebarNav(this, false), studentContent), "STUDENT_APP");

        teacherDash = new TeacherDashboardPanel(this);
        teacherClass = new ClassroomPanel(this, true);
        teacherContent.add(teacherDash, "TEACHER_DASH");
        teacherContent.add(teacherClass, "TEACHER_CLASS");
        rootContainer.add(buildMobileShell(new SidebarNav(this, true), teacherContent), "TEACHER_APP");

        add(rootContainer);
    }

    private JPanel buildMobileShell(SidebarNav sidebar, JPanel contentArea) {
        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(Theme.PALE_LIME);

        JPanel topBar = new JPanel(new BorderLayout());    
        topBar.setBackground(Theme.DEEP_TEAL);
        topBar.setBorder(new EmptyBorder(8, 12, 8, 14));

        JButton toggleBtn = Theme.createWhiteButton("\u22EE");
        toggleBtn.setFont(new Font(Theme.UI_FONT, Font.BOLD, 20));
        toggleBtn.setPreferredSize(new Dimension(38, 32));
        toggleBtn.setToolTipText("Toggle navigation menu");

        JLabel appTitle = new JLabel("Learn to Code", SwingConstants.RIGHT);
        appTitle.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
        appTitle.setForeground(Theme.BRIGHT_LIME);

        JPanel logoBadge = Theme.createRoundedCard(Theme.BRIGHT_LIME, 2);
        logoBadge.setLayout(new BorderLayout());
        logoBadge.setPreferredSize(new Dimension(34, 32));
        JLabel logoMark = new JLabel("LC", SwingConstants.CENTER);
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

        toggleBtn.addActionListener(e -> {
            sidebar.setVisible(!sidebar.isVisible());
            updateBrand.run();
            shell.revalidate();
            shell.repaint();
        });

        topBar.add(toggleBtn, BorderLayout.WEST);
        topBar.add(brand, BorderLayout.EAST);

        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setBorder(null);
        contentScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentScroll.getVerticalScrollBar().setUnitIncrement(16);

        shell.add(topBar, BorderLayout.NORTH);
        shell.add(sidebar, BorderLayout.WEST);
        shell.add(contentScroll, BorderLayout.CENTER);

        shell.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                boolean desktopWide = shell.getWidth() >= 700;
                sidebar.setVisible(desktopWide);
                updateBrand.run();
                shell.revalidate();
            }
        });
        return shell;
    }

    public void start() {
        setVisible(true);
        session.logout();
        rootLayout.show(rootContainer, "WELCOME");
    }

    public void logout() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Sign out of the current session?",
                "Sign Out",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }
        session.logout();
        rootLayout.show(rootContainer, "WELCOME");
    }

    public UserSession getSession() {
        return session;
    }

    public void resetAllDemoData() {
        session.resetAllDemoData();
        teacherClass.clearDraftQuiz();
    }

    public void launchQuizSession(int questionCount) {
        if (!requireStudentSession()) return;
        quizPlayPanel.startQuiz(questionCount);
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

    public void showStudentScreen(String name) {
        if (!requireStudentSession()) return;
        studentDash.refresh();
        studentCourses.refresh();
        studentClass.refresh();
        rootLayout.show(rootContainer, "STUDENT_APP");
        studentLayout.show(studentContent, name);
    }

    public void showTeacherScreen(String name) {
        teacherDash.refresh();
        teacherClass.refresh();
        rootLayout.show(rootContainer, "TEACHER_APP");
        teacherLayout.show(teacherContent, name);
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

        JPanel form = new JPanel(new GridLayout(4, 1, 0, 6));
        form.setBackground(Theme.PALE_LIME);
        form.setMaximumSize(new Dimension(280, 130));

        JLabel nameLbl = new JLabel("Display Name (Guest):");
        nameLbl.setForeground(Theme.TEXT_DARK);
        nameLbl.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));

        JLabel secLbl = new JLabel("Class Section Code:");
        secLbl.setForeground(Theme.TEXT_DARK);
        secLbl.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));

        JTextField nameField = new JTextField("Angel");
        JTextField sectionField = new JTextField("OOP101");
        Theme.styleTextField(nameField);
        Theme.styleTextField(sectionField);
        nameField.setToolTipText("Guest names are not verified accounts in this local prototype.");

        form.add(nameLbl);
        form.add(nameField);
        form.add(secLbl);
        form.add(sectionField);

        JPanel btnRow = new JPanel(new GridLayout(2, 1, 0, 12));
        btnRow.setBackground(Theme.PALE_LIME);
        btnRow.setMaximumSize(new Dimension(280, 105));

        JButton studentBtn = Theme.createPrimaryButton("SIGN IN AS STUDENT");
        studentBtn.addActionListener(e -> handleLogin(nameField.getText(), sectionField.getText(), false));

        JButton teacherBtn = Theme.createWhiteButton("SIGN IN AS TEACHER");
        teacherBtn.setEnabled(true);
        teacherBtn.setToolTipText("Presentation teacher access.");
        teacherBtn.addActionListener(e -> handleLogin(nameField.getText(), sectionField.getText(), true));

        JButton resetDemoButton = Theme.createWhiteButton("Reset Demo Data");
        resetDemoButton.setFont(new Font(Theme.UI_FONT, Font.BOLD, 10));
        resetDemoButton.setPreferredSize(new Dimension(120, 28));
        resetDemoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetDemoButton.setToolTipText("Clear all students, activities, questions, and course progress");
        resetDemoButton.addActionListener(e -> confirmResetDemoData());

        btnRow.add(studentBtn);
        btnRow.add(teacherBtn);

        panel.add(logoBox);
        panel.add(Box.createVerticalStrut(18));
        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(22));
        panel.add(form);
        panel.add(Box.createVerticalStrut(22));
        panel.add(btnRow);
        panel.add(Box.createVerticalStrut(10));
        panel.add(resetDemoButton);
        return panel;
    }

    private void confirmResetDemoData() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Clear all students, activities and questions, and saved course progress?\nThis cannot be undone.",
                "Reset All Demo Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (result != JOptionPane.YES_OPTION) return;

        try {
            resetAllDemoData();
            showAlert("All demo data has been cleared.");
        } catch (IllegalStateException ex) {
            showError(ex.getMessage());
        }
    }

    private void handleLogin(String rawName, String rawSection, boolean asTeacher) {
        try {
            String name = rawName.trim();
            String section = rawSection.trim().toUpperCase();
            if (asTeacher) {
                if (name.isEmpty() || section.isEmpty()) {
                    throw new InvalidMenuChoiceException("Please enter both your Name and Class Section Code.");
                }
                session.loginDemoTeacher(name, section);
                showTeacherScreen("TEACHER_DASH");
                return;
            }
            if (name.isEmpty() || section.isEmpty()) {
                throw new InvalidMenuChoiceException("Please enter both your Name and Class Section Code.");
            }
            session.loginStudent(name, section);
            showStudentScreen("STUDENT_DASH");
            showStudentTutorialIfNeeded();
        } catch (InvalidMenuChoiceException ex) {
            showError(ex.getMessage());
        }
    }

    private void showStudentTutorialIfNeeded() {
        if (session.hasSeenStudentTutorial()) return;

        SwingUtilities.invokeLater(() -> {
            JDialog tutorial = new JDialog(this, "Student Quick Tour", true);
            tutorial.setResizable(false);
            tutorial.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            String[] titles = {"Welcome to Learn to Code", "Choose an activity", "Earn XP and track progress"};
            String[] descriptions = {
                "<html><div style='width:300px;'>Hi, "
                        + session.getCurrentUserName()
                        + "! This quick tour will show you where to start.</div></html>",
                "<html><div style='width:300px;'>Use <b>Practice</b>, <b>Timed Quiz</b>, "
                        + "<b>Final Boss</b>, or <b>Party Mode</b> from your dashboard.</div></html>",
                "<html><div style='width:300px;'>Complete lessons and quizzes to earn XP. "
                        + "Your progress and continue card update automatically.</div></html>"
            };

            JPanel card = new JPanel(new CardLayout());
            card.setBackground(Theme.WHITE);
            final JLabel[] pageLabels = new JLabel[3];
            final JLabel[] dotLabels = new JLabel[3];

            for (int i = 0; i < titles.length; i++) {
                JPanel page = new JPanel();
                page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
                page.setBackground(Theme.WHITE);
                page.setBorder(new EmptyBorder(20, 24, 16, 24));

                JLabel icon = new JLabel(String.valueOf(i + 1), SwingConstants.CENTER);
                icon.setOpaque(true);
                icon.setBackground(Theme.BRIGHT_LIME);
                icon.setForeground(Theme.TEXT_DARK);
                icon.setFont(new Font(Theme.UI_FONT, Font.BOLD, 22));
                icon.setAlignmentX(Component.CENTER_ALIGNMENT);
                icon.setPreferredSize(new Dimension(54, 54));
                icon.setMaximumSize(new Dimension(54, 54));

                JLabel title = new JLabel(titles[i], SwingConstants.CENTER);
                title.setFont(new Font(Theme.UI_FONT, Font.BOLD, 18));
                title.setForeground(Theme.TEXT_DARK);
                title.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel description = new JLabel(descriptions[i], SwingConstants.CENTER);
                description.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 13));
                description.setForeground(Theme.TEXT_DARK);
                description.setAlignmentX(Component.CENTER_ALIGNMENT);

                page.add(icon);
                page.add(Box.createVerticalStrut(14));
                page.add(title);
                page.add(Box.createVerticalStrut(8));
                page.add(description);
                card.add(page, "STEP_" + i);
                pageLabels[i] = title;
            }

            JPanel footer = new JPanel(new BorderLayout(8, 8));
            footer.setBackground(Theme.WHITE);
            footer.setBorder(new EmptyBorder(8, 20, 16, 20));

            JPanel dots = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            dots.setBackground(Theme.WHITE);
            for (int i = 0; i < dotLabels.length; i++) {
                JLabel dot = new JLabel("\u2022");
                dot.setFont(new Font(Theme.UI_FONT, Font.BOLD, 18));
                dot.setForeground(i == 0 ? Theme.DEEP_TEAL : new Color(150, 170, 150));
                dotLabels[i] = dot;
                dots.add(dot);
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
            footer.add(dots, BorderLayout.NORTH);
            footer.add(actionRow, BorderLayout.CENTER);

            final int[] step = {0};
            Runnable updateStep = () -> {
                ((CardLayout) card.getLayout()).show(card, "STEP_" + step[0]);
                backButton.setEnabled(step[0] > 0);
                nextButton.setText(step[0] == titles.length - 1 ? "Got It" : "Next");
                for (int i = 0; i < dotLabels.length; i++) {
                    dotLabels[i].setForeground(i == step[0] ? Theme.DEEP_TEAL : new Color(150, 170, 150));
                }
            };

            backButton.addActionListener(e -> {
                if (step[0] > 0) {
                    step[0]--;
                    updateStep.run();
                }
            });
            nextButton.addActionListener(e -> {
                if (step[0] < titles.length - 1) {
                    step[0]++;
                    updateStep.run();
                } else {
                    tutorial.dispose();
                }
            });
            skipButton.addActionListener(e -> tutorial.dispose());
            tutorial.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent event) {
                    session.markStudentTutorialSeen();
                }
            });

            JPanel content = new JPanel(new BorderLayout());
            content.setBackground(Theme.WHITE);
            content.add(card, BorderLayout.CENTER);
            content.add(footer, BorderLayout.SOUTH);
            tutorial.setContentPane(content);
            tutorial.pack();
            tutorial.setSize(390, 330);
            tutorial.setLocationRelativeTo(this);
            updateStep.run();
            tutorial.setVisible(true);
        });
    }

    private boolean requireStudentSession() {
        if (!session.isStudentSession()) {
            showError("Log in as a student before opening student features.");
            return false;
        }
        return true;
    }

    public void showAlert(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Learn to Code", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
