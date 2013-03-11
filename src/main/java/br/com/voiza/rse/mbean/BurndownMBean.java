package br.com.voiza.rse.mbean;

import br.com.voiza.rse.dto.IssueDTO;
import br.com.voiza.rse.service.IssueServiceBean;
import br.com.voiza.rse.service.ProjectServiceBean;
import br.com.voiza.rse.service.VersionServiceBean;
import br.com.voiza.rse.util.DateUtil;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Version;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author aneto
 */
@ManagedBean
@ViewScoped
public class BurndownMBean {
    
    @EJB
    private ProjectServiceBean projectServiceBean;
    
    @EJB
    private VersionServiceBean versionServiceBean;
    
    @EJB
    private IssueServiceBean issueServiceBean;
    
    private List<Project> listProjects = new ArrayList<Project>();
    
    private List<Version> listVersions = new ArrayList<Version>();
    
    private Integer projectId;
    
    private Integer versionId;    
    
    private CartesianChartModel burnDownChartModel; 
    
    private boolean renderChart = false;
    
    private Date dataInicial;
    
    private Date dataFinal;
    
    private static final Logger LOGGER = Logger.getLogger(BurndownMBean.class.getName());
    
    @PostConstruct
    public void init() {
        loadProjects();
    }    
    
    /**
     * Carrega os projetos para a lista listProjects
     */
    private void loadProjects() {
        listProjects = projectServiceBean.loadProjects();
    }
    
    /**
     * Carrega as versões relacionadas ao projeto
     */
    public void loadVersions() {
        listVersions = versionServiceBean.loadVersions(projectId);
    }
    
    public void generateBurndownChart() {
        List<IssueDTO> listIssues = issueServiceBean.loadIssues(projectId, versionId);
        Integer totalPoints = issueServiceBean.somaPontuacao(listIssues);
        // TODO Corrigir o método que obtém a diferença em dias para não considerar os finais de semana
        Integer numeroDias = DateUtil.differenceInDays(dataInicial, dataFinal);
        Double mediaEstimada = (totalPoints.doubleValue() / (numeroDias - 2)); 
        Double previsto = totalPoints.doubleValue();
        Integer realizado = totalPoints;
        
        burnDownChartModel = new CartesianChartModel();  
        
        LineChartSeries seriePrevisto = new LineChartSeries();  
        seriePrevisto.setLabel("Previsto");
        
        LineChartSeries serieRealizado = new LineChartSeries();  
        serieRealizado.setLabel("Realizado");  
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        Calendar data = Calendar.getInstance();
        data.setTime(dataInicial);
        do {
            if (!DateUtil.isWeekend(data)) {
                seriePrevisto.set(sdf.format(data.getTime()), previsto);
                previsto -= mediaEstimada;
                
                boolean realizacaoNoDia = false;
                for (IssueDTO issueDTO : listIssues) {
                    if (data.getTime().equals(issueDTO.getOriginal().getDueDate())) {
                        realizado -= issueDTO.getPointsRealizado();
                        realizacaoNoDia = true;
                    }
                }
                serieRealizado.set(sdf.format(data.getTime()), realizacaoNoDia || realizado == totalPoints ? realizado : null);
            }
            data.add(Calendar.DAY_OF_MONTH, 1);
        } while(!data.getTime().after(dataFinal));
        
        burnDownChartModel.addSeries(seriePrevisto);  
        burnDownChartModel.addSeries(serieRealizado);
        
        renderChart = true;
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

    public CartesianChartModel getBurnDownChartModel() {
        return burnDownChartModel;
    }

    public void setBurnDownChartModel(CartesianChartModel burnDownChartModel) {
        this.burnDownChartModel = burnDownChartModel;
    }

    public boolean isRenderChart() {
        return renderChart;
    }

    public void setRenderChart(boolean renderChart) {
        this.renderChart = renderChart;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }
    
}
