package App.ui;

import App.controller.ApplicationController;
import App.model.CourseCatalog;
import App.model.UserSession;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CoursePanel extends BaseScreen {
    private static class RoadmapList extends JPanel implements Scrollable {
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

    private final JPanel roadmapList = new RoadmapList();
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

        roadmapList.setLayout(new BoxLayout(roadmapList, BoxLayout.Y_AXIS));
        roadmapList.setBackground(Theme.PALE_LIME);

        JScrollPane roadmapScrollPane = new JScrollPane(roadmapList);
        roadmapScrollPane.setBorder(null);
        roadmapScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        roadmapScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(header, BorderLayout.NORTH);
        add(roadmapScrollPane, BorderLayout.CENTER);
        refresh();
    }

    @Override
    public void refresh() {
        roadmapList.removeAll();
        UserSession userSession = controller.getSession();
        CourseCatalog courseCatalog = userSession.getCourseCatalog();
        List<CourseCatalog.Lesson> lessons = courseCatalog.getLessons(CourseCatalog.OOP_BASICS);
        boolean hasStartedRoadmap = userSession.hasStartedCourse(CourseCatalog.OOP_BASICS);
        int completedLessonCount = userSession.getCompletedCourseLessons(CourseCatalog.OOP_BASICS);

        progressLabel.setText(hasStartedRoadmap
                ? "Progress: " + completedLessonCount + " of " + lessons.size() + " lessons"
                : lessons.size() + "-lesson learning path - start with lesson 1");

        for (int lessonIndex = 0; lessonIndex < lessons.size(); lessonIndex++) {
            final int selectedLessonIndex = lessonIndex;
            CourseCatalog.Lesson lesson = lessons.get(lessonIndex);
            boolean isLessonComplete = lessonIndex < completedLessonCount;
            boolean isLessonAvailable = lessonIndex <= completedLessonCount;

            JPanel card = Theme.createRoundedCard(Theme.SOFT_LIME_CARD, 12);
            card.setLayout(new BorderLayout(8, 6));

            JLabel title = new JLabel(lesson.getTitle());
            title.setFont(new Font(Theme.UI_FONT, Font.BOLD, 14));
            title.setForeground(Theme.TEXT_DARK);

            JTextArea summary = new JTextArea(lesson.getOverview(), 2, 0);
            summary.setEditable(false);
            summary.setLineWrap(true);
            summary.setWrapStyleWord(true);
            summary.setFont(new Font(Theme.UI_FONT, Font.PLAIN, 11));
            summary.setForeground(Theme.TEXT_DARK);
            summary.setOpaque(false);
            summary.setFocusable(false);

            JPanel lessonDetails = new JPanel(new BorderLayout(0, 4));
            lessonDetails.setOpaque(false);
            lessonDetails.add(title, BorderLayout.NORTH);
            lessonDetails.add(summary, BorderLayout.CENTER);

            String lessonButtonText = isLessonComplete ? "REVIEW" : isLessonAvailable
                    ? (hasStartedRoadmap ? "STUDY LESSON" : "START ROADMAP") : "LOCKED";
            JButton lessonButton = Theme.createPrimaryButton(lessonButtonText);
            lessonButton.setEnabled(isLessonAvailable);
            lessonButton.addActionListener(
                    event -> openLesson(selectedLessonIndex, lesson));

            card.add(lessonDetails, BorderLayout.CENTER);
            card.add(lessonButton, BorderLayout.EAST);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            roadmapList.add(card);
            roadmapList.add(Box.createVerticalStrut(8));
        }

        roadmapList.revalidate();
        roadmapList.repaint();
    }

    private void openLesson(int lessonIndex, CourseCatalog.Lesson lesson) {
        UserSession userSession = controller.getSession();
        userSession.startCourse(CourseCatalog.OOP_BASICS);
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
        Theme.applyTextStyling(lessonContent);

        JScrollPane lessonScroll = new JScrollPane(lessonContent);
        int dialogWidth = Math.max(240, Math.min(430, getWidth() - 40));
        int dialogHeight = Math.max(180, Math.min(320, getHeight() - 80));
        lessonScroll.setPreferredSize(new Dimension(dialogWidth, dialogHeight));
        JOptionPane.showMessageDialog(
                this, lessonScroll, lesson.getTitle(), JOptionPane.INFORMATION_MESSAGE);

        Object selectedAnswer = JOptionPane.showInputDialog(
                this,
                lesson.getCheckpoint(),
                "Checkpoint",
                JOptionPane.QUESTION_MESSAGE,
                null,
                lesson.getChoices(),
                lesson.getChoices()[0]);
        if (selectedAnswer == null) return;

        boolean correct = selectedAnswer.toString().startsWith(lesson.getCorrectAnswer() + ")");
        if (!correct) {
            controller.showAlert("Not quite. Review the lesson and try again.");
            return;
        }

        if (lessonIndex == userSession.getCompletedCourseLessons(CourseCatalog.OOP_BASICS)) {
            userSession.completeCourseLesson(CourseCatalog.OOP_BASICS, lessonIndex);
        }
        controller.showAlert(lesson.getExplanation());
        controller.showStudentScreen("COURSES");
    }
}
