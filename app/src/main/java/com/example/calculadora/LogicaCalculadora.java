package com.example.calculadora;

import java.text.DecimalFormat;

public class LogicaCalculadora {

    private String cadenaResultado = "";
    private String cadenaOperacion = "";
    private String cadenaEntrada = "";
    private String operador = "";
    private double valorEntrada = 0;
    private double resultado = 0;
    private int contadorClicksOperador = 0;
    private double contadorClicksIgual = 0;
    private double contadorClicksDecimal = 0;
    private boolean isValorIntroducido = false;
    private boolean isOperacionCompletada = false;
    private boolean isBotonNumericoPulsado = false;
    private CalculadoraListener calculadoraListener;

    ////// Constructor //////
    public LogicaCalculadora() {
        this.calculadoraListener = null;
    }

    ////// Getters & Setters //////
    public void setCalculadoraListener(CalculadoraListener listener) {
        if (listener != null) {
            this.calculadoraListener = listener;
        }
    }

    public String getCadenaResultado() {
        return cadenaResultado;
    }

    public void setCadenaResultado(String cadenaResultado) {
        getResultadoRequest(cadenaResultado); // Lanzo evento resultado.
        this.cadenaResultado = cadenaResultado;
    }

    public String getCadenaOperacion() {
        return cadenaOperacion;
    }

    public void setCadenaOperacion(String cadenaOperacion) {
        getOperacionRequest(cadenaOperacion); // Lanzo evento operacion.
        this.cadenaOperacion = cadenaOperacion;
    }

    ////// Controladores //////

    /**
     * Método que lanza un evento con el valor del resultado
     */
    public void getResultadoRequest(String resultado) {
        calculadoraListener.getResultado(resultado);
    }

    /**
     * Método que lanza un evento con el valor de la operación
     */
    public void getOperacionRequest(String operacion) {
        calculadoraListener.getOperacion(operacion);
    }

    /**
     * Método que gestiona el comportamiento de los botones de la app.
     */
    public void gestorBotones(String tipoBoton, String tipoComponente, String valorBoton) {
        boolean isError;
        switch (tipoBoton) {
            case "suma":
                isError = gestionarErrores(tipoComponente);
                if (!isError) {
                    gestionarBotonSuma();
                }
                break;
            case "resta":
                isError = gestionarErrores(tipoComponente);
                if (!isError) {
                    gestionarBotonResta();
                }
                break;
            case "multiplicacion":
                isError = gestionarErrores(tipoComponente);
                contadorClicksOperador++;
                if (!isError) {
                    gestionarBotonMultiplicacion();
                }
                break;
            case "division":
                isError = gestionarErrores(tipoComponente);
                contadorClicksOperador++;
                if (!isError) {
                    gestionarBotonDivision();
                }
                break;
            case "igual":
                isError = gestionarErrores(tipoComponente);
                if (!isError) {
                    gestionarBotonIgual();
                }
                break;
            case "limpiar":
                isError = gestionarErrores(tipoComponente);
                if (!isError) {
                    refrescarTodosLosValores();
                }
                break;
            case "borrar":
                isError = gestionarErrores(tipoComponente);
                if (!isError) {
                    gestionarBotonBorrar();
                }
                break;
            case "numerico":
                gestionarBotonNumerico(valorBoton);
                break;
            case "decimal":
                isError = gestionarErrores(tipoComponente);
                if (!isError) {
                    gestionarBotonNumerico(valorBoton);
                    contadorClicksDecimal++;
                }
                break;
        }
    }

    ////// Métodos //////

    /**
     * Método que gestiona los errores de la app, segun el tipo de componente que se le pasa
     * como parámetro.
     */
    private boolean gestionarErrores(String tipoComponenteARevisar) {
        boolean isError;
        switch (tipoComponenteARevisar) {
            case "calculoDivision":
                isError = (valorEntrada == 0 && operador == "/") ? true : false;
                break;
            case "calculoMultiplicacion":
                DecimalFormat decimalHandler = new DecimalFormat("#");
                decimalHandler.setMaximumFractionDigits(10);
                isError = (decimalHandler.format(resultado).length() > 7) ? true : false;
                break;
            case "buttonSumaMultiplicacionDivision":
                isError = (getCadenaResultado().isEmpty()) ? true : false;
                break;
            case "buttonResta":
                if (valorEntrada == 0 && (Double.compare(valorEntrada, 0.0) == 0)) {
                    isError = false;
                } else if (valorEntrada == 0 && (Double.compare(valorEntrada, 0.0) == -1)) {
                    isError = true;
                } else {
                    isError = (getCadenaResultado().isEmpty()) ? true : false;
                }
                break;
            case "buttonIgual":
                isError = (getCadenaResultado().isEmpty()
                        || getCadenaOperacion().isEmpty())
                        ? true : false;
                break;
            case "buttonLimpiarYBorrar":
                isError = (getCadenaResultado().isEmpty()) ? true : false;
                break;
            case "buttonDecimal":
                isError = !isBotonNumericoPulsado || contadorClicksDecimal == 1 ? true : false;
                break;
            default:
                isError = false;
                break;
        }
        return isError;
    }

