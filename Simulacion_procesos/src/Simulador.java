import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Simulador {
    private final LinkedList<Proceso> colaDeProcesos = new LinkedList<>();
    private final LinkedList<Proceso> colaDeES = new LinkedList<>();
    private final LinkedList<Proceso> colaDeInterrupciones = new LinkedList<>();
    private final LinkedList<Proceso> procesosEliminados = new LinkedList<>();
    private final LinkedList<Proceso> procesosFinalizados = new LinkedList<>();
    private int memoriaUsada = 0;
    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Crear Proceso nuevo");
            System.out.println("2. Ver proceso activo");
            System.out.println("3. Ejecutar proceso actual");
            System.out.println("4. Pasar al proceso siguiente");
            System.out.println("5. Ejecutar entrada y salida");
            System.out.println("6. Ejecutar interrupción");
            System.out.println("7. Matar proceso actual");
            System.out.println("8. Imprimir lista de procesos preparados (detalle)");
            System.out.println("9. Imprimir lista de E/S");
            System.out.println("10. Imprimir procesos pendientes de E/S");
            System.out.println("11. Ver estado actual del sistema");
            System.out.println("12. Salir del programa");

            int seleccion = scanner.nextInt();
            scanner.nextLine();

            switch (seleccion) {
                case 1:
                    crearProceso();
                    break;
                case 2:
                    verProcesoActivo();
                    break;
                case 3:
                    ejecutarProcesoActual();
                    break;
                case 4:
                    pasarAlProcesoSiguiente();
                    break;
                case 5:
                    ejecutarEntradaYSalida();
                    break;
                case 6:
                    ejecutarInterrupcion();
                    break;
                case 7:
                    matarProcesoActual();
                    break;
                case 8:
                    imprimirListaProcesosPreparados();
                    break;
                case 9:
                    imprimirListaES();
                    break;
                case 10:
                    imprimirProcesosPendientesInterrupcion();
                    break;
                case 11:
                    verEstadoActualSistema();
                    break;
                case 12:
                    salirDelPrograma();
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }


    public void verEstadoActualSistema() {
        // Número de procesos en cola de preparados
        System.out.println("Número de procesos en cola de preparados: " + colaDeProcesos.size());

        // Número de procesos en cola de E/S
        System.out.println("Número de procesos en cola de E/S: " + colaDeES.size());

        // Mostrar lista de procesos pendientes de ejecutar interrupción
        imprimirProcesosPendientesInterrupcion();

        // Lista con el nombre de los procesos finalizados exitosamente
        System.out.println("Procesos finalizados exitosamente:");
        if (procesosFinalizados.isEmpty()) {
            System.out.println("No hay procesos finalizados exitosamente.");
        } else {
            for (Proceso proceso : procesosFinalizados) {
                System.out.println("- " + proceso.getNombre());
            }
        }

        // Lista de procesos finalizados antes de tiempo (eliminados)
        System.out.println("Procesos finalizados antes de tiempo (eliminados):");
        if (procesosEliminados.isEmpty()) {
            System.out.println("No hay procesos eliminados.");
        } else {
            for (Proceso proceso : procesosEliminados) {
                System.out.println("- " + proceso.getNombre());
            }
        }
    }



    public void imprimirProcesosPendientesInterrupcion() {
        if (colaDeInterrupciones.isEmpty()) {
            System.out.println("No hay procesos pendientes de ejecutar interrupción.");
            return;
        }

        System.out.println("Lista de procesos pendientes de interrupción:");
        DatosProceso(colaDeInterrupciones);
    }

    //Metodo que muestra los datos de los procesos
    public void DatosProceso(LinkedList<Proceso> colaProcesos) {
        for (Proceso proceso : colaProcesos) {
            System.out.println("-----------------------------------");
            System.out.println("Nombre del proceso: " + proceso.getNombre());
            System.out.println("ID del proceso: " + proceso.getId());
            System.out.println("Instrucciones pendientes: " + (proceso.getInstruccionesTotales() - proceso.getInstruccionesEjecutadas()));
            System.out.println("Dirección de memoria asignada: " + proceso.getMemoriaAsignada());
        }
        System.out.println("-----------------------------------");
    }

    public void imprimirListaES() {

        if (colaDeES.isEmpty()) {
            System.out.println("No hay procesos en la cola de E/S.");
            return;
        }

        System.out.println("Lista de procesos en cola de E/S:");
        DatosProceso(colaDeES);
    }

    public void imprimirListaProcesosPreparados() {
        if (colaDeProcesos.isEmpty()) {
            System.out.println("No hay procesos en la cola de preparados.");
            return;
        }

        System.out.println("Lista de procesos preparados:");
        int index = 0;
        for (Proceso proceso : colaDeProcesos) {
            System.out.println("-----------------------------------");
            if (index == 0) {
                System.out.println(">> PROCESO ACTIVO <<");
            }

            //Se dejo asi y no se uso la otra funcion por que se muestra el proceso activo
            System.out.println("Nombre del proceso: " + proceso.getNombre());
            System.out.println("ID del proceso: " + proceso.getId());
            System.out.println("Instrucciones pendientes: " + (proceso.getInstruccionesTotales() - proceso.getInstruccionesEjecutadas()));
            System.out.println("Dirección de memoria asignada: " + proceso.getMemoriaAsignada());
            index++;
        }
        System.out.println("-----------------------------------");
    }

    public void matarProcesoActual() {
        if (colaDeProcesos.isEmpty()) {
            System.out.println("No hay procesos en la cola.");
            return;
        }

        Proceso procesoActivo = colaDeProcesos.poll(); // Accedemos y removemos el primer proceso de la cola de procesos.
        memoriaUsada -= procesoActivo.getMemoriaAsignada(); // Liberamos la memoria que estaba utilizando el proceso.
        procesosEliminados.add(procesoActivo); // Añadimos el proceso al registro de procesos eliminados.

        System.out.println("El proceso " + procesoActivo.getNombre() + " ha sido eliminado.");
        System.out.println("Instrucciones pendientes del proceso: " + (procesoActivo.getInstruccionesTotales() - procesoActivo.getInstruccionesEjecutadas()));
    }

    public void ejecutarInterrupcion() {
        if (colaDeInterrupciones.isEmpty()) {
            System.out.println("No hay procesos en la cola de interrupciones.");
            return;
        }

        Proceso procesoInterrumpido = colaDeInterrupciones.poll(); // Accedemos y removemos el primer proceso de la cola de interrupciones.
        colaDeProcesos.addLast(procesoInterrumpido);   // Añadimos el proceso al final de la cola de procesos preparados.

        System.out.println("El proceso " + procesoInterrumpido.getNombre() + " ha sido movido de la cola de interrupciones a la cola de procesos preparados.");
    }


    public void pasarAlProcesoSiguiente() {
        if (colaDeProcesos.isEmpty()) {
            System.out.println("No hay procesos en la cola.");
            return;
        }

        Proceso procesoActivo = colaDeProcesos.poll(); // Accedemos y removemos el primer proceso de la cola.

        // Genera una acción aleatoria (0, 1 o 2)
        int accionAleatoria = new Random().nextInt(3);

        switch (accionAleatoria) {
            case 0:
                // Colocar al final de la cola de procesos
                colaDeProcesos.addLast(procesoActivo);
                System.out.println("El proceso " + procesoActivo.getNombre() + " ha sido movido al final de la cola de procesos.");
                break;
            case 1:
                // Ir a la cola de entrada y salida
                colaDeES.add(procesoActivo);
                System.out.println("El proceso " + procesoActivo.getNombre() + " ha sido movido a la cola de entrada y salida.");
                break;
            case 2:
                // Solicitar una interrupción
                colaDeInterrupciones.add(procesoActivo);
                System.out.println("El proceso " + procesoActivo.getNombre() + " ha solicitado una interrupción.");
                break;
        }
    }

    public void ejecutarProcesoActual() {
        if (colaDeProcesos.isEmpty()) {
            System.out.println("No hay procesos en la cola.");
            return;
        }

        Proceso procesoActivo = colaDeProcesos.peekFirst(); // Accedemos al primer proceso de la cola, sin eliminarlo.

        // Si hay menos de 5 instrucciones por ejecutar, ajustar instruccionesARealizar.
        // Math.min toma 2 argumentos y regresa el menor de los dos,
        // en este caso si quedan menos que 5 son las que se ejecutarán
        int instruccionesARealizar = Math.min(procesoActivo.getInstruccionesTotales() - procesoActivo.getInstruccionesEjecutadas(), 5);

        // Restar las instrucciones ejecutadas del total.
        procesoActivo.setInstruccionesEjecutadas(procesoActivo.getInstruccionesEjecutadas() + instruccionesARealizar);

        // Mostrar mensaje de ejecución.
        System.out.println("El proceso " + procesoActivo.getNombre() + " se ha ejecutado.");
        System.out.println("Instrucciones restantes: " + (procesoActivo.getInstruccionesTotales() - procesoActivo.getInstruccionesEjecutadas()));

        // Si las instrucciones restantes llegan a 0, el proceso ha terminado.
        if (procesoActivo.getInstruccionesTotales() == procesoActivo.getInstruccionesEjecutadas()) {
            System.out.println("El proceso " + procesoActivo.getNombre() + " ha finalizado.");
            procesosFinalizados.addLast(colaDeProcesos.pollFirst()); // Mover el proceso a la lista de procesos finalizados.
        }
    }

    public void crearProceso() {
        System.out.print("Escribe el nombre del nuevo proceso: ");
        String nombre = scanner.nextLine();

        Proceso nuevoProceso = new Proceso(nombre);

        // Checar si hay memoria suficiente para el nuevo proceso.
        int memoriaTotal = 2048;
        if ((memoriaUsada + nuevoProceso.getMemoriaAsignada()) <= memoriaTotal) {
            colaDeProcesos.addLast(nuevoProceso);  // Agregar el proceso a la cola de procesos.
            memoriaUsada += nuevoProceso.getMemoriaAsignada();  // Incrementar la memoria usada.

            System.out.println("Proceso \"" + nombre + "\" creado con ID: " + nuevoProceso.getId());
            System.out.println("Instrucciones: " + nuevoProceso.getInstruccionesTotales());
            System.out.println("Memoria asignada: " + nuevoProceso.getMemoriaAsignada() + " localidades");
        } else {
            System.out.println("No hay suficiente memoria para crear el proceso.");
        }
    }

    public void verProcesoActivo() {
        if (colaDeProcesos.isEmpty()) {
            System.out.println("No hay procesos en la cola.");
            return;
        }

        Proceso procesoActivo = colaDeProcesos.peekFirst(); // Accedemos al primer proceso de la cola, sin eliminarlo.

        //Se dejo asi por que solo se muestra un proceso, el que es el activo
        System.out.println("Información del proceso activo:");
        System.out.println("Nombre del proceso: " + procesoActivo.getNombre());
        System.out.println("ID único: " + procesoActivo.getId());
        System.out.println("Instrucciones totales: " + procesoActivo.getInstruccionesTotales());
        System.out.println("Instrucciones ejecutadas: " + procesoActivo.getInstruccionesEjecutadas());
    }

    public void ejecutarEntradaYSalida() {
        if (colaDeES.isEmpty()) {
            System.out.println("No hay procesos en la cola de entrada y salida.");
            return;
        }

        Proceso procesoDeES = colaDeES.poll(); // Accedemos y removemos el primer proceso de la cola de E/S.
        colaDeProcesos.addLast(procesoDeES);   // Añadimos el proceso al final de la cola de procesos preparados.

        System.out.println("El proceso " + procesoDeES.getNombre() + " ha sido movido de la cola de entrada y salida a la cola de procesos preparados.");
    }




    // Métodos para manejar las otras acciones del simulador...

    public void salirDelPrograma() {
        Scanner scanner = new Scanner(System.in);

        // Verificar si hay procesos en la cola de preparados
        if (!colaDeProcesos.isEmpty()) {
            System.out.println("Advertencia: Si decides salir, " + colaDeProcesos.size() + " procesos no se concluirán exitosamente.");
            System.out.println("¿Deseas continuar? (Sí/No)");

            String respuesta = scanner.nextLine().trim().toLowerCase();

            if (respuesta.equals("no")) {
                System.out.println("Regresando al menú principal...");
                return;
            }
        }

        System.out.println("Saliendo del programa. ¡Hasta luego!");
        System.exit(0);
    }

}
