package br.com.dvl.service;

import java.util.List;

import br.com.dvl.dao.ProgramacaoVisitaDAO;
import br.com.dvl.model.ProgramacaoVisita;

public class ProgramacaoVisitaService {

	ProgramacaoVisitaDAO programacaoVisitaDAO = new ProgramacaoVisitaDAO();

	public boolean ExcluirProgramVisNativeSQLWithBatch(List<ProgramacaoVisita> listaProgramacaoVisita) {
		return programacaoVisitaDAO.ExcluirProgramVisNativeSQLWithBatch(listaProgramacaoVisita);
	}

	public List<String> IncluirProgramVisNativeSQLWithBatch(List<ProgramacaoVisita> listaProgramacaoVisita) {
		return programacaoVisitaDAO.IncluirProgramVisNativeSQLWithBatch(listaProgramacaoVisita);
	}

	public String buscaChave(Long codUsu, String arquivoImportacao, String arquivoLog) throws Exception {
		return programacaoVisitaDAO.buscaChave(codUsu, arquivoImportacao, arquivoLog);
	}

	public boolean insereLog(Long codUsu, String arquivoImportacao, String arquivoLog) throws Exception {
		return programacaoVisitaDAO.insereLog(codUsu, arquivoImportacao, arquivoLog);
	}

}
