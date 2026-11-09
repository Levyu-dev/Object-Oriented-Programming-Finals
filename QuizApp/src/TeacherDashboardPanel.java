import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class TeacherDashboardPanel extends JPanel {

    public TeacherDashboardPanel(ApplicationController controller) {
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.WHITE);
        setBorder(new EmptyBorder(20, 28, 20, 28));

        // Top Header + Class Metrics
        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setBackground(Theme.WHITE);

        JLabel welcome = new JLabel("Instructor Dashboard — Section OOP101");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 26));
        welcome.setForeground(Theme.TEXT_DARK);

        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 15, 0));
        statsGrid.setBackground(Theme.WHITE);
        statsGrid.setBorder(new EmptyBorder(12, 0, 12, 0));
        statsGrid.add(createStatBox("24 Students", "Enrolled in OOP101"));
        statsGrid.add(createStatBox("88.5%", "Class Quiz Average"));
        statsGrid.add(createStatBox("3 Active", "Assigned Activities"));

        topBox.add(welcome);
        topBox.add(statsGrid);

        // Center: Student Progress & Gradebook Overview
        JPanel gradebookPanel = new JPanel(new BorderLayout(8, 8));
        gradebookPanel.setBackground(Theme.BG_YELLOW);
        gradebookPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel tableTitle = new JLabel("Student Progress & Gradebook Overview");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        tableTitle.setForeground(Theme.TEXT_DARK);

        String[] columns = {"Student Username", "Level & XP", "Quizzes Done", "Average Score"};
        Object[][] data = {
            {"@alex_dev", "Level 6 (1,450 XP)", "3 / 3", "96%"},
            {"@user", "Level 5 (1,200 XP)", "2 / 3", "92%"},
            {"@jumana_code", "Level 5 (1,120 XP)", "2 / 3", "89%"},
            {"@iyce_java", "Level 4 (980 XP)", "1 / 3", "84%"},
            {"@angel_ui", "Level 4 (950 XP)", "1 / 3", "81%"}
        };
        JTable studentTable = new JTable(data, columns);
        studentTable.setRowHeight(28);
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        studentTable.setEnabled(false);

        gradebookPanel.add(tableTitle, BorderLayout.NORTH);
        gradebookPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // Bottom: Quick Action to Post Assignments
        JButton manageAssignBtn = Theme.createRedButton("Manage & Post Class Assignments →");
        manageAssignBtn.setPreferredSize(new Dimension(0, 46));
        manageAssignBtn.addActionListener(e -> controller.showTeacherScreen("TEACHER_CLASS"));

        add(topBox, BorderLayout.NORTH);
        add(gradebookPanel, BorderLayout.CENTER);
        add(manageAssignBtn, BorderLayout.SOUTH);
    }

    private JPanel createStatBox(String big, String small) {
        JPanel box = new JPanel();
        box.setBackground(Theme.CARD_RED);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel l1 = new JLabel(big);
        l1.setFont(new Font("SansSerif", Font.BOLD, 20));
        l1.setForeground(Theme.WHITE);
        l1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel l2 = new JLabel(small);
        l2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l2.setForeground(Theme.WHITE);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(l1);
        box.add(l2);
        return box;
    }
}
