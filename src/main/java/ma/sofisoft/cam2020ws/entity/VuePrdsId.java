package ma.sofisoft.cam2020ws.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VuePrdsId implements java.io.Serializable {
	
	@Column(name = "code_produit_dims")
	private String codeProduitDims;
	
	@Column(name = "designation")
	private String designation;
	
	@Column(name = "date_Mvt")
	private Date date;

	public String getCodeProduit() {
		return codeProduitDims;
	}

	public void setCodeProduit(String codeProduit) {
		this.codeProduitDims = codeProduit;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof StkVenteDirecteId))
			return false;
		VuePrdsId castOther = (VuePrdsId) other;

		return (this.getCodeProduit().equals(castOther.getCodeProduit()) && 
				(this.getDesignation().equals(castOther.getDesignation())) &&
				(this.getDate().compareTo(castOther.getDate()) == 0));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCodeProduit().hashCode();
		result = 37 * result + (int) this.getDesignation().hashCode();
		result = 37 * result + (int) this.getDate().hashCode();
		return result;
	}
}
