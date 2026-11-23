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

        public void addPoints(int points) {
            xp += points;
            completedQuizzes++;
        }
    }

    private String currentUserName = "Student";
    private String currentSection = "OOP101";
    private StudentRecord currentStudent;
    private boolean demoTeacherSession;

    private final List<StudentRecord> allStudents = new ArrayList<>();
    private final Map<String, DefaultListModel<String>> sectionAssignments = new HashMap<>();
    private final Map<String, Map<String, TeacherQuiz>> teacherQuizzes = new HashMap<>();
    private final Map<String, Integer> courseProgress = new HashMap<>();
    private final Set<String> startedCourses = new HashSet<>();
    private final CourseCatalog courseCatalog = new CourseCatalog();
    private final File progressFile = new File("data/course-progress.properties");

    public UserSession() {
        loadCourseProgress();
    }

    public void loginStudent(String name, String section) {
        this.currentUserName = name.trim();
        this.currentSection = section.trim().toUpperCase();

        StudentRecord existing = findStudent(this.currentUserName, this.currentSection);
        if (existing == null) {
            existing = new StudentRecord(this.currentUserName, this.currentSection, 0, 0);
            allStudents.add(existing);
        }

        this.currentStudent = existing;
        this.demoTeacherSession = false;
    }

    private StudentRecord findStudent(String name, String section) {
        for (StudentRecord student : allStudents) {
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

    boolean isDemoTeacherSession() {
        return demoTeacherSession;
    }

    public void addXP(int points) {
        getCurrentStudent().addPoints(points);
    }

    public CourseCatalog getCourseCatalog() {
        return courseCatalog;
    }

    public int getCompletedCourseLessons(String courseName) {
        if (!isStudentSession()) return 0;
        String key = progressKey(courseName);
        return Math.min(courseProgress.getOrDefault(key, 0), courseCatalog.getLessonCount(courseName));
    }

    public boolean hasStartedCourse(String courseName) {
        return isStudentSession() && startedCourses.contains(progressKey(courseName));
    }

    public void startCourse(String courseName) {
        if (!isStudentSession() || !courseCatalog.getCourses().containsKey(courseName)) return;
        startedCourses.add(progressKey(courseName));
        courseProgress.putIfAbsent(progressKey(courseName), 0);
        saveCourseProgress();
    }

    public void completeCourseLesson(String courseName, int lessonIndex) {
        if (!isStudentSession()) return;
        int total = courseCatalog.getLessonCount(courseName);
        if (total == 0) {
            throw new IllegalArgumentException("Unknown course: " + courseName);
        }
        String key = progressKey(courseName);
        int completed = courseProgress.getOrDefault(key, 0);
        if (lessonIndex != completed || completed >= total) return;
        courseProgress.put(key, completed + 1);
        startedCourses.add(key);
        saveCourseProgress();
    }

    public String getDashboardCourse() {
        if (!isStudentSession()) return null;
        String firstStarted = null;
        for (String courseName : courseCatalog.getCourses().keySet()) {
            if (!hasStartedCourse(courseName)) continue;
            if (firstStarted == null) firstStarted = courseName;
            int completed = getCompletedCourseLessons(courseName);
            int total = courseCatalog.getLessonCount(courseName);
            if (completed < total) {
                return courseName;
            }
        }
        return firstStarted;
    }

    private String progressKey(String courseName) {
        String safeName = courseName.trim().replace(".", "_").replace(" ", "_") + "_Roadmap";
        return currentSection + "." + currentUserName.replace(" ", "_") + "." + safeName;
    }

    private void loadCourseProgress() {
        if (!progressFile.isFile()) return;
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(progressFile)) {
            properties.load(input);
            for (String key : properties.stringPropertyNames()) {
                if (key.endsWith(".started")) {
                    if (Boolean.parseBoolean(properties.getProperty(key))) {
                        startedCourses.add(key.substring(0, key.length() - ".started".length()));
                    }
                    continue;
                }
                try {
                    int progress = Integer.parseInt(properties.getProperty(key));
                    courseProgress.put(key, progress);
                    if (progress > 0) startedCourses.add(key);
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
        Properties properties = new Properties();
        for (Map.Entry<String, Integer> entry : courseProgress.entrySet()) {
            properties.setProperty(entry.getKey(), Integer.toString(entry.getValue()));
        }
        for (String key : startedCourses) {
            properties.setProperty(key + ".started", "true");
        }
        try (FileOutputStream output = new FileOutputStream(progressFile)) {
            properties.store(output, "Learn to Code course progress");
        } catch (IOException ignored) {
        }
    }

    public DefaultListModel<String> getAssignmentsForSection(String section) {
        String key = section.trim().toUpperCase();
        sectionAssignments.putIfAbsent(key, new DefaultListModel<>());
        return sectionAssignments.get(key);
    }

    public boolean addTeacherQuiz(String section, TeacherQuiz quiz) {
        if (!demoTeacherSession) {
            throw new IllegalStateException("Only a teacher demo session can publish quizzes.");
        }
        String sectionKey = section.trim().toUpperCase();
        DefaultListModel<String> assignments = getAssignmentsForSection(sectionKey);
        String label = quiz.getAssignmentLabel();
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).equalsIgnoreCase(label)) {
                return false;
            }
        }

        assignments.addElement(label);
        teacherQuizzes.putIfAbsent(sectionKey, new HashMap<>());
        teacherQuizzes.get(sectionKey).put(label, quiz);
        return true;
    }

    public boolean deleteTeacherQuiz(String section, String assignmentLabel) {
        if (!demoTeacherSession) {
            throw new IllegalStateException("Only a teacher demo session can delete activities.");
        }
        String sectionKey = section.trim().toUpperCase();
        DefaultListModel<String> assignments = getAssignmentsForSection(sectionKey);
        if (!assignments.removeElement(assignmentLabel)) {
            return false;
        }

        Map<String, TeacherQuiz> quizzes = teacherQuizzes.get(sectionKey);
        if (quizzes != null) {
            quizzes.remove(assignmentLabel);
            if (quizzes.isEmpty()) teacherQuizzes.remove(sectionKey);
        }
        if (assignments.isEmpty()) sectionAssignments.remove(sectionKey);
        return true;
    }

    public void resetAllDemoData() {
        if (currentStudent != null || demoTeacherSession) {
            throw new IllegalStateException("Sign out before resetting demo data.");
        }
        try {
            Files.deleteIfExists(progressFile.toPath());
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to clear saved course progress.", ex);
        }

        allStudents.clear();
        sectionAssignments.clear();
        teacherQuizzes.clear();
        courseProgress.clear();
        startedCourses.clear();
    }

    public TeacherQuiz getTeacherQuiz(String section, String assignmentLabel) {
        Map<String, TeacherQuiz> quizzes = teacherQuizzes.get(section.trim().toUpperCase());
        return quizzes == null ? null : quizzes.get(assignmentLabel);
    }

    public List<StudentRecord> getStudentsInSection(String section) {
        List<StudentRecord> list = new ArrayList<>();
        for (StudentRecord s : allStudents) {
            if (s.getSection().equalsIgnoreCase(section)) list.add(s);
        }
        list.sort((a, b) -> Integer.compare(b.getXp(), a.getXp()));
        return list;
    }

    public String getCurrentUserName() { return currentUserName; }
    public String getCurrentSection() { return currentSection; }
}
