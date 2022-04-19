package com.nesterov.expressions.util;

import java.util.List;

public class LexemeBuffer {

    private int position;
     List<Lexeme> lexemes;

    public LexemeBuffer(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    public Lexeme next(){
        return lexemes.get(position++);
    }

    public void back(){
        position--;
    }

    public int getPosition() {
        return position;
    }
}
