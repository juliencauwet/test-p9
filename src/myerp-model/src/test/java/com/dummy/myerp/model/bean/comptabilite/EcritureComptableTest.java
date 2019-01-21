package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;


public class EcritureComptableTest {

    List<CompteComptable> comptes = Arrays.asList(
            new CompteComptable(401,	"Fournisseurs"),
            new CompteComptable(411,	"Clients"	),
            new CompteComptable(4456,"Taxes sur le chiffre d'affaires déductibles"	),
            new CompteComptable(4457,"Taxes sur le chiffre d'affaires collectées par  l'entreprise"	),
            new CompteComptable(512,	"Banque"	),
            new CompteComptable(606,	"Achats non stockés de matières et fournitures"	),
            new CompteComptable(706,	"Prestations de services"	),
            new CompteComptable(1),
            new CompteComptable(2)
    );

    LigneEcritureComptable lec1 = new LigneEcritureComptable (	comptes.get(1),"Cartouches d’imprimante", new BigDecimal(43.95),	null	);
    LigneEcritureComptable lec2 = new LigneEcritureComptable (	comptes.get(1),"TVA 20%",	new BigDecimal(8.79),	null	);
    LigneEcritureComptable lec3 = new LigneEcritureComptable (	comptes.get(2),"Facture F110001",	null,	new BigDecimal(52.74)	);
    LigneEcritureComptable lec4 = new LigneEcritureComptable (	comptes.get(2),"Facture C110002",	new BigDecimal(3000),	null	);
    LigneEcritureComptable lec5 = new LigneEcritureComptable (	comptes.get(4),"TMA Appli Xxx",	null,	new BigDecimal(2500)	);
    LigneEcritureComptable lec6 = new LigneEcritureComptable (	comptes.get(5),"TVA 20%",	null,	new BigDecimal(500)	);
    LigneEcritureComptable lec7 = new LigneEcritureComptable (	comptes.get(6),null,	new BigDecimal(52.74),	null	);


    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }

      @Test public void isEquilibree() {
            EcritureComptable vEcriture;
            vEcriture = new EcritureComptable();

            vEcriture.setLibelle("Equilibrée");
            vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
            vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
            vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
            vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
            Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

            vEcriture.getListLigneEcriture().clear();
            vEcriture.setLibelle("Non équilibrée");
            vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
            vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
            vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
            vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
            Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
          }


          //TODO: getTotalDebit: pas le même résultat à 0,000000001
  //  @Test
  // public void getTotalDebitTest() {
  //     EcritureComptable ecritureComptable = new EcritureComptable();

  //     ecritureComptable.getListLigneEcriture().add(lec1);
  //     ecritureComptable.getListLigneEcriture().add(lec2);
  //     ecritureComptable.getListLigneEcriture().add(lec3);
  //     ecritureComptable.getListLigneEcriture().add(lec4);
  //     ecritureComptable.getListLigneEcriture().add(lec5);

  //     Assert.assertEquals(new BigDecimal(3052.74), ecritureComptable.getTotalDebit());

  // }

    @Test
    public void getTotalCreditTest() {


    }
}
