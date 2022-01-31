package nl.vu.dynamicplugins.authhandler.services;


import nl.vu.dynamicplugins.authhandler.models.ErrorResponse;
import nl.vu.dynamicplugins.authhandler.models.LoginRequest;
import nl.vu.dynamicplugins.authhandler.models.RegisterRequest;
import nl.vu.dynamicplugins.authhandler.models.User;
import nl.vu.dynamicplugins.core.base.services.BaseEndpoint;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;

@Component(service = AuthHandlerService.class, immediate = true, property = //
        { //
                "service.exported.interfaces=*", //
                "service.exported.configs=org.apache.cxf.rs", //
                "org.apache.cxf.rs.address=/auth-handler", "cxf.bus.prop.skip.default.json.provider.registration=true" } //
)
public class AuthHandlerService extends BaseEndpoint {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthHandlerService.class);
    private final UsersStorageHandler usersStorage;

    public AuthHandlerService() {
        usersStorage = new UsersStorageHandler();
    }

    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("login")
    public Response handleAuth(LoginRequest loginRequest) throws NoSuchAlgorithmException {
        User user = usersStorage.retrieveAuthenticatedUserFromStorage(loginRequest);

        if(user == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Provided credentials are wrong!"))
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(user)
                .build();
    }

    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("register")
    public Response handleRegister(RegisterRequest registerRequest) throws NoSuchAlgorithmException {
        if(!registerRequest.getPassword().equals(registerRequest.getRepeatPassword())) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Passwords do not match!"))
                    .build();
        }

        User user = usersStorage.createUser(registerRequest);

        if(user == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("User could not be created!"))
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(user)
                .build();
    }

    @Override
    public Response health() {
        LOGGER.info("Requesting health for auth-handler");
        return super.health();
    }
}