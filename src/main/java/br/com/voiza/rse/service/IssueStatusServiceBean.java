/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.voiza.rse.service;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.IssueStatus;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Classe reponsável por obter informações do Redmine relacionados ao status do Ticket.
 * 
 * @author rlessa
 */
@Stateless
@LocalBean
public class IssueStatusServiceBean {

    @EJB
    private RedmineServiceBean redmineService;
    private static final Logger LOGGER = Logger.getLogger(IssueStatusServiceBean.class.getName());

    /**
     * Retorna a lista de Status do Redmine
     * @return 
     */
    public List<IssueStatus> loadIssueStatus() {
        try {
            List<IssueStatus> status = redmineService.getRedmineManager().getStatuses();
            ordenaListaIssueStatus(status);
            return status;
        } catch (RedmineException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Ordena a lista de Status pelo nome
     * @param list 
     */
    private void ordenaListaIssueStatus(List<IssueStatus> list) {
        Collections.sort(list, new Comparator<IssueStatus>() {
            @Override
            public int compare(IssueStatus t1, IssueStatus t2) {
                return t1.getName().compareTo(t2.getName());
            }
        });
    }
}