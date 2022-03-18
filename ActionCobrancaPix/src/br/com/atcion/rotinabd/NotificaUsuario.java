package br.com.atcion.rotinabd;

import java.sql.Date;

public class NotificaUsuario {
	private Long nuaviso;
	private String titulo;
	private String descricao;
	private String solucao;
	private String identificador;
	private Long importancia;
	private Long codusu;
	private Long codgrupo;
	private String tipo;
	private Date dhcriacao;
	private Long codusuremetente;
	private Long nuavisopai;
	private Date dtexpiracao;
	private Date dtnotificacao;
	private Long ordem;

	public Long getNuaviso() {
		return nuaviso;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getSolucao() {
		return solucao;
	}

	public String getIdentificador() {
		return identificador;
	}

	public Long getImportancia() {
		return importancia;
	}

	public Long getCodusu() {
		return codusu;
	}

	public Long getCodgrupo() {
		return codgrupo;
	}

	public String getTipo() {
		return tipo;
	}

	public Date getDhcriacao() {
		return dhcriacao;
	}

	public Long getCodusuremetente() {
		return codusuremetente;
	}

	public Long getNuavisopai() {
		return nuavisopai;
	}

	public Date getDtexpiracao() {
		return dtexpiracao;
	}

	public Date getDtnotificacao() {
		return dtnotificacao;
	}

	public Long getOrdem() {
		return ordem;
	}

	public void setNuaviso(Long nuaviso) {
		this.nuaviso = nuaviso;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setSolucao(String solucao) {
		this.solucao = solucao;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public void setImportancia(Long importancia) {
		this.importancia = importancia;
	}

	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}

	public void setCodgrupo(Long codgrupo) {
		this.codgrupo = codgrupo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setDhcriacao(Date dhcriacao) {
		this.dhcriacao = dhcriacao;
	}

	public void setCodusuremetente(Long codusuremetente) {
		this.codusuremetente = codusuremetente;
	}

	public void setNuavisopai(Long nuavisopai) {
		this.nuavisopai = nuavisopai;
	}

	public void setDtexpiracao(Date dtexpiracao) {
		this.dtexpiracao = dtexpiracao;
	}

	public void setDtnotificacao(Date dtnotificacao) {
		this.dtnotificacao = dtnotificacao;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}
}