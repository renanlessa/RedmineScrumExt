package br.com.voiza.rse.service;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Tracker;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Classe reponsável por obter informações do Redmine relacionados à Tipo de Ticket.
 * 
 * @author aneto
 */
@Stateless
@LocalBean
public class TrackerServiceBean {
    
    @EJB
    private RedmineServiceBean redmineService;
    private static final Logger LOGGER = Logger.getLogger(TrackerServiceBean.class.getName());

    /**
     * Retorna a lista de Trackers do Redmine
     * @return 
     */
    public List<Tracker> loadTrackers() {
        try {
            List<Tracker> trackers = redmineService.getRedmineManager().getIssueManager().getTrackers();
            ordenaListaTrackers(trackers);
            return trackers;
        } catch (RedmineException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Ordena a lista de Trackers pelo nome
     * @param list 
     */
    private void ordenaListaTrackers(List<Tracker> list) {
        Collections.sort(list, new Comparator<Tracker>() {
            @Override
            public int compare(Tracker t1, Tracker t2) {
                return t1.getName().compareTo(t2.getName());
            }
        });
    }
}