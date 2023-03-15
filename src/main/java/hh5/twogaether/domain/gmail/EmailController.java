package hh5.twogaether.domain.gmail;

import hh5.twogaether.security.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@Controller
@RequiredArgsConstructor
public class EmailController {
    private final EmailServiceImpl emailServiceImpl;
    @GetMapping("/email/{emailCode}/{userEmail}")
    public String emailConfirm(@PathVariable String emailCode,
                                                                     @PathVariable String userEmail)  throws Exception {
        emailServiceImpl.emailLinkCheck(emailCode, userEmail);

        EmailCheckSuccessResponseDto emailCheckSuccessResponseDto = new EmailCheckSuccessResponseDto(200, ResponseMessage.EMAIL_SUCCESS_CHECK_LINK);
//        return new ResponseEntity<>(emailCheckSuccessResponseDto, HttpStatus.OK);

        return "email2";
    }

    //@GetMapping("/pwreset/{emailCode}/{userEmail}")
    //public String pwReset(@PathVariable String emailCode,
    //                     @PathVariable String userEmail)throws Exception {
    //    emailServiceImpl.pwLinkCheck(emailCode, userEmail);
    //    EmailCheckSuccessResponseDto emailCheckSuccessResponseDto = new EmailCheckSuccessResponseDto(200, ResponseMessage.EMAIL_SUCCESS_CHECK_LINK);
    //    return "비밀번호를 초기화해주는 엄청난 html";
    //}
}
