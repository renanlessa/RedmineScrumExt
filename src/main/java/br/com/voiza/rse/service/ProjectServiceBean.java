package br.com.voiza.rse.service;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Project;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Classe reponsável por obter informações do Redmine relacionados à Projetos.
 * @author aneto
 */
@Stateless
@LocalBean
public class ProjectServiceBean {
    
    @EJB
    private RedmineServiceBean redmineService;
    
    private static final Logger LOGGER = Logger.getLogger(ProjectServiceBean.class.getName());
    
    /**
     * Carrega os projetos relacionados ao usuário
     */
    public List<Project> loadProjects() {
        try {
            return redmineService.getRedmineManager().getProjects();
        } catch (RedmineException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
