package br.com.sankhya.pamcard.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Viagem {

	Contratante contratante;
	Unidade unidade;

	private Integer favorecidoQtde;
	private List<Favorecido> listFavorecido = new ArrayList<Favorecido>();
	Contrato contrato;
	private Long idCliente;
	private Date dataPartida;
	private Date dataTermino;
	private Integer veiculoQtde;
	private List<Veiculo> listVeiculo = new ArrayList<Veiculo>();
	private Double pedagioValor;
	private Integer distanciaKM;
	private Carga carga;
	private Integer documentoQtde;
	private List<DocumentoViagem> listDocumentoViagem = new ArrayList<DocumentoViagem>();
	private Integer documentoComplementarQtde;
	private List<Integer> listDocumentoComplementarTipo;
	private Cidade origemCidade;
	private Cidade destinoCidade;
	private Veiculo categoriaVeiculo;
	private Integer pedagioSolucaoId;
	private Frete frete;
	private Integer parcelaQtde;
	private List<Parcela> listParcela = new ArrayList<Parcela>();
	private String veiculoAltoDesempenhoIndicador;	

	private String cargaDestinacaoComercialIndicador;
	private String retornoFreteIndicador;
	private Integer retornoCep;
	private Integer retornoDistanciaKm;
	private CIOT ciot;
	private String contratacaoTipo;
	List<PessoaFiscal> listPessoaFiscal = new ArrayList<PessoaFiscal>();
	
	public Frete getFrete() {
		return frete;
	}

	public void setFrete(Frete frete) {
		this.frete = frete;
	}

	public Contratante getContratante() {
		return contratante;
	}

	public void setContratante(Contratante contratante) {
		this.contratante = contratante;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public Integer getFavorecidoQtde() {
		return favorecidoQtde;
	}

	public void setFavorecidoQtde(Integer favorecidoQtde) {
		this.favorecidoQtde = favorecidoQtde;
	}

	public List<Favorecido> getListFavorecido() {
		return listFavorecido;
	}

	public void setListFavorecido(List<Favorecido> listFavorecido) {
		this.listFavorecido = listFavorecido;
		this.favorecidoQtde = this.listFavorecido.size();
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Date getDataPartida() {
		return dataPartida;
	}

	public void setDataPartida(Date dataPartida) {
		this.dataPartida = dataPartida;
	}

	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public Integer getVeiculoQtde() {
		return veiculoQtde;
	}

	public void setVeiculoQtde(Integer veiculoQtde) {
		this.veiculoQtde = veiculoQtde;
	}

	public List<Veiculo> getListVeiculo() {
		return listVeiculo;
	}

	public void setListVeiculo(List<Veiculo> listVeiculo) {
		this.listVeiculo = listVeiculo;
	}

	public Double getPedagioValor() {
		return pedagioValor;
	}

	public void setPedagioValor(Double pedagioValor) {
		this.pedagioValor = pedagioValor;
	}

	public Integer getDistanciaKM() {
		return distanciaKM;
	}

	public void setDistanciaKM(Integer distanciaKM) {
		this.distanciaKM = distanciaKM;
	}

	public Carga getCarga() {
		return carga;
	}

	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	public Integer getDocumentoQtde() {
		return documentoQtde;
	}

	public void setDocumentoQtde(Integer documentoQtde) {
		this.documentoQtde = documentoQtde;
	}

	public List<DocumentoViagem> getListDocumentoViagem() {
		return listDocumentoViagem;
	}

	public void setListDocumentoViagem(List<DocumentoViagem> listDocumentoViagem) {
		this.listDocumentoViagem = listDocumentoViagem;
	}

	public Integer getDocumentoComplementarQtde() {
		return documentoComplementarQtde;
	}

	public List<Integer> getListDocumentoComplementarTipo() {
		return listDocumentoComplementarTipo;
	}

	public void setListDocumentoComplementarTipo(List<Integer> listDocumentoComplementarTipo) {
		this.listDocumentoComplementarTipo = listDocumentoComplementarTipo;
		this.documentoComplementarQtde = this.listDocumentoComplementarTipo.size();
	}

	public void setDocumentoComplementarQtde(Integer documentoComplementarQtde) {
		this.documentoComplementarQtde = documentoComplementarQtde;
	}

	public Cidade getOrigemCidade() {
		return origemCidade;
	}

	public void setOrigemCidade(Cidade origemCidade) {
		this.origemCidade = origemCidade;
	}

	public Cidade getDestinoCidade() {
		return destinoCidade;
	}

	public void setDestinoCidade(Cidade destinoCidade) {
		this.destinoCidade = destinoCidade;
	}

	public Veiculo getCategoriaVeiculo() {
		return categoriaVeiculo;
	}

	public void setCategoriaVeiculo(Veiculo categoriaVeiculo) {
		this.categoriaVeiculo = categoriaVeiculo;
	}

	public Integer getPedagioSolucaoId() {
		return pedagioSolucaoId;
	}

	public void setPedagioSolucaoId(Integer pedagioSolucaoId) {
		this.pedagioSolucaoId = pedagioSolucaoId;
	}

	public Integer getParcelaQtde() {
		return parcelaQtde;
	}

	public void setParcelaQtde(Integer parcelaQtde) {
		this.parcelaQtde = parcelaQtde;
	}

	public List<Parcela> getListParcela() {
		return listParcela;
	}

	public void setListParcela(List<Parcela> listParcela) {
		this.listParcela = listParcela;
		this.parcelaQtde = this.listParcela.size();
	}

	public String getVeiculoAltoDesempenhoIndicador() {
		return veiculoAltoDesempenhoIndicador;
	}

	public void setVeiculoAltoDesempenhoIndicador(String veiculoAltoDesempenhoIndicador) {
		this.veiculoAltoDesempenhoIndicador = veiculoAltoDesempenhoIndicador;
	}

	public String getCargaDestinacaoComercialIndicador() {
		return cargaDestinacaoComercialIndicador;
	}

	public void setCargaDestinacaoComercialIndicador(String cargaDestinacaoComercialIndicador) {
		this.cargaDestinacaoComercialIndicador = cargaDestinacaoComercialIndicador;
	}

	public String getRetornoFreteIndicador() {
		return retornoFreteIndicador;
	}

	public void setRetornoFreteIndicador(String retornoFreteIndicador) {
		this.retornoFreteIndicador = retornoFreteIndicador;
	}

	public Integer getRetornoCep() {
		return retornoCep;
	}

	public void setRetornoCep(Integer retornoCep) {
		this.retornoCep = retornoCep;
	}

	public Integer getRetornoDistanciaKm() {
		return retornoDistanciaKm;
	}

	public void setRetornoDistanciaKm(Integer retornoDistanciaKm) {
		this.retornoDistanciaKm = retornoDistanciaKm;
	}

	public CIOT getCiot() {
		return ciot;
	}

	public void setCiot(CIOT ciot) {
		this.ciot = ciot;
	}

	public String getContratacaoTipo() {
		return contratacaoTipo;
	}

	public void setContratacaoTipo(String contratacaoTipo) {
		this.contratacaoTipo = contratacaoTipo;
	}

	public List<PessoaFiscal> getListPessoaFiscal() {
		return listPessoaFiscal;
	}

	public void setListPessoaFiscal(List<PessoaFiscal> listPessoaFiscal) {
		this.listPessoaFiscal = listPessoaFiscal;
	}

}
