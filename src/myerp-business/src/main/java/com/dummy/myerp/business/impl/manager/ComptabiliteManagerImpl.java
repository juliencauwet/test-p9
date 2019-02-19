package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.TechnicalException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) throws NotFoundException, TechnicalException, FunctionalException {

        // Bien se réferer à la JavaDoc de cette méthode !
        /* Le principe :
                1.  Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
                    (table sequence_ecriture_comptable)
         */
       int valeur = 0; //initialisation de la valeur qui correspond à la dernière valeur de séquence

       String code = pEcritureComptable.getJournal().getCode(); // code du journal de l'écriture

       String annee = getYear(pEcritureComptable.getDate()); // année de la date de l'écriture

       try {
           //recherche de la séquence par l'année et le code journal
           List<SequenceEcritureComptable> seqs = getDaoProxy().getComptabiliteDao().getSQLgetSequenceEcritureComptableByYearAndCode(annee, Integer.parseInt(annee));

           for (int i = 0; i < seqs.size(); i++){
               //boucle à travers les séquences celle qui a la valeur la plus haute valeur
               if (seqs.get(i).getDerniereValeur() > valeur){
                   valeur = seqs.get(i).getDerniereValeur();
               }
           }
       }catch (Exception e){
           throw new TechnicalException(" Impossible de se connecter à la base de données ");
       }
        /*
                2.  * S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
                        1. Utiliser le numéro 1.
                    * Sinon :
                        1. Utiliser la dernière valeur + 1
       */
        //si aucune valeur n'a été trouvé alors la séquence sera 1, sinon elle sera incrémentée de 1
        valeur++;

        /*
                3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
         */
        // appelle la méthode pour construire la référence
        pEcritureComptable.setReference(buildReference(annee, code, valeur));
        /*

        /*
                4.  Enregistrer (insert/update) la valeur de la séquence en persitance
                    (table sequence_ecriture_comptable)
         */
        SequenceEcritureComptable newSeq = new SequenceEcritureComptable();
        newSeq.setDerniereValeur(valeur);
        newSeq.setAnnee(Integer.parseInt(annee));
        newSeq.setCode(pEcritureComptable.getJournal().getCode());

        if(valeur == 1)
            getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(newSeq);


    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                                          new ConstraintViolationException(
                                              "L'écriture comptable ne respecte pas les contraintes de validation",
                                              vViolations));
        }
        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        RG_2_Equilibree(pEcritureComptable);

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        RG_3_auMoins2Lignes(pEcritureComptable);

        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
        RG_5_formatCorrect(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {

        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                    pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                    || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.

            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    protected void RG_2_Equilibree(EcritureComptable ecriture)throws FunctionalException{
        if (!ecriture.isEquilibree()) {
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }
    }

    protected void RG_3_auMoins2Lignes(EcritureComptable ecriture)throws FunctionalException{
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : ecriture.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (ecriture.getListLigneEcriture().size() < 2
                || vNbrCredit < 1
                || vNbrDebit < 1) {
            throw new FunctionalException(
                    "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }
    }

    protected void RG_5_formatCorrect(EcritureComptable pEcritureComptable) throws FunctionalException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(pEcritureComptable.getDate());

         //teste si l'année de ref correspond à l'année de l'écriture
        if (!Integer.toString(cal.get(Calendar.YEAR)).equals(pEcritureComptable.getReference().substring(3,7)))
            throw new FunctionalException("L'année de l'écriture diffère de l'année de référence.");
        if (!pEcritureComptable.getReference().substring(0,2).equals(pEcritureComptable.getJournal().getCode()))
            throw new FunctionalException("Le code journal de référence diffère du code du journal");
    }

    protected String buildReference(String annee, String code, int valeur){
        String ref = "";

        ref = code + "-";
        ref += annee + "/";
        ref += String.format("%05d", valeur);

        return ref;
    }

    protected String getYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return Integer.toString(cal.get(Calendar.YEAR));
    }
}
