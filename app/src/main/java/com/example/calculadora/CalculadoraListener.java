package com.example.calculadora;

/** Interface con los métodos a disparar por los eventos */
public interface CalculadoraListener {
    void getResultado(String resultado);
    void getOperacion(String operacion);
}