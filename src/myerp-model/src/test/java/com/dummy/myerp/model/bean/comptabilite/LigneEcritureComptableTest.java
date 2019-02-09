package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class LigneEcritureComptableTest {

    CompteComptable cc7 = new CompteComptable(706,	"Prestations de services"	);
    CompteComptable cc6 = new CompteComptable(606,	"Achats non stockés de matières et fournitures"	);

    LigneEcritureComptable lec = new LigneEcritureComptable(cc7, "ligne test", new BigDecimal(2), null);

    @Test
    public void getCompteComptable() {
        Assert.assertEquals(cc7, lec.getCompteComptable());
    }

    @Test
    public void setCompteComptable() {
        lec.setCompteComptable(cc6);
        Assert.assertEquals(cc6, lec.getCompteComptable());
    }

    @Test
    public void getLibelle() {
        Assert.assertEquals("ligne test", lec.getLibelle());
    }

    @Test
    public void setLibelle() {
        lec.setLibelle("contre test");
        Assert.assertEquals("contre test", lec.getLibelle());
    }

    @Test
    public void getDebit() {
    }

    @Test
    public void setDebit() {
    }

    @Test
    public void getCredit() {
    }

    @Test
    public void setCredit() {
    }

    @Test
    public void toStringTest() {
        Assert.assertEquals("LigneEcritureComptable{compteComptable=CompteComptable{numero=706, libelle='Prestations de services'}, libelle='ligne test', debit=2, credit=null}", lec.toString());

    }
}