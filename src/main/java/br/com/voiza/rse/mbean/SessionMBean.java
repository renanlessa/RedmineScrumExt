package br.com.voiza.rse.mbean;

import br.com.voiza.rse.service.RedmineServiceBean;
import com.taskadapter.redmineapi.bean.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author aneto
 */
@ManagedBean
@SessionScoped
public class SessionMBean extends SuperMBean {
    
    @EJB
    private RedmineServiceBean redmineServiceBean;
    private static final String USER_SESSION_ATTRIBUTE = "user";
    
    private String username;
    private String password;
    private User user;
    private String redmineURL;
    
    private String theme = "ui-lightness";
    
    private static final Logger LOGGER = Logger.getLogger(SessionMBean.class.getName());
       
    public String login() {
        try {
            user = redmineServiceBean.connectWithRedmine(username, password, redmineURL);
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(USER_SESSION_ATTRIBUTE, user);
            
            return SUCCESS;
        } catch (Exception ex) {
            addErrorMessage(ex.getMessage());
            return FAIL;
        }        
    }
    
    public String logout() {
        user = null;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(USER_SESSION_ATTRIBUTE);
        
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
        hosts.add("http://projetos.voiza.com.br");
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
