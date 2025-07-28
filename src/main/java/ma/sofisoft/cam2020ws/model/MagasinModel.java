package ma.sofisoft.cam2020ws.model;

public class MagasinModel {

	//select vue.numMagasin, vue.codeMagasin, vue.nomMagasin, sum(vue.montantTTC), SUM(vue.quantite), SUM(vue.nombreTickets)
	
	private long numMagasin;
	private String codeMagasin;
	private String nomMagasin;
	
	public MagasinModel(long numMagasin, String codeMagasin, String nomMagasin) {
		super();
		this.numMagasin = numMagasin;
		this.codeMagasin = codeMagasin;
		this.nomMagasin = nomMagasin;
	}
	
	public String getCodeMagasin() {
		return codeMagasin;
	}
	public void setCodeMagasin(String codeMagasin) {
		this.codeMagasin = codeMagasin;
	}
	public long getNumMagasin() {
		return numMagasin;
	}
	public void setNumMagasin(long numMagasin) {
		this.numMagasin = numMagasin;
	}
	public String getNomMagasin() {
		return nomMagasin;
	}
	public void setNomMagasin(String nomMagasin) {
		this.nomMagasin = nomMagasin;
	}
}
