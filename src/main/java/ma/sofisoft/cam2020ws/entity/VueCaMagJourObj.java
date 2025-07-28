package ma.sofisoft.cam2020ws.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ma.sofisoft.cam2020ws.CamController;

@Entity
@Table(name = "vue_ca_mag_jour_obj")
@Immutable
public class VueCaMagJourObj {
	
	@Transient
	Logger logger = LoggerFactory.getLogger(CamController.class);
	/*@Id*/
	
	@Column(name = "num_magasin")
	private Integer numMagasin;
	
	@Id
	@Column(name = "code_magasin")
	private String codeMagasin;
	
	@Column(name = "nom_magasin")
	private String nomMagasin;
		
	@Column(name = "montant_ttc")
	private Double montantTTC;
	
	@Column(name = "quantite")
	private Double quantite;
	
	@Column(name = "nbre_ticket")
	private Double nombreTickets;
	
	@Column(name = "jour_vente")
	private Date jourVente;
	
	@Column(name = "Compteur")
	private Double compteur;
	
	@Column(name = "Objectif")
	private Double objectif = 0d;
	
	
	private Double prixMoyen = 0d;
	private Double tauxObjectif = 0d;
	private Double debitMoyen = 0d;
	private Double panierMoyen = 0d;
	
	public VueCaMagJourObj(Integer numMagasin,
			String codeMagasin,
			String nomMagasin,
			Double montantTTC,
			Double quantite, 
			Double nombreTickets, 
			Double compteur,
			Double Objectif) {
		super();
		this.numMagasin = numMagasin;
		this.codeMagasin = codeMagasin;
		this.nomMagasin = nomMagasin;
		this.montantTTC = montantTTC;
		this.quantite = quantite;
		this.nombreTickets = nombreTickets;
		this.compteur = compteur;
		
		if(this.quantite!=null && this.quantite!=0) this.prixMoyen = this.montantTTC/this.quantite;
		if(this.nombreTickets!=null && this.nombreTickets!=0) this.debitMoyen = this.quantite/this.nombreTickets;
		if(this.nombreTickets!=null && this.nombreTickets!=0) this.panierMoyen = this.montantTTC/this.nombreTickets;
		
		/*logger.info("prixMoyen "+prixMoyen);
		logger.info("debitMoyen "+debitMoyen);
		logger.info("panierMoyen "+panierMoyen);*/
		
		if(Objectif!=null && Objectif == 0 && this.montantTTC!=null && this.montantTTC!=0) {
			this.tauxObjectif = this.montantTTC*100/this.montantTTC;
		}else if(Objectif!=null && Objectif!=0) {
			this.tauxObjectif = this.montantTTC*100/Objectif;
		}
		
		//logger.info("tauxObjectif "+this.tauxObjectif);
	}
	
	public VueCaMagJourObj(Integer numMagasin,
			String codeMagasin, 
			String nomMagasin, 
			Double montantTTC, 
			Double quantite,
			Double nombreTickets) {
		super();
		this.numMagasin = numMagasin;
		this.codeMagasin = codeMagasin;
		this.nomMagasin = nomMagasin;
		this.montantTTC = montantTTC;
		this.quantite = quantite;
		this.nombreTickets = nombreTickets;
	}
	
	public VueCaMagJourObj(Date jourVente, 
			Double montantTTC, 
			Double quantite, 
			Double nombreTickets,
			Double Objectif,
			Double compteur) {
		super();
		this.montantTTC = montantTTC;
		this.quantite = quantite;
		this.nombreTickets = nombreTickets;
		this.jourVente = jourVente;
		this.compteur = compteur;
		
		if(this.quantite!=null && this.quantite!=0) this.prixMoyen = this.montantTTC/this.quantite;
		if(this.nombreTickets!=null && this.nombreTickets!=0) this.debitMoyen = this.quantite/this.nombreTickets;
		if(this.nombreTickets!=null && this.nombreTickets!=0) this.panierMoyen = this.montantTTC/this.nombreTickets;
		
		/*logger.info("prixMoyen "+prixMoyen);
		logger.info("debitMoyen "+debitMoyen);
		logger.info("panierMoyen "+panierMoyen);*/
		
		if(Objectif!=null && Objectif == 0 && this.montantTTC!=null && this.montantTTC!=0) {
			this.tauxObjectif = this.montantTTC*100/this.montantTTC;
		}else if(Objectif!=null && Objectif!=0) {
			this.tauxObjectif = this.montantTTC*100/Objectif;
		}
		
		//logger.info("tauxObjectif "+this.tauxObjectif);
	}
	
