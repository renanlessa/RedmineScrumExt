package br.com.voiza.rse.service;

import br.com.voiza.rse.dto.IssueDTO;
import br.com.voiza.rse.mbean.StoryCardMBean;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.CustomField;
import com.taskadapter.redmineapi.bean.Issue;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Classe reponsável por obter informações do Redmine relacionados à Issues.
 * @author aneto
 */
@Stateless
@LocalBean
public class IssueServiceBean {
    
    @EJB
    private RedmineServiceBean redmineService;
    
    private static String ID_TRACKER_STORY = "5";
    private static String ID_TRACKER_TECHNICAL_STORY = "4";
    
    private static final Logger LOGGER = Logger.getLogger(IssueServiceBean.class.getName());
    
    /**
     * Busca as issues de uma versão de um determinado projeto
     * 
     * @param projectId Projeto ao qual se deseja realizar a consulta
     * @param versionId Versão à qual se deseja realizar a consulta
     * @return List de IssueDTO contendo as issues localizadas
     */
    public List<IssueDTO> loadIssues(Integer projectId, Integer versionId) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("project_id", projectId.toString());
            params.put("status_id", "*");
            params.put("fixed_version_id", versionId.toString());
            
            List<Issue> list = new ArrayList<Issue>();
            params.put("tracker_id", ID_TRACKER_STORY);
            // Primeiro busca todas as issues que são Story
            list.addAll(redmineService.getRedmineManager().getIssues(params));

            params.put("tracker_id", ID_TRACKER_TECHNICAL_STORY);
            // Agora busca todas as issues que são Technical Story
            list.addAll(redmineService.getRedmineManager().getIssues(params));
            
            List<IssueDTO> listIssues = montaListaIssueDTO(list);
            return listIssues;
        } catch (RedmineException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Popula uma lista de IssueDTO baseado em uma lista de Issue.
     * Esta ação é necessária para popular os campos personalizados do Redmine.
     * Este método utiliza Reflection para chamar os métodos "set" de IssueDTO, 
     * portanto para cada campo deve existir um "set" com mesmo nome
     * 
     * @param list Lista contendo a lista de Issue
     * @return Lista contendo a lista de IssueDTO
     */
    private List<IssueDTO> montaListaIssueDTO(List<Issue> list) {
        List<IssueDTO> listIssues = new ArrayList<IssueDTO>();
        IssueDTO issueDTO;

        for (Issue issue : list) {
            issueDTO = new IssueDTO();
            issueDTO.setOriginal(issue);
            for (CustomField customField : issue.getCustomFields()) {
                try {
                    // Remove os caracteres do nome do campo: espaço, ( e )
                    String methodName = customField.getName().replace(" ", "").replace("(", "").replace(")", "");
                    Method setMethod = issueDTO.getClass().getMethod("set" + methodName, new Class[]{String.class});
                    setMethod.invoke(issueDTO, new Object[]{customField.getValue()});
                } catch (Exception ex) {
                    Logger.getLogger(StoryCardMBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            listIssues.add(issueDTO);
        }
        
        return listIssues;
    }
    
    /**
     * Conta a pontuação final das issues passadas, usando o atributo PointsRealizado
     * @param issues
     * @return A soma da pontuação de todas as issues
     */
    public Integer somaPontuacao(List<IssueDTO> issues) {
        Integer totalPoints = 0;
        for (IssueDTO issue : issues) {
            totalPoints += (issue.getPointsRealizado() != null ? issue.getPointsRealizado() : 0);
        }
        return totalPoints;
    }
}
