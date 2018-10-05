package com.dummy.myerp.model.bean.fixtures;

import com.dummy.myerp.model.bean.comptabilite.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Fixtures {

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
    LigneEcritureComptable lec8 = new LigneEcritureComptable (	comptes.get(7),null,	null,	new BigDecimal(52.74)	);
    LigneEcritureComptable lec9 = new LigneEcritureComptable (	comptes.get(1),"Facture C110004",	new BigDecimal(5700),	null	);
    LigneEcritureComptable lec10 = new LigneEcritureComptable (	comptes.get(3),"TMA Appli Xxx",	null,	new BigDecimal(4750)	);
    LigneEcritureComptable lec11 = new LigneEcritureComptable (	comptes.get(4),"TVA 20%",	null,	new BigDecimal(950)	);
    LigneEcritureComptable lec12 = new LigneEcritureComptable (	comptes.get(7),null,	new BigDecimal(3000),	null	);
    LigneEcritureComptable lec13 = new LigneEcritureComptable (	comptes.get(1),null,	null,	new BigDecimal(3000));
    LigneEcritureComptable lec14 = new LigneEcritureComptable(comptes.get(8), "lec test1", new BigDecimal(123), null);
    LigneEcritureComptable lec15 = new LigneEcritureComptable(comptes.get(9), "lec test2", null, new BigDecimal(1234));

    JournalComptable jc1 = new JournalComptable (	"AC",	"Achat"	);
    JournalComptable jc2 = new JournalComptable (	"VE",	"Vente"	);
    JournalComptable jc3 = new JournalComptable (	"BQ",	"Banque"	);
    JournalComptable jc4 = new JournalComptable (	"OD",	"Opérations Diverses"	);
    JournalComptable jc5 = new JournalComptable (	"TE",	"jc test"	);

    SequenceEcritureComptable sec1 = new SequenceEcritureComptable (2018,	40, "AC");
    SequenceEcritureComptable sec2 = new SequenceEcritureComptable (2017,	41, "VE");
    SequenceEcritureComptable sec3 = new SequenceEcritureComptable (2018,	51, ""	);
    SequenceEcritureComptable sec4 = new SequenceEcritureComptable (2018,	88, ""	);

    List<EcritureComptable> ecritures =Arrays.asList(
    new EcritureComptable (	1,	jc1,	"AC-2016/00001",	new Date(),	"Cartouches d’imprimante"	),
    new EcritureComptable (	2,	jc2,	"BQ-2018/00002",	new Date(),	"TMA Appli Xxx"),
    new EcritureComptable (	3,	jc3,	"BQ-2018/00003",	new Date(),	"Paiement Facture F110001"),
    new EcritureComptable (	4,	jc2,	"VE-2018/00004",	new Date(),	"TMA Appli Yyy"),
    new EcritureComptable (	5,	jc4,	"OD-2018/00005",	new Date(),	"Paiement Facture C110002"),
    new EcritureComptable ( 6,  jc5,    "TE-2018/00006",   new Date(), "ec test")
    );
}
