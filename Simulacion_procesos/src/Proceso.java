import java.util.Random;

public class Proceso {
    private String nombre;
    private int id;
    private int instruccionesTotales;
    private int instruccionesEjecutadas = 0;
    private int memoriaAsignada;

    public Proceso(String nombre) {
        this.nombre = nombre;
        this.id = new Random().nextInt(10000);  // Un ID único basado en un número aleatorio.
        this.instruccionesTotales = new Random().nextInt(21) + 10; // Un número aleatorio entre 10-30.
        int[] posiblesMemorias = {64, 128, 256, 512};
        this.memoriaAsignada = posiblesMemorias[new Random().nextInt(4)];
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInstruccionesTotales() {
        return instruccionesTotales;
    }

    public void setInstruccionesTotales(int instruccionesTotales) {
        this.instruccionesTotales = instruccionesTotales;
    }

    public int getInstruccionesEjecutadas() {
        return instruccionesEjecutadas;
    }

    public void setInstruccionesEjecutadas(int instruccionesEjecutadas) {
        this.instruccionesEjecutadas = instruccionesEjecutadas;
    }

    public int getMemoriaAsignada() {
        return memoriaAsignada;
    }

    public void setMemoriaAsignada(int memoriaAsignada) {
        this.memoriaAsignada = memoriaAsignada;
    }
}

