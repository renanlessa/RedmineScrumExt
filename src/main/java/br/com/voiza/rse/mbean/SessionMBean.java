package br.com.voiza.rse.mbean;

import br.com.voiza.rse.service.RedmineServiceBean;
import br.com.voiza.rse.util.SessionUtil;
import com.taskadapter.redmineapi.bean.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author aneto
 */
@ManagedBean
@SessionScoped
public class SessionMBean extends SuperMBean {
    
    @EJB
    private RedmineServiceBean redmineServiceBean;
    
    private String username;
    private String password;
    private User user;
    private String redmineURL;
    
    private String theme = "ui-lightness";
    
    
    private static final Logger LOGGER = Logger.getLogger(SessionMBean.class.getName());

    @PostConstruct
    public void init() {
        SessionUtil.addPropertiesToSession();
    }
    
    public String login() {
        try {
            user = redmineServiceBean.connectWithRedmine(username, password, redmineURL);
            
            SessionUtil.addUserToSession(user);
            
            return SUCCESS;
        } catch (Exception ex) {
            addErrorMessage(ex.getMessage());
            return FAIL;
        }        
    }

    /**
     * Retorna o ApplicationVersion presente no arquivo de properties que foi carregado na sessão do usuário.
     * @return 
     */
    public String getApplicationVersion() {
        return SessionUtil.getPropertiesFromSession().getApplicationVersion();
    }
    
    public String logout() {
        user = null;
        
        SessionUtil.removeUserFromSession();
        
        return LOGOUT;
    }
    
    public User getUser() {
        return user;
    }
    
    public boolean isLoggedUser() {
        boolean logged = user != null;
        LOGGER.log(Level.INFO, "IsLoggedUser: {0} ", logged);
        return logged;
    }
    
    public List<String> getRedmineURLs() {
        List<String> hosts = new ArrayList<String>();
        hosts.add("https://projetos.voiza.com.br");
        hosts.add("http://redmine.voiza.com.br");
        return hosts;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getRedmineURL() {
        return redmineURL;
    }

    public void setRedmineURL(String redmineURL) {
        this.redmineURL = redmineURL;
    }

}
