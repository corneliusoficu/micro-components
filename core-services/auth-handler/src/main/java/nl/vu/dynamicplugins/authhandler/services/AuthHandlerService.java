package nl.vu.dynamicplugins.authhandler.services;


import nl.vu.dynamicplugins.authhandler.models.LoginRequest;
import nl.vu.dynamicplugins.authhandler.models.User;
import nl.vu.dynamicplugins.authhandler.models.ValidationResult;
import nl.vu.dynamicplugins.authhandler.validators.AuthValidator;
import nl.vu.dynamicplugins.core.base.services.BaseEndpoint;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Component(service = AuthHandlerService.class, immediate = true, property = //
        { //
                "service.exported.interfaces=*", //
                "service.exported.configs=org.apache.cxf.rs", //
                "org.apache.cxf.rs.address=/auth-handler", "cxf.bus.prop.skip.default.json.provider.registration=true" } //
)
public class AuthHandlerService extends BaseEndpoint {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthHandlerService.class);
    private final AuthValidator authValidator;
    private final UsersStorageHandler usersStorage;

    public AuthHandlerService() {
        authValidator = new AuthValidator();
        usersStorage = new UsersStorageHandler();
    }

    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("login")
    public Response handleAuth(LoginRequest loginRequest) {
        ValidationResult validationResult = authValidator.validateLoginRequest(loginRequest);

        if(validationResult.isError()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(validationResult)
                    .build();
        }

        User user = usersStorage.retrieveUserFromStorage(loginRequest);

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