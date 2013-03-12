package br.com.voiza.rse.mbean;

import br.com.voiza.rse.dto.IssueDTO;
import br.com.voiza.rse.service.IssueServiceBean;
import br.com.voiza.rse.service.ProjectServiceBean;
import br.com.voiza.rse.service.VersionServiceBean;
import br.com.voiza.rse.util.DateUtil;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Version;
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
    
    private Integer dias;
    
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
    
    /**
     * Popula o modelo usado para geração do gráfico burndown
     */
    public void generateBurndownChart() {
        List<IssueDTO> listIssues = issueServiceBean.loadIssues(projectId, versionId);
        Integer totalPoints = issueServiceBean.somaPontuacao(listIssues);
        Double mediaEstimada = (totalPoints.doubleValue() / dias);
        Double previsto = totalPoints.doubleValue();
        Integer realizado = totalPoints;
        
        burnDownChartModel = new CartesianChartModel();  
        
        LineChartSeries seriePrevisto = new LineChartSeries();  
        seriePrevisto.setLabel("Previsto");
        
        LineChartSeries serieRealizado = new LineChartSeries();  
        serieRealizado.setLabel("Realizado");  

        Calendar data = Calendar.getInstance();
        data.setTime(dataInicial);
        Integer iteracoes = 0;
        do {
            if (!DateUtil.isBusinessDay(data)) {
                String diaMes = DateUtil.DIA_MES.format(data.getTime());
                
                if (iteracoes > dias) {
                    previsto = 0d;
                }
                seriePrevisto.set(diaMes, previsto);
                previsto -= mediaEstimada;
                
                boolean realizadoNoDia = false;
                for (IssueDTO issueDTO : listIssues) {
                    if (data.getTime().equals(issueDTO.getOriginal().getDueDate())) {
                        realizado -= issueDTO.getPointsRealizado();
                        realizadoNoDia = true;
                    }
                }
                serieRealizado.set(diaMes, (realizadoNoDia || realizado == totalPoints) ? realizado : null);
                
                iteracoes++;
            }
            data.add(Calendar.DAY_OF_MONTH, 1);
        } while(!data.getTime().after(dataFinal));
        
        burnDownChartModel.addSeries(seriePrevisto);  
        burnDownChartModel.addSeries(serieRealizado);
        
        renderChart = true;
    }
    
    public void calculaQuantidadeDias() {
        if (dataInicial != null && dataFinal != null) {
            dias = DateUtil.businessDaysInPeriod(dataInicial, dataFinal);
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

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }
    
}
