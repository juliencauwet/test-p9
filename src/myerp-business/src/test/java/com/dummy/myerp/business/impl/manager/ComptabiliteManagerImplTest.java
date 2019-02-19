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
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");;

    @Mock
    ComptabiliteDaoMock comptabiliteDao;

    @BeforeClass
    public static void setUp() {
        ComptabiliteDao comptabiliteDao=Mockito.mock(ComptabiliteDao.class);
        DaoProxy daoProxy = new DaoProxyMock(comptabiliteDao);
        ComptabiliteManagerImpl.configure(null, daoProxy, null);
    }


    CompteComptable cc1 = new CompteComptable(401,	"Fournisseurs"	);
    CompteComptable cc2 = new CompteComptable(411,	"Clients"	);
    CompteComptable cc3 = new CompteComptable(4456,"Taxes sur le chiffre d'affaires déductibles"	);
    CompteComptable cc4 = new CompteComptable(4457,"Taxes sur le chiffre d'affaires collectées par l'entreprise"	);

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


    /**
     * vérifie que l'écriture comptable soit conforme aux critères de validation
     */
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
     * vérifie si les lignes d'écriture s'équilibrent. Doit renvoyer une exception car totaldebit et totalcrédit sont différents
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

    /**
     * teste que l'écriture comptable soit conforme au RG_3. Renvoie une exception car elle ne contient pas de ligne de crédit
     * @throws Exception
     */
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

    /**
     * teste que ec1 soit conforme à RG_5 mais renvoie une erreur pour code année incorrect
     */
    @Test(expected = FunctionalException.class)
    public void RG_5_formatNonCorrect_AnneeRefIncoorecte() throws FunctionalException {

        EcritureComptable ec1 = new EcritureComptable (	1,	jc1,	"AC-2016/00001",	new Date(),	"Cartouches d’imprimante"	);

        manager.RG_5_formatCorrect(ec1);

    }

    /**
     * teste que ec2 soit conforme à RG_5 mais renvoie une erreur pour code journal incorrect
     */
    @Test(expected = FunctionalException.class)
    public void RG_5_formatNonCorrect_CodeJournaIncorrect() throws FunctionalException {

        EcritureComptable ec2 = new EcritureComptable (	2,	jc2,	"BQ-2018/00002",	new Date(),	"TMA Appli Xxx");

        manager.RG_5_formatCorrect(ec2);
    }

    /**
     *vérifie que ec3 est conform à RG_5
     */
    @Test
    public void RG_5_formatCorrect_ec3() throws FunctionalException {

        EcritureComptable ec3 = new EcritureComptable (	3,	jc3,	"BQ-2019/00003",	new Date(),	"Paiement Facture F110001");

        manager.RG_5_formatCorrect(ec3);
    }

    /**
     *vérifie que ec4 est conform à RG_5
     */
    @Test
    public void RG_5_formatCorrect_ec4() throws FunctionalException {

        EcritureComptable ec4 = new EcritureComptable (	4,	jc2,	"VE-2019/00004",	new Date(),	"TMA Appli Yyy");
        manager.RG_5_formatCorrect(ec4);
    }

    /**
     *vérifie que ec5 est conform à RG_5
     */

    @Test
    public void RG_5_formatCorrect_ec5() throws FunctionalException {

        manager.RG_5_formatCorrect(ec5);
    }

    /**
     * teste que la référence vérifie le journal comptable
     * code journal incorrect donc lance exception
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

    /**teste que la référence indique l'année correcte
     * année référence incorrecte, renvoie une exception
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

    /**
     * teste le renvoi d'une liste de comptes comptables
     */
    @Test
    public void getListCompteComptable() {
        when(getDaoProxy().getComptabiliteDao().getListCompteComptable()).thenReturn(Arrays.asList(cc1, cc2, cc3, cc4));
        List<CompteComptable> list = manager.getListCompteComptable();
        Assert.assertEquals(4, list.size());

    }

    /**
     * teste le renvoi d'une liste de journaux comptables
     */
    @Test
    public void getListJournalComptable() {
        when(getDaoProxy().getComptabiliteDao().getListJournalComptable()).thenReturn(Arrays.asList(jc1, jc2, jc3));
        List<JournalComptable> list = manager.getListJournalComptable();
        Assert.assertEquals(3, list.size());

    }

    /**
     * teste le renvoi d'une liste d'écriture comptable
     */
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

    /**
     * renvoie ec1 après requête au Dao de récupérer l'écriture comptable par la référence. Renvoie une exception car la référence existe
     * @throws NotFoundException
     * @throws FunctionalException
     */
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
     * trouve la référence mais l'id est différente. Renvoie une exception
     * extends AbstractBusinessManager afin de mocker la methode getDaoProxy
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

    /**
     * l'Id est le même et il trouve la référence donc tout est OK
     * @throws FunctionalException
     * @throws NotFoundException
     */
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
     * Le teste passe à travers toutes les méthodes qui aidenet à ajouter une référence à l'écriture comptable
     * @throws NotFoundException
     * @throws TechnicalException
     * @throws FunctionalException
     */
    //JournalComptable jc1 = new JournalComptable (	"AC",	"Achat"	);
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
    public void addReferenceIfCreationOfSequence() throws NotFoundException, TechnicalException, FunctionalException {
        String year = Integer.toString(LocalDateTime.now().getYear());
        when(getDaoProxy().getComptabiliteDao().getSQLgetSequenceEcritureComptableByYearAndCode(anyString(), anyInt())).thenReturn(new ArrayList<>());
        EcritureComptable ec = new EcritureComptable();
        ec.setDate(new Date());
        ec.setJournal(jc1);
        manager.addReference(ec);
        Assert.assertEquals("AC-"+ year + "/00001", ec.getReference());

    }

    /**
     * test la méthode checkEcritureComptableUnit et lance une exception car ec1 n'est pas équilibrée. Le teste vérifie que la méthode a été appelée
     * @throws FunctionalException
     */
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnit()throws FunctionalException{

        manager.checkEcritureComptableUnit(ec1);
        verify(manager, times(1)).RG_2_Equilibree(ec1);
    }


}
