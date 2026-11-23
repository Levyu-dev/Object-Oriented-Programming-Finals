public class TestStudent {

    public static void main(String[] args) {

        // Create student
        Student student = new Student("Iyce", "iyce123");

        // Add quiz results
        student.recordQuizResult("HTML", 5, 10);
        student.recordQuizResult("Java", 8, 10);
        student.recordQuizResult("Java", 9, 10);

        // Add completed challenges
        student.completeChallenge("Java Basics");
        student.completeChallenge("HTML Fundamentals");

        // Sort by accuracy (highest first)
        System.out.println("\nSORTING BY ACCURACY");

        student.sortPerformanceByAccuracy(false);

        System.out.println("Verified: "
                + BubbleSort.verifyAccuracy(
                        student.getPerformanceRecords(), false));

        // (lowest first)
        System.out.println("\nSORTING BY QUIZ SCORE");

        student.sortPerformanceByQuizScore(true);

        System.out.println("Verified: "
                + BubbleSort.verifyQuizScore(
                        student.getPerformanceRecords(), true));

        student.displayProgress();
    }
}
