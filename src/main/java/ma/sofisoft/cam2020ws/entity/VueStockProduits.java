package ma.sofisoft.cam2020ws.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

//@Entity
//@Table(name = "vue_stock_produits")
//@Immutable
public class VueStockProduits {

	@Id
	@Column(name = "Row")
	private int row;	
	
	@Column(name = "DESIGNATION")
	private String designation;
	
	@Column(name = "CODE_PRODUIT")
	private String codeProduit;
	
	@Column(name = "QUANTITE")
	private Integer quantite;
	
	@Transient private Integer numDim1;
	@Transient private Integer numDim2;
	@Transient private String Dims1;
	@Transient private String Dims2;
	@Transient private Integer codeDepot;
	@Transient private int numProduit;
	@Transient private String magasin;
	
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
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
}
