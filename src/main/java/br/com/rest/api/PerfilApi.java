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

import br.com.rest.model.dto.BuscarPerfilAlunoOut;
import br.com.rest.model.dto.DefaultReturn;
import br.com.rest.model.dto.InserirPerfilIn;
import br.com.rest.model.dto.PerfilAlunoOut;
import br.com.rest.services.AlunoQuestionarioRELServices;

@Path("/perfil")
public class PerfilApi {

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public BuscarPerfilAlunoOut consultarResumo(@QueryParam(value = "questionario") Long idQuestionario, 
								  @QueryParam(value = "matricula") String matricula, 
			  					  @QueryParam(value = "startDate") String startDate,
			  					  @QueryParam(value = "endDate") String endDate,
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
		
		BuscarPerfilAlunoOut resumoEstilos = AlunoQuestionarioRELServices.consultar(idQuestionario, matricula, dataInicio, dataFim, turma);
		return resumoEstilos;
	}
	
	@POST
	@Path("/pontuacao")
	@Produces(MediaType.APPLICATION_JSON)
	public DefaultReturn inserirPontuacaoByQuestionario(InserirPerfilIn estiloAlunoDto) {
		DefaultReturn retorno = validaParametroInserirPontuacaoByQuestionario(estiloAlunoDto);
		if(retorno.getErros() != null && retorno.getErros().size() > 0)
			return retorno;
					
		return AlunoQuestionarioRELServices.inserir(estiloAlunoDto);
	}
	
	@GET
	@Path("/pontuacao/ultimaData")
	@Produces(MediaType.APPLICATION_JSON)
	public DefaultReturn inserirPontuacaoByQuestionario(@QueryParam(value = "matricula") String matricula, @QueryParam(value = "idQuestionario") Long idQuestionario) {
		if(matricula == null || matricula.length() == 0 || idQuestionario == null || idQuestionario <= 0L) {
			DefaultReturn retorno = new DefaultReturn();
			retorno.addErro("Id do aluno e do question�rio s�o par�metros obrigat�rios.");
			return retorno;
		}
							
		return AlunoQuestionarioRELServices.consultarPorUltimaData(matricula, idQuestionario);
	}
	
	private DefaultReturn validaParametroInserirPontuacaoByQuestionario(InserirPerfilIn inserirPerfilIn) {
		DefaultReturn defaultReturn = new DefaultReturn();
		if(inserirPerfilIn.getIdAluno() == null && inserirPerfilIn.getMatriculaAluno() == null) {
			defaultReturn.addErro("Id do aluno e/ou matricula � obrigat�rio.");
		}
		
		if(inserirPerfilIn.getIdQuestionario() == null) {
			defaultReturn.addErro("Id do question�rio � obrigat�rio.");
		}
		
		if(inserirPerfilIn.getDataRealizado() == null) {
			defaultReturn.addErro("Data que o question�rio foi respondido � obrigat�ria.");
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			try {
				dateFormat.parse(inserirPerfilIn.getDataRealizado());
			} catch (ParseException e) {
				defaultReturn.addErro("Formato da data que foi realizado o question�rio � dd-MM-yyyy HH:mm:ss'.");
			}
		}
		
		if(inserirPerfilIn.getPontuacaoPorEstilo() == null) {
			defaultReturn.addErro("Pontua��o por estilo � um par�metro obrigat�rio");
		} else if(inserirPerfilIn.getPontuacaoPorEstilo().keySet().size() == 0){
			defaultReturn.addErro("Pontua��o por estilo n�o pode ser vazio");
		}
		
		return defaultReturn;
	}
}
