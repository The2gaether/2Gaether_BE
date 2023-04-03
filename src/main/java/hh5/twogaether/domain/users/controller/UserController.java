package hh5.twogaether.domain.users.controller;

import hh5.twogaether.domain.mypage.service.MyPageService;
import hh5.twogaether.domain.users.dto.LoginRequestDto;
import hh5.twogaether.domain.users.dto.LoginResponseDto;
import hh5.twogaether.domain.users.dto.SignUpRequestDto;
import hh5.twogaether.domain.users.dto.ResponseMessageDto;
import hh5.twogaether.domain.users.entity.User;
import hh5.twogaether.domain.users.service.UserService;
import hh5.twogaether.security.UserDetailsImpl;
import hh5.twogaether.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static hh5.twogaether.security.jwt.JwtUtil.AUTHORIZATION_HEADER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MyPageService myPageService;
    private final JwtUtil jwtUtil;

    @GetMapping("/")
    public String healthCheck() {
        return "hello world!";
    }

    @PostMapping("/users/signup")
    public ResponseEntity<ResponseMessageDto> signUp(@RequestBody SignUpRequestDto signupRequestDto) throws Exception {
        userService.createUser(signupRequestDto);
        return new ResponseEntity<>(new ResponseMessageDto(CREATED.value(), "회원가입 완료"), CREATED);
    }

    @PostMapping("/users/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto,
                                                      HttpServletResponse response) {
        User user = userService.login(loginRequestDto);
        response.addHeader(AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
        return new ResponseEntity<>(new LoginResponseDto(OK.value(), "로그인 완료", user.getUsername()), OK);
    }

    @PostMapping("/users/dupcheck")
    public ResponseEntity<ResponseMessageDto> emailDupcheck(@RequestBody SignUpRequestDto signUpRequestDto) {
        userService.checkEmailDuplication(signUpRequestDto.getEmail());
        return new ResponseEntity<>(new ResponseMessageDto(OK.value(), "사용 가능한 이메일입니다."), OK);
    }

    @DeleteMapping("/users/delete")
    public ResponseEntity<ResponseMessageDto> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getUser().getId());
        return new ResponseEntity<>(new ResponseMessageDto(OK.value(), "사용자 정보를 삭제합니다."), OK);
    }
}