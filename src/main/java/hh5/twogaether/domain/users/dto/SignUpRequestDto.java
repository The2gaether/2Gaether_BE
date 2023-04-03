package hh5.twogaether.domain.users.dto;

import hh5.twogaether.domain.users.entity.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static hh5.twogaether.domain.users.entity.UserRoleEnum.*;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    @Email(message = "이메일 형식으로 입력해야 합니다.")
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^*+=-]).{8,15}$",
            message = "비밀번호는 8 ~ 15자로 소문자, 대문자, 숫자, 특수문자(!@#$%^*+=-)를 포함해야 합니다.")
    private String password;

    private String username;

    private UserRoleEnum userRole = USER;

    public SignUpRequestDto(SignUpRequestDto signupRequestDto, String encryptPassword) {
        this.username = signupRequestDto.getUsername();
        this.email = signupRequestDto.getEmail();
        this.password = encryptPassword;
    }

}
