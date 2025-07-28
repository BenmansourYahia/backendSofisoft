package ma.sofisoft.cam2020ws.entity;

import javax.persistence.Embeddable;

@Embeddable
public class ParametreId implements java.io.Serializable {

	private String module;	
	private String parametre;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getParametre() {
		return parametre;
	}

	public void setParametre(String parametre) {
		this.parametre = parametre;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof LineVenteDirectId))
			return false;
		ParametreId castOther = (ParametreId) other;

		return (this.getParametre().equals(castOther.getParametre()))
				&& (this.getModule() == castOther.getModule());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.parametre.hashCode();
		result = 37 * result + this.module.hashCode();
		return result;
	}
}
