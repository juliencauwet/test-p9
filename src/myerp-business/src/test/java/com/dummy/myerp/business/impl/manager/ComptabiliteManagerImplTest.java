package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.DaoProxyImpl;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.model.bean.fixtures.Fixtures;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.technical.exception.TechnicalException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplTest {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    @Before
    public void setUp() {  MockitoAnnotations.initMocks(this); }

    @InjectMocks
    DaoProxyImpl daoProxy;

    @Mock
    ComptabiliteDaoImpl comptabiliteDao;


    CompteComptable cc1 = new CompteComptable(401,	"Fournisseurs"	);
    CompteComptable cc2 = new CompteComptable(411,	"Clients"	);
    CompteComptable cc3 = new CompteComptable(4456,"Taxes sur le chiffre d'affaires déductibles"	);
    CompteComptable cc4 = new CompteComptable(4457,"Taxes sur le chiffre d'affaires collectées par l'entreprise"	);
    CompteComptable cc5 = new CompteComptable(512,	"Banque"	);
    CompteComptable cc6 = new CompteComptable(606,	"Achats non stockés de matières et fournitures"	);
    CompteComptable cc7 = new CompteComptable(706,	"Prestations de services"	);
    CompteComptable cc8 = new CompteComptable(1);
    CompteComptable cc9 = new CompteComptable(2);

    LigneEcritureComptable lec1 = new LigneEcritureComptable (	cc1,"Cartouches d’imprimante", new BigDecimal(43.95),	null	);
    LigneEcritureComptable lec2 = new LigneEcritureComptable (	cc1,"TVA 20%",	new BigDecimal(8.79),	null	);
    LigneEcritureComptable lec3 = new LigneEcritureComptable (	cc2,"Facture F110001",	null,	new BigDecimal(52.74)	);
    LigneEcritureComptable lec4 = new LigneEcritureComptable (	cc2,"Facture C110002",	new BigDecimal(3000),	null	);
    LigneEcritureComptable lec5 = new LigneEcritureComptable (	cc4,"TMA Appli Xxx",	null,	new BigDecimal(2500)	);
    LigneEcritureComptable lec6 = new LigneEcritureComptable (	cc5,"TVA 20%",	null,	new BigDecimal(500)	);
    LigneEcritureComptable lec7 = new LigneEcritureComptable (	cc6,null,	new BigDecimal(52.74),	null	);
    LigneEcritureComptable lec8 = new LigneEcritureComptable (	cc7,null,	null,	new BigDecimal(52.74)	);
    LigneEcritureComptable lec9 = new LigneEcritureComptable (	cc1,"Facture C110004",	new BigDecimal(5700),	null	);
    LigneEcritureComptable lec10 = new LigneEcritureComptable (	cc3,"TMA Appli Xxx",	null,	new BigDecimal(4750)	);
    LigneEcritureComptable lec11 = new LigneEcritureComptable (	cc4,"TVA 20%",	null,	new BigDecimal(950)	);
    LigneEcritureComptable lec12 = new LigneEcritureComptable (	cc7,null,	new BigDecimal(3000),	null	);
    LigneEcritureComptable lec13 = new LigneEcritureComptable (	cc1,null,	null,	new BigDecimal(3000));
    LigneEcritureComptable lec14 = new LigneEcritureComptable(cc8, "lec test1", new BigDecimal(123), null);
    LigneEcritureComptable lec15 = new LigneEcritureComptable(cc9, "lec test2", null, new BigDecimal(1234));

    JournalComptable jc1 = new JournalComptable (	"AC",	"Achat"	);
    JournalComptable jc2 = new JournalComptable (	"VE",	"Vente"	);
    JournalComptable jc3 = new JournalComptable (	"BQ",	"Banque"	);
    JournalComptable jc4 = new JournalComptable (	"OD",	"Opérations Diverses"	);
    JournalComptable jc5 = new JournalComptable (	"TE",	"jc test"	);

    SequenceEcritureComptable sec1 = new SequenceEcritureComptable (2018,	40, "AC");
    SequenceEcritureComptable sec2 = new SequenceEcritureComptable (2017,	41, "VE");
    SequenceEcritureComptable sec3 = new SequenceEcritureComptable (2018,	51, ""	);
    SequenceEcritureComptable sec4 = new SequenceEcritureComptable (2018,	88, ""	);

    EcritureComptable ec1 = new EcritureComptable (	1,	jc1,	"AC-2016/00001",	new Date(),	"Cartouches d’imprimante"	);
    EcritureComptable ec2 = new EcritureComptable (	2,	jc2,	"BQ-2018/00002",	new Date(),	"TMA Appli Xxx");
    EcritureComptable ec3 = new EcritureComptable (	3,	jc3,	"BQ-2018/00003",	new Date(),	"Paiement Facture F110001");
    EcritureComptable ec4 = new EcritureComptable (	4,	jc2,	"VE-2018/00004",	new Date(),	"TMA Appli Yyy");
    EcritureComptable ec5 = new EcritureComptable (	5,	jc4,	"OD-2018/00005",	new Date(),	"Paiement Facture C110002");
    EcritureComptable ec6 = new EcritureComptable ( 6,  jc5,    "TE-2018/00006",   new Date(), "ec test");


    @Test
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(1234)));
        manager.RG_2_Equilibree(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        manager.RG_3_auMoins2Lignes(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void RG_5_formatNonCorrect_AnneeRefIncoorecte() throws FunctionalException {

        EcritureComptable ec1 = new EcritureComptable (	1,	jc1,	"AC-2016/00001",	new Date(),	"Cartouches d’imprimante"	);

        manager.RG_5_formatCorrect(ec1);

    }

    @Test(expected = FunctionalException.class)
    public void RG_5_formatNonCorrect_CodeJournaIncorrect() throws FunctionalException {

        EcritureComptable ec2 = new EcritureComptable (	2,	jc2,	"BQ-2018/00002",	new Date(),	"TMA Appli Xxx");

        manager.RG_5_formatCorrect(ec2);
    }

    @Test
    public void RG_5_formatCorrect_ec3() throws FunctionalException {

        EcritureComptable ec3 = new EcritureComptable (	3,	jc3,	"BQ-2018/00003",	new Date(),	"Paiement Facture F110001");

        manager.RG_5_formatCorrect(ec3);
    }

    @Test
    public void RG_5_formatCorrect_ec4() throws FunctionalException {

        EcritureComptable ec4 = new EcritureComptable (	4,	jc2,	"VE-2018/00004",	new Date(),	"TMA Appli Yyy");
        manager.RG_5_formatCorrect(ec4);
    }

    @Test
    public void RG_5_formatCorrect_ec5() throws FunctionalException {

        manager.RG_5_formatCorrect(ec5);
    }


    @Test
    public void getListCompteComptable() {


        Mockito.when(comptabiliteDao.getListCompteComptable()).thenReturn(new ArrayList<>(Arrays.asList(cc1, cc2, cc3)));

        List<CompteComptable> list = comptabiliteDao.getListCompteComptable();

        Assert.assertEquals(3, list.size());

    }

    @Test
    public void addReferenceTestEc1() throws TechnicalException, NotFoundException, FunctionalException {

        EcritureComptable ec = new EcritureComptable();

        Mockito.when(comptabiliteDao.getEcritureComptable(ec.getId())).thenReturn(ec1);


        ec.setDate(new Date());
        ec.setLibelle("test");
        ec.setJournal(jc2);

        manager.addReference(ec);
        Assert.assertEquals("AC-2016/00001", ec.getReference());

    }
}
