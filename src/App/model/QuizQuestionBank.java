package App.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizQuestionBank {
    private static final List<String> QUICK_PRACTICE_TOPICS = Collections.unmodifiableList(Arrays.asList(
            "Java Basics", "OOP Basics", "Conditionals", "Loops", "Methods", "Arrays"));

    public static List<String> getQuickPracticeTopics() {
        return QUICK_PRACTICE_TOPICS;
    }

    public static int getQuickPracticeQuestionCount(String topic) {
        return createQuestionsForTopic(topic).size();
    }

    public static List<QuizQuestion> createQuestionsForTopic(String topic) {
        if ("OOP Basics".equals(topic)) return new ArrayList<>(createCoreQuizQuestions());

        List<QuizQuestion> quizQuestions = new ArrayList<>();
        if ("Java Basics".equals(topic)) {
            addMultipleChoiceQuestion(quizQuestions, "Which primitive type stores whole numbers?", "A) int", "B) String", "C) boolean", "D) char", "A");
            addMultipleChoiceQuestion(quizQuestions, "What ends most Java statements?", "A) :", "B) ;", "C) ,", "D) .", "B");
            addMultipleChoiceQuestion(quizQuestions, "Which method is the usual Java program entry point?", "A) start()", "B) run()", "C) public static void main(String[] args)", "D) init()", "C");
            addMultipleChoiceQuestion(quizQuestions, "Which type stores text?", "A) String", "B) int", "C) boolean", "D) double", "A");
            addMultipleChoiceQuestion(quizQuestions, "Which statement prints a line to the console?", "A) Console.write()", "B) System.out.println()", "C) print.System()", "D) System.input()", "B");
            addMultipleChoiceQuestion(quizQuestions, "Which primitive type can store decimal values with double precision?", "A) int", "B) char", "C) double", "D) boolean", "C");
            addMultipleChoiceQuestion(quizQuestions, "Which symbol begins a single-line comment?", "A) //", "B) **", "C) ##", "D) ::", "A");
            addMultipleChoiceQuestion(quizQuestions, "Which keyword declares a class?", "A) object", "B) class", "C) define", "D) new", "B");
        } else if ("Conditionals".equals(topic)) {
            addMultipleChoiceQuestion(quizQuestions, "Which statement runs code only when a condition is true?", "A) if", "B) import", "C) class", "D) return", "A");
            addMultipleChoiceQuestion(quizQuestions, "Which operator checks whether two primitive values are equal?", "A) =", "B) ==", "C) !=", "D) <=", "B");
            addMultipleChoiceQuestion(quizQuestions, "What type represents true or false?", "A) int", "B) char", "C) boolean", "D) String", "C");
            addMultipleChoiceQuestion(quizQuestions, "Which keyword handles the alternative when an if condition is false?", "A) else", "B) extends", "C) break", "D) static", "A");
            addMultipleChoiceQuestion(quizQuestions, "Which operator means logical AND?", "A) ||", "B) &&", "C) !", "D) %", "B");
            addMultipleChoiceQuestion(quizQuestions, "What does else if allow a program to do?", "A) Declare a class", "B) Test another condition", "C) Repeat a loop", "D) End the program", "B");
            addMultipleChoiceQuestion(quizQuestions, "Which keyword selects among matching cases?", "A) switch", "B) while", "C) package", "D) new", "A");
            addMultipleChoiceQuestion(quizQuestions, "What does !true evaluate to?", "A) true", "B) false", "C) 1", "D) null", "B");
        } else if ("Loops".equals(topic)) {
            addMultipleChoiceQuestion(quizQuestions, "Which loop is convenient when the iteration count is known?", "A) for", "B) if", "C) switch", "D) try", "A");
            addMultipleChoiceQuestion(quizQuestions, "Which loop checks its condition before each iteration?", "A) do-while", "B) while", "C) constructor", "D) if-else", "B");
            addMultipleChoiceQuestion(quizQuestions, "Which loop always executes its body at least once?", "A) for", "B) while", "C) do-while", "D) enhanced for", "C");
            addMultipleChoiceQuestion(quizQuestions, "Which keyword exits a loop immediately?", "A) continue", "B) break", "C) skip", "D) stop", "B");
            addMultipleChoiceQuestion(quizQuestions, "Which keyword skips to the next loop iteration?", "A) continue", "B) break", "C) return", "D) exit", "A");
            addMultipleChoiceQuestion(quizQuestions, "What does i++ do?", "A) Decreases i", "B) Increases i by one", "C) Resets i to zero", "D) Compares i", "B");
            addMultipleChoiceQuestion(quizQuestions, "What can while (true) create if it never exits?", "A) An infinite loop", "B) A constructor", "C) An array", "D) A class", "A");
            addMultipleChoiceQuestion(quizQuestions, "What is a loop inside another loop called?", "A) Infinite loop", "B) Nested loop", "C) Conditional loop", "D) Return loop", "B");
        } else if ("Methods".equals(topic)) {
            addMultipleChoiceQuestion(quizQuestions, "Where is a method's return type written?", "A) Before its name", "B) After its body", "C) Inside its parameters", "D) It is omitted", "A");
            addMultipleChoiceQuestion(quizQuestions, "What does void mean for a method?", "A) It returns an integer", "B) It returns no value", "C) It cannot be called", "D) It is a class", "B");
            addMultipleChoiceQuestion(quizQuestions, "What is a parameter?", "A) A value received by a method", "B) A package name", "C) A loop condition", "D) An object created by new", "A");
            addMultipleChoiceQuestion(quizQuestions, "What is an argument?", "A) The method name", "B) A value passed to a method call", "C) The return type", "D) A class field", "B");
            addMultipleChoiceQuestion(quizQuestions, "When are two methods overloaded?", "A) Same name with different parameter lists", "B) Same body in two files", "C) Both are private", "D) Both return void", "A");
            addMultipleChoiceQuestion(quizQuestions, "Which keyword sends a value back from a method?", "A) yield", "B) return", "C) send", "D) break", "B");
            addMultipleChoiceQuestion(quizQuestions, "How can a static method be called without creating an object?", "A) Through its class name", "B) Through a loop", "C) Through a constructor only", "D) It cannot be called", "A");
            addMultipleChoiceQuestion(quizQuestions, "A variable declared inside a method is generally what?", "A) A local variable", "B) A package", "C) A superclass", "D) An interface", "A");
        } else if ("Arrays".equals(topic)) {
            addMultipleChoiceQuestion(quizQuestions, "What is the index of the first array element?", "A) 1", "B) 0", "C) -1", "D) It depends on the type", "B");
            addMultipleChoiceQuestion(quizQuestions, "How do you get an array's number of elements?", "A) size()", "B) count", "C) length", "D) capacity()", "C");
            addMultipleChoiceQuestion(quizQuestions, "Which declaration creates an integer array variable?", "A) int[] scores;", "B) array int scores;", "C) scores<int>;", "D) int scores[]()", "A");
            addMultipleChoiceQuestion(quizQuestions, "What is the last valid index of an array?", "A) length", "B) length + 1", "C) length - 1", "D) 0", "C");
            addMultipleChoiceQuestion(quizQuestions, "Which expression creates space for five integers?", "A) int[5] scores;", "B) new int[5]", "C) int scores(5)", "D) new array int", "B");
            addMultipleChoiceQuestion(quizQuestions, "What kind of values does a Java array hold?", "A) Values of its declared element type", "B) Any unrelated values", "C) Methods only", "D) Classes only", "A");
            addMultipleChoiceQuestion(quizQuestions, "Which loop is designed to visit each array value?", "A) enhanced for", "B) switch", "C) if", "D) do", "A");
            addMultipleChoiceQuestion(quizQuestions, "How do you access the first value in an array named values?", "A) values[1]", "B) values.first", "C) values[0]", "D) values()", "C");
        }
        return quizQuestions;
    }

    private static void addMultipleChoiceQuestion(
            List<QuizQuestion> quizQuestions, String prompt,
            String choiceA, String choiceB, String choiceC, String choiceD,
            String correctAnswerLetter) {
        quizQuestions.add(new MultipleChoiceQuestion(
                prompt, new String[]{choiceA, choiceB, choiceC, choiceD}, correctAnswerLetter));
    }

    public static List<QuizQuestion> createCoreQuizQuestions() {
        List<QuizQuestion> quizQuestions = new ArrayList<>();

        quizQuestions.add(new MultipleChoiceQuestion(
                "A method that calculates and returns a value.",
                new String[]{"A) Void Method", "B) Value-returning method", "C) User-defined Method", "D) Equivalent Method"},
                "B"));
        quizQuestions.add(new MultipleChoiceQuestion(
                "A keyword used to make a class inherit from another.",
                new String[]{"A) super", "B) static", "C) override", "D) extends"},
                "D"));
        quizQuestions.add(new MultipleChoiceQuestion(
                "Which keyword hides implementation details and shows essential features?",
                new String[]{"A) abstraction", "B) inheritance", "C) overloading", "D) encapsulation"},
                "A"));
        quizQuestions.add(new ShortAnswerQuestion(
                "What does polymorphism mean?", "many forms"));
        quizQuestions.add(new ShortAnswerQuestion(
                "A parent-child relationship between classes.", "inheritance"));
        quizQuestions.add(new CodeOutputQuestion(
                "class Low { Low() { System.out.println(1); } }\nclass High extends Low { High() { System.out.println(2); } }\nLow obj = new High();",
                "12"));
        quizQuestions.add(new CodeCompletionQuestion(
                "Expected Outcome: Meow.\nclass Animal { void speak() {} }\nclass Cat _____ Animal { void speak() { System.out.println(\"Meow\"); } }",
                "extends"));
        quizQuestions.add(new DebuggingQuestion(
                "class Parent { Parent(String msg) { System.out.println(msg); } }\nclass Child extends Parent { Child() { System.out.println(\"Child code\"); } }",
                "super(msg);"));

        return Collections.unmodifiableList(quizQuestions);
    }
}
