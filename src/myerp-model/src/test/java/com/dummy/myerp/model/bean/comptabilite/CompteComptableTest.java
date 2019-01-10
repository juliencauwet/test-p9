package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CompteComptableTest {


    private List<CompteComptable> comptes = new ArrayList<>();

    CompteComptable cc1 = new CompteComptable(401,	"Fournisseurs"	);
    CompteComptable cc2 = new CompteComptable(411,	"Clients"	);
    CompteComptable cc3 = new CompteComptable(4456,"Taxes sur le chiffre d'affaires déductibles"	);
    CompteComptable cc4 = new CompteComptable(4457,"Taxes sur le chiffre d'affaires collectées par l'entreprise"	);
    CompteComptable cc5 = new CompteComptable(512,	"Banque"	);
    CompteComptable cc6 = new CompteComptable(606,	"Achats non stockés de matières et fournitures"	);
    CompteComptable cc7 = new CompteComptable(706,	"Prestations de services"	);

    @Before
    public void init(){


        comptes.add(cc1);
        comptes.add(cc2);
        comptes.add(cc3);
        comptes.add(cc4);
        comptes.add(cc5);
        comptes.add(cc6);
        comptes.add(cc7);
    }


    @Test
    public void getByNumero() {
        CompteComptable compte = CompteComptable.getByNumero(comptes, 401);

        Assert.assertEquals(compte, cc1);

    }

}