package ma.sofisoft.cam2020ws.model;

public class DashboardModel {
    private String codeMagasin;
    private String nomMagasin;
    private double montantTTC;
    private double quantite;
    private double nombreTickets;
    private double prixMoyen;
    private double tauxObjectif;
    private double debitMoyen;
    private double panierMoyen;
    private double objectif;

    public DashboardModel(
            String codeMagasin,
            String nomMagasin,
            double montantTTC,
            double quantite,
            double nombreTickets, // <-- DOIT ÊTRE double, PAS int ou Long !
            double prixMoyen,
            double tauxObjectif,
            double debitMoyen,
            double panierMoyen
    ) {
        this.codeMagasin = codeMagasin;
        this.nomMagasin = nomMagasin;
        this.montantTTC = montantTTC;
        this.quantite = quantite;
        this.nombreTickets = nombreTickets;
        this.prixMoyen = prixMoyen;
        this.tauxObjectif = tauxObjectif;
        this.debitMoyen = debitMoyen;
        this.panierMoyen = panierMoyen;
    }
    public String getCodeMagasin() {
        return codeMagasin;
    }

    public void setCodeMagasin(String codeMagasin) {
        this.codeMagasin = codeMagasin;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

    public double getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(double montantTTC) {
        this.montantTTC = montantTTC;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getNombreTickets() {
        return nombreTickets;
    }

    public void setNombreTickets(double nombreTickets) {
        this.nombreTickets = nombreTickets;
    }

    public double getPrixMoyen() {
        return prixMoyen;
    }

    public void setPrixMoyen(double prixMoyen) {
        this.prixMoyen = prixMoyen;
    }

    public double getTauxObjectif() {
        return tauxObjectif;
    }

    public void setTauxObjectif(double tauxObjectif) {
        this.tauxObjectif = tauxObjectif;
    }

    public double getDebitMoyen() {
        return debitMoyen;
    }

    public void setDebitMoyen(double debitMoyen) {
        this.debitMoyen = debitMoyen;
    }

    public double getPanierMoyen() {
        return panierMoyen;
    }

    public void setPanierMoyen(double panierMoyen) {
        this.panierMoyen = panierMoyen;
    }
    public double getObjectif() {
        return objectif;
    }
    public void setObjectif(double objectif) {
        this.objectif = objectif;
    }
}