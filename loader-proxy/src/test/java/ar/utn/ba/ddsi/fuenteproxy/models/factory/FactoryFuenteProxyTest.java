package ar.utn.ba.ddsi.fuenteproxy.models.factory;

import ar.utn.ba.ddsi.fuenteproxy.models.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryFuenteProxyTest {

    private FactoryFuenteProxy factory;

    @BeforeEach
    void setUp() {
        factory = FactoryFuenteProxy.getInstance();
    }

    @Test
    void getInstance_retornaMismaInstancia() {
        // Act
        FactoryFuenteProxy instancia1 = FactoryFuenteProxy.getInstance();
        FactoryFuenteProxy instancia2 = FactoryFuenteProxy.getInstance();

        // Assert
        assertNotNull(instancia1);
        assertSame(instancia1, instancia2);
    }

    @Test
    void createFuenteProxy_conFuenteMetamapa_retornaFuenteMetamapa() {
        // Arrange
        Fuente fuente = new Fuente(1L, "Metamapa", "http://localhost:8090/export", EnumTipoFuente.METAMAPA);

        // Act
        FuenteProxy resultado = factory.createFuenteProxy(fuente);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado instanceof FuenteMetamapa);
        assertEquals("http://localhost:8090/export", resultado.getUrl());
    }

    @Test
    void createFuenteProxy_conFuenteDemo_retornaFuenteDemo() {
        // Arrange
        Fuente fuente = new Fuente(2L, "Demo", "http://demo.com", EnumTipoFuente.DEMO);

        // Act
        FuenteProxy resultado = factory.createFuenteProxy(fuente);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado instanceof FuenteDemo);
        assertEquals("http://demo.com", resultado.getUrl());
    }

    @Test
    void createFuenteProxy_conFuenteEstatica_lanzaExcepcion() {
        // Arrange
        Fuente fuente = new Fuente(3L, "Estatica", "http://estatica.com", EnumTipoFuente.ESTATICA);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> factory.createFuenteProxy(fuente));
    }

    @Test
    void createFuenteProxy_conFuenteNula_lanzaExcepcion() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> factory.createFuenteProxy(null));
    }

    @Test
    void createFuenteProxy_conTipoFuenteNulo_lanzaExcepcion() {
        // Arrange
        Fuente fuente = new Fuente();
        fuente.setNombre("Test");
        fuente.setUrl("http://test.com");
        fuente.setTipoFuente(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> factory.createFuenteProxy(fuente));
    }
}
