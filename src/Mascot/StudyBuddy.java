
public class StudyBuddy {

    // attributes
    private String name;
    private String characterType;
    private String expression;
    private String message;

    // constructor
    public StudyBuddy(String name, String characterType) {
        if (characterType == null ||
            (!characterType.equalsIgnoreCase("male") &&
             !characterType.equalsIgnoreCase("female"))) {
            throw new IllegalArgumentException("Invalid character type.");
        }

        this.name = name;
        this.characterType = characterType;
        this.expression = "normal";
        this.message = "Hey! Ready to learn something new?";
    }

    // change mascot expression
    public void setExpression(String expression) {
        if (expression == null) {
            throw new IllegalArgumentException("Expression cannot be null.");
        }

        switch (expression.toLowerCase()) {
            case "normal":
            case "happy":
            case "excited":
            case "confused":
            case "thinking":
            case "sad":
                this.expression = expression;
                break;
            default:
                throw new IllegalArgumentException("Invalid expression.");
        }
    }

    // change mascot message
    public void showMessage(String message) {
        this.message = message;
    }

    // react to user's answer
    public void reactToAnswer(boolean correct) {
        if (correct) {
            setExpression("happy");
            showMessage("Nice work! You're getting better!");
        } else {
            setExpression("confused");
            showMessage("Almost there! Let's try again.");
        }
    }

    // react to completed achievement
    public void reactToAchievement(String achievementName) {
        setExpression("excited");
        showMessage("You unlocked " + achievementName + "! Great job!");
    }

    // getters
    public String getName() {
        return name;
    }

    public String getCharacterType() {
        return characterType;
    }

    public String getExpression() {
        return expression;
    }

    public String getMessage() {
        return message;
    }

    // get correct image filename
    private String getFileName() {
        switch (expression.toLowerCase()) {
            case "normal":
                return "normal";
            case "happy":
                return "Happy";
            case "excited":
                return "Excited";
            case "confused":
                return "Confused";
            case "thinking":
                return "Thinking";
            case "sad":
                return "Sad";
            default:
                return "normal";
        }
    }

    // get mascot image path
    public String getImagePath() {
        String folderName;

        if (characterType.equalsIgnoreCase("male")) {
            folderName = "Male";
        } else {
            folderName = "Female";
        }

        String fileName = getFileName();

        if (characterType.equalsIgnoreCase("male")) {
            if (expression.equalsIgnoreCase("sad")) {
                fileName = "sad";
            }

            fileName += " M";
        }

        return "assets/mascot/" + folderName + "/" + fileName + ".png";
    }
}
