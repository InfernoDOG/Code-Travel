package com.codetravel.combat;

public class CodeChallenge {
    private String question;
    private String answer;
    private int damage;

    public CodeChallenge(String question, String expectedAnswer, int damage) {
        this.question = question;
        this.answer = expectedAnswer;
        this.damage = damage;
    }

    public boolean checkAnswer(String answer) {
        return answer.equalsIgnoreCase(answer.trim());
    }

    public String getAnswer() {
    return this.answer; // или как называется поле с правильным ответом внутри CodeChallenge
    }
    public String getQuestion() { return question; }
    public int getDamage() { return damage; }
}
