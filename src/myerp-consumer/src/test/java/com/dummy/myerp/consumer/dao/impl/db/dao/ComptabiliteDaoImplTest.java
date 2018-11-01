package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void getEcritureComptableTest() throws NotFoundException {
        EcritureComptable ec = comptabiliteDao.getEcritureComptable(2);
    }

    @Test
    public void getEcritureComptableByRef() {
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