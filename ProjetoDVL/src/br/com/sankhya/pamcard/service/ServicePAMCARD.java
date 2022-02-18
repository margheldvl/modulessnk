package br.com.sankhya.pamcard.service;

import java.util.List;
import java.util.Map;

import br.com.sankhya.pamcard.dao.ContratoFrete;
import br.com.sankhya.pamcard.model.CIOT;
import br.com.sankhya.pamcard.model.DocumentoViagem;
import br.com.sankhya.pamcard.model.Favorecido;
import br.com.sankhya.pamcard.model.Frete;
import br.com.sankhya.pamcard.model.Parcela;
import br.com.sankhya.pamcard.model.PessoaFiscal;
import br.com.sankhya.pamcard.model.Veiculo;
import br.com.sankhya.pamcard.model.Viagem;

public class ServicePAMCARD {

	public Viagem carregaViagem(Long ciotNum) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.carregaViagem(ciotNum);
	}

	public List<CIOT> pesquisaCiotLiberado(String status) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.pesquisaCiotLiberado(status);

	}

	public List<Favorecido> buscaFavorecido(String NuCIOT) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.buscaFavorecido(NuCIOT);

	}

	public List<Veiculo> buscaVeiculo(String NuCIOT) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.buscaVeiculo(NuCIOT);

	}

	public Viagem buscaVIA(String NuCIOT) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.buscaVIA(NuCIOT);

	}

	public List<DocumentoViagem> buscaDocumentoViagem(Long numCIOT) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.buscaDocumentoViagem(numCIOT);

	}

	public List<PessoaFiscal> buscaPessoaFiscal(String numCIOT) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.buscaPessoaFiscal(numCIOT);

	}

	public List<Parcela> buscaParcela(Long numCIOT) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.buscaParcela(numCIOT);

	}

	public Frete buscaFrete(Long numCIOT) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.buscaFrete(numCIOT);

	}

	public List<CIOT> buscaListaCiotPorSttus(String status) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.buscaListaCiotPorSttus(status);

	}
	
	public String atualizaDadosIntegracaoCIOT(Map<String,String> mapLogCIOT) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.atualizaDadosIntegracaoCIOT(mapLogCIOT);		
	}
	
	public String atualizaCIOT(List<CIOT> listCIOT) throws Exception {
		ContratoFrete contratoFrete = new ContratoFrete();
		return contratoFrete.atualizaCIOT(listCIOT);		
	}

}
