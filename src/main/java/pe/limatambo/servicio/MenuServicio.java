/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import java.util.List;
import pe.limatambo.entidades.Menu;
import pe.limatambo.excepcion.GeneralException;

/**
 *
 * @author dev-out-03
 */
public interface MenuServicio extends GenericoServicio<Menu, Integer>{
    public List<Menu> listarPorTipoDeUsuario(Integer tipousuario) throws GeneralException;
}