	public VueCaMagJourObj( Double montantTTC, 
			Double quantite, 
			Double nombreTickets,
			Double Objectif,
			Double compteur) {
		super();
		/*logger.info("montantTTC "+montantTTC);
		logger.info("quantite "+quantite);
		logger.info("nombreTickets "+nombreTickets);
		logger.info("Objectif "+Objectif);
		logger.info("compteur "+compteur);*/
		
		this.montantTTC = montantTTC;
		this.quantite = quantite;
		this.nombreTickets = nombreTickets;
		this.compteur = compteur;
		
		if(this.quantite!=null && this.quantite!=0) this.prixMoyen = this.montantTTC/this.quantite;
		if(this.nombreTickets!=null && this.nombreTickets!=0) this.debitMoyen = this.quantite/this.nombreTickets;
		if(this.nombreTickets!=null && this.nombreTickets!=0) this.panierMoyen = this.montantTTC/this.nombreTickets;
		
		/*logger.info("prixMoyen "+prixMoyen);
		logger.info("debitMoyen "+debitMoyen);
		logger.info("panierMoyen "+panierMoyen);*/
		
		if(Objectif!=null && Objectif == 0 && this.montantTTC!=null && this.montantTTC!=0) {
			this.tauxObjectif = this.montantTTC*100/this.montantTTC;
		}else if(Objectif!=null && Objectif!=0) {
			this.tauxObjectif = this.montantTTC*100/Objectif;
		}
		
		//logger.info("tauxObjectif "+this.tauxObjectif);
	}
	
	public Double getPrixMoyen() {
		return prixMoyen;
	}

	public void setPrixMoyen(Double prixMoyen) {
		this.prixMoyen = prixMoyen;
	}

	public Double getTauxObjectif() {
		return tauxObjectif;
	}

	public void setTauxObjectif(Double tauxObjectif) {
		this.tauxObjectif = tauxObjectif;
	}

	public Double getDebitMoyen() {
		return debitMoyen;
	}

	public void setDebitMoyen(Double debitMoyen) {
		this.debitMoyen = debitMoyen;
	}

	public Double getPanierMoyen() {
		return panierMoyen;
	}

	public void setPanierMoyen(Double panierMoyen) {
		this.panierMoyen = panierMoyen;
	}
			
	public String getNomMagasin() {
		return nomMagasin;
	}
	public void setNomMagasin(String nomMagasin) {
		this.nomMagasin = nomMagasin;
	}
	
	public Integer getNumMagasin() {
		return numMagasin;
	}
	public void setNumMagasin(Integer numMagasin) {
		this.numMagasin = numMagasin;
	}
	
	public String getCodeMagasin() {
		return codeMagasin;
	}
	public void setCodeMagasin(String codeMagasin) {
		this.codeMagasin = codeMagasin;
	}
	public Double getMontantTTC() {
		return montantTTC;
	}
	public void setMontantTTC(Double montantTTC) {
		this.montantTTC = montantTTC;
	}
	public Date getJourVente() {
		return jourVente;
	}
	public void setJourVente(Date jourVente) {
		this.jourVente = jourVente;
	}
	public Double getQuantite() {
		return quantite;
	}
	public void setQuantite(Double quantite) {
		this.quantite = quantite;
	}
	public Double getNombreTickets() {
		return nombreTickets;
	}
	public void setNombreTickets(Double nombreTickets) {
		this.nombreTickets = nombreTickets;
	}

	public Double getCompteur() {
		return compteur;
	}

	public void setCompteur(Double compteur) {
		this.compteur = compteur;
	}

	public Double getObjectif() {
		return objectif;
	}

	public void setObjectif(Double objectif) {
		this.objectif = objectif;
	}
	
	
}
