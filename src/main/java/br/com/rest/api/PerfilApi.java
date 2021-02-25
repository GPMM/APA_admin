package br.com.rest.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.rest.model.dto.DefaultReturn;
import br.com.rest.model.dto.EstiloAlunoIn;
import br.com.rest.model.dto.EstiloAlunoOut;
import br.com.rest.model.entity.EstiloAlunoREL;
import br.com.rest.services.EstiloAlunoServices;

@Path("/perfil")
public class PerfilApi {

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<EstiloAlunoOut> consultarResumo(@QueryParam(value = "questionario") Long idQuestionario, 
								  @QueryParam(value = "matricula") String matricula, 
			  					  @QueryParam(value = "startDate") String startDate,
			  					  @QueryParam(value = "endDate") String endDate,
								  @QueryParam(value = "nivel") String nivel, 
								  @QueryParam(value = "turma") String turma) {
		if (idQuestionario == null) {
		    throw new WebApplicationException(
		      Response.status(Response.Status.BAD_REQUEST)
		        .entity("Par�metro question�rio � obrigat�rio")
		        .build()
		    );
		  }
		
		Date dataInicio = null, dataFim = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		if(startDate != null) {
			try {
				dataInicio = format.parse(startDate);
			} catch (ParseException e) {
				System.out.println("Erro ao transformar data inicial");
			}
		}
		
		if(endDate != null) {
			try {
				dataFim = format.parse(endDate);
			} catch (ParseException e) {
				System.out.println("Erro ao transformar data final");
			}
		}
		
		Set<EstiloAlunoOut> resumoEstilos = EstiloAlunoServices.consultar(idQuestionario, matricula, dataInicio, dataFim, nivel, turma);
		return resumoEstilos;
	}
	
	@POST
	@Path("/pontuacao")
	@Produces(MediaType.APPLICATION_JSON)
	public DefaultReturn inserirPontuacaoByQuestionario(EstiloAlunoIn estiloAlunoDto) {
		DefaultReturn retorno = validaParametroInserirPontuacaoByQuestionario(estiloAlunoDto);
		if(retorno.getErros() != null && retorno.getErros().size() > 0)
			return retorno;
					
		return EstiloAlunoServices.inserir(estiloAlunoDto);
	}
	
	private DefaultReturn validaParametroInserirPontuacaoByQuestionario(EstiloAlunoIn estiloAlunoIn) {
		DefaultReturn defaultReturn = new DefaultReturn();
		if(estiloAlunoIn.getIdAluno() == null && estiloAlunoIn.getMatriculaAluno() == null) {
			defaultReturn.addErro("Id do aluno e/ou matricula � obrigat�rio.");
		}
		
		if(estiloAlunoIn.getIdQuestionario() == null) {
			defaultReturn.addErro("Id do question�rio � obrigat�rio.");
		}
		
		if(estiloAlunoIn.getDataRealizado() == null) {
			defaultReturn.addErro("Data que o question�rio foi respondido � obrigat�ria.");
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			try {
				dateFormat.parse(estiloAlunoIn.getDataRealizado());
			} catch (ParseException e) {
				defaultReturn.addErro("Formato da data que foi realizado o question�rio � dd-MM-yyyy HH:mm:ss'.");
			}
		}
		
		if(estiloAlunoIn.getPontuacaoPorEstilo() == null) {
			defaultReturn.addErro("Pontua��o por estilo � um par�metro obrigat�rio");
		} else if(estiloAlunoIn.getPontuacaoPorEstilo().keySet().size() == 0){
			defaultReturn.addErro("Pontua��o por estilo n�o pode ser vazio");
		}
		
		return defaultReturn;
	}
}
