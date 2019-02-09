package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class JournalComptableTest {

    @Test
    public void toStringTest(){
        JournalComptable jc1 = new JournalComptable (	"AC",	"Achat"	);
        Assert.assertEquals("JournalComptable{code='AC', libelle='Achat'}", jc1.toString());
    }

}