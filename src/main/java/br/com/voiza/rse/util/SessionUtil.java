package br.com.voiza.rse.util;

import br.com.voiza.rse.dto.PropertiesDTO;
import com.taskadapter.redmineapi.bean.User;
import javax.faces.context.FacesContext;

/**
 * Classe utilitária para gerenciar os dados guardados na sessão.
 * 
 * @author aneto
 */
public class SessionUtil {
 
    private static final String USER_SESSION_ATTRIBUTE = "user";
    private static final String PROPERTIES_SESSION_ATTRIBUTE = "properties";
    
    /**
     * Adiciona as propriedades da aplicação, na sessão o usuário
     */
    public static void addPropertiesToSession() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(PROPERTIES_SESSION_ATTRIBUTE, PropertiesUtil.loadPropertiesData());
    }
    
    /**
     * Retorna as propriedades presentes na sessão
     * @return 
     */
    public static PropertiesDTO getPropertiesFromSession() {
         return (PropertiesDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(PROPERTIES_SESSION_ATTRIBUTE);
    }
    
    /**
     * Adiciona o usuário à sessão
     * @param user 
     */
    public static void addUserToSession(User user) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(USER_SESSION_ATTRIBUTE, user);
    }
    
    /**
     * Remove o usuário da sessão
     */
    public static void removeUserFromSession() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(USER_SESSION_ATTRIBUTE);
    }
    
}
