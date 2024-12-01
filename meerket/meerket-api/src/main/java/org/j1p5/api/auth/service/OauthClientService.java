package org.j1p5.api.auth.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.j1p5.domain.auth.OauthClient;
import org.j1p5.domain.auth.dto.OauthProfile;
import org.j1p5.domain.auth.dto.OauthToken;
import org.j1p5.domain.user.entity.Provider;
import org.springframework.stereotype.Component;

@Component
public class OauthClientService {

    private final Map<Provider, OauthClient> clients;

    public OauthClientService(List<OauthClient> oauthClients) {
        this.clients =
                oauthClients.stream()
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        OauthClient::getProvider, oauthClient -> oauthClient));
    }

    public OauthProfile request(String code, Provider provider) {
        OauthClient oauthClient = clients.get(provider);

        OauthToken token = oauthClient.getOauthToken(code);

        return oauthClient.getOauthProfile(token.getAccessToken());
    }
}
