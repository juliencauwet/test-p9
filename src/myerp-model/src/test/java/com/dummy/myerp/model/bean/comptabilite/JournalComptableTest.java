package com.dummy.myerp.model.bean.comptabilite;

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JournalComptableTest {

    @Test
    public void toStringTest(){
        JournalComptable jc1 = new JournalComptable (	"AC",	"Achat"	);
        Assert.assertEquals("JournalComptable{code='AC', libelle='Achat'}", jc1.toString());
    }

    @Test
    public void getByCode(){


        List<JournalComptable> list = Arrays.asList(
                new JournalComptable (	"AC",	"Achat"	),
                new JournalComptable (	"VE",	"Vente"	),
                new JournalComptable (	"BQ",	"Banque")
        );

        JournalComptable jc = JournalComptable.getByCode(list, "BQ");

        Assert.assertThat(jc.getLibelle(), is("Banque"));
    }

}