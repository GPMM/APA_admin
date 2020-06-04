package br.com.rest.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "AlunoQuestionarioPerfil")
@Table(name = "REL_ALUNO_QUESTIONARIO")
@XmlRootElement
public class AlunoQuestionarioPerfilEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer idPerfil;
	
	@ManyToOne
	@JoinColumn(name = "fk_aluno")
	private AlunoEntity aluno;
	
	@ManyToOne
	@JoinColumn(name = "fk_questionario")
	private QuestionarioEntity questionario;
	
	@Column
	private Integer perfilAtivo;
	
	@Column
	private Integer perfilReflexivo;
	
	@Column
	private Integer perfilPragmatico;
	
	@Column
	private Integer perfilTeorico;
	
	public Integer getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public AlunoEntity getAluno() {
		return aluno;
	}

	public void setAluno(AlunoEntity aluno) {
		this.aluno = aluno;
	}

	public Integer getPerfilAtivo() {
		return perfilAtivo;
	}

	public void setPerfilAtivo(Integer perfilAtivo) {
		this.perfilAtivo = perfilAtivo;
	}

	public Integer getPerfilReflexivo() {
		return perfilReflexivo;
	}

	public void setPerfilReflexivo(Integer perfilReflexivo) {
		this.perfilReflexivo = perfilReflexivo;
	}

	public Integer getPerfilPragmatico() {
		return perfilPragmatico;
	}

	public void setPerfilPragmatico(Integer perfilPragmatico) {
		this.perfilPragmatico = perfilPragmatico;
	}

	public Integer getPerfilTeorico() {
		return perfilTeorico;
	}

	public void setPerfilTeorico(Integer perfilTeorico) {
		this.perfilTeorico = perfilTeorico;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPerfil == null) ? 0 : idPerfil.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlunoQuestionarioPerfilEntity other = (AlunoQuestionarioPerfilEntity) obj;
		if (idPerfil == null) {
			if (other.idPerfil != null)
				return false;
		} else if (!idPerfil.equals(other.idPerfil))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AlunoQuestionarioEntity [idPerfil=" + idPerfil + ", aluno=" + aluno + ", questionario=" + questionario
				+ ", perfilAtivo=" + perfilAtivo + ", perfilReflexivo=" + perfilReflexivo + ", perfilPragmatico="
				+ perfilPragmatico + ", perfilTeorico=" + perfilTeorico + "]";
	}

}
