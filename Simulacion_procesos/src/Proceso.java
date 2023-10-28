import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Proceso {
    private static final int TAMANIO_PAGINA = 16;
    private List<Integer> tablaDePaginas; // Almacena el numero de paginas en memoria
    private final String nombre;
    private boolean esHueco;
    private final int id;
    private final int instruccionesTotales;
    private int instruccionesEjecutadas = 0;
    private final int memoriaAsignada;

    public Proceso(String nombre,boolean esHueco) {
        this.nombre = nombre;
        this.esHueco = esHueco;
        this.id = new Random().nextInt(10000);
        this.instruccionesTotales = new Random().nextInt(21) + 10;
        int[] posiblesMemorias = {64, 128, 256, 512};
        this.memoriaAsignada = posiblesMemorias[new Random().nextInt(4)];
        this.tablaDePaginas = new ArrayList<>(); // Inicialización de la tabla de páginas
    }
    public String getNombre() {
        return nombre;
    }
    public int getMemoriaAsignada() {
        return memoriaAsignada;
    }

    public boolean esHueco() {
        return esHueco;
    }

    public void setEsHueco(boolean b) {
        this.esHueco = b;

    }

    public int getId() {
        return id;
    }


    public int getInstruccionesTotales() {
        return instruccionesTotales;
    }
        public int getInstruccionesEjecutadas() {
        return instruccionesEjecutadas;
    }

    public void setInstruccionesEjecutadas(int instruccionesEjecutadas) {
        this.instruccionesEjecutadas = instruccionesEjecutadas;
    }
        // Nuevos métodos para manejar la tabla de páginas
    public List<Integer> getTablaDePaginas() {
        return tablaDePaginas;
    }
    public int getNumeroDePaginas(){
        return(int) Math.ceil((double) this.getMemoriaAsignada()/TAMANIO_PAGINA);
    }

    public void agregarPagina(int numeroDePagina){
        tablaDePaginas.add(numeroDePagina);
    }


    public void setTablaDePaginas(List<Integer> paginasAsignadas) {
        this.tablaDePaginas = paginasAsignadas;
    }
}

