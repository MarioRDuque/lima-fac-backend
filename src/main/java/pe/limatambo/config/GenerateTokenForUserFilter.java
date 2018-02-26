package pe.limatambo.config;

import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.json.*;

import com.fasterxml.jackson.databind.*;
import pe.limatambo.dto.SessionItemDTO;
import pe.limatambo.security.TokenUser;
import pe.limatambo.security.TokenUtil;
import pe.limatambo.servicio.MenuServicio;
import pe.limatambo.util.Respuesta.EstadoOperacionEnum;
import pe.limatambo.util.SessionResponse;


/* This filter maps to /session and tries to validate the username and password */
public class GenerateTokenForUserFilter extends AbstractAuthenticationProcessingFilter {

    private TokenUtil tokenUtil;
    private MenuServicio menuServicio;

    protected GenerateTokenForUserFilter(String urlMapping, AuthenticationManager authenticationManager, TokenUtil tokenUtil,MenuServicio menuServicio) {
        super(new AntPathRequestMatcher(urlMapping));
        setAuthenticationManager(authenticationManager);
        this.tokenUtil = tokenUtil;
        this.menuServicio = menuServicio;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException, JSONException {
        try{
            String jsonString = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject userJSON = new JSONObject(jsonString);
            String username = userJSON.getString("username");
            String password = userJSON.getString("password");
            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            return getAuthenticationManager().authenticate(authToken); // This will take to successfulAuthentication or faliureAuthentication function
        }
        catch( JSONException | AuthenticationException e){
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication ( HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authToken) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authToken);
        TokenUser tokenUser = (TokenUser)authToken.getPrincipal();
        SessionResponse resp = new SessionResponse();
        SessionItemDTO respItem = new SessionItemDTO();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String tokenString = this.tokenUtil.createTokenForUser(tokenUser);
        respItem.setNombre(tokenUser.getUsuario().getNombre());
        respItem.setUsuarioId(tokenUser.getUsuario().getUserId());
        respItem.setToken(tokenString);
        respItem.setTipoUsuario(tokenUser.getUsuario().getTipousuario().getId());
        respItem.setMenus(menuServicio.listarPorTipoDeUsuario(tokenUser.getUsuario().getTipousuario().getId()));
        resp.setEstadoOperacion(EstadoOperacionEnum.EXITO.getValor());
        resp.setOperacionMensaje("Login Success");
        resp.setItem(respItem);
        String jsonRespString = ow.writeValueAsString(resp);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(jsonRespString);
        res.getWriter().flush();
        res.getWriter().close();
    }
}
