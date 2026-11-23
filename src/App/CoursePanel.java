package App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CoursePanel extends BaseScreen {
    private final JPanel roadmap = new JPanel();
    private final JLabel progressLabel = new JLabel();

    public CoursePanel(ApplicationController controller) {
        super(controller);
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.PALE_LIME);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Theme.PALE_LIME);

        JLabel title = new JLabel("OOP Basics Roadmap");
        title.setFont(new Font(Theme.UI_FONT, Font.BOLD, 20));
        title.setForeground(Theme.TEXT_DARK);

        JLabel description = new JLabel("Learn the concepts in order, study examples, and pass each checkpoint.");
        description.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 12));
        description.setForeground(Theme.TEXT_DARK);

        progressLabel.setFont(new Font(Theme.UI_FONT, Font.BOLD, 12));
        progressLabel.setForeground(Theme.DEEP_TEAL);
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(description);
        header.add(Box.createVerticalStrut(6));
        header.add(progressLabel);

        roadmap.setLayout(new BoxLayout(roadmap, BoxLayout.Y_AXIS));
        roadmap.setBackground(Theme.PALE_LIME);

        JScrollPane scroll = new JScrollPane(roadmap);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        refresh();
    }

    @Override
    public void refresh() {
        roadmap.removeAll();
        UserSession session = controller.getSession();
        CourseCatalog catalog = session.getCourseCatalog();
        List<CourseCatalog.Lesson> lessons = catalog.getLessons(CourseCatalog.OOP_BASICS);
        boolean started = session.hasStartedCourse(CourseCatalog.OOP_BASICS);
        int completed = session.getCompletedCourseLessons(CourseCatalog.OOP_BASICS);

        progressLabel.setText(started
                ? "Progress: " + completed + " of " + lessons.size() + " lessons"
                : lessons.size() + "-lesson learning path - start with lesson 1");

        for (int i = 0; i < lessons.size(); i++) {
            final int lessonIndex = i;
            CourseCatalog.Lesson lesson = lessons.get(i);
            boolean isComplete = i < completed;
            boolean isAvailable = i <= completed;

            JPanel card = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
            card.setLayout(new BorderLayout(8, 6));

            JLabel title = new JLabel(lesson.getTitle());
            title.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
            title.setForeground(Theme.TEXT_DARK);

            JTextArea summary = new JTextArea(lesson.getOverview(), 2, 30);
            summary.setEditable(false);
            summary.setLineWrap(true);
            summary.setWrapStyleWord(true);
            summary.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 11));
            summary.setForeground(Theme.TEXT_DARK);
            summary.setOpaque(false);
            summary.setFocusable(false);

            JPanel details = new JPanel(new BorderLayout(0, 4));
            details.setOpaque(false);
            details.add(title, BorderLayout.NORTH);
            details.add(summary, BorderLayout.CENTER);

            String buttonText = isComplete ? "REVIEW" : isAvailable
                    ? (started ? "STUDY LESSON" : "START ROADMAP") : "LOCKED";
            JButton lessonButton = Theme.createPrimaryButton(buttonText);
            lessonButton.setEnabled(isAvailable);
            lessonButton.addActionListener(e -> openLesson(lessonIndex, lessons.get(lessonIndex)));

            card.add(details, BorderLayout.CENTER);
            card.add(lessonButton, BorderLayout.EAST);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            roadmap.add(card);
            roadmap.add(Box.createVerticalStrut(8));
        }

        roadmap.revalidate();
        roadmap.repaint();
    }

    private void openLesson(int lessonIndex, CourseCatalog.Lesson lesson) {
        UserSession session = controller.getSession();
        session.startCourse(CourseCatalog.OOP_BASICS);
        controller.showStudentScreen("COURSES");

        JPanel lessonContent = new JPanel();
        lessonContent.setLayout(new BoxLayout(lessonContent, BoxLayout.Y_AXIS));
        lessonContent.setBackground(Theme.WHITE);

        JTextArea overview = new JTextArea(lesson.getOverview());
        overview.setEditable(false);
        overview.setLineWrap(true);
        overview.setWrapStyleWord(true);
        overview.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 14));
        overview.setBackground(Theme.WHITE);

        JTextArea example = new JTextArea(lesson.getExample());
        example.setEditable(false);
        example.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        example.setBackground(Theme.INPUT_BACKGROUND);
        example.setBorder(new EmptyBorder(10, 10, 10, 10));

        lessonContent.add(overview);
        lessonContent.add(Box.createVerticalStrut(12));
        lessonContent.add(new JLabel("Example"));
        lessonContent.add(Box.createVerticalStrut(4));
        lessonContent.add(example);

        JScrollPane lessonScroll = new JScrollPane(lessonContent);
        lessonScroll.setPreferredSize(new Dimension(430, 250));
        JOptionPane.showMessageDialog(
                this, lessonScroll, lesson.getTitle(), JOptionPane.INFORMATION_MESSAGE);

        Object selected = JOptionPane.showInputDialog(
                this,
                lesson.getCheckpoint(),
                "Checkpoint",
                JOptionPane.QUESTION_MESSAGE,
                null,
                lesson.getChoices(),
                lesson.getChoices()[0]);
        if (selected == null) return;

        boolean correct = selected.toString().startsWith(lesson.getAnswer() + ")");
        if (!correct) {
            controller.showAlert("Not quite. Review the lesson and try again.");
            return;
        }

        if (lessonIndex == session.getCompletedCourseLessons(CourseCatalog.OOP_BASICS)) {
            session.completeCourseLesson(CourseCatalog.OOP_BASICS, lessonIndex);
        }
        controller.showAlert(lesson.getExplanation());
        controller.showStudentScreen("COURSES");
    }
}