    /**
     * Método que gestiona la lógica del botón suma. Setea que la operación no está completada,
     * asigna un valor de entrada según el operador anterior y lanza una acción específica
     * para el operador asignado al botón.
     */
    private void gestionarBotonSuma() {
        isOperacionCompletada = false;
        contadorClicksIgual = 0;
        contadorClicksDecimal = 0;
        gestionarValorEntradaSumaYResta();
        realizarAccionOperador("+");
    }

    /**
     * Método que gestiona la lógica del botón resta. Setea que la operación no está completada,
     * asigna un valor de entrada según el operador anterior y lanza una acción específica
     * para el operador asignado al botón.
     */
    private void gestionarBotonResta() {
        isOperacionCompletada = false;
        contadorClicksIgual = 0;
        contadorClicksDecimal = 0;
        gestionarValorEntradaSumaYResta();
        if (valorEntrada == 0 && (1 / valorEntrada == Double.POSITIVE_INFINITY)
                && getCadenaOperacion().isEmpty()) {    // Detecto si el valor de entrada no se ha iniciado y es un +0.0, en tal caso, permito que el botón resta escriba en el textview
            setCadenaResultado(getCadenaResultado() + "-");
            valorEntrada = Double.parseDouble(getCadenaResultado() + 0);
        } else {
            realizarAccionOperador("-");
        }
    }

    /**
     * Método que gestiona la lógica del botón multiplicacion. Setea que la operación no está completada,
     * asigna un valor de entrada según el operador anterior y lanza una acción específica
     * para el operador asignado al botón.
     */
    private void gestionarBotonMultiplicacion() {
        isOperacionCompletada = false;
        contadorClicksIgual = 0;
        contadorClicksDecimal = 0;
        gestionarValorEntradaDivisionYMultiplicacion("*");
        realizarAccionOperador("*");
    }

    /**
     * Método que gestiona la lógica del botón división. Setea que la operación no está completada,
     * asigna un valor de entrada según el operador anterior y lanza una acción específica
     * para el operador asignado al botón.
     */
    private void gestionarBotonDivision() {
        isOperacionCompletada = false;
        contadorClicksIgual = 0;
        contadorClicksDecimal = 0;
        gestionarValorEntradaDivisionYMultiplicacion("/");
        realizarAccionOperador("/");
    }

    /**
     * Método que gestiona la lógica del botón igual. Toma los valores de entrada, genera
     * la cadena de entrada, calcula el resultado, muestra la cadena de entrada y resetea
     * los flags.
     */
    private void gestionarBotonIgual() {
        valorEntrada = contadorClicksIgual > 0 ? valorEntrada
                : Double.parseDouble(getCadenaResultado());
        cadenaEntrada = String.valueOf(resultado) + " " + operador + " " + valorEntrada;    // Tomo el resultado actual, el operador y el último valor de entrada para almacenarlos en cadenaEntrada.
        calcularResultado();    // Calculo resultado
        setCadenaOperacion(cadenaEntrada + " " + "=");   // Muestro la operación realizada en el textview operación
        isValorIntroducido = true;  // Indico que se ha introducido un valor
        isOperacionCompletada = true;   // Indico que se ha completado una operación.
        contadorClicksIgual++;
        isBotonNumericoPulsado = false;
    }

    /**
     * Método que gestiona la lógica del botón borrar. Toma el valor del textview entrada/Resultado
     * y genera una nueva, eliminando un caracter, mostrándo esta última y asignándo su valor a valorEntrada.
     */
    private void gestionarBotonBorrar() {
        boolean isError = gestionarErrores("buttonLimpiarYBorrar");
        if (!isError) {
            String entradaInicial = getCadenaResultado();  // Tomo el valor actual de la cadena
            String entradaSinUltimoCaracter = entradaInicial.substring(0, entradaInicial.length() - 1);     // Genero una cadena nueva a partir de entradaInicial sin el último caracter
            setCadenaResultado(entradaSinUltimoCaracter);     // Muestro la nueva cadena en el textView entrada/Resultado
            valorEntrada = entradaSinUltimoCaracter.isEmpty() ? 0 : Double.parseDouble((entradaSinUltimoCaracter));    // Asigno el valor a valorEntrada. Si la nueva cadena está vacía, asigno 0.
        }
    }

