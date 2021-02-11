
/**
 * User class is the super class for seller and buyer
 */
public class User
{
    protected String username, password;
    protected int id;
    protected String email="";
    protected String fullName="";

    public User(String username, String password, int id)
    {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public UserBound getBoundary(){
        return new UserBound();
    }

    public int getId() {
        return id;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public String getFullName(){
        return fullName;
    }
}
