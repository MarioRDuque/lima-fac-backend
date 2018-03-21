/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import java.util.List;
import pe.limatambo.entidades.Parametro;

/**
 *
 * @author dev-out-03
 */
public interface ParametroServicio extends GenericoServicio<Parametro, Long>{
    public List<Parametro> listar();
}
