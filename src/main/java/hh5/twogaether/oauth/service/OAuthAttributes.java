package hh5.twogaether.oauth.service;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String name;
    private String email;

    public static OAuthAttributes of(String providerName, Map<String, Object> attributes) throws IllegalAccessException {
        if ("kakao".equals(providerName)) {
            return ofKakao(attributes);
        } else if ("google".equals(providerName)) {
            return ofGoogle(attributes);
        } else {
            throw new IllegalAccessException("허용되지 않은 접근입니다.");
        }
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");
        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .build();
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .build();
    }
}
