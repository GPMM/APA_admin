package br.com.rest.model.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "Turma")
@Table(name = "TURMA")
@XmlRootElement
public class TurmaEntity  implements Serializable {

	private static final long serialVersionUID = 6875426200939558888L;

	@Id
	@GeneratedValue
	private Long idTurma;
	
	@Column(unique=true)
	private String codigo;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name = "REL_TURMA_QUESTIONARIO",
    joinColumns = @JoinColumn(name = "fk_turma"), inverseJoinColumns = @JoinColumn(name = "fk_questionario"))
	private Set<QuestionarioEntity> questionarios;
	
	@ManyToOne
	@JoinColumn(name = "fk_professor")
	private ProfessorEntity professor;
	
	@Column
	private String disciplina;
	
	@ManyToMany
	@JoinTable(name = "REL_TURMA_ALUNO",
		joinColumns = { @JoinColumn(name = "fk_turma") },
		inverseJoinColumns = { @JoinColumn(name = "fk_aluno") })
	private List<AlunoEntity> alunos;

	public Long getIdTurma() {
		return idTurma;
	}

	public void setIdTurma(Long idTurma) {
		this.idTurma = idTurma;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Set<QuestionarioEntity> getQuestionarios() {
		return questionarios;
	}

	public void setQuestionarios(Set<QuestionarioEntity> questionarios) {
		this.questionarios = questionarios;
	}

	public ProfessorEntity getProfessor() {
		return professor;
	}

	public void setProfessor(ProfessorEntity professor) {
		this.professor = professor;
	}

	public String getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public List<AlunoEntity> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<AlunoEntity> alunos) {
		this.alunos = alunos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTurma == null) ? 0 : idTurma.hashCode());
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
		TurmaEntity other = (TurmaEntity) obj;
		if (idTurma == null) {
			if (other.idTurma != null)
				return false;
		} else if (!idTurma.equals(other.idTurma))
			return false;
		return true;
	}

}
