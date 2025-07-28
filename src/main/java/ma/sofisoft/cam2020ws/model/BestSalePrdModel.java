package ma.sofisoft.cam2020ws.model;

public class BestSalePrdModel {
	
	private int Quantite;
	private int NumProduit;
	private String CodeProduit;
	private String Designation;
	private double Total;
	private double prixVente;
	
	
	
	public int getQuantite() {
		return Quantite;
	}
	public void setQuantite(int quantite) {
		Quantite = quantite;
	}
	public int getNumProduit() {
		return NumProduit;
	}
	public void setNumProduit(int numProduit) {
		NumProduit = numProduit;
	}
	public String getCodeProduit() {
		return CodeProduit;
	}
	public void setCodeProduit(String codeProduit) {
		CodeProduit = codeProduit;
	}
	public String getDesignation() {
		return Designation;
	}
	public void setDesignation(String designation) {
		Designation = designation;
	}
	public double getTotal() {
		Total = Quantite * prixVente;
		return Total;
	}
	public void setTotal(double total) {
		Total = total;
	}
	public double getPrixVente() {
		return prixVente;
	}
	public void setPrixVente(double prixVente) {
		this.prixVente = prixVente;
	}
	
	
	

}
