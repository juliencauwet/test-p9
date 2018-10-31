package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.configuration.TestDbConfig;
import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
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
    }

    @Test
    public void checkListCompteComptableSize() {

        AbstractDbConsumer.configure(mds);
        List<CompteComptable> ccs = comptabiliteDao.getListCompteComptable();

        Assert.assertEquals(7, ccs.size());
    }

    @Test
    public void getListJournalComptable() {
    }

    @Test
    public void getListEcritureComptable() {
    }

    @Test
    public void getEcritureComptable() {
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