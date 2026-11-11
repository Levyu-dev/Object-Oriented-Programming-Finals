import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SidebarNav extends JPanel {

    public SidebarNav(ApplicationController controller, boolean isTeacher) {
        setBackground(Theme.BG_YELLOW);
        setPreferredSize(new Dimension(62, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 8, 20, 8));

        if (isTeacher) {
            JButton dashBtn = Theme.createCircleButton("Dash", "Teacher Dashboard");
            dashBtn.addActionListener(e -> controller.showTeacherScreen("TEACHER_DASH"));

            JButton assignBtn = Theme.createCircleButton("Assign", "Manage Assignments");
            assignBtn.addActionListener(e -> controller.showTeacherScreen("TEACHER_CLASS"));

            add(dashBtn);
            add(Box.createVerticalStrut(16));
            add(assignBtn);
        } else {
            JButton homeBtn = Theme.createCircleButton("Home", "Student Dashboard");
            homeBtn.addActionListener(e -> controller.showStudentScreen("STUDENT_DASH"));

            JButton practiceBtn = Theme.createCircleButton("Prac", "Practice");
            practiceBtn.addActionListener(e -> controller.showStudentScreen("PRACTICE"));

            JButton classBtn = Theme.createCircleButton("Class", "My Classroom");
            classBtn.addActionListener(e -> controller.showStudentScreen("STUDENT_CLASS"));

            add(homeBtn);
            add(Box.createVerticalStrut(16));
            add(practiceBtn);
            add(Box.createVerticalStrut(16));
            add(classBtn);
        }

        JButton logoutBtn = Theme.createCircleButton("Exit", "Logout");
        logoutBtn.addActionListener(e -> controller.logout());

        add(Box.createVerticalGlue());
        add(logoutBtn);
    }
}
