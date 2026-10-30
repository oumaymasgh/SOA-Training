package webservices;

import filtres.TokenUtil;
import io.jsonwebtoken.Claims;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/*
 * Si vous avez besoin d’obtenir des informations sur le contexte d’exécution de la requête,
 * vous pouvez utilisez l’annotation @javax.ws.rs.core.Context pour obtenir une instance d’une classe particulière.
 * UriInfo : Cette interface donne accès à l’URI de la requête.
 */

@Path("/authentification")
public class AuthenticationEndPoint {

    // ======================================
    // = Injection Points =
    // ======================================

    @Context
    private UriInfo uriInfo;

    // ======================================
    // = LOGIN : renvoie access token + refresh token =
    // ======================================

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {
        try {
            // Authenticate the user using the credentials provided
            authenticate(username, password);

            String issuer = uriInfo.getAbsolutePath().toString();

            // Issue the two tokens
            String accessToken  = TokenUtil.createToken(username, issuer, "access",  TokenUtil.ACCESS_DURATION);
            String refreshToken = TokenUtil.createToken(username, issuer, "refresh", TokenUtil.REFRESH_DURATION);

            System.out.println("access token  : " + accessToken);
            System.out.println("refresh token : " + refreshToken);

            String json = "{\"accessToken\":\"" + accessToken + "\","
                    + "\"refreshToken\":\"" + refreshToken + "\"}";

            return Response.ok(json).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    // ======================================
    // = REFRESH : refresh token -> nouvel access token =
    // ======================================

    @POST
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response refresh(@FormParam("refreshToken") String refreshToken) {
        try {
            // Checks the signature and the expiration date
            Claims claims = TokenUtil.parse(refreshToken);

            // Only a refresh token is accepted here
            if (!"refresh".equals(claims.get("type"))) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            String username = claims.getSubject();
            String issuer = uriInfo.getAbsolutePath().toString();

            String newAccess = TokenUtil.createToken(username, issuer, "access", TokenUtil.ACCESS_DURATION);

            return Response.ok("{\"accessToken\":\"" + newAccess + "\"}").build();

        } catch (Exception e) {
            System.out.println("#### refresh failed : " + e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


    private void authenticate(String username, String password) {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        System.out.println("Authenticating user...");
    }
}