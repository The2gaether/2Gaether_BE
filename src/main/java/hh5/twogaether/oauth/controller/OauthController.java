package hh5.twogaether.oauth.controller;

import hh5.twogaether.oauth.service.OauthService;
import hh5.twogaether.oauth.dto.OauthLoginRequestDto;
import hh5.twogaether.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static hh5.twogaether.security.jwt.JwtUtil.AUTHORIZATION_HEADER;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OauthController {

    private final OauthService oauthService;
    private final JwtUtil jwtUtil;

    /**
     * OAuth 로그인 시 인증 코드를 넘겨받은 후 첫 로그인 시 회원가입
     */
    // redirect url 과 authorization code 를 받아온다.
    @PostMapping("/login/oauth/{providerName}")
    public String login(@PathVariable String providerName,
                        @RequestBody OauthLoginRequestDto oauthLoginRequestDto,
                        HttpServletResponse response) throws IllegalAccessException {
        log.info("[providerName] = {}",providerName);
        log.info("[code] = {}",oauthLoginRequestDto.getCode());
        String email = oauthService.login(providerName, oauthLoginRequestDto.getCode());

        // access(& refresh) 토큰 만들기
        response.addHeader(AUTHORIZATION_HEADER, jwtUtil.createToken(email));
        return email;
    }
}
