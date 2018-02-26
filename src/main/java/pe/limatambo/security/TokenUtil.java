package pe.limatambo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import javax.servlet.http.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import pe.limatambo.dao.UsuarioDao;
import pe.limatambo.entidades.Usuario;

@Service
public class TokenUtil {

    private static final long VALIDITY_TIME_MS = 10 * 24 * 60 * 60 * 1000;// 10 days Validity
    private static final String AUTH_HEADER_NAME = "Authorization";
    @Autowired 
    private UsuarioDao usuarioDao;

    private final String secret = "mrin";

    public Optional<Authentication> verifyToken(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);

        if (token != null && !token.isEmpty()) {
            final TokenUser user = parseUserFromToken(token.replace("Bearer", "").trim());
            if (user != null) {
                return Optional.of(new UserAuthentication(user));
            }
        }
        return Optional.empty();
    }

    //Get User Info from the Token
    public TokenUser parseUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        Usuario user = new Usuario();
        user.setUserId((String) claims.get("userId"));
        user.setPassword((String) claims.get("sub"));
        return new TokenUser(user);
    }
    
    public Usuario parseTokenFromUser(HttpServletRequest request) {
        String token = request.getHeader(TokenUtil.AUTH_HEADER_NAME);
        if(token != null){
            Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
            Usuario user = new Usuario();
            user.setUserId((String) claims.get("userId"));
            user.setPassword((String) claims.get("sub"));
            return user;
        } else{
            return null;
        }
    }

    public String createTokenForUser(TokenUser tokenUser) {
        return createTokenForUser(tokenUser.getUsuario());
    }

    public String createTokenForUser(Usuario user) {
        Usuario userTrue = usuarioDao.findOneByUserId(user.getUserId()).orElse(user);
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
                .setSubject(userTrue.getUserId())
                .claim("userId", userTrue.getUserId())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

}
