package br.com.rest.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;

import br.com.rest.model.dao.EstiloAlunoDAO;
import br.com.rest.model.dao.PersistenceManager;
import br.com.rest.model.dto.DefaultReturn;
import br.com.rest.model.dto.EstiloAlunoIn;
import br.com.rest.model.dto.EstiloAlunoOut;
import br.com.rest.model.dto.EstiloDTO;
import br.com.rest.model.entity.AlunoEntity;
import br.com.rest.model.entity.EstiloAlunoREL;
import br.com.rest.model.entity.EstiloEntity;
import br.com.rest.model.entity.QuestionarioEntity;

public class EstiloAlunoServices {

	private static EstiloAlunoDAO estiloAlunoDao = EstiloAlunoDAO.getInstance();

	public static Set<EstiloAlunoOut> consultar(Long idQuestionario, String matricula, Date startDate, Date endDate, String nivel,
			String turma) {
		Set<EstiloAlunoREL> resumoEstiloAlunos = null;
		Set<EstiloAlunoOut> resumoEstiloAlunosDTO = null;
		try {
			resumoEstiloAlunos = estiloAlunoDao.dynamicQueryFiltro(idQuestionario, matricula, startDate, endDate, nivel, turma);
			resumoEstiloAlunosDTO = new HashSet<EstiloAlunoOut>();
			for (EstiloAlunoREL estiloAluno : resumoEstiloAlunos) {
				resumoEstiloAlunosDTO.add(entityToDto(estiloAluno));
			}

			return resumoEstiloAlunosDTO;
		} catch (NoResultException e) {
			e.printStackTrace();
			return resumoEstiloAlunosDTO;
		}
	}
	
	public static DefaultReturn inserir(EstiloAlunoIn estiloDto) {
		DefaultReturn retornoPadrao = new DefaultReturn();
		EstiloAlunoREL estiloAluno = null;
		PersistenceManager.getTransaction().begin();

		try {
			estiloAluno = dtoToEntity(estiloDto);
			estiloAlunoDao.incluir(estiloAluno);
			PersistenceManager.getTransaction().commit();
			retornoPadrao.setMsg("Pontuacao do aluno inserida com sucesso.");
			return retornoPadrao;
		} catch (Exception e) {
			PersistenceManager.getTransaction().rollback();
			e.printStackTrace();
			return null;
		}
	}

	public static EstiloAlunoOut entityToDto(EstiloAlunoREL estiloAluno) { //TODO colocar as pontuacoes por estilo
		EstiloAlunoOut estiloAlunoDto = new EstiloAlunoOut();

		if (estiloAluno.getAluno() != null) {
			estiloAlunoDto.setIdAluno(estiloAluno.getAluno().getId());
			estiloAlunoDto.setMatriculaAluno(estiloAluno.getAluno().getMatricula());
			estiloAlunoDto.setNomeAluno(estiloAluno.getAluno().getNome());
		}
		
		if(estiloAluno.getQuestionario() != null) {
			estiloAlunoDto.setIdQuestionario(estiloAluno.getQuestionario().getIdQuestionario());
			estiloAlunoDto.setNomeQuestionario(estiloAluno.getQuestionario().getNome());
			EstiloDTO estiloDto;
			for(EstiloEntity estiloEntity : estiloAluno.getQuestionario().getEstilos()) {
				estiloDto = EstiloServices.entityToDto(estiloEntity);
				estiloAlunoDto.addEstilo(estiloDto);
			}
		}
		
		estiloAlunoDto.setDataRealizado(estiloAluno.getDataRealizado());
		estiloAlunoDto.setIdPerfil(estiloAluno.getIdPerfil());
		
		if(estiloAluno.getPontuacaoPorEstilo() != null && estiloAluno.getPontuacaoPorEstilo().keySet() != null && estiloAluno.getPontuacaoPorEstilo().keySet().size() > 0) {
			Map<EstiloDTO, Long> pontuacaoPorEstiloDto = new HashMap<EstiloDTO, Long>();
			for(EstiloEntity estilo: estiloAluno.getPontuacaoPorEstilo().keySet()) {
				EstiloDTO estiloDto = EstiloServices.entityToDto(estilo);
				pontuacaoPorEstiloDto.put(estiloDto, estiloAluno.getPontuacaoPorEstilo().get(estilo));
			}
		}

		return estiloAlunoDto;
	}
	
	public static EstiloAlunoREL dtoToEntity(EstiloAlunoIn estiloAlunoDto) throws IllegalArgumentException{ //TODO colocar as pontuacoes por estilo
		if(estiloAlunoDto != null && (estiloAlunoDto.getIdAluno() != null || estiloAlunoDto.getMatriculaAluno() != null) && estiloAlunoDto.getIdQuestionario() != null) {
			EstiloAlunoREL estiloAlunoRel = new EstiloAlunoREL();
			AlunoEntity aluno = null;
			QuestionarioEntity questionario = null;
			
			if(estiloAlunoDto.getIdAluno() != null) {
				aluno = AlunoServices.findAlunoById(estiloAlunoDto.getIdAluno());
			} else if(estiloAlunoDto.getMatriculaAluno() != null){
				aluno = AlunoServices.findAlunoByMatricula(estiloAlunoDto.getMatriculaAluno());
			}
			
			if(aluno == null) throw new IllegalArgumentException("Aluno inexistente no banco de dados.");
			
			estiloAlunoRel.setAluno(aluno);
			
			if(estiloAlunoDto.getIdQuestionario() != null) {
				questionario = QuestionarioServices.findQuestionariosById(estiloAlunoDto.getIdQuestionario());
			} else {
				return null;
			}
			
			if(questionario == null) throw new IllegalArgumentException("Questionário inexistente no banco de dados.");
			
			estiloAlunoRel.setQuestionario(questionario);
			
			if(estiloAlunoDto.getDataRealizado() != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				Date dataFormatada;
				try {
					dataFormatada = dateFormat.parse(estiloAlunoDto.getDataRealizado());
				} catch (ParseException e) {
					throw new IllegalArgumentException("Formato de data errado, deve ser 'dd-MM-yyyy HH:mm:ss'.");
				}
				estiloAlunoRel.setDataRealizado(dataFormatada);
			}
			
			//TODO acertar a pontuacao por estilo
			
			if(estiloAlunoDto.getPontuacaoPorEstilo() != null && estiloAlunoDto.getPontuacaoPorEstilo().keySet() != null && estiloAlunoDto.getPontuacaoPorEstilo().keySet().size() > 0) {
				Map<EstiloEntity, Long> pontuacaoPorEstilo = new HashMap<EstiloEntity, Long>();
				for(Long estiloId: estiloAlunoDto.getPontuacaoPorEstilo().keySet()) {
					EstiloEntity estiloEntity = EstiloServices.findEstiloById(estiloId);
					pontuacaoPorEstilo.put(estiloEntity, estiloAlunoDto.getPontuacaoPorEstilo().get(estiloId));
				}
				estiloAlunoRel.setPontuacaoPorEstilo(pontuacaoPorEstilo);
			}
			
			return estiloAlunoRel;
		} else {
			return null;
		}
	}
	
	
}
