import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Simulador {
    private LinkedList<Proceso> colaDeProcesos = new LinkedList<>();
    private LinkedList<Proceso> colaDeES = new LinkedList<>();
    private LinkedList<Proceso> colaDeInterrupciones = new LinkedList<>();
    private LinkedList<Proceso> procesosEliminados = new LinkedList<>();
    private LinkedList<Proceso> procesosFinalizados = new LinkedList<>();
    private int memoriaTotal = 2048;
    private int memoriaUsada = 0;
    private Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        while (true) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Crear Proceso nuevo");
            System.out.println("2. Ver proceso activo");
            System.out.println("3. Ejecutar proceso actual");
            System.out.println("4. Pasar al proceso siguiente");
            // ... [Otras opciones]
            System.out.println("10. Salir del programa");

            int seleccion = scanner.nextInt();

            switch (seleccion) {
                case 1:
                    crearProceso();
                    break;
                case 2:
                    verProcesoActivo();
                    break;
                case 3: 
                    ejecutarProcesoActual();
                case 4:
                    pasarAlProcesoSiguiente();
                // ... [Otras opciones]
                case 10:
                    salirDelPrograma();
                    break;
            }
        }
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

        int instruccionesARealizar = 5;
        if (procesoActivo.getInstruccionesTotales() - procesoActivo.getInstruccionesEjecutadas() < 5) {
            // Si hay menos de 5 instrucciones por ejecutar, ajustar instruccionesARealizar.
            instruccionesARealizar = procesoActivo.getInstruccionesTotales() - procesoActivo.getInstruccionesEjecutadas();
        }

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
        String nombre = scanner.next();

        Proceso nuevoProceso = new Proceso(nombre);

        // Checar si hay memoria suficiente para el nuevo proceso.
        if ((memoriaUsada + nuevoProceso.getMemoriaAsignada()) <= memoriaTotal) {
            colaDeProcesos.addLast(nuevoProceso);  // Agregar el proceso a la cola de procesos.
            memoriaUsada += nuevoProceso.getMemoriaAsignada();  // Incrementar la memoria usada.

            System.out.println("Proceso " + nombre + " creado con ID: " + nuevoProceso.getId());
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

        System.out.println("Información del proceso activo:");
        System.out.println("Nombre del proceso: " + procesoActivo.getNombre());
        System.out.println("ID único: " + procesoActivo.getId());
        System.out.println("Instrucciones totales: " + procesoActivo.getInstruccionesTotales());
        System.out.println("Instrucciones ejecutadas: " + procesoActivo.getInstruccionesEjecutadas());
    }

    

    // Métodos para manejar las otras acciones del simulador...

    public void salirDelPrograma() {
        // Implementación del método para salir del programa...
    }

    public LinkedList<Proceso> getColaDeProcesos() {
        return colaDeProcesos;
    }

    public void setColaDeProcesos(LinkedList<Proceso> colaDeProcesos) {
        this.colaDeProcesos = colaDeProcesos;
    }

    public LinkedList<Proceso> getColaDeES() {
        return colaDeES;
    }

    public void setColaDeES(LinkedList<Proceso> colaDeES) {
        this.colaDeES = colaDeES;
    }

    public LinkedList<Proceso> getColaDeInterrupciones() {
        return colaDeInterrupciones;
    }

    public void setColaDeInterrupciones(LinkedList<Proceso> colaDeInterrupciones) {
        this.colaDeInterrupciones = colaDeInterrupciones;
    }

    public LinkedList<Proceso> getProcesosEliminados() {
        return procesosEliminados;
    }

    public void setProcesosEliminados(LinkedList<Proceso> procesosEliminados) {
        this.procesosEliminados = procesosEliminados;
    }

    public LinkedList<Proceso> getProcesosFinalizados() {
        return procesosFinalizados;
    }

    public void setProcesosFinalizados(LinkedList<Proceso> procesosFinalizados) {
        this.procesosFinalizados = procesosFinalizados;
    }

    public int getMemoriaTotal() {
        return memoriaTotal;
    }

    public void setMemoriaTotal(int memoriaTotal) {
        this.memoriaTotal = memoriaTotal;
    }

    public int getMemoriaUsada() {
        return memoriaUsada;
    }

    public void setMemoriaUsada(int memoriaUsada) {
        this.memoriaUsada = memoriaUsada;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
}
