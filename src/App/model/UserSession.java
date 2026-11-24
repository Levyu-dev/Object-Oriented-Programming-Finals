package App.model;

import javax.swing.DefaultListModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class UserSession {
    public enum QuestionType {
        SHORT_ANSWER,
        MULTIPLE_CHOICE
    }

    public static class TeacherQuestion {
        private final QuestionType type;
        private final String prompt;
        private final String correctAnswer;
        private final String[] choices;
        private final int points;

        public TeacherQuestion(QuestionType type, String prompt, String correctAnswer, String[] choices, int points) {
            this.type = type;
            this.prompt = prompt;
            this.correctAnswer = correctAnswer;
            this.choices = choices == null ? new String[0] : choices.clone();
            this.points = points;
        }

        public QuestionType getType() { return type; }
        public String getPrompt() { return prompt; }
        public String getCorrectAnswer() { return correctAnswer; }
        public String[] getChoices() { return choices.clone(); }
        public int getPoints() { return points; }
    }

    public static class TeacherQuiz {
        private final String title;
        private final List<TeacherQuestion> questions;

        public TeacherQuiz(String title, List<TeacherQuestion> questions) {
            this.title = title;
            this.questions = Collections.unmodifiableList(new ArrayList<>(questions));
        }

        public String getTitle() { return title; }
        public List<TeacherQuestion> getQuestions() { return questions; }
        public String getAssignmentLabel() { return title + " (" + questions.size() + " Questions)"; }
    }

    public static class StudentRecord {
        private final String name;
        private final String section;
        private int xp;
        private int completedQuizzes;
        private long totalCorrectAnswers;
        private long totalQuestions;

        public StudentRecord(String name, String section, int xp, int completedQuizzes) {
            this.name = name;
            this.section = section;
            this.xp = xp;
            this.completedQuizzes = completedQuizzes;
        }

        public String getName() { return name; }
        public String getSection() { return section; }
        public int getXp() { return xp; }
        public int getCompletedQuizzes() { return completedQuizzes; }
        public int getAverageQuizScore() {
            return totalQuestions == 0
                    ? 0
                    : (int) Math.round(totalCorrectAnswers * 100.0 / totalQuestions);
        }
        public double getAccuracy() {
            return totalQuestions == 0
                    ? 0.0
                    : totalCorrectAnswers * 100.0 / totalQuestions;
        }

        public void addPoints(int points) {
            xp += points;
            completedQuizzes++;
        }

        private void recordQuizResult(int points, int correctAnswers, int questionCount) {
            if (points < 0 || questionCount <= 0
                    || correctAnswers < 0 || correctAnswers > questionCount) {
                throw new IllegalArgumentException("Invalid quiz result.");
            }
            xp += points;
            completedQuizzes++;
            totalCorrectAnswers += correctAnswers;
            totalQuestions += questionCount;
        }
    }

    private String currentUserName = "Student";
    private String currentSection = "OOP101";
    private StudentRecord currentStudent;
    private boolean demoTeacherSession;

    private final List<StudentRecord> studentRecords = new ArrayList<>();
    private final Map<String, DefaultListModel<String>> sectionAssignments = new HashMap<>();
    private final Map<String, Map<String, TeacherQuiz>> teacherQuizzes = new HashMap<>();
    private final Map<String, Integer> courseProgress = new HashMap<>();
    private final Set<String> startedCourses = new HashSet<>();
    private final CourseCatalog courseCatalog = new CourseCatalog();
    private final File progressFile = new File("data/course-progress.properties");
    private final File tutorialFile = new File("data/student-tutorial.properties");

    public UserSession() {
        loadCourseProgress();
    }

    public void loginStudent(String name, String section) {
        this.currentUserName = name.trim();
        this.currentSection = section.trim().toUpperCase();

        StudentRecord studentRecord = findStudent(this.currentUserName, this.currentSection);
        if (studentRecord == null) {
            studentRecord = new StudentRecord(this.currentUserName, this.currentSection, 0, 0);
            studentRecords.add(studentRecord);
        }

        this.currentStudent = studentRecord;
        this.demoTeacherSession = false;
    }

    private StudentRecord findStudent(String name, String section) {
        for (StudentRecord student : studentRecords) {
            if (student.getName().equalsIgnoreCase(name)
                    && student.getSection().equalsIgnoreCase(section)) {
                return student;
            }
        }
        return null;
    }

    public void loginDemoTeacher(String name, String section) {
        this.currentUserName = name.trim();
        this.currentSection = section.trim().toUpperCase();
        this.currentStudent = null;
        this.demoTeacherSession = true;
    }

    public void logout() {
        currentUserName = "";
        currentSection = "";
        currentStudent = null;
        demoTeacherSession = false;
    }

    public StudentRecord getCurrentStudent() {
        if (currentStudent == null) {
            throw new IllegalStateException("No student is currently logged in.");
        }
        return currentStudent;
    }

    public boolean isStudentSession() {
        return currentStudent != null;
    }

    public boolean hasSeenStudentTutorial() {
        if (!isStudentSession()) return true;
        Properties tutorialProperties = loadTutorialProperties();
        return Boolean.parseBoolean(
                tutorialProperties.getProperty(createStudentTutorialKey()));
    }

    public void markStudentTutorialSeen() {
        if (!isStudentSession()) return;
        Properties tutorialProperties = loadTutorialProperties();
        tutorialProperties.setProperty(createStudentTutorialKey(), "true");
        saveTutorialProperties(tutorialProperties);
    }

    private String createStudentTutorialKey() {
        return currentSection.trim().toUpperCase()
                + "." + currentUserName.trim().replace(" ", "_").toLowerCase();
    }

    private Properties loadTutorialProperties() {
        Properties tutorialProperties = new Properties();
        if (!tutorialFile.isFile()) return tutorialProperties;
        try (FileInputStream inputStream = new FileInputStream(tutorialFile)) {
            tutorialProperties.load(inputStream);
        } catch (IOException ignored) {
        }
        return tutorialProperties;
    }

    private void saveTutorialProperties(Properties tutorialProperties) {
        File parent = tutorialFile.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) return;
        try (FileOutputStream outputStream = new FileOutputStream(tutorialFile)) {
            tutorialProperties.store(outputStream, "Learn to Code student tutorials");
        } catch (IOException ignored) {
        }
    }

    public void addExperiencePoints(int points) {
        getCurrentStudent().addPoints(points);
    }

    public void addExperiencePoints(int points, int correctAnswers, int questionCount) {
        getCurrentStudent().recordQuizResult(points, correctAnswers, questionCount);
    }

    public CourseCatalog getCourseCatalog() {
        return courseCatalog;
    }

    public int getCompletedCourseLessons(String courseName) {
        if (!isStudentSession()) return 0;
        String courseProgressKey = createCourseProgressKey(courseName);
        return Math.min(
                courseProgress.getOrDefault(courseProgressKey, 0),
                courseCatalog.getLessonCount(courseName));
    }

    public boolean hasStartedCourse(String courseName) {
        return isStudentSession() && startedCourses.contains(createCourseProgressKey(courseName));
    }

    public void startCourse(String courseName) {
        if (!isStudentSession() || !courseCatalog.getCourses().containsKey(courseName)) return;
        String courseProgressKey = createCourseProgressKey(courseName);
        startedCourses.add(courseProgressKey);
        courseProgress.putIfAbsent(courseProgressKey, 0);
        saveCourseProgress();
    }

    public void completeCourseLesson(String courseName, int lessonIndex) {
        if (!isStudentSession()) return;
        int total = courseCatalog.getLessonCount(courseName);
        if (total == 0) {
            throw new IllegalArgumentException("Unknown course: " + courseName);
        }
        String courseProgressKey = createCourseProgressKey(courseName);
        int completedLessonCount = courseProgress.getOrDefault(courseProgressKey, 0);
        if (lessonIndex != completedLessonCount || completedLessonCount >= total) return;
        courseProgress.put(courseProgressKey, completedLessonCount + 1);
        startedCourses.add(courseProgressKey);
        saveCourseProgress();
    }

    public String getDashboardCourse() {
        if (!isStudentSession()) return null;
        String firstStartedCourse = null;
        for (String courseName : courseCatalog.getCourses().keySet()) {
            if (!hasStartedCourse(courseName)) continue;
            if (firstStartedCourse == null) firstStartedCourse = courseName;
            int completedLessonCount = getCompletedCourseLessons(courseName);
            int totalLessonCount = courseCatalog.getLessonCount(courseName);
            if (completedLessonCount < totalLessonCount) {
                return courseName;
            }
        }
        return firstStartedCourse;
    }

    private String createCourseProgressKey(String courseName) {
        String safeCourseName = courseName.trim().replace(".", "_").replace(" ", "_") + "_Roadmap";
        return currentSection + "." + currentUserName.replace(" ", "_") + "." + safeCourseName;
    }

    private void loadCourseProgress() {
        if (!progressFile.isFile()) return;
        Properties courseProgressProperties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(progressFile)) {
            courseProgressProperties.load(inputStream);
            for (String propertyKey : courseProgressProperties.stringPropertyNames()) {
                if (propertyKey.endsWith(".started")) {
                    if (Boolean.parseBoolean(courseProgressProperties.getProperty(propertyKey))) {
                        startedCourses.add(propertyKey.substring(
                                0, propertyKey.length() - ".started".length()));
                    }
                    continue;
                }
                try {
                    int completedLessonCount = Integer.parseInt(
                            courseProgressProperties.getProperty(propertyKey));
                    courseProgress.put(propertyKey, completedLessonCount);
                    if (completedLessonCount > 0) startedCourses.add(propertyKey);
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (IOException ignored) {
        }
    }

    private void saveCourseProgress() {
        File parent = progressFile.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            return;
        }
        Properties courseProgressProperties = new Properties();
        for (Map.Entry<String, Integer> progressEntry : courseProgress.entrySet()) {
            courseProgressProperties.setProperty(
                    progressEntry.getKey(), Integer.toString(progressEntry.getValue()));
        }
        for (String startedCourseKey : startedCourses) {
            courseProgressProperties.setProperty(startedCourseKey + ".started", "true");
        }
        try (FileOutputStream outputStream = new FileOutputStream(progressFile)) {
            courseProgressProperties.store(outputStream, "Learn to Code course progress");
        } catch (IOException ignored) {
        }
    }

    public DefaultListModel<String> getAssignmentsForSection(String section) {
        String sectionKey = section.trim().toUpperCase();
        sectionAssignments.putIfAbsent(sectionKey, new DefaultListModel<>());
        return sectionAssignments.get(sectionKey);
    }

    public boolean addTeacherQuiz(String section, TeacherQuiz quiz) {
        if (!demoTeacherSession) {
            throw new IllegalStateException("Only a teacher demo session can publish quizzes.");
        }
        String sectionKey = section.trim().toUpperCase();
        DefaultListModel<String> sectionAssignmentsList = getAssignmentsForSection(sectionKey);
        String assignmentLabel = quiz.getAssignmentLabel();
        for (int assignmentIndex = 0;
                assignmentIndex < sectionAssignmentsList.size(); assignmentIndex++) {
            if (sectionAssignmentsList.get(assignmentIndex).equalsIgnoreCase(assignmentLabel)) {
                return false;
            }
        }

        sectionAssignmentsList.addElement(assignmentLabel);
        teacherQuizzes.putIfAbsent(sectionKey, new HashMap<>());
        teacherQuizzes.get(sectionKey).put(assignmentLabel, quiz);
        return true;
    }

    public boolean deleteTeacherQuiz(String section, String assignmentLabel) {
        if (!demoTeacherSession) {
            throw new IllegalStateException("Only a teacher demo session can delete activities.");
        }
        String sectionKey = section.trim().toUpperCase();
        DefaultListModel<String> sectionAssignmentsList = getAssignmentsForSection(sectionKey);
        if (!sectionAssignmentsList.removeElement(assignmentLabel)) {
            return false;
        }

        Map<String, TeacherQuiz> quizzesForSection = teacherQuizzes.get(sectionKey);
        if (quizzesForSection != null) {
            quizzesForSection.remove(assignmentLabel);
            if (quizzesForSection.isEmpty()) teacherQuizzes.remove(sectionKey);
        }
        if (sectionAssignmentsList.isEmpty()) sectionAssignments.remove(sectionKey);
        return true;
    }

    public void resetAllDemoData() {
        if (currentStudent != null || demoTeacherSession) {
            throw new IllegalStateException("Sign out before resetting demo data.");
        }
        try {
            Files.deleteIfExists(progressFile.toPath());
            Files.deleteIfExists(tutorialFile.toPath());
        } catch (IOException ioException) {
            throw new IllegalStateException("Unable to clear saved course progress.", ioException);
        }

        studentRecords.clear();
        sectionAssignments.clear();
        teacherQuizzes.clear();
        courseProgress.clear();
        startedCourses.clear();
    }

    public TeacherQuiz getTeacherQuiz(String section, String assignmentLabel) {
        Map<String, TeacherQuiz> quizzesForSection =
                teacherQuizzes.get(section.trim().toUpperCase());
        return quizzesForSection == null ? null : quizzesForSection.get(assignmentLabel);
    }

    public List<StudentRecord> getStudentsInSection(String section) {
        List<StudentRecord> studentsInSection = new ArrayList<>();
        for (StudentRecord student : studentRecords) {
            if (student.getSection().equalsIgnoreCase(section)) {
                studentsInSection.add(student);
            }
        }
        studentsInSection.sort(
                (firstStudent, secondStudent) -> Integer.compare(
                        secondStudent.getXp(), firstStudent.getXp()));
        return studentsInSection;
    }

    public String getCurrentUserName() { return currentUserName; }
    public String getCurrentSection() { return currentSection; }
}
