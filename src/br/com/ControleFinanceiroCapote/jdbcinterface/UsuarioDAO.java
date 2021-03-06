package br.com.ControleFinanceiroCapote.jdbcinterface;

import java.util.List;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Usuario;

public interface UsuarioDAO {
	
	public boolean inserir(Usuario usuario) throws ValidationException;
	public boolean deletaUsuario(int id) throws ValidationException;
	public boolean ativaUsuario(int id) throws ValidationException;
	public List<Usuario> getUsers(String text) throws ValidationException;
	public Usuario authUser(Usuario user) throws Exception;
	public List<Familia> getFamilies() throws ValidationException;
	public Usuario getUserById(int id) throws ValidationException;
}