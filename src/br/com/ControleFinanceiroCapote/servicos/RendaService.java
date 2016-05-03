package br.com.ControleFinanceiroCapote.servicos;

import java.sql.Connection;
import java.util.List;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbc.JDBCRendaDAO;
import br.com.ControleFinanceiroCapote.objetos.Renda;

public class RendaService {
	
	public RendaService() {
		// TODO Auto-generated constructor stub
	}
	public void addRenda(Renda renda) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRendadao = new JDBCRendaDAO(conexao);
		jdbcRendadao.inserir(renda);
		conec.fecharConexao();
	}
	public List<Renda> getIncomes(int id, int userId) {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRendadao = new JDBCRendaDAO(conexao);
		List<Renda> incomeList = jdbcRendadao.getIncomes(id, userId);
		conec.fecharConexao();

		return incomeList;
	}
	
	public void deletaRenda(int id) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRendadao = new JDBCRendaDAO(conexao);
		jdbcRendadao.deletaRenda(id);
		conec.fecharConexao();
	}
}
