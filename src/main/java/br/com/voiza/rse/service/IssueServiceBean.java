package br.com.voiza.rse.service;

import br.com.voiza.rse.dto.IssueDTO;
import br.com.voiza.rse.mbean.StoryCardMBean;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.CustomField;
import com.taskadapter.redmineapi.bean.Issue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
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
    
    private static final String ID_TRACKER_STORY = "5";
    private static final String ID_TRACKER_TECHNICAL_STORY = "4";
    
    private static final Logger LOGGER = Logger.getLogger(IssueServiceBean.class.getName());
    
    /**
     * Busca as issues de uma versão de um determinado projeto
     * 
     * @param projectId Projeto ao qual se deseja realizar a consulta
     * @param versionId Versão à qual se deseja realizar a consulta
     * @return List de IssueDTO contendo as issues localizadas
     */
    /**
     * Busca as issues de uma versão de um determinado projeto
     * 
     * @param projectId Projeto ao qual se deseja realizar a consulta
     * @param versionId Versão à qual se deseja realizar a consulta
     * @param trackers Lista de tipos para filtrar. Caso seja null, carregará Story e Technical Story
     * @return List de IssueDTO contendo as issues localizadas
     */
    /**
     * Busca as issues de uma versão de um determinado projeto
     * 
     * @param projectId Projeto ao qual se deseja realizar a consulta
     * @param versionId Versão à qual se deseja realizar a consulta
     * @param trackers Lista de tipos para filtrar. Caso seja null, carregará Story e Technical Story
     * @param issuesStatus Lista de status(situacao) para filtrar. Caso seja null, retorna todos os status
     * @return List de IssueDTO contendo as issues localizadas
     */
    public List<IssueDTO> loadIssues(Integer projectId, Integer versionId, List<String> trackers, List<String> issuesStatus) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("project_id", projectId.toString());            
            params.put("fixed_version_id", versionId.toString());

            List<Issue> list = new ArrayList<Issue>();

            if (trackers != null && trackers.size() > 0) {
                for (String trackerId : trackers) {
                    params.put("tracker_id", trackerId);

                    if (issuesStatus != null && issuesStatus.size() > 0) {
                        for (String issueStatusId : issuesStatus) {
                            params.put("status_id", issueStatusId);
                            list.addAll(redmineService.getRedmineManager().getIssueManager().getIssues(params));
                            //list.addAll(redmineService.getRedmineManager().getIssues(params));
                        }
                    } else {
                        params.put("status_id", "*");
                        list.addAll(redmineService.getRedmineManager().getIssueManager().getIssues(params));
                    }
                }
            } else if (issuesStatus != null && issuesStatus.size() > 0) {
                for (String issueStatusId : issuesStatus) {
                    params.put("status_id", issueStatusId);
                    params.put("tracker_id", ID_TRACKER_STORY);
                    list.addAll(redmineService.getRedmineManager().getIssueManager().getIssues(params));
                    params.put("tracker_id", ID_TRACKER_TECHNICAL_STORY);
                    list.addAll(redmineService.getRedmineManager().getIssueManager().getIssues(params));
                }
            } else {
                params.put("tracker_id", ID_TRACKER_STORY);
                list.addAll(redmineService.getRedmineManager().getIssueManager().getIssues(params));
                params.put("tracker_id", ID_TRACKER_TECHNICAL_STORY);
                list.addAll(redmineService.getRedmineManager().getIssueManager().getIssues(params));
            }

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
            issueDTO = assebleDTO(issue);
            listIssues.add(issueDTO);
        }
        
        return listIssues;
    }
    
    private IssueDTO assebleDTO(Issue issue) {
        IssueDTO issueDTO = new IssueDTO();
        issueDTO.setOriginal(issue);
        for (CustomField customField : issue.getCustomFields()) {
            try {
                // Remove os caracteres do nome do campo: espaço, ( e )
                String methodName = customField.getName().replace(" ", "").replace("(", "").replace(")", "");
                Method setMethod = issueDTO.getClass().getMethod("set" + methodName, new Class[]{String.class});
                setMethod.invoke(issueDTO, new Object[]{customField.getValue()});
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(StoryCardMBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(StoryCardMBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(StoryCardMBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(StoryCardMBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(StoryCardMBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return issueDTO;
    }
    
    /**
     * Conta a pontuação final das issues passadas, usando o atributo PointsRealizado
     * @param issues
     * @return A soma da pontuação de todas as issues
     */
    public double somaPontuacao(List<IssueDTO> issues) {
        double totalPoints = 0;
        for (IssueDTO issue : issues) {
            if(issue.getPointsSprintPlanning() != null && !issue.getPointsSprintPlanning().equals("?"))
                totalPoints += Double.parseDouble(issue.getPointsSprintPlanning());
        }
        return totalPoints;
    }
    
    /**
     * Conta a pontuação final das issues passadas, usando o atributo PointsRealizado
     * @param issues
     * @param dataInicial
     * @return A soma da pontuação de todas as issues
     */
    public Integer somaPontuacao(List<IssueDTO> issues, Date dataInicial) {
        Integer totalPoints = 0;
        for (IssueDTO issue : issues) {
            Date storyDate = issue.getOriginal().getCreatedOn();
            if (dataInicial.after(storyDate) || dataInicial.equals(storyDate)) {
                totalPoints += (issue.getPointsRealizado() != null ? issue.getPointsRealizado() : 0);
            }
        }
        return totalPoints;
    }    

    /**
     * Retorna uma issue a partir do seu Id.
     * 
     * @param parentId
     * @return 
     */
    private IssueDTO loadIssue(Integer parentId) {
        try {
            return assebleDTO(redmineService.getRedmineManager().getIssueManager().getIssueById(parentId));
        } catch (RedmineException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }
}