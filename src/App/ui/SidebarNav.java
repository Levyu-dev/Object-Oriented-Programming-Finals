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
            teacherDashboardButton.addActionListener(
                    event -> controller.showTeacherScreen("TEACHER_DASH"));

            JButton assignmentsButton = Theme.createCircleButton("Assign", "Manage Assignments");
            assignmentsButton.addActionListener(
                    event -> controller.showTeacherScreen("TEACHER_CLASS"));

            add(teacherDashboardButton);
            add(Box.createVerticalStrut(16));
            add(assignmentsButton);
        } else {
            JButton studentHomeButton = Theme.createCircleButton("Home", "Student Dashboard");
            studentHomeButton.addActionListener(
                    event -> controller.showStudentScreen("STUDENT_DASH"));

            JButton practiceButton = Theme.createCircleButton("Prac", "Practice");
            practiceButton.addActionListener(event -> controller.showStudentScreen("PRACTICE"));

            JButton coursesButton = Theme.createCircleButton("Course", "Browse courses");
            coursesButton.addActionListener(event -> controller.showStudentScreen("COURSES"));

            JButton classroomButton = Theme.createCircleButton("Class", "My Classroom");
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
}

