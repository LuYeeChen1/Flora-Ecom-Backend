package com.backend.flowershop.infrastructure.security;

import com.backend.flowershop.application.port.security.UserInfoEmailPort;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CognitoUserInfoClient implements UserInfoEmailPort {
    private final RestClient restClient;
    private final String userInfoUri;

    public CognitoUserInfoClient(RestClient.Builder builder,
                                 @Value("${security.cognito.userinfo-uri}") String userInfoUri) {
        this.restClient = builder.build();
        this.userInfoUri = userInfoUri;
    }

    @Override
    public Optional<String> fetchEmail(String accessToken) {
        UserInfoResponse response = restClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(UserInfoResponse.class);
        return Optional.ofNullable(response).map(UserInfoResponse::email);
    }

    private record UserInfoResponse(String email) {
    }
}