    /**
     * Método que gestiona la lógica para un botón numérico (incluido el botón de punto), chequeando
     * si se ha completado una operación (en tal caso, resetea todos los valores), refrescando
     * los valores de las entradas, añadiendo el valor del botón al textView de entrada/resultado
     * y asignando un nuevo valor de entrada..
     */
    private void gestionarBotonNumerico(String valorNumero) {
        if (isOperacionCompletada) {    // Si se completó una operación, resetea todos los valores
            refrescarTodosLosValores();
            isOperacionCompletada = false;
        }
        isBotonNumericoPulsado = true;
        refrescarEntradas();    // Reseteo valorEntrada y el textview si se ha introducido un valor
        setCadenaResultado(getCadenaResultado() + valorNumero);     // Tomo valor del textview y concateno el valor del botón
        valorEntrada = Double.parseDouble(getCadenaResultado());   // Asigno el valor del textview al valor de entrada.
    }

    /**
     * Método que gestiona el valor de entrada para los botones suma y resta. Si el operador anterior
     * es + o - y consta que se ha introducido un valor, asigna 0. Si el operador anterior
     * es * o /, asigna 1. Si no se ha introducido un valor (si no se sigue introduciendo datos para
     * una operación), devuelve valorEntrada sin modificación.
     */
    private void gestionarValorEntradaSumaYResta() {
        if (operador.equals("+") || operador.equals(("-"))) {
            valorEntrada = isValorIntroducido ? 0 : valorEntrada;   // Compruebo si se ha introducido un valor, de cumplirse, asigno 0 al valor de entrada para reiniciar el valor tras pulsar igual. Si no, mantengo su valor.
        } else if (operador.equals("/") || operador.equals(("*"))) {
            valorEntrada = isValorIntroducido ? 1 : valorEntrada;   // Compruebo si se ha introducido un valor, de cumplirse, asigno 1 al valor de entrada para reiniciar el valor tras pulsar igual. Si no, mantengo su valor.
        }
    }

    /**
     * Método que gestiona el valor de entrada para los botones división y multiplicación.
     * Para ello, se ayuda de un contador de clicks sobre los botones implicados, y aplica
     * un valor a la entrada según la situación.
     */
    private void gestionarValorEntradaDivisionYMultiplicacion(String tipoOperador) {
        // Contador consumido y operador igual a operador actual
        if (contadorClicksOperador == 2 && operador.equals(tipoOperador)) {
            valorEntrada = isValorIntroducido ? 1 : valorEntrada;
            contadorClicksOperador = 0;
            // Contador no consumido, operador igual a operador actual y operación completada
        } else if (contadorClicksOperador < 2 && operador.equals(tipoOperador) && isOperacionCompletada) {
            valorEntrada = isValorIntroducido ? 0 : valorEntrada;
            contadorClicksOperador = 0;
            // Contador no consumido, operador igual a operador actual y operación no completada
        } else if (contadorClicksOperador < 2 && operador.equals(tipoOperador) && !isOperacionCompletada) {
            valorEntrada = isValorIntroducido ? 1 : valorEntrada;
            contadorClicksOperador = 0;
            // Contador no consumido, operación no completada y anterior operador es *
        } else if (contadorClicksOperador < 2 && !isOperacionCompletada && operador.equals("*")) {
            valorEntrada = isValorIntroducido ? 1 : valorEntrada;
            contadorClicksOperador = 0;
            // Contador no consumido, operación no completada y anterior operador es /
        } else if (contadorClicksOperador < 2 && !isOperacionCompletada && operador.equals("/")) {
            valorEntrada = isValorIntroducido ? 1 : valorEntrada;
            contadorClicksOperador = 0;
            // Contador no consumido y operación no completada
        } else if (contadorClicksOperador < 2 && !isOperacionCompletada) {
            valorEntrada = isValorIntroducido ? 0 : valorEntrada;
            contadorClicksOperador = 0;
            // Contador no consumido y operación completada
        } else if (contadorClicksOperador < 2 && isOperacionCompletada) {
            valorEntrada = isValorIntroducido ? 1 : valorEntrada;
            contadorClicksOperador = 0;
            // Contador no consumido, idependientemente del operador y del estado de la operación
        } else if (contadorClicksOperador < 2) {
            valorEntrada = isValorIntroducido ? 0 : valorEntrada;
            contadorClicksOperador = 0;
        } else {
            valorEntrada = isValorIntroducido ? 0 : valorEntrada;
        }
    }

