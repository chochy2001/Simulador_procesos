import java.util.LinkedList;
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
                // ... [Otras opciones]
                case 10:
                    salirDelPrograma();
                    break;
            }
        }
    }

    public void crearProceso() {
        // Implementación del método para crear un proceso...
    }

    public void verProcesoActivo() {
        // Implementación del método para ver el proceso activo...
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
