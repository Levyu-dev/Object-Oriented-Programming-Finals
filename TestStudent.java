
public class TestStudent {

    public static void main(String[] args) {

        // Create student
        Student student = new Student("Iyce", "iyce123");

        // Add quiz results
        student.recordQuizResult("Java", 8, 10);
        student.recordQuizResult("HTML", 5, 10);
        student.recordQuizResult("Java", 9, 10);

        // Add completed challenges
        student.completeChallenge("Java Basics");
        student.completeChallenge("HTML Fundamentals");

        // Display progress
        student.displayProgress();
    }
}
