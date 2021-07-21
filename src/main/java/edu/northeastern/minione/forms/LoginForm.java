package edu.northeastern.minione.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginForm {
    @NotNull
    @Size(min=2, max=30, message="Username size should be in the range [2...30]")
    private String userName;

    @NotNull
    @Size(min=6, max=50, message = "The length of a safe password should be greater than 5.")
    private String password;

    public String getUserName(){
        return this.userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }

}
