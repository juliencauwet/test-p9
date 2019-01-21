package com.dummy.myerp.testbusiness.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;


/**
 * Classe de test de l'initialisation du contexte Spring
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestInitSpring extends BusinessTestCase {

    /**
     * Constructeur.
     */
    public TestInitSpring() {
        super();
    }


    /**
     * Teste l'initialisation du contexte Spring
     */
    //TODO: réaliser ce test
    @Test
    public void testInit() {
   //     SpringRegistry.init();
   //     assertNotNull(SpringRegistry.getBusinessProxy());
   //     assertNotNull(SpringRegistry.getTransactionManager());
    }
}
