package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.After;
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

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    CompteComptable cc2 = new CompteComptable(411,	"Clients"	);
    CompteComptable cc4 = new CompteComptable(706,"Prestations de services"	);
    CompteComptable cc5 = new CompteComptable(4457,	"Taxes sur le chiffre d'affaires collectées par l'entreprise"	);

    LigneEcritureComptable lec1 = new LigneEcritureComptable (	cc2,"Cartouches d’imprimante", new BigDecimal(43.95),	null	);
    LigneEcritureComptable lec2 = new LigneEcritureComptable (	cc2,"TVA 20%",	new BigDecimal(8.79),	null	);
    LigneEcritureComptable lec3 = new LigneEcritureComptable (	cc2,"Facture F110001",	null,	new BigDecimal(52.74)	);

    @Autowired
    ApplicationContext applicationContext;
    Map<DataSourcesEnum, DataSource> mds = new HashMap<>();

    @Before
    public void beforeTest() throws NotFoundException, ParseException{
        DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
        mds.put(DataSourcesEnum.MYERP, dataSource);
        AbstractDbConsumer.configure(mds);

    }

    /**
     * récupère l'ensemble des comptes comptables et vérifie qu'il y en a 7 (comme en base de données)
     */
    @Test
    public void checkListCompteComptableSize() {
        List<CompteComptable> ccs = comptabiliteDao.getListCompteComptable();
        Assert.assertEquals(7, ccs.size());
    }

    /**
     * récupère l'ensemble des journaux comptables et vérifie qu'il y en a 4 (comme en base de données)
     */
    @Test
    public void getListJournalComptableTest() {
        List<JournalComptable> jcs = comptabiliteDao.getListJournalComptable();
        Assert.assertEquals(4, jcs.size());
    }

    /**
     * récupère l'ensemble des écritures comptables et vérifie qu'il y en a 4 (comme en base de données)
     */
    @Test
    public void getListEcritureComptableTest() {
        List<EcritureComptable> ecs = comptabiliteDao.getListEcritureComptable();
        Assert.assertEquals(4, ecs.size());
    }

    /**
     * vérifie que l'écriture recherchée a la bonne référence
     * @throws NotFoundException
     * @throws ParseException
     */
    @Test
    public void getEcritureComptableTest() throws NotFoundException, ParseException {

        EcritureComptable ec = comptabiliteDao.getEcritureComptable(-2);
        Assert.assertEquals(ec.getReference(), "VE-2016/00002");
    }

    /**
     *
     * @throws NotFoundException
     */
    @Test
    public void getEcritureComptableByRefTest() throws NotFoundException {

        EcritureComptable ec = comptabiliteDao.getEcritureComptableByRef("AC-2016/00001");
        Assert.assertTrue(ec.getId() == -1);

    }

    @Test
    public void insertEcritureComptable() throws ParseException, NotFoundException{

        CompteComptable cc2 = new CompteComptable(411,	"Clients"	);

        EcritureComptable ec2 = new EcritureComptable ();
        ec2.setJournal(comptabiliteDao.getListJournalComptable().get(1));
        ec2.setDate(new Date());
        ec2.setReference("VE-2016/00005");
        ec2.setLibelle("TMA Appli Xxx");
        ec2.setListLigneEcriture(Arrays.asList(
                new LigneEcritureComptable (	cc2,"Facture C110002",	new BigDecimal(3000.00),	null	),
                new LigneEcritureComptable (	cc2,"TMA Appli Xxx",	null,	new BigDecimal(2500.00)	),
                new LigneEcritureComptable (	cc2,"TVA 20%",	null,	new BigDecimal(500.00)	)
        ));

        comptabiliteDao.insertEcritureComptable(ec2);
        EcritureComptable ec1 = comptabiliteDao.getEcritureComptableByRef("VE-2016/00005");
        Assert.assertEquals(ec1.getId(), ec2.getId());
        comptabiliteDao.deleteEcritureComptable(ec2.getId());
    }

    @Test
    public void insertListLigneEcritureComptable() throws ParseException, NotFoundException{

        List<LigneEcritureComptable> ecritures = Arrays.asList(lec1, lec2, lec3);
        comptabiliteDao.deleteListLigneEcritureComptable(-2);
        EcritureComptable ec = comptabiliteDao.getEcritureComptable(-2);
        ec.setListLigneEcriture(ecritures);

        comptabiliteDao.insertListLigneEcritureComptable(ec);
        Assert.assertEquals(ec.getListLigneEcriture().size(), 3);
    }

    @Test
    public void updateEcritureComptable() throws NotFoundException{
        EcritureComptable ecritureComptable = comptabiliteDao.getEcritureComptable(-2);
        ecritureComptable.setLibelle("libelle test update");
        comptabiliteDao.updateEcritureComptable(ecritureComptable);

        Assert.assertEquals("libelle test update", comptabiliteDao.getEcritureComptable(-2).getLibelle());
    }

    @Test(expected = NotFoundException.class)
    public void deleteEcritureComptable()throws NotFoundException {
        Assert.assertEquals(2, comptabiliteDao.getEcritureComptable(-3).getListLigneEcriture().size());
        comptabiliteDao.deleteEcritureComptable(-3);
        comptabiliteDao.getEcritureComptable(-3);
    }

    @Test
    public void deleteListLigneEcritureComptable() throws NotFoundException{
        List<LigneEcritureComptable> list = comptabiliteDao.getEcritureComptable(-2).getListLigneEcriture();
        comptabiliteDao.deleteListLigneEcritureComptable(-2);
        Assert.assertEquals(comptabiliteDao.getEcritureComptable(-2).getListLigneEcriture().size(), 0);
    }

    @Test
    public  void getSQLgetSequenceEcritureComptableByYearAndCodeTest()throws  NotFoundException{
        List<SequenceEcritureComptable>sec = comptabiliteDao.getSQLgetSequenceEcritureComptableByYearAndCode("AC", 2016);
        Assert.assertEquals(1 ,sec.size() );
    }

    @After
    public void afterTest(){}{

    }
}