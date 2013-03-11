package br.com.voiza.rse.mbean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author aneto
 */
public class SuperMBean {

    public static final String SUCCESS = "success";
    public static final String LOGOUT = "logout";
    public static final String FAIL = "fail";
    
    public void addErrorMessage(String text) {
        FacesMessage message = new FacesMessage(text);    
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, message);   
    }
    
}
