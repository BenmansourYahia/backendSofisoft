package ma.sofisoft.cam2020ws.model;

public class StockModel {
	
	private Integer codeDepot;
	private int numProduit;
	private String magasin;
	private String designation;
	private String codeProduit;
	private Integer quantite;
	private Integer numDim1;
	private Integer numDim2;
	private String Dims1;
	private String Dims2;
		
	public Integer getNumDim1() {
		return numDim1;
	}
	public void setNumDim1(Integer numDim1) {
		this.numDim1 = numDim1;
	}
	public Integer getNumDim2() {
		return numDim2;
	}
	public void setNumDim2(Integer numDim2) {
		this.numDim2 = numDim2;
	}
	public int getNumProduit() {
		return numProduit;
	}
	public void setNumProduit(int numProduit) {
		this.numProduit = numProduit;
	}
	public String getMagasin() {
		return magasin;
	}
	public void setMagasin(String magasin) {
		this.magasin = magasin;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getCodeProduit() {
		return codeProduit;
	}
	public void setCodeProduit(String codeProduit) {
		this.codeProduit = codeProduit;
	}
	public Integer getQuantite() {
		return quantite;
	}
	
	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}
	
	public String getDims1() {
		return Dims1;
	}
	public void setDims1(String dims1) {
		Dims1 = dims1;
	}
	public String getDims2() {
		return Dims2;
	}
	public void setDims2(String dims2) {
		Dims2 = dims2;
	}
	public Integer getCodeDepot() {
		return codeDepot;
	}
	public void setCodeDepot(Integer codeDepot) {
		this.codeDepot = codeDepot;
	}
	
	
}
