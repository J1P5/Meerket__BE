package org.j1p5.domain.auth;

import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.service.UserAppender;
import org.j1p5.domain.user.service.UserReader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OauthService {

    private final UserReader userReader;
    private final UserAppender userAppender;
    private final Map<Provider, OauthClient> clients;

    public OauthService(UserReader reader, UserAppender appender, List<OauthClient> oauthClients) {
        this.userReader = reader;
        this.userAppender = appender;
        this.clients = oauthClients.stream().collect(
                Collectors.toUnmodifiableMap(OauthClient::getProvider, oauthClient -> oauthClient)
        );
    }

    public Long login(String code, String provider) {
        OauthProfile profile = request(code, provider);

        UserEntity user = userReader.read(profile.getEmail(), provider);
        if (user == null) {
            user = userAppender.append(profile, provider);
            return user.getId();
        }

        return user.getId();

    }

    private OauthProfile request(String code, String provider) {
        OauthClient oauthClient = clients.get(Provider.valueOf(provider));

        OauthToken token = oauthClient.getOauthToken(code);

        return oauthClient.getOauthProfile(token.getAccessToken());
    }
}
