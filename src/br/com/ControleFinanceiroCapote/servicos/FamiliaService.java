package br.com.ControleFinanceiroCapote.servicos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.helpers.Helper;
import br.com.ControleFinanceiroCapote.jdbc.JDBCFamiliaDAO;
import br.com.ControleFinanceiroCapote.jdbc.JDBCUsuarioDAO;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Usuario;

public class FamiliaService {

	public FamiliaService() {
		// TODO Auto-generated constructor stub
	}

	public void addfamily(Familia family) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		jdbcFamilia.inserir(family);
		conec.fecharConexao();
	}

	public List<Familia> getFamilies(int id) {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		List<Familia> families = jdbcFamilia.getFamily(id);
		conec.fecharConexao();

		return families;
	}

	public Familia getFamilyById(int id) {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		Familia family = jdbcFamilia.getFamilyById(id);
		conec.fecharConexao();

		return family;
	}

	public void deleteFamily(int id) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		jdbcFamilia.deletaFamilia(id);
		conec.fecharConexao();
	}

	public List<Usuario> getFamilyMembers(int userId) {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);	
		JDBCUsuarioDAO userMethods = new JDBCUsuarioDAO(conexao);
		List<Usuario> users = userMethods.getUsersInfo(jdbcFamilia.getFamilyMembers(userId));
		conec.fecharConexao();
		return users;
	}

	public void leadProvider(int id, int userId) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		if (jdbcFamilia.isLeader(userId))
			jdbcFamilia.leadProvider(id); 
		else 
			throw new Exception("Você precisa ser dono da familia para fazer essa operação!");
		
		conec.fecharConexao();
	}
	
	public void kickUser(int id, int userId) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		if (jdbcFamilia.isLeader(userId))
			jdbcFamilia.kickUser(id);
		else 
			throw new Exception("Você precisa ser dono da familia para fazer essa operação!");
		conec.fecharConexao();
	}

}
