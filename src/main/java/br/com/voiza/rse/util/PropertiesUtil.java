package br.com.voiza.rse.util;

import br.com.voiza.rse.dto.PropertiesDTO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe Utilitária para acessar o arquivo de propriedades do sistema.
 * @author aneto
 */
public class PropertiesUtil {
 
    private static final String ARQUIVO = "application.properties";
    
    private static final Logger LOGGER = Logger.getLogger(PropertiesUtil.class.getName());
    
    /**
     * Carrega as informações do arquivo de properties para um DTO
     * @return 
     */
    public static PropertiesDTO loadPropertiesData() {
        PropertiesDTO propertiesDTO = new PropertiesDTO();
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(ARQUIVO);
            if(input != null){

                Properties properties = new Properties();
                properties.load(input);
                propertiesDTO.setApplicationVersion(properties.getProperty("version"));
                propertiesDTO.setIdDone(Integer.parseInt(properties.getProperty("done.id")));
                input.close();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Não foi possível realizar a leitura do arquivo properties.", ex);
        }

        return propertiesDTO;
    }
    
}
