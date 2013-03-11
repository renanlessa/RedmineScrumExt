package br.com.voiza.rse.service;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Version;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Classe reponsável por obter informações do Redmine relacionados à Versões.
 * @author aneto
 */
@Stateless
@LocalBean
public class VersionServiceBean {
    
    @EJB
    private RedmineServiceBean redmineService;
    
    private static final Logger LOGGER = Logger.getLogger(VersionServiceBean.class.getName());
    
    /**
     * Carrega as versões do projeto informado no parametro projectId
     * @param projectId Id do projeto que se deseja obter as versões
     * @return Lista com as versões do projeto disponíveis para o usuário
     */
    public List<Version> loadVersions(Integer projectId) {
        try {
            return redmineService.getRedmineManager().getVersions(projectId);
        } catch (RedmineException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Carrega uma determinada versão com base no seu identificador.
     * @param versionId
     * @return 
     */
    public Version loadVersion(Integer versionId) {
        try {
            return redmineService.getRedmineManager().getVersionById(versionId);
        } catch (RedmineException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        return null;
    }    
    
}
