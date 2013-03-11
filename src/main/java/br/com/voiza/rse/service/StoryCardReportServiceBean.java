package br.com.voiza.rse.service;

import br.com.voiza.rse.dto.IssueDTO;
import br.com.voiza.rse.mbean.StoryCardMBean;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author aneto
 */
@Stateless
@LocalBean
public class StoryCardReportServiceBean {

    private static final String F_STORY_TYPE = "storyType";
    private static final String F_STORY_ID = "storyId";
    private static final String F_TITLE = "title";
    private static final String F_ESTIMATED_POINTS = "estimatedPoints";
    private static final String F_BUSINESS_VALUE = "businessValue";
    private static final String F_STORY_SIZE = "storySize";
    
    public byte[] buildReport(List<IssueDTO> selectedIssues) {
        try {
            ClassLoader classLoader = StoryCardMBean.class.getClassLoader();
            InputStream jasperFile = classLoader.getResourceAsStream("br/com/voiza/rse/report/cartoes.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
            Collection<Map<String, ?>> dataSourceRoot = new ArrayList<Map<String, ?>>();

            for (IssueDTO issue : selectedIssues) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put(F_STORY_TYPE, issue.getOriginal().getTracker().getName());
                item.put(F_STORY_ID, issue.getOriginal().getId());
                item.put(F_TITLE, issue.getOriginal().getSubject());
                item.put(F_ESTIMATED_POINTS, issue.getPointsRealizado());
                item.put(F_BUSINESS_VALUE, issue.getBusinessValue());
                item.put(F_STORY_SIZE, issue.getStorySize());
                dataSourceRoot.add(item);
            }

            JRDataSource reportDataSource = new JRMapCollectionDataSource(dataSourceRoot);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<String, Object>(), reportDataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException ex) {
            Logger.getLogger(StoryCardReportServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
