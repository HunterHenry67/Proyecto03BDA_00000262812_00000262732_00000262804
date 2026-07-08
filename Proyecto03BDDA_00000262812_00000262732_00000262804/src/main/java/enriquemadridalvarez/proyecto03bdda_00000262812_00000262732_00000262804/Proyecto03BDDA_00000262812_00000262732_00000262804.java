/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package enriquemadridalvarez.proyecto03bdda_00000262812_00000262732_00000262804;

import Entidades.Genero;
import Excepciones.PersistenciaException;
import Persistencia.GeneroDAO;
import Interfaces.IGeneroDAO;
import Presentacion.frmLogin;

/**
 *
 * @author BALAMRUSH
 */
public class Proyecto03BDDA_00000262812_00000262732_00000262804 {

    public static void main(String[] args) {
        pruebaGeneroDAO();
//        new frmLogin();
    }
    
    public static void pruebaGeneroDAO(){
        try{
            IGeneroDAO generoDAO = new GeneroDAO();

            Genero genero = new Genero();
            genero.setNombre("Rock");

            Genero generoGuardado = generoDAO.agregar(genero);
        }catch(PersistenciaException e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}
