package br.com.voiza.rse.service;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Project;
import java.util.Collections;
import java.util.Comparator;
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
            List<Project> list = redmineService.getRedmineManager().getProjects();
            ordenaListaProject(list);
            return list;
        } catch (RedmineException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Ordena a lista de Projects pelo nome
     * @param list 
     */
    private void ordenaListaProject(List<Project> list) {
        Collections.sort(list, new Comparator<Project>() {
            @Override
            public int compare(Project p1, Project p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });
    }    
    
}
