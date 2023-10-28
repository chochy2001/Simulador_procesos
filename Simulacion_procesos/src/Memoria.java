import java.util.ArrayList;
import java.util.List;

public class Memoria {

    public int memoriaDisponible;
    public Integer[] localidades;  // Arreglo para representar las localidades de memoria
    private static final int TAMANIO_PAGINA = 16;

    public Memoria(int totalMemoria) {
        this.memoriaDisponible = totalMemoria;
        this.localidades = new Integer[totalMemoria];
    }

    // Intentar asignar memoria para un nuevo proceso
    public List<Integer> asignarMemoria(Proceso proceso) {
        int numeroDePaginas = proceso.getNumeroDePaginas();
        List<Integer> paginasAsignadas = new ArrayList<>();

        if (numeroDePaginas * TAMANIO_PAGINA <= memoriaDisponible) {
            for (int i = 0; i < numeroDePaginas; i++) {
                int paginaInicio = encontrarPaginaLibre();
                if (paginaInicio != -1) {
                    for (int j = 0; j < TAMANIO_PAGINA; j++) {
                        localidades[paginaInicio + j] = proceso.getId();
                    }
                    paginasAsignadas.add(paginaInicio);
                } else {
                    // En caso de que no haya suficientes páginas para el proceso completo
                    // Debemos deshacer las asignaciones anteriores (esto puede optimizarse más)
                    for (Integer pagina : paginasAsignadas) {
                        for (int j = 0; j < TAMANIO_PAGINA; j++) {
                            localidades[pagina + j] = null;
                        }
                    }
                    return new ArrayList<>();
                }
            }
            memoriaDisponible -= numeroDePaginas * TAMANIO_PAGINA;
        }
        proceso.setTablaDePaginas(paginasAsignadas);
        return paginasAsignadas;
    }

    public void desfragmentar() {
        Integer[] nuevaLocalidad = new Integer[localidades.length];
        int indiceNuevaLocalidad = 0;

        // Mover los procesos al inicio
        for (int i = 0; i < localidades.length; i++) {
            if (localidades[i] != null) {
                nuevaLocalidad[indiceNuevaLocalidad] = localidades[i];
                indiceNuevaLocalidad++;
            }
        }

        // Llenar las localidades restantes con null para representar espacios libres
        for (int i = indiceNuevaLocalidad; i < localidades.length; i++) {
            nuevaLocalidad[i] = null;
        }

        this.localidades = nuevaLocalidad;
        System.out.println("Desfragmentación completada. Mostrando el nuevo estado de la memoria:");
        mostrarEstadoDeLaMemoria();
    }
    public void mostrarEstadoDeLaMemoria() {
        for (int i = 0; i < localidades.length; i++) {
            if (localidades[i] == null) {
                System.out.printf("Localidad %d: Libre%n", i);
            } else {
                System.out.printf("Localidad %d: Proceso con ID %d%n", i, localidades[i]);
            }
        }
    }
    
    private int encontrarPaginaLibre() {
        for (int i = 0; i <= localidades.length - TAMANIO_PAGINA; i++) {
            boolean paginaLibre = true;
            for (int j = 0; j < TAMANIO_PAGINA; j++) {
                if (localidades[i + j] != null) {
                    paginaLibre = false;
                    break;
                }
            }
            if (paginaLibre) {
                return i;
            }
        }
        return -1;
    }

    public void liberarMemoria(Proceso proceso) {
        for (Integer paginaInicio : proceso.getTablaDePaginas()) {
            for (int j = 0; j < TAMANIO_PAGINA; j++) {
                localidades[paginaInicio + j] = null;
            }
            memoriaDisponible += TAMANIO_PAGINA;
        }
        proceso.setTablaDePaginas(new ArrayList<>());
    }

    public int getMemoriaDisponible() {
        return memoriaDisponible;
    }
}