    /**
     * Método que realiza una serie de acciones para el tipo de operador que recibe como parámetro:
     * - Calcula el resultado para la operación anterior.
     * - Almacena el valor y el operador en cadenaEntrada, y la muestra en textviewOperacion.
     * - Asigna el operador recibido (para realizar la siguiente operación).
     * - Indica que se ha introducido un valor con el flag isValorIntroducido.
     */
    private void realizarAccionOperador(String operador) {
        calcularResultado();    // Al pulsar el botón del operador, se realiza la operación anterior
        cadenaEntrada = String.valueOf(resultado) + " " + operador;     // Tomo el resultado y el operador para almacenarlos en cadenaEntrada.
        setCadenaOperacion(cadenaEntrada);   // Muestro la cadena de entrada (resultado y operador activo) en el textview de operación.
        this.operador = operador;   // Asigno el operador
        isValorIntroducido = true;  // Indico que se ha introducido un valor
    }

    /**
     * Método que calcula el resultado según el tipo de operación y los valores del resultado y
     * valor de entrada almacenados en las variables globales.
     */
    private void calcularResultado() {
        boolean isError;
        resultado = resultado != 0 ? resultado : valorEntrada;  // Comprueba si resultado no es 0 (cuando se inicia una operación), de ser así, le asigna el valor de entrada
        switch (operador) {
            case "+":
                resultado += valorEntrada;  // Realizo operación suma
                resultado = redondearADosDecimales(resultado);
                setCadenaResultado(String.valueOf(resultado));    // Muestro resultado en textview entrada/Resultado
                break;
            case "-":
                resultado -= valorEntrada;  // Realizo operación resta
                resultado = redondearADosDecimales(resultado);
                setCadenaResultado(String.valueOf(resultado));
                break;
            case "*":
                resultado *= valorEntrada;  // Realizo operación multiplicación
                resultado = redondearADosDecimales(resultado);
                isError = gestionarErrores("calculoMultiplicacion");
                if (isError) {
                    setCadenaResultado(String.valueOf("Overflow"));
                    refrescarTodosLosValoresExceptoTextView();
                } else {
                    setCadenaResultado(String.valueOf(resultado));
                }
                break;
            case "/":
                isError = gestionarErrores("calculoDivision");
                if (isError) {
//                    textviewEntradaResultado.setTextSize(28);
                    setCadenaResultado(String.valueOf("No se puede dividir entre 0"));
                } else {
                    resultado /= valorEntrada;  // Realizo operación división
                    resultado = redondearADosDecimales(resultado);
                    setCadenaResultado(String.valueOf(resultado));
                }
                break;
        }
    }

    /**
     * Método que redondea a dos decimales el valor que recibe como parámetro.
     */
    private double redondearADosDecimales(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    /**
     * Método que resetea todas las variables globales de la clase.
     */
    private void refrescarTodosLosValores() {
//        textviewEntradaResultado.setTextSize(56);
        valorEntrada = 0;
        setCadenaResultado("");
        isValorIntroducido = false;
        isBotonNumericoPulsado = false;
        setCadenaOperacion("");
        resultado = 0;
        cadenaEntrada = "";
        operador = "";
    }

    /**
     * Método que resetea todas las variables globales de la clase excepto los TextView
     */
    private void refrescarTodosLosValoresExceptoTextView() {
        valorEntrada = 0;
        isValorIntroducido = false;
        isBotonNumericoPulsado = false;
        resultado = 0;
        cadenaEntrada = "";
        operador = "";
    }

    /**
     * Método que refresca, si se ha recibido una entrada de un valor por parte del usuario,
     * el valor de entrada, el texto que se muestra en el textview de entrada/resultado
     * y se modifica el valor de isValorIntroducido a false.
     */
    private void refrescarEntradas() {
        if (isValorIntroducido) {
//            textviewEntradaResultado.setTextSize(56);
            valorEntrada = 0;
            setCadenaResultado("");
            isValorIntroducido = false;
        }
    }
}
