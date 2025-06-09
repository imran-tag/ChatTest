package fr.uha.ensisa.gl.chatest.config;

import fr.uha.ensisa.gl.chatest.dao.chatest.DaoFactoryMem;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceChainRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MvcConfigurationTest {
    @Mock
    private ApplicationContext applicationContext;

    private MvcConfiguration mvcConfiguration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mvcConfiguration = new MvcConfiguration();
        mvcConfiguration.applicationContext = applicationContext; // Inject mocked ApplicationContext
    }

    @Test
    public void testTemplateResolver() {
        // Act
        SpringResourceTemplateResolver resolver = mvcConfiguration.templateResolver();

        // Assert
        assertNotNull(resolver);
        assertEquals("/WEB-INF/views/", resolver.getPrefix());
        assertEquals(".html", resolver.getSuffix());
    }

    @Test
    public void testSpringTemplateEngine() {
        // Arrange
        SpringResourceTemplateResolver mockResolver = mock(SpringResourceTemplateResolver.class);
        when(applicationContext.getBean(SpringResourceTemplateResolver.class)).thenReturn(mockResolver);

        // Act
        SpringTemplateEngine engine = mvcConfiguration.springTemplateEngine();

        // Assert
        assertNotNull(engine);
    }

    @Test
    public void testViewResolver() {
        // Arrange
        SpringTemplateEngine mockEngine = mock(SpringTemplateEngine.class);
        when(applicationContext.getBean(SpringTemplateEngine.class)).thenReturn(mockEngine);

        // Act
        ViewResolver resolver = mvcConfiguration.viewResolver();

        // Assert
        assertNotNull(resolver);
    }

    @Test
    public void testMultipartResolver() {
        // Act
        MultipartResolver resolver = mvcConfiguration.multipartResolver();

        // Assert
        assertNotNull(resolver);
    }

    @Test
    public void testAddResourceHandlers() {
        // Mock dependencies
        ResourceHandlerRegistry registry = mock(ResourceHandlerRegistry.class);
        ResourceHandlerRegistration resourcesRegistration = mock(ResourceHandlerRegistration.class);
        ResourceHandlerRegistration libsRegistration = mock(ResourceHandlerRegistration.class);
        ResourceChainRegistration chainRegistration = mock(ResourceChainRegistration.class);

        // Stub methods
        when(registry.addResourceHandler("/resources/**")).thenReturn(resourcesRegistration);
        when(registry.addResourceHandler("/libs/**")).thenReturn(libsRegistration);
        when(resourcesRegistration.addResourceLocations("/resources/")).thenReturn(resourcesRegistration);
        when(resourcesRegistration.setCachePeriod(0)).thenReturn(resourcesRegistration);
        when(libsRegistration.addResourceLocations("/libs/bootstrap/")).thenReturn(libsRegistration);
        when(libsRegistration.setCachePeriod((int) TimeUnit.DAYS.toSeconds(365))).thenReturn(libsRegistration);
        when(libsRegistration.resourceChain(true)).thenReturn(chainRegistration);
        when(chainRegistration.addResolver(any(EncodedResourceResolver.class))).thenReturn(chainRegistration); // Fix: Return self
        when(chainRegistration.addResolver(any(PathResourceResolver.class))).thenReturn(chainRegistration); // Fix: Return self

        // Act
        mvcConfiguration.addResourceHandlers(registry);

        // Verify calls for "/resources/**"
        verify(registry).addResourceHandler("/resources/**");
        verify(resourcesRegistration).addResourceLocations("/resources/");
        verify(resourcesRegistration).setCachePeriod(0);

        // Verify calls for "/libs/**"
        verify(registry).addResourceHandler("/libs/**");
        verify(libsRegistration).addResourceLocations("/libs/bootstrap/");
        verify(libsRegistration).setCachePeriod((int) TimeUnit.DAYS.toSeconds(365));
        verify(chainRegistration).addResolver(any(EncodedResourceResolver.class));
        verify(chainRegistration).addResolver(any(PathResourceResolver.class));
    }

    @Test
    public void testDaoFactory() {
        // Act
        IDaoFactory daoFactory = mvcConfiguration.daoFactory();

        // Assert
        assertNotNull(daoFactory);
        assertTrue(daoFactory instanceof DaoFactoryMem);
    }
}
