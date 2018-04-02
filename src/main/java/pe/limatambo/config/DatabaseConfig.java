package pe.limatambo.config;

import java.beans.PropertyVetoException;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 *
 * @author dev-out-04
 */
@Configuration
@EnableTransactionManagement
//@PropertySource(value = {"file:src/main/resources/application.properties"})
public class DatabaseConfig {
    
    @Autowired
    private Environment environment;
    @Autowired
    DataSource dataSource;
    private final Logger loggerConfig = LoggerFactory.getLogger(getClass());
    
    @Bean
    public LocalSessionFactoryBean sessionFactory() throws PropertyVetoException{
        try {
            LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
            sessionFactory.setDataSource(dataSource);
            sessionFactory.setPackagesToScan(environment.getRequiredProperty("entitymanager.packagesToScan"));//(new String[]{"pe.cajapaita.backerp.contabilidad.entidad"});
            return sessionFactory;
        } catch (IllegalStateException e) {
            loggerConfig.error(e.getMessage());
            throw e;
        }
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    } 

}
