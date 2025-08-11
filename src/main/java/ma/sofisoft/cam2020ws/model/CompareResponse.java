package ma.sofisoft.cam2020ws.model;

import java.util.List;

public class CompareResponse {
    private List<DashboardModel> magasins; // KPIs par magasin
    private double ecartCA;
    private double ecartTickets;
    private double ecartQuantite;
    private List<DashboardModel> classement; // trié par CA

    // Getters et setters
    public List<DashboardModel> getMagasins() { return magasins; }
    public void setMagasins(List<DashboardModel> magasins) { this.magasins = magasins; }

    public double getEcartCA() { return ecartCA; }
    public void setEcartCA(double ecartCA) { this.ecartCA = ecartCA; }

    public double getEcartTickets() { return ecartTickets; }
    public void setEcartTickets(double ecartTickets) { this.ecartTickets = ecartTickets; }

    public double getEcartQuantite() { return ecartQuantite; }
    public void setEcartQuantite(double ecartQuantite) { this.ecartQuantite = ecartQuantite; }

    public List<DashboardModel> getClassement() { return classement; }
    public void setClassement(List<DashboardModel> classement) { this.classement = classement; }
}