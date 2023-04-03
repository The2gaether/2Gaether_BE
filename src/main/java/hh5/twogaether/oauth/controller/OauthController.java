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

    @PostMapping("/login/oauth/{providerName}")
    public String login(@PathVariable String providerName,
                        @RequestBody OauthLoginRequestDto oauthLoginRequestDto,
                        HttpServletResponse response) throws IllegalAccessException {
        String email = oauthService.login(providerName, oauthLoginRequestDto.getCode());
        response.addHeader(AUTHORIZATION_HEADER, jwtUtil.createToken(email));
        return email;
    }
}
