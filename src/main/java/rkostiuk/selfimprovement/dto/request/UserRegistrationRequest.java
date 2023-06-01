package rkostiuk.selfimprovement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserRegistrationRequest {

    @NotEmpty(message = "{valid.user.name.notEmpty}")
    @Size(min = 2, max = 128, message = "{valid.user.name.size}")
    private String name;

    @Size(min = 2, max = 128, message = "{valid.user.surname.size}")
    private String surname;

    @NotEmpty(message = "{valid.user.email.notEmpty}")
    @Size(min = 2, max = 128, message = "{valid.user.email.size}")
    @Email(message = "{valid.user.email.format}")
    private String email;

    @NotEmpty(message = "{valid.user.password.notEmpty}")
    @Size(min = 4, message = "{valid.user.password.size}")
    private String password;

    @NotEmpty(message = "{valid.user.password.notEmpty}")
    private String repeatedPassword;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "{valid.user.birthday.past}")
    private Date birthday;

}
