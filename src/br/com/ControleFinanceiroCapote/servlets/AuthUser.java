package br.com.ControleFinanceiroCapote.servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.helpers.Helper;
import br.com.ControleFinanceiroCapote.jdbc.JDBCUsuarioDAO;
import br.com.ControleFinanceiroCapote.objetos.Usuario;

public class AuthUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AuthUser() {
		super();
	}

	private void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException, NoSuchAlgorithmException, ValidationException {
		Usuario user = new Usuario();
		try {
			String context = request.getServletContext().getContextPath();
			user.setUsuario(request.getParameter("inputUsername"));
			user.setSenha(Helper.hasher(request.getParameter("inputSenha")));

			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCUsuarioDAO jdbcContato = new JDBCUsuarioDAO(conexao);
			Usuario userExists = jdbcContato.authUser(user);
			conec.fecharConexao();
			if (userExists != null) {

				HttpSession sessao = request.getSession();

				// String usuario = request.getParameter("inputUsername");
				// String senha = request.getParameter("inputSenha");

				// Encoder encoder = Base64.getEncoder();
				// String encodedPassword =
				// encoder.encodeToString(senha.getBytes());

				sessao.setAttribute("id", Integer.toString(userExists.getId()));
				sessao.setAttribute("familyId",
						Integer.toString(userExists.getId_familia()));
				sessao.setAttribute("lvl", userExists.getNivel());

				// Decoder decoder = Base64.getDecoder();
				// byte[] decodedPassword = decoder.decode(encodedPassword);
				// password = new String(decodedPassword,
				// StandardCharsets.UTF_8);

				// sessao.setAttribute("msg", "Bem vindo, "+usuario+", senha
				// criptografada: "+encodedPassword+"!");
				// RequestDispatcher dispatcher =
				// request.getRequestDispatcher("/resources/admin/admin.html");
				// dispatcher.forward(request, response);

				// response.sendRedirect(context+"/resources/contato/views/teste.jsp");

				response.sendRedirect(userExists.getNivel() == 0 ? context
						+ "/resources/Index.html" : context
						+ "/resources/admin/admin.html");
				} else {
				response.sendRedirect(context + "/Login.html?incorrect");
				// msg.put("msg", "Usu�rio e/ou senha incorretos");
			}
		} catch (IOException e) {
			throw new ValidationException(e);
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			process(request, response);
		} catch (SQLException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.sendRedirect("Login.html?svError");
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.sendRedirect("Login.html?svError");
		}
	}
}
