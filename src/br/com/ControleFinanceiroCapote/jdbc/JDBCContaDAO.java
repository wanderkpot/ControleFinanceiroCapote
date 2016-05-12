package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbcinterface.ContaDAO;
import br.com.ControleFinanceiroCapote.objetos.Conta;
import br.com.ControleFinanceiroCapote.objetos.Renda;

public class JDBCContaDAO implements ContaDAO {

	private Connection conexao;

	public JDBCContaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Conta conta) {
		if (conta.getId() == 0) {
			StringBuilder comando = new StringBuilder();
			comando.append("INSERT INTO contas ");
			comando.append("(Id_Categoria, Id_Usuario, Descricao_Contas, Valor_Contas, ");
			comando.append("Status_Conta, Data_Vencimento, Vezes, Conta_Fixa)");
			comando.append(" VALUES ");
			comando.append("(?,?,?,?,?,?,?,?)");

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando.toString(), Statement.RETURN_GENERATED_KEYS);
				p.setInt(1, conta.getCategoria());
				p.setInt(2, conta.getUserId());
				p.setString(3, conta.getDescription());
				p.setDouble(4, conta.getTotalValue());
				p.setInt(5, 1);
				p.setDate(6, (Date) conta.getStartDate());
				p.setInt(7, conta.getTimes());
				p.setInt(8, conta.getHasDeadline());
				p.execute();
				rs = p.getGeneratedKeys();
				if (rs.next()) {
					conta.setId(rs.getInt(1));

					if (conta.getTimes() != 0) {
						insertParcels(conta.getId(), conta.getTimes(), conta.getParcelValue(), conta.getTotalValue());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			StringBuilder comando = new StringBuilder();
			comando.append("UPDATE contas ");
			comando.append(
					"SET Id_Categoria = ?, Id_Usuario = ?, Descricao_contas = ?, Valor_contas = ?, ");
			comando.append("Status_conta = ?, Data_Vencimento = ?, Vezes = ?, Conta_Fixa = ?");
			comando.append(" WHERE Id_contas = ?");

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando.toString(), Statement.RETURN_GENERATED_KEYS);
				p.setInt(1, conta.getCategoria());
				p.setInt(2, conta.getUserId());
				p.setString(3, conta.getDescription());
				p.setDouble(4, conta.getTotalValue());
				p.setInt(5, 1);
				p.setDate(6, (Date) conta.getStartDate());
				p.setInt(7, conta.getTimes());
				p.setInt(8, conta.getHasDeadline());
				p.setInt(9, conta.getId());
				p.execute();
				if (conta.getTimes() != 0) {
					insertParcels(conta.getId(), conta.getTimes(), conta.getParcelValue(), conta.getTotalValue());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void insertParcels(int billIDd, int times, Double parcelValue, double totalValue) {
		double math = ((parcelValue*times) - totalValue),
			sum = 0, value;
		
		if (math != 0) {
			sum = Math.ceil(math/parcelValue);
		}
		value = parcelValue+sum;
		StringBuilder comando = new StringBuilder();
		comando.append("INSERT INTO parcela_conta");
		comando.append("(Id_Conta, Valor_Parcela, Status_Parcela)");
		comando.append(" VALUES ");
		comando.append("(?,?,?)");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, billIDd);
			p.setDouble(2, value);
			p.setInt(3, 1);

			for (int x = 0; x < times; x++) {
				p.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean deletaConta(int id) throws ValidationException {

		deleteParcels(id);

		StringBuilder comando = new StringBuilder();
		comando.append("UPDATE contas ");
		comando.append("SET Status_Conta = 0 ");
		comando.append("WHERE Id_Contas = ?");
		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void deleteParcels(int id) {
		
		StringBuilder comando = new StringBuilder();
		comando.append("UPDATE parcela_conta ");
		comando.append("SET Status_Parcela = 0 ");
		comando.append("WHERE Id_Conta = ?");
		PreparedStatement p;

		try {

			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Conta> getBills(int id, int userId) {
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT r.Id_Contas as id, r.Id_Categoria as categoryId, ");
		comando.append("r.Descricao_Contas as descr, r.Valor_Contas as totalValue, r.Status_Conta as status, ");
		comando.append("r.Data_Vencimento as endDate, r.Conta_Fixa as isFixed, r.Vezes as x ");
		comando.append("FROM contas r ");
		comando.append("WHERE r.Id_Usuario = " + userId);
		comando.append(" AND ");
		comando.append("Status_Conta = 1");
		if (id != 0) {
			comando.append(" AND ");
			comando.append("r.Id_Contas = " + id);
		}

		List<Conta> BillsList = new ArrayList<Conta>();
		Conta income = null;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			while (rs.next()) {
				income = new Conta();

				income.setId(rs.getInt("id"));
				income.setCategoria(rs.getInt("categoryId"));
				income.setDescription(rs.getString("descr"));
				income.setTotalValue(rs.getInt("totalValue"));
				income.setStatus(rs.getInt("status"));
				income.setStartDate(rs.getDate("endDate"));
				income.setHasDeadline(rs.getInt("isFixed"));
				income.setTimes(rs.getInt("x"));

				BillsList.add(income);
			}

			for (Conta inc : BillsList) {
				try {
					inc.setCategoriaName(getCategoriesName(inc.getCategoria()));
				} catch (Exception e) {
					inc.setCategoriaName("Não há categoria");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return BillsList;
	}

	private String getCategoriesName(int categoria) throws SQLException {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT Descricao as descr ");
		comando.append("FROM categorias ");
		comando.append("WHERE Id_Categorias = " + categoria);

		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			while (rs.next()) {
				return rs.getString("descr");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
