package com.eyre.parentemailhelper.pojo;

import java.util.ArrayList;
import java.util.List;

public class Paragraph {

    private String text = "";

    private List<String> lines = new ArrayList<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getLines() {
        return lines;
    }

    public void addLines(String line) {
        lines.add(line);
    }
}
