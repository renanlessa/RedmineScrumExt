package br.com.voiza.rse.filter;

import com.taskadapter.redmineapi.bean.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aneto
 */
@WebFilter("/content/*")
public class AuthorizationFilter implements Filter {
    
    private static final Logger LOGGER = Logger.getLogger(AuthorizationFilter.class.getName());
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {    
        LOGGER.log(Level.INFO, "AuthorizationFilter - doFilter");
        HttpServletRequest req = (HttpServletRequest) request;
        User user = (User) req.getSession().getAttribute("user");
        LOGGER.log(Level.INFO, "AuthorizationFilter - user: {0}", user);
        
        if (user != null) {
            LOGGER.log(Level.INFO, "Acesso Garantido");
            chain.doFilter(request, response);
        } else {
            LOGGER.log(Level.INFO, "Acesso Negado");
            HttpServletResponse res = (HttpServletResponse) response;
            res.sendRedirect(req.getContextPath() + "/login.jsf");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void destroy() {
        
    }
}
