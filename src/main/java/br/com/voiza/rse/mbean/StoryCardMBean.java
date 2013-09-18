package br.com.voiza.rse.mbean;

import br.com.voiza.rse.dto.IssueDTO;
import br.com.voiza.rse.service.IssueServiceBean;
import br.com.voiza.rse.service.IssueStatusServiceBean;
import br.com.voiza.rse.service.ProjectServiceBean;
import br.com.voiza.rse.service.StoryCardReportServiceBean;
import br.com.voiza.rse.service.TrackerServiceBean;
import br.com.voiza.rse.service.VersionServiceBean;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.Version;
import com.taskadapter.redmineapi.bean.IssueStatus;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aneto
 */
@ManagedBean
@ViewScoped
public class StoryCardMBean extends SuperMBean implements Serializable {

    @EJB
    private IssueServiceBean issueServiceBean;

    @EJB
    private ProjectServiceBean projectServiceBean;

    @EJB
    private VersionServiceBean versionServiceBean;

    @EJB
    private TrackerServiceBean trackerServiceBean;

    @EJB
    private IssueStatusServiceBean issueStatusServiceBean;

    @EJB
    private StoryCardReportServiceBean storyCardReportServiceBean;

    private List<Project> listProjects = new ArrayList<Project>();

    private List<Tracker> listTrackers = new ArrayList<Tracker>();

    private List<Version> listVersions = new ArrayList<Version>();

    private List<IssueDTO> listIssues = new ArrayList<IssueDTO>();

    private List<IssueDTO> selectedIssues = new ArrayList<IssueDTO>();

    private List<String> selectedTrackers = new ArrayList<String>();

    private List<IssueStatus> listIssueStatus = new ArrayList<IssueStatus>();
    
    private List<String> selectedIssueStatus = new ArrayList<String>();

    private Integer projectId;
    
    private Integer versionId;
    
    private Integer totalPoints;
    
    private boolean renderListagem;
    
    private static final Logger LOGGER = Logger.getLogger(StoryCardMBean.class.getName());

    @PostConstruct
    public void init() {
        loadProjects();
        loadTrackers();
        loadIssueStatus();
    }
    
    /**
     * Carrega os projetos para a lista listProjects
     */
    private void loadProjects() {
        listProjects = projectServiceBean.loadProjects();
    }
    
    /**
     * Carrega os tracker para a lista listTrackers
     */
    private void loadTrackers() {
        listTrackers = trackerServiceBean.loadTrackers();
    }
    
    /**
     * Carrega os status para a lista listIssueStatus
     */
    private void loadIssueStatus() {
        listIssueStatus = issueStatusServiceBean.loadIssueStatus();
    }
    
    /**
     * Carrega as versões relacionadas ao projeto
     */
    public void loadVersions() {
        listVersions = versionServiceBean.loadVersions(projectId);
    }
    
    /**
     * Carrega as Issues de Story e Technical Story para projeto e versão
     */
    public void loadIssues() {
        listIssues.clear();
        listIssues = issueServiceBean.loadIssues(projectId, versionId, selectedTrackers, selectedIssueStatus);
        totalPoints = issueServiceBean.somaPontuacao(listIssues);
        renderListagem = !listIssues.isEmpty();
        if (!renderListagem) {
            addErrorMessage("Nenhum registro encontrado.");
        }
    }
    
    /**
     * Realiza a impressão do PDF contendo os cartões.
     */
    public void printPDF() {
        byte[] pdf = storyCardReportServiceBean.buildReport(selectedIssues);
        try {
            StringBuilder nomeRel = new StringBuilder("Cartoes.pdf");

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            ec.responseReset();
            ec.setResponseContentType("application/pdf");
            ec.setResponseContentLength(pdf.length);
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + nomeRel.toString() + "\"");

            OutputStream output = ec.getResponseOutputStream();
            output.write(pdf);
            output.flush();
            output.close();

            fc.responseComplete(); 
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }    

    public List<Project> getListProjects() {
        return listProjects;
    }

    public void setListProjects(List<Project> listProjects) {
        this.listProjects = listProjects;
    }

    public List<Version> getListVersions() {
        return listVersions;
    }

    public void setListVersions(List<Version> listVersions) {
        this.listVersions = listVersions;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public List<IssueDTO> getListIssues() {
        return listIssues;
    }

    public void setListIssues(List<IssueDTO> listIssues) {
        this.listIssues = listIssues;
    }

    public List<IssueDTO> getSelectedIssues() {
        return selectedIssues;
    }

    public void setSelectedIssues(List<IssueDTO> selectedIssues) {
        this.selectedIssues = selectedIssues;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public boolean isRenderListagem() {
        return renderListagem;
    }

    public void setRenderListagem(boolean renderListagem) {
        this.renderListagem = renderListagem;
    }

    public List<Tracker> getListTrackers() {
        return listTrackers;
    }

    public void setListTrackers(List<Tracker> listTrackers) {
        this.listTrackers = listTrackers;
    }

    public List<String> getSelectedTrackers() {
        return selectedTrackers;
    }

    public void setSelectedTrackers(List<String> selectedTrackers) {
        this.selectedTrackers = selectedTrackers;
    }

    public List<IssueStatus> getListIssueStatus() {
        return listIssueStatus;
    }

    public void setListIssueStatus(List<IssueStatus> listIssueStatus) {
        this.listIssueStatus = listIssueStatus;
    }
    
    public List<String> getSelectedIssueStatus() {
        return selectedIssueStatus;
    }

    public void setSelectedIssueStatus(List<String> selectedIssueStatus) {
        this.selectedIssueStatus = selectedIssueStatus;
    }
}