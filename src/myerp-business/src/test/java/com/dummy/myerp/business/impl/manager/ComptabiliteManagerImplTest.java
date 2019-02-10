package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoMock;
import com.dummy.myerp.consumer.dao.impl.db.dao.DaoProxyMock;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.technical.exception.TechnicalException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplTest extends AbstractBusinessManager{

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    private EcritureComptable vEcritureComptable;
    private static SimpleDateFormat dateFormat;


    @BeforeClass
   public static void setUp() {
       ComptabiliteDao comptabiliteDao=Mockito.mock(ComptabiliteDao.class);
       DaoProxy daoProxy = new DaoProxyMock(comptabiliteDao);
       ComptabiliteDaoMock comptabiliteDaoMock=new ComptabiliteDaoMock();
       ComptabiliteManagerImpl.configure(null, daoProxy, null);
       //when(daoProxy.getComptabiliteDao().getListEcritureComptable()).thenReturn(comptabiliteDaoMock.getListEcritureComptable());
       dateFormat = new SimpleDateFormat("yyyy");
   }



    @Mock
    ComptabiliteDaoMock comptabiliteDao;

    @Mock
    ComptabiliteManagerImpl comptabiliteManager;


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

    SequenceEcritureComptable sec1 = new SequenceEcritureComptable (2017,	40, "AC");
    SequenceEcritureComptable sec2 = new SequenceEcritureComptable (2017,	41, "VE");

    EcritureComptable ec1 = new EcritureComptable (	1,	jc1,	"AC-2016/00001",	new Date(),	"Cartouches d’imprimante"	);
    EcritureComptable ec2 = new EcritureComptable (	2,	jc2,	"BQ-2018/00002",	new Date(),	"TMA Appli Xxx");
    EcritureComptable ec3 = new EcritureComptable (	3,	jc3,	"BQ-2018/00003",	new Date(),	"Paiement Facture F110001");
    EcritureComptable ec5 = new EcritureComptable (	5,	jc4,	"OD-2019/00005",	new Date(),	"Paiement Facture C110002");

   @Test
   public void checkEcritureComptableUnitWhenNoViolation() throws Exception {

       LocalDateTime now = LocalDateTime.now();
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
       String year = now.format(formatter);

       String ref = "AC-" + year + "/00001";

       EcritureComptable vEcritureComptable = new EcritureComptable();
       vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
       vEcritureComptable.setDate(new Date());
       vEcritureComptable.setLibelle("Libelle");
       vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                null, new BigDecimal(123),
                                                                                null));
       vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                null, null,
                                                                                new BigDecimal(123)));
        vEcritureComptable.setReference(ref);


       manager.checkEcritureComptableUnit(vEcritureComptable);
   }

    /**
     * Renvoie exception car le format de référence ne respecte pas les regles (regular expression)
     * @throws Exception
     */
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitWhenReferenceFormatViolation() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        String year = now.format(formatter);

        String ref = "AC-" + year + "/0001";

        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
        vEcritureComptable.setReference(ref);


        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    /**
     *
     * @throws Exception
     */
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    /**
     * vérifie si les lignes d'écriture s'équilibrent. Doit renvoyer une exceptioncar totaldebit et totalcrédit sont différents
     * @throws Exception
     */
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

        EcritureComptable ec3 = new EcritureComptable (	3,	jc3,	"BQ-2019/00003",	new Date(),	"Paiement Facture F110001");

        manager.RG_5_formatCorrect(ec3);
    }

    @Test
    public void RG_5_formatCorrect_ec4() throws FunctionalException {

        EcritureComptable ec4 = new EcritureComptable (	4,	jc2,	"VE-2019/00004",	new Date(),	"TMA Appli Yyy");
        manager.RG_5_formatCorrect(ec4);
    }


    @Test
    public void RG_5_formatCorrect_ec5() throws FunctionalException {

        manager.RG_5_formatCorrect(ec5);
    }

    /**
     * code journal incorrect
     * @throws FunctionalException
     */
    @Test(expected = FunctionalException.class)
    public void RG_5_CodeJournalNonCorrect() throws FunctionalException{
        String year = Integer.toString(LocalDateTime.now().getYear());

        EcritureComptable ec = new EcritureComptable();
        ec.setDate(new Date());
        ec.setJournal(jc1);
        ec.setReference("EU-" + year + "/00002");
        manager.RG_5_formatCorrect(ec);
    }

    /**
     * année référence incorrecte
     * @throws FunctionalException
     */
    @Test(expected = FunctionalException.class)
    public void RG_5_AnneeNonCorrect() throws FunctionalException{
        String year = "1897";

        EcritureComptable ec = new EcritureComptable();
        ec.setDate(new Date());
        ec.setJournal(jc1);
        ec.setReference("AC-" + year + "/00002");
        manager.RG_5_formatCorrect(ec);
    }

    @Test
    public void getListCompteComptable() {
        when(getDaoProxy().getComptabiliteDao().getListCompteComptable()).thenReturn(Arrays.asList(cc1, cc2, cc3, cc4));
        List<CompteComptable> list = manager.getListCompteComptable();
        Assert.assertEquals(4, list.size());

    }

    @Test
    public void getListJournalComptable() {
        when(getDaoProxy().getComptabiliteDao().getListJournalComptable()).thenReturn(Arrays.asList(jc1, jc2, jc3));
        List<JournalComptable> list = manager.getListJournalComptable();
        Assert.assertEquals(3, list.size());

    }

    @Test
    public void getListEcritureComptable() {
        when(getDaoProxy().getComptabiliteDao().getListEcritureComptable()).thenReturn(Arrays.asList(ec1,ec2, ec3));
        List<EcritureComptable> list = manager.getListEcritureComptable();
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void buildRefTest(){
        Assert.assertEquals("OD-2019/00004", manager.buildReference(manager.getYear(ec5.getDate()), ec5.getJournal().getCode(), 4));
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableContextIfRefExists () throws NotFoundException, FunctionalException {

        when(getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(anyString())).thenReturn(ec1);

        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setId(101);
        ecritureComptable.setJournal(jc1);
        ecritureComptable.setReference("AC-2016/00001");

        manager.checkEcritureComptableContext(ecritureComptable);

    }

    /**
     * teste
     *  extends AbstractBusinessManager afin de mocker la methode getDaoProxy
     * @throws NotFoundException
     * @throws FunctionalException
     */
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableContextIfDifferentIdFound () throws NotFoundException, FunctionalException {

        when(getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(anyString())).thenReturn(ec1);

        ec1.setId(200);
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(jc1);
        ecritureComptable.setReference("AC-2016/00001");
        ecritureComptable.setId(100);

        manager.checkEcritureComptableContext(ecritureComptable);

    }

    @Test
    public void checkEcritureComptableContextOK() throws FunctionalException, NotFoundException{
        EcritureComptable ec = new EcritureComptable();
        when(getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(anyString())).thenReturn(ec2);
        ec.setReference("BQ-2018/00002");
        ec.setId(103);
        ec2.setId(103);
        manager.checkEcritureComptableContext(ec);
    }

    /**
     * vérifie que la méthode updateEcritureComptable du mock est bien appelée une fois
     */
    @Test
    public void updateEcritureComptable(){
        ec1.setDate(new Date());
        comptabiliteDao.updateEcritureComptable(ec1);
        verify(comptabiliteDao, times(1)).updateEcritureComptable(ec1);

    }

    @Test
    public void deleteEcritureComptable(){

        comptabiliteDao.deleteEcritureComptable(2);
        verify(comptabiliteDao, times(1)).deleteEcritureComptable(2);

    }

    @Test
    public void addReference() throws NotFoundException, TechnicalException, FunctionalException {
        String year = Integer.toString(LocalDateTime.now().getYear());
        when(getDaoProxy().getComptabiliteDao().getSQLgetSequenceEcritureComptableByYearAndCode(anyString(), anyInt())).thenReturn(Arrays.asList(sec1, sec2));
        EcritureComptable ec = new EcritureComptable();
        ec.setDate(new Date());
        ec.setJournal(jc1);
        manager.addReference(ec);
        Assert.assertEquals("AC-"+ year + "/00042", ec.getReference());

    }

    @Test
    public void checkEcritureComptable(){

    }

}
