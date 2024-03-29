package br.com.rest.api;

import javax.net.ssl.HttpsURLConnection;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.rest.services.TurmaServices;

@Path("/turma")
public class TurmaApi {

	@GET
	@Path("filtro")
	@Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
	public Response findTurmasFiltroProf(@QueryParam(value = "idProfessor") Long idProfessor) {
		try {
			return Response.ok().entity(TurmaServices.buscarTurmasFiltroProfessorSimplified(idProfessor)).build();
		} catch (NoResultException e) {
			return Response.status(HttpsURLConnection.HTTP_NO_CONTENT).build();
		} catch (Exception e) {
			return Response.status(HttpsURLConnection.HTTP_INTERNAL_ERROR).build();
		}
	}
	
	@GET
	@Path("inclui/{codigoTurma}/aluno/{matricula}")
	@Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
	public Response incluiAlunoTurma(@PathParam(value = "codigoTurma") String codigoTurma,
										  @PathParam(value = "matricula") String matricula) {
		try{
			TurmaServices.incluiAlunoTurma(matricula, codigoTurma);
			return Response.status(HttpsURLConnection.HTTP_NO_CONTENT).build();
		} catch (IllegalStateException i) {
			return Response.status(HttpsURLConnection.HTTP_CONFLICT).build();
		} catch (Exception e) {
			return Response.status(HttpsURLConnection.HTTP_INTERNAL_ERROR).build();
		}
		
	}
	

}
