package ma.sofisoft.cam2020ws.model;

import java.util.Date;

public class EvolutionCAModel {
     Date jourVente;
   double montantTTC;

    public EvolutionCAModel(Date jourVente, double montantTTC) {
        this.jourVente = jourVente;
        this.montantTTC = montantTTC;
    }

    public Date getJourVente() {
        return jourVente;
    }

    public void setJourVente(Date jourVente) {
        this.jourVente = jourVente;
    }

    public double getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(double montantTTC) {
        this.montantTTC = montantTTC;
    }
}