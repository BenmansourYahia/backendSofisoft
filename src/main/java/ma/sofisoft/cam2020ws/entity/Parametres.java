package ma.sofisoft.cam2020ws.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "parametres")
public class Parametres {	
	
	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "module", column = @Column(name = "module", nullable = false, precision = 10, scale = 0)),
		@AttributeOverride(name = "parametre", column = @Column(name = "parametre", nullable = false, precision = 10, scale = 0)) })
	private ParametreId id;
	
	@Column(name = "valeur")
	private String valeur;
	
	@Column(name = "types")
	private Integer types;
			
	public ParametreId getId() {
		return id;
	}

	public void setId(ParametreId id) {
		this.id = id;
	}

	public String getValeur() {
		return valeur;
	}
	
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
	
	public Integer getType() {
		return types;
	}
	
	public void setType(Integer type) {
		this.types = type;
	}
}
