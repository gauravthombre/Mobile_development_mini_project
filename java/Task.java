package com.example.studytaskmanager;

public class Task {
    private int id;
    private String title;
    private String subject;
    private String dueDate;
    private String priority;
    private boolean done;

    public Task(int id, String title, String subject, String dueDate, String priority, boolean done) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.dueDate = dueDate;
        this.priority = priority;
        this.done = done;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getSubject() { return subject; }
    public String getDueDate() { return dueDate; }
    public String getPriority() { return priority; }
    public boolean isDone() { return done; }
}
