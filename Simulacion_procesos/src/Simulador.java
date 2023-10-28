import java.util.*;

public class Simulador {
    private final LinkedList<Proceso> colaDeProcesos = new LinkedList<>();
    private final LinkedList<Proceso> colaDeES = new LinkedList<>();
    private final LinkedList<Proceso> colaDeInterrupciones = new LinkedList<>();
    private final LinkedList<Proceso> procesosEliminados = new LinkedList<>();
    private final LinkedList<Proceso> procesosFinalizados = new LinkedList<>();
    private int memoriaUsada = 0;
    private final Scanner scanner = new Scanner(System.in);
    //Creamos instancia de Memoria en Simulador con 1024 localidades (Establecidos por la practica)
    private final Memoria memoria = new Memoria(1024);

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
            System.out.println("13. Ver estado de la memoria");
            System.out.println("14. Desfragmentar Memoria");


            try {
                int seleccion = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (seleccion) {
                    case 1 -> crearProceso();
                    case 2 -> verProcesoActivo();
                    case 3 -> ejecutarProcesoActual();
                    case 4 -> pasarAlProcesoSiguiente();
                    case 5 -> ejecutarEntradaYSalida();
                    case 6 -> ejecutarInterrupcion();
                    case 7 -> matarProcesoActual();
                    case 8 -> imprimirListaProcesosPreparados();
                    case 9 -> imprimirListaES();
                    case 10 -> imprimirProcesosPendientesInterrupcion();
                    case 11 -> verEstadoActualSistema();
                    case 12 -> {
                        salirDelPrograma();
                        continuar = false;
                    }
                    case 13 -> verEstadoDeLaMemoria();
                    case 14 -> DesfragmentarMemoria();
                    default -> System.out.println("Opción no válida. Intente nuevamente.");
                }
            }catch (InputMismatchException e){
                System.out.println("Por favor, introduce un número válido.");
                scanner.nextLine();
            }
        }
    }

    private void DesfragmentarMemoria() {
        memoria.desfragmentar();
        System.out.println("Memoria desfragmentada.");
    }

    private void verEstadoDeLaMemoria() {
        System.out.println("===== Estado de la Memoria =====");

        // Mostrar la lista de localidades y qué proceso está usando cada localidad
        System.out.println("Localidades de memoria:");
        for (int i = 0; i < memoria.localidades.length; i++) {
            if (memoria.localidades[i] == null) {
                System.out.printf("Localidad %d: Hueco%n", i);
            } else {
                System.out.printf("Localidad %d: Proceso ID %d%n", i, memoria.localidades[i]);
            }
        }
        System.out.println("------------------");

        // Mostrar información de cada proceso y hueco en la cola
        for (Proceso nodo : colaDeProcesos) {
            if (nodo.esHueco()) {
                System.out.printf("Hueco - Comienza en: %d, Tamaño: %d%n", nodo.getId(), nodo.getMemoriaAsignada());
            } else {
                System.out.printf("ID Proceso %d,nombre: %s , Memoria Asignada: %d%n", nodo.getId(), nodo.getNombre(), nodo.getMemoriaAsignada());
                System.out.printf("Páginas asignadas: %s%n", nodo.getTablaDePaginas());
            }
        }
        System.out.println("------------------");

        // Mostrar la memoria total, disponible y usada
        System.out.printf("Memoria Total: %d localidades%n", memoria.localidades.length);
        System.out.printf("Memoria Disponible: %d localidades%n", memoria.getMemoriaDisponible());
        System.out.printf("Memoria Usada: %d localidades%n", memoria.localidades.length - memoria.getMemoriaDisponible());

        System.out.println("==============================");
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

        Proceso procesoActivo = colaDeProcesos.poll();
        memoriaUsada -= procesoActivo.getMemoriaAsignada();
        procesosEliminados.add(procesoActivo);
        memoria.liberarMemoria(procesoActivo);
        procesoActivo.setEsHueco(true);  // Establece el proceso como hueco.
        colaDeProcesos.add(procesoActivo);  // Añade el proceso (ahora hueco) de nuevo a la cola.

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

        Proceso nuevoProceso = new Proceso(nombre, false);
        List<Integer> paginasAsignadas = memoria.asignarMemoria(nuevoProceso);

        // Checar si hay memoria suficiente para el nuevo proceso
        if (!paginasAsignadas.isEmpty()) {
            // Actualizar la memoria disponible y la memoria usada
            int memoriaDisponible = memoria.getMemoriaDisponible();
            int memoriaRequerida = nuevoProceso.getMemoriaAsignada();
            memoriaUsada += memoriaRequerida;

            // Agregar el proceso a la cola de procesos
            colaDeProcesos.addLast(nuevoProceso);

            // Imprimir la información del proceso creado
            System.out.printf("Proceso \"%s\" creado con ID: %d%n", nombre, nuevoProceso.getId());
            System.out.printf("Instrucciones: %d%n", nuevoProceso.getInstruccionesTotales());
            System.out.printf("Memoria asignada: %d localidades%n", memoriaRequerida);
            System.out.printf("Memoria disponible: %d localidades%n", memoriaDisponible);
            System.out.printf("Páginas asignadas: %s%n", paginasAsignadas);
        } else {
            System.out.printf("No hay suficiente memoria disponible para el proceso %s con ID %d%nrequiere %d localidades de memoria y hay %d localidades disponibles%n", nombre, nuevoProceso.getId(), nuevoProceso.getMemoriaAsignada(), memoria.getMemoriaDisponible());
            System.out.println("Es necesario matar o ejecutar un proceso para liberar memoria.\n\n");
        }
    }

    public void verProcesoActivo() {
        if (colaDeProcesos.isEmpty()) {
            System.out.println("No hay procesos en la cola.");
            return;
        }

        Proceso procesoActivo = colaDeProcesos.peekFirst(); // Accedemos al primer proceso de la cola, sin eliminarlo.

        // Información básica del proceso
        System.out.println("Información del proceso activo:");
        System.out.println("Nombre del proceso: " + procesoActivo.getNombre());
        System.out.println("ID único: " + procesoActivo.getId());
        System.out.println("Instrucciones totales: " + procesoActivo.getInstruccionesTotales());
        System.out.println("Instrucciones ejecutadas: " + procesoActivo.getInstruccionesEjecutadas());

        // Información de la tabla de páginas
        List<Integer> tablaDePaginas = procesoActivo.getTablaDePaginas();
        System.out.println("Tabla de páginas del proceso:");
        if (tablaDePaginas.isEmpty()) {
            System.out.println("No hay páginas asignadas para este proceso.");
        } else {
            for (int i = 0; i < tablaDePaginas.size(); i++) {
                System.out.printf("Página %d: Localidad %d%n", i+1, tablaDePaginas.get(i));
            }
        }
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

            while (true) {
                System.out.println("¿Deseas continuar? (Sí/No)");

                String respuesta = scanner.nextLine().trim().toLowerCase();

                if (respuesta.equals("no")) {
                    System.out.println("Regresando al menú principal...");
                    return;
                } else if (respuesta.equals("sí") || respuesta.equals("si")) {
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    System.exit(0);
                } else {
                    System.out.println("Respuesta no reconocida. Por favor, ingresa 'Sí' o 'No'.");
                }
            }
        } else {
            System.out.println("Saliendo del programa. ¡Hasta luego!");
            System.exit(0);
        }
    }

}
