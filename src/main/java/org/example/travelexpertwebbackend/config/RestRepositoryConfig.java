package org.example.travelexpertwebbackend.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Type;
import org.example.travelexpertwebbackend.entity.Agency;
import org.example.travelexpertwebbackend.entity.Agent;
import org.example.travelexpertwebbackend.entity.BookingDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestRepositoryConfig implements RepositoryRestConfigurer {
    @Autowired
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {
        Class[] classes = entityManager.getMetamodel()
                .getEntities().stream().map(Type::getJavaType).toArray(Class[]::new);
        config.exposeIdsFor(classes);
    }
}
