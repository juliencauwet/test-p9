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
        //INSERT INTO MYERP.ecriture_comptable (id,journal_code,reference,date,libelle) VALUES (	-2,	'VE',	'VE-2016/00002',	'2016-12-30',	'TMA Appli Xxx'	);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");

        CompteComptable cc2 = new CompteComptable(411,	"Clients"	);
        CompteComptable cc4 = new CompteComptable(706,"Prestations de services"	);
        CompteComptable cc5 = new CompteComptable(4457,	"Taxes sur le chiffre d'affaires collectées par l'entreprise"	);

        EcritureComptable ec2 = new EcritureComptable (	-2,	new JournalComptable (	"VE",	"Vente"	),	"VE-2016/00002", sdf.parse("2016-12-30"),	"TMA Appli Xxx");

        ec2.setListLigneEcriture(Arrays.asList(
                new LigneEcritureComptable (	cc2,"Facture C110002",	new BigDecimal(3000.00),	null	),
                new LigneEcritureComptable (	cc4,"TMA Appli Xxx",	null,	new BigDecimal(2500.00)	),
                new LigneEcritureComptable (	cc5,"TVA 20%",	null,	new BigDecimal(500.00)	)
        ));

        EcritureComptable ec = comptabiliteDao.getEcritureComptable(-2);
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
    public void insertEcritureComptable() {
    }

    @Test
    public void insertListLigneEcritureComptable() {
    }

    @Test
    public void updateEcritureComptable() {
    }

    @Test
    public void deleteEcritureComptable() {
    }

    @Test
    public void deleteListLigneEcritureComptable() {
    }
}