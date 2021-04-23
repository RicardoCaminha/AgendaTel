/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositorio;

import controle.TabelaAgendaModelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Contato;

/**
 *
 * @author pscs
 */
public class RepositorioMysql {
    
    private static RepositorioMysql instance = null;
    
    
    private static final String USUARIO = "root";
    private static final String SENHA = "pscs@pix";
    private static final String URL = "jdbc:mysql://192.168.1.74:3306/agendatel";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    
    private RepositorioMysql(){
    }
    
    public static RepositorioMysql getInstance() {
            if (RepositorioMysql.instance == null) {
                    RepositorioMysql.instance = new RepositorioMysql();
            }
            return instance;
    }
    
    
    public void persistirContato(Contato contato) throws Exception{
        Connection connection = null;
        try {

            if(Objects.isNull(contato)) throw new Exception("Contato nulo!");
            if(contato.getNome().isEmpty()) throw new Exception("Nome vazio!");
            if(contato.getTelefone().isEmpty()) throw new Exception("Telefone vazio!");
            
            connection = abrir();
            connection.setAutoCommit(false);
            
            String sql = "INSERT INTO contatos (nome,telefone) values (?,?)";
            
            PreparedStatement ps = null;
            
            ps = connection.prepareStatement(sql);
            ps.setString(1, contato.getNome());
            ps.setString(2, contato.getTelefone());
            
            int row = ps.executeUpdate();
            
            connection.commit();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            connection.close();
        }
    }
    
    
    
    public void editar(Contato contato, String telefone) throws Exception{
        Connection connection = null;
        try {

            if(Objects.isNull(contato)) throw new Exception("Contato nulo!");
            if(contato.getNome().isEmpty()) throw new Exception("Nome vazio!");
            if(contato.getTelefone().isEmpty()) throw new Exception("Telefone vazio!");
            if(Objects.isNull(telefone)) throw new Exception("Telefone id vazio!");
            
            connection = abrir();
            connection.setAutoCommit(false);
            
            String sql = "UPDATE contatos SET nome = ?, telefone = ?  WHERE telefone = ?";
            
            PreparedStatement ps = null;
            
            ps = connection.prepareStatement(sql);
            ps.setString(1, contato.getNome());
            ps.setString(2, contato.getTelefone());
            ps.setString(3, telefone);
            
            int row = ps.executeUpdate();
            
            connection.commit();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            connection.close();
        }
    }
    
    
    public void remover(Contato contato) throws Exception{
        Connection connection = null;
        try {

            if(Objects.isNull(contato)) throw new Exception("Contato nulo!");
            if(contato.getTelefone().isEmpty()) throw new Exception("Telefone vazio!");
            
            connection = abrir();
            connection.setAutoCommit(false);
            
            String sql = "DELETE FROM contatos WHERE telefone = ?";
            
            PreparedStatement ps = null;
            
            ps = connection.prepareStatement(sql);
            ps.setString(1, contato.getTelefone());
            
            int row = ps.executeUpdate();
            
            connection.commit();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            connection.close();
        }
    }
    
    public List<Contato> carregarContatos() throws Exception{
        Connection connection = null;
        List<Contato> contatos = new ArrayList<Contato>();
        try{
            
            connection = abrir();
            connection.setAutoCommit(false);
            
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = " SELECT * FROM contatos";
            
            ps = connection.prepareStatement(sql);
	    rs = ps.executeQuery();
            
            while (rs.next()) {
                contatos.add(new Contato(rs.getString("nome"), rs.getString("telefone")));
            }
            
            
            return contatos;
        }
        catch(Exception e){
            e.printStackTrace();
            Logger.getLogger(TabelaAgendaModelo.class.getName()).log(Level.SEVERE, null, e);
            throw new Exception("Erro na operação de leitura do banco.");
        }
    
}
    
    // Conectar ao banco
    public static Connection abrir() throws Exception {
            // Registrar o driver
            Class.forName(DRIVER);
            // Capturar a conexão
            Connection conn = DriverManager.getConnection(URL,USUARIO,SENHA);
            // Retorna a conexao aberta
            return conn;
    }
}
