package controle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import repositorio.RepositorioMysql;

public class TabelaAgendaModelo extends AbstractTableModel {

    private List<Contato> lista=new ArrayList<Contato>();
    

    public TabelaAgendaModelo() {
    }

    public Contato getContato(int linha) {
        return lista.get(linha);
    }
    
    @Override
    public int getRowCount() {
        if(lista!=null) return lista.size();
        else return 0;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0: 
                return "Nome";
            case 1:
                return "Telefone";
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contato contato=lista.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return contato.getNome();
            case 1:
                return contato.getTelefone();
            default:
                return null;
        }
    }

    public void remove(int linha) {
        try {
            RepositorioMysql.getInstance().remover(getContato(linha));
           carregarContatos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao tentar remover! "+ e.getMessage());
        }
        
    }

    public void setContato(Contato contatoEditado, int linha) {
        try {
            
            Contato contatoAntigo = getContato(linha);
            RepositorioMysql.getInstance().editar(contatoEditado, contatoAntigo.getTelefone());
            carregarContatos();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao tentar atualizar! ");
        }
        
    }
    
    public void adicionarContato(Contato contato) {
        try {
            RepositorioMysql.getInstance().persistirContato(contato);
           carregarContatos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de persistência. "+ e.getMessage());
        }
        
        /*
        lista.add(contato);
        fireTableDataChanged();*/
    }

    void carregarContatos() {
//        FileInputStream fis = null;
        try {
           /* fis = new FileInputStream("agenda.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            lista = (ArrayList<Contato>) ois.readObject();*/
           lista = RepositorioMysql.getInstance().carregarContatos();
            fireTableDataChanged();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro de persistência. "+ ex.getMessage());
            
        }/* finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(TabelaAgendaModelo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
    }

    public void persistirContatos() {
        try {
//            for(Contato contato : lista){
//                RepositorioMysql.getInstance().persistirContato(contato);
//            }
            /*
            
            FileOutputStream fos = new FileOutputStream("agenda.ser");
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(lista);
            oos.close();*/
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro de persistência. "+ ex.getMessage());
        }

    }
}