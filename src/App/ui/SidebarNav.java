package App.ui;

import App.controller.ApplicationController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SidebarNav extends JPanel {

    public SidebarNav(ApplicationController controller, boolean teacherMode) {
        setBackground(Theme.SOFT_LIME_CARD);
        setPreferredSize(new Dimension(62, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 8, 20, 8));

        if (teacherMode) {
            JButton teacherDashboardButton = Theme.createCircleButton("Dash", "Teacher Dashboard");
            setNavIcon(teacherDashboardButton, "assets/icons/dashboard.png");
            teacherDashboardButton.addActionListener(
                    event -> controller.showTeacherScreen("TEACHER_DASH"));

            JButton assignmentsButton = Theme.createCircleButton("Assign", "Manage Assignments");
            setNavIcon(assignmentsButton, "assets/icons/classroom.png");
            assignmentsButton.addActionListener(
                    event -> controller.showTeacherScreen("TEACHER_CLASS"));

            JButton benchmarkButton = Theme.createCircleButton("DSA", "Algorithm Benchmarks");
            benchmarkButton.addActionListener(
                    event -> controller.showTeacherScreen("TEACHER_BENCHMARK"));

            add(teacherDashboardButton);
            add(Box.createVerticalStrut(16));
            add(assignmentsButton);
            add(Box.createVerticalStrut(16));
            add(benchmarkButton);
        } else {
            JButton studentHomeButton = Theme.createCircleButton("Home", "Student Dashboard");
            setNavIcon(studentHomeButton, "assets/icons/dashboard.png");
            studentHomeButton.addActionListener(
                    event -> controller.showStudentScreen("STUDENT_DASH"));

            JButton practiceButton = Theme.createCircleButton("Prac", "Practice");
            setNavIcon(practiceButton, "assets/icons/practice.png");
            practiceButton.addActionListener(event -> controller.showStudentScreen("PRACTICE"));

            JButton coursesButton = Theme.createCircleButton("Course", "Browse courses");
            setNavIcon(coursesButton, "assets/icons/roadmap.png");
            coursesButton.addActionListener(event -> controller.showStudentScreen("COURSES"));

            JButton classroomButton = Theme.createCircleButton("Class", "My Classroom");
            setNavIcon(classroomButton, "assets/icons/classroom.png");
            classroomButton.addActionListener(
                    event -> controller.showStudentScreen("STUDENT_CLASS"));

            add(studentHomeButton);
            add(Box.createVerticalStrut(16));
            add(practiceButton);
            add(Box.createVerticalStrut(16));
            add(coursesButton);
            add(Box.createVerticalStrut(16));
            add(classroomButton);
        }

        JButton signOutButton = Theme.createCircleButton("Sign Out", "Sign out of the current session");
        signOutButton.addActionListener(event -> controller.logout());

        add(Box.createVerticalGlue());
        add(signOutButton);
    }

    private void setNavIcon(JButton button, String path) {
        ImageIcon icon = Theme.loadAssetIcon(path, 24, 24);
        if (icon != null) {
            button.setIcon(icon);
            button.setText("");
        }
    }
}

