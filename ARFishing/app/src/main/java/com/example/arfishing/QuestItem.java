package com.example.arfishing;

public class QuestItem {
    String questName;

    int questCount;
    int questNow;

    int questComplete;
    int resld;

    public QuestItem(String questName, int questCount, int questNow, int questComplete, int resld){
        this.questName = questName;
        this.questCount = questCount;
        this.questNow = questNow;
        this.questComplete = questComplete;
        this.resld = resld;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public int getQuestCount() {
        return questCount;
    }

    public void setQuestCount(int questCount) {
        this.questCount = questCount;
    }

    public int getQuestNow() {
        return questNow;
    }

    public void setQuestNow(int questNow) {
        this.questNow = questNow;
    }

    public int getQuestComplete() {
        return questComplete;
    }

    public void setQuestComplete(int questComplete) {
        this.questComplete = questComplete;
    }

    public int getResld() {
        return resld;
    }

    public void setResld(int resld) {
        this.resld = resld;
    }
}

