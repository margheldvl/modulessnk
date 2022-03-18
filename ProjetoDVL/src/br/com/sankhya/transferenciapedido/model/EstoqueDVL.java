package br.com.sankhya.transferenciapedido.model;

import java.math.BigDecimal;
import java.sql.Date;

public class EstoqueDVL {
	private Long codemp;
	private Long codlocal;
	private Long codprod;
	private String controle;
	private Double reservado;
	private Double estmin;
	private Double estmax;
	private String ativo;
	private String codbarra;
	private Date dtval;
	private String tipo;
	private Long codparc;
	private Double adestatual;
	private Double percpureza;
	private Double percgermin;
	private Date dtfabricacao;
	private String statuslote;
	private Double estoque;
	private String md5paf;
	private Date dtentrada;
	private Double qtdpedpendest;
	private Double wmsbloqueado;
	private Double percvc;
	private String codagregacao;
	
	transient private String descricaoprod;
	transient BigDecimal qdeBaixa;
	//transient Double qdeBaixa;
	transient Double qdeDiferenca;
	transient private String codvol;
	transient private boolean prodExcluido;
	transient Double vlrunit;
	transient Double vlrtotal;
	
	public Long getCodemp() {
		return codemp;
	}

	public Long getCodlocal() {
		return codlocal;
	}

	public Long getCodprod() {
		return codprod;
	}

	public String getControle() {
		return controle;
	}

	public Double getReservado() {
		return reservado;
	}

	public Double getEstmin() {
		return estmin;
	}

	public Double getEstmax() {
		return estmax;
	}

	public String getAtivo() {
		return ativo;
	}

	public String getCodbarra() {
		return codbarra;
	}

	public Date getDtval() {
		return dtval;
	}

	public String getTipo() {
		return tipo;
	}

	public Long getCodparc() {
		return codparc;
	}

	public Double getAdestatual() {
		return adestatual;
	}

	public Double getPercpureza() {
		return percpureza;
	}

	public Double getPercgermin() {
		return percgermin;
	}

	public Date getDtfabricacao() {
		return dtfabricacao;
	}

	public String getStatuslote() {
		return statuslote;
	}

	public Double getEstoque() {
		return estoque;
	}

	public String getMd5paf() {
		return md5paf;
	}

	public Date getDtentrada() {
		return dtentrada;
	}

	public Double getQtdpedpendest() {
		return qtdpedpendest;
	}

	public Double getWmsbloqueado() {
		return wmsbloqueado;
	}

	public Double getPercvc() {
		return percvc;
	}

	public String getCodagregacao() {
		return codagregacao;
	}

	public void setCodemp(Long codemp) {
		this.codemp = codemp;

	}

	public void setCodlocal(Long codlocal) {
		this.codlocal = codlocal;
	}

	public void setCodprod(Long codprod) {
		this.codprod = codprod;
	}

	public void setControle(String controle) {
		this.controle = controle;
	}

	public void setReservado(Double reservado) {
		this.reservado = reservado;
	}

	public void setEstmin(Double estmin) {
		this.estmin = estmin;
	}

	public void setEstmax(Double estmax) {
		this.estmax = estmax;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public void setCodbarra(String codbarra) {
		this.codbarra = codbarra;
	}

	public void setDtval(Date dtval) {
		this.dtval = dtval;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setCodparc(Long codparc) {
		this.codparc = codparc;
	}

	public void setAdestatual(Double adestatual) {
		this.adestatual = adestatual;
	}

	public void setPercpureza(Double percpureza) {
		this.percpureza = percpureza;
	}

	public void setPercgermin(Double percgermin) {
		this.percgermin = percgermin;
	}

	public void setDtfabricacao(Date dtfabricacao) {
		this.dtfabricacao = dtfabricacao;
	}

	public void setStatuslote(String statuslote) {
		this.statuslote = statuslote;
	}

	public void setEstoque(Double estoque) {
		this.estoque = estoque;
	}

	public void setMd5paf(String md5paf) {
		this.md5paf = md5paf;
	}

	public void setDtentrada(Date dtentrada) {
		this.dtentrada = dtentrada;
	}

	public void setQtdpedpendest(Double qtdpedpendest) {
		this.qtdpedpendest = qtdpedpendest;
	}

	public void setWmsbloqueado(Double wmsbloqueado) {
		this.wmsbloqueado = wmsbloqueado;
	}

	public void setPercvc(Double percvc) {
		this.percvc = percvc;
	}

	public void setCodagregacao(String codagregacao) {
		this.codagregacao = codagregacao;
	}

	public String getDescricaoprod() {
		return descricaoprod;
	}

	public void setDescricaoprod(String descricaoprod) {
		this.descricaoprod = descricaoprod;
	}

	
	public Double getQdeDiferenca() {
		return qdeDiferenca;
	}

	public void setQdeDiferenca(Double qdeDiferenca) {
		this.qdeDiferenca = qdeDiferenca;
	}

	public String getCodvol() {
		return codvol;
	}

	public void setCodvol(String codvol) {
		this.codvol = codvol;
	}

	public boolean isProdExcluido() {
		return prodExcluido;
	}

	public void setProdExcluido(boolean prodExcluido) {
		this.prodExcluido = prodExcluido;
	}

	public Double getVlrtotal() {
		return vlrtotal;
	}

	public void setVlrtotal(Double vlrtotal) {
		this.vlrtotal = vlrtotal;
	}

	public Double getVlrunit() {
		return vlrunit;
	}

	public void setVlrunit(Double vlrunit) {
		this.vlrunit = vlrunit;
	}

	public BigDecimal getQdeBaixa() {
		return qdeBaixa;
	}

	public void setQdeBaixa(BigDecimal qdeBaixa) {
		this.qdeBaixa = qdeBaixa;
	}

}