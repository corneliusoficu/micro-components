package nl.vu.dynamicplugins.authhandler.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import nl.vu.dynamicplugins.authhandler.MongoDBHandler;
import nl.vu.dynamicplugins.authhandler.PasswordAuthentication;
import nl.vu.dynamicplugins.authhandler.models.LoginRequest;
import nl.vu.dynamicplugins.authhandler.models.RegisterRequest;
import nl.vu.dynamicplugins.authhandler.models.User;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class UsersStorageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersStorageHandler.class);

    private static final String DEFAULT_ISSUER = "corneliu.soficu";
    private static final Long DEFAULT_TTL_HOURS = 24L;

    private final MongoDBHandler mongoDBHandler;
    private final PasswordAuthentication passwordAuthentication;

    public UsersStorageHandler() {
        this.mongoDBHandler = MongoDBHandler.getConnection();
        this.passwordAuthentication = new PasswordAuthentication();
    }

    User retrieveAuthenticatedUserFromStorage(LoginRequest loginRequest) throws NoSuchAlgorithmException {
        String email = loginRequest.getEmail();
        LOGGER.info("Retrieving user: {}", email);
        Document userDocument = mongoDBHandler.getUser(email);

        if(userDocument == null) {
            return null;
        }

        boolean isAuthenticated = passwordAuthentication
                .authenticate(loginRequest.getPassword().toCharArray(), userDocument.getString("hashed_password"));

        if(!isAuthenticated) {
            return null;
        }

        String token = this.createJWT(1L, DEFAULT_ISSUER, email, DEFAULT_TTL_HOURS);

        if(token == null) {
            return null;
        }

        User user = new User();
        user.setId(userDocument.getObjectId("_id").toHexString());
        user.setEmail(userDocument.getString("email"));
        user.setFirstName(userDocument.getString("first_name"));
        user.setLastName(userDocument.getString("last_name"));
        user.setToken(token);
        return user;
    }

    public User createUser(RegisterRequest registerRequest) {
        Document userDocument = mongoDBHandler.getUser(registerRequest.getEmail());
        if(userDocument != null) {
            return null;
        }

        String hashedPasswordStr = passwordAuthentication.hash(registerRequest.getPassword().toCharArray());

        mongoDBHandler.createUser(
                registerRequest.getEmail(),
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                hashedPasswordStr);

        userDocument = mongoDBHandler.getUser(registerRequest.getEmail());
        User user = new User();
        user.setId(userDocument.getObjectId("_id").toHexString());
        user.setEmail(userDocument.getString("email"));
        user.setFirstName(userDocument.getString("first_name"));
        user.setLastName(userDocument.getString("last_name"));
        return user;
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
