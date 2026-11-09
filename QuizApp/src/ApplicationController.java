import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ApplicationController extends JFrame {
    private CardLayout rootLayout = new CardLayout();
    private JPanel rootContainer = new JPanel(rootLayout);

    // Separate CardLayouts for Student and Teacher so they cannot cross over
    private CardLayout studentLayout = new CardLayout();
    private JPanel studentContent = new JPanel(studentLayout);

    private CardLayout teacherLayout = new CardLayout();
    private JPanel teacherContent = new JPanel(teacherLayout);

    private DefaultListModel<String> assignmentModel = new DefaultListModel<>();

    public ApplicationController() {
        setTitle("Learn to Code - OOP Project");
        setSize(960, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        assignmentModel.addElement("Lab 1: Classes & Objects (10 Questions)");
        assignmentModel.addElement("Quiz 2: Inheritance & Super (15 Questions)");

        // 1. Welcome / Role Login Screen
        rootContainer.add(createWelcomeScreen(), "WELCOME");

        // 2. Isolated Student Shell
        JPanel studentShell = new JPanel(new BorderLayout());
        studentShell.add(new SidebarNav(this, false), BorderLayout.WEST);
        studentContent.add(new DashboardPanel(this), "STUDENT_DASH");
        studentContent.add(new PracticePanel(this), "PRACTICE");
        studentContent.add(new ClassroomPanel(this, false), "STUDENT_CLASS");
        studentShell.add(studentContent, BorderLayout.CENTER);
        rootContainer.add(studentShell, "STUDENT_APP");

        // 3. Isolated Teacher Shell
        JPanel teacherShell = new JPanel(new BorderLayout());
        teacherShell.add(new SidebarNav(this, true), BorderLayout.WEST);
        teacherContent.add(new TeacherDashboardPanel(this), "TEACHER_DASH");
        teacherContent.add(new ClassroomPanel(this, true), "TEACHER_CLASS");
        teacherShell.add(teacherContent, BorderLayout.CENTER);
        rootContainer.add(teacherShell, "TEACHER_APP");

        add(rootContainer);
    }

    public void start() {
        setVisible(true);
        logout();
    }

    public void logout() {
        rootLayout.show(rootContainer, "WELCOME");
    }

    public void showStudentScreen(String name) {
        rootLayout.show(rootContainer, "STUDENT_APP");
        studentLayout.show(studentContent, name);
    }

    public void showTeacherScreen(String name) {
        rootLayout.show(rootContainer, "TEACHER_APP");
        teacherLayout.show(teacherContent, name);
    }

    public DefaultListModel<String> getAssignmentModel() {
        return assignmentModel;
    }

    private JPanel createWelcomeScreen() {
        JPanel panel = new JPanel();
        panel.setBackground(Theme.BG_YELLOW);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(120, 50, 120, 50));

        JLabel title = new JLabel("Learn to Code.");
        title.setFont(new Font("SansSerif", Font.BOLD, 38));
        title.setForeground(Theme.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Select your role to continue");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sub.setForeground(Theme.TEXT_DARK);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 25));
        btnRow.setBackground(Theme.BG_YELLOW);

        JButton studentBtn = Theme.createRedButton("ENTER AS STUDENT");
        studentBtn.setPreferredSize(new Dimension(210, 50));
        studentBtn.addActionListener(e -> showStudentScreen("STUDENT_DASH"));

        JButton teacherBtn = Theme.createWhiteButton("ENTER AS TEACHER");
        teacherBtn.setPreferredSize(new Dimension(210, 50));
        teacherBtn.addActionListener(e -> verifyTeacherLogin());

        btnRow.add(studentBtn);
        btnRow.add(teacherBtn);
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(sub);
        panel.add(btnRow);
        return panel;
    }

    private void verifyTeacherLogin() {
        try {
            String pin = JOptionPane.showInputDialog(this, "Enter Instructor PIN (Hint: 1234):", "Teacher Verification", JOptionPane.QUESTION_MESSAGE);
            if (pin == null) return; // User clicked Cancel
            if (!pin.trim().equals("1234")) {
                throw new InvalidMenuChoiceException("Access Denied: Invalid Instructor PIN.");
            }
            showTeacherScreen("TEACHER_DASH");
        } catch (InvalidMenuChoiceException ex) {
            showError(ex.getMessage());
        }
    }

    public void showAlert(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Learn to Code", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
