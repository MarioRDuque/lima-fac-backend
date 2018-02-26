/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.util;

import java.util.List;
import lombok.Data;

/**
 *
 * @author dev-out-03
 */

@Data
public class PaginaLista implements java.io.Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final int LIMITE_MAX = 60;
    
    private int count;
    private int limit;
    private Long offset; // a partir de que registro se realiz la siguiente llamada
    private int nextPage;
    private int previousPage;
    private List<Object> listaData;
    
}
