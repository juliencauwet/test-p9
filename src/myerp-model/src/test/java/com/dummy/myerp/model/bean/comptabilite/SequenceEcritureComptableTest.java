package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SequenceEcritureComptableTest {

    SequenceEcritureComptable sec = new SequenceEcritureComptable();

    @Test
    public void setAnnee() {
        sec.setAnnee(2019);
        Assert.assertTrue(2019 == sec.getAnnee());
    }

    @Test
    public void setDerniereValeur() {
        sec.setDerniereValeur(12);
        Assert.assertTrue(12 == sec.getDerniereValeur());
    }

    @Test
    public void setCode() {
        sec.setCode("TE");
        Assert.assertEquals("TE", sec.getCode());
    }

    @Test
    public void toStringTest() {

        Assert.assertEquals("SequenceEcritureComptable{annee=null, derniereValeur=null}", sec.toString());

    }
}