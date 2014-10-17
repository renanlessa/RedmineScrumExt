package br.com.voiza.rse.service;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

/**
 * 
 * @author aneto
 */
@Singleton
@LocalBean
public class RedmineServiceBean {
    
    private static final String REDMINE_HOST_DEFAULT = "https://projetos.voiza.com.br";
    private RedmineManager redmineManager;

    private static final Logger LOGGER = Logger.getLogger(RedmineServiceBean.class.getName());

    public User connectWithRedmine(String username, String password, String redmineHost) throws Exception {
        LOGGER.log(Level.INFO, "Iniciando serviço de conexão com o Redmine");
        if (username != null && password != null) {

            redmineManager = RedmineManagerFactory.createWithUserAuth(redmineHost != null ? redmineHost : REDMINE_HOST_DEFAULT, username, password);
            //redmineManager = new RedmineManager(redmineHost != null ? redmineHost : REDMINE_HOST_DEFAULT, username, password);

            try {
                User user = redmineManager.getUserManager().getCurrentUser();
                LOGGER.log(Level.INFO, "Usuário autenticado com sucesso: {0}", user.getFullName());
                return user;
            } catch (RedmineException ex) {
                LOGGER.log(Level.SEVERE, "Não foi possível obter os dados do usuario. ERRO: {0}", ex.getMessage());
                throw new Exception("Não foi possível obter os dados do usuario.");
            }
        } else {
            LOGGER.log(Level.SEVERE, "Dados de acesso ao Redmine nao informados.");
            throw new Exception("Dados de acesso ao Redmine nao informados.");
        }
    }

    public RedmineManager getRedmineManager() {
        LOGGER.log(Level.INFO, "Realizando consulta no Redmine");
        return redmineManager;
    }    
}