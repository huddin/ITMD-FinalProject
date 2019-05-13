package javaapplication1.domain;

import java.util.Date;

//This class is middle layer between dao and menu.
//When a action is called from menu this class is called and then this class calls dao
//This keeps the code very clean and and readable and adds an extra layer of protection,
// so you can’t access Dao directly

public class Ticket {

    private final int id;
    private final String description;
    private final Date startDate;
    private final Date endDate;
    private final int userId;

    public Ticket(int id, String description, Date startDate, Date endDate, int userId) {
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getUserId() {
        return userId;
    }
}
