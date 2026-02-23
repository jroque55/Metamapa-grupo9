package utn.ddsi.agregador.domain.agregador;
import org.junit.jupiter.api.Test;
import utn.ddsi.agregador.domain.coleccion.*;
import utn.ddsi.agregador.domain.hecho.Hecho;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ConsensoTests {

    /* =====================================================
       MÉTODO AUXILIAR
       ===================================================== */
    private void aplicarConsenso(
            Coleccion coleccion,
            List<HechoXColeccion> hechos,
            Map<Long, EvidenciaDeHecho> evidenciaPorHecho,
            int totalFuentes
    ) {
        for (HechoXColeccion hxc : hechos) {
            Long hechoId = hxc.getHecho().getId_hecho();

            EvidenciaDeHecho evidencia =
                    evidenciaPorHecho.getOrDefault(
                            hechoId,
                            EvidenciaDeHecho.vacia(hechoId)
                    );

            boolean consensuado =
                    coleccion.getAlgoritmoDeConsenso()
                            .aplicar(evidencia, totalFuentes);

            hxc.setConsensuado(consensuado);
        }
    }

    /* =====================================================
       DEFAULT
       ===================================================== */
    @Test
    void algoritmoDefault_consensuaSiempre() {
        AlgoritmoDeConsenso algoritmo = new ConsensoDefault();

        EvidenciaDeHecho evidencia = EvidenciaDeHecho.vacia(1L);

        assertTrue(algoritmo.aplicar(evidencia, 5));
    }

    /* =====================================================
       MULTIPLES MENCIONES
       ===================================================== */
    @Test
    void multiplesMenciones_dosFuentes_sinConflicto_consensua() {
        AlgoritmoDeConsenso algoritmo = new MencionesMultiples();

        EvidenciaDeHecho evidencia = new EvidenciaDeHecho(
                1L,
                Set.of(10L, 20L), // fuentes
                false
        );

        assertTrue(algoritmo.aplicar(evidencia, 3));
    }

    @Test
    void multiplesMenciones_unaFuente_noConsensua() {
        AlgoritmoDeConsenso algoritmo = new MencionesMultiples();

        EvidenciaDeHecho evidencia = new EvidenciaDeHecho(
                1L,
                Set.of(10L),
                false
        );

        assertFalse(algoritmo.aplicar(evidencia, 3));
    }

    @Test
    void multiplesMenciones_conConflicto_noConsensua() {
        AlgoritmoDeConsenso algoritmo = new MencionesMultiples();

        EvidenciaDeHecho evidencia = new EvidenciaDeHecho(
                1L,
                Set.of(10L, 20L),
                true
        );

        assertFalse(algoritmo.aplicar(evidencia, 3));
    }

    /* =====================================================
       MAYORIA SIMPLE
       ===================================================== */
    @Test
    void mayoriaSimple_mitadExacta_consensua() {
        AlgoritmoDeConsenso algoritmo = new MayoriaSimple();

        EvidenciaDeHecho evidencia = new EvidenciaDeHecho(
                1L,
                Set.of(10L, 20L), // 2 de 4
                false
        );

        assertTrue(algoritmo.aplicar(evidencia, 4));
    }

    @Test
    void mayoriaSimple_menosDeLaMitad_noConsensua() {
        AlgoritmoDeConsenso algoritmo = new MayoriaSimple();

        EvidenciaDeHecho evidencia = new EvidenciaDeHecho(
                1L,
                Set.of(10L), // 1 de 4
                false
        );

        assertFalse(algoritmo.aplicar(evidencia, 4));
    }

    /* =====================================================
       ABSOLUTA
       ===================================================== */
    @Test
    void absoluta_todasLasFuentes_consensua() {
        AlgoritmoDeConsenso algoritmo = new ConsensoAbsoluto();

        EvidenciaDeHecho evidencia = new EvidenciaDeHecho(
                1L,
                Set.of(10L, 20L, 30L),
                false
        );

        assertTrue(algoritmo.aplicar(evidencia, 3));
    }

    @Test
    void absoluta_faltaUnaFuente_noConsensua() {
        AlgoritmoDeConsenso algoritmo = new ConsensoAbsoluto();

        EvidenciaDeHecho evidencia = new EvidenciaDeHecho(
                1L,
                Set.of(10L, 20L),
                false
        );

        assertFalse(algoritmo.aplicar(evidencia, 3));
    }

    /* =====================================================
       CASO BORDE: EVIDENCIA VACIA
       ===================================================== */
    @Test
    void evidenciaVacia_noRompe_y_noConsensua() {
        AlgoritmoDeConsenso algoritmo = new MayoriaSimple();

        EvidenciaDeHecho evidencia = EvidenciaDeHecho.vacia(1L);

        assertFalse(algoritmo.aplicar(evidencia, 5));
    }

    /* =====================================================
       MULTIPLES MENCIONES + COLECCION
       ===================================================== */
    @Test
    void multiplesMenciones_variosHechos_mixto() {
        Coleccion coleccion = new Coleccion();
        coleccion.setAlgoritmoDeConsenso(new MencionesMultiples());

        int totalFuentes = 3;

        Hecho h1 = new Hecho(); h1.setId_hecho(1L);
        Hecho h2 = new Hecho(); h2.setId_hecho(2L);
        Hecho h3 = new Hecho(); h3.setId_hecho(3L);

        HechoXColeccion hx1 = new HechoXColeccion(h1, coleccion, false);
        HechoXColeccion hx2 = new HechoXColeccion(h2, coleccion, false);
        HechoXColeccion hx3 = new HechoXColeccion(h3, coleccion, false);

        Map<Long, EvidenciaDeHecho> evidencia = Map.of(
                1L, new EvidenciaDeHecho(1L, Set.of(10L, 20L), false), // ✔
                2L, new EvidenciaDeHecho(2L, Set.of(10L), false),     // ✖
                3L, new EvidenciaDeHecho(3L, Set.of(10L, 20L), true) // ✖ conflicto
        );

        aplicarConsenso(coleccion, List.of(hx1, hx2, hx3), evidencia, totalFuentes);

        assertTrue(hx1.getConsensuado());
        assertFalse(hx2.getConsensuado());
        assertFalse(hx3.getConsensuado());
    }

    /* =====================================================
       DEFAULT + COLECCION
       ===================================================== */
    @Test
    void default_consensua_todos_los_hechos() {
        Coleccion coleccion = new Coleccion();
        coleccion.setAlgoritmoDeConsenso(new ConsensoDefault());

        Hecho h1 = new Hecho(); h1.setId_hecho(1L);
        Hecho h2 = new Hecho(); h2.setId_hecho(2L);

        HechoXColeccion hx1 = new HechoXColeccion(h1, coleccion, false);
        HechoXColeccion hx2 = new HechoXColeccion(h2, coleccion, false);

        aplicarConsenso(
                coleccion,
                List.of(hx1, hx2),
                Collections.emptyMap(),
                5
        );

        assertTrue(hx1.getConsensuado());
        assertTrue(hx2.getConsensuado());
    }
}