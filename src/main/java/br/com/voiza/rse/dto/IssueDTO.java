package br.com.voiza.rse.dto;

import com.taskadapter.redmineapi.bean.Issue;

/**
 * DTO para transformar uma Issue vinda do Redmine em uma Issue com os campos personalizados da Voiza.
 * 
 * @author aneto
 */
public class IssueDTO {
    
    private Issue original;
    private Integer businessValue;
    private String storySize;
    private Integer pointsEstimado;
    private Integer pointsRealizado;
    private String issue;
    private boolean unplanned;
    private String requirements;

    public Issue getOriginal() {
        return original;
    }

    public void setOriginal(Issue original) {
        this.original = original;
    }

    public void setBusinessValue(String businessValue) {
        if (businessValue != null) {
            this.businessValue = Integer.parseInt(businessValue);
        }
    }

    public void setPointsestimado(String pointsEstimado) {
        if (pointsEstimado != null && !pointsEstimado.equals("?")) {
            this.pointsEstimado = Integer.parseInt(pointsEstimado);
        }
    }

    public void setPointsrealizado(String pointsRealizado) {
        if (pointsRealizado != null && !pointsRealizado.equals("?")) {
            this.pointsRealizado = Integer.parseInt(pointsRealizado);
        }
    }

    public void setUnplanned(String unplanned) {
        if (unplanned != null) {
            this.unplanned = unplanned.equals("1");
        }
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Integer getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(Integer businessValue) {
        this.businessValue = businessValue;
    }

    public String getStorySize() {
        return storySize;
    }

    public void setStorySize(String storySize) {
        this.storySize = storySize;
    }

    public Integer getPointsEstimado() {
        return pointsEstimado;
    }

    public void setPointsEstimado(Integer pointsEstimado) {
        this.pointsEstimado = pointsEstimado;
    }

    public Integer getPointsRealizado() {
        return pointsRealizado;
    }

    public void setPointsRealizado(Integer pointsRealizado) {
        this.pointsRealizado = pointsRealizado;
    }

    public boolean isUnplanned() {
        return unplanned;
    }

    public void setUnplanned(boolean unplanned) {
        this.unplanned = unplanned;
    }

    @Override
    public String toString() {
        return "IssueDTO{" + "original=" + original.getId() 
                + ", businessValue=" + businessValue 
                + ", storySize=" + storySize 
                + ", pointsRealizado=" + pointsRealizado + '}';
    }
    
}
