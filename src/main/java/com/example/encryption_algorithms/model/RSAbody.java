package com.example.encryption_algorithms.model;

public class RSAbody {
    private int order;
    private char letter;
    private int alph_order;
    private int result;

    public RSAbody() {}

    public RSAbody(int order, char letter, int alph_order, int result) {
        this.order = order;
        this.letter = letter;
        this.alph_order = alph_order;
        this.result = result;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getAlph_order() {
        return alph_order;
    }

    public void setAlph_order(int alph_order) {
        this.alph_order = alph_order;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }
}
