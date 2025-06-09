package fr.uha.ensisa.gl.chatest.controller.Tests;

import fr.uha.ensisa.gl.chatest.controller.HomeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    public void setup() {
        homeController = new HomeController();
    }

    @Test
    public void testHomePage() {
        assertEquals("redirect:/test/list", homeController.home());
    }
}
