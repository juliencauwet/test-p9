package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ComptabiliteDaoMock {

    CompteComptable cc1 = new CompteComptable(401,	"Fournisseurs"	);
    CompteComptable cc2 = new CompteComptable(411,	"Clients"	);
    CompteComptable cc3 = new CompteComptable(4456,"Taxes sur le chiffre d'affaires déductibles"	);
    CompteComptable cc4 = new CompteComptable(4457,"Taxes sur le chiffre d'affaires collectées par l'entreprise"	);
    CompteComptable cc5 = new CompteComptable(512,	"Banque"	);
    CompteComptable cc6 = new CompteComptable(606,	"Achats non stockés de matières et fournitures"	);
    CompteComptable cc7 = new CompteComptable(706,	"Prestations de services"	);
    CompteComptable cc8 = new CompteComptable(1);
    CompteComptable cc9 = new CompteComptable(2);

    List<CompteComptable> comptes = Arrays.asList(cc1, cc2, cc3, cc4, cc5, cc6, cc7, cc7, cc8, cc9);

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

    List<LigneEcritureComptable> lignes = Arrays.asList(lec1, lec2, lec3, lec4,lec5,lec6, lec7, lec8, lec9, lec10,lec11,lec12, lec13, lec14, lec15);

    public List<CompteComptable> getListCompteComptable() {
        return this.comptes;
    }


    public List<JournalComptable> getListJournalComptable() {
        return null;
    }


    public List<EcritureComptable> getListEcritureComptable() {

        List<EcritureComptable> ecritureComptableList=new ArrayList<>();

        EcritureComptable ecritureComptable=new EcritureComptable();
        ecritureComptable.setLibelle("Cartouches d’imprimante");
        ecritureComptable.setId(-1);
        ecritureComptable.setJournal(new JournalComptable("AC","Acomptes"));
        ecritureComptable.setReference("AC-2016/00001");
        ecritureComptable.setDate(new Date(2016-12-31));
        ecritureComptableList.add(ecritureComptable);

        return ecritureComptableList;
    }


    public List<EcritureComptable> getEcritureComptable(Integer pId) throws NotFoundException {
        return Arrays.asList(
         new EcritureComptable (	1,	new JournalComptable(),	"AC-2016/00001",	new Date(),	"Cartouches d’imprimante"	),
         new EcritureComptable (	2,	new JournalComptable(),	"BQ-2018/00002",	new Date(),	"TMA Appli Xxx"),
         new EcritureComptable (	3,	new JournalComptable(),	"BQ-2018/00003",	new Date(),	"Paiement Facture F110001"),
         new EcritureComptable (	4,	new JournalComptable(),	"VE-2018/00004",	new Date(),	"TMA Appli Yyy")

        );
    }


    public EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(22);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable
                .setReference("AC-" + new SimpleDateFormat("yyyy").format(vEcritureComptable.getDate()) + "/00022");
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

        return vEcritureComptable;
    }


    public void loadListLigneEcriture(EcritureComptable pEcritureComptable) {
    }


    public void insertEcritureComptable(EcritureComptable pEcritureComptable) {

    }


    public void updateEcritureComptable(EcritureComptable pEcritureComptable) {

    }


    public void deleteEcritureComptable(Integer pId) {

    }

    public SequenceEcritureComptable getSequenceByCodeAndAnneeCourante(SequenceEcritureComptable pSequence) throws NotFoundException {

        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(2016,40,"AC");

        //INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'AC',	2016,	40	);
        //INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'VE',	2016,	41	);
        //INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'BQ',	2016,	51	);
        //INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'OD',	2016,	88	);

        return sequenceEcritureComptable;
    }

    public void upsertSequenceEcritureComptable(SequenceEcritureComptable pSequence) {

    }
}
