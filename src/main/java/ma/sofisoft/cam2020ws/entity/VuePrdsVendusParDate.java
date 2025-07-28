package ma.sofisoft.cam2020ws.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vue_prds_vendus_par_date")
@Immutable
public class VuePrdsVendusParDate {
	
	@EmbeddedId
	private VuePrdsId id; //

	@Column(name = "num_magasin")
	private int numMagasin;
	
	@Column(name = "code_produit_dims", insertable = false,updatable = false ,nullable = false)
	private String codeProduitDims;
	
	@Column(name = "code_produit_gen")
	private String codeProduitGen;
	
	@Column(name = "designation", insertable = false,updatable = false ,nullable = false)
	private String designation;
	
	@Column(name = "quantite")
	private long quantite;
	
	@Column(name = "date_Mvt", insertable = false,updatable = false ,nullable = false)
	private Date date;
	
	@Column(name = "prix_vente")
	private double prixVente;
	
	@Column(name = "total")
	private double total;
	
	@Column(name = "libelle_dim1")
	private String libelleDim1;
	
	@Column(name = "libelle_dim2")
	private String libelleDim2;
	
	@Column(name = "num_produit")
	private long numProduit;
	
	public VuePrdsVendusParDate(long numProduit,String codeProduitGen,String designation,long quantite, double total,double prixVente) {
		this.codeProduitGen = codeProduitGen;
		this.designation = designation;
		this.quantite = quantite;
		this.total = total;
		this.prixVente = prixVente;
		this.numProduit = numProduit;
	}	
	
	public VuePrdsVendusParDate(long numProduit, String codeProduitDims,String designation,long quantite, double total,double prixVente,String dim1,String dim2) {
		this.numProduit = numProduit;
		this.codeProduitDims = codeProduitDims;
		this.designation = designation;
		this.quantite = quantite;
		this.total = total;
		this.prixVente = prixVente;
		this.libelleDim1 = dim1;
		this.libelleDim2 = dim2;
	}
	
	public VuePrdsVendusParDate(long numProduit,String codeProduitGen, String codeProduitDims,String designation,long quantite, double total,double prixVente,String dim1,String dim2) {
		this.numProduit = numProduit;
		this.codeProduitDims = codeProduitDims;
		this.codeProduitGen = codeProduitGen;
		this.designation = designation;
		this.quantite = quantite;
		this.total = total;
		this.prixVente = prixVente;
		this.libelleDim1 = dim1;
		this.libelleDim2 = dim2;
	}
	
	public String getCodeProduitGen() {
		return codeProduitGen;
	}

	public void setCodeProduitGen(String codeProduitGen) {
		this.codeProduitGen = codeProduitGen;
	}

	public String getLibelleDim1() {
		return libelleDim1;
	}

	public void setLibelleDim1(String libelleDim1) {
		this.libelleDim1 = libelleDim1;
	}

	public String getLibelleDim2() {
		return libelleDim2;
	}

	public void setLibelleDim2(String libelleDim2) {
		this.libelleDim2 = libelleDim2;
	}


	public long getNumProduit() {
		return numProduit;
	}

	public void setNumProduit(long numProduit) {
		this.numProduit = numProduit;
	}

	public VuePrdsId getId() {
		return id;
	}

	public void setId(VuePrdsId id) {
		this.id = id;
	}

	public int getNumMagasin() {
		return numMagasin;
	}

	public void setNumMagasin(int numMagasin) {
		this.numMagasin = numMagasin;
	}

	public String getCodeProduitDims() {
		return codeProduitDims;
	}

	public void setCodeProduitDims(String codeProduit) {
		this.codeProduitDims = codeProduit;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public long getQuantite() {
		return quantite;
	}

	public void setQuantite(long quantite) {
		this.quantite = quantite;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}


	public double getPrixVente() {
		return prixVente;
	}


	public void setPrixVente(double prixVente) {
		this.prixVente = prixVente;
	}
}
