package hh5.twogaether.oauth.service;

import hh5.twogaether.domain.users.entity.User;
import hh5.twogaether.domain.users.repository.UserRepository;
import hh5.twogaether.oauth.dto.OauthTokenResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static hh5.twogaether.domain.users.entity.UserRoleEnum.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OauthService {

    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final UserRepository userRepository;

    /**
     * @InMemoryRepository 는 application-oauth properties 정보를 담고 있음
     * @getToken() 넘겨받은 code 로 OAuth 서버에 Token 요청
     * @getUserProjile 첫 로그인 시 회원가입 후 email 반환(프론트 요청)
     * TODO 유저 인증 후 Refresh Token 생성
     */
    @Transactional
    public String login(String providerName, String code) throws IllegalAccessException {
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        OauthTokenResponseDto tokenResponse = getToken(code, provider);
        String email = getUerProfile(providerName, tokenResponse, provider);
        return email;
    }

    private OauthTokenResponseDto getToken(String code, ClientRegistration provider) {
        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {  // HTTP Header 생성
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(requestToken(code, provider))
                .retrieve()
                .bodyToMono(OauthTokenResponseDto.class)
                .block();
    }

    private MultiValueMap<String, String> requestToken(String code, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id", provider.getClientId());
        return formData;
    }

    private String getUerProfile(String providerName, OauthTokenResponseDto tokenResponse,
                                 ClientRegistration provider) throws IllegalAccessException {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        OAuthAttributes attributes = OAuthAttributes.of(providerName, userAttributes);
        String nickname = attributes.getName();
        String email = attributes.getEmail();
        Optional<User> foundUser = userRepository.findByUsername(email);
        if (foundUser.isEmpty()) {
            userRepository.save(new User(nickname, email, providerName, USER));
        }
        return email;
    }

    private Map<String, Object> getUserAttributes(ClientRegistration provider,
                                                  OauthTokenResponseDto tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
