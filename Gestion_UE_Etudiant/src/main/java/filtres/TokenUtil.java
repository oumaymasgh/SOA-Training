package filtres;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

public class TokenUtil {

    private static final String KEY_STRING = "simplekey";

    public static final long ACCESS_DURATION  = 30 * 1000L;           // 30 sec
    public static final long REFRESH_DURATION = 7 * 24 * 60 * 60 * 1000L;

    public static Key getKey() {
        byte[] bytes = KEY_STRING.getBytes();
        return new SecretKeySpec(bytes, 0, bytes.length, "DES");
    }

    public static String createToken(String username, String issuer, String type, long duration) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .claim("type", type)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + duration))
                .signWith(SignatureAlgorithm.HS512, getKey())
                .compact();
    }

    public static Claims parse(String token) {
        return Jwts.parser().setSigningKey(getKey()).parseClaimsJws(token).getBody();
    }

}