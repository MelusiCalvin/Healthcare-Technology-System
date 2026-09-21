package za.co.ubuntuhealth.identity.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationRequest {

    @NotBlank
    @Size(max = 120)
    private String firstName;

    @NotBlank
    @Size(max = 120)
    private String lastName;

    @NotBlank
    @Size(max = 120)
    private String username;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @NotBlank
    @Size(max = 10)
    private String sex;

    @NotBlank
    @Size(min = 8, max = 72)
    private String password;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}