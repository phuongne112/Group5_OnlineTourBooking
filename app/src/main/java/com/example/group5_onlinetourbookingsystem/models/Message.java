package com.example.group5_onlinetourbookingsystem.models;

public class Message {
    private int id;
    private String senderName;
    private String subject;
    private String content;
    private String email;
    private boolean answered; // ✅ Thêm trạng thái đã trả lời

    public Message(int id, String senderName, String subject, String content, String email) {
        this.id = id;
        this.senderName = senderName;
        this.subject = subject;
        this.content = content;
        this.email = email;
        this.answered = false; // mặc định là chưa trả lời
    }

    // ✅ Constructor có trạng thái answered
    public Message(int id, String senderName, String subject, String content, String email, boolean answered) {
        this.id = id;
        this.senderName = senderName;
        this.subject = subject;
        this.content = content;
        this.email = email;
        this.answered = answered;
    }

    // Getter
    public int getId() { return id; }
    public String getSenderName() { return senderName; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public String getEmail() { return email; }
    public boolean isAnswered() { return answered; }

    // Setter
    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}
