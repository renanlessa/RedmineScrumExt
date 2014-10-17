package br.com.voiza.rse.service;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Version;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Classe reponsável por obter informações do Redmine relacionados à Versões.
 *
 * @author aneto
 */
@Stateless
@LocalBean
public class VersionServiceBean {

    @EJB
    private RedmineServiceBean redmineService;
    private static final Logger LOGGER = Logger.getLogger(VersionServiceBean.class.getName());
    private static final String STATUS_OPEN = "open";

    /**
     * Carrega as versões abertas do projeto informado no parametro projectId
     *
     * @param projectId Id do projeto que se deseja obter as versões
     * @return Lista com as versões com status aberto do projeto disponíveis para o usuário
     */
    public List<Version> loadVersions(Integer projectId) {
        try {
            List<Version> openVersions = new ArrayList<Version>();
            List<Version> versions = redmineService.getRedmineManager().getProjectManager().getVersions(projectId);
            for (Version version : versions) {
                if(version.getStatus().equals(STATUS_OPEN))
                    openVersions.add(version);
            }
            ordenaListaVersion(openVersions);
            return openVersions;
        } catch (RedmineException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Ordena a lista de Version pelo nome
     * @param list 
     */
    private void ordenaListaVersion(List<Version> list) {
        Collections.sort(list, new Comparator<Version>() {
            @Override
            public int compare(Version v1, Version v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
    }

    /**
     * Carrega uma determinada versão com base no seu identificador.
     * @param versionId
     * @return Version
     */
    public Version loadVersion(Integer versionId) {
        try {
            return redmineService.getRedmineManager().getProjectManager().getVersionById(versionId);
        } catch (RedmineException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }
}