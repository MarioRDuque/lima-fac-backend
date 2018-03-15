/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import java.util.List;
import pe.limatambo.entidades.Moneda;

/**
 *
 * @author dev-out-03
 */
public interface MonedaServicio extends GenericoServicio<Moneda, Integer>{
    public List<Moneda> listar();
}
