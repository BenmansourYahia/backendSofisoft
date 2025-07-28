package ma.sofisoft.cam2020ws.model;

public class UserModel {

	private long numUtilisateur;
	private long numMagasin;
	private String nom;
	private String motPasse;
	private Long numVendeur;
	
	public UserModel(long numUtilisateur, long numMagasin, String nom, String motPasse, Long numVendeur) {
		super();
		this.numUtilisateur = numUtilisateur;
		this.numMagasin = numMagasin;
		this.nom = nom;
		this.motPasse = motPasse;
		this.numVendeur = numVendeur;
	}
	
	
	public long getNumUtilisateur() {
		return numUtilisateur;
	}
	public void setNumUtilisateur(long numUtilisateur) {
		this.numUtilisateur = numUtilisateur;
	}
	public long getNumMagasin() {
		return numMagasin;
	}
	public void setNumMagasin(long numMagasin) {
		this.numMagasin = numMagasin;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getMotPasse() {
		return motPasse;
	}
	public void setMotPasse(String motPasse) {
		this.motPasse = motPasse;
	}
	public Long getNumVendeur() {
		return numVendeur;
	}
	public void setNumVendeur(Long numVendeur) {
		this.numVendeur = numVendeur;
	}	
}
