package nl.vu.dynamicplugins.authhandler.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import nl.vu.dynamicplugins.authhandler.models.LoginRequest;
import nl.vu.dynamicplugins.authhandler.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class UsersStorageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersStorageHandler.class);

    private static final String DEFAULT_ISSUER = "corneliu.soficu";
    private static final Long DEFAULT_TTL_HOURS = 24L;

    User retrieveUserFromStorage(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();

        String token = this.createJWT(1L, DEFAULT_ISSUER, email, DEFAULT_TTL_HOURS);

        if(token == null) {
            return null;
        }

        User mockUser = new User();
        mockUser.setEmail("corneliu.soficu@gmail.com");
        mockUser.setId(1L);
        mockUser.setFirstName("Corneliu");
        mockUser.setLastName("Soficu");
        mockUser.setToken(token);

        return mockUser;
    }

    private String createJWT(Long id, String issuer, String subject, Long ttlHours) {
        Date date = new Date();
        long differenceMillis = date.getTime() + TimeUnit.HOURS.toMillis(ttlHours);
        Date expireDate = new Date(differenceMillis);
        String token;

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            token = JWT.create()
                    .withIssuedAt(date)
                    .withExpiresAt(expireDate)
                    .withSubject(subject)
                    .withKeyId(id.toString())
                    .withIssuer(issuer)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            LOGGER.error("Could not create JWT for user: {}", subject);
            token = null;
        }

        return token;
    }
}
