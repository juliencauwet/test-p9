package com.dummy.myerp.testbusiness.business;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;


/**
 * Classe de test de l'initialisation du contexte Spring
 */
public class TestInitSpring extends BusinessTestCase {


    Logger logger = LoggerFactory.getLogger(this.getClass());

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
        SpringRegistry.init();
        //SpringRegistry.getDaoProxy();
        assertNotNull(SpringRegistry.getBusinessProxy());
        assertNotNull(SpringRegistry.getTransactionManager());
    }
}
