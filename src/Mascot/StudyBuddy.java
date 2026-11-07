public class StudyBuddy {

    // attributes
    private String name;
    private String characterType;
    private String expression;
    private String message;

    // constructor
    public StudyBuddy(String name, String characterType) {
        this.name = name;
        this.characterType = characterType;
        this.expression = "normal";
        this.message = "Hey! Ready to learn something new?";
    }

    
    // change mascot expression
    public void setExpression(String expression) {
        this.expression = expression;
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

      // get mascot image path
    public String getImagePath() {
        String folder = "assets/mascot/" + characterType + "/";
        String fileName = expression;

        if (characterType.equalsIgnoreCase("male")) {
            fileName += " M";
        }

        return folder + fileName + ".png";
    }
}

