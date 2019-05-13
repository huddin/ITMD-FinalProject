package javaapplication1.domain;

//This class is middle layer between dao and menu.
//When a action is called from menu this class is called and then this class calls dao
//This keeps the code very clean and understandable.
//This keeps the code very clean and and readable and adds an extra layer of protection,
// so you can’t access Dao directly

public class User {

    private final int id;
    private final String name;
    private final String password;
    private final boolean admin;

    public User(int id, String name, String password, boolean admin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.admin = admin;
    }

//    public String getName() {
//        return name;
//    }
//
//    public String getPassword() {
//        return password;
//    }

    public int getId() {
        return id;
    }

    public boolean isAdmin() {
        return admin;
    }
}
