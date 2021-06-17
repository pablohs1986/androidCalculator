package com.example.calculadora;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    LogicaCalculadora logica;
    TextView textviewEntradaResultado, textviewOperacion;
    Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9,
            buttonSuma, buttonResta, buttonMultiplicacion, buttonDivision, buttonPunto, buttonIgual,
            buttonLimpiar, buttonBorrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(this.getSupportActionBar()).hide(); // Oculta el header de la activity

        textviewEntradaResultado = (TextView) findViewById(R.id.textviewEntradaResultado);
        textviewOperacion = (TextView) findViewById(R.id.textViewOperacion);

        logica = new LogicaCalculadora();

        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        buttonSuma = (Button) findViewById(R.id.buttonSuma);
        buttonResta = (Button) findViewById(R.id.buttonResta);
        buttonMultiplicacion = (Button) findViewById(R.id.buttonMultiplicacion);
        buttonDivision = (Button) findViewById(R.id.buttonDivision);
        buttonPunto = (Button) findViewById(R.id.buttonDecimal);
        buttonIgual = (Button) findViewById(R.id.buttonIgual);
        buttonLimpiar = (Button) findViewById(R.id.buttonLimpiar);
        buttonBorrar = (Button) findViewById(R.id.buttonBorrar);

        ////// Listeners //////

        ////// Listener global suscrito a los eventos del backend //////
        logica.setCalculadoraListener(new CalculadoraListener() {
            @Override
            public void getResultado(String resultado) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textviewEntradaResultado.setText(resultado);
                    }
                });
            }

            @Override
            public void getOperacion(String operacion) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textviewOperacion.setText(operacion);
                    }
                });
            }
        });

        ////// Botones operaciones //////
        buttonSuma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logica.gestorBotones("suma", "buttonSumaMultiplicacionDivision", "");
            }
        });

        buttonResta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logica.gestorBotones("resta", "buttonResta", "");
            }
        });

        buttonMultiplicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logica.gestorBotones("multiplicacion", "buttonSumaMultiplicacionDivision", "");
            }
        });

        buttonDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logica.gestorBotones("division", "buttonSumaMultiplicacionDivision", "");
            }
        });

        buttonIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logica.gestorBotones("igual", "buttonIgual", "");
            }
        });

        buttonLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logica.gestorBotones("limpiar", "buttonLimpiarYBorrar", "");
            }
        });

        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logica.gestorBotones("borrar", "buttonLimpiarYBorrar", "");
            }
        });
    }

    /**
     * Método que gestiona los lísteners para los botones numéricos. Es llamado desde cada botón
     * en el xml. Segun el id del botón sobre el que se hace click, se lanza un evento.
     */
    @SuppressLint("NonConstantResourceId")
    public void buttonNumericoOnClick(View view) {
        switch (view.getId()) {
            case R.id.button0:
                logica.gestorBotones("numerico", "", "0");
                break;
            case R.id.button1:
                logica.gestorBotones("numerico", "", "1");
                break;
            case R.id.button2:
                logica.gestorBotones("numerico", "", "2");
                break;
            case R.id.button3:
                logica.gestorBotones("numerico", "", "3");
                break;
            case R.id.button4:
                logica.gestorBotones("numerico", "", "4");
                break;
            case R.id.button5:
                logica.gestorBotones("numerico", "", "5");
                break;
            case R.id.button6:
                logica.gestorBotones("numerico", "", "6");
                break;
            case R.id.button7:
                logica.gestorBotones("numerico", "", "7");
                break;
            case R.id.button8:
                logica.gestorBotones("numerico", "", "8");
                break;
            case R.id.button9:
                logica.gestorBotones("numerico", "", "9");
                break;
            case R.id.buttonDecimal:
                logica.gestorBotones("decimal", "buttonDecimal", ".");
                break;
        }
    }

}