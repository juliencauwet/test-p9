package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ComptabiliteDaoImplTest {

    ComptabiliteDaoImpl comptabiliteDao = new ComptabiliteDaoImpl();

    @Mock
    CompteComptable compteComptable;

    @Mock
    EcritureComptable ecritureComptable1;

    @Mock
    EcritureComptable ecritureComptable2;

    @Mock
    JournalComptable journalComptable;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    @Autowired
    ApplicationContext applicationContext;
    Map<DataSourcesEnum, DataSource> mds = new HashMap<>();

    @Before
    public void beforeTest(){
        DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
        mds.put(DataSourcesEnum.MYERP, dataSource);
        AbstractDbConsumer.configure(mds);
    }

    @Test
    public void checkListCompteComptableSize() {
        List<CompteComptable> ccs = comptabiliteDao.getListCompteComptable();
        Assert.assertEquals(7, ccs.size());
    }

    @Test
    public void getListJournalComptableTest() {
        List<JournalComptable> jcs = comptabiliteDao.getListJournalComptable();
        Assert.assertEquals(4, jcs.size());
    }

    @Test
    public void getListEcritureComptableTest() {
        List<EcritureComptable> ecs = comptabiliteDao.getListEcritureComptable();
        Assert.assertEquals(5, ecs.size());
    }

    @Test
    public void getEcritureComptableTest() throws NotFoundException, ParseException {

        EcritureComptable ec = comptabiliteDao.getEcritureComptable(-2);
        Date d = ec.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ds = sdf.format(d);
        ec.setDate(sdf.parse(ds));

        CompteComptable cc2 = new CompteComptable(411,	"Clients"	);
        CompteComptable cc4 = new CompteComptable(706,"Prestations de services"	);
        CompteComptable cc5 = new CompteComptable(4457,	"Taxes sur le chiffre d'affaires collectées par l'entreprise"	);

        EcritureComptable ec2 = new EcritureComptable (	-2,	new JournalComptable (	"VE",	"Vente"	),	"VE-2016/00002", sdf.parse("2016-12-30"),	"TMA Appli Xxx");
        ec2.setDate(sdf.parse(sdf.format(ec2.getDate())));
        ec2.setListLigneEcriture(Arrays.asList(
                new LigneEcritureComptable (	cc2,"Facture C110002",	new BigDecimal(3000.00),	null	),
                new LigneEcritureComptable (	cc4,"TMA Appli Xxx",	null,	new BigDecimal(2500.00)	),
                new LigneEcritureComptable (	cc5,"TVA 20%",	null,	new BigDecimal(500.00)	)
        ));

        Assert.assertEquals(ec2, ec);
    }

    @Test
    public void getEcritureComptableByRefTest() throws NotFoundException {
        EcritureComptable ec1 = new EcritureComptable (	-1,	new JournalComptable (	"AC",	"Achat"	),	"AC-2016/00001",	new Date(),	"Cartouches d’imprimante"	);
        ec1.setListLigneEcriture(Arrays.asList(
                new LigneEcritureComptable(),
                new LigneEcritureComptable(),
                new LigneEcritureComptable()
        ));

        /*
        INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-1,	1,	606,	'Cartouches d’imprimante',	43.95,	null	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-1,	2,	4456,	'TVA 20%',	8.79,	null	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-1,	3,	401,	'Facture F110001',	null,	52.74	);

         */

        EcritureComptable ec = comptabiliteDao.getEcritureComptableByRef("AC-2016/00001");

        Assert.assertEquals(ec1,ec);

    }

    @Test
    public void loadListLigneEcriture() {
    }

    @Test
    public void insertEcritureComptable() throws ParseException{



        CompteComptable cc2 = new CompteComptable(411,	"Clients"	);
        CompteComptable cc4 = new CompteComptable(706,"Prestations de services"	);
        CompteComptable cc5 = new CompteComptable(4457,	"Taxes sur le chiffre d'affaires collectées par l'entreprise"	);

        EcritureComptable ec2 = new EcritureComptable (	-2,	new JournalComptable (	"VE",	"Vente"	),	"VE-2016/00002", sdf.parse("2016-12-30"),	"TMA Appli Xxx");
        ec2.setListLigneEcriture(Arrays.asList(
                new LigneEcritureComptable (	cc2,"Facture C110002",	new BigDecimal(3000.00),	null	),
                new LigneEcritureComptable (	cc4,"TMA Appli Xxx",	null,	new BigDecimal(2500.00)	),
                new LigneEcritureComptable (	cc5,"TVA 20%",	null,	new BigDecimal(500.00)	)
        ));

        comptabiliteDao.insertEcritureComptable(ec2);
    }

    @Test
    public void insertListLigneEcritureComptable() throws ParseException{
        LigneEcritureComptable lec1 = new LigneEcritureComptable (	compteComptable,"Cartouches d’imprimante", new BigDecimal(43.95),	null	);
        LigneEcritureComptable lec2 = new LigneEcritureComptable (	compteComptable,"TVA 20%",	new BigDecimal(8.79),	null	);
        LigneEcritureComptable lec3 = new LigneEcritureComptable (	compteComptable,"Facture F110001",	null,	new BigDecimal(52.74)	);

        List<LigneEcritureComptable> ecritures = Arrays.asList(lec1, lec2, lec3);

        EcritureComptable ec = new EcritureComptable(12, journalComptable, "VE-2017/00003", sdf.parse("2017-04-22"),"test");
        ec.setListLigneEcriture(ecritures);

        comptabiliteDao.insertListLigneEcritureComptable(ec);
    }

    @Test
    public void updateEcritureComptable() throws NotFoundException{
        EcritureComptable ecritureComptable = comptabiliteDao.getEcritureComptable(-3);
        ecritureComptable.setLibelle(" libelle test update");
        comptabiliteDao.updateEcritureComptable(ecritureComptable);

        Assert.assertEquals(" libelle test update", comptabiliteDao.getEcritureComptable(-3).getLibelle());
    }

    @Test
    public void deleteEcritureComptable()throws NotFoundException {
        Assert.assertEquals(2, comptabiliteDao.getEcritureComptable(-3).getListLigneEcriture().size());
        comptabiliteDao.deleteEcritureComptable(-3);
        comptabiliteDao.getEcritureComptable(-3);
    }

    @Test
    public void deleteListLigneEcritureComptable() {
    }
}